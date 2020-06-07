package com.flemmli97.runecraftory.common.init;

import com.flemmli97.runecraftory.common.blocks.*;
import com.flemmli97.runecraftory.common.blocks.crops.*;
import com.flemmli97.runecraftory.common.blocks.tile.*;
import com.flemmli97.runecraftory.common.fluids.blocks.BlockHotSpring;
import com.flemmli97.runecraftory.common.lib.LibOreDictionary;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumMineralTier;
import com.flemmli97.tenshilib.common.ItemBlockInitUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = LibReference.MODID)
public class ModBlocks {
	
	public static final Block forge = new BlockForge();
	public static final Block cooking = new BlockCookingBench();
	public static final Block pharm = new BlockPharmacy();
	public static final Block accessory = new BlockAccessoryCrafter();
	public static final Block farmland = new BlockRFFarmland();
	
	public static final Block mineralIron = new BlockMineral(EnumMineralTier.IRON);
	public static final Block mineralBronze = new BlockMineral(EnumMineralTier.BRONZE);
	public static final Block mineralSilver = new BlockMineral(EnumMineralTier.SILVER);
	public static final Block mineralGold = new BlockMineral(EnumMineralTier.GOLD);
	public static final Block mineralPlatinum = new BlockMineral(EnumMineralTier.PLATINUM);
	public static final Block mineralOrichalcum = new BlockMineral(EnumMineralTier.ORICHALCUM);
	public static final Block mineralDiamond = new BlockMineral(EnumMineralTier.DIAMOND);
	public static final Block mineralDragonic = new BlockMineral(EnumMineralTier.DRAGONIC);
	public static final Block mineralAquamarine = new BlockMineral(EnumMineralTier.AQUAMARINE);
	public static final Block mineralAmethyst = new BlockMineral(EnumMineralTier.AMETHYST);
	public static final Block mineralRuby = new BlockMineral(EnumMineralTier.RUBY);
	public static final Block mineralEmerald = new BlockMineral(EnumMineralTier.EMERALD);
	public static final Block mineralSapphire = new BlockMineral(EnumMineralTier.SAPPHIRE);

	public static final Block brokenMineralIron = new BlockBrokenMineral(EnumMineralTier.IRON);
	public static final Block brokenMineralBronze = new BlockBrokenMineral(EnumMineralTier.BRONZE);
	public static final Block brokenMineralSilver = new BlockBrokenMineral(EnumMineralTier.SILVER);
	public static final Block brokenMineralGold = new BlockBrokenMineral(EnumMineralTier.GOLD);
	public static final Block brokenMineralPlatinum = new BlockBrokenMineral(EnumMineralTier.PLATINUM);
	public static final Block brokenMineralOrichalcum = new BlockBrokenMineral(EnumMineralTier.ORICHALCUM);
	public static final Block brokenMineralDiamond = new BlockBrokenMineral(EnumMineralTier.DIAMOND);
	public static final Block brokenMineralDragonic = new BlockBrokenMineral(EnumMineralTier.DRAGONIC);
	public static final Block brokenMineralAquamarine = new BlockBrokenMineral(EnumMineralTier.AQUAMARINE);
	public static final Block brokenMineralAmethyst = new BlockBrokenMineral(EnumMineralTier.AMETHYST);
	public static final Block brokenMineralRuby = new BlockBrokenMineral(EnumMineralTier.RUBY);
	public static final Block brokenMineralEmerald = new BlockBrokenMineral(EnumMineralTier.EMERALD);
	public static final Block brokenMineralSapphire = new BlockBrokenMineral(EnumMineralTier.SAPPHIRE);

	public static final Block bossSpawner = new BlockBossSpawner();
	public static final Block research = new BlockResearchTable();
	public static final Block ignore = new BlockIgnore();
	public static final Block board = new BlockRequestBoard();
	public static final Block shipping = new BlockShippingBin();

	public static final Block hotSpring = new BlockHotSpring();

