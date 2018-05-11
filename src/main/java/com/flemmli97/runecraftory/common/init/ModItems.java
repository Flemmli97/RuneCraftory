package com.flemmli97.runecraftory.common.init;

import com.flemmli97.runecraftory.common.items.IModelRegister;
import com.flemmli97.runecraftory.common.items.creative.ItemDebug;
import com.flemmli97.runecraftory.common.items.creative.ItemIcon;
import com.flemmli97.runecraftory.common.items.creative.ItemInstaTame;
import com.flemmli97.runecraftory.common.items.creative.ItemLevelUp;
import com.flemmli97.runecraftory.common.items.creative.ItemSkillUp;
import com.flemmli97.runecraftory.common.items.creative.ItemSpawnEgg;
import com.flemmli97.runecraftory.common.items.equipment.accessoire.ItemCheapBracelet;
import com.flemmli97.runecraftory.common.items.food.ItemCrops;
import com.flemmli97.runecraftory.common.items.itemblocks.ItemBlockAccess;
import com.flemmli97.runecraftory.common.items.itemblocks.ItemBlockCooking;
import com.flemmli97.runecraftory.common.items.itemblocks.ItemBlockForge;
import com.flemmli97.runecraftory.common.items.itemblocks.ItemBlockPharm;
import com.flemmli97.runecraftory.common.items.itemblocks.ItemCropSeed;
import com.flemmli97.runecraftory.common.items.misc.ItemBones;
import com.flemmli97.runecraftory.common.items.misc.ItemCloths;
import com.flemmli97.runecraftory.common.items.misc.ItemCrystals;
import com.flemmli97.runecraftory.common.items.misc.ItemFeathers;
import com.flemmli97.runecraftory.common.items.misc.ItemFurs;
import com.flemmli97.runecraftory.common.items.misc.ItemJewels;
import com.flemmli97.runecraftory.common.items.misc.ItemLiquids;
import com.flemmli97.runecraftory.common.items.misc.ItemMineralScrap;
import com.flemmli97.runecraftory.common.items.misc.ItemMinerals;
import com.flemmli97.runecraftory.common.items.misc.ItemPowders;
import com.flemmli97.runecraftory.common.items.misc.ItemRecipe;
import com.flemmli97.runecraftory.common.items.misc.ItemSticks;
import com.flemmli97.runecraftory.common.items.misc.ItemStones;
import com.flemmli97.runecraftory.common.items.misc.ItemStrings;
import com.flemmli97.runecraftory.common.items.special.spells.ItemFireballCast;
import com.flemmli97.runecraftory.common.items.tools.ItemToolHammer;
import com.flemmli97.runecraftory.common.items.tools.ItemToolHoe;
import com.flemmli97.runecraftory.common.items.tools.ItemToolPetInspector;
import com.flemmli97.runecraftory.common.items.tools.ItemToolSickle;
import com.flemmli97.runecraftory.common.items.tools.ItemToolWateringCan;
import com.flemmli97.runecraftory.common.items.weapons.dualblade.ItemShortDagger;
import com.flemmli97.runecraftory.common.items.weapons.fist.ItemLeatherGlove;
import com.flemmli97.runecraftory.common.items.weapons.haxe.ItemBattleAxe;
import com.flemmli97.runecraftory.common.items.weapons.haxe.ItemBattleHammer;
import com.flemmli97.runecraftory.common.items.weapons.haxe.ItemBattleScythe;
import com.flemmli97.runecraftory.common.items.weapons.longsword.ItemClaymore;
import com.flemmli97.runecraftory.common.items.weapons.shortsword.ItemBroadSword;
import com.flemmli97.runecraftory.common.items.weapons.spear.ItemSpear;
import com.flemmli97.runecraftory.common.lib.LibCropOreDictionary;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumToolTier;

import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = LibReference.MODID)
public class ModItems {

	public static final ToolMaterial mat = EnumHelper.addToolMaterial("runeCraftory_mat", 3, 0, 6, 0, 0);
	
