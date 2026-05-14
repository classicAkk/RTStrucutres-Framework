package net.awyvrix.rtstructures.registries;

import net.awyvrix.rtstructures.api.StructureInstance;
import net.awyvrix.rtstructures.content.structureTool.PlacementAnchor;
import net.awyvrix.rtstructures.content.structureTool.StructureCache;
import net.awyvrix.rtstructures.core.StructureTemplate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.file.Path;

public abstract class StructureType {
    public final String id;
    protected final StructureProperties properties;

    public StructureType(String id, StructureProperties properties) {
        this.id = id;
        this.properties = properties;

        StructureTypeRegistry.register(id, this);
    }

    public StructureProperties properties() {
        return properties;
    }

    public String getId() {
        return id;
    }

    public static StructureType getType(String id) {
        return StructureTypeRegistry.get(id);
    }

    public StructureTemplate loadTemplate(Path worldDir) {
        try {
            return StructureCache.get(worldDir, properties.path());

        } catch (Exception e) {
            throw new RuntimeException("Failed to load structure template: " + properties.path(), e);
        }
    }

    public StructureInstance create(ServerLevel level, BlockPos pos) {
        Path worldDir = level.getServer().getWorldPath(LevelResource.ROOT);
        StructureTemplate template = loadTemplate(worldDir);

        return new StructureInstance(
                this,
                level,
                template,
                pos,
                properties.defaultAnchor()
        );
    }

    public StructureInstance create(ServerLevel level, BlockPos pos, PlacementAnchor anchor) {
        Path worldDir = level.getServer().getWorldPath(LevelResource.ROOT);
        StructureTemplate template = loadTemplate(worldDir);

        return new StructureInstance(
                this,
                level,
                template,
                pos,
                anchor
        );
    }

    public void onTick(StructureInstance instance) {}

    //public void onBuildTick(StructureInstance instance) {}

    public void onCompleted(StructureInstance instance) {}

    public void onDamaged(StructureInstance instance) {}

    public void onDestroyed(StructureInstance instance) {}

    public void onDemolished(StructureInstance instance) {}

    //public void onRepair(StructureInstance instance) {}
}