	//Crops
	public static final Block turnip = new BlockCropBase("turnip", LibOreDictionary.TURNIP);
	public static final Block turnipPink = new BlockCropBase("turnip_pink", LibOreDictionary.PINKTURNIP);
	public static final Block cabbage = new BlockCropBase("cabbage", LibOreDictionary.CABBAGE);
	public static final Block pinkMelon = new BlockCropBase("pink_melon", LibOreDictionary.PINKMELON);
	public static final Block pineapple = new BlockCropBase("pineapple", LibOreDictionary.PINEAPPLE);
	public static final Block strawberry = new BlockCropBase("strawberry", LibOreDictionary.STRAWBERRY);
	public static final Block goldenTurnip = new BlockCropBase("golden_turnip", LibOreDictionary.GOLDENTURNIP);
	public static final Block goldenPotato = new BlockCropBase("golden_potato", LibOreDictionary.GOLDENPOTATO);
	public static final Block goldenPumpkin = new BlockCropBase("golden_pumpkin", LibOreDictionary.GOLDENPUMPKIN);
	public static final Block goldenCabbage = new BlockCropBase("golden_cabbage", LibOreDictionary.GOLDENCABBAGE);
	public static final Block hotHotFruit = new BlockCropBase("hot-hot_fruit", LibOreDictionary.HOTHOTFRUIT);
	public static final Block bokChoy = new BlockCropBase("bok_choy", LibOreDictionary.BOKCHOY);
	public static final Block leek = new BlockCropBase("leek", LibOreDictionary.LEEK);
	public static final Block radish = new BlockCropBase("radish", LibOreDictionary.RADISH);
	public static final Block spinach = new BlockCropBase("spinach", LibOreDictionary.SPINACH);
	public static final Block greenPepper = new BlockCropBase("green_pepper", LibOreDictionary.GREENPEPPER);
	public static final Block yam = new BlockCropBase("yam", LibOreDictionary.YAM);
	public static final Block eggplant = new BlockCropBase("eggplant", LibOreDictionary.EGGPLANT);
	public static final Block tomato = new BlockCropBase("tomato", LibOreDictionary.TOMATO);
	public static final Block corn = new BlockCropBase("corn", LibOreDictionary.CORN);
	public static final Block cucumber = new BlockCropBase("cucumber", LibOreDictionary.CUCUMBER);
	public static final Block pumpkin = new BlockCropBase("pumpkin", LibOreDictionary.PUMPKIN);
	public static final Block onion = new BlockCropBase("onion", LibOreDictionary.ONION);
	public static final Block fodder = new BlockCropBase("fodder", LibOreDictionary.FODDER);

	public static final Block potatoGiant = new BlockCropBase("potato", LibOreDictionary.POTATO);
	public static final Block carrotGiant = new BlockCropBase("carrot", LibOreDictionary.CARROT);

	//Flowers
	public static final Block whiteCrystal = new BlockCropBase("white_crystal", LibOreDictionary.WHITECRYSTAL);
	public static final Block redCrystal = new BlockCropBase("red_crystal", LibOreDictionary.REDCRYSTAL);
	public static final Block pomPomGrass = new BlockCropBase("pom-pom_grass", LibOreDictionary.POMPOMGRASS);
	public static final Block autumnGrass = new BlockCropBase("autumn_grass", LibOreDictionary.AUTUMNGRASS);
	public static final Block noelGrass = new BlockCropBase("noel_grass", LibOreDictionary.NOELGRASS);
	public static final Block greenCrystal = new BlockCropBase("green_crystal", LibOreDictionary.GREENCRYSTAL);
	public static final Block fireflower = new BlockCropBase("fireflower", LibOreDictionary.FIREFLOWER);
	public static final Block fourLeafClover = new BlockCropBase("4-leaf_clover", LibOreDictionary.FOURLEAFCLOVER);
	public static final Block ironleaf = new BlockCropBase("ironleaf", LibOreDictionary.IRONLEAF);
	public static final Block emeryFlower = new BlockCropBase("emery_flower", LibOreDictionary.EMERYFLOWER);
	public static final Block blueCrystal = new BlockCropBase("blue_crystal", LibOreDictionary.BLUECRYSTAL);
	public static final Block lampGrass = new BlockCropBase("lamp_grass", LibOreDictionary.LAMPGRASS);
	public static final Block cherryGrass = new BlockCropBase("cherry_grass", LibOreDictionary.CHERRYGRASS);
	public static final Block charmBlue = new BlockCropBase("charm_blue", LibOreDictionary.CHARMBLUE);
	public static final Block pinkCat = new BlockCropBase("pink_cat", LibOreDictionary.PINKCAT);
	public static final Block moondropFlower = new BlockCropBase("moondrop_flower", LibOreDictionary.MOONDROPFLOWER);
	public static final Block toyherb = new BlockCropBase("toyherb", LibOreDictionary.TOYHERB);
	
