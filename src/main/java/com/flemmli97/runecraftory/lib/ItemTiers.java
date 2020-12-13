package com.flemmli97.runecraftory.lib;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;

public class ItemTiers {

    public static final IItemTier tier = new IItemTier() {
        @Override
        public int getMaxUses() {
            return 0;
        }

        @Override
        public float getEfficiency() {
            return 0;
        }

        @Override
        public float getAttackDamage() {
            return 0;
        }

        @Override
        public int getHarvestLevel() {
            return 0;
        }

        @Override
        public int getEnchantability() {
            return 0;
        }

        @Override
        public Ingredient getRepairMaterial() {
            return Ingredient.EMPTY;
        }
    };
}
