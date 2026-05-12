package net.awyvrix.rtstructures.core;

import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public final class BlockPalette {
    private final BlockState[] states;

    public BlockPalette(List<BlockState> list) {
        this.states = list.toArray(new BlockState[0]);
    }

    public int size() {
        return states.length;
    }

    public BlockState get(int id) {
        return states[id];
    }

    public BlockState[] raw() {
        return states;
    }
}