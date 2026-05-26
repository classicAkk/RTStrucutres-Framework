package net.awyvrix.rtstructures.content.tools.linkTool;

import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.UUID;

public final class StructureNode {
    UUID id;
    UUID owner;
    BlockPos localPos;

    boolean locked;
    List<SocketPoint> points;

    public StructureNode(UUID id, UUID owner, BlockPos localPos, List<SocketPoint> points) {
        this.id = id;
        this.owner = owner;
        this.locked = true;
        this.localPos = localPos;
        this.points = points;
    }
}