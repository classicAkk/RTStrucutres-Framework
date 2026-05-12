package net.awyvrix.rtstructures.content.commands.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.awyvrix.rtstructures.api.*;
import net.awyvrix.rtstructures.content.commands.ExecuteMode;
import net.awyvrix.rtstructures.content.commands.CommandUtils;
import net.awyvrix.rtstructures.content.commands.StructureSuggestions;
import net.awyvrix.rtstructures.content.structureTool.*;
import net.awyvrix.rtstructures.core.StructureTemplate;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.file.Path;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class ExecuteCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return literal("execute")
                .then(argument("mode", StringArgumentType.word())
                        .suggests((ctx, builder) ->
                                StructureSuggestions.enumValues(
                                        ExecuteMode.class,
                                        builder
                                )
                        )
                        .executes(ctx -> {
                            ServerLevel level = ctx.getSource().getLevel();

                            Path worldDir = level.getServer().getWorldPath(LevelResource.ROOT);
                            String filename = CommandsState.filename;
                            String modeName = StringArgumentType.getString(ctx, "mode").toUpperCase();

                            BlockPos anchorPos = StructureToolState.placeAnchor;
                            StructureTemplate template = StructureCache.get(worldDir, filename);
                            PlacementAnchor anchorMode = PlacementAnchor.CUSTOM;

                            StructureInstance instance =
                                    new StructureInstance(
                                            level,
                                            template,
                                            anchorPos,
                                            anchorMode
                                    );

                            StructureManager.add(instance);

                            if (modeName.equals("INSTANT")) {
                                StructureLoadTool.executeLoad(
                                        level,
                                        worldDir,
                                        CommandsState.filename,
                                        StructureToolState.placeAnchor,
                                        CommandsState.anchor
                                );
                            } else {
                                instance.build(BuildType.valueOf(modeName), 2.0f);
                            }
                            CommandUtils.success(ctx, "Load executed: " + CommandsState.filename);

                            return 1;
                        })
                );
    }
}