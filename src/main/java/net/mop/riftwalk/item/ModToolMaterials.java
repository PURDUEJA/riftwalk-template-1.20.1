package net.mop.riftwalk.item;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class ModToolMaterials {
    public static final ToolMaterial RIFT = new ToolMaterial() {
        @Override
        public int getDurability() {
            return 2500; // Higher than netherite (2031)
        }

        @Override
        public float getMiningSpeedMultiplier() {
            return 11.0f; // Netherite is 9.0f
        }

        @Override
        public float getAttackDamage() {
            return 5.0f; // Netherite is 4.0f
        }

        @Override
        public int getMiningLevel() {
            return 5; // Netherite is 4
        }

        @Override
        public int getEnchantability() {
            return 20; // Netherite is 15
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.ofItems(ModItems.RIFTSHARD);
        }
    };
}