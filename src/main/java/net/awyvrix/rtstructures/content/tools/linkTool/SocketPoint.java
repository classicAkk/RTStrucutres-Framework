package net.awyvrix.rtstructures.content.tools.linkTool;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public record SocketPoint(
        String id,
        BlockPos localPos,
        int maxConnections,
        Direction direction
) {}