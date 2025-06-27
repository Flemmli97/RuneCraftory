package io.github.flemmli97.runecraftory.common.recipes;

import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ArmorRecipe extends SextupleRecipe {

    public ArmorRecipe(String group, int level, int cost, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(group, level, cost, result, ingredients);
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ModItems.ITEM_BLOCK_ACCESS.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModCrafting.ARMOR_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends SextupleRecipe> getType() {
        return ModCrafting.ARMOR.get();
    }
}
