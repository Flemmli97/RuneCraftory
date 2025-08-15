package io.github.flemmli97.runecraftory.neoforge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.recipes.CraftingType;
import io.github.flemmli97.runecraftory.common.recipes.HammerRemainderRecipe;
import io.github.flemmli97.runecraftory.common.recipes.LevelUpUpgradeRecipe;
import io.github.flemmli97.runecraftory.common.recipes.SextupleRecipeBuilder;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import net.favouriteless.modopedia.common.init.MDataComponents;
import net.favouriteless.modopedia.common.init.MItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class RecipesGen extends RecipeProvider {

    public RecipesGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        ItemStack book = new ItemStack(MItems.BOOK.get());
        book.set(MDataComponents.BOOK.get(), RuneCraftory.modRes("runepedia"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, book)
                .requires(Ingredient.of(Items.BOOK))
                .requires(Ingredient.of(Items.SHORT_GRASS))
                .requires(Ingredient.of(Items.STONE))
                .unlockedBy("book", has(Items.BOOK))
                .save(output, RuneCraftory.MODID + ":runepedia");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.WHITE_WOOL)
                .requires(RuneCraftoryItems.FUR_SMALL.get(), 4)
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(output, RuneCraftory.MODID + ":small_fur_conversion");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.WHITE_WOOL)
                .requires(RuneCraftoryItems.FUR_MEDIUM.get())
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(output, RuneCraftory.MODID + ":medium_fur_conversion");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.WHITE_WOOL, 2)
                .requires(RuneCraftoryItems.FUR_LARGE.get())
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(output, RuneCraftory.MODID + ":large_fur_conversion");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.IRON_NUGGET, 1)
                .requires(RuneCraftoryItems.SCRAP.get(), 3)
                .unlockedBy("iron", has(Items.IRON_INGOT))
                .save(output, RuneCraftory.MODID + ":scrap_iron_conversion");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.IRON_NUGGET, 1)
                .requires(RuneCraftoryItems.SCRAP_PLUS.get(), 2)
                .unlockedBy("iron", has(Items.IRON_INGOT))
                .save(output, RuneCraftory.MODID + ":scrap_plus_iron_conversion");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.ARROW, 4)
                .define('#', RunecraftoryTags.Items.WOOD_ROD)
                .define('X', RuneCraftoryItems.ARROW_HEAD.get())
                .define('Y', RunecraftoryTags.Items.FEATHERS)
                .pattern("X").pattern("#").pattern("Y")
                .unlockedBy("feather", RecipeProvider.has(RunecraftoryTags.Items.FEATHERS))
                .unlockedBy("arrowhead", RecipeProvider.has(RuneCraftoryItems.ARROW_HEAD.get()))
                .save(output, RuneCraftory.MODID + ":arrows");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.ARROW, 4)
                .define('#', RunecraftoryTags.Items.WOOD_ROD)
                .define('X', Items.FLINT)
                .define('Y', RunecraftoryTags.Items.FEATHERS)
                .pattern("X").pattern("#").pattern("Y")
                .unlockedBy("feather", RecipeProvider.has(RunecraftoryTags.Items.FEATHERS))
                .unlockedBy("flint", RecipeProvider.has(Items.FLINT))
                .save(output, RuneCraftory.MODID + ":arrows_vanilla");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.MILK_BUCKET)
                .requires(RuneCraftoryItems.MILK_S.get(), 3)
                .requires(Items.BUCKET)
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(output, RuneCraftory.MODID + ":small_milk_conversion");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.MILK_BUCKET)
                .requires(RuneCraftoryItems.MILK_M.get(), 2)
                .requires(Items.BUCKET)
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(output, RuneCraftory.MODID + ":medium_milk_conversion");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.MILK_BUCKET)
                .requires(RuneCraftoryItems.MILK_L.get())
                .requires(Items.BUCKET)
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(output, RuneCraftory.MODID + ":large_milk_conversion");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.MUSHROOM_STEW)
                .requires(RuneCraftoryItems.MUSHROOM.get(), 2)
                .requires(Items.BOWL)
                .unlockedBy("mushroom_stew", has(RuneCraftoryItems.MUSHROOM.get()))
                .save(output, RuneCraftory.MODID + ":mushroom_stew");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.MUSHROOM_STEW)
                .requires(RuneCraftoryItems.MONARCH_MUSHROOM.get(), 2)
                .requires(Items.BOWL)
                .unlockedBy("mushroom_stew", has(RuneCraftoryItems.MONARCH_MUSHROOM.get()))
                .save(output, RuneCraftory.MODID + ":mushroom_stew_monarch");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 2)
                .requires(RuneCraftoryItems.FISH_FOSSIL.get(), 1)
                .unlockedBy("fish_fossil", has(RuneCraftoryItems.FISH_FOSSIL.get()))
                .save(output, RuneCraftory.MODID + ":fish_fossil_bone_meal");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 3)
                .requires(RuneCraftoryItems.SKULL.get(), 1)
                .unlockedBy("skull", has(RuneCraftoryItems.SKULL.get()))
                .save(output, RuneCraftory.MODID + ":skull_bone_meal");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 9)
                .requires(RuneCraftoryItems.DRAGON_BONES.get(), 1)
                .unlockedBy("dragon_bones", has(RuneCraftoryItems.DRAGON_BONES.get()))
                .save(output, RuneCraftory.MODID + ":dragon_bones_bone_meal");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RuneCraftoryItems.SHIPPING_BIN.get())
                .pattern("ses")
                .pattern("scs")
                .pattern("sls")
                .define('s', ItemTags.LOGS)
                .define('e', RunecraftoryTags.Items.GEMS_EMERALD)
                .define('c', RunecraftoryTags.Items.CHEST)
                .define('l', ItemTags.PLANKS)
                .unlockedBy("shipping_bin", has(Items.CHEST))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RuneCraftoryItems.FORGE.get())
                .pattern("ccc")
                .pattern("ibi")
                .pattern("clc")
                .define('c', RunecraftoryTags.Items.COBBLESTONE)
                .define('i', RunecraftoryTags.Items.IRON)
                .define('b', Items.BLAST_FURNACE)
                .define('l', Items.LAVA_BUCKET)
                .unlockedBy("forge_recipe", has(Items.LAVA_BUCKET))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RuneCraftoryItems.ACCESSORY_WORKBENCH.get())
                .pattern(" s ")
                .pattern("aaa")
                .pattern("lcl")
                .define('s', RunecraftoryTags.Items.SHEARS)
                .define('a', ItemTags.WOODEN_SLABS)
                .define('c', Items.CRAFTING_TABLE)
                .define('l', ItemTags.LOGS)
                .unlockedBy("accessory_recipe", has(Items.CRAFTING_TABLE))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RuneCraftoryItems.CHEMISTRY_SET.get())
                .pattern("b s")
                .pattern("qqq")
                .pattern("ccc")
                .define('b', Items.GLASS_BOTTLE)
                .define('s', Items.BREWING_STAND)
                .define('c', Items.CYAN_TERRACOTTA)
                .define('q', Items.QUARTZ_BLOCK)
                .unlockedBy("chemistry_recipe", has(Items.BREWING_STAND))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RuneCraftoryItems.COOKING_TABLE.get())
                .pattern("   ")
                .pattern("qwq")
                .pattern("lsl")
                .define('q', Items.QUARTZ_BLOCK)
                .define('w', Items.WATER_BUCKET)
                .define('s', Items.SMOKER)
                .define('l', ItemTags.LOGS)
                .unlockedBy("cooking_recipe", has(Items.SMOKER))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RuneCraftoryItems.QUEST_BOARD.get())
                .pattern("PPP")
                .pattern("SSS")
                .pattern("PPP")
                .define('S', ItemTags.SIGNS)
                .define('P', Items.PAPER)
                .unlockedBy("quest_board", has(ItemTags.SIGNS))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RuneCraftoryItems.CASH_REGISTER.get())
                .pattern("wgw")
                .pattern("ece")
                .pattern("www")
                .define('w', Items.WHITE_CONCRETE)
                .define('e', RunecraftoryTags.Items.GEMS_EMERALD)
                .define('c', RunecraftoryTags.Items.CHEST)
                .define('g', Items.GRAY_CONCRETE)
                .unlockedBy("shipping_bin", has(Items.CHEST))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RuneCraftoryItems.TELEPORT.get())
                .pattern(" e ")
                .pattern("ebe")
                .pattern(" e ")
                .define('e', Items.ENDER_PEARL)
                .define('b', ItemTags.BEDS)
                .unlockedBy("teleport", has(ItemTags.BEDS))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RuneCraftoryItems.FIRE_BALL_SMALL.get())
                .pattern("bcb")
                .pattern("clc")
                .pattern("bcb")
                .define('b', Items.BLAZE_POWDER)
                .define('c', Items.FIRE_CHARGE)
                .define('l', Items.LAVA_BUCKET)
                .unlockedBy("fireball", has(Items.LAVA_BUCKET))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RuneCraftoryItems.LOVE_LETTER.get())
                .pattern(" P ")
                .pattern("PFP")
                .pattern(" P ")
                .define('F', RunecraftoryTags.Items.FLOWERS)
                .define('P', Items.PAPER)
                .unlockedBy("love_letter", has(RunecraftoryTags.Items.FLOWERS))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RuneCraftoryItems.DIVORCE_PAPER.get())
                .pattern(" P ")
                .pattern("PSP")
                .pattern(" P ")
                .define('S', RuneCraftoryItems.SCRAP.get())
                .define('P', Items.PAPER)
                .unlockedBy("divorce_paper", has(Items.PAPER))
                .save(output);

        output.accept(RuneCraftory.modRes("bronze_dust"), new HammerRemainderRecipe("", CraftingBookCategory.MISC, RuneCraftoryItems.BRONZE_DUST.get().getDefaultInstance(),
                NonNullList.of(Ingredient.EMPTY, Ingredient.of(RunecraftoryTags.Items.COPPER), Ingredient.of(RunecraftoryTags.Items.RAW_MATERIALS_TIN),
                        Ingredient.of(RunecraftoryTags.Items.HAMMER_TOOLS))), null);

        LevelUpUpgradeRecipe.build(output, 1, Ingredient.of(RunecraftoryTags.Items.MAGIC_SPELLS), Ingredient.of(RunecraftoryTags.Items.DUSTS_BRONZE),
                RuneCraftory.modRes("spells_tier_2"));
        LevelUpUpgradeRecipe.build(output, 2, Ingredient.of(RunecraftoryTags.Items.MAGIC_SPELLS), Ingredient.of(RunecraftoryTags.Items.GOLD),
                RuneCraftory.modRes("spells_tier_3"));
        LevelUpUpgradeRecipe.build(output, 3, Ingredient.of(RunecraftoryTags.Items.MAGIC_SPELLS), Ingredient.of(Items.DIAMOND_BLOCK),
                RuneCraftory.modRes("spells_tier_4"));
        LevelUpUpgradeRecipe.build(output, 4, Ingredient.of(RunecraftoryTags.Items.MAGIC_SPELLS), Ingredient.of(RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM),
                RuneCraftory.modRes("spells_tier_5"));
        LevelUpUpgradeRecipe.build(output, 5, Ingredient.of(RunecraftoryTags.Items.MAGIC_SPELLS), Ingredient.of(RunecraftoryTags.Items.ORICHALCUM),
                RuneCraftory.modRes("spells_tier_6"));
        LevelUpUpgradeRecipe.build(output, 6, Ingredient.of(RunecraftoryTags.Items.MAGIC_SPELLS), Ingredient.of(Items.NETHER_STAR),
                RuneCraftory.modRes("spells_tier_7"));
        LevelUpUpgradeRecipe.build(output, 7, Ingredient.of(RunecraftoryTags.Items.MAGIC_SPELLS), Ingredient.of(RuneCraftoryItems.DRAGONIC.get()),
                RuneCraftory.modRes("spells_tier_8"));
        LevelUpUpgradeRecipe.build(output, 8, Ingredient.of(RunecraftoryTags.Items.MAGIC_SPELLS), Ingredient.of(RuneCraftoryItems.CRYSTAL_RUNE.get()),
                RuneCraftory.modRes("spells_tier_9"));
        LevelUpUpgradeRecipe.build(output, 9, Ingredient.of(RunecraftoryTags.Items.MAGIC_SPELLS), Ingredient.of(RuneCraftoryItems.RUNE_SPHERE_SHARD.get()),
                RuneCraftory.modRes("spells_tier_10"));

        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.HOE_SCRAP.get(), 1, 5, 0)
                .addIngredient(RunecraftoryTags.Items.STICKS).addIngredient(RunecraftoryTags.Items.MINERALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.HOE_IRON.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.STICKS).addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.HOE_SILVER.get(), 1, 30, 0)
                .addIngredient(RunecraftoryTags.Items.STICKS).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).addIngredient(RuneCraftoryItems.THREAD_PRETTY.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.HOE_GOLD.get(), 1, 45, 0)
                .addIngredient(RunecraftoryTags.Items.STICKS).addIngredient(RunecraftoryTags.Items.GOLD).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.HOE_PLATINUM.get(), 1, 80, 0)
                .addIngredient(RunecraftoryTags.Items.STICKS).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM).build(output);

        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.WATERING_CAN_SCRAP.get(), 1, 5, 0)
                .addIngredient(Items.BUCKET)
                .addIngredient(RunecraftoryTags.Items.MINERALS)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.WATERING_CAN_IRON.get(), 1, 15, 0)
                .addIngredient(Items.BUCKET)
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.WATERING_CAN_SILVER.get(), 1, 30, 0)
                .addIngredient(Items.BUCKET)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .addIngredient(RuneCraftoryItems.CLOTH_QUALITY.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.WATERING_CAN_GOLD.get(), 1, 45, 0)
                .addIngredient(Items.BUCKET)
                .addIngredient(RunecraftoryTags.Items.GOLD)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.WATERING_CAN_PLATINUM.get(), 1, 80, 0)
                .addIngredient(Items.BUCKET)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);

        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.SICKLE_SCRAP.get(), 1, 5, 0)
                .addIngredient(RunecraftoryTags.Items.MINERALS)
                .addIngredient(RunecraftoryTags.Items.SHARDS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.SICKLE_IRON.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE)
                .addIngredient(RunecraftoryTags.Items.SHARDS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.SICKLE_SILVER.get(), 1, 30, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .addIngredient(RuneCraftoryItems.THREAD_PRETTY.get())
                .addIngredient(RunecraftoryTags.Items.SHARDS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.SICKLE_GOLD.get(), 1, 45, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD)
                .addIngredient(RunecraftoryTags.Items.SHARDS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.SICKLE_PLATINUM.get(), 1, 80, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM)
                .addIngredient(RunecraftoryTags.Items.SHARDS).build(output);

        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.HAMMER_SCRAP.get(), 1, 5, 0)
                .addIngredient(RunecraftoryTags.Items.IRON)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.HAMMER_IRON.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE)
                .addIngredient(RunecraftoryTags.Items.IRON)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.HAMMER_SILVER.get(), 1, 30, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .addIngredient(RuneCraftoryItems.TURTLE_SHELL.get())
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.HAMMER_GOLD.get(), 1, 45, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD)
                .addIngredient(RunecraftoryTags.Items.IRON)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.HAMMER_PLATINUM.get(), 1, 80, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM)
                .addIngredient(RunecraftoryTags.Items.IRON)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);

        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.AXE_SCRAP.get(), 1, 5, 0)
                .addIngredient(RunecraftoryTags.Items.IRON)
                .addIngredient(RunecraftoryTags.Items.STICKS)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.AXE_IRON.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE)
                .addIngredient(RunecraftoryTags.Items.STICKS)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.AXE_SILVER.get(), 1, 30, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .addIngredient(RunecraftoryTags.Items.STICKS)
                .addIngredient(RuneCraftoryItems.BLADE_SHARD.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.AXE_GOLD.get(), 1, 45, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD)
                .addIngredient(RunecraftoryTags.Items.STICKS)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.AXE_PLATINUM.get(), 1, 80, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM)
                .addIngredient(RunecraftoryTags.Items.STICKS)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);

        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.FISHING_ROD_SCRAP.get(), 1, 5, 0)
                .addIngredient(RunecraftoryTags.Items.STRINGS)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.FISHING_ROD_IRON.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE)
                .addIngredient(RunecraftoryTags.Items.STRINGS)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.FISHING_ROD_SILVER.get(), 1, 30, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .addIngredient(RuneCraftoryItems.THREAD_PRETTY.get())
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.FISHING_ROD_GOLD.get(), 1, 45, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD)
                .addIngredient(RunecraftoryTags.Items.STRINGS)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.FISHING_ROD_PLATINUM.get(), 1, 80, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM)
                .addIngredient(RunecraftoryTags.Items.STRINGS)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);

        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.BROAD_SWORD.get(), 1, 3, 0)
                .addIngredient(RunecraftoryTags.Items.MINERALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.STEEL_SWORD.get(), 1, 5, 0)
                .addIngredient(RuneCraftoryItems.BROAD_SWORD.get())
                .addIngredient(RunecraftoryTags.Items.MINERALS).addIngredient(RunecraftoryTags.Items.CLAWS_FANGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.STEEL_SWORD_PLUS.get(), 1, 7, 0)
                .addIngredient(RuneCraftoryItems.STEEL_SWORD.get())
                .addIngredient(RunecraftoryTags.Items.IRON).addIngredient(RunecraftoryTags.Items.MINERALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.CUTLASS.get(), 1, 10, 0)
                .addIngredient(RuneCraftoryItems.BROAD_SWORD.get())
                .addIngredient(RunecraftoryTags.Items.GEMS_EMERALD)
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).addIngredient(RunecraftoryTags.Items.CLAWS_FANGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.AQUA_SWORD.get(), 1, 13, 0)
                .addIngredient(RuneCraftoryItems.BROAD_SWORD.get())
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).addIngredient(RuneCraftoryItems.AQUAMARINE.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.INVISI_BLADE.get(), 1, 16, 0)
                .addIngredient(RuneCraftoryItems.BROAD_SWORD.get())
                .addIngredient(RuneCraftoryItems.INVIS_STONE.get()).addIngredient(RunecraftoryTags.Items.CLAWS_FANGS).addIngredient(RunecraftoryTags.Items.CRYSTALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.DEFENDER.get(), 1, 20, 0)
                .addIngredient(RuneCraftoryItems.BROAD_SWORD.get())
                .addIngredient(RunecraftoryTags.Items.GEMS_SAPPHIRE).addIngredient(Items.SHIELD)
                .addIngredient(RunecraftoryTags.Items.CLAWS_FANGS)
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.BURNING_SWORD.get(), 1, 24, 0)
                .addIngredient(RuneCraftoryItems.BROAD_SWORD.get())
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RuneCraftoryItems.CRYSTAL_FIRE.get())
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.GORGEOUS_SWORD.get(), 1, 27, 20)
                .addIngredient(RuneCraftoryItems.BROAD_SWORD.get())
                .addIngredient(Items.GOLD_BLOCK).addIngredient(Items.DIAMOND)
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.GAIA_SWORD.get(), 1, 32, 0)
                .addIngredient(RuneCraftoryItems.BROAD_SWORD.get())
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RuneCraftoryItems.CRYSTAL_EARTH.get())
                .addIngredient(RuneCraftoryItems.HORN_RIGID.get())
                .build(output);

        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.CLAYMORE.get(), 1, 3, 0)
                .addIngredient(RunecraftoryTags.Items.MINERALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.ZWEIHAENDER.get(), 1, 5, 0)
                .addIngredient(RuneCraftoryItems.CLAYMORE.get())
                .addIngredient(RunecraftoryTags.Items.MINERALS).addIngredient(RunecraftoryTags.Items.CLAWS_FANGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.ZWEIHAENDER_PLUS.get(), 1, 7, 0)
                .addIngredient(RuneCraftoryItems.ZWEIHAENDER.get())
                .addIngredient(RunecraftoryTags.Items.IRON).addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.GREAT_SWORD.get(), 1, 13, 0)
                .addIngredient(RuneCraftoryItems.CLAYMORE.get())
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).addIngredient(RunecraftoryTags.Items.LIQUIDS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.SEA_CUTTER.get(), 1, 15, 0)
                .addIngredient(RuneCraftoryItems.CLAYMORE.get())
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).addIngredient(RuneCraftoryItems.AQUAMARINE.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.CYCLONE_BLADE.get(), 1, 18, 0)
                .addIngredient(RuneCraftoryItems.CLAYMORE.get())
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(Items.EMERALD).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.POISON_BLADE.get(), 1, 21, 0)
                .addIngredient(RuneCraftoryItems.CLAYMORE.get())
                .addIngredient(RuneCraftoryItems.POWDER_POISON.get()).addIngredient(RunecraftoryTags.Items.GOLD)
                .addIngredient(Items.POISONOUS_POTATO)
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.KATZBALGER.get(), 1, 23, 0)
                .addIngredient(RuneCraftoryItems.CLAYMORE.get())
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .addIngredient(RuneCraftoryItems.HORN_RIGID.get())
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.EARTH_SHADE.get(), 1, 26, 0)
                .addIngredient(RuneCraftoryItems.CLAYMORE.get())
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .addIngredient(RuneCraftoryItems.CRYSTAL_EARTH.get())
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.BIG_KNIFE.get(), 1, 31, 0)
                .addIngredient(Items.IRON_SWORD)
                .addIngredient(Items.DIAMOND).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .build(output);

        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.SPEAR.get(), 1, 3, 0)
                .addIngredient(RunecraftoryTags.Items.STICKS).addIngredient(RunecraftoryTags.Items.MINERALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.WOOD_STAFF.get(), 1, 6, 0)
                .addIngredient(RunecraftoryTags.Items.STICKS).addIngredient(RunecraftoryTags.Items.STICKS)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.LANCE.get(), 1, 10, 0)
                .addIngredient(RuneCraftoryItems.SPEAR.get())
                .addIngredient(RunecraftoryTags.Items.MINERALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.LANCE_PLUS.get(), 1, 13, 0)
                .addIngredient(RuneCraftoryItems.LANCE.get())
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.NEEDLE_SPEAR.get(), 1, 16, 0)
                .addIngredient(RuneCraftoryItems.SPEAR.get())
                .addIngredient(RunecraftoryTags.Items.STICKS).addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE)
                .addIngredient(RuneCraftoryItems.FANG_WOLF.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.TRIDENT.get(), 1, 19, 0)
                .addIngredient(Items.TRIDENT)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.WATER_SPEAR.get(), 1, 22, 0)
                .addIngredient(Items.TRIDENT)
                .addIngredient(RunecraftoryTags.Items.GEMS_AQUAMARINE).addIngredient(RunecraftoryTags.Items.GEMS_AQUAMARINE)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.HALBERD.get(), 1, 24, 0)
                .addIngredient(RuneCraftoryItems.SPEAR.get())
                .addIngredient(RunecraftoryTags.Items.IRON).addIngredient(RunecraftoryTags.Items.GOLD)
                .addIngredient(RuneCraftoryItems.BLADE_SHARD.get())
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.CORSESCA.get(), 1, 27, 0)
                .addIngredient(RuneCraftoryItems.SPEAR.get())
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RuneCraftoryItems.HORN_RIGID.get())
                .build(output);