	public static final Item hoeScrap = new ItemToolHoe(EnumToolTier.SCRAP);
	public static final Item hoeIron = new ItemToolHoe(EnumToolTier.IRON);
	public static final Item hoeSilver = new ItemToolHoe(EnumToolTier.SILVER);
	public static final Item hoeGold = new ItemToolHoe(EnumToolTier.GOLD);
	public static final Item hoePlatinum = new ItemToolHoe(EnumToolTier.PLATINUM);
	public static final Item wateringCanScrap = new ItemToolWateringCan(EnumToolTier.SCRAP);
	public static final Item wateringCanIron = new ItemToolWateringCan(EnumToolTier.IRON);
	public static final Item wateringCanSilver = new ItemToolWateringCan(EnumToolTier.SILVER);
	public static final Item wateringCanGold = new ItemToolWateringCan(EnumToolTier.GOLD);
	public static final Item wateringCanPlatinum = new ItemToolWateringCan(EnumToolTier.PLATINUM);
	public static final Item sickleScrap = new ItemToolSickle(EnumToolTier.SCRAP);
	public static final Item sickleIron = new ItemToolSickle(EnumToolTier.IRON);
	public static final Item sickleSilver = new ItemToolSickle(EnumToolTier.SILVER);
	public static final Item sickleGold = new ItemToolSickle(EnumToolTier.GOLD);
	public static final Item sicklePlatinum = new ItemToolSickle(EnumToolTier.PLATINUM);
	public static final Item hammerScrap = new ItemToolHammer(EnumToolTier.SCRAP);
	public static final Item hammerIron = new ItemToolHammer(EnumToolTier.IRON);
	public static final Item hammerSilver = new ItemToolHammer(EnumToolTier.SILVER);
	public static final Item hammerGold = new ItemToolHammer(EnumToolTier.GOLD);
	public static final Item hammerPlatinum = new ItemToolHammer(EnumToolTier.PLATINUM);
	public static final Item inspector = new ItemToolPetInspector();
	
	public static final Item[] TOOLS = new Item[] {hoeScrap, hoeIron, hoeSilver, hoeGold, hoePlatinum, 
			wateringCanScrap, wateringCanIron, wateringCanSilver, wateringCanGold, wateringCanPlatinum, 
			sickleScrap, sickleIron, sickleSilver, sickleGold, sicklePlatinum,
			hammerScrap, hammerIron, hammerSilver, hammerGold, hammerPlatinum, inspector};
	
	public static final Item broadSword=new ItemBroadSword();
	
	public static final Item claymore = new ItemClaymore();
	
	public static final Item spear = new ItemSpear();
	
	public static final Item battleAxe = new ItemBattleAxe();
	public static final Item battleScythe = new ItemBattleScythe();
	
	public static final Item battleHammer = new ItemBattleHammer();
	
	public static final Item dagger = new ItemShortDagger();
	
	public static final Item leatherGlove = new ItemLeatherGlove();
	
	public static final Item[] WEAPONS = new Item[] {
			broadSword,
			claymore,
			spear,
			battleAxe, battleScythe,
			battleHammer,
			dagger,
			leatherGlove};
	
	public static final Item cheapBracelet = new ItemCheapBracelet();
	
	public static final Item[] ARMOR = new Item[] {cheapBracelet};
	
	public static final Item itemBlockForge = new ItemBlockForge();
	public static final Item itemBlockAccess = new ItemBlockAccess();
	public static final Item itemBlockCooking = new ItemBlockCooking();
	public static final Item itemBlockPharm = new ItemBlockPharm();
	
	public static final Item[] ITEMBLOCKS = new Item[] {itemBlockForge, itemBlockAccess, itemBlockCooking, itemBlockPharm};
	
	public static final Item crystal = new ItemCrystals();
	public static final Item jewel = new ItemJewels();
	public static final Item mineral = new ItemMinerals();
	public static final Item scrap = new ItemMineralScrap();
	public static final Item sticks = new ItemSticks();
	public static final Item liquids = new ItemLiquids();
	public static final Item feathers = new ItemFeathers();
	public static final Item bones = new ItemBones();
	public static final Item stones = new ItemStones();
	public static final Item strings = new ItemStrings();
	public static final Item furs = new ItemFurs();
	public static final Item powders =  new ItemPowders();
	public static final Item cloth =  new ItemCloths();
	
