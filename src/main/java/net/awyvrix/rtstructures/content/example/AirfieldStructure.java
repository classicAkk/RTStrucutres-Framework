package net.awyvrix.rtstructures.content.example;

import net.awyvrix.rtstructures.RTStructuresFramework;
import net.awyvrix.rtstructures.api.BuildType;
import net.awyvrix.rtstructures.api.StructureInstance;
import net.awyvrix.rtstructures.content.tools.structureTool.PlacementAnchor;
import net.awyvrix.rtstructures.registries.StructureProperties;
import net.awyvrix.rtstructures.registries.StructureType;

public class AirfieldStructure extends StructureType {
    private int productionTick;

    public AirfieldStructure() {
        super("airfield", StructureProperties.create()
                .modPath("basic/airfield", RTStructuresFramework.MOD_ID)
                .defaultAnchor(PlacementAnchor.CENTER)
                .defaultBuildType(BuildType.FAST)

                .defaultBuildSpeed(3f)
                .damagedThreshold(0.2f)
                .destroyedThreshold(0.6f)
        );
    }

    @Override
    public void onCompleted(StructureInstance instance) {
        System.out.println("[RTS] Airfield completed");
    }

    @Override
    public void onTick(StructureInstance instance) {
        if (!instance.isCompleted()) return;

        productionTick++;
        if (productionTick >= 100) {
            productionTick = 0;

            System.out.println("[RTS] Kirov reporting...");
            // Do your stuff. E.g spawn unit
        }
    }

    @Override
    public void onDestroyed(StructureInstance instance) {
        System.out.println("[RTS] Airfield destroyed!");
    }
}