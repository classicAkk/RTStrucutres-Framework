package net.awyvrix.structureframework.content.init;

import net.awyvrix.structureframework.RTStructureFramework;
import net.awyvrix.structureframework.content.structureTool.StructureToolItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemInit {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RTStructureFramework.MOD_ID);

    public static final DeferredItem<Item> STRUCTURE_TOOL = ITEMS.register("structure_tool",
            () -> new StructureToolItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}