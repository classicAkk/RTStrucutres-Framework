package net.awyvrix.rtstructures.content.example;

import net.awyvrix.rtstructures.RTStructureFramework;
import net.awyvrix.rtstructures.api.BuildType;
import net.awyvrix.rtstructures.content.structureTool.PlacementAnchor;
import net.awyvrix.rtstructures.registries.StructureProperties;
import net.awyvrix.rtstructures.registries.StructureType;

public class SimpleHouseStructure extends StructureType {

    public SimpleHouseStructure() {
        super("simple_house", StructureProperties.create()
                // Structure file
                .modPath("basic/simple_house", RTStructureFramework.MOD_ID) // In your resources/data/mod_id/...

                // Default placement
                .defaultAnchor(PlacementAnchor.CENTER)

                // Default build behavior
                .defaultBuildType(BuildType.FAST)
                .defaultBuildSpeed(2f)

                // Validation
                .damagedThreshold(0.15f)
                .destroyedThreshold(0.5f)
        );
    }
}