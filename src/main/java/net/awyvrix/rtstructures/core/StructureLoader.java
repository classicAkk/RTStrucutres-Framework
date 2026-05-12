package net.awyvrix.rtstructures.core;

import java.io.IOException;
import java.nio.file.Path;

public final class StructureLoader {
    public static StructureTemplate load(Path worldDir, String filename) throws IOException {
        Path path = worldDir.resolve("generated/rtstructures/" + filename + ".rtstructure");

        return StructureDeserializer.load(path);
    }
}