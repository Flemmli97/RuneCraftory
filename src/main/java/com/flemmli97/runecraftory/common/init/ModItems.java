package com.flemmli97.runecraftory.common.init;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.mappings.CropMap;
import com.flemmli97.runecraftory.client.render.item.BakedItemRecipeModel;
import com.flemmli97.runecraftory.common.items.consumables.ItemCrops;
import com.flemmli97.runecraftory.common.items.consumables.ItemFishBase;
import com.flemmli97.runecraftory.common.items.consumables.ItemGenericConsumable;
import com.flemmli97.runecraftory.common.items.consumables.ItemHerb;
import com.flemmli97.runecraftory.common.items.consumables.ItemMedicine;
import com.flemmli97.runecraftory.common.items.consumables.ItemMushroom;
import com.flemmli97.runecraftory.common.items.consumables.ItemStatIncrease;
import com.flemmli97.runecraftory.common.items.consumables.ItemStatIncrease.Stat;
import com.flemmli97.runecraftory.common.items.creative.ItemDebug;
import com.flemmli97.runecraftory.common.items.creative.ItemEntityLevelUp;
import com.flemmli97.runecraftory.common.items.creative.ItemIcon;
import com.flemmli97.runecraftory.common.items.creative.ItemInstaTame;
import com.flemmli97.runecraftory.common.items.creative.ItemLevelUp;
import com.flemmli97.runecraftory.common.items.creative.ItemSkillUp;
import com.flemmli97.runecraftory.common.items.creative.ItemSpawnEgg;
import com.flemmli97.runecraftory.common.items.equipment.ItemAccessoireBase;
import com.flemmli97.runecraftory.common.items.equipment.ItemArmorBase;
import com.flemmli97.runecraftory.common.items.equipment.ItemHeadBase;
import com.flemmli97.runecraftory.common.items.equipment.ItemSeedShield;
import com.flemmli97.runecraftory.common.items.equipment.ItemShieldBase;
import com.flemmli97.runecraftory.common.items.equipment.ItemShoeBase;
import com.flemmli97.runecraftory.common.items.itemblocks.ItemBlockAccess;
import com.flemmli97.runecraftory.common.items.itemblocks.ItemBlockCooking;
import com.flemmli97.runecraftory.common.items.itemblocks.ItemBlockForge;
import com.flemmli97.runecraftory.common.items.itemblocks.ItemBlockPharm;
import com.flemmli97.runecraftory.common.items.itemblocks.ItemCropSeed;
import com.flemmli97.runecraftory.common.items.itemblocks.ItemDungeonSeed;
import com.flemmli97.runecraftory.common.items.misc.ItemFormular;
import com.flemmli97.runecraftory.common.items.misc.ItemGreenifier;
import com.flemmli97.runecraftory.common.items.misc.ItemMaterial;
import com.flemmli97.runecraftory.common.items.misc.ItemRecipe;
import com.flemmli97.runecraftory.common.items.misc.ItemSizeFertilizer;
import com.flemmli97.runecraftory.common.items.misc.ItemWettablePowder;
import com.flemmli97.runecraftory.common.items.special.spells.ItemEmptySkill;
import com.flemmli97.runecraftory.common.items.special.spells.ItemFireballCast;
import com.flemmli97.runecraftory.common.items.special.spells.ItemWaterLaserCast;
import com.flemmli97.runecraftory.common.items.tools.ItemToolAxe;
import com.flemmli97.runecraftory.common.items.tools.ItemToolBrush;
import com.flemmli97.runecraftory.common.items.tools.ItemToolFishingRod;
import com.flemmli97.runecraftory.common.items.tools.ItemToolGlass;
import com.flemmli97.runecraftory.common.items.tools.ItemToolHammer;
import com.flemmli97.runecraftory.common.items.tools.ItemToolHoe;
import com.flemmli97.runecraftory.common.items.tools.ItemToolPetInspector;
import com.flemmli97.runecraftory.common.items.tools.ItemToolSickle;
import com.flemmli97.runecraftory.common.items.tools.ItemToolWateringCan;
import com.flemmli97.runecraftory.common.items.weapons.ItemAxeBase;
import com.flemmli97.runecraftory.common.items.weapons.ItemDualBladeBase;
import com.flemmli97.runecraftory.common.items.weapons.ItemGloveBase;
import com.flemmli97.runecraftory.common.items.weapons.ItemHammerBase;
import com.flemmli97.runecraftory.common.items.weapons.ItemLongSwordBase;
import com.flemmli97.runecraftory.common.items.weapons.ItemShortSwordBase;
import com.flemmli97.runecraftory.common.items.weapons.ItemSpearBase;
import com.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import com.flemmli97.runecraftory.common.items.weapons.shortsword.ItemSeedSword;
import com.flemmli97.runecraftory.common.lib.LibOreDictionary;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.lib.enums.EnumToolTier;
import com.flemmli97.tenshilib.common.javahelper.ArrayUtils;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



@Mod.EventBusSubscriber(modid = LibReference.MODID)
public class ModItems {

	public static final ToolMaterial mat = EnumHelper.addToolMaterial("runeCraftory_mat", 3, 0, 6, 0, 0);
    public static ItemArmor.ArmorMaterial chain = EnumHelper.addArmorMaterial("runeCraftory_armorMat", "", 0, new int[] { 0, 0, 0, 0 }, 0, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 0.0f);

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
	public static final Item axeScrap = new ItemToolAxe(EnumToolTier.SCRAP);
	public static final Item axeIron = new ItemToolAxe(EnumToolTier.IRON);
	public static final Item axeSilver = new ItemToolAxe(EnumToolTier.SILVER);
	public static final Item axeGold = new ItemToolAxe(EnumToolTier.GOLD);
	public static final Item axePlatinum = new ItemToolAxe(EnumToolTier.PLATINUM);
	public static final Item fishingRodScrap = new ItemToolFishingRod(EnumToolTier.SCRAP);
	public static final Item fishingRodIron = new ItemToolFishingRod(EnumToolTier.IRON);
	public static final Item fishingRodSilver = new ItemToolFishingRod(EnumToolTier.SILVER);
	public static final Item fishingRodGold = new ItemToolFishingRod(EnumToolTier.GOLD);
	public static final Item fishingRodPlatinum = new ItemToolFishingRod(EnumToolTier.PLATINUM);
	public static final Item inspector = new ItemToolPetInspector();
	public static final Item brush = new ItemToolBrush();
	public static final Item glass = new ItemToolGlass();

	public static final Item seedSword = new ItemSeedSword();
	public static final Item broadSword = new ItemShortSwordBase("broad_sword");
	public static final Item steelSword = new ItemShortSwordBase("steel_sword");
	public static final Item steelSwordPlus = new ItemShortSwordBase("steel_sword_plus");
	public static final Item cutlass = new ItemShortSwordBase("cutlass");
	public static final Item aquaSword = new ItemShortSwordBase("aqua_sword");
	public static final Item invisBlade = new ItemShortSwordBase("invis_blade");
	public static final Item defender = new ItemShortSwordBase("defender");
	public static final Item burningSword = new ItemShortSwordBase("burning_sword");
	public static final Item gorgeousSword = new ItemShortSwordBase("gorgeous_sword");
	public static final Item gaiaSword = new ItemShortSwordBase("gaia_sword");
	public static final Item snakeSword = new ItemShortSwordBase("snake_sword");
	public static final Item luckBlade = new ItemShortSwordBase("luck_blade");
	public static final Item platinumSword = new ItemShortSwordBase("platinum_sword");
	public static final Item windSword = new ItemShortSwordBase("wind_sword");
	public static final Item chaosBlade = new ItemShortSwordBase("chaos_blade");
	public static final Item sakura = new ItemShortSwordBase("sakura");
	public static final Item sunspot = new ItemShortSwordBase("sunspot");
	public static final Item durendal = new ItemShortSwordBase("durendal");
	public static final Item aerialBlade = new ItemShortSwordBase("aerial_blade");
	public static final Item grantale = new ItemShortSwordBase("grantale");
	public static final Item smashBlade = new ItemShortSwordBase("smash_blade");
	public static final Item icifier = new ItemShortSwordBase("icifier");
	public static final Item soulEater = new ItemShortSwordBase("soul_eater");
	public static final Item raventine = new ItemShortSwordBase("raventine");
	public static final Item starSaber = new ItemShortSwordBase("star_saber");
	public static final Item platinumSwordPlus = new ItemShortSwordBase("platinum_sword_plus");
	public static final Item dragonSlayer = new ItemShortSwordBase("dragon_slayer");
	public static final Item runeBlade = new ItemShortSwordBase("rune_blade");
	public static final Item gladius = new ItemShortSwordBase("gladius");
	public static final Item runeLegend = new ItemShortSwordBase("rune_legend");
	public static final Item backScratcher = new ItemShortSwordBase("back_scratcher");
	public static final Item spoon = new ItemShortSwordBase("spoon");
	public static final Item veggieBlade = new ItemShortSwordBase("veggie_blade");

	public static final Item claymore = new ItemLongSwordBase("claymore");
	public static final Item zweihaender = new ItemLongSwordBase("zweihaender");
	public static final Item zweihaenderPlus = new ItemLongSwordBase("zweihaender_plus");
	public static final Item greatSword = new ItemLongSwordBase("great_sword");
	public static final Item seaCutter = new ItemLongSwordBase("seaCutter");
	public static final Item cycloneBlade = new ItemLongSwordBase("cyclone_blade");
	public static final Item poisonBlade = new ItemLongSwordBase("poison_blade");
	public static final Item katzbalger = new ItemLongSwordBase("katzbalger");
	public static final Item earthShade = new ItemLongSwordBase("earth_shade");
	public static final Item bigKnife = new ItemLongSwordBase("big_knife");
	public static final Item katana = new ItemLongSwordBase("katana");
	public static final Item flameSaber = new ItemLongSwordBase("flame_saber");
	public static final Item bioSmasher = new ItemLongSwordBase("bio_smasher");
	public static final Item snowCrown = new ItemLongSwordBase("snow_crown");
	public static final Item dancingDicer = new ItemLongSwordBase("dancing_dicer");
	public static final Item flamberge = new ItemLongSwordBase("flamberge");
	public static final Item flambergePlus = new ItemLongSwordBase("flamberge_plus");
	public static final Item volcanon = new ItemLongSwordBase("volcanon");
	public static final Item psycho = new ItemLongSwordBase("psycho");
	public static final Item shineBlade = new ItemLongSwordBase("shine_blade");
	public static final Item grandSmasher = new ItemLongSwordBase("grand_smasher");
	public static final Item belzebuth = new ItemLongSwordBase("belzebuth");
	public static final Item orochi = new ItemLongSwordBase("orochi");
	public static final Item punisher = new ItemLongSwordBase("punisher");
	public static final Item steelSlicer = new ItemLongSwordBase("steel_slicer");
	public static final Item moonShadow = new ItemLongSwordBase("moon_shadow");
	public static final Item blueEyedBlade = new ItemLongSwordBase("blue_eyed_blade");
	public static final Item balmung = new ItemLongSwordBase("balmung");
	public static final Item braveheart = new ItemLongSwordBase("braveheart");
	public static final Item forceElement = new ItemLongSwordBase("force_element");
	public static final Item heavensAsunder = new ItemLongSwordBase("heavens_asunder");
	public static final Item caliburn = new ItemLongSwordBase("caliburn");
	public static final Item dekash = new ItemLongSwordBase("dekash");
	public static final Item daicone = new ItemLongSwordBase("daicone");

	public static final Item spear = new ItemSpearBase("spear");
	public static final Item woodStaff = new ItemSpearBase("wood_staff");
	public static final Item lance = new ItemSpearBase("lance");
	public static final Item lancePlus = new ItemSpearBase("lance_plus");
	public static final Item needleSpear = new ItemSpearBase("needle_spear");
	public static final Item trident = new ItemSpearBase("trident");
	public static final Item waterSpear = new ItemSpearBase("water_spear");
	public static final Item halberd = new ItemSpearBase("halberd");
	public static final Item corsesca = new ItemSpearBase("corsesca");
	public static final Item corsescaPlus = new ItemSpearBase("corsesca_plus");
	public static final Item poisonSpear = new ItemSpearBase("poison_spear");
	public static final Item fiveStaff = new ItemSpearBase("five_staff");
	public static final Item heavyLance = new ItemSpearBase("heavy_lance");
	public static final Item featherLance = new ItemSpearBase("feather_lance");
	public static final Item iceberg = new ItemSpearBase("iceberg");
	public static final Item bloodLance = new ItemSpearBase("blood_lance");
	public static final Item magicalLance = new ItemSpearBase("magical_lance");
	public static final Item flareLance = new ItemSpearBase("flare_lance");
	public static final Item brionac = new ItemSpearBase("brionac");
	public static final Item poisonQueen = new ItemSpearBase("poison_queen");
	public static final Item monkStaff = new ItemSpearBase("monk_staff");
	public static final Item metus = new ItemSpearBase("metus");
	public static final Item silentGrave = new ItemSpearBase("silent_grave");
	public static final Item overbreak = new ItemSpearBase("overbreak");
	public static final Item bjor = new ItemSpearBase("bjor");
	public static final Item belvarose = new ItemSpearBase("belvarose");
	public static final Item gaeBolg = new ItemSpearBase("gae_bolg");
	public static final Item dragonsFang = new ItemSpearBase("dragons_fang");
	public static final Item gungnir = new ItemSpearBase("gungnir");
	public static final Item legion = new ItemSpearBase("legion");
	public static final Item pitchfork = new ItemSpearBase("pitchfork");
	public static final Item safetyLance = new ItemSpearBase("safety_lance");
	public static final Item pineClub = new ItemSpearBase("pine_club");

