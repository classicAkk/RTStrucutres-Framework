package net.awyvrix.rtstructures.content.worldData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.BitSet;

public final class BitSetCodec {

    public static CompoundTag encode(BitSet set) {
        CompoundTag tag = new CompoundTag();
        long[] arr = set.toLongArray();
        tag.putLongArray("data", arr);
        return tag;
    }

    public static BitSet decode(Tag tag) {
        if (!(tag instanceof CompoundTag ct)) return new BitSet();
        long[] arr = ct.getLongArray("data");
        return BitSet.valueOf(arr);
    }
}