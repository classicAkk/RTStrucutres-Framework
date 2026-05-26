package net.awyvrix.rtstructures.content.tools.linkTool;

import net.minecraft.core.BlockPos;

public record ContactPoint(
        BlockPos pos,
        String channel
) {}