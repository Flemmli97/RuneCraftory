package com.flemmli97.runecraftory.data;

import com.flemmli97.runecraftory.api.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.crafting.RecipeBuilder;
import com.flemmli97.runecraftory.common.registry.ModItems;
import com.flemmli97.runecraftory.common.registry.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class RecipesGen extends RecipeProvider {

    public RecipesGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(ModItems.shippingBin.get())
                .patternLine("ses")
                .patternLine("scs")
                .patternLine("sls")
                .key('s', ItemTags.LOGS)
                .key('e', Tags.Items.GEMS_EMERALD)
                .key('c', Tags.Items.CHESTS)
                .key('l', ItemTags.PLANKS)
                .addCriterion("shipping_bin", hasItem(Items.CHEST))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModItems.itemBlockForge.get())
                .patternLine("ccc")
                .patternLine("ibi")
                .patternLine("clc")
                .key('c', Tags.Items.COBBLESTONE)
                .key('i', Tags.Items.INGOTS_IRON)
                .key('b', Items.BLAST_FURNACE)
                .key('l', Items.LAVA_BUCKET)
                .addCriterion("forge_recipe", hasItem(Items.LAVA_BUCKET))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModItems.itemBlockAccess.get())
                .patternLine(" s ")
                .patternLine("aaa")
                .patternLine("lcl")
                .key('s', Tags.Items.SHEARS)
                .key('a', ItemTags.WOODEN_SLABS)
                .key('c', Items.CRAFTING_TABLE)
                .key('l', ItemTags.LOGS)
                .addCriterion("accessory_recipe", hasItem(Items.CRAFTING_TABLE))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModItems.itemBlockChem.get())
                .patternLine("b s")
                .patternLine("ccc")
                .patternLine("qqq")
                .key('b', Items.GLASS_BOTTLE)
                .key('s', Items.BREWING_STAND)
                .key('c', Items.CYAN_TERRACOTTA)
                .key('q', Items.QUARTZ_BLOCK)
                .addCriterion("chemistry_recipe", hasItem(Items.BREWING_STAND))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModItems.itemBlockCooking.get())
                .patternLine("   ")
                .patternLine("qwq")
                .patternLine("lsl")
                .key('q', Items.QUARTZ_BLOCK)
                .key('w', Items.WATER_BUCKET)
                .key('s', Items.SMOKER)
                .key('l', ItemTags.LOGS)
                .addCriterion("cooking_recipe", hasItem(Items.SMOKER))
                .build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.broadSword.get(), 1, 1, 20)
                .addIngredient(ModTags.minerals).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.claymore.get(), 1, 3, 25)
                .addIngredient(ModTags.minerals).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.spear.get(), 1, 3, 25)
                .addIngredient(ModTags.minerals).build(consumer);

        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.cheapBracelet.get(), 1, 1, 15)
                .addIngredient(ModTags.minerals).build(consumer);

        RecipeBuilder.create(EnumCrafting.CHEM, ModItems.recoveryPotion.get(), 3, 1, 30)
                .addIngredient(ModItems.medicinalHerb.get()).build(consumer);

        RecipeBuilder.create(EnumCrafting.COOKING, ModItems.onigiri.get(), 1, 1, 10)
                .addIngredient(ModItems.rice.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.COOKING, ModItems.squidSashimi.get(), 1, 5, 15)
                .addIngredient(ModItems.squid.get()).build(consumer);
    }
}
