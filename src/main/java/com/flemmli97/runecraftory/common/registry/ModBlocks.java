package com.flemmli97.runecraftory.common.registry;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.blocks.BlockCrop;
import com.flemmli97.runecraftory.common.blocks.BlockHerb;
import com.flemmli97.runecraftory.common.blocks.tile.TileCrop;
import com.google.common.collect.Lists;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
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
    public static final List<RegistryObject<Block>> flowers = Lists.newArrayList();
    public static final List<RegistryObject<Block>> herbs = Lists.newArrayList();

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

    //Crops*/
    public static final RegistryObject<Block> turnip = crop("turnip", ()->ModItems.turnip, ()->ModItems.turnipGiant, ()->ModItems.turnipSeeds);
    public static final RegistryObject<Block> turnipPink = crop("turnip_pink", ()->ModItems.turnipPink, ()->ModItems.turnipPinkGiant, ()->ModItems.turnipPinkSeeds);
    public static final RegistryObject<Block> cabbage = crop("cabbage", ()->ModItems.cabbage, ()->ModItems.cabbageGiant, ()->ModItems.cabbageSeeds);
    public static final RegistryObject<Block> pinkMelon = crop("pink_melon", ()->ModItems.pinkMelon, ()->ModItems.pinkMelonGiant, ()->ModItems.pinkMelonSeeds);
    public static final RegistryObject<Block> pineapple = crop("pineapple", ()->ModItems.pineapple, ()->ModItems.pineappleGiant, ()->ModItems.pineappleSeeds);
    public static final RegistryObject<Block> strawberry = crop("strawberry", ()->ModItems.strawberry, ()->ModItems.strawberryGiant, ()->ModItems.strawberrySeeds);
    public static final RegistryObject<Block> goldenTurnip = crop("golden_turnip", ()->ModItems.goldenTurnip, ()->ModItems.goldenTurnipGiant, ()->ModItems.goldTurnipSeeds);
    public static final RegistryObject<Block> goldenPotato = crop("golden_potato", ()->ModItems.goldenPotato, ()->ModItems.goldenPotatoGiant, ()->ModItems.goldPotatoSeeds);
    public static final RegistryObject<Block> goldenPumpkin = crop("golden_pumpkin", ()->ModItems.goldenPumpkin, ()->ModItems.goldenPumpkinGiant, ()->ModItems.goldPumpkinSeeds);
    public static final RegistryObject<Block> goldenCabbage = crop("golden_cabbage", ()->ModItems.goldenCabbage, ()->ModItems.goldenCabbageGiant, ()->ModItems.goldCabbageSeeds);
    public static final RegistryObject<Block> hotHotFruit = crop("hot_hot_fruit", ()->ModItems.hotHotFruit, ()->ModItems.hotHotFruitGiant, ()->ModItems.hotHotSeeds);
    public static final RegistryObject<Block> bokChoy = crop("bok_choy", ()->ModItems.bokChoy, ()->ModItems.bokChoyGiant, ()->ModItems.bokChoySeeds);
    public static final RegistryObject<Block> leek = crop("leek", ()->ModItems.leek, ()->ModItems.leekGiant, ()->ModItems.leekSeeds);
    public static final RegistryObject<Block> radish = crop("radish", ()->ModItems.radish, ()->ModItems.radishGiant, ()->ModItems.radishSeeds);
    public static final RegistryObject<Block> spinach = crop("spinach", ()->ModItems.spinach, ()->ModItems.spinachGiant, ()->ModItems.spinachSeeds);
    public static final RegistryObject<Block> greenPepper = crop("green_pepper", ()->ModItems.greenPepper, ()->ModItems.greenPepperGiant, ()->ModItems.greenPepperSeeds);
    public static final RegistryObject<Block> yam = crop("yam", ()->ModItems.yam, ()->ModItems.yamGiant, ()->ModItems.yamSeeds);
    public static final RegistryObject<Block> eggplant = crop("eggplant", ()->ModItems.eggplant, ()->ModItems.eggplantGiant, ()->ModItems.eggplantSeeds);
    public static final RegistryObject<Block> tomato = crop("tomato", ()->ModItems.tomato, ()->ModItems.tomatoGiant, ()->ModItems.tomatoSeeds);
    public static final RegistryObject<Block> corn = crop("corn", ()->ModItems.corn, ()->ModItems.cornGiant, ()->ModItems.cornSeeds);
    public static final RegistryObject<Block> cucumber = crop("cucumber", ()->ModItems.cucumber, ()->ModItems.cucumberGiant, ()->ModItems.cucumberSeeds);
    public static final RegistryObject<Block> pumpkin = crop("pumpkin", ()->ModItems.pumpkin, ()->ModItems.pumpkinGiant, ()->ModItems.pumpkinSeeds);
    public static final RegistryObject<Block> onion = crop("onion", ()->ModItems.onion, ()->ModItems.onionGiant, ()->ModItems.onionSeeds);
    public static final RegistryObject<Block> fodder = crop("fodder", ()->ModItems.fodder, ()->()-> Items.AIR, ()->ModItems.fodderSeeds);

    public static final RegistryObject<Block> potatoGiant = crop("potato", ()->()-> Items.POTATO, ()->ModItems.potatoGiant, ()->()-> Items.POTATO);
    public static final RegistryObject<Block> carrotGiant = crop("carrot", ()->()-> Items.CARROT, ()->ModItems.carrotGiant, ()->()-> Items.CARROT);

    //Flowers
    public static final RegistryObject<Block> whiteCrystal = flower("white_crystal", ()->ModItems.whiteCrystal, ()->ModItems.whiteCrystalGiant, ()->ModItems.whiteCrystalSeeds);
    public static final RegistryObject<Block> redCrystal = flower("red_crystal", ()->ModItems.redCrystal, ()->ModItems.redCrystalGiant, ()->ModItems.redCrystalSeeds);
    public static final RegistryObject<Block> pomPomGrass = flower("pom_pom_grass", ()->ModItems.pomPomGrass, ()->ModItems.pomPomGrassGiant, ()->ModItems.pomPomGrassSeeds);
    public static final RegistryObject<Block> autumnGrass = flower("autumn_grass", ()->ModItems.autumnGrass, ()->ModItems.autumnGrassGiant, ()->ModItems.autumnGrassSeeds);
    public static final RegistryObject<Block> noelGrass = flower("noel_grass", ()->ModItems.noelGrass, ()->ModItems.noelGrassGiant, ()->ModItems.noelGrassSeeds);
    public static final RegistryObject<Block> greenCrystal = flower("green_crystal", ()->ModItems.greenCrystal, ()->ModItems.greenCrystalGiant, ()->ModItems.greenCrystalSeeds);
    public static final RegistryObject<Block> fireflower = flower("fireflower", ()->ModItems.fireflower, ()->ModItems.fireflowerGiant, ()->ModItems.fireflowerSeeds);
    public static final RegistryObject<Block> fourLeafClover = flower("4_leaf_clover", ()->ModItems.fourLeafClover, ()->ModItems.fourLeafCloverGiant, ()->ModItems.fourLeafCloverSeeds);
    public static final RegistryObject<Block> ironleaf = flower("ironleaf", ()->ModItems.ironleaf, ()->ModItems.ironleafGiant, ()->ModItems.ironleafSeeds);
    public static final RegistryObject<Block> emeryFlower = flower("emery_flower", ()->ModItems.emeryFlower, ()->ModItems.emeryFlowerGiant, ()->ModItems.emeryFlowerSeeds);
    public static final RegistryObject<Block> blueCrystal = flower("blue_crystal", ()->ModItems.blueCrystal, ()->ModItems.blueCrystalGiant, ()->ModItems.blueCrystalSeeds);
    public static final RegistryObject<Block> lampGrass = flower("lamp_grass", ()->ModItems.lampGrass, ()->ModItems.lampGrassGiant, ()->ModItems.lampGrassSeeds);
    public static final RegistryObject<Block> cherryGrass = flower("cherry_grass", ()->ModItems.cherryGrass, ()->ModItems.cherryGrassGiant, ()->ModItems.cherryGrassSeeds);
    public static final RegistryObject<Block> charmBlue = flower("charm_blue", ()->ModItems.charmBlue, ()->ModItems.charmBlueGiant, ()->ModItems.charmBlueSeeds);
    public static final RegistryObject<Block> pinkCat = flower("pink_cat", ()->ModItems.pinkCat, ()->ModItems.pinkCatGiant, ()->ModItems.pinkCatSeeds);
    public static final RegistryObject<Block> moondropFlower = flower("moondrop_flower", ()->ModItems.moondropFlower, ()->ModItems.moondropFlowerGiant, ()->ModItems.moondropSeeds);
    public static final RegistryObject<Block> toyherb = flower("toyherb",()->ModItems.toyherb, ()->ModItems.toyherbGiant, ()->ModItems.toyherbSeeds);

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

    public static final RegistryObject<TileEntityType<TileCrop>> cropTile = cropTile("crop_tile", combine(Lists.newArrayList(crops), flowers));

    public static RegistryObject<Block> crop(String name, Supplier<Supplier<Item>> crop, Supplier<Supplier<Item>> giant, Supplier<Supplier<Item>> seed) {
        RegistryObject<Block> reg = BLOCKS.register("plant_"+name, () -> new BlockCrop(AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().sound(SoundType.PLANT), crop.get(), giant.get(), seed.get()));
        crops.add(reg);
        return reg;
    }

    public static RegistryObject<Block> flower(String name, Supplier<Supplier<Item>> crop, Supplier<Supplier<Item>> giant, Supplier<Supplier<Item>> seed) {
        RegistryObject<Block> reg = BLOCKS.register("plant_"+name, () -> new BlockCrop(AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().sound(SoundType.PLANT), crop.get(), giant.get(), seed.get()));
        flowers.add(reg);
        return reg;
    }

    public static RegistryObject<Block> herb(String name, Supplier<Supplier<Item>> item) {
        RegistryObject<Block> reg = BLOCKS.register(name, () -> new BlockHerb(AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().sound(SoundType.PLANT), item.get()));
        herbs.add(reg);
        return reg;
    }

    /**
     * Adds another collection to the first and returns the first
     */
    public static <T> Collection<T> combine(Collection<T> one, Collection<T> other){
        one.addAll(other);
        return one;
    }
    public static <V extends TileEntity> RegistryObject<TileEntityType<V>> cropTile(String name, Collection<RegistryObject<Block>> blocks){
        return TILES.register(name, ()->new TileEntityType(TileCrop::new, blocks.stream().map(obj->obj.get()).collect(Collectors.toSet()), null));
    }
}
