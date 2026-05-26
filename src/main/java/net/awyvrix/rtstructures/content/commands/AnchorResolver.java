package net.awyvrix.rtstructures.content.commands;

import net.awyvrix.rtstructures.content.tools.structureTool.PlacementAnchor;
import net.awyvrix.rtstructures.content.tools.structureTool.StructureToolState;
import net.awyvrix.rtstructures.core.StructureMetadata;
import net.awyvrix.rtstructures.core.StructureTemplate;
import net.minecraft.core.BlockPos;

public class AnchorResolver {
    public static BlockPos getAnchorOffset(StructureTemplate template, PlacementAnchor anchor) {
        int x = StructureToolState.placeAnchor.getX();
        int y = StructureToolState.placeAnchor.getY();
        int z = StructureToolState.placeAnchor.getZ();

        return switch (anchor) {
            case CORNER -> new BlockPos(x, y, z);
            case CENTER -> new BlockPos(
                            x - template.sizeX / 2,
                            y - template.sizeY / 2,
                            z - template.sizeZ / 2
            );

            case CUSTOM -> {
                StructureMetadata meta = template.metadata;
                if (!meta.hasCustomAnchor) {
                    throw new IllegalStateException("Structure has no custom anchor");
                }

                yield new BlockPos(
                        x - meta.anchorX,
                        y - meta.anchorY,
                        z - meta.anchorZ
                );
            }
        };
    }
}