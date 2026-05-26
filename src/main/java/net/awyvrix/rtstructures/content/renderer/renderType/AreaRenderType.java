package net.awyvrix.rtstructures.content.renderer.renderType;

import net.awyvrix.rtstructures.content.renderer.BoxColor;
import net.awyvrix.rtstructures.content.tools.linkTool.LinkToolState;
import net.awyvrix.rtstructures.content.tools.structureTool.StructureToolState;
import net.minecraft.core.BlockPos;

import java.util.function.Supplier;

public enum AreaRenderType {
    ST_SELECTION(() -> StructureToolState.pos1, () -> StructureToolState.pos2, new BoxColor(0f, 1f, 0f), 0.001f),
    LT_SELECTION(() -> LinkToolState.pos1, () -> LinkToolState.pos2, new BoxColor(1f, 1f, 0f), 0.001f);

    private final Supplier<BlockPos> pos1Supplier;
    private final Supplier<BlockPos> pos2Supplier;
    private final BoxColor color;
    private final double inflate;

    AreaRenderType(Supplier<BlockPos> pos1Supplier, Supplier<BlockPos> pos2Supplier, BoxColor color, double inflate) {
        this.pos1Supplier = pos1Supplier;
        this.pos2Supplier = pos2Supplier;
        this.color = color;
        this.inflate = inflate;
    }

    public BlockPos getPos1() {
        return pos1Supplier.get();
    }

    public BlockPos getPos2() {
        return pos2Supplier.get();
    }

    public BoxColor getColor() {
        return color;
    }

    public double getInflate() {
        return inflate;
    }
}