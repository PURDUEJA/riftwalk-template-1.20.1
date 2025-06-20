package net.mop.riftwalk.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.mop.riftwalk.Riftwalk;

public class ModItems {
    public static final Item RIFTSHARD = registerItem("riftshard", new Item(new FabricItemSettings()));
    public static final Item RIFT_FRAGMENT = registerItem("rift_fragment", new Item(new FabricItemSettings()));

    private static void addItemsToIngredientTabItemGroup(FabricItemGroupEntries entries) {
        entries.add(RIFTSHARD);
        entries.add(RIFT_FRAGMENT);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Riftwalk.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Riftwalk.LOGGER.info("Registering Mod Items for " + Riftwalk.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientTabItemGroup);
    }
}
