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
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
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

    /// For datagen only
    public static final Map<RegistryEntrySupplier<Block, ?>, RunecraftoryTags.Biomes.BiomeGenerationTags> GENERATION_TAGS = new HashMap<>();

    public static final List<RegistryEntrySupplier<Block, ?>> CROPS = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Block, ?>> FLOWERS = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Block, ?>> HERBS = new ArrayList<>();
    public static final Map<RegistryEntrySupplier<Block, ?>, RegistryEntrySupplier<Block, ?>> GIANT_CROP_MAP = new HashMap<>();
    public static final EnumMap<EnumMineralTier, RegistryEntrySupplier<Block, ?>> MINERAL_MAP = new EnumMap<>(EnumMineralTier.class);
    public static final EnumMap<EnumMineralTier, RegistryEntrySupplier<Block, ?>> BROKEN_MINERAL_MAP = new EnumMap<>(EnumMineralTier.class);

    public static final RegistryEntrySupplier<Block, BlockForge> FORGE = BLOCKS.register("forge", () -> new BlockForge(BlockBehaviour.Properties.of().sound(SoundType.METAL).noOcclusion().strength(3, 100)));
    public static final RegistryEntrySupplier<Block, BlockAccessory> ACCESSORY_WORKBENCH = BLOCKS.register("accessory_workbench", () -> new BlockAccessory(BlockBehaviour.Properties.of().noOcclusion().strength(3, 100)));
    public static final RegistryEntrySupplier<Block, BlockChemistry> CHEMISTRY_SET = BLOCKS.register("chemistry_set", () -> new BlockChemistry(BlockBehaviour.Properties.of().noOcclusion().strength(3, 100)));
    public static final RegistryEntrySupplier<Block, BlockCooking> COOKING_TABLE = BLOCKS.register("cooking_table", () -> new BlockCooking(BlockBehaviour.Properties.of().noOcclusion().strength(3, 100)));

    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_IRON = mineral(EnumMineralTier.IRON, List.of(BiomeTags.IS_OVERWORLD), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_TIN = mineral(EnumMineralTier.TIN, List.of(BiomeTags.IS_OVERWORLD), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_SILVER = mineral(EnumMineralTier.SILVER, List.of(BiomeTags.IS_OVERWORLD), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_GOLD = mineral(EnumMineralTier.GOLD, List.of(BiomeTags.IS_OVERWORLD), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_PLATINUM = mineral(EnumMineralTier.PLATINUM, List.of(BiomeTags.IS_OVERWORLD), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_ORICHALCUM = mineral(EnumMineralTier.ORICHALCUM, List.of(BiomeTags.IS_OVERWORLD), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_DIAMOND = mineral(EnumMineralTier.DIAMOND, List.of(BiomeTags.IS_OVERWORLD), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_DRAGONIC = mineral(EnumMineralTier.DRAGONIC, List.of(BiomeTags.IS_END), List.of());
    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_AQUAMARINE = mineral(EnumMineralTier.AQUAMARINE, List.of(RunecraftoryTags.Biomes.IS_AQUATIC, BiomeTags.IS_BEACH, RunecraftoryTags.Biomes.IS_WET), List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END));
    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_AMETHYST = mineral(EnumMineralTier.AMETHYST, List.of(BiomeTags.IS_FOREST, BiomeTags.IS_MOUNTAIN, RunecraftoryTags.Biomes.IS_DEAD), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_RUBY = mineral(EnumMineralTier.RUBY, List.of(RunecraftoryTags.Biomes.IS_HOT, BiomeTags.IS_NETHER), List.of());
    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_EMERALD = mineral(EnumMineralTier.EMERALD, List.of(RunecraftoryTags.Biomes.IS_PLAINS, RunecraftoryTags.Biomes.IS_WASTELAND, RunecraftoryTags.Biomes.IS_SPARSE_VEGETATION_OVERWORLD, BiomeTags.IS_HILL), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, BlockMineral> MINERAL_SAPPHIRE = mineral(EnumMineralTier.SAPPHIRE, List.of(RunecraftoryTags.Biomes.IS_MAGICAL, RunecraftoryTags.Biomes.IS_SNOWY), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));

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

    public static final RegistryEntrySupplier<Block, BlockBossSpawner> BOSS_SPAWNER = BLOCKS.register("boss_spawner", () -> new BlockBossSpawner(BlockBehaviour.Properties.of().sound(SoundType.METAL).instrument(NoteBlockInstrument.BASEDRUM).strength(60, 9999).noOcclusion()));
    public static final RegistryEntrySupplier<Block, BlockShippingBin> SHIPPING = BLOCKS.register("shipping_bin", () -> new BlockShippingBin(BlockBehaviour.Properties.of().sound(SoundType.WOOD).instrument(NoteBlockInstrument.XYLOPHONE).strength(3, 10)));
    public static final RegistryEntrySupplier<Block, BlockSingleTimeSpawner> SINGLE_SPAWN_BLOCK = BLOCKS.register("one_time_spawner", () -> new BlockSingleTimeSpawner(BlockBehaviour.Properties.of().sound(SoundType.METAL).instrument(NoteBlockInstrument.BASEDRUM).strength(60, 9999).noOcclusion()));
    public static final RegistryEntrySupplier<Block, BlockCashRegister> CASH_REGISTER = BLOCKS.register("cash_register", () -> new BlockCashRegister(BlockBehaviour.Properties.of().sound(SoundType.TUFF).requiresCorrectToolForDrops().strength(3, 5)));
    public static final RegistryEntrySupplier<Block, BlockMonsterBarn> MONSTER_BARN = BLOCKS.register("monster_barn", () -> new BlockMonsterBarn(BlockBehaviour.Properties.of().sound(SoundType.GRASS).noOcclusion().noCollission().strength(1, 10000)));
    public static final RegistryEntrySupplier<Block, BlockQuestboard> QUEST_BOARD = BLOCKS.register("quest_board", () -> new BlockQuestboard(BlockBehaviour.Properties.of().sound(SoundType.WOOD).noOcclusion().strength(2, 5)));

    public static final RegistryEntrySupplier<Block, BlockCrop> TURNIP = crop("turnip", ModItems.TURNIP::getKey, ModItems.TURNIP_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> TURNIP_PINK = crop("turnip_pink", ModItems.TURNIP_PINK::getKey, ModItems.TURNIP_PINK_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> CABBAGE = crop("cabbage", ModItems.CABBAGE::getKey, ModItems.CABBAGE_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> PINK_MELON = crop("pink_melon", ModItems.PINK_MELON::getKey, ModItems.PINK_MELON_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> PINEAPPLE = crop("pineapple", ModItems.PINEAPPLE::getKey, ModItems.PINEAPPLE_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> STRAWBERRY = crop("strawberry", ModItems.STRAWBERRY::getKey, ModItems.STRAWBERRY_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> GOLDEN_TURNIP = crop("golden_turnip", ModItems.GOLDEN_TURNIP::getKey, ModItems.GOLD_TURNIP_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> GOLDEN_POTATO = crop("golden_potato", ModItems.GOLDEN_POTATO::getKey, ModItems.GOLD_POTATO_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> GOLDEN_PUMPKIN = crop("golden_pumpkin", ModItems.GOLDEN_PUMPKIN::getKey, ModItems.GOLD_PUMPKIN_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> GOLDEN_CABBAGE = crop("golden_cabbage", ModItems.GOLDEN_CABBAGE::getKey, ModItems.GOLD_CABBAGE_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> HOT_HOT_FRUIT = crop("hot_hot_fruit", ModItems.HOT_HOT_FRUIT::getKey, ModItems.HOT_HOT_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> BOK_CHOY = crop("bok_choy", ModItems.BOK_CHOY::getKey, ModItems.BOK_CHOY_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> LEEK = crop("leek", ModItems.LEEK::getKey, ModItems.LEEK_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> RADISH = crop("radish", ModItems.RADISH::getKey, ModItems.RADISH_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> SPINACH = crop("spinach", ModItems.SPINACH::getKey, ModItems.SPINACH_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> GREEN_PEPPER = crop("green_pepper", ModItems.GREEN_PEPPER::getKey, ModItems.GREEN_PEPPER_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> YAM = crop("yam", ModItems.YAM::getKey, ModItems.YAM_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> EGGPLANT = crop("eggplant", ModItems.EGGPLANT::getKey, ModItems.EGGPLANT_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> TOMATO = crop("tomato", ModItems.TOMATO::getKey, ModItems.TOMATO_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> CORN = crop("corn", ModItems.CORN::getKey, ModItems.CORN_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> CUCUMBER = crop("cucumber", ModItems.CUCUMBER::getKey, ModItems.CUCUMBER_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> PUMPKIN = crop("pumpkin", ModItems.PUMPKIN::getKey, ModItems.PUMPKIN_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> ONION = crop("onion", ModItems.ONION::getKey, ModItems.ONION_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> TURNIP_GIANT = giantCrop("tyrant_turnip", ModItems.TURNIP_GIANT::getKey, ModItems.TURNIP_SEEDS::getKey, TURNIP);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> TURNIP_PINK_GIANT = giantCrop("colossal_pink", ModItems.TURNIP_PINK_GIANT::getKey, ModItems.TURNIP_PINK_SEEDS::getKey, TURNIP_PINK);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> CABBAGE_GIANT = giantCrop("king_cabbage", ModItems.CABBAGE_GIANT::getKey, ModItems.CABBAGE_SEEDS::getKey, CABBAGE);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> PINK_MELON_GIANT = giantCrop("conqueror_melon", ModItems.PINK_MELON_GIANT::getKey, ModItems.PINK_MELON_SEEDS::getKey, PINK_MELON);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> PINEAPPLE_GIANT = giantCrop("king_pineapple", ModItems.PINEAPPLE_GIANT::getKey, ModItems.PINEAPPLE_SEEDS::getKey, PINEAPPLE);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> STRAWBERRY_GIANT = giantCrop("sultan_strawberry", ModItems.STRAWBERRY_GIANT::getKey, ModItems.STRAWBERRY_SEEDS::getKey, STRAWBERRY);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> GOLDEN_TURNIP_GIANT = giantCrop("golden_tyrant_turnip", ModItems.GOLDEN_TURNIP_GIANT::getKey, ModItems.GOLD_TURNIP_SEEDS::getKey, GOLDEN_TURNIP);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> GOLDEN_POTATO_GIANT = giantCrop("gold_prince_potato", ModItems.GOLDEN_POTATO_GIANT::getKey, ModItems.GOLD_POTATO_SEEDS::getKey, GOLDEN_POTATO);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> GOLDEN_PUMPKIN_GIANT = giantCrop("golden_doom_pumpkin", ModItems.GOLDEN_PUMPKIN_GIANT::getKey, ModItems.GOLD_PUMPKIN_SEEDS::getKey, PUMPKIN);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> GOLDEN_CABBAGE_GIANT = giantCrop("golden_king_cabbage", ModItems.GOLDEN_CABBAGE_GIANT::getKey, ModItems.GOLD_CABBAGE_SEEDS::getKey, CABBAGE_GIANT);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> HOT_HOT_FRUIT_GIANT = giantCrop("giant_hot_hot_fruit", ModItems.HOT_HOT_FRUIT_GIANT::getKey, ModItems.HOT_HOT_SEEDS::getKey, HOT_HOT_FRUIT);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> BOK_CHOY_GIANT = giantCrop("boss_bok_choy", ModItems.BOK_CHOY_GIANT::getKey, ModItems.BOK_CHOY_SEEDS::getKey, BOK_CHOY);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> LEEK_GIANT = giantCrop("legendary_leek", ModItems.LEEK_GIANT::getKey, ModItems.LEEK_SEEDS::getKey, LEEK);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> RADISH_GIANT = giantCrop("noble_radish", ModItems.RADISH_GIANT::getKey, ModItems.RADISH_SEEDS::getKey, RADISH);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> SPINACH_GIANT = giantCrop("sovereign_spinach", ModItems.SPINACH_GIANT::getKey, ModItems.SPINACH_SEEDS::getKey, SPINACH);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> GREEN_PEPPER_GIANT = giantCrop("green_pepper_rex", ModItems.GREEN_PEPPER_GIANT::getKey, ModItems.GREEN_PEPPER_SEEDS::getKey, GREEN_PEPPER);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> YAM_GIANT = giantCrop("lorldy_yam", ModItems.YAM_GIANT::getKey, ModItems.YAM_SEEDS::getKey, YAM);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> EGGPLANT_GIANT = giantCrop("emperor_eggplant", ModItems.EGGPLANT_GIANT::getKey, ModItems.EGGPLANT_SEEDS::getKey, EGGPLANT);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> TOMATO_GIANT = giantCrop("titan_tomato", ModItems.TOMATO_GIANT::getKey, ModItems.TOMATO_SEEDS::getKey, TOMATO);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> CORN_GIANT = giantCrop("gigant_corn", ModItems.CORN_GIANT::getKey, ModItems.CORN_SEEDS::getKey, CORN);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> CUCUMBER_GIANT = giantCrop("kaiser_cucumber", ModItems.CUCUMBER_GIANT::getKey, ModItems.CUCUMBER_SEEDS::getKey, CUCUMBER);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> PUMPKIN_GIANT = giantCrop("doom_pumpkin", ModItems.PUMPKIN_GIANT::getKey, ModItems.PUMPKIN_SEEDS::getKey, PUMPKIN);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> ONION_GIANT = giantCrop("ultra_onion", ModItems.ONION_GIANT::getKey, ModItems.ONION_SEEDS::getKey, ONION);
    public static final RegistryEntrySupplier<Block, BlockCrop> FODDER = crop("fodder", ModItems.FODDER::getKey, ModItems.FODDER_SEEDS::getKey);

    public static final RegistryEntrySupplier<Block, BlockCrop> POTATO_GIANT = crop("potato", () -> ofVanilla(Items.POTATO), () -> ofVanilla(Items.POTATO));
    public static final RegistryEntrySupplier<Block, BlockCrop> CARROT_GIANT = crop("carrot", () -> ofVanilla(Items.CARROT), () -> ofVanilla(Items.CARROT));

    public static final RegistryEntrySupplier<Block, BlockCrop> TOYHERB = flower("toyherb", ModItems.TOYHERB::getKey, ModItems.TOYHERB_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> MOONDROP_FLOWER = flower("moondrop_flower", ModItems.MOONDROP_FLOWER::getKey, ModItems.MOONDROP_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> PINK_CAT = flower("pink_cat", ModItems.PINK_CAT::getKey, ModItems.PINK_CAT_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> CHARM_BLUE = flower("charm_blue", ModItems.CHARM_BLUE::getKey, ModItems.CHARM_BLUE_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> LAMP_GRASS = flower("lamp_grass", ModItems.LAMP_GRASS::getKey, ModItems.LAMP_GRASS_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> CHERRY_GRASS = flower("cherry_grass", ModItems.CHERRY_GRASS::getKey, ModItems.CHERRY_GRASS_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> POM_POM_GRASS = flower("pom_pom_grass", ModItems.POM_POM_GRASS::getKey, ModItems.POM_POM_GRASS_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> AUTUMN_GRASS = flower("autumn_grass", ModItems.AUTUMN_GRASS::getKey, ModItems.AUTUMN_GRASS_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> NOEL_GRASS = flower("noel_grass", ModItems.NOEL_GRASS::getKey, ModItems.NOEL_GRASS_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> FIREFLOWER = flower("fireflower", ModItems.FIREFLOWER::getKey, ModItems.FIREFLOWER_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> FOUR_LEAF_CLOVER = flower("four_leaf_clover", ModItems.FOUR_LEAF_CLOVER::getKey, ModItems.FOUR_LEAF_CLOVER_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> IRONLEAF = flower("ironleaf", ModItems.IRONLEAF::getKey, ModItems.IRONLEAF_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> WHITE_CRYSTAL = flower("white_crystal", ModItems.WHITE_CRYSTAL::getKey, ModItems.WHITE_CRYSTAL_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> RED_CRYSTAL = flower("red_crystal", ModItems.RED_CRYSTAL::getKey, ModItems.RED_CRYSTAL_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> GREEN_CRYSTAL = flower("green_crystal", ModItems.GREEN_CRYSTAL::getKey, ModItems.GREEN_CRYSTAL_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> BLUE_CRYSTAL = flower("blue_crystal", ModItems.BLUE_CRYSTAL::getKey, ModItems.BLUE_CRYSTAL_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> EMERY_FLOWER = flower("emery_flower", ModItems.EMERY_FLOWER::getKey, ModItems.EMERY_FLOWER_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> TOYHERB_GIANT = giantFlower("ultra_toyherb", ModItems.TOYHERB_GIANT::getKey, ModItems.TOYHERB_SEEDS::getKey, TOYHERB);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> MOONDROP_FLOWER_GIANT = giantFlower("ultra_moondrop_flower", ModItems.MOONDROP_FLOWER_GIANT::getKey, ModItems.MOONDROP_SEEDS::getKey, MOONDROP_FLOWER);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> PINK_CAT_GIANT = giantFlower("king_pink_cat", ModItems.PINK_CAT_GIANT::getKey, ModItems.PINK_CAT_SEEDS::getKey, PINK_CAT);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> CHARM_BLUE_GIANT = giantFlower("great_charm_blue", ModItems.CHARM_BLUE_GIANT::getKey, ModItems.CHARM_BLUE_SEEDS::getKey, CHARM_BLUE);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> LAMP_GRASS_GIANT = giantFlower("kaiser_lamp_grass", ModItems.LAMP_GRASS_GIANT::getKey, ModItems.LAMP_GRASS_SEEDS::getKey, LAMP_GRASS);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> CHERRY_GRASS_GIANT = giantFlower("king_cherry_grass", ModItems.CHERRY_GRASS_GIANT::getKey, ModItems.CHERRY_GRASS_SEEDS::getKey, CHERRY_GRASS);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> POM_POM_GRASS_GIANT = giantFlower("king_pom_pom_grass", ModItems.POM_POM_GRASS_GIANT::getKey, ModItems.POM_POM_GRASS_SEEDS::getKey, POM_POM_GRASS);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> AUTUMN_GRASS_GIANT = giantFlower("big_autumn_grass", ModItems.AUTUMN_GRASS_GIANT::getKey, ModItems.AUTUMN_GRASS_SEEDS::getKey, AUTUMN_GRASS);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> NOEL_GRASS_GIANT = giantFlower("large_noel_grass", ModItems.NOEL_GRASS_GIANT::getKey, ModItems.NOEL_GRASS_SEEDS::getKey, NOEL_GRASS);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> FIREFLOWER_GIANT = giantFlower("big_fireflower", ModItems.FIREFLOWER_GIANT::getKey, ModItems.FIREFLOWER_SEEDS::getKey, FIREFLOWER);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> FOUR_LEAF_CLOVER_GIANT = giantFlower("great_four_leaf_clover", ModItems.FOUR_LEAF_CLOVER_GIANT::getKey, ModItems.FOUR_LEAF_CLOVER_SEEDS::getKey, FOUR_LEAF_CLOVER);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> IRONLEAF_GIANT = giantFlower("super_ironleaf", ModItems.IRONLEAF_GIANT::getKey, ModItems.IRONLEAF_SEEDS::getKey, IRONLEAF);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> WHITE_CRYSTAL_GIANT = giantFlower("big_white_crystal", ModItems.WHITE_CRYSTAL_GIANT::getKey, ModItems.WHITE_CRYSTAL_SEEDS::getKey, WHITE_CRYSTAL);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> RED_CRYSTAL_GIANT = giantFlower("big_red_crystal", ModItems.RED_CRYSTAL_GIANT::getKey, ModItems.RED_CRYSTAL_SEEDS::getKey, RED_CRYSTAL);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> GREEN_CRYSTAL_GIANT = giantFlower("big_green_crystal", ModItems.GREEN_CRYSTAL_GIANT::getKey, ModItems.GREEN_CRYSTAL_SEEDS::getKey, GREEN_CRYSTAL);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> BLUE_CRYSTAL_GIANT = giantFlower("big_blue_crystal", ModItems.BLUE_CRYSTAL_GIANT::getKey, ModItems.BLUE_CRYSTAL_SEEDS::getKey, BLUE_CRYSTAL);
    public static final RegistryEntrySupplier<Block, BlockGiantCrop> EMERY_FLOWER_GIANT = giantFlower("great_emery_flower", ModItems.EMERY_FLOWER_GIANT::getKey, ModItems.EMERY_FLOWER_SEEDS::getKey, EMERY_FLOWER);

    public static final RegistryEntrySupplier<Block, BlockCrop> SHIELD_CROP = flower("shield_flower", ModItems.PLANT_SHIELD::getKey, ModItems.SHIELD_SEEDS::getKey);
    public static final RegistryEntrySupplier<Block, BlockCrop> SWORD_CROP = flower("sword_flower", ModItems.PLANT_SWORD::getKey, ModItems.SWORD_SEEDS::getKey);

    public static final RegistryEntrySupplier<Block, BlockCrop> DUNGEON = flower("dungeon_flower", () -> ofVanilla(Items.STONE), ModItems.DUNGEON_SEEDS::getKey);

    public static final RegistryEntrySupplier<Block, BlockHerb> WEEDS = herb("weeds", List.of(RunecraftoryTags.Biomes.VANILLA_DIMENSIONS), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, BlockHerb> WITHERED_GRASS = herb("withered_grass", List.of(RunecraftoryTags.Biomes.VANILLA_DIMENSIONS), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST), BlockHerb.GroundTypes.SANDY);
    public static final RegistryEntrySupplier<Block, BlockHerb> WHITE_GRASS = herb("white_grass", List.of(RunecraftoryTags.Biomes.IS_SNOWY), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, BlockHerb> INDIGO_GRASS = herb("indigo_grass", List.of(RunecraftoryTags.Biomes.IS_WET, RunecraftoryTags.Biomes.IS_MAGICAL, RunecraftoryTags.Biomes.IS_LUSH), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, BlockHerb> PURPLE_GRASS = herb("purple_grass", List.of(RunecraftoryTags.Biomes.IS_WET, RunecraftoryTags.Biomes.IS_MAGICAL), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, BlockHerb> GREEN_GRASS = herb("green_grass", List.of(RunecraftoryTags.Biomes.GENERAL_HERBS), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, BlockHerb> BLUE_GRASS = herb("blue_grass", List.of(BiomeTags.IS_BEACH, RunecraftoryTags.Biomes.IS_MAGICAL, BiomeTags.IS_RIVER, RunecraftoryTags.Biomes.IS_SWAMP), List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END));
    public static final RegistryEntrySupplier<Block, BlockHerb> YELLOW_GRASS = herb("yellow_grass", List.of(RunecraftoryTags.Biomes.IS_DRY_OVERWORLD, RunecraftoryTags.Biomes.IS_SANDY, BiomeTags.IS_NETHER), List.of(RunecraftoryTags.Biomes.IS_AQUATIC, BiomeTags.IS_END), BlockHerb.GroundTypes.SANDY, BlockHerb.GroundTypes.NETHER);
    public static final RegistryEntrySupplier<Block, BlockHerb> RED_GRASS = herb("red_grass", List.of(BiomeTags.IS_NETHER), List.of(), BlockHerb.GroundTypes.NETHER);
    public static final RegistryEntrySupplier<Block, BlockHerb> ORANGE_GRASS = herb("orange_grass", List.of(BiomeTags.IS_NETHER, RunecraftoryTags.Biomes.IS_HOT, BiomeTags.IS_SAVANNA), List.of(RunecraftoryTags.Biomes.IS_AQUATIC, BiomeTags.IS_END), BlockHerb.GroundTypes.SANDY, BlockHerb.GroundTypes.NETHER);
    public static final RegistryEntrySupplier<Block, BlockHerb> BLACK_GRASS = herb("black_grass", List.of(BiomeTags.IS_END), List.of(), BlockHerb.GroundTypes.END);
    public static final RegistryEntrySupplier<Block, BlockHerb> ANTIDOTE_GRASS = herb("antidote_grass", List.of(RunecraftoryTags.Biomes.GENERAL_HERBS), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, BlockHerb> MEDICINAL_HERB = herb("medicinal_herb", List.of(RunecraftoryTags.Biomes.GENERAL_HERBS), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, BlockHerb> MUSHROOM = herb("mushroom", List.of(BiomeTags.IS_FOREST, RunecraftoryTags.Biomes.IS_MUSHROOM, RunecraftoryTags.Biomes.IS_MAGICAL), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, BlockHerb> MONARCH_MUSHROOM = herb("monarch_mushroom", List.of(BiomeTags.IS_FOREST, RunecraftoryTags.Biomes.IS_MUSHROOM, RunecraftoryTags.Biomes.IS_MAGICAL), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));
    public static final RegistryEntrySupplier<Block, BlockHerb> ELLI_LEAVES = herb("elli_leaves", List.of(BiomeTags.IS_END), List.of(), BlockHerb.GroundTypes.END);
    public static final RegistryEntrySupplier<Block, BlockHerb> BAMBOO_SPROUT = herb("bamboo_sprout", List.of(BiomeTags.IS_JUNGLE, RunecraftoryTags.Biomes.IS_LUSH, RunecraftoryTags.Biomes.IS_DENSE_VEGETATION_OVERWORLD), List.of(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST));

    public static final RegistryEntrySupplier<Block, BlockTreeRoot> TREE_SOIL = BLOCKS.register("tree_soil", () -> new BlockTreeRoot(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).sound(SoundType.GRAVEL).strength(-1, 99999)));
    public static final RegistryEntrySupplier<Block, BlockTreeBase> APPLE_TREE = BLOCKS.register("apple_tree", () -> new BlockTreeBase(logProps(), ModFeatures.APPLE_1, ModFeatures.APPLE_2, ModFeatures.APPLE_3, ModItems.APPLE_SAPLING.getKey()));
    public static final RegistryEntrySupplier<Block, BlockTreeSapling> APPLE_SAPLING = BLOCKS.register("apple_sapling", () -> new BlockTreeSapling(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING), APPLE_TREE));
    public static final RegistryEntrySupplier<Block, RotatedPillarBlock> APPLE_WOOD = BLOCKS.register("apple_wood", () -> new RotatedPillarBlock(logProps().strength(-1, 99999)));
    public static final RegistryEntrySupplier<Block, LeavesBlock> APPLE_LEAVES = BLOCKS.register("apple_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).strength(-1, 99999)));
    public static final RegistryEntrySupplier<Block, BlockFruitTreeLeaf> APPLE = BLOCKS.register("apple_leaves_fruit", () -> new BlockFruitTreeLeaf(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).strength(-1, 99999), ofVanilla(Items.APPLE)));
    public static final RegistryEntrySupplier<Block, BlockTreeBase> ORANGE_TREE = BLOCKS.register("orange_tree", () -> new BlockTreeBase(logProps(), ModFeatures.ORANGE_1, ModFeatures.ORANGE_2, ModFeatures.ORANGE_3, ModItems.ORANGE_SAPLING.getKey()));
    public static final RegistryEntrySupplier<Block, BlockTreeSapling> ORANGE_SAPLING = BLOCKS.register("orange_sapling", () -> new BlockTreeSapling(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING), ORANGE_TREE));
    public static final RegistryEntrySupplier<Block, RotatedPillarBlock> ORANGE_WOOD = BLOCKS.register("orange_wood", () -> new RotatedPillarBlock(logProps().strength(-1, 99999)));
    public static final RegistryEntrySupplier<Block, LeavesBlock> ORANGE_LEAVES = BLOCKS.register("orange_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).strength(-1, 99999)));
    public static final RegistryEntrySupplier<Block, BlockFruitTreeLeaf> ORANGE = BLOCKS.register("orange_leaves_fruit", () -> new BlockFruitTreeLeaf(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).strength(-1, 99999), ModItems.ORANGE.getKey()));
    public static final RegistryEntrySupplier<Block, BlockTreeBase> GRAPE_TREE = BLOCKS.register("grape_tree", () -> new BlockTreeBase(logProps(), ModFeatures.GRAPE_1, ModFeatures.GRAPE_2, ModFeatures.GRAPE_3, ModItems.GRAPE_SAPLING.getKey()));
    public static final RegistryEntrySupplier<Block, BlockTreeSapling> GRAPE_SAPLING = BLOCKS.register("grape_sapling", () -> new BlockTreeSapling(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING), GRAPE_TREE));
    public static final RegistryEntrySupplier<Block, RotatedPillarBlock> GRAPE_WOOD = BLOCKS.register("grape_wood", () -> new RotatedPillarBlock(logProps().sound(SoundType.WOOD).strength(-1, 99999)));
    public static final RegistryEntrySupplier<Block, LeavesBlock> GRAPE_LEAVES = BLOCKS.register("grape_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).strength(-1, 99999)));
    public static final RegistryEntrySupplier<Block, BlockFruitTreeLeaf> GRAPE = BLOCKS.register("grape_leaves_fruit", () -> new BlockFruitTreeLeaf(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).strength(-1, 99999), ModItems.GRAPES.getKey()));

    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<BrokenMineralBlockEntity>> BROKEN_MINERAL_TILE = brokenMineralTile("broken_mineral_tile", BROKEN_MINERAL_MAP.values());
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<AccessoryBlockEntity>> ACCESSORY_TILE = BLOCK_ENTITY_TYPES.register("accessory_tile", () -> BlockEntityType.Builder.of(AccessoryBlockEntity::new, ACCESSORY_WORKBENCH.get()).build(null));
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<ForgingBlockEntity>> FORGING_TILE = BLOCK_ENTITY_TYPES.register("forge_tile", () -> BlockEntityType.Builder.of(ForgingBlockEntity::new, FORGE.get()).build(null));
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<ChemistryBlockEntity>> CHEMISTRY_TILE = BLOCK_ENTITY_TYPES.register("chemistry_tile", () -> BlockEntityType.Builder.of(ChemistryBlockEntity::new, CHEMISTRY_SET.get()).build(null));
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<CookingBlockEntity>> COOKING_TILE = BLOCK_ENTITY_TYPES.register("cooking_tile", () -> BlockEntityType.Builder.of(CookingBlockEntity::new, COOKING_TABLE.get()).build(null));
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<BossSpawnerBlockEntity>> BOSS_SPAWNER_TILE = BLOCK_ENTITY_TYPES.register("spawner_tile", () -> BlockEntityType.Builder.of(BossSpawnerBlockEntity::new, BOSS_SPAWNER.get()).build(null));
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<SingleTimeSpawner>> SINGLE_SPAWNER_TILE = BLOCK_ENTITY_TYPES.register("single_spawner_tile", () -> BlockEntityType.Builder.of(SingleTimeSpawner::new, SINGLE_SPAWN_BLOCK.get()).build(null));
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<MonsterBarnBlockEntity>> MONSTER_BARN_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("monster_barn_block_entity", () -> BlockEntityType.Builder.of(MonsterBarnBlockEntity::new, MONSTER_BARN.get()).build(null));
    public static final RegistryEntrySupplier<BlockEntityType<?>, BlockEntityType<TreeBlockEntity>> TREE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("tree", () -> BlockEntityType.Builder.of(TreeBlockEntity::new, APPLE_TREE.get(), ORANGE_TREE.get(), GRAPE_TREE.get()).build(null));

    public static RegistryEntrySupplier<Block, BlockMineral> mineral(EnumMineralTier name, List<TagKey<Biome>> whitelist, List<TagKey<Biome>> blacklist) {
        RegistryEntrySupplier<Block, BlockMineral> reg = BLOCKS.register("ore_" + name.getSerializedName(), () -> new BlockMineral(name, BlockBehaviour.Properties.of().lightLevel(s -> 1).strength(5, 10)
                .requiresCorrectToolForDrops()));
        MINERAL_MAP.put(name, reg);
        if (Platform.INSTANCE.isDatagen()) {
            GENERATION_TAGS.put(reg, new RunecraftoryTags.Biomes.BiomeGenerationTags(whitelist, blacklist));
        }
        return reg;
    }

    public static RegistryEntrySupplier<Block, BlockBrokenMineral> brokenMineral(EnumMineralTier name) {
        RegistryEntrySupplier<Block, BlockBrokenMineral> reg = BLOCKS.register("ore_broken_" + name.getSerializedName(), () -> new BlockBrokenMineral(name, BlockBehaviour.Properties.of().strength(30, 15)));
        BROKEN_MINERAL_MAP.put(name, reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block, BlockCrop> crop(String name, Supplier<ResourceKey<Item>> crop, Supplier<ResourceKey<Item>> seed) {
        RegistryEntrySupplier<Block, BlockCrop> reg = BLOCKS.register(name, () -> new BlockCrop(cropProps(), crop.get(), seed.get()));
        CROPS.add(reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block, BlockGiantCrop> giantCrop(String name, Supplier<ResourceKey<Item>> giant, Supplier<ResourceKey<Item>> seed, RegistryEntrySupplier<Block, ?> crop) {
        RegistryEntrySupplier<Block, BlockGiantCrop> reg = BLOCKS.register(name, () -> new BlockGiantCrop(cropProps(), giant.get(), seed.get()));
        CROPS.add(reg);
        if (Platform.INSTANCE.isDatagen())
            GIANT_CROP_MAP.put(crop, reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block, BlockCrop> flower(String name, Supplier<ResourceKey<Item>> crop, Supplier<ResourceKey<Item>> seed) {
        RegistryEntrySupplier<Block, BlockCrop> reg = BLOCKS.register(name, () -> new BlockCrop(cropProps(), crop.get(), seed.get()));
        FLOWERS.add(reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block, BlockGiantCrop> giantFlower(String name, Supplier<ResourceKey<Item>> giant, Supplier<ResourceKey<Item>> seed, RegistryEntrySupplier<Block, ?> flower) {
        RegistryEntrySupplier<Block, BlockGiantCrop> reg = BLOCKS.register(name, () -> new BlockGiantCrop(cropProps(), giant.get(), seed.get()));
        FLOWERS.add(reg);
        if (Platform.INSTANCE.isDatagen())
            GIANT_CROP_MAP.put(flower, reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block, BlockHerb> herb(String name, List<TagKey<Biome>> whitelist, List<TagKey<Biome>> blacklist, BlockHerb.GroundTypes... types) {
        RegistryEntrySupplier<Block, BlockHerb> reg = BLOCKS.register(name, () -> new BlockHerb(plantProps(), types));
        HERBS.add(reg);
        if (Platform.INSTANCE.isDatagen()) {
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
