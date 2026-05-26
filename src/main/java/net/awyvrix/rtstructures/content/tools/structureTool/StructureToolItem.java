package net.awyvrix.rtstructures.content.tools.structureTool;

import net.awyvrix.rtstructures.content.RTStructuresKeyBinds;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;

import java.util.List;

import static net.awyvrix.rtstructures.content.tools.util.ToolUtil.*;

public class StructureToolItem extends Item {
    public StructureToolItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return handleUseOn(context);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if(!Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.rtstructures.structure_tool.shift_down")
                    .withStyle(ChatFormatting.GREEN));
        } else {
            tooltipComponents.add(displayCoordinatesTooltip(StructureToolState.pos1, "Pos1: "));
            tooltipComponents.add(displayCoordinatesTooltip(StructureToolState.pos2, "Pos2: "));
            tooltipComponents.add(displayCoordinatesTooltip(StructureToolState.anchor, "Anchor: "));
            tooltipComponents.add(displayCoordinatesTooltip(getBoxSize(StructureToolState.pos1, StructureToolState.pos2), "Size: "));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}