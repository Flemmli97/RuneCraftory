package com.flemmli97.runecraftory.common.crafting;

import com.flemmli97.runecraftory.common.registry.ModCrafting;
import com.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class ArmorRecipe extends SextupleRecipe{

    public ArmorRecipe(ResourceLocation id, String group, int level, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(id, group, level, result, ingredients);
    }

    @Override
    public IRecipeType<? extends SextupleRecipe> getType() {
        return ModCrafting.ARMOR;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModCrafting.ARMORSERIALIZER.get();
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(ModItems.onigiri.get());
    }

    public static class Serializer extends SextupleRecipe.Serializer<ArmorRecipe>{

        @Override
        public ArmorRecipe get(ResourceLocation id, String group, int level, ItemStack result, NonNullList<Ingredient> ingredients) {
            return new ArmorRecipe(id, group, level, result, ingredients);
        }
    }
}
