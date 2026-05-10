package net.awyvrix.structureframework.content.structureTool;

import net.awyvrix.structureframework.core.StructureLoader;
import net.awyvrix.structureframework.core.StructureTemplate;

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

    public static void clear() {
        CACHE.clear();
    }
}