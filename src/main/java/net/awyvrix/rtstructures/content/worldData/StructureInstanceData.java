package net.awyvrix.rtstructures.content.worldData;

import net.awyvrix.rtstructures.api.BuildState;
import net.awyvrix.rtstructures.api.StructureInstance;
import net.awyvrix.rtstructures.content.tools.structureTool.PlacementAnchor;
import net.awyvrix.rtstructures.registries.StructureType;
import net.minecraft.core.BlockPos;

import java.util.BitSet;
import java.util.UUID;
public record StructureInstanceData(
        StructureType type,
        UUID id,
        String structureId,
        BlockPos anchor,
        PlacementAnchor anchorMode,
        BuildState state,
        String typeId,
        BitSet removed,
        long ticksAlive,
        float progress,
        String dimension
) {

    public static StructureInstanceData from(StructureInstance inst) {
        return new StructureInstanceData(
                inst.getType(),
                inst.getId(),
                inst.getStructureId(),
                inst.getOrigin(),
                inst.getAnchorMode(),
                inst.getState(),
                inst.getType().id,
                (BitSet) inst.removed.clone(),
                inst.getTicksAlive(),
                inst.getProgress(),
                inst.getDimension().location().toString()
        );
    }
}