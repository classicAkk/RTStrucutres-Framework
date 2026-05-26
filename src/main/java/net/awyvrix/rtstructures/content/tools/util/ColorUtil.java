package net.awyvrix.rtstructures.content.tools.util;

public final class ColorUtil {

    private ColorUtil() {}

    public static int parseHex(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        return Integer.parseInt(hex, 16);
    }
}