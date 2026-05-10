package net.awyvrix.structureframework.core;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BlockPaletteBuilder {
    private final Map<String, Short> indexMap = new HashMap<>();
    private final List<BlockState> states = new ArrayList<>();

    public short getOrAdd(BlockState state) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        int propertiesHash = state.getValues().hashCode();
        String key = id + "|" + propertiesHash;
        Short idx = indexMap.get(key);

        if (idx != null) return idx;
        short newIndex = (short) states.size();
        states.add(state);
        indexMap.put(key, newIndex);

        return newIndex;
    }

    public BlockPalette build() {
        return new BlockPalette(states);
    }
}