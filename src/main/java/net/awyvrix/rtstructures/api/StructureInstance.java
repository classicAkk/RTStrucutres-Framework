package net.awyvrix.rtstructures.api;

import net.awyvrix.rtstructures.content.structureTool.PlacementAnchor;
import net.awyvrix.rtstructures.core.BlockEntry;
import net.awyvrix.rtstructures.core.StructureTemplate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.BitSet;
import java.util.UUID;

public final class StructureInstance {
    private StructureBuilderEngine engine;
    private boolean building;

    private final UUID id;
    private final ServerLevel level;

    private final StructureTemplate template;
    private final BlockPos anchorPos;

    private final PlacementAnchor anchorMode;

    private BuildState state;
    private float progress;

    private final BitSet built;
    private final BitSet damaged;
    private long ticksAlive;

    public StructureInstance(ServerLevel level, StructureTemplate template, BlockPos anchorPos, PlacementAnchor anchorMode) {
        this.level = level;
        this.id = UUID.randomUUID();

        this.template = template;
        this.anchorPos = anchorPos;
        this.anchorMode = anchorMode;

        int size = template.blocks.length;

        this.built = new BitSet(size);
        this.damaged = new BitSet(size);

        this.state = BuildState.IDLE;
        this.progress = 0f;
    }

    public void attachEngine(StructureBuilderEngine engine) {
        this.engine = engine;
    }

    public StructureBuilderEngine getEngine() {
        return engine;
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

    public boolean isCompleted() {
        return state == BuildState.COMPLETED;
    }

    public boolean isBuilding() {
        return state == BuildState.BUILDING;
    }

    public void damageBlock(int index) {
        if (index < 0 || index >= template.blocks.length) return;
        damaged.set(index);
        state = BuildState.DAMAGED;
    }

    public void repairBlock(int index) {
        if (index < 0 || index >= template.blocks.length) return;
        damaged.clear(index);

        if (damaged.isEmpty() && isCompleted()) {
            state = BuildState.COMPLETED;
        }
    }

    public boolean isIntact(float threshold) {
        return getDamagePercent() < threshold;
    }

    public void startBuild(BuildState initialState) {
        this.state = BuildState.BUILDING;
        this.progress = 0f;
    }

    public void markBlockBuilt(int index) {
        if (index < 0 || index >= template.blocks.length) return;
        built.set(index);
        updateProgress();

        if (built.cardinality() == template.blocks.length) {
            state = BuildState.COMPLETED;
            progress = 1f;
        }
    }

    public boolean isBuilt(int index) {
        return built.get(index);
    }

    private void updateProgress() {
        int total = template.blocks.length;
        int done = built.cardinality();

        this.progress = total == 0 ? 0f : (float) done / total;
    }

    public void tick() {
        ticksAlive++;

        if (state != BuildState.BUILDING) return;
    }

    public void tick(ServerLevel level) {
        if (!building) {
            return;
        }

        if (engine != null) {
            engine.tick();
        }
    }

    public long getTicksAlive() {
        return ticksAlive;
    }

    BitSet getBuiltSet() {
        return built;
    }

    BitSet getDamagedSet() {
        return damaged;
    }

    public String debugInfo() {
        return "StructureInstance{" +
                "id=" + id +
                ", progress=" + progress +
                ", state=" + state +
                ", built=" + built.cardinality() +
                "/" + template.blocks.length +
                ", damaged=" + damaged.cardinality() +
                ", age=" + ticksAlive +
                '}';
    }

    public ServerLevel getLevel() {
        return level;
    }

    public ValidationResult validate() {
        return StructureValidator.validate(level, this);
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
        this.engine = new StructureBuilderEngine(this, level, type, speed);
        this.building = true;
    }

    public void stopBuilding() {
        this.building = false;
    }
}