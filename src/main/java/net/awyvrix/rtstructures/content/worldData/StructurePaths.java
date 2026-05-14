package net.awyvrix.rtstructures.content.worldData;

import java.nio.file.Path;

public final class StructurePaths {
    private StructurePaths() {}
    public static Path getGeneratedRoot(Path worldDir) {
        return worldDir.resolve("generated").resolve("rtstructures").resolve("moddata");
    }

    public static Path getPath(Path worldDir, String id) {
        String namespace = "rtstructures";
        String path = id;

        if (id.contains(":")) {
            String[] split = id.split(":", 2);
            namespace = split[0];
            path = split[1];
        }

        return getGeneratedRoot(worldDir).resolve(namespace).resolve(path + ".rtstructure");
    }
}