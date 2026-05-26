package net.awyvrix.rtstructures.content.tools.linkTool;

import net.awyvrix.rtstructures.core.network.SocketRegistry;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LinkToolClientState {
    public static final LinkToolClientState INSTANCE = new LinkToolClientState();
    private final List<SocketCategory> categories = new ArrayList<>();
    private final Map<String, ResourceLocation> lastSockets = new HashMap<>();

    private int categoryIndex = 0;

    private SocketType selectedSocket;

    private LinkToolClientState() {
    }

    public void reload() {
        categories.clear();
        categories.addAll(SocketRegistry.categories());

        if (!categories.isEmpty()) {
            SocketCategory category = categories.getFirst();
            List<SocketType> sockets = SocketRegistry.socketsInCategory(category.id());

            if (!sockets.isEmpty()) {
                selectedSocket = sockets.getFirst();
            }
        }
    }

    public SocketCategory currentCategory() {
        if (categories.isEmpty()) return null;
        return categories.get(categoryIndex);
    }

    public SocketType selectedSocket() {
        return selectedSocket;
    }

    public void nextCategory(int direction) {
        if (categories.isEmpty()) return;
        categoryIndex += direction;

        if (categoryIndex < 0) {
            categoryIndex = categories.size() - 1;
        }

        if (categoryIndex >= categories.size()) {
            categoryIndex = 0;
        }

        SocketCategory category = currentCategory();
        ResourceLocation remembered = lastSockets.get(category.id());
        List<SocketType> sockets = SocketRegistry.socketsInCategory(category.id());

        if (sockets.isEmpty()) return;
        if (remembered != null) {
            SocketType socket = SocketRegistry.socket(remembered);

            if (socket != null) {
                selectedSocket = socket;
                return;
            }
        }

        selectedSocket = sockets.getFirst();
    }

    public void nextSocket(int direction) {
        SocketCategory category = currentCategory();

        if (category == null) return;
        List<SocketType> sockets = SocketRegistry.socketsInCategory(category.id());

        if (sockets.isEmpty()) return;
        int index = sockets.indexOf(selectedSocket);
        index += direction;

        if (index < 0) {
            index = sockets.size() - 1;
        }

        if (index >= sockets.size()) {
            index = 0;
        }

        selectedSocket = sockets.get(index);
        lastSockets.put(category.id(), selectedSocket.id());
    }

    public void select(SocketType socket) {
        selectedSocket = socket;
        SocketCategory category = SocketRegistry.category(socket.categoryId());

        if (category == null) return;
        categoryIndex = categories.indexOf(category);
        lastSockets.put(category.id(), socket.id());
    }
}