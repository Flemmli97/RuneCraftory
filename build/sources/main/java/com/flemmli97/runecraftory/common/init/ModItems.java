package com.flemmli97.runecraftory.common.init;

import com.flemmli97.runecraftory.common.items.creative.ItemDebug;
import com.flemmli97.runecraftory.common.items.creative.ItemIcon;
import com.flemmli97.runecraftory.common.items.creative.ItemLevelUp;
import com.flemmli97.runecraftory.common.items.creative.ItemSkillUp;
import com.flemmli97.runecraftory.common.items.creative.ItemSpawnEgg;
import com.flemmli97.runecraftory.common.items.itemblocks.ItemBlockAccess;
import com.flemmli97.runecraftory.common.items.itemblocks.ItemBlockBase;
import com.flemmli97.runecraftory.common.items.itemblocks.ItemBlockCooking;
import com.flemmli97.runecraftory.common.items.itemblocks.ItemBlockForge;
import com.flemmli97.runecraftory.common.items.itemblocks.ItemBlockPharm;
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

@Mod.EventBusSubscriber(modid = LibReference.MODID)
public class ModItems {

	public static ToolMaterial mat = EnumHelper.addToolMaterial("runeCraftory_mat", 3, 0, 6, 0, 0);

	public static Item hoeScrap = new ItemToolHoe(EnumToolTier.SCRAP);
	public static Item hoeIron = new ItemToolHoe(EnumToolTier.IRON);
	public static Item hoeSilver = new ItemToolHoe(EnumToolTier.SILVER);
	public static Item hoeGold = new ItemToolHoe(EnumToolTier.GOLD);
	public static Item hoePlatinum = new ItemToolHoe(EnumToolTier.PLATINUM);
	public static Item wateringCanScrap = new ItemToolWateringCan(EnumToolTier.SCRAP);
	public static Item wateringCanIron = new ItemToolWateringCan(EnumToolTier.IRON);
	public static Item wateringCanSilver = new ItemToolWateringCan(EnumToolTier.SILVER);
	public static Item wateringCanGold = new ItemToolWateringCan(EnumToolTier.GOLD);
	public static Item wateringCanPlatinum = new ItemToolWateringCan(EnumToolTier.PLATINUM);
	public static Item sickleScrap = new ItemToolSickle(EnumToolTier.SCRAP);
	public static Item sickleIron = new ItemToolSickle(EnumToolTier.IRON);
	public static Item sickleSilver = new ItemToolSickle(EnumToolTier.SILVER);
	public static Item sickleGold = new ItemToolSickle(EnumToolTier.GOLD);
	public static Item sicklePlatinum = new ItemToolSickle(EnumToolTier.PLATINUM);
	public static Item hammerScrap = new ItemToolHammer(EnumToolTier.SCRAP);
	public static Item hammerIron = new ItemToolHammer(EnumToolTier.IRON);
	public static Item hammerSilver = new ItemToolHammer(EnumToolTier.SILVER);
	public static Item hammerGold = new ItemToolHammer(EnumToolTier.GOLD);
	public static Item hammerPlatinum = new ItemToolHammer(EnumToolTier.PLATINUM);
	public static Item inspector = new ItemToolPetInspector();
	
	public static Item broadSword=new ItemBroadSword();
	
	public static Item claymore = new ItemClaymore();
	
	public static Item spear = new ItemSpear();
	
	public static Item battleAxe = new ItemBattleAxe();
	public static Item battleScythe = new ItemBattleScythe();
	
	public static Item battleHammer = new ItemBattleHammer();
	
	public static Item dagger = new ItemShortDagger();
	
	public static Item leatherGlove = new ItemLeatherGlove();
	
	public static Item itemBlockForge = new ItemBlockForge();
	public static Item itemBlockAccess = new ItemBlockAccess();
	public static Item itemBlockCooking = new ItemBlockCooking();
	public static Item itemBlockPharm = new ItemBlockPharm();
	
	public static Item crystal = new ItemCrystals();
	public static Item jewel = new ItemJewels();
	public static Item mineral = new ItemMinerals();
	public static Item scrap = new ItemMineralScrap();
	public static Item sticks = new ItemSticks();
	public static Item liquids = new ItemLiquids();
	public static Item feathers = new ItemFeathers();
	public static Item bones = new ItemBones();
	public static Item stones = new ItemStones();
	public static Item strings = new ItemStrings();
	public static Item furs = new ItemFurs();
	public static Item powders =  new ItemPowders();
	public static Item cloth =  new ItemCloths();
	
	public static Item fireBallSmall = new ItemFireballCast();
	public static Item recipe = new ItemRecipe();
	public static Item icon = new ItemIcon();

	public static Item debug = new ItemDebug();
	public static Item level = new ItemLevelUp();
	public static Item skill = new ItemSkillUp();

	public static Item spawnEgg = new ItemSpawnEgg();

