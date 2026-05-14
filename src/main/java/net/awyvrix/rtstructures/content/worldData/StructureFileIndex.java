package net.awyvrix.rtstructures.content.worldData;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public final class StructureFileIndex {
    private StructureFileIndex() {}

    public static List<String> getAllIds(Path worldDir) {
        Path root = StructurePaths.getGeneratedRoot(worldDir);
        if (!Files.exists(root)) return List.of();

        try (Stream<Path> stream = Files.walk(root)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".rtstructure"))
                    .map(root::relativize)
                    .map(StructureFileIndex::toStructureId)
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException("Failed to index structures", e);
        }
    }

    private static String toStructureId(Path relative) {
        String namespace = relative.getName(0).toString();
        Path sub = relative.subpath(1, relative.getNameCount());
        String path = sub.toString().replace('\\', '/').replace(".rtstructure", "");

        return namespace + ":" + path;
    }
}