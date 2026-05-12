package net.awyvrix.rtstructures.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.awyvrix.rtstructures.content.commands.impl.*;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import static net.minecraft.commands.Commands.literal;

public class RTStructuresCommands {

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
                literal("rtstrucutres")
                        .requires(src -> src.hasPermission(2))

                        .then(SaveCommand.register())
                        .then(LoadCommand.register())
                        .then(ExecuteCommand.register())
                        .then(ExecLoadCommand.register())
                        .then(ToggleBoxesCommand.register())
        );
    }
}