	public static final Item battleAxe = new ItemAxeBase("battle_axe");
	public static final Item battleScythe = new ItemAxeBase("battle_scythe");
	public static final Item poleAxe = new ItemAxeBase("pole_axe");
	public static final Item poleAxePlus = new ItemAxeBase("pole_axe_plus");
	public static final Item greatAxe = new ItemAxeBase("great_axe");
	public static final Item tomohawk = new ItemAxeBase("tomohawk");
	public static final Item basiliskFang = new ItemAxeBase("basilisk_fang");
	public static final Item rockAxe = new ItemAxeBase("rock_axe");
	public static final Item demonAxe = new ItemAxeBase("demon_axe");
	public static final Item frostAxe = new ItemAxeBase("frost_axe");
	public static final Item crescentAxe = new ItemAxeBase("crescent_axe");
	public static final Item crescentAxePlus = new ItemAxeBase("crescent_axe_plus");
	public static final Item heatAxe = new ItemAxeBase("heat_axe");
	public static final Item doubleEdge = new ItemAxeBase("double_edge");
	public static final Item alldale = new ItemAxeBase("alldale");
	public static final Item devilFinger = new ItemAxeBase("devil_finger");
	public static final Item executioner = new ItemAxeBase("executioner");
	public static final Item saintAxe = new ItemAxeBase("saint_axe");
	public static final Item axe = new ItemAxeBase("axe");
	public static final Item lollipop = new ItemAxeBase("lollipop");

	public static final Item battleHammer = new ItemHammerBase("battle_hammer");
	public static final Item bat = new ItemHammerBase("bat");
	public static final Item warHammer = new ItemHammerBase("war_hammer");
	public static final Item warHammerPlus = new ItemHammerBase("war_hammer_plus");
	public static final Item ironBat = new ItemHammerBase("iron_bat");
	public static final Item greatHammer = new ItemHammerBase("great_hammer");
	public static final Item iceHammer = new ItemHammerBase("ice_hammer");
	public static final Item boneHammer = new ItemHammerBase("bone_hammer");
	public static final Item strongStone = new ItemHammerBase("strong_stone");
	public static final Item flameHammer = new ItemHammerBase("flame_hammer");
	public static final Item gigantHammer = new ItemHammerBase("gigant_hammer");
	public static final Item skyHammer = new ItemHammerBase("sky_hammer");
	public static final Item gravitonHammer = new ItemHammerBase("graviton_hammer");
	public static final Item spikedHammer = new ItemHammerBase("spiked_hammer");
	public static final Item crystalHammer = new ItemHammerBase("crystal_hammer");
	public static final Item schnabel = new ItemHammerBase("schnabel");
	public static final Item gigantHammerPlus = new ItemHammerBase("gigant_hammer_plus");
	public static final Item kongo = new ItemHammerBase("kongo");
	public static final Item mjolnir = new ItemHammerBase("mjolnir");
	public static final Item fatalCrush = new ItemHammerBase("fatal_crush");
	public static final Item splashStar = new ItemHammerBase("splash_star");
	public static final Item hammer = new ItemHammerBase("hammer");
	public static final Item toyHammer = new ItemHammerBase("toy_hammer");

	public static final Item shortDagger = new ItemDualBladeBase("short_dagger");
	public static final Item steelEdge = new ItemDualBladeBase("steel_edge");
	public static final Item frostEdge = new ItemDualBladeBase("frost_edge");
	public static final Item ironEdge = new ItemDualBladeBase("iron_edge");
	public static final Item thiefKnife = new ItemDualBladeBase("thief_knife");
	public static final Item windEdge = new ItemDualBladeBase("wind_edge");
	public static final Item gorgeousLx = new ItemDualBladeBase("gourgeous_lx");
	public static final Item steelKatana = new ItemDualBladeBase("steel_katana");
	public static final Item twinBlade = new ItemDualBladeBase("twin_blade");
	public static final Item rampage = new ItemDualBladeBase("rampage");
	public static final Item salamander = new ItemDualBladeBase("salamander");
	public static final Item platinumEdge = new ItemDualBladeBase("platinum_edge");
	public static final Item sonicDagger = new ItemDualBladeBase("sonic_dagger");
	public static final Item chaosEdge = new ItemDualBladeBase("chaos_edge");
	public static final Item desertWind = new ItemDualBladeBase("desert_wind");
	public static final Item brokenWall = new ItemDualBladeBase("broken_wall");
	public static final Item forceDivide = new ItemDualBladeBase("force_divide");
	public static final Item heartFire = new ItemDualBladeBase("heart_fire");
	public static final Item orcusSword = new ItemDualBladeBase("orcus_sword");
	public static final Item deepBlizzard = new ItemDualBladeBase("deep_blizzard");
	public static final Item darkInvitation = new ItemDualBladeBase("dark_invitation");
	public static final Item priestSaber = new ItemDualBladeBase("priest_saber");
	public static final Item efreet = new ItemDualBladeBase("efreet");
	public static final Item dragoonClaw = new ItemDualBladeBase("dragoon_claw");
	public static final Item emeraldEdge = new ItemDualBladeBase("emerald_edge");
	public static final Item runeEdge = new ItemDualBladeBase("rune_edge");
	public static final Item earnestEdge = new ItemDualBladeBase("earnest_edge");
	public static final Item twinJustice = new ItemDualBladeBase("twin_justice");
	public static final Item doubleScratch = new ItemDualBladeBase("double_scratch");
	public static final Item acutorimass = new ItemDualBladeBase("acutorimass");
	public static final Item twinLeeks = new ItemDualBladeBase("twin_leeks");

	public static final Item leatherGlove = new ItemGloveBase("leather_glove");
	public static final Item brassKnuckles = new ItemGloveBase("brass_knuckles");
	public static final Item kote = new ItemGloveBase("kote");
	public static final Item gloves = new ItemGloveBase("gloves");
	public static final Item bearClaws = new ItemGloveBase("bear_claws");
	public static final Item fistEarth = new ItemGloveBase("fist_of_earth");
	public static final Item fistFire = new ItemGloveBase("fist_of_fire");
	public static final Item fistWater = new ItemGloveBase("fist_of_water");
	public static final Item dragonClaws = new ItemGloveBase("dragon_claws");
	public static final Item fistDark = new ItemGloveBase("fist_of_dark");
	public static final Item fistWind = new ItemGloveBase("fist_of_wind");
	public static final Item fistLight = new ItemGloveBase("fist_of_light");
	public static final Item catPunch = new ItemGloveBase("cat_punch");
	public static final Item animalPuppets = new ItemGloveBase("animal_puppets");
	public static final Item ironleafFists = new ItemGloveBase("ironleaf_fists");
	public static final Item caestus = new ItemGloveBase("caestus");
	public static final Item golemPunch = new ItemGloveBase("golem_punch");
	public static final Item godHand = new ItemGloveBase("hand_of_god");
	public static final Item bazalKatar = new ItemGloveBase("bazal_katar");
	public static final Item fenrir = new ItemGloveBase("fenrir");

	public static final Item rod = new ItemStaffBase("rod", EnumElement.FIRE);
	public static final Item amethystRod = new ItemStaffBase("amethyst_rod", EnumElement.EARTH);
	public static final Item aquamarineRod = new ItemStaffBase("aquamarine_rod", EnumElement.WATER);
	public static final Item friendlyRod = new ItemStaffBase("friendly_rod", EnumElement.LOVE);
	public static final Item loveLoveRod = new ItemStaffBase("love_love_rod", EnumElement.LOVE);
	public static final Item staff = new ItemStaffBase("staf", EnumElement.EARTH);
	public static final Item emeraldRod = new ItemStaffBase("emerald_rod", EnumElement.WIND);
	public static final Item silverStaff = new ItemStaffBase("silver_staff", EnumElement.DARK);
	public static final Item flareStaff = new ItemStaffBase("flare_staff", EnumElement.FIRE);
	public static final Item rubyRod = new ItemStaffBase("ruby_rod", EnumElement.FIRE);
	public static final Item sapphireRod = new ItemStaffBase("sapphire_rod", EnumElement.LIGHT);
	public static final Item earthStaff = new ItemStaffBase("earth_staff", EnumElement.EARTH);
	public static final Item lightningWand = new ItemStaffBase("lightning_wand", EnumElement.WIND);
	public static final Item iceStaff = new ItemStaffBase("ice_staff", EnumElement.WATER);
	public static final Item diamondRod = new ItemStaffBase("diamond_rod", EnumElement.DARK);
	public static final Item wizardsStaff = new ItemStaffBase("wizards_staff", EnumElement.LIGHT);
	public static final Item magesStaff = new ItemStaffBase("mages_staff", EnumElement.EARTH);
	public static final Item shootingStarStaff = new ItemStaffBase("shooting_star_staff", EnumElement.LIGHT);
	public static final Item hellBranch = new ItemStaffBase("hell_branch", EnumElement.DARK);
	public static final Item crimsonStaff = new ItemStaffBase("crimson_staff", EnumElement.FIRE);
	public static final Item bubbleStaff = new ItemStaffBase("bubble_staff", EnumElement.WATER);
	public static final Item gaiaRod = new ItemStaffBase("gaia_rod", EnumElement.EARTH);
	public static final Item cycloneRod = new ItemStaffBase("cyclone_rod", EnumElement.WIND);
	public static final Item stormWand = new ItemStaffBase("storm_wand", EnumElement.WIND);
	public static final Item runeStaff = new ItemStaffBase("rune_staff", EnumElement.LIGHT);
	public static final Item magesStaffPlus = new ItemStaffBase("mages_staff_plus", EnumElement.LOVE);
	public static final Item magicBroom = new ItemStaffBase("magic_broom", EnumElement.WIND);
	public static final Item magicShot = new ItemStaffBase("magic_shot", EnumElement.LOVE);
	public static final Item hellCurse = new ItemStaffBase("hell_curse", EnumElement.DARK);
	public static final Item algernon = new ItemStaffBase("algernon", EnumElement.EARTH);
	public static final Item sorceresWand = new ItemStaffBase("sorceres_wand", EnumElement.LIGHT);
	public static final Item basket = new ItemStaffBase("basket", EnumElement.LOVE);
	public static final Item goldenTurnipStaff = new ItemStaffBase("golden_turnip_staff", EnumElement.LOVE);
	public static final Item sweetPotatoStaff = new ItemStaffBase("sweet_potato_staff", EnumElement.LOVE);
	public static final Item elvishHarp = new ItemStaffBase("elvish_harp", EnumElement.LOVE);
	public static final Item syringe = new ItemStaffBase("syringe", EnumElement.WATER);

	public static final Item engagementRing = new ItemAccessoireBase("engagement_ring");
	public static final Item cheapBracelet = new ItemAccessoireBase("cheap_bracelet");
	public static final Item bronzeBracelet = new ItemAccessoireBase("bronze_bracelet");
	public static final Item silverBracelet = new ItemAccessoireBase("silver_bracelet");
	public static final Item goldBracelet = new ItemAccessoireBase("gold_bracelet");
	public static final Item platinumBracelet = new ItemAccessoireBase("platinum_bracelet");
	public static final Item silverRing = new ItemAccessoireBase("silver_ring");
	public static final Item shieldRing = new ItemAccessoireBase("shield_ring");
	public static final Item criticalRing = new ItemAccessoireBase("critical_ring");
	public static final Item silentRing = new ItemAccessoireBase("silent_ring");
	public static final Item paralysisRing = new ItemAccessoireBase("paralysis_ring");
	public static final Item poisonRing = new ItemAccessoireBase("poison_ring");
	public static final Item magicRing = new ItemAccessoireBase("magic_ring");
	public static final Item throwingRing = new ItemAccessoireBase("throwing_ring");
	public static final Item stayUpRing = new ItemAccessoireBase("stay_up_ring");
	public static final Item aquamarineRing = new ItemAccessoireBase("aquamarine_ring");
	public static final Item amethystRing = new ItemAccessoireBase("amethyst_ring");
	public static final Item emeraldRing = new ItemAccessoireBase("emerald_ring");
	public static final Item sapphireRing = new ItemAccessoireBase("sapphire_ring");
	public static final Item rubyRing = new ItemAccessoireBase("ruby_ring");
	public static final Item cursedRing = new ItemAccessoireBase("cursed_ring");
	public static final Item diamondRing = new ItemAccessoireBase("diamond_ring");
	public static final Item aquamarineBrooch = new ItemAccessoireBase("aquamarine_brooch");
	public static final Item amethystBrooch = new ItemAccessoireBase("amethyst_brooch");
	public static final Item emeraldBrooch = new ItemAccessoireBase("emerald_brooch");
	public static final Item sapphireBrooch = new ItemAccessoireBase("sapphire_brooch");
	public static final Item rubyBrooch = new ItemAccessoireBase("ruby_brooch");
	public static final Item diamondBrooch = new ItemAccessoireBase("diamond_brooch");
	public static final Item dolphinBrooch = new ItemAccessoireBase("dolphin_brooch");
	public static final Item fireRing = new ItemAccessoireBase("fire_ring");
	public static final Item windRing = new ItemAccessoireBase("wind_ring");
	public static final Item waterRing = new ItemAccessoireBase("water_ring");
	public static final Item earthRing = new ItemAccessoireBase("earth_ring");
	public static final Item happyRing = new ItemAccessoireBase("happy_ring");
	public static final Item silverPendant = new ItemAccessoireBase("silver_pendant");
	public static final Item starPendant = new ItemAccessoireBase("star_pendant");
	public static final Item sunPendant = new ItemAccessoireBase("sun_pendant");
	public static final Item fieldPendant = new ItemAccessoireBase("field_pendant");
	public static final Item dewPendant = new ItemAccessoireBase("dew_pendant");
	public static final Item earthPendant = new ItemAccessoireBase("earth_pendant");
	public static final Item heartPendant = new ItemAccessoireBase("heart_pendant");
	public static final Item strangePendant = new ItemAccessoireBase("strange_pendant");
	public static final Item anettesNecklace = new ItemAccessoireBase("anettes_necklace");
	public static final Item workGloves = new ItemAccessoireBase("work_gloves");
	public static final Item glovesAccess = new ItemAccessoireBase("gloves_accessory");
	public static final Item powerGloves = new ItemAccessoireBase("power_gloves");
	public static final Item earrings = new ItemAccessoireBase("earrings");
	public static final Item witchEarrings = new ItemAccessoireBase("witch_earrings");
	public static final Item magicEarrings = new ItemAccessoireBase("magic_earrings").setModelType(EntityEquipmentSlot.HEAD);
	public static final Item charm = new ItemAccessoireBase("charm");
	public static final Item holyAmulet = new ItemAccessoireBase("holy_amulet");
	public static final Item rosary = new ItemAccessoireBase("rosary");
	public static final Item talisman = new ItemAccessoireBase("talisman");
	public static final Item magicCharm = new ItemAccessoireBase("magic_charm");
	public static final Item leatherBelt = new ItemAccessoireBase("leather_belt");
	public static final Item luckyStrike = new ItemAccessoireBase("lucky_strike");
	public static final Item champBelt = new ItemAccessoireBase("champ_belt");
	public static final Item handKnitScarf = new ItemAccessoireBase("hand_knit_scarf");
	public static final Item fluffyScarf = new ItemAccessoireBase("fluffy_scarf");
	public static final Item herosProof = new ItemAccessoireBase("heros_proof");
	public static final Item proofOfWisdom = new ItemAccessoireBase("proof_of_wisdom");
	public static final Item artOfAttack = new ItemAccessoireBase("art_of_attack");
	public static final Item artOfDefense = new ItemAccessoireBase("art_of_defense");
	public static final Item artOfMagic = new ItemAccessoireBase("art_of_magic");
	public static final Item badge = new ItemAccessoireBase("badge");
	public static final Item courageBadge = new ItemAccessoireBase("courage_badge");

