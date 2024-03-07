package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumMineralTier;
import io.github.flemmli97.runecraftory.common.blocks.BlockAccessory;
import io.github.flemmli97.runecraftory.common.blocks.BlockBossSpawner;
import io.github.flemmli97.runecraftory.common.blocks.BlockBrokenMineral;
import io.github.flemmli97.runecraftory.common.blocks.BlockCashRegister;
import io.github.flemmli97.runecraftory.common.blocks.BlockChemistry;
import io.github.flemmli97.runecraftory.common.blocks.BlockCooking;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrop;
import io.github.flemmli97.runecraftory.common.blocks.BlockForge;
import io.github.flemmli97.runecraftory.common.blocks.BlockFruitTreeLeaf;
import io.github.flemmli97.runecraftory.common.blocks.BlockGiantCrop;
import io.github.flemmli97.runecraftory.common.blocks.BlockHerb;
import io.github.flemmli97.runecraftory.common.blocks.BlockMeltableSnow;
import io.github.flemmli97.runecraftory.common.blocks.BlockMineral;
import io.github.flemmli97.runecraftory.common.blocks.BlockMonsterBarn;
import io.github.flemmli97.runecraftory.common.blocks.BlockQuestboard;
import io.github.flemmli97.runecraftory.common.blocks.BlockShippingBin;
import io.github.flemmli97.runecraftory.common.blocks.BlockSingleTimeSpawner;
import io.github.flemmli97.runecraftory.common.blocks.BlockTreeBase;
import io.github.flemmli97.runecraftory.common.blocks.BlockTreeRoot;
import io.github.flemmli97.runecraftory.common.blocks.BlockTreeSapling;
import io.github.flemmli97.runecraftory.common.blocks.tile.AccessoryBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.tile.BossSpawnerBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.tile.BrokenMineralBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.tile.ChemistryBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.tile.CookingBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.tile.ForgingBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.tile.MonsterBarnBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.tile.SingleTimeSpawner;
import io.github.flemmli97.runecraftory.common.blocks.tile.TreeBlockEntity;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class ModBlocks {

    public static final PlatformRegistry<Block> BLOCKS = PlatformUtils.INSTANCE.of(Registry.BLOCK_REGISTRY, RuneCraftory.MODID);
    public static final PlatformRegistry<BlockEntityType<?>> TILES = PlatformUtils.INSTANCE.of(Registry.BLOCK_ENTITY_TYPE_REGISTRY, RuneCraftory.MODID);
    public static final List<RegistryEntrySupplier<Block>> CROPS = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Block>> FLOWERS = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Block>> HERBS = new ArrayList<>();
    public static final Map<RegistryEntrySupplier<Block>, RegistryEntrySupplier<Block>> GIANT_CROP_MAP = new HashMap<>();
    public static final EnumMap<EnumMineralTier, RegistryEntrySupplier<Block>> MINERAL_MAP = new EnumMap<>(EnumMineralTier.class);
    public static final EnumMap<EnumMineralTier, RegistryEntrySupplier<Block>> BROKEN_MINERAL_MAP = new EnumMap<>(EnumMineralTier.class);

    public static final RegistryEntrySupplier<Block> FORGE = BLOCKS.register("forge", () -> new BlockForge(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3, 100)));
    public static final RegistryEntrySupplier<Block> COOKING = BLOCKS.register("cooking_table", () -> new BlockCooking(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3, 100)));
    public static final RegistryEntrySupplier<Block> CHEMISTRY = BLOCKS.register("chemistry_set", () -> new BlockChemistry(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3, 100)));
    public static final RegistryEntrySupplier<Block> ACCESSORY = BLOCKS.register("accessory_workbench", () -> new BlockAccessory(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3, 100)));

    public static final RegistryEntrySupplier<Block> MINERAL_IRON = mineral(EnumMineralTier.IRON);
    public static final RegistryEntrySupplier<Block> MINERAL_BRONZE = mineral(EnumMineralTier.BRONZE);
    public static final RegistryEntrySupplier<Block> MINERAL_SILVER = mineral(EnumMineralTier.SILVER);
    public static final RegistryEntrySupplier<Block> MINERAL_GOLD = mineral(EnumMineralTier.GOLD);
    public static final RegistryEntrySupplier<Block> MINERAL_PLATINUM = mineral(EnumMineralTier.PLATINUM);
    public static final RegistryEntrySupplier<Block> MINERAL_ORICHALCUM = mineral(EnumMineralTier.ORICHALCUM);
    public static final RegistryEntrySupplier<Block> MINERAL_DIAMOND = mineral(EnumMineralTier.DIAMOND);
    public static final RegistryEntrySupplier<Block> MINERAL_DRAGONIC = mineral(EnumMineralTier.DRAGONIC);
    public static final RegistryEntrySupplier<Block> MINERAL_AQUAMARINE = mineral(EnumMineralTier.AQUAMARINE);
    public static final RegistryEntrySupplier<Block> MINERAL_AMETHYST = mineral(EnumMineralTier.AMETHYST);
    public static final RegistryEntrySupplier<Block> MINERAL_RUBY = mineral(EnumMineralTier.RUBY);
    public static final RegistryEntrySupplier<Block> MINERAL_EMERALD = mineral(EnumMineralTier.EMERALD);
    public static final RegistryEntrySupplier<Block> MINERAL_SAPPHIRE = mineral(EnumMineralTier.SAPPHIRE);

    public static final RegistryEntrySupplier<Block> BROKEN_MINERAL_IRON = brokenMineral(EnumMineralTier.IRON);
    public static final RegistryEntrySupplier<Block> BROKEN_MINERAL_BRONZE = brokenMineral(EnumMineralTier.BRONZE);
    public static final RegistryEntrySupplier<Block> BROKEN_MINERAL_SILVER = brokenMineral(EnumMineralTier.SILVER);
    public static final RegistryEntrySupplier<Block> BROKEN_MINERAL_GOLD = brokenMineral(EnumMineralTier.GOLD);
    public static final RegistryEntrySupplier<Block> BROKEN_MINERAL_PLATINUM = brokenMineral(EnumMineralTier.PLATINUM);
    public static final RegistryEntrySupplier<Block> BROKEN_MINERAL_ORICHALCUM = brokenMineral(EnumMineralTier.ORICHALCUM);
    public static final RegistryEntrySupplier<Block> BROKEN_MINERAL_DIAMOND = brokenMineral(EnumMineralTier.DIAMOND);
    public static final RegistryEntrySupplier<Block> BROKEN_MINERAL_DRAGONIC = brokenMineral(EnumMineralTier.DRAGONIC);
    public static final RegistryEntrySupplier<Block> BROKEN_MINERAL_AQUAMARINE = brokenMineral(EnumMineralTier.AQUAMARINE);
    public static final RegistryEntrySupplier<Block> BROKEN_MINERAL_AMETHYST = brokenMineral(EnumMineralTier.AMETHYST);
    public static final RegistryEntrySupplier<Block> BROKEN_MINERAL_RUBY = brokenMineral(EnumMineralTier.RUBY);
    public static final RegistryEntrySupplier<Block> BROKEN_MINERAL_EMERALD = brokenMineral(EnumMineralTier.EMERALD);
    public static final RegistryEntrySupplier<Block> BROKEN_MINERAL_SAPPHIRE = brokenMineral(EnumMineralTier.SAPPHIRE);

    public static final RegistryEntrySupplier<Block> BOSS_SPAWNER = BLOCKS.register("boss_spawner", () -> new BlockBossSpawner(BlockBehaviour.Properties.of(Material.METAL).strength(60, 9999).noOcclusion()));
    public static final RegistryEntrySupplier<Block> SHIPPING = BLOCKS.register("shipping_bin", () -> new BlockShippingBin(BlockBehaviour.Properties.of(Material.WOOD).strength(3, 10)));
    public static final RegistryEntrySupplier<Block> SINGLE_SPAWN_BLOCK = BLOCKS.register("one_time_spawner", () -> new BlockSingleTimeSpawner(BlockBehaviour.Properties.of(Material.METAL).strength(60, 9999).noOcclusion()));
    public static final RegistryEntrySupplier<Block> CASH_REGISTER = BLOCKS.register("cash_register", () -> new BlockCashRegister(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3, 5)));
    public static final RegistryEntrySupplier<Block> MONSTER_BARN = BLOCKS.register("monster_barn", () -> new BlockMonsterBarn(BlockBehaviour.Properties.of(Material.GRASS).sound(SoundType.GRASS).noOcclusion().noCollission().strength(1, 10000)));
    public static final RegistryEntrySupplier<Block> QUEST_BOARD = BLOCKS.register("quest_board", () -> new BlockQuestboard(BlockBehaviour.Properties.of(Material.WOOD).sound(SoundType.WOOD).noOcclusion().strength(2, 5)));

    public static final RegistryEntrySupplier<Block> TURNIP = crop("turnip", () -> ModItems.TURNIP, () -> ModItems.TURNIP_SEEDS);
    public static final RegistryEntrySupplier<Block> TURNIP_PINK = crop("turnip_pink", () -> ModItems.TURNIP_PINK, () -> ModItems.TURNIP_PINK_SEEDS);
    public static final RegistryEntrySupplier<Block> CABBAGE = crop("cabbage", () -> ModItems.CABBAGE, () -> ModItems.CABBAGE_SEEDS);
    public static final RegistryEntrySupplier<Block> PINK_MELON = crop("pink_melon", () -> ModItems.PINK_MELON, () -> ModItems.PINK_MELON_SEEDS);
    public static final RegistryEntrySupplier<Block> PINEAPPLE = crop("pineapple", () -> ModItems.PINEAPPLE, () -> ModItems.PINEAPPLE_SEEDS);
    public static final RegistryEntrySupplier<Block> STRAWBERRY = crop("strawberry", () -> ModItems.STRAWBERRY, () -> ModItems.STRAWBERRY_SEEDS);
    public static final RegistryEntrySupplier<Block> GOLDEN_TURNIP = crop("golden_turnip", () -> ModItems.GOLDEN_TURNIP, () -> ModItems.GOLD_TURNIP_SEEDS);
    public static final RegistryEntrySupplier<Block> GOLDEN_POTATO = crop("golden_potato", () -> ModItems.GOLDEN_POTATO, () -> ModItems.GOLD_POTATO_SEEDS);
    public static final RegistryEntrySupplier<Block> GOLDEN_PUMPKIN = crop("golden_pumpkin", () -> ModItems.GOLDEN_PUMPKIN, () -> ModItems.GOLD_PUMPKIN_SEEDS);
    public static final RegistryEntrySupplier<Block> GOLDEN_CABBAGE = crop("golden_cabbage", () -> ModItems.GOLDEN_CABBAGE, () -> ModItems.GOLD_CABBAGE_SEEDS);
    public static final RegistryEntrySupplier<Block> HOT_HOT_FRUIT = crop("hot_hot_fruit", () -> ModItems.HOT_HOT_FRUIT, () -> ModItems.HOT_HOT_SEEDS);
    public static final RegistryEntrySupplier<Block> BOK_CHOY = crop("bok_choy", () -> ModItems.BOK_CHOY, () -> ModItems.BOK_CHOY_SEEDS);
    public static final RegistryEntrySupplier<Block> LEEK = crop("leek", () -> ModItems.LEEK, () -> ModItems.LEEK_SEEDS);
    public static final RegistryEntrySupplier<Block> RADISH = crop("radish", () -> ModItems.RADISH, () -> ModItems.RADISH_SEEDS);
    public static final RegistryEntrySupplier<Block> SPINACH = crop("spinach", () -> ModItems.SPINACH, () -> ModItems.SPINACH_SEEDS);
    public static final RegistryEntrySupplier<Block> GREEN_PEPPER = crop("green_pepper", () -> ModItems.GREEN_PEPPER, () -> ModItems.GREEN_PEPPER_SEEDS);
    public static final RegistryEntrySupplier<Block> YAM = crop("yam", () -> ModItems.YAM, () -> ModItems.YAM_SEEDS);
    public static final RegistryEntrySupplier<Block> EGGPLANT = crop("eggplant", () -> ModItems.EGGPLANT, () -> ModItems.EGGPLANT_SEEDS);
    public static final RegistryEntrySupplier<Block> TOMATO = crop("tomato", () -> ModItems.TOMATO, () -> ModItems.TOMATO_SEEDS);
    public static final RegistryEntrySupplier<Block> CORN = crop("corn", () -> ModItems.CORN, () -> ModItems.CORN_SEEDS);
    public static final RegistryEntrySupplier<Block> CUCUMBER = crop("cucumber", () -> ModItems.CUCUMBER, () -> ModItems.CUCUMBER_SEEDS);
    public static final RegistryEntrySupplier<Block> PUMPKIN = crop("pumpkin", () -> ModItems.PUMPKIN, () -> ModItems.PUMPKIN_SEEDS);
    public static final RegistryEntrySupplier<Block> ONION = crop("onion", () -> ModItems.ONION, () -> ModItems.ONION_SEEDS);
    public static final RegistryEntrySupplier<Block> TURNIP_GIANT = giantCrop("tyrant_turnip", () -> ModItems.TURNIP_GIANT, () -> ModItems.TURNIP_SEEDS, TURNIP);
    public static final RegistryEntrySupplier<Block> TURNIP_PINK_GIANT = giantCrop("colossal_pink", () -> ModItems.TURNIP_PINK_GIANT, () -> ModItems.TURNIP_PINK_SEEDS, TURNIP_PINK);
    public static final RegistryEntrySupplier<Block> CABBAGE_GIANT = giantCrop("king_cabbage", () -> ModItems.CABBAGE_GIANT, () -> ModItems.CABBAGE_SEEDS, CABBAGE);
    public static final RegistryEntrySupplier<Block> PINK_MELON_GIANT = giantCrop("conqueror_melon", () -> ModItems.PINK_MELON_GIANT, () -> ModItems.PINK_MELON_SEEDS, PINK_MELON);
    public static final RegistryEntrySupplier<Block> PINEAPPLE_GIANT = giantCrop("king_pineapple", () -> ModItems.PINEAPPLE_GIANT, () -> ModItems.PINEAPPLE_SEEDS, PINEAPPLE);
    public static final RegistryEntrySupplier<Block> STRAWBERRY_GIANT = giantCrop("sultan_strawberry", () -> ModItems.STRAWBERRY_GIANT, () -> ModItems.STRAWBERRY_SEEDS, STRAWBERRY);
    public static final RegistryEntrySupplier<Block> GOLDEN_TURNIP_GIANT = giantCrop("golden_tyrant_turnip", () -> ModItems.GOLDEN_TURNIP_GIANT, () -> ModItems.GOLD_TURNIP_SEEDS, GOLDEN_TURNIP);
    public static final RegistryEntrySupplier<Block> GOLDEN_POTATO_GIANT = giantCrop("gold_prince_potato", () -> ModItems.GOLDEN_POTATO_GIANT, () -> ModItems.GOLD_POTATO_SEEDS, GOLDEN_POTATO);
    public static final RegistryEntrySupplier<Block> GOLDEN_PUMPKIN_GIANT = giantCrop("golden_doom_pumpkin", () -> ModItems.GOLDEN_PUMPKIN_GIANT, () -> ModItems.GOLD_PUMPKIN_SEEDS, PUMPKIN);
    public static final RegistryEntrySupplier<Block> GOLDEN_CABBAGE_GIANT = giantCrop("golden_king_cabbage", () -> ModItems.GOLDEN_CABBAGE_GIANT, () -> ModItems.GOLD_CABBAGE_SEEDS, CABBAGE_GIANT);
    public static final RegistryEntrySupplier<Block> HOT_HOT_FRUIT_GIANT = giantCrop("giant_hot_hot_fruit", () -> ModItems.HOT_HOT_FRUIT_GIANT, () -> ModItems.HOT_HOT_SEEDS, HOT_HOT_FRUIT);
    public static final RegistryEntrySupplier<Block> BOK_CHOY_GIANT = giantCrop("boss_bok_choy", () -> ModItems.BOK_CHOY_GIANT, () -> ModItems.BOK_CHOY_SEEDS, BOK_CHOY);
    public static final RegistryEntrySupplier<Block> LEEK_GIANT = giantCrop("legendary_leek", () -> ModItems.LEEK_GIANT, () -> ModItems.LEEK_SEEDS, LEEK);
    public static final RegistryEntrySupplier<Block> RADISH_GIANT = giantCrop("noble_radish", () -> ModItems.RADISH_GIANT, () -> ModItems.RADISH_SEEDS, RADISH);
    public static final RegistryEntrySupplier<Block> SPINACH_GIANT = giantCrop("sovereign_spinach", () -> ModItems.SPINACH_GIANT, () -> ModItems.SPINACH_SEEDS, SPINACH);
    public static final RegistryEntrySupplier<Block> GREEN_PEPPER_GIANT = giantCrop("green_pepper_rex", () -> ModItems.GREEN_PEPPER_GIANT, () -> ModItems.GREEN_PEPPER_SEEDS, GREEN_PEPPER);
    public static final RegistryEntrySupplier<Block> YAM_GIANT = giantCrop("lorldy_yam", () -> ModItems.YAM_GIANT, () -> ModItems.YAM_SEEDS, YAM);
    public static final RegistryEntrySupplier<Block> EGGPLANT_GIANT = giantCrop("emperor_eggplant", () -> ModItems.EGGPLANT_GIANT, () -> ModItems.EGGPLANT_SEEDS, EGGPLANT);
    public static final RegistryEntrySupplier<Block> TOMATO_GIANT = giantCrop("titan_tomato", () -> ModItems.TOMATO_GIANT, () -> ModItems.TOMATO_SEEDS, TOMATO);
    public static final RegistryEntrySupplier<Block> CORN_GIANT = giantCrop("gigant_corn", () -> ModItems.CORN_GIANT, () -> ModItems.CORN_SEEDS, CORN);
    public static final RegistryEntrySupplier<Block> CUCUMBER_GIANT = giantCrop("kaiser_cucumber", () -> ModItems.CUCUMBER_GIANT, () -> ModItems.CUCUMBER_SEEDS, CUCUMBER);
    public static final RegistryEntrySupplier<Block> PUMPKIN_GIANT = giantCrop("doom_pumpkin", () -> ModItems.PUMPKIN_GIANT, () -> ModItems.PUMPKIN_SEEDS, PUMPKIN);
    public static final RegistryEntrySupplier<Block> ONION_GIANT = giantCrop("ultra_onion", () -> ModItems.ONION_GIANT, () -> ModItems.ONION_SEEDS, ONION);
    public static final RegistryEntrySupplier<Block> FODDER = crop("fodder", () -> ModItems.FODDER, () -> ModItems.FODDER_SEEDS);

    public static final RegistryEntrySupplier<Block> POTATO_GIANT = crop("potato", () -> () -> Items.POTATO, () -> () -> Items.POTATO);
    public static final RegistryEntrySupplier<Block> CARROT_GIANT = crop("carrot", () -> () -> Items.CARROT, () -> () -> Items.CARROT);

    public static final RegistryEntrySupplier<Block> TOYHERB = flower("toyherb", () -> ModItems.TOYHERB, () -> ModItems.TOYHERB_SEEDS);
    public static final RegistryEntrySupplier<Block> MOONDROP_FLOWER = flower("moondrop_flower", () -> ModItems.MOONDROP_FLOWER, () -> ModItems.MOONDROP_SEEDS);
    public static final RegistryEntrySupplier<Block> PINK_CAT = flower("pink_cat", () -> ModItems.PINK_CAT, () -> ModItems.PINK_CAT_SEEDS);
    public static final RegistryEntrySupplier<Block> CHARM_BLUE = flower("charm_blue", () -> ModItems.CHARM_BLUE, () -> ModItems.CHARM_BLUE_SEEDS);
    public static final RegistryEntrySupplier<Block> LAMP_GRASS = flower("lamp_grass", () -> ModItems.LAMP_GRASS, () -> ModItems.LAMP_GRASS_SEEDS);
    public static final RegistryEntrySupplier<Block> CHERRY_GRASS = flower("cherry_grass", () -> ModItems.CHERRY_GRASS, () -> ModItems.CHERRY_GRASS_SEEDS);
    public static final RegistryEntrySupplier<Block> POM_POM_GRASS = flower("pom_pom_grass", () -> ModItems.POM_POM_GRASS, () -> ModItems.POM_POM_GRASS_SEEDS);
    public static final RegistryEntrySupplier<Block> AUTUMN_GRASS = flower("autumn_grass", () -> ModItems.AUTUMN_GRASS, () -> ModItems.AUTUMN_GRASS_SEEDS);
    public static final RegistryEntrySupplier<Block> NOEL_GRASS = flower("noel_grass", () -> ModItems.NOEL_GRASS, () -> ModItems.NOEL_GRASS_SEEDS);
    public static final RegistryEntrySupplier<Block> FIREFLOWER = flower("fireflower", () -> ModItems.FIREFLOWER, () -> ModItems.FIREFLOWER_SEEDS);
    public static final RegistryEntrySupplier<Block> FOUR_LEAF_CLOVER = flower("four_leaf_clover", () -> ModItems.FOUR_LEAF_CLOVER, () -> ModItems.FOUR_LEAF_CLOVER_SEEDS);
    public static final RegistryEntrySupplier<Block> IRONLEAF = flower("ironleaf", () -> ModItems.IRONLEAF, () -> ModItems.IRONLEAF_SEEDS);
    public static final RegistryEntrySupplier<Block> WHITE_CRYSTAL = flower("white_crystal", () -> ModItems.WHITE_CRYSTAL, () -> ModItems.WHITE_CRYSTAL_SEEDS);
    public static final RegistryEntrySupplier<Block> RED_CRYSTAL = flower("red_crystal", () -> ModItems.RED_CRYSTAL, () -> ModItems.RED_CRYSTAL_SEEDS);
    public static final RegistryEntrySupplier<Block> GREEN_CRYSTAL = flower("green_crystal", () -> ModItems.GREEN_CRYSTAL, () -> ModItems.GREEN_CRYSTAL_SEEDS);
    public static final RegistryEntrySupplier<Block> BLUE_CRYSTAL = flower("blue_crystal", () -> ModItems.BLUE_CRYSTAL, () -> ModItems.BLUE_CRYSTAL_SEEDS);
    public static final RegistryEntrySupplier<Block> EMERY_FLOWER = flower("emery_flower", () -> ModItems.EMERY_FLOWER, () -> ModItems.EMERY_FLOWER_SEEDS);
    public static final RegistryEntrySupplier<Block> TOYHERB_GIANT = giantFlower("ultra_toyherb", () -> ModItems.TOYHERB_GIANT, () -> ModItems.TOYHERB_SEEDS, TOYHERB);
    public static final RegistryEntrySupplier<Block> MOONDROP_FLOWER_GIANT = giantFlower("ultra_moondrop_flower", () -> ModItems.MOONDROP_FLOWER_GIANT, () -> ModItems.MOONDROP_SEEDS, MOONDROP_FLOWER);
    public static final RegistryEntrySupplier<Block> PINK_CAT_GIANT = giantFlower("king_pink_cat", () -> ModItems.PINK_CAT_GIANT, () -> ModItems.PINK_CAT_SEEDS, PINK_CAT);
    public static final RegistryEntrySupplier<Block> CHARM_BLUE_GIANT = giantFlower("great_charm_blue", () -> ModItems.CHARM_BLUE_GIANT, () -> ModItems.CHARM_BLUE_SEEDS, CHARM_BLUE);
    public static final RegistryEntrySupplier<Block> LAMP_GRASS_GIANT = giantFlower("kaiser_lamp_grass", () -> ModItems.LAMP_GRASS_GIANT, () -> ModItems.LAMP_GRASS_SEEDS, LAMP_GRASS);
    public static final RegistryEntrySupplier<Block> CHERRY_GRASS_GIANT = giantFlower("king_cherry_grass", () -> ModItems.CHERRY_GRASS_GIANT, () -> ModItems.CHERRY_GRASS_SEEDS, CHERRY_GRASS);
    public static final RegistryEntrySupplier<Block> POM_POM_GRASS_GIANT = giantFlower("king_pom_pom_grass", () -> ModItems.POM_POM_GRASS_GIANT, () -> ModItems.POM_POM_GRASS_SEEDS, POM_POM_GRASS);
    public static final RegistryEntrySupplier<Block> AUTUMN_GRASS_GIANT = giantFlower("big_autumn_grass", () -> ModItems.AUTUMN_GRASS_GIANT, () -> ModItems.AUTUMN_GRASS_SEEDS, AUTUMN_GRASS);
    public static final RegistryEntrySupplier<Block> NOEL_GRASS_GIANT = giantFlower("large_noel_grass", () -> ModItems.NOEL_GRASS_GIANT, () -> ModItems.NOEL_GRASS_SEEDS, NOEL_GRASS);
    public static final RegistryEntrySupplier<Block> FIREFLOWER_GIANT = giantFlower("big_fireflower", () -> ModItems.FIREFLOWER_GIANT, () -> ModItems.FIREFLOWER_SEEDS, FIREFLOWER);
    public static final RegistryEntrySupplier<Block> FOUR_LEAF_CLOVER_GIANT = giantFlower("great_four_leaf_clover", () -> ModItems.FOUR_LEAF_CLOVER_GIANT, () -> ModItems.FOUR_LEAF_CLOVER_SEEDS, FOUR_LEAF_CLOVER);
    public static final RegistryEntrySupplier<Block> IRONLEAF_GIANT = giantFlower("super_ironleaf", () -> ModItems.IRONLEAF_GIANT, () -> ModItems.IRONLEAF_SEEDS, IRONLEAF);
    public static final RegistryEntrySupplier<Block> WHITE_CRYSTAL_GIANT = giantFlower("big_white_crystal", () -> ModItems.WHITE_CRYSTAL_GIANT, () -> ModItems.WHITE_CRYSTAL_SEEDS, WHITE_CRYSTAL);
    public static final RegistryEntrySupplier<Block> RED_CRYSTAL_GIANT = giantFlower("big_red_crystal", () -> ModItems.RED_CRYSTAL_GIANT, () -> ModItems.RED_CRYSTAL_SEEDS, RED_CRYSTAL);
    public static final RegistryEntrySupplier<Block> GREEN_CRYSTAL_GIANT = giantFlower("big_green_crystal", () -> ModItems.GREEN_CRYSTAL_GIANT, () -> ModItems.GREEN_CRYSTAL_SEEDS, GREEN_CRYSTAL);
    public static final RegistryEntrySupplier<Block> BLUE_CRYSTAL_GIANT = giantFlower("big_blue_crystal", () -> ModItems.BLUE_CRYSTAL_GIANT, () -> ModItems.BLUE_CRYSTAL_SEEDS, BLUE_CRYSTAL);
    public static final RegistryEntrySupplier<Block> EMERY_FLOWER_GIANT = giantFlower("great_emery_flower", () -> ModItems.EMERY_FLOWER_GIANT, () -> ModItems.EMERY_FLOWER_SEEDS, EMERY_FLOWER);

    public static final RegistryEntrySupplier<Block> SHIELD_CROP = flower("shield_flower", () -> ModItems.PLANT_SHIELD, () -> ModItems.SHIELD_SEEDS);
    public static final RegistryEntrySupplier<Block> SWORD_CROP = flower("sword_flower", () -> ModItems.PLANT_SWORD, () -> ModItems.SWORD_SEEDS);

    public static final RegistryEntrySupplier<Block> DUNGEON = flower("dungeon_flower", () -> () -> Items.STONE, () -> ModItems.DUNGEON_SEEDS);

    public static final RegistryEntrySupplier<Block> MUSHROOM = herb("mushroom");
    public static final RegistryEntrySupplier<Block> MONARCH_MUSHROOM = herb("monarch_mushroom");
    public static final RegistryEntrySupplier<Block> ELLI_LEAVES = herb("elli_leaves", BlockHerb.GroundTypes.END);
    public static final RegistryEntrySupplier<Block> WITHERED_GRASS = herb("withered_grass", BlockHerb.GroundTypes.SANDY);
    public static final RegistryEntrySupplier<Block> WEEDS = herb("weeds");
    public static final RegistryEntrySupplier<Block> WHITE_GRASS = herb("white_grass");
    public static final RegistryEntrySupplier<Block> INDIGO_GRASS = herb("indigo_grass");
    public static final RegistryEntrySupplier<Block> PURPLE_GRASS = herb("purple_grass");
    public static final RegistryEntrySupplier<Block> GREEN_GRASS = herb("green_grass");
    public static final RegistryEntrySupplier<Block> BLUE_GRASS = herb("blue_grass");
    public static final RegistryEntrySupplier<Block> YELLOW_GRASS = herb("yellow_grass", BlockHerb.GroundTypes.SANDY, BlockHerb.GroundTypes.NETHER);
    public static final RegistryEntrySupplier<Block> RED_GRASS = herb("red_grass", BlockHerb.GroundTypes.NETHER);
    public static final RegistryEntrySupplier<Block> ORANGE_GRASS = herb("orange_grass", BlockHerb.GroundTypes.SANDY, BlockHerb.GroundTypes.NETHER);
    public static final RegistryEntrySupplier<Block> BLACK_GRASS = herb("black_grass", BlockHerb.GroundTypes.END);
    public static final RegistryEntrySupplier<Block> ANTIDOTE_GRASS = herb("antidote_grass");
    public static final RegistryEntrySupplier<Block> MEDICINAL_HERB = herb("medicinal_herb");
    public static final RegistryEntrySupplier<Block> BAMBOO_SPROUT = herb("bamboo_sprout");

    public static final RegistryEntrySupplier<Block> SNOW = BLOCKS.register("snow", () -> new BlockMeltableSnow(BlockBehaviour.Properties.of(Material.TOP_SNOW).randomTicks().strength(0.1f).requiresCorrectToolForDrops().sound(SoundType.SNOW).isViewBlocking((blockState, blockGetter, blockPos) -> blockState.getValue(SnowLayerBlock.LAYERS) >= 8)));

    public static final RegistryEntrySupplier<Block> TREE_SOIL = BLOCKS.register("tree_soil", () -> new BlockTreeRoot(BlockBehaviour.Properties.of(Material.DIRT).sound(SoundType.GRAVEL).strength(-1, 99999)));
    public static final RegistryEntrySupplier<BlockTreeBase> APPLE_TREE = BLOCKS.register("apple_tree", () -> new BlockTreeBase(logProps(), () -> ModFeatures.APPLE_1.value(), () -> ModFeatures.APPLE_2.value(), () -> ModFeatures.APPLE_3.value(), ModItems.APPLE_SAPLING));
    public static final RegistryEntrySupplier<Block> APPLE_SAPLING = BLOCKS.register("apple_sapling", () -> new BlockTreeSapling(BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS), APPLE_TREE));
    public static final RegistryEntrySupplier<RotatedPillarBlock> APPLE_WOOD = BLOCKS.register("apple_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(-1, 99999)));
    public static final RegistryEntrySupplier<Block> APPLE_LEAVES = BLOCKS.register("apple_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).strength(-1, 99999)));
    public static final RegistryEntrySupplier<Block> APPLE = BLOCKS.register("apple_leaves_fruit", () -> new BlockFruitTreeLeaf(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).strength(-1, 99999), () -> Items.APPLE));
    public static final RegistryEntrySupplier<BlockTreeBase> ORANGE_TREE = BLOCKS.register("orange_tree", () -> new BlockTreeBase(logProps(), () -> ModFeatures.ORANGE_1.value(), () -> ModFeatures.ORANGE_2.value(), () -> ModFeatures.ORANGE_3.value(), ModItems.ORANGE_SAPLING));
    public static final RegistryEntrySupplier<Block> ORANGE_SAPLING = BLOCKS.register("orange_sapling", () -> new BlockTreeSapling(BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS), ORANGE_TREE));
    public static final RegistryEntrySupplier<RotatedPillarBlock> ORANGE_WOOD = BLOCKS.register("orange_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(-1, 99999)));
    public static final RegistryEntrySupplier<Block> ORANGE_LEAVES = BLOCKS.register("orange_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).strength(-1, 99999)));
    public static final RegistryEntrySupplier<Block> ORANGE = BLOCKS.register("orange_leaves_fruit", () -> new BlockFruitTreeLeaf(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).strength(-1, 99999), ModItems.ORANGE::get));
    public static final RegistryEntrySupplier<BlockTreeBase> GRAPE_TREE = BLOCKS.register("grape_tree", () -> new BlockTreeBase(logProps(), () -> ModFeatures.GRAPE_1.value(), () -> ModFeatures.GRAPE_2.value(), () -> ModFeatures.GRAPE_3.value(), ModItems.GRAPE_SAPLING));
    public static final RegistryEntrySupplier<Block> GRAPE_SAPLING = BLOCKS.register("grape_sapling", () -> new BlockTreeSapling(BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS), GRAPE_TREE));
    public static final RegistryEntrySupplier<RotatedPillarBlock> GRAPE_WOOD = BLOCKS.register("grape_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(-1, 99999)));
    public static final RegistryEntrySupplier<Block> GRAPE_LEAVES = BLOCKS.register("grape_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).strength(-1, 99999)));
    public static final RegistryEntrySupplier<Block> GRAPE = BLOCKS.register("grape_leaves_fruit", () -> new BlockFruitTreeLeaf(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).strength(-1, 99999), ModItems.GRAPES::get));

    public static final RegistryEntrySupplier<BlockEntityType<BrokenMineralBlockEntity>> BROKEN_MINERAL_TILE = brokenMineralTile("broken_mineral_tile", BROKEN_MINERAL_MAP.values());
    public static final RegistryEntrySupplier<BlockEntityType<AccessoryBlockEntity>> ACCESSORY_TILE = TILES.register("accessory_tile", () -> Platform.INSTANCE.blockEntityType(AccessoryBlockEntity::new, ACCESSORY.get()));
    public static final RegistryEntrySupplier<BlockEntityType<ForgingBlockEntity>> FORGING_TILE = TILES.register("forge_tile", () -> Platform.INSTANCE.blockEntityType(ForgingBlockEntity::new, FORGE.get()));
    public static final RegistryEntrySupplier<BlockEntityType<ChemistryBlockEntity>> CHEMISTRY_TILE = TILES.register("chemistry_tile", () -> Platform.INSTANCE.blockEntityType(ChemistryBlockEntity::new, CHEMISTRY.get()));
    public static final RegistryEntrySupplier<BlockEntityType<CookingBlockEntity>> COOKING_TILE = TILES.register("cooking_tile", () -> Platform.INSTANCE.blockEntityType(CookingBlockEntity::new, COOKING.get()));
    public static final RegistryEntrySupplier<BlockEntityType<BossSpawnerBlockEntity>> BOSS_SPAWNER_TILE = TILES.register("spawner_tile", () -> Platform.INSTANCE.blockEntityType(BossSpawnerBlockEntity::new, BOSS_SPAWNER.get()));
    public static final RegistryEntrySupplier<BlockEntityType<SingleTimeSpawner>> SINGLE_SPAWNER_TILE = TILES.register("single_spawner_tile", () -> Platform.INSTANCE.blockEntityType(SingleTimeSpawner::new, SINGLE_SPAWN_BLOCK.get()));
    public static final RegistryEntrySupplier<BlockEntityType<MonsterBarnBlockEntity>> MONSTER_BARN_BLOCK_ENTITY = TILES.register("monster_barn_block_entity", () -> Platform.INSTANCE.blockEntityType(MonsterBarnBlockEntity::new, MONSTER_BARN.get()));
    public static final RegistryEntrySupplier<BlockEntityType<TreeBlockEntity>> TREE_BLOCK_ENTITY = TILES.register("tree", () -> Platform.INSTANCE.blockEntityType(TreeBlockEntity::new, APPLE_TREE.get()));

    public static RegistryEntrySupplier<Block> mineral(EnumMineralTier name) {
        RegistryEntrySupplier<Block> reg = BLOCKS.register("ore_" + name.getSerializedName(), () -> new BlockMineral(name, BlockBehaviour.Properties.of(Material.STONE).strength(5, 10)
                .requiresCorrectToolForDrops()));
        MINERAL_MAP.put(name, reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block> brokenMineral(EnumMineralTier name) {
        RegistryEntrySupplier<Block> reg = BLOCKS.register("ore_broken_" + name.getSerializedName(), () -> new BlockBrokenMineral(name, BlockBehaviour.Properties.of(Material.STONE).strength(30, 15)));
        BROKEN_MINERAL_MAP.put(name, reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block> crop(String name, Supplier<Supplier<Item>> crop, Supplier<Supplier<Item>> seed) {
        RegistryEntrySupplier<Block> reg = BLOCKS.register(name, () -> new BlockCrop(BlockBehaviour.Properties.of(Material.PLANT).noCollission().sound(SoundType.GRASS), crop.get(), seed.get()));
        CROPS.add(reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block> giantCrop(String name, Supplier<Supplier<Item>> giant, Supplier<Supplier<Item>> seed, RegistryEntrySupplier<Block> crop) {
        RegistryEntrySupplier<Block> reg = BLOCKS.register(name, () -> new BlockGiantCrop(BlockBehaviour.Properties.of(Material.PLANT).noCollission().sound(SoundType.GRASS), giant.get(), seed.get()));
        CROPS.add(reg);
        if (Platform.INSTANCE.isDatagen())
            GIANT_CROP_MAP.put(crop, reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block> flower(String name, Supplier<Supplier<Item>> crop, Supplier<Supplier<Item>> seed) {
        RegistryEntrySupplier<Block> reg = BLOCKS.register(name, () -> new BlockCrop(BlockBehaviour.Properties.of(Material.PLANT).noCollission().sound(SoundType.GRASS), crop.get(), seed.get()));
        FLOWERS.add(reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block> giantFlower(String name, Supplier<Supplier<Item>> giant, Supplier<Supplier<Item>> seed, RegistryEntrySupplier<Block> flower) {
        RegistryEntrySupplier<Block> reg = BLOCKS.register(name, () -> new BlockGiantCrop(BlockBehaviour.Properties.of(Material.PLANT).noCollission().sound(SoundType.GRASS), giant.get(), seed.get()));
        FLOWERS.add(reg);
        if (Platform.INSTANCE.isDatagen())
            GIANT_CROP_MAP.put(flower, reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block> herb(String name, BlockHerb.GroundTypes... types) {
        RegistryEntrySupplier<Block> reg = BLOCKS.register(name, () -> new BlockHerb(BlockBehaviour.Properties.of(Material.PLANT).noCollission().sound(SoundType.GRASS), types));
        HERBS.add(reg);
        return reg;
    }

    /**
     * Adds another collection to the first and returns the first
     */
    public static <T> Collection<T> combine(Collection<T> one, Collection<T> other) {
        one.addAll(other);
        return one;
    }

    public static RegistryEntrySupplier<BlockEntityType<BrokenMineralBlockEntity>> brokenMineralTile(String name, Collection<RegistryEntrySupplier<Block>> blocks) {
        return TILES.register(name, () -> Platform.INSTANCE.blockEntityType(BrokenMineralBlockEntity::new, blocks.stream().map(RegistryEntrySupplier::get).collect(Collectors.toSet())));
    }

    private static BlockBehaviour.Properties logProps() {
        return BlockBehaviour.Properties.of(Material.WOOD).strength(2.0f).sound(SoundType.WOOD);
    }
}
