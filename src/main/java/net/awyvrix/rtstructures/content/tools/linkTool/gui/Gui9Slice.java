package net.awyvrix.rtstructures.content.tools.linkTool.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public final class Gui9Slice {
    public static void blitGrid9(GuiGraphics g, ResourceLocation t, int x, int y, int w, int h) {
        int s = 30;

        int innerW = w - s * 2;
        int innerH = h - s * 2;

        int tex = 90;

        // Top
        g.blit(t, x, y, 0, 0, s, s, tex, tex);
        g.blit(t, x + w - s, y, 60, 0, s, s, tex, tex);

        // Bottom
        g.blit(t, x, y + h - s, 0, 60, s, s, tex, tex);
        g.blit(t, x + w - s, y + h - s, 60, 60, s, s, tex, tex);

        // Center
        for (int cy = 0; cy < innerH; cy += s) {
            for (int cx = 0; cx < innerW; cx += s) {
                int drawW = Math.min(s, innerW - cx);
                int drawH = Math.min(s, innerH - cy);
                g.blit(t, x + s + cx, y + s + cy, 30, 30, drawW, drawH, tex, tex);
            }
        }

        // Left
        for (int cy = 0; cy < innerH; cy += s) {
            int drawH = Math.min(s, innerH - cy);

            g.blit(t, x, y + s + cy, 0, 30, s, drawH, tex, tex);
        }

        // Right
        for (int cy = 0; cy < innerH; cy += s) {
            int drawH = Math.min(s, innerH - cy);
            g.blit(t, x + w - s, y + s + cy, 60, 30, s, drawH, tex, tex);
        }

        // Top
        for (int cx = 0; cx < innerW; cx += s) {
            int drawW = Math.min(s, innerW - cx);
            g.blit(t, x + s + cx, y, 30, 0, drawW, s, tex, tex);
        }

        // Bottom
        for (int cx = 0; cx < innerW; cx += s) {
            int drawW = Math.min(s, innerW - cx);
            g.blit(t, x + s + cx, y + h - s, 30, 60, drawW, s, tex, tex);
        }
    }
}