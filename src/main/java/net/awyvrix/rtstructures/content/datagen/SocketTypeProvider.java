package net.awyvrix.rtstructures.content.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public final class SocketTypeProvider implements DataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final PackOutput output;

    public SocketTypeProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Path folder = output.getOutputFolder().resolve("data/rtstructures/socket_types");
        CompletableFuture<?>[] futures = new CompletableFuture<?>[SocketTypeList.values().length];
        int index = 0;

        for (SocketTypeList socket : SocketTypeList.values()) {
            JsonObject json = new JsonObject();

            json.addProperty("category", socket.category());
            json.addProperty("display_name", socket.displayName());
            json.addProperty("icon", socket.icon());

            Path path = folder.resolve(socket.id() + ".json");
            futures[index++] = DataProvider.saveStable(cache, json, path);
        }

        return CompletableFuture.allOf(futures);
    }

    @Override
    public String getName() {
        return "Socket Types";
    }
}