	public static final Item[] MATERIALS = new Item[] {crystal, jewel, mineral, scrap, sticks, liquids, feathers, bones, stones, strings, furs, powders, cloth};
	
	public static final Item fireBallSmall = new ItemFireballCast();
	
	public static final Item[] SPELLS = new Item[] {fireBallSmall};
	
	public static final Item recipe = new ItemRecipe();
	
	public static final Item icon = new ItemIcon();
	public static final Item debug = new ItemDebug();
	public static final Item level = new ItemLevelUp();
	public static final Item skill = new ItemSkillUp();
	public static final Item tame = new ItemInstaTame();
	public static final Item spawnEgg = new ItemSpawnEgg();
	
	public static final Item[] CREATIVE = new Item[] {icon, debug, level, skill, tame, spawnEgg};

	//Crop items
	public static final Item turnip = new ItemCrops("turnip", 10, 5, LibCropOreDictionary.TURNIP);
	public static final Item turnipSeeds = new ItemCropSeed("turnip", LibCropOreDictionary.TURNIP);
	
	public static final Item turnipPink = new ItemCrops("turnip_pink", 15, 7, LibCropOreDictionary.PINKTURNIP);
	public static final Item turnipPinkSeeds = new ItemCropSeed("turnip_pink", LibCropOreDictionary.PINKTURNIP);
	
	public static final Item cabbage = new ItemCrops("cabbage", 22, 0, LibCropOreDictionary.CABBAGE);
	public static final Item cabbageSeeds = new ItemCropSeed("cabbage", LibCropOreDictionary.CABBAGE);
	
	public static final Item pinkMelon = new ItemCrops("pink_melon", 11, 14, LibCropOreDictionary.PINKMELON);
	public static final Item pinkMelonSeeds = new ItemCropSeed("pink_melon", LibCropOreDictionary.PINKMELON);
	
	public static final Item[] CROPS = new Item[] {turnip, turnipPink, cabbage, pinkMelon};
	public static final Item[] CROPSEEDS = new Item[] {turnipSeeds, turnipPinkSeeds, cabbageSeeds, pinkMelonSeeds};

	@SubscribeEvent
	public static final void registerItems(RegistryEvent.Register<Item> event) {
		for(Item item : TOOLS)
			event.getRegistry().register(item);
	    for(Item item : WEAPONS)
	    	event.getRegistry().register(item);
	    for(Item item : ARMOR)
	    	event.getRegistry().register(item);
	    for(Item item : ITEMBLOCKS)
	    	event.getRegistry().register(item);
	    for(Item item : MATERIALS)
	    	event.getRegistry().register(item);
	    for(Item item : SPELLS)
	    	event.getRegistry().register(item);
	    for(Item item : CROPS)
	    	event.getRegistry().register(item);
	    for(Item item : CROPSEEDS)
	    	event.getRegistry().register(item);
	    for(Item item : CREATIVE)
	    	event.getRegistry().register(item);
	    
	    event.getRegistry().register(recipe);
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static final void initModel(ModelRegistryEvent event)
	{
		for(Item item : TOOLS)
			((IModelRegister)item).initModel();
		for(Item item : WEAPONS)
			((IModelRegister)item).initModel();
		for(Item item : ARMOR)
			((IModelRegister)item).initModel();
		for(Item item : ITEMBLOCKS)
			((IModelRegister)item).initModel();
		for(Item item : MATERIALS)
			((IModelRegister)item).initModel();
		for(Item item : SPELLS)
			((IModelRegister)item).initModel();
		for(Item item : CREATIVE)
			((IModelRegister)item).initModel();
	    for(Item item : CROPS)
			((IModelRegister)item).initModel();
	    for(Item item : CROPSEEDS)
			((IModelRegister)item).initModel();

	}
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static final void recipeRender(ModelBakeEvent event)
	{
		((ItemRecipe) recipe).initModel(event);
	}
}
