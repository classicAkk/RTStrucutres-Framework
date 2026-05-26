package net.awyvrix.rtstructures.core;

import net.awyvrix.rtstructures.content.tools.linkTool.ContactPoint;
import net.awyvrix.rtstructures.content.tools.linkTool.SocketPoint;
import net.awyvrix.rtstructures.content.tools.linkTool.StructureNode;

import java.util.List;

public final class StructureMetadata {
    public final int version = 3;
    public final boolean isLinkable;
    public final boolean hasCustomAnchor;

    public final short anchorX;
    public final short anchorY;
    public final short anchorZ;

    public final List<StructureNode> nodes;
    public final List<SocketPoint> connectPoints;
    public final List<ContactPoint> contactPoints;

    public StructureMetadata(
            boolean hasCustomAnchor, boolean isLinkable,
            short anchorX, short anchorY, short anchorZ,
            List<StructureNode> nodes, List<SocketPoint> connectPoints, List<ContactPoint> contactPoints
    ) {
        this.isLinkable = isLinkable;
        this.hasCustomAnchor = hasCustomAnchor;

        this.anchorX = anchorX;
        this.anchorY = anchorY;
        this.anchorZ = anchorZ;

        this.nodes = nodes;
        this.connectPoints = connectPoints;
        this.contactPoints = contactPoints;
    }
}