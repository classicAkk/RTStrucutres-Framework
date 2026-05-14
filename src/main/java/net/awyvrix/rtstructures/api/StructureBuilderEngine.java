package net.awyvrix.rtstructures.api;

import net.awyvrix.rtstructures.core.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public final class StructureBuilderEngine {
    private final StructureInstance instance;
    private final ServerLevel level;

    private final BuildType type;
    private final float speed;
    private final Deque<Integer> queue = new ArrayDeque<>();

    private boolean initialized = false;
    private final Deque<Integer> growthFront = new ArrayDeque<>();
    private boolean diagonalMode;

    public StructureBuilderEngine(StructureInstance instance, ServerLevel level, BuildType type, float speed) {
        this.instance = instance;
        this.level = level;
        this.type = type;
        this.speed = speed;
    }

    private void init() {
        if (initialized) return;

        initialized = true;
        switch (type) {
            case INSTANT -> {}

            case FAST, FAST_SAFE -> {
                StructureTemplate template = instance.getStructure();

                List<Integer> indices = new ArrayList<>();
                for (int i = 0; i < template.blocks.length; i++) {
                    indices.add(i);
                }

                // Horizontal build
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

            case SEPARATED -> buildSeparatedInit(false);

            case SEPARATED_DIAGONAL -> buildSeparatedInit(true);

            case DEMOLISH -> {
                StructureTemplate template = instance.getStructure();
                List<Integer> indices = new ArrayList<>();

                for (int i = 0; i < template.blocks.length; i++) {
                    indices.add(i);
                }
                indices.sort((a, b) -> {
                    BlockEntry ba = template.blocks[a];
                    BlockEntry bb = template.blocks[b];

                    return Integer.compare(bb.y(), ba.y());
                });
                queue.addAll(indices);
            }
        }
    }

    public void tick() {
        init();

        if (type != BuildType.DEMOLISH && instance.isCompletedStructure()) {
            instance.complete();
            return;
        }
        if (type == BuildType.INSTANT) {
            buildInstant();
            return;
        }
        int steps = Math.max(1, (int) speed);

        if (Objects.requireNonNull(type) == BuildType.SEPARATED || Objects.requireNonNull(type) == BuildType.SEPARATED_DIAGONAL) {
            separatedTick(steps);
        } else {
            normalTick(steps);
        }
    }

    private void separatedTick(int steps) {
        int processed = 0;

        while (!growthFront.isEmpty() && processed < steps) {
            int index = growthFront.removeFirst();

            if (tryGrow(index)) {
                spreadGrowth(index);
            } else {
                growthFront.addLast(index);
            }
            processed++;
        }
    }

    private void normalTick(int steps) {
        for (int i = 0; i < steps; i++) {
            if (queue.isEmpty()) return;
            if (type == BuildType.DEMOLISH) {
                instance.demolish();
                return;
            }

            int index = queue.pollFirst();
            tryPlace(index);
        }
    }

    private void tryPlace(int index) {
        StructureTemplate template = instance.getStructure();
        BlockEntry block = template.blocks[index];
        BlockPos worldPos = instance.toWorldPos(block);
        BlockState current = level.getBlockState(worldPos);
        BlockState target = template.palette.get(block.paletteId());

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
                }
            }

            case DEMOLISH -> {
                if (instance.removed.get(index)) return;
                StructureTemplate t = instance.getStructure();
                BlockPos expected = instance.toWorldPos(t.blocks[index]);

                if (!current.isAir() && isSameBlock(current, t.palette.get(t.blocks[index].paletteId()))) {
                    level.setBlock(expected, Blocks.AIR.defaultBlockState(), 3);
                    markBlockRemoved(index);
                }
            }
        }
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

    private void buildSeparatedInit(boolean diagonal) {
        this.diagonalMode = diagonal;

        int start = findAnchorIndex();
        growthFront.clear();
        growthFront.add(start);
    }

    private void buildInstant() {
        StructurePlacer.place(
                level,
                instance.getOrigin(),
                instance.getStructure(),
                instance.getAnchorMode()
        );
    }

    private boolean isConnected(int index) {
        StructureTemplate template = instance.getStructure();
        BlockEntry block = template.blocks[index];
        BlockPos pos = instance.toWorldPos(block);

        return hasSolidNeighbor(pos);
    }

    private boolean isSameBlock(BlockState a, BlockState b) {
        return a.getBlock() == b.getBlock();
    }

    private boolean hasSolidNeighbor(BlockPos pos) {
        return isSolid(pos.north())
                || isSolid(pos.south())
                || isSolid(pos.east())
                || isSolid(pos.west())
                || isSolid(pos.above())
                || isSolid(pos.below());
    }

    private boolean tryGrow(int index) {
        StructureTemplate template = instance.getStructure();
        BlockEntry block = template.blocks[index];

        BlockPos pos = instance.toWorldPos(block);
        BlockState target = template.palette.get(block.paletteId());

        if (!level.getBlockState(pos).isAir()) return false;
        place(pos, target);
        spreadGrowth(index);

        if (hasGrowthNeighbor(pos)) {
            place(pos, target);
            spreadGrowth(index);
            return true;
        }
        return false;
    }

    private boolean hasGrowthNeighbor(BlockPos pos) {
        return isBuilt(pos.north())
                || isBuilt(pos.south())
                || isBuilt(pos.east())
                || isBuilt(pos.west())
                || isBuilt(pos.above())
                || isBuilt(pos.below());
    }

    private boolean isBuilt(BlockPos pos) {
        return !level.getBlockState(pos).isAir();
    }

    private void spreadGrowth(int index) {
        for (int n : getNeighbors(index, diagonalMode)) {
            if (!growthFront.contains(n)) {
                growthFront.add(n);
            }
        }
    }

    private boolean is6Neighbor(BlockEntry a, BlockEntry b) {
        int dx = Math.abs(a.x() - b.x());
        int dy = Math.abs(a.y() - b.y());
        int dz = Math.abs(a.z() - b.z());

        return dx + dy + dz == 1;
    }

    private boolean is26Neighbor(BlockEntry a, BlockEntry b) {
        int dx = Math.abs(a.x() - b.x());
        int dy = Math.abs(a.y() - b.y());
        int dz = Math.abs(a.z() - b.z());

        return dx <= 1 && dy <= 1 && dz <= 1 && (dx + dy + dz > 0);
    }

    private List<Integer> getNeighbors(int index, boolean diagonal) {
        StructureTemplate t = instance.getStructure();
        BlockEntry b = t.blocks[index];

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < t.blocks.length; i++) {
            if (i == index) continue;
            BlockEntry other = t.blocks[i];
            boolean ok = diagonal ? is26Neighbor(b, other) : is6Neighbor(b, other);

            if (ok) {
                result.add(i);
            }
        }

        return result;
    }

    private boolean isSolid(BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.isCollisionShapeFullBlock(level, pos) || !state.isAir();
    }

    private void place(BlockPos pos, BlockState state) {
        level.setBlock(pos, state, 3);
    }

    private boolean isBuiltAt(BlockPos pos, BlockState expected) {
        return level.getBlockState(pos).equals(expected);
    }

    private BlockPos toWorldPos(StructureInstance instance, BlockEntry block) {
        return instance.toWorldPos(block);
    }

    public void onComplete() {
        initialized = false;
    }

    private int findAnchorIndex() {
        StructureTemplate t = instance.getStructure();
        BlockPos anchorLocal = new BlockPos(
                t.metadata.anchorX,
                t.metadata.anchorY,
                t.metadata.anchorZ
        );

        for (int i = 0; i < t.blocks.length; i++) {
            BlockEntry b = t.blocks[i];
            if (b.x() == anchorLocal.getX() && b.y() == anchorLocal.getY() && b.z() == anchorLocal.getZ()) {
                return i;
            }
        }

        return 0;
    }

    private void addNeighbors(int i, boolean[] visited) {
        // Placeholder for BFS expansion
    }

    public void markBlockRemoved(int index) {
        instance.removed.set(index);
    }
}