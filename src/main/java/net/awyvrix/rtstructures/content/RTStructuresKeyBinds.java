package net.awyvrix.rtstructures.content;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class RTStructuresKeyBinds {
    public static final KeyMapping EXPAND_MODE = new KeyMapping(
            "key.rtstructures.structure_tool.expand_mode",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_CONTROL,
            "key.categories.rtstructures"
    );
    public static final KeyMapping NEXT_CATEGORY = new KeyMapping(
            "key.rtstructures.structure_tool.next_category",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_ALT,
            "key.categories.rtstructures"
    );
    public static final KeyMapping NEXT_SOCKET = new KeyMapping(
            "key.rtstructures.structure_tool.next_socket",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_CONTROL,
            "key.categories.rtstructures"
    );
    public static final KeyMapping DELETE_NODE = new KeyMapping(
            "key.rtstructures.structure_tool.delete_node",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_CONTROL,
            "key.categories.rtstructures"
    );
}
