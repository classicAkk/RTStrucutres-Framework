package net.awyvrix.rtstructures.content.structureTool;

import net.awyvrix.rtstructures.RTStructureFramework;
import net.awyvrix.rtstructures.content.RTStructuresKeyBinds;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = RTStructureFramework.MOD_ID)
public class StructureToolEvents {

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();

        if (player.level().isClientSide()) return;
        ItemStack stack = player.getMainHandItem();

        if (!(stack.getItem() instanceof StructureToolItem)) return;
        BlockPos pos = event.getPos();

        if (player.isShiftKeyDown()) {
            StructureToolState.placeAnchor = pos;

            StructureToolItem.displayCoordinates(player, pos, "Place Anchor Set: ");
            event.setCanceled(true);
        } else {
            if (RTStructuresKeyBinds.EXPAND_MODE.isDown()) {
                StructureToolItem.expandArea(pos);
                StructureToolItem.displayCoordinates(player, StructureToolItem.getBoxSize(StructureToolState.pos1, StructureToolState.pos2), "Box Expanded: ");
            } else {
                StructureToolState.pos2 = pos;
                StructureToolItem.displayCoordinates(player, pos, "Box Expanded: ");
            }
            event.setCanceled(true);
        }
    }
}