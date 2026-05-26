package net.awyvrix.rtstructures.content.datagen;

import net.awyvrix.rtstructures.RTStructuresFramework;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = RTStructuresFramework.MOD_ID)
public class DataGenEvent {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();

        generator.addProvider(event.includeServer(), new SocketTypeProvider(output));
        generator.addProvider(event.includeClient(), new SocketLangProvider(output));
    }
}
