package net.awyvrix.rtstructures;


import com.mojang.logging.LogUtils;
import net.awyvrix.rtstructures.api.StructureInstance;
import net.awyvrix.rtstructures.api.StructureManager;
import net.awyvrix.rtstructures.content.commands.RTStructuresCommands;
import net.awyvrix.rtstructures.content.init.ItemInit;
import net.awyvrix.rtstructures.content.init.StructuresInit;
import net.awyvrix.rtstructures.content.tools.linkTool.jsonResolver.SocketDataManager;
import net.awyvrix.rtstructures.content.tools.structureTool.StructureCache;
import net.awyvrix.rtstructures.content.worldData.StructureInstanceData;
import net.awyvrix.rtstructures.content.worldData.StructureWorldData;
import net.awyvrix.rtstructures.core.StructureBootstrap;
import net.awyvrix.rtstructures.core.StructureTemplate;
import net.awyvrix.rtstructures.registries.RTSRegistryEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import org.slf4j.Logger;

import java.io.IOException;

@Mod(RTStructuresFramework.MOD_ID)
public class RTStructuresFramework {
    public static final String MOD_ID = "rtstructures";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RTStructuresFramework(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(RTSRegistryEvents::register);
        NeoForge.EVENT_BUS.register(RTStructuresFramework.class);

        ItemInit.register(modEventBus);
        StructuresInit.register(modEventBus);
        NeoForge.EVENT_BUS.register(RTStructuresCommands.class);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {}

    @SubscribeEvent
    public static void onReload(AddReloadListenerEvent event) {
        event.addListener(new SocketDataManager());
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.OP_BLOCKS) {
            event.accept(ItemInit.STRUCTURE_TOOL);
            event.accept(ItemInit.LINK_TOOL);
        }
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {}

    @SubscribeEvent
    public static void serverStartedEvent(ServerStartedEvent event) {
        try {
            StructureBootstrap.copyAll(event.getServer(), event.getServer().getWorldPath(LevelResource.ROOT));
        } catch (IOException e) {
            throw new RuntimeException("Could not load mod rtstructures: " + e);
        }
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        var server = event.getServer();
        StructureCache.preloadAll(server.getWorldPath(LevelResource.ROOT));
        StructureWorldData data = StructureWorldData.get(server.overworld());
        int loaded = 0;

        for (StructureInstanceData saved : data.getAll().values()) {
            String raw = saved.dimension();
            if (raw.startsWith("ResourceKey[")) {
                raw = raw.substring(raw.indexOf("/") + 2, raw.length() - 1);
            }
            ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(raw));
            ServerLevel level = server.getLevel(key);

            if (level == null) continue;
            StructureTemplate template = StructureCache.load(level.getServer().getWorldPath(LevelResource.ROOT), saved.structureId());
            StructureInstance instance = new StructureInstance(
                    saved.type(),
                    level,
                    template,
                    saved.anchor(),
                    saved.anchorMode()
            );

            instance.restoreState(
                    saved.state(),
                    saved.progress(),
                    saved.removed(),
                    saved.ticksAlive()
            );

            StructureManager.add(instance);
            loaded++;
        }

        System.out.println("[RTStructuresFramework] Loaded structures: " + loaded);
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        for (ServerLevel level : event.getServer().getAllLevels()) {
            StructureWorldData data = StructureWorldData.get(level);
            data.setDirty();
        }

        StructureCache.clear();
    }
}