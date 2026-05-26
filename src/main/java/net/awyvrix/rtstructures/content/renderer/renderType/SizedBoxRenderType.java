package net.awyvrix.rtstructures.content.renderer.renderType;

import net.awyvrix.rtstructures.content.renderer.BoxColor;
import net.awyvrix.rtstructures.content.tools.structureTool.StructureToolState;
import net.minecraft.core.BlockPos;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

public enum SizedBoxRenderType {
    LOAD_PREVIEW(
            () -> StructureToolState.loadPos,
            () -> StructureToolState.sizeX,
            () -> StructureToolState.sizeY,
            () -> StructureToolState.sizeZ,
            new BoxColor(1f, 0.2f, 0.2f),
            0.003
    );

    private final Supplier<BlockPos> posSupplier;

    private final IntSupplier sizeXSupplier;
    private final IntSupplier sizeYSupplier;
    private final IntSupplier sizeZSupplier;
    private final BoxColor color;
    private final double inflate;

    SizedBoxRenderType(
            Supplier<BlockPos> posSupplier,
            IntSupplier sizeXSupplier,
            IntSupplier sizeYSupplier,
            IntSupplier sizeZSupplier,
            BoxColor color,
            double inflate
    ) {
        this.posSupplier = posSupplier;
        this.sizeXSupplier = sizeXSupplier;
        this.sizeYSupplier = sizeYSupplier;
        this.sizeZSupplier = sizeZSupplier;

        this.color = color;
        this.inflate = inflate;
    }

    public BlockPos getPos() {
        return posSupplier.get();
    }

    public int getSizeX() {
        return sizeXSupplier.getAsInt();
    }

    public int getSizeY() {
        return sizeYSupplier.getAsInt();
    }

    public int getSizeZ() {
        return sizeZSupplier.getAsInt();
    }

    public BoxColor getColor() {
        return color;
    }

    public double getInflate() {
        return inflate;
    }
}