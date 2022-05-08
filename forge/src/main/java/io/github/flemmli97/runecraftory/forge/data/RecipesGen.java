package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.crafting.RecipeBuilder;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class RecipesGen extends RecipeProvider {

    public RecipesGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(ModItems.shippingBin.get())
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
                .pattern("qqq")
                .pattern("ccc")
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
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.steelSword.get(), 1, 3, 30)
                .addIngredient(ModTags.minerals).addIngredient(ModTags.clawFangs).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.steelSwordPlus.get(), 1, 6, 40)
                .addIngredient(ModItems.steelSword.get()).addIngredient(ModTags.minerals).build(consumer);
        /*RecipeBuilder.create(EnumCrafting.FORGE, ModItems.cutlass.get(), 1, 8, 30)
                .addIngredient(ModTags.bronze).addIngredient(ModTags.clawFangs).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.aquaSword.get(), 1, 12, 30)
                .addIngredient(ModTags.silver).addIngredient(ModItems.aquamarine.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.invisiBlade.get(), 1, 16, 30)
                .addIngredient(ModItems.invisStone.get()).addIngredient(ModTags.clawFangs).addIngredient(ModTags.crystals).build(consumer);*/

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.claymore.get(), 1, 3, 25)
                .addIngredient(ModTags.minerals).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.zweihaender.get(), 1, 5, 30)
                .addIngredient(ModTags.minerals).addIngredient(ModTags.clawFangs).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.zweihaenderPlus.get(), 1, 7, 30)
                .addIngredient(ModItems.zweihaender.get()).addIngredient(ModTags.bronze).build(consumer);
        /*RecipeBuilder.create(EnumCrafting.FORGE, ModItems.greatSword.get(), 1, 13, 30)
                .addIngredient(ModTags.bronze).addIngredient(ModTags.liquids).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.seaCutter.get(), 1, 15, 30)
                .addIngredient(ModTags.silver).addIngredient(ModItems.aquamarine.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.cycloneBlade.get(), 1, 18, 30)
                .addIngredient(ModTags.gold).addIngredient(Items.EMERALD).build(consumer);*/

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.spear.get(), 1, 3, 25)
                .addIngredient(ModTags.minerals).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.woodStaff.get(), 1, 6, 25)
                .addIngredient(ModTags.sticks).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.lance.get(), 1, 10, 25)
                .addIngredient(ModTags.sticks).addIngredient(ModTags.minerals).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.lancePlus.get(), 1, 13, 25)
                .addIngredient(ModItems.lance.get()).addIngredient(ModTags.bronze).build(consumer);
        /*RecipeBuilder.create(EnumCrafting.FORGE, ModItems.needleSpear.get(), 1, 16, 25)
                .addIngredient(ModTags.sticks).addIngredient(ModTags.minerals)
                .addIngredient(ModTags.clawFangs).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.trident.get(), 1, 19, 25)
                .addIngredient(ModTags.silver).addIngredient(ModTags.sticks).build(consumer);*/

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.battleAxe.get(), 1, 5, 25)
                .addIngredient(ModTags.minerals).addIngredient(ModTags.clawFangs).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.battleScythe.get(), 1, 9, 25)
                .addIngredient(ModTags.bronze).addIngredient(ModTags.clawFangs).build(consumer);
        /*RecipeBuilder.create(EnumCrafting.FORGE, ModItems.poleAxe.get(), 1, 15, 25)
                .addIngredient(ModTags.minerals).addIngredient(ModTags.clawFangs).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.poleAxePlus.get(), 1, 19, 25)
                .addIngredient(ModTags.minerals).addIngredient(ModTags.clawFangs).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.greatAxe.get(), 1, 24, 25)
                .addIngredient(ModTags.minerals).addIngredient(ModTags.clawFangs).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.tomohawk.get(), 1, 28, 25)
                .addIngredient(ModTags.minerals).addIngredient(ModTags.clawFangs).build(consumer);*/

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.battleHammer.get(), 1, 3, 25)
                .addIngredient(ModTags.minerals).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.bat.get(), 1, 8, 25)
                .addIngredient(ModTags.bronze).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.warHammer.get(), 1, 14, 25)
                .addIngredient(ModTags.bronze).addIngredient(ModTags.shards).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.warHammerPlus.get(), 1, 17, 25)
                .addIngredient(ModItems.warHammer.get()).addIngredient(ModTags.liquids).build(consumer);
        /*RecipeBuilder.create(EnumCrafting.FORGE, ModItems.ironBat.get(), 1, 21, 25)
                .addIngredient(ModItems.bat.get()).addIngredient(ModTags.silver).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.greatHammer.get(), 1, 25, 25)
                .addIngredient(ModTags.silver).addIngredient(ModTags.shards).build(consumer);*/

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.shortDagger.get(), 1, 3, 25)
                .addIngredient(ModTags.minerals).addIngredient(ModTags.minerals).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.steelEdge.get(), 1, 7, 25)
                .addIngredient(ModTags.iron).addIngredient(ModTags.iron).addIngredient(ModTags.bronze).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.frostEdge.get(), 1, 11, 25)
                .addIngredient(ModItems.aquamarine.get()).addIngredient(ModItems.aquamarine.get()).addIngredient(ModTags.liquids).build(consumer);
        /*RecipeBuilder.create(EnumCrafting.FORGE, ModItems.ironEdge.get(), 1, 15, 25)
                .addIngredient(ModTags.minerals).addIngredient(ModTags.minerals).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.thiefKnife.get(), 1, 17, 25)
                .addIngredient(ModTags.iron).addIngredient(ModTags.iron).addIngredient(ModTags.silver).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.windEdge.get(), 1, 20, 25)
                .addIngredient(ModTags.gold).addIngredient(Items.EMERALD)
                .addIngredient(Items.EMERALD).addIngredient(ModTags.clawFangs).build(consumer);*/

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.leatherGlove.get(), 1, 3, 25)
                .addIngredient(ModTags.minerals).addIngredient(ModTags.cloths).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.rod.get(), 1, 3, 25)
                .addIngredient(ModTags.sticks).build(consumer);

        //RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.cheapBracelet.get(), 1, 1, 15)
        //        .addIngredient(ModTags.minerals).build(consumer);

        RecipeBuilder.create(EnumCrafting.CHEM, ModItems.recoveryPotion.get(), 3, 1, 30)
                .addIngredient(ModItems.medicinalHerb.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.CHEM, ModItems.healingPotion.get(), 3, 20, 30)
                .addIngredient(ModItems.medicinalHerb.get())
                .addIngredient(ModItems.medicinalHerb.get())
                .addIngredient(ModItems.redGrass.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.CHEM, ModItems.mysteryPotion.get(), 3, 45, 30)
                .addIngredient(ModItems.medicinalHerb.get())
                .addIngredient(ModItems.medicinalHerb.get())
                .addIngredient(ModItems.medicinalHerb.get())
                .addIngredient(ModItems.whiteGrass.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.CHEM, ModItems.magicalPotion.get(), 3, 45, 30)
                .addIngredient(ModItems.medicinalHerb.get())
                .addIngredient(ModItems.medicinalHerb.get())
                .addIngredient(ModItems.medicinalHerb.get())
                .addIngredient(ModItems.medicinalHerb.get())
                .addIngredient(ModItems.elliLeaves.get()).build(consumer);

        RecipeBuilder.create(EnumCrafting.COOKING, ModItems.onigiri.get(), 1, 1, 10)
                .addIngredient(ModItems.rice.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.COOKING, ModItems.pickledTurnip.get(), 1, 5, 12)
                .addIngredient(ModItems.turnip.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.COOKING, ModItems.bakedApple.get(), 1, 3, 12)
                .addIngredient(Items.APPLE).build(consumer);
        RecipeBuilder.create(EnumCrafting.COOKING, ModItems.flan.get(), 1, 15, 20)
                .addIngredient(ModTags.eggs).addIngredient(ModTags.milk).build(consumer);
        RecipeBuilder.create(EnumCrafting.COOKING, ModItems.hotMilk.get(), 1, 5, 20)
                .addIngredient(ModTags.milk).build(consumer);
        RecipeBuilder.create(EnumCrafting.COOKING, ModItems.friedVeggies.get(), 1, 20, 25)
                .addIngredient(ModItems.cabbage.get()).build(consumer);
        //RecipeBuilder.create(EnumCrafting.COOKING, ModItems.squidSashimi.get(), 1, 5, 15)
        //        .addIngredient(ModItems.squid.get()).build(consumer);
    }
}
