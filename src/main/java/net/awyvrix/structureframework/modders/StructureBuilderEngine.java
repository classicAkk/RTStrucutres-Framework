package net.awyvrix.structureframework.modders;

import net.awyvrix.structureframework.content.structureTool.PlacementAnchor;
import net.awyvrix.structureframework.core.BlockEntry;
import net.awyvrix.structureframework.core.BlockPalette;
import net.awyvrix.structureframework.core.StructureMetadata;
import net.awyvrix.structureframework.core.StructureTemplate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public final class StructureBuilderEngine {
    private final StructureInstance instance;
    private final ServerLevel level;

    private final BuildType type;
    private final float speed;
    private final Deque<Integer> queue = new ArrayDeque<>();

    private boolean initialized = false;

    public StructureBuilderEngine(StructureInstance instance, ServerLevel level, BuildType type, float speed) {
        this.instance = instance;
        this.level = level;
        this.type = type;
        this.speed = speed;
    }

    private void init() {
        if (initialized) return;

        switch (type) {
            case FAST, FAST_SAFE -> {
                StructureTemplate template = instance.getStructure();
                List<Integer> indices = new ArrayList<>();

                for (int i = 0; i < template.blocks.length; i++) {
                    indices.add(i);
                }

                indices.sort((a, b) -> {
                    BlockEntry ba = template.blocks[a];
                    BlockEntry bb = template.blocks[b];
                    int dy = Integer.compare(ba.y(), bb.y());

                    if (dy != 0) return dy;
                    int dx = Integer.compare(ba.x(), bb.x());

                    if (dx != 0) return dx;
                    return Integer.compare(ba.z(), bb.z());
                });

                queue.addAll(indices);
            }

            case CONNECT -> buildConnectInit();
            case SEPARATED -> buildSeparatedInit();
        }

        instance.startBuild(BuildState.BUILDING);
        initialized = true;
    }

    public void tick() {
        init();

        if (instance.isCompleted()) return;
        StructureTemplate template = instance.getStructure();
        BlockPalette palette = template.palette;

        int originX;
        int originY;
        int originZ;

        PlacementAnchor anchor = instance.getAnchorMode();
        BlockPos targetPos = instance.getOrigin();

        switch (anchor) {
            case CORNER -> {
                originX = targetPos.getX();
                originY = targetPos.getY();
                originZ = targetPos.getZ();
            }

            case CENTER -> {
                originX = targetPos.getX() - (template.sizeX / 2);
                originY = targetPos.getY() - (template.sizeY / 2);
                originZ = targetPos.getZ() - (template.sizeZ / 2);
            }

            case CUSTOM -> {
                StructureMetadata meta = template.metadata;

                if (!meta.hasCustomAnchor) {
                    throw new IllegalStateException("Structure has no custom anchor");
                }

                originX = targetPos.getX() - meta.anchorX;
                originY = targetPos.getY() - meta.anchorY;
                originZ = targetPos.getZ() - meta.anchorZ;
            }

            default -> throw new IllegalStateException();
        }

        // Speed Control
        int steps = Math.max(1, (int) speed);

        for (int i = 0; i < steps; i++) {
            if (queue.isEmpty()) {
                instance.markBlockBuilt(instance.getStructure().blocks.length);
                return;
            }

            int index = queue.poll();
            BlockEntry block = template.blocks[index];
            BlockState state = palette.get(block.paletteId());
            BlockPos pos = new BlockPos(originX + block.x(), originY + block.y(), originZ + block.z());
            tryPlace(pos, state, index);
        }
    }

    private void tryPlace(BlockPos worldPos, BlockState target, int index) {
        BlockState current = level.getBlockState(worldPos);
        switch (type) {
            case FAST -> {
                place(worldPos, target);
            }
            case FAST_SAFE -> {
                if (current.isAir()) {
                    place(worldPos, target);
                }
            }
            case CONNECT -> {
                if (isConnected(index)) {
                    place(worldPos, target);
                } else {
                    queue.addLast(index);
                    return;
                }
            }
            case SEPARATED -> {
                place(worldPos, target);
            }
        }

        instance.markBlockBuilt(index);
    }

    private void buildConnectInit() {
        boolean[] visited = new boolean[instance.getStructure().blocks.length];
        int start = findAnchorIndex();
        queue.add(start);
        visited[start] = true;

        while (!queue.isEmpty()) {
            int i = queue.poll();
            addNeighbors(i, visited);
        }
    }

    private void buildSeparatedInit() {
        int start = findAnchorIndex();
        queue.add(start);
    }

    private boolean isConnected(int index) {
        StructureTemplate t = instance.getStructure();
        BlockEntry b = t.blocks[index];
        BlockPos pos = toWorldPos(instance, b);

        return hasAdjacentBuilt(pos);
    }

    private boolean hasAdjacentBuilt(BlockPos pos) {
        return isBuiltAt(pos.offset(1, 0, 0))
                || isBuiltAt(pos.offset(-1, 0, 0))
                || isBuiltAt(pos.offset(0, 1, 0))
                || isBuiltAt(pos.offset(0, -1, 0))
                || isBuiltAt(pos.offset(0, 0, 1))
                || isBuiltAt(pos.offset(0, 0, -1));
    }

    private void place(BlockPos pos, BlockState state) {
        level.setBlock(pos, state, 3);
    }

    private boolean isBuiltAt(BlockPos pos) {
        return level.getBlockState(pos).isAir();
    }

    private BlockPos toWorldPos(
            StructureInstance inst,
            BlockEntry b
    ) {
        return new BlockPos(
                inst.getOrigin().getX() + b.x(),
                inst.getOrigin().getY() + b.y(),
                inst.getOrigin().getZ() + b.z()
        );
    }

    private int findAnchorIndex() {
        return 0;
    }

    private void addNeighbors(int i, boolean[] visited) {
        // Placeholder for BFS expansion
    }
}