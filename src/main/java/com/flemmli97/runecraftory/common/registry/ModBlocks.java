package com.flemmli97.runecraftory.common.registry;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.enums.EnumMineralTier;
import com.flemmli97.runecraftory.common.blocks.BlockAccessory;
import com.flemmli97.runecraftory.common.blocks.BlockBossSpawner;
import com.flemmli97.runecraftory.common.blocks.BlockBrokenMineral;
import com.flemmli97.runecraftory.common.blocks.BlockChemistry;
import com.flemmli97.runecraftory.common.blocks.BlockCooking;
import com.flemmli97.runecraftory.common.blocks.BlockCrop;
import com.flemmli97.runecraftory.common.blocks.BlockFarm;
import com.flemmli97.runecraftory.common.blocks.BlockForge;
import com.flemmli97.runecraftory.common.blocks.BlockHerb;
import com.flemmli97.runecraftory.common.blocks.BlockMineral;
import com.flemmli97.runecraftory.common.blocks.BlockShippingBin;
import com.flemmli97.runecraftory.common.blocks.tile.TileAccessory;
import com.flemmli97.runecraftory.common.blocks.tile.TileChemistry;
import com.flemmli97.runecraftory.common.blocks.tile.TileCooking;
import com.flemmli97.runecraftory.common.blocks.tile.TileCrop;
import com.flemmli97.runecraftory.common.blocks.tile.TileFarm;
import com.flemmli97.runecraftory.common.blocks.tile.TileForge;
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
import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RuneCraftory.MODID);
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, RuneCraftory.MODID);
    public static final List<RegistryObject<Block>> crops = Lists.newArrayList();
    public static final List<RegistryObject<Block>> flowers = Lists.newArrayList();
    public static final List<RegistryObject<Block>> herbs = Lists.newArrayList();
    public static final EnumMap<EnumMineralTier, RegistryObject<Block>> mineralMap = new EnumMap<>(EnumMineralTier.class);
    public static final EnumMap<EnumMineralTier, RegistryObject<Block>> brokenMineralMap = new EnumMap<>(EnumMineralTier.class);

    public static final RegistryObject<Block> forge = BLOCKS.register("forge", () -> new BlockForge(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(3, 100)));
    public static final RegistryObject<Block> cooking = BLOCKS.register("cooking", () -> new BlockCooking(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(3, 100)));
    public static final RegistryObject<Block> chemistry = BLOCKS.register("chemistry", () -> new BlockChemistry(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(3, 100)));
    public static final RegistryObject<Block> accessory = BLOCKS.register("accessory", () -> new BlockAccessory(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(3, 100)));
    public static final RegistryObject<Block> farmland = BLOCKS.register("farmland", () -> new BlockFarm(AbstractBlock.Properties.create(Material.EARTH).hardnessAndResistance(0.6F).sound(SoundType.GROUND).blockVision((state, reader, pos) -> true).suffocates((state, reader, pos) -> true)));

    public static final RegistryObject<Block> mineralIron = mineral(EnumMineralTier.IRON);
    public static final RegistryObject<Block> mineralBronze = mineral(EnumMineralTier.BRONZE);
    public static final RegistryObject<Block> mineralSilver = mineral(EnumMineralTier.SILVER);
    public static final RegistryObject<Block> mineralGold = mineral(EnumMineralTier.GOLD);
    public static final RegistryObject<Block> mineralPlatinum = mineral(EnumMineralTier.PLATINUM);
    public static final RegistryObject<Block> mineralOrichalcum = mineral(EnumMineralTier.ORICHALCUM);
    public static final RegistryObject<Block> mineralDiamond = mineral(EnumMineralTier.DIAMOND);
    public static final RegistryObject<Block> mineralDragonic = mineral(EnumMineralTier.DRAGONIC);
    public static final RegistryObject<Block> mineralAquamarine = mineral(EnumMineralTier.AQUAMARINE);
    public static final RegistryObject<Block> mineralAmethyst = mineral(EnumMineralTier.AMETHYST);
    public static final RegistryObject<Block> mineralRuby = mineral(EnumMineralTier.RUBY);
    public static final RegistryObject<Block> mineralEmerald = mineral(EnumMineralTier.EMERALD);
    public static final RegistryObject<Block> mineralSapphire = mineral(EnumMineralTier.SAPPHIRE);

    public static final RegistryObject<Block> brokenMineralIron = brokenMineral(EnumMineralTier.IRON);
    public static final RegistryObject<Block> brokenMineralBronze = brokenMineral(EnumMineralTier.BRONZE);
    public static final RegistryObject<Block> brokenMineralSilver = brokenMineral(EnumMineralTier.SILVER);
    public static final RegistryObject<Block> brokenMineralGold = brokenMineral(EnumMineralTier.GOLD);
    public static final RegistryObject<Block> brokenMineralPlatinum = brokenMineral(EnumMineralTier.PLATINUM);
    public static final RegistryObject<Block> brokenMineralOrichalcum = brokenMineral(EnumMineralTier.ORICHALCUM);
    public static final RegistryObject<Block> brokenMineralDiamond = brokenMineral(EnumMineralTier.DIAMOND);
    public static final RegistryObject<Block> brokenMineralDragonic = brokenMineral(EnumMineralTier.DRAGONIC);
    public static final RegistryObject<Block> brokenMineralAquamarine = brokenMineral(EnumMineralTier.AQUAMARINE);
    public static final RegistryObject<Block> brokenMineralAmethyst = brokenMineral(EnumMineralTier.AMETHYST);
    public static final RegistryObject<Block> brokenMineralRuby = brokenMineral(EnumMineralTier.RUBY);
    public static final RegistryObject<Block> brokenMineralEmerald = brokenMineral(EnumMineralTier.EMERALD);
    public static final RegistryObject<Block> brokenMineralSapphire = brokenMineral(EnumMineralTier.SAPPHIRE);

    public static final RegistryObject<Block> bossSpawner = BLOCKS.register("boss_spawner", () -> new BlockBossSpawner(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(30, 9999)));
    public static final RegistryObject<Block> board = BLOCKS.register("black_board", () -> new Block(AbstractBlock.Properties.create(Material.WOOD).hardnessAndResistance(5, 5)));
    public static final RegistryObject<Block> shipping = BLOCKS.register("shipping_bin", () -> new BlockShippingBin(AbstractBlock.Properties.create(Material.WOOD).hardnessAndResistance(3, 10)));

    /*public static final RegistryObject<Block> hotSpring = new BlockHotSpring();

    //Crops*/
    public static final RegistryObject<Block> turnip = crop("turnip", () -> ModItems.turnip, () -> ModItems.turnipGiant, () -> ModItems.turnipSeeds);
    public static final RegistryObject<Block> turnipPink = crop("turnip_pink", () -> ModItems.turnipPink, () -> ModItems.turnipPinkGiant, () -> ModItems.turnipPinkSeeds);
    public static final RegistryObject<Block> cabbage = crop("cabbage", () -> ModItems.cabbage, () -> ModItems.cabbageGiant, () -> ModItems.cabbageSeeds);
    public static final RegistryObject<Block> pinkMelon = crop("pink_melon", () -> ModItems.pinkMelon, () -> ModItems.pinkMelonGiant, () -> ModItems.pinkMelonSeeds);
    public static final RegistryObject<Block> pineapple = crop("pineapple", () -> ModItems.pineapple, () -> ModItems.pineappleGiant, () -> ModItems.pineappleSeeds);
    public static final RegistryObject<Block> strawberry = crop("strawberry", () -> ModItems.strawberry, () -> ModItems.strawberryGiant, () -> ModItems.strawberrySeeds);
    public static final RegistryObject<Block> goldenTurnip = crop("golden_turnip", () -> ModItems.goldenTurnip, () -> ModItems.goldenTurnipGiant, () -> ModItems.goldTurnipSeeds);
    public static final RegistryObject<Block> goldenPotato = crop("golden_potato", () -> ModItems.goldenPotato, () -> ModItems.goldenPotatoGiant, () -> ModItems.goldPotatoSeeds);
    public static final RegistryObject<Block> goldenPumpkin = crop("golden_pumpkin", () -> ModItems.goldenPumpkin, () -> ModItems.goldenPumpkinGiant, () -> ModItems.goldPumpkinSeeds);
    public static final RegistryObject<Block> goldenCabbage = crop("golden_cabbage", () -> ModItems.goldenCabbage, () -> ModItems.goldenCabbageGiant, () -> ModItems.goldCabbageSeeds);
    public static final RegistryObject<Block> hotHotFruit = crop("hot_hot_fruit", () -> ModItems.hotHotFruit, () -> ModItems.hotHotFruitGiant, () -> ModItems.hotHotSeeds);
    public static final RegistryObject<Block> bokChoy = crop("bok_choy", () -> ModItems.bokChoy, () -> ModItems.bokChoyGiant, () -> ModItems.bokChoySeeds);
    public static final RegistryObject<Block> leek = crop("leek", () -> ModItems.leek, () -> ModItems.leekGiant, () -> ModItems.leekSeeds);
    public static final RegistryObject<Block> radish = crop("radish", () -> ModItems.radish, () -> ModItems.radishGiant, () -> ModItems.radishSeeds);
    public static final RegistryObject<Block> spinach = crop("spinach", () -> ModItems.spinach, () -> ModItems.spinachGiant, () -> ModItems.spinachSeeds);
    public static final RegistryObject<Block> greenPepper = crop("green_pepper", () -> ModItems.greenPepper, () -> ModItems.greenPepperGiant, () -> ModItems.greenPepperSeeds);
    public static final RegistryObject<Block> yam = crop("yam", () -> ModItems.yam, () -> ModItems.yamGiant, () -> ModItems.yamSeeds);
    public static final RegistryObject<Block> eggplant = crop("eggplant", () -> ModItems.eggplant, () -> ModItems.eggplantGiant, () -> ModItems.eggplantSeeds);
    public static final RegistryObject<Block> tomato = crop("tomato", () -> ModItems.tomato, () -> ModItems.tomatoGiant, () -> ModItems.tomatoSeeds);
    public static final RegistryObject<Block> corn = crop("corn", () -> ModItems.corn, () -> ModItems.cornGiant, () -> ModItems.cornSeeds);
    public static final RegistryObject<Block> cucumber = crop("cucumber", () -> ModItems.cucumber, () -> ModItems.cucumberGiant, () -> ModItems.cucumberSeeds);
    public static final RegistryObject<Block> pumpkin = crop("pumpkin", () -> ModItems.pumpkin, () -> ModItems.pumpkinGiant, () -> ModItems.pumpkinSeeds);
    public static final RegistryObject<Block> onion = crop("onion", () -> ModItems.onion, () -> ModItems.onionGiant, () -> ModItems.onionSeeds);
    public static final RegistryObject<Block> fodder = crop("fodder", () -> ModItems.fodder, () -> () -> Items.AIR, () -> ModItems.fodderSeeds);

    public static final RegistryObject<Block> shieldCrop = crop("shield", () -> ModItems.seedShield, () -> () -> Items.AIR, () -> ModItems.shieldSeeds);
    public static final RegistryObject<Block> swordCrop = crop("sword", () -> ModItems.seedSword, () -> () -> Items.AIR, () -> ModItems.swordSeeds);

    public static final RegistryObject<Block> dungeon = crop("dungeon", () -> () -> Items.STONE, () -> () -> Items.AIR, () -> ModItems.dungeonSeeds);

    public static final RegistryObject<Block> potatoGiant = crop("potato", () -> () -> Items.POTATO, () -> ModItems.potatoGiant, () -> () -> Items.POTATO);
    public static final RegistryObject<Block> carrotGiant = crop("carrot", () -> () -> Items.CARROT, () -> ModItems.carrotGiant, () -> () -> Items.CARROT);

    //Flowers
    public static final RegistryObject<Block> whiteCrystal = flower("white_crystal", () -> ModItems.whiteCrystal, () -> ModItems.whiteCrystalGiant, () -> ModItems.whiteCrystalSeeds);
    public static final RegistryObject<Block> redCrystal = flower("red_crystal", () -> ModItems.redCrystal, () -> ModItems.redCrystalGiant, () -> ModItems.redCrystalSeeds);
    public static final RegistryObject<Block> pomPomGrass = flower("pom_pom_grass", () -> ModItems.pomPomGrass, () -> ModItems.pomPomGrassGiant, () -> ModItems.pomPomGrassSeeds);
    public static final RegistryObject<Block> autumnGrass = flower("autumn_grass", () -> ModItems.autumnGrass, () -> ModItems.autumnGrassGiant, () -> ModItems.autumnGrassSeeds);
    public static final RegistryObject<Block> noelGrass = flower("noel_grass", () -> ModItems.noelGrass, () -> ModItems.noelGrassGiant, () -> ModItems.noelGrassSeeds);
    public static final RegistryObject<Block> greenCrystal = flower("green_crystal", () -> ModItems.greenCrystal, () -> ModItems.greenCrystalGiant, () -> ModItems.greenCrystalSeeds);
    public static final RegistryObject<Block> fireflower = flower("fireflower", () -> ModItems.fireflower, () -> ModItems.fireflowerGiant, () -> ModItems.fireflowerSeeds);
    public static final RegistryObject<Block> fourLeafClover = flower("4_leaf_clover", () -> ModItems.fourLeafClover, () -> ModItems.fourLeafCloverGiant, () -> ModItems.fourLeafCloverSeeds);
    public static final RegistryObject<Block> ironleaf = flower("ironleaf", () -> ModItems.ironleaf, () -> ModItems.ironleafGiant, () -> ModItems.ironleafSeeds);
    public static final RegistryObject<Block> emeryFlower = flower("emery_flower", () -> ModItems.emeryFlower, () -> ModItems.emeryFlowerGiant, () -> ModItems.emeryFlowerSeeds);
    public static final RegistryObject<Block> blueCrystal = flower("blue_crystal", () -> ModItems.blueCrystal, () -> ModItems.blueCrystalGiant, () -> ModItems.blueCrystalSeeds);
    public static final RegistryObject<Block> lampGrass = flower("lamp_grass", () -> ModItems.lampGrass, () -> ModItems.lampGrassGiant, () -> ModItems.lampGrassSeeds);
    public static final RegistryObject<Block> cherryGrass = flower("cherry_grass", () -> ModItems.cherryGrass, () -> ModItems.cherryGrassGiant, () -> ModItems.cherryGrassSeeds);
    public static final RegistryObject<Block> charmBlue = flower("charm_blue", () -> ModItems.charmBlue, () -> ModItems.charmBlueGiant, () -> ModItems.charmBlueSeeds);
    public static final RegistryObject<Block> pinkCat = flower("pink_cat", () -> ModItems.pinkCat, () -> ModItems.pinkCatGiant, () -> ModItems.pinkCatSeeds);
    public static final RegistryObject<Block> moondropFlower = flower("moondrop_flower", () -> ModItems.moondropFlower, () -> ModItems.moondropFlowerGiant, () -> ModItems.moondropSeeds);
    public static final RegistryObject<Block> toyherb = flower("toyherb", () -> ModItems.toyherb, () -> ModItems.toyherbGiant, () -> ModItems.toyherbSeeds);

    public static final RegistryObject<Block> mushroom = herb("mushroom");
    public static final RegistryObject<Block> monarchMushroom = herb("monarch_mushroom");
    public static final RegistryObject<Block> elliLeaves = herb("elli_leaves");
    public static final RegistryObject<Block> witheredGrass = herb("withered_grass");
    public static final RegistryObject<Block> weeds = herb("weeds");
    public static final RegistryObject<Block> whiteGrass = herb("white_grass");
    public static final RegistryObject<Block> indigoGrass = herb("indigo_grass");
    public static final RegistryObject<Block> purpleGrass = herb("purple_grass");
    public static final RegistryObject<Block> greenGrass = herb("green_grass");
    public static final RegistryObject<Block> blueGrass = herb("blue_grass");
    public static final RegistryObject<Block> yellowGrass = herb("yellow_grass");
    public static final RegistryObject<Block> redGrass = herb("red_grass");
    public static final RegistryObject<Block> orangeGrass = herb("orange_grass");
    public static final RegistryObject<Block> blackGrass = herb("black_grass");
    public static final RegistryObject<Block> antidoteGrass = herb("antidote_grass");
    public static final RegistryObject<Block> medicinalHerb = herb("medicinal_herb");
    public static final RegistryObject<Block> bambooSprout = herb("bamboo_sprout");

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
    public static final RegistryObject<TileEntityType<TileFarm>> farmTile = TILES.register("farmland_tile", () -> TileEntityType.Builder.create(TileFarm::new, farmland.get()).build(null));
    public static final RegistryObject<TileEntityType<TileCrop>> cropTile = cropTile("crop_tile", combine(Lists.newArrayList(crops), flowers));
    public static final RegistryObject<TileEntityType<TileAccessory>> accessoryTile = TILES.register("accessory_tile", () -> TileEntityType.Builder.create(TileAccessory::new, accessory.get()).build(null));
    public static final RegistryObject<TileEntityType<TileForge>> forgingTile = TILES.register("forge_tile", () -> TileEntityType.Builder.create(TileForge::new, forge.get()).build(null));
    public static final RegistryObject<TileEntityType<TileChemistry>> chemistryTile = TILES.register("chemistry_tile", () -> TileEntityType.Builder.create(TileChemistry::new, chemistry.get()).build(null));
    public static final RegistryObject<TileEntityType<TileCooking>> cookingTile = TILES.register("cooking_tile", () -> TileEntityType.Builder.create(TileCooking::new, cooking.get()).build(null));

    public static RegistryObject<Block> mineral(EnumMineralTier name) {
        RegistryObject<Block> reg = BLOCKS.register("ore_" + name.getString(), () -> new BlockMineral(name, AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(3, 5)));
        mineralMap.put(name, reg);
        return reg;
    }

    public static RegistryObject<Block> brokenMineral(EnumMineralTier name) {
        RegistryObject<Block> reg = BLOCKS.register("ore_broken_" + name.getString(), () -> new BlockBrokenMineral(name, AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(30, 15)));
        brokenMineralMap.put(name, reg);
        return reg;
    }

    public static RegistryObject<Block> crop(String name, Supplier<Supplier<Item>> crop, Supplier<Supplier<Item>> giant, Supplier<Supplier<Item>> seed) {
        RegistryObject<Block> reg = BLOCKS.register("plant_" + name, () -> new BlockCrop(AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().sound(SoundType.PLANT), crop.get(), giant.get(), seed.get()));
        crops.add(reg);
        return reg;
    }

    public static RegistryObject<Block> flower(String name, Supplier<Supplier<Item>> crop, Supplier<Supplier<Item>> giant, Supplier<Supplier<Item>> seed) {
        RegistryObject<Block> reg = BLOCKS.register("plant_" + name, () -> new BlockCrop(AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().sound(SoundType.PLANT), crop.get(), giant.get(), seed.get()));
        flowers.add(reg);
        return reg;
    }

    public static RegistryObject<Block> herb(String name) {
        RegistryObject<Block> reg = BLOCKS.register(name, () -> new BlockHerb(AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().sound(SoundType.PLANT)));
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

    public static <V extends TileEntity> RegistryObject<TileEntityType<V>> cropTile(String name, Collection<RegistryObject<Block>> blocks) {
        return TILES.register(name, () -> new TileEntityType(TileCrop::new, blocks.stream().map(obj -> obj.get()).collect(Collectors.toSet()), null));
    }
}
