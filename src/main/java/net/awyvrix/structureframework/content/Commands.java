package net.awyvrix.structureframework.content;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.awyvrix.structureframework.content.renderer.StructureToolRenderer;
import net.awyvrix.structureframework.content.structureTool.*;
import net.awyvrix.structureframework.core.StructureMetadata;
import net.awyvrix.structureframework.core.StructureTemplate;
import net.awyvrix.structureframework.modders.*;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.bus.api.SubscribeEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Commands {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(net.minecraft.commands.Commands.literal("rtstrucutres")
                .requires(src -> src.hasPermission(2))
                .then(net.minecraft.commands.Commands.literal("save")
                        .then(net.minecraft.commands.Commands.argument("filename", StringArgumentType.string())
                                .executes(ctx -> {
                                    String filename = StringArgumentType.getString(ctx, "filename");
                                    Level level = ctx.getSource().getLevel();

                                    ctx.getSource().sendSuccess(() -> Component.literal("Saved: " + filename)
                                            .withStyle(ChatFormatting.GREEN), false);

                                    Path worldDir = level.getServer().getWorldPath(LevelResource.ROOT);
                                    Path path = worldDir.resolve("generated/rtstructures/" + filename + ".rtstructure");

                                    Component msg = Component.literal("Path: " + path)
                                            .setStyle(Style.EMPTY
                                                    .withColor(ChatFormatting.GOLD)
                                                    .withClickEvent(new ClickEvent(
                                                            ClickEvent.Action.COPY_TO_CLIPBOARD,
                                                            path.toString()
                                                    ))
                                                    .withHoverEvent(new net.minecraft.network.chat.HoverEvent(
                                                            net.minecraft.network.chat.HoverEvent.Action.SHOW_TEXT,
                                                            Component.literal("Click to copy path")
                                                    ))
                                            );

                                    ctx.getSource().sendSuccess(() -> msg, false);

                                    StructureSaveTool.executeSave(level, filename);
                                    return 1;
                                })
                        )
                )
                .then(net.minecraft.commands.Commands.literal("load")
                        .then(net.minecraft.commands.Commands.argument("filename", StringArgumentType.string())
                                .suggests((ctx, builder) -> {
                                    Level level = ctx.getSource().getLevel();
                                    Path worldDir = level.getServer().getWorldPath(LevelResource.ROOT);
                                    Path structuresDir = worldDir.resolve("generated/rtstructures");
                                    try {
                                        if (Files.exists(structuresDir)) {
                                            try (Stream<Path> stream = Files.list(structuresDir)) {
                                                stream.filter(path -> path.toString().endsWith(".rtstructure"))
                                                        .forEach(path -> {
                                                            String fileName = path.getFileName().toString();
                                                            fileName = fileName.substring(0, fileName.length() - ".rtstructure".length());
                                                            builder.suggest(fileName);
                                                        });
                                            }
                                        }

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    return builder.buildFuture();
                                })
                                .then(net.minecraft.commands.Commands.argument("anchor", StringArgumentType.word())
                                        .suggests((ctx, builder) -> {
                                            for (PlacementAnchor value : PlacementAnchor.values()) {
                                                builder.suggest(value.name().toLowerCase());
                                            }
                                            return builder.buildFuture();
                                        })

                                        .executes(ctx -> {
                                            String filename = StringArgumentType.getString(ctx, "filename");
                                            String anchorName = StringArgumentType.getString(ctx, "anchor").toUpperCase();

                                            PlacementAnchor anchor = PlacementAnchor.valueOf(anchorName);
                                            Component msg = Component.literal("Successfully loaded structure: " + filename)
                                                    .setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN));
                                            Component msg2 = Component.literal("Execute loading: " + filename)
                                                    .setStyle(Style.EMPTY.withColor(ChatFormatting.RED));

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

                                            ctx.getSource().sendSuccess(() -> msg, false);
                                            ctx.getSource().sendSuccess(() -> msg2, false);
                                            return 1;
                                        })
                                )
                        )
                )
                .then(net.minecraft.commands.Commands.literal("always_display_boxes")
                        .then(net.minecraft.commands.Commands.argument("always_display_boxes", BoolArgumentType.bool())
                                .executes(ctx -> {
                                    boolean alwaysDisplay = BoolArgumentType.getBool(ctx, "always_display_boxes");

                                    if (alwaysDisplay) {
                                        ctx.getSource().sendSuccess(() ->
                                                Component.literal("Always Display: true").withStyle(ChatFormatting.GREEN), false);
                                    } else {
                                        ctx.getSource().sendSuccess(() ->
                                                Component.literal("Always Display: false").withStyle(ChatFormatting.RED), false);
                                    }

                                    CommandsState.alwaysDisplay = alwaysDisplay;
                                    return 1;
                                })
                        )
                )
                .then(net.minecraft.commands.Commands.literal("execute")
                        .executes(ctx -> {
                            Level level = ctx.getSource().getLevel();
                            Path worldDir = level.getServer().getWorldPath(LevelResource.ROOT);

                            Component msg = Component.literal("Load executed: " + CommandsState.filename).setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN));
                            ctx.getSource().sendSuccess(() -> msg, false);
                            StructureLoadTool.executeLoad(level, worldDir, CommandsState.filename, StructureToolState.placeAnchor, CommandsState.anchor);

                            return 1;
                        })
                )
                .then(net.minecraft.commands.Commands.literal("debug")
                        .executes(ctx -> {
                            ServerLevel level = ctx.getSource().getLevel();
                            String filename = "house";

                            Path worldDir = level.getServer().getWorldPath(LevelResource.ROOT);
                            StructureTemplate template = StructureCache.get(worldDir, filename);

                            BlockPos anchorPos = StructureToolState.placeAnchor;
                            PlacementAnchor anchorMode = PlacementAnchor.CUSTOM;
                            StructureInstance instance = new StructureInstance(level, template, anchorPos, anchorMode);


                            StructureManager.add(instance);
                            instance.build(BuildType.FAST, 2.0f);
                            return 1;
                        })
                )
        );
    }

    public static final class AnchorResolver {
        public static BlockPos getAnchorOffset(StructureTemplate template, PlacementAnchor anchor) {
            int x = StructureToolState.placeAnchor.getX();
            int y = StructureToolState.placeAnchor.getY();
            int z = StructureToolState.placeAnchor.getZ();
            return switch (anchor) {
                case CORNER -> new BlockPos(x, y, z);
                case CENTER -> new BlockPos(x-template.sizeX / 2, y-template.sizeY / 2, z-template.sizeZ / 2);
                case CUSTOM -> {
                    StructureMetadata meta = template.metadata;
                    if (!meta.hasCustomAnchor) {
                        throw new IllegalStateException("Structure has no custom anchor");
                    }

                    yield new BlockPos(x-meta.anchorX, y-meta.anchorY, z-meta.anchorZ);
                }
            };
        }
    }
}