package net.awyvrix.structureframework.core;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class StructureCapture {
    public static StructureTemplate capture(Level level, BlockPos a, BlockPos b) {
        return capture(level, a, b, null);
    }

    public static StructureTemplate capture(Level level, BlockPos a, BlockPos b, @Nullable BlockPos customAnchor) {
        int minX = Math.min(a.getX(), b.getX());
        int minY = Math.min(a.getY(), b.getY());
        int minZ = Math.min(a.getZ(), b.getZ());

        int maxX = Math.max(a.getX(), b.getX());
        int maxY = Math.max(a.getY(), b.getY());
        int maxZ = Math.max(a.getZ(), b.getZ());

        int sizeX = maxX - minX + 1;
        int sizeY = maxY - minY + 1;
        int sizeZ = maxZ - minZ + 1;

        BlockPaletteBuilder paletteBuilder = new BlockPaletteBuilder();
        List<BlockEntry> blocks = new ArrayList<>();

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                for (int z = 0; z < sizeZ; z++) {
                    BlockPos worldPos = new BlockPos(minX + x, minY + y, minZ + z);
                    BlockState state = level.getBlockState(worldPos);

                    if (state.isAir()) continue;
                    short pid = paletteBuilder.getOrAdd(state);

                    blocks.add(new BlockEntry((short)x, (short)y, (short)z, pid));
                }
            }
        }

        BlockPalette palette = paletteBuilder.build();
        StructureMetadata metadata;

        if (customAnchor != null) {
            short anchorX = (short)(customAnchor.getX() - minX);
            short anchorY = (short)(customAnchor.getY() - minY);
            short anchorZ = (short)(customAnchor.getZ() - minZ);

            metadata = new StructureMetadata(true, anchorX, anchorY, anchorZ);
        } else {
            metadata = new StructureMetadata(false, (short)0, (short)0, (short)0);
        }

        return new StructureTemplate(
                sizeX,
                sizeY,
                sizeZ,
                palette,
                blocks.toArray(new BlockEntry[0]),
                metadata
        );
    }
}