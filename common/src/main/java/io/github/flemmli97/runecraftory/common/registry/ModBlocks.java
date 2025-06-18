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
import io.github.flemmli97.runecraftory.common.blocks.entity.AccessoryBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.entity.BossSpawnerBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.entity.BrokenMineralBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.entity.ChemistryBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.entity.CookingBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.entity.ForgingBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.entity.MonsterBarnBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.entity.SingleTimeSpawner;
import io.github.flemmli97.runecraftory.common.blocks.entity.TreeBlockEntity;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


public class ModBlocks {

    public static final LoaderRegister<Block> BLOCKS = LoaderRegistryAccess.INSTANCE.of(Registries.BLOCK, RuneCraftory.MODID);
    public static final LoaderRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = LoaderRegistryAccess.INSTANCE.of(Registries.BLOCK_ENTITY_TYPE, RuneCraftory.MODID);

    public static final List<RegistryEntrySupplier<Block, ?>> CROPS = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Block, ?>> FLOWERS = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Block, ?>> HERBS = new ArrayList<>();
    public static final Map<RegistryEntrySupplier<Block, ?>, RegistryEntrySupplier<Block, ?>> GIANT_CROP_MAP = new HashMap<>();
    public static final EnumMap<EnumMineralTier, RegistryEntrySupplier<Block, ?>> MINERAL_MAP = new EnumMap<>(EnumMineralTier.class);
    public static final EnumMap<EnumMineralTier, RegistryEntrySupplier<Block, ?>> BROKEN_MINERAL_MAP = new EnumMap<>(EnumMineralTier.class);

