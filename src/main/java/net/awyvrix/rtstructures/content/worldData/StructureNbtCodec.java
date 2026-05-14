package net.awyvrix.rtstructures.content.worldData;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.BitSet;

public final class StructureNbtCodec {

    private StructureNbtCodec() {}

    public static CompoundTag writeBitSet(BitSet set) {
        CompoundTag tag = new CompoundTag();
        tag.putLongArray("data", set.toLongArray());
        return tag;
    }

    public static BitSet readBitSet(Tag tag) {
        if (!(tag instanceof CompoundTag c)) {
            return new BitSet();
        }

        long[] arr = c.getLongArray("data");
        return BitSet.valueOf(arr);
    }

    public static CompoundTag writeBlockPos(BlockPos pos) {
        CompoundTag t = new CompoundTag();
        t.putInt("x", pos.getX());
        t.putInt("y", pos.getY());
        t.putInt("z", pos.getZ());
        return t;
    }

    public static BlockPos readBlockPos(CompoundTag t) {
        return new BlockPos(
                t.getInt("x"),
                t.getInt("y"),
                t.getInt("z")
        );
    }
}