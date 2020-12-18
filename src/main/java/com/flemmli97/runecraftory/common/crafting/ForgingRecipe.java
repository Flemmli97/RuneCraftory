package com.flemmli97.runecraftory.common.crafting;

import com.flemmli97.runecraftory.common.registry.ModCrafting;
import com.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class ForgingRecipe extends SextupleRecipe{

    public ForgingRecipe(ResourceLocation id, String group, int level, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(id, group, level, result, ingredients);
    }

    @Override
    public IRecipeType<? extends SextupleRecipe> getType() {
        return ModCrafting.FORGE;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModCrafting.FORGESERIALIZER.get();
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(ModItems.onigiri.get());
    }

    public static class Serializer extends SextupleRecipe.Serializer<ForgingRecipe>{

        @Override
        public ForgingRecipe get(ResourceLocation id, String group, int level, ItemStack result, NonNullList<Ingredient> ingredients) {
            return new ForgingRecipe(id, group, level, result, ingredients);
        }
    }
}
