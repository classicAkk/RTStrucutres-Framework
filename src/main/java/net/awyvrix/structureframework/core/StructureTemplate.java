package net.awyvrix.structureframework.core;

public final class StructureTemplate {
    public final int sizeX, sizeY, sizeZ;

    public final BlockPalette palette;
    public final BlockEntry[] blocks;
    public final StructureMetadata metadata;

    public StructureTemplate(
            int sizeX,
            int sizeY,
            int sizeZ,
            BlockPalette palette,
            BlockEntry[] blocks,
            StructureMetadata metadata
    ) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.palette = palette;
        this.blocks = blocks;
        this.metadata = metadata;
    }
}