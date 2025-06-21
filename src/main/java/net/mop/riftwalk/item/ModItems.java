package net.mop.riftwalk.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.mop.riftwalk.Riftwalk;

import java.util.List;

public class ModItems {
    public static final Item RIFTSHARD = registerItem("riftshard", new Item(new FabricItemSettings()));
    public static final Item RIFT_FRAGMENT = registerItem("rift_fragment", new Item(new FabricItemSettings()));

    public static final Item RIFTBLADE = registerItem("riftblade",
            new RiftbladeItem(ModToolMaterials.RIFT, 4, -2.4f, new FabricItemSettings()));



        // Rift Armor
        public static final Item RIFT_HELMET = registerItem("rift_helmet",
                new RiftArmorItem(ModArmorMaterials.RIFT, ArmorItem.Type.HELMET, new FabricItemSettings()));

        public static final Item RIFT_CHESTPLATE = registerItem("rift_chestplate",
                new RiftArmorItem(ModArmorMaterials.RIFT, ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));

        public static final Item RIFT_LEGGINGS = registerItem("rift_leggings",
                new RiftArmorItem(ModArmorMaterials.RIFT, ArmorItem.Type.LEGGINGS, new FabricItemSettings()));

        public static final Item RIFT_BOOTS = registerItem("rift_boots",
                new RiftArmorItem(ModArmorMaterials.RIFT, ArmorItem.Type.BOOTS, new FabricItemSettings()));


    public static final Item RIFT_UPGRADE_SMITHING_TEMPLATE = registerItem("rift_upgrade_smithing_template",
            new SmithingTemplateItem(
                    Text.translatable("item.riftwalk.rift_upgrade_smithing_template.applies_to").formatted(Formatting.BLUE),
                    Text.translatable("item.riftwalk.rift_upgrade_smithing_template.ingredients").formatted(Formatting.BLUE),
                    Text.translatable("item.riftwalk.rift_upgrade_smithing_template.title").formatted(Formatting.GRAY),
                    Text.translatable("item.riftwalk.rift_upgrade_smithing_template.base_slot_description"),
                    Text.translatable("item.riftwalk.rift_upgrade_smithing_template.additions_slot_description"),
                    getArmorBaseSlotTextures(),
                    getRiftIngredientSlotTextures()
            ));

    // Helper methods for armor-only smithing template
    private static List<Identifier> getArmorBaseSlotTextures() {
        return List.of(
                new Identifier("item/empty_armor_slot_helmet"),
                new Identifier("item/empty_armor_slot_chestplate"),
                new Identifier("item/empty_armor_slot_leggings"),
                new Identifier("item/empty_armor_slot_boots")
        );
    }

    private static List<Identifier> getRiftIngredientSlotTextures() {
        return List.of(new Identifier("riftwalk:item/empty_slot_riftshard"));
    }


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
