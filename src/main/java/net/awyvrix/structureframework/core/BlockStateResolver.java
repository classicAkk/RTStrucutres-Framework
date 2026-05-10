package net.awyvrix.structureframework.core;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class BlockStateResolver {
    public static BlockState resolve(String id) {
        ResourceLocation rl = ResourceLocation.parse(id);

        Block block = BuiltInRegistries.BLOCK.get(rl);

        if (block == null) {
            return Blocks.AIR.defaultBlockState();
        }

        return block.defaultBlockState();
    }
}