package net.awyvrix.structureframework.modders;

import net.awyvrix.structureframework.content.structureTool.PlacementAnchor;
import net.awyvrix.structureframework.core.StructureTemplate;
import net.minecraft.core.BlockPos;

public final class AnchorMath {
    public static BlockPos computeMinCorner(StructureTemplate template, BlockPos anchorPos, PlacementAnchor mode) {
        return switch (mode) {
            case CORNER -> anchorPos;
            case CENTER -> new BlockPos(
                    anchorPos.getX() - (template.sizeX / 2),
                    anchorPos.getY() - (template.sizeY / 2),
                    anchorPos.getZ() - (template.sizeZ / 2)
            );
            case CUSTOM -> new BlockPos(
                    anchorPos.getX() - template.metadata.anchorX,
                    anchorPos.getY() - template.metadata.anchorY,
                    anchorPos.getZ() - template.metadata.anchorZ
            );
        };
    }
}