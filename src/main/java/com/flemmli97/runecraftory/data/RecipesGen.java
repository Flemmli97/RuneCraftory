package com.flemmli97.runecraftory.data;

import com.flemmli97.runecraftory.api.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.crafting.RecipeBuilder;
import com.flemmli97.runecraftory.common.registry.ModItems;
import com.flemmli97.runecraftory.common.registry.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraftforge.common.data.ForgeRecipeProvider;

import java.util.function.Consumer;

public class RecipesGen extends ForgeRecipeProvider {

    public RecipesGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.broadSword.get(), 1)
                .addIngredient(ModTags.minerals)
                .dummyCriterion().build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.claymore.get(), 1)
                .addIngredient(ModTags.minerals)
                .dummyCriterion().build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.spear.get(), 1)
                .addIngredient(ModTags.minerals)
                .dummyCriterion().build(consumer);

        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.cheapBracelet.get(), 1)
                .addIngredient(ModTags.minerals)
                .dummyCriterion().build(consumer);

        RecipeBuilder.create(EnumCrafting.CHEM, ModItems.recoveryPotion.get(), 3)
                .addIngredient(ModItems.medicinalHerb.get())
                .dummyCriterion().build(consumer);

        RecipeBuilder.create(EnumCrafting.COOKING, ModItems.onigiri.get(), 1)
                .addIngredient(ModItems.rice.get())
                .dummyCriterion().build(consumer);
        RecipeBuilder.create(EnumCrafting.COOKING, ModItems.squidSashimi.get(), 5)
                .addIngredient(ModItems.squid.get())
                .dummyCriterion().build(consumer);
    }
}