	public static final Block mushroom = new BlockHerb("mushroom", LibOreDictionary.MUSHROOM);
	public static final Block monarchMushroom = new BlockHerb("monarch_mushroom", LibOreDictionary.MONARCHMUSHROOM);
	public static final Block elliLeaves = new BlockHerb("elli_leaves", LibOreDictionary.ELLILEAVES);
	public static final Block witheredGrass = new BlockHerb("withered_grass", LibOreDictionary.WITHEREDGRASS);
	public static final Block weeds = new BlockHerb("weeds", LibOreDictionary.WEEDS);
	public static final Block whiteGrass = new BlockHerb("white_grass", LibOreDictionary.WHITEGRASS);
	public static final Block indigoGrass = new BlockHerb("indigo_grass", LibOreDictionary.INDIGOGRASS);
	public static final Block purpleGrass = new BlockHerb("purple_grass", LibOreDictionary.PURPLEGRASS);
	public static final Block greenGrass = new BlockHerb("green_grass", LibOreDictionary.GREENGRASS);
	public static final Block blueGrass = new BlockHerb("blue_grass", LibOreDictionary.BLUEGRASS);
	public static final Block yellowGrass = new BlockHerb("yellow_grass", LibOreDictionary.YELLOWGRASS);
	public static final Block redGrass = new BlockHerb("red_grass", LibOreDictionary.REDGRASS);
	public static final Block orangeGrass = new BlockHerb("orange_grass", LibOreDictionary.ORANGEGRASS);
	public static final Block blackGrass = new BlockHerb("black_grass", LibOreDictionary.BLACKGRASS);
	public static final Block antidoteGrass = new BlockHerb("antidote_grass", LibOreDictionary.ANTIDOTEGRASS);
	public static final Block medicinalHerb = new BlockHerb("medicinal_herb", LibOreDictionary.MEDICINALHERB);
	public static final Block bambooSprout = new BlockBambooSprout("bamboo_sprout", LibOreDictionary.BAMBOOSPROUT);

	//Trees

	public static final Block appleTree = new BlockTreeBase("apple_tree_base");
	public static final Block appleWood = new BlockTreeWood("apple_wood");
	public static final Block appleLeaves = new BlockTreeLeaves("apple_leaves");
	public static final Block apple = new BlockTreeFruit("apple_block");
	public static final Block appleSapling = new BlockTreeSapling("apple_sapling", appleTree);
	public static final Block orangeTree = new BlockTreeBase("orange_tree_base");
	public static final Block orangeWood = new BlockTreeWood("orange_wood");
	public static final Block orangeLeaves = new BlockTreeLeaves("orange_leaves");
	public static final Block orange = new BlockTreeFruit("orange_block");
	public static final Block orangeSapling = new BlockTreeSapling("orange_sapling", orangeTree);
	public static final Block grapeTree = new BlockTreeBase("grape_tree_base");
	public static final Block grapeWood = new BlockTreeWood("grape_wood");
	public static final Block grapeLeaves = new BlockTreeLeaves("grape_leaves");
	public static final Block grape = new BlockTreeFruit("grape_block");
	public static final Block grapeSapling = new BlockTreeSapling("grape_sapling", grapeTree);
	public static final Block shinyTree = new BlockTreeBase("shiny_tree_base");
	public static final Block shinyWood = new BlockTreeWood("shiny_wood");
	public static final Block shinyLeaves = new BlockTreeLeaves("shiny_leaves");
	public static final Block shinySapling = new BlockTreeSapling("shiny_sapling", shinyTree);

	public static final Block[] MINERALBLOCKS = new Block[] 
			{ mineralIron, mineralBronze, mineralSilver, mineralGold, mineralPlatinum, mineralOrichalcum, mineralDiamond, mineralDragonic, mineralAquamarine, mineralAmethyst, mineralRuby, mineralEmerald, mineralSapphire,
			brokenMineralIron, brokenMineralBronze, brokenMineralSilver, brokenMineralGold, brokenMineralPlatinum, brokenMineralOrichalcum, brokenMineralDiamond, brokenMineralDragonic, brokenMineralAquamarine, brokenMineralAmethyst, brokenMineralRuby, brokenMineralEmerald, brokenMineralSapphire};

	//Grass, Herbs, Elli Leaves, Ayngondaia Lawn
	
	public static final Block[] CROPS = new Block[] {
			turnip, turnipPink, cabbage, pinkMelon, pineapple, strawberry, goldenTurnip, goldenPotato, goldenPumpkin, goldenCabbage, hotHotFruit, bokChoy, leek, radish, spinach, greenPepper, yam, eggplant, tomato, corn, cucumber, pumpkin, onion, fodder
	};
	
	public static final Block[] FLOWERS = new Block[] {
			whiteCrystal, redCrystal, pomPomGrass, autumnGrass, noelGrass, greenCrystal, fireflower, fourLeafClover, ironleaf, emeryFlower, blueCrystal, lampGrass, cherryGrass, charmBlue, pinkCat, moondropFlower, toyherb
	};
	
