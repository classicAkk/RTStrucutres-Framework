package net.awyvrix.rtstructures.content.commands.impl;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.awyvrix.rtstructures.content.tools.structureTool.CommandsState;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class ToggleBoxesCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return literal("toggle_boxes")
                .then(argument("toggle_boxes", BoolArgumentType.bool())
                        .executes(ctx -> {
                            boolean alwaysDisplay = BoolArgumentType.getBool(ctx, "toggle_boxes");
                            CommandsState.alwaysDisplay = alwaysDisplay;

                            ctx.getSource().sendSuccess(() -> Component.literal("Always Display: " + alwaysDisplay)
                                            .withStyle(alwaysDisplay ? ChatFormatting.GREEN : ChatFormatting.RED), false);

                            return 1;
                        }));
    }
}