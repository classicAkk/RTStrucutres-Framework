package net.awyvrix.rtstructures.content.commands.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.awyvrix.rtstructures.content.commands.AnchorResolver;
import net.awyvrix.rtstructures.content.commands.CommandUtils;
import net.awyvrix.rtstructures.content.commands.StructureSuggestions;
import net.awyvrix.rtstructures.content.renderer.StructureToolRenderer;
import net.awyvrix.rtstructures.content.structureTool.CommandsState;
import net.awyvrix.rtstructures.content.structureTool.PlacementAnchor;
import net.awyvrix.rtstructures.content.structureTool.StructureCache;
import net.awyvrix.rtstructures.core.StructureTemplate;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.file.Path;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class LoadCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return literal("load")
                .then(argument("filename", StringArgumentType.string())
                        .suggests(StructureSuggestions::structures)
                        .then(argument("anchor", StringArgumentType.word())
                                .suggests((ctx, builder) ->
                                        StructureSuggestions.enumValues(PlacementAnchor.class, builder)
                                )

                                .executes(ctx -> {
                                    String filename = StringArgumentType.getString(ctx, "filename");
                                    PlacementAnchor anchor = CommandUtils.getEnum(ctx, "anchor", PlacementAnchor.class);
                                    Level level = ctx.getSource().getLevel();
                                    Path worldDir = level.getServer().getWorldPath(LevelResource.ROOT);

                                    CommandsState.filename = filename;
                                    CommandsState.anchor = anchor;

                                    StructureTemplate template = StructureCache.get(worldDir, filename);
                                    BlockPos anchorOffset = AnchorResolver.getAnchorOffset(template, anchor);

                                    StructureToolRenderer.clearPreviewBox();
                                    StructureToolRenderer.setPreviewBox(
                                            anchorOffset,
                                            template.sizeX,
                                            template.sizeY,
                                            template.sizeZ
                                    );

                                    CommandUtils.success(ctx, "Successfully loaded structure: " + filename);

                                    return 1;
                                })
                        )
                );
    }
}