package io.github.flemmli97.runecraftory.neoforge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.calendar.Season;
import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.api.datapack.provider.CropProvider;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class CropGen extends CropProvider {

    public CropGen(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
        super(packOutput, RuneCraftory.MODID, provider);
    }

    @Override
    protected void add(HolderLookup.Provider provider) {
        this.addStat(Items.WHEAT_SEEDS, new CropProperties.Builder(5, 3, true).addGoodSeason(Season.AUTUMN));
        this.addStat(Items.CARROT, new CropProperties.Builder(4, 4, false).addGoodSeason(Season.AUTUMN)
                .withGiantVersion(RuneCraftoryBlocks.CARROT_GIANT.get()));
        this.addStat(Items.POTATO, new CropProperties.Builder(4, 4, false).addGoodSeason(Season.SUMMER)
                .withGiantVersion(RuneCraftoryBlocks.POTATO_GIANT.get()));
        this.addStat(Items.BEETROOT_SEEDS, new CropProperties.Builder(5, 3, false).addGoodSeason(Season.SPRING));

        this.addStat(RuneCraftoryItems.TOYHERB_SEEDS.get(), new CropProperties.Builder(4, 2, false)
                .addGoodSeason(Season.SPRING).addBadSeason(Season.WINTER)
                .withGiantVersion(RuneCraftoryBlocks.TOYHERB_GIANT.get()));
        this.addStat(RuneCraftoryItems.MOONDROP_SEEDS.get(), new CropProperties.Builder(7, 4, false)
                .addGoodSeason(Season.SPRING).addGoodSeason(Season.AUTUMN).addBadSeason(Season.WINTER)
                .withGiantVersion(RuneCraftoryBlocks.MOONDROP_FLOWER_GIANT.get()));
        this.addStat(RuneCraftoryItems.PINK_CAT_SEEDS.get(), new CropProperties.Builder(6, 3, false)
                .addGoodSeason(Season.SUMMER).addBadSeason(Season.WINTER)
                .withGiantVersion(RuneCraftoryBlocks.PINK_CAT_GIANT.get()));
        this.addStat(RuneCraftoryItems.CHARM_BLUE_SEEDS.get(), new CropProperties.Builder(8, 3, false)
                .addGoodSeason(Season.AUTUMN).addBadSeason(Season.WINTER)
                .withGiantVersion(RuneCraftoryBlocks.CHARM_BLUE_GIANT.get()));
        this.addStat(RuneCraftoryItems.CHERRY_GRASS_SEEDS.get(), new CropProperties.Builder(10, 4, false)
                .addGoodSeason(Season.SPRING).addBadSeason(Season.WINTER)
                .withGiantVersion(RuneCraftoryBlocks.CHERRY_GRASS_GIANT.get()));
        this.addStat(RuneCraftoryItems.LAMP_GRASS_SEEDS.get(), new CropProperties.Builder(16, 3, false)
                .addGoodSeason(Season.WINTER)
                .withGiantVersion(RuneCraftoryBlocks.LAMP_GRASS_GIANT.get()));
        this.addStat(RuneCraftoryItems.BLUE_CRYSTAL_SEEDS.get(), new CropProperties.Builder(55, 3, false)
                .addGoodSeason(Season.SPRING).addBadSeason(Season.SUMMER).addBadSeason(Season.AUTUMN).addBadSeason(Season.WINTER)
                .withGiantVersion(RuneCraftoryBlocks.BLUE_CRYSTAL_GIANT.get()));
        this.addStat(RuneCraftoryItems.IRONLEAF_SEEDS.get(), new CropProperties.Builder(21, 2, false)
                .addGoodSeason(Season.WINTER).addBadSeason(Season.SUMMER)
                .withGiantVersion(RuneCraftoryBlocks.IRONLEAF_GIANT.get()));
        this.addStat(RuneCraftoryItems.FOUR_LEAF_CLOVER_SEEDS.get(), new CropProperties.Builder(28, 3, false)
                .addGoodSeason(Season.SPRING).addGoodSeason(Season.AUTUMN).addBadSeason(Season.SUMMER)
                .withGiantVersion(RuneCraftoryBlocks.FOUR_LEAF_CLOVER_GIANT.get()));
        this.addStat(RuneCraftoryItems.FIREFLOWER_SEEDS.get(), new CropProperties.Builder(42, 4, false)
                .addGoodSeason(Season.SUMMER).addBadSeason(Season.WINTER)
                .withGiantVersion(RuneCraftoryBlocks.FIREFLOWER_GIANT.get()));
        this.addStat(RuneCraftoryItems.GREEN_CRYSTAL_SEEDS.get(), new CropProperties.Builder(70, 3, false)
                .addGoodSeason(Season.SUMMER).addBadSeason(Season.SPRING).addBadSeason(Season.AUTUMN).addBadSeason(Season.WINTER)
                .withGiantVersion(RuneCraftoryBlocks.GREEN_CRYSTAL_GIANT.get()));
        this.addStat(RuneCraftoryItems.NOEL_GRASS_SEEDS.get(), new CropProperties.Builder(33, 4, false)
                .addGoodSeason(Season.WINTER).addBadSeason(Season.SUMMER)
                .withGiantVersion(RuneCraftoryBlocks.NOEL_GRASS_GIANT.get()));
        this.addStat(RuneCraftoryItems.AUTUMN_GRASS_SEEDS.get(), new CropProperties.Builder(29, 3, false)
                .addGoodSeason(Season.AUTUMN).addBadSeason(Season.WINTER)
                .withGiantVersion(RuneCraftoryBlocks.AUTUMN_GRASS_GIANT.get()));
        this.addStat(RuneCraftoryItems.POM_POM_GRASS_SEEDS.get(), new CropProperties.Builder(14, 3, false)
                .addGoodSeason(Season.AUTUMN).addBadSeason(Season.WINTER)
                .withGiantVersion(RuneCraftoryBlocks.POM_POM_GRASS_GIANT.get()));
        this.addStat(RuneCraftoryItems.RED_CRYSTAL_SEEDS.get(), new CropProperties.Builder(80, 3, false)
                .addGoodSeason(Season.AUTUMN).addBadSeason(Season.SPRING).addBadSeason(Season.SUMMER).addBadSeason(Season.WINTER)
                .withGiantVersion(RuneCraftoryBlocks.RED_CRYSTAL_GIANT.get()));
        this.addStat(RuneCraftoryItems.WHITE_CRYSTAL_SEEDS.get(), new CropProperties.Builder(90, 3, false)
                .addGoodSeason(Season.WINTER).addBadSeason(Season.SPRING).addBadSeason(Season.SUMMER).addBadSeason(Season.AUTUMN)
                .withGiantVersion(RuneCraftoryBlocks.WHITE_CRYSTAL_GIANT.get()));
        this.addStat(RuneCraftoryItems.EMERY_FLOWER_SEEDS.get(), new CropProperties.Builder(120, 2, false)
                .withGiantVersion(RuneCraftoryBlocks.EMERY_FLOWER_GIANT.get()));

        this.addStat(RuneCraftoryItems.TURNIP_SEEDS.get(), new CropProperties.Builder(4, 3, false)
                .addGoodSeason(Season.SUMMER).addGoodSeason(Season.AUTUMN)
                .withGiantVersion(RuneCraftoryBlocks.TURNIP_GIANT.get()));
        this.addStat(RuneCraftoryItems.TURNIP_PINK_SEEDS.get(), new CropProperties.Builder(8, 3, false)
                .addGoodSeason(Season.SPRING).addBadSeason(Season.WINTER)
                .withGiantVersion(RuneCraftoryBlocks.TURNIP_PINK_GIANT.get()));
        this.addStat(RuneCraftoryItems.CABBAGE_SEEDS.get(), new CropProperties.Builder(7, 3, false)
                .addGoodSeason(Season.SPRING).addGoodSeason(Season.AUTUMN).addBadSeason(Season.WINTER)
                .withGiantVersion(RuneCraftoryBlocks.CABBAGE_GIANT.get()));
        this.addStat(RuneCraftoryItems.PINK_MELON_SEEDS.get(), new CropProperties.Builder(7, 2, true)
                .addGoodSeason(Season.SPRING).addGoodSeason(Season.SUMMER).addBadSeason(Season.WINTER)
                .withGiantVersion(RuneCraftoryBlocks.PINK_MELON_GIANT.get()));
        this.addStat(RuneCraftoryItems.HOT_HOT_SEEDS.get(), new CropProperties.Builder(31, 5, false)
                .addGoodSeason(Season.WINTER).addBadSeason(Season.SUMMER)
                .withGiantVersion(RuneCraftoryBlocks.HOT_HOT_FRUIT_GIANT.get()));
        this.addStat(RuneCraftoryItems.GOLD_TURNIP_SEEDS.get(), new CropProperties.Builder(90, 3, false)
                .addGoodSeason(Season.WINTER).addBadSeason(Season.SUMMER)
                .withGiantVersion(RuneCraftoryBlocks.GOLDEN_TURNIP_GIANT.get()));
        this.addStat(RuneCraftoryItems.GOLD_POTATO_SEEDS.get(), new CropProperties.Builder(50, 3, false)
                .addGoodSeason(Season.WINTER).addBadSeason(Season.SUMMER)
                .withGiantVersion(RuneCraftoryBlocks.GOLDEN_POTATO_GIANT.get()));
        this.addStat(RuneCraftoryItems.GOLD_PUMPKIN_SEEDS.get(), new CropProperties.Builder(75, 3, true)
                .addGoodSeason(Season.WINTER).addBadSeason(Season.SUMMER)
                .withGiantVersion(RuneCraftoryBlocks.PUMPKIN_GIANT.get()));
        this.addStat(RuneCraftoryItems.GOLD_CABBAGE_SEEDS.get(), new CropProperties.Builder(75, 3, false)
                .addGoodSeason(Season.WINTER).addBadSeason(Season.SUMMER)
                .withGiantVersion(RuneCraftoryBlocks.GOLDEN_CABBAGE_GIANT.get()));
        this.addStat(RuneCraftoryItems.BOK_CHOY_SEEDS.get(), new CropProperties.Builder(5, 4, false)
                .addGoodSeason(Season.AUTUMN).addBadSeason(Season.SUMMER)
                .withGiantVersion(RuneCraftoryBlocks.BOK_CHOY_GIANT.get()));
        this.addStat(RuneCraftoryItems.LEEK_SEEDS.get(), new CropProperties.Builder(10, 2, false)
                .addGoodSeason(Season.SPRING).addGoodSeason(Season.AUTUMN).addBadSeason(Season.SUMMER)
                .withGiantVersion(RuneCraftoryBlocks.LEEK_GIANT.get()));
        this.addStat(RuneCraftoryItems.RADISH_SEEDS.get(), new CropProperties.Builder(4, 1, false)
                .addGoodSeason(Season.AUTUMN).addBadSeason(Season.SUMMER)
                .withGiantVersion(RuneCraftoryBlocks.RADISH_GIANT.get()));
        this.addStat(RuneCraftoryItems.GREEN_PEPPER_SEEDS.get(), new CropProperties.Builder(8, 5, true)
                .addGoodSeason(Season.SUMMER).addBadSeason(Season.WINTER)
                .withGiantVersion(RuneCraftoryBlocks.GREEN_PEPPER_GIANT.get()));
        this.addStat(RuneCraftoryItems.SPINACH_SEEDS.get(), new CropProperties.Builder(2, 2, false)
                .addGoodSeason(Season.AUTUMN)
                .withGiantVersion(RuneCraftoryBlocks.SPINACH_GIANT.get()));
        this.addStat(RuneCraftoryItems.YAM_SEEDS.get(), new CropProperties.Builder(9, 5, false)
                .addGoodSeason(Season.SUMMER).addGoodSeason(Season.AUTUMN).addBadSeason(Season.WINTER)
                .withGiantVersion(RuneCraftoryBlocks.YAM_GIANT.get()));
        this.addStat(RuneCraftoryItems.EGGPLANT_SEEDS.get(), new CropProperties.Builder(7, 4, true)
                .addGoodSeason(Season.SPRING).addGoodSeason(Season.SUMMER).addBadSeason(Season.WINTER)
                .withGiantVersion(RuneCraftoryBlocks.EGGPLANT_GIANT.get()));
        this.addStat(RuneCraftoryItems.PINEAPPLE_SEEDS.get(), new CropProperties.Builder(30, 2, true)
                .addGoodSeason(Season.SUMMER).addBadSeason(Season.SPRING).addBadSeason(Season.AUTUMN).addBadSeason(Season.WINTER)
                .withGiantVersion(RuneCraftoryBlocks.PINEAPPLE_GIANT.get()));
        this.addStat(RuneCraftoryItems.ONION_SEEDS.get(), new CropProperties.Builder(20, 6, false)
                .addGoodSeason(Season.AUTUMN).addBadSeason(Season.SUMMER)
                .withGiantVersion(RuneCraftoryBlocks.ONION_GIANT.get()));
        this.addStat(RuneCraftoryItems.CORN_SEEDS.get(), new CropProperties.Builder(5, 1, false)
                .addGoodSeason(Season.SPRING).addGoodSeason(Season.SUMMER).addBadSeason(Season.WINTER)
                .withGiantVersion(RuneCraftoryBlocks.CORN_GIANT.get()));
        this.addStat(RuneCraftoryItems.TOMATO_SEEDS.get(), new CropProperties.Builder(8, 2, true)
                .addGoodSeason(Season.SPRING).addGoodSeason(Season.SUMMER).addBadSeason(Season.WINTER)
                .withGiantVersion(RuneCraftoryBlocks.TOMATO_GIANT.get()));
        this.addStat(RuneCraftoryItems.STRAWBERRY_SEEDS.get(), new CropProperties.Builder(15, 5, true)
                .addGoodSeason(Season.SUMMER).addGoodSeason(Season.AUTUMN)
                .withGiantVersion(RuneCraftoryBlocks.STRAWBERRY_GIANT.get()));
        this.addStat(RuneCraftoryItems.CUCUMBER_SEEDS.get(), new CropProperties.Builder(5, 5, true)
                .addGoodSeason(Season.SPRING).addGoodSeason(Season.SUMMER).addBadSeason(Season.WINTER)
                .withGiantVersion(RuneCraftoryBlocks.CUCUMBER_GIANT.get()));
        this.addStat(RuneCraftoryItems.FODDER_SEEDS.get(), new CropProperties.Builder(5, 1, true));

        this.addStat(RuneCraftoryItems.SHIELD_SEEDS.get(), new CropProperties.Builder(15, 1, false));
        this.addStat(RuneCraftoryItems.SWORD_SEEDS.get(), new CropProperties.Builder(15, 1, false));
        this.addStat(RuneCraftoryItems.DUNGEON_SEEDS.get(), new CropProperties.Builder(25, 1, false));

        this.addStat(RuneCraftoryItems.APPLE_SAPLING.get(), new CropProperties.Builder(20, 1, false));
        this.addStat(RuneCraftoryItems.ORANGE_SAPLING.get(), new CropProperties.Builder(20, 1, false));
        this.addStat(RuneCraftoryItems.GRAPE_SAPLING.get(), new CropProperties.Builder(20, 1, false));
    }
}