	public static final Item shirt = new ItemArmorBase("shirt");
	public static final Item vest = new ItemArmorBase("vest");
	public static final Item cottonCloth = new ItemArmorBase("cotton_cloth");
	public static final Item mail = new ItemArmorBase("mail");
	public static final Item chainMail = new ItemArmorBase("chain_mail");
	public static final Item scaleVest = new ItemArmorBase("scale_vest");
	public static final Item sparklingShirt = new ItemArmorBase("sparkling_shirt");
	public static final Item windCloak = new ItemArmorBase("wind_cloak");
	public static final Item protector = new ItemArmorBase("protector");
	public static final Item platinumMail = new ItemArmorBase("platinum_mail");
	public static final Item lemellarVest = new ItemArmorBase("lemellar_vest");
	public static final Item mercenarysCloak = new ItemArmorBase("mercenarys_cloak");
	public static final Item woolyShirt = new ItemArmorBase("wooly_shirt");
	public static final Item elvishCloak = new ItemArmorBase("elvish_cloak");
	public static final Item dragonCloak = new ItemArmorBase("dragon_cloak");
	public static final Item powerProtector = new ItemArmorBase("power_protector");
	public static final Item runeVest = new ItemArmorBase("rune_vest");
	public static final Item royalGarter = new ItemArmorBase("royal_garter");
	public static final Item fourDragonsVest = new ItemArmorBase("four_dragons_vest");

	public static final Item headband = new ItemHeadBase("headband");
	public static final Item blueRibbon = new ItemHeadBase("blue_ribbon");
	public static final Item greenRibbon = new ItemHeadBase("green_ribbon");
	public static final Item purpleRibbon = new ItemHeadBase("purple_ribbon");
	public static final Item spectacles = new ItemHeadBase("spectacles");
	public static final Item strawHat = new ItemHeadBase("straw_hat");
	public static final Item fancyHat = new ItemHeadBase("fancy_hat");
	public static final Item brandGlasses = new ItemHeadBase("brand_glasses");
	public static final Item cuteKnitting = new ItemHeadBase("cute_knitting");
	public static final Item intelligentGlasses = new ItemHeadBase("intelligent_glasses");
	public static final Item fireproofHood = new ItemHeadBase("fireproof_hood");
	public static final Item silkHat = new ItemHeadBase("silk_hat");
	public static final Item blackRibbon = new ItemHeadBase("black_ribbon");
	public static final Item lolitaHeaddress = new ItemHeadBase("lolita_headdress");
	public static final Item headdress = new ItemHeadBase("headdress");
	public static final Item yellowRibbon = new ItemHeadBase("yellow_ribbon");
	public static final Item catEars = new ItemHeadBase("cat_ears");
	public static final Item silverHairpin = new ItemHeadBase("silver_hairpin");
	public static final Item redRibbon = new ItemHeadBase("red_ribbon");
	public static final Item orangeRibbon = new ItemHeadBase("orange_ribbon");
	public static final Item whiteRibbon = new ItemHeadBase("white_ribbon");
	public static final Item fourSeasons = new ItemHeadBase("four_seasons");
	public static final Item feathersHat = new ItemHeadBase("feathers_hat");
	public static final Item goldHairpin = new ItemHeadBase("gold_hairpin");
	public static final Item indigoRibbon = new ItemHeadBase("indigo_ribbon");
	public static final Item crown = new ItemHeadBase("crown");
	public static final Item turnipHeadgear = new ItemHeadBase("turnip_headgear");
	public static final Item pumpkinHeadgear = new ItemHeadBase("pumpkin_headgear");

	public static final Item seedShield = new ItemSeedShield();
	public static final Item smallShield = new ItemShieldBase("small_shield");
	public static final Item umbrella = new ItemShieldBase("umbrella");
	public static final Item ironShield = new ItemShieldBase("iron_shield");
	public static final Item monkeyPlush = new ItemShieldBase("monkey_plush");
	public static final Item roundShield = new ItemShieldBase("round_shield");
	public static final Item turtleShield = new ItemShieldBase("turtle_shield");
	public static final Item chaosShield = new ItemShieldBase("chaos_shield");
	public static final Item boneShield = new ItemShieldBase("bone_shield");
	public static final Item magicShield = new ItemShieldBase("magic_shield");
	public static final Item heavyShield = new ItemShieldBase("heavy_shield");
	public static final Item platinumShield = new ItemShieldBase("platinum_shield");
	public static final Item kiteShield = new ItemShieldBase("kite_shield");
	public static final Item knightShield = new ItemShieldBase("knight_shield");
	public static final Item elementShield = new ItemShieldBase("element_shield");
	public static final Item magicalShield = new ItemShieldBase("magical_shield");
	public static final Item prismShield = new ItemShieldBase("prism_shield");
	public static final Item runeShield = new ItemShieldBase("rune_shield");

	public static final Item leatherBoots = new ItemShoeBase("leather_boots");
	public static final Item freeFarmingShoes = new ItemShoeBase("free_farming_shoes");
	public static final Item piyoSandals = new ItemShoeBase("piyo_sandals");
	public static final Item secretShoes = new ItemShoeBase("secret_shoes");
	public static final Item silverBoots = new ItemShoeBase("silver_boots");
	public static final Item heavyBoots = new ItemShoeBase("heavy_boots");
	public static final Item sneakingBoots = new ItemShoeBase("sneaking_boots");
	public static final Item fastStepBoots = new ItemShoeBase("fast_step_boots");
	public static final Item goldBoots = new ItemShoeBase("gold_boots");
	public static final Item boneBoots = new ItemShoeBase("bone_boots");
	public static final Item snowBoots = new ItemShoeBase("snow_boots");
	public static final Item striderBoots = new ItemShoeBase("strider_boots");
	public static final Item stepInBoots = new ItemShoeBase("step_in_boots");
	public static final Item featherBoots = new ItemShoeBase("feather_boots");
	public static final Item ghostBoots = new ItemShoeBase("ghost_boots");
	public static final Item ironGeta = new ItemShoeBase("iron_geta");
	public static final Item knightBoots = new ItemShoeBase("knight_boots");
	public static final Item fairyBoots = new ItemShoeBase("fairy_boots");
	public static final Item wetBoots = new ItemShoeBase("wet_boots");
	public static final Item waterShoes = new ItemShoeBase("water_shoes");
	public static final Item iceSkates = new ItemShoeBase("ice_skates");
	public static final Item rocketWing = new ItemShoeBase("rocket_wing");

	public static final Item itemBlockForge = new ItemBlockForge();
	public static final Item itemBlockAccess = new ItemBlockAccess();
	public static final Item itemBlockCooking = new ItemBlockCooking();
	public static final Item itemBlockPharm = new ItemBlockPharm();
				