//        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.CORSESCA_PLUS.get(), 1, 32, 20)
//                .addIngredient(ModItems.CORSESCA.get())
//                .build(consumer);

        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.BATTLE_AXE.get(), 1, 5, 0)
                .addIngredient(RunecraftoryTags.Items.MINERALS).addIngredient(RunecraftoryTags.Items.CLAWS_FANGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.BATTLE_SCYTHE.get(), 1, 9, 0)
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).addIngredient(RunecraftoryTags.Items.CLAWS_FANGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.POLE_AXE.get(), 1, 15, 0)
                .addIngredient(RuneCraftoryItems.BATTLE_AXE.get())
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).addIngredient(RuneCraftoryItems.FANG_WOLF.get()).addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.POLE_AXE_PLUS.get(), 1, 19, 0)
                .addIngredient(RuneCraftoryItems.POLE_AXE.get()).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.GREAT_AXE.get(), 1, 24, 0)
                .addIngredient(RuneCraftoryItems.BATTLE_AXE.get())
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RunecraftoryTags.Items.CLAWS_FANGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.TOMAHAWK.get(), 1, 28, 0)
                .addIngredient(RuneCraftoryItems.BATTLE_AXE.get())
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).addIngredient(RunecraftoryTags.Items.GEMS_EMERALD)
                .addIngredient(RuneCraftoryItems.CLOTH_SILK.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.BASILISK_FANG.get(), 1, 30, 0)
                .addIngredient(RuneCraftoryItems.BATTLE_AXE.get())
                .addIngredient(RuneCraftoryItems.PARA_POISON.get()).addIngredient(RunecraftoryTags.Items.CLAWS_FANGS)
                .addIngredient(RunecraftoryTags.Items.CLAWS_FANGS)
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.ROCK_AXE.get(), 1, 33, 0)
                .addIngredient(RuneCraftoryItems.BATTLE_AXE.get())
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RuneCraftoryItems.CRYSTAL_EARTH.get())
                .addIngredient(RuneCraftoryItems.GLOVE_GIANT.get())
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.DEMON_AXE.get(), 1, 37, 0)
                .addIngredient(RuneCraftoryItems.BATTLE_AXE.get())
                .addIngredient(Items.DIAMOND).addIngredient(RuneCraftoryItems.SCORPION_PINCER.get())
                .addIngredient(RuneCraftoryItems.DEVIL_BLOOD.get())
                .build(output);

        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.BATTLE_HAMMER.get(), 1, 3, 0)
                .addIngredient(RunecraftoryTags.Items.MINERALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.BAT.get(), 1, 8, 0)
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.WAR_HAMMER.get(), 1, 14, 0)
                .addIngredient(RuneCraftoryItems.BATTLE_HAMMER.get())
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).addIngredient(RunecraftoryTags.Items.SHARDS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.WAR_HAMMER_PLUS.get(), 1, 17, 0)
                .addIngredient(RuneCraftoryItems.WAR_HAMMER.get()).addIngredient(RunecraftoryTags.Items.LIQUIDS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.IRON_BAT.get(), 1, 21, 0)
                .addIngredient(RuneCraftoryItems.BAT.get()).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.GREAT_HAMMER.get(), 1, 25, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).addIngredient(RuneCraftoryItems.THREAD_PRETTY.get()).addIngredient(RunecraftoryTags.Items.SHARDS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.ICE_HAMMER.get(), 1, 28, 0)
                .addIngredient(RuneCraftoryItems.BATTLE_HAMMER.get())
                .addIngredient(RunecraftoryTags.Items.GOLD)
                .addIngredient(Items.PACKED_ICE).addIngredient(RunecraftoryTags.Items.GEMS_AQUAMARINE)
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.BONE_HAMMER.get(), 1, 31, 0)
                .addIngredient(RuneCraftoryItems.BATTLE_HAMMER.get())
                .addIngredient(Items.BONE_BLOCK).addIngredient(RunecraftoryTags.Items.GOLD)
                .addIngredient(RunecraftoryTags.Items.SHELLS_BONES)
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.STRONG_STONE.get(), 1, 35, 0)
                .addIngredient(RuneCraftoryItems.BATTLE_HAMMER.get())
                .addIngredient(Items.DIAMOND).addIngredient(RunecraftoryTags.Items.STRINGS)
                .addIngredient(Items.IRON_BLOCK)
                .build(output);

        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.SHORT_DAGGER.get(), 1, 3, 0)
                .addIngredient(RunecraftoryTags.Items.MINERALS).addIngredient(RunecraftoryTags.Items.MINERALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.STEEL_EDGE.get(), 1, 7, 0)
                .addIngredient(RuneCraftoryItems.SHORT_DAGGER.get())
                .addIngredient(RunecraftoryTags.Items.IRON).addIngredient(RunecraftoryTags.Items.IRON).addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.FROST_EDGE.get(), 1, 11, 0)
                .addIngredient(RuneCraftoryItems.SHORT_DAGGER.get())
                .addIngredient(Items.ICE).addIngredient(RuneCraftoryItems.AQUAMARINE.get()).addIngredient(RunecraftoryTags.Items.LIQUIDS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.IRON_EDGE.get(), 1, 15, 0)
                .addIngredient(RuneCraftoryItems.SHORT_DAGGER.get())
                .addIngredient(RunecraftoryTags.Items.IRON).addIngredient(RunecraftoryTags.Items.IRON).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.THIEF_KNIFE.get(), 1, 17, 0)
                .addIngredient(RuneCraftoryItems.SHORT_DAGGER.get())
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.WIND_EDGE.get(), 1, 20, 0)
                .addIngredient(RuneCraftoryItems.SHORT_DAGGER.get())
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(Items.EMERALD)
                .addIngredient(RuneCraftoryItems.CRYSTAL_WIND.get()).addIngredient(RunecraftoryTags.Items.CLAWS_FANGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.GORGEOUS_LX.get(), 1, 23, 0)
                .addIngredient(RuneCraftoryItems.GORGEOUS_SWORD.get()).addIngredient(RuneCraftoryItems.GORGEOUS_SWORD.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.STEEL_KATANA.get(), 1, 26, 0)
                .addIngredient(RuneCraftoryItems.SHORT_DAGGER.get())
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RunecraftoryTags.Items.GOLD)
                .addIngredient(RuneCraftoryItems.FEATHER_BLACK.get())
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.TWIN_BLADE.get(), 1, 28, 0)
                .addIngredient(RuneCraftoryItems.SHORT_DAGGER.get())
                .addIngredient(RuneCraftoryItems.POWDER_POISON.get()).addIngredient(RuneCraftoryItems.HORN_RIGID.get())
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.RAMPAGE.get(), 1, 31, 0)
                .addIngredient(RuneCraftoryItems.SHORT_DAGGER.get())
                .addIngredient(Items.GOLD_BLOCK).addIngredient(Items.DIAMOND)
                .addIngredient(RuneCraftoryItems.FANG_GOLD_WOLF.get())
                .build(output);

        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.LEATHER_GLOVE.get(), 1, 3, 0)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.BRASS_KNUCKLES.get(), 1, 8, 0)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.KOTE.get(), 1, 12, 0)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).addIngredient(RunecraftoryTags.Items.IRON).addIngredient(RuneCraftoryItems.CLAW_PALM.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.GLOVES.get(), 1, 14, 0)
                .addIngredient(RuneCraftoryItems.LEATHER_GLOVE.get())
                .addIngredient(Items.STRING).addIngredient(RuneCraftoryItems.FUR_MEDIUM.get())
                .addIngredient(RuneCraftoryItems.CLOTH_QUALITY.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.BEAR_CLAWS.get(), 1, 16, 0)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).addIngredient(RuneCraftoryItems.CLAW_PANTHER.get())
                .addIngredient(RunecraftoryTags.Items.GOLD).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.FIST_EARTH.get(), 1, 22, 0)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).addIngredient(RunecraftoryTags.Items.GOLD)
                .addIngredient(RuneCraftoryItems.HORN_RIGID.get()).addIngredient(RuneCraftoryItems.CRYSTAL_EARTH.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.FIST_FIRE.get(), 1, 25, 0)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).addIngredient(Items.OBSIDIAN)
                .addIngredient(Items.DIAMOND).addIngredient(RuneCraftoryItems.CRYSTAL_FIRE.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.FIST_WATER.get(), 1, 27, 0)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).addIngredient(RunecraftoryTags.Items.GEMS_AQUAMARINE)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM).addIngredient(RuneCraftoryItems.CRYSTAL_WATER.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.DRAGON_CLAWS.get(), 1, 30, 0)
                .addIngredient(RuneCraftoryItems.LEATHER_GLOVE.get())
                .addIngredient(RunecraftoryTags.Items.SCALES).addIngredient(RuneCraftoryItems.FANG_DRAGON.get())
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.FIST_DARK.get(), 1, 33, 0)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).addIngredient(RuneCraftoryItems.CURSED_DOLL.get())
                .addIngredient(RuneCraftoryItems.CLAW_MALM.get()).addIngredient(RuneCraftoryItems.CRYSTAL_DARK.get()).build(output);

        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.ROD.get(), 1, 3, 0)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.AMETHYST_ROD.get(), 1, 6, 0)
                .addIngredient(RuneCraftoryItems.ROD.get())
                .addIngredient(RunecraftoryTags.Items.GEMS_AMETHYST).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.AQUAMARINE_ROD.get(), 1, 12, 0)
                .addIngredient(RuneCraftoryItems.ROD.get())
                .addIngredient(RunecraftoryTags.Items.GEMS_AQUAMARINE).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.FRIENDLY_ROD.get(), 1, 16, 0)
                .addIngredient(RuneCraftoryItems.ROD.get())
                .addIngredient(RuneCraftoryItems.CRYSTAL_LOVE.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.LOVE_LOVE_ROD.get(), 1, 19, 0)
                .addIngredient(RuneCraftoryItems.FRIENDLY_ROD.get()).addIngredient(RuneCraftoryItems.CRYSTAL_LOVE.get())
                .addIngredient(RuneCraftoryItems.CRYSTAL_LOVE.get()).addIngredient(RuneCraftoryItems.CRYSTAL_LOVE.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.STAFF.get(), 1, 22, 0)
                .addIngredient(RuneCraftoryItems.ROD.get())
                .addIngredient(RuneCraftoryItems.CRYSTAL_MAGIC.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.EMERALD_ROD.get(), 1, 24, 0)
                .addIngredient(RuneCraftoryItems.ROD.get())
                .addIngredient(RunecraftoryTags.Items.GEMS_EMERALD).addIngredient(RunecraftoryTags.Items.GEMS_EMERALD).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.SILVER_STAFF.get(), 1, 28, 0)
                .addIngredient(RuneCraftoryItems.ROD.get())
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.FLARE_STAFF.get(), 1, 30, 0)
                .addIngredient(RuneCraftoryItems.ROD.get())
                .addIngredient(RuneCraftoryItems.CRYSTAL_FIRE.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.RUBY_ROD.get(), 1, 32, 0)
                .addIngredient(RuneCraftoryItems.ROD.get())
                .addIngredient(RunecraftoryTags.Items.GEMS_RUBY).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, RuneCraftoryItems.SAPPHIRE_ROD.get(), 1, 36, 0)
                .addIngredient(RuneCraftoryItems.ROD.get())
                .addIngredient(RunecraftoryTags.Items.GEMS_SAPPHIRE).build(output);

        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.YARN.get(), 1, 5, 0)
                .addIngredient(RunecraftoryTags.Items.FURS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.ENGAGEMENT_RING.get(), 1, 20, 0)
                .addIngredient(RunecraftoryTags.Items.MINERALS).addIngredient(RunecraftoryTags.Items.JEWELS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.CHEAP_BRACELET.get(), 1, 3, 0)
                .addIngredient(RunecraftoryTags.Items.MINERALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.BRONZE_BRACELET.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.SILVER_BRACELET.get(), 1, 25, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.GOLD_BRACELET.get(), 1, 40, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.PLATINUM_BRACELET.get(), 1, 60, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM).addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.SILVER_RING.get(), 1, 20, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).addIngredient(RunecraftoryTags.Items.CRYSTALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.GOLD_RING.get(), 1, 20, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RuneCraftoryItems.ORICHALCUM.get()).addIngredient(RuneCraftoryItems.TURNIPS_MIRACLE.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.PLATINUM_RING.get(), 1, 70, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM).addIngredient(RuneCraftoryItems.DRAGONIC.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.SHIELD_RING.get(), 1, 40, 0)
                .addIngredient(Items.SHIELD).addIngredient(RuneCraftoryItems.TURTLE_SHELL.get())
                .addIngredient(RuneCraftoryItems.TORTOISE_SHELL.get()).addIngredient(RunecraftoryTags.Items.MINERALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.CRITICAL_RING.get(), 1, 30, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RuneCraftoryItems.HORN_RIGID.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.SILENT_RING.get(), 1, 30, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RuneCraftoryItems.LAMP_SQUID.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.PARALYSIS_RING.get(), 1, 30, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RuneCraftoryItems.TAIL_SCORPION.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.POISON_RING.get(), 1, 30, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RuneCraftoryItems.POWDER_POISON.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.MAGIC_RING.get(), 1, 55, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM)
                .addIngredient(Items.EXPERIENCE_BOTTLE).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.THROWING_RING.get(), 1, 25, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).addIngredient(Items.DISPENSER)
                .addIngredient(RuneCraftoryItems.PUPPETRY_STRINGS.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.STAY_UP_RING.get(), 1, 27, 0)
                .addIngredient(RuneCraftoryItems.SPORE_HOLY.get()).addIngredient(RunecraftoryTags.Items.MINERALS)
                .addIngredient(Items.PHANTOM_MEMBRANE).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.AQUAMARINE_RING.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RunecraftoryTags.Items.GEMS_AQUAMARINE).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.AMETHYST_RING.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RunecraftoryTags.Items.GEMS_AMETHYST).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.EMERALD_RING.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RunecraftoryTags.Items.GEMS_EMERALD).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.SAPPHIRE_RING.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RunecraftoryTags.Items.GEMS_SAPPHIRE).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.RUBY_RING.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RunecraftoryTags.Items.GEMS_RUBY).build(output);

        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.SHIRT.get(), 1, 2, 0)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.VEST.get(), 1, 7, 0)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).addIngredient(RunecraftoryTags.Items.FURS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.COTTON_CLOTH.get(), 1, 13, 0)
                .addIngredient(RuneCraftoryItems.OLD_BANDAGE.get()).addIngredient(RuneCraftoryItems.OLD_BANDAGE.get()).addIngredient(RunecraftoryTags.Items.STRINGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.MAIL.get(), 1, 18, 0)
                .addIngredient(Items.LIGHT_BLUE_WOOL).addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).addIngredient(RunecraftoryTags.Items.LIQUIDS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.CHAIN_MAIL.get(), 1, 21, 0)
                .addIngredient(Items.CHAIN).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).addIngredient(RunecraftoryTags.Items.STRINGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.SCALE_VEST.get(), 1, 25, 0)
                .addIngredient(Items.IRON_CHESTPLATE).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .addIngredient(RuneCraftoryItems.CARAPACE_PRETTY.get()).addIngredient(RuneCraftoryItems.ROOT.get()).build(output);

        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.HEADBAND.get(), 1, 1, 0)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.BLUE_RIBBON.get(), 1, 7, 0)
                .addIngredient(RuneCraftoryItems.BLUE_GRASS.get()).addIngredient(RunecraftoryTags.Items.CLOTHS).addIngredient(RunecraftoryTags.Items.STRINGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.GREEN_RIBBON.get(), 1, 7, 0)
                .addIngredient(RuneCraftoryItems.GREEN_GRASS.get()).addIngredient(RunecraftoryTags.Items.CLOTHS).addIngredient(RunecraftoryTags.Items.STRINGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.PURPLE_RIBBON.get(), 1, 7, 0)
                .addIngredient(RuneCraftoryItems.PURPLE_GRASS.get()).addIngredient(RunecraftoryTags.Items.CLOTHS).addIngredient(RunecraftoryTags.Items.STRINGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.SPECTACLES.get(), 1, 10, 0)
                .addIngredient(Items.GLASS).addIngredient(RunecraftoryTags.Items.GEMS_AMETHYST)
                .addIngredient(RunecraftoryTags.Items.GEMS_AQUAMARINE).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.STRAW_HAT.get(), 1, 12, 0)
                .addIngredient(Items.HAY_BLOCK).addIngredient(RunecraftoryTags.Items.STRINGS).addIngredient(RunecraftoryTags.Items.STRINGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.FANCY_HAT.get(), 1, 15, 0)
                .addIngredient(Items.RED_WOOL).addIngredient(RunecraftoryTags.Items.STRINGS).addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);

        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.LEATHER_BOOTS.get(), 1, 5, 0)
                .addIngredient(Items.LEATHER_BOOTS).addIngredient(RunecraftoryTags.Items.FURS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.FREE_FARMING_SHOES.get(), 1, 8, 0)
                .addIngredient(RunecraftoryTags.Items.FURS).addIngredient(RunecraftoryTags.Items.STRINGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.PIYO_SANDALS.get(), 1, 11, 0)
                .addIngredient(RuneCraftoryItems.CARAPACE_INSECT.get()).addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.SECRET_SHOES.get(), 1, 14, 0)
                .addIngredient(Items.LEATHER_BOOTS).addIngredient(RunecraftoryTags.Items.MINERALS)
                .addIngredient(RuneCraftoryItems.GLUE.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.SILVER_BOOTS.get(), 1, 18, 0)
                .addIngredient(Items.LEATHER_BOOTS).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .addIngredient(RunecraftoryTags.Items.SHARDS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.HEAVY_BOOTS.get(), 1, 21, 0)
                .addIngredient(Items.IRON_BOOTS).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .addIngredient(RuneCraftoryItems.CARAPACE_PRETTY.get()).build(output);

        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.SMALL_SHIELD.get(), 1, 2, 0)
                .addIngredient(Items.SHIELD).addIngredient(RunecraftoryTags.Items.MINERALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.UMBRELLA.get(), 1, 7, 0)
                .addIngredient(RunecraftoryTags.Items.STICKS).addIngredient(RunecraftoryTags.Items.STRINGS).addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.IRON_SHIELD.get(), 1, 10, 0)
                .addIngredient(Items.SHIELD).addIngredient(RunecraftoryTags.Items.IRON).addIngredient(RunecraftoryTags.Items.IRON).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.MONKEY_PLUSH.get(), 1, 14, 0)
                .addIngredient(RuneCraftoryItems.DOWN_YELLOW.get()).addIngredient(RunecraftoryTags.Items.CLOTHS).addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.ROUND_SHIELD.get(), 1, 18, 0)
                .addIngredient(Items.SHIELD).addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, RuneCraftoryItems.TURTLE_SHIELD.get(), 1, 23, 0)
                .addIngredient(Items.SHIELD).addIngredient(RuneCraftoryItems.TURTLE_SHELL.get()).addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).build(output);

        SextupleRecipeBuilder.create(CraftingType.CHEMISTRY_SET, RuneCraftoryItems.RECOVERY_POTION.get(), 2, 3, 0)
                .addIngredient(RuneCraftoryItems.MEDICINAL_HERB.get())
                .addIngredient(RuneCraftoryItems.MEDICINAL_HERB.get())
                .addIngredient(RuneCraftoryItems.GREEN_GRASS.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.CHEMISTRY_SET, RuneCraftoryItems.HEALING_POTION.get(), 2, 20, 0)
                .addIngredient(RuneCraftoryItems.MEDICINAL_HERB.get())
                .addIngredient(RuneCraftoryItems.MEDICINAL_HERB.get())
                .addIngredient(RuneCraftoryItems.RED_GRASS.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.CHEMISTRY_SET, RuneCraftoryItems.MYSTERY_POTION.get(), 2, 45, 0)
                .addIngredient(RuneCraftoryItems.MEDICINAL_HERB.get())
                .addIngredient(RuneCraftoryItems.MEDICINAL_HERB.get())
                .addIngredient(RuneCraftoryItems.WHITE_GRASS.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.CHEMISTRY_SET, RuneCraftoryItems.MAGICAL_POTION.get(), 2, 70, 0)
                .addIngredient(RuneCraftoryItems.MEDICINAL_HERB.get())
                .addIngredient(RuneCraftoryItems.MEDICINAL_HERB.get())
                .addIngredient(RuneCraftoryItems.ELLI_LEAVES.get()).build(output);

        // Simple
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.ONIGIRI.get(), 1, 1, 0)
                .addIngredient(RunecraftoryTags.Items.RICE).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.CHEESE.get(), 1, 5, 0)
                .addIngredient(RuneCraftoryItems.SOUR_DROP.get()).addIngredient(RunecraftoryTags.Items.MILKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.PICKLED_TURNIP.get(), 1, 5, 0)
                .addIngredient(RunecraftoryTags.Items.TURNIP).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.SALMON_ONIGIRI.get(), 1, 20, 0)
                .addIngredient(RuneCraftoryItems.SALTED_SALMON.get()).addIngredient(RunecraftoryTags.Items.RICE).build(output);

        // Steamer
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.FLAN.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.EGGS).addIngredient(RunecraftoryTags.Items.MILKS).build(output);

        // Mixer
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.MAYONNAISE.get(), 1, 3, 0)
                .addIngredient(RunecraftoryTags.Items.EGGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.BUTTER.get(), 1, 3, 0)
                .addIngredient(RunecraftoryTags.Items.MILKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.KETCHUP.get(), 1, 5, 0)
                .addIngredient(RuneCraftoryItems.SOUR_DROP.get()).addIngredient(RunecraftoryTags.tagCommon("crops/tomato")).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.APPLE_JUICE.get(), 1, 8, 0)
                .addIngredient(Items.APPLE).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.ORANGE_JUICE.get(), 1, 12, 0)
                .addIngredient(RunecraftoryTags.Items.ORANGE).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.GRAPE_JUICE.get(), 1, 19, 0)
                .addIngredient(RunecraftoryTags.Items.GRAPES).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.TOMATO_JUICE.get(), 1, 26, 0)
                .addIngredient(RunecraftoryTags.tagCommon("crops/tomato")).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.PINEAPPLE_JUICE.get(), 1, 66, 0)
                .addIngredient(RunecraftoryTags.tagCommon("crops/pineapple")).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.FRUIT_JUICE.get(), 1, 30, 0)
                .addIngredient(Items.APPLE).addIngredient(RunecraftoryTags.Items.ORANGE)
                .addIngredient(RunecraftoryTags.Items.GRAPES).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.FRUIT_SMOOTHIE.get(), 1, 44, 0)
                .addIngredient(RuneCraftoryItems.FRUIT_JUICE.get()).addIngredient(RunecraftoryTags.Items.MILKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.VEGETABLE_JUICE.get(), 1, 27, 0)
                .addIngredient(RunecraftoryTags.tagCommon("crops/pumpkin")).addIngredient(RunecraftoryTags.tagCommon("crops/turnip"))
                .addIngredient(RunecraftoryTags.tagCommon("crops/carrot")).addIngredient(RunecraftoryTags.tagCommon("crops/spinach")).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.VEGGIE_SMOOTHIE.get(), 1, 37, 0)
                .addIngredient(RuneCraftoryItems.VEGETABLE_JUICE.get()).addIngredient(RunecraftoryTags.Items.MILKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.MIXED_JUICE.get(), 1, 38, 0)
                .addIngredient(RuneCraftoryItems.FRUIT_JUICE.get()).addIngredient(RuneCraftoryItems.VEGETABLE_JUICE.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.MIXED_SMOOTHIE.get(), 1, 55, 0)
                .addIngredient(RuneCraftoryItems.MIXED_SMOOTHIE.get()).addIngredient(RunecraftoryTags.Items.MILKS).build(output);

        // Oven
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.BAKED_ONIGIRI.get(), 1, 10, 0)
                .addIngredient(RuneCraftoryItems.ONIGIRI.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.TOAST.get(), 1, 11, 0)
                .addIngredient(RunecraftoryTags.Items.BREAD).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.APPLE_PIE.get(), 1, 25, 0)
                .addIngredient(RunecraftoryTags.Items.FLOUR).addIngredient(Items.APPLE)
                .addIngredient(RunecraftoryTags.Items.MILKS).addIngredient(RunecraftoryTags.Items.EGGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.CHEESECAKE.get(), 1, 35, 0)
                .addIngredient(RunecraftoryTags.Items.SUGAR).addIngredient(RunecraftoryTags.Items.CHEESE)
                .addIngredient(RunecraftoryTags.Items.MILKS).addIngredient(RunecraftoryTags.Items.EGGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.CHOCOLATE_CAKE.get(), 1, 50, 0)
                .addIngredient(RunecraftoryTags.Items.BUTTER).addIngredient(RunecraftoryTags.Items.FLOUR)
                .addIngredient(RunecraftoryTags.Items.SUGAR).addIngredient(RunecraftoryTags.Items.CHOCOLATE)
                .addIngredient(RunecraftoryTags.Items.MILKS).addIngredient(RunecraftoryTags.Items.EGGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.COOKIE.get(), 1, 30, 0)
                .addIngredient(RunecraftoryTags.Items.BUTTER).addIngredient(RunecraftoryTags.Items.FLOUR)
                .addIngredient(RunecraftoryTags.Items.SUGAR).addIngredient(RunecraftoryTags.Items.EGGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.CHOCO_COOKIE.get(), 1, 45, 0)
                .addIngredient(RunecraftoryTags.Items.BUTTER).addIngredient(RunecraftoryTags.Items.FLOUR)
                .addIngredient(RunecraftoryTags.Items.SUGAR).addIngredient(RunecraftoryTags.Items.CHOCOLATE)
                .addIngredient(RunecraftoryTags.Items.EGGS).build(output);

        // Pot
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.YOGURT.get(), 1, 7, 0)
                .addIngredient(RunecraftoryTags.Items.MILKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.MARMALADE.get(), 1, 10, 0)
                .addIngredient(RunecraftoryTags.Items.ORANGE).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.APPLE_JAM.get(), 1, 8, 0)
                .addIngredient(Items.APPLE).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.GRAPE_JAM.get(), 1, 12, 0)
                .addIngredient(RunecraftoryTags.Items.GRAPES).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.STRAWBERRY_JAM.get(), 1, 12, 0)
                .addIngredient(RunecraftoryTags.tagCommon("fruits/strawberry")).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.HOT_MILK.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.MILKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.HOT_CHOCOLATE.get(), 1, 20, 0)
                .addIngredient(RunecraftoryTags.Items.CHOCOLATE).addIngredient(RunecraftoryTags.Items.CHOCOLATE).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.UDON.get(), 1, 17, 0)
                .addIngredient(RunecraftoryTags.Items.FLOUR).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.TEMPURA_UDON.get(), 1, 33, 0)
                .addIngredient(RuneCraftoryItems.TEMPURA.get()).addIngredient(RuneCraftoryItems.UDON.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.CURRY_UDON.get(), 1, 52, 0)
                .addIngredient(RuneCraftoryItems.UDON.get()).addIngredient(RuneCraftoryItems.CURRY_POWDER.get())
                .addIngredient(RunecraftoryTags.tagCommon("crops/carrot")).addIngredient(RuneCraftoryItems.HEAVY_SPICE.get()).build(output);

        // Frying
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.BAKED_APPLE.get(), 1, 5, 0)
                .addIngredient(Items.APPLE).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, RuneCraftoryItems.FRIED_VEGGIES.get(), 1, 25, 0)
                .addIngredient(RunecraftoryTags.tagCommon("crops/cabbage")).build(output);

        // Knife

    }
}
