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

public class ExecLoadCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return literal("execload")
                .then(argument("filename", StringArgumentType.string())
                        .suggests(StructureSuggestions::structures)
                        .then(argument("anchor", StringArgumentType.word())
                                .suggests((ctx, builder) ->
                                        StructureSuggestions.enumValues(
                                                PlacementAnchor.class,
                                                builder
                                        )
                                )

                                .then(argument("mode", StringArgumentType.word())
                                        .suggests((ctx, builder) ->
                                                StructureSuggestions.enumValues(
                                                        ExecuteMode.class,
                                                        builder
                                                )
                                        )
                                        .executes(ctx -> {
                                            String filename = StringArgumentType.getString(ctx, "filename");
                                            PlacementAnchor anchor = CommandUtils.getEnum(ctx, "anchor", PlacementAnchor.class);
                                            String modeName = StringArgumentType.getString(ctx, "mode").toUpperCase();
                                            ServerLevel level = ctx.getSource().getLevel();

                                            CommandsState.filename = filename;
                                            CommandsState.anchor = anchor;

                                            Path worldDir = level.getServer().getWorldPath(LevelResource.ROOT);
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

                                            CommandUtils.success(ctx, "Successfully loaded structure: " + filename);
                                            CommandUtils.success(ctx, "Executing structure: " + filename);

                                            return 1;
                                        })
                                )
                        )
                );
    }
}