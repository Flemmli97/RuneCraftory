package io.github.flemmli97.runecraftory.common.crafting;

import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class ChemistryRecipe extends SextupleRecipe {

    public ChemistryRecipe(ResourceLocation id, String group, int level, int cost, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(id, group, level, cost, result, ingredients);
    }

    @Override
    public IRecipeType<? extends SextupleRecipe> getType() {
        return ModCrafting.CHEMISTRY;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModCrafting.CHEMISTRYSERIALIZER.get();
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(ModItems.itemBlockChem.get());
    }

    public static class Serializer extends SextupleRecipe.Serializer<ChemistryRecipe> {

        @Override
        public ChemistryRecipe get(ResourceLocation id, String group, int level, int cost, ItemStack result, NonNullList<Ingredient> ingredients) {
            return new ChemistryRecipe(id, group, level, cost, result, ingredients);
        }
    }
}
