package net.awyvrix.rtstructures.content.tools.linkTool;

import net.awyvrix.rtstructures.content.tools.ToolState;
import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LinkToolState extends ToolState {
    public static final LinkToolState INSTANCE = new LinkToolState();

    public static BlockPos pos1;
    public static BlockPos pos2;
    public static BlockPos anchor;
    public static BlockPos placeAnchor;
    public static int currentLink;

    // Pre-baked
    public static HashMap<BlockPos, StructureNode> pre_baked_nodes = new HashMap<>();

    // Baked
    public static List<StructureNode> nodes; // Point that contains connectPoints; Manages security and connections e.g energy substation
    public static List<SocketPoint> socketPoints; // Logic points of connect (sockets) e.g wires
    public static List<ContactPoint> ContactPoints; // Physical points of connect e.g road segments (VISUAL ONLY)

    private LinkToolState() {}
}