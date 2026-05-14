package net.awyvrix.rtstructures.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;

import java.io.*;
import java.nio.file.*;
import java.util.Map;

public final class StructureBootstrap {
    public static void copyAll(MinecraftServer server, Path worldDir) throws IOException {
        Path targetRoot = worldDir.resolve("generated/rtstructures/moddata");
        Files.createDirectories(targetRoot);

        var resources = server.getResourceManager().listResources("rtstructures", rl -> rl.getPath().endsWith(".rtstructure"));

        for (var entry : resources.entrySet()) {
            ResourceLocation id = entry.getKey();
            Resource resource = entry.getValue();

            Path resourcePath = Paths.get(id.getPath());
            Path relativePath = resourcePath.subpath(1, resourcePath.getNameCount());

            // generated/rtstructures/moddata/<mod>/...
            Path targetFile = targetRoot.resolve(id.getNamespace()).resolve(relativePath);
            Files.createDirectories(targetFile.getParent());

            try (InputStream in = resource.open()) {
                Files.copy(in, targetFile, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
}