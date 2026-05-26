package net.awyvrix.rtstructures.content.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public final class SocketLangProvider extends LanguageProvider {
    public SocketLangProvider(PackOutput output) {
        super(output, "rtstructures", "en_us");
    }

    @Override
    protected void addTranslations() {
        for (SocketTypeList socket : SocketTypeList.values()) {
            add("socket." + socket.id(), toDisplayName(socket.id()));
        }
    }

    private static String toDisplayName(String id) {
        String[] split = id.split("_");
        StringBuilder builder = new StringBuilder();

        for (String part : split) {
            if (!builder.isEmpty()) builder.append(" ");

            builder.append(Character.toUpperCase(part.charAt(0)));
            builder.append(part.substring(1).toLowerCase());
        }

        return builder.toString();
    }
}