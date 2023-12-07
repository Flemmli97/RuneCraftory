package io.github.flemmli97.runecraftory.forge.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.crafting.RecipeBuilder;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class RecipesGen extends RecipeProvider {

    public RecipesGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        consumer.accept(this.patchouliShapelessBook(new ResourceLocation(RuneCraftory.MODID, "book"), new ResourceLocation(RuneCraftory.MODID, "runecraftory_book"),
                Ingredient.of(Items.BOOK), Ingredient.of(Items.GRASS), Ingredient.of(Items.STONE)));

        ShapelessRecipeBuilder.shapeless(Items.WHITE_WOOL)
                .requires(ModItems.furSmall.get(), 4)
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(consumer, RuneCraftory.MODID + ":small_fur_conversion");
        ShapelessRecipeBuilder.shapeless(Items.WHITE_WOOL)
                .requires(ModItems.furMedium.get())
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(consumer, RuneCraftory.MODID + ":medium_fur_conversion");
        ShapelessRecipeBuilder.shapeless(Items.WHITE_WOOL, 2)
                .requires(ModItems.furLarge.get())
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(consumer, RuneCraftory.MODID + ":large_fur_conversion");
        ShapelessRecipeBuilder.shapeless(Items.IRON_NUGGET, 1)
                .requires(ModItems.scrap.get(), 3)
                .unlockedBy("iron", has(Items.IRON_INGOT))
                .save(consumer, RuneCraftory.MODID + ":scrap_iron_conversion");
        ShapelessRecipeBuilder.shapeless(Items.IRON_NUGGET, 1)
                .requires(ModItems.scrapPlus.get(), 2)
                .unlockedBy("iron", has(Items.IRON_INGOT))
                .save(consumer, RuneCraftory.MODID + ":scrap_plus_iron_conversion");
        ShapedRecipeBuilder.shaped(Items.ARROW, 4)
                .define('#', ModTags.WOOD_ROD)
                .define('X', ModItems.arrowHead.get())
                .define('Y', ModTags.FEATHERS)
                .pattern("X").pattern("#").pattern("Y")
                .unlockedBy("feather", RecipeProvider.has(ModTags.FEATHERS))
                .unlockedBy("arrowhead", RecipeProvider.has(ModItems.arrowHead.get()))
                .save(consumer, RuneCraftory.MODID + ":arrows");
        ShapedRecipeBuilder.shaped(Items.ARROW, 4)
                .define('#', ModTags.WOOD_ROD)
                .define('X', Items.FLINT)
                .define('Y', ModTags.FEATHERS)
                .pattern("X").pattern("#").pattern("Y")
                .unlockedBy("feather", RecipeProvider.has(ModTags.FEATHERS))
                .unlockedBy("flint", RecipeProvider.has(Items.FLINT))
                .save(consumer, RuneCraftory.MODID + ":arrows_vanilla");
        ShapelessRecipeBuilder.shapeless(Items.MILK_BUCKET)
                .requires(ModItems.milkS.get(), 3)
                .requires(Items.BUCKET)
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(consumer, RuneCraftory.MODID + ":small_milk_conversion");
        ShapelessRecipeBuilder.shapeless(Items.MILK_BUCKET)
                .requires(ModItems.milkM.get(), 2)
                .requires(Items.BUCKET)
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(consumer, RuneCraftory.MODID + ":medium_milk_conversion");
        ShapelessRecipeBuilder.shapeless(Items.MILK_BUCKET)
                .requires(ModItems.milkL.get())
                .requires(Items.BUCKET)
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(consumer, RuneCraftory.MODID + ":large_milk_conversion");
        ShapelessRecipeBuilder.shapeless(Items.MUSHROOM_STEW)
                .requires(ModItems.mushroom.get(), 2)
                .requires(Items.BOWL)
                .unlockedBy("mushroom_stew", has(ModItems.mushroom.get()))
                .save(consumer, RuneCraftory.MODID + ":mushroom_stew");
        ShapelessRecipeBuilder.shapeless(Items.MUSHROOM_STEW, 2)
                .requires(ModItems.monarchMushroom.get(), 2)
                .requires(Items.BOWL)
                .unlockedBy("mushroom_stew", has(ModItems.monarchMushroom.get()))
                .save(consumer, RuneCraftory.MODID + ":mushroom_stew_monarch");
        ShapelessRecipeBuilder.shapeless(Items.BONE_MEAL, 2)
                .requires(ModItems.fishFossil.get(), 1)
                .unlockedBy("fish_fossil", has(ModItems.fishFossil.get()))
                .save(consumer, RuneCraftory.MODID + ":fish_fossil_bone_meal");
        ShapelessRecipeBuilder.shapeless(Items.BONE_MEAL, 3)
                .requires(ModItems.skull.get(), 1)
                .unlockedBy("skull", has(ModItems.skull.get()))
                .save(consumer, RuneCraftory.MODID + ":skull_bone_meal");

        ShapedRecipeBuilder.shaped(ModItems.shippingBin.get())
                .pattern("ses")
                .pattern("scs")
                .pattern("sls")
                .define('s', ItemTags.LOGS)
                .define('e', ModTags.EMERALDS)
                .define('c', ModTags.CHEST)
                .define('l', ItemTags.PLANKS)
                .unlockedBy("shipping_bin", has(Items.CHEST))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.itemBlockForge.get())
                .pattern("ccc")
                .pattern("ibi")
                .pattern("clc")
                .define('c', ModTags.COBBLESTONE)
                .define('i', ModTags.IRON)
                .define('b', Items.BLAST_FURNACE)
                .define('l', Items.LAVA_BUCKET)
                .unlockedBy("forge_recipe", has(Items.LAVA_BUCKET))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.itemBlockAccess.get())
                .pattern(" s ")
                .pattern("aaa")
                .pattern("lcl")
                .define('s', ModTags.SHEARS)
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
        ShapedRecipeBuilder.shaped(ModItems.questBoard.get())
                .pattern("PPP")
                .pattern("SSS")
                .pattern("PPP")
                .define('S', ItemTags.SIGNS)
                .define('P', Items.PAPER)
                .unlockedBy("quest_board", has(ItemTags.SIGNS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.teleport.get())
                .pattern(" e ")
                .pattern("ebe")
                .pattern(" e ")
                .define('e', Items.ENDER_PEARL)
                .define('b', ItemTags.BEDS)
                .unlockedBy("teleport", has(ItemTags.BEDS))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.fireBallSmall.get())
                .pattern("bcb")
                .pattern("clc")
                .pattern("bcb")
                .define('b', Items.BLAZE_POWDER)
                .define('c', Items.FIRE_CHARGE)
                .define('l', Items.LAVA_BUCKET)
                .unlockedBy("fireball", has(Items.LAVA_BUCKET))
                .save(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.hoeScrap.get(), 1, 5, 20)
                .addIngredient(ModTags.MINERALS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.hoeIron.get(), 1, 15, 20)
                .addIngredient(ModTags.COPPER_BLOCK).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.hoeSilver.get(), 1, 30, 20)
                .addIngredient(ModTags.SILVER).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.hoeGold.get(), 1, 45, 20)
                .addIngredient(ModTags.GOLD).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.hoePlatinum.get(), 1, 80, 20)
                .addIngredient(ModTags.PLATINUM).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.wateringCanScrap.get(), 1, 5, 20)
                .addIngredient(ModTags.MINERALS)
                .addIngredient(ModTags.CLOTHS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.wateringCanIron.get(), 1, 15, 20)
                .addIngredient(ModTags.COPPER_BLOCK)
                .addIngredient(ModTags.CLOTHS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.wateringCanSilver.get(), 1, 30, 20)
                .addIngredient(ModTags.SILVER)
                .addIngredient(ModTags.CLOTHS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.wateringCanGold.get(), 1, 45, 20)
                .addIngredient(ModTags.GOLD)
                .addIngredient(ModTags.CLOTHS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.wateringCanPlatinum.get(), 1, 80, 20)
                .addIngredient(ModTags.PLATINUM)
                .addIngredient(ModTags.CLOTHS).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.sickleScrap.get(), 1, 5, 20)
                .addIngredient(ModTags.MINERALS)
                .addIngredient(ModTags.SHARDS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.sickleIron.get(), 1, 15, 20)
                .addIngredient(ModTags.COPPER_BLOCK)
                .addIngredient(ModTags.SHARDS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.sickleSilver.get(), 1, 30, 20)
                .addIngredient(ModTags.SILVER)
                .addIngredient(ModTags.SHARDS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.sickleGold.get(), 1, 45, 20)
                .addIngredient(ModTags.GOLD)
                .addIngredient(ModTags.SHARDS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.sicklePlatinum.get(), 1, 80, 20)
                .addIngredient(ModTags.PLATINUM)
                .addIngredient(ModTags.SHARDS).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.hammerScrap.get(), 1, 5, 20)
                .addIngredient(ModTags.IRON)
                .addIngredient(ModTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.hammerIron.get(), 1, 15, 20)
                .addIngredient(ModTags.COPPER_BLOCK)
                .addIngredient(ModTags.IRON)
                .addIngredient(ModTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.hammerSilver.get(), 1, 30, 20)
                .addIngredient(ModTags.SILVER)
                .addIngredient(ModTags.IRON)
                .addIngredient(ModTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.hammerGold.get(), 1, 45, 20)
                .addIngredient(ModTags.GOLD)
                .addIngredient(ModTags.IRON)
                .addIngredient(ModTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.hammerPlatinum.get(), 1, 80, 20)
                .addIngredient(ModTags.PLATINUM)
                .addIngredient(ModTags.IRON)
                .addIngredient(ModTags.STICKS).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.axeScrap.get(), 1, 5, 20)
                .addIngredient(ModTags.IRON)
                .addIngredient(ModTags.STICKS)
                .addIngredient(ModTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.axeIron.get(), 1, 15, 20)
                .addIngredient(ModTags.COPPER_BLOCK)
                .addIngredient(ModTags.STICKS)
                .addIngredient(ModTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.axeSilver.get(), 1, 30, 20)
                .addIngredient(ModTags.SILVER)
                .addIngredient(ModTags.STICKS)
                .addIngredient(ModTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.axeGold.get(), 1, 45, 20)
                .addIngredient(ModTags.GOLD)
                .addIngredient(ModTags.STICKS)
                .addIngredient(ModTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.axePlatinum.get(), 1, 80, 20)
                .addIngredient(ModTags.PLATINUM)
                .addIngredient(ModTags.STICKS)
                .addIngredient(ModTags.STICKS).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.fishingRodScrap.get(), 1, 5, 20)
                .addIngredient(ModTags.STRINGS)
                .addIngredient(ModTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.fishingRodIron.get(), 1, 15, 20)
                .addIngredient(ModTags.COPPER_BLOCK)
                .addIngredient(ModTags.STRINGS)
                .addIngredient(ModTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.fishingRodSilver.get(), 1, 30, 20)
                .addIngredient(ModTags.SILVER)
                .addIngredient(ModTags.STRINGS)
                .addIngredient(ModTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.fishingRodGold.get(), 1, 45, 20)
                .addIngredient(ModTags.GOLD)
                .addIngredient(ModTags.STRINGS)
                .addIngredient(ModTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.fishingRodPlatinum.get(), 1, 80, 20)
                .addIngredient(ModTags.PLATINUM)
                .addIngredient(ModTags.STRINGS)
                .addIngredient(ModTags.STICKS).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.broadSword.get(), 1, 3, 20)
                .addIngredient(ModTags.MINERALS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.steelSword.get(), 1, 5, 30)
                .addIngredient(ModTags.MINERALS).addIngredient(ModTags.CLAWS_FANGS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.steelSwordPlus.get(), 1, 7, 30)
                .addIngredient(ModItems.steelSword.get()).addIngredient(ModTags.MINERALS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.cutlass.get(), 1, 10, 40)
                .addIngredient(ModTags.COPPER_BLOCK).addIngredient(ModTags.CLAWS_FANGS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.aquaSword.get(), 1, 13, 40)
                .addIngredient(ModTags.SILVER).addIngredient(ModItems.aquamarine.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.invisiBlade.get(), 1, 16, 40)
                .addIngredient(ModItems.invisStone.get()).addIngredient(ModTags.CLAWS_FANGS).addIngredient(ModTags.CRYSTALS).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.claymore.get(), 1, 3, 25)
                .addIngredient(ModTags.MINERALS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.zweihaender.get(), 1, 5, 30)
                .addIngredient(ModTags.MINERALS).addIngredient(ModTags.CLAWS_FANGS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.zweihaenderPlus.get(), 1, 7, 30)
                .addIngredient(ModItems.zweihaender.get()).addIngredient(ModTags.COPPER_BLOCK).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.greatSword.get(), 1, 13, 40)
                .addIngredient(ModTags.COPPER_BLOCK).addIngredient(ModTags.LIQUIDS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.seaCutter.get(), 1, 15, 40)
                .addIngredient(ModTags.SILVER).addIngredient(ModItems.aquamarine.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.cycloneBlade.get(), 1, 18, 40)
                .addIngredient(ModTags.GOLD).addIngredient(Items.EMERALD).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.spear.get(), 1, 3, 25)
                .addIngredient(ModTags.MINERALS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.woodStaff.get(), 1, 6, 25)
                .addIngredient(ModTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.lance.get(), 1, 10, 25)
                .addIngredient(ModTags.STICKS).addIngredient(ModTags.MINERALS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.lancePlus.get(), 1, 13, 35)
                .addIngredient(ModItems.lance.get()).addIngredient(ModTags.COPPER_BLOCK).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.needleSpear.get(), 1, 16, 40)
                .addIngredient(ModTags.STICKS).addIngredient(ModTags.MINERALS)
                .addIngredient(ModTags.CLAWS_FANGS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.trident.get(), 1, 19, 40)
                .addIngredient(ModTags.SILVER).addIngredient(ModTags.STICKS).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.battleAxe.get(), 1, 5, 25)
                .addIngredient(ModTags.MINERALS).addIngredient(ModTags.CLAWS_FANGS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.battleScythe.get(), 1, 9, 25)
                .addIngredient(ModTags.COPPER_BLOCK).addIngredient(ModTags.CLAWS_FANGS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.poleAxe.get(), 1, 15, 25)
                .addIngredient(ModTags.MINERALS).addIngredient(ModTags.CLAWS_FANGS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.poleAxePlus.get(), 1, 19, 35)
                .addIngredient(ModTags.MINERALS).addIngredient(ModTags.CLAWS_FANGS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.greatAxe.get(), 1, 24, 40)
                .addIngredient(ModTags.MINERALS).addIngredient(ModTags.CLAWS_FANGS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.tomahawk.get(), 1, 28, 40)
                .addIngredient(ModTags.MINERALS).addIngredient(ModTags.CLAWS_FANGS).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.battleHammer.get(), 1, 3, 25)
                .addIngredient(ModTags.MINERALS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.bat.get(), 1, 8, 25)
                .addIngredient(ModTags.COPPER_BLOCK).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.warHammer.get(), 1, 14, 25)
                .addIngredient(ModTags.COPPER_BLOCK).addIngredient(ModTags.SHARDS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.warHammerPlus.get(), 1, 17, 35)
                .addIngredient(ModItems.warHammer.get()).addIngredient(ModTags.LIQUIDS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.ironBat.get(), 1, 21, 40)
                .addIngredient(ModItems.bat.get()).addIngredient(ModTags.SILVER).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.greatHammer.get(), 1, 25, 40)
                .addIngredient(ModTags.SILVER).addIngredient(ModTags.SHARDS).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.shortDagger.get(), 1, 3, 25)
                .addIngredient(ModTags.MINERALS).addIngredient(ModTags.MINERALS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.steelEdge.get(), 1, 7, 25)
                .addIngredient(ModTags.IRON).addIngredient(ModTags.IRON).addIngredient(ModTags.COPPER_BLOCK).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.frostEdge.get(), 1, 11, 25)
                .addIngredient(ModItems.aquamarine.get()).addIngredient(ModItems.aquamarine.get()).addIngredient(ModTags.LIQUIDS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.ironEdge.get(), 1, 15, 30)
                .addIngredient(ModTags.MINERALS).addIngredient(ModTags.MINERALS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.thiefKnife.get(), 1, 17, 40)
                .addIngredient(ModTags.IRON).addIngredient(ModTags.IRON).addIngredient(ModTags.SILVER).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.windEdge.get(), 1, 20, 40)
                .addIngredient(ModTags.GOLD).addIngredient(Items.EMERALD)
                .addIngredient(Items.EMERALD).addIngredient(ModTags.CLAWS_FANGS).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.leatherGlove.get(), 1, 3, 25)
                .addIngredient(ModTags.MINERALS).addIngredient(ModTags.CLOTHS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.brassKnuckles.get(), 1, 8, 25)
                .addIngredient(ModTags.MINERALS).addIngredient(ModTags.CLOTHS).addIngredient(ModTags.COPPER_BLOCK).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.kote.get(), 1, 12, 30)
                .addIngredient(ModTags.MINERALS).addIngredient(ModTags.CLOTHS).addIngredient(ModTags.IRON).addIngredient(ModItems.clawPalm.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.gloves.get(), 1, 14, 35)
                .addIngredient(ModTags.MINERALS).addIngredient(ModTags.CLOTHS).addIngredient(ModItems.furMedium.get()).addIngredient(ModItems.clothQuality.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.bearClaws.get(), 1, 16, 45)
                .addIngredient(ModTags.MINERALS).addIngredient(ModTags.CLOTHS).addIngredient(ModTags.GOLD).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.fistEarth.get(), 1, 22, 45)
                .addIngredient(ModTags.MINERALS).addIngredient(ModTags.CLOTHS).addIngredient(ModTags.GOLD).addIngredient(ModItems.hornRigid.get()).addIngredient(ModItems.crystalEarth.get()).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.rod.get(), 1, 3, 25)
                .addIngredient(ModTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.amethystRod.get(), 1, 6, 25)
                .addIngredient(ModTags.AMETHYSTS).addIngredient(ModTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.aquamarineRod.get(), 1, 12, 35)
                .addIngredient(ModTags.AQUAMARINES).addIngredient(ModTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.friendlyRod.get(), 1, 16, 35)
                .addIngredient(ModItems.crystalLove.get()).addIngredient(ModTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.loveLoveRod.get(), 1, 19, 40)
                .addIngredient(ModItems.friendlyRod.get()).addIngredient(ModItems.crystalLove.get())
                .addIngredient(ModItems.crystalLove.get()).addIngredient(ModItems.crystalLove.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.staff.get(), 1, 22, 40)
                .addIngredient(ModItems.crystalMagic.get()).addIngredient(ModTags.STICKS).build(consumer);

        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.yarn.get(), 1, 5, 15)
                .addIngredient(ModTags.FURS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.cheapBracelet.get(), 1, 3, 15)
                .addIngredient(ModTags.MINERALS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.bronzeBracelet.get(), 1, 15, 15)
                .addIngredient(ModTags.COPPER_BLOCK).addIngredient(ModTags.CLOTHS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.silverBracelet.get(), 1, 25, 20)
                .addIngredient(ModTags.SILVER).addIngredient(ModTags.CLOTHS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.goldBracelet.get(), 1, 35, 30)
                .addIngredient(ModTags.GOLD).addIngredient(ModTags.CLOTHS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.platinumBracelet.get(), 1, 50, 40)
                .addIngredient(ModTags.PLATINUM).addIngredient(ModTags.CLOTHS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.silverRing.get(), 1, 20, 30)
                .addIngredient(ModTags.SILVER).addIngredient(ModTags.CRYSTALS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.goldRing.get(), 1, 20, 30)
                .addIngredient(ModTags.GOLD).addIngredient(ModItems.orichalcum.get()).addIngredient(ModItems.turnipsMiracle.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.platinumRing.get(), 1, 70, 50)
                .addIngredient(ModTags.PLATINUM).addIngredient(ModItems.dragonic.get()).build(consumer);

        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.shirt.get(), 1, 2, 10)
                .addIngredient(ModTags.CLOTHS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.vest.get(), 1, 10, 25)
                .addIngredient(ModTags.CLOTHS).addIngredient(ModTags.FURS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.cottonCloth.get(), 1, 15, 25)
                .addIngredient(ModItems.oldBandage.get()).addIngredient(ModItems.oldBandage.get()).addIngredient(ModTags.STRINGS).build(consumer);

        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.headband.get(), 1, 1, 10)
                .addIngredient(ModTags.CLOTHS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.blueRibbon.get(), 1, 7, 15)
                .addIngredient(ModItems.blueGrass.get()).addIngredient(ModTags.CLOTHS).addIngredient(ModTags.STRINGS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.greenRibbon.get(), 1, 7, 15)
                .addIngredient(ModItems.greenGrass.get()).addIngredient(ModTags.CLOTHS).addIngredient(ModTags.STRINGS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.purpleRibbon.get(), 1, 7, 15)
                .addIngredient(ModItems.purpleGrass.get()).addIngredient(ModTags.CLOTHS).addIngredient(ModTags.STRINGS).build(consumer);

        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.leatherBoots.get(), 1, 5, 10)
                .addIngredient(Items.LEATHER_BOOTS).addIngredient(ModTags.FURS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.freeFarmingShoes.get(), 1, 9, 10)
                .addIngredient(ModTags.FURS).addIngredient(ModTags.STRINGS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.piyoSandals.get(), 1, 12, 10)
                .addIngredient(ModItems.carapaceInsect.get()).addIngredient(ModTags.CLOTHS).build(consumer);

        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.smallShield.get(), 1, 2, 10)
                .addIngredient(Items.SHIELD).addIngredient(ModTags.MINERALS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.ironShield.get(), 1, 10, 10)
                .addIngredient(Items.SHIELD).addIngredient(ModTags.IRON).addIngredient(ModTags.IRON).build(consumer);

        RecipeBuilder.create(EnumCrafting.CHEM, ModItems.recoveryPotion.get(), 2, 3, 30)
                .addIngredient(ModItems.medicinalHerb.get())
                .addIngredient(ModItems.medicinalHerb.get())
                .addIngredient(ModItems.greenGrass.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.CHEM, ModItems.healingPotion.get(), 2, 20, 30)
                .addIngredient(ModItems.medicinalHerb.get())
                .addIngredient(ModItems.medicinalHerb.get())
                .addIngredient(ModItems.redGrass.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.CHEM, ModItems.mysteryPotion.get(), 2, 45, 30)
                .addIngredient(ModItems.medicinalHerb.get())
                .addIngredient(ModItems.medicinalHerb.get())
                .addIngredient(ModItems.whiteGrass.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.CHEM, ModItems.magicalPotion.get(), 2, 70, 30)
                .addIngredient(ModItems.medicinalHerb.get())
                .addIngredient(ModItems.medicinalHerb.get())
                .addIngredient(ModItems.elliLeaves.get()).build(consumer);

        RecipeBuilder.create(EnumCrafting.COOKING, ModItems.onigiri.get(), 1, 1, 10)
                .addIngredient(ModItems.rice.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.COOKING, ModItems.pickledTurnip.get(), 1, 5, 12)
                .addIngredient(ModTags.TURNIP).build(consumer);

        RecipeBuilder.create(EnumCrafting.COOKING, ModItems.flan.get(), 1, 15, 20)
                .addIngredient(ModTags.EGGS).addIngredient(ModTags.MILKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.COOKING, ModItems.hotMilk.get(), 1, 5, 20)
                .addIngredient(ModTags.MILKS).build(consumer);

        RecipeBuilder.create(EnumCrafting.COOKING, ModItems.grapeJuice.get(), 1, 5, 20)
                .addIngredient(ModTags.GRAPES).build(consumer);

        RecipeBuilder.create(EnumCrafting.COOKING, ModItems.friedVeggies.get(), 1, 20, 25)
                .addIngredient(ModItems.cabbage.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.COOKING, ModItems.bakedApple.get(), 1, 3, 12)
                .addIngredient(Items.APPLE).build(consumer);

    }

    private FinishedRecipe patchouliShapelessBook(ResourceLocation id, ResourceLocation book, Ingredient... ingredients) {
        return new FinishedRecipe() {

            @Override
            public JsonObject serializeRecipe() {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("type", "patchouli:shapeless_book_recipe");
                this.serializeRecipeData(jsonObject);
                return jsonObject;
            }

            @Override
            public void serializeRecipeData(JsonObject json) {
                JsonArray arr = new JsonArray();
                for (Ingredient ing : ingredients)
                    arr.add(ing.toJson());
                json.add("ingredients", arr);
                json.addProperty("book", book.toString());
            }

            @Override
            public ResourceLocation getId() {
                return id;
            }

            @Override
            public RecipeSerializer<?> getType() {
                return null;
            }

            @Nullable
            @Override
            public JsonObject serializeAdvancement() {
                return null;
            }

            @Nullable
            @Override
            public ResourceLocation getAdvancementId() {
                return null;
            }
        };
    }
}
