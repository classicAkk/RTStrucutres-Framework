package net.awyvrix.rtstructures.core.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.awyvrix.rtstructures.content.tools.linkTool.SocketCategory;
import net.awyvrix.rtstructures.content.tools.linkTool.SocketType;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class SocketRegistry {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<String, SocketCategory> CATEGORIES = new LinkedHashMap<>();
    private static final Map<ResourceLocation, SocketType> SOCKETS = new LinkedHashMap<>();

    private SocketRegistry() {}

    public static void clear() {
        CATEGORIES.clear();
        SOCKETS.clear();
    }

    public static void addCategory(SocketCategory category) {
        CATEGORIES.put(category.id(), category);
    }

    public static void addSocket(SocketType socket) {
        SOCKETS.put(socket.id(), socket);
    }

    public static Collection<SocketCategory> categories() {
        return CATEGORIES.values();
    }

    public static Collection<SocketType> sockets() {
        return SOCKETS.values();
    }

    @Nullable
    public static SocketCategory category(String id) {
        return CATEGORIES.get(id);
    }

    @Nullable
    public static SocketType socket(ResourceLocation id) {
        return SOCKETS.get(id);
    }

    public static List<SocketType> socketsInCategory(String category) {
        return SOCKETS.values()
                .stream()
                .filter(socket -> socket.categoryId().equals(category))
                .toList();
    }
}