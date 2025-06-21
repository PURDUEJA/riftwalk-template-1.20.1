package net.mop.riftwalk.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.mop.riftwalk.Riftwalk;
import net.mop.riftwalk.block.ModBlocks;

public class ModItemGroups {
    public static final ItemGroup RIFTWALK = Registry.register(Registries.ITEM_GROUP,
            new Identifier(Riftwalk.MOD_ID, "riftshard"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.riftshard"))
                    .icon(() -> new ItemStack(ModItems.RIFTSHARD)).entries((displayContext, entries) -> {
                        // add items to the group in here.
                        //Items
                        entries.add(ModItems.RIFTSHARD);
                        entries.add(ModItems.RIFT_FRAGMENT);
                        entries.add(ModItems.RIFTBLADE);

                        //Blocks
                        entries.add(ModBlocks.RIFT_ORE);

                    }).build());


    public static void registerItemGroups() {
        Riftwalk.LOGGER.info("Registering ModItemGroups for" + Riftwalk.MOD_ID);
    }
}
