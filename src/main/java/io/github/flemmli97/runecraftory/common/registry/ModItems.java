package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.enums.EnumMineralTier;
import io.github.flemmli97.runecraftory.api.enums.EnumToolTier;
import io.github.flemmli97.runecraftory.common.RFCreativeTabs;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemGiantCrops;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemMedicine;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemMushroom;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemRecipeBread;
import io.github.flemmli97.runecraftory.common.items.creative.ItemDebug;
import io.github.flemmli97.runecraftory.common.items.creative.ItemLevelUp;
import io.github.flemmli97.runecraftory.common.items.creative.ItemSkillUp;
import io.github.flemmli97.runecraftory.common.items.equipment.ItemAccessoireBase;
import io.github.flemmli97.runecraftory.common.items.equipment.ItemArmorBase;
import io.github.flemmli97.runecraftory.common.items.equipment.ItemSeedShield;
import io.github.flemmli97.runecraftory.common.items.tools.ItemBrush;
import io.github.flemmli97.runecraftory.common.items.tools.ItemFertilizer;
import io.github.flemmli97.runecraftory.common.items.tools.ItemPetInspector;
import io.github.flemmli97.runecraftory.common.items.tools.ItemStatIncrease;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolAxe;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolFishingRod;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolGlass;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolHammer;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolHoe;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolSickle;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolWateringCan;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemAxeBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemDualBladeBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemGloveBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemHammerBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemLongSwordBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemShortSwordBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpearBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpell;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.items.weapons.shortsword.ItemSeedSword;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.UseAction;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RuneCraftory.MODID);
    private static final Food foodProp = new Food.Builder().hunger(1).saturation(1).setAlwaysEdible().build();

    public static final RegistryObject<Item> hoeScrap = hoe(EnumToolTier.SCRAP);
    public static final RegistryObject<Item> hoeIron = hoe(EnumToolTier.IRON);
    public static final RegistryObject<Item> hoeSilver = hoe(EnumToolTier.SILVER);
    public static final RegistryObject<Item> hoeGold = hoe(EnumToolTier.GOLD);
    public static final RegistryObject<Item> hoePlatinum = hoe(EnumToolTier.PLATINUM);
    public static final RegistryObject<Item> wateringCanScrap = wateringCan(EnumToolTier.SCRAP);
    public static final RegistryObject<Item> wateringCanIron = wateringCan(EnumToolTier.IRON);
    public static final RegistryObject<Item> wateringCanSilver = wateringCan(EnumToolTier.SILVER);
    public static final RegistryObject<Item> wateringCanGold = wateringCan(EnumToolTier.GOLD);
    public static final RegistryObject<Item> wateringCanPlatinum = wateringCan(EnumToolTier.PLATINUM);
    public static final RegistryObject<Item> sickleScrap = sickle(EnumToolTier.SCRAP);
    public static final RegistryObject<Item> sickleIron = sickle(EnumToolTier.IRON);
    public static final RegistryObject<Item> sickleSilver = sickle(EnumToolTier.SILVER);
    public static final RegistryObject<Item> sickleGold = sickle(EnumToolTier.GOLD);
    public static final RegistryObject<Item> sicklePlatinum = sickle(EnumToolTier.PLATINUM);
    public static final RegistryObject<Item> hammerScrap = hammerTool(EnumToolTier.SCRAP);
    public static final RegistryObject<Item> hammerIron = hammerTool(EnumToolTier.IRON);
    public static final RegistryObject<Item> hammerSilver = hammerTool(EnumToolTier.SILVER);
    public static final RegistryObject<Item> hammerGold = hammerTool(EnumToolTier.GOLD);
    public static final RegistryObject<Item> hammerPlatinum = hammerTool(EnumToolTier.PLATINUM);
    public static final RegistryObject<Item> axeScrap = axeTool(EnumToolTier.SCRAP);
    public static final RegistryObject<Item> axeIron = axeTool(EnumToolTier.IRON);
    public static final RegistryObject<Item> axeSilver = axeTool(EnumToolTier.SILVER);
    public static final RegistryObject<Item> axeGold = axeTool(EnumToolTier.GOLD);
    public static final RegistryObject<Item> axePlatinum = axeTool(EnumToolTier.PLATINUM);
    public static final RegistryObject<Item> fishingRodScrap = fishingRod(EnumToolTier.SCRAP);
    public static final RegistryObject<Item> fishingRodIron = fishingRod(EnumToolTier.IRON);
    public static final RegistryObject<Item> fishingRodSilver = fishingRod(EnumToolTier.SILVER);
    public static final RegistryObject<Item> fishingRodGold = fishingRod(EnumToolTier.GOLD);
    public static final RegistryObject<Item> fishingRodPlatinum = fishingRod(EnumToolTier.PLATINUM);
    public static final RegistryObject<Item> inspector = ITEMS.register("inspector", () -> new ItemPetInspector(new Item.Properties().group(RFCreativeTabs.weaponToolTab)));
    public static final RegistryObject<Item> brush = ITEMS.register("brush", () -> new ItemBrush(new Item.Properties().group(RFCreativeTabs.weaponToolTab)));
    public static final RegistryObject<Item> glass = ITEMS.register("magnifying_glass", () -> new ItemToolGlass(new Item.Properties().group(RFCreativeTabs.weaponToolTab)));

    //Recovery and stuff
    public static final RegistryObject<Item> roundoff = medicine("roundoff", false);
    public static final RegistryObject<Item> paraGone = medicine("para_gone", false);
    public static final RegistryObject<Item> coldMed = medicine("cold_medicine", false);
    public static final RegistryObject<Item> antidote = medicine("antidote_potion", false);
    public static final RegistryObject<Item> recoveryPotion = medicine("recovery_potion", true);
    public static final RegistryObject<Item> healingPotion = medicine("healing_potion", true);
    public static final RegistryObject<Item> mysteryPotion = medicine("mystery_potion", true);
    public static final RegistryObject<Item> magicalPotion = medicine("magical_potion", true);
    public static final RegistryObject<Item> invinciroid = drinkable("invinciroid");
    public static final RegistryObject<Item> lovePotion = drinkable("love_potion");
    public static final RegistryObject<Item> formuade = drinkable("formuade");
    public static final RegistryObject<Item> leveliser = ITEMS.register("leveliser", () -> new ItemStatIncrease(ItemStatIncrease.Stat.LEVEL, new Item.Properties().group(RFCreativeTabs.medicine)));
    public static final RegistryObject<Item> heartDrink = ITEMS.register("heart_drink", () -> new ItemStatIncrease(ItemStatIncrease.Stat.HP, new Item.Properties().group(RFCreativeTabs.medicine)));
    public static final RegistryObject<Item> vitalGummi = ITEMS.register("vital_gummi", () -> new ItemStatIncrease(ItemStatIncrease.Stat.VIT, new Item.Properties().group(RFCreativeTabs.medicine)));
    public static final RegistryObject<Item> intelligencer = ITEMS.register("intelligencer", () -> new ItemStatIncrease(ItemStatIncrease.Stat.INT, new Item.Properties().group(RFCreativeTabs.medicine)));
    public static final RegistryObject<Item> protein = ITEMS.register("protein", () -> new ItemStatIncrease(ItemStatIncrease.Stat.STR, new Item.Properties().group(RFCreativeTabs.medicine)));
    public static final RegistryObject<Item> formularA = ITEMS.register("formular_a", () -> new ItemFertilizer(ItemFertilizer.formularA, new Item.Properties().group(RFCreativeTabs.weaponToolTab)));
    public static final RegistryObject<Item> formularB = ITEMS.register("formular_b", () -> new ItemFertilizer(ItemFertilizer.formularB, new Item.Properties().group(RFCreativeTabs.weaponToolTab)));
    public static final RegistryObject<Item> formularC = ITEMS.register("formular_c", () -> new ItemFertilizer(ItemFertilizer.formularC, new Item.Properties().group(RFCreativeTabs.weaponToolTab)));
    public static final RegistryObject<Item> minimizer = ITEMS.register("minimizer", () -> new ItemFertilizer(ItemFertilizer.minimizer, new Item.Properties().group(RFCreativeTabs.weaponToolTab)));
    public static final RegistryObject<Item> giantizer = ITEMS.register("giantizer", () -> new ItemFertilizer(ItemFertilizer.giantizer, new Item.Properties().group(RFCreativeTabs.weaponToolTab)));
    public static final RegistryObject<Item> greenifier = ITEMS.register("greenifier", () -> new ItemFertilizer(ItemFertilizer.greenifier, new Item.Properties().group(RFCreativeTabs.weaponToolTab)));
    public static final RegistryObject<Item> greenifierPlus = ITEMS.register("greenifier_plus", () -> new ItemFertilizer(ItemFertilizer.greenifierPlus, new Item.Properties().group(RFCreativeTabs.weaponToolTab)));
    public static final RegistryObject<Item> wettablePowder = ITEMS.register("wettable_powder", () -> new ItemFertilizer(ItemFertilizer.wettable, new Item.Properties().group(RFCreativeTabs.weaponToolTab)));
    public static final RegistryObject<Item> objectX = ITEMS.register("object_x", () -> new Item(new Item.Properties().food(foodProp).group(RFCreativeTabs.medicine)) {
        @Override
        public UseAction getUseAction(ItemStack stack) {
            return UseAction.DRINK;
        }
    });

    //Weapons
    public static final RegistryObject<Item> seedSword = ITEMS.register("seed_sword_item", () -> new ItemSeedSword(new Item.Properties()));
    public static final RegistryObject<Item> broadSword = shortSword("broad_sword");
    public static final RegistryObject<Item> steelSword = shortSword("steel_sword");
    public static final RegistryObject<Item> steelSwordPlus = shortSword("steel_sword_plus");
    public static final RegistryObject<Item> cutlass = shortSword("cutlass");
    public static final RegistryObject<Item> aquaSword = shortSword("aqua_sword");
    public static final RegistryObject<Item> invisiBlade = shortSword("invisiblade");
    public static final RegistryObject<Item> defender = shortSword("defender");
    public static final RegistryObject<Item> burningSword = shortSword("burning_sword");
    public static final RegistryObject<Item> gorgeousSword = shortSword("gorgeous_sword");
    public static final RegistryObject<Item> gaiaSword = shortSword("gaia_sword");
    public static final RegistryObject<Item> snakeSword = shortSword("snake_sword");
    public static final RegistryObject<Item> luckBlade = shortSword("luck_blade");
    public static final RegistryObject<Item> platinumSword = shortSword("platinum_sword");
    public static final RegistryObject<Item> windSword = shortSword("wind_sword");
    public static final RegistryObject<Item> chaosBlade = shortSword("chaos_blade");
    public static final RegistryObject<Item> sakura = shortSword("sakura");
    public static final RegistryObject<Item> sunspot = shortSword("sunspot");
    public static final RegistryObject<Item> durendal = shortSword("durendal");
    public static final RegistryObject<Item> aerialBlade = shortSword("aerial_blade");
    public static final RegistryObject<Item> grantale = shortSword("grantale");
    public static final RegistryObject<Item> smashBlade = shortSword("smash_blade");
    public static final RegistryObject<Item> icifier = shortSword("icifier");
    public static final RegistryObject<Item> soulEater = shortSword("soul_eater");
    public static final RegistryObject<Item> raventine = shortSword("raventine");
    public static final RegistryObject<Item> starSaber = shortSword("star_saber");
    public static final RegistryObject<Item> platinumSwordPlus = shortSword("platinum_sword_plus");
    public static final RegistryObject<Item> dragonSlayer = shortSword("dragon_slayer");
    public static final RegistryObject<Item> runeBlade = shortSword("rune_blade");
    public static final RegistryObject<Item> gladius = shortSword("gladius");
    public static final RegistryObject<Item> runeLegend = shortSword("rune_legend");
    public static final RegistryObject<Item> backScratcher = shortSword("back_scratcher");
    public static final RegistryObject<Item> spoon = shortSword("spoon");
    public static final RegistryObject<Item> veggieBlade = shortSword("veggie_blade");

    public static final RegistryObject<Item> claymore = longSword("claymore");
    public static final RegistryObject<Item> zweihaender = longSword("zweihaender");
    public static final RegistryObject<Item> zweihaenderPlus = longSword("zweihaender_plus");
    public static final RegistryObject<Item> greatSword = longSword("great_sword");
    public static final RegistryObject<Item> seaCutter = longSword("sea_cutter");
    public static final RegistryObject<Item> cycloneBlade = longSword("cyclone_blade");
    public static final RegistryObject<Item> poisonBlade = longSword("poison_blade");
    public static final RegistryObject<Item> katzbalger = longSword("katzbalger");
    public static final RegistryObject<Item> earthShade = longSword("earth_shade");
    public static final RegistryObject<Item> bigKnife = longSword("big_knife");
    public static final RegistryObject<Item> katana = longSword("katana");
    public static final RegistryObject<Item> flameSaber = longSword("flame_saber");
    public static final RegistryObject<Item> bioSmasher = longSword("bio_smasher");
    public static final RegistryObject<Item> snowCrown = longSword("snow_crown");
    public static final RegistryObject<Item> dancingDicer = longSword("dancing_dicer");
    public static final RegistryObject<Item> flamberge = longSword("flamberge");
    public static final RegistryObject<Item> flambergePlus = longSword("flamberge_plus");
    public static final RegistryObject<Item> volcanon = longSword("volcanon");
    public static final RegistryObject<Item> psycho = longSword("psycho");
    public static final RegistryObject<Item> shineBlade = longSword("shine_blade");
    public static final RegistryObject<Item> grandSmasher = longSword("grand_smasher");
    public static final RegistryObject<Item> belzebuth = longSword("belzebuth");
    public static final RegistryObject<Item> orochi = longSword("orochi");
    public static final RegistryObject<Item> punisher = longSword("punisher");
    public static final RegistryObject<Item> steelSlicer = longSword("steel_slicer");
    public static final RegistryObject<Item> moonShadow = longSword("moon_shadow");
    public static final RegistryObject<Item> blueEyedBlade = longSword("blue_eyed_blade");
    public static final RegistryObject<Item> balmung = longSword("balmung");
    public static final RegistryObject<Item> braveheart = longSword("braveheart");
    public static final RegistryObject<Item> forceElement = longSword("force_element");
    public static final RegistryObject<Item> heavensAsunder = longSword("heavens_asunder");
    public static final RegistryObject<Item> caliburn = longSword("caliburn");
    public static final RegistryObject<Item> dekash = longSword("dekash");
    public static final RegistryObject<Item> daicone = longSword("daicone");

    public static final RegistryObject<Item> spear = spear("spear");
    public static final RegistryObject<Item> woodStaff = spear("wood_staff");
    public static final RegistryObject<Item> lance = spear("lance");
    public static final RegistryObject<Item> lancePlus = spear("lance_plus");
    public static final RegistryObject<Item> needleSpear = spear("needle_spear");
    public static final RegistryObject<Item> trident = spear("trident");
    public static final RegistryObject<Item> waterSpear = spear("water_spear");
    public static final RegistryObject<Item> halberd = spear("halberd");
    public static final RegistryObject<Item> corsesca = spear("corsesca");
    public static final RegistryObject<Item> corsescaPlus = spear("corsesca_plus");
    public static final RegistryObject<Item> poisonSpear = spear("poison_spear");
    public static final RegistryObject<Item> fiveStaff = spear("five_staff");
    public static final RegistryObject<Item> heavyLance = spear("heavy_lance");
    public static final RegistryObject<Item> featherLance = spear("feather_lance");
    public static final RegistryObject<Item> iceberg = spear("iceberg");
    public static final RegistryObject<Item> bloodLance = spear("blood_lance");
    public static final RegistryObject<Item> magicalLance = spear("magical_lance");
    public static final RegistryObject<Item> flareLance = spear("flare_lance");
    public static final RegistryObject<Item> brionac = spear("brionac");
    public static final RegistryObject<Item> poisonQueen = spear("poison_queen");
    public static final RegistryObject<Item> monkStaff = spear("monk_staff");
    public static final RegistryObject<Item> metus = spear("metus");
    public static final RegistryObject<Item> silentGrave = spear("silent_grave");
    public static final RegistryObject<Item> overbreak = spear("overbreak");
    public static final RegistryObject<Item> bjor = spear("bjor");
    public static final RegistryObject<Item> belvarose = spear("belvarose");
    public static final RegistryObject<Item> gaeBolg = spear("gae_bolg");
    public static final RegistryObject<Item> dragonsFang = spear("dragons_fang");
    public static final RegistryObject<Item> gungnir = spear("gungnir");
    public static final RegistryObject<Item> legion = spear("legion");
    public static final RegistryObject<Item> pitchfork = spear("pitchfork");
    public static final RegistryObject<Item> safetyLance = spear("safety_lance");
    public static final RegistryObject<Item> pineClub = spear("pine_club");

    public static final RegistryObject<Item> battleAxe = axe("battle_axe");
    public static final RegistryObject<Item> battleScythe = axe("battle_scythe");
    public static final RegistryObject<Item> poleAxe = axe("pole_axe");
    public static final RegistryObject<Item> poleAxePlus = axe("pole_axe_plus");
    public static final RegistryObject<Item> greatAxe = axe("great_axe");
    public static final RegistryObject<Item> tomohawk = axe("tomohawk");
    public static final RegistryObject<Item> basiliskFang = axe("basilisk_fang");
    public static final RegistryObject<Item> rockAxe = axe("rock_axe");
    public static final RegistryObject<Item> demonAxe = axe("demon_axe");
    public static final RegistryObject<Item> frostAxe = axe("frost_axe");
    public static final RegistryObject<Item> crescentAxe = axe("crescent_axe");
    public static final RegistryObject<Item> crescentAxePlus = axe("crescent_axe_plus");
    public static final RegistryObject<Item> heatAxe = axe("heat_axe");
    public static final RegistryObject<Item> doubleEdge = axe("double_edge");
    public static final RegistryObject<Item> alldale = axe("alldale");
    public static final RegistryObject<Item> devilFinger = axe("devil_finger");
    public static final RegistryObject<Item> executioner = axe("executioner");
    public static final RegistryObject<Item> saintAxe = axe("saint_axe");
    public static final RegistryObject<Item> axe = axe("axe");
    public static final RegistryObject<Item> lollipop = axe("lollipop");

    public static final RegistryObject<Item> battleHammer = hammer("battle_hammer");
    public static final RegistryObject<Item> bat = hammer("bat");
    public static final RegistryObject<Item> warHammer = hammer("war_hammer");
    public static final RegistryObject<Item> warHammerPlus = hammer("war_hammer_plus");
    public static final RegistryObject<Item> ironBat = hammer("iron_bat");
    public static final RegistryObject<Item> greatHammer = hammer("great_hammer");
    public static final RegistryObject<Item> iceHammer = hammer("ice_hammer");
    public static final RegistryObject<Item> boneHammer = hammer("bone_hammer");
    public static final RegistryObject<Item> strongStone = hammer("strong_stone");
    public static final RegistryObject<Item> flameHammer = hammer("flame_hammer");
    public static final RegistryObject<Item> gigantHammer = hammer("gigant_hammer");
    public static final RegistryObject<Item> skyHammer = hammer("sky_hammer");
    public static final RegistryObject<Item> gravitonHammer = hammer("graviton_hammer");
    public static final RegistryObject<Item> spikedHammer = hammer("spiked_hammer");
    public static final RegistryObject<Item> crystalHammer = hammer("crystal_hammer");
    public static final RegistryObject<Item> schnabel = hammer("schnabel");
    public static final RegistryObject<Item> gigantHammerPlus = hammer("gigant_hammer_plus");
    public static final RegistryObject<Item> kongo = hammer("kongo");
    public static final RegistryObject<Item> mjolnir = hammer("mjolnir");
    public static final RegistryObject<Item> fatalCrush = hammer("fatal_crush");
    public static final RegistryObject<Item> splashStar = hammer("splash_star");
    public static final RegistryObject<Item> hammer = hammer("hammer");
    public static final RegistryObject<Item> toyHammer = hammer("toy_hammer");

    public static final RegistryObject<Item> shortDagger = dualBlade("short_dagger");
    public static final RegistryObject<Item> steelEdge = dualBlade("steel_edge");
    public static final RegistryObject<Item> frostEdge = dualBlade("frost_edge");
    public static final RegistryObject<Item> ironEdge = dualBlade("iron_edge");
    public static final RegistryObject<Item> thiefKnife = dualBlade("thief_knife");
    public static final RegistryObject<Item> windEdge = dualBlade("wind_edge");
    public static final RegistryObject<Item> gorgeousLx = dualBlade("gourgeous_lx");
    public static final RegistryObject<Item> steelKatana = dualBlade("steel_katana");
    public static final RegistryObject<Item> twinBlade = dualBlade("twin_blade");
    public static final RegistryObject<Item> rampage = dualBlade("rampage");
    public static final RegistryObject<Item> salamander = dualBlade("salamander");
    public static final RegistryObject<Item> platinumEdge = dualBlade("platinum_edge");
    public static final RegistryObject<Item> sonicDagger = dualBlade("sonic_dagger");
    public static final RegistryObject<Item> chaosEdge = dualBlade("chaos_edge");
    public static final RegistryObject<Item> desertWind = dualBlade("desert_wind");
    public static final RegistryObject<Item> brokenWall = dualBlade("broken_wall");
    public static final RegistryObject<Item> forceDivide = dualBlade("force_divide");
    public static final RegistryObject<Item> heartFire = dualBlade("heart_fire");
    public static final RegistryObject<Item> orcusSword = dualBlade("orcus_sword");
    public static final RegistryObject<Item> deepBlizzard = dualBlade("deep_blizzard");
    public static final RegistryObject<Item> darkInvitation = dualBlade("dark_invitation");
    public static final RegistryObject<Item> priestSaber = dualBlade("priest_saber");
    public static final RegistryObject<Item> efreet = dualBlade("efreet");
    public static final RegistryObject<Item> dragoonClaw = dualBlade("dragoon_claw");
    public static final RegistryObject<Item> emeraldEdge = dualBlade("emerald_edge");
    public static final RegistryObject<Item> runeEdge = dualBlade("rune_edge");
    public static final RegistryObject<Item> earnestEdge = dualBlade("earnest_edge");
    public static final RegistryObject<Item> twinJustice = dualBlade("twin_justice");
    public static final RegistryObject<Item> doubleScratch = dualBlade("double_scratch");
    public static final RegistryObject<Item> acutorimass = dualBlade("acutorimass");
    public static final RegistryObject<Item> twinLeeks = dualBlade("twin_leeks");

    public static final RegistryObject<Item> leatherGlove = gloves("leather_glove");
    public static final RegistryObject<Item> brassKnuckles = gloves("brass_knuckles");
    public static final RegistryObject<Item> kote = gloves("kote");
    public static final RegistryObject<Item> gloves = gloves("gloves");
    public static final RegistryObject<Item> bearClaws = gloves("bear_claws");
    public static final RegistryObject<Item> fistEarth = gloves("fist_of_earth");
    public static final RegistryObject<Item> fistFire = gloves("fist_of_fire");
    public static final RegistryObject<Item> fistWater = gloves("fist_of_water");
    public static final RegistryObject<Item> dragonClaws = gloves("dragon_claws");
    public static final RegistryObject<Item> fistDark = gloves("fist_of_dark");
    public static final RegistryObject<Item> fistWind = gloves("fist_of_wind");
    public static final RegistryObject<Item> fistLight = gloves("fist_of_light");
    public static final RegistryObject<Item> catPunch = gloves("cat_punch");
    public static final RegistryObject<Item> animalPuppets = gloves("animal_puppets");
    public static final RegistryObject<Item> ironleafFists = gloves("ironleaf_fists");
    public static final RegistryObject<Item> caestus = gloves("caestus");
    public static final RegistryObject<Item> golemPunch = gloves("golem_punch");
    public static final RegistryObject<Item> godHand = gloves("hand_of_god");
    public static final RegistryObject<Item> bazalKatar = gloves("bazal_katar");
    public static final RegistryObject<Item> fenrir = gloves("fenrir");

    public static final RegistryObject<Item> rod = staff("rod", EnumElement.FIRE);
    public static final RegistryObject<Item> amethystRod = staff("amethyst_rod", EnumElement.EARTH);
    public static final RegistryObject<Item> aquamarineRod = staff("aquamarine_rod", EnumElement.WATER);
    public static final RegistryObject<Item> friendlyRod = staff("friendly_rod", EnumElement.LOVE);
    public static final RegistryObject<Item> loveLoveRod = staff("love_love_rod", EnumElement.LOVE);
    public static final RegistryObject<Item> staff = staff("staf", EnumElement.EARTH);
    public static final RegistryObject<Item> emeraldRod = staff("emerald_rod", EnumElement.WIND);
    public static final RegistryObject<Item> silverStaff = staff("silver_staff", EnumElement.DARK);
    public static final RegistryObject<Item> flareStaff = staff("flare_staff", EnumElement.FIRE);
    public static final RegistryObject<Item> rubyRod = staff("ruby_rod", EnumElement.FIRE);
    public static final RegistryObject<Item> sapphireRod = staff("sapphire_rod", EnumElement.LIGHT);
    public static final RegistryObject<Item> earthStaff = staff("earth_staff", EnumElement.EARTH);
    public static final RegistryObject<Item> lightningWand = staff("lightning_wand", EnumElement.WIND);
    public static final RegistryObject<Item> iceStaff = staff("ice_staff", EnumElement.WATER);
    public static final RegistryObject<Item> diamondRod = staff("diamond_rod", EnumElement.DARK);
    public static final RegistryObject<Item> wizardsStaff = staff("wizards_staff", EnumElement.LIGHT);
    public static final RegistryObject<Item> magesStaff = staff("mages_staff", EnumElement.EARTH);
    public static final RegistryObject<Item> shootingStarStaff = staff("shooting_star_staff", EnumElement.LIGHT);
    public static final RegistryObject<Item> hellBranch = staff("hell_branch", EnumElement.DARK);
    public static final RegistryObject<Item> crimsonStaff = staff("crimson_staff", EnumElement.FIRE);
    public static final RegistryObject<Item> bubbleStaff = staff("bubble_staff", EnumElement.WATER);
    public static final RegistryObject<Item> gaiaRod = staff("gaia_rod", EnumElement.EARTH);
    public static final RegistryObject<Item> cycloneRod = staff("cyclone_rod", EnumElement.WIND);
    public static final RegistryObject<Item> stormWand = staff("storm_wand", EnumElement.WIND);
    public static final RegistryObject<Item> runeStaff = staff("rune_staff", EnumElement.LIGHT);
    public static final RegistryObject<Item> magesStaffPlus = staff("mages_staff_plus", EnumElement.LOVE);
    public static final RegistryObject<Item> magicBroom = staff("magic_broom", EnumElement.WIND);
    public static final RegistryObject<Item> magicShot = staff("magic_shot", EnumElement.LOVE);
    public static final RegistryObject<Item> hellCurse = staff("hell_curse", EnumElement.DARK);
    public static final RegistryObject<Item> algernon = staff("algernon", EnumElement.EARTH);
    public static final RegistryObject<Item> sorceresWand = staff("sorceres_wand", EnumElement.LIGHT);
    public static final RegistryObject<Item> basket = staff("basket", EnumElement.LOVE);
    public static final RegistryObject<Item> goldenTurnipStaff = staff("golden_turnip_staff", EnumElement.LOVE);
    public static final RegistryObject<Item> sweetPotatoStaff = staff("sweet_potato_staff", EnumElement.LOVE);
    public static final RegistryObject<Item> elvishHarp = staff("elvish_harp", EnumElement.LOVE);
    public static final RegistryObject<Item> syringe = staff("syringe", EnumElement.WATER);

    public static final RegistryObject<Item> engagementRing = accessoire("engagement_ring");
    public static final RegistryObject<Item> cheapBracelet = accessoire("cheap_bracelet");
    public static final RegistryObject<Item> bronzeBracelet = accessoire("bronze_bracelet");
    public static final RegistryObject<Item> silverBracelet = accessoire("silver_bracelet");
    public static final RegistryObject<Item> goldBracelet = accessoire("gold_bracelet");
    public static final RegistryObject<Item> platinumBracelet = accessoire("platinum_bracelet");
    public static final RegistryObject<Item> silverRing = accessoire("silver_ring");
    public static final RegistryObject<Item> shieldRing = accessoire("shield_ring");
    public static final RegistryObject<Item> criticalRing = accessoire("critical_ring");
    public static final RegistryObject<Item> silentRing = accessoire("silent_ring");
    public static final RegistryObject<Item> paralysisRing = accessoire("paralysis_ring");
    public static final RegistryObject<Item> poisonRing = accessoire("poison_ring");
    public static final RegistryObject<Item> magicRing = accessoire("magic_ring");
    public static final RegistryObject<Item> throwingRing = accessoire("throwing_ring");
    public static final RegistryObject<Item> stayUpRing = accessoire("stay_up_ring");
    public static final RegistryObject<Item> aquamarineRing = accessoire("aquamarine_ring");
    public static final RegistryObject<Item> amethystRing = accessoire("amethyst_ring");
    public static final RegistryObject<Item> emeraldRing = accessoire("emerald_ring");
    public static final RegistryObject<Item> sapphireRing = accessoire("sapphire_ring");
    public static final RegistryObject<Item> rubyRing = accessoire("ruby_ring");
    public static final RegistryObject<Item> cursedRing = accessoire("cursed_ring");
    public static final RegistryObject<Item> diamondRing = accessoire("diamond_ring");
    public static final RegistryObject<Item> aquamarineBrooch = accessoire("aquamarine_brooch");
    public static final RegistryObject<Item> amethystBrooch = accessoire("amethyst_brooch");
    public static final RegistryObject<Item> emeraldBrooch = accessoire("emerald_brooch");
    public static final RegistryObject<Item> sapphireBrooch = accessoire("sapphire_brooch");
    public static final RegistryObject<Item> rubyBrooch = accessoire("ruby_brooch");
    public static final RegistryObject<Item> diamondBrooch = accessoire("diamond_brooch");
    public static final RegistryObject<Item> dolphinBrooch = accessoire("dolphin_brooch");
    public static final RegistryObject<Item> fireRing = accessoire("fire_ring");
    public static final RegistryObject<Item> windRing = accessoire("wind_ring");
    public static final RegistryObject<Item> waterRing = accessoire("water_ring");
    public static final RegistryObject<Item> earthRing = accessoire("earth_ring");
    public static final RegistryObject<Item> happyRing = accessoire("happy_ring");
    public static final RegistryObject<Item> silverPendant = accessoire("silver_pendant");
    public static final RegistryObject<Item> starPendant = accessoire("star_pendant");
    public static final RegistryObject<Item> sunPendant = accessoire("sun_pendant");
    public static final RegistryObject<Item> fieldPendant = accessoire("field_pendant");
    public static final RegistryObject<Item> dewPendant = accessoire("dew_pendant");
    public static final RegistryObject<Item> earthPendant = accessoire("earth_pendant");
    public static final RegistryObject<Item> heartPendant = accessoire("heart_pendant");
    public static final RegistryObject<Item> strangePendant = accessoire("strange_pendant");
    public static final RegistryObject<Item> anettesNecklace = accessoire("anettes_necklace");
    public static final RegistryObject<Item> workGloves = accessoire("work_gloves");
    public static final RegistryObject<Item> glovesAccess = accessoire("gloves_accessory");
    public static final RegistryObject<Item> powerGloves = accessoire("power_gloves");
    public static final RegistryObject<Item> earrings = accessoire("earrings");
    public static final RegistryObject<Item> witchEarrings = accessoire("witch_earrings");
    public static final RegistryObject<Item> magicEarrings = accessoire("magic_earrings", EquipmentSlotType.HEAD);
    public static final RegistryObject<Item> charm = accessoire("charm");
    public static final RegistryObject<Item> holyAmulet = accessoire("holy_amulet");
    public static final RegistryObject<Item> rosary = accessoire("rosary");
    public static final RegistryObject<Item> talisman = accessoire("talisman");
    public static final RegistryObject<Item> magicCharm = accessoire("magic_charm");
    public static final RegistryObject<Item> leatherBelt = accessoire("leather_belt");
    public static final RegistryObject<Item> luckyStrike = accessoire("lucky_strike");
    public static final RegistryObject<Item> champBelt = accessoire("champ_belt");
    public static final RegistryObject<Item> handKnitScarf = accessoire("hand_knit_scarf");
    public static final RegistryObject<Item> fluffyScarf = accessoire("fluffy_scarf");
    public static final RegistryObject<Item> herosProof = accessoire("heros_proof");
    public static final RegistryObject<Item> proofOfWisdom = accessoire("proof_of_wisdom");
    public static final RegistryObject<Item> artOfAttack = accessoire("art_of_attack");
    public static final RegistryObject<Item> artOfDefense = accessoire("art_of_defense");
    public static final RegistryObject<Item> artOfMagic = accessoire("art_of_magic");
    public static final RegistryObject<Item> badge = accessoire("badge");
    public static final RegistryObject<Item> courageBadge = accessoire("courage_badge");

    public static final RegistryObject<Item> shirt = equipment(EquipmentSlotType.CHEST, "shirt");
    public static final RegistryObject<Item> vest = equipment(EquipmentSlotType.CHEST, "vest");
    public static final RegistryObject<Item> cottonCloth = equipment(EquipmentSlotType.CHEST, "cotton_cloth");
    public static final RegistryObject<Item> mail = equipment(EquipmentSlotType.CHEST, "mail");
    public static final RegistryObject<Item> chainMail = equipment(EquipmentSlotType.CHEST, "chain_mail");
    public static final RegistryObject<Item> scaleVest = equipment(EquipmentSlotType.CHEST, "scale_vest");
    public static final RegistryObject<Item> sparklingShirt = equipment(EquipmentSlotType.CHEST, "sparkling_shirt");
    public static final RegistryObject<Item> windCloak = equipment(EquipmentSlotType.CHEST, "wind_cloak");
    public static final RegistryObject<Item> protector = equipment(EquipmentSlotType.CHEST, "protector");
    public static final RegistryObject<Item> platinumMail = equipment(EquipmentSlotType.CHEST, "platinum_mail");
    public static final RegistryObject<Item> lemellarVest = equipment(EquipmentSlotType.CHEST, "lemellar_vest");
    public static final RegistryObject<Item> mercenarysCloak = equipment(EquipmentSlotType.CHEST, "mercenarys_cloak");
    public static final RegistryObject<Item> woolyShirt = equipment(EquipmentSlotType.CHEST, "wooly_shirt");
    public static final RegistryObject<Item> elvishCloak = equipment(EquipmentSlotType.CHEST, "elvish_cloak");
    public static final RegistryObject<Item> dragonCloak = equipment(EquipmentSlotType.CHEST, "dragon_cloak");
    public static final RegistryObject<Item> powerProtector = equipment(EquipmentSlotType.CHEST, "power_protector");
    public static final RegistryObject<Item> runeVest = equipment(EquipmentSlotType.CHEST, "rune_vest");
    public static final RegistryObject<Item> royalGarter = equipment(EquipmentSlotType.CHEST, "royal_garter");
    public static final RegistryObject<Item> fourDragonsVest = equipment(EquipmentSlotType.CHEST, "four_dragons_vest");

    public static final RegistryObject<Item> headband = equipment(EquipmentSlotType.HEAD, "headband");
    public static final RegistryObject<Item> blueRibbon = equipment(EquipmentSlotType.HEAD, "blue_ribbon");
    public static final RegistryObject<Item> greenRibbon = equipment(EquipmentSlotType.HEAD, "green_ribbon");
    public static final RegistryObject<Item> purpleRibbon = equipment(EquipmentSlotType.HEAD, "purple_ribbon");
    public static final RegistryObject<Item> spectacles = equipment(EquipmentSlotType.HEAD, "spectacles");
    public static final RegistryObject<Item> strawHat = equipment(EquipmentSlotType.HEAD, "straw_hat");
    public static final RegistryObject<Item> fancyHat = equipment(EquipmentSlotType.HEAD, "fancy_hat");
    public static final RegistryObject<Item> brandGlasses = equipment(EquipmentSlotType.HEAD, "brand_glasses");
    public static final RegistryObject<Item> cuteKnitting = equipment(EquipmentSlotType.HEAD, "cute_knitting");
    public static final RegistryObject<Item> intelligentGlasses = equipment(EquipmentSlotType.HEAD, "intelligent_glasses");
    public static final RegistryObject<Item> fireproofHood = equipment(EquipmentSlotType.HEAD, "fireproof_hood");
    public static final RegistryObject<Item> silkHat = equipment(EquipmentSlotType.HEAD, "silk_hat");
    public static final RegistryObject<Item> blackRibbon = equipment(EquipmentSlotType.HEAD, "black_ribbon");
    public static final RegistryObject<Item> lolitaHeaddress = equipment(EquipmentSlotType.HEAD, "lolita_headdress");
    public static final RegistryObject<Item> headdress = equipment(EquipmentSlotType.HEAD, "headdress");
    public static final RegistryObject<Item> yellowRibbon = equipment(EquipmentSlotType.HEAD, "yellow_ribbon");
    public static final RegistryObject<Item> catEars = equipment(EquipmentSlotType.HEAD, "cat_ears");
    public static final RegistryObject<Item> silverHairpin = equipment(EquipmentSlotType.HEAD, "silver_hairpin");
    public static final RegistryObject<Item> redRibbon = equipment(EquipmentSlotType.HEAD, "red_ribbon");
    public static final RegistryObject<Item> orangeRibbon = equipment(EquipmentSlotType.HEAD, "orange_ribbon");
    public static final RegistryObject<Item> whiteRibbon = equipment(EquipmentSlotType.HEAD, "white_ribbon");
    public static final RegistryObject<Item> fourSeasons = equipment(EquipmentSlotType.HEAD, "four_seasons");
    public static final RegistryObject<Item> feathersHat = equipment(EquipmentSlotType.HEAD, "feathers_hat");
    public static final RegistryObject<Item> goldHairpin = equipment(EquipmentSlotType.HEAD, "gold_hairpin");
    public static final RegistryObject<Item> indigoRibbon = equipment(EquipmentSlotType.HEAD, "indigo_ribbon");
    public static final RegistryObject<Item> crown = equipment(EquipmentSlotType.HEAD, "crown");
    public static final RegistryObject<Item> turnipHeadgear = equipment(EquipmentSlotType.HEAD, "turnip_headgear");
    public static final RegistryObject<Item> pumpkinHeadgear = equipment(EquipmentSlotType.HEAD, "pumpkin_headgear");

    public static final RegistryObject<Item> leatherBoots = equipment(EquipmentSlotType.FEET, "leather_boots");
    public static final RegistryObject<Item> freeFarmingShoes = equipment(EquipmentSlotType.FEET, "free_farming_shoes");
    public static final RegistryObject<Item> piyoSandals = equipment(EquipmentSlotType.FEET, "piyo_sandals");
    public static final RegistryObject<Item> secretShoes = equipment(EquipmentSlotType.FEET, "secret_shoes");
    public static final RegistryObject<Item> silverBoots = equipment(EquipmentSlotType.FEET, "silver_boots");
    public static final RegistryObject<Item> heavyBoots = equipment(EquipmentSlotType.FEET, "heavy_boots");
    public static final RegistryObject<Item> sneakingBoots = equipment(EquipmentSlotType.FEET, "sneaking_boots");
    public static final RegistryObject<Item> fastStepBoots = equipment(EquipmentSlotType.FEET, "fast_step_boots");
    public static final RegistryObject<Item> goldBoots = equipment(EquipmentSlotType.FEET, "gold_boots");
    public static final RegistryObject<Item> boneBoots = equipment(EquipmentSlotType.FEET, "bone_boots");
    public static final RegistryObject<Item> snowBoots = equipment(EquipmentSlotType.FEET, "snow_boots");
    public static final RegistryObject<Item> striderBoots = equipment(EquipmentSlotType.FEET, "strider_boots");
    public static final RegistryObject<Item> stepInBoots = equipment(EquipmentSlotType.FEET, "step_in_boots");
    public static final RegistryObject<Item> featherBoots = equipment(EquipmentSlotType.FEET, "feather_boots");
    public static final RegistryObject<Item> ghostBoots = equipment(EquipmentSlotType.FEET, "ghost_boots");
    public static final RegistryObject<Item> ironGeta = equipment(EquipmentSlotType.FEET, "iron_geta");
    public static final RegistryObject<Item> knightBoots = equipment(EquipmentSlotType.FEET, "knight_boots");
    public static final RegistryObject<Item> fairyBoots = equipment(EquipmentSlotType.FEET, "fairy_boots");
    public static final RegistryObject<Item> wetBoots = equipment(EquipmentSlotType.FEET, "wet_boots");
    public static final RegistryObject<Item> waterShoes = equipment(EquipmentSlotType.FEET, "water_shoes");
    public static final RegistryObject<Item> iceSkates = equipment(EquipmentSlotType.FEET, "ice_skates");
    public static final RegistryObject<Item> rocketWing = equipment(EquipmentSlotType.FEET, "rocket_wing");

    public static final RegistryObject<Item> seedShield = ITEMS.register("seed_shield_item", () -> new ItemSeedShield(new Item.Properties().maxStackSize(1)));
    public static final RegistryObject<Item> smallShield = shield("small_shield");
    public static final RegistryObject<Item> umbrella = shield("umbrella");
    public static final RegistryObject<Item> ironShield = shield("iron_shield");
    public static final RegistryObject<Item> monkeyPlush = shield("monkey_plush");
    public static final RegistryObject<Item> roundShield = shield("round_shield");
    public static final RegistryObject<Item> turtleShield = shield("turtle_shield");
    public static final RegistryObject<Item> chaosShield = shield("chaos_shield");
    public static final RegistryObject<Item> boneShield = shield("bone_shield");
    public static final RegistryObject<Item> magicShield = shield("magic_shield");
    public static final RegistryObject<Item> heavyShield = shield("heavy_shield");
    public static final RegistryObject<Item> platinumShield = shield("platinum_shield");
    public static final RegistryObject<Item> kiteShield = shield("kite_shield");
    public static final RegistryObject<Item> knightShield = shield("knight_shield");
    public static final RegistryObject<Item> elementShield = shield("element_shield");
    public static final RegistryObject<Item> magicalShield = shield("magical_shield");
    public static final RegistryObject<Item> prismShield = shield("prism_shield");
    public static final RegistryObject<Item> runeShield = shield("rune_shield");

    public static final RegistryObject<Item> itemBlockForge = blockItem("forge", () -> ModBlocks.forge);
    public static final RegistryObject<Item> itemBlockAccess = blockItem("accessory", () -> ModBlocks.accessory);
    public static final RegistryObject<Item> itemBlockCooking = blockItem("cooking", () -> ModBlocks.cooking);
    public static final RegistryObject<Item> itemBlockChem = blockItem("chemistry", () -> ModBlocks.chemistry);

    public static final RegistryObject<Item> mineralIron = mineral(EnumMineralTier.IRON);
    public static final RegistryObject<Item> mineralBronze = mineral(EnumMineralTier.BRONZE);
    public static final RegistryObject<Item> mineralSilver = mineral(EnumMineralTier.SILVER);
    public static final RegistryObject<Item> mineralGold = mineral(EnumMineralTier.GOLD);
    public static final RegistryObject<Item> mineralPlatinum = mineral(EnumMineralTier.PLATINUM);
    public static final RegistryObject<Item> mineralOrichalcum = mineral(EnumMineralTier.ORICHALCUM);
    public static final RegistryObject<Item> mineralDiamond = mineral(EnumMineralTier.DIAMOND);
    public static final RegistryObject<Item> mineralDragonic = mineral(EnumMineralTier.DRAGONIC);
    public static final RegistryObject<Item> mineralAquamarine = mineral(EnumMineralTier.AQUAMARINE);
    public static final RegistryObject<Item> mineralAmethyst = mineral(EnumMineralTier.AMETHYST);
    public static final RegistryObject<Item> mineralRuby = mineral(EnumMineralTier.RUBY);
    public static final RegistryObject<Item> mineralEmerald = mineral(EnumMineralTier.EMERALD);
    public static final RegistryObject<Item> mineralSapphire = mineral(EnumMineralTier.SAPPHIRE);

    public static final RegistryObject<Item> brokenMineralIron = brokenMineral(EnumMineralTier.IRON);
    public static final RegistryObject<Item> brokenMineralBronze = brokenMineral(EnumMineralTier.BRONZE);
    public static final RegistryObject<Item> brokenMineralSilver = brokenMineral(EnumMineralTier.SILVER);
    public static final RegistryObject<Item> brokenMineralGold = brokenMineral(EnumMineralTier.GOLD);
    public static final RegistryObject<Item> brokenMineralPlatinum = brokenMineral(EnumMineralTier.PLATINUM);
    public static final RegistryObject<Item> brokenMineralOrichalcum = brokenMineral(EnumMineralTier.ORICHALCUM);
    public static final RegistryObject<Item> brokenMineralDiamond = brokenMineral(EnumMineralTier.DIAMOND);
    public static final RegistryObject<Item> brokenMineralDragonic = brokenMineral(EnumMineralTier.DRAGONIC);
    public static final RegistryObject<Item> brokenMineralAquamarine = brokenMineral(EnumMineralTier.AQUAMARINE);
    public static final RegistryObject<Item> brokenMineralAmethyst = brokenMineral(EnumMineralTier.AMETHYST);
    public static final RegistryObject<Item> brokenMineralRuby = brokenMineral(EnumMineralTier.RUBY);
    public static final RegistryObject<Item> brokenMineralEmerald = brokenMineral(EnumMineralTier.EMERALD);
    public static final RegistryObject<Item> brokenMineralSapphire = brokenMineral(EnumMineralTier.SAPPHIRE);

    public static final RegistryObject<Item> bronze = mat("bronze");
    public static final RegistryObject<Item> silver = mat("silver");
    public static final RegistryObject<Item> platinum = mat("platinum");
    public static final RegistryObject<Item> orichalcum = mat("orichalcum");
    public static final RegistryObject<Item> dragonic = mat("dragonic_stone");
    public static final RegistryObject<Item> scrap = mat("scrap");
    public static final RegistryObject<Item> scrapPlus = mat("scrap_plus");
    public static final RegistryObject<Item> amethyst = mat("amethyst");
    public static final RegistryObject<Item> aquamarine = mat("aquamarine");
    public static final RegistryObject<Item> ruby = mat("ruby");
    public static final RegistryObject<Item> sapphire = mat("sapphire");
    public static final RegistryObject<Item> coreRed = mat("core_red");
    public static final RegistryObject<Item> coreBlue = mat("core_blue");
    public static final RegistryObject<Item> coreYellow = mat("core_yellow");
    public static final RegistryObject<Item> coreGreen = mat("core_green");
    public static final RegistryObject<Item> crystalSkull = mat("crystal_skull");
    public static final RegistryObject<Item> crystalWater = mat("crystal_water");
    public static final RegistryObject<Item> crystalEarth = mat("crystal_earth");
    public static final RegistryObject<Item> crystalFire = mat("crystal_fire");
    public static final RegistryObject<Item> crystalWind = mat("crystal_wind");
    public static final RegistryObject<Item> crystalLight = mat("crystal_light");
    public static final RegistryObject<Item> crystalDark = mat("crystal_dark");
    public static final RegistryObject<Item> crystalLove = mat("crystal_love");
    public static final RegistryObject<Item> crystalSmall = mat("crystal_small");
    public static final RegistryObject<Item> crystalBig = mat("crystal_big");
    public static final RegistryObject<Item> crystalMagic = mat("crystal_magic");
    public static final RegistryObject<Item> crystalRune = mat("crystal_rune");
    public static final RegistryObject<Item> crystalElectro = mat("crystal_electro");
    public static final RegistryObject<Item> stickThick = mat("stick_thick");
    public static final RegistryObject<Item> hornInsect = mat("horn_insect");
    public static final RegistryObject<Item> hornRigid = mat("horn_rigid");
    public static final RegistryObject<Item> hornDevil = mat("horn_devil");
    public static final RegistryObject<Item> plantStem = mat("plant_stem");
    public static final RegistryObject<Item> hornBull = mat("horn_bull");
    public static final RegistryObject<Item> movingBranch = mat("moving_branch");
    public static final RegistryObject<Item> glue = mat("glue");
    public static final RegistryObject<Item> devilBlood = mat("devil_blood");
    public static final RegistryObject<Item> paraPoison = mat("paralysis_poison");
    public static final RegistryObject<Item> poisonKing = mat("poison_king");
    public static final RegistryObject<Item> featherBlack = mat("feather_black");
    public static final RegistryObject<Item> featherThunder = mat("feather_thunder");
    public static final RegistryObject<Item> featherYellow = mat("feather_yellow");
    public static final RegistryObject<Item> dragonFin = mat("dragon_fin");
    public static final RegistryObject<Item> turtleShell = mat("turtle_shell");
    public static final RegistryObject<Item> fishFossil = mat("fish_fossil");
    public static final RegistryObject<Item> skull = mat("skull");
    public static final RegistryObject<Item> dragonBones = mat("dragon_bones");
    public static final RegistryObject<Item> tortoiseShell = mat("tortoise_shell");
    //public static final RegistryObject<Item> ammonite = mat("ammonite"); replaced with nautilus shell
    public static final RegistryObject<Item> rock = mat("rock");
    public static final RegistryObject<Item> stoneRound = mat("stone_round");
    public static final RegistryObject<Item> stoneTiny = mat("stone_tiny_golem");
    public static final RegistryObject<Item> stoneGolem = mat("stone_golem");
    public static final RegistryObject<Item> tabletGolem = mat("tablet_golem");
    public static final RegistryObject<Item> stoneSpirit = mat("stone_golem_spirit");
    public static final RegistryObject<Item> tabletTruth = mat("tablet_truth");
    public static final RegistryObject<Item> yarn = mat("yarn");
    public static final RegistryObject<Item> oldBandage = mat("old_bandage");
    public static final RegistryObject<Item> ambrosiasThorns = mat("ambrosias_thorns");
    public static final RegistryObject<Item> threadSpider = mat("thread_spider");
    public static final RegistryObject<Item> puppetryStrings = mat("puppetry_strings");
    public static final RegistryObject<Item> vine = mat("vine");
    public static final RegistryObject<Item> tailScorpion = mat("tail_scorpion");
    public static final RegistryObject<Item> strongVine = mat("strong_vine");
    public static final RegistryObject<Item> threadPretty = mat("thread_pretty");
    public static final RegistryObject<Item> tailChimera = mat("tail_chimera");
    public static final RegistryObject<Item> arrowHead = mat("arrowhead");
    public static final RegistryObject<Item> bladeShard = mat("blade_shard");
    public static final RegistryObject<Item> brokenHilt = mat("broken_hilt");
    public static final RegistryObject<Item> brokenBox = mat("broken_box");
    public static final RegistryObject<Item> bladeGlistening = mat("blade_glistening");
    public static final RegistryObject<Item> greatHammerShard = mat("great_hammer_shard");
    public static final RegistryObject<Item> hammerPiece = mat("hammer_piece");
    public static final RegistryObject<Item> shoulderPiece = mat("shoulder_piece");
    public static final RegistryObject<Item> piratesArmor = mat("pirates_armor");
    public static final RegistryObject<Item> screwRusty = mat("screw_rusty");
    public static final RegistryObject<Item> screwShiny = mat("screw_shiny");
    public static final RegistryObject<Item> rockShardLeft = mat("rock_shard_left");
    public static final RegistryObject<Item> rockShardRight = mat("rock_shard_right");
    public static final RegistryObject<Item> MTGUPlate = mat("mtgu_plate");
    public static final RegistryObject<Item> brokenIceWall = mat("broken_ice_wall");
    public static final RegistryObject<Item> furSmall = mat("fur_s");
    public static final RegistryObject<Item> furMedium = mat("fur_m");
    public static final RegistryObject<Item> furLarge = mat("fur_l");
    public static final RegistryObject<Item> fur = mat("fur");
    public static final RegistryObject<Item> furball = mat("furball");
    public static final RegistryObject<Item> downYellow = mat("down_yellow");
    public static final RegistryObject<Item> furQuality = mat("fur_quality");
    public static final RegistryObject<Item> furPuffy = mat("fur_puffy");
    public static final RegistryObject<Item> downPenguin = mat("down_penguin");
    public static final RegistryObject<Item> lightningMane = mat("lightning_mane");
    public static final RegistryObject<Item> furRedLion = mat("fur_red_lion");
    public static final RegistryObject<Item> furBlueLion = mat("fur_blue_lion");
    public static final RegistryObject<Item> chestHair = mat("chest_hair");
    public static final RegistryObject<Item> spore = mat("spore");
    public static final RegistryObject<Item> powderPoison = mat("powder_poison");
    public static final RegistryObject<Item> sporeHoly = mat("spore_holy");
    public static final RegistryObject<Item> fairyDust = mat("fairy_dust");
    public static final RegistryObject<Item> fairyElixier = mat("fairy_elixier");
    public static final RegistryObject<Item> root = mat("root");
    public static final RegistryObject<Item> powderMagic = mat("powder_magic");
    public static final RegistryObject<Item> powderMysterious = mat("powder_mysterious");
    public static final RegistryObject<Item> magic = mat("magic");
    public static final RegistryObject<Item> ashEarth = mat("ash_earth");
    public static final RegistryObject<Item> ashFire = mat("ash_fire");
    public static final RegistryObject<Item> ashWater = mat("ash_water");
    public static final RegistryObject<Item> turnipsMiracle = mat("turnips_miracle");
    public static final RegistryObject<Item> melodyBottle = mat("melody_bottle");
    public static final RegistryObject<Item> clothCheap = mat("cloth_cheap");
    public static final RegistryObject<Item> clothQuality = mat("cloth_quality");
    public static final RegistryObject<Item> clothQualityWorn = mat("cloth_quality_worn");
    public static final RegistryObject<Item> clothSilk = mat("cloth_silk");
    public static final RegistryObject<Item> ghostHood = mat("ghost_hood");
    public static final RegistryObject<Item> gloveGiant = mat("glove_giant");
    public static final RegistryObject<Item> gloveBlueGiant = mat("glove_blue_giant");
    public static final RegistryObject<Item> carapaceInsect = mat("carapace_insect");
    public static final RegistryObject<Item> carapacePretty = mat("carapace_pretty");
    public static final RegistryObject<Item> clothAncientOrc = mat("cloth_ancient_orc");
    public static final RegistryObject<Item> jawInsect = mat("jaw_insect");
    public static final RegistryObject<Item> clawPanther = mat("claw_panther");
    public static final RegistryObject<Item> clawMagic = mat("claw_magic");
    public static final RegistryObject<Item> fangWolf = mat("fang_wolf");
    public static final RegistryObject<Item> fangGoldWolf = mat("fang_gold_wolf");
    public static final RegistryObject<Item> clawPalm = mat("claw_palm");
    public static final RegistryObject<Item> clawMalm = mat("claw_malm");
    public static final RegistryObject<Item> giantsNail = mat("giants_nail");
    public static final RegistryObject<Item> clawChimera = mat("claw_chimera");
    public static final RegistryObject<Item> tuskIvory = mat("tusk_ivory");
    public static final RegistryObject<Item> tuskUnbrokenIvory = mat("tusk_unbroken_ivory");
    public static final RegistryObject<Item> scorpionPincer = mat("scorpion_pincer");
    public static final RegistryObject<Item> dangerousScissors = mat("dangerous_scissors");
    public static final RegistryObject<Item> propellorCheap = mat("propeller_cheap");
    public static final RegistryObject<Item> propellorQuality = mat("propellor_quality");
    public static final RegistryObject<Item> fangDragon = mat("fang_dragon");
    public static final RegistryObject<Item> jawQueen = mat("jaw_queen");
    public static final RegistryObject<Item> windDragonTooth = mat("wind_dragon_tooth");
    public static final RegistryObject<Item> giantsNailBig = mat("giants_nail_big");
    public static final RegistryObject<Item> scaleWet = mat("scale_wet");
    public static final RegistryObject<Item> scaleGrimoire = mat("scale_grimoire");
    public static final RegistryObject<Item> scaleDragon = mat("scale_dragon");
    public static final RegistryObject<Item> scaleCrimson = mat("scale_crimson");
    public static final RegistryObject<Item> scaleBlue = mat("scale_blue");
    public static final RegistryObject<Item> scaleGlitter = mat("scale_glitter");
    public static final RegistryObject<Item> scaleLove = mat("scale_love");
    public static final RegistryObject<Item> scaleBlack = mat("scale_black");
    public static final RegistryObject<Item> scaleFire = mat("scale_fire");
    public static final RegistryObject<Item> scaleEarth = mat("scale_earth");
    public static final RegistryObject<Item> scaleLegend = mat("scale_legend");
    public static final RegistryObject<Item> steelDouble = mat("steel_double");
    public static final RegistryObject<Item> steelTen = mat("steel_ten_fold");
    public static final RegistryObject<Item> glittaAugite = mat("glitta_augite");
    public static final RegistryObject<Item> invisStone = mat("invisible_stone");
    public static final RegistryObject<Item> lightOre = mat("light_ore");
    public static final RegistryObject<Item> runeSphereShard = mat("rune_sphere_shard");
    public static final RegistryObject<Item> shadeStone = mat("shade_stone");
    public static final RegistryObject<Item> racoonLeaf = mat("racoon_leaf");
    public static final RegistryObject<Item> icyNose = mat("icy_nose");
    public static final RegistryObject<Item> bigBirdsComb = mat("big_birds_comb");
    public static final RegistryObject<Item> rafflesiaPetal = mat("rafflesia_petal");
    public static final RegistryObject<Item> cursedDoll = mat("cursed_doll");
    public static final RegistryObject<Item> warriorsProof = mat("warriors_proof");
    public static final RegistryObject<Item> proofOfRank = mat("proof_of_rank");
    public static final RegistryObject<Item> throneOfEmpire = mat("throne_of_emire");
    public static final RegistryObject<Item> whiteStone = mat("white_stone");
    public static final RegistryObject<Item> rareCan = mat("rare_can");
    public static final RegistryObject<Item> can = mat("can");
    public static final RegistryObject<Item> boots = mat("boots");

    public static final RegistryObject<Item> lawn = mat("ayngondaia_lawn");

    //Skills and Magic
    public static final RegistryObject<Item> fireBallSmall = spell(() -> ModSpells.FIREBALL, "fireball");
    public static final RegistryObject<Item> fireBallBig = spell(() -> ModSpells.EMPTY, "fireball_big");
    public static final RegistryObject<Item> explosion = spell(() -> ModSpells.EMPTY, "explosion");
    public static final RegistryObject<Item> waterLaser = spell(() -> ModSpells.WATERLASER, "water_laser");
    public static final RegistryObject<Item> parallelLaser = spell(() -> ModSpells.EMPTY, "parallel_laser");
    public static final RegistryObject<Item> deltaLaser = spell(() -> ModSpells.EMPTY, "delta_laser");
    public static final RegistryObject<Item> screwRock = spell(() -> ModSpells.EMPTY, "screw_rock");
    public static final RegistryObject<Item> earthSpike = spell(() -> ModSpells.EMPTY, "earth_spike");
    public static final RegistryObject<Item> avengerRock = spell(() -> ModSpells.EMPTY, "avenger_rock");
    public static final RegistryObject<Item> sonicWind = spell(() -> ModSpells.EMPTY, "sonic_wind");
    public static final RegistryObject<Item> doubleSonic = spell(() -> ModSpells.DOUBLESONIC, "double_sonic");
    public static final RegistryObject<Item> penetrateSonic = spell(() -> ModSpells.EMPTY, "penetrate_sonic");
    public static final RegistryObject<Item> lightBarrier = spell(() -> ModSpells.EMPTY, "light_barrier");
    public static final RegistryObject<Item> shine = spell(() -> ModSpells.EMPTY, "shine");
    public static final RegistryObject<Item> prism = spell(() -> ModSpells.EMPTY, "prism");
    public static final RegistryObject<Item> darkSnake = spell(() -> ModSpells.EMPTY, "dark_snake");
    public static final RegistryObject<Item> darkBall = spell(() -> ModSpells.EMPTY, "dark_ball");
    public static final RegistryObject<Item> darkness = spell(() -> ModSpells.EMPTY, "darkness");
    public static final RegistryObject<Item> cure = spell(() -> ModSpells.EMPTY, "cure");
    public static final RegistryObject<Item> cureAll = spell(() -> ModSpells.EMPTY, "cure_all");
    public static final RegistryObject<Item> cureMaster = spell(() -> ModSpells.EMPTY, "cure_master");
    public static final RegistryObject<Item> mediPoison = spell(() -> ModSpells.MEDIPOISON, "medi_poison");
    public static final RegistryObject<Item> mediPara = spell(() -> ModSpells.MEDIPARA, "medi_paralysis");
    public static final RegistryObject<Item> mediSeal = spell(() -> ModSpells.MEDISEAL, "medi_seal");
    public static final RegistryObject<Item> greeting = spell(() -> ModSpells.EMPTY, "greeting");
    public static final RegistryObject<Item> powerWave = spell(() -> ModSpells.EMPTY, "power_wave");
    public static final RegistryObject<Item> dashSlash = spell(() -> ModSpells.EMPTY, "dash_slash");
    public static final RegistryObject<Item> rushAttack = spell(() -> ModSpells.EMPTY, "rush_attack");
    public static final RegistryObject<Item> roundBreak = spell(() -> ModSpells.EMPTY, "round_break");
    public static final RegistryObject<Item> mindThrust = spell(() -> ModSpells.EMPTY, "mind_thrust");
    public static final RegistryObject<Item> gust = spell(() -> ModSpells.EMPTY, "gust");
    public static final RegistryObject<Item> storm = spell(() -> ModSpells.EMPTY, "storm");
    public static final RegistryObject<Item> blitz = spell(() -> ModSpells.EMPTY, "blitz");
    public static final RegistryObject<Item> twinAttack = spell(() -> ModSpells.EMPTY, "twin_attack");
    public static final RegistryObject<Item> railStrike = spell(() -> ModSpells.EMPTY, "rail_strike");
    public static final RegistryObject<Item> windSlash = spell(() -> ModSpells.EMPTY, "wind_slash");
    public static final RegistryObject<Item> flashStrike = spell(() -> ModSpells.EMPTY, "flash_strike");
    public static final RegistryObject<Item> naiveBlade = spell(() -> ModSpells.EMPTY, "naive_blade");
    public static final RegistryObject<Item> steelHeart = spell(() -> ModSpells.EMPTY, "steel_heart");
    public static final RegistryObject<Item> deltaStrike = spell(() -> ModSpells.EMPTY, "delta_strike");
    public static final RegistryObject<Item> hurricane = spell(() -> ModSpells.EMPTY, "hurricane");
    public static final RegistryObject<Item> reaperSlash = spell(() -> ModSpells.EMPTY, "reaper_slash");
    public static final RegistryObject<Item> millionStrike = spell(() -> ModSpells.EMPTY, "million_strike");
    public static final RegistryObject<Item> axelDisaster = spell(() -> ModSpells.EMPTY, "axel_disaster");
    public static final RegistryObject<Item> stardustUpper = spell(() -> ModSpells.EMPTY, "stardust_upper");
    public static final RegistryObject<Item> tornadoSwing = spell(() -> ModSpells.EMPTY, "tornado_swing");
    public static final RegistryObject<Item> grandImpact = spell(() -> ModSpells.EMPTY, "grand_impact");
    public static final RegistryObject<Item> gigaSwing = spell(() -> ModSpells.EMPTY, "giga_swing");
    public static final RegistryObject<Item> upperCut = spell(() -> ModSpells.EMPTY, "upper_cut");
    public static final RegistryObject<Item> doubleKick = spell(() -> ModSpells.EMPTY, "double_kick");
    public static final RegistryObject<Item> straightPunch = spell(() -> ModSpells.EMPTY, "straight_punch");
    public static final RegistryObject<Item> nekoDamashi = spell(() -> ModSpells.EMPTY, "neko_damashi");
    public static final RegistryObject<Item> rushPunch = spell(() -> ModSpells.EMPTY, "rush_punch");
    public static final RegistryObject<Item> cyclone = spell(() -> ModSpells.EMPTY, "cyclone");
    public static final RegistryObject<Item> rapidMove = spell(() -> ModSpells.EMPTY, "rapid_move");
    public static final RegistryObject<Item> bonusConcerto = spell(() -> ModSpells.EMPTY, "bonus_concerto");
    public static final RegistryObject<Item> strikingMarch = spell(() -> ModSpells.EMPTY, "striking_march");
    public static final RegistryObject<Item> ironWaltz = spell(() -> ModSpells.EMPTY, "iron_waltz");
    public static final RegistryObject<Item> teleport = spell(() -> ModSpells.EMPTY, "teleport");

    public static final RegistryObject<Item> rockfish = fish("rockfish");
    public static final RegistryObject<Item> sandFlounder = fish("sand_flounder");
    public static final RegistryObject<Item> pondSmelt = fish("pond_smelt");
    public static final RegistryObject<Item> lobster = fish("lobster");
    public static final RegistryObject<Item> lampSquid = fish("lamb_squid");
    public static final RegistryObject<Item> cherrySalmon = fish("cherry_salmon");
    public static final RegistryObject<Item> fallFlounder = fish("fall_flounder");
    public static final RegistryObject<Item> girella = fish("girella");
    public static final RegistryObject<Item> tuna = fish("tuna");
    public static final RegistryObject<Item> crucianCarp = fish("crucian_carp");
    public static final RegistryObject<Item> yellowtail = fish("yellowtail");
    public static final RegistryObject<Item> blowfish = fish("blowfish");
    public static final RegistryObject<Item> flounder = fish("flounder");
    public static final RegistryObject<Item> rainbowTrout = fish("rainbow_trout");
    public static final RegistryObject<Item> loverSnapper = fish("lover_snapper");
    public static final RegistryObject<Item> snapper = fish("snapper");
    public static final RegistryObject<Item> shrimp = fish("shrimp");
    public static final RegistryObject<Item> sunsquid = fish("sunsquid");
    public static final RegistryObject<Item> pike = fish("pike");
    public static final RegistryObject<Item> needlefish = fish("needle_fish");
    public static final RegistryObject<Item> mackerel = fish("mackerel");
    public static final RegistryObject<Item> salmon = fish("salmon");
    public static final RegistryObject<Item> gibelio = fish("gibelio");
    public static final RegistryObject<Item> turbot = fish("turbot");
    public static final RegistryObject<Item> skipjack = fish("skipjack");
    public static final RegistryObject<Item> glitterSnapper = fish("glitter_snapper");
    public static final RegistryObject<Item> chub = fish("chub");
    public static final RegistryObject<Item> charFish = fish("char");
    public static final RegistryObject<Item> sardine = fish("sardine");
    public static final RegistryObject<Item> taimen = fish("taimen");
    public static final RegistryObject<Item> squid = fish("squid");
    public static final RegistryObject<Item> masuTrout = fish("masu_trout");

    public static final RegistryObject<Item> icon0 = ITEMS.register("icon_0", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> icon1 = ITEMS.register("icon_1", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> debug = ITEMS.register("debug_item", () -> new ItemDebug(new Item.Properties()));
    public static final RegistryObject<Item> level = ITEMS.register("level_item", () -> new ItemLevelUp(new Item.Properties()));
    public static final RegistryObject<Item> skill = ITEMS.register("skill_item", () -> new ItemSkillUp(new Item.Properties()));
    public static final RegistryObject<Item> tame = ITEMS.register("insta_tame", () -> new ItemDebug(new Item.Properties()));
    public static final RegistryObject<Item> entityLevel = ITEMS.register("entity_level_item", () -> new ItemDebug(new Item.Properties()));
    public static final RegistryObject<Item> unknown = ITEMS.register("unknown", () -> new Item(new Item.Properties()));

    //Crop items
    public static final RegistryObject<Item> turnipSeeds = seed("turnip", () -> ModBlocks.turnip);
    public static final RegistryObject<Item> turnipPinkSeeds = seed("turnip_pink", () -> ModBlocks.turnipPink);
    public static final RegistryObject<Item> cabbageSeeds = seed("cabbage", () -> ModBlocks.cabbage);
    public static final RegistryObject<Item> pinkMelonSeeds = seed("pink_melon", () -> ModBlocks.pinkMelon);
    public static final RegistryObject<Item> hotHotSeeds = seed("hot_hot_fruit", () -> ModBlocks.hotHotFruit);
    public static final RegistryObject<Item> goldTurnipSeeds = seed("golden_turnip", () -> ModBlocks.goldenTurnip);
    public static final RegistryObject<Item> goldPotatoSeeds = seed("golden_potato", () -> ModBlocks.goldenPotato);
    public static final RegistryObject<Item> goldPumpkinSeeds = seed("golden_pumpkin", () -> ModBlocks.goldenPumpkin);
    public static final RegistryObject<Item> goldCabbageSeeds = seed("golden_cabbage", () -> ModBlocks.goldenCabbage);
    public static final RegistryObject<Item> bokChoySeeds = seed("bok_choy", () -> ModBlocks.bokChoy);
    public static final RegistryObject<Item> leekSeeds = seed("leek", () -> ModBlocks.leek);
    public static final RegistryObject<Item> radishSeeds = seed("radish", () -> ModBlocks.radish);
    public static final RegistryObject<Item> greenPepperSeeds = seed("green_pepper", () -> ModBlocks.greenPepper);
    public static final RegistryObject<Item> spinachSeeds = seed("spinach", () -> ModBlocks.spinach);
    public static final RegistryObject<Item> yamSeeds = seed("yam", () -> ModBlocks.yam);
    public static final RegistryObject<Item> eggplantSeeds = seed("eggplant", () -> ModBlocks.eggplant);
    public static final RegistryObject<Item> pineappleSeeds = seed("pineapple", () -> ModBlocks.pineapple);
    public static final RegistryObject<Item> pumpkinSeeds = seed("pumpkin", () -> ModBlocks.pumpkin);
    public static final RegistryObject<Item> onionSeeds = seed("onion", () -> ModBlocks.onion);
    public static final RegistryObject<Item> cornSeeds = seed("corn", () -> ModBlocks.corn);
    public static final RegistryObject<Item> tomatoSeeds = seed("tomato", () -> ModBlocks.tomato);
    public static final RegistryObject<Item> strawberrySeeds = seed("strawberry", () -> ModBlocks.strawberry);
    public static final RegistryObject<Item> cucumberSeeds = seed("cucumber", () -> ModBlocks.cucumber);
    public static final RegistryObject<Item> fodderSeeds = seed("fodder", () -> ModBlocks.fodder);

    public static final RegistryObject<Item> turnip = crop("turnip", false);
    public static final RegistryObject<Item> turnipGiant = crop("turnip", true);
    public static final RegistryObject<Item> turnipPink = crop("turnip_pink", false);
    public static final RegistryObject<Item> turnipPinkGiant = crop("turnip_pink", true);
    public static final RegistryObject<Item> cabbage = crop("cabbage", false);
    public static final RegistryObject<Item> cabbageGiant = crop("cabbage", true);
    public static final RegistryObject<Item> pinkMelon = crop("pink_melon", false);
    public static final RegistryObject<Item> pinkMelonGiant = crop("pink_melon", true);
    public static final RegistryObject<Item> pineapple = crop("pineapple", false);
    public static final RegistryObject<Item> pineappleGiant = crop("pineapple", true);
    public static final RegistryObject<Item> strawberry = crop("strawberry", false);
    public static final RegistryObject<Item> strawberryGiant = crop("strawberry", true);
    public static final RegistryObject<Item> goldenTurnip = crop("golden_turnip", false);
    public static final RegistryObject<Item> goldenTurnipGiant = crop("golden_turnip", true);
    public static final RegistryObject<Item> goldenPotato = crop("golden_potato", false);
    public static final RegistryObject<Item> goldenPotatoGiant = crop("golden_potato", true);
    public static final RegistryObject<Item> goldenPumpkin = crop("golden_pumpkin", false);
    public static final RegistryObject<Item> goldenPumpkinGiant = crop("golden_pumpkin", true);
    public static final RegistryObject<Item> goldenCabbage = crop("golden_cabbage", false);
    public static final RegistryObject<Item> goldenCabbageGiant = crop("golden_cabbage", true);
    public static final RegistryObject<Item> hotHotFruit = crop("hot_hot_fruit", false);
    public static final RegistryObject<Item> hotHotFruitGiant = crop("hot_hot_fruit", true);
    public static final RegistryObject<Item> bokChoy = crop("bok_choy", false);
    public static final RegistryObject<Item> bokChoyGiant = crop("bok_choy", true);
    public static final RegistryObject<Item> leek = crop("leek", false);
    public static final RegistryObject<Item> leekGiant = crop("leek", true);
    public static final RegistryObject<Item> radish = crop("radish", false);
    public static final RegistryObject<Item> radishGiant = crop("radish", true);
    public static final RegistryObject<Item> spinach = crop("spinach", false);
    public static final RegistryObject<Item> spinachGiant = crop("spinach", true);
    public static final RegistryObject<Item> greenPepper = crop("green_pepper", false);
    public static final RegistryObject<Item> greenPepperGiant = crop("green_pepper", true);
    public static final RegistryObject<Item> yam = crop("yam", false);
    public static final RegistryObject<Item> yamGiant = crop("yam", true);
    public static final RegistryObject<Item> eggplant = crop("eggplant", false);
    public static final RegistryObject<Item> eggplantGiant = crop("eggplant", true);
    public static final RegistryObject<Item> tomato = crop("tomato", false);
    public static final RegistryObject<Item> tomatoGiant = crop("tomato", true);
    public static final RegistryObject<Item> corn = crop("corn", false);
    public static final RegistryObject<Item> cornGiant = crop("corn", true);
    public static final RegistryObject<Item> cucumber = crop("cucumber", false);
    public static final RegistryObject<Item> cucumberGiant = crop("cucumber", true);
    public static final RegistryObject<Item> pumpkin = crop("pumpkin", false);
    public static final RegistryObject<Item> pumpkinGiant = crop("pumpkin", true);
    public static final RegistryObject<Item> onion = crop("onion", false);
    public static final RegistryObject<Item> onionGiant = crop("onion", true);

    public static final RegistryObject<Item> fodder = mat("fodder");

    //Flowers
    public static final RegistryObject<Item> toyherbSeeds = seed("toyherb", () -> ModBlocks.toyherb);
    public static final RegistryObject<Item> moondropSeeds = seed("moondrop_flower", () -> ModBlocks.moondropFlower);
    public static final RegistryObject<Item> pinkCatSeeds = seed("pink_cat", () -> ModBlocks.pinkCat);
    public static final RegistryObject<Item> charmBlueSeeds = seed("charm_blue", () -> ModBlocks.charmBlue);
    public static final RegistryObject<Item> lampGrassSeeds = seed("lamp_grass", () -> ModBlocks.lampGrass);
    public static final RegistryObject<Item> cherryGrassSeeds = seed("cherry_grass", () -> ModBlocks.cherryGrass);
    public static final RegistryObject<Item> whiteCrystalSeeds = seed("white_crystal", () -> ModBlocks.whiteCrystal);
    public static final RegistryObject<Item> redCrystalSeeds = seed("red_crystal", () -> ModBlocks.redCrystal);
    public static final RegistryObject<Item> pomPomGrassSeeds = seed("pom_pom_grass", () -> ModBlocks.pomPomGrass);
    public static final RegistryObject<Item> autumnGrassSeeds = seed("autumn_grass", () -> ModBlocks.autumnGrass);
    public static final RegistryObject<Item> noelGrassSeeds = seed("noel_grass", () -> ModBlocks.noelGrass);
    public static final RegistryObject<Item> greenCrystalSeeds = seed("green_crystal", () -> ModBlocks.greenCrystal);
    public static final RegistryObject<Item> fireflowerSeeds = seed("fireflower", () -> ModBlocks.fireflower);
    public static final RegistryObject<Item> fourLeafCloverSeeds = seed("four_leaf_clover", () -> ModBlocks.fourLeafClover);
    public static final RegistryObject<Item> ironleafSeeds = seed("ironleaf", () -> ModBlocks.ironleaf);
    public static final RegistryObject<Item> emeryFlowerSeeds = seed("emery_flower", () -> ModBlocks.emeryFlower);
    public static final RegistryObject<Item> blueCrystalSeeds = seed("blue_crystal", () -> ModBlocks.blueCrystal);

    public static final RegistryObject<Item> whiteCrystal = crop("white_crystal", false);
    public static final RegistryObject<Item> whiteCrystalGiant = crop("white_crystal", true);
    public static final RegistryObject<Item> redCrystal = crop("red_crystal", false);
    public static final RegistryObject<Item> redCrystalGiant = crop("red_crystal", true);
    public static final RegistryObject<Item> pomPomGrass = crop("pom-pom_grass", false);
    public static final RegistryObject<Item> pomPomGrassGiant = crop("pom-pom_grass", true);
    public static final RegistryObject<Item> autumnGrass = crop("autumn_grass", false);
    public static final RegistryObject<Item> autumnGrassGiant = crop("autumn_grass", true);
    public static final RegistryObject<Item> noelGrass = crop("noel_grass", false);
    public static final RegistryObject<Item> noelGrassGiant = crop("noel_grass", true);
    public static final RegistryObject<Item> greenCrystal = crop("green_crystal", false);
    public static final RegistryObject<Item> greenCrystalGiant = crop("green_crystal", true);
    public static final RegistryObject<Item> fireflower = crop("fireflower", false);
    public static final RegistryObject<Item> fireflowerGiant = crop("fireflower", true);
    public static final RegistryObject<Item> fourLeafClover = crop("four_leaf_clover", false);
    public static final RegistryObject<Item> fourLeafCloverGiant = crop("four_leaf_clover", true);
    public static final RegistryObject<Item> ironleaf = crop("ironleaf", false);
    public static final RegistryObject<Item> ironleafGiant = crop("ironleaf", true);
    public static final RegistryObject<Item> emeryFlower = crop("emery_flower", false);
    public static final RegistryObject<Item> emeryFlowerGiant = crop("emery_flower", true);
    public static final RegistryObject<Item> blueCrystal = crop("blue_crystal", false);
    public static final RegistryObject<Item> blueCrystalGiant = crop("blue_crystal", true);
    public static final RegistryObject<Item> lampGrass = crop("lamp_grass", false);
    public static final RegistryObject<Item> lampGrassGiant = crop("lamp_grass", true);
    public static final RegistryObject<Item> cherryGrass = crop("cherry_grass", false);
    public static final RegistryObject<Item> cherryGrassGiant = crop("cherry_grass", true);
    public static final RegistryObject<Item> charmBlue = crop("charm_blue", false);
    public static final RegistryObject<Item> charmBlueGiant = crop("charm_blue", true);
    public static final RegistryObject<Item> pinkCat = crop("pink_cat", false);
    public static final RegistryObject<Item> pinkCatGiant = crop("pink_cat", true);
    public static final RegistryObject<Item> moondropFlower = crop("moondrop_flower", false);
    public static final RegistryObject<Item> moondropFlowerGiant = crop("moondrop_flower", true);
    public static final RegistryObject<Item> toyherb = crop("toyherb", false);
    public static final RegistryObject<Item> toyherbGiant = crop("toyherb", true);
    //Vanilla

    public static final RegistryObject<Item> potatoGiant = crop("potato", true);

    public static final RegistryObject<Item> carrotGiant = crop("carrot", true);

    //Special seeds

    public static final RegistryObject<Item> shieldSeeds = seed("shield", () -> ModBlocks.shieldCrop);

    public static final RegistryObject<Item> swordSeeds = seed("sword", () -> ModBlocks.swordCrop);

    public static final RegistryObject<Item> dungeonSeeds = seed("dungeon", () -> ModBlocks.dungeon);

    //Herbs

    public static final RegistryObject<Item> elliLeaves = herb("elli_leaves", () -> ModBlocks.elliLeaves);
    public static final RegistryObject<Item> witheredGrass = herb("withered_grass", () -> ModBlocks.witheredGrass);
    public static final RegistryObject<Item> weeds = herb("weeds", () -> ModBlocks.weeds);
    public static final RegistryObject<Item> whiteGrass = herb("white_grass", () -> ModBlocks.whiteGrass);
    public static final RegistryObject<Item> indigoGrass = herb("indigo_grass", () -> ModBlocks.indigoGrass);
    public static final RegistryObject<Item> purpleGrass = herb("purple_grass", () -> ModBlocks.purpleGrass);
    public static final RegistryObject<Item> greenGrass = herb("green_grass", () -> ModBlocks.greenGrass);
    public static final RegistryObject<Item> blueGrass = herb("blue_grass", () -> ModBlocks.blueGrass);
    public static final RegistryObject<Item> yellowGrass = herb("yellow_grass", () -> ModBlocks.yellowGrass);
    public static final RegistryObject<Item> redGrass = herb("red_grass", () -> ModBlocks.redGrass);
    public static final RegistryObject<Item> orangeGrass = herb("orange_grass", () -> ModBlocks.orangeGrass);
    public static final RegistryObject<Item> blackGrass = herb("black_grass", () -> ModBlocks.blackGrass);
    public static final RegistryObject<Item> antidoteGrass = herb("antidote_grass", () -> ModBlocks.antidoteGrass);
    public static final RegistryObject<Item> medicinalHerb = herb("medicinal_herb", () -> ModBlocks.medicinalHerb);
    public static final RegistryObject<Item> bambooSprout = herb("bamboo_sprout", () -> ModBlocks.bambooSprout);

    //Food

    public static final RegistryObject<Item> riceFlour = food("rice_flour");
    public static final RegistryObject<Item> curryPowder = food("curry_powder");
    public static final RegistryObject<Item> oil = food("oil");
    public static final RegistryObject<Item> flour = food("flour");
    public static final RegistryObject<Item> honey = food("honey");
    public static final RegistryObject<Item> yogurt = food("yogurt");
    public static final RegistryObject<Item> cheese = food("cheese");
    public static final RegistryObject<Item> mayonnaise = food("mayonnaise");
    public static final RegistryObject<Item> eggL = food("egg_l");
    public static final RegistryObject<Item> eggM = food("egg_m");
    public static final RegistryObject<Item> eggS = food("egg_s");
    public static final RegistryObject<Item> milkL = food("milk_l");
    public static final RegistryObject<Item> milkM = food("milk_m");
    public static final RegistryObject<Item> milkS = food("milk_s");
    public static final RegistryObject<Item> wine = food("wine");
    public static final RegistryObject<Item> chocolate = food("chocolate");
    public static final RegistryObject<Item> rice = food("rice");
    public static final RegistryObject<Item> turnipHeaven = food("turnip_heaven");
    public static final RegistryObject<Item> pickleMix = food("pickle_mix");
    public static final RegistryObject<Item> salmonOnigiri = food("salmon_onigiri");
    public static final RegistryObject<Item> bread = food("bread");
    public static final RegistryObject<Item> onigiri = food("onigiri");
    public static final RegistryObject<Item> relaxTeaLeaves = food("relax_tea_leaves");
    public static final RegistryObject<Item> iceCream = food("ice_cream");
    public static final RegistryObject<Item> raisinBread = food("raisin_bread");
    public static final RegistryObject<Item> bambooRice = food("bamboo_rice");
    public static final RegistryObject<Item> pickles = food("pickles");
    public static final RegistryObject<Item> pickledTurnip = food("pickled_turnip");
    public static final RegistryObject<Item> fruitSandwich = food("fruit_sandwich");
    public static final RegistryObject<Item> sandwich = food("sandwich");
    public static final RegistryObject<Item> salad = food("salad");
    public static final RegistryObject<Item> dumplings = food("dumplings");
    public static final RegistryObject<Item> pumpkinFlan = food("pumpkin_flan");
    public static final RegistryObject<Item> flan = food("flan");
    public static final RegistryObject<Item> chocolateSponge = food("chocolate_sponge");
    public static final RegistryObject<Item> poundCake = food("pound_cake");
    public static final RegistryObject<Item> steamedGyoza = food("steamed_gyoza");
    public static final RegistryObject<Item> curryManju = food("curry_manju");
    public static final RegistryObject<Item> chineseManju = food("chinese_manju");
    public static final RegistryObject<Item> meatDumpling = food("meat_dumpling");
    public static final RegistryObject<Item> cheeseBread = food("cheese_bread");
    public static final RegistryObject<Item> steamedBread = food("steamed_bread");
    public static final RegistryObject<Item> hotJuice = food("hot_juice");
    public static final RegistryObject<Item> preludetoLove = food("prelude_to_love");
    public static final RegistryObject<Item> goldJuice = food("gold_juice");
    public static final RegistryObject<Item> butter = food("butter");
    public static final RegistryObject<Item> ketchup = food("ketchup");
    public static final RegistryObject<Item> mixedSmoothie = food("mixed_smoothie");
    public static final RegistryObject<Item> mixedJuice = food("mixed_juice");
    public static final RegistryObject<Item> veggieSmoothie = food("veggie_smoothie");
    public static final RegistryObject<Item> vegetableJuice = food("vegetable_juice");
    public static final RegistryObject<Item> fruitSmoothie = food("fruit_smoothie");
    public static final RegistryObject<Item> fruitJuice = food("fruit_juice");
    public static final RegistryObject<Item> strawberryMilk = food("strawberry_milk");
    public static final RegistryObject<Item> appleJuice = food("apple_juice");
    public static final RegistryObject<Item> orangeJuice = food("orange_juice");
    public static final RegistryObject<Item> grapeJuice = food("grape_juice");
    public static final RegistryObject<Item> tomatoJuice = food("tomato_juice");
    public static final RegistryObject<Item> pineappleJuice = food("pineapple_juice");
    public static final RegistryObject<Item> applePie = food("apple_pie");
    public static final RegistryObject<Item> cheesecake = food("cheesecake");
    public static final RegistryObject<Item> chocolateCake = food("chocolate_cake");
    public static final RegistryObject<Item> cake = food("cake");
    public static final RegistryObject<Item> chocoCookie = food("choco_cookie");
    public static final RegistryObject<Item> cookie = food("cookie");
    public static final RegistryObject<Item> yamoftheAges = food("yam_of_the_ages");
    public static final RegistryObject<Item> seafoodGratin = food("seafood_gratin");
    public static final RegistryObject<Item> gratin = food("gratin");
    public static final RegistryObject<Item> seafoodDoria = food("seafood_doria");
    public static final RegistryObject<Item> doria = food("doria");
    public static final RegistryObject<Item> seafoodPizza = food("seafood_pizza");
    public static final RegistryObject<Item> pizza = food("pizza");
    public static final RegistryObject<Item> butterRoll = food("butter_roll");
    public static final RegistryObject<Item> jamRoll = food("jam_roll");
    public static final RegistryObject<Item> toast = food("toast");
    public static final RegistryObject<Item> sweetPotato = food("sweet_potato");
    public static final RegistryObject<Item> bakedOnigiri = food("baked_onigiri");
    public static final RegistryObject<Item> cornontheCob = food("corn_on_the_cob");
    public static final RegistryObject<Item> rockfishStew = food("rockfish_stew");
    public static final RegistryObject<Item> unionStew = food("union_stew");
    public static final RegistryObject<Item> grilledMiso = food("grilled_miso");
    public static final RegistryObject<Item> relaxTea = food("relax_tea");
    public static final RegistryObject<Item> royalCurry = food("royal_curry");
    public static final RegistryObject<Item> ultimateCurry = food("ultimate_curry");
    public static final RegistryObject<Item> curryRice = food("curry_rice");
    public static final RegistryObject<Item> eggBowl = food("egg_bowl");
    public static final RegistryObject<Item> tempuraBowl = food("tempura_bowl");
    public static final RegistryObject<Item> milkPorridge = food("milk_porridge");
    public static final RegistryObject<Item> ricePorridge = food("rice_porridge");
    public static final RegistryObject<Item> tempuraUdon = food("tempura_udon");
    public static final RegistryObject<Item> curryUdon = food("curry_udon");
    public static final RegistryObject<Item> udon = food("udon");
    public static final RegistryObject<Item> cheeseFondue = food("cheese_fondue");
    public static final RegistryObject<Item> marmalade = food("marmalade");
    public static final RegistryObject<Item> grapeJam = food("grape_jam");
    public static final RegistryObject<Item> appleJam = food("apple_jam");
    public static final RegistryObject<Item> strawberryJam = food("strawberry_jam");
    public static final RegistryObject<Item> boiledGyoza = food("boiled_gyoza");
    public static final RegistryObject<Item> glazedYam = food("glazed_yam");
    public static final RegistryObject<Item> boiledEgg = food("boiled_egg");
    public static final RegistryObject<Item> boiledSpinach = food("boiled_spinach");
    public static final RegistryObject<Item> boiledPumpkin = food("boiled_pumpkin");
    public static final RegistryObject<Item> grapeLiqueur = food("grape_liqueur");
    public static final RegistryObject<Item> hotChocolate = food("hot_chocolate");
    public static final RegistryObject<Item> hotMilk = food("hot_milk");
    public static final RegistryObject<Item> grilledSandFlounder = food("grilled_sand_flounder");
    public static final RegistryObject<Item> grilledShrimp = food("grilled_shrimp");
    public static final RegistryObject<Item> grilledLobster = food("grilled_lobster");
    public static final RegistryObject<Item> grilledBlowfish = food("grilled_blowfish");
    public static final RegistryObject<Item> grilledLampSquid = food("grilled_lamp_squid");
    public static final RegistryObject<Item> grilledSunsquid = food("grilled_sunsquid");
    public static final RegistryObject<Item> grilledSquid = food("grilled_squid");
    public static final RegistryObject<Item> grilledFallFlounder = food("grilled_fall_flounder");
    public static final RegistryObject<Item> grilledTurbot = food("grilled_turbot");
    public static final RegistryObject<Item> grilledFlounder = food("grilled_flounder");
    public static final RegistryObject<Item> saltedPike = food("salted_pike");
    public static final RegistryObject<Item> grilledNeedlefish = food("grilled_needlefish");
    public static final RegistryObject<Item> driedSardines = food("dried_sardines");
    public static final RegistryObject<Item> tunaTeriyaki = food("tuna_teriyaki");
    public static final RegistryObject<Item> saltedPondSmelt = food("salted_pond_smelt");
    public static final RegistryObject<Item> grilledYellowtail = food("grilled_yellowtail");
    public static final RegistryObject<Item> grilledMackerel = food("grilled_mackerel");
    public static final RegistryObject<Item> grilledSkipjack = food("grilled_skipjack");
    public static final RegistryObject<Item> grilledLoverSnapper = food("grilled_lover_snapper");
    public static final RegistryObject<Item> grilledGlitterSnapper = food("grilled_glitter_snapper");
    public static final RegistryObject<Item> grilledGirella = food("grilled_girella");
    public static final RegistryObject<Item> grilledSnapper = food("grilled_snapper");
    public static final RegistryObject<Item> grilledGibelio = food("grilled_gibelio");
    public static final RegistryObject<Item> grilledCrucianCarp = food("grilled_crucian_carp");
    public static final RegistryObject<Item> saltedTaimen = food("salted_taimen");
    public static final RegistryObject<Item> saltedSalmon = food("salted_salmon");
    public static final RegistryObject<Item> saltedChub = food("salted_chub");
    public static final RegistryObject<Item> saltedCherrySalmon = food("salted_cherry_salmon");
    public static final RegistryObject<Item> saltedRainbowTrout = food("salted_rainbow_trout");
    public static final RegistryObject<Item> saltedChar = food("salted_char");
    public static final RegistryObject<Item> saltedMasuTrout = food("salted_masu_trout");
    public static final RegistryObject<Item> dryCurry = food("dry_curry");
    public static final RegistryObject<Item> risotto = food("risotto");
    public static final RegistryObject<Item> gyoza = food("gyoza");
    public static final RegistryObject<Item> pancakes = food("pancakes");
    public static final RegistryObject<Item> tempura = food("tempura");
    public static final RegistryObject<Item> friedUdon = food("fried_udon");
    public static final RegistryObject<Item> donut = food("donut");
    public static final RegistryObject<Item> frenchToast = food("french_toast");
    public static final RegistryObject<Item> curryBread = food("curry_bread");
    public static final RegistryObject<Item> bakedApple = food("baked_apple");
    public static final RegistryObject<Item> omeletRice = food("omelet_rice");
    public static final RegistryObject<Item> omelet = food("omelet");
    public static final RegistryObject<Item> friedEggs = food("fried_eggs");
    public static final RegistryObject<Item> misoEggplant = food("miso_eggplant");
    public static final RegistryObject<Item> cornCereal = food("corn_cereal");
    public static final RegistryObject<Item> popcorn = food("popcorn");
    public static final RegistryObject<Item> croquettes = food("croquettes");
    public static final RegistryObject<Item> frenchFries = food("french_fries");
    public static final RegistryObject<Item> cabbageCakes = food("cabbage_cakes");
    public static final RegistryObject<Item> friedRice = food("fried_rice");
    public static final RegistryObject<Item> friedVeggies = food("fried_veggies");
    public static final RegistryObject<Item> shrimpSashimi = food("shrimp_sashimi");
    public static final RegistryObject<Item> lobsterSashimi = food("lobster_sashimi");
    public static final RegistryObject<Item> blowfishSashimi = food("blowfish_sashimi");
    public static final RegistryObject<Item> lampSquidSashimi = food("lamp_squid_sashimi");
    public static final RegistryObject<Item> sunsquidSashimi = food("sunsquid_sashimi");
    public static final RegistryObject<Item> squidSashimi = food("squid_sashimi");
    public static final RegistryObject<Item> fallSashimi = food("fall_sashimi");
    public static final RegistryObject<Item> turbotSashimi = food("turbot_sashimi");
    public static final RegistryObject<Item> flounderSashimi = food("flounder_sashimi");
    public static final RegistryObject<Item> pikeSashimi = food("pike_sashimi");
    public static final RegistryObject<Item> needlefishSashimi = food("needlefish_sashimi");
    public static final RegistryObject<Item> sardineSashimi = food("sardine_sashimi");
    public static final RegistryObject<Item> tunaSashimi = food("tuna_sashimi");
    public static final RegistryObject<Item> yellowtailSashimi = food("yellowtail_sashimi");
    public static final RegistryObject<Item> skipjackSashimi = food("skipjack_sashimi");
    public static final RegistryObject<Item> girellaSashimi = food("girella_sashimi");
    public static final RegistryObject<Item> loverSashimi = food("lover_sashimi");
    public static final RegistryObject<Item> glitterSashimi = food("glitter_sashimi");
    public static final RegistryObject<Item> snapperSashimi = food("snapper_sashimi");
    public static final RegistryObject<Item> taimenSashimi = food("taimen_sashimi");
    public static final RegistryObject<Item> cherrySashimi = food("cherry_sashimi");
    public static final RegistryObject<Item> salmonSashimi = food("salmon_sashimi");
    public static final RegistryObject<Item> rainbowSashimi = food("rainbow_sashimi");
    public static final RegistryObject<Item> charSashimi = food("char_sashimi");
    public static final RegistryObject<Item> troutSashimi = food("trout_sashimi");
    public static final RegistryObject<Item> disastrousDish = food("disastrous_dish");
    public static final RegistryObject<Item> failedDish = food("failed_dish");
    public static final RegistryObject<Item> mixedHerbs = food("mixed_herbs");
    public static final RegistryObject<Item> sourDrop = food("sour_drop");
    public static final RegistryObject<Item> sweetPowder = food("sweet_powder");
    public static final RegistryObject<Item> heavySpice = food("heavy_spice");

    public static final RegistryObject<Item> forgingBread = ITEMS.register("forging_bread", () -> new ItemRecipeBread(EnumCrafting.FORGE, new Item.Properties().group(RFCreativeTabs.food).maxStackSize(16)));
    public static final RegistryObject<Item> armorBread = ITEMS.register("armory_bread", () -> new ItemRecipeBread(EnumCrafting.ARMOR, new Item.Properties().group(RFCreativeTabs.food).maxStackSize(16)));
    public static final RegistryObject<Item> chemistryBread = ITEMS.register("chemistry_bread", () -> new ItemRecipeBread(EnumCrafting.CHEM, new Item.Properties().group(RFCreativeTabs.food).maxStackSize(16)));
    public static final RegistryObject<Item> cookingBread = ITEMS.register("cooking_bread", () -> new ItemRecipeBread(EnumCrafting.COOKING, new Item.Properties().group(RFCreativeTabs.food).maxStackSize(16)));

    public static final RegistryObject<Item> orange = food("orange");
    public static final RegistryObject<Item> grapes = food("grapes");

    public static final RegistryObject<Item> mushroom = ITEMS.register("mushroom", () -> new ItemMushroom(new Item.Properties().food(foodProp).group(RFCreativeTabs.food)));
    public static final RegistryObject<Item> monarchMushroom = ITEMS.register("monarch_mushroom", () -> new ItemMushroom(new Item.Properties().food(foodProp).group(RFCreativeTabs.food)));
    public static final RegistryObject<Item> mealyApple = food("mealy_apple");

    public static final RegistryObject<Item> shippingBin = blockItem("shipping_bin", () -> ModBlocks.shipping);
    public static final RegistryObject<Item> requestBoard = blockItem("black_board", () -> ModBlocks.board);
    public static final RegistryObject<Item> spawner = blockItem("boss_spawner", () -> ModBlocks.bossSpawner, RFCreativeTabs.monsters);
    public static final RegistryObject<Item> farmland = blockItem("farmland", () -> ModBlocks.farmland, null);

    public static RegistryObject<Item> hoe(EnumToolTier tier) {
        return ITEMS.register("hoe_" + tier.getName(), () -> new ItemToolHoe(tier, new Item.Properties().addToolType(ToolType.HOE, tier.getTierLevel()).group(RFCreativeTabs.weaponToolTab)));
    }

    public static RegistryObject<Item> wateringCan(EnumToolTier tier) {
        return ITEMS.register("watering_can_" + tier.getName(), () -> new ItemToolWateringCan(tier, new Item.Properties().group(RFCreativeTabs.weaponToolTab)));
    }

    public static RegistryObject<Item> sickle(EnumToolTier tier) {
        return ITEMS.register("sickle_" + tier.getName(), () -> new ItemToolSickle(tier, new Item.Properties().group(RFCreativeTabs.weaponToolTab)));
    }

    public static RegistryObject<Item> hammerTool(EnumToolTier tier) {
        return ITEMS.register("hammer_" + tier.getName(), () -> new ItemToolHammer(tier, new Item.Properties().addToolType(ToolType.PICKAXE, tier.getTierLevel()).group(RFCreativeTabs.weaponToolTab)));
    }

    public static RegistryObject<Item> axeTool(EnumToolTier tier) {
        return ITEMS.register("axe_" + tier.getName(), () -> new ItemToolAxe(tier, new Item.Properties().addToolType(ToolType.AXE, tier.getTierLevel()).group(RFCreativeTabs.weaponToolTab)));
    }

    public static RegistryObject<Item> fishingRod(EnumToolTier tier) {
        return ITEMS.register("fishing_rod_" + tier.getName(), () -> new ItemToolFishingRod(tier, new Item.Properties().group(RFCreativeTabs.weaponToolTab)));
    }

    public static RegistryObject<Item> shortSword(String name) {
        return ITEMS.register(name, () -> new ItemShortSwordBase(new Item.Properties().group(RFCreativeTabs.weaponToolTab)));
    }

    public static RegistryObject<Item> longSword(String name) {
        return ITEMS.register(name, () -> new ItemLongSwordBase(new Item.Properties().group(RFCreativeTabs.weaponToolTab)));
    }

    public static RegistryObject<Item> spear(String name) {
        return ITEMS.register(name, () -> new ItemSpearBase(new Item.Properties().group(RFCreativeTabs.weaponToolTab)));
    }

    public static RegistryObject<Item> axe(String name) {
        return ITEMS.register(name, () -> new ItemAxeBase(new Item.Properties().addToolType(ToolType.AXE, 1).group(RFCreativeTabs.weaponToolTab)));
    }

    public static RegistryObject<Item> hammer(String name) {
        return ITEMS.register(name, () -> new ItemHammerBase(new Item.Properties().addToolType(ToolType.PICKAXE, 1).group(RFCreativeTabs.weaponToolTab)));
    }

    public static RegistryObject<Item> dualBlade(String name) {
        return ITEMS.register(name, () -> new ItemDualBladeBase(new Item.Properties().group(RFCreativeTabs.weaponToolTab)));
    }

    public static RegistryObject<Item> gloves(String name) {
        return ITEMS.register(name, () -> new ItemGloveBase(new Item.Properties().group(RFCreativeTabs.weaponToolTab)));
    }

    public static RegistryObject<Item> staff(String name, EnumElement starterElement) {
        return ITEMS.register(name, () -> new ItemStaffBase(starterElement, new Item.Properties().maxStackSize(1).group(RFCreativeTabs.weaponToolTab)));
    }

    public static RegistryObject<Item> accessoire(String name) {
        return ITEMS.register(name, () -> new ItemAccessoireBase(new Item.Properties().group(RFCreativeTabs.equipment)));
    }

    public static RegistryObject<Item> accessoire(String name, EquipmentSlotType renderSlot) {
        return ITEMS.register(name, () -> new ItemAccessoireBase(renderSlot, new Item.Properties().group(RFCreativeTabs.equipment)));
    }

    public static RegistryObject<Item> equipment(EquipmentSlotType slot, String name) {
        return ITEMS.register(name, () -> new ItemArmorBase(slot, new Item.Properties().group(RFCreativeTabs.equipment)));
    }

    public static RegistryObject<Item> shield(String name) {
        return ITEMS.register(name, () -> new ShieldItem(new Item.Properties().maxStackSize(1).group(RFCreativeTabs.equipment)) {
            @Override
            public boolean isShield(ItemStack stack, LivingEntity entity) {
                return true;
            }
        });
    }

    public static RegistryObject<Item> blockItem(String name, Supplier<Supplier<Block>> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get().get(), new Item.Properties().group(RFCreativeTabs.blocks)));
    }

    public static RegistryObject<Item> blockItem(String name, Supplier<Supplier<Block>> block, ItemGroup group) {
        return ITEMS.register(name, () -> new BlockItem(block.get().get(), new Item.Properties().group(group)));
    }

    public static RegistryObject<Item> mineral(EnumMineralTier tier) {
        Supplier<Block> block = () -> ModBlocks.mineralMap.get(tier).get();
        return ITEMS.register("ore_" + tier.getString(), () -> new BlockItem(block.get(), new Item.Properties().group(RFCreativeTabs.blocks)));
    }

    public static RegistryObject<Item> brokenMineral(EnumMineralTier tier) {
        Supplier<Block> block = () -> ModBlocks.brokenMineralMap.get(tier).get();
        return ITEMS.register("ore_broken_" + tier.getString(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static RegistryObject<Item> mat(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties().group(RFCreativeTabs.upgradeItems)));
    }

    public static RegistryObject<Item> medicine(String name, boolean affectStats) {
        return ITEMS.register(name, () -> new ItemMedicine(affectStats, new Item.Properties().food(foodProp).maxStackSize(16).group(RFCreativeTabs.medicine)));
    }

    public static RegistryObject<Item> drinkable(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties().food(foodProp).maxStackSize(16).group(RFCreativeTabs.medicine)) {
            @Override
            public UseAction getUseAction(ItemStack stack) {
                return UseAction.DRINK;
            }
        });
    }

    public static RegistryObject<Item> spell(Supplier<Supplier<Spell>> sup, String name) {
        return ITEMS.register(name, () -> new ItemSpell(sup.get(), new Item.Properties().maxStackSize(1).group(RFCreativeTabs.cast)));
    }

    public static RegistryObject<Item> fish(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties().group(RFCreativeTabs.food)));
    }

    public static RegistryObject<Item> seed(String name, Supplier<Supplier<Block>> block) {
        return ITEMS.register("seed_" + name, () -> new BlockNamedItem(block.get().get(), new Item.Properties().group(RFCreativeTabs.crops)));
    }

    public static RegistryObject<Item> crop(String name, boolean giant) {
        if (giant)
            return ITEMS.register("crop_" + name + "_giant", () -> new ItemGiantCrops(new Item.Properties().food(foodProp).group(RFCreativeTabs.crops)));
        else
            return ITEMS.register("crop_" + name, () -> new Item(new Item.Properties().food(foodProp).group(RFCreativeTabs.crops)));
    }

    public static RegistryObject<Item> herb(String name, Supplier<Supplier<Block>> block) {
        return ITEMS.register(name, () -> new BlockNamedItem(block.get().get(), new Item.Properties().food(foodProp).group(RFCreativeTabs.medicine)));
    }

    public static RegistryObject<Item> food(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties().food(foodProp).group(RFCreativeTabs.food)));
    }
}
