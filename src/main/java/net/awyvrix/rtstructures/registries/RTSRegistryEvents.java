package net.awyvrix.rtstructures.registries;

import net.neoforged.neoforge.registries.NewRegistryEvent;

public final class RTSRegistryEvents {
    private RTSRegistryEvents() {}

    public static void register(NewRegistryEvent event) {
        event.register(RTSRegistries.STRUCTURES);
    }
}