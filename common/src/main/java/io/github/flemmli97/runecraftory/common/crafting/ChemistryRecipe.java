package io.github.flemmli97.runecraftory.common.crafting;

import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ChemistryRecipe extends SextupleRecipe {

    public ChemistryRecipe(ResourceLocation id, String group, int level, int cost, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(id, group, level, cost, result, ingredients);
    }

    @Override
    public RecipeType<? extends SextupleRecipe> getType() {
        return ModCrafting.CHEMISTRY.get();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModCrafting.CHEMISTRYSERIALIZER.get();
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ModItems.itemBlockChem.get());
    }

    public static class Serializer extends SextupleRecipe.Serializer<ChemistryRecipe> {

        @Override
        public ChemistryRecipe get(ResourceLocation id, String group, int level, int cost, ItemStack result, NonNullList<Ingredient> ingredients) {
            return new ChemistryRecipe(id, group, level, cost, result, ingredients);
        }
    }
}
