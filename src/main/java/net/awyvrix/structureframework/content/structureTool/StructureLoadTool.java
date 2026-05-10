package net.awyvrix.structureframework.content.structureTool;

import net.awyvrix.structureframework.core.StructureLoader;
import net.awyvrix.structureframework.core.StructurePlacer;
import net.awyvrix.structureframework.core.StructureTemplate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.nio.file.Path;

public final class StructureLoadTool {
    public static void executeLoad(
            Level level,
            Path worldDir,
            String filename,
            BlockPos targetPos,
            PlacementAnchor anchor
    ) {

        try {
            StructureTemplate template = StructureLoader.load(worldDir, filename);
            StructurePlacer.place(level, targetPos, template, anchor);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load structure", e);
        }
    }
}