	public static final Block[] HERBS = new Block[] {
			mushroom, monarchMushroom, elliLeaves, witheredGrass, weeds, whiteGrass, indigoGrass, purpleGrass, greenGrass, blueGrass, yellowGrass, redGrass, orangeGrass, blackGrass, antidoteGrass, medicinalHerb, bambooSprout
	};
	
	public static final Block[] TREE = new Block[] {
			appleTree, appleWood, appleLeaves, apple, appleSapling,
			orangeTree, orangeWood, orangeLeaves, orange, orangeSapling,
			grapeTree, grapeWood, grapeLeaves, grape, grapeSapling,
			shinyTree, shinyWood, shinyLeaves, shinySapling			
	};
	
	public static final Block[] CRAFTING = new Block[] {
			forge, accessory, cooking, pharm
	};

	public static final Block[] OTHER = new Block[] {
			bossSpawner, farmland, ignore, board, shipping, research
	};
	//Trees
	
	@SubscribeEvent
	public static final void registerBlocks(RegistryEvent.Register<Block> event) {
	    for(Block block : CRAFTING)
	    	event.getRegistry().register(block);
	    for(Block block : MINERALBLOCKS)
	    	event.getRegistry().register(block);
	    for(Block block : CROPS)
	    	event.getRegistry().register(block);
	    for(Block block : FLOWERS)
	    	event.getRegistry().register(block);
	    for(Block block : HERBS)
	    	event.getRegistry().register(block);
	    for(Block block : TREE)
	    	event.getRegistry().register(block);
	    for(Block block : OTHER)
	    	event.getRegistry().register(block);

        event.getRegistry().register(hotSpring);

  		event.getRegistry().getValue(new ResourceLocation("minecraft:farmland")).setTickRandomly(false);
  		registerTileEntities();
	}
	
	private static final void registerTileEntities()
	{
  		GameRegistry.registerTileEntity(TileBrokenOre.class, new ResourceLocation(LibReference.MODID,"tile_broken_ore"));
  		GameRegistry.registerTileEntity(TileFarmland.class, new ResourceLocation(LibReference.MODID,"tile_farmland"));
  		GameRegistry.registerTileEntity(TileCrop.class, new ResourceLocation(LibReference.MODID,"tile_crop"));
  		GameRegistry.registerTileEntity(TileBossSpawner.class, new ResourceLocation(LibReference.MODID,"tile_boss_spawner"));
  		GameRegistry.registerTileEntity(TileForge.class, new ResourceLocation(LibReference.MODID,"tile_forge"));
  		GameRegistry.registerTileEntity(TileAccessory.class, new ResourceLocation(LibReference.MODID,"tile_access"));
  		GameRegistry.registerTileEntity(TileChem.class, new ResourceLocation(LibReference.MODID,"tile_chemitry"));
  		GameRegistry.registerTileEntity(TileCooking.class, new ResourceLocation(LibReference.MODID,"tile_cooking"));
  		GameRegistry.registerTileEntity(TileResearchTable.class, new ResourceLocation(LibReference.MODID,"tile_research"));
	}
	
	@SubscribeEvent
    public static final void registerItemBlocks(RegistryEvent.Register<Item> event) {
	    for(Block block : MINERALBLOCKS)
	    	event.getRegistry().register(ItemBlockInitUtils.itemFromBlock(block));
	    for(Block block : OTHER)
	    	event.getRegistry().register(ItemBlockInitUtils.itemFromBlock(block));
	    for(Block block : TREE)
	    	event.getRegistry().register(ItemBlockInitUtils.itemFromBlock(block));
	}

	@SubscribeEvent
	@SideOnly(value = Side.CLIENT)
	public static final void initModel(ModelRegistryEvent event)
	{
		ItemBlockInitUtils.registerSpecificModel(farmland, Blocks.FARMLAND.getRegistryName());
		ItemBlockInitUtils.registerSpecificModel(bossSpawner, Blocks.MOB_SPAWNER.getRegistryName());
		ItemBlockInitUtils.registerSpecificModel(ignore, Blocks.END_ROD.getRegistryName());
		for(Block block : MINERALBLOCKS)
			ItemBlockInitUtils.registerDefaultModel(block);
		for(Block block : TREE)
			ItemBlockInitUtils.registerDefaultModel(block);
		ItemBlockInitUtils.registerDefaultModel(research);
		ItemBlockInitUtils.registerDefaultModel(board);
		ItemBlockInitUtils.registerDefaultModel(shipping);
		for(Block block : HERBS)
		    if(block instanceof BlockHerb)
		        ModelLoader.setCustomStateMapper(block, (new StateMap.Builder()).ignore(BlockHerb.LEVEL).build());
	}
}
