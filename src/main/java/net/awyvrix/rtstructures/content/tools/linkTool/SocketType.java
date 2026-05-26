package net.awyvrix.rtstructures.content.tools.linkTool;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public record SocketType(
        ResourceLocation id,
        String categoryId,
        String displayName,
        int color,
        @Nullable ResourceLocation icon
) {}