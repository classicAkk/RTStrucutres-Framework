package net.awyvrix.rtstructures.content;

import net.awyvrix.rtstructures.RTStructuresFramework;
import net.awyvrix.rtstructures.api.StructureManager;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = RTStructuresFramework.MOD_ID)
public final class ServerTickHandler {

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        for (ServerLevel level : event.getServer().getAllLevels()) {
            StructureManager.tick(level);
        }
    }
}