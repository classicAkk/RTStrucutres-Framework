package net.awyvrix.rtstructures.registries;

import java.util.HashMap;
import java.util.Map;

public final class StructureTypeRegistry {
    private static final Map<String, StructureType> TYPES = new HashMap<>();

    public static void register(String id, StructureType type) {
        if (TYPES.containsKey(id)) {
            throw new IllegalStateException("Duplicate structure type: " + id);
        }

        TYPES.put(id, type);
    }

    public static StructureType get(String id) {
        return TYPES.getOrDefault(id.toLowerCase(), TYPES.get("raw"));
    }

    public static void clear() {
        TYPES.clear();
    }
}