package net.awyvrix.rtstructures.content.worldData;

import net.awyvrix.rtstructures.RTStructureFramework;
import net.awyvrix.rtstructures.api.BuildState;
import net.awyvrix.rtstructures.api.StructureInstance;
import net.awyvrix.rtstructures.api.StructureManager;
import net.awyvrix.rtstructures.content.structureTool.PlacementAnchor;
import net.awyvrix.rtstructures.content.structureTool.StructureCache;
import net.awyvrix.rtstructures.core.StructureTemplate;
import net.awyvrix.rtstructures.registries.StructureType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.LevelResource;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class StructureWorldData extends SavedData {
    private final Map<UUID, StructureInstanceData> instances = new HashMap<>();

    public StructureWorldData() {}

    public void add(StructureInstance instance) {
        instances.put(instance.getId(), StructureInstanceData.from(instance));
        setDirty();
    }

    public void remove(UUID id) {
        instances.remove(id);
        setDirty();
    }

    public Map<UUID, StructureInstanceData> getAll() {
        return instances;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        ListTag list = new ListTag();
        for (StructureInstanceData data : instances.values()) {

            CompoundTag t = new CompoundTag();

            t.putUUID("id", data.id());
            t.putString("structureId", data.structureId());

            // anchor
            CompoundTag anchor = new CompoundTag();
            anchor.putInt("x", data.anchor().getX());
            anchor.putInt("y", data.anchor().getY());
            anchor.putInt("z", data.anchor().getZ());
            t.put("anchor", anchor);

            t.putString("anchorMode", data.anchorMode().name());
            t.putString("state", data.state().name());
            t.putString("type", data.typeId());

            t.putLong("ticksAlive", data.ticksAlive());
            t.putFloat("progress", data.progress());

            t.putString("dimension", data.dimension().toString());
            t.put("removed", StructureNbtCodec.writeBitSet(data.removed()));

            list.add(t);
        }

        tag.put(RTStructureFramework.MOD_ID, list);
        return tag;
    }

    public static StructureWorldData load(CompoundTag tag, HolderLookup.Provider provider) {
        StructureWorldData data = new StructureWorldData();

        ListTag list = tag.getList(RTStructureFramework.MOD_ID, Tag.TAG_COMPOUND);

        for (Tag raw : list) {
            CompoundTag t = (CompoundTag) raw;
            UUID id = t.getUUID("id");
            String structureId = t.getString("structureId");
            StructureType type = StructureType.getType(t.getString("type"));
            PlacementAnchor mode = PlacementAnchor.valueOf(t.getString("anchorMode"));
            CompoundTag anchorTag = t.getCompound("anchor");

            BlockPos anchor = new BlockPos(
                    anchorTag.getInt("x"),
                    anchorTag.getInt("y"),
                    anchorTag.getInt("z")
            );

            StructureInstanceData instanceData = new StructureInstanceData(
                    type,
                    id,
                    structureId,
                    anchor,
                    mode,
                    BuildState.valueOf(t.getString("state")),
                    type.id,
                    StructureNbtCodec.readBitSet(t.get("removed")),
                    t.getLong("ticksAlive"),
                    t.getFloat("progress"),
                    t.getString("dimension")
            );

            data.instances.put(id, instanceData);
        }

        return data;
    }

    public static final SavedData.Factory<StructureWorldData> FACTORY =
            new SavedData.Factory<>(StructureWorldData::new, StructureWorldData::load);

    public static StructureWorldData get(ServerLevel level) {
        return level.getDataStorage()
                .computeIfAbsent(FACTORY, "rtstructures_data");
    }

    public void apply(ServerLevel level) {
        for (StructureInstanceData saved : instances.values()) {
            if (!saved.dimension().equals(level.dimension().location().toString())) continue;
            StructureTemplate template = StructureCache.load(level.getServer().getWorldPath(LevelResource.ROOT), saved.structureId());

            StructureInstance instance = new StructureInstance(
                    saved.type(),
                    level,
                    template,
                    saved.anchor(),
                    saved.anchorMode()
            );

            instance.restoreState(
                    saved.state(),
                    saved.progress(),
                    saved.removed(),
                    saved.ticksAlive()
            );

            StructureManager.add(instance);
        }
    }

    public void syncFromManager() {
        instances.clear();
        for (StructureInstance instance : StructureManager.getAll()) {
            instances.put(instance.getId(), StructureInstanceData.from(instance));
        }

        setDirty();
    }
}