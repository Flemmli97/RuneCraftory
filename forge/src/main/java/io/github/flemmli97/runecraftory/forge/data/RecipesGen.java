package io.github.flemmli97.runecraftory.forge.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.crafting.RecipeBuilder;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
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
                .requires(ModItems.FUR_SMALL.get(), 4)
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(consumer, RuneCraftory.MODID + ":small_fur_conversion");
        ShapelessRecipeBuilder.shapeless(Items.WHITE_WOOL)
                .requires(ModItems.FUR_MEDIUM.get())
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(consumer, RuneCraftory.MODID + ":medium_fur_conversion");
        ShapelessRecipeBuilder.shapeless(Items.WHITE_WOOL, 2)
                .requires(ModItems.FUR_LARGE.get())
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(consumer, RuneCraftory.MODID + ":large_fur_conversion");
        ShapelessRecipeBuilder.shapeless(Items.IRON_NUGGET, 1)
                .requires(ModItems.SCRAP.get(), 3)
                .unlockedBy("iron", has(Items.IRON_INGOT))
                .save(consumer, RuneCraftory.MODID + ":scrap_iron_conversion");
        ShapelessRecipeBuilder.shapeless(Items.IRON_NUGGET, 1)
                .requires(ModItems.SCRAP_PLUS.get(), 2)
                .unlockedBy("iron", has(Items.IRON_INGOT))
                .save(consumer, RuneCraftory.MODID + ":scrap_plus_iron_conversion");
        ShapedRecipeBuilder.shaped(Items.ARROW, 4)
                .define('#', RunecraftoryTags.WOOD_ROD)
                .define('X', ModItems.ARROW_HEAD.get())
                .define('Y', RunecraftoryTags.FEATHERS)
                .pattern("X").pattern("#").pattern("Y")
                .unlockedBy("feather", RecipeProvider.has(RunecraftoryTags.FEATHERS))
                .unlockedBy("arrowhead", RecipeProvider.has(ModItems.ARROW_HEAD.get()))
                .save(consumer, RuneCraftory.MODID + ":arrows");
        ShapedRecipeBuilder.shaped(Items.ARROW, 4)
                .define('#', RunecraftoryTags.WOOD_ROD)
                .define('X', Items.FLINT)
                .define('Y', RunecraftoryTags.FEATHERS)
                .pattern("X").pattern("#").pattern("Y")
                .unlockedBy("feather", RecipeProvider.has(RunecraftoryTags.FEATHERS))
                .unlockedBy("flint", RecipeProvider.has(Items.FLINT))
                .save(consumer, RuneCraftory.MODID + ":arrows_vanilla");
        ShapelessRecipeBuilder.shapeless(Items.MILK_BUCKET)
                .requires(ModItems.MILK_S.get(), 3)
                .requires(Items.BUCKET)
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(consumer, RuneCraftory.MODID + ":small_milk_conversion");
        ShapelessRecipeBuilder.shapeless(Items.MILK_BUCKET)
                .requires(ModItems.MILK_M.get(), 2)
                .requires(Items.BUCKET)
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(consumer, RuneCraftory.MODID + ":medium_milk_conversion");
        ShapelessRecipeBuilder.shapeless(Items.MILK_BUCKET)
                .requires(ModItems.MILK_L.get())
                .requires(Items.BUCKET)
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(consumer, RuneCraftory.MODID + ":large_milk_conversion");
        ShapelessRecipeBuilder.shapeless(Items.MUSHROOM_STEW)
                .requires(ModItems.MUSHROOM.get(), 2)
                .requires(Items.BOWL)
                .unlockedBy("mushroom_stew", has(ModItems.MUSHROOM.get()))
                .save(consumer, RuneCraftory.MODID + ":mushroom_stew");
        ShapelessRecipeBuilder.shapeless(Items.MUSHROOM_STEW, 2)
                .requires(ModItems.MONARCH_MUSHROOM.get(), 2)
                .requires(Items.BOWL)
                .unlockedBy("mushroom_stew", has(ModItems.MONARCH_MUSHROOM.get()))
                .save(consumer, RuneCraftory.MODID + ":mushroom_stew_monarch");
        ShapelessRecipeBuilder.shapeless(Items.BONE_MEAL, 2)
                .requires(ModItems.FISH_FOSSIL.get(), 1)
                .unlockedBy("fish_fossil", has(ModItems.FISH_FOSSIL.get()))
                .save(consumer, RuneCraftory.MODID + ":fish_fossil_bone_meal");
        ShapelessRecipeBuilder.shapeless(Items.BONE_MEAL, 3)
                .requires(ModItems.SKULL.get(), 1)
                .unlockedBy("skull", has(ModItems.SKULL.get()))
                .save(consumer, RuneCraftory.MODID + ":skull_bone_meal");
        ShapelessRecipeBuilder.shapeless(Items.BONE_MEAL, 9)
                .requires(ModItems.DRAGON_BONES.get(), 1)
                .unlockedBy("dragon_bones", has(ModItems.DRAGON_BONES.get()))
                .save(consumer, RuneCraftory.MODID + ":dragon_bones_bone_meal");

        ShapedRecipeBuilder.shaped(ModItems.SHIPPING_BIN.get())
                .pattern("ses")
                .pattern("scs")
                .pattern("sls")
                .define('s', ItemTags.LOGS)
                .define('e', RunecraftoryTags.EMERALDS)
                .define('c', RunecraftoryTags.CHEST)
                .define('l', ItemTags.PLANKS)
                .unlockedBy("shipping_bin", has(Items.CHEST))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.ITEM_BLOCK_FORGE.get())
                .pattern("ccc")
                .pattern("ibi")
                .pattern("clc")
                .define('c', RunecraftoryTags.COBBLESTONE)
                .define('i', RunecraftoryTags.IRON)
                .define('b', Items.BLAST_FURNACE)
                .define('l', Items.LAVA_BUCKET)
                .unlockedBy("forge_recipe", has(Items.LAVA_BUCKET))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.ITEM_BLOCK_ACCESS.get())
                .pattern(" s ")
                .pattern("aaa")
                .pattern("lcl")
                .define('s', RunecraftoryTags.SHEARS)
                .define('a', ItemTags.WOODEN_SLABS)
                .define('c', Items.CRAFTING_TABLE)
                .define('l', ItemTags.LOGS)
                .unlockedBy("accessory_recipe", has(Items.CRAFTING_TABLE))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.ITEM_BLOCK_CHEM.get())
                .pattern("b s")
                .pattern("qqq")
                .pattern("ccc")
                .define('b', Items.GLASS_BOTTLE)
                .define('s', Items.BREWING_STAND)
                .define('c', Items.CYAN_TERRACOTTA)
                .define('q', Items.QUARTZ_BLOCK)
                .unlockedBy("chemistry_recipe", has(Items.BREWING_STAND))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.ITEM_BLOCK_COOKING.get())
                .pattern("   ")
                .pattern("qwq")
                .pattern("lsl")
                .define('q', Items.QUARTZ_BLOCK)
                .define('w', Items.WATER_BUCKET)
                .define('s', Items.SMOKER)
                .define('l', ItemTags.LOGS)
                .unlockedBy("cooking_recipe", has(Items.SMOKER))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.QUEST_BOARD.get())
                .pattern("PPP")
                .pattern("SSS")
                .pattern("PPP")
                .define('S', ItemTags.SIGNS)
                .define('P', Items.PAPER)
                .unlockedBy("quest_board", has(ItemTags.SIGNS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.TELEPORT.get())
                .pattern(" e ")
                .pattern("ebe")
                .pattern(" e ")
                .define('e', Items.ENDER_PEARL)
                .define('b', ItemTags.BEDS)
                .unlockedBy("teleport", has(ItemTags.BEDS))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.FIRE_BALL_SMALL.get())
                .pattern("bcb")
                .pattern("clc")
                .pattern("bcb")
                .define('b', Items.BLAZE_POWDER)
                .define('c', Items.FIRE_CHARGE)
                .define('l', Items.LAVA_BUCKET)
                .unlockedBy("fireball", has(Items.LAVA_BUCKET))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.LOVE_LETTER.get())
                .pattern(" P ")
                .pattern("PFP")
                .pattern(" P ")
                .define('F', RunecraftoryTags.FLOWERS)
                .define('P', Items.PAPER)
                .unlockedBy("love_letter", has(RunecraftoryTags.FLOWERS))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.DIVORCE_PAPER.get())
                .pattern(" P ")
                .pattern("PSP")
                .pattern(" P ")
                .define('S', ModItems.SCRAP.get())
                .define('P', Items.PAPER)
                .unlockedBy("divorce_paper", has(Items.PAPER))
                .save(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.HOE_SCRAP.get(), 1, 5, 20)
                .addIngredient(RunecraftoryTags.MINERALS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.HOE_IRON.get(), 1, 15, 20)
                .addIngredient(RunecraftoryTags.COPPER_BLOCK).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.HOE_SILVER.get(), 1, 30, 20)
                .addIngredient(RunecraftoryTags.SILVER).addIngredient(ModItems.THREAD_PRETTY.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.HOE_GOLD.get(), 1, 45, 20)
                .addIngredient(RunecraftoryTags.GOLD).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.HOE_PLATINUM.get(), 1, 80, 20)
                .addIngredient(RunecraftoryTags.PLATINUM).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.WATERING_CAN_SCRAP.get(), 1, 5, 20)
                .addIngredient(RunecraftoryTags.MINERALS)
                .addIngredient(RunecraftoryTags.CLOTHS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.WATERING_CAN_IRON.get(), 1, 15, 20)
                .addIngredient(RunecraftoryTags.COPPER_BLOCK)
                .addIngredient(RunecraftoryTags.CLOTHS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.WATERING_CAN_SILVER.get(), 1, 30, 20)
                .addIngredient(RunecraftoryTags.SILVER)
                .addIngredient(ModItems.CLOTH_QUALITY.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.WATERING_CAN_GOLD.get(), 1, 45, 20)
                .addIngredient(RunecraftoryTags.GOLD)
                .addIngredient(RunecraftoryTags.CLOTHS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.WATERING_CAN_PLATINUM.get(), 1, 80, 20)
                .addIngredient(RunecraftoryTags.PLATINUM)
                .addIngredient(RunecraftoryTags.CLOTHS).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.SICKLE_SCRAP.get(), 1, 5, 20)
                .addIngredient(RunecraftoryTags.MINERALS)
                .addIngredient(RunecraftoryTags.SHARDS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.SICKLE_IRON.get(), 1, 15, 20)
                .addIngredient(RunecraftoryTags.COPPER_BLOCK)
                .addIngredient(RunecraftoryTags.SHARDS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.SICKLE_SILVER.get(), 1, 30, 20)
                .addIngredient(RunecraftoryTags.SILVER)
                .addIngredient(ModItems.THREAD_PRETTY.get())
                .addIngredient(RunecraftoryTags.SHARDS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.SICKLE_GOLD.get(), 1, 45, 20)
                .addIngredient(RunecraftoryTags.GOLD)
                .addIngredient(RunecraftoryTags.SHARDS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.SICKLE_PLATINUM.get(), 1, 80, 20)
                .addIngredient(RunecraftoryTags.PLATINUM)
                .addIngredient(RunecraftoryTags.SHARDS).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.HAMMER_SCRAP.get(), 1, 5, 20)
                .addIngredient(RunecraftoryTags.IRON)
                .addIngredient(RunecraftoryTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.HAMMER_IRON.get(), 1, 15, 20)
                .addIngredient(RunecraftoryTags.COPPER_BLOCK)
                .addIngredient(RunecraftoryTags.IRON)
                .addIngredient(RunecraftoryTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.HAMMER_SILVER.get(), 1, 30, 20)
                .addIngredient(RunecraftoryTags.SILVER)
                .addIngredient(ModItems.TURTLE_SHELL.get())
                .addIngredient(RunecraftoryTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.HAMMER_GOLD.get(), 1, 45, 20)
                .addIngredient(RunecraftoryTags.GOLD)
                .addIngredient(RunecraftoryTags.IRON)
                .addIngredient(RunecraftoryTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.HAMMER_PLATINUM.get(), 1, 80, 20)
                .addIngredient(RunecraftoryTags.PLATINUM)
                .addIngredient(RunecraftoryTags.IRON)
                .addIngredient(RunecraftoryTags.STICKS).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.AXE_SCRAP.get(), 1, 5, 20)
                .addIngredient(RunecraftoryTags.IRON)
                .addIngredient(RunecraftoryTags.STICKS)
                .addIngredient(RunecraftoryTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.AXE_IRON.get(), 1, 15, 20)
                .addIngredient(RunecraftoryTags.COPPER_BLOCK)
                .addIngredient(RunecraftoryTags.STICKS)
                .addIngredient(RunecraftoryTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.AXE_SILVER.get(), 1, 30, 20)
                .addIngredient(RunecraftoryTags.SILVER)
                .addIngredient(RunecraftoryTags.STICKS)
                .addIngredient(ModItems.BLADE_SHARD.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.AXE_GOLD.get(), 1, 45, 20)
                .addIngredient(RunecraftoryTags.GOLD)
                .addIngredient(RunecraftoryTags.STICKS)
                .addIngredient(RunecraftoryTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.AXE_PLATINUM.get(), 1, 80, 20)
                .addIngredient(RunecraftoryTags.PLATINUM)
                .addIngredient(RunecraftoryTags.STICKS)
                .addIngredient(RunecraftoryTags.STICKS).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.FISHING_ROD_SCRAP.get(), 1, 5, 20)
                .addIngredient(RunecraftoryTags.STRINGS)
                .addIngredient(RunecraftoryTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.FISHING_ROD_IRON.get(), 1, 15, 20)
                .addIngredient(RunecraftoryTags.COPPER_BLOCK)
                .addIngredient(RunecraftoryTags.STRINGS)
                .addIngredient(RunecraftoryTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.FISHING_ROD_SILVER.get(), 1, 30, 20)
                .addIngredient(RunecraftoryTags.SILVER)
                .addIngredient(ModItems.THREAD_PRETTY.get())
                .addIngredient(RunecraftoryTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.FISHING_ROD_GOLD.get(), 1, 45, 20)
                .addIngredient(RunecraftoryTags.GOLD)
                .addIngredient(RunecraftoryTags.STRINGS)
                .addIngredient(RunecraftoryTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.FISHING_ROD_PLATINUM.get(), 1, 80, 20)
                .addIngredient(RunecraftoryTags.PLATINUM)
                .addIngredient(RunecraftoryTags.STRINGS)
                .addIngredient(RunecraftoryTags.STICKS).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.BROAD_SWORD.get(), 1, 3, 20)
                .addIngredient(RunecraftoryTags.MINERALS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.STEEL_SWORD.get(), 1, 5, 30)
                .addIngredient(RunecraftoryTags.MINERALS).addIngredient(RunecraftoryTags.CLAWS_FANGS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.STEEL_SWORD_PLUS.get(), 1, 7, 30)
                .addIngredient(ModItems.STEEL_SWORD.get()).addIngredient(RunecraftoryTags.MINERALS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.CUTLASS.get(), 1, 10, 40)
                .addIngredient(RunecraftoryTags.COPPER_BLOCK).addIngredient(RunecraftoryTags.CLAWS_FANGS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.AQUA_SWORD.get(), 1, 13, 40)
                .addIngredient(RunecraftoryTags.SILVER).addIngredient(ModItems.AQUAMARINE.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.INVISI_BLADE.get(), 1, 16, 40)
                .addIngredient(ModItems.INVIS_STONE.get()).addIngredient(RunecraftoryTags.CLAWS_FANGS).addIngredient(RunecraftoryTags.CRYSTALS).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.CLAYMORE.get(), 1, 3, 25)
                .addIngredient(RunecraftoryTags.MINERALS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.ZWEIHAENDER.get(), 1, 5, 30)
                .addIngredient(RunecraftoryTags.MINERALS).addIngredient(RunecraftoryTags.CLAWS_FANGS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.ZWEIHAENDER_PLUS.get(), 1, 7, 30)
                .addIngredient(ModItems.ZWEIHAENDER.get()).addIngredient(RunecraftoryTags.COPPER_BLOCK).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.GREAT_SWORD.get(), 1, 13, 40)
                .addIngredient(RunecraftoryTags.COPPER_BLOCK).addIngredient(RunecraftoryTags.LIQUIDS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.SEA_CUTTER.get(), 1, 15, 40)
                .addIngredient(RunecraftoryTags.SILVER).addIngredient(ModItems.AQUAMARINE.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.CYCLONE_BLADE.get(), 1, 18, 40)
                .addIngredient(RunecraftoryTags.GOLD).addIngredient(Items.EMERALD).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.SPEAR.get(), 1, 3, 25)
                .addIngredient(RunecraftoryTags.MINERALS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.WOOD_STAFF.get(), 1, 6, 25)
                .addIngredient(RunecraftoryTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.LANCE.get(), 1, 10, 25)
                .addIngredient(RunecraftoryTags.STICKS).addIngredient(RunecraftoryTags.MINERALS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.LANCE_PLUS.get(), 1, 13, 35)
                .addIngredient(ModItems.LANCE.get()).addIngredient(RunecraftoryTags.COPPER_BLOCK).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.NEEDLE_SPEAR.get(), 1, 16, 40)
                .addIngredient(RunecraftoryTags.STICKS).addIngredient(RunecraftoryTags.COPPER_BLOCK)
                .addIngredient(ModItems.FANG_WOLF.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.TRIDENT.get(), 1, 19, 40)
                .addIngredient(RunecraftoryTags.SILVER).addIngredient(RunecraftoryTags.AQUAMARINES).addIngredient(RunecraftoryTags.STICKS).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.BATTLE_AXE.get(), 1, 5, 25)
                .addIngredient(RunecraftoryTags.MINERALS).addIngredient(RunecraftoryTags.CLAWS_FANGS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.BATTLE_SCYTHE.get(), 1, 9, 25)
                .addIngredient(RunecraftoryTags.COPPER_BLOCK).addIngredient(RunecraftoryTags.CLAWS_FANGS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.POLE_AXE.get(), 1, 15, 25)
                .addIngredient(RunecraftoryTags.COPPER_BLOCK).addIngredient(ModItems.FANG_WOLF.get()).addIngredient(RunecraftoryTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.POLE_AXE_PLUS.get(), 1, 19, 35)
                .addIngredient(ModItems.POLE_AXE.get()).addIngredient(RunecraftoryTags.SILVER).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.GREAT_AXE.get(), 1, 24, 40)
                .addIngredient(RunecraftoryTags.GOLD).addIngredient(RunecraftoryTags.CLAWS_FANGS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.TOMAHAWK.get(), 1, 28, 40)
                .addIngredient(RunecraftoryTags.SILVER).addIngredient(RunecraftoryTags.EMERALDS)
                .addIngredient(ModItems.CLOTH_SILK.get()).addIngredient(RunecraftoryTags.CLAWS_FANGS).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.BATTLE_HAMMER.get(), 1, 3, 25)
                .addIngredient(RunecraftoryTags.MINERALS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.BAT.get(), 1, 8, 25)
                .addIngredient(RunecraftoryTags.COPPER_BLOCK).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.WAR_HAMMER.get(), 1, 14, 25)
                .addIngredient(RunecraftoryTags.COPPER_BLOCK).addIngredient(RunecraftoryTags.SHARDS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.WAR_HAMMER_PLUS.get(), 1, 17, 35)
                .addIngredient(ModItems.WAR_HAMMER.get()).addIngredient(RunecraftoryTags.LIQUIDS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.IRON_BAT.get(), 1, 21, 40)
                .addIngredient(ModItems.BAT.get()).addIngredient(RunecraftoryTags.SILVER).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.GREAT_HAMMER.get(), 1, 25, 40)
                .addIngredient(RunecraftoryTags.SILVER).addIngredient(ModItems.THREAD_PRETTY.get()).addIngredient(RunecraftoryTags.SHARDS).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.SHORT_DAGGER.get(), 1, 3, 25)
                .addIngredient(RunecraftoryTags.MINERALS).addIngredient(RunecraftoryTags.MINERALS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.STEEL_EDGE.get(), 1, 7, 25)
                .addIngredient(RunecraftoryTags.IRON).addIngredient(RunecraftoryTags.IRON).addIngredient(RunecraftoryTags.COPPER_BLOCK).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.FROST_EDGE.get(), 1, 11, 25)
                .addIngredient(ModItems.AQUAMARINE.get()).addIngredient(ModItems.AQUAMARINE.get()).addIngredient(RunecraftoryTags.LIQUIDS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.IRON_EDGE.get(), 1, 15, 30)
                .addIngredient(RunecraftoryTags.IRON).addIngredient(RunecraftoryTags.IRON).addIngredient(RunecraftoryTags.SILVER).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.THIEF_KNIFE.get(), 1, 17, 40)
                .addIngredient(RunecraftoryTags.SILVER).addIngredient(RunecraftoryTags.SILVER).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.WIND_EDGE.get(), 1, 20, 40)
                .addIngredient(RunecraftoryTags.GOLD).addIngredient(Items.EMERALD)
                .addIngredient(Items.EMERALD).addIngredient(RunecraftoryTags.CLAWS_FANGS).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.LEATHER_GLOVE.get(), 1, 3, 25)
                .addIngredient(RunecraftoryTags.CLOTHS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.BRASS_KNUCKLES.get(), 1, 8, 25)
                .addIngredient(RunecraftoryTags.CLOTHS).addIngredient(RunecraftoryTags.COPPER_BLOCK).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.KOTE.get(), 1, 12, 30)
                .addIngredient(RunecraftoryTags.CLOTHS).addIngredient(RunecraftoryTags.IRON).addIngredient(ModItems.CLAW_PALM.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.GLOVES.get(), 1, 14, 35)
                .addIngredient(Items.STRING).addIngredient(ModItems.FUR_MEDIUM.get()).addIngredient(ModItems.CLOTH_QUALITY.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.BEAR_CLAWS.get(), 1, 16, 45)
                .addIngredient(RunecraftoryTags.CLOTHS).addIngredient(ModItems.CLAW_PANTHER.get()).addIngredient(RunecraftoryTags.GOLD).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.FIST_EARTH.get(), 1, 22, 45)
                .addIngredient(RunecraftoryTags.CLOTHS).addIngredient(RunecraftoryTags.GOLD).addIngredient(ModItems.HORN_RIGID.get()).addIngredient(ModItems.CRYSTAL_EARTH.get()).build(consumer);

        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.ROD.get(), 1, 3, 25)
                .addIngredient(RunecraftoryTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.AMETHYST_ROD.get(), 1, 6, 25)
                .addIngredient(RunecraftoryTags.AMETHYSTS).addIngredient(RunecraftoryTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.AQUAMARINE_ROD.get(), 1, 12, 35)
                .addIngredient(RunecraftoryTags.AQUAMARINES).addIngredient(RunecraftoryTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.FRIENDLY_ROD.get(), 1, 16, 35)
                .addIngredient(ModItems.CRYSTAL_LOVE.get()).addIngredient(RunecraftoryTags.STICKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.LOVE_LOVE_ROD.get(), 1, 19, 40)
                .addIngredient(ModItems.FRIENDLY_ROD.get()).addIngredient(ModItems.CRYSTAL_LOVE.get())
                .addIngredient(ModItems.CRYSTAL_LOVE.get()).addIngredient(ModItems.CRYSTAL_LOVE.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.STAFF.get(), 1, 22, 40)
                .addIngredient(ModItems.CRYSTAL_MAGIC.get()).addIngredient(RunecraftoryTags.STICKS).build(consumer);

        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.YARN.get(), 1, 5, 15)
                .addIngredient(RunecraftoryTags.FURS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.ENGAGEMENT_RING.get(), 1, 20, 15)
                .addIngredient(RunecraftoryTags.MINERALS).addIngredient(RunecraftoryTags.JEWELS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.CHEAP_BRACELET.get(), 1, 3, 15)
                .addIngredient(RunecraftoryTags.MINERALS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.BRONZE_BRACELET.get(), 1, 15, 15)
                .addIngredient(RunecraftoryTags.COPPER_BLOCK).addIngredient(RunecraftoryTags.CLOTHS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.SILVER_BRACELET.get(), 1, 25, 20)
                .addIngredient(RunecraftoryTags.SILVER).addIngredient(RunecraftoryTags.CLOTHS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.GOLD_BRACELET.get(), 1, 35, 30)
                .addIngredient(RunecraftoryTags.GOLD).addIngredient(RunecraftoryTags.CLOTHS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.PLATINUM_BRACELET.get(), 1, 50, 40)
                .addIngredient(RunecraftoryTags.PLATINUM).addIngredient(RunecraftoryTags.CLOTHS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.SILVER_RING.get(), 1, 20, 30)
                .addIngredient(RunecraftoryTags.SILVER).addIngredient(RunecraftoryTags.CRYSTALS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.GOLD_RING.get(), 1, 20, 30)
                .addIngredient(RunecraftoryTags.GOLD).addIngredient(ModItems.ORICHALCUM.get()).addIngredient(ModItems.TURNIPS_MIRACLE.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.PLATINUM_RING.get(), 1, 70, 50)
                .addIngredient(RunecraftoryTags.PLATINUM).addIngredient(ModItems.DRAGONIC.get()).build(consumer);

        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.SHIRT.get(), 1, 2, 10)
                .addIngredient(RunecraftoryTags.CLOTHS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.VEST.get(), 1, 10, 25)
                .addIngredient(RunecraftoryTags.CLOTHS).addIngredient(RunecraftoryTags.FURS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.COTTON_CLOTH.get(), 1, 15, 25)
                .addIngredient(ModItems.OLD_BANDAGE.get()).addIngredient(ModItems.OLD_BANDAGE.get()).addIngredient(RunecraftoryTags.STRINGS).build(consumer);

        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.HEADBAND.get(), 1, 1, 10)
                .addIngredient(RunecraftoryTags.CLOTHS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.BLUE_RIBBON.get(), 1, 7, 15)
                .addIngredient(ModItems.BLUE_GRASS.get()).addIngredient(RunecraftoryTags.CLOTHS).addIngredient(RunecraftoryTags.STRINGS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.GREEN_RIBBON.get(), 1, 7, 15)
                .addIngredient(ModItems.GREEN_GRASS.get()).addIngredient(RunecraftoryTags.CLOTHS).addIngredient(RunecraftoryTags.STRINGS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.PURPLE_RIBBON.get(), 1, 7, 15)
                .addIngredient(ModItems.PURPLE_GRASS.get()).addIngredient(RunecraftoryTags.CLOTHS).addIngredient(RunecraftoryTags.STRINGS).build(consumer);

        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.LEATHER_BOOTS.get(), 1, 5, 10)
                .addIngredient(Items.LEATHER_BOOTS).addIngredient(RunecraftoryTags.FURS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.FREE_FARMING_SHOES.get(), 1, 9, 10)
                .addIngredient(RunecraftoryTags.FURS).addIngredient(RunecraftoryTags.STRINGS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.PIYO_SANDALS.get(), 1, 12, 10)
                .addIngredient(ModItems.CARAPACE_INSECT.get()).addIngredient(RunecraftoryTags.CLOTHS).build(consumer);

        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.SMALL_SHIELD.get(), 1, 2, 10)
                .addIngredient(Items.SHIELD).addIngredient(RunecraftoryTags.MINERALS).build(consumer);
        RecipeBuilder.create(EnumCrafting.ARMOR, ModItems.IRON_SHIELD.get(), 1, 10, 10)
                .addIngredient(Items.SHIELD).addIngredient(RunecraftoryTags.IRON).addIngredient(RunecraftoryTags.IRON).build(consumer);

        RecipeBuilder.create(EnumCrafting.CHEM, ModItems.RECOVERY_POTION.get(), 2, 3, 30)
                .addIngredient(ModItems.MEDICINAL_HERB.get())
                .addIngredient(ModItems.MEDICINAL_HERB.get())
                .addIngredient(ModItems.GREEN_GRASS.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.CHEM, ModItems.HEALING_POTION.get(), 2, 20, 30)
                .addIngredient(ModItems.MEDICINAL_HERB.get())
                .addIngredient(ModItems.MEDICINAL_HERB.get())
                .addIngredient(ModItems.RED_GRASS.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.CHEM, ModItems.MYSTERY_POTION.get(), 2, 45, 30)
                .addIngredient(ModItems.MEDICINAL_HERB.get())
                .addIngredient(ModItems.MEDICINAL_HERB.get())
                .addIngredient(ModItems.WHITE_GRASS.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.CHEM, ModItems.MAGICAL_POTION.get(), 2, 70, 30)
                .addIngredient(ModItems.MEDICINAL_HERB.get())
                .addIngredient(ModItems.MEDICINAL_HERB.get())
                .addIngredient(ModItems.ELLI_LEAVES.get()).build(consumer);

        RecipeBuilder.create(EnumCrafting.COOKING, ModItems.ONIGIRI.get(), 1, 1, 10)
                .addIngredient(ModItems.RICE.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.COOKING, ModItems.PICKLED_TURNIP.get(), 1, 5, 12)
                .addIngredient(RunecraftoryTags.TURNIP).build(consumer);

        RecipeBuilder.create(EnumCrafting.COOKING, ModItems.FLAN.get(), 1, 15, 20)
                .addIngredient(RunecraftoryTags.EGGS).addIngredient(RunecraftoryTags.MILKS).build(consumer);
        RecipeBuilder.create(EnumCrafting.COOKING, ModItems.HOT_MILK.get(), 1, 5, 20)
                .addIngredient(RunecraftoryTags.MILKS).build(consumer);

        RecipeBuilder.create(EnumCrafting.COOKING, ModItems.GRAPE_JUICE.get(), 1, 5, 20)
                .addIngredient(RunecraftoryTags.GRAPES).build(consumer);

        RecipeBuilder.create(EnumCrafting.COOKING, ModItems.FRIED_VEGGIES.get(), 1, 20, 25)
                .addIngredient(ModItems.CABBAGE.get()).build(consumer);
        RecipeBuilder.create(EnumCrafting.COOKING, ModItems.BAKED_APPLE.get(), 1, 3, 12)
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
