package net.awyvrix.rtstructures.content.commands.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.awyvrix.rtstructures.content.tools.structureTool.StructureSaveTool;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.file.Path;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class SaveCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return literal("save")
                .then(argument("filename",
                        StringArgumentType.string())
                        .executes(ctx -> {
                            String filename = StringArgumentType.getString(ctx, "filename");

                            Level level = ctx.getSource().getLevel();
                            Path worldDir = level.getServer().getWorldPath(LevelResource.ROOT);

                            Path path = worldDir.resolve("generated/rtstructures/" + filename + ".rtstructure");
                            StructureSaveTool.executeSave(level, filename);

                            ctx.getSource().sendSuccess(() -> Component.literal("Saved: " + filename).withStyle(ChatFormatting.GREEN), false);
                            Component msg = Component.literal("Path: " + path)
                                    .setStyle(Style.EMPTY
                                            .withColor(ChatFormatting.GOLD)
                                            .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, path.toString()))
                                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to copy path")))
                                    );
                            ctx.getSource().sendSuccess(() -> msg, false);

                            return 1;
                        }));
    }
}