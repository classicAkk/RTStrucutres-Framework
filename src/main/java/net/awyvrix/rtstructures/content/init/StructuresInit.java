package net.awyvrix.rtstructures.content.init;

import net.awyvrix.rtstructures.RTStructuresFramework;
import net.awyvrix.rtstructures.content.example.AirfieldStructure;
import net.awyvrix.rtstructures.content.example.CrystalStructure;
import net.awyvrix.rtstructures.content.example.ProtectedStructure;
import net.awyvrix.rtstructures.content.example.SimpleHouseStructure;
import net.awyvrix.rtstructures.registries.RTSRegistries;
import net.awyvrix.rtstructures.registries.StructureType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class StructuresInit {
    private StructuresInit() {}

    public static final DeferredRegister<StructureType> STRUCTURES =
            DeferredRegister.create(RTSRegistries.STRUCTURES, RTStructuresFramework.MOD_ID);


    public static final Supplier<StructureType> SIMPLE_HOUSE = STRUCTURES.register("simple_house", SimpleHouseStructure::new);
    public static final Supplier<StructureType> AIRFIELD = STRUCTURES.register("airfield", AirfieldStructure::new);
    public static final Supplier<StructureType> CRYSTAL = STRUCTURES.register("crystal", CrystalStructure::new);
    public static final Supplier<StructureType> PROTECTED_STRUCTURE = STRUCTURES.register("protected_structure", ProtectedStructure::new);


    public static void register(IEventBus bus) {
        STRUCTURES.register(bus);
    }
}