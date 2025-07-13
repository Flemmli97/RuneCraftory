package io.github.flemmli97.runecraftory.neoforge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.recipes.CraftingType;
import io.github.flemmli97.runecraftory.common.recipes.HammerRemainderRecipe;
import io.github.flemmli97.runecraftory.common.recipes.LevelUpUpgradeRecipe;
import io.github.flemmli97.runecraftory.common.recipes.SextupleRecipeBuilder;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
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
                .requires(ModItems.FUR_SMALL.get(), 4)
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(output, RuneCraftory.MODID + ":small_fur_conversion");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.WHITE_WOOL)
                .requires(ModItems.FUR_MEDIUM.get())
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(output, RuneCraftory.MODID + ":medium_fur_conversion");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.WHITE_WOOL, 2)
                .requires(ModItems.FUR_LARGE.get())
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(output, RuneCraftory.MODID + ":large_fur_conversion");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.IRON_NUGGET, 1)
                .requires(ModItems.SCRAP.get(), 3)
                .unlockedBy("iron", has(Items.IRON_INGOT))
                .save(output, RuneCraftory.MODID + ":scrap_iron_conversion");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.IRON_NUGGET, 1)
                .requires(ModItems.SCRAP_PLUS.get(), 2)
                .unlockedBy("iron", has(Items.IRON_INGOT))
                .save(output, RuneCraftory.MODID + ":scrap_plus_iron_conversion");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.ARROW, 4)
                .define('#', RunecraftoryTags.Items.WOOD_ROD)
                .define('X', ModItems.ARROW_HEAD.get())
                .define('Y', RunecraftoryTags.Items.FEATHERS)
                .pattern("X").pattern("#").pattern("Y")
                .unlockedBy("feather", RecipeProvider.has(RunecraftoryTags.Items.FEATHERS))
                .unlockedBy("arrowhead", RecipeProvider.has(ModItems.ARROW_HEAD.get()))
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
                .requires(ModItems.MILK_S.get(), 3)
                .requires(Items.BUCKET)
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(output, RuneCraftory.MODID + ":small_milk_conversion");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.MILK_BUCKET)
                .requires(ModItems.MILK_M.get(), 2)
                .requires(Items.BUCKET)
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(output, RuneCraftory.MODID + ":medium_milk_conversion");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.MILK_BUCKET)
                .requires(ModItems.MILK_L.get())
                .requires(Items.BUCKET)
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(output, RuneCraftory.MODID + ":large_milk_conversion");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.MUSHROOM_STEW)
                .requires(ModItems.MUSHROOM.get(), 2)
                .requires(Items.BOWL)
                .unlockedBy("mushroom_stew", has(ModItems.MUSHROOM.get()))
                .save(output, RuneCraftory.MODID + ":mushroom_stew");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.MUSHROOM_STEW)
                .requires(ModItems.MONARCH_MUSHROOM.get(), 2)
                .requires(Items.BOWL)
                .unlockedBy("mushroom_stew", has(ModItems.MONARCH_MUSHROOM.get()))
                .save(output, RuneCraftory.MODID + ":mushroom_stew_monarch");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 2)
                .requires(ModItems.FISH_FOSSIL.get(), 1)
                .unlockedBy("fish_fossil", has(ModItems.FISH_FOSSIL.get()))
                .save(output, RuneCraftory.MODID + ":fish_fossil_bone_meal");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 3)
                .requires(ModItems.SKULL.get(), 1)
                .unlockedBy("skull", has(ModItems.SKULL.get()))
                .save(output, RuneCraftory.MODID + ":skull_bone_meal");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 9)
                .requires(ModItems.DRAGON_BONES.get(), 1)
                .unlockedBy("dragon_bones", has(ModItems.DRAGON_BONES.get()))
                .save(output, RuneCraftory.MODID + ":dragon_bones_bone_meal");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SHIPPING_BIN.get())
                .pattern("ses")
                .pattern("scs")
                .pattern("sls")
                .define('s', ItemTags.LOGS)
                .define('e', RunecraftoryTags.Items.GEMS_EMERALD)
                .define('c', RunecraftoryTags.Items.CHEST)
                .define('l', ItemTags.PLANKS)
                .unlockedBy("shipping_bin", has(Items.CHEST))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FORGE.get())
                .pattern("ccc")
                .pattern("ibi")
                .pattern("clc")
                .define('c', RunecraftoryTags.Items.COBBLESTONE)
                .define('i', RunecraftoryTags.Items.IRON)
                .define('b', Items.BLAST_FURNACE)
                .define('l', Items.LAVA_BUCKET)
                .unlockedBy("forge_recipe", has(Items.LAVA_BUCKET))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ACCESSORY_WORKBENCH.get())
                .pattern(" s ")
                .pattern("aaa")
                .pattern("lcl")
                .define('s', RunecraftoryTags.Items.SHEARS)
                .define('a', ItemTags.WOODEN_SLABS)
                .define('c', Items.CRAFTING_TABLE)
                .define('l', ItemTags.LOGS)
                .unlockedBy("accessory_recipe", has(Items.CRAFTING_TABLE))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CHEMISTRY_SET.get())
                .pattern("b s")
                .pattern("qqq")
                .pattern("ccc")
                .define('b', Items.GLASS_BOTTLE)
                .define('s', Items.BREWING_STAND)
                .define('c', Items.CYAN_TERRACOTTA)
                .define('q', Items.QUARTZ_BLOCK)
                .unlockedBy("chemistry_recipe", has(Items.BREWING_STAND))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.COOKING_TABLE.get())
                .pattern("   ")
                .pattern("qwq")
                .pattern("lsl")
                .define('q', Items.QUARTZ_BLOCK)
                .define('w', Items.WATER_BUCKET)
                .define('s', Items.SMOKER)
                .define('l', ItemTags.LOGS)
                .unlockedBy("cooking_recipe", has(Items.SMOKER))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.QUEST_BOARD.get())
                .pattern("PPP")
                .pattern("SSS")
                .pattern("PPP")
                .define('S', ItemTags.SIGNS)
                .define('P', Items.PAPER)
                .unlockedBy("quest_board", has(ItemTags.SIGNS))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CASH_REGISTER.get())
                .pattern("wgw")
                .pattern("ece")
                .pattern("www")
                .define('w', Items.WHITE_CONCRETE)
                .define('e', RunecraftoryTags.Items.GEMS_EMERALD)
                .define('c', RunecraftoryTags.Items.CHEST)
                .define('g', Items.GRAY_CONCRETE)
                .unlockedBy("shipping_bin", has(Items.CHEST))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.TELEPORT.get())
                .pattern(" e ")
                .pattern("ebe")
                .pattern(" e ")
                .define('e', Items.ENDER_PEARL)
                .define('b', ItemTags.BEDS)
                .unlockedBy("teleport", has(ItemTags.BEDS))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.FIRE_BALL_SMALL.get())
                .pattern("bcb")
                .pattern("clc")
                .pattern("bcb")
                .define('b', Items.BLAZE_POWDER)
                .define('c', Items.FIRE_CHARGE)
                .define('l', Items.LAVA_BUCKET)
                .unlockedBy("fireball", has(Items.LAVA_BUCKET))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.LOVE_LETTER.get())
                .pattern(" P ")
                .pattern("PFP")
                .pattern(" P ")
                .define('F', RunecraftoryTags.Items.FLOWERS)
                .define('P', Items.PAPER)
                .unlockedBy("love_letter", has(RunecraftoryTags.Items.FLOWERS))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DIVORCE_PAPER.get())
                .pattern(" P ")
                .pattern("PSP")
                .pattern(" P ")
                .define('S', ModItems.SCRAP.get())
                .define('P', Items.PAPER)
                .unlockedBy("divorce_paper", has(Items.PAPER))
                .save(output);

        output.accept(RuneCraftory.modRes("bronze_dust"), new HammerRemainderRecipe("", CraftingBookCategory.MISC, ModItems.BRONZE_DUST.get().getDefaultInstance(),
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
        LevelUpUpgradeRecipe.build(output, 7, Ingredient.of(RunecraftoryTags.Items.MAGIC_SPELLS), Ingredient.of(ModItems.DRAGONIC.get()),
                RuneCraftory.modRes("spells_tier_8"));
        LevelUpUpgradeRecipe.build(output, 8, Ingredient.of(RunecraftoryTags.Items.MAGIC_SPELLS), Ingredient.of(ModItems.CRYSTAL_RUNE.get()),
                RuneCraftory.modRes("spells_tier_9"));
        LevelUpUpgradeRecipe.build(output, 9, Ingredient.of(RunecraftoryTags.Items.MAGIC_SPELLS), Ingredient.of(ModItems.RUNE_SPHERE_SHARD.get()),
                RuneCraftory.modRes("spells_tier_10"));

        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.HOE_SCRAP.get(), 1, 5, 0)
                .addIngredient(RunecraftoryTags.Items.STICKS).addIngredient(RunecraftoryTags.Items.MINERALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.HOE_IRON.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.STICKS).addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.HOE_SILVER.get(), 1, 30, 0)
                .addIngredient(RunecraftoryTags.Items.STICKS).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).addIngredient(ModItems.THREAD_PRETTY.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.HOE_GOLD.get(), 1, 45, 0)
                .addIngredient(RunecraftoryTags.Items.STICKS).addIngredient(RunecraftoryTags.Items.GOLD).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.HOE_PLATINUM.get(), 1, 80, 0)
                .addIngredient(RunecraftoryTags.Items.STICKS).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM).build(output);

        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.WATERING_CAN_SCRAP.get(), 1, 5, 0)
                .addIngredient(Items.BUCKET)
                .addIngredient(RunecraftoryTags.Items.MINERALS)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.WATERING_CAN_IRON.get(), 1, 15, 0)
                .addIngredient(Items.BUCKET)
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.WATERING_CAN_SILVER.get(), 1, 30, 0)
                .addIngredient(Items.BUCKET)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .addIngredient(ModItems.CLOTH_QUALITY.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.WATERING_CAN_GOLD.get(), 1, 45, 0)
                .addIngredient(Items.BUCKET)
                .addIngredient(RunecraftoryTags.Items.GOLD)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.WATERING_CAN_PLATINUM.get(), 1, 80, 0)
                .addIngredient(Items.BUCKET)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);

        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.SICKLE_SCRAP.get(), 1, 5, 0)
                .addIngredient(RunecraftoryTags.Items.MINERALS)
                .addIngredient(RunecraftoryTags.Items.SHARDS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.SICKLE_IRON.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE)
                .addIngredient(RunecraftoryTags.Items.SHARDS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.SICKLE_SILVER.get(), 1, 30, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .addIngredient(ModItems.THREAD_PRETTY.get())
                .addIngredient(RunecraftoryTags.Items.SHARDS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.SICKLE_GOLD.get(), 1, 45, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD)
                .addIngredient(RunecraftoryTags.Items.SHARDS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.SICKLE_PLATINUM.get(), 1, 80, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM)
                .addIngredient(RunecraftoryTags.Items.SHARDS).build(output);

        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.HAMMER_SCRAP.get(), 1, 5, 0)
                .addIngredient(RunecraftoryTags.Items.IRON)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.HAMMER_IRON.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE)
                .addIngredient(RunecraftoryTags.Items.IRON)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.HAMMER_SILVER.get(), 1, 30, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .addIngredient(ModItems.TURTLE_SHELL.get())
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.HAMMER_GOLD.get(), 1, 45, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD)
                .addIngredient(RunecraftoryTags.Items.IRON)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.HAMMER_PLATINUM.get(), 1, 80, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM)
                .addIngredient(RunecraftoryTags.Items.IRON)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);

        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.AXE_SCRAP.get(), 1, 5, 0)
                .addIngredient(RunecraftoryTags.Items.IRON)
                .addIngredient(RunecraftoryTags.Items.STICKS)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.AXE_IRON.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE)
                .addIngredient(RunecraftoryTags.Items.STICKS)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.AXE_SILVER.get(), 1, 30, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .addIngredient(RunecraftoryTags.Items.STICKS)
                .addIngredient(ModItems.BLADE_SHARD.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.AXE_GOLD.get(), 1, 45, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD)
                .addIngredient(RunecraftoryTags.Items.STICKS)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.AXE_PLATINUM.get(), 1, 80, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM)
                .addIngredient(RunecraftoryTags.Items.STICKS)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);

        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.FISHING_ROD_SCRAP.get(), 1, 5, 0)
                .addIngredient(RunecraftoryTags.Items.STRINGS)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.FISHING_ROD_IRON.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE)
                .addIngredient(RunecraftoryTags.Items.STRINGS)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.FISHING_ROD_SILVER.get(), 1, 30, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .addIngredient(ModItems.THREAD_PRETTY.get())
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.FISHING_ROD_GOLD.get(), 1, 45, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD)
                .addIngredient(RunecraftoryTags.Items.STRINGS)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.FISHING_ROD_PLATINUM.get(), 1, 80, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM)
                .addIngredient(RunecraftoryTags.Items.STRINGS)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);

        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.BROAD_SWORD.get(), 1, 3, 0)
                .addIngredient(RunecraftoryTags.Items.MINERALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.STEEL_SWORD.get(), 1, 5, 0)
                .addIngredient(ModItems.BROAD_SWORD.get())
                .addIngredient(RunecraftoryTags.Items.MINERALS).addIngredient(RunecraftoryTags.Items.CLAWS_FANGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.STEEL_SWORD_PLUS.get(), 1, 7, 0)
                .addIngredient(ModItems.STEEL_SWORD.get())
                .addIngredient(RunecraftoryTags.Items.IRON).addIngredient(RunecraftoryTags.Items.MINERALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.CUTLASS.get(), 1, 10, 0)
                .addIngredient(ModItems.BROAD_SWORD.get())
                .addIngredient(RunecraftoryTags.Items.GEMS_EMERALD)
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).addIngredient(RunecraftoryTags.Items.CLAWS_FANGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.AQUA_SWORD.get(), 1, 13, 0)
                .addIngredient(ModItems.BROAD_SWORD.get())
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).addIngredient(ModItems.AQUAMARINE.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.INVISI_BLADE.get(), 1, 16, 0)
                .addIngredient(ModItems.BROAD_SWORD.get())
                .addIngredient(ModItems.INVIS_STONE.get()).addIngredient(RunecraftoryTags.Items.CLAWS_FANGS).addIngredient(RunecraftoryTags.Items.CRYSTALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.DEFENDER.get(), 1, 20, 0)
                .addIngredient(ModItems.BROAD_SWORD.get())
                .addIngredient(RunecraftoryTags.Items.GEMS_SAPPHIRE).addIngredient(Items.SHIELD)
                .addIngredient(RunecraftoryTags.Items.CLAWS_FANGS)
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.BURNING_SWORD.get(), 1, 24, 0)
                .addIngredient(ModItems.BROAD_SWORD.get())
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(ModItems.CRYSTAL_FIRE.get())
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.GORGEOUS_SWORD.get(), 1, 27, 20)
                .addIngredient(ModItems.BROAD_SWORD.get())
                .addIngredient(Items.GOLD_BLOCK).addIngredient(Items.DIAMOND)
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.GAIA_SWORD.get(), 1, 32, 0)
                .addIngredient(ModItems.BROAD_SWORD.get())
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(ModItems.CRYSTAL_EARTH.get())
                .addIngredient(ModItems.HORN_RIGID.get())
                .build(output);

        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.CLAYMORE.get(), 1, 3, 0)
                .addIngredient(RunecraftoryTags.Items.MINERALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.ZWEIHAENDER.get(), 1, 5, 0)
                .addIngredient(ModItems.CLAYMORE.get())
                .addIngredient(RunecraftoryTags.Items.MINERALS).addIngredient(RunecraftoryTags.Items.CLAWS_FANGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.ZWEIHAENDER_PLUS.get(), 1, 7, 0)
                .addIngredient(ModItems.ZWEIHAENDER.get())
                .addIngredient(RunecraftoryTags.Items.IRON).addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.GREAT_SWORD.get(), 1, 13, 0)
                .addIngredient(ModItems.CLAYMORE.get())
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).addIngredient(RunecraftoryTags.Items.LIQUIDS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.SEA_CUTTER.get(), 1, 15, 0)
                .addIngredient(ModItems.CLAYMORE.get())
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).addIngredient(ModItems.AQUAMARINE.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.CYCLONE_BLADE.get(), 1, 18, 0)
                .addIngredient(ModItems.CLAYMORE.get())
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(Items.EMERALD).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.POISON_BLADE.get(), 1, 21, 0)
                .addIngredient(ModItems.CLAYMORE.get())
                .addIngredient(ModItems.POWDER_POISON.get()).addIngredient(RunecraftoryTags.Items.GOLD)
                .addIngredient(Items.POISONOUS_POTATO)
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.KATZBALGER.get(), 1, 23, 0)
                .addIngredient(ModItems.CLAYMORE.get())
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .addIngredient(ModItems.HORN_RIGID.get())
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.EARTH_SHADE.get(), 1, 26, 0)
                .addIngredient(ModItems.CLAYMORE.get())
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .addIngredient(ModItems.CRYSTAL_EARTH.get())
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.BIG_KNIFE.get(), 1, 31, 0)
                .addIngredient(Items.IRON_SWORD)
                .addIngredient(Items.DIAMOND).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .build(output);

        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.SPEAR.get(), 1, 3, 0)
                .addIngredient(RunecraftoryTags.Items.STICKS).addIngredient(RunecraftoryTags.Items.MINERALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.WOOD_STAFF.get(), 1, 6, 0)
                .addIngredient(RunecraftoryTags.Items.STICKS).addIngredient(RunecraftoryTags.Items.STICKS)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.LANCE.get(), 1, 10, 0)
                .addIngredient(ModItems.SPEAR.get())
                .addIngredient(RunecraftoryTags.Items.MINERALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.LANCE_PLUS.get(), 1, 13, 0)
                .addIngredient(ModItems.LANCE.get())
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.NEEDLE_SPEAR.get(), 1, 16, 0)
                .addIngredient(ModItems.SPEAR.get())
                .addIngredient(RunecraftoryTags.Items.STICKS).addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE)
                .addIngredient(ModItems.FANG_WOLF.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.TRIDENT.get(), 1, 19, 0)
                .addIngredient(Items.TRIDENT)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.WATER_SPEAR.get(), 1, 22, 0)
                .addIngredient(Items.TRIDENT)
                .addIngredient(RunecraftoryTags.Items.GEMS_AQUAMARINE).addIngredient(RunecraftoryTags.Items.GEMS_AQUAMARINE)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.HALBERD.get(), 1, 24, 0)
                .addIngredient(ModItems.SPEAR.get())
                .addIngredient(RunecraftoryTags.Items.IRON).addIngredient(RunecraftoryTags.Items.GOLD)
                .addIngredient(ModItems.BLADE_SHARD.get())
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.CORSESCA.get(), 1, 27, 0)
                .addIngredient(ModItems.SPEAR.get())
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(ModItems.HORN_RIGID.get())
                .build(output);
