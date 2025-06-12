package io.github.flemmli97.runecraftory.common.crafting;

import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class CookingRecipe extends SextupleRecipe {

    public CookingRecipe(String group, int level, int cost, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(group, level, cost, result, ingredients);
    }

    @Override
    public boolean areItemsFitting(ItemStack stack) {
        return stack.has(DataComponents.FOOD) || stack.getUseAnimation() == UseAnim.DRINK;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ModItems.ITEM_BLOCK_COOKING.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModCrafting.COOKING_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends SextupleRecipe> getType() {
        return ModCrafting.COOKING.get();
    }
}
