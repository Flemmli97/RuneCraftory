package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.blocks.AccessoryBlock;
import io.github.flemmli97.runecraftory.common.blocks.BossSpawnerBlock;
import io.github.flemmli97.runecraftory.common.blocks.BrokenMineralBlock;
import io.github.flemmli97.runecraftory.common.blocks.CashRegisterBlock;
import io.github.flemmli97.runecraftory.common.blocks.ChemistryBlock;
import io.github.flemmli97.runecraftory.common.blocks.CookingBlock;
import io.github.flemmli97.runecraftory.common.blocks.ExtendedCropBlock;
import io.github.flemmli97.runecraftory.common.blocks.ForgeBlock;
import io.github.flemmli97.runecraftory.common.blocks.FruitTreeLeafBlock;
import io.github.flemmli97.runecraftory.common.blocks.GiantCropBlock;
import io.github.flemmli97.runecraftory.common.blocks.HerbBlock;
import io.github.flemmli97.runecraftory.common.blocks.MineralBlock;
import io.github.flemmli97.runecraftory.common.blocks.MonsterBarnBlock;
import io.github.flemmli97.runecraftory.common.blocks.QuestboardBlock;
import io.github.flemmli97.runecraftory.common.blocks.ShippingBinBlock;
import io.github.flemmli97.runecraftory.common.blocks.SingleTimeSpawnerBlock;
import io.github.flemmli97.runecraftory.common.blocks.TreeBaseBlock;
import io.github.flemmli97.runecraftory.common.blocks.TreeLogBlock;
import io.github.flemmli97.runecraftory.common.blocks.TreeRootBlock;
import io.github.flemmli97.runecraftory.common.blocks.TreeSaplingBlock;
import io.github.flemmli97.runecraftory.common.blocks.entity.AccessoryBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.entity.BossSpawnerBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.entity.BrokenMineralBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.entity.ChemistryBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.entity.CookingBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.entity.ForgingBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.entity.MonsterBarnBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.entity.SingleTimeSpawner;
import io.github.flemmli97.runecraftory.common.blocks.entity.TreeBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.entity.TreeLogBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.util.MineralBlockTier;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.TenshiLibCrossPlat;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.LiquidBlock;
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

public class RuneCraftoryBlocks {

    public static final LoaderRegister<Block> BLOCKS = LoaderRegistryAccess.INSTANCE.of(Registries.BLOCK, RuneCraftory.MODID);
    public static final LoaderRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = LoaderRegistryAccess.INSTANCE.of(Registries.BLOCK_ENTITY_TYPE, RuneCraftory.MODID);

    /// For datagen only
    public static final Map<RegistryEntrySupplier<Block, ?>, RunecraftoryTags.Biomes.BiomeGenerationTags> GENERATION_TAGS = new HashMap<>();

    public static final List<RegistryEntrySupplier<Block, ?>> CROPS = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Block, ?>> FLOWERS = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Block, ?>> HERBS = new ArrayList<>();
    public static final Map<RegistryEntrySupplier<Block, ?>, RegistryEntrySupplier<Block, ?>> GIANT_CROP_MAP = new HashMap<>();
    public static final EnumMap<MineralBlockTier, RegistryEntrySupplier<Block, ?>> MINERAL_MAP = new EnumMap<>(MineralBlockTier.class);
    public static final EnumMap<MineralBlockTier, RegistryEntrySupplier<Block, ?>> BROKEN_MINERAL_MAP = new EnumMap<>(MineralBlockTier.class);

    public static final RegistryEntrySupplier<Block, ForgeBlock> FORGE = BLOCKS.register("forge", () -> new ForgeBlock(BlockBehaviour.Properties.of().sound(SoundType.METAL).noOcclusion().strength(3, 100)));
    public static final RegistryEntrySupplier<Block, AccessoryBlock> ACCESSORY_WORKBENCH = BLOCKS.register("accessory_workbench", () -> new AccessoryBlock(BlockBehaviour.Properties.of().noOcclusion().strength(3, 100)));
    public static final RegistryEntrySupplier<Block, ChemistryBlock> CHEMISTRY_SET = BLOCKS.register("chemistry_set", () -> new ChemistryBlock(BlockBehaviour.Properties.of().noOcclusion().strength(3, 100)));
    public static final RegistryEntrySupplier<Block, CookingBlock> COOKING_TABLE = BLOCKS.register("cooking_table", () -> new CookingBlock(BlockBehaviour.Properties.of().noOcclusion().strength(3, 100)));

