package net.awyvrix.rtstructures.content.tools.structureTool;

import net.awyvrix.rtstructures.RTStructuresFramework;
import net.awyvrix.rtstructures.content.RTStructuresKeyBinds;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import static net.awyvrix.rtstructures.content.tools.util.ToolUtil.*;

@EventBusSubscriber(modid = RTStructuresFramework.MOD_ID)
public class StructureToolEvents {

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();

        if (player.level().isClientSide()) return;
        ItemStack stack = player.getMainHandItem();

        if (!(stack.getItem() instanceof StructureToolItem)) return;
        BlockPos pos = event.getPos();

        if (player.isShiftKeyDown()) {
            handlePlaceAnchor(player, pos);
        } else {
            handlePos2(player, pos);
        }

        event.setCanceled(true);
    }
}