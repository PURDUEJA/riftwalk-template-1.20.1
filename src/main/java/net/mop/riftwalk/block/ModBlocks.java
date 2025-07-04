package net.mop.riftwalk.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.mop.riftwalk.Riftwalk;

public class ModBlocks {
    public static final Block RIFT_ORE = registerBlock("rift_ore",
            new RiftOreBlock(FabricBlockSettings.copyOf(Blocks.IRON_ORE)
                    .resistance(1200.0f)
                    .luminance(state -> state.get(RiftOreBlock.LIT) ? 9 : 0) // Add light when lit
                    .sounds(BlockSoundGroup.STONE)
                    .requiresTool()
                    .strength(4.0f)
            ));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(Riftwalk.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(Riftwalk.MOD_ID, name), new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        Riftwalk.LOGGER.info("Registering ModBlocks for " + Riftwalk.MOD_ID);
    }
}