//        RecipeBuilder.create(EnumCrafting.FORGE, ModItems.CORSESCA_PLUS.get(), 1, 32, 20)
//                .addIngredient(ModItems.CORSESCA.get())
//                .build(consumer);

        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.BATTLE_AXE.get(), 1, 5, 0)
                .addIngredient(RunecraftoryTags.Items.MINERALS).addIngredient(RunecraftoryTags.Items.CLAWS_FANGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.BATTLE_SCYTHE.get(), 1, 9, 0)
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).addIngredient(RunecraftoryTags.Items.CLAWS_FANGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.POLE_AXE.get(), 1, 15, 0)
                .addIngredient(ModItems.BATTLE_AXE.get())
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).addIngredient(ModItems.FANG_WOLF.get()).addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.POLE_AXE_PLUS.get(), 1, 19, 0)
                .addIngredient(ModItems.POLE_AXE.get()).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.GREAT_AXE.get(), 1, 24, 0)
                .addIngredient(ModItems.BATTLE_AXE.get())
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RunecraftoryTags.Items.CLAWS_FANGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.TOMAHAWK.get(), 1, 28, 0)
                .addIngredient(ModItems.BATTLE_AXE.get())
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).addIngredient(RunecraftoryTags.Items.GEMS_EMERALD)
                .addIngredient(ModItems.CLOTH_SILK.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.BASILISK_FANG.get(), 1, 30, 0)
                .addIngredient(ModItems.BATTLE_AXE.get())
                .addIngredient(ModItems.PARA_POISON.get()).addIngredient(RunecraftoryTags.Items.CLAWS_FANGS)
                .addIngredient(RunecraftoryTags.Items.CLAWS_FANGS)
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.ROCK_AXE.get(), 1, 33, 0)
                .addIngredient(ModItems.BATTLE_AXE.get())
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(ModItems.CRYSTAL_EARTH.get())
                .addIngredient(ModItems.GLOVE_GIANT.get())
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.DEMON_AXE.get(), 1, 37, 0)
                .addIngredient(ModItems.BATTLE_AXE.get())
                .addIngredient(Items.DIAMOND).addIngredient(ModItems.SCORPION_PINCER.get())
                .addIngredient(ModItems.DEVIL_BLOOD.get())
                .build(output);

        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.BATTLE_HAMMER.get(), 1, 3, 0)
                .addIngredient(RunecraftoryTags.Items.MINERALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.BAT.get(), 1, 8, 0)
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.WAR_HAMMER.get(), 1, 14, 0)
                .addIngredient(ModItems.BATTLE_HAMMER.get())
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).addIngredient(RunecraftoryTags.Items.SHARDS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.WAR_HAMMER_PLUS.get(), 1, 17, 0)
                .addIngredient(ModItems.WAR_HAMMER.get()).addIngredient(RunecraftoryTags.Items.LIQUIDS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.IRON_BAT.get(), 1, 21, 0)
                .addIngredient(ModItems.BAT.get()).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.GREAT_HAMMER.get(), 1, 25, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).addIngredient(ModItems.THREAD_PRETTY.get()).addIngredient(RunecraftoryTags.Items.SHARDS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.ICE_HAMMER.get(), 1, 28, 0)
                .addIngredient(ModItems.BATTLE_HAMMER.get())
                .addIngredient(RunecraftoryTags.Items.GOLD)
                .addIngredient(Items.PACKED_ICE).addIngredient(RunecraftoryTags.Items.GEMS_AQUAMARINE)
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.BONE_HAMMER.get(), 1, 31, 0)
                .addIngredient(ModItems.BATTLE_HAMMER.get())
                .addIngredient(Items.BONE_BLOCK).addIngredient(RunecraftoryTags.Items.GOLD)
                .addIngredient(RunecraftoryTags.Items.SHELLS_BONES)
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.STRONG_STONE.get(), 1, 35, 0)
                .addIngredient(ModItems.BATTLE_HAMMER.get())
                .addIngredient(Items.DIAMOND).addIngredient(RunecraftoryTags.Items.STRINGS)
                .addIngredient(Items.IRON_BLOCK)
                .build(output);

        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.SHORT_DAGGER.get(), 1, 3, 0)
                .addIngredient(RunecraftoryTags.Items.MINERALS).addIngredient(RunecraftoryTags.Items.MINERALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.STEEL_EDGE.get(), 1, 7, 0)
                .addIngredient(ModItems.SHORT_DAGGER.get())
                .addIngredient(RunecraftoryTags.Items.IRON).addIngredient(RunecraftoryTags.Items.IRON).addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.FROST_EDGE.get(), 1, 11, 0)
                .addIngredient(ModItems.SHORT_DAGGER.get())
                .addIngredient(Items.ICE).addIngredient(ModItems.AQUAMARINE.get()).addIngredient(RunecraftoryTags.Items.LIQUIDS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.IRON_EDGE.get(), 1, 15, 0)
                .addIngredient(ModItems.SHORT_DAGGER.get())
                .addIngredient(RunecraftoryTags.Items.IRON).addIngredient(RunecraftoryTags.Items.IRON).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.THIEF_KNIFE.get(), 1, 17, 0)
                .addIngredient(ModItems.SHORT_DAGGER.get())
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.WIND_EDGE.get(), 1, 20, 0)
                .addIngredient(ModItems.SHORT_DAGGER.get())
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(Items.EMERALD)
                .addIngredient(ModItems.CRYSTAL_WIND.get()).addIngredient(RunecraftoryTags.Items.CLAWS_FANGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.GORGEOUS_LX.get(), 1, 23, 0)
                .addIngredient(ModItems.GORGEOUS_SWORD.get()).addIngredient(ModItems.GORGEOUS_SWORD.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.STEEL_KATANA.get(), 1, 26, 0)
                .addIngredient(ModItems.SHORT_DAGGER.get())
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RunecraftoryTags.Items.GOLD)
                .addIngredient(ModItems.FEATHER_BLACK.get())
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.TWIN_BLADE.get(), 1, 28, 0)
                .addIngredient(ModItems.SHORT_DAGGER.get())
                .addIngredient(ModItems.POWDER_POISON.get()).addIngredient(ModItems.HORN_RIGID.get())
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.RAMPAGE.get(), 1, 31, 0)
                .addIngredient(ModItems.SHORT_DAGGER.get())
                .addIngredient(Items.GOLD_BLOCK).addIngredient(Items.DIAMOND)
                .addIngredient(ModItems.FANG_GOLD_WOLF.get())
                .build(output);

        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.LEATHER_GLOVE.get(), 1, 3, 0)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.BRASS_KNUCKLES.get(), 1, 8, 0)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.KOTE.get(), 1, 12, 0)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).addIngredient(RunecraftoryTags.Items.IRON).addIngredient(ModItems.CLAW_PALM.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.GLOVES.get(), 1, 14, 0)
                .addIngredient(ModItems.LEATHER_GLOVE.get())
                .addIngredient(Items.STRING).addIngredient(ModItems.FUR_MEDIUM.get())
                .addIngredient(ModItems.CLOTH_QUALITY.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.BEAR_CLAWS.get(), 1, 16, 0)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).addIngredient(ModItems.CLAW_PANTHER.get())
                .addIngredient(RunecraftoryTags.Items.GOLD).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.FIST_EARTH.get(), 1, 22, 0)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).addIngredient(RunecraftoryTags.Items.GOLD)
                .addIngredient(ModItems.HORN_RIGID.get()).addIngredient(ModItems.CRYSTAL_EARTH.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.FIST_FIRE.get(), 1, 25, 0)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).addIngredient(Items.OBSIDIAN)
                .addIngredient(Items.DIAMOND).addIngredient(ModItems.CRYSTAL_FIRE.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.FIST_WATER.get(), 1, 27, 0)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).addIngredient(RunecraftoryTags.Items.GEMS_AQUAMARINE)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM).addIngredient(ModItems.CRYSTAL_WATER.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.DRAGON_CLAWS.get(), 1, 30, 0)
                .addIngredient(ModItems.LEATHER_GLOVE.get())
                .addIngredient(RunecraftoryTags.Items.SCALES).addIngredient(ModItems.FANG_DRAGON.get())
                .build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.FIST_DARK.get(), 1, 33, 0)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).addIngredient(ModItems.CURSED_DOLL.get())
                .addIngredient(ModItems.CLAW_MALM.get()).addIngredient(ModItems.CRYSTAL_DARK.get()).build(output);

        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.ROD.get(), 1, 3, 0)
                .addIngredient(RunecraftoryTags.Items.STICKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.AMETHYST_ROD.get(), 1, 6, 0)
                .addIngredient(ModItems.ROD.get())
                .addIngredient(RunecraftoryTags.Items.GEMS_AMETHYST).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.AQUAMARINE_ROD.get(), 1, 12, 0)
                .addIngredient(ModItems.ROD.get())
                .addIngredient(RunecraftoryTags.Items.GEMS_AQUAMARINE).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.FRIENDLY_ROD.get(), 1, 16, 0)
                .addIngredient(ModItems.ROD.get())
                .addIngredient(ModItems.CRYSTAL_LOVE.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.LOVE_LOVE_ROD.get(), 1, 19, 0)
                .addIngredient(ModItems.FRIENDLY_ROD.get()).addIngredient(ModItems.CRYSTAL_LOVE.get())
                .addIngredient(ModItems.CRYSTAL_LOVE.get()).addIngredient(ModItems.CRYSTAL_LOVE.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.STAFF.get(), 1, 22, 0)
                .addIngredient(ModItems.ROD.get())
                .addIngredient(ModItems.CRYSTAL_MAGIC.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.EMERALD_ROD.get(), 1, 24, 0)
                .addIngredient(ModItems.ROD.get())
                .addIngredient(RunecraftoryTags.Items.GEMS_EMERALD).addIngredient(RunecraftoryTags.Items.GEMS_EMERALD).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.SILVER_STAFF.get(), 1, 28, 0)
                .addIngredient(ModItems.ROD.get())
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.FLARE_STAFF.get(), 1, 30, 0)
                .addIngredient(ModItems.ROD.get())
                .addIngredient(ModItems.CRYSTAL_FIRE.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.RUBY_ROD.get(), 1, 32, 0)
                .addIngredient(ModItems.ROD.get())
                .addIngredient(RunecraftoryTags.Items.GEMS_RUBY).build(output);
        SextupleRecipeBuilder.create(CraftingType.FORGE, ModItems.SAPPHIRE_ROD.get(), 1, 36, 0)
                .addIngredient(ModItems.ROD.get())
                .addIngredient(RunecraftoryTags.Items.GEMS_SAPPHIRE).build(output);

        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.YARN.get(), 1, 5, 0)
                .addIngredient(RunecraftoryTags.Items.FURS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.ENGAGEMENT_RING.get(), 1, 20, 0)
                .addIngredient(RunecraftoryTags.Items.MINERALS).addIngredient(RunecraftoryTags.Items.JEWELS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.CHEAP_BRACELET.get(), 1, 3, 0)
                .addIngredient(RunecraftoryTags.Items.MINERALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.BRONZE_BRACELET.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.SILVER_BRACELET.get(), 1, 25, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.GOLD_BRACELET.get(), 1, 40, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.PLATINUM_BRACELET.get(), 1, 60, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM).addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.SILVER_RING.get(), 1, 20, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).addIngredient(RunecraftoryTags.Items.CRYSTALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.GOLD_RING.get(), 1, 20, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(ModItems.ORICHALCUM.get()).addIngredient(ModItems.TURNIPS_MIRACLE.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.PLATINUM_RING.get(), 1, 70, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM).addIngredient(ModItems.DRAGONIC.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.SHIELD_RING.get(), 1, 40, 0)
                .addIngredient(Items.SHIELD).addIngredient(ModItems.TURTLE_SHELL.get())
                .addIngredient(ModItems.TORTOISE_SHELL.get()).addIngredient(RunecraftoryTags.Items.MINERALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.CRITICAL_RING.get(), 1, 30, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(ModItems.HORN_RIGID.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.SILENT_RING.get(), 1, 30, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(ModItems.LAMP_SQUID.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.PARALYSIS_RING.get(), 1, 30, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(ModItems.TAIL_SCORPION.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.POISON_RING.get(), 1, 30, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(ModItems.POWDER_POISON.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.MAGIC_RING.get(), 1, 55, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM)
                .addIngredient(Items.EXPERIENCE_BOTTLE).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.THROWING_RING.get(), 1, 25, 0)
                .addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).addIngredient(Items.DISPENSER)
                .addIngredient(ModItems.PUPPETRY_STRINGS.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.STAY_UP_RING.get(), 1, 27, 0)
                .addIngredient(ModItems.SPORE_HOLY.get()).addIngredient(RunecraftoryTags.Items.MINERALS)
                .addIngredient(Items.PHANTOM_MEMBRANE).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.AQUAMARINE_RING.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RunecraftoryTags.Items.GEMS_AQUAMARINE).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.AMETHYST_RING.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RunecraftoryTags.Items.GEMS_AMETHYST).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.EMERALD_RING.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RunecraftoryTags.Items.GEMS_EMERALD).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.SAPPHIRE_RING.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RunecraftoryTags.Items.GEMS_SAPPHIRE).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.RUBY_RING.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.GOLD).addIngredient(RunecraftoryTags.Items.GEMS_RUBY).build(output);

        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.SHIRT.get(), 1, 2, 0)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.VEST.get(), 1, 7, 0)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).addIngredient(RunecraftoryTags.Items.FURS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.COTTON_CLOTH.get(), 1, 13, 0)
                .addIngredient(ModItems.OLD_BANDAGE.get()).addIngredient(ModItems.OLD_BANDAGE.get()).addIngredient(RunecraftoryTags.Items.STRINGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.MAIL.get(), 1, 18, 0)
                .addIngredient(Items.LIGHT_BLUE_WOOL).addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).addIngredient(RunecraftoryTags.Items.LIQUIDS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.CHAIN_MAIL.get(), 1, 21, 0)
                .addIngredient(Items.CHAIN).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER).addIngredient(RunecraftoryTags.Items.STRINGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.SCALE_VEST.get(), 1, 25, 0)
                .addIngredient(Items.IRON_CHESTPLATE).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .addIngredient(ModItems.CARAPACE_PRETTY.get()).addIngredient(ModItems.ROOT.get()).build(output);

        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.HEADBAND.get(), 1, 1, 0)
                .addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.BLUE_RIBBON.get(), 1, 7, 0)
                .addIngredient(ModItems.BLUE_GRASS.get()).addIngredient(RunecraftoryTags.Items.CLOTHS).addIngredient(RunecraftoryTags.Items.STRINGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.GREEN_RIBBON.get(), 1, 7, 0)
                .addIngredient(ModItems.GREEN_GRASS.get()).addIngredient(RunecraftoryTags.Items.CLOTHS).addIngredient(RunecraftoryTags.Items.STRINGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.PURPLE_RIBBON.get(), 1, 7, 0)
                .addIngredient(ModItems.PURPLE_GRASS.get()).addIngredient(RunecraftoryTags.Items.CLOTHS).addIngredient(RunecraftoryTags.Items.STRINGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.SPECTACLES.get(), 1, 10, 0)
                .addIngredient(Items.GLASS).addIngredient(RunecraftoryTags.Items.GEMS_AMETHYST)
                .addIngredient(RunecraftoryTags.Items.GEMS_AQUAMARINE).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.STRAW_HAT.get(), 1, 12, 0)
                .addIngredient(Items.HAY_BLOCK).addIngredient(RunecraftoryTags.Items.STRINGS).addIngredient(RunecraftoryTags.Items.STRINGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.FANCY_HAT.get(), 1, 15, 0)
                .addIngredient(Items.RED_WOOL).addIngredient(RunecraftoryTags.Items.STRINGS).addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);

        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.LEATHER_BOOTS.get(), 1, 5, 0)
                .addIngredient(Items.LEATHER_BOOTS).addIngredient(RunecraftoryTags.Items.FURS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.FREE_FARMING_SHOES.get(), 1, 8, 0)
                .addIngredient(RunecraftoryTags.Items.FURS).addIngredient(RunecraftoryTags.Items.STRINGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.PIYO_SANDALS.get(), 1, 11, 0)
                .addIngredient(ModItems.CARAPACE_INSECT.get()).addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.SECRET_SHOES.get(), 1, 14, 0)
                .addIngredient(Items.LEATHER_BOOTS).addIngredient(RunecraftoryTags.Items.MINERALS)
                .addIngredient(ModItems.GLUE.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.SILVER_BOOTS.get(), 1, 18, 0)
                .addIngredient(Items.LEATHER_BOOTS).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .addIngredient(RunecraftoryTags.Items.SHARDS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.HEAVY_BOOTS.get(), 1, 21, 0)
                .addIngredient(Items.IRON_BOOTS).addIngredient(RunecraftoryTags.Items.RAW_MATERIALS_SILVER)
                .addIngredient(ModItems.CARAPACE_PRETTY.get()).build(output);

        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.SMALL_SHIELD.get(), 1, 2, 0)
                .addIngredient(Items.SHIELD).addIngredient(RunecraftoryTags.Items.MINERALS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.UMBRELLA.get(), 1, 7, 0)
                .addIngredient(RunecraftoryTags.Items.STICKS).addIngredient(RunecraftoryTags.Items.STRINGS).addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.IRON_SHIELD.get(), 1, 10, 0)
                .addIngredient(Items.SHIELD).addIngredient(RunecraftoryTags.Items.IRON).addIngredient(RunecraftoryTags.Items.IRON).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.MONKEY_PLUSH.get(), 1, 14, 0)
                .addIngredient(ModItems.DOWN_YELLOW.get()).addIngredient(RunecraftoryTags.Items.CLOTHS).addIngredient(RunecraftoryTags.Items.CLOTHS).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.ROUND_SHIELD.get(), 1, 18, 0)
                .addIngredient(Items.SHIELD).addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).build(output);
        SextupleRecipeBuilder.create(CraftingType.ACCESSORY_WORKBENCH, ModItems.TURTLE_SHIELD.get(), 1, 23, 0)
                .addIngredient(Items.SHIELD).addIngredient(ModItems.TURTLE_SHELL.get()).addIngredient(RunecraftoryTags.Items.DUSTS_BRONZE).build(output);

        SextupleRecipeBuilder.create(CraftingType.CHEMISTRY_SET, ModItems.RECOVERY_POTION.get(), 2, 3, 0)
                .addIngredient(ModItems.MEDICINAL_HERB.get())
                .addIngredient(ModItems.MEDICINAL_HERB.get())
                .addIngredient(ModItems.GREEN_GRASS.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.CHEMISTRY_SET, ModItems.HEALING_POTION.get(), 2, 20, 0)
                .addIngredient(ModItems.MEDICINAL_HERB.get())
                .addIngredient(ModItems.MEDICINAL_HERB.get())
                .addIngredient(ModItems.RED_GRASS.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.CHEMISTRY_SET, ModItems.MYSTERY_POTION.get(), 2, 45, 0)
                .addIngredient(ModItems.MEDICINAL_HERB.get())
                .addIngredient(ModItems.MEDICINAL_HERB.get())
                .addIngredient(ModItems.WHITE_GRASS.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.CHEMISTRY_SET, ModItems.MAGICAL_POTION.get(), 2, 70, 0)
                .addIngredient(ModItems.MEDICINAL_HERB.get())
                .addIngredient(ModItems.MEDICINAL_HERB.get())
                .addIngredient(ModItems.ELLI_LEAVES.get()).build(output);

        // Simple
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.ONIGIRI.get(), 1, 1, 0)
                .addIngredient(RunecraftoryTags.Items.RICE).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.CHEESE.get(), 1, 5, 0)
                .addIngredient(ModItems.SOUR_DROP.get()).addIngredient(RunecraftoryTags.Items.MILKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.PICKLED_TURNIP.get(), 1, 5, 0)
                .addIngredient(RunecraftoryTags.Items.TURNIP).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.SALMON_ONIGIRI.get(), 1, 20, 0)
                .addIngredient(ModItems.SALTED_SALMON.get()).addIngredient(RunecraftoryTags.Items.RICE).build(output);

        // Steamer
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.FLAN.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.EGGS).addIngredient(RunecraftoryTags.Items.MILKS).build(output);

        // Mixer
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.MAYONNAISE.get(), 1, 3, 0)
                .addIngredient(RunecraftoryTags.Items.EGGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.BUTTER.get(), 1, 3, 0)
                .addIngredient(RunecraftoryTags.Items.MILKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.KETCHUP.get(), 1, 5, 0)
                .addIngredient(ModItems.SOUR_DROP.get()).addIngredient(RunecraftoryTags.tagCommon("crops/tomato")).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.APPLE_JUICE.get(), 1, 8, 0)
                .addIngredient(Items.APPLE).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.ORANGE_JUICE.get(), 1, 12, 0)
                .addIngredient(RunecraftoryTags.Items.ORANGE).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.GRAPE_JUICE.get(), 1, 19, 0)
                .addIngredient(RunecraftoryTags.Items.GRAPES).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.TOMATO_JUICE.get(), 1, 26, 0)
                .addIngredient(RunecraftoryTags.tagCommon("crops/tomato")).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.PINEAPPLE_JUICE.get(), 1, 66, 0)
                .addIngredient(RunecraftoryTags.tagCommon("crops/pineapple")).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.FRUIT_JUICE.get(), 1, 30, 0)
                .addIngredient(Items.APPLE).addIngredient(RunecraftoryTags.Items.ORANGE)
                .addIngredient(RunecraftoryTags.Items.GRAPES).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.FRUIT_SMOOTHIE.get(), 1, 44, 0)
                .addIngredient(ModItems.FRUIT_JUICE.get()).addIngredient(RunecraftoryTags.Items.MILKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.VEGETABLE_JUICE.get(), 1, 27, 0)
                .addIngredient(RunecraftoryTags.tagCommon("crops/pumpkin")).addIngredient(RunecraftoryTags.tagCommon("crops/turnip"))
                .addIngredient(RunecraftoryTags.tagCommon("crops/carrot")).addIngredient(RunecraftoryTags.tagCommon("crops/spinach")).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.VEGGIE_SMOOTHIE.get(), 1, 37, 0)
                .addIngredient(ModItems.VEGETABLE_JUICE.get()).addIngredient(RunecraftoryTags.Items.MILKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.MIXED_JUICE.get(), 1, 38, 0)
                .addIngredient(ModItems.FRUIT_JUICE.get()).addIngredient(ModItems.VEGETABLE_JUICE.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.MIXED_SMOOTHIE.get(), 1, 55, 0)
                .addIngredient(ModItems.MIXED_SMOOTHIE.get()).addIngredient(RunecraftoryTags.Items.MILKS).build(output);

        // Oven
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.BAKED_ONIGIRI.get(), 1, 10, 0)
                .addIngredient(ModItems.ONIGIRI.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.TOAST.get(), 1, 11, 0)
                .addIngredient(RunecraftoryTags.Items.BREAD).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.APPLE_PIE.get(), 1, 25, 0)
                .addIngredient(RunecraftoryTags.Items.FLOUR).addIngredient(Items.APPLE)
                .addIngredient(RunecraftoryTags.Items.MILKS).addIngredient(RunecraftoryTags.Items.EGGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.CHEESECAKE.get(), 1, 35, 0)
                .addIngredient(RunecraftoryTags.Items.SUGAR).addIngredient(RunecraftoryTags.Items.CHEESE)
                .addIngredient(RunecraftoryTags.Items.MILKS).addIngredient(RunecraftoryTags.Items.EGGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.CHOCOLATE_CAKE.get(), 1, 50, 0)
                .addIngredient(RunecraftoryTags.Items.BUTTER).addIngredient(RunecraftoryTags.Items.FLOUR)
                .addIngredient(RunecraftoryTags.Items.SUGAR).addIngredient(RunecraftoryTags.Items.CHOCOLATE)
                .addIngredient(RunecraftoryTags.Items.MILKS).addIngredient(RunecraftoryTags.Items.EGGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.COOKIE.get(), 1, 30, 0)
                .addIngredient(RunecraftoryTags.Items.BUTTER).addIngredient(RunecraftoryTags.Items.FLOUR)
                .addIngredient(RunecraftoryTags.Items.SUGAR).addIngredient(RunecraftoryTags.Items.EGGS).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.CHOCO_COOKIE.get(), 1, 45, 0)
                .addIngredient(RunecraftoryTags.Items.BUTTER).addIngredient(RunecraftoryTags.Items.FLOUR)
                .addIngredient(RunecraftoryTags.Items.SUGAR).addIngredient(RunecraftoryTags.Items.CHOCOLATE)
                .addIngredient(RunecraftoryTags.Items.EGGS).build(output);

        // Pot
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.YOGURT.get(), 1, 7, 0)
                .addIngredient(RunecraftoryTags.Items.MILKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.MARMALADE.get(), 1, 10, 0)
                .addIngredient(RunecraftoryTags.Items.ORANGE).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.APPLE_JAM.get(), 1, 8, 0)
                .addIngredient(Items.APPLE).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.GRAPE_JAM.get(), 1, 12, 0)
                .addIngredient(RunecraftoryTags.Items.GRAPES).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.STRAWBERRY_JAM.get(), 1, 12, 0)
                .addIngredient(RunecraftoryTags.tagCommon("fruits/strawberry")).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.HOT_MILK.get(), 1, 15, 0)
                .addIngredient(RunecraftoryTags.Items.MILKS).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.HOT_CHOCOLATE.get(), 1, 20, 0)
                .addIngredient(RunecraftoryTags.Items.CHOCOLATE).addIngredient(RunecraftoryTags.Items.CHOCOLATE).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.UDON.get(), 1, 17, 0)
                .addIngredient(RunecraftoryTags.Items.FLOUR).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.TEMPURA_UDON.get(), 1, 33, 0)
                .addIngredient(ModItems.TEMPURA.get()).addIngredient(ModItems.UDON.get()).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.CURRY_UDON.get(), 1, 52, 0)
                .addIngredient(ModItems.UDON.get()).addIngredient(ModItems.CURRY_POWDER.get())
                .addIngredient(RunecraftoryTags.tagCommon("crops/carrot")).addIngredient(ModItems.HEAVY_SPICE.get()).build(output);

        // Frying
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.BAKED_APPLE.get(), 1, 5, 0)
                .addIngredient(Items.APPLE).build(output);
        SextupleRecipeBuilder.create(CraftingType.COOKING_TABLE, ModItems.FRIED_VEGGIES.get(), 1, 25, 0)
                .addIngredient(RunecraftoryTags.tagCommon("crops/cabbage")).build(output);

        // Knife

    }

//    private FinishedRecipe patchouliShapelessBook(ResourceLocation id, ResourceLocation book, Ingredient... ingredients) {
//        return new FinishedRecipe() {
//
//            @Override
//            public JsonObject serializeRecipe() {
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("type", "patchouli:shapeless_book_recipe");
//                this.serializeRecipeData(jsonObject);
//                return jsonObject;
//            }
//
//            @Override
//            public void serializeRecipeData(JsonObject json) {
//                JsonArray arr = new JsonArray();
//                for (Ingredient ing : ingredients)
//                    arr.add(ing.toJson());
//                json.add("ingredients", arr);
//                json.addProperty("book", book.toString());
//            }
//
//            @Override
//            public ResourceLocation getId() {
//                return id;
//            }
//
//            @Override
//            public RecipeSerializer<?> getType() {
//                return null;
//            }
//
//            @Nullable
//            @Override
//            public JsonObject serializeAdvancement() {
//                return null;
//            }
//
//            @Nullable
//            @Override
//            public ResourceLocation getAdvancementId() {
//                return null;
//            }
//        };
//    }
}
