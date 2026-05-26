package net.awyvrix.rtstructures.content.tools.linkTool.jsonResolver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.awyvrix.rtstructures.RTStructuresFramework;
import net.awyvrix.rtstructures.content.tools.linkTool.SocketCategory;
import net.awyvrix.rtstructures.content.tools.linkTool.SocketType;
import net.awyvrix.rtstructures.content.tools.util.ColorUtil;
import net.awyvrix.rtstructures.core.network.SocketRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.Reader;
import java.util.Map;

public final class SocketDataManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public SocketDataManager() {
        super(GSON, "socket_categories");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objects, ResourceManager resourceManager, ProfilerFiller profiler) {
        SocketRegistry.clear();

        loadCategories(resourceManager);
        loadSockets(resourceManager);
    }

    private void loadCategories(ResourceManager resourceManager) {
        Map<ResourceLocation, Resource> resources = resourceManager.listResources(
                "socket_categories",
                path -> path.getPath().endsWith(".json")
        );

        for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
            try (Reader reader = entry.getValue().openAsReader()) {
                CategoryJson json = GSON.fromJson(reader, CategoryJson.class);
                String path = entry.getKey().getPath();
                String id = path.substring(
                        path.lastIndexOf("/") + 1,
                        path.length() - 5
                );

                ResourceLocation icon = null;
                if (json.icon != null) {
                    icon = ResourceLocation.parse(json.icon);
                }

                SocketCategory category = new SocketCategory(id, json.display_name, ColorUtil.parseHex(json.color), icon);
                SocketRegistry.addCategory(category);
            }

            catch (Exception exception) {
                RTStructuresFramework.LOGGER.error("Failed to load socket category {}", entry.getKey(), exception);
            }
        }
    }

    private void loadSockets(ResourceManager resourceManager) {
        Map<ResourceLocation, Resource> resources =
                resourceManager.listResources("socket_types", path -> path.getPath().endsWith(".json"));

        for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
            try (Reader reader = entry.getValue().openAsReader()) {
                SocketJson json = GSON.fromJson(reader, SocketJson.class);
                String path = entry.getKey().getPath();

                String fileName = path.substring(path.lastIndexOf("/") + 1, path.length() - 5);
                ResourceLocation socketId = ResourceLocation.fromNamespaceAndPath(entry.getKey().getNamespace(), fileName);
                SocketCategory category = SocketRegistry.category(json.category);

                if (category == null) {
                    RTStructuresFramework.LOGGER.warn("Unknown socket category '{}' for socket '{}'", json.category, socketId);
                    continue;
                }

                int color = json.color != null ? ColorUtil.parseHex(json.color) : category.color();
                ResourceLocation icon = null;

                if (json.icon != null) {
                    icon = ResourceLocation.parse(json.icon);
                }

                SocketType socket = new SocketType(socketId, json.category, json.display_name, color, icon);
                SocketRegistry.addSocket(socket);
            }

            catch (Exception exception) {
                RTStructuresFramework.LOGGER.error("Failed to load socket type {}", entry.getKey(), exception);
            }
        }
    }
}