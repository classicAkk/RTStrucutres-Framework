package net.awyvrix.rtstructures;

import net.awyvrix.rtstructures.content.RTStructuresKeyBinds;
import net.awyvrix.rtstructures.content.init.ItemInit;
import net.awyvrix.rtstructures.content.tools.linkTool.*;
import net.awyvrix.rtstructures.core.network.SocketRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = RTStructuresFramework.MOD_ID, value = Dist.CLIENT)
public class RTStructuresFrameworkClient {

    public RTStructuresFrameworkClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {}

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(RTStructuresKeyBinds.EXPAND_MODE);
        event.register(RTStructuresKeyBinds.NEXT_CATEGORY);
        event.register(RTStructuresKeyBinds.NEXT_SOCKET);
        event.register(RTStructuresKeyBinds.DELETE_NODE);
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        double scrollDelta = event.getScrollDeltaY();
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (RTStructuresKeyBinds.NEXT_CATEGORY.isDown() && stack.getItem() == ItemInit.LINK_TOOL.get()) {
            event.setCanceled(true);
            if (scrollDelta > 0) {
                LinkToolClientState.INSTANCE.nextCategory(1);
            } else if (scrollDelta < 0) {
                LinkToolClientState.INSTANCE.nextCategory(-1);
            }
            player.displayClientMessage(Component.literal("" + LinkToolState.currentLink).withStyle(ChatFormatting.GOLD), true);
        }
        if (RTStructuresKeyBinds.NEXT_SOCKET.isDown() && stack.getItem() == ItemInit.LINK_TOOL.get()) {
            event.setCanceled(true);
            if (scrollDelta > 0) {
                LinkToolClientState.INSTANCE.nextSocket(1);
            } else if (scrollDelta < 0) {
                LinkToolClientState.INSTANCE.nextSocket(-1);
            }
            //player.displayClientMessage(Component.literal("" + LinkToolState.currentLink).withStyle(ChatFormatting.GOLD), true);
        }
    }

    /*
    @SubscribeEvent
    public static void onMouseClick(InputEvent.MouseButton.Pre event) {
        if (event.getButton() != GLFW.GLFW_MOUSE_BUTTON_MIDDLE) return;
        Minecraft minecraft = Minecraft.getInstance();

        if (!(minecraft.hitResult instanceof BlockHitResult hit)) return;
        SocketType socket = DebugSocketLookup.findSocket(minecraft.level, hit.getBlockPos());

        if (socket == null) return;
        LinkToolClientState.INSTANCE.select(socket);
        event.setCanceled(true);
    }
     */

    @SubscribeEvent
    public static void onRecipesUpdated(RecipesUpdatedEvent event) {
        LinkToolClientState.INSTANCE.reload();
    }
}