    public static final RegistryEntrySupplier<Block, MineralBlock> MINERAL_IRON = mineral(MineralBlockTier.IRON, List.of(BiomeTags.IS_OVERWORLD), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, MineralBlock> MINERAL_TIN = mineral(MineralBlockTier.TIN, List.of(BiomeTags.IS_OVERWORLD), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, MineralBlock> MINERAL_SILVER = mineral(MineralBlockTier.SILVER, List.of(BiomeTags.IS_OVERWORLD), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, MineralBlock> MINERAL_GOLD = mineral(MineralBlockTier.GOLD, List.of(BiomeTags.IS_OVERWORLD), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, MineralBlock> MINERAL_PLATINUM = mineral(MineralBlockTier.PLATINUM, List.of(BiomeTags.IS_OVERWORLD), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, MineralBlock> MINERAL_ORICHALCUM = mineral(MineralBlockTier.ORICHALCUM, List.of(BiomeTags.IS_OVERWORLD), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, MineralBlock> MINERAL_DIAMOND = mineral(MineralBlockTier.DIAMOND, List.of(BiomeTags.IS_OVERWORLD), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, MineralBlock> MINERAL_DRAGONIC = mineral(MineralBlockTier.DRAGONIC, List.of(BiomeTags.IS_END), List.of());
    public static final RegistryEntrySupplier<Block, MineralBlock> MINERAL_AQUAMARINE = mineral(MineralBlockTier.AQUAMARINE, List.of(RunecraftoryTags.Biomes.IS_AQUATIC, BiomeTags.IS_BEACH, RunecraftoryTags.Biomes.IS_WET), List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END));
    public static final RegistryEntrySupplier<Block, MineralBlock> MINERAL_AMETHYST = mineral(MineralBlockTier.AMETHYST, List.of(BiomeTags.IS_FOREST, BiomeTags.IS_MOUNTAIN, RunecraftoryTags.Biomes.IS_DEAD), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, MineralBlock> MINERAL_RUBY = mineral(MineralBlockTier.RUBY, List.of(RunecraftoryTags.Biomes.IS_HOT, BiomeTags.IS_NETHER), List.of());
    public static final RegistryEntrySupplier<Block, MineralBlock> MINERAL_EMERALD = mineral(MineralBlockTier.EMERALD, List.of(RunecraftoryTags.Biomes.IS_PLAINS, RunecraftoryTags.Biomes.IS_WASTELAND, RunecraftoryTags.Biomes.IS_SPARSE_VEGETATION_OVERWORLD, BiomeTags.IS_HILL), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, MineralBlock> MINERAL_SAPPHIRE = mineral(MineralBlockTier.SAPPHIRE, List.of(RunecraftoryTags.Biomes.IS_MAGICAL, RunecraftoryTags.Biomes.IS_SNOWY), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));

    public static final RegistryEntrySupplier<Block, BrokenMineralBlock> BROKEN_MINERAL_IRON = brokenMineral(MineralBlockTier.IRON);
    public static final RegistryEntrySupplier<Block, BrokenMineralBlock> BROKEN_MINERAL_TIN = brokenMineral(MineralBlockTier.TIN);
    public static final RegistryEntrySupplier<Block, BrokenMineralBlock> BROKEN_MINERAL_SILVER = brokenMineral(MineralBlockTier.SILVER);
    public static final RegistryEntrySupplier<Block, BrokenMineralBlock> BROKEN_MINERAL_GOLD = brokenMineral(MineralBlockTier.GOLD);
    public static final RegistryEntrySupplier<Block, BrokenMineralBlock> BROKEN_MINERAL_PLATINUM = brokenMineral(MineralBlockTier.PLATINUM);
    public static final RegistryEntrySupplier<Block, BrokenMineralBlock> BROKEN_MINERAL_ORICHALCUM = brokenMineral(MineralBlockTier.ORICHALCUM);
    public static final RegistryEntrySupplier<Block, BrokenMineralBlock> BROKEN_MINERAL_DIAMOND = brokenMineral(MineralBlockTier.DIAMOND);
    public static final RegistryEntrySupplier<Block, BrokenMineralBlock> BROKEN_MINERAL_DRAGONIC = brokenMineral(MineralBlockTier.DRAGONIC);
    public static final RegistryEntrySupplier<Block, BrokenMineralBlock> BROKEN_MINERAL_AQUAMARINE = brokenMineral(MineralBlockTier.AQUAMARINE);
    public static final RegistryEntrySupplier<Block, BrokenMineralBlock> BROKEN_MINERAL_AMETHYST = brokenMineral(MineralBlockTier.AMETHYST);
    public static final RegistryEntrySupplier<Block, BrokenMineralBlock> BROKEN_MINERAL_RUBY = brokenMineral(MineralBlockTier.RUBY);
    public static final RegistryEntrySupplier<Block, BrokenMineralBlock> BROKEN_MINERAL_EMERALD = brokenMineral(MineralBlockTier.EMERALD);
    public static final RegistryEntrySupplier<Block, BrokenMineralBlock> BROKEN_MINERAL_SAPPHIRE = brokenMineral(MineralBlockTier.SAPPHIRE);

    public static final RegistryEntrySupplier<Block, BossSpawnerBlock> BOSS_SPAWNER = BLOCKS.register("boss_spawner", () -> new BossSpawnerBlock(BlockBehaviour.Properties.of().sound(SoundType.METAL).instrument(NoteBlockInstrument.BASEDRUM).strength(60, 9999).noOcclusion()));
    public static final RegistryEntrySupplier<Block, ShippingBinBlock> SHIPPING = BLOCKS.register("shipping_bin", () -> new ShippingBinBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD).instrument(NoteBlockInstrument.XYLOPHONE).strength(3, 10)));
    public static final RegistryEntrySupplier<Block, SingleTimeSpawnerBlock> SINGLE_SPAWN_BLOCK = BLOCKS.register("one_time_spawner", () -> new SingleTimeSpawnerBlock(BlockBehaviour.Properties.of().sound(SoundType.METAL).instrument(NoteBlockInstrument.BASEDRUM).strength(60, 9999).noOcclusion()));
    public static final RegistryEntrySupplier<Block, CashRegisterBlock> CASH_REGISTER = BLOCKS.register("cash_register", () -> new CashRegisterBlock(BlockBehaviour.Properties.of().sound(SoundType.TUFF).requiresCorrectToolForDrops().strength(3, 5)));
    public static final RegistryEntrySupplier<Block, MonsterBarnBlock> MONSTER_BARN = BLOCKS.register("monster_barn", () -> new MonsterBarnBlock(BlockBehaviour.Properties.of().sound(SoundType.GRASS).noOcclusion().noCollission().strength(1, 10000)));
    public static final RegistryEntrySupplier<Block, QuestboardBlock> QUEST_BOARD = BLOCKS.register("quest_board", () -> new QuestboardBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD).noOcclusion().strength(2, 5)));

    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> TURNIP = crop("turnip", RuneCraftoryItems.TURNIP::getKey, RuneCraftoryItems.TURNIP_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> TURNIP_PINK = crop("turnip_pink", RuneCraftoryItems.TURNIP_PINK::getKey, RuneCraftoryItems.TURNIP_PINK_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> CABBAGE = crop("cabbage", RuneCraftoryItems.CABBAGE::getKey, RuneCraftoryItems.CABBAGE_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> PINK_MELON = crop("pink_melon", RuneCraftoryItems.PINK_MELON::getKey, RuneCraftoryItems.PINK_MELON_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> PINEAPPLE = crop("pineapple", RuneCraftoryItems.PINEAPPLE::getKey, RuneCraftoryItems.PINEAPPLE_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> STRAWBERRY = crop("strawberry", RuneCraftoryItems.STRAWBERRY::getKey, RuneCraftoryItems.STRAWBERRY_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> GOLDEN_TURNIP = crop("golden_turnip", RuneCraftoryItems.GOLDEN_TURNIP::getKey, RuneCraftoryItems.GOLD_TURNIP_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> GOLDEN_POTATO = crop("golden_potato", RuneCraftoryItems.GOLDEN_POTATO::getKey, RuneCraftoryItems.GOLD_POTATO_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> GOLDEN_PUMPKIN = crop("golden_pumpkin", RuneCraftoryItems.GOLDEN_PUMPKIN::getKey, RuneCraftoryItems.GOLD_PUMPKIN_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> GOLDEN_CABBAGE = crop("golden_cabbage", RuneCraftoryItems.GOLDEN_CABBAGE::getKey, RuneCraftoryItems.GOLD_CABBAGE_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> HOT_HOT_FRUIT = crop("hot_hot_fruit", RuneCraftoryItems.HOT_HOT_FRUIT::getKey, RuneCraftoryItems.HOT_HOT_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> BOK_CHOY = crop("bok_choy", RuneCraftoryItems.BOK_CHOY::getKey, RuneCraftoryItems.BOK_CHOY_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> LEEK = crop("leek", RuneCraftoryItems.LEEK::getKey, RuneCraftoryItems.LEEK_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> RADISH = crop("radish", RuneCraftoryItems.RADISH::getKey, RuneCraftoryItems.RADISH_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> SPINACH = crop("spinach", RuneCraftoryItems.SPINACH::getKey, RuneCraftoryItems.SPINACH_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> GREEN_PEPPER = crop("green_pepper", RuneCraftoryItems.GREEN_PEPPER::getKey, RuneCraftoryItems.GREEN_PEPPER_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> YAM = crop("yam", RuneCraftoryItems.YAM::getKey, RuneCraftoryItems.YAM_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> EGGPLANT = crop("eggplant", RuneCraftoryItems.EGGPLANT::getKey, RuneCraftoryItems.EGGPLANT_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> TOMATO = crop("tomato", RuneCraftoryItems.TOMATO::getKey, RuneCraftoryItems.TOMATO_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> CORN = crop("corn", RuneCraftoryItems.CORN::getKey, RuneCraftoryItems.CORN_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> CUCUMBER = crop("cucumber", RuneCraftoryItems.CUCUMBER::getKey, RuneCraftoryItems.CUCUMBER_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> PUMPKIN = crop("pumpkin", RuneCraftoryItems.PUMPKIN::getKey, RuneCraftoryItems.PUMPKIN_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> ONION = crop("onion", RuneCraftoryItems.ONION::getKey, RuneCraftoryItems.ONION_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> TURNIP_GIANT = giantCrop("tyrant_turnip", RuneCraftoryItems.TURNIP_GIANT::getKey, RuneCraftoryItems.TURNIP_SEEDS::getKey, TURNIP);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> TURNIP_PINK_GIANT = giantCrop("colossal_pink", RuneCraftoryItems.TURNIP_PINK_GIANT::getKey, RuneCraftoryItems.TURNIP_PINK_SEEDS::getKey, TURNIP_PINK);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> CABBAGE_GIANT = giantCrop("king_cabbage", RuneCraftoryItems.CABBAGE_GIANT::getKey, RuneCraftoryItems.CABBAGE_SEEDS::getKey, CABBAGE);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> PINK_MELON_GIANT = giantCrop("conqueror_melon", RuneCraftoryItems.PINK_MELON_GIANT::getKey, RuneCraftoryItems.PINK_MELON_SEEDS::getKey, PINK_MELON);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> PINEAPPLE_GIANT = giantCrop("king_pineapple", RuneCraftoryItems.PINEAPPLE_GIANT::getKey, RuneCraftoryItems.PINEAPPLE_SEEDS::getKey, PINEAPPLE);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> STRAWBERRY_GIANT = giantCrop("sultan_strawberry", RuneCraftoryItems.STRAWBERRY_GIANT::getKey, RuneCraftoryItems.STRAWBERRY_SEEDS::getKey, STRAWBERRY);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> GOLDEN_TURNIP_GIANT = giantCrop("golden_tyrant_turnip", RuneCraftoryItems.GOLDEN_TURNIP_GIANT::getKey, RuneCraftoryItems.GOLD_TURNIP_SEEDS::getKey, GOLDEN_TURNIP);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> GOLDEN_POTATO_GIANT = giantCrop("gold_prince_potato", RuneCraftoryItems.GOLDEN_POTATO_GIANT::getKey, RuneCraftoryItems.GOLD_POTATO_SEEDS::getKey, GOLDEN_POTATO);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> GOLDEN_PUMPKIN_GIANT = giantCrop("golden_doom_pumpkin", RuneCraftoryItems.GOLDEN_PUMPKIN_GIANT::getKey, RuneCraftoryItems.GOLD_PUMPKIN_SEEDS::getKey, PUMPKIN);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> GOLDEN_CABBAGE_GIANT = giantCrop("golden_king_cabbage", RuneCraftoryItems.GOLDEN_CABBAGE_GIANT::getKey, RuneCraftoryItems.GOLD_CABBAGE_SEEDS::getKey, CABBAGE_GIANT);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> HOT_HOT_FRUIT_GIANT = giantCrop("giant_hot_hot_fruit", RuneCraftoryItems.HOT_HOT_FRUIT_GIANT::getKey, RuneCraftoryItems.HOT_HOT_SEEDS::getKey, HOT_HOT_FRUIT);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> BOK_CHOY_GIANT = giantCrop("boss_bok_choy", RuneCraftoryItems.BOK_CHOY_GIANT::getKey, RuneCraftoryItems.BOK_CHOY_SEEDS::getKey, BOK_CHOY);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> LEEK_GIANT = giantCrop("legendary_leek", RuneCraftoryItems.LEEK_GIANT::getKey, RuneCraftoryItems.LEEK_SEEDS::getKey, LEEK);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> RADISH_GIANT = giantCrop("noble_radish", RuneCraftoryItems.RADISH_GIANT::getKey, RuneCraftoryItems.RADISH_SEEDS::getKey, RADISH);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> SPINACH_GIANT = giantCrop("sovereign_spinach", RuneCraftoryItems.SPINACH_GIANT::getKey, RuneCraftoryItems.SPINACH_SEEDS::getKey, SPINACH);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> GREEN_PEPPER_GIANT = giantCrop("green_pepper_rex", RuneCraftoryItems.GREEN_PEPPER_GIANT::getKey, RuneCraftoryItems.GREEN_PEPPER_SEEDS::getKey, GREEN_PEPPER);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> YAM_GIANT = giantCrop("lorldy_yam", RuneCraftoryItems.YAM_GIANT::getKey, RuneCraftoryItems.YAM_SEEDS::getKey, YAM);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> EGGPLANT_GIANT = giantCrop("emperor_eggplant", RuneCraftoryItems.EGGPLANT_GIANT::getKey, RuneCraftoryItems.EGGPLANT_SEEDS::getKey, EGGPLANT);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> TOMATO_GIANT = giantCrop("titan_tomato", RuneCraftoryItems.TOMATO_GIANT::getKey, RuneCraftoryItems.TOMATO_SEEDS::getKey, TOMATO);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> CORN_GIANT = giantCrop("gigant_corn", RuneCraftoryItems.CORN_GIANT::getKey, RuneCraftoryItems.CORN_SEEDS::getKey, CORN);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> CUCUMBER_GIANT = giantCrop("kaiser_cucumber", RuneCraftoryItems.CUCUMBER_GIANT::getKey, RuneCraftoryItems.CUCUMBER_SEEDS::getKey, CUCUMBER);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> PUMPKIN_GIANT = giantCrop("doom_pumpkin", RuneCraftoryItems.PUMPKIN_GIANT::getKey, RuneCraftoryItems.PUMPKIN_SEEDS::getKey, PUMPKIN);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> ONION_GIANT = giantCrop("ultra_onion", RuneCraftoryItems.ONION_GIANT::getKey, RuneCraftoryItems.ONION_SEEDS::getKey, ONION);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> FODDER = crop("fodder", RuneCraftoryItems.FODDER::getKey, RuneCraftoryItems.FODDER_SEEDS::getKey);

    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> POTATO_GIANT = crop("potato", () -> ofVanilla(Items.POTATO), () -> ofVanilla(Items.POTATO));
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> CARROT_GIANT = crop("carrot", () -> ofVanilla(Items.CARROT), () -> ofVanilla(Items.CARROT));

    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> TOYHERB = flower("toyherb", RuneCraftoryItems.TOYHERB::getKey, RuneCraftoryItems.TOYHERB_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> MOONDROP_FLOWER = flower("moondrop_flower", RuneCraftoryItems.MOONDROP_FLOWER::getKey, RuneCraftoryItems.MOONDROP_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> PINK_CAT = flower("pink_cat", RuneCraftoryItems.PINK_CAT::getKey, RuneCraftoryItems.PINK_CAT_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> CHARM_BLUE = flower("charm_blue", RuneCraftoryItems.CHARM_BLUE::getKey, RuneCraftoryItems.CHARM_BLUE_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> LAMP_GRASS = flower("lamp_grass", RuneCraftoryItems.LAMP_GRASS::getKey, RuneCraftoryItems.LAMP_GRASS_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> CHERRY_GRASS = flower("cherry_grass", RuneCraftoryItems.CHERRY_GRASS::getKey, RuneCraftoryItems.CHERRY_GRASS_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> POM_POM_GRASS = flower("pom_pom_grass", RuneCraftoryItems.POM_POM_GRASS::getKey, RuneCraftoryItems.POM_POM_GRASS_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> AUTUMN_GRASS = flower("autumn_grass", RuneCraftoryItems.AUTUMN_GRASS::getKey, RuneCraftoryItems.AUTUMN_GRASS_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> NOEL_GRASS = flower("noel_grass", RuneCraftoryItems.NOEL_GRASS::getKey, RuneCraftoryItems.NOEL_GRASS_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> FIREFLOWER = flower("fireflower", RuneCraftoryItems.FIREFLOWER::getKey, RuneCraftoryItems.FIREFLOWER_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> FOUR_LEAF_CLOVER = flower("four_leaf_clover", RuneCraftoryItems.FOUR_LEAF_CLOVER::getKey, RuneCraftoryItems.FOUR_LEAF_CLOVER_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> IRONLEAF = flower("ironleaf", RuneCraftoryItems.IRONLEAF::getKey, RuneCraftoryItems.IRONLEAF_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> WHITE_CRYSTAL = flower("white_crystal", RuneCraftoryItems.WHITE_CRYSTAL::getKey, RuneCraftoryItems.WHITE_CRYSTAL_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> RED_CRYSTAL = flower("red_crystal", RuneCraftoryItems.RED_CRYSTAL::getKey, RuneCraftoryItems.RED_CRYSTAL_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> GREEN_CRYSTAL = flower("green_crystal", RuneCraftoryItems.GREEN_CRYSTAL::getKey, RuneCraftoryItems.GREEN_CRYSTAL_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> BLUE_CRYSTAL = flower("blue_crystal", RuneCraftoryItems.BLUE_CRYSTAL::getKey, RuneCraftoryItems.BLUE_CRYSTAL_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> EMERY_FLOWER = flower("emery_flower", RuneCraftoryItems.EMERY_FLOWER::getKey, RuneCraftoryItems.EMERY_FLOWER_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> TOYHERB_GIANT = giantFlower("ultra_toyherb", RuneCraftoryItems.TOYHERB_GIANT::getKey, RuneCraftoryItems.TOYHERB_SEEDS::getKey, TOYHERB);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> MOONDROP_FLOWER_GIANT = giantFlower("ultra_moondrop_flower", RuneCraftoryItems.MOONDROP_FLOWER_GIANT::getKey, RuneCraftoryItems.MOONDROP_SEEDS::getKey, MOONDROP_FLOWER);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> PINK_CAT_GIANT = giantFlower("king_pink_cat", RuneCraftoryItems.PINK_CAT_GIANT::getKey, RuneCraftoryItems.PINK_CAT_SEEDS::getKey, PINK_CAT);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> CHARM_BLUE_GIANT = giantFlower("great_charm_blue", RuneCraftoryItems.CHARM_BLUE_GIANT::getKey, RuneCraftoryItems.CHARM_BLUE_SEEDS::getKey, CHARM_BLUE);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> LAMP_GRASS_GIANT = giantFlower("kaiser_lamp_grass", RuneCraftoryItems.LAMP_GRASS_GIANT::getKey, RuneCraftoryItems.LAMP_GRASS_SEEDS::getKey, LAMP_GRASS);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> CHERRY_GRASS_GIANT = giantFlower("king_cherry_grass", RuneCraftoryItems.CHERRY_GRASS_GIANT::getKey, RuneCraftoryItems.CHERRY_GRASS_SEEDS::getKey, CHERRY_GRASS);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> POM_POM_GRASS_GIANT = giantFlower("king_pom_pom_grass", RuneCraftoryItems.POM_POM_GRASS_GIANT::getKey, RuneCraftoryItems.POM_POM_GRASS_SEEDS::getKey, POM_POM_GRASS);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> AUTUMN_GRASS_GIANT = giantFlower("big_autumn_grass", RuneCraftoryItems.AUTUMN_GRASS_GIANT::getKey, RuneCraftoryItems.AUTUMN_GRASS_SEEDS::getKey, AUTUMN_GRASS);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> NOEL_GRASS_GIANT = giantFlower("large_noel_grass", RuneCraftoryItems.NOEL_GRASS_GIANT::getKey, RuneCraftoryItems.NOEL_GRASS_SEEDS::getKey, NOEL_GRASS);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> FIREFLOWER_GIANT = giantFlower("big_fireflower", RuneCraftoryItems.FIREFLOWER_GIANT::getKey, RuneCraftoryItems.FIREFLOWER_SEEDS::getKey, FIREFLOWER);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> FOUR_LEAF_CLOVER_GIANT = giantFlower("great_four_leaf_clover", RuneCraftoryItems.FOUR_LEAF_CLOVER_GIANT::getKey, RuneCraftoryItems.FOUR_LEAF_CLOVER_SEEDS::getKey, FOUR_LEAF_CLOVER);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> IRONLEAF_GIANT = giantFlower("super_ironleaf", RuneCraftoryItems.IRONLEAF_GIANT::getKey, RuneCraftoryItems.IRONLEAF_SEEDS::getKey, IRONLEAF);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> WHITE_CRYSTAL_GIANT = giantFlower("big_white_crystal", RuneCraftoryItems.WHITE_CRYSTAL_GIANT::getKey, RuneCraftoryItems.WHITE_CRYSTAL_SEEDS::getKey, WHITE_CRYSTAL);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> RED_CRYSTAL_GIANT = giantFlower("big_red_crystal", RuneCraftoryItems.RED_CRYSTAL_GIANT::getKey, RuneCraftoryItems.RED_CRYSTAL_SEEDS::getKey, RED_CRYSTAL);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> GREEN_CRYSTAL_GIANT = giantFlower("big_green_crystal", RuneCraftoryItems.GREEN_CRYSTAL_GIANT::getKey, RuneCraftoryItems.GREEN_CRYSTAL_SEEDS::getKey, GREEN_CRYSTAL);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> BLUE_CRYSTAL_GIANT = giantFlower("big_blue_crystal", RuneCraftoryItems.BLUE_CRYSTAL_GIANT::getKey, RuneCraftoryItems.BLUE_CRYSTAL_SEEDS::getKey, BLUE_CRYSTAL);
    public static final RegistryEntrySupplier<Block, GiantCropBlock> EMERY_FLOWER_GIANT = giantFlower("great_emery_flower", RuneCraftoryItems.EMERY_FLOWER_GIANT::getKey, RuneCraftoryItems.EMERY_FLOWER_SEEDS::getKey, EMERY_FLOWER);

    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> SHIELD_CROP = flower("shield_flower", RuneCraftoryItems.PLANT_SHIELD::getKey, RuneCraftoryItems.SHIELD_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> SWORD_CROP = flower("sword_flower", RuneCraftoryItems.PLANT_SWORD::getKey, RuneCraftoryItems.SWORD_SEEDS::getKey);

    public static final RegistryEntrySupplier<Block, ExtendedCropBlock> DUNGEON = flower("dungeon_flower", () -> ofVanilla(Items.STONE), RuneCraftoryItems.DUNGEON_SEEDS::getKey);

    public static final RegistryEntrySupplier<Block, HerbBlock> WEEDS = herb("weeds", List.of(RunecraftoryTags.Biomes.VANILLA_DIMENSIONS), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, HerbBlock> WITHERED_GRASS = herb("withered_grass", List.of(RunecraftoryTags.Biomes.VANILLA_DIMENSIONS), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST), HerbBlock.GroundTypes.SANDY);
    public static final RegistryEntrySupplier<Block, HerbBlock> WHITE_GRASS = herb("white_grass", List.of(RunecraftoryTags.Biomes.IS_SNOWY), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, HerbBlock> INDIGO_GRASS = herb("indigo_grass", List.of(RunecraftoryTags.Biomes.IS_WET, RunecraftoryTags.Biomes.IS_MAGICAL, RunecraftoryTags.Biomes.IS_LUSH), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, HerbBlock> PURPLE_GRASS = herb("purple_grass", List.of(RunecraftoryTags.Biomes.IS_WET, RunecraftoryTags.Biomes.IS_MAGICAL), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, HerbBlock> GREEN_GRASS = herb("green_grass", List.of(RunecraftoryTags.Biomes.GENERAL_HERBS), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, HerbBlock> BLUE_GRASS = herb("blue_grass", List.of(BiomeTags.IS_BEACH, RunecraftoryTags.Biomes.IS_MAGICAL, BiomeTags.IS_RIVER, RunecraftoryTags.Biomes.IS_SWAMP), List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END));
    public static final RegistryEntrySupplier<Block, HerbBlock> YELLOW_GRASS = herb("yellow_grass", List.of(RunecraftoryTags.Biomes.IS_DRY_OVERWORLD, RunecraftoryTags.Biomes.IS_SANDY, BiomeTags.IS_NETHER), List.of(RunecraftoryTags.Biomes.IS_AQUATIC, BiomeTags.IS_END), HerbBlock.GroundTypes.SANDY, HerbBlock.GroundTypes.NETHER);
    public static final RegistryEntrySupplier<Block, HerbBlock> RED_GRASS = herb("red_grass", List.of(BiomeTags.IS_NETHER), List.of(), HerbBlock.GroundTypes.NETHER);
    public static final RegistryEntrySupplier<Block, HerbBlock> ORANGE_GRASS = herb("orange_grass", List.of(BiomeTags.IS_NETHER, RunecraftoryTags.Biomes.IS_HOT, BiomeTags.IS_SAVANNA), List.of(RunecraftoryTags.Biomes.IS_AQUATIC, BiomeTags.IS_END), HerbBlock.GroundTypes.SANDY, HerbBlock.GroundTypes.NETHER);
    public static final RegistryEntrySupplier<Block, HerbBlock> BLACK_GRASS = herb("black_grass", List.of(BiomeTags.IS_END), List.of(), HerbBlock.GroundTypes.END);
    public static final RegistryEntrySupplier<Block, HerbBlock> ANTIDOTE_GRASS = herb("antidote_grass", List.of(RunecraftoryTags.Biomes.GENERAL_HERBS), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, HerbBlock> MEDICINAL_HERB = herb("medicinal_herb", List.of(RunecraftoryTags.Biomes.GENERAL_HERBS), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, HerbBlock> MUSHROOM = herb("mushroom", List.of(BiomeTags.IS_FOREST, RunecraftoryTags.Biomes.IS_MUSHROOM, RunecraftoryTags.Biomes.IS_MAGICAL), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, HerbBlock> MONARCH_MUSHROOM = herb("monarch_mushroom", List.of(BiomeTags.IS_FOREST, RunecraftoryTags.Biomes.IS_MUSHROOM, RunecraftoryTags.Biomes.IS_MAGICAL), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, HerbBlock> ELLI_LEAVES = herb("elli_leaves", List.of(BiomeTags.IS_END), List.of(), HerbBlock.GroundTypes.END);
    public static final RegistryEntrySupplier<Block, HerbBlock> BAMBOO_SPROUT = herb("bamboo_sprout", List.of(BiomeTags.IS_JUNGLE, RunecraftoryTags.Biomes.IS_LUSH, RunecraftoryTags.Biomes.IS_DENSE_VEGETATION_OVERWORLD), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));

    public static final RegistryEntrySupplier<Block, TreeRootBlock> TREE_SOIL = BLOCKS.register("tree_soil", () -> new TreeRootBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).sound(SoundType.GRAVEL).strength(-1, 99999)));
    public static final RegistryEntrySupplier<Block, TreeBaseBlock> APPLE_TREE = BLOCKS.register("apple_tree", () -> new TreeBaseBlock(logProps(), RuneCraftoryFeatures.APPLE_1, RuneCraftoryFeatures.APPLE_2, RuneCraftoryFeatures.APPLE_3, RuneCraftoryItems.APPLE_SAPLING.getKey()));
    public static final RegistryEntrySupplier<Block, TreeSaplingBlock> APPLE_SAPLING = BLOCKS.register("apple_sapling", () -> new TreeSaplingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING), APPLE_TREE));
    public static final RegistryEntrySupplier<Block, TreeLogBlock> APPLE_WOOD = BLOCKS.register("apple_wood", () -> new TreeLogBlock(logProps()));
    public static final RegistryEntrySupplier<Block, LeavesBlock> APPLE_LEAVES = BLOCKS.register("apple_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)));
    public static final RegistryEntrySupplier<Block, FruitTreeLeafBlock> APPLE = BLOCKS.register("apple_leaves_fruit", () -> new FruitTreeLeafBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES), ofVanilla(Items.APPLE)));
    public static final RegistryEntrySupplier<Block, TreeBaseBlock> ORANGE_TREE = BLOCKS.register("orange_tree", () -> new TreeBaseBlock(logProps(), RuneCraftoryFeatures.ORANGE_1, RuneCraftoryFeatures.ORANGE_2, RuneCraftoryFeatures.ORANGE_3, RuneCraftoryItems.ORANGE_SAPLING.getKey()));
    public static final RegistryEntrySupplier<Block, TreeSaplingBlock> ORANGE_SAPLING = BLOCKS.register("orange_sapling", () -> new TreeSaplingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING), ORANGE_TREE));
    public static final RegistryEntrySupplier<Block, TreeLogBlock> ORANGE_WOOD = BLOCKS.register("orange_wood", () -> new TreeLogBlock(logProps()));
    public static final RegistryEntrySupplier<Block, LeavesBlock> ORANGE_LEAVES = BLOCKS.register("orange_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)));
    public static final RegistryEntrySupplier<Block, FruitTreeLeafBlock> ORANGE = BLOCKS.register("orange_leaves_fruit", () -> new FruitTreeLeafBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES), RuneCraftoryItems.ORANGE.getKey()));
    public static final RegistryEntrySupplier<Block, TreeBaseBlock> GRAPE_TREE = BLOCKS.register("grape_tree", () -> new TreeBaseBlock(logProps(), RuneCraftoryFeatures.GRAPE_1, RuneCraftoryFeatures.GRAPE_2, RuneCraftoryFeatures.GRAPE_3, RuneCraftoryItems.GRAPE_SAPLING.getKey()));
    public static final RegistryEntrySupplier<Block, TreeSaplingBlock> GRAPE_SAPLING = BLOCKS.register("grape_sapling", () -> new TreeSaplingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING), GRAPE_TREE));
    public static final RegistryEntrySupplier<Block, TreeLogBlock> GRAPE_WOOD = BLOCKS.register("grape_wood", () -> new TreeLogBlock(logProps()));
    public static final RegistryEntrySupplier<Block, LeavesBlock> GRAPE_LEAVES = BLOCKS.register("grape_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)));
    public static final RegistryEntrySupplier<Block, FruitTreeLeafBlock> GRAPE = BLOCKS.register("grape_leaves_fruit", () -> new FruitTreeLeafBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES), RuneCraftoryItems.GRAPES.getKey()));

    public static final RegistryEntrySupplier<Block, LiquidBlock> HOT_SPRING_WATER = BLOCKS.register("hot_spring_water", () -> new LiquidBlock(RuneCraftoryFluids.FLOWING_HOT_SPRING_WATER.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));

    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<BrokenMineralBlockEntity>> BROKEN_MINERAL_TILE = brokenMineralTile("broken_mineral_tile", BROKEN_MINERAL_MAP.values());
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<AccessoryBlockEntity>> ACCESSORY_TILE = BLOCK_ENTITY_TYPES.register("accessory_tile", () -> BlockEntityType.Builder.of(AccessoryBlockEntity::new, ACCESSORY_WORKBENCH.get()).build(null));
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<ForgingBlockEntity>> FORGING_TILE = BLOCK_ENTITY_TYPES.register("forge_tile", () -> BlockEntityType.Builder.of(ForgingBlockEntity::new, FORGE.get()).build(null));
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<ChemistryBlockEntity>> CHEMISTRY_TILE = BLOCK_ENTITY_TYPES.register("chemistry_tile", () -> BlockEntityType.Builder.of(ChemistryBlockEntity::new, CHEMISTRY_SET.get()).build(null));
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<CookingBlockEntity>> COOKING_TILE = BLOCK_ENTITY_TYPES.register("cooking_tile", () -> BlockEntityType.Builder.of(CookingBlockEntity::new, COOKING_TABLE.get()).build(null));
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<BossSpawnerBlockEntity>> BOSS_SPAWNER_TILE = BLOCK_ENTITY_TYPES.register("spawner_tile", () -> BlockEntityType.Builder.of(BossSpawnerBlockEntity::new, BOSS_SPAWNER.get()).build(null));
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<SingleTimeSpawner>> SINGLE_SPAWNER_TILE = BLOCK_ENTITY_TYPES.register("single_spawner_tile", () -> BlockEntityType.Builder.of(SingleTimeSpawner::new, SINGLE_SPAWN_BLOCK.get()).build(null));
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<MonsterBarnBlockEntity>> MONSTER_BARN_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("monster_barn_block_entity", () -> BlockEntityType.Builder.of(MonsterBarnBlockEntity::new, MONSTER_BARN.get()).build(null));
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<TreeBlockEntity>> TREE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("tree", () -> BlockEntityType.Builder.of(TreeBlockEntity::new, APPLE_TREE.get(), ORANGE_TREE.get(), GRAPE_TREE.get()).build(null));
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<TreeLogBlockEntity>> TREE_LOG_ENTITY = BLOCK_ENTITY_TYPES.register("tree_log", () -> BlockEntityType.Builder.of(TreeLogBlockEntity::new, APPLE_WOOD.get(), ORANGE_WOOD.get(), GRAPE_WOOD.get()).build(null));

    public static RegistryEntrySupplier<Block, MineralBlock> mineral(MineralBlockTier name, List<TagKey<Biome>> whitelist, List<TagKey<Biome>> blacklist) {
        RegistryEntrySupplier<Block, MineralBlock> reg = BLOCKS.register("ore_" + name.getSerializedName(), () -> new MineralBlock(name, BlockBehaviour.Properties.of().lightLevel(s -> 1).strength(5, 10)
                .requiresCorrectToolForDrops()));
        MINERAL_MAP.put(name, reg);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            GENERATION_TAGS.put(reg, new RunecraftoryTags.Biomes.BiomeGenerationTags(whitelist, blacklist));
        }
        return reg;
    }

    public static RegistryEntrySupplier<Block, BrokenMineralBlock> brokenMineral(MineralBlockTier name) {
        RegistryEntrySupplier<Block, BrokenMineralBlock> reg = BLOCKS.register("ore_broken_" + name.getSerializedName(), () -> new BrokenMineralBlock(name, BlockBehaviour.Properties.of().strength(30, 15)));
        BROKEN_MINERAL_MAP.put(name, reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block, ExtendedCropBlock> crop(String name, Supplier<ResourceKey<Item>> crop, Supplier<ResourceKey<Item>> seed) {
        RegistryEntrySupplier<Block, ExtendedCropBlock> reg = BLOCKS.register(name, () -> new ExtendedCropBlock(cropProps(), crop.get(), seed.get()));
        CROPS.add(reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block, GiantCropBlock> giantCrop(String name, Supplier<ResourceKey<Item>> giant, Supplier<ResourceKey<Item>> seed, RegistryEntrySupplier<Block, ?> crop) {
        RegistryEntrySupplier<Block, GiantCropBlock> reg = BLOCKS.register(name, () -> new GiantCropBlock(cropProps(), giant.get(), seed.get()));
        CROPS.add(reg);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen())
            GIANT_CROP_MAP.put(crop, reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block, ExtendedCropBlock> flower(String name, Supplier<ResourceKey<Item>> crop, Supplier<ResourceKey<Item>> seed) {
        RegistryEntrySupplier<Block, ExtendedCropBlock> reg = BLOCKS.register(name, () -> new ExtendedCropBlock(cropProps(), crop.get(), seed.get()));
        FLOWERS.add(reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block, GiantCropBlock> giantFlower(String name, Supplier<ResourceKey<Item>> giant, Supplier<ResourceKey<Item>> seed, RegistryEntrySupplier<Block, ?> flower) {
        RegistryEntrySupplier<Block, GiantCropBlock> reg = BLOCKS.register(name, () -> new GiantCropBlock(cropProps(), giant.get(), seed.get()));
        FLOWERS.add(reg);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen())
            GIANT_CROP_MAP.put(flower, reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block, HerbBlock> herb(String name, List<TagKey<Biome>> whitelist, List<TagKey<Biome>> blacklist, HerbBlock.GroundTypes... types) {
        RegistryEntrySupplier<Block, HerbBlock> reg = BLOCKS.register(name, () -> new HerbBlock(plantProps(), types));
        HERBS.add(reg);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            GENERATION_TAGS.put(reg, new RunecraftoryTags.Biomes.BiomeGenerationTags(whitelist, blacklist));
        }
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

    private static ResourceKey<Item> ofVanilla(Item item) {
        return ResourceKey.create(Registries.ITEM, BuiltInRegistries.ITEM.getKey(item));
    }
}