	//public static final Item iron = new ItemMaterial("iron");
	public static final Item bronze = new ItemMaterial("bronze");
	public static final Item silver = new ItemMaterial("silver");
	//public static final Item gold = new ItemMaterial("gold");
	public static final Item platinum = new ItemMaterial("platinum");
	public static final Item orichalcum = new ItemMaterial("orichalcum");
	public static final Item dragonic = new ItemMaterial("dragonic_stone");
	public static final Item scrap = new ItemMaterial("scrap");
	public static final Item scrapPlus = new ItemMaterial("scrap_plus");
	public static final Item amethyst = new ItemMaterial("amethyst");
	public static final Item aquamarine = new ItemMaterial("aquamarine");
	public static final Item emerald = new ItemMaterial("emerald");
	public static final Item ruby = new ItemMaterial("ruby");
	public static final Item sapphire = new ItemMaterial("sapphire");
	//public static final Item diamond = new ItemMaterial("diamond");
	public static final Item coreRed = new ItemMaterial("core_red");
	public static final Item coreBlue = new ItemMaterial("core_blue");
	public static final Item coreYellow = new ItemMaterial("core_yellow");
	public static final Item coreGreen = new ItemMaterial("core_green");
	public static final Item crystalSkull = new ItemMaterial("crystal_skull");
	public static final Item crystalWater = new ItemMaterial("crystal_water");
	public static final Item crystalEarth = new ItemMaterial("crystal_earth");
	public static final Item crystalFire = new ItemMaterial("crystal_fire");
	public static final Item crystalWind = new ItemMaterial("crystal_wind");
	public static final Item crystalLight = new ItemMaterial("crystal_light");
	public static final Item crystalDark = new ItemMaterial("crystal_dark");
	public static final Item crystalLove = new ItemMaterial("crystal_love");
	public static final Item crystalSmall = new ItemMaterial("crystal_small");
	public static final Item crystalBig = new ItemMaterial("crystal_big");
	public static final Item crystalMagic = new ItemMaterial("crystal_magic");
	public static final Item crystalRune = new ItemMaterial("crystal_rune");
	public static final Item crystalElectro = new ItemMaterial("crystal_electro");
	//public static final Item stick = new ItemMaterial("stick");
	public static final Item stickThick = new ItemMaterial("stick_thick");
	public static final Item hornInsect = new ItemMaterial("horn_insect");
	public static final Item hornRigid = new ItemMaterial("horn_rigid");
	public static final Item hornDevil = new ItemMaterial("horn_devil");
	public static final Item plantStem = new ItemMaterial("plant_stem");
	public static final Item hornBull = new ItemMaterial("horn_bull");
	public static final Item movingBranch = new ItemMaterial("moving_branch");
	public static final Item glue = new ItemMaterial("glue");
	public static final Item devilBlood = new ItemMaterial("devil_blood");
	public static final Item paraPoison = new ItemMaterial("paralysis_poison");
	public static final Item poisonKing = new ItemMaterial("poison_king");
	//public static final Item feather = new ItemMaterial("feather");
	public static final Item featherBlack = new ItemMaterial("feather_black");
	public static final Item featherThunder = new ItemMaterial("feather_thunder");
	public static final Item featherYellow = new ItemMaterial("feather_yellow");
	public static final Item dragonFin = new ItemMaterial("dragon_fin");
	public static final Item turtleShell = new ItemMaterial("turtle_shell");
	public static final Item fishFossil = new ItemMaterial("fish_fossil");
	public static final Item skull = new ItemMaterial("skull");
	public static final Item dragonBones = new ItemMaterial("dragon_bones");
	public static final Item tortoiseShell = new ItemMaterial("tortoise_shell");
	public static final Item ammonite = new ItemMaterial("ammonite");
	public static final Item rock = new ItemMaterial("rock");
	public static final Item stoneRound = new ItemMaterial("stone_round");
	public static final Item stoneTiny = new ItemMaterial("stone_tiny_golem");
	public static final Item stoneGolem = new ItemMaterial("stone_golem");
	public static final Item tabletGolem = new ItemMaterial("tablet_golem");
	public static final Item stoneSpirit = new ItemMaterial("stone_gole_spirit");
	public static final Item tabletTruth = new ItemMaterial("tablet_truth");
	public static final Item yarn = new ItemMaterial("yarn");
	public static final Item oldBandage = new ItemMaterial("old_bandage");
	public static final Item ambrosiasThorns = new ItemMaterial("ambrosias_thorns");
	public static final Item threadSpider = new ItemMaterial("thread_spider");
	public static final Item puppetryStrings = new ItemMaterial("puppetry_strings");
	public static final Item vine = new ItemMaterial("vine");
	public static final Item tailScorpion = new ItemMaterial("tail_scorpion");
	public static final Item strongVine = new ItemMaterial("strong_vine");
	public static final Item threadPretty = new ItemMaterial("thread_pretty");
	public static final Item tailChimera = new ItemMaterial("tail_chimera");
	public static final Item arrowHead = new ItemMaterial("arrowhead");
	public static final Item bladeShard = new ItemMaterial("blade_shard");
	public static final Item brokenHilt = new ItemMaterial("broken_hilt");
	public static final Item brokenBox = new ItemMaterial("broken_box");
	public static final Item bladeGlistening = new ItemMaterial("blade_glistening");
	public static final Item greatHammerShard = new ItemMaterial("great_hammer_shard");
	public static final Item hammerPiece = new ItemMaterial("hammer_piece");
	public static final Item shoulderPiece = new ItemMaterial("shoulder_piece");
	public static final Item piratesArmor = new ItemMaterial("pirates_armor");
	public static final Item screwRusty = new ItemMaterial("screw_rusty");
	public static final Item screwShiny = new ItemMaterial("screw_shiny");
	public static final Item rockShardLeft = new ItemMaterial("rock_shard_left");
	public static final Item rockShardRight = new ItemMaterial("rock_shard_right");
	public static final Item MTGUPlate = new ItemMaterial("mtgu_plate");
	public static final Item brokenIceWall = new ItemMaterial("broken_ice_wall");
	public static final Item furSmall = new ItemMaterial("fur_s");
	public static final Item furMedium = new ItemMaterial("fur_m");
	public static final Item furLarge = new ItemMaterial("fur_l");
	public static final Item fur = new ItemMaterial("fur");
	public static final Item furball = new ItemMaterial("furball");
	public static final Item downYellow = new ItemMaterial("down_yellow");
	public static final Item furQuality = new ItemMaterial("fur_quality");
	public static final Item furPuffy = new ItemMaterial("fur_puffy");
	public static final Item downPenguin = new ItemMaterial("down_penguin");
	public static final Item lightningMane = new ItemMaterial("lightning_mane");
	public static final Item furRedLion = new ItemMaterial("fur_red_lion");
	public static final Item furBlueLion = new ItemMaterial("fur_blue_lion");
	public static final Item chestHair = new ItemMaterial("chest_hair");
	public static final Item spore = new ItemMaterial("spore");
	public static final Item powderPoison = new ItemMaterial("powder_poison");
	public static final Item sporeHoly = new ItemMaterial("spore_holy");
	public static final Item fairyDust = new ItemMaterial("fairy_dust");
	public static final Item fairyElixier = new ItemMaterial("fairy_elixier");
	//public static final Item gunpowder = new ItemMaterial("gunpowder"); 
	public static final Item root = new ItemMaterial("root");
	public static final Item powderMagic = new ItemMaterial("powder_magic");
	public static final Item powderMysterious = new ItemMaterial("powder_mysterious");
	public static final Item magic = new ItemMaterial("magic");
	public static final Item ashEarth = new ItemMaterial("ash_earth");
	public static final Item ashFire = new ItemMaterial("ash_fire");
	public static final Item ashWater = new ItemMaterial("ash_water");
	public static final Item turnipsMiracle = new ItemMaterial("turnips_miracle");
	public static final Item melodyBottle = new ItemMaterial("melody_bottle");
	public static final Item clothCheap = new ItemMaterial("cloth_cheap");
	public static final Item clothQuality = new ItemMaterial("cloth_quality");
	public static final Item clothQualityWorn = new ItemMaterial("cloth_quality_worn");
	public static final Item clothSilk = new ItemMaterial("cloth_silk");
	public static final Item ghostHood = new ItemMaterial("ghost_hood");
	public static final Item gloveGiant = new ItemMaterial("glove_giant");
	public static final Item gloveBlueGiant = new ItemMaterial("glove_blue_giant");
	public static final Item carapaceInsect = new ItemMaterial("carapace_insect");
	public static final Item carapacePretty = new ItemMaterial("carapace_pretty");
	public static final Item clothAncientOrc = new ItemMaterial("cloth_ancient_orc");
	public static final Item jawInsect = new ItemMaterial("jaw_insect");
	public static final Item clawPanther = new ItemMaterial("claw_panther");
	public static final Item clawMagic = new ItemMaterial("claw_magic");
	public static final Item fangWolf = new ItemMaterial("fang_wolf");
	public static final Item fangGoldWolf = new ItemMaterial("fang_gold_wolf");
	public static final Item clawPalm = new ItemMaterial("claw_palm");
	public static final Item clawMalm = new ItemMaterial("claw_malm");
	public static final Item giantsNail = new ItemMaterial("giants_nail");
	public static final Item clawChimera = new ItemMaterial("claw_chimera");
	public static final Item tuskIvory = new ItemMaterial("tusk_ivory");
	public static final Item tuskUnbrokenIvory = new ItemMaterial("tusk_unbroken_ivory");
	public static final Item scorpionPincer = new ItemMaterial("scorpion_pincer");
	public static final Item dangerousScissors = new ItemMaterial("dangerous_scissors");
	public static final Item propellorCheap = new ItemMaterial("propeller_cheap");
	public static final Item propellorQuality = new ItemMaterial("propellor_quality");
	public static final Item fangDragon = new ItemMaterial("fang_dragon");
	public static final Item jawQueen = new ItemMaterial("jaw_queen");
	public static final Item windDragonTooth = new ItemMaterial("wind_dragon_tooth");
	public static final Item giantsNailBig = new ItemMaterial("giants_nail_big");
	public static final Item scaleWet = new ItemMaterial("scale_wet");
	public static final Item scaleGrimoire = new ItemMaterial("scale_grimoire");
	public static final Item scaleDragon = new ItemMaterial("scale_dragon");
	public static final Item scaleCrimson = new ItemMaterial("scale_crimson");
	public static final Item scaleBlue = new ItemMaterial("scale_blue");
	public static final Item scaleGlitter = new ItemMaterial("scale_glitter");
	public static final Item scaleLove = new ItemMaterial("scale_love");
	public static final Item scaleBlack = new ItemMaterial("scale_black");
	public static final Item scaleFire = new ItemMaterial("scale_fire");
	public static final Item scaleEarth = new ItemMaterial("scale_earth");
	public static final Item scaleLegend = new ItemMaterial("scale_legend");
	public static final Item steelDouble = new ItemMaterial("steel_double");
	public static final Item steelTen = new ItemMaterial("steel_ten_fold");
	public static final Item glittaAugite = new ItemMaterial("glitta_augite");
	public static final Item invisStone = new ItemMaterial("invisible_stone");
	public static final Item lightOre = new ItemMaterial("light_ore");
	public static final Item runeSphereShard = new ItemMaterial("rune_sphere_shard");
	public static final Item shadeStone = new ItemMaterial("shade_stone");
	public static final Item racoonLeaf = new ItemMaterial("racoon_leaf");
	public static final Item icyNose = new ItemMaterial("icy_nose");
	public static final Item bigBirdsComb = new ItemMaterial("big_birds_comb");
	public static final Item rafflesiaPetal = new ItemMaterial("rafflesia_petal");
	public static final Item cursedDoll = new ItemMaterial("cursed_doll");
	public static final Item warriorsProof = new ItemMaterial("warriors_proof");
	public static final Item proofOfRank = new ItemMaterial("proof_of_rank");
	public static final Item throneOfEmpire = new ItemMaterial("throne_of_emire");
	public static final Item whiteStone = new ItemMaterial("white_stone");
	public static final Item rareCan = new ItemMaterial("rare_can");
	public static final Item can = new ItemMaterial("can");
	public static final Item boots = new ItemMaterial("boots");

	public static final Item lawn = new ItemMaterial("ayngondaia_lawn");

	//Recovery and stuff
	public static final Item roundoff = new ItemMedicine("roundoff");
	public static final Item paraGone = new ItemMedicine("para_gone");
	public static final Item coldMed = new ItemMedicine("cold_medicine");
	public static final Item antidote = new ItemMedicine("antidote_potion");
	public static final Item recoveryPotion = new ItemMedicine("recovery_potion");
	public static final Item healingPotion = new ItemMedicine("healing_potion");
	public static final Item mysteryPotion = new ItemMedicine("mystery_potion");
	public static final Item magicalPotion = new ItemMedicine("magical_potion");
	public static final Item invinciroid = new ItemMedicine("invinciroid");
	public static final Item lovePotion = new ItemMedicine("love_potion");
	public static final Item formuade = new ItemMedicine("formuade");
	public static final Item leveliser = new ItemStatIncrease("leveliser", Stat.LEVEL);
	public static final Item heartDrink = new ItemStatIncrease("heart_drink", Stat.HP);
	public static final Item vitalGummi = new ItemStatIncrease("vital_gummi", Stat.VIT);
	public static final Item intelligencer = new ItemStatIncrease("intelligencer", Stat.INT);
	public static final Item protein = new ItemStatIncrease("protein", Stat.STR);
	public static final Item formularA = new ItemFormular(0);
	public static final Item formularB = new ItemFormular(1);
	public static final Item formularC = new ItemFormular(2);
	public static final Item minimizer = new ItemSizeFertilizer(false);
	public static final Item giantizer = new ItemSizeFertilizer(true);
	public static final Item greenifier = new ItemGreenifier(false);
	public static final Item greenifierPlus = new ItemGreenifier(true);
	public static final Item wettablePowder = new ItemWettablePowder();
	public static final Item objectX = new ItemGenericConsumable("object_x");

	//Skills and Magic
	public static final Item fireBallSmall = new ItemFireballCast();
	public static final Item fireBallBig = new ItemEmptySkill("fireball_big");
	public static final Item explosion = new ItemEmptySkill("explosion");
	public static final Item waterLaser = new ItemWaterLaserCast();
	public static final Item parallelLaser = new ItemEmptySkill("parallel_laser");
	public static final Item deltaLaser = new ItemEmptySkill("delta_laser");
	public static final Item screwRock = new ItemEmptySkill("screw_rock");
	public static final Item earthSpike = new ItemEmptySkill("earth_spike");
	public static final Item avengerRock = new ItemEmptySkill("avenger_rock");
	public static final Item sonicWind = new ItemEmptySkill("sonic_wind");
	public static final Item doubleSonic = new ItemEmptySkill("double_sonic");
	public static final Item penetrateSonic = new ItemEmptySkill("penetrate_sonic");
	public static final Item lightBarrier = new ItemEmptySkill("light_barrier");
	public static final Item shine = new ItemEmptySkill("shine");
	public static final Item prism = new ItemEmptySkill("prism");
	public static final Item darkBall = new ItemEmptySkill("dark_ball");
	public static final Item darkSnake = new ItemEmptySkill("dark_snake");
	public static final Item darkness = new ItemEmptySkill("darkness");
	public static final Item cure = new ItemEmptySkill("cure");
	public static final Item cureAll = new ItemEmptySkill("cure_all");
	public static final Item cureMaster = new ItemEmptySkill("cure_master");
	public static final Item mediPoison = new ItemEmptySkill("medi_poison");
	public static final Item mediPara = new ItemEmptySkill("medi_paralysis");
	public static final Item mediSeal = new ItemEmptySkill("medi_seal");
	public static final Item greeting = new ItemEmptySkill("greeting");
	public static final Item powerWave = new ItemEmptySkill("power_wave");
	public static final Item dashSlash = new ItemEmptySkill("dash_slash");
	public static final Item rushAttack = new ItemEmptySkill("rush_attack");
	public static final Item roundBreak = new ItemEmptySkill("round_break");
	public static final Item mindThrust = new ItemEmptySkill("mind_thrust");
	public static final Item gust = new ItemEmptySkill("gust");
	public static final Item storm = new ItemEmptySkill("storm");
	public static final Item blitz = new ItemEmptySkill("blitz");
	public static final Item twinAttack = new ItemEmptySkill("twin_attack");
	public static final Item railStrike = new ItemEmptySkill("rail_strike");
	public static final Item windSlash = new ItemEmptySkill("wind_slash");
	public static final Item flashStrike = new ItemEmptySkill("flash_strike");
	public static final Item naiveBlade = new ItemEmptySkill("naive_blade");
	public static final Item steelHeart = new ItemEmptySkill("steel_heart");
	public static final Item deltaStrike = new ItemEmptySkill("delta_strike");
	public static final Item hurricane = new ItemEmptySkill("hurricane");
	public static final Item reaperSlash = new ItemEmptySkill("reaper_slash");
	public static final Item millionStrike = new ItemEmptySkill("million_strike");
	public static final Item axelDisaster = new ItemEmptySkill("axel_disaster");
	public static final Item stardustUpper = new ItemEmptySkill("stardust_upper");
	public static final Item tornadoSwing = new ItemEmptySkill("tornado_swing");
	public static final Item grandImpact = new ItemEmptySkill("grand_impact");
	public static final Item gigaSwing = new ItemEmptySkill("giga_swing");
	public static final Item upperCut = new ItemEmptySkill("upper_cut");
	public static final Item doubleKick = new ItemEmptySkill("double_kick");
	public static final Item straightPunch = new ItemEmptySkill("straight_punch");
	public static final Item nekoDamashi = new ItemEmptySkill("neko_damashi");
	public static final Item rushPunch = new ItemEmptySkill("rush_punch");
	public static final Item cyclone = new ItemEmptySkill("cyclone");
	public static final Item rapidMove = new ItemEmptySkill("rapid_move");
	public static final Item bonusConcerto = new ItemEmptySkill("bonus_concerto");
	public static final Item strikingMarch = new ItemEmptySkill("striking_march");
	public static final Item ironWaltz = new ItemEmptySkill("iron_waltz");

	public static final Item rockfish = new ItemFishBase("rockfish");
	public static final Item sandFlounder = new ItemFishBase("sand_flounder");
	public static final Item pondSmelt = new ItemFishBase("pond_smelt");
	public static final Item lobster = new ItemFishBase("lobster");
	public static final Item lampSquid = new ItemFishBase("lamb_squid");
	public static final Item cherrySalmon = new ItemFishBase("cherry_salmon");
	public static final Item fallFlounder = new ItemFishBase("fall_flounder");
	public static final Item girella = new ItemFishBase("girella");
	public static final Item tuna = new ItemFishBase("tuna");
	public static final Item crucianCarp = new ItemFishBase("crucian_carp");
	public static final Item yellowtail = new ItemFishBase("yellowtail");
	public static final Item blowfish = new ItemFishBase("blowfish");
	public static final Item flounder = new ItemFishBase("flounder");
	public static final Item rainbowTrout = new ItemFishBase("rainbow_trout");
	public static final Item loverSnapper = new ItemFishBase("lover_snapper");
	public static final Item snapper = new ItemFishBase("snapper");
	public static final Item shrimp = new ItemFishBase("shrimp");
	public static final Item sunsquid = new ItemFishBase("sunsquid");
	public static final Item pike = new ItemFishBase("pike");
	public static final Item needlefish = new ItemFishBase("needle_fish");
	public static final Item mackerel = new ItemFishBase("mackerel");
	public static final Item salmon = new ItemFishBase("salmon");
	public static final Item gibelio = new ItemFishBase("gibelio");
	public static final Item turbot = new ItemFishBase("turbot");
	public static final Item skipjack = new ItemFishBase("skipjack");
	public static final Item glitterSnapper = new ItemFishBase("glitter_snapper");
	public static final Item chub = new ItemFishBase("chub");
	public static final Item charFish = new ItemFishBase("char");
	public static final Item sardine = new ItemFishBase("sardine");
	public static final Item taimen = new ItemFishBase("taimen");
	public static final Item squid = new ItemFishBase("squid");
	public static final Item masuTrout = new ItemFishBase("masu_trout");

