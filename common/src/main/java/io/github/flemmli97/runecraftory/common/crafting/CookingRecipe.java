package io.github.flemmli97.runecraftory.common.crafting;

import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class CookingRecipe extends SextupleRecipe {

    public CookingRecipe(ResourceLocation id, String group, int level, int cost, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(id, group, level, cost, result, ingredients);
    }

    @Override
    public boolean areItemsFitting(ItemStack stack) {
        return stack.isEdible() || stack.getUseAnimation() == UseAnim.DRINK;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ModItems.ITEM_BLOCK_COOKING.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModCrafting.COOKINGSERIALIZER.get();
    }

    @Override
    public RecipeType<? extends SextupleRecipe> getType() {
        return ModCrafting.COOKING.get();
    }

    public static class Serializer extends SextupleRecipe.Serializer<CookingRecipe> {

        @Override
        public CookingRecipe get(ResourceLocation id, String group, int level, int cost, ItemStack result, NonNullList<Ingredient> ingredients) {
            return new CookingRecipe(id, group, level, cost, result, ingredients);
        }
    }
}
