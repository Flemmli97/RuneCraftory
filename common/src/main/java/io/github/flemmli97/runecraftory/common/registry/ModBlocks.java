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
    public static final List<RegistryEntrySupplier<Block>> crops = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Block>> flowers = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Block>> herbs = new ArrayList<>();
    public static final Map<RegistryEntrySupplier<Block>, RegistryEntrySupplier<Block>> giantCropMap = new HashMap<>();
    public static final EnumMap<EnumMineralTier, RegistryEntrySupplier<Block>> mineralMap = new EnumMap<>(EnumMineralTier.class);
    public static final EnumMap<EnumMineralTier, RegistryEntrySupplier<Block>> brokenMineralMap = new EnumMap<>(EnumMineralTier.class);

    public static final RegistryEntrySupplier<Block> forge = BLOCKS.register("forge", () -> new BlockForge(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3, 100)));
    public static final RegistryEntrySupplier<Block> cooking = BLOCKS.register("cooking_table", () -> new BlockCooking(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3, 100)));
    public static final RegistryEntrySupplier<Block> chemistry = BLOCKS.register("chemistry_set", () -> new BlockChemistry(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3, 100)));
    public static final RegistryEntrySupplier<Block> accessory = BLOCKS.register("accessory_workbench", () -> new BlockAccessory(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3, 100)));

    public static final RegistryEntrySupplier<Block> mineralIron = mineral(EnumMineralTier.IRON);
    public static final RegistryEntrySupplier<Block> mineralBronze = mineral(EnumMineralTier.BRONZE);
    public static final RegistryEntrySupplier<Block> mineralSilver = mineral(EnumMineralTier.SILVER);
    public static final RegistryEntrySupplier<Block> mineralGold = mineral(EnumMineralTier.GOLD);
    public static final RegistryEntrySupplier<Block> mineralPlatinum = mineral(EnumMineralTier.PLATINUM);
    public static final RegistryEntrySupplier<Block> mineralOrichalcum = mineral(EnumMineralTier.ORICHALCUM);
    public static final RegistryEntrySupplier<Block> mineralDiamond = mineral(EnumMineralTier.DIAMOND);
    public static final RegistryEntrySupplier<Block> mineralDragonic = mineral(EnumMineralTier.DRAGONIC);
    public static final RegistryEntrySupplier<Block> mineralAquamarine = mineral(EnumMineralTier.AQUAMARINE);
    public static final RegistryEntrySupplier<Block> mineralAmethyst = mineral(EnumMineralTier.AMETHYST);
    public static final RegistryEntrySupplier<Block> mineralRuby = mineral(EnumMineralTier.RUBY);
    public static final RegistryEntrySupplier<Block> mineralEmerald = mineral(EnumMineralTier.EMERALD);
    public static final RegistryEntrySupplier<Block> mineralSapphire = mineral(EnumMineralTier.SAPPHIRE);

    public static final RegistryEntrySupplier<Block> brokenMineralIron = brokenMineral(EnumMineralTier.IRON);
    public static final RegistryEntrySupplier<Block> brokenMineralBronze = brokenMineral(EnumMineralTier.BRONZE);
    public static final RegistryEntrySupplier<Block> brokenMineralSilver = brokenMineral(EnumMineralTier.SILVER);
    public static final RegistryEntrySupplier<Block> brokenMineralGold = brokenMineral(EnumMineralTier.GOLD);
    public static final RegistryEntrySupplier<Block> brokenMineralPlatinum = brokenMineral(EnumMineralTier.PLATINUM);
    public static final RegistryEntrySupplier<Block> brokenMineralOrichalcum = brokenMineral(EnumMineralTier.ORICHALCUM);
    public static final RegistryEntrySupplier<Block> brokenMineralDiamond = brokenMineral(EnumMineralTier.DIAMOND);
    public static final RegistryEntrySupplier<Block> brokenMineralDragonic = brokenMineral(EnumMineralTier.DRAGONIC);
    public static final RegistryEntrySupplier<Block> brokenMineralAquamarine = brokenMineral(EnumMineralTier.AQUAMARINE);
    public static final RegistryEntrySupplier<Block> brokenMineralAmethyst = brokenMineral(EnumMineralTier.AMETHYST);
    public static final RegistryEntrySupplier<Block> brokenMineralRuby = brokenMineral(EnumMineralTier.RUBY);
    public static final RegistryEntrySupplier<Block> brokenMineralEmerald = brokenMineral(EnumMineralTier.EMERALD);
    public static final RegistryEntrySupplier<Block> brokenMineralSapphire = brokenMineral(EnumMineralTier.SAPPHIRE);

    public static final RegistryEntrySupplier<Block> bossSpawner = BLOCKS.register("boss_spawner", () -> new BlockBossSpawner(BlockBehaviour.Properties.of(Material.METAL).strength(60, 9999).noOcclusion()));
    public static final RegistryEntrySupplier<Block> shipping = BLOCKS.register("shipping_bin", () -> new BlockShippingBin(BlockBehaviour.Properties.of(Material.WOOD).strength(3, 10)));
    public static final RegistryEntrySupplier<Block> singleSpawnBlock = BLOCKS.register("one_time_spawner", () -> new BlockSingleTimeSpawner(BlockBehaviour.Properties.of(Material.METAL).strength(60, 9999).noOcclusion()));
    public static final RegistryEntrySupplier<Block> cashRegister = BLOCKS.register("cash_register", () -> new BlockCashRegister(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3, 5)));
    public static final RegistryEntrySupplier<Block> monsterBarn = BLOCKS.register("monster_barn", () -> new BlockMonsterBarn(BlockBehaviour.Properties.of(Material.GRASS).sound(SoundType.GRASS).noOcclusion().noCollission().strength(1, 10000)));
    public static final RegistryEntrySupplier<Block> questBoard = BLOCKS.register("quest_board", () -> new BlockQuestboard(BlockBehaviour.Properties.of(Material.WOOD).sound(SoundType.WOOD).noOcclusion().strength(2, 5)));

    /*public static final RegistryEntrySupplier<Block> hotSpring = new BlockHotSpring();

    //Crops*/
    public static final RegistryEntrySupplier<Block> turnip = crop("turnip", () -> ModItems.turnip, () -> ModItems.turnipSeeds);
    public static final RegistryEntrySupplier<Block> turnipPink = crop("turnip_pink", () -> ModItems.turnipPink, () -> ModItems.turnipPinkSeeds);
    public static final RegistryEntrySupplier<Block> cabbage = crop("cabbage", () -> ModItems.cabbage, () -> ModItems.cabbageSeeds);
    public static final RegistryEntrySupplier<Block> pinkMelon = crop("pink_melon", () -> ModItems.pinkMelon, () -> ModItems.pinkMelonSeeds);
    public static final RegistryEntrySupplier<Block> pineapple = crop("pineapple", () -> ModItems.pineapple, () -> ModItems.pineappleSeeds);
    public static final RegistryEntrySupplier<Block> strawberry = crop("strawberry", () -> ModItems.strawberry, () -> ModItems.strawberrySeeds);
    public static final RegistryEntrySupplier<Block> goldenTurnip = crop("golden_turnip", () -> ModItems.goldenTurnip, () -> ModItems.goldTurnipSeeds);
    public static final RegistryEntrySupplier<Block> goldenPotato = crop("golden_potato", () -> ModItems.goldenPotato, () -> ModItems.goldPotatoSeeds);
    public static final RegistryEntrySupplier<Block> goldenPumpkin = crop("golden_pumpkin", () -> ModItems.goldenPumpkin, () -> ModItems.goldPumpkinSeeds);
    public static final RegistryEntrySupplier<Block> goldenCabbage = crop("golden_cabbage", () -> ModItems.goldenCabbage, () -> ModItems.goldCabbageSeeds);
    public static final RegistryEntrySupplier<Block> hotHotFruit = crop("hot_hot_fruit", () -> ModItems.hotHotFruit, () -> ModItems.hotHotSeeds);
    public static final RegistryEntrySupplier<Block> bokChoy = crop("bok_choy", () -> ModItems.bokChoy, () -> ModItems.bokChoySeeds);
    public static final RegistryEntrySupplier<Block> leek = crop("leek", () -> ModItems.leek, () -> ModItems.leekSeeds);
    public static final RegistryEntrySupplier<Block> radish = crop("radish", () -> ModItems.radish, () -> ModItems.radishSeeds);
    public static final RegistryEntrySupplier<Block> spinach = crop("spinach", () -> ModItems.spinach, () -> ModItems.spinachSeeds);
    public static final RegistryEntrySupplier<Block> greenPepper = crop("green_pepper", () -> ModItems.greenPepper, () -> ModItems.greenPepperSeeds);
    public static final RegistryEntrySupplier<Block> yam = crop("yam", () -> ModItems.yam, () -> ModItems.yamSeeds);
    public static final RegistryEntrySupplier<Block> eggplant = crop("eggplant", () -> ModItems.eggplant, () -> ModItems.eggplantSeeds);
    public static final RegistryEntrySupplier<Block> tomato = crop("tomato", () -> ModItems.tomato, () -> ModItems.tomatoSeeds);
    public static final RegistryEntrySupplier<Block> corn = crop("corn", () -> ModItems.corn, () -> ModItems.cornSeeds);
    public static final RegistryEntrySupplier<Block> cucumber = crop("cucumber", () -> ModItems.cucumber, () -> ModItems.cucumberSeeds);
    public static final RegistryEntrySupplier<Block> pumpkin = crop("pumpkin", () -> ModItems.pumpkin, () -> ModItems.pumpkinSeeds);
    public static final RegistryEntrySupplier<Block> onion = crop("onion", () -> ModItems.onion, () -> ModItems.onionSeeds);
    public static final RegistryEntrySupplier<Block> turnipGiant = giantCrop("tyrant_turnip", () -> ModItems.turnipGiant, () -> ModItems.turnipSeeds, turnip);
    public static final RegistryEntrySupplier<Block> turnipPinkGiant = giantCrop("colossal_pink", () -> ModItems.turnipPinkGiant, () -> ModItems.turnipPinkSeeds, turnipPink);
    public static final RegistryEntrySupplier<Block> cabbageGiant = giantCrop("king_cabbage", () -> ModItems.cabbageGiant, () -> ModItems.cabbageSeeds, cabbage);
    public static final RegistryEntrySupplier<Block> pinkMelonGiant = giantCrop("conqueror_melon", () -> ModItems.pinkMelonGiant, () -> ModItems.pinkMelonSeeds, pinkMelon);
    public static final RegistryEntrySupplier<Block> pineappleGiant = giantCrop("king_pineapple", () -> ModItems.pineappleGiant, () -> ModItems.pineappleSeeds, pineapple);
    public static final RegistryEntrySupplier<Block> strawberryGiant = giantCrop("sultan_strawberry", () -> ModItems.strawberryGiant, () -> ModItems.strawberrySeeds, strawberry);
    public static final RegistryEntrySupplier<Block> goldenTurnipGiant = giantCrop("golden_tyrant_turnip", () -> ModItems.goldenTurnipGiant, () -> ModItems.goldTurnipSeeds, goldenTurnip);
    public static final RegistryEntrySupplier<Block> goldenPotatoGiant = giantCrop("gold_prince_potato", () -> ModItems.goldenPotatoGiant, () -> ModItems.goldPotatoSeeds, goldenPotato);
    public static final RegistryEntrySupplier<Block> goldenPumpkinGiant = giantCrop("golden_doom_pumpkin", () -> ModItems.goldenPumpkinGiant, () -> ModItems.goldPumpkinSeeds, pumpkin);
    public static final RegistryEntrySupplier<Block> goldenCabbageGiant = giantCrop("golden_king_cabbage", () -> ModItems.goldenCabbageGiant, () -> ModItems.goldCabbageSeeds, cabbageGiant);
    public static final RegistryEntrySupplier<Block> hotHotFruitGiant = giantCrop("giant_hot_hot_fruit", () -> ModItems.hotHotFruitGiant, () -> ModItems.hotHotSeeds, hotHotFruit);
    public static final RegistryEntrySupplier<Block> bokChoyGiant = giantCrop("boss_bok_choy", () -> ModItems.bokChoyGiant, () -> ModItems.bokChoySeeds, bokChoy);
    public static final RegistryEntrySupplier<Block> leekGiant = giantCrop("legendary_leek", () -> ModItems.leekGiant, () -> ModItems.leekSeeds, leek);
    public static final RegistryEntrySupplier<Block> radishGiant = giantCrop("noble_radish", () -> ModItems.radishGiant, () -> ModItems.radishSeeds, radish);
    public static final RegistryEntrySupplier<Block> spinachGiant = giantCrop("sovereign_spinach", () -> ModItems.spinachGiant, () -> ModItems.spinachSeeds, spinach);
    public static final RegistryEntrySupplier<Block> greenPepperGiant = giantCrop("green_pepper_rex", () -> ModItems.greenPepperGiant, () -> ModItems.greenPepperSeeds, greenPepper);
    public static final RegistryEntrySupplier<Block> yamGiant = giantCrop("lorldy_yam", () -> ModItems.yamGiant, () -> ModItems.yamSeeds, yam);
    public static final RegistryEntrySupplier<Block> eggplantGiant = giantCrop("emperor_eggplant", () -> ModItems.eggplantGiant, () -> ModItems.eggplantSeeds, eggplant);
    public static final RegistryEntrySupplier<Block> tomatoGiant = giantCrop("titan_tomato", () -> ModItems.tomatoGiant, () -> ModItems.tomatoSeeds, tomato);
    public static final RegistryEntrySupplier<Block> cornGiant = giantCrop("gigant_corn", () -> ModItems.cornGiant, () -> ModItems.cornSeeds, corn);
    public static final RegistryEntrySupplier<Block> cucumberGiant = giantCrop("kaiser_cucumber", () -> ModItems.cucumberGiant, () -> ModItems.cucumberSeeds, cucumber);
    public static final RegistryEntrySupplier<Block> pumpkinGiant = giantCrop("doom_pumpkin", () -> ModItems.pumpkinGiant, () -> ModItems.pumpkinSeeds, pumpkin);
    public static final RegistryEntrySupplier<Block> onionGiant = giantCrop("ultra_onion", () -> ModItems.onionGiant, () -> ModItems.onionSeeds, onion);
    public static final RegistryEntrySupplier<Block> fodder = crop("fodder", () -> ModItems.fodder, () -> ModItems.fodderSeeds);

    public static final RegistryEntrySupplier<Block> shieldCrop = crop("shield", () -> ModItems.seedShield, () -> ModItems.shieldSeeds);
    public static final RegistryEntrySupplier<Block> swordCrop = crop("sword", () -> ModItems.seedSword, () -> ModItems.swordSeeds);

    public static final RegistryEntrySupplier<Block> dungeon = crop("dungeon", () -> () -> Items.STONE, () -> ModItems.dungeonSeeds);

    public static final RegistryEntrySupplier<Block> potatoGiant = crop("potato", () -> () -> Items.POTATO, () -> () -> Items.POTATO);
    public static final RegistryEntrySupplier<Block> carrotGiant = crop("carrot", () -> () -> Items.CARROT, () -> () -> Items.CARROT);

    //Flowers
    public static final RegistryEntrySupplier<Block> toyherb = flower("toyherb", () -> ModItems.toyherb, () -> ModItems.toyherbSeeds);
    public static final RegistryEntrySupplier<Block> moondropFlower = flower("moondrop_flower", () -> ModItems.moondropFlower, () -> ModItems.moondropSeeds);
    public static final RegistryEntrySupplier<Block> pinkCat = flower("pink_cat", () -> ModItems.pinkCat, () -> ModItems.pinkCatSeeds);
    public static final RegistryEntrySupplier<Block> charmBlue = flower("charm_blue", () -> ModItems.charmBlue, () -> ModItems.charmBlueSeeds);
    public static final RegistryEntrySupplier<Block> lampGrass = flower("lamp_grass", () -> ModItems.lampGrass, () -> ModItems.lampGrassSeeds);
    public static final RegistryEntrySupplier<Block> cherryGrass = flower("cherry_grass", () -> ModItems.cherryGrass, () -> ModItems.cherryGrassSeeds);
    public static final RegistryEntrySupplier<Block> pomPomGrass = flower("pom_pom_grass", () -> ModItems.pomPomGrass, () -> ModItems.pomPomGrassSeeds);
    public static final RegistryEntrySupplier<Block> autumnGrass = flower("autumn_grass", () -> ModItems.autumnGrass, () -> ModItems.autumnGrassSeeds);
    public static final RegistryEntrySupplier<Block> noelGrass = flower("noel_grass", () -> ModItems.noelGrass, () -> ModItems.noelGrassSeeds);
    public static final RegistryEntrySupplier<Block> fireflower = flower("fireflower", () -> ModItems.fireflower, () -> ModItems.fireflowerSeeds);
    public static final RegistryEntrySupplier<Block> fourLeafClover = flower("four_leaf_clover", () -> ModItems.fourLeafClover, () -> ModItems.fourLeafCloverSeeds);
    public static final RegistryEntrySupplier<Block> ironleaf = flower("ironleaf", () -> ModItems.ironleaf, () -> ModItems.ironleafSeeds);
    public static final RegistryEntrySupplier<Block> whiteCrystal = flower("white_crystal", () -> ModItems.whiteCrystal, () -> ModItems.whiteCrystalSeeds);
    public static final RegistryEntrySupplier<Block> redCrystal = flower("red_crystal", () -> ModItems.redCrystal, () -> ModItems.redCrystalSeeds);
    public static final RegistryEntrySupplier<Block> greenCrystal = flower("green_crystal", () -> ModItems.greenCrystal, () -> ModItems.greenCrystalSeeds);
    public static final RegistryEntrySupplier<Block> blueCrystal = flower("blue_crystal", () -> ModItems.blueCrystal, () -> ModItems.blueCrystalSeeds);
    public static final RegistryEntrySupplier<Block> emeryFlower = flower("emery_flower", () -> ModItems.emeryFlower, () -> ModItems.emeryFlowerSeeds);
    public static final RegistryEntrySupplier<Block> toyherbGiant = giantFlower("ultra_toyherb", () -> ModItems.toyherbGiant, () -> ModItems.toyherbSeeds, toyherb);
    public static final RegistryEntrySupplier<Block> moondropFlowerGiant = giantFlower("ultra_moondrop_flower", () -> ModItems.moondropFlowerGiant, () -> ModItems.moondropSeeds, moondropFlower);
    public static final RegistryEntrySupplier<Block> pinkCatGiant = giantFlower("king_pink_cat", () -> ModItems.pinkCatGiant, () -> ModItems.pinkCatSeeds, pinkCat);
    public static final RegistryEntrySupplier<Block> charmBlueGiant = giantFlower("great_charm_blue", () -> ModItems.charmBlueGiant, () -> ModItems.charmBlueSeeds, charmBlue);
    public static final RegistryEntrySupplier<Block> lampGrassGiant = giantFlower("kaiser_lamp_grass", () -> ModItems.lampGrassGiant, () -> ModItems.lampGrassSeeds, lampGrass);
    public static final RegistryEntrySupplier<Block> cherryGrassGiant = giantFlower("king_cherry_grass", () -> ModItems.cherryGrassGiant, () -> ModItems.cherryGrassSeeds, cherryGrass);
    public static final RegistryEntrySupplier<Block> pomPomGrassGiant = giantFlower("king_pom_pom_grass", () -> ModItems.pomPomGrassGiant, () -> ModItems.pomPomGrassSeeds, pomPomGrass);
    public static final RegistryEntrySupplier<Block> autumnGrassGiant = giantFlower("big_autumn_grass", () -> ModItems.autumnGrassGiant, () -> ModItems.autumnGrassSeeds, autumnGrass);
    public static final RegistryEntrySupplier<Block> noelGrassGiant = giantFlower("large_noel_grass", () -> ModItems.noelGrassGiant, () -> ModItems.noelGrassSeeds, noelGrass);
    public static final RegistryEntrySupplier<Block> fireflowerGiant = giantFlower("big_fireflower", () -> ModItems.fireflowerGiant, () -> ModItems.fireflowerSeeds, fireflower);
    public static final RegistryEntrySupplier<Block> fourLeafCloverGiant = giantFlower("great_four_leaf_clover", () -> ModItems.fourLeafCloverGiant, () -> ModItems.fourLeafCloverSeeds, fourLeafClover);
    public static final RegistryEntrySupplier<Block> ironleafGiant = giantFlower("super_ironleaf", () -> ModItems.ironleafGiant, () -> ModItems.ironleafSeeds, ironleaf);
    public static final RegistryEntrySupplier<Block> whiteCrystalGiant = giantFlower("big_white_crystal", () -> ModItems.whiteCrystalGiant, () -> ModItems.whiteCrystalSeeds, whiteCrystal);
    public static final RegistryEntrySupplier<Block> redCrystalGiant = giantFlower("big_red_crystal", () -> ModItems.redCrystalGiant, () -> ModItems.redCrystalSeeds, redCrystal);
    public static final RegistryEntrySupplier<Block> greenCrystalGiant = giantFlower("big_green_crystal", () -> ModItems.greenCrystalGiant, () -> ModItems.greenCrystalSeeds, greenCrystal);
    public static final RegistryEntrySupplier<Block> blueCrystalGiant = giantFlower("big_blue_crystal", () -> ModItems.blueCrystalGiant, () -> ModItems.blueCrystalSeeds, blueCrystal);
    public static final RegistryEntrySupplier<Block> emeryFlowerGiant = giantFlower("great_emery_flower", () -> ModItems.emeryFlowerGiant, () -> ModItems.emeryFlowerSeeds, emeryFlower);

    public static final RegistryEntrySupplier<Block> mushroom = herb("mushroom");
    public static final RegistryEntrySupplier<Block> monarchMushroom = herb("monarch_mushroom");
    public static final RegistryEntrySupplier<Block> elliLeaves = herb("elli_leaves", BlockHerb.GroundTypes.END);
    public static final RegistryEntrySupplier<Block> witheredGrass = herb("withered_grass", BlockHerb.GroundTypes.SANDY);
    public static final RegistryEntrySupplier<Block> weeds = herb("weeds");
    public static final RegistryEntrySupplier<Block> whiteGrass = herb("white_grass");
    public static final RegistryEntrySupplier<Block> indigoGrass = herb("indigo_grass");
    public static final RegistryEntrySupplier<Block> purpleGrass = herb("purple_grass");
    public static final RegistryEntrySupplier<Block> greenGrass = herb("green_grass");
    public static final RegistryEntrySupplier<Block> blueGrass = herb("blue_grass");
    public static final RegistryEntrySupplier<Block> yellowGrass = herb("yellow_grass", BlockHerb.GroundTypes.SANDY, BlockHerb.GroundTypes.NETHER);
    public static final RegistryEntrySupplier<Block> redGrass = herb("red_grass", BlockHerb.GroundTypes.NETHER);
    public static final RegistryEntrySupplier<Block> orangeGrass = herb("orange_grass", BlockHerb.GroundTypes.SANDY, BlockHerb.GroundTypes.NETHER);
    public static final RegistryEntrySupplier<Block> blackGrass = herb("black_grass", BlockHerb.GroundTypes.END);
    public static final RegistryEntrySupplier<Block> antidoteGrass = herb("antidote_grass");
    public static final RegistryEntrySupplier<Block> medicinalHerb = herb("medicinal_herb");
    public static final RegistryEntrySupplier<Block> bambooSprout = herb("bamboo_sprout");

    public static final RegistryEntrySupplier<Block> snow = BLOCKS.register("snow", () -> new BlockMeltableSnow(BlockBehaviour.Properties.of(Material.TOP_SNOW).randomTicks().strength(0.1f).requiresCorrectToolForDrops().sound(SoundType.SNOW).isViewBlocking((blockState, blockGetter, blockPos) -> blockState.getValue(SnowLayerBlock.LAYERS) >= 8)));

    //Trees
    public static final RegistryEntrySupplier<Block> treeSoil = BLOCKS.register("tree_soil", () -> new BlockTreeRoot(BlockBehaviour.Properties.of(Material.DIRT).sound(SoundType.GRAVEL).strength(-1, 99999)));
    public static final RegistryEntrySupplier<BlockTreeBase> appleTree = BLOCKS.register("apple_tree", () -> new BlockTreeBase(logProps(), () -> ModFeatures.APPLE_1.value(), () -> ModFeatures.APPLE_2.value(), () -> ModFeatures.APPLE_3.value(), ModItems.appleSapling));
    public static final RegistryEntrySupplier<Block> appleSapling = BLOCKS.register("apple_sapling", () -> new BlockTreeSapling(BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS), appleTree));
    public static final RegistryEntrySupplier<RotatedPillarBlock> appleWood = BLOCKS.register("apple_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(-1, 99999)));
    public static final RegistryEntrySupplier<Block> appleLeaves = BLOCKS.register("apple_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).strength(-1, 99999)));
    public static final RegistryEntrySupplier<Block> apple = BLOCKS.register("apple_leaves_fruit", () -> new BlockFruitTreeLeaf(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).strength(-1, 99999), () -> Items.APPLE));
    /*
    public static final RegistryEntrySupplier<Block> orangeTree = new BlockTreeBase("orange_tree_base");
    public static final RegistryEntrySupplier<Block> orangeWood = new BlockTreeWood("orange_wood");
    public static final RegistryEntrySupplier<Block> orangeLeaves = new BlockTreeLeaves("orange_leaves");
    public static final RegistryEntrySupplier<Block> orange = new BlockTreeFruit("orange_block");
    public static final RegistryEntrySupplier<Block> orangeSapling = new BlockTreeSapling("orange_sapling", orangeTree);
    public static final RegistryEntrySupplier<Block> grapeTree = new BlockTreeBase("grape_tree_base");
    public static final RegistryEntrySupplier<Block> grapeWood = new BlockTreeWood("grape_wood");
    public static final RegistryEntrySupplier<Block> grapeLeaves = new BlockTreeLeaves("grape_leaves");
    public static final RegistryEntrySupplier<Block> grape = new BlockTreeFruit("grape_block");
    public static final RegistryEntrySupplier<Block> grapeSapling = new BlockTreeSapling("grape_sapling", grapeTree);
    public static final RegistryEntrySupplier<Block> shinyTree = new BlockTreeBase("shiny_tree_base");
    public static final RegistryEntrySupplier<Block> shinyWood = new BlockTreeWood("shiny_wood");
    public static final RegistryEntrySupplier<Block> shinyLeaves = new BlockTreeLeaves("shiny_leaves");
    public static final RegistryEntrySupplier<Block> shinySapling = new BlockTreeSapling("shiny_sapling", shinyTree);*/

    public static final RegistryEntrySupplier<BlockEntityType<BrokenMineralBlockEntity>> brokenMineralTile = brokenMineralTile("broken_mineral_tile", brokenMineralMap.values());
    public static final RegistryEntrySupplier<BlockEntityType<AccessoryBlockEntity>> accessoryTile = TILES.register("accessory_tile", () -> Platform.INSTANCE.blockEntityType(AccessoryBlockEntity::new, accessory.get()));
    public static final RegistryEntrySupplier<BlockEntityType<ForgingBlockEntity>> forgingTile = TILES.register("forge_tile", () -> Platform.INSTANCE.blockEntityType(ForgingBlockEntity::new, forge.get()));
    public static final RegistryEntrySupplier<BlockEntityType<ChemistryBlockEntity>> chemistryTile = TILES.register("chemistry_tile", () -> Platform.INSTANCE.blockEntityType(ChemistryBlockEntity::new, chemistry.get()));
    public static final RegistryEntrySupplier<BlockEntityType<CookingBlockEntity>> cookingTile = TILES.register("cooking_tile", () -> Platform.INSTANCE.blockEntityType(CookingBlockEntity::new, cooking.get()));
    public static final RegistryEntrySupplier<BlockEntityType<BossSpawnerBlockEntity>> bossSpawnerTile = TILES.register("spawner_tile", () -> Platform.INSTANCE.blockEntityType(BossSpawnerBlockEntity::new, bossSpawner.get()));
    public static final RegistryEntrySupplier<BlockEntityType<SingleTimeSpawner>> singleSpawnerTile = TILES.register("single_spawner_tile", () -> Platform.INSTANCE.blockEntityType(SingleTimeSpawner::new, singleSpawnBlock.get()));
    public static final RegistryEntrySupplier<BlockEntityType<MonsterBarnBlockEntity>> monsterBarnBlockEntity = TILES.register("monster_barn_block_entity", () -> Platform.INSTANCE.blockEntityType(MonsterBarnBlockEntity::new, monsterBarn.get()));
    public static final RegistryEntrySupplier<BlockEntityType<TreeBlockEntity>> treeBlockEntity = TILES.register("tree", () -> Platform.INSTANCE.blockEntityType(TreeBlockEntity::new, appleTree.get()));

    public static RegistryEntrySupplier<Block> mineral(EnumMineralTier name) {
        RegistryEntrySupplier<Block> reg = BLOCKS.register("ore_" + name.getSerializedName(), () -> new BlockMineral(name, BlockBehaviour.Properties.of(Material.STONE).strength(5, 10)
                .requiresCorrectToolForDrops()));
        mineralMap.put(name, reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block> brokenMineral(EnumMineralTier name) {
        RegistryEntrySupplier<Block> reg = BLOCKS.register("ore_broken_" + name.getSerializedName(), () -> new BlockBrokenMineral(name, BlockBehaviour.Properties.of(Material.STONE).strength(30, 15)));
        brokenMineralMap.put(name, reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block> crop(String name, Supplier<Supplier<Item>> crop, Supplier<Supplier<Item>> seed) {
        RegistryEntrySupplier<Block> reg = BLOCKS.register(name, () -> new BlockCrop(BlockBehaviour.Properties.of(Material.PLANT).noCollission().sound(SoundType.GRASS), crop.get(), seed.get()));
        crops.add(reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block> giantCrop(String name, Supplier<Supplier<Item>> giant, Supplier<Supplier<Item>> seed, RegistryEntrySupplier<Block> crop) {
        RegistryEntrySupplier<Block> reg = BLOCKS.register(name, () -> new BlockGiantCrop(BlockBehaviour.Properties.of(Material.PLANT).noCollission().sound(SoundType.GRASS), giant.get(), seed.get()));
        crops.add(reg);
        if (Platform.INSTANCE.isDatagen())
            giantCropMap.put(crop, reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block> flower(String name, Supplier<Supplier<Item>> crop, Supplier<Supplier<Item>> seed) {
        RegistryEntrySupplier<Block> reg = BLOCKS.register(name, () -> new BlockCrop(BlockBehaviour.Properties.of(Material.PLANT).noCollission().sound(SoundType.GRASS), crop.get(), seed.get()));
        flowers.add(reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block> giantFlower(String name, Supplier<Supplier<Item>> giant, Supplier<Supplier<Item>> seed, RegistryEntrySupplier<Block> flower) {
        RegistryEntrySupplier<Block> reg = BLOCKS.register(name, () -> new BlockGiantCrop(BlockBehaviour.Properties.of(Material.PLANT).noCollission().sound(SoundType.GRASS), giant.get(), seed.get()));
        flowers.add(reg);
        if (Platform.INSTANCE.isDatagen())
            giantCropMap.put(flower, reg);
        return reg;
    }

    public static RegistryEntrySupplier<Block> herb(String name, BlockHerb.GroundTypes... types) {
        RegistryEntrySupplier<Block> reg = BLOCKS.register(name, () -> new BlockHerb(BlockBehaviour.Properties.of(Material.PLANT).noCollission().sound(SoundType.GRASS), types));
        herbs.add(reg);
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
