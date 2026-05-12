package net.awyvrix.rtstructures.content.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class StructureSuggestions {
    public static CompletableFuture<Suggestions> structures(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) {
        Level level = ctx.getSource().getLevel();
        Path worldDir = level.getServer().getWorldPath(LevelResource.ROOT);
        Path dir = worldDir.resolve("generated/rtstructures");

        try {
            if (Files.exists(dir)) {
                try (Stream<Path> stream = Files.list(dir)) {

                    stream.filter(path -> path.toString().endsWith(".rtstructure"))
                            .map(path -> path.getFileName().toString())
                            .map(name -> name.replace(".rtstructure", ""))
                            .forEach(builder::suggest);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.buildFuture();
    }

    public static <E extends Enum<E>>
    CompletableFuture<Suggestions> enumValues(Class<E> enumClass, SuggestionsBuilder builder) {
        for (E value : enumClass.getEnumConstants()) {
            builder.suggest(value.name().toLowerCase());
        }

        return builder.buildFuture();
    }
}