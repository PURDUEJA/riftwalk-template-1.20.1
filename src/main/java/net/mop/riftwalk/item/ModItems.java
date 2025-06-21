package net.mop.riftwalk.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.mop.riftwalk.Riftwalk;

public class ModItems {
    public static final Item RIFTSHARD = registerItem("riftshard", new Item(new FabricItemSettings()));
    public static final Item RIFT_FRAGMENT = registerItem("rift_fragment", new Item(new FabricItemSettings()));

    public static final Item RIFTBLADE = registerItem("riftblade",
            new RiftbladeItem(ModToolMaterials.RIFT, 4, -2.4f, new FabricItemSettings()));


        // Existing items...

        // Rift Armor
        public static final Item RIFT_HELMET = registerItem("rift_helmet",
                new RiftArmorItem(ModArmorMaterials.RIFT, ArmorItem.Type.HELMET, new FabricItemSettings()));

        public static final Item RIFT_CHESTPLATE = registerItem("rift_chestplate",
                new RiftArmorItem(ModArmorMaterials.RIFT, ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));

        public static final Item RIFT_LEGGINGS = registerItem("rift_leggings",
                new RiftArmorItem(ModArmorMaterials.RIFT, ArmorItem.Type.LEGGINGS, new FabricItemSettings()));

        public static final Item RIFT_BOOTS = registerItem("rift_boots",
                new RiftArmorItem(ModArmorMaterials.RIFT, ArmorItem.Type.BOOTS, new FabricItemSettings()));


    private static void addItemsToIngredientTabItemGroup(FabricItemGroupEntries entries) {
        entries.add(RIFTSHARD);
        entries.add(RIFT_FRAGMENT);
        entries.add(RIFTBLADE);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Riftwalk.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Riftwalk.LOGGER.info("Registering Mod Items for " + Riftwalk.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientTabItemGroup);
    }
}
