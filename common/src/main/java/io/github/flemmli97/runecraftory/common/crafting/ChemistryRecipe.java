package io.github.flemmli97.runecraftory.common.crafting;

import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
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
        return new ItemStack(ModItems.ITEM_BLOCK_CHEM.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModCrafting.CHEMISTRY_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends SextupleRecipe> getType() {
        return ModCrafting.CHEMISTRY.get();
    }

    @Override
    public boolean requireExactMatch() {
        return true;
    }
}
