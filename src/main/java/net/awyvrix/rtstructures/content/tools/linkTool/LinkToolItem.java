package net.awyvrix.rtstructures.content.tools.linkTool;

import net.awyvrix.rtstructures.content.RTStructuresKeyBinds;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;

import java.util.List;

import static net.awyvrix.rtstructures.content.tools.util.ToolUtil.*;

public class LinkToolItem extends Item {
    public LinkToolItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if (player == null) return InteractionResult.FAIL;
        if (RTStructuresKeyBinds.DELETE_NODE.isDown()) {
            LinkToolState.anchor = pos;
            displayCoordinates(player, pos, "Socket deleted: ");
        } else {
            Direction side = context.getClickedFace();
            player.displayClientMessage(Component.literal("Side set: " + side).withStyle(ChatFormatting.GOLD), true);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if(!Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.rtstructures.structure_tool.shift_down")
                    .withStyle(ChatFormatting.GREEN));
            tooltipComponents.add(Component.translatable("tooltip.rtstructures.link_tool.shift_down")
                    .withStyle(ChatFormatting.RED));
        } else {
            tooltipComponents.add(displayCoordinatesTooltip(LinkToolState.pos1, "Pos1: "));
            tooltipComponents.add(displayCoordinatesTooltip(LinkToolState.pos2, "Pos2: "));
            tooltipComponents.add(displayCoordinatesTooltip(LinkToolState.anchor, "Anchor: "));
            tooltipComponents.add(displayCoordinatesTooltip(getBoxSize(LinkToolState.pos1, LinkToolState.pos2), "Size: "));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}