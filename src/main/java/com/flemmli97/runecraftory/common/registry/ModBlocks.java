package com.flemmli97.runecraftory.common.registry;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.blocks.BlockHerb;
import com.flemmli97.runecraftory.common.blocks.tile.TileCrop;
import com.google.common.collect.Lists;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RuneCraftory.MODID);
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, RuneCraftory.MODID);
    public static final List<RegistryObject<Block>> crops = Lists.newArrayList();

    /*public static final RegistryObject<Block> forge = new BlockForge();
    public static final RegistryObject<Block> cooking = new BlockCookingBench();
    public static final RegistryObject<Block> pharm = new BlockPharmacy();
    public static final RegistryObject<Block> accessory = new BlockAccessoryCrafter();
    public static final RegistryObject<Block> farmland = new BlockRFFarmland();

    public static final RegistryObject<Block> mineralIron = new BlockMineral(EnumMineralTier.IRON);
    public static final RegistryObject<Block> mineralBronze = new BlockMineral(EnumMineralTier.BRONZE);
    public static final RegistryObject<Block> mineralSilver = new BlockMineral(EnumMineralTier.SILVER);
    public static final RegistryObject<Block> mineralGold = new BlockMineral(EnumMineralTier.GOLD);
    public static final RegistryObject<Block> mineralPlatinum = new BlockMineral(EnumMineralTier.PLATINUM);
    public static final RegistryObject<Block> mineralOrichalcum = new BlockMineral(EnumMineralTier.ORICHALCUM);
    public static final RegistryObject<Block> mineralDiamond = new BlockMineral(EnumMineralTier.DIAMOND);
    public static final RegistryObject<Block> mineralDragonic = new BlockMineral(EnumMineralTier.DRAGONIC);
    public static final RegistryObject<Block> mineralAquamarine = new BlockMineral(EnumMineralTier.AQUAMARINE);
    public static final RegistryObject<Block> mineralAmethyst = new BlockMineral(EnumMineralTier.AMETHYST);
    public static final RegistryObject<Block> mineralRuby = new BlockMineral(EnumMineralTier.RUBY);
    public static final RegistryObject<Block> mineralEmerald = new BlockMineral(EnumMineralTier.EMERALD);
    public static final RegistryObject<Block> mineralSapphire = new BlockMineral(EnumMineralTier.SAPPHIRE);

    public static final RegistryObject<Block> brokenMineralIron = new BlockBrokenMineral(EnumMineralTier.IRON);
    public static final RegistryObject<Block> brokenMineralBronze = new BlockBrokenMineral(EnumMineralTier.BRONZE);
    public static final RegistryObject<Block> brokenMineralSilver = new BlockBrokenMineral(EnumMineralTier.SILVER);
    public static final RegistryObject<Block> brokenMineralGold = new BlockBrokenMineral(EnumMineralTier.GOLD);
    public static final RegistryObject<Block> brokenMineralPlatinum = new BlockBrokenMineral(EnumMineralTier.PLATINUM);
    public static final RegistryObject<Block> brokenMineralOrichalcum = new BlockBrokenMineral(EnumMineralTier.ORICHALCUM);
    public static final RegistryObject<Block> brokenMineralDiamond = new BlockBrokenMineral(EnumMineralTier.DIAMOND);
    public static final RegistryObject<Block> brokenMineralDragonic = new BlockBrokenMineral(EnumMineralTier.DRAGONIC);
    public static final RegistryObject<Block> brokenMineralAquamarine = new BlockBrokenMineral(EnumMineralTier.AQUAMARINE);
    public static final RegistryObject<Block> brokenMineralAmethyst = new BlockBrokenMineral(EnumMineralTier.AMETHYST);
    public static final RegistryObject<Block> brokenMineralRuby = new BlockBrokenMineral(EnumMineralTier.RUBY);
    public static final RegistryObject<Block> brokenMineralEmerald = new BlockBrokenMineral(EnumMineralTier.EMERALD);
    public static final RegistryObject<Block> brokenMineralSapphire = new BlockBrokenMineral(EnumMineralTier.SAPPHIRE);

    public static final RegistryObject<Block> bossSpawner = new BlockBossSpawner();
    public static final RegistryObject<Block> research = new BlockResearchTable();
    public static final RegistryObject<Block> ignore = new BlockIgnore();
    public static final RegistryObject<Block> board = new BlockRequestBoard();
    public static final RegistryObject<Block> shipping = new BlockShippingBin();

    public static final RegistryObject<Block> hotSpring = new BlockHotSpring();

    //Crops
    public static final RegistryObject<Block> turnip = new BlockCropBase("turnip", LibOreDictionary.TURNIP);
    public static final RegistryObject<Block> turnipPink = new BlockCropBase("turnip_pink", LibOreDictionary.PINKTURNIP);
    public static final RegistryObject<Block> cabbage = new BlockCropBase("cabbage", LibOreDictionary.CABBAGE);
    public static final RegistryObject<Block> pinkMelon = new BlockCropBase("pink_melon", LibOreDictionary.PINKMELON);
    public static final RegistryObject<Block> pineapple = new BlockCropBase("pineapple", LibOreDictionary.PINEAPPLE);
    public static final RegistryObject<Block> strawberry = new BlockCropBase("strawberry", LibOreDictionary.STRAWBERRY);
    public static final RegistryObject<Block> goldenTurnip = new BlockCropBase("golden_turnip", LibOreDictionary.GOLDENTURNIP);
    public static final RegistryObject<Block> goldenPotato = new BlockCropBase("golden_potato", LibOreDictionary.GOLDENPOTATO);
    public static final RegistryObject<Block> goldenPumpkin = new BlockCropBase("golden_pumpkin", LibOreDictionary.GOLDENPUMPKIN);
    public static final RegistryObject<Block> goldenCabbage = new BlockCropBase("golden_cabbage", LibOreDictionary.GOLDENCABBAGE);
    public static final RegistryObject<Block> hotHotFruit = new BlockCropBase("hot-hot_fruit", LibOreDictionary.HOTHOTFRUIT);
    public static final RegistryObject<Block> bokChoy = new BlockCropBase("bok_choy", LibOreDictionary.BOKCHOY);
    public static final RegistryObject<Block> leek = new BlockCropBase("leek", LibOreDictionary.LEEK);
    public static final RegistryObject<Block> radish = new BlockCropBase("radish", LibOreDictionary.RADISH);
    public static final RegistryObject<Block> spinach = new BlockCropBase("spinach", LibOreDictionary.SPINACH);
    public static final RegistryObject<Block> greenPepper = new BlockCropBase("green_pepper", LibOreDictionary.GREENPEPPER);
    public static final RegistryObject<Block> yam = new BlockCropBase("yam", LibOreDictionary.YAM);
    public static final RegistryObject<Block> eggplant = new BlockCropBase("eggplant", LibOreDictionary.EGGPLANT);
    public static final RegistryObject<Block> tomato = new BlockCropBase("tomato", LibOreDictionary.TOMATO);
    public static final RegistryObject<Block> corn = new BlockCropBase("corn", LibOreDictionary.CORN);
    public static final RegistryObject<Block> cucumber = new BlockCropBase("cucumber", LibOreDictionary.CUCUMBER);
    public static final RegistryObject<Block> pumpkin = new BlockCropBase("pumpkin", LibOreDictionary.PUMPKIN);
    public static final RegistryObject<Block> onion = new BlockCropBase("onion", LibOreDictionary.ONION);
    public static final RegistryObject<Block> fodder = new BlockCropBase("fodder", LibOreDictionary.FODDER);

    public static final RegistryObject<Block> potatoGiant = new BlockCropBase("potato", LibOreDictionary.POTATO);
    public static final RegistryObject<Block> carrotGiant = new BlockCropBase("carrot", LibOreDictionary.CARROT);

    //Flowers
    public static final RegistryObject<Block> whiteCrystal = new BlockCropBase("white_crystal", LibOreDictionary.WHITECRYSTAL);
    public static final RegistryObject<Block> redCrystal = new BlockCropBase("red_crystal", LibOreDictionary.REDCRYSTAL);
    public static final RegistryObject<Block> pomPomGrass = new BlockCropBase("pom-pom_grass", LibOreDictionary.POMPOMGRASS);
    public static final RegistryObject<Block> autumnGrass = new BlockCropBase("autumn_grass", LibOreDictionary.AUTUMNGRASS);
    public static final RegistryObject<Block> noelGrass = new BlockCropBase("noel_grass", LibOreDictionary.NOELGRASS);
    public static final RegistryObject<Block> greenCrystal = new BlockCropBase("green_crystal", LibOreDictionary.GREENCRYSTAL);
    public static final RegistryObject<Block> fireflower = new BlockCropBase("fireflower", LibOreDictionary.FIREFLOWER);
    public static final RegistryObject<Block> fourLeafClover = new BlockCropBase("4-leaf_clover", LibOreDictionary.FOURLEAFCLOVER);
    public static final RegistryObject<Block> ironleaf = new BlockCropBase("ironleaf", LibOreDictionary.IRONLEAF);
    public static final RegistryObject<Block> emeryFlower = new BlockCropBase("emery_flower", LibOreDictionary.EMERYFLOWER);
    public static final RegistryObject<Block> blueCrystal = new BlockCropBase("blue_crystal", LibOreDictionary.BLUECRYSTAL);
    public static final RegistryObject<Block> lampGrass = new BlockCropBase("lamp_grass", LibOreDictionary.LAMPGRASS);
    public static final RegistryObject<Block> cherryGrass = new BlockCropBase("cherry_grass", LibOreDictionary.CHERRYGRASS);
    public static final RegistryObject<Block> charmBlue = new BlockCropBase("charm_blue", LibOreDictionary.CHARMBLUE);
    public static final RegistryObject<Block> pinkCat = new BlockCropBase("pink_cat", LibOreDictionary.PINKCAT);
    public static final RegistryObject<Block> moondropFlower = new BlockCropBase("moondrop_flower", LibOreDictionary.MOONDROPFLOWER);
    public static final RegistryObject<Block> toyherb = new BlockCropBase("toyherb", LibOreDictionary.TOYHERB);*/

    public static final RegistryObject<Block> mushroom = herb("mushroom", ()->ModItems.mushroom);
    public static final RegistryObject<Block> monarchMushroom = herb("monarch_mushroom", ()->ModItems.mushroomMonarch);
    public static final RegistryObject<Block> elliLeaves = herb("elli_leaves", ()->ModItems.elliLeaves);
    public static final RegistryObject<Block> witheredGrass = herb("withered_grass", ()->ModItems.witheredGrass);
    public static final RegistryObject<Block> weeds = herb("weeds", ()->ModItems.weeds);
    public static final RegistryObject<Block> whiteGrass = herb("white_grass", ()->ModItems.whiteGrass);
    public static final RegistryObject<Block> indigoGrass = herb("indigo_grass", ()->ModItems.indigoGrass);
    public static final RegistryObject<Block> purpleGrass = herb("purple_grass", ()->ModItems.purpleGrass);
    public static final RegistryObject<Block> greenGrass = herb("green_grass", ()->ModItems.greenGrass);
    public static final RegistryObject<Block> blueGrass = herb("blue_grass",()-> ModItems.blueGrass);
    public static final RegistryObject<Block> yellowGrass = herb("yellow_grass", ()->ModItems.yellowGrass);
    public static final RegistryObject<Block> redGrass = herb("red_grass", ()->ModItems.redGrass);
    public static final RegistryObject<Block> orangeGrass = herb("orange_grass",()-> ModItems.orangeGrass);
    public static final RegistryObject<Block> blackGrass = herb("black_grass",()-> ModItems.blackGrass);
    public static final RegistryObject<Block> antidoteGrass = herb("antidote_grass", ()->ModItems.antidoteGrass);
    public static final RegistryObject<Block> medicinalHerb = herb("medicinal_herb", ()->ModItems.medicinalHerb);
    public static final RegistryObject<Block> bambooSprout = herb("bamboo_sprout",()-> ModItems.bambooSprout);

    //Trees

    /*public static final RegistryObject<Block> appleTree = new BlockTreeBase("apple_tree_base");
    public static final RegistryObject<Block> appleWood = new BlockTreeWood("apple_wood");
    public static final RegistryObject<Block> appleLeaves = new BlockTreeLeaves("apple_leaves");
    public static final RegistryObject<Block> apple = new BlockTreeFruit("apple_block");
    public static final RegistryObject<Block> appleSapling = new BlockTreeSapling("apple_sapling", appleTree);
    public static final RegistryObject<Block> orangeTree = new BlockTreeBase("orange_tree_base");
    public static final RegistryObject<Block> orangeWood = new BlockTreeWood("orange_wood");
    public static final RegistryObject<Block> orangeLeaves = new BlockTreeLeaves("orange_leaves");
    public static final RegistryObject<Block> orange = new BlockTreeFruit("orange_block");
    public static final RegistryObject<Block> orangeSapling = new BlockTreeSapling("orange_sapling", orangeTree);
    public static final RegistryObject<Block> grapeTree = new BlockTreeBase("grape_tree_base");
    public static final RegistryObject<Block> grapeWood = new BlockTreeWood("grape_wood");
    public static final RegistryObject<Block> grapeLeaves = new BlockTreeLeaves("grape_leaves");
    public static final RegistryObject<Block> grape = new BlockTreeFruit("grape_block");
    public static final RegistryObject<Block> grapeSapling = new BlockTreeSapling("grape_sapling", grapeTree);
    public static final RegistryObject<Block> shinyTree = new BlockTreeBase("shiny_tree_base");
    public static final RegistryObject<Block> shinyWood = new BlockTreeWood("shiny_wood");
    public static final RegistryObject<Block> shinyLeaves = new BlockTreeLeaves("shiny_leaves");
    public static final RegistryObject<Block> shinySapling = new BlockTreeSapling("shiny_sapling", shinyTree);*/

    public static final RegistryObject<TileEntityType<TileCrop>> cropTile = cropTile("crop_tile", crops);

    public static RegistryObject<Block> herb(String name, Supplier<Supplier<Item>> item) {
        return BLOCKS.register(name, () -> new BlockHerb(AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().sound(SoundType.PLANT), item.get()));
    }

    public static <V extends TileEntity> RegistryObject<TileEntityType<V>> cropTile(String name, Collection<RegistryObject<Block>> blocks){
        return TILES.register(name, ()->new TileEntityType(TileCrop::new, blocks.stream().map(obj->obj.get()).collect(Collectors.toSet()), null));
    }
}