	public static final Item recipe = new ItemRecipe();
	
	public static final Item icon0 = new ItemIcon(0);
	public static final Item icon1 = new ItemIcon(1);
	public static final Item icon2 = new ItemIcon(2);

	public static final Item debug = new ItemDebug();
	public static final Item level = new ItemLevelUp();
	public static final Item skill = new ItemSkillUp();
	public static final Item tame = new ItemInstaTame();
	public static final Item spawnEgg = new ItemSpawnEgg();
	public static final Item entityLevel = new ItemEntityLevelUp();
	
	//Crop items
	public static final Item turnipSeeds = new ItemCropSeed("turnip", LibOreDictionary.TURNIP);
	public static final Item turnipPinkSeeds = new ItemCropSeed("turnip_pink", LibOreDictionary.PINKTURNIP);
	public static final Item cabbageSeeds = new ItemCropSeed("cabbage", LibOreDictionary.CABBAGE);
	public static final Item pinkMelonSeeds = new ItemCropSeed("pink_melon", LibOreDictionary.PINKMELON);
	public static final Item hotHotSeeds = new ItemCropSeed("hot_hot_fruit", LibOreDictionary.HOTHOTFRUIT);
	public static final Item goldTurnipSeeds = new ItemCropSeed("golden_turnip", LibOreDictionary.GOLDENTURNIP);
	public static final Item goldPotatoSeeds = new ItemCropSeed("golden_potato", LibOreDictionary.GOLDENPOTATO);
	public static final Item goldPumpkinSeeds = new ItemCropSeed("golden_pumpkin", LibOreDictionary.GOLDENPUMPKIN);
	public static final Item goldCabbageSeeds = new ItemCropSeed("golden_cabbage", LibOreDictionary.GOLDENCABBAGE);
	public static final Item bokChoySeeds = new ItemCropSeed("bok_choy", LibOreDictionary.BOKCHOY);
	public static final Item leekSeeds = new ItemCropSeed("leek", LibOreDictionary.LEEK);
	public static final Item radishSeeds = new ItemCropSeed("radish", LibOreDictionary.RADISH);
	public static final Item greenPepperSeeds = new ItemCropSeed("green_pepper", LibOreDictionary.GREENPEPPER);
	public static final Item spinachSeeds = new ItemCropSeed("spinach", LibOreDictionary.SPINACH);
	public static final Item yamSeeds = new ItemCropSeed("yam", LibOreDictionary.YAM);
	public static final Item eggplantSeeds = new ItemCropSeed("eggplant", LibOreDictionary.EGGPLANT);
	public static final Item pineappleSeeds = new ItemCropSeed("pineapple", LibOreDictionary.PINEAPPLE);
	public static final Item pumpkinSeeds = new ItemCropSeed("pumpkin", LibOreDictionary.PUMPKIN);
	public static final Item onionSeeds = new ItemCropSeed("onion", LibOreDictionary.ONION);
	public static final Item cornSeeds = new ItemCropSeed("corn", LibOreDictionary.CORN);
	public static final Item tomatoSeeds = new ItemCropSeed("tomato", LibOreDictionary.TOMATO);
	public static final Item strawberrySeeds = new ItemCropSeed("strawberry", LibOreDictionary.STRAWBERRY);
	public static final Item cucumberSeeds = new ItemCropSeed("cucumber", LibOreDictionary.CUCUMBER);
	public static final Item fodderSeeds = new ItemCropSeed("fodder", LibOreDictionary.FODDER);
	
	public static final Item turnip = new ItemCrops("turnip", LibOreDictionary.TURNIP, false);
	public static final Item turnipGiant = new ItemCrops("turnip", LibOreDictionary.TURNIP, true);
	public static final Item turnipPink = new ItemCrops("turnip_pink", LibOreDictionary.PINKTURNIP, false);
	public static final Item turnipPinkGiant = new ItemCrops("turnip_pink", LibOreDictionary.PINKTURNIP, true);
	public static final Item cabbage = new ItemCrops("cabbage", LibOreDictionary.CABBAGE, false);
	public static final Item cabbageGiant = new ItemCrops("cabbage", LibOreDictionary.CABBAGE, true);
	public static final Item pinkMelon = new ItemCrops("pink_melon", LibOreDictionary.PINKMELON, false);
	public static final Item pinkMelonGiant = new ItemCrops("pink_melon", LibOreDictionary.PINKMELON, true);
	public static final Item pineapple = new ItemCrops("pineapple", LibOreDictionary.PINEAPPLE, false);
	public static final Item pineappleGiant = new ItemCrops("pineapple", LibOreDictionary.PINEAPPLE, true);
	public static final Item strawberry = new ItemCrops("strawberry", LibOreDictionary.STRAWBERRY, false);
	public static final Item strawberryGiant = new ItemCrops("strawberry", LibOreDictionary.STRAWBERRY, true);
	public static final Item goldenTurnip = new ItemCrops("golden_turnip", LibOreDictionary.GOLDENTURNIP, false);
	public static final Item goldenTurnipGiant = new ItemCrops("golden_turnip", LibOreDictionary.GOLDENTURNIP, true);
	public static final Item goldenPotato = new ItemCrops("golden_potato", LibOreDictionary.GOLDENPOTATO, false);
	public static final Item goldenPotatoGiant = new ItemCrops("golden_potato", LibOreDictionary.GOLDENPOTATO, true);
	public static final Item goldenPumpkin = new ItemCrops("golden_pumpkin", LibOreDictionary.GOLDENPUMPKIN, false);
	public static final Item goldenPumpkinGiant = new ItemCrops("golden_pumpkin", LibOreDictionary.GOLDENPUMPKIN, true);
	public static final Item goldenCabbage = new ItemCrops("golden_cabbage", LibOreDictionary.GOLDENCABBAGE, false);
	public static final Item goldenCabbageGiant = new ItemCrops("golden_cabbage", LibOreDictionary.GOLDENCABBAGE, true);
	public static final Item hotHotFruit = new ItemCrops("hot_hot_fruit", LibOreDictionary.HOTHOTFRUIT, false);
	public static final Item hotHotFruitGiant = new ItemCrops("hot_hot_fruit", LibOreDictionary.HOTHOTFRUIT, true);
	public static final Item bokChoy = new ItemCrops("bok_choy", LibOreDictionary.BOKCHOY, false);
	public static final Item bokChoyGiant = new ItemCrops("bok_choy", LibOreDictionary.BOKCHOY, true);
	public static final Item leek = new ItemCrops("leek", LibOreDictionary.LEEK, false);
	public static final Item leekGiant = new ItemCrops("leek", LibOreDictionary.LEEK, true);
	public static final Item radish = new ItemCrops("radish", LibOreDictionary.RADISH, false);
	public static final Item radishGiant = new ItemCrops("radish", LibOreDictionary.RADISH, true);
	public static final Item spinach = new ItemCrops("spinach", LibOreDictionary.SPINACH, false);
	public static final Item spinachGiant = new ItemCrops("spinach", LibOreDictionary.SPINACH, true);
	public static final Item greenPepper = new ItemCrops("green_pepper", LibOreDictionary.GREENPEPPER, false);
	public static final Item greenPepperGiant = new ItemCrops("green_pepper", LibOreDictionary.GREENPEPPER, true);
	public static final Item yam = new ItemCrops("yam", LibOreDictionary.YAM, false);
	public static final Item yamGiant = new ItemCrops("yam", LibOreDictionary.YAM, true);
	public static final Item eggplant = new ItemCrops("eggplant", LibOreDictionary.EGGPLANT, false);
	public static final Item eggplantGiant = new ItemCrops("eggplant", LibOreDictionary.EGGPLANT, true);
	public static final Item tomato = new ItemCrops("tomato", LibOreDictionary.TOMATO, false);
	public static final Item tomatoGiant = new ItemCrops("tomato", LibOreDictionary.TOMATO, true);
	public static final Item corn = new ItemCrops("corn", LibOreDictionary.CORN, false);
	public static final Item cornGiant = new ItemCrops("corn", LibOreDictionary.CORN, true);
	public static final Item cucumber = new ItemCrops("cucumber", LibOreDictionary.CUCUMBER, false);
	public static final Item cucumberGiant = new ItemCrops("cucumber", LibOreDictionary.CUCUMBER, true);
	public static final Item pumpkin = new ItemCrops("pumpkin", LibOreDictionary.PUMPKIN, false);
	public static final Item pumpkinGiant = new ItemCrops("pumpkin", LibOreDictionary.PUMPKIN, true);
	public static final Item onion = new ItemCrops("onion", LibOreDictionary.ONION, false);
	public static final Item onionGiant = new ItemCrops("onion", LibOreDictionary.ONION, true);

	public static final Item fodder = CropMap.addCrop(LibOreDictionary.FODDER, new ItemMaterial("fodder"));

	//Flowers
	public static final Item toyherbSeeds = new ItemCropSeed("toyherb", LibOreDictionary.TOYHERB);
	public static final Item moondropSeeds = new ItemCropSeed("moondrop_flower", LibOreDictionary.MOONDROPFLOWER);
	public static final Item pinkCatSeeds = new ItemCropSeed("pink_cat", LibOreDictionary.PINKCAT);
	public static final Item charmBlueSeeds = new ItemCropSeed("charm_blue", LibOreDictionary.CHARMBLUE);
	public static final Item lampGrassSeeds = new ItemCropSeed("lamp_grass", LibOreDictionary.LAMPGRASS);
	public static final Item cherryGrassSeeds = new ItemCropSeed("cherry_grass", LibOreDictionary.CHERRYGRASS);
	public static final Item whiteCrystalSeeds = new ItemCropSeed("white_crystal", LibOreDictionary.WHITECRYSTAL);
	public static final Item redCrystalSeeds = new ItemCropSeed("red_crystal", LibOreDictionary.REDCRYSTAL);
	public static final Item pomPomGrassSeeds = new ItemCropSeed("pom_pom_grass", LibOreDictionary.POMPOMGRASS);
	public static final Item autumnGrassSeeds = new ItemCropSeed("autumn_grass", LibOreDictionary.AUTUMNGRASS);
	public static final Item noelGrassSeeds = new ItemCropSeed("noel_grass", LibOreDictionary.NOELGRASS);
	public static final Item greenCrystalSeeds = new ItemCropSeed("green_crystal", LibOreDictionary.GREENCRYSTAL);
	public static final Item fireflowerSeeds = new ItemCropSeed("fireflower", LibOreDictionary.FIREFLOWER);
	public static final Item fourLeafCloverSeeds = new ItemCropSeed("four_leaf_clover", LibOreDictionary.FOURLEAFCLOVER);
	public static final Item ironleafSeeds = new ItemCropSeed("ironleaf", LibOreDictionary.IRONLEAF);
	public static final Item emeryFlowerSeeds = new ItemCropSeed("emery_flower", LibOreDictionary.EMERYFLOWER);
	public static final Item blueCrystalSeeds = new ItemCropSeed("blue_crystal", LibOreDictionary.BLUECRYSTAL);

	public static final Item whiteCrystal = new ItemCrops("white_crystal", LibOreDictionary.WHITECRYSTAL, false);
	public static final Item whiteCrystalGiant = new ItemCrops("white_crystal", LibOreDictionary.WHITECRYSTAL, true);
	public static final Item redCrystal = new ItemCrops("red_crystal", LibOreDictionary.REDCRYSTAL, false);
	public static final Item redCrystalGiant = new ItemCrops("red_crystal", LibOreDictionary.REDCRYSTAL, true);
	public static final Item pomPomGrass = new ItemCrops("pom-pom_grass", LibOreDictionary.POMPOMGRASS, false);
	public static final Item pomPomGrassGiant = new ItemCrops("pom-pom_grass", LibOreDictionary.POMPOMGRASS, true);
	public static final Item autumnGrass = new ItemCrops("autumn_grass", LibOreDictionary.AUTUMNGRASS, false);
	public static final Item autumnGrassGiant = new ItemCrops("autumn_grass", LibOreDictionary.AUTUMNGRASS, true);
	public static final Item noelGrass = new ItemCrops("noel_grass", LibOreDictionary.NOELGRASS, false);
	public static final Item noelGrassGiant = new ItemCrops("noel_grass", LibOreDictionary.NOELGRASS, true);
	public static final Item greenCrystal = new ItemCrops("green_crystal", LibOreDictionary.GREENCRYSTAL, false);
	public static final Item greenCrystalGiant = new ItemCrops("green_crystal", LibOreDictionary.GREENCRYSTAL, true);
	public static final Item fireflower = new ItemCrops("fireflower", LibOreDictionary.FIREFLOWER, false);
	public static final Item fireflowerGiant = new ItemCrops("fireflower", LibOreDictionary.FIREFLOWER, true);
	public static final Item fourLeafClover = new ItemCrops("four_leaf_clover", LibOreDictionary.FOURLEAFCLOVER, false);
	public static final Item fourLeafCloverGiant = new ItemCrops("four_leaf_clover", LibOreDictionary.FOURLEAFCLOVER, true);
	public static final Item ironleaf = new ItemCrops("ironleaf", LibOreDictionary.IRONLEAF, false);
	public static final Item ironleafGiant = new ItemCrops("ironleaf", LibOreDictionary.IRONLEAF, true);
	public static final Item emeryFlower = new ItemCrops("emery_flower", LibOreDictionary.EMERYFLOWER, false);
	public static final Item emeryFlowerGiant = new ItemCrops("emery_flower", LibOreDictionary.EMERYFLOWER, true);
	public static final Item blueCrystal = new ItemCrops("blue_crystal", LibOreDictionary.BLUECRYSTAL, false);
	public static final Item blueCrystalGiant = new ItemCrops("blue_crystal", LibOreDictionary.BLUECRYSTAL, true);
	public static final Item lampGrass = new ItemCrops("lamp_grass", LibOreDictionary.LAMPGRASS, false);
	public static final Item lampGrassGiant = new ItemCrops("lamp_grass", LibOreDictionary.LAMPGRASS, true);
	public static final Item cherryGrass = new ItemCrops("cherry_grass", LibOreDictionary.CHERRYGRASS, false);
	public static final Item cherryGrassGiant = new ItemCrops("cherry_grass", LibOreDictionary.CHERRYGRASS, true);
	public static final Item charmBlue = new ItemCrops("charm_blue", LibOreDictionary.CHARMBLUE, false);
	public static final Item charmBlueGiant = new ItemCrops("charm_blue", LibOreDictionary.CHARMBLUE, true);
	public static final Item pinkCat = new ItemCrops("pink_cat", LibOreDictionary.PINKCAT, false);
	public static final Item pinkCatGiant = new ItemCrops("pink_cat", LibOreDictionary.PINKCAT, true);
	public static final Item moondropFlower = new ItemCrops("moondrop_flower", LibOreDictionary.MOONDROPFLOWER, false);
	public static final Item moondropFlowerGiant = new ItemCrops("moondrop_flower", LibOreDictionary.MOONDROPFLOWER, true);
	public static final Item toyherb = new ItemCrops("toyherb", LibOreDictionary.TOYHERB, false);
	public static final Item toyherbGiant = new ItemCrops("toyherb", LibOreDictionary.TOYHERB, true);
	//Vanilla
	