	@SubscribeEvent
	public static final void registerItems(RegistryEvent.Register<Item> event) {
	    event.getRegistry().register(hoeScrap);
	    event.getRegistry().register(hoeIron);
	    event.getRegistry().register(hoeSilver);
	    event.getRegistry().register(hoeGold);
	    event.getRegistry().register(hoePlatinum);
	    event.getRegistry().register(wateringCanScrap);
	    event.getRegistry().register(wateringCanIron);
	    event.getRegistry().register(wateringCanSilver);
	    event.getRegistry().register(wateringCanGold);
	    event.getRegistry().register(wateringCanPlatinum);
	    event.getRegistry().register(sickleScrap);
	    event.getRegistry().register(sickleIron);
	    event.getRegistry().register(sickleSilver);
	    event.getRegistry().register(sickleGold);
	    event.getRegistry().register(sicklePlatinum);
	    event.getRegistry().register(hammerScrap);
	    event.getRegistry().register(hammerIron);
	    event.getRegistry().register(hammerSilver);
	    event.getRegistry().register(hammerGold);
	    event.getRegistry().register(hammerPlatinum);
	    event.getRegistry().register(inspector);

	    event.getRegistry().register(broadSword);

	    event.getRegistry().register(claymore);

	    event.getRegistry().register(spear);
	    
	    event.getRegistry().register(battleAxe);
	    event.getRegistry().register(battleScythe);
	    
	    event.getRegistry().register(battleHammer);
	    
	    event.getRegistry().register(dagger);
	    
	    event.getRegistry().register(leatherGlove);
	    
	    event.getRegistry().register(crystal);
	    event.getRegistry().register(jewel);
	    event.getRegistry().register(mineral);
	    event.getRegistry().register(scrap);
	    event.getRegistry().register(sticks);
	    event.getRegistry().register(liquids);
	    event.getRegistry().register(feathers);
	    event.getRegistry().register(bones);
	    event.getRegistry().register(stones);
	    event.getRegistry().register(strings);
	    event.getRegistry().register(furs);
	    event.getRegistry().register(powders);
	    event.getRegistry().register(cloth);

	    event.getRegistry().register(fireBallSmall);
	    
	    event.getRegistry().register(recipe);
	    
	    event.getRegistry().register(itemBlockForge);
	    event.getRegistry().register(itemBlockAccess);
	    event.getRegistry().register(itemBlockCooking);
	    event.getRegistry().register(itemBlockPharm);
	    
	    event.getRegistry().register(spawnEgg);
	    event.getRegistry().register(icon);

	    event.getRegistry().register(debug);
	    event.getRegistry().register(level);
	    event.getRegistry().register(skill);
	}
	
	@SubscribeEvent
	public static void initModel(ModelRegistryEvent event)
	{
		((ItemToolHoe) hoeScrap).initModel();
		((ItemToolHoe) hoeIron).initModel();
		((ItemToolHoe) hoeSilver).initModel();
		((ItemToolHoe) hoeGold).initModel();
		((ItemToolHoe) hoePlatinum).initModel();
		((ItemToolWateringCan) wateringCanScrap).initModel();
		((ItemToolWateringCan) wateringCanIron).initModel();
		((ItemToolWateringCan) wateringCanSilver).initModel();
		((ItemToolWateringCan) wateringCanGold).initModel();
		((ItemToolWateringCan) wateringCanPlatinum).initModel();
		((ItemToolSickle) sickleScrap).initModel();
		((ItemToolSickle) sickleIron).initModel();
		((ItemToolSickle) sickleSilver).initModel();
		((ItemToolSickle) sickleGold).initModel();
		((ItemToolSickle) sicklePlatinum).initModel();
		((ItemToolHammer) hammerScrap).initModel();
		((ItemToolHammer) hammerIron).initModel();
		((ItemToolHammer) hammerSilver).initModel();
		((ItemToolHammer) hammerGold).initModel();
		((ItemToolHammer) hammerPlatinum).initModel();
		
	    ((ItemBroadSword) broadSword).initModel();

	    ((ItemClaymore) claymore).initModel();

	    ((ItemSpear) spear).initModel();
	    
	    ((ItemBattleAxe) battleAxe).initModel();
	    ((ItemBattleScythe) battleScythe).initModel();
	    
	    ((ItemBattleHammer) battleHammer).initModel();
	    
	    ((ItemShortDagger) dagger).initModel();
	    
	    ((ItemLeatherGlove) leatherGlove).initModel();
	    
		((ItemToolPetInspector) inspector).initModel();
		
		((ItemCrystals) crystal).initModel();
		((ItemJewels) jewel).initModel();
		((ItemMinerals) mineral).initModel();
		((ItemMineralScrap) scrap).initModel();
		((ItemSticks) sticks).initModel();
		((ItemLiquids) liquids).initModel();
		((ItemFeathers) feathers).initModel();
		((ItemBones)bones).initModel();
		((ItemStones)stones).initModel();
		((ItemStrings)strings).initModel();
		((ItemFurs) furs).initModel();
		((ItemPowders) powders).initModel();
		((ItemCloths) cloth).initModel();
				
		((ItemSpawnEgg) spawnEgg).initModel();
		((ItemIcon) icon).initModel();
		
		((ItemBlockBase) itemBlockAccess).initModel();
		((ItemBlockBase) itemBlockForge).initModel();
		((ItemBlockBase) itemBlockCooking).initModel();
		((ItemBlockBase) itemBlockPharm).initModel();
	}
	
	@SubscribeEvent
	public static void recipeRender(ModelBakeEvent event)
	{
		((ItemRecipe) recipe).initModel(event);
	}
}
