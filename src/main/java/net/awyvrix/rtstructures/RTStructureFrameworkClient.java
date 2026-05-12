package net.awyvrix.rtstructures;

import net.awyvrix.rtstructures.content.RTStructuresKeyBinds;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@EventBusSubscriber(modid = RTStructureFramework.MOD_ID, value = Dist.CLIENT)
public class RTStructureFrameworkClient {

    public RTStructureFrameworkClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {}

    @EventBusSubscriber(modid = RTStructureFramework.MOD_ID, value = Dist.CLIENT)
    public class KeybindRegistry {

        @SubscribeEvent
        public static void registerKeys(RegisterKeyMappingsEvent event) {
            event.register(RTStructuresKeyBinds.EXPAND_MODE);
        }
    }
}