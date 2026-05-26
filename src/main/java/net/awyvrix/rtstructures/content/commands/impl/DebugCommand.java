package net.awyvrix.rtstructures.content.commands.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.awyvrix.rtstructures.api.StructureInstance;
import net.awyvrix.rtstructures.api.StructureManager;
import net.awyvrix.rtstructures.content.commands.StructureSuggestions;
import net.awyvrix.rtstructures.content.init.StructuresInit;
import net.awyvrix.rtstructures.content.tools.structureTool.*;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class DebugCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return literal("debug")
                .then(argument("mode", StringArgumentType.word())
                        .suggests((ctx, builder) ->
                                StructureSuggestions.enumValues(DebugMode.class, builder)
                        )
                        .executes(ctx -> {
                            String modeName = StringArgumentType.getString(ctx, "mode").toUpperCase();
                            ServerLevel level = ctx.getSource().getLevel();
                            BlockPos anchorPos = StructureToolState.placeAnchor;

                            if (modeName.equals("SIMPLE_HOUSE")) {
                                StructureInstance instance  = StructuresInit.SIMPLE_HOUSE.get().create(level, anchorPos);
                                StructureManager.add(instance);

                                instance.build();
                            }
                            if (modeName.equals("AIRFIELD")) {
                                StructureInstance instance = StructuresInit.AIRFIELD.get().create(level, anchorPos);
                                StructureManager.add(instance);

                                instance.build();
                            }
                            if (modeName.equals("CRYSTAL")) {
                                StructureInstance instance = StructuresInit.CRYSTAL.get().create(level, anchorPos);
                                StructureManager.add(instance);

                                instance.build();
                            }
                            if (modeName.equals("PROTECTED_STRUCTURE")) {
                                StructureInstance instance = StructuresInit.PROTECTED_STRUCTURE.get().create(level, anchorPos);
                                StructureManager.add(instance);

                                instance.build();
                            }

                            return 1;
                        })
                );
    }
}