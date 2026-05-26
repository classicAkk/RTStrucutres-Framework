package net.awyvrix.rtstructures.content.renderer.renderType;

import net.awyvrix.rtstructures.content.renderer.BoxColor;
import net.awyvrix.rtstructures.content.tools.linkTool.LinkToolState;
import net.awyvrix.rtstructures.content.tools.linkTool.StructureNode;
import net.awyvrix.rtstructures.content.tools.structureTool.StructureToolState;
import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.function.Supplier;

public enum HalfBlockMapRenderType {
    NODE_POS(() -> LinkToolState.pre_baked_nodes, new BoxColor(0.2f, 0.2f, 1f), 0.001f);

    private final Supplier<HashMap<BlockPos, StructureNode>> posSupplier;
    private final BoxColor color;
    private final double inflate;

    HalfBlockMapRenderType(Supplier<HashMap<BlockPos, StructureNode>> posSupplier, BoxColor color, double inflate) {
        this.posSupplier = posSupplier;
        this.color = color;
        this.inflate = inflate;
    }

    public HashMap<BlockPos, StructureNode> getPos() {
        return posSupplier.get();
    }

    public BoxColor getColor() {
        return color;
    }

    public double getInflate() {
        return inflate;
    }
}