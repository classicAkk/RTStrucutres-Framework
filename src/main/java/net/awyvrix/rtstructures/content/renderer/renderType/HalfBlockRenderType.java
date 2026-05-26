package net.awyvrix.rtstructures.content.renderer.renderType;

import net.awyvrix.rtstructures.content.renderer.BoxColor;
import net.awyvrix.rtstructures.content.tools.linkTool.LinkToolState;
import net.awyvrix.rtstructures.content.tools.structureTool.StructureToolState;
import net.minecraft.core.BlockPos;

import java.util.function.Supplier;

public enum HalfBlockRenderType {
    ST_POS1(() -> StructureToolState.pos1, new BoxColor(0.2f, 0.2f, 1f), 0.001f),
    ST_POS2(() -> StructureToolState.pos2, new BoxColor(1f, 0.2f, 0.2f), 0.001f),
    ST_ANCHOR(() -> StructureToolState.anchor, new BoxColor(0f, 1f, 1f), 0.001f),
    ST_PLACE_ANCHOR(() -> StructureToolState.placeAnchor, new BoxColor(1f, 0f, 1f), 0.001f),

    LT_POS1(() -> LinkToolState.pos1, new BoxColor(0.2f, 0.2f, 1f), 0.001f),
    LT_POS2(() -> LinkToolState.pos2, new BoxColor(1f, 0.2f, 0.2f), 0.001f),
    LT_ANCHOR(() -> LinkToolState.anchor, new BoxColor(0f, 1f, 1f), 0.001f),
    LT_PLACE_ANCHOR(() -> LinkToolState.placeAnchor, new BoxColor(1f, 0f, 1f), 0.001);

    private final Supplier<BlockPos> posSupplier;
    private final BoxColor color;
    private final double inflate;

    HalfBlockRenderType(Supplier<BlockPos> posSupplier, BoxColor color, double inflate) {
        this.posSupplier = posSupplier;
        this.color = color;
        this.inflate = inflate;
    }

    public BlockPos getPos() {
        return posSupplier.get();
    }

    public BoxColor getColor() {
        return color;
    }

    public double getInflate() {
        return inflate;
    }
}