package net.awyvrix.rtstructures.registries;

import net.awyvrix.rtstructures.api.BuildType;
import net.awyvrix.rtstructures.content.tools.structureTool.PlacementAnchor;

public class StructureProperties {
    private String path;

    private BuildType defaultBuildType = BuildType.FAST;
    private float defaultBuildSpeed = 1.0f;

    private float damagedThreshold = 0.1f;
    private float destroyedThreshold = 0.5f;

    private PlacementAnchor defaultAnchor = PlacementAnchor.CORNER;

    private boolean autoValidate = true;
    private boolean canRepair = true;

    public static StructureProperties create() {
        return new StructureProperties();
    }


    public StructureProperties path(String path) {
        this.path = path;
        return this;
    }

    public StructureProperties modPath(String path, String MOD_ID) {
        this.path = "moddata/" + MOD_ID + "/" + path;
        return this;
    }


    public StructureProperties defaultBuildType(
            BuildType type
    ) {
        this.defaultBuildType = type;
        return this;
    }

    public StructureProperties defaultBuildSpeed(
            float speed
    ) {
        this.defaultBuildSpeed = speed;
        return this;
    }

    public StructureProperties damagedThreshold(
            float value
    ) {
        this.damagedThreshold = value;
        return this;
    }

    public StructureProperties destroyedThreshold(
            float value
    ) {
        this.destroyedThreshold = value;
        return this;
    }

    public StructureProperties defaultAnchor(
            PlacementAnchor anchor
    ) {
        this.defaultAnchor = anchor;
        return this;
    }

    public StructureProperties autoValidate(
            boolean value
    ) {
        this.autoValidate = value;
        return this;
    }

    public StructureProperties canRepair(
            boolean value
    ) {
        this.canRepair = value;
        return this;
    }

    public String path() {
        return path;
    }

    public BuildType defaultBuildType() {
        return defaultBuildType;
    }

    public float defaultBuildSpeed() {
        return defaultBuildSpeed;
    }

    public float damagedThreshold() {
        return damagedThreshold;
    }

    public float destroyedThreshold() {
        return destroyedThreshold;
    }

    public PlacementAnchor defaultAnchor() {
        return defaultAnchor;
    }

    public boolean autoValidate() {
        return autoValidate;
    }

    public boolean canRepair() {
        return canRepair;
    }
}