package net.mop.riftwalk;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.mop.riftwalk.block.ModBlocks;
import net.mop.riftwalk.item.ModItemGroups;
import net.mop.riftwalk.item.ModItems;
import net.mop.riftwalk.networking.ModMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Riftwalk implements ModInitializer {
    public static final String MOD_ID = "riftwalk";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {

        LOGGER.info("I am onInitalize method");
        ModItemGroups.registerItemGroups();
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();


        // Register ore generation
        BiomeModifications.addFeature(
                // Select only End biomes
                BiomeSelectors.foundInTheEnd(),
                // Use the UNDERGROUND_ORES generation step
                GenerationStep.Feature.UNDERGROUND_ORES,
                // Your placed feature registry key
                RegistryKey.of(RegistryKeys.PLACED_FEATURE,
                        new Identifier("riftwalk", "rift_ore"))
        );

        ModMessages.registerC2SPackets();
    }
}