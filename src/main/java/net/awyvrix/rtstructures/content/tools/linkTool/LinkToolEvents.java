package net.awyvrix.rtstructures.content.tools.linkTool;

import net.awyvrix.rtstructures.RTStructuresFramework;
import net.awyvrix.rtstructures.content.tools.structureTool.StructureToolState;
import net.awyvrix.rtstructures.content.tools.util.ToolUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import static net.awyvrix.rtstructures.content.tools.util.ToolUtil.relativePos;

@EventBusSubscriber(modid = RTStructuresFramework.MOD_ID)
public class LinkToolEvents {

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();

        if (player.level().isClientSide()) return;
        ItemStack stack = player.getMainHandItem();

        if (!(stack.getItem() instanceof LinkToolItem)) return;
        BlockPos pos = event.getPos();

        if (StructureToolState.anchor == null) {
            player.displayClientMessage(Component.literal("No anchor provided").withStyle(ChatFormatting.RED), true);
            event.setCanceled(true);
            return;
        }

        if (LinkToolState.pre_baked_nodes.containsKey(pos)) {
            ToolUtil.displayCoordinates(player, pos, "Node removed: ");
            LinkToolState.pre_baked_nodes.remove(pos);
        } else {
            ToolUtil.displayCoordinates(player, pos, "Node set: ");
            StructureNode node = new StructureNode(null, null, relativePos(StructureToolState.anchor, pos), null);
            LinkToolState.pre_baked_nodes.putIfAbsent(pos, node);
        }

        event.setCanceled(true);
    }
}