    public static final RegistryEntrySupplier<Block, BlockForge> FORGE = BLOCKS.register("forge", () -> new BlockForge(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3, 100)));
    public static final RegistryEntrySupplier<Block, BlockCooking> COOKING = BLOCKS.register("cooking_table", () -> new BlockCooking(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3, 100)));
    public static final RegistryEntrySupplier<Block, BlockChemistry> CHEMISTRY = BLOCKS.register("chemistry_set", () -> new BlockChemistry(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3, 100)));
    public static final RegistryEntrySupplier<Block, BlockAccessory> ACCESSORY = BLOCKS.register("accessory_workbench", () -> new BlockAccessory(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3, 100)));

    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_IRON = mineral(EnumMineralTier.IRON);
    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_TIN = mineral(EnumMineralTier.TIN);
    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_SILVER = mineral(EnumMineralTier.SILVER);
    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_GOLD = mineral(EnumMineralTier.GOLD);
    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_PLATINUM = mineral(EnumMineralTier.PLATINUM);
    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_ORICHALCUM = mineral(EnumMineralTier.ORICHALCUM);
    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_DIAMOND = mineral(EnumMineralTier.DIAMOND);
    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_DRAGONIC = mineral(EnumMineralTier.DRAGONIC);
    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_AQUAMARINE = mineral(EnumMineralTier.AQUAMARINE);
    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_AMETHYST = mineral(EnumMineralTier.AMETHYST);
    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_RUBY = mineral(EnumMineralTier.RUBY);
    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_EMERALD = mineral(EnumMineralTier.EMERALD);
    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_SAPPHIRE = mineral(EnumMineralTier.SAPPHIRE);

    public static final RegistryEntrySupplier<Block, BlockBrokenMineral> BROKEN_MINERAL_IRON = brokenMineral(EnumMineralTier.IRON);
    public static final RegistryEntrySupplier<Block, BlockBrokenMineral> BROKEN_MINERAL_TIN = brokenMineral(EnumMineralTier.TIN);
    public static final RegistryEntrySupplier<Block, BlockBrokenMineral> BROKEN_MINERAL_SILVER = brokenMineral(EnumMineralTier.SILVER);
    public static final RegistryEntrySupplier<Block, BlockBrokenMineral> BROKEN_MINERAL_GOLD = brokenMineral(EnumMineralTier.GOLD);
    public static final RegistryEntrySupplier<Block, BlockBrokenMineral> BROKEN_MINERAL_PLATINUM = brokenMineral(EnumMineralTier.PLATINUM);
    public static final RegistryEntrySupplier<Block, BlockBrokenMineral> BROKEN_MINERAL_ORICHALCUM = brokenMineral(EnumMineralTier.ORICHALCUM);
    public static final RegistryEntrySupplier<Block, BlockBrokenMineral> BROKEN_MINERAL_DIAMOND = brokenMineral(EnumMineralTier.DIAMOND);
    public static final RegistryEntrySupplier<Block, BlockBrokenMineral> BROKEN_MINERAL_DRAGONIC = brokenMineral(EnumMineralTier.DRAGONIC);
    public static final RegistryEntrySupplier<Block, BlockBrokenMineral> BROKEN_MINERAL_AQUAMARINE = brokenMineral(EnumMineralTier.AQUAMARINE);
    public static final RegistryEntrySupplier<Block, BlockBrokenMineral> BROKEN_MINERAL_AMETHYST = brokenMineral(EnumMineralTier.AMETHYST);
    public static final RegistryEntrySupplier<Block, BlockBrokenMineral> BROKEN_MINERAL_RUBY = brokenMineral(EnumMineralTier.RUBY);
    public static final RegistryEntrySupplier<Block, BlockBrokenMineral> BROKEN_MINERAL_EMERALD = brokenMineral(EnumMineralTier.EMERALD);
    public static final RegistryEntrySupplier<Block, BlockBrokenMineral> BROKEN_MINERAL_SAPPHIRE = brokenMineral(EnumMineralTier.SAPPHIRE);

    public static final RegistryEntrySupplier<Block, BlockBossSpawner> BOSS_SPAWNER = BLOCKS.register("boss_spawner", () -> new BlockBossSpawner(BlockBehaviour.Properties.of(Material.METAL).strength(60, 9999).noOcclusion()));
    public static final RegistryEntrySupplier<Block, BlockShippingBin> SHIPPING = BLOCKS.register("shipping_bin", () -> new BlockShippingBin(BlockBehaviour.Properties.of(Material.WOOD).strength(3, 10)));
    public static final RegistryEntrySupplier<Block, BlockSingleTimeSpawner> SINGLE_SPAWN_BLOCK = BLOCKS.register("one_time_spawner", () -> new BlockSingleTimeSpawner(BlockBehaviour.Properties.of(Material.METAL).strength(60, 9999).noOcclusion()));
    public static final RegistryEntrySupplier<Block, BlockCashRegister> CASH_REGISTER = BLOCKS.register("cash_register", () -> new BlockCashRegister(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3, 5)));
    public static final RegistryEntrySupplier<Block, BlockMonsterBarn> MONSTER_BARN = BLOCKS.register("monster_barn", () -> new BlockMonsterBarn(BlockBehaviour.Properties.of(Material.GRASS).sound(SoundType.GRASS).noOcclusion().noCollission().strength(1, 10000)));
    public static final RegistryEntrySupplier<Block, BlockQuestboard> QUEST_BOARD = BLOCKS.register("quest_board", () -> new BlockQuestboard(BlockBehaviour.Properties.of(Material.WOOD).sound(SoundType.WOOD).noOcclusion().strength(2, 5)));

    public static final RegistryEntrySupplier<Block, BlockCrop> TURNIP = crop("turnip", () -> ModItems.TURNIP, () -> ModItems.TURNIP_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> TURNIP_PINK = crop("turnip_pink", () -> ModItems.TURNIP_PINK, () -> ModItems.TURNIP_PINK_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> CABBAGE = crop("cabbage", () -> ModItems.CABBAGE, () -> ModItems.CABBAGE_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> PINK_MELON = crop("pink_melon", () -> ModItems.PINK_MELON, () -> ModItems.PINK_MELON_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> PINEAPPLE = crop("pineapple", () -> ModItems.PINEAPPLE, () -> ModItems.PINEAPPLE_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> STRAWBERRY = crop("strawberry", () -> ModItems.STRAWBERRY, () -> ModItems.STRAWBERRY_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> GOLDEN_TURNIP = crop("golden_turnip", () -> ModItems.GOLDEN_TURNIP, () -> ModItems.GOLD_TURNIP_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> GOLDEN_POTATO = crop("golden_potato", () -> ModItems.GOLDEN_POTATO, () -> ModItems.GOLD_POTATO_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> GOLDEN_PUMPKIN = crop("golden_pumpkin", () -> ModItems.GOLDEN_PUMPKIN, () -> ModItems.GOLD_PUMPKIN_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> GOLDEN_CABBAGE = crop("golden_cabbage", () -> ModItems.GOLDEN_CABBAGE, () -> ModItems.GOLD_CABBAGE_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> HOT_HOT_FRUIT = crop("hot_hot_fruit", () -> ModItems.HOT_HOT_FRUIT, () -> ModItems.HOT_HOT_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> BOK_CHOY = crop("bok_choy", () -> ModItems.BOK_CHOY, () -> ModItems.BOK_CHOY_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> LEEK = crop("leek", () -> ModItems.LEEK, () -> ModItems.LEEK_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> RADISH = crop("radish", () -> ModItems.RADISH, () -> ModItems.RADISH_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> SPINACH = crop("spinach", () -> ModItems.SPINACH, () -> ModItems.SPINACH_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> GREEN_PEPPER = crop("green_pepper", () -> ModItems.GREEN_PEPPER, () -> ModItems.GREEN_PEPPER_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> YAM = crop("yam", () -> ModItems.YAM, () -> ModItems.YAM_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> EGGPLANT = crop("eggplant", () -> ModItems.EGGPLANT, () -> ModItems.EGGPLANT_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> TOMATO = crop("tomato", () -> ModItems.TOMATO, () -> ModItems.TOMATO_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> CORN = crop("corn", () -> ModItems.CORN, () -> ModItems.CORN_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> CUCUMBER = crop("cucumber", () -> ModItems.CUCUMBER, () -> ModItems.CUCUMBER_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> PUMPKIN = crop("pumpkin", () -> ModItems.PUMPKIN, () -> ModItems.PUMPKIN_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> ONION = crop("onion", () -> ModItems.ONION, () -> ModItems.ONION_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> TURNIP_GIANT = giantCrop("tyrant_turnip", () -> ModItems.TURNIP_GIANT, () -> ModItems.TURNIP_SEEDS, TURNIP);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> TURNIP_PINK_GIANT = giantCrop("colossal_pink", () -> ModItems.TURNIP_PINK_GIANT, () -> ModItems.TURNIP_PINK_SEEDS, TURNIP_PINK);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> CABBAGE_GIANT = giantCrop("king_cabbage", () -> ModItems.CABBAGE_GIANT, () -> ModItems.CABBAGE_SEEDS, CABBAGE);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> PINK_MELON_GIANT = giantCrop("conqueror_melon", () -> ModItems.PINK_MELON_GIANT, () -> ModItems.PINK_MELON_SEEDS, PINK_MELON);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> PINEAPPLE_GIANT = giantCrop("king_pineapple", () -> ModItems.PINEAPPLE_GIANT, () -> ModItems.PINEAPPLE_SEEDS, PINEAPPLE);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> STRAWBERRY_GIANT = giantCrop("sultan_strawberry", () -> ModItems.STRAWBERRY_GIANT, () -> ModItems.STRAWBERRY_SEEDS, STRAWBERRY);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> GOLDEN_TURNIP_GIANT = giantCrop("golden_tyrant_turnip", () -> ModItems.GOLDEN_TURNIP_GIANT, () -> ModItems.GOLD_TURNIP_SEEDS, GOLDEN_TURNIP);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> GOLDEN_POTATO_GIANT = giantCrop("gold_prince_potato", () -> ModItems.GOLDEN_POTATO_GIANT, () -> ModItems.GOLD_POTATO_SEEDS, GOLDEN_POTATO);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> GOLDEN_PUMPKIN_GIANT = giantCrop("golden_doom_pumpkin", () -> ModItems.GOLDEN_PUMPKIN_GIANT, () -> ModItems.GOLD_PUMPKIN_SEEDS, PUMPKIN);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> GOLDEN_CABBAGE_GIANT = giantCrop("golden_king_cabbage", () -> ModItems.GOLDEN_CABBAGE_GIANT, () -> ModItems.GOLD_CABBAGE_SEEDS, CABBAGE_GIANT);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> HOT_HOT_FRUIT_GIANT = giantCrop("giant_hot_hot_fruit", () -> ModItems.HOT_HOT_FRUIT_GIANT, () -> ModItems.HOT_HOT_SEEDS, HOT_HOT_FRUIT);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> BOK_CHOY_GIANT = giantCrop("boss_bok_choy", () -> ModItems.BOK_CHOY_GIANT, () -> ModItems.BOK_CHOY_SEEDS, BOK_CHOY);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> LEEK_GIANT = giantCrop("legendary_leek", () -> ModItems.LEEK_GIANT, () -> ModItems.LEEK_SEEDS, LEEK);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> RADISH_GIANT = giantCrop("noble_radish", () -> ModItems.RADISH_GIANT, () -> ModItems.RADISH_SEEDS, RADISH);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> SPINACH_GIANT = giantCrop("sovereign_spinach", () -> ModItems.SPINACH_GIANT, () -> ModItems.SPINACH_SEEDS, SPINACH);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> GREEN_PEPPER_GIANT = giantCrop("green_pepper_rex", () -> ModItems.GREEN_PEPPER_GIANT, () -> ModItems.GREEN_PEPPER_SEEDS, GREEN_PEPPER);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> YAM_GIANT = giantCrop("lorldy_yam", () -> ModItems.YAM_GIANT, () -> ModItems.YAM_SEEDS, YAM);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> EGGPLANT_GIANT = giantCrop("emperor_eggplant", () -> ModItems.EGGPLANT_GIANT, () -> ModItems.EGGPLANT_SEEDS, EGGPLANT);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> TOMATO_GIANT = giantCrop("titan_tomato", () -> ModItems.TOMATO_GIANT, () -> ModItems.TOMATO_SEEDS, TOMATO);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> CORN_GIANT = giantCrop("gigant_corn", () -> ModItems.CORN_GIANT, () -> ModItems.CORN_SEEDS, CORN);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> CUCUMBER_GIANT = giantCrop("kaiser_cucumber", () -> ModItems.CUCUMBER_GIANT, () -> ModItems.CUCUMBER_SEEDS, CUCUMBER);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> PUMPKIN_GIANT = giantCrop("doom_pumpkin", () -> ModItems.PUMPKIN_GIANT, () -> ModItems.PUMPKIN_SEEDS, PUMPKIN);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> ONION_GIANT = giantCrop("ultra_onion", () -> ModItems.ONION_GIANT, () -> ModItems.ONION_SEEDS, ONION);
    public static final RegistryEntrySupplier<Block, BlockCrop> FODDER = crop("fodder", () -> ModItems.FODDER, () -> ModItems.FODDER_SEEDS);

    public static final RegistryEntrySupplier<Block, BlockCrop> POTATO_GIANT = crop("potato", () -> () -> Items.POTATO, () -> () -> Items.POTATO);
    public static final RegistryEntrySupplier<Block, BlockCrop> CARROT_GIANT = crop("carrot", () -> () -> Items.CARROT, () -> () -> Items.CARROT);

    public static final RegistryEntrySupplier<Block, BlockCrop> TOYHERB = flower("toyherb", () -> ModItems.TOYHERB, () -> ModItems.TOYHERB_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> MOONDROP_FLOWER = flower("moondrop_flower", () -> ModItems.MOONDROP_FLOWER, () -> ModItems.MOONDROP_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> PINK_CAT = flower("pink_cat", () -> ModItems.PINK_CAT, () -> ModItems.PINK_CAT_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> CHARM_BLUE = flower("charm_blue", () -> ModItems.CHARM_BLUE, () -> ModItems.CHARM_BLUE_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> LAMP_GRASS = flower("lamp_grass", () -> ModItems.LAMP_GRASS, () -> ModItems.LAMP_GRASS_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> CHERRY_GRASS = flower("cherry_grass", () -> ModItems.CHERRY_GRASS, () -> ModItems.CHERRY_GRASS_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> POM_POM_GRASS = flower("pom_pom_grass", () -> ModItems.POM_POM_GRASS, () -> ModItems.POM_POM_GRASS_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> AUTUMN_GRASS = flower("autumn_grass", () -> ModItems.AUTUMN_GRASS, () -> ModItems.AUTUMN_GRASS_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> NOEL_GRASS = flower("noel_grass", () -> ModItems.NOEL_GRASS, () -> ModItems.NOEL_GRASS_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> FIREFLOWER = flower("fireflower", () -> ModItems.FIREFLOWER, () -> ModItems.FIREFLOWER_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> FOUR_LEAF_CLOVER = flower("four_leaf_clover", () -> ModItems.FOUR_LEAF_CLOVER, () -> ModItems.FOUR_LEAF_CLOVER_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> IRONLEAF = flower("ironleaf", () -> ModItems.IRONLEAF, () -> ModItems.IRONLEAF_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> WHITE_CRYSTAL = flower("white_crystal", () -> ModItems.WHITE_CRYSTAL, () -> ModItems.WHITE_CRYSTAL_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> RED_CRYSTAL = flower("red_crystal", () -> ModItems.RED_CRYSTAL, () -> ModItems.RED_CRYSTAL_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> GREEN_CRYSTAL = flower("green_crystal", () -> ModItems.GREEN_CRYSTAL, () -> ModItems.GREEN_CRYSTAL_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> BLUE_CRYSTAL = flower("blue_crystal", () -> ModItems.BLUE_CRYSTAL, () -> ModItems.BLUE_CRYSTAL_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> EMERY_FLOWER = flower("emery_flower", () -> ModItems.EMERY_FLOWER, () -> ModItems.EMERY_FLOWER_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> TOYHERB_GIANT = giantFlower("ultra_toyherb", () -> ModItems.TOYHERB_GIANT, () -> ModItems.TOYHERB_SEEDS, TOYHERB);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> MOONDROP_FLOWER_GIANT = giantFlower("ultra_moondrop_flower", () -> ModItems.MOONDROP_FLOWER_GIANT, () -> ModItems.MOONDROP_SEEDS, MOONDROP_FLOWER);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> PINK_CAT_GIANT = giantFlower("king_pink_cat", () -> ModItems.PINK_CAT_GIANT, () -> ModItems.PINK_CAT_SEEDS, PINK_CAT);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> CHARM_BLUE_GIANT = giantFlower("great_charm_blue", () -> ModItems.CHARM_BLUE_GIANT, () -> ModItems.CHARM_BLUE_SEEDS, CHARM_BLUE);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> LAMP_GRASS_GIANT = giantFlower("kaiser_lamp_grass", () -> ModItems.LAMP_GRASS_GIANT, () -> ModItems.LAMP_GRASS_SEEDS, LAMP_GRASS);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> CHERRY_GRASS_GIANT = giantFlower("king_cherry_grass", () -> ModItems.CHERRY_GRASS_GIANT, () -> ModItems.CHERRY_GRASS_SEEDS, CHERRY_GRASS);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> POM_POM_GRASS_GIANT = giantFlower("king_pom_pom_grass", () -> ModItems.POM_POM_GRASS_GIANT, () -> ModItems.POM_POM_GRASS_SEEDS, POM_POM_GRASS);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> AUTUMN_GRASS_GIANT = giantFlower("big_autumn_grass", () -> ModItems.AUTUMN_GRASS_GIANT, () -> ModItems.AUTUMN_GRASS_SEEDS, AUTUMN_GRASS);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> NOEL_GRASS_GIANT = giantFlower("large_noel_grass", () -> ModItems.NOEL_GRASS_GIANT, () -> ModItems.NOEL_GRASS_SEEDS, NOEL_GRASS);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> FIREFLOWER_GIANT = giantFlower("big_fireflower", () -> ModItems.FIREFLOWER_GIANT, () -> ModItems.FIREFLOWER_SEEDS, FIREFLOWER);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> FOUR_LEAF_CLOVER_GIANT = giantFlower("great_four_leaf_clover", () -> ModItems.FOUR_LEAF_CLOVER_GIANT, () -> ModItems.FOUR_LEAF_CLOVER_SEEDS, FOUR_LEAF_CLOVER);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> IRONLEAF_GIANT = giantFlower("super_ironleaf", () -> ModItems.IRONLEAF_GIANT, () -> ModItems.IRONLEAF_SEEDS, IRONLEAF);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> WHITE_CRYSTAL_GIANT = giantFlower("big_white_crystal", () -> ModItems.WHITE_CRYSTAL_GIANT, () -> ModItems.WHITE_CRYSTAL_SEEDS, WHITE_CRYSTAL);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> RED_CRYSTAL_GIANT = giantFlower("big_red_crystal", () -> ModItems.RED_CRYSTAL_GIANT, () -> ModItems.RED_CRYSTAL_SEEDS, RED_CRYSTAL);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> GREEN_CRYSTAL_GIANT = giantFlower("big_green_crystal", () -> ModItems.GREEN_CRYSTAL_GIANT, () -> ModItems.GREEN_CRYSTAL_SEEDS, GREEN_CRYSTAL);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> BLUE_CRYSTAL_GIANT = giantFlower("big_blue_crystal", () -> ModItems.BLUE_CRYSTAL_GIANT, () -> ModItems.BLUE_CRYSTAL_SEEDS, BLUE_CRYSTAL);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> EMERY_FLOWER_GIANT = giantFlower("great_emery_flower", () -> ModItems.EMERY_FLOWER_GIANT, () -> ModItems.EMERY_FLOWER_SEEDS, EMERY_FLOWER);

    public static final RegistryEntrySupplier<Block, BlockCrop> SHIELD_CROP = flower("shield_flower", () -> ModItems.PLANT_SHIELD, () -> ModItems.SHIELD_SEEDS);
    public static final RegistryEntrySupplier<Block, BlockCrop> SWORD_CROP = flower("sword_flower", () -> ModItems.PLANT_SWORD, () -> ModItems.SWORD_SEEDS);

    public static final RegistryEntrySupplier<Block, BlockCrop> DUNGEON = flower("dungeon_flower", () -> () -> Items.STONE, () -> ModItems.DUNGEON_SEEDS);

    public static final RegistryEntrySupplier<Block, BlockHerb> MUSHROOM = herb("mushroom");
    public static final RegistryEntrySupplier<Block, BlockHerb> MONARCH_MUSHROOM = herb("monarch_mushroom");
    public static final RegistryEntrySupplier<Block, BlockHerb> ELLI_LEAVES = herb("elli_leaves", BlockHerb.GroundTypes.END);
    public static final RegistryEntrySupplier<Block, BlockHerb> WITHERED_GRASS = herb("withered_grass", BlockHerb.GroundTypes.SANDY);
    public static final RegistryEntrySupplier<Block, BlockHerb> WEEDS = herb("weeds");
    public static final RegistryEntrySupplier<Block, BlockHerb> WHITE_GRASS = herb("white_grass");
    public static final RegistryEntrySupplier<Block, BlockHerb> INDIGO_GRASS = herb("indigo_grass");
    public static final RegistryEntrySupplier<Block, BlockHerb> PURPLE_GRASS = herb("purple_grass");
    public static final RegistryEntrySupplier<Block, BlockHerb> GREEN_GRASS = herb("green_grass");
    public static final RegistryEntrySupplier<Block, BlockHerb> BLUE_GRASS = herb("blue_grass");
    public static final RegistryEntrySupplier<Block, BlockHerb> YELLOW_GRASS = herb("yellow_grass", BlockHerb.GroundTypes.SANDY, BlockHerb.GroundTypes.NETHER);
    public static final RegistryEntrySupplier<Block, BlockHerb> RED_GRASS = herb("red_grass", BlockHerb.GroundTypes.NETHER);
    public static final RegistryEntrySupplier<Block, BlockHerb> ORANGE_GRASS = herb("orange_grass", BlockHerb.GroundTypes.SANDY, BlockHerb.GroundTypes.NETHER);
    public static final RegistryEntrySupplier<Block, BlockHerb> BLACK_GRASS = herb("black_grass", BlockHerb.GroundTypes.END);
    public static final RegistryEntrySupplier<Block, BlockHerb> ANTIDOTE_GRASS = herb("antidote_grass");
    public static final RegistryEntrySupplier<Block, BlockHerb> MEDICINAL_HERB = herb("medicinal_herb");
    public static final RegistryEntrySupplier<Block, BlockHerb> BAMBOO_SPROUT = herb("bamboo_sprout");

    public static final RegistryEntrySupplier<Block, BlockTreeRoot> TREE_SOIL = BLOCKS.register("tree_soil", () -> new BlockTreeRoot(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).sound(SoundType.GRAVEL).strength(-1, 99999)));
    public static final RegistryEntrySupplier<Block, BlockTreeBase> APPLE_TREE = BLOCKS.register("apple_tree", () -> new BlockTreeBase(logProps(), () -> ModFeatures.APPLE_1.value(), () -> ModFeatures.APPLE_2.value(), () -> ModFeatures.APPLE_3.value(), ModItems.APPLE_SAPLING));
    public static final RegistryEntrySupplier<Block, BlockTreeSapling> APPLE_SAPLING = BLOCKS.register("apple_sapling", () -> new BlockTreeSapling(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING), APPLE_TREE));
    public static final RegistryEntrySupplier<Block, RotatedPillarBlock> APPLE_WOOD = BLOCKS.register("apple_wood", () -> new RotatedPillarBlock(logProps().strength(-1, 99999)));
    public static final RegistryEntrySupplier<Block, LeavesBlock> APPLE_LEAVES = BLOCKS.register("apple_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).strength(-1, 99999)));
    public static final RegistryEntrySupplier<Block, BlockFruitTreeLeaf> APPLE = BLOCKS.register("apple_leaves_fruit", () -> new BlockFruitTreeLeaf(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).strength(-1, 99999), () -> Items.APPLE));
    public static final RegistryEntrySupplier<Block, BlockTreeBase> ORANGE_TREE = BLOCKS.register("orange_tree", () -> new BlockTreeBase(logProps(), () -> ModFeatures.ORANGE_1.value(), () -> ModFeatures.ORANGE_2.value(), () -> ModFeatures.ORANGE_3.value(), ModItems.ORANGE_SAPLING));
    public static final RegistryEntrySupplier<Block, BlockTreeSapling> ORANGE_SAPLING = BLOCKS.register("orange_sapling", () -> new BlockTreeSapling(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING), ORANGE_TREE));
    public static final RegistryEntrySupplier<Block, RotatedPillarBlock> ORANGE_WOOD = BLOCKS.register("orange_wood", () -> new RotatedPillarBlock(logProps().strength(-1, 99999)));
    public static final RegistryEntrySupplier<Block, LeavesBlock> ORANGE_LEAVES = BLOCKS.register("orange_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).strength(-1, 99999)));
    public static final RegistryEntrySupplier<Block, BlockFruitTreeLeaf> ORANGE = BLOCKS.register("orange_leaves_fruit", () -> new BlockFruitTreeLeaf(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).strength(-1, 99999), ModItems.ORANGE));
    public static final RegistryEntrySupplier<Block, BlockTreeBase> GRAPE_TREE = BLOCKS.register("grape_tree", () -> new BlockTreeBase(logProps(), () -> ModFeatures.GRAPE_1.value(), () -> ModFeatures.GRAPE_2.value(), () -> ModFeatures.GRAPE_3.value(), ModItems.GRAPE_SAPLING));
    public static final RegistryEntrySupplier<Block, BlockTreeSapling> GRAPE_SAPLING = BLOCKS.register("grape_sapling", () -> new BlockTreeSapling(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING), GRAPE_TREE));
    public static final RegistryEntrySupplier<Block, RotatedPillarBlock> GRAPE_WOOD = BLOCKS.register("grape_wood", () -> new RotatedPillarBlock(logProps().sound(SoundType.WOOD).strength(-1, 99999)));
    public static final RegistryEntrySupplier<Block, LeavesBlock> GRAPE_LEAVES = BLOCKS.register("grape_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).strength(-1, 99999)));
    public static final RegistryEntrySupplier<Block, BlockFruitTreeLeaf> GRAPE = BLOCKS.register("grape_leaves_fruit", () -> new BlockFruitTreeLeaf(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).strength(-1, 99999), ModItems.GRAPES));

    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<BrokenMineralBlockEntity>> BROKEN_MINERAL_TILE = brokenMineralTile("broken_mineral_tile", BROKEN_MINERAL_MAP.values());
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<AccessoryBlockEntity>> ACCESSORY_TILE = BLOCK_ENTITY_TYPES.register("accessory_tile", () -> BlockEntityType.Builder.of(AccessoryBlockEntity::new, ACCESSORY.get()).build(null));
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<ForgingBlockEntity>> FORGING_TILE = BLOCK_ENTITY_TYPES.register("forge_tile", () -> BlockEntityType.Builder.of(ForgingBlockEntity::new, FORGE.get()).build(null));
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<ChemistryBlockEntity>> CHEMISTRY_TILE = BLOCK_ENTITY_TYPES.register("chemistry_tile", () -> BlockEntityType.Builder.of(ChemistryBlockEntity::new, CHEMISTRY.get()).build(null));
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<CookingBlockEntity>> COOKING_TILE = BLOCK_ENTITY_TYPES.register("cooking_tile", () -> BlockEntityType.Builder.of(CookingBlockEntity::new, COOKING.get()).build(null));
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<BossSpawnerBlockEntity>> BOSS_SPAWNER_TILE = BLOCK_ENTITY_TYPES.register("spawner_tile", () -> BlockEntityType.Builder.of(BossSpawnerBlockEntity::new, BOSS_SPAWNER.get()).build(null));
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<SingleTimeSpawner>> SINGLE_SPAWNER_TILE = BLOCK_ENTITY_TYPES.register("single_spawner_tile", () -> BlockEntityType.Builder.of(SingleTimeSpawner::new, SINGLE_SPAWN_BLOCK.get()).build(null));
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<MonsterBarnBlockEntity>> MONSTER_BARN_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("monster_barn_block_entity", () -> BlockEntityType.Builder.of(MonsterBarnBlockEntity::new, MONSTER_BARN.get()).build(null));
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<TreeBlockEntity>> TREE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("tree", () -> BlockEntityType.Builder.of(TreeBlockEntity::new, APPLE_TREE.get(), ORANGE_TREE.get(), GRAPE_TREE.get()).build(null));

    public static RegistryEntrySupplier<Block, BlockMineral> mineral(EnumMineralTier name) {
        RegistryEntrySupplier<Block, BlockMineral> reg = BLOCKS.register("ore_" + name.getSerializedName(), () -> new BlockMineral(name, BlockBehaviour.Properties.of(Material.STONE).strength(5, 10)
                .requiresCorrectToolForDrops()));
        MINERAL_MAP.put(name, reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block, BlockBrokenMineral> brokenMineral(EnumMineralTier name) {
        RegistryEntrySupplier<Block, BlockBrokenMineral> reg = BLOCKS.register("ore_broken_" + name.getSerializedName(), () -> new BlockBrokenMineral(name, BlockBehaviour.Properties.of(Material.STONE).strength(30, 15)));
        BROKEN_MINERAL_MAP.put(name, reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block, BlockCrop> crop(String name, Supplier<Supplier<? extends Item>> crop, Supplier<Supplier<? extends Item>> seed) {
        RegistryEntrySupplier<Block, BlockCrop> reg = BLOCKS.register(name, () -> new BlockCrop(cropProps(), crop.get(), seed.get()));
        CROPS.add(reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block, BlockGiantCrop> giantCrop(String name, Supplier<Supplier<? extends Item>> giant, Supplier<Supplier<? extends Item>> seed, RegistryEntrySupplier<Block> crop) {
        RegistryEntrySupplier<Block, BlockGiantCrop> reg = BLOCKS.register(name, () -> new BlockGiantCrop(cropProps(), giant.get(), seed.get()));
        CROPS.add(reg);
        if (Platform.INSTANCE.isDatagen())
            GIANT_CROP_MAP.put(crop, reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block, BlockCrop> flower(String name, Supplier<Supplier<? extends Item>> crop, Supplier<Supplier<? extends Item>> seed) {
        RegistryEntrySupplier<Block, BlockCrop> reg = BLOCKS.register(name, () -> new BlockCrop(cropProps(), crop.get(), seed.get()));
        FLOWERS.add(reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block, BlockGiantCrop> giantFlower(String name, Supplier<Supplier<? extends Item>> giant, Supplier<Supplier<? extends Item>> seed, RegistryEntrySupplier<Block, ?> flower) {
        RegistryEntrySupplier<Block, BlockGiantCrop> reg = BLOCKS.register(name, () -> new BlockGiantCrop(cropProps(), giant.get(), seed.get()));
        FLOWERS.add(reg);
        if (Platform.INSTANCE.isDatagen())
            GIANT_CROP_MAP.put(flower, reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block, BlockHerb> herb(String name, BlockHerb.GroundTypes... types) {
        RegistryEntrySupplier<Block, BlockHerb> reg = BLOCKS.register(name, () -> new BlockHerb(plantProps(), types));
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

    public static RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<BrokenMineralBlockEntity>> brokenMineralTile(String name, Collection<RegistryEntrySupplier<Block, ?>> blocks) {
        return BLOCK_ENTITY_TYPES.register(name, () -> BlockEntityType.Builder.of(BrokenMineralBlockEntity::new,
                blocks.stream().map(RegistryEntrySupplier::get).toArray(Block[]::new)).build(null));
    }

    private static BlockBehaviour.Properties logProps() {
        return BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).strength(2.0f).sound(SoundType.WOOD);
    }

    private static BlockBehaviour.Properties plantProps() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY);
    }

    private static BlockBehaviour.Properties cropProps() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY);
    }
}
