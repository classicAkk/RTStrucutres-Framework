package net.awyvrix.rtstructures.content.tools.linkTool.gui;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.awyvrix.rtstructures.RTStructuresFramework;
import net.awyvrix.rtstructures.content.RTStructuresKeyBinds;
import net.awyvrix.rtstructures.content.tools.linkTool.LinkToolClientState;
import net.awyvrix.rtstructures.content.tools.linkTool.LinkToolItem;
import net.awyvrix.rtstructures.content.tools.linkTool.SocketCategory;
import net.awyvrix.rtstructures.content.tools.linkTool.SocketType;
import net.awyvrix.rtstructures.core.network.SocketRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = RTStructuresFramework.MOD_ID, value = Dist.CLIENT)
public final class LinkToolOverlayRenderer {

    private static final ResourceLocation OVERLAY =
            ResourceLocation.fromNamespaceAndPath(RTStructuresFramework.MOD_ID, "textures/gui/inspect_create_styled.png");

    private static final int PADDING_X = 10;
    private static final int PADDING_Y = 8;
    private static final int LINE_HEIGHT = 12;

    @SubscribeEvent
    public static void onRenderGui(RenderGuiLayerEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) return;
        ItemStack stack = minecraft.player.getMainHandItem();

        if (!(stack.getItem() instanceof LinkToolItem)) return;
        SocketType socket = LinkToolClientState.INSTANCE.selectedSocket();

        if (socket == null) return;
        SocketCategory category = SocketRegistry.category(socket.categoryId());

        if (category == null) return;
        GuiGraphics graphics = event.getGuiGraphics();
        Font font = minecraft.font;
        Window window = minecraft.getWindow();

        int centerX = window.getGuiScaledWidth() / 2;
        int centerY = window.getGuiScaledHeight() / 2;

        int x = centerX + 40;
        int y = centerY - 55;

        List<Component> lines = new ArrayList<>();
        lines.add(Component.literal("Link Tool").withColor(0xFFFFFF));
        lines.add(Component.literal(" Category").withColor(0x888888));
        lines.add(Component.literal("  ").append(Component.translatable(category.displayName()).withColor(category.color())));
        lines.add(Component.literal("-------").withColor(0x888888));
        lines.add(Component.literal("  ").append(Component.translatable(socket.displayName()).withColor(0x888888)));
        lines.add(Component.literal("  "+socket.id().toString()).withColor(0xAAAAAA));
        lines.add(Component.literal("-------").withColor(0x888888));

        if (RTStructuresKeyBinds.NEXT_CATEGORY.isUnbound()) {
            lines.add(Component.literal("Unbound + Scroll - Category").withColor(0xFF2400));
        } else {
            lines.add(Component.literal(RTStructuresKeyBinds.NEXT_CATEGORY.getTranslatedKeyMessage().getString() + " + Scroll - Category").withColor(0xFFA500));
        }

        if (RTStructuresKeyBinds.NEXT_SOCKET.isUnbound()) {
            lines.add(Component.literal("Unbound + Scroll - Socket").withColor(0xFF2400));
        } else {
            lines.add(Component.literal(RTStructuresKeyBinds.NEXT_SOCKET.getTranslatedKeyMessage().getString() + " + Scroll - Socket").withColor(0xFFA500));
        }

        // Measure
        int maxWidth = 0;
        for (Component line : lines) {
            maxWidth = Math.max(maxWidth, font.width(line));
        }

        int contentHeight = lines.size() * LINE_HEIGHT;
        int boxWidth = maxWidth + PADDING_X * 2;
        int boxHeight = contentHeight + PADDING_Y * 2;

        // BG
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        graphics.setColor(1F, 1F, 1F, 0.2F);

        Gui9Slice.blitGrid9(graphics, OVERLAY, x - PADDING_X, y - PADDING_Y, boxWidth, boxHeight);

        graphics.setColor(1F, 1F, 1F, 1F);
        RenderSystem.disableBlend();

        // Text
        int drawY = y;
        for (Component line : lines) {
            int color = 0xFFFFFF;

            if (line.getString().equals("Category") || line.getString().equals("Socket")) {
                color = 0x888888;
            }

            graphics.drawString(font, line, x, drawY, color);
            drawY += LINE_HEIGHT;
        }
    }
}