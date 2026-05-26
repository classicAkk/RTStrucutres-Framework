package net.awyvrix.rtstructures.content.tools.structureTool;

import net.awyvrix.rtstructures.content.worldData.StructureFileIndex;
import net.awyvrix.rtstructures.content.worldData.StructurePaths;
import net.awyvrix.rtstructures.core.StructureDeserializer;
import net.awyvrix.rtstructures.core.StructureLoader;
import net.awyvrix.rtstructures.core.StructureTemplate;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class StructureCache {
    private static final Map<String, StructureTemplate> CACHE = new HashMap<>();

    public static StructureTemplate get(Path worldDir, String filename) {
        return CACHE.computeIfAbsent(filename, f -> {
            try {
                return StructureLoader.load(worldDir, f);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static StructureTemplate load(Path worldDir, String id) {
        return CACHE.computeIfAbsent(id, k -> loadFromDisk(worldDir, k));
    }

    public static void preloadAll(Path worldDir) {
        for (String id : StructureFileIndex.getAllIds(worldDir)) {
            CACHE.put(id, loadFromDisk(worldDir, id));
        }
    }

    private static StructureTemplate loadFromDisk(Path worldDir, String id) {
        try {
            Path path = StructurePaths.getPath(worldDir, id);
            return StructureDeserializer.load(path);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load structure: " + id, e);
        }
    }

    public static void clear() {
        CACHE.clear();
    }
}