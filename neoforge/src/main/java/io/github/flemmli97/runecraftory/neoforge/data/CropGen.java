package io.github.flemmli97.runecraftory.neoforge.data;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.calendar.Season;
import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.api.datapack.provider.CropProvider;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class CropGen extends CropProvider {

    public CropGen(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
        super(packOutput, RuneCraftory.MODID, provider);
    }

    @SuppressWarnings("deprecation")
    private static Holder<Item> holderOf(Item item) {
        return item.builtInRegistryHolder();
    }

    @SuppressWarnings("deprecation")
    private static Holder<Block> holderOf(Block block) {
        return block.builtInRegistryHolder();
    }

    @Override
    protected void add(HolderLookup.Provider provider) {
        this.addStat(new CropProperties.Builder(holderOf(Items.WHEAT_SEEDS), holderOf(Items.WHEAT), holderOf(Blocks.WHEAT), 5, 3, true).addGoodSeason(Season.AUTUMN));
        this.addStat(new CropProperties.Builder(holderOf(Items.CARROT), holderOf(Items.CARROT), holderOf(Blocks.CARROTS),
                Pair.of(RuneCraftoryItems.CARROT_GIANT.get(), RuneCraftoryBlocks.CARROT_GIANT.get()),
                4, 4, false).addGoodSeason(Season.AUTUMN));
        this.addStat(new CropProperties.Builder(holderOf(Items.POTATO), holderOf(Items.POTATO), holderOf(Blocks.POTATOES),
                Pair.of(RuneCraftoryItems.POTATO_GIANT.get(), RuneCraftoryBlocks.POTATO_GIANT.get()),
                4, 4, false).addGoodSeason(Season.SUMMER));
        this.addStat(new CropProperties.Builder(holderOf(Items.BEETROOT_SEEDS), holderOf(Items.BEETROOT), holderOf(Blocks.BEETROOTS), 5, 3, false).addGoodSeason(Season.SPRING));

        this.addStat(new CropProperties.Builder(RuneCraftoryItems.TOYHERB_SEEDS.asHolder(),
                RuneCraftoryItems.TOYHERB.asHolder(), RuneCraftoryBlocks.TOYHERB.asHolder(),
                Pair.of(RuneCraftoryItems.TOYHERB_GIANT.get(), RuneCraftoryBlocks.TOYHERB_GIANT.get()), 4, 2, false)
                .addGoodSeason(Season.SPRING).addBadSeason(Season.WINTER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.MOONDROP_SEEDS.asHolder(),
                RuneCraftoryItems.MOONDROP_FLOWER.asHolder(), RuneCraftoryBlocks.MOONDROP_FLOWER.asHolder(),
                Pair.of(RuneCraftoryItems.MOONDROP_FLOWER_GIANT.get(), RuneCraftoryBlocks.MOONDROP_FLOWER_GIANT.get()), 7, 4, false)
                .addGoodSeason(Season.SPRING).addGoodSeason(Season.AUTUMN).addBadSeason(Season.WINTER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.PINK_CAT_SEEDS.asHolder(),
                RuneCraftoryItems.PINK_CAT.asHolder(), RuneCraftoryBlocks.PINK_CAT.asHolder(),
                Pair.of(RuneCraftoryItems.PINK_CAT_GIANT.get(), RuneCraftoryBlocks.PINK_CAT_GIANT.get()), 6, 3, false)
                .addGoodSeason(Season.SUMMER).addBadSeason(Season.WINTER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.CHARM_BLUE_SEEDS.asHolder(),
                RuneCraftoryItems.CHARM_BLUE.asHolder(), RuneCraftoryBlocks.CHARM_BLUE.asHolder(),
                Pair.of(RuneCraftoryItems.CHARM_BLUE_GIANT.get(), RuneCraftoryBlocks.CHARM_BLUE_GIANT.get()), 8, 3, false)
                .addGoodSeason(Season.AUTUMN).addBadSeason(Season.WINTER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.CHERRY_GRASS_SEEDS.asHolder(),
                RuneCraftoryItems.CHERRY_GRASS.asHolder(), RuneCraftoryBlocks.CHERRY_GRASS.asHolder(),
                Pair.of(RuneCraftoryItems.CHERRY_GRASS_GIANT.get(), RuneCraftoryBlocks.CHERRY_GRASS_GIANT.get()), 10, 4, false)
                .addGoodSeason(Season.SPRING).addBadSeason(Season.WINTER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.LAMP_GRASS_SEEDS.asHolder(),
                RuneCraftoryItems.LAMP_GRASS.asHolder(), RuneCraftoryBlocks.LAMP_GRASS.asHolder(),
                Pair.of(RuneCraftoryItems.LAMP_GRASS_GIANT.get(), RuneCraftoryBlocks.LAMP_GRASS.get()), 16, 3, false)
                .addGoodSeason(Season.WINTER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.BLUE_CRYSTAL_SEEDS.asHolder(),
                RuneCraftoryItems.BLUE_CRYSTAL.asHolder(), RuneCraftoryBlocks.BLUE_CRYSTAL.asHolder(),
                Pair.of(RuneCraftoryItems.BLUE_CRYSTAL_GIANT.get(), RuneCraftoryBlocks.BLUE_CRYSTAL_GIANT.get()), 55, 3, false)
                .addGoodSeason(Season.SPRING).addBadSeason(Season.SUMMER).addBadSeason(Season.AUTUMN).addBadSeason(Season.WINTER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.IRONLEAF_SEEDS.asHolder(),
                RuneCraftoryItems.IRONLEAF.asHolder(), RuneCraftoryBlocks.IRONLEAF.asHolder(),
                Pair.of(RuneCraftoryItems.IRONLEAF_GIANT.get(), RuneCraftoryBlocks.IRONLEAF_GIANT.get()), 21, 2, false)
                .addGoodSeason(Season.WINTER).addBadSeason(Season.SUMMER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.FOUR_LEAF_CLOVER_SEEDS.asHolder(),
                RuneCraftoryItems.FOUR_LEAF_CLOVER.asHolder(), RuneCraftoryBlocks.FOUR_LEAF_CLOVER.asHolder(),
                Pair.of(RuneCraftoryItems.FOUR_LEAF_CLOVER_GIANT.get(), RuneCraftoryBlocks.FOUR_LEAF_CLOVER_GIANT.get()), 28, 3, false)
                .addGoodSeason(Season.SPRING).addGoodSeason(Season.AUTUMN).addBadSeason(Season.SUMMER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.FIREFLOWER_SEEDS.asHolder(),
                RuneCraftoryItems.FIREFLOWER.asHolder(), RuneCraftoryBlocks.FIREFLOWER.asHolder(),
                Pair.of(RuneCraftoryItems.FIREFLOWER_GIANT.get(), RuneCraftoryBlocks.FIREFLOWER_GIANT.get()), 42, 4, false)
                .addGoodSeason(Season.SUMMER).addBadSeason(Season.WINTER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.GREEN_CRYSTAL_SEEDS.asHolder(),
                RuneCraftoryItems.GREEN_CRYSTAL.asHolder(), RuneCraftoryBlocks.GREEN_CRYSTAL.asHolder(),
                Pair.of(RuneCraftoryItems.GREEN_CRYSTAL_GIANT.get(), RuneCraftoryBlocks.GREEN_CRYSTAL_GIANT.get()), 70, 3, false)
                .addGoodSeason(Season.SUMMER).addBadSeason(Season.SPRING).addBadSeason(Season.AUTUMN).addBadSeason(Season.WINTER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.NOEL_GRASS_SEEDS.asHolder(),
                RuneCraftoryItems.NOEL_GRASS.asHolder(), RuneCraftoryBlocks.NOEL_GRASS.asHolder(),
                Pair.of(RuneCraftoryItems.NOEL_GRASS_GIANT.get(), RuneCraftoryBlocks.NOEL_GRASS_GIANT.get()), 33, 4, false)
                .addGoodSeason(Season.WINTER).addBadSeason(Season.SUMMER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.AUTUMN_GRASS_SEEDS.asHolder(),
                RuneCraftoryItems.AUTUMN_GRASS.asHolder(), RuneCraftoryBlocks.AUTUMN_GRASS.asHolder(),
                Pair.of(RuneCraftoryItems.AUTUMN_GRASS_GIANT.get(), RuneCraftoryBlocks.AUTUMN_GRASS_GIANT.get()), 29, 3, false)
                .addGoodSeason(Season.AUTUMN).addBadSeason(Season.WINTER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.POM_POM_GRASS_SEEDS.asHolder(),
                RuneCraftoryItems.POM_POM_GRASS.asHolder(), RuneCraftoryBlocks.POM_POM_GRASS.asHolder(),
                Pair.of(RuneCraftoryItems.POM_POM_GRASS_GIANT.get(), RuneCraftoryBlocks.POM_POM_GRASS_GIANT.get()), 14, 3, false)
                .addGoodSeason(Season.AUTUMN).addBadSeason(Season.WINTER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.RED_CRYSTAL_SEEDS.asHolder(),
                RuneCraftoryItems.RED_CRYSTAL.asHolder(), RuneCraftoryBlocks.RED_CRYSTAL.asHolder(),
                Pair.of(RuneCraftoryItems.RED_CRYSTAL_GIANT.get(), RuneCraftoryBlocks.RED_CRYSTAL_GIANT.get()), 80, 3, false)
                .addGoodSeason(Season.AUTUMN).addBadSeason(Season.SPRING).addBadSeason(Season.SUMMER).addBadSeason(Season.WINTER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.WHITE_CRYSTAL_SEEDS.asHolder(),
                RuneCraftoryItems.WHITE_CRYSTAL.asHolder(), RuneCraftoryBlocks.WHITE_CRYSTAL.asHolder(),
                Pair.of(RuneCraftoryItems.WHITE_CRYSTAL_GIANT.get(), RuneCraftoryBlocks.WHITE_CRYSTAL_GIANT.get()), 90, 3, false)
                .addGoodSeason(Season.WINTER).addBadSeason(Season.SPRING).addBadSeason(Season.SUMMER).addBadSeason(Season.AUTUMN));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.EMERY_FLOWER_SEEDS.asHolder(),
                RuneCraftoryItems.EMERY_FLOWER.asHolder(), RuneCraftoryBlocks.EMERY_FLOWER.asHolder(),
                Pair.of(RuneCraftoryItems.EMERY_FLOWER_GIANT.get(), RuneCraftoryBlocks.EMERY_FLOWER_GIANT.get()), 120, 2, false));

        this.addStat(new CropProperties.Builder(RuneCraftoryItems.TURNIP_SEEDS.asHolder(),
                RuneCraftoryItems.TURNIP.asHolder(), RuneCraftoryBlocks.TURNIP.asHolder(),
                Pair.of(RuneCraftoryItems.TURNIP_GIANT.get(), RuneCraftoryBlocks.TURNIP_GIANT.get()), 4, 3, false)
                .addGoodSeason(Season.SUMMER).addGoodSeason(Season.AUTUMN));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.TURNIP_PINK_SEEDS.asHolder(),
                RuneCraftoryItems.TURNIP_PINK.asHolder(), RuneCraftoryBlocks.TURNIP_PINK.asHolder(),
                Pair.of(RuneCraftoryItems.TURNIP_PINK_GIANT.get(), RuneCraftoryBlocks.TURNIP_PINK_GIANT.get()), 8, 3, false)
                .addGoodSeason(Season.SPRING).addBadSeason(Season.WINTER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.CABBAGE_SEEDS.asHolder(),
                RuneCraftoryItems.CABBAGE.asHolder(), RuneCraftoryBlocks.CABBAGE.asHolder(),
                Pair.of(RuneCraftoryItems.CABBAGE_GIANT.get(), RuneCraftoryBlocks.CABBAGE_GIANT.get()), 7, 3, false)
                .addGoodSeason(Season.SPRING).addGoodSeason(Season.AUTUMN).addBadSeason(Season.WINTER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.PINK_MELON_SEEDS.asHolder(),
                RuneCraftoryItems.PINK_MELON.asHolder(), RuneCraftoryBlocks.PINK_MELON.asHolder(),
                Pair.of(RuneCraftoryItems.PINK_MELON_GIANT.get(), RuneCraftoryBlocks.PINK_MELON_GIANT.get()), 7, 2, true)
                .addGoodSeason(Season.SPRING).addGoodSeason(Season.SUMMER).addBadSeason(Season.WINTER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.HOT_HOT_FRUIT_SEEDS.asHolder(),
                RuneCraftoryItems.HOT_HOT_FRUIT.asHolder(), RuneCraftoryBlocks.HOT_HOT_FRUIT.asHolder(),
                Pair.of(RuneCraftoryItems.HOT_HOT_FRUIT_GIANT.get(), RuneCraftoryBlocks.HOT_HOT_FRUIT_GIANT.get()), 31, 5, false)
                .addGoodSeason(Season.WINTER).addBadSeason(Season.SUMMER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.GOLDEN_TURNIP_SEEDS.asHolder(),
                RuneCraftoryItems.GOLDEN_TURNIP.asHolder(), RuneCraftoryBlocks.GOLDEN_TURNIP.asHolder(),
                Pair.of(RuneCraftoryItems.GOLDEN_TURNIP_GIANT.get(), RuneCraftoryBlocks.GOLDEN_TURNIP_GIANT.get()), 90, 3, false)
                .addGoodSeason(Season.WINTER).addBadSeason(Season.SUMMER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.GOLDEN_POTATO_SEEDS.asHolder(),
                RuneCraftoryItems.GOLDEN_POTATO.asHolder(), RuneCraftoryBlocks.GOLDEN_POTATO.asHolder(),
                Pair.of(RuneCraftoryItems.GOLDEN_POTATO_GIANT.get(), RuneCraftoryBlocks.GOLDEN_POTATO_GIANT.get()), 50, 3, false)
                .addGoodSeason(Season.WINTER).addBadSeason(Season.SUMMER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.GOLDEN_PUMPKIN_SEEDS.asHolder(),
                RuneCraftoryItems.GOLDEN_PUMPKIN.asHolder(), RuneCraftoryBlocks.GOLDEN_PUMPKIN.asHolder(),
                Pair.of(RuneCraftoryItems.GOLDEN_PUMPKIN_GIANT.get(), RuneCraftoryBlocks.GOLDEN_PUMPKIN_GIANT.get()), 75, 3, true)
                .addGoodSeason(Season.WINTER).addBadSeason(Season.SUMMER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.GOLDEN_CABBAGE_SEEDS.asHolder(),
                RuneCraftoryItems.GOLDEN_CABBAGE.asHolder(), RuneCraftoryBlocks.GOLDEN_CABBAGE.asHolder(),
                Pair.of(RuneCraftoryItems.GOLDEN_CABBAGE_GIANT.get(), RuneCraftoryBlocks.GOLDEN_CABBAGE_GIANT.get()), 75, 3, false)
                .addGoodSeason(Season.WINTER).addBadSeason(Season.SUMMER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.NAPA_CABBAGE_SEEDS.asHolder(),
                RuneCraftoryItems.NAPA_CABBAGE.asHolder(), RuneCraftoryBlocks.NAPA_CABBAGE.asHolder(),
                Pair.of(RuneCraftoryItems.NAPA_CABBAGE_GIANT.get(), RuneCraftoryBlocks.NAPA_CABBAGE_GIANT.get()), 5, 4, false)
                .addGoodSeason(Season.AUTUMN).addBadSeason(Season.SUMMER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.LEEK_SEEDS.asHolder(),
                RuneCraftoryItems.LEEK.asHolder(), RuneCraftoryBlocks.LEEK.asHolder(),
                Pair.of(RuneCraftoryItems.LEEK_GIANT.get(), RuneCraftoryBlocks.LEEK_GIANT.get()), 10, 2, false)
                .addGoodSeason(Season.SPRING).addGoodSeason(Season.AUTUMN).addBadSeason(Season.SUMMER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.RADISH_SEEDS.asHolder(),
                RuneCraftoryItems.RADISH.asHolder(), RuneCraftoryBlocks.RADISH.asHolder(),
                Pair.of(RuneCraftoryItems.RADISH_GIANT.get(), RuneCraftoryBlocks.RADISH_GIANT.get()), 4, 1, false)
                .addGoodSeason(Season.AUTUMN).addBadSeason(Season.SUMMER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.GREEN_PEPPER_SEEDS.asHolder(),
                RuneCraftoryItems.GREEN_PEPPER.asHolder(), RuneCraftoryBlocks.GREEN_PEPPER.asHolder(),
                Pair.of(RuneCraftoryItems.GREEN_PEPPER_GIANT.get(), RuneCraftoryBlocks.GREEN_PEPPER_GIANT.get()), 8, 5, true)
                .addGoodSeason(Season.SUMMER).addBadSeason(Season.WINTER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.SPINACH_SEEDS.asHolder(),
                RuneCraftoryItems.SPINACH.asHolder(), RuneCraftoryBlocks.SPINACH.asHolder(),
                Pair.of(RuneCraftoryItems.SPINACH_GIANT.get(), RuneCraftoryBlocks.SPINACH_GIANT.get()), 2, 2, false)
                .addGoodSeason(Season.AUTUMN));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.YAM_SEEDS.asHolder(),
                RuneCraftoryItems.YAM.asHolder(), RuneCraftoryBlocks.YAM.asHolder(),
                Pair.of(RuneCraftoryItems.YAM_GIANT.get(), RuneCraftoryBlocks.YAM_GIANT.get()), 9, 5, false)
                .addGoodSeason(Season.SUMMER).addGoodSeason(Season.AUTUMN).addBadSeason(Season.WINTER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.EGGPLANT_SEEDS.asHolder(),
                RuneCraftoryItems.EGGPLANT.asHolder(), RuneCraftoryBlocks.EGGPLANT.asHolder(),
                Pair.of(RuneCraftoryItems.EGGPLANT_GIANT.get(), RuneCraftoryBlocks.EGGPLANT_GIANT.get()), 7, 4, true)
                .addGoodSeason(Season.SPRING).addGoodSeason(Season.SUMMER).addBadSeason(Season.WINTER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.PINEAPPLE_SEEDS.asHolder(),
                RuneCraftoryItems.PINEAPPLE.asHolder(), RuneCraftoryBlocks.PINEAPPLE.asHolder(),
                Pair.of(RuneCraftoryItems.PINEAPPLE_GIANT.get(), RuneCraftoryBlocks.PINEAPPLE_GIANT.get()), 30, 2, true)
                .addGoodSeason(Season.SUMMER).addBadSeason(Season.SPRING).addBadSeason(Season.AUTUMN).addBadSeason(Season.WINTER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.ONION_SEEDS.asHolder(),
                RuneCraftoryItems.ONION.asHolder(), RuneCraftoryBlocks.ONION.asHolder(),
                Pair.of(RuneCraftoryItems.ONION_GIANT.get(), RuneCraftoryBlocks.ONION_GIANT.get()), 20, 6, false)
                .addGoodSeason(Season.AUTUMN).addBadSeason(Season.SUMMER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.CORN_SEEDS.asHolder(),
                RuneCraftoryItems.CORN.asHolder(), RuneCraftoryBlocks.CORN.asHolder(),
                Pair.of(RuneCraftoryItems.CORN_GIANT.get(), RuneCraftoryBlocks.CORN_GIANT.get()), 5, 1, false)
                .addGoodSeason(Season.SPRING).addGoodSeason(Season.SUMMER).addBadSeason(Season.WINTER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.TOMATO_SEEDS.asHolder(),
                RuneCraftoryItems.TOMATO.asHolder(), RuneCraftoryBlocks.TOMATO.asHolder(),
                Pair.of(RuneCraftoryItems.TOMATO_GIANT.get(), RuneCraftoryBlocks.TOMATO_GIANT.get()), 8, 2, true)
                .addGoodSeason(Season.SPRING).addGoodSeason(Season.SUMMER).addBadSeason(Season.WINTER));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.STRAWBERRY_SEEDS.asHolder(),
                RuneCraftoryItems.STRAWBERRY.asHolder(), RuneCraftoryBlocks.STRAWBERRY.asHolder(),
                Pair.of(RuneCraftoryItems.STRAWBERRY_GIANT.get(), RuneCraftoryBlocks.STRAWBERRY_GIANT.get()), 15, 5, true)
                .addGoodSeason(Season.SUMMER).addGoodSeason(Season.AUTUMN));
        this.addStat(new CropProperties.Builder(RuneCraftoryItems.CUCUMBER_SEEDS.asHolder(),
                RuneCraftoryItems.CUCUMBER.asHolder(), RuneCraftoryBlocks.CUCUMBER.asHolder(),
                Pair.of(RuneCraftoryItems.CUCUMBER_GIANT.get(), RuneCraftoryBlocks.CUCUMBER_GIANT.get()), 5, 5, true)
                .addGoodSeason(Season.SPRING).addGoodSeason(Season.SUMMER).addBadSeason(Season.WINTER));

        this.addStat(new CropProperties.Builder(HolderSet.direct(RuneCraftoryItems.SHIELD_SEEDS.asHolder()),
                HolderSet.empty(), HolderSet.direct(RuneCraftoryBlocks.SHIELD_CROP.asHolder()), 15, 1, false));
        this.addStat(new CropProperties.Builder(HolderSet.direct(RuneCraftoryItems.SWORD_SEEDS.asHolder()),
                HolderSet.empty(), HolderSet.direct(RuneCraftoryBlocks.SWORD_CROP.asHolder()), 15, 1, false));
        this.addStat(new CropProperties.Builder(HolderSet.direct(RuneCraftoryItems.DUNGEON_SEEDS.asHolder()),
                HolderSet.empty(), HolderSet.direct(RuneCraftoryBlocks.DUNGEON.asHolder()), 25, 1, false));

        this.addStat(new CropProperties.Builder(HolderSet.direct(RuneCraftoryItems.APPLE_SAPLING.asHolder()),
                HolderSet.empty(), HolderSet.direct(RuneCraftoryBlocks.APPLE_SAPLING.asHolder(), RuneCraftoryBlocks.APPLE_TREE.asHolder()), 20, 1, false));
        this.addStat(new CropProperties.Builder(HolderSet.direct(RuneCraftoryItems.ORANGE_SAPLING.asHolder()),
                HolderSet.empty(), HolderSet.direct(RuneCraftoryBlocks.ORANGE_SAPLING.asHolder(), RuneCraftoryBlocks.ORANGE_TREE.asHolder()), 20, 1, false));
        this.addStat(new CropProperties.Builder(HolderSet.direct(RuneCraftoryItems.GRAPE_SAPLING.asHolder()),
                HolderSet.empty(), HolderSet.direct(RuneCraftoryBlocks.GRAPE_SAPLING.asHolder(), RuneCraftoryBlocks.GRAPE_TREE.asHolder()), 20, 1, false));
    }
}
