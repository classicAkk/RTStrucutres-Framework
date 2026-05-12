package net.awyvrix.rtstructures.core;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class StructureSerializer {
    public static void save(StructureTemplate template, Path path) throws IOException {
        try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(path))) {

            // Magic
            out.writeInt(0x52545331); // "RTS1"

            // Version
            out.writeInt(2);

            // Size
            out.writeShort(template.sizeX);
            out.writeShort(template.sizeY);
            out.writeShort(template.sizeZ);

            // Palette
            BlockPalette palette = template.palette;
            out.writeShort(palette.size());

            for (int i = 0; i < palette.size(); i++) {
                String name = BlockStateCodec.encode(palette.get(i));
                writeString(out, name);
            }

            // Blocks
            BlockEntry[] blocks = template.blocks;
            out.writeInt(blocks.length);

            for (BlockEntry b : blocks) {
                out.writeShort(b.x());
                out.writeShort(b.y());
                out.writeShort(b.z());
                out.writeShort(b.paletteId());
            }

            // Metadata
            out.writeBoolean(template.metadata.hasCustomAnchor);

            if (template.metadata.hasCustomAnchor) {
                out.writeShort(template.metadata.anchorX);
                out.writeShort(template.metadata.anchorY);
                out.writeShort(template.metadata.anchorZ);
            }
        }
    }

    private static void writeString(DataOutputStream out, String s) throws IOException {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        out.writeShort(bytes.length);
        out.write(bytes);
    }

    public final class BlockStateCodec {
        public static String encode(BlockState state) {
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
            StringBuilder sb = new StringBuilder(id.toString());

            if (!state.getValues().isEmpty()) {
                sb.append("[");
                boolean first = true;

                for (Map.Entry<Property<?>, Comparable<?>> entry : state.getValues().entrySet()) {
                    if (!first) sb.append(",");
                    Property<?> prop = entry.getKey();
                    Comparable<?> value = entry.getValue();

                    sb.append(prop.getName()).append("=").append(value.toString());
                    first = false;
                }
                sb.append("]");
            }

            return sb.toString();
        }

        public static BlockState decode(String str) {
            if (!str.contains("[")) {
                ResourceLocation rl = ResourceLocation.parse(str);
                Block block = BuiltInRegistries.BLOCK.get(rl);
                return block.defaultBlockState();
            }

            String idPart = str.substring(0, str.indexOf("["));
            String propsPart = str.substring(str.indexOf("[") + 1, str.length() - 1);

            ResourceLocation rl = ResourceLocation.parse(idPart);
            Block block = BuiltInRegistries.BLOCK.get(rl);

            BlockState state = block.defaultBlockState();
            Map<String, String> props = parseProps(propsPart);

            for (Map.Entry<String, String> e : props.entrySet()) {
                Property<?> prop = block.getStateDefinition().getProperty(e.getKey());

                if (prop != null) {
                    state = apply(state, prop, e.getValue());
                }
            }

            return state;
        }

        private static Map<String, String> parseProps(String input) {
            Map<String, String> map = new HashMap<>();

            if (input.isEmpty()) return map;
            String[] parts = input.split(",");

            for (String p : parts) {
                String[] kv = p.split("=");
                map.put(kv[0], kv[1]);
            }

            return map;
        }

        @SuppressWarnings("unchecked")
        private static <T extends Comparable<T>> BlockState apply(
                BlockState state,
                Property<T> prop,
                String value
        ) {

            Optional<T> parsed = prop.getValue(value);
            return parsed.map(t -> state.setValue(prop, t)).orElse(state);
        }
    }
}