	public static final Item potatoGiant = new ItemCrops("potato", LibOreDictionary.POTATO, true);

	public static final Item carrotGiant = new ItemCrops("carrot", LibOreDictionary.CARROT, true);

	//Special seeds
	
	public static final Item shieldSeeds = new ItemCropSeed("shield", LibOreDictionary.SEEDSHIELDITEM);

	public static final Item swordSeeds = new ItemCropSeed("sword", LibOreDictionary.SEEDSWORDITEM);

	public static final Item dungeonSeeds = new ItemDungeonSeed();
	
	//Herbs
	
	public static final Item elliLeaves = new ItemHerb("elli_leaves", LibOreDictionary.ELLILEAVES);
	public static final Item witheredGrass = new ItemHerb("withered_grass", LibOreDictionary.WITHEREDGRASS);
	public static final Item weeds = new ItemHerb("weeds", LibOreDictionary.WEEDS);
	public static final Item whiteGrass = new ItemHerb("white_grass", LibOreDictionary.WHITEGRASS);
	public static final Item indigoGrass = new ItemHerb("indigo_grass", LibOreDictionary.INDIGOGRASS);
	public static final Item purpleGrass = new ItemHerb("purple_grass", LibOreDictionary.PURPLEGRASS);
	public static final Item greenGrass = new ItemHerb("green_grass", LibOreDictionary.GREENGRASS);
	public static final Item blueGrass = new ItemHerb("blue_grass", LibOreDictionary.BLUEGRASS);
	public static final Item yellowGrass = new ItemHerb("yellow_grass", LibOreDictionary.YELLOWGRASS);
	public static final Item redGrass = new ItemHerb("red_grass", LibOreDictionary.REDGRASS);
	public static final Item orangeGrass = new ItemHerb("orange_grass", LibOreDictionary.ORANGEGRASS);
	public static final Item blackGrass = new ItemHerb("black_grass", LibOreDictionary.BLACKGRASS);
	public static final Item antidoteGrass = new ItemHerb("antidote_grass", LibOreDictionary.ANTIDOTEGRASS);
	public static final Item medicinalHerb = new ItemHerb("medicinal_herb", LibOreDictionary.MEDICINALHERB);
	public static final Item bambooSprout = new ItemHerb("bamboo_sprout", LibOreDictionary.MEDICINALHERB);

	//Food
	
	public static final Item riceFlour = new ItemGenericConsumable("rice_flour");
	public static final Item curryPowder = new ItemGenericConsumable("curry_powder");
	public static final Item oil = new ItemGenericConsumable("oil");
	public static final Item flour = new ItemGenericConsumable("flour");
	public static final Item honey = new ItemGenericConsumable("honey");
	public static final Item yogurt = new ItemGenericConsumable("yogurt");
	public static final Item cheese = new ItemGenericConsumable("cheese");
	public static final Item mayonnaise = new ItemGenericConsumable("mayonnaise");
	public static final Item eggL = new ItemGenericConsumable("egg_l");
	public static final Item eggM = new ItemGenericConsumable("egg_m");
	public static final Item eggS = new ItemGenericConsumable("egg_s");
	public static final Item milkL = new ItemGenericConsumable("milk_l");
	public static final Item milkM = new ItemGenericConsumable("milk_m");
	public static final Item milkS = new ItemGenericConsumable("milk_s");
	public static final Item wine = new ItemGenericConsumable("wine");
	public static final Item chocolate = new ItemGenericConsumable("chocolate");
	public static final Item rice = new ItemGenericConsumable("rice");
	public static final Item turnipHeaven = new ItemGenericConsumable("turnip_heaven");
	public static final Item pickleMix = new ItemGenericConsumable("pickle_mix");
	public static final Item salmonOnigiri = new ItemGenericConsumable("salmon_onigiri");
	public static final Item bread = new ItemGenericConsumable("bread");
	public static final Item onigiri = new ItemGenericConsumable("onigiri");
	public static final Item relaxTeaLeaves = new ItemGenericConsumable("relax_tea_leaves");
	public static final Item iceCream = new ItemGenericConsumable("ice_cream");
	public static final Item raisinBread = new ItemGenericConsumable("raisin_bread");
	public static final Item bambooRice = new ItemGenericConsumable("bamboo_rice");
	public static final Item pickles = new ItemGenericConsumable("pickles");
	public static final Item pickledTurnip = new ItemGenericConsumable("pickled_turnip");
	public static final Item fruitSandwich = new ItemGenericConsumable("fruit_sandwich");
	public static final Item sandwich = new ItemGenericConsumable("sandwich");
	public static final Item salad = new ItemGenericConsumable("salad");
	public static final Item dumplings = new ItemGenericConsumable("dumplings");
	public static final Item pumpkinFlan = new ItemGenericConsumable("pumpkin_flan");
	public static final Item flan = new ItemGenericConsumable("flan");
	public static final Item chocolateSponge = new ItemGenericConsumable("chocolate_sponge");
	public static final Item poundCake = new ItemGenericConsumable("pound_cake");
	public static final Item steamedGyoza = new ItemGenericConsumable("steamed_gyoza");
	public static final Item curryManju = new ItemGenericConsumable("curry_manju");
	public static final Item chineseManju = new ItemGenericConsumable("chinese_manju");
	public static final Item meatDumpling = new ItemGenericConsumable("meat_dumpling");
	public static final Item cheeseBread = new ItemGenericConsumable("cheese_bread");
	public static final Item steamedBread = new ItemGenericConsumable("steamed_bread");
	public static final Item hotJuice = new ItemGenericConsumable("hot_juice");
	public static final Item preludetoLove = new ItemGenericConsumable("prelude_to_love");
	public static final Item goldJuice = new ItemGenericConsumable("gold_juice");
	public static final Item butter = new ItemGenericConsumable("butter");
	public static final Item ketchup = new ItemGenericConsumable("ketchup");
	public static final Item mixedSmoothie = new ItemGenericConsumable("mixed_smoothie");
	public static final Item mixedJuice = new ItemGenericConsumable("mixed_juice");
	public static final Item veggieSmoothie = new ItemGenericConsumable("veggie_smoothie");
	public static final Item vegetableJuice = new ItemGenericConsumable("vegetable_juice");
	public static final Item fruitSmoothie = new ItemGenericConsumable("fruit_smoothie");
	public static final Item fruitJuice = new ItemGenericConsumable("fruit_juice");
	public static final Item strawberryMilk = new ItemGenericConsumable("strawberry_milk");
	public static final Item appleJuice = new ItemGenericConsumable("apple_juice");
	public static final Item orangeJuice = new ItemGenericConsumable("orange_juice");
	public static final Item grapeJuice = new ItemGenericConsumable("grape_juice");
	public static final Item tomatoJuice = new ItemGenericConsumable("tomato_juice");
	public static final Item pineappleJuice = new ItemGenericConsumable("pineapple_juice");
	public static final Item applePie = new ItemGenericConsumable("apple_pie");
	public static final Item cheesecake = new ItemGenericConsumable("cheesecake");
	public static final Item chocolateCake = new ItemGenericConsumable("chocolate_cake");
	public static final Item cake = new ItemGenericConsumable("cake");
	public static final Item chocoCookie = new ItemGenericConsumable("choco_cookie");
	public static final Item cookie = new ItemGenericConsumable("cookie");
	public static final Item yamoftheAges = new ItemGenericConsumable("yam_of_the_ages");
	public static final Item seafoodGratin = new ItemGenericConsumable("seafood_gratin");
	public static final Item gratin = new ItemGenericConsumable("gratin");
	public static final Item seafoodDoria = new ItemGenericConsumable("seafood_doria");
	public static final Item doria = new ItemGenericConsumable("doria");
	public static final Item seafoodPizza = new ItemGenericConsumable("seafood_pizza");
	public static final Item pizza = new ItemGenericConsumable("pizza");
	public static final Item butterRoll = new ItemGenericConsumable("butter_roll");
	public static final Item jamRoll = new ItemGenericConsumable("jam_roll");
	public static final Item toast = new ItemGenericConsumable("toast");
	public static final Item sweetPotato = new ItemGenericConsumable("sweet_potato");
	public static final Item bakedOnigiri = new ItemGenericConsumable("baked_onigiri");
	public static final Item cornontheCob = new ItemGenericConsumable("corn_on_the_cob");
	public static final Item rockfishStew = new ItemGenericConsumable("rockfish_stew");
	public static final Item unionStew = new ItemGenericConsumable("union_stew");
	public static final Item grilledMiso = new ItemGenericConsumable("grilled_miso");
	public static final Item relaxTea = new ItemGenericConsumable("relax_tea");
	public static final Item royalCurry = new ItemGenericConsumable("royal_curry");
	public static final Item ultimateCurry = new ItemGenericConsumable("ultimate_curry");
	public static final Item curryRice = new ItemGenericConsumable("curry_rice");
	public static final Item stew = new ItemGenericConsumable("stew");
	public static final Item eggBowl = new ItemGenericConsumable("egg_bowl");
	public static final Item tempuraBowl = new ItemGenericConsumable("tempura_bowl");
	public static final Item milkPorridge = new ItemGenericConsumable("milk_porridge");
	public static final Item ricePorridge = new ItemGenericConsumable("rice_porridge");
	public static final Item tempuraUdon = new ItemGenericConsumable("tempura_udon");
	public static final Item curryUdon = new ItemGenericConsumable("curry_udon");
	public static final Item udon = new ItemGenericConsumable("udon");
	public static final Item cheeseFondue = new ItemGenericConsumable("cheese_fondue");
	public static final Item marmalade = new ItemGenericConsumable("marmalade");
	public static final Item grapeJam = new ItemGenericConsumable("grape_jam");
	public static final Item appleJam = new ItemGenericConsumable("apple_jam");
	public static final Item strawberryJam = new ItemGenericConsumable("strawberry_jam");
	public static final Item boiledGyoza = new ItemGenericConsumable("boiled_gyoza");
	public static final Item glazedYam = new ItemGenericConsumable("glazed_yam");
	public static final Item boiledEgg = new ItemGenericConsumable("boiled_egg");
	public static final Item boiledSpinach = new ItemGenericConsumable("boiled_spinach");
	public static final Item boiledPumpkin = new ItemGenericConsumable("boiled_pumpkin");
	public static final Item grapeLiqueur = new ItemGenericConsumable("grape_liqueur");
	public static final Item hotChocolate = new ItemGenericConsumable("hot_chocolate");
	public static final Item hotMilk = new ItemGenericConsumable("hot_milk");
	public static final Item grilledSandFlounder = new ItemGenericConsumable("grilled_sand_flounder");
	public static final Item grilledShrimp = new ItemGenericConsumable("grilled_shrimp");
	public static final Item grilledLobster = new ItemGenericConsumable("grilled_lobster");
	public static final Item grilledBlowfish = new ItemGenericConsumable("grilled_blowfish");
	public static final Item grilledLampSquid = new ItemGenericConsumable("grilled_lamp_squid");
	public static final Item grilledSunsquid = new ItemGenericConsumable("grilled_sunsquid");
	public static final Item grilledSquid = new ItemGenericConsumable("grilled_squid");
	public static final Item grilledFallFlounder = new ItemGenericConsumable("grilled_fall_flounder");
	public static final Item grilledTurbot = new ItemGenericConsumable("grilled_turbot");
	public static final Item grilledFlounder = new ItemGenericConsumable("grilled_flounder");
	public static final Item saltedPike = new ItemGenericConsumable("salted_pike");
	public static final Item grilledNeedlefish = new ItemGenericConsumable("grilled_needlefish");
	public static final Item driedSardines = new ItemGenericConsumable("dried_sardines");
	public static final Item tunaTeriyaki = new ItemGenericConsumable("tuna_teriyaki");
	public static final Item saltedPondSmelt = new ItemGenericConsumable("salted_pond_smelt");
	public static final Item grilledYellowtail = new ItemGenericConsumable("grilled_yellowtail");
	public static final Item grilledMackerel = new ItemGenericConsumable("grilled_mackerel");
	public static final Item grilledSkipjack = new ItemGenericConsumable("grilled_skipjack");
	public static final Item grilledLoverSnapper = new ItemGenericConsumable("grilled_lover_snapper");
	public static final Item grilledGlitterSnapper = new ItemGenericConsumable("grilled_glitter_snapper");
	public static final Item grilledGirella = new ItemGenericConsumable("grilled_girella");
	public static final Item grilledSnapper = new ItemGenericConsumable("grilled_snapper");
	public static final Item grilledGibelio = new ItemGenericConsumable("grilled_gibelio");
	public static final Item grilledCrucianCarp = new ItemGenericConsumable("grilled_crucian_carp");
	public static final Item saltedTaimen = new ItemGenericConsumable("salted_taimen");
	public static final Item saltedSalmon = new ItemGenericConsumable("salted_salmon");
	public static final Item saltedChub = new ItemGenericConsumable("salted_chub");
	public static final Item saltedCherrySalmon = new ItemGenericConsumable("salted_cherry_salmon");
	public static final Item saltedRainbowTrout = new ItemGenericConsumable("salted_rainbow_trout");
	public static final Item saltedChar = new ItemGenericConsumable("salted_char");
	public static final Item saltedMasuTrout = new ItemGenericConsumable("salted_masu_trout");
	public static final Item dryCurry = new ItemGenericConsumable("dry_curry");
	public static final Item risotto = new ItemGenericConsumable("risotto");
	public static final Item gyoza = new ItemGenericConsumable("gyoza");
	public static final Item pancakes = new ItemGenericConsumable("pancakes");
	public static final Item tempura = new ItemGenericConsumable("tempura");
	public static final Item friedUdon = new ItemGenericConsumable("fried_udon");
	public static final Item donut = new ItemGenericConsumable("donut");
	public static final Item frenchToast = new ItemGenericConsumable("french_toast");
	public static final Item curryBread = new ItemGenericConsumable("curry_bread");
	public static final Item bakedApple = new ItemGenericConsumable("baked_apple");
	public static final Item omeletRice = new ItemGenericConsumable("omelet_rice");
	public static final Item omelet = new ItemGenericConsumable("omelet");
	public static final Item friedEggs = new ItemGenericConsumable("fried_eggs");
	public static final Item misoEggplant = new ItemGenericConsumable("miso_eggplant");
	public static final Item cornCereal = new ItemGenericConsumable("corn_cereal");
	public static final Item popcorn = new ItemGenericConsumable("popcorn");
	public static final Item croquettes = new ItemGenericConsumable("croquettes");
	public static final Item frenchFries = new ItemGenericConsumable("french_fries");
	public static final Item cabbageCakes = new ItemGenericConsumable("cabbage_cakes");
	public static final Item friedRice = new ItemGenericConsumable("fried_rice");
	public static final Item friedVeggies = new ItemGenericConsumable("fried_veggies");
	public static final Item shrimpSashimi = new ItemGenericConsumable("shrimp_sashimi");
	public static final Item lobsterSashimi = new ItemGenericConsumable("lobster_sashimi");
	public static final Item blowfishSashimi = new ItemGenericConsumable("blowfish_sashimi");
	public static final Item lampSquidSashimi = new ItemGenericConsumable("lamp_squid_sashimi");
	public static final Item sunsquidSashimi = new ItemGenericConsumable("sunsquid_sashimi");
	public static final Item squidSashimi = new ItemGenericConsumable("squid_sashimi");
	public static final Item fallSashimi = new ItemGenericConsumable("fall_sashimi");
	public static final Item turbotSashimi = new ItemGenericConsumable("turbot_sashimi");
	public static final Item flounderSashimi = new ItemGenericConsumable("flounder_sashimi");
	public static final Item pikeSashimi = new ItemGenericConsumable("pike_sashimi");
	public static final Item needlefishSashimi = new ItemGenericConsumable("needlefish_sashimi");
	public static final Item sardineSashimi = new ItemGenericConsumable("sardine_sashimi");
	public static final Item tunaSashimi = new ItemGenericConsumable("tuna_sashimi");
	public static final Item yellowtailSashimi = new ItemGenericConsumable("yellowtail_sashimi");
	public static final Item skipjackSashimi = new ItemGenericConsumable("skipjack_sashimi");
	public static final Item girellaSashimi = new ItemGenericConsumable("girella_sashimi");
	public static final Item loverSashimi = new ItemGenericConsumable("lover_sashimi");
	public static final Item glitterSashimi = new ItemGenericConsumable("glitter_sashimi");
	public static final Item snapperSashimi = new ItemGenericConsumable("snapper_sashimi");
	public static final Item taimenSashimi = new ItemGenericConsumable("taimen_sashimi");
	public static final Item cherrySashimi = new ItemGenericConsumable("cherry_sashimi");
	public static final Item salmonSashimi = new ItemGenericConsumable("salmon_sashimi");
	public static final Item rainbowSashimi = new ItemGenericConsumable("rainbow_sashimi");
	public static final Item charSashimi = new ItemGenericConsumable("char_sashimi");
	public static final Item troutSashimi = new ItemGenericConsumable("trout_sashimi");
	public static final Item disastrousDish = new ItemGenericConsumable("disastrous_dish");
	public static final Item failedDish = new ItemGenericConsumable("failed_dish");
	public static final Item mixedHerbs = new ItemGenericConsumable("mixed_herbs");
	public static final Item sourDrop = new ItemGenericConsumable("sour_drop");
	public static final Item sweetPowder = new ItemGenericConsumable("sweet_powder");
	public static final Item heavySpice = new ItemGenericConsumable("heavy_spice");
	
	public static final Item orange = new ItemGenericConsumable("orange");
	public static final Item grapes = new ItemGenericConsumable("grapes");

	public static final Item mushroom = new ItemMushroom("mushroom", LibOreDictionary.MUSHROOM).setCreativeTab(RuneCraftory.food);
	public static final Item mushroomMonarch = new ItemMushroom("monarch_mushroom", LibOreDictionary.MONARCHMUSHROOM).setCreativeTab(RuneCraftory.food);
	public static final Item mealyApple = new ItemGenericConsumable("mealy_apple");

	//=====Item groups
	
	public static final Item[] TOOLS = new Item[] {hoeScrap, hoeIron, hoeSilver, hoeGold, hoePlatinum, 
			wateringCanScrap, wateringCanIron, wateringCanSilver, wateringCanGold, wateringCanPlatinum, 
			sickleScrap, sickleIron, sickleSilver, sickleGold, sicklePlatinum,
			hammerScrap, hammerIron, hammerSilver, hammerGold, hammerPlatinum,
			axeScrap, axeIron, axeSilver, axeGold, axePlatinum,
			fishingRodScrap, fishingRodIron, fishingRodSilver, fishingRodGold, fishingRodPlatinum,
			inspector, brush, glass, formularA, formularB, formularC, minimizer, giantizer, greenifier, greenifierPlus, wettablePowder};
	
	public static final Item[] WEAPONS = new Item[] {
			broadSword, steelSword, steelSwordPlus, cutlass, aquaSword, invisBlade, defender, burningSword, gorgeousSword, snakeSword, gaiaSword, luckBlade, platinumSword, windSword, chaosBlade, sakura, sunspot, durendal, aerialBlade, grantale, smashBlade, icifier, soulEater, raventine, starSaber, platinumSwordPlus, dragonSlayer, runeBlade, gladius, runeLegend, backScratcher, spoon, veggieBlade, seedSword,
			claymore, zweihaender, zweihaenderPlus, greatSword, seaCutter, cycloneBlade, poisonBlade, katzbalger, earthShade, bigKnife, katana, flameSaber, bioSmasher, snowCrown, dancingDicer, flamberge, flambergePlus, volcanon, psycho, shineBlade, grandSmasher, belzebuth, orochi, punisher, steelSlicer, moonShadow, blueEyedBlade, balmung, braveheart, forceElement, heavensAsunder, caliburn, dekash, daicone,
			spear, woodStaff, lance, lancePlus, needleSpear, trident, waterSpear, halberd, corsesca, corsescaPlus, poisonSpear, fiveStaff, heavyLance, featherLance, iceberg, bloodLance, magicalLance, flareLance, brionac, poisonQueen, monkStaff, metus, silentGrave, overbreak, bjor, belvarose, gaeBolg, dragonsFang, gungnir, legion, pitchfork, safetyLance, pineClub,
			battleAxe, battleScythe, poleAxe, poleAxePlus, greatAxe, tomohawk, basiliskFang, rockAxe, demonAxe, frostAxe, crescentAxe, crescentAxePlus, heatAxe, doubleEdge, alldale, devilFinger, executioner, saintAxe, axe, lollipop,
			battleHammer, bat, warHammer, warHammerPlus, ironBat, greatHammer, iceHammer, boneHammer, strongStone, flameHammer, gigantHammer, skyHammer, gravitonHammer, spikedHammer, crystalHammer, schnabel, gigantHammerPlus, kongo, mjolnir, fatalCrush, splashStar, hammer, toyHammer,
			shortDagger, steelEdge, frostEdge, ironEdge, thiefKnife, windEdge, gorgeousLx, steelKatana, twinBlade, rampage, salamander, platinumEdge, sonicDagger, chaosEdge, desertWind, brokenWall, forceDivide, heartFire, orcusSword, deepBlizzard, darkInvitation, priestSaber, efreet, dragoonClaw, emeraldEdge, runeEdge, earnestEdge, twinJustice, doubleScratch, acutorimass, twinLeeks,
			leatherGlove, brassKnuckles, kote, gloves, bearClaws, fistEarth, fistFire, fistWater, dragonClaws, fistDark, fistWind, fistLight, catPunch, animalPuppets, ironleafFists, caestus, golemPunch, godHand, bazalKatar, fenrir,
			rod, amethystRod, aquamarineRod, friendlyRod, loveLoveRod, staff, emeraldRod, silverStaff, flareStaff, rubyRod, sapphireRod, earthStaff, lightningWand, iceStaff, diamondRod, wizardsStaff, magesStaff, shootingStarStaff, hellBranch, crimsonStaff, bubbleStaff, gaiaRod, cycloneRod, stormWand, runeStaff, magesStaffPlus, magicBroom, magicShot, hellCurse, algernon, sorceresWand, basket, goldenTurnipStaff, sweetPotatoStaff, elvishHarp, syringe};
	
	public static final Item[] ARMOR = new Item[] {
			headband, blueRibbon, greenRibbon, purpleRibbon, spectacles, strawHat, fancyHat, brandGlasses, cuteKnitting, intelligentGlasses, fireproofHood, silkHat, blackRibbon, lolitaHeaddress, headdress, yellowRibbon, catEars, silverHairpin, redRibbon, orangeRibbon, whiteRibbon, fourSeasons, feathersHat, goldHairpin, indigoRibbon, crown, turnipHeadgear, pumpkinHeadgear, 
			shirt, vest, cottonCloth, mail, chainMail, scaleVest, sparklingShirt, windCloak, protector, platinumMail, lemellarVest, mercenarysCloak, woolyShirt, elvishCloak, dragonCloak, powerProtector, runeVest, royalGarter, fourDragonsVest, 
			engagementRing, cheapBracelet, bronzeBracelet, silverBracelet, goldBracelet, platinumBracelet, silverRing, shieldRing, criticalRing, silentRing, paralysisRing, poisonRing, magicRing, throwingRing, stayUpRing, aquamarineRing, amethystRing, emeraldRing, sapphireRing, rubyRing, cursedRing, diamondRing, aquamarineBrooch, amethystBrooch, emeraldBrooch, sapphireBrooch, rubyBrooch, diamondBrooch, dolphinBrooch, fireRing, windRing, waterRing, earthRing, happyRing, silverPendant, starPendant, sunPendant, fieldPendant, dewPendant, earthPendant, heartPendant, strangePendant, anettesNecklace, workGloves, glovesAccess, powerGloves, earrings, witchEarrings, magicEarrings, charm, holyAmulet, rosary, talisman, magicCharm, leatherBelt, luckyStrike, champBelt, handKnitScarf, fluffyScarf, herosProof, proofOfWisdom, artOfAttack, artOfDefense, artOfMagic, badge, courageBadge,
			leatherBoots, freeFarmingShoes, piyoSandals, secretShoes, silverBoots, heavyBoots, sneakingBoots, fastStepBoots, goldBoots, boneBoots, snowBoots, striderBoots, stepInBoots, featherBoots, ghostBoots, ironGeta, knightBoots, fairyBoots, wetBoots, waterShoes, iceSkates, rocketWing, 
			seedShield, smallShield, umbrella, ironShield, monkeyPlush, roundShield, turtleShield, chaosShield, boneShield, magicShield, heavyShield, platinumShield, kiteShield, knightShield, elementShield, magicalShield, prismShield, runeShield};

	public static final Item[] ITEMBLOCKS = new Item[] {itemBlockForge, itemBlockAccess, itemBlockCooking, itemBlockPharm};

	public static final Item[] minerals = new Item[] {bronze, silver, platinum, orichalcum, dragonic};
	public static final Item[] scraps = new Item[] {scrap, scrapPlus};
	public static final Item[] jewels = new Item[] {amethyst, aquamarine, emerald, ruby, sapphire, coreRed, coreBlue, coreYellow, coreGreen, crystalSkull};
	public static final Item[] crystals = new Item[] {crystalWater, crystalEarth, crystalFire, crystalWind, crystalLight, crystalDark, crystalLove, crystalSmall, crystalBig, crystalMagic, crystalRune, crystalElectro};
	public static final Item[] sticks = new Item[] {stickThick, hornInsect, hornRigid, hornDevil, plantStem, hornBull, movingBranch};
	public static final Item[] liquids = new Item[] {glue, devilBlood, paraPoison, poisonKing};
	public static final Item[] feathers = new Item[] {featherBlack, featherThunder, featherYellow, dragonFin};
	public static final Item[] bones = new Item[] {turtleShell, fishFossil, skull, dragonBones, tortoiseShell, ammonite};
	public static final Item[] stones = new Item[] {rock, stoneRound, stoneTiny, stoneGolem, tabletGolem, stoneSpirit, tabletTruth};
	public static final Item[] strings = new Item[] {yarn, oldBandage, ambrosiasThorns, threadSpider, puppetryStrings, vine, tailScorpion, strongVine, threadPretty, tailChimera};
	public static final Item[] shards = new Item[] {arrowHead, bladeShard, brokenHilt, brokenBox, bladeGlistening, greatHammerShard, hammerPiece, shoulderPiece, piratesArmor, screwRusty, screwShiny, rockShardLeft, rockShardRight, MTGUPlate, brokenIceWall};
	public static final Item[] furs = new Item[] {furSmall, furMedium, furLarge, fur, furball, downYellow, furQuality, furPuffy, downPenguin, lightningMane, furRedLion, furBlueLion, chestHair};
	public static final Item[] powders = new Item[] {spore, powderPoison, sporeHoly, fairyDust, fairyElixier, root, powderMagic, powderMysterious, magic, ashEarth, ashFire, ashWater, turnipsMiracle, melodyBottle};
	public static final Item[] cloths = new Item[] {clothCheap, clothQuality, clothQualityWorn, clothSilk, ghostHood, gloveGiant, gloveBlueGiant, carapaceInsect, carapacePretty, clothAncientOrc};
	public static final Item[] claws = new Item[] {jawInsect, clawPanther, clawMagic, fangWolf, fangGoldWolf, clawPalm, clawMalm, giantsNail, clawChimera, tuskIvory, tuskUnbrokenIvory, scorpionPincer, dangerousScissors, propellorCheap, propellorQuality, fangDragon, jawQueen, windDragonTooth, giantsNailBig};
	public static final Item[] scales = new Item[] {scaleWet, scaleGrimoire, scaleDragon, scaleCrimson, scaleBlue, scaleGlitter, scaleLove, scaleBlack, scaleFire, scaleEarth, scaleLegend};
	public static final Item[] others = new Item[] {steelDouble, steelTen, glittaAugite, invisStone, lightOre, runeSphereShard, shadeStone, racoonLeaf, icyNose, bigBirdsComb, rafflesiaPetal, cursedDoll, warriorsProof, proofOfRank, throneOfEmpire, whiteStone, rareCan, can, boots, lawn};
	
	public static final Item[] MATERIALS = ArrayUtils.combine(new Item[] {}, 
			new Item[][] {minerals, scraps, jewels, crystals, sticks, liquids, feathers, bones, stones, strings, shards, furs, powders, cloths, claws, scales, others});

	public static final Item[] MEDICINE = new Item[] {roundoff, paraGone, coldMed, antidote, recoveryPotion, healingPotion, mysteryPotion, magicalPotion, invinciroid, lovePotion, formuade, leveliser, heartDrink, vitalGummi, intelligencer, protein, objectX};
	public static final Item[] MAGIC =  new Item[] {fireBallSmall, fireBallBig, explosion, waterLaser, parallelLaser, deltaLaser, screwRock, earthSpike, avengerRock, sonicWind, doubleSonic, penetrateSonic, lightBarrier, shine, prism, darkBall, darkSnake, darkness, cure, cureAll, cureMaster, mediPoison, mediPara, mediSeal};
	public static final Item[] SKILLS =  new Item[] {
			powerWave, dashSlash, rushAttack, roundBreak, mindThrust, gust, storm, blitz, twinAttack, railStrike, windSlash, flashStrike, naiveBlade, steelHeart, deltaStrike, hurricane, reaperSlash, millionStrike, axelDisaster, stardustUpper, tornadoSwing, grandImpact, gigaSwing, upperCut, doubleKick, straightPunch, nekoDamashi, rushPunch, cyclone, rapidMove,
			bonusConcerto, strikingMarch, ironWaltz, greeting
	};
	public static final Item[] SPELLS = ArrayUtils.combine(new Item[] {}, new Item[][] {MAGIC, SKILLS});
	public static final Item[] FISH = new Item[] {rockfish, sandFlounder, pondSmelt, lobster, lampSquid, cherrySalmon, fallFlounder, girella, tuna, crucianCarp, yellowtail, blowfish, flounder, rainbowTrout, loverSnapper, snapper, shrimp, sunsquid, pike, needlefish, mackerel, salmon, gibelio, turbot, skipjack, glitterSnapper, chub, charFish, sardine, taimen, squid, masuTrout};
	public static final Item[] CREATIVE = new Item[] {icon0, icon1, icon2, debug, level, skill, tame, spawnEgg, entityLevel};

	public static final Item[] CROPS = new Item[] {turnip, turnipPink, cabbage, pinkMelon, pineapple, strawberry, goldenTurnip, goldenPotato, goldenPumpkin, goldenCabbage, hotHotFruit, bokChoy, leek, radish, spinach, greenPepper, yam, eggplant, tomato, corn, cucumber, pumpkin, onion, fodder};
	public static final Item[] CROPSGIANT = new Item[] {turnipGiant, turnipPinkGiant, cabbageGiant, pinkMelonGiant, pineappleGiant, strawberryGiant, goldenTurnipGiant, goldenPotatoGiant, goldenPumpkinGiant, goldenCabbageGiant, hotHotFruitGiant, bokChoyGiant, leekGiant, radishGiant, spinachGiant, greenPepperGiant, yamGiant, eggplantGiant, tomatoGiant, cornGiant, cucumberGiant, pumpkinGiant, onionGiant, potatoGiant, carrotGiant};
	public static final Item[] CROPSEEDS = new Item[] {turnipSeeds, turnipPinkSeeds, cabbageSeeds, pinkMelonSeeds, pineappleSeeds, strawberrySeeds, goldTurnipSeeds, goldPotatoSeeds, goldPumpkinSeeds, goldCabbageSeeds, hotHotSeeds, bokChoySeeds, leekSeeds, radishSeeds, greenPepperSeeds, spinachSeeds, yamSeeds, eggplantSeeds, tomatoSeeds, cornSeeds, cucumberSeeds, pumpkinSeeds, onionSeeds, fodderSeeds, swordSeeds, shieldSeeds, dungeonSeeds};

	public static final Item[] FLOWERS = new Item[] {whiteCrystal, redCrystal, pomPomGrass, autumnGrass, noelGrass, greenCrystal, fireflower, fourLeafClover, ironleaf, emeryFlower, blueCrystal, lampGrass, cherryGrass, charmBlue, pinkCat, moondropFlower, toyherb};
	public static final Item[] FLOWERSGIANT = new Item[] {whiteCrystalGiant, redCrystalGiant, pomPomGrassGiant, autumnGrassGiant, noelGrassGiant, greenCrystalGiant, fireflowerGiant, fourLeafCloverGiant, ironleafGiant, emeryFlowerGiant, blueCrystalGiant, lampGrassGiant, cherryGrassGiant, charmBlueGiant, pinkCatGiant, moondropFlowerGiant, toyherbGiant};
	public static final Item[] FLOWERSEEDS = new Item[] {whiteCrystalSeeds, redCrystalSeeds, pomPomGrassSeeds, autumnGrassSeeds, noelGrassSeeds, greenCrystalSeeds, fireflowerSeeds, fourLeafCloverSeeds, ironleafSeeds, emeryFlowerSeeds, blueCrystalSeeds, lampGrassSeeds, cherryGrassSeeds, charmBlueSeeds, pinkCatSeeds, moondropSeeds, toyherbSeeds};
	
	public static final Item[] HERBS = new Item[] {elliLeaves, witheredGrass, weeds, whiteGrass, indigoGrass, purpleGrass, greenGrass, blueGrass, yellowGrass, redGrass, orangeGrass, blackGrass, antidoteGrass, medicinalHerb};
	public static final Item[] FOOD = new Item[] {
			riceFlour, curryPowder, oil, flour, honey, yogurt, cheese, mayonnaise, eggL, eggM, eggS, milkL, milkM, milkS, wine, chocolate, rice, disastrousDish, failedDish, mixedHerbs, sourDrop, sweetPowder, heavySpice,
			turnipHeaven, pickleMix, salmonOnigiri, onigiri, relaxTeaLeaves, pickles, pickledTurnip, raisinBread, bambooRice, fruitSandwich, sandwich, salad, 
			shrimpSashimi, lobsterSashimi, blowfishSashimi, lampSquidSashimi, sunsquidSashimi, squidSashimi, fallSashimi, turbotSashimi, flounderSashimi, pikeSashimi, needlefishSashimi, sardineSashimi, tunaSashimi, yellowtailSashimi, skipjackSashimi, girellaSashimi, loverSashimi, glitterSashimi, snapperSashimi, taimenSashimi, cherrySashimi, salmonSashimi, rainbowSashimi, charSashimi, troutSashimi,
			omeletRice, omelet, friedEggs, misoEggplant, cornCereal, popcorn, croquettes, frenchFries, cabbageCakes, friedRice, friedVeggies, 
			grilledSandFlounder, grilledShrimp, grilledLobster, grilledBlowfish, grilledLampSquid, grilledSunsquid, grilledSquid, grilledFallFlounder, grilledTurbot, grilledFlounder, saltedPike, grilledNeedlefish, driedSardines, tunaTeriyaki, saltedPondSmelt, grilledYellowtail, grilledMackerel, grilledSkipjack, grilledLoverSnapper, grilledGlitterSnapper, grilledGirella, grilledSnapper, grilledGibelio, grilledCrucianCarp, saltedTaimen, saltedSalmon, saltedChub, saltedCherrySalmon, saltedRainbowTrout, saltedChar, saltedMasuTrout, dryCurry, risotto, gyoza, pancakes, tempura, friedUdon, donut, frenchToast, curryBread, bakedApple,
			curryRice, stew, eggBowl, tempuraBowl, milkPorridge, ricePorridge, tempuraUdon, curryUdon, udon, cheeseFondue, marmalade, grapeJam, appleJam, strawberryJam, boiledGyoza, glazedYam, boiledEgg, boiledSpinach, boiledPumpkin, grapeLiqueur, hotChocolate, hotMilk, 
			bread, dumplings, pumpkinFlan, flan, chocolateSponge, poundCake, steamedGyoza, curryManju, chineseManju, meatDumpling, cheeseBread, steamedBread, 
			iceCream, hotJuice, preludetoLove, goldJuice, butter, ketchup, mixedSmoothie, mixedJuice, veggieSmoothie, vegetableJuice, fruitSmoothie, fruitJuice, strawberryMilk, appleJuice, orangeJuice, grapeJuice, tomatoJuice, pineappleJuice, 
			applePie, cheesecake, chocolateCake, cake, chocoCookie, cookie, yamoftheAges, seafoodGratin, gratin, seafoodDoria, doria, seafoodPizza, pizza, butterRoll, jamRoll, toast, sweetPotato, bakedOnigiri, cornontheCob, rockfishStew, unionStew, grilledMiso, relaxTea, royalCurry, ultimateCurry, 
			mushroom, mushroomMonarch, mealyApple, orange, grapes, bambooSprout};
	
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
	    for(Item item : MEDICINE)
	    	event.getRegistry().register(item);
	    for(Item item : SPELLS)
	    	event.getRegistry().register(item);
	    for(Item item : FISH)
	    	event.getRegistry().register(item);
	    for(Item item : CROPS)
	    	event.getRegistry().register(item);
	    for(Item item : CROPSGIANT)
	    	event.getRegistry().register(item);
	    for(Item item : FLOWERS)
	    	event.getRegistry().register(item);
	    for(Item item : FLOWERSGIANT)
	    	event.getRegistry().register(item);
	    for(Item item : CROPSEEDS)
	    	event.getRegistry().register(item);
	    for(Item item : FLOWERSEEDS)
	    	event.getRegistry().register(item);
	    for(Item item : CREATIVE)
	    	event.getRegistry().register(item);
	    for(Item item : HERBS)
	    	event.getRegistry().register(item);
	    for(Item item : FOOD)
	    	event.getRegistry().register(item);
	    event.getRegistry().register(recipe);
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static final void initModel(ModelRegistryEvent event)
	{
		for(Item item : TOOLS)
			registerDefaultModel(item);
		for(Item item : WEAPONS)
			registerDefaultModel(item);
		for(Item item : ARMOR)
			registerDefaultModel(item);
		for(Item item : ITEMBLOCKS)
			registerDefaultModel(item);
		for(Item item : MATERIALS)
			registerDefaultModel(item);
		for(Item item : SPELLS)
			registerDefaultModel(item);
	    for(Item item : MEDICINE)
	    	registerDefaultModel(item);
	    for(Item item : FISH)
	    	registerDefaultModel(item);
		for(Item item : CREATIVE)
			registerDefaultModel(item);
	    for(Item item : CROPS)
			registerDefaultModel(item);
	    for(Item item : CROPSGIANT)
			registerDefaultModel(item);
	    for(Item item : CROPSEEDS)
			registerDefaultModel(item);
	    for(Item item : FLOWERS)
	    	registerDefaultModel(item);
	    for(Item item : FLOWERSGIANT)
	    	registerDefaultModel(item);
	    for(Item item : FLOWERSEEDS)
	    	registerDefaultModel(item);
	    for(Item item : FOOD)
	    	registerDefaultModel(item);
	    for(Item item : HERBS)
	    	registerDefaultModel(item);
	}
	
	private static final void registerDefaultModel(Item item)
	{
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));		
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static final void recipeRender(ModelBakeEvent event)
	{
	    ModelLoader.setCustomMeshDefinition(recipe, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return new ModelResourceLocation(recipe.getRegistryName(), "inventory");
            }
        });
        event.getModelRegistry().putObject(new ModelResourceLocation(recipe.getRegistryName(), "inventory"), new BakedItemRecipeModel(event.getModelManager().getModel(new ModelResourceLocation(recipe.getRegistryName(), "inventory"))));    
	}
}
