package net.awyvrix.rtstructures.content.example;

import net.awyvrix.rtstructures.RTStructuresFramework;
import net.awyvrix.rtstructures.api.BuildType;
import net.awyvrix.rtstructures.api.StructureInstance;
import net.awyvrix.rtstructures.content.tools.structureTool.PlacementAnchor;
import net.awyvrix.rtstructures.registries.StructureProperties;
import net.awyvrix.rtstructures.registries.StructureType;

public class CrystalStructure extends StructureType {

    public CrystalStructure() {
        super("crystal", StructureProperties.create()
                .modPath("crystal", RTStructuresFramework.MOD_ID)
                .defaultBuildType(BuildType.FAST)
                .defaultBuildSpeed(1f)
                .defaultAnchor(PlacementAnchor.CUSTOM)

                .damagedThreshold(0.05f)
                .destroyedThreshold(0.8f)
        );
    }

    @Override
    public void onTick(StructureInstance instance) {
        if (instance.isDamaged()) {
            if (!instance.isBuilding()) {
                instance.build(BuildType.FAST, 0.5f);
            }
        }
    }

    @Override
    public void onDestroyed(StructureInstance instance) {
        if (instance.getDamagePercent() >= 0.95f) {
            instance.destroy();
        }
    }

    @Override
    public void onCompleted(StructureInstance instance) {
        System.out.println(instance.debugInfo());
    }
}