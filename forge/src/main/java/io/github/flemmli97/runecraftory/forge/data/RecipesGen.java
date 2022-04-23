package io.github.flemmli97.runecraftory.forge.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.function.Consumer;

public class RecipesGen extends RecipeProvider {

    public RecipesGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        /*ShapedRecipeBuilder.shaped(ModItems.shippingBin.get())
                .pattern("ses")
                .pattern("scs")
                .pattern("sls")
                .define('s', ItemTags.LOGS)
                .define('e', Tags.Items.GEMS_EMERALD)
                .define('c', Tags.Items.CHESTS)
                .define('l', ItemTags.PLANKS)
                .unlockedBy("shipping_bin", has(Items.CHEST))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.itemBlockForge.get())
                .pattern("ccc")
                .pattern("ibi")
                .pattern("clc")
                .define('c', Tags.Items.COBBLESTONE)
                .define('i', Tags.Items.INGOTS_IRON)
                .define('b', Items.BLAST_FURNACE)
                .define('l', Items.LAVA_BUCKET)
                .unlockedBy("forge_recipe", has(Items.LAVA_BUCKET))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.itemBlockAccess.get())
                .pattern(" s ")
                .pattern("aaa")
                .pattern("lcl")
                .define('s', Tags.Items.SHEARS)
                .define('a', ItemTags.WOODEN_SLABS)
                .define('c', Items.CRAFTING_TABLE)
                .define('l', ItemTags.LOGS)
                .unlockedBy("accessory_recipe", has(Items.CRAFTING_TABLE))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.itemBlockChem.get())
                .pattern("b s")
                .pattern("ccc")
                .pattern("qqq")
                .define('b', Items.GLASS_BOTTLE)
                .define('s', Items.BREWING_STAND)
                .define('c', Items.CYAN_TERRACOTTA)
                .define('q', Items.QUARTZ_BLOCK)
                .unlockedBy("chemistry_recipe", has(Items.BREWING_STAND))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.itemBlockCooking.get())
                .pattern("   ")
                .pattern("qwq")
                .pattern("lsl")
                .define('q', Items.QUARTZ_BLOCK)
                .define('w', Items.WATER_BUCKET)
                .define('s', Items.SMOKER)
                .define('l', ItemTags.LOGS)
                .unlockedBy("cooking_recipe", has(Items.SMOKER))
                .save(consumer);

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
                .addIngredient(ModItems.squid.get()).build(consumer);*/
    }
}
