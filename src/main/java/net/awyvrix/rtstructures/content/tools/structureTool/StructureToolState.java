package net.awyvrix.rtstructures.content.tools.structureTool;

import net.awyvrix.rtstructures.content.tools.ToolState;
import net.minecraft.core.BlockPos;

public final class StructureToolState extends ToolState {
    public static final StructureToolState INSTANCE = new StructureToolState();

    public static BlockPos pos1;
    public static BlockPos pos2;
    public static BlockPos anchor;
    public static BlockPos placeAnchor;

    public static BlockPos loadPos;
    public static int sizeX;
    public static int sizeY;
    public static int sizeZ;


    private StructureToolState() {}
}