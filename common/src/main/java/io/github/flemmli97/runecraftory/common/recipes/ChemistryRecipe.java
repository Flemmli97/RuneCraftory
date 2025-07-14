package io.github.flemmli97.runecraftory.common.recipes;

import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryCrafting;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ChemistryRecipe extends SextupleRecipe {

    public ChemistryRecipe(String group, int level, int cost, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(group, level, cost, result, ingredients);
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(RuneCraftoryItems.CHEMISTRY_SET.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RuneCraftoryCrafting.CHEMISTRY_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends SextupleRecipe> getType() {
        return RuneCraftoryCrafting.CHEMISTRY.get();
    }

    @Override
    public boolean requireExactMatch() {
        return true;
    }
}
