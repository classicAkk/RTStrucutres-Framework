# RTS Structure Framework Documentation (v1.0)

# RTS Structure Framework
RTS-oriented structure framework for Minecraft 1.21.1 NeoForge.

The library provides:
- custom `.rtstructure` format
- fast structure loading/saving
- runtime structure instances
- RTS-style building system
- structure validation/damage detection
- custom anchors
- deterministic placement pipeline

---

# Features

## Structure System
- Save structures from world selection
- Load structures instantly
- Custom binary structure format
- Full BlockState support
- Palette-based storage
- Deterministic placement

## RTS Runtime
- Runtime StructureInstance
- Tick-based building engine
- Multiple build modes
- Damage validation
- Completion tracking
- Progressive construction

## Anchors
Supported placement anchors:
- `CORNER`
- `CENTER`
- `CUSTOM`

## Build Modes
Supported build modes:
- `FAST`
- `FAST_SAFE`
- `CONNECT`
- `SEPARATED`

---

# Installation

## Gradle (NeoForge)

### repositories

```gradle
repositories {
    maven {
        url = "https://your-maven-url"
    }
}
```

### dependencies

```gradle
dependencies {
    implementation fg.deobf("net.awyvrix:structure-framework:1.0.0")
}
```

---

# Basic Usage

# Saving Structure

```java
StructureTemplate template = StructureCapture.capture(level, pos1, pos2, customAnchor);

Path path = worldDir.resolve("generated/rtstructures/house.rtstructure");

StructureSerializer.save(template, path);
```

---

# Loading Structure

```java
Path path = worldDir.resolve("generated/rtstructures/house.rtstructure");

StructureTemplate template = StructureDeserializer.load(path);
```

---

# Instant Placement

```java
StructurePlacer.place(
        level,
        targetPos,
        template,
        PlacementAnchor.CENTER
);
```

---

# Runtime Structure Instance

```java
StructureInstance instance =
        new StructureInstance(
                level,
                template,
                targetPos,
                PlacementAnchor.CENTER
        );
```

---

# Register Structure

```java
StructureManager.add(instance);
```

---

# Start Building

```java
instance.build(
        BuildType.FAST,
        10
);
```

Where:

* `BuildType` = construction logic
* `speed` = blocks per tick

---

# Validation System

## Check Damage

```java
boolean damaged = instance.isDamaged();
```

---

## Completion Percent

```java
float percent = instance.getCompletionPercent();
```

---

## Damage Percent

```java
float percent = instance.getDamagePercent();
```

---

## Full Validation Result

```java
ValidationResult result = instance.validate();
```

---

# Build Modes

## FAST

Instant layered building.

Characteristics:

* fast
* unrestricted
* replaces blocks

Best for:

* debugging
* cinematic building
* RTS instant construction

---

## FAST_SAFE

Layered building without replacing existing blocks.

Characteristics:

* preserves world
* safer placement
* slower completion

Best for:

* survival gameplay
* protected worlds

---

## CONNECT

Builds only connected blocks.

Characteristics:

* realistic propagation
* support-dependent
* may stall if disconnected

Best for:

* RTS simulation
* organic building systems

---

## SEPARATED

Growth-style construction from anchor.

Characteristics:

* spreading build effect
* directional expansion
* organic appearance

Best for:

* alien structures
* plant-like construction
* visual effects

---

# Placement Anchors

## CORNER

Uses minimum corner as placement origin.

```text
[X]
###
###
###
```

---

## CENTER

Uses structure center.

```text
###
#X#
###
```

---

## CUSTOM

Uses user-defined anchor saved in metadata.

```text
###
##X
###
```

---

# Runtime Architecture

# Structure Pipeline

```text
WORLD SELECTION
        ↓
StructureCapture
        ↓
StructureTemplate
        ↓
StructureSerializer
        ↓
.rtstructure
        ↓
StructureDeserializer
        ↓
Runtime StructureTemplate
        ↓
StructureInstance
        ↓
Builder Engine / Placement
```

---

# Structure Format

Extension:

```text
.rtstructure
```

Format type:

```text
Binary
```

---

# Internal File Layout

```text
MAGIC
VERSION
SIZE
PALETTE
BLOCKS
METADATA
```

---

## MAGIC

Used for file validation.

```text
RTS1
```

---

## VERSION

Format version integer.

```text
1
```

---

## SIZE

Structure dimensions.

```text
sizeX
sizeY
sizeZ
```

Stored as:

```text
short short short
```

---

## PALETTE

Unique BlockStates used by structure.

Palette stores:

```text
minecraft:block[property=value]
```

Example:

```text
minecraft:oak_stairs[facing=north,half=bottom]
```

Purpose:

* compression
* deterministic states
* fast lookup

---

## BLOCKS

Block entries reference palette IDs.

Stored data:

```text
x
y
z
paletteId
```

Purpose:

* compact storage
* fast runtime iteration

---

## METADATA

Stores structure metadata.

Current metadata:

* custom anchor
* anchor coordinates

---

# Deterministic Placement System

The framework uses deterministic placement.

This means:

* identical placement order
* stable block states
* reproducible builds
* multiplayer consistency

---

# Layered Construction

Construction order:

```text
bottom → top
```

Within each layer:

```text
radial expansion from center
```

This creates RTS-style construction behavior.

---

# Performance Notes

## Optimizations

Current optimizations:

* palette compression
* immutable templates
* queue-based building
* deterministic ordering
* no BlockEntities
* lightweight runtime objects

---

## Recommended Limits

Recommended structure sizes:

| Size         | Recommendation                   |
| ------------ | -------------------------------- |
| < 10k blocks | Excellent                        |
| 10k - 50k    | Good                             |
| 50k - 200k   | Requires tuning                  |
| 200k+        | Future async systems recommended |

---

# Current Limitations

v1 intentionally excludes:

* rotations
* mirroring
* BlockEntities
* entities
* async chunk batching
* networking
* worker AI
* visual effects

These systems are planned for future versions.

---

# Planned Features

## v1.x

* repair system
* async loading
* structure upgrades
* placement events
* schematic converter

## v2

* rotations
* BlockEntity support
* worker AI
* multiplayer sync
* chunk batching
* visual construction

---

# Example RTS Workflow

```java
StructureTemplate template =
        StructureDeserializer.load(path);

StructureInstance instance =
        new StructureInstance(
                level,
                template,
                position,
                PlacementAnchor.CENTER
        );

StructureManager.add(instance);

instance.build(
        BuildType.CONNECT,
        5
);
```

---

# License

MIT License

---

# Credits

Created for NeoForge 1.21.1
Designed for RTS-oriented gameplay systems.

```
```
