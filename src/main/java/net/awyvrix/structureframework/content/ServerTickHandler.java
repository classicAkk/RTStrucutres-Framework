package net.awyvrix.structureframework.content;

import net.awyvrix.structureframework.RTStructureFramework;
import net.awyvrix.structureframework.modders.StructureManager;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = RTStructureFramework.MOD_ID)
public final class ServerTickHandler {

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        for (ServerLevel level : event.getServer().getAllLevels()) {
            StructureManager.tick(level);
        }
    }
}