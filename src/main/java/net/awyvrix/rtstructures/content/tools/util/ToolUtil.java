package net.awyvrix.rtstructures.content.tools.util;

import net.awyvrix.rtstructures.content.RTStructuresKeyBinds;
import net.awyvrix.rtstructures.content.tools.ToolState;
import net.awyvrix.rtstructures.content.tools.structureTool.StructureToolState;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;

public class ToolUtil {
    public static void expandArea(ToolState state, BlockPos newPos) {
        if (state.pos1 == null) {
            state.pos1 = newPos;
            return;
        }

        if (state.pos2 == null) {
            state.pos2 = newPos;
            return;
        }

        int minX = Math.min(state.pos1.getX(), newPos.getX());
        int minY = Math.min(state.pos1.getY(), newPos.getY());
        int minZ = Math.min(state.pos1.getZ(), newPos.getZ());

        int maxX = Math.max(state.pos2.getX(), newPos.getX());
        int maxY = Math.max(state.pos2.getY(), newPos.getY());
        int maxZ = Math.max(state.pos2.getZ(), newPos.getZ());

        state.pos1 = new BlockPos(minX, minY, minZ);
        state.pos2 = new BlockPos(maxX, maxY, maxZ);
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
        if (pos == null) {return Component.literal(text).withStyle(ChatFormatting.GOLD)
                .append(Component.translatable("tooltip.rtstructures.structure_tool.no_data").withStyle(ChatFormatting.RED));
        }
        return Component.literal(text).withStyle(ChatFormatting.GOLD)
                .append(Component.literal("X: ").withStyle(ChatFormatting.RED))
                .append(Component.literal(String.valueOf(pos.getX())).withStyle(ChatFormatting.RED))
                .append(Component.literal(" Y: ").withStyle(ChatFormatting.GREEN))
                .append(Component.literal(String.valueOf(pos.getY())).withStyle(ChatFormatting.GREEN))
                .append(Component.literal(" Z: ").withStyle(ChatFormatting.BLUE))
                .append(Component.literal(String.valueOf(pos.getZ())).withStyle(ChatFormatting.BLUE));
    }

    public static BlockPos relativePos(BlockPos centerPos, BlockPos pos) {
        return new BlockPos(
                centerPos.getX() - pos.getX(),
                centerPos.getY() - pos.getY(),
                centerPos.getZ() - pos.getZ()
                );
    }

    public static InteractionResult handleUseOn(UseOnContext context) {
        if (context.getLevel().isClientSide()) return InteractionResult.SUCCESS;
        Player player = context.getPlayer();

        if (player == null) return InteractionResult.FAIL;
        BlockPos pos = context.getClickedPos();

        if (player.isShiftKeyDown()) {
            handleAnchor(player, pos);
        } else {
            handlePos1(player, pos);
        }

        return InteractionResult.SUCCESS;
    }

    // Anchor
    public static void handleAnchor(Player player, BlockPos pos) {
        StructureToolState.anchor = togglePosition(player, StructureToolState.anchor, pos, "Anchor set: ", "Anchor deleted: ");
    }

    public static void handlePlaceAnchor(Player player, BlockPos pos) {
        StructureToolState.placeAnchor = togglePosition(player, StructureToolState.placeAnchor, pos, "Place Anchor Set: ", "Place Anchor Deleted: ");
    }

    // Pos1
    public static void handlePos1(Player player, BlockPos pos) {
        if (isExpandMode()) {
            expandSelection(player, pos);
            return;
        }

        StructureToolState.pos1 = togglePosition(player, StructureToolState.pos1, pos, "Point 1 set: ", "Point 1 deleted: ");
    }

    // Pos2
    public static void handlePos2(Player player, BlockPos pos) {
        if (isExpandMode()) {
            expandSelection(player, pos);
            return;
        }

        StructureToolState.pos2 = togglePosition(player, StructureToolState.pos2, pos, "Point 2 set: ", "Point 2 deleted: ");
    }

    // Shared Toggle Logic
    public static BlockPos togglePosition(Player player, BlockPos current, BlockPos clicked, String setMessage, String deleteMessage) {
        if (current != null && current.equals(clicked)) {
            displayCoordinates(player, clicked, deleteMessage);
            return null;
        }

        displayCoordinates(player, clicked, setMessage);
        return clicked;
    }

    // Expand
    public static void expandSelection(Player player, BlockPos pos) {
        if (StructureToolState.pos1 == null || StructureToolState.pos2 == null) return;
        expandArea(StructureToolState.INSTANCE, pos);
        displayCoordinates(player, getBoxSize(StructureToolState.pos1, StructureToolState.pos2), "Box expanded: ");
    }

    // Expand Mode
    public static boolean isExpandMode() {
        return RTStructuresKeyBinds.EXPAND_MODE.isDown();
    }
}