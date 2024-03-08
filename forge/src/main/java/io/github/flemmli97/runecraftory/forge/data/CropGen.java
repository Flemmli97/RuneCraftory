package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.api.datapack.provider.CropProvider;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;

public class CropGen extends CropProvider {

    public CropGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID);
    }

    @Override
    protected void add() {
        this.addStat(Items.WHEAT_SEEDS, new CropProperties.Builder(5, 3, true).addGoodSeason(EnumSeason.FALL));
        this.addStat(Items.CARROT, new CropProperties.Builder(4, 4, false).addGoodSeason(EnumSeason.FALL)
                .withGiantVersion(ModBlocks.CARROT_GIANT.get()));
        this.addStat(Items.POTATO, new CropProperties.Builder(4, 4, false).addGoodSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.POTATO_GIANT.get()));
        this.addStat(Items.BEETROOT_SEEDS, new CropProperties.Builder(5, 3, false).addGoodSeason(EnumSeason.SPRING));

        this.addStat(ModItems.TOYHERB_SEEDS.get(), new CropProperties.Builder(4, 2, false)
                .addGoodSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.TOYHERB_GIANT.get()));
        this.addStat(ModItems.MOONDROP_SEEDS.get(), new CropProperties.Builder(7, 4, false)
                .addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.MOONDROP_FLOWER_GIANT.get()));
        this.addStat(ModItems.PINK_CAT_SEEDS.get(), new CropProperties.Builder(6, 3, false)
                .addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.PINK_CAT_GIANT.get()));
        this.addStat(ModItems.CHARM_BLUE_SEEDS.get(), new CropProperties.Builder(8, 3, false)
                .addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.CHARM_BLUE_GIANT.get()));
        this.addStat(ModItems.CHERRY_GRASS_SEEDS.get(), new CropProperties.Builder(10, 4, false)
                .addGoodSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.CHERRY_GRASS_GIANT.get()));
        this.addStat(ModItems.LAMP_GRASS_SEEDS.get(), new CropProperties.Builder(16, 3, false)
                .addGoodSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.LAMP_GRASS_GIANT.get()));
        this.addStat(ModItems.BLUE_CRYSTAL_SEEDS.get(), new CropProperties.Builder(55, 3, false)
                .addGoodSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.BLUE_CRYSTAL_GIANT.get()));
        this.addStat(ModItems.IRONLEAF_SEEDS.get(), new CropProperties.Builder(21, 2, false)
                .addGoodSeason(EnumSeason.WINTER).addBadSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.IRONLEAF_GIANT.get()));
        this.addStat(ModItems.FOUR_LEAF_CLOVER_SEEDS.get(), new CropProperties.Builder(28, 3, false)
                .addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.FOUR_LEAF_CLOVER_GIANT.get()));
        this.addStat(ModItems.FIREFLOWER_SEEDS.get(), new CropProperties.Builder(42, 4, false)
                .addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.FIREFLOWER_GIANT.get()));
        this.addStat(ModItems.GREEN_CRYSTAL_SEEDS.get(), new CropProperties.Builder(70, 3, false)
                .addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.GREEN_CRYSTAL_GIANT.get()));
        this.addStat(ModItems.NOEL_GRASS_SEEDS.get(), new CropProperties.Builder(33, 4, false)
                .addGoodSeason(EnumSeason.WINTER).addBadSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.NOEL_GRASS_GIANT.get()));
        this.addStat(ModItems.AUTUMN_GRASS_SEEDS.get(), new CropProperties.Builder(29, 3, false)
                .addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.AUTUMN_GRASS_GIANT.get()));
        this.addStat(ModItems.POM_POM_GRASS_SEEDS.get(), new CropProperties.Builder(14, 3, false)
                .addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.POM_POM_GRASS_GIANT.get()));
        this.addStat(ModItems.RED_CRYSTAL_SEEDS.get(), new CropProperties.Builder(80, 3, false)
                .addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.RED_CRYSTAL_GIANT.get()));
        this.addStat(ModItems.WHITE_CRYSTAL_SEEDS.get(), new CropProperties.Builder(90, 3, false)
                .addGoodSeason(EnumSeason.WINTER).addBadSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.FALL)
                .withGiantVersion(ModBlocks.WHITE_CRYSTAL_GIANT.get()));
        this.addStat(ModItems.EMERY_FLOWER_SEEDS.get(), new CropProperties.Builder(120, 2, false)
                .withGiantVersion(ModBlocks.EMERY_FLOWER_GIANT.get()));

        this.addStat(ModItems.TURNIP_SEEDS.get(), new CropProperties.Builder(4, 3, false)
                .addGoodSeason(EnumSeason.SUMMER).addGoodSeason(EnumSeason.FALL)
                .withGiantVersion(ModBlocks.TURNIP_GIANT.get()));
        this.addStat(ModItems.TURNIP_PINK_SEEDS.get(), new CropProperties.Builder(8, 3, false)
                .addGoodSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.TURNIP_PINK_GIANT.get()));
        this.addStat(ModItems.CABBAGE_SEEDS.get(), new CropProperties.Builder(7, 3, false)
                .addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.CABBAGE_GIANT.get()));
        this.addStat(ModItems.PINK_MELON_SEEDS.get(), new CropProperties.Builder(7, 2, true)
                .addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.PINK_MELON_GIANT.get()));
        this.addStat(ModItems.HOT_HOT_SEEDS.get(), new CropProperties.Builder(31, 5, false)
                .addGoodSeason(EnumSeason.WINTER).addBadSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.HOT_HOT_FRUIT_GIANT.get()));
        this.addStat(ModItems.GOLD_TURNIP_SEEDS.get(), new CropProperties.Builder(90, 3, false)
                .addGoodSeason(EnumSeason.WINTER).addBadSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.GOLDEN_TURNIP_GIANT.get()));
        this.addStat(ModItems.GOLD_POTATO_SEEDS.get(), new CropProperties.Builder(50, 3, false)
                .addGoodSeason(EnumSeason.WINTER).addBadSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.GOLDEN_POTATO_GIANT.get()));
        this.addStat(ModItems.GOLD_PUMPKIN_SEEDS.get(), new CropProperties.Builder(75, 3, true)
                .addGoodSeason(EnumSeason.WINTER).addBadSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.PUMPKIN_GIANT.get()));
        this.addStat(ModItems.GOLD_CABBAGE_SEEDS.get(), new CropProperties.Builder(75, 3, false)
                .addGoodSeason(EnumSeason.WINTER).addBadSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.GOLDEN_CABBAGE_GIANT.get()));
        this.addStat(ModItems.BOK_CHOY_SEEDS.get(), new CropProperties.Builder(5, 4, false)
                .addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.BOK_CHOY_GIANT.get()));
        this.addStat(ModItems.LEEK_SEEDS.get(), new CropProperties.Builder(10, 2, false)
                .addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.LEEK_GIANT.get()));
        this.addStat(ModItems.RADISH_SEEDS.get(), new CropProperties.Builder(4, 1, false)
                .addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.RADISH_GIANT.get()));
        this.addStat(ModItems.GREEN_PEPPER_SEEDS.get(), new CropProperties.Builder(8, 5, true)
                .addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.GREEN_PEPPER_GIANT.get()));
        this.addStat(ModItems.SPINACH_SEEDS.get(), new CropProperties.Builder(2, 2, false)
                .addGoodSeason(EnumSeason.FALL)
                .withGiantVersion(ModBlocks.SPINACH_GIANT.get()));
        this.addStat(ModItems.YAM_SEEDS.get(), new CropProperties.Builder(9, 5, false)
                .addGoodSeason(EnumSeason.SUMMER).addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.YAM_GIANT.get()));
        this.addStat(ModItems.EGGPLANT_SEEDS.get(), new CropProperties.Builder(7, 4, true)
                .addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.EGGPLANT_GIANT.get()));
        this.addStat(ModItems.PINEAPPLE_SEEDS.get(), new CropProperties.Builder(30, 2, true)
                .addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.PINEAPPLE_GIANT.get()));
        this.addStat(ModItems.ONION_SEEDS.get(), new CropProperties.Builder(20, 6, false)
                .addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.ONION_GIANT.get()));
        this.addStat(ModItems.CORN_SEEDS.get(), new CropProperties.Builder(5, 1, false)
                .addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.CORN_GIANT.get()));
        this.addStat(ModItems.TOMATO_SEEDS.get(), new CropProperties.Builder(8, 2, true)
                .addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.TOMATO_GIANT.get()));
        this.addStat(ModItems.STRAWBERRY_SEEDS.get(), new CropProperties.Builder(15, 5, true)
                .addGoodSeason(EnumSeason.SUMMER).addGoodSeason(EnumSeason.FALL)
                .withGiantVersion(ModBlocks.STRAWBERRY_GIANT.get()));
        this.addStat(ModItems.CUCUMBER_SEEDS.get(), new CropProperties.Builder(5, 5, true)
                .addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.CUCUMBER_GIANT.get()));
        this.addStat(ModItems.FODDER_SEEDS.get(), new CropProperties.Builder(5, 1, true));

        this.addStat(ModItems.SHIELD_SEEDS.get(), new CropProperties.Builder(15, 1, false));
        this.addStat(ModItems.SWORD_SEEDS.get(), new CropProperties.Builder(15, 1, false));
        this.addStat(ModItems.DUNGEON_SEEDS.get(), new CropProperties.Builder(25, 1, false));

        this.addStat(ModItems.APPLE_SAPLING.get(), new CropProperties.Builder(20, 1, false));
        this.addStat(ModItems.ORANGE_SAPLING.get(), new CropProperties.Builder(20, 1, false));
        this.addStat(ModItems.GRAPE_SAPLING.get(), new CropProperties.Builder(20, 1, false));
    }
}
