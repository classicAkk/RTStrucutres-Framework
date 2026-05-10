package net.awyvrix.structureframework.core;

import net.minecraft.world.level.block.state.BlockState;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class StructureDeserializer {
    public static StructureTemplate load(Path path) throws IOException {
        try (DataInputStream in = new DataInputStream(Files.newInputStream(path))) {

            // Magic
            int magic = in.readInt();
            if (magic != 0x52545331) {
                throw new IOException("Invalid format");
            }

            int version = in.readInt();
            if (version != 2) {
                throw new IOException("Unsupported version: " + version);
            }

            // Size
            int sizeX = in.readShort();
            int sizeY = in.readShort();
            int sizeZ = in.readShort();

            // Palette
            int paletteSize = in.readShort();
            List<BlockState> palette = new ArrayList<>(paletteSize);

            for (int i = 0; i < paletteSize; i++) {
                String blockName = readString(in);
                BlockState state = StructureSerializer.BlockStateCodec.decode(blockName);
                palette.add(state);
            }
            BlockPalette blockPalette = new BlockPalette(palette);

            // Blocks
            int blockCount = in.readInt();
            BlockEntry[] blocks = new BlockEntry[blockCount];

            for (int i = 0; i < blockCount; i++) {
                short x = in.readShort();
                short y = in.readShort();
                short z = in.readShort();
                short pid = in.readShort();

                blocks[i] = new BlockEntry(x, y, z, pid);
            }

            // Metadata
            boolean hasAnchor = in.readBoolean();
            short ax = 0;
            short ay = 0;
            short az = 0;

            if (hasAnchor) {
                ax = in.readShort();
                ay = in.readShort();
                az = in.readShort();
            }

            StructureMetadata metadata = new StructureMetadata(hasAnchor, ax, ay, az);
            return new StructureTemplate(
                    sizeX, sizeY, sizeZ,
                    blockPalette,
                    blocks,
                    metadata
            );
        }
    }

    private static String readString(DataInputStream in) throws IOException {
        int len = in.readShort();
        byte[] bytes = in.readNBytes(len);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}