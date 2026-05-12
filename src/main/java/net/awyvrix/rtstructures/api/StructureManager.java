package net.awyvrix.rtstructures.api;

import net.minecraft.server.level.ServerLevel;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class StructureManager {
    private static final Map<UUID, StructureInstance> INSTANCES = new ConcurrentHashMap<>();

    private StructureManager() {}

    public static void add(StructureInstance instance) {
        INSTANCES.put(instance.getId(), instance);
    }

    public static void remove(UUID id) {
        INSTANCES.remove(id);
    }

    public static void remove(StructureInstance instance) {
        INSTANCES.remove(instance.getId());
    }

    public static StructureInstance get(UUID id) {
        return INSTANCES.get(id);
    }

    public static Collection<StructureInstance> getAll() {
        return INSTANCES.values();
    }

    public static void tick(ServerLevel level) {

        for (StructureInstance instance : INSTANCES.values()) {
            if (instance.getLevel() != level) continue;
            instance.tick(level);
        }
    }
}