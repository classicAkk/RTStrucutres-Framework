package net.awyvrix.rtstructures.api;

import net.awyvrix.rtstructures.content.tools.structureTool.PlacementAnchor;
import net.awyvrix.rtstructures.content.worldData.StructureWorldData;
import net.awyvrix.rtstructures.core.BlockEntry;
import net.awyvrix.rtstructures.core.StructureTemplate;
import net.awyvrix.rtstructures.registries.RawStructureType;
import net.awyvrix.rtstructures.registries.StructureProperties;
import net.awyvrix.rtstructures.registries.StructureType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.BitSet;
import java.util.UUID;

public final class StructureInstance {
    private StructureBuilderEngine engine;
    private boolean building;
    private StructureType type;

    private final UUID id;
    private final ServerLevel level;

    private final StructureTemplate template;
    private final BlockPos anchorPos;

    private final PlacementAnchor anchorMode;

    private BuildState state;
    private float progress;
    private long ticksAlive;
    private boolean completionCalled;

    public final BitSet removed = new BitSet();
    private ValidationResult validation = new ValidationResult(0, 0);

    public StructureInstance(StructureType type, ServerLevel level, StructureTemplate template, BlockPos anchorPos, PlacementAnchor anchorMode) {
        this.level = level;
        this.type = type;
        this.id = UUID.randomUUID();

        this.template = template;
        this.anchorPos = anchorPos;
        this.anchorMode = anchorMode;

        this.state = BuildState.IDLE;
        this.progress = 0f;
    }

    public StructureInstance(ServerLevel level, StructureTemplate template, BlockPos origin, PlacementAnchor anchorMode) {
        this(new RawStructureType("raw"), level, template, origin, anchorMode);
    }

    public void restoreState(
            BuildState state,
            float progress,
            BitSet removed,
            long ticks
    ) {
        this.state = state;
        this.progress = progress;

        this.removed.or(removed);

        this.ticksAlive = ticks;
    }

    public void attachEngine(StructureBuilderEngine engine) {
        this.engine = engine;
    }

    public StructureBuilderEngine getEngine() {
        return engine;
    }

    public StructureType getType() {
        return type;
    }

    public String getStructureId() {
        return type.id;
    }

    public UUID getId() {
        return id;
    }

    public StructureTemplate getStructure() {
        return template;
    }

    public BlockPos getOrigin() {
        return anchorPos;
    }

    public PlacementAnchor getAnchorMode() {
        return anchorMode;
    }

    public BuildState getState() {
        return state;
    }

    public float getProgress() {
        return progress;
    }

    public ResourceKey<Level> getDimension() {
        return level.dimension();
    }

    public boolean isCompleted() {
        return state == BuildState.COMPLETED;
    }

    public boolean isBuilding() {
        return building;
    }

    public boolean isIntact(float threshold) {
        return getDamagePercent() < threshold;
    }

    public void tick(ServerLevel level) {
        ticksAlive++;
        updateValidation();
        if (building && engine != null) {
            engine.tick();
            updateValidation();
            if (isCompleted() && !completionCalled) {
                completionCalled = true;
                building = false;
                type.onCompleted(this);
            }
        }

        if (isDamaged()) {
            type.onDamaged(this);
        }

        if (isDestroyed()) {
            type.onDestroyed(this);
        }

        type.onTick(this);
    }

    public long getTicksAlive() {
        return ticksAlive;
    }

    public String debugInfo() {
        ValidationResult v = validate();
        return "StructureInstance{" +
                "id=" + id +
                ", completion=" + v.completionPercent() +
                "%" +
                ", state=" + state +
                ", matched=" + v.matchedBlocks() +
                "/" + v.totalBlocks() +
                ", damaged=" + v.damagedBlocks() +
                ", age=" + ticksAlive +
                '}';
    }

    public ServerLevel getLevel() {
        return level;
    }

    public ValidationResult validate() {
        return validation;
    }

    public void updateValidation() {
        this.validation = StructureValidator.validate(level, this);
        if (this.validation.isDamaged()) {
            state = BuildState.DAMAGED;
            completionCalled = false;
        };
    }

    public boolean isDestroyed() {
        return validate().isDestroyed();
    }

    public boolean isDamaged() {
        return validate().isDamaged();
    }

    public boolean isCompletedStructure() {
        return validate().isCompleted();
    }

    public float getDamagePercent() {
        return validate().damagedPercent();
    }

    public float getCompletionPercent() {
        return validate().completionPercent();
    }

    public BlockPos toWorldPos(BlockEntry block) {
        BlockPos minCorner = AnchorMath.computeMinCorner(template, anchorPos, anchorMode);

        return new BlockPos(
                minCorner.getX() + block.x(),
                minCorner.getY() + block.y(),
                minCorner.getZ() + block.z()
        );
    }

    public void build(BuildType type, float speed) {
        this.state = BuildState.BUILDING;
        this.engine = new StructureBuilderEngine(this, level, type, speed);
        this.building = true;
    }

    public void build() {
        StructureProperties props = type.properties();
        build(props.defaultBuildType(), props.defaultBuildSpeed());
    }

    public void stopBuilding() {
        this.building = false;
    }

    public void complete() {
        state = BuildState.COMPLETED;
    }

    public void demolish() {
        state = BuildState.DEMOLISHED;
        type.onDemolished(this);
    }

    public void destroy() {
        if (engine != null) {
            engine.onComplete();
            engine = null;
        }

        building = false;
        state = BuildState.DEMOLISHED;

        StructureManager.remove(this);
        StructureWorldData.get(level).remove(id);
    }
}