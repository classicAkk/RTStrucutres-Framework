package net.awyvrix.rtstructures.registries;

import net.awyvrix.rtstructures.RTStructuresFramework;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;

public final class RTSRegistries {
    private RTSRegistries() {}

    public static final ResourceKey<Registry<StructureType>> STRUCTURES_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(RTStructuresFramework.MOD_ID, "rtstructures"));

    public static final Registry<StructureType> STRUCTURES = new RegistryBuilder<>(STRUCTURES_KEY).create();
}