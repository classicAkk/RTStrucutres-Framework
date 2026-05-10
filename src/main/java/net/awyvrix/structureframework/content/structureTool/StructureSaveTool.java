package net.awyvrix.structureframework.content.structureTool;

import net.awyvrix.structureframework.core.StructureCapture;
import net.awyvrix.structureframework.core.StructureSerializer;
import net.awyvrix.structureframework.core.StructureTemplate;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.file.Files;
import java.nio.file.Path;

public class StructureSaveTool {
    public static void executeSave(Level level, String fileName) {
        if (level.isClientSide()) return;
        if (StructureToolState.pos1 == null || StructureToolState.pos2 == null) {
            throw new IllegalStateException("Pos1 or Pos2 not set");
        }
        StructureTemplate template = StructureCapture.capture(level, StructureToolState.pos1, StructureToolState.pos2, StructureToolState.anchor);

        try {
            Path worldDir = level.getServer().getWorldPath(LevelResource.ROOT);
            Path path = worldDir.resolve("generated/rtstructures/" + fileName + ".rtstructure");
            Files.createDirectories(path.getParent());
            StructureSerializer.save(template, path);

            System.out.println("Saved rtstructure to: " + path);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save structure", e);
        }
    }
}