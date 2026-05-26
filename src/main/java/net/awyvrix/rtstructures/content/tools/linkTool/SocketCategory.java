package net.awyvrix.rtstructures.content.tools.linkTool;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public record SocketCategory(
        String id,
        String displayName,
        int color,
        @Nullable ResourceLocation icon
) {}