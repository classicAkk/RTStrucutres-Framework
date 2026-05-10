package net.awyvrix.structureframework.core;

public final class StructureMetadata {
    public final boolean hasCustomAnchor;

    public final short anchorX;
    public final short anchorY;
    public final short anchorZ;

    public StructureMetadata(
            boolean hasCustomAnchor,
            short anchorX,
            short anchorY,
            short anchorZ
    ) {
        this.hasCustomAnchor = hasCustomAnchor;

        this.anchorX = anchorX;
        this.anchorY = anchorY;
        this.anchorZ = anchorZ;
    }
}