package net.awyvrix.rtstructures;


import com.mojang.logging.LogUtils;
import net.awyvrix.rtstructures.content.commands.RTStructuresCommands;
import net.awyvrix.rtstructures.content.init.ItemInit;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(RTStructureFramework.MOD_ID)
public class RTStructureFramework {
    public static final String MOD_ID = "rtstructures_framework";
    private static final Logger LOGGER = LogUtils.getLogger();

    public RTStructureFramework(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        ItemInit.register(modEventBus);
        NeoForge.EVENT_BUS.register(RTStructuresCommands.class);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {}

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.OP_BLOCKS) {
            event.accept(ItemInit.STRUCTURE_TOOL);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}
}