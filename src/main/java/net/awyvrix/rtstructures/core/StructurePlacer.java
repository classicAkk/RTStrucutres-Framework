package net.awyvrix.rtstructures.core;

import net.awyvrix.rtstructures.content.structureTool.PlacementAnchor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class StructurePlacer {
    public static void place(Level level, BlockPos targetPos, StructureTemplate template, PlacementAnchor anchor) {
        int originX;
        int originY;
        int originZ;

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
        BlockPalette palette = template.palette;

        for (BlockEntry block : template.blocks) {
            BlockState state = palette.get(block.paletteId());

            BlockPos pos = new BlockPos(
                    originX + block.x(),
                    originY + block.y(),
                    originZ + block.z()
            );

            level.setBlock(pos, state, 3);
        }
    }
}