package net.awyvrix.rtstructures.content.example;

import net.awyvrix.rtstructures.RTStructuresFramework;
import net.awyvrix.rtstructures.api.BuildType;
import net.awyvrix.rtstructures.api.StructureInstance;
import net.awyvrix.rtstructures.registries.StructureProperties;
import net.awyvrix.rtstructures.registries.StructureType;

public class ProtectedStructure extends StructureType {

    public ProtectedStructure() {
        super("protected_structure", StructureProperties.create()
                .modPath("protected_structure", RTStructuresFramework.MOD_ID)

                .defaultBuildType(BuildType.FAST_SAFE)
                .defaultBuildSpeed(5f)
                .destroyedThreshold(0.95f)
        );
    }

    @Override
    public void onDamaged(StructureInstance instance) {
        System.out.println("[RTS] Core structure damaged!");
    }

    @Override
    public void onDestroyed(StructureInstance instance) {
        // Prevent full destruction
        if (instance.getDamagePercent() >= 0.95f) {
            System.out.println("[RTS] Emergency repair activated");

            instance.build(BuildType.FAST, 10f);
        }
    }
}