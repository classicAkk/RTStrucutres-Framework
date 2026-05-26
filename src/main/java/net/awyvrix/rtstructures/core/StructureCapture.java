package net.awyvrix.rtstructures.core;

import net.awyvrix.rtstructures.content.tools.linkTool.SocketPoint;
import net.awyvrix.rtstructures.content.tools.linkTool.ContactPoint;
import net.awyvrix.rtstructures.content.tools.linkTool.StructureNode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class StructureCapture {
    public static StructureTemplate capture(Level level, BlockPos a, BlockPos b) {
        return capture(level, a, b, null, null, null, null);
    }

    public static StructureTemplate capture(Level level,
                                            BlockPos a, BlockPos b, @Nullable BlockPos customAnchor,
                                            List<StructureNode> nodes, List<SocketPoint> connectPoints, List<ContactPoint> contactPoints
                                            ) {
        boolean isLinkable = nodes != null && connectPoints != null && contactPoints != null;

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

            metadata = new StructureMetadata(true, isLinkable, anchorX, anchorY, anchorZ, nodes, connectPoints, contactPoints);
        } else {
            metadata = new StructureMetadata(false, isLinkable, (short)0, (short)0, (short)0, nodes, connectPoints, contactPoints);
        }

        return new StructureTemplate(
                sizeX, sizeY, sizeZ,
                palette, blocks.toArray(new BlockEntry[0]), metadata
        );
    }
}