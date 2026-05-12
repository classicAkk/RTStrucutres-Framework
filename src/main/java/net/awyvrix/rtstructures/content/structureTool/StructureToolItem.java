package net.awyvrix.rtstructures.content.structureTool;

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

public class StructureToolItem extends Item {
    public StructureToolItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if (player.isShiftKeyDown()) {
            StructureToolState.anchor = pos;
            displayCoordinates(player, pos, "Anchor set: ");
        } else {
            if (RTStructuresKeyBinds.EXPAND_MODE.isDown()) {
                expandArea(pos);
                displayCoordinates(player, getBoxSize(StructureToolState.pos1, StructureToolState.pos2), "Box expanded: ");
            } else {
                StructureToolState.pos1 = pos;
                displayCoordinates(player, pos, "Box expanded: ");
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if(!Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.structure_framework.structure_tool.shift_down")
                    .withStyle(ChatFormatting.GREEN));
        } else {
            tooltipComponents.add(displayCoordinatesTooltip(StructureToolState.pos1, "Pos1: "));
            tooltipComponents.add(displayCoordinatesTooltip(StructureToolState.pos2, "Pos2: "));
            tooltipComponents.add(displayCoordinatesTooltip(StructureToolState.anchor, "Anchor: "));
            tooltipComponents.add(displayCoordinatesTooltip(getBoxSize(StructureToolState.pos1, StructureToolState.pos2), "Size: "));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    public static void expandArea(BlockPos newPos) {
        if (StructureToolState.pos1 == null) {
            StructureToolState.pos1 = newPos;
            return;
        }

        if (StructureToolState.pos2 == null) {
            StructureToolState.pos2 = newPos;
            return;
        }

        int minX = Math.min(StructureToolState.pos1.getX(), newPos.getX());
        int minY = Math.min(StructureToolState.pos1.getY(), newPos.getY());
        int minZ = Math.min(StructureToolState.pos1.getZ(), newPos.getZ());

        int maxX = Math.max(StructureToolState.pos2.getX(), newPos.getX());
        int maxY = Math.max(StructureToolState.pos2.getY(), newPos.getY());
        int maxZ = Math.max(StructureToolState.pos2.getZ(), newPos.getZ());

        StructureToolState.pos1 = new BlockPos(minX, minY, minZ);
        StructureToolState.pos2 = new BlockPos(maxX, maxY, maxZ);
    }

    public static BlockPos getBoxSize(BlockPos p1, BlockPos p2) {
        if (p1 == null || p2 == null) return BlockPos.ZERO;

        int dx = Math.abs(p2.getX() - p1.getX()) + 1;
        int dy = Math.abs(p2.getY() - p1.getY()) + 1;
        int dz = Math.abs(p2.getZ() - p1.getZ()) + 1;

        return new BlockPos(dx, dy, dz);
    }

    public static void displayCoordinates(Player player, BlockPos pos, String text) {
        player.displayClientMessage(
                Component.literal(text).withStyle(ChatFormatting.GOLD)
                        .append(Component.literal("X: ").withStyle(ChatFormatting.RED))
                        .append(Component.literal(String.valueOf(pos.getX())).withStyle(ChatFormatting.RED))
                        .append(Component.literal(" Y: ").withStyle(ChatFormatting.GREEN))
                        .append(Component.literal(String.valueOf(pos.getY())).withStyle(ChatFormatting.GREEN))
                        .append(Component.literal(" Z: ").withStyle(ChatFormatting.BLUE))
                        .append(Component.literal(String.valueOf(pos.getZ())).withStyle(ChatFormatting.BLUE)),
                true
        );
    }

    public static Component displayCoordinatesTooltip(BlockPos pos, String text) {
        if (pos == null) return Component.literal(text + Component.translatable("tooltip.structure_framework.structure_tool.no_data")).withStyle(ChatFormatting.RED);
        return Component.literal(text).withStyle(ChatFormatting.GOLD)
                .append(Component.literal("X: ").withStyle(ChatFormatting.RED))
                .append(Component.literal(String.valueOf(pos.getX())).withStyle(ChatFormatting.RED))
                .append(Component.literal(" Y: ").withStyle(ChatFormatting.GREEN))
                .append(Component.literal(String.valueOf(pos.getY())).withStyle(ChatFormatting.GREEN))
                .append(Component.literal(" Z: ").withStyle(ChatFormatting.BLUE))
                .append(Component.literal(String.valueOf(pos.getZ())).withStyle(ChatFormatting.BLUE));
    }
}