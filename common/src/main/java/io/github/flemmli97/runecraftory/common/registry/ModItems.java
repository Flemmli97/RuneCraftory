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
import io.github.flemmli97.runecraftory.common.items.consumables.ItemObjectX;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemRecipeBread;
import io.github.flemmli97.runecraftory.common.items.creative.ItemDebug;
import io.github.flemmli97.runecraftory.common.items.creative.ItemLevelUp;
import io.github.flemmli97.runecraftory.common.items.creative.ItemSkillUp;
import io.github.flemmli97.runecraftory.common.items.equipment.ItemSeedShield;
import io.github.flemmli97.runecraftory.common.items.equipment.ItemStatShield;
import io.github.flemmli97.runecraftory.common.items.tools.ItemBrush;
import io.github.flemmli97.runecraftory.common.items.tools.ItemCommandStaff;
import io.github.flemmli97.runecraftory.common.items.tools.ItemFertilizer;
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
import io.github.flemmli97.runecraftory.common.items.weapons.shortsword.ItemSeedSword;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ModItems {

    public static final PlatformRegistry<Item> ITEMS = PlatformUtils.INSTANCE.of(Registry.ITEM_REGISTRY, RuneCraftory.MODID);

    //Those collections are for datagen
    public static final List<RegistryEntrySupplier<Item>> NOTEX = new ArrayList<>();
    public static final Map<TagKey<Item>, List<RegistryEntrySupplier<Item>>> DATAGENTAGS = new HashMap<>();
    public static final List<RegistryEntrySupplier<Item>> SEEDS = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Item>> VEGGIES = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Item>> FRUITS = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Item>> FLOWERS = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Item>> CROPS = new ArrayList<>();

    private static final FoodProperties lowFoodProp = new FoodProperties.Builder().nutrition(1).saturationMod(0.5f).alwaysEat().build();
    private static final FoodProperties foodProp = new FoodProperties.Builder().nutrition(2).saturationMod(0.5f).alwaysEat().build();
    private static final FoodProperties highFoodProp = new FoodProperties.Builder().nutrition(6).saturationMod(0.75f).alwaysEat().build();

    public static final RegistryEntrySupplier<Item> hoeScrap = hoe(EnumToolTier.SCRAP);
    public static final RegistryEntrySupplier<Item> hoeIron = hoe(EnumToolTier.IRON);
    public static final RegistryEntrySupplier<Item> hoeSilver = hoe(EnumToolTier.SILVER);
    public static final RegistryEntrySupplier<Item> hoeGold = hoe(EnumToolTier.GOLD);
    public static final RegistryEntrySupplier<Item> hoePlatinum = hoe(EnumToolTier.PLATINUM);
    public static final RegistryEntrySupplier<Item> wateringCanScrap = wateringCan(EnumToolTier.SCRAP);
    public static final RegistryEntrySupplier<Item> wateringCanIron = wateringCan(EnumToolTier.IRON);
    public static final RegistryEntrySupplier<Item> wateringCanSilver = wateringCan(EnumToolTier.SILVER);
    public static final RegistryEntrySupplier<Item> wateringCanGold = wateringCan(EnumToolTier.GOLD);
    public static final RegistryEntrySupplier<Item> wateringCanPlatinum = wateringCan(EnumToolTier.PLATINUM);
    public static final RegistryEntrySupplier<Item> sickleScrap = sickle(EnumToolTier.SCRAP);
    public static final RegistryEntrySupplier<Item> sickleIron = sickle(EnumToolTier.IRON);
    public static final RegistryEntrySupplier<Item> sickleSilver = sickle(EnumToolTier.SILVER);
    public static final RegistryEntrySupplier<Item> sickleGold = sickle(EnumToolTier.GOLD);
    public static final RegistryEntrySupplier<Item> sicklePlatinum = sickle(EnumToolTier.PLATINUM);
    public static final RegistryEntrySupplier<Item> hammerScrap = hammerTool(EnumToolTier.SCRAP);
    public static final RegistryEntrySupplier<Item> hammerIron = hammerTool(EnumToolTier.IRON);
    public static final RegistryEntrySupplier<Item> hammerSilver = hammerTool(EnumToolTier.SILVER);
    public static final RegistryEntrySupplier<Item> hammerGold = hammerTool(EnumToolTier.GOLD);
    public static final RegistryEntrySupplier<Item> hammerPlatinum = hammerTool(EnumToolTier.PLATINUM);
    public static final RegistryEntrySupplier<Item> axeScrap = axeTool(EnumToolTier.SCRAP);
    public static final RegistryEntrySupplier<Item> axeIron = axeTool(EnumToolTier.IRON);
    public static final RegistryEntrySupplier<Item> axeSilver = axeTool(EnumToolTier.SILVER);
    public static final RegistryEntrySupplier<Item> axeGold = axeTool(EnumToolTier.GOLD);
    public static final RegistryEntrySupplier<Item> axePlatinum = axeTool(EnumToolTier.PLATINUM);
    public static final RegistryEntrySupplier<Item> fishingRodScrap = fishingRod(EnumToolTier.SCRAP);
    public static final RegistryEntrySupplier<Item> fishingRodIron = fishingRod(EnumToolTier.IRON);
    public static final RegistryEntrySupplier<Item> fishingRodSilver = fishingRod(EnumToolTier.SILVER);
    public static final RegistryEntrySupplier<Item> fishingRodGold = fishingRod(EnumToolTier.GOLD);
    public static final RegistryEntrySupplier<Item> fishingRodPlatinum = fishingRod(EnumToolTier.PLATINUM);
    public static final RegistryEntrySupplier<Item> mobStaff = ITEMS.register("monster_command_staff", () -> new ItemCommandStaff(new Item.Properties().stacksTo(1).tab(RFCreativeTabs.weaponToolTab)));
    public static final RegistryEntrySupplier<Item> brush = ITEMS.register("brush", () -> new ItemBrush(new Item.Properties().stacksTo(1).tab(RFCreativeTabs.weaponToolTab)));
    public static final RegistryEntrySupplier<Item> glass = ITEMS.register("magnifying_glass", () -> new ItemToolGlass(new Item.Properties().stacksTo(1).tab(RFCreativeTabs.weaponToolTab)));

    public static final RegistryEntrySupplier<Item> leveliser = ITEMS.register("leveliser", () -> new ItemStatIncrease(ItemStatIncrease.Stat.LEVEL, new Item.Properties().tab(RFCreativeTabs.medicine)));
    public static final RegistryEntrySupplier<Item> heartDrink = ITEMS.register("heart_drink", () -> new ItemStatIncrease(ItemStatIncrease.Stat.HP, new Item.Properties().tab(RFCreativeTabs.medicine)));
    public static final RegistryEntrySupplier<Item> vitalGummi = ITEMS.register("vital_gummi", () -> new ItemStatIncrease(ItemStatIncrease.Stat.VIT, new Item.Properties().tab(RFCreativeTabs.medicine)));
    public static final RegistryEntrySupplier<Item> intelligencer = ITEMS.register("intelligencer", () -> new ItemStatIncrease(ItemStatIncrease.Stat.INT, new Item.Properties().tab(RFCreativeTabs.medicine)));
    public static final RegistryEntrySupplier<Item> protein = ITEMS.register("protein", () -> new ItemStatIncrease(ItemStatIncrease.Stat.STR, new Item.Properties().tab(RFCreativeTabs.medicine)));
    public static final RegistryEntrySupplier<Item> formularA = ITEMS.register("formular_a", () -> new ItemFertilizer(ItemFertilizer.formularA, new Item.Properties().tab(RFCreativeTabs.weaponToolTab)));
    public static final RegistryEntrySupplier<Item> formularB = ITEMS.register("formular_b", () -> new ItemFertilizer(ItemFertilizer.formularB, new Item.Properties().tab(RFCreativeTabs.weaponToolTab)));
    public static final RegistryEntrySupplier<Item> formularC = ITEMS.register("formular_c", () -> new ItemFertilizer(ItemFertilizer.formularC, new Item.Properties().tab(RFCreativeTabs.weaponToolTab)));
    public static final RegistryEntrySupplier<Item> minimizer = ITEMS.register("minimizer", () -> new ItemFertilizer(ItemFertilizer.minimizer, new Item.Properties().tab(RFCreativeTabs.weaponToolTab)));
    public static final RegistryEntrySupplier<Item> giantizer = ITEMS.register("giantizer", () -> new ItemFertilizer(ItemFertilizer.giantizer, new Item.Properties().tab(RFCreativeTabs.weaponToolTab)));
    public static final RegistryEntrySupplier<Item> greenifier = ITEMS.register("greenifier", () -> new ItemFertilizer(ItemFertilizer.greenifier, new Item.Properties().tab(RFCreativeTabs.weaponToolTab)));
    public static final RegistryEntrySupplier<Item> greenifierPlus = ITEMS.register("greenifier_plus", () -> new ItemFertilizer(ItemFertilizer.greenifierPlus, new Item.Properties().tab(RFCreativeTabs.weaponToolTab)));
    public static final RegistryEntrySupplier<Item> wettablePowder = ITEMS.register("wettable_powder", () -> new ItemFertilizer(ItemFertilizer.wettable, new Item.Properties().tab(RFCreativeTabs.weaponToolTab)));

    //Weapons
    public static final RegistryEntrySupplier<Item> seedSword = ITEMS.register("seed_sword_item", () -> new ItemSeedSword(new Item.Properties()));
    public static final RegistryEntrySupplier<Item> broadSword = shortSword("broad_sword", Texture.Y);
    public static final RegistryEntrySupplier<Item> steelSword = shortSword("steel_sword", Texture.Y);
    public static final RegistryEntrySupplier<Item> steelSwordPlus = shortSword("steel_sword_plus", Texture.Y);
    public static final RegistryEntrySupplier<Item> cutlass = shortSword("cutlass", Texture.Y);
    public static final RegistryEntrySupplier<Item> aquaSword = shortSword("aqua_sword", Texture.Y);
    public static final RegistryEntrySupplier<Item> invisiBlade = shortSword("invisiblade", Texture.Y);
    public static final RegistryEntrySupplier<Item> defender = shortSword("defender", Texture.N);
    public static final RegistryEntrySupplier<Item> burningSword = shortSword("burning_sword", Texture.N);
    public static final RegistryEntrySupplier<Item> gorgeousSword = shortSword("gorgeous_sword", Texture.N);
    public static final RegistryEntrySupplier<Item> gaiaSword = shortSword("gaia_sword", Texture.N);
    public static final RegistryEntrySupplier<Item> snakeSword = shortSword("snake_sword", Texture.N);
    public static final RegistryEntrySupplier<Item> luckBlade = shortSword("luck_blade", Texture.N);
    public static final RegistryEntrySupplier<Item> platinumSword = shortSword("platinum_sword", Texture.N);
    public static final RegistryEntrySupplier<Item> windSword = shortSword("wind_sword", Texture.N);
    public static final RegistryEntrySupplier<Item> chaosBlade = shortSword("chaos_blade", Texture.N);
    public static final RegistryEntrySupplier<Item> sakura = shortSword("sakura", Texture.N);
    public static final RegistryEntrySupplier<Item> sunspot = shortSword("sunspot", Texture.N);
    public static final RegistryEntrySupplier<Item> durendal = shortSword("durendal", Texture.N);
    public static final RegistryEntrySupplier<Item> aerialBlade = shortSword("aerial_blade", Texture.N);
    public static final RegistryEntrySupplier<Item> grantale = shortSword("grantale", Texture.N);
    public static final RegistryEntrySupplier<Item> smashBlade = shortSword("smash_blade", Texture.N);
    public static final RegistryEntrySupplier<Item> icifier = shortSword("icifier", Texture.N);
    public static final RegistryEntrySupplier<Item> soulEater = shortSword("soul_eater", Texture.N);
    public static final RegistryEntrySupplier<Item> raventine = shortSword("raventine", Texture.N);
    public static final RegistryEntrySupplier<Item> starSaber = shortSword("star_saber", Texture.N);
    public static final RegistryEntrySupplier<Item> platinumSwordPlus = shortSword("platinum_sword_plus", Texture.N);
    public static final RegistryEntrySupplier<Item> dragonSlayer = shortSword("dragon_slayer", Texture.N);
    public static final RegistryEntrySupplier<Item> runeBlade = shortSword("rune_blade", Texture.N);
    public static final RegistryEntrySupplier<Item> gladius = shortSword("gladius", Texture.N);
    public static final RegistryEntrySupplier<Item> runeLegend = shortSword("rune_legend", Texture.N);
    public static final RegistryEntrySupplier<Item> backScratcher = shortSword("back_scratcher", Texture.N);
    public static final RegistryEntrySupplier<Item> spoon = shortSword("spoon", Texture.N);
    public static final RegistryEntrySupplier<Item> veggieBlade = shortSword("veggie_blade", Texture.N);

    public static final RegistryEntrySupplier<Item> claymore = longSword("claymore", Texture.Y);
    public static final RegistryEntrySupplier<Item> zweihaender = longSword("zweihaender", Texture.Y);
    public static final RegistryEntrySupplier<Item> zweihaenderPlus = longSword("zweihaender_plus", Texture.Y);
    public static final RegistryEntrySupplier<Item> greatSword = longSword("great_sword", Texture.Y);
    public static final RegistryEntrySupplier<Item> seaCutter = longSword("sea_cutter", Texture.Y);
    public static final RegistryEntrySupplier<Item> cycloneBlade = longSword("cyclone_blade", Texture.Y);
    public static final RegistryEntrySupplier<Item> poisonBlade = longSword("poison_blade", Texture.N);
    public static final RegistryEntrySupplier<Item> katzbalger = longSword("katzbalger", Texture.N);
    public static final RegistryEntrySupplier<Item> earthShade = longSword("earth_shade", Texture.N);
    public static final RegistryEntrySupplier<Item> bigKnife = longSword("big_knife", Texture.N);
    public static final RegistryEntrySupplier<Item> katana = longSword("katana", Texture.N);
    public static final RegistryEntrySupplier<Item> flameSaber = longSword("flame_saber", Texture.N);
    public static final RegistryEntrySupplier<Item> bioSmasher = longSword("bio_smasher", Texture.N);
    public static final RegistryEntrySupplier<Item> snowCrown = longSword("snow_crown", Texture.N);
    public static final RegistryEntrySupplier<Item> dancingDicer = longSword("dancing_dicer", Texture.N);
    public static final RegistryEntrySupplier<Item> flamberge = longSword("flamberge", Texture.N);
    public static final RegistryEntrySupplier<Item> flambergePlus = longSword("flamberge_plus", Texture.N);
    public static final RegistryEntrySupplier<Item> volcanon = longSword("volcanon", Texture.N);
    public static final RegistryEntrySupplier<Item> psycho = longSword("psycho", Texture.N);
    public static final RegistryEntrySupplier<Item> shineBlade = longSword("shine_blade", Texture.N);
    public static final RegistryEntrySupplier<Item> grandSmasher = longSword("grand_smasher", Texture.N);
    public static final RegistryEntrySupplier<Item> belzebuth = longSword("belzebuth", Texture.N);
    public static final RegistryEntrySupplier<Item> orochi = longSword("orochi", Texture.N);
    public static final RegistryEntrySupplier<Item> punisher = longSword("punisher", Texture.N);
    public static final RegistryEntrySupplier<Item> steelSlicer = longSword("steel_slicer", Texture.N);
    public static final RegistryEntrySupplier<Item> moonShadow = longSword("moon_shadow", Texture.N);
    public static final RegistryEntrySupplier<Item> blueEyedBlade = longSword("blue_eyed_blade", Texture.N);
    public static final RegistryEntrySupplier<Item> balmung = longSword("balmung", Texture.N);
    public static final RegistryEntrySupplier<Item> braveheart = longSword("braveheart", Texture.N);
    public static final RegistryEntrySupplier<Item> forceElement = longSword("force_element", Texture.N);
    public static final RegistryEntrySupplier<Item> heavensAsunder = longSword("heavens_asunder", Texture.N);
    public static final RegistryEntrySupplier<Item> caliburn = longSword("caliburn", Texture.N);
    public static final RegistryEntrySupplier<Item> dekash = longSword("dekash", Texture.N);
    public static final RegistryEntrySupplier<Item> daicone = longSword("daicone", Texture.N);

    public static final RegistryEntrySupplier<Item> spear = spear("spear", Texture.Y);
    public static final RegistryEntrySupplier<Item> woodStaff = spear("wood_staff", Texture.Y);
    public static final RegistryEntrySupplier<Item> lance = spear("lance", Texture.Y);
    public static final RegistryEntrySupplier<Item> lancePlus = spear("lance_plus", Texture.Y);
    public static final RegistryEntrySupplier<Item> needleSpear = spear("needle_spear", Texture.Y);
    public static final RegistryEntrySupplier<Item> trident = spear("trident", Texture.Y);
    public static final RegistryEntrySupplier<Item> waterSpear = spear("water_spear", Texture.N);
    public static final RegistryEntrySupplier<Item> halberd = spear("halberd", Texture.N);
    public static final RegistryEntrySupplier<Item> corsesca = spear("corsesca", Texture.N);
    public static final RegistryEntrySupplier<Item> corsescaPlus = spear("corsesca_plus", Texture.N);
    public static final RegistryEntrySupplier<Item> poisonSpear = spear("poison_spear", Texture.N);
    public static final RegistryEntrySupplier<Item> fiveStaff = spear("five_staff", Texture.N);
    public static final RegistryEntrySupplier<Item> heavyLance = spear("heavy_lance", Texture.N);
    public static final RegistryEntrySupplier<Item> featherLance = spear("feather_lance", Texture.N);
    public static final RegistryEntrySupplier<Item> iceberg = spear("iceberg", Texture.N);
    public static final RegistryEntrySupplier<Item> bloodLance = spear("blood_lance", Texture.N);
    public static final RegistryEntrySupplier<Item> magicalLance = spear("magical_lance", Texture.N);
    public static final RegistryEntrySupplier<Item> flareLance = spear("flare_lance", Texture.N);
    public static final RegistryEntrySupplier<Item> brionac = spear("brionac", Texture.N);
    public static final RegistryEntrySupplier<Item> poisonQueen = spear("poison_queen", Texture.N);
    public static final RegistryEntrySupplier<Item> monkStaff = spear("monk_staff", Texture.N);
    public static final RegistryEntrySupplier<Item> metus = spear("metus", Texture.N);
    public static final RegistryEntrySupplier<Item> silentGrave = spear("silent_grave", Texture.N);
    public static final RegistryEntrySupplier<Item> overbreak = spear("overbreak", Texture.N);
    public static final RegistryEntrySupplier<Item> bjor = spear("bjor", Texture.N);
    public static final RegistryEntrySupplier<Item> belvarose = spear("belvarose", Texture.N);
    public static final RegistryEntrySupplier<Item> gaeBolg = spear("gae_bolg", Texture.N);
    public static final RegistryEntrySupplier<Item> dragonsFang = spear("dragons_fang", Texture.N);
    public static final RegistryEntrySupplier<Item> gungnir = spear("gungnir", Texture.N);
    public static final RegistryEntrySupplier<Item> legion = spear("legion", Texture.N);
    public static final RegistryEntrySupplier<Item> pitchfork = spear("pitchfork", Texture.N);
    public static final RegistryEntrySupplier<Item> safetyLance = spear("safety_lance", Texture.N);
    public static final RegistryEntrySupplier<Item> pineClub = spear("pine_club", Texture.N);

    public static final RegistryEntrySupplier<Item> battleAxe = axe("battle_axe", Texture.Y);
    public static final RegistryEntrySupplier<Item> battleScythe = axe("battle_scythe", Texture.Y);
    public static final RegistryEntrySupplier<Item> poleAxe = axe("pole_axe", Texture.Y);
    public static final RegistryEntrySupplier<Item> poleAxePlus = axe("pole_axe_plus", Texture.Y);
    public static final RegistryEntrySupplier<Item> greatAxe = axe("great_axe", Texture.Y);
    public static final RegistryEntrySupplier<Item> tomohawk = axe("tomohawk", Texture.Y);
    public static final RegistryEntrySupplier<Item> basiliskFang = axe("basilisk_fang", Texture.N);
    public static final RegistryEntrySupplier<Item> rockAxe = axe("rock_axe", Texture.N);
    public static final RegistryEntrySupplier<Item> demonAxe = axe("demon_axe", Texture.N);
    public static final RegistryEntrySupplier<Item> frostAxe = axe("frost_axe", Texture.N);
    public static final RegistryEntrySupplier<Item> crescentAxe = axe("crescent_axe", Texture.N);
    public static final RegistryEntrySupplier<Item> crescentAxePlus = axe("crescent_axe_plus", Texture.N);
    public static final RegistryEntrySupplier<Item> heatAxe = axe("heat_axe", Texture.N);
    public static final RegistryEntrySupplier<Item> doubleEdge = axe("double_edge", Texture.N);
    public static final RegistryEntrySupplier<Item> alldale = axe("alldale", Texture.N);
    public static final RegistryEntrySupplier<Item> devilFinger = axe("devil_finger", Texture.N);
    public static final RegistryEntrySupplier<Item> executioner = axe("executioner", Texture.N);
    public static final RegistryEntrySupplier<Item> saintAxe = axe("saint_axe", Texture.N);
    public static final RegistryEntrySupplier<Item> axe = axe("axe", Texture.N);
    public static final RegistryEntrySupplier<Item> lollipop = axe("lollipop", Texture.N);

    public static final RegistryEntrySupplier<Item> battleHammer = hammer("battle_hammer", Texture.Y);
    public static final RegistryEntrySupplier<Item> bat = hammer("bat", Texture.Y);
    public static final RegistryEntrySupplier<Item> warHammer = hammer("war_hammer", Texture.Y);
    public static final RegistryEntrySupplier<Item> warHammerPlus = hammer("war_hammer_plus", Texture.Y);
    public static final RegistryEntrySupplier<Item> ironBat = hammer("iron_bat", Texture.Y);
    public static final RegistryEntrySupplier<Item> greatHammer = hammer("great_hammer", Texture.Y);
    public static final RegistryEntrySupplier<Item> iceHammer = hammer("ice_hammer", Texture.N);
    public static final RegistryEntrySupplier<Item> boneHammer = hammer("bone_hammer", Texture.N);
    public static final RegistryEntrySupplier<Item> strongStone = hammer("strong_stone", Texture.N);
    public static final RegistryEntrySupplier<Item> flameHammer = hammer("flame_hammer", Texture.N);
    public static final RegistryEntrySupplier<Item> gigantHammer = hammer("gigant_hammer", Texture.N);
    public static final RegistryEntrySupplier<Item> skyHammer = hammer("sky_hammer", Texture.N);
    public static final RegistryEntrySupplier<Item> gravitonHammer = hammer("graviton_hammer", Texture.N);
    public static final RegistryEntrySupplier<Item> spikedHammer = hammer("spiked_hammer", Texture.N);
    public static final RegistryEntrySupplier<Item> crystalHammer = hammer("crystal_hammer", Texture.N);
    public static final RegistryEntrySupplier<Item> schnabel = hammer("schnabel", Texture.N);
    public static final RegistryEntrySupplier<Item> gigantHammerPlus = hammer("gigant_hammer_plus", Texture.N);
    public static final RegistryEntrySupplier<Item> kongo = hammer("kongo", Texture.N);
    public static final RegistryEntrySupplier<Item> mjolnir = hammer("mjolnir", Texture.N);
    public static final RegistryEntrySupplier<Item> fatalCrush = hammer("fatal_crush", Texture.N);
    public static final RegistryEntrySupplier<Item> splashStar = hammer("splash_star", Texture.N);
    public static final RegistryEntrySupplier<Item> hammer = hammer("hammer", Texture.N);
    public static final RegistryEntrySupplier<Item> toyHammer = hammer("toy_hammer", Texture.N);

    public static final RegistryEntrySupplier<Item> shortDagger = dualBlade("short_dagger", Texture.Y);
    public static final RegistryEntrySupplier<Item> steelEdge = dualBlade("steel_edge", Texture.Y);
    public static final RegistryEntrySupplier<Item> frostEdge = dualBlade("frost_edge", Texture.Y);
    public static final RegistryEntrySupplier<Item> ironEdge = dualBlade("iron_edge", Texture.Y);
    public static final RegistryEntrySupplier<Item> thiefKnife = dualBlade("thief_knife", Texture.Y);
    public static final RegistryEntrySupplier<Item> windEdge = dualBlade("wind_edge", Texture.Y);
    public static final RegistryEntrySupplier<Item> gorgeousLx = dualBlade("gourgeous_lx", Texture.N);
    public static final RegistryEntrySupplier<Item> steelKatana = dualBlade("steel_katana", Texture.N);
    public static final RegistryEntrySupplier<Item> twinBlade = dualBlade("twin_blade", Texture.N);
    public static final RegistryEntrySupplier<Item> rampage = dualBlade("rampage", Texture.N);
    public static final RegistryEntrySupplier<Item> salamander = dualBlade("salamander", Texture.N);
    public static final RegistryEntrySupplier<Item> platinumEdge = dualBlade("platinum_edge", Texture.N);
    public static final RegistryEntrySupplier<Item> sonicDagger = dualBlade("sonic_dagger", Texture.N);
    public static final RegistryEntrySupplier<Item> chaosEdge = dualBlade("chaos_edge", Texture.N);
    public static final RegistryEntrySupplier<Item> desertWind = dualBlade("desert_wind", Texture.N);
    public static final RegistryEntrySupplier<Item> brokenWall = dualBlade("broken_wall", Texture.N);
    public static final RegistryEntrySupplier<Item> forceDivide = dualBlade("force_divide", Texture.N);
    public static final RegistryEntrySupplier<Item> heartFire = dualBlade("heart_fire", Texture.N);
    public static final RegistryEntrySupplier<Item> orcusSword = dualBlade("orcus_sword", Texture.N);
    public static final RegistryEntrySupplier<Item> deepBlizzard = dualBlade("deep_blizzard", Texture.N);
    public static final RegistryEntrySupplier<Item> darkInvitation = dualBlade("dark_invitation", Texture.N);
    public static final RegistryEntrySupplier<Item> priestSaber = dualBlade("priest_saber", Texture.N);
    public static final RegistryEntrySupplier<Item> efreet = dualBlade("efreet", Texture.N);
    public static final RegistryEntrySupplier<Item> dragoonClaw = dualBlade("dragoon_claw", Texture.N);
    public static final RegistryEntrySupplier<Item> emeraldEdge = dualBlade("emerald_edge", Texture.N);
    public static final RegistryEntrySupplier<Item> runeEdge = dualBlade("rune_edge", Texture.N);
    public static final RegistryEntrySupplier<Item> earnestEdge = dualBlade("earnest_edge", Texture.N);
    public static final RegistryEntrySupplier<Item> twinJustice = dualBlade("twin_justice", Texture.N);
    public static final RegistryEntrySupplier<Item> doubleScratch = dualBlade("double_scratch", Texture.N);
    public static final RegistryEntrySupplier<Item> acutorimass = dualBlade("acutorimass", Texture.N);
    public static final RegistryEntrySupplier<Item> twinLeeks = dualBlade("twin_leeks", Texture.N);

    public static final RegistryEntrySupplier<Item> leatherGlove = gloves("leather_glove", Texture.Y);
    public static final RegistryEntrySupplier<Item> brassKnuckles = gloves("brass_knuckles", Texture.Y);
    public static final RegistryEntrySupplier<Item> kote = gloves("kote", Texture.Y);
    public static final RegistryEntrySupplier<Item> gloves = gloves("gloves", Texture.Y);
    public static final RegistryEntrySupplier<Item> bearClaws = gloves("bear_claws", Texture.Y);
    public static final RegistryEntrySupplier<Item> fistEarth = gloves("fist_of_earth", Texture.Y);
    public static final RegistryEntrySupplier<Item> fistFire = gloves("fist_of_fire", Texture.N);
    public static final RegistryEntrySupplier<Item> fistWater = gloves("fist_of_water", Texture.N);
    public static final RegistryEntrySupplier<Item> dragonClaws = gloves("dragon_claws", Texture.N);
    public static final RegistryEntrySupplier<Item> fistDark = gloves("fist_of_dark", Texture.N);
    public static final RegistryEntrySupplier<Item> fistWind = gloves("fist_of_wind", Texture.N);
    public static final RegistryEntrySupplier<Item> fistLight = gloves("fist_of_light", Texture.N);
    public static final RegistryEntrySupplier<Item> catPunch = gloves("cat_punch", Texture.N);
    public static final RegistryEntrySupplier<Item> animalPuppets = gloves("animal_puppets", Texture.N);
    public static final RegistryEntrySupplier<Item> ironleafFists = gloves("ironleaf_fists", Texture.N);
    public static final RegistryEntrySupplier<Item> caestus = gloves("caestus", Texture.N);
    public static final RegistryEntrySupplier<Item> golemPunch = gloves("golem_punch", Texture.N);
    public static final RegistryEntrySupplier<Item> godHand = gloves("hand_of_god", Texture.N);
    public static final RegistryEntrySupplier<Item> bazalKatar = gloves("bazal_katar", Texture.N);
    public static final RegistryEntrySupplier<Item> fenrir = gloves("fenrir", Texture.N);

    public static final RegistryEntrySupplier<Item> rod = staff("rod", EnumElement.FIRE, 1, Texture.Y);
    public static final RegistryEntrySupplier<Item> amethystRod = staff("amethyst_rod", EnumElement.EARTH, 1, Texture.Y);
    public static final RegistryEntrySupplier<Item> aquamarineRod = staff("aquamarine_rod", EnumElement.WATER, 1, Texture.Y);
    public static final RegistryEntrySupplier<Item> friendlyRod = staff("friendly_rod", EnumElement.LOVE, 1, Texture.Y);
    public static final RegistryEntrySupplier<Item> loveLoveRod = staff("love_love_rod", EnumElement.LOVE, 1, Texture.Y);
    public static final RegistryEntrySupplier<Item> staff = staff("staff", EnumElement.EARTH, 1, Texture.Y);
    public static final RegistryEntrySupplier<Item> emeraldRod = staff("emerald_rod", EnumElement.WIND, 1, Texture.N);
    public static final RegistryEntrySupplier<Item> silverStaff = staff("silver_staff", EnumElement.DARK, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> flareStaff = staff("flare_staff", EnumElement.FIRE, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> rubyRod = staff("ruby_rod", EnumElement.FIRE, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> sapphireRod = staff("sapphire_rod", EnumElement.LIGHT, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> earthStaff = staff("earth_staff", EnumElement.EARTH, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> lightningWand = staff("lightning_wand", EnumElement.WIND, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> iceStaff = staff("ice_staff", EnumElement.WATER, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> diamondRod = staff("diamond_rod", EnumElement.DARK, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> wizardsStaff = staff("wizards_staff", EnumElement.LIGHT, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> magesStaff = staff("mages_staff", EnumElement.EARTH, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> shootingStarStaff = staff("shooting_star_staff", EnumElement.LIGHT, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> hellBranch = staff("hell_branch", EnumElement.DARK, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> crimsonStaff = staff("crimson_staff", EnumElement.FIRE, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> bubbleStaff = staff("bubble_staff", EnumElement.WATER, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> gaiaRod = staff("gaia_rod", EnumElement.EARTH, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> cycloneRod = staff("cyclone_rod", EnumElement.WIND, 3, Texture.N);
    public static final RegistryEntrySupplier<Item> stormWand = staff("storm_wand", EnumElement.WIND, 3, Texture.N);
    public static final RegistryEntrySupplier<Item> runeStaff = staff("rune_staff", EnumElement.LIGHT, 3, Texture.N);
    public static final RegistryEntrySupplier<Item> magesStaffPlus = staff("mages_staff_plus", EnumElement.LOVE, 3, Texture.N);
    public static final RegistryEntrySupplier<Item> magicBroom = staff("magic_broom", EnumElement.WIND, 3, Texture.N);
    public static final RegistryEntrySupplier<Item> magicShot = staff("magic_shot", EnumElement.LOVE, 3, Texture.N);
    public static final RegistryEntrySupplier<Item> hellCurse = staff("hell_curse", EnumElement.DARK, 3, Texture.N);
    public static final RegistryEntrySupplier<Item> algernon = staff("algernon", EnumElement.EARTH, 3, Texture.N);
    public static final RegistryEntrySupplier<Item> sorceresWand = staff("sorceres_wand", EnumElement.LIGHT, 3, Texture.N);
    public static final RegistryEntrySupplier<Item> basket = staff("basket", EnumElement.LOVE, 1, Texture.N);
    public static final RegistryEntrySupplier<Item> goldenTurnipStaff = staff("golden_turnip_staff", EnumElement.LOVE, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> sweetPotatoStaff = staff("sweet_potato_staff", EnumElement.LOVE, 1, Texture.N);
    public static final RegistryEntrySupplier<Item> elvishHarp = staff("elvish_harp", EnumElement.LOVE, 3, Texture.N);
    public static final RegistryEntrySupplier<Item> syringe = staff("syringe", EnumElement.WATER, 2, Texture.N);

    public static final RegistryEntrySupplier<Item> engagementRing = equipment(EquipmentSlot.LEGS, "engagement_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> cheapBracelet = equipment(EquipmentSlot.LEGS, "cheap_bracelet", Texture.Y);
    public static final RegistryEntrySupplier<Item> bronzeBracelet = equipment(EquipmentSlot.LEGS, "bronze_bracelet", Texture.Y);
    public static final RegistryEntrySupplier<Item> silverBracelet = equipment(EquipmentSlot.LEGS, "silver_bracelet", Texture.Y);
    public static final RegistryEntrySupplier<Item> goldBracelet = equipment(EquipmentSlot.LEGS, "gold_bracelet", Texture.Y);
    public static final RegistryEntrySupplier<Item> platinumBracelet = equipment(EquipmentSlot.LEGS, "platinum_bracelet", Texture.Y);
    public static final RegistryEntrySupplier<Item> silverRing = equipment(EquipmentSlot.LEGS, "silver_ring", Texture.Y);
    public static final RegistryEntrySupplier<Item> goldRing = equipment(EquipmentSlot.LEGS, "gold_ring", Texture.Y);
    public static final RegistryEntrySupplier<Item> platinumRing = equipment(EquipmentSlot.LEGS, "platinum_ring", Texture.Y);
    public static final RegistryEntrySupplier<Item> shieldRing = equipment(EquipmentSlot.LEGS, "shield_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> criticalRing = equipment(EquipmentSlot.LEGS, "critical_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> silentRing = equipment(EquipmentSlot.LEGS, "silent_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> paralysisRing = equipment(EquipmentSlot.LEGS, "paralysis_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> poisonRing = equipment(EquipmentSlot.LEGS, "poison_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> magicRing = equipment(EquipmentSlot.LEGS, "magic_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> throwingRing = equipment(EquipmentSlot.LEGS, "throwing_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> stayUpRing = equipment(EquipmentSlot.LEGS, "stay_up_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> aquamarineRing = equipment(EquipmentSlot.LEGS, "aquamarine_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> amethystRing = equipment(EquipmentSlot.LEGS, "amethyst_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> emeraldRing = equipment(EquipmentSlot.LEGS, "emerald_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> sapphireRing = equipment(EquipmentSlot.LEGS, "sapphire_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> rubyRing = equipment(EquipmentSlot.LEGS, "ruby_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> cursedRing = equipment(EquipmentSlot.LEGS, "cursed_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> diamondRing = equipment(EquipmentSlot.LEGS, "diamond_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> aquamarineBrooch = equipment(EquipmentSlot.LEGS, "aquamarine_brooch", Texture.N);
    public static final RegistryEntrySupplier<Item> amethystBrooch = equipment(EquipmentSlot.LEGS, "amethyst_brooch", Texture.N);
    public static final RegistryEntrySupplier<Item> emeraldBrooch = equipment(EquipmentSlot.LEGS, "emerald_brooch", Texture.N);
    public static final RegistryEntrySupplier<Item> sapphireBrooch = equipment(EquipmentSlot.LEGS, "sapphire_brooch", Texture.N);
    public static final RegistryEntrySupplier<Item> rubyBrooch = equipment(EquipmentSlot.LEGS, "ruby_brooch", Texture.N);
    public static final RegistryEntrySupplier<Item> diamondBrooch = equipment(EquipmentSlot.LEGS, "diamond_brooch", Texture.N);
    public static final RegistryEntrySupplier<Item> dolphinBrooch = equipment(EquipmentSlot.LEGS, "dolphin_brooch", Texture.N);
    public static final RegistryEntrySupplier<Item> fireRing = equipment(EquipmentSlot.LEGS, "fire_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> windRing = equipment(EquipmentSlot.LEGS, "wind_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> waterRing = equipment(EquipmentSlot.LEGS, "water_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> earthRing = equipment(EquipmentSlot.LEGS, "earth_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> happyRing = equipment(EquipmentSlot.LEGS, "happy_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> silverPendant = equipment(EquipmentSlot.LEGS, "silver_pendant", Texture.N);
    public static final RegistryEntrySupplier<Item> starPendant = equipment(EquipmentSlot.LEGS, "star_pendant", Texture.N);
    public static final RegistryEntrySupplier<Item> sunPendant = equipment(EquipmentSlot.LEGS, "sun_pendant", Texture.N);
    public static final RegistryEntrySupplier<Item> fieldPendant = equipment(EquipmentSlot.LEGS, "field_pendant", Texture.N);
    public static final RegistryEntrySupplier<Item> dewPendant = equipment(EquipmentSlot.LEGS, "dew_pendant", Texture.N);
    public static final RegistryEntrySupplier<Item> earthPendant = equipment(EquipmentSlot.LEGS, "earth_pendant", Texture.N);
    public static final RegistryEntrySupplier<Item> heartPendant = equipment(EquipmentSlot.LEGS, "heart_pendant", Texture.N);
    public static final RegistryEntrySupplier<Item> strangePendant = equipment(EquipmentSlot.LEGS, "strange_pendant", Texture.N);
    public static final RegistryEntrySupplier<Item> anettesNecklace = equipment(EquipmentSlot.LEGS, "anettes_necklace", Texture.N);
    public static final RegistryEntrySupplier<Item> workGloves = equipment(EquipmentSlot.LEGS, "work_gloves", Texture.N);
    public static final RegistryEntrySupplier<Item> glovesAccess = equipment(EquipmentSlot.LEGS, "gloves_accessory", Texture.N);
    public static final RegistryEntrySupplier<Item> powerGloves = equipment(EquipmentSlot.LEGS, "power_gloves", Texture.N);
    public static final RegistryEntrySupplier<Item> earrings = equipment(EquipmentSlot.LEGS, "earrings", Texture.N);
    public static final RegistryEntrySupplier<Item> witchEarrings = equipment(EquipmentSlot.LEGS, "witch_earrings", Texture.N);
    public static final RegistryEntrySupplier<Item> magicEarrings = equipment(EquipmentSlot.LEGS, "magic_earrings", Texture.Y);
    public static final RegistryEntrySupplier<Item> charm = equipment(EquipmentSlot.LEGS, "charm", Texture.N);
    public static final RegistryEntrySupplier<Item> holyAmulet = equipment(EquipmentSlot.LEGS, "holy_amulet", Texture.N);
    public static final RegistryEntrySupplier<Item> rosary = equipment(EquipmentSlot.LEGS, "rosary", Texture.N);
    public static final RegistryEntrySupplier<Item> talisman = equipment(EquipmentSlot.LEGS, "talisman", Texture.N);
    public static final RegistryEntrySupplier<Item> magicCharm = equipment(EquipmentSlot.LEGS, "magic_charm", Texture.N);
    public static final RegistryEntrySupplier<Item> leatherBelt = equipment(EquipmentSlot.LEGS, "leather_belt", Texture.N);
    public static final RegistryEntrySupplier<Item> luckyStrike = equipment(EquipmentSlot.LEGS, "lucky_strike", Texture.N);
    public static final RegistryEntrySupplier<Item> champBelt = equipment(EquipmentSlot.LEGS, "champ_belt", Texture.N);
    public static final RegistryEntrySupplier<Item> handKnitScarf = equipment(EquipmentSlot.LEGS, "hand_knit_scarf", Texture.N);
    public static final RegistryEntrySupplier<Item> fluffyScarf = equipment(EquipmentSlot.LEGS, "fluffy_scarf", Texture.N);
    public static final RegistryEntrySupplier<Item> herosProof = equipment(EquipmentSlot.LEGS, "heros_proof", Texture.N);
    public static final RegistryEntrySupplier<Item> proofOfWisdom = equipment(EquipmentSlot.LEGS, "proof_of_wisdom", Texture.N);
    public static final RegistryEntrySupplier<Item> artOfAttack = equipment(EquipmentSlot.LEGS, "art_of_attack", Texture.N);
    public static final RegistryEntrySupplier<Item> artOfDefense = equipment(EquipmentSlot.LEGS, "art_of_defense", Texture.N);
    public static final RegistryEntrySupplier<Item> artOfMagic = equipment(EquipmentSlot.LEGS, "art_of_magic", Texture.N);
    public static final RegistryEntrySupplier<Item> badge = equipment(EquipmentSlot.LEGS, "badge", Texture.N);
    public static final RegistryEntrySupplier<Item> courageBadge = equipment(EquipmentSlot.LEGS, "courage_badge", Texture.N);

    public static final RegistryEntrySupplier<Item> shirt = equipment(EquipmentSlot.CHEST, "shirt", Texture.Y);
    public static final RegistryEntrySupplier<Item> vest = equipment(EquipmentSlot.CHEST, "vest", Texture.Y);
    public static final RegistryEntrySupplier<Item> cottonCloth = equipment(EquipmentSlot.CHEST, "cotton_cloth", Texture.Y);
    public static final RegistryEntrySupplier<Item> mail = equipment(EquipmentSlot.CHEST, "mail", Texture.N);
    public static final RegistryEntrySupplier<Item> chainMail = equipment(EquipmentSlot.CHEST, "chain_mail", Texture.N);
    public static final RegistryEntrySupplier<Item> scaleVest = equipment(EquipmentSlot.CHEST, "scale_vest", Texture.N);
    public static final RegistryEntrySupplier<Item> sparklingShirt = equipment(EquipmentSlot.CHEST, "sparkling_shirt", Texture.N);
    public static final RegistryEntrySupplier<Item> windCloak = equipment(EquipmentSlot.CHEST, "wind_cloak", Texture.N);
    public static final RegistryEntrySupplier<Item> protector = equipment(EquipmentSlot.CHEST, "protector", Texture.N);
    public static final RegistryEntrySupplier<Item> platinumMail = equipment(EquipmentSlot.CHEST, "platinum_mail", Texture.N);
    public static final RegistryEntrySupplier<Item> lemellarVest = equipment(EquipmentSlot.CHEST, "lemellar_vest", Texture.N);
    public static final RegistryEntrySupplier<Item> mercenarysCloak = equipment(EquipmentSlot.CHEST, "mercenarys_cloak", Texture.N);
    public static final RegistryEntrySupplier<Item> woolyShirt = equipment(EquipmentSlot.CHEST, "wooly_shirt", Texture.N);
    public static final RegistryEntrySupplier<Item> elvishCloak = equipment(EquipmentSlot.CHEST, "elvish_cloak", Texture.N);
    public static final RegistryEntrySupplier<Item> dragonCloak = equipment(EquipmentSlot.CHEST, "dragon_cloak", Texture.N);
    public static final RegistryEntrySupplier<Item> powerProtector = equipment(EquipmentSlot.CHEST, "power_protector", Texture.N);
    public static final RegistryEntrySupplier<Item> runeVest = equipment(EquipmentSlot.CHEST, "rune_vest", Texture.N);
    public static final RegistryEntrySupplier<Item> royalGarter = equipment(EquipmentSlot.CHEST, "royal_garter", Texture.N);
    public static final RegistryEntrySupplier<Item> fourDragonsVest = equipment(EquipmentSlot.CHEST, "four_dragons_vest", Texture.N);

    public static final RegistryEntrySupplier<Item> headband = equipment(EquipmentSlot.HEAD, "headband", Texture.Y);
    public static final RegistryEntrySupplier<Item> blueRibbon = equipment(EquipmentSlot.HEAD, "blue_ribbon", Texture.Y, true);
    public static final RegistryEntrySupplier<Item> greenRibbon = equipment(EquipmentSlot.HEAD, "green_ribbon", Texture.Y, true);
    public static final RegistryEntrySupplier<Item> purpleRibbon = equipment(EquipmentSlot.HEAD, "purple_ribbon", Texture.Y, true);
    public static final RegistryEntrySupplier<Item> spectacles = equipment(EquipmentSlot.HEAD, "spectacles", Texture.N);
    public static final RegistryEntrySupplier<Item> strawHat = equipment(EquipmentSlot.HEAD, "straw_hat", Texture.N);
    public static final RegistryEntrySupplier<Item> fancyHat = equipment(EquipmentSlot.HEAD, "fancy_hat", Texture.N);
    public static final RegistryEntrySupplier<Item> brandGlasses = equipment(EquipmentSlot.HEAD, "brand_glasses", Texture.N);
    public static final RegistryEntrySupplier<Item> cuteKnitting = equipment(EquipmentSlot.HEAD, "cute_knitting", Texture.N);
    public static final RegistryEntrySupplier<Item> intelligentGlasses = equipment(EquipmentSlot.HEAD, "intelligent_glasses", Texture.N);
    public static final RegistryEntrySupplier<Item> fireproofHood = equipment(EquipmentSlot.HEAD, "fireproof_hood", Texture.N);
    public static final RegistryEntrySupplier<Item> silkHat = equipment(EquipmentSlot.HEAD, "silk_hat", Texture.N);
    public static final RegistryEntrySupplier<Item> blackRibbon = equipment(EquipmentSlot.HEAD, "black_ribbon", Texture.Y, true);
    public static final RegistryEntrySupplier<Item> lolitaHeaddress = equipment(EquipmentSlot.HEAD, "lolita_headdress", Texture.N);
    public static final RegistryEntrySupplier<Item> headdress = equipment(EquipmentSlot.HEAD, "headdress", Texture.N);
    public static final RegistryEntrySupplier<Item> yellowRibbon = equipment(EquipmentSlot.HEAD, "yellow_ribbon", Texture.Y, true);
    public static final RegistryEntrySupplier<Item> catEars = equipment(EquipmentSlot.HEAD, "cat_ears", Texture.N);
    public static final RegistryEntrySupplier<Item> silverHairpin = equipment(EquipmentSlot.HEAD, "silver_hairpin", Texture.N, true);
    public static final RegistryEntrySupplier<Item> redRibbon = equipment(EquipmentSlot.HEAD, "red_ribbon", Texture.Y);
    public static final RegistryEntrySupplier<Item> orangeRibbon = equipment(EquipmentSlot.HEAD, "orange_ribbon", Texture.Y, true);
    public static final RegistryEntrySupplier<Item> whiteRibbon = equipment(EquipmentSlot.HEAD, "white_ribbon", Texture.Y, true);
    public static final RegistryEntrySupplier<Item> fourSeasons = equipment(EquipmentSlot.HEAD, "four_seasons", Texture.N);
    public static final RegistryEntrySupplier<Item> feathersHat = equipment(EquipmentSlot.HEAD, "feathers_hat", Texture.N);
    public static final RegistryEntrySupplier<Item> goldHairpin = equipment(EquipmentSlot.HEAD, "gold_hairpin", Texture.N);
    public static final RegistryEntrySupplier<Item> indigoRibbon = equipment(EquipmentSlot.HEAD, "indigo_ribbon", Texture.Y, true);
    public static final RegistryEntrySupplier<Item> crown = equipment(EquipmentSlot.HEAD, "crown", Texture.N);
    public static final RegistryEntrySupplier<Item> turnipHeadgear = equipment(EquipmentSlot.HEAD, "turnip_headgear", Texture.N);
    public static final RegistryEntrySupplier<Item> pumpkinHeadgear = equipment(EquipmentSlot.HEAD, "pumpkin_headgear", Texture.N);

    public static final RegistryEntrySupplier<Item> leatherBoots = equipment(EquipmentSlot.FEET, "leather_boots", Texture.Y);
    public static final RegistryEntrySupplier<Item> freeFarmingShoes = equipment(EquipmentSlot.FEET, "free_farming_shoes", Texture.Y);
    public static final RegistryEntrySupplier<Item> piyoSandals = equipment(EquipmentSlot.FEET, "piyo_sandals", Texture.Y);
    public static final RegistryEntrySupplier<Item> secretShoes = equipment(EquipmentSlot.FEET, "secret_shoes", Texture.N);
    public static final RegistryEntrySupplier<Item> silverBoots = equipment(EquipmentSlot.FEET, "silver_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> heavyBoots = equipment(EquipmentSlot.FEET, "heavy_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> sneakingBoots = equipment(EquipmentSlot.FEET, "sneaking_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> fastStepBoots = equipment(EquipmentSlot.FEET, "fast_step_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> goldBoots = equipment(EquipmentSlot.FEET, "gold_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> boneBoots = equipment(EquipmentSlot.FEET, "bone_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> snowBoots = equipment(EquipmentSlot.FEET, "snow_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> striderBoots = equipment(EquipmentSlot.FEET, "strider_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> stepInBoots = equipment(EquipmentSlot.FEET, "step_in_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> featherBoots = equipment(EquipmentSlot.FEET, "feather_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> ghostBoots = equipment(EquipmentSlot.FEET, "ghost_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> ironGeta = equipment(EquipmentSlot.FEET, "iron_geta", Texture.N);
    public static final RegistryEntrySupplier<Item> knightBoots = equipment(EquipmentSlot.FEET, "knight_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> fairyBoots = equipment(EquipmentSlot.FEET, "fairy_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> wetBoots = equipment(EquipmentSlot.FEET, "wet_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> waterShoes = equipment(EquipmentSlot.FEET, "water_shoes", Texture.N);
    public static final RegistryEntrySupplier<Item> iceSkates = equipment(EquipmentSlot.FEET, "ice_skates", Texture.N);
    public static final RegistryEntrySupplier<Item> rocketWing = equipment(EquipmentSlot.FEET, "rocket_wing", Texture.N);

    public static final RegistryEntrySupplier<Item> seedShield = ITEMS.register("seed_shield_item", () -> new ItemSeedShield(new Item.Properties().stacksTo(1)));
    public static final RegistryEntrySupplier<Item> smallShield = shield("small_shield", Texture.Y);
    public static final RegistryEntrySupplier<Item> umbrella = shield("umbrella", Texture.N);
    public static final RegistryEntrySupplier<Item> ironShield = shield("iron_shield", Texture.Y);
    public static final RegistryEntrySupplier<Item> monkeyPlush = shield("monkey_plush", Texture.N);
    public static final RegistryEntrySupplier<Item> roundShield = shield("round_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> turtleShield = shield("turtle_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> chaosShield = shield("chaos_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> boneShield = shield("bone_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> magicShield = shield("magic_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> heavyShield = shield("heavy_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> platinumShield = shield("platinum_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> kiteShield = shield("kite_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> knightShield = shield("knight_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> elementShield = shield("element_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> magicalShield = shield("magical_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> prismShield = shield("prism_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> runeShield = shield("rune_shield", Texture.N);

    public static final RegistryEntrySupplier<Item> itemBlockForge = blockItem("forge", () -> ModBlocks.forge);
    public static final RegistryEntrySupplier<Item> itemBlockAccess = blockItem("accessory_workbench", () -> ModBlocks.accessory);
    public static final RegistryEntrySupplier<Item> itemBlockCooking = blockItem("cooking_table", () -> ModBlocks.cooking);
    public static final RegistryEntrySupplier<Item> itemBlockChem = blockItem("chemistry_set", () -> ModBlocks.chemistry);

    public static final RegistryEntrySupplier<Item> mineralIron = mineral(EnumMineralTier.IRON);
    public static final RegistryEntrySupplier<Item> mineralBronze = mineral(EnumMineralTier.BRONZE);
    public static final RegistryEntrySupplier<Item> mineralSilver = mineral(EnumMineralTier.SILVER);
    public static final RegistryEntrySupplier<Item> mineralGold = mineral(EnumMineralTier.GOLD);
    public static final RegistryEntrySupplier<Item> mineralPlatinum = mineral(EnumMineralTier.PLATINUM);
    public static final RegistryEntrySupplier<Item> mineralOrichalcum = mineral(EnumMineralTier.ORICHALCUM);
    public static final RegistryEntrySupplier<Item> mineralDiamond = mineral(EnumMineralTier.DIAMOND);
    public static final RegistryEntrySupplier<Item> mineralDragonic = mineral(EnumMineralTier.DRAGONIC);
    public static final RegistryEntrySupplier<Item> mineralAquamarine = mineral(EnumMineralTier.AQUAMARINE);
    public static final RegistryEntrySupplier<Item> mineralAmethyst = mineral(EnumMineralTier.AMETHYST);
    public static final RegistryEntrySupplier<Item> mineralRuby = mineral(EnumMineralTier.RUBY);
    public static final RegistryEntrySupplier<Item> mineralEmerald = mineral(EnumMineralTier.EMERALD);
    public static final RegistryEntrySupplier<Item> mineralSapphire = mineral(EnumMineralTier.SAPPHIRE);
    public static final RegistryEntrySupplier<Item> brokenMineralIron = brokenMineral(EnumMineralTier.IRON);
    public static final RegistryEntrySupplier<Item> brokenMineralBronze = brokenMineral(EnumMineralTier.BRONZE);
    public static final RegistryEntrySupplier<Item> brokenMineralSilver = brokenMineral(EnumMineralTier.SILVER);
    public static final RegistryEntrySupplier<Item> brokenMineralGold = brokenMineral(EnumMineralTier.GOLD);
    public static final RegistryEntrySupplier<Item> brokenMineralPlatinum = brokenMineral(EnumMineralTier.PLATINUM);
    public static final RegistryEntrySupplier<Item> brokenMineralOrichalcum = brokenMineral(EnumMineralTier.ORICHALCUM);
    public static final RegistryEntrySupplier<Item> brokenMineralDiamond = brokenMineral(EnumMineralTier.DIAMOND);
    public static final RegistryEntrySupplier<Item> brokenMineralDragonic = brokenMineral(EnumMineralTier.DRAGONIC);
    public static final RegistryEntrySupplier<Item> brokenMineralAquamarine = brokenMineral(EnumMineralTier.AQUAMARINE);
    public static final RegistryEntrySupplier<Item> brokenMineralAmethyst = brokenMineral(EnumMineralTier.AMETHYST);
    public static final RegistryEntrySupplier<Item> brokenMineralRuby = brokenMineral(EnumMineralTier.RUBY);
    public static final RegistryEntrySupplier<Item> brokenMineralEmerald = brokenMineral(EnumMineralTier.EMERALD);
    public static final RegistryEntrySupplier<Item> brokenMineralSapphire = brokenMineral(EnumMineralTier.SAPPHIRE);

    public static final RegistryEntrySupplier<Item> bronze = mat("bronze", Texture.Y);
    public static final RegistryEntrySupplier<Item> silver = mat("silver", Texture.Y);
    public static final RegistryEntrySupplier<Item> platinum = mat("platinum", Texture.Y);
    public static final RegistryEntrySupplier<Item> orichalcum = mat("orichalcum", Rarity.UNCOMMON, Texture.Y);
    public static final RegistryEntrySupplier<Item> dragonic = mat("dragonic_stone", Rarity.UNCOMMON, Texture.Y);
    public static final RegistryEntrySupplier<Item> scrap = mat("scrap", Texture.Y);
    public static final RegistryEntrySupplier<Item> scrapPlus = mat("scrap_plus", Texture.Y);
    public static final RegistryEntrySupplier<Item> amethyst = mat("amethyst", Texture.Y);
    public static final RegistryEntrySupplier<Item> aquamarine = mat("aquamarine", Texture.Y);
    public static final RegistryEntrySupplier<Item> ruby = mat("ruby", Texture.Y);
    public static final RegistryEntrySupplier<Item> sapphire = mat("sapphire", Texture.Y);
    public static final RegistryEntrySupplier<Item> coreRed = mat("red_core", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> coreBlue = mat("blue_core", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> coreYellow = mat("yellow_core", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> coreGreen = mat("green_core", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> crystalSkull = mat("crystal_skull", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> crystalWater = mat("water_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item> crystalEarth = mat("earth_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item> crystalFire = mat("fire_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item> crystalWind = mat("wind_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item> crystalLight = mat("light_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item> crystalDark = mat("dark_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item> crystalLove = mat("love_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item> crystalSmall = mat("small_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item> crystalBig = mat("big_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item> crystalMagic = mat("magic_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item> crystalRune = mat("rune_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item> crystalElectro = mat("electro_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item> stickThick = mat("thick_stick", Texture.Y);
    public static final RegistryEntrySupplier<Item> hornInsect = mat("insect_horn", Texture.Y);
    public static final RegistryEntrySupplier<Item> hornRigid = mat("rigid_horn", Texture.Y);
    public static final RegistryEntrySupplier<Item> hornDevil = mat("devil_horn", Texture.Y);
    public static final RegistryEntrySupplier<Item> plantStem = mat("plant_stem", Texture.Y);
    public static final RegistryEntrySupplier<Item> hornBull = mat("bulls_horn", Texture.Y);
    public static final RegistryEntrySupplier<Item> movingBranch = mat("moving_branch", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> glue = mat("glue", Texture.Y);
    public static final RegistryEntrySupplier<Item> devilBlood = mat("devil_blood", Texture.Y);
    public static final RegistryEntrySupplier<Item> paraPoison = mat("paralysis_poison", Texture.Y);
    public static final RegistryEntrySupplier<Item> poisonKing = mat("poison_king", Texture.Y);
    public static final RegistryEntrySupplier<Item> featherBlack = mat("black_feather", Texture.Y);
    public static final RegistryEntrySupplier<Item> featherThunder = mat("thunder_feather", Texture.Y);
    public static final RegistryEntrySupplier<Item> featherYellow = mat("yellow_feather", Texture.Y);
    public static final RegistryEntrySupplier<Item> dragonFin = mat("dragon_fin", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> turtleShell = mat("turtle_shell", Texture.Y);
    public static final RegistryEntrySupplier<Item> fishFossil = mat("fish_fossil", Texture.Y);
    public static final RegistryEntrySupplier<Item> skull = mat("skull", Texture.Y);
    public static final RegistryEntrySupplier<Item> dragonBones = mat("dragon_bones", Texture.Y);
    public static final RegistryEntrySupplier<Item> tortoiseShell = mat("black_tortoise_shell", Texture.Y);
    public static final RegistryEntrySupplier<Item> rock = mat("rock", Texture.Y);
    public static final RegistryEntrySupplier<Item> stoneRound = mat("round_stone", Texture.Y);
    public static final RegistryEntrySupplier<Item> stoneTiny = mat("tiny_golem_stone", Texture.Y);
    public static final RegistryEntrySupplier<Item> stoneGolem = mat("golem_stone", Texture.Y);
    public static final RegistryEntrySupplier<Item> tabletGolem = mat("golem_tablet", Texture.Y);
    public static final RegistryEntrySupplier<Item> stoneSpirit = mat("golem_spirit_stone", Texture.Y);
    public static final RegistryEntrySupplier<Item> tabletTruth = mat("tablet_of_truth", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> yarn = mat("yarn", Texture.Y);
    public static final RegistryEntrySupplier<Item> oldBandage = mat("old_bandage", Texture.Y);
    public static final RegistryEntrySupplier<Item> ambrosiasThorns = mat("ambrosias_thorns", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> threadSpider = mat("spider_thread", Texture.N);
    public static final RegistryEntrySupplier<Item> puppetryStrings = mat("puppetry_strings", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> vine = mat("vine", Texture.N);
    public static final RegistryEntrySupplier<Item> tailScorpion = mat("scorpion_tail", Texture.N);
    public static final RegistryEntrySupplier<Item> strongVine = mat("strong_vine", Texture.N);
    public static final RegistryEntrySupplier<Item> threadPretty = mat("pretty_thread", Texture.N);
    public static final RegistryEntrySupplier<Item> tailChimera = mat("chimera_tail", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> arrowHead = mat("arrowhead", Texture.Y);
    public static final RegistryEntrySupplier<Item> bladeShard = mat("blade_shard", Texture.Y);
    public static final RegistryEntrySupplier<Item> brokenHilt = mat("broken_hilt", Texture.Y);
    public static final RegistryEntrySupplier<Item> brokenBox = mat("broken_box", Texture.Y);
    public static final RegistryEntrySupplier<Item> bladeGlistening = mat("glistening_blade", Texture.N);
    public static final RegistryEntrySupplier<Item> greatHammerShard = mat("great_hammer_shard", Texture.N);
    public static final RegistryEntrySupplier<Item> hammerPiece = mat("hammer_piece", Texture.N);
    public static final RegistryEntrySupplier<Item> shoulderPiece = mat("shoulder_piece", Texture.N);
    public static final RegistryEntrySupplier<Item> piratesArmor = mat("pirates_armor", Texture.N);
    public static final RegistryEntrySupplier<Item> screwRusty = mat("rusty_screw", Texture.N);
    public static final RegistryEntrySupplier<Item> screwShiny = mat("shiny_screw", Texture.N);
    public static final RegistryEntrySupplier<Item> rockShardLeft = mat("left_rock_shard", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> rockShardRight = mat("right_rock_shard", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> MTGUPlate = mat("mtgu_plate", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> brokenIceWall = mat("broken_ice_wall", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> furSmall = mat("fur_s", Texture.Y);
    public static final RegistryEntrySupplier<Item> furMedium = mat("fur_m", Texture.Y);
    public static final RegistryEntrySupplier<Item> furLarge = mat("fur_l", Texture.Y);
    public static final RegistryEntrySupplier<Item> fur = mat("fur", Texture.Y);
    public static final RegistryEntrySupplier<Item> furball = mat("wooly_furball", Texture.N);
    public static final RegistryEntrySupplier<Item> downYellow = mat("yellow_down", Texture.Y);
    public static final RegistryEntrySupplier<Item> furQuality = mat("quality_puffy_fur", Texture.Y);
    public static final RegistryEntrySupplier<Item> downPenguin = mat("penguin_down", Texture.Y);
    public static final RegistryEntrySupplier<Item> lightningMane = mat("lightning_mane", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> furRedLion = mat("red_lion_fur", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> furBlueLion = mat("blue_lion_fur", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> chestHair = mat("chest_hair", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> spore = mat("spore", Texture.Y);
    public static final RegistryEntrySupplier<Item> powderPoison = mat("poison_powder", Texture.N);
    public static final RegistryEntrySupplier<Item> sporeHoly = mat("holy_spore", Texture.N);
    public static final RegistryEntrySupplier<Item> fairyDust = mat("fairy_dust", Texture.Y);
    public static final RegistryEntrySupplier<Item> fairyElixir = mat("fairy_elixir", Texture.Y);
    public static final RegistryEntrySupplier<Item> root = mat("root", Texture.N);
    public static final RegistryEntrySupplier<Item> powderMagic = mat("magic_powder", Texture.N);
    public static final RegistryEntrySupplier<Item> powderMysterious = mat("mysterious_powder", Texture.N);
    public static final RegistryEntrySupplier<Item> magic = mat("magic", Texture.N);
    public static final RegistryEntrySupplier<Item> ashEarth = mat("earth_dragon_ash", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> ashFire = mat("fire_dragon_ash", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> ashWater = mat("water_dragon_ash", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> turnipsMiracle = mat("turnips_miracle", Texture.N);
    public static final RegistryEntrySupplier<Item> melodyBottle = mat("melody_bottle", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> clothCheap = mat("cheap_cloth", Texture.Y);
    public static final RegistryEntrySupplier<Item> clothQuality = mat("quality_cloth", Texture.Y);
    public static final RegistryEntrySupplier<Item> clothQualityWorn = mat("quality_worn_cloth", Texture.Y);
    public static final RegistryEntrySupplier<Item> clothSilk = mat("silk_cloth", Texture.N);
    public static final RegistryEntrySupplier<Item> ghostHood = mat("ghost_hood", Texture.Y);
    public static final RegistryEntrySupplier<Item> gloveGiant = mat("giants_glove", Texture.N);
    public static final RegistryEntrySupplier<Item> gloveBlueGiant = mat("blue_giants_glove", Texture.N);
    public static final RegistryEntrySupplier<Item> carapaceInsect = mat("insect_carapace", Texture.Y);
    public static final RegistryEntrySupplier<Item> carapacePretty = mat("pretty_carapace", Texture.Y);
    public static final RegistryEntrySupplier<Item> clothAncientOrc = mat("ancient_orc_cloth", Texture.N);
    public static final RegistryEntrySupplier<Item> jawInsect = mat("insect_jaw", Texture.Y);
    public static final RegistryEntrySupplier<Item> clawPanther = mat("panther_claw", Texture.Y);
    public static final RegistryEntrySupplier<Item> clawMagic = mat("magic_claw", Texture.Y);
    public static final RegistryEntrySupplier<Item> fangWolf = mat("wolf_fang", Texture.N);
    public static final RegistryEntrySupplier<Item> fangGoldWolf = mat("gold_wolf_fang", Texture.N);
    public static final RegistryEntrySupplier<Item> clawPalm = mat("palm_claw", Texture.N);
    public static final RegistryEntrySupplier<Item> clawMalm = mat("malm_claw", Texture.N);
    public static final RegistryEntrySupplier<Item> giantsNail = mat("giants_nail", Texture.N);
    public static final RegistryEntrySupplier<Item> clawChimera = mat("chimeras_claw", Texture.N);
    public static final RegistryEntrySupplier<Item> tuskIvory = mat("ivory_tusk", Texture.N);
    public static final RegistryEntrySupplier<Item> tuskUnbrokenIvory = mat("unbroken_tusk", Texture.N);
    public static final RegistryEntrySupplier<Item> scorpionPincer = mat("scorpion_pincer", Texture.N);
    public static final RegistryEntrySupplier<Item> dangerousScissors = mat("dangerous_scissors", Texture.N);
    public static final RegistryEntrySupplier<Item> propellorCheap = mat("cheap_propeller", Texture.N);
    public static final RegistryEntrySupplier<Item> propellorQuality = mat("quality_propeller", Texture.N);
    public static final RegistryEntrySupplier<Item> fangDragon = mat("dragon_fang", Texture.N);
    public static final RegistryEntrySupplier<Item> jawQueen = mat("queens_jaw", Texture.Y);
    public static final RegistryEntrySupplier<Item> windDragonTooth = mat("wind_dragon_tooth", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> giantsNailBig = mat("big_giants_nail", Texture.N);
    public static final RegistryEntrySupplier<Item> scaleWet = mat("wet_scale", Texture.Y);
    public static final RegistryEntrySupplier<Item> scaleGrimoire = mat("grimoire_scale", Texture.Y);
    public static final RegistryEntrySupplier<Item> scaleDragon = mat("dragon_scale", Texture.Y);
    public static final RegistryEntrySupplier<Item> scaleCrimson = mat("crimson_scale", Texture.Y);
    public static final RegistryEntrySupplier<Item> scaleBlue = mat("blue_scale", Texture.Y);
    public static final RegistryEntrySupplier<Item> scaleGlitter = mat("glitter_scale", Texture.Y);
    public static final RegistryEntrySupplier<Item> scaleLove = mat("love_scale", Texture.Y);
    public static final RegistryEntrySupplier<Item> scaleBlack = mat("black_scale", Texture.Y);
    public static final RegistryEntrySupplier<Item> scaleFire = mat("fire_wyrm_scale", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> scaleEarth = mat("earth_wyrm_scale", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> scaleLegend = mat("legendary_scale", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> steelDouble = mat("double_steel", Texture.Y);
    public static final RegistryEntrySupplier<Item> steelTen = mat("ten_fold_steel", Texture.N);
    public static final RegistryEntrySupplier<Item> glittaAugite = mat("glitta_augite", Texture.N);
    public static final RegistryEntrySupplier<Item> invisStone = mat("invisible_stone", Texture.Y);
    public static final RegistryEntrySupplier<Item> lightOre = mat("light_ore", Texture.N);
    public static final RegistryEntrySupplier<Item> runeSphereShard = mat("rune_sphere_shard", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> shadeStone = mat("shade_stone", Texture.N);
    public static final RegistryEntrySupplier<Item> racoonLeaf = mat("racoon_leaf", Texture.N);
    public static final RegistryEntrySupplier<Item> icyNose = mat("icy_nose", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> bigBirdsComb = mat("big_birds_comb", Texture.N);
    public static final RegistryEntrySupplier<Item> rafflesiaPetal = mat("rafflesia_petal", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> cursedDoll = mat("cursed_doll", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> warriorsProof = mat("warriors_proof", Texture.Y);
    public static final RegistryEntrySupplier<Item> proofOfRank = mat("proof_of_rank", Texture.Y);
    public static final RegistryEntrySupplier<Item> throneOfEmpire = mat("throne_of_emire", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> whiteStone = mat("white_stone", Texture.N);
    public static final RegistryEntrySupplier<Item> rareCan = mat("rare_can", Rarity.UNCOMMON, Texture.N);
    public static final RegistryEntrySupplier<Item> can = mat("can", Texture.N);
    public static final RegistryEntrySupplier<Item> boots = mat("boots", Texture.N);
    public static final RegistryEntrySupplier<Item> lawn = mat("ayngondaia_lawn", Rarity.UNCOMMON, Texture.N);

    //Skills and Magic
    public static final RegistryEntrySupplier<Item> fireBallSmall = spell(() -> ModSpells.FIREBALL, "fireball");
    public static final RegistryEntrySupplier<Item> fireBallBig = spell(() -> ModSpells.BIGFIREBALL, "fireball_big");
    public static final RegistryEntrySupplier<Item> explosion = spell(() -> ModSpells.EXPLOSION, "explosion");
    public static final RegistryEntrySupplier<Item> waterLaser = spell(() -> ModSpells.WATERLASER, "water_laser");
    public static final RegistryEntrySupplier<Item> parallelLaser = spell(() -> ModSpells.PARALLELLASER, "parallel_laser");
    public static final RegistryEntrySupplier<Item> deltaLaser = spell(() -> ModSpells.DELTALASER, "delta_laser");
    public static final RegistryEntrySupplier<Item> screwRock = spell(() -> ModSpells.SCREWROCK, "screw_rock");
    public static final RegistryEntrySupplier<Item> earthSpike = spell(() -> ModSpells.EMPTY, "earth_spike");
    public static final RegistryEntrySupplier<Item> avengerRock = spell(() -> ModSpells.AVENGERROCK, "avenger_rock");
    public static final RegistryEntrySupplier<Item> sonicWind = spell(() -> ModSpells.SONIC, "sonic_wind");
    public static final RegistryEntrySupplier<Item> doubleSonic = spell(() -> ModSpells.DOUBLESONIC, "double_sonic");
    public static final RegistryEntrySupplier<Item> penetrateSonic = spell(() -> ModSpells.PENETRATESONIC, "penetrate_sonic");
    public static final RegistryEntrySupplier<Item> lightBarrier = spell(() -> ModSpells.LIGHTBARRIER, "light_barrier");
    public static final RegistryEntrySupplier<Item> shine = spell(() -> ModSpells.SHINE, "shine");
    public static final RegistryEntrySupplier<Item> prism = spell(() -> ModSpells.PRISM, "prism");
    public static final RegistryEntrySupplier<Item> darkSnake = spell(() -> ModSpells.DARKSNAKE, "dark_snake");
    public static final RegistryEntrySupplier<Item> darkBall = spell(() -> ModSpells.DARKBALL, "dark_ball");
    public static final RegistryEntrySupplier<Item> darkness = spell(() -> ModSpells.DARKNESS, "darkness");
    public static final RegistryEntrySupplier<Item> cure = spell(() -> ModSpells.CURE, "cure");
    public static final RegistryEntrySupplier<Item> cureAll = spell(() -> ModSpells.CUREALL, "cure_all");
    public static final RegistryEntrySupplier<Item> cureMaster = spell(() -> ModSpells.MASTERCURE, "cure_master");
    public static final RegistryEntrySupplier<Item> mediPoison = spell(() -> ModSpells.MEDIPOISON, "medi_poison");
    public static final RegistryEntrySupplier<Item> mediPara = spell(() -> ModSpells.MEDIPARA, "medi_paralysis");
    public static final RegistryEntrySupplier<Item> mediSeal = spell(() -> ModSpells.MEDISEAL, "medi_seal");
    public static final RegistryEntrySupplier<Item> greeting = spell(() -> ModSpells.EMPTY, "greeting");
    public static final RegistryEntrySupplier<Item> powerWave = spell(() -> ModSpells.EMPTY, "power_wave");
    public static final RegistryEntrySupplier<Item> dashSlash = spell(() -> ModSpells.EMPTY, "dash_slash");
    public static final RegistryEntrySupplier<Item> rushAttack = spell(() -> ModSpells.EMPTY, "rush_attack");
    public static final RegistryEntrySupplier<Item> roundBreak = spell(() -> ModSpells.EMPTY, "round_break");
    public static final RegistryEntrySupplier<Item> mindThrust = spell(() -> ModSpells.EMPTY, "mind_thrust");
    public static final RegistryEntrySupplier<Item> gust = spell(() -> ModSpells.EMPTY, "gust");
    public static final RegistryEntrySupplier<Item> storm = spell(() -> ModSpells.EMPTY, "storm");
    public static final RegistryEntrySupplier<Item> blitz = spell(() -> ModSpells.EMPTY, "blitz");
    public static final RegistryEntrySupplier<Item> twinAttack = spell(() -> ModSpells.EMPTY, "twin_attack");
    public static final RegistryEntrySupplier<Item> railStrike = spell(() -> ModSpells.EMPTY, "rail_strike");
    public static final RegistryEntrySupplier<Item> windSlash = spell(() -> ModSpells.EMPTY, "wind_slash");
    public static final RegistryEntrySupplier<Item> flashStrike = spell(() -> ModSpells.EMPTY, "flash_strike");
    public static final RegistryEntrySupplier<Item> naiveBlade = spell(() -> ModSpells.EMPTY, "naive_blade");
    public static final RegistryEntrySupplier<Item> steelHeart = spell(() -> ModSpells.EMPTY, "steel_heart");
    public static final RegistryEntrySupplier<Item> deltaStrike = spell(() -> ModSpells.EMPTY, "delta_strike");
    public static final RegistryEntrySupplier<Item> hurricane = spell(() -> ModSpells.EMPTY, "hurricane");
    public static final RegistryEntrySupplier<Item> reaperSlash = spell(() -> ModSpells.EMPTY, "reaper_slash");
    public static final RegistryEntrySupplier<Item> millionStrike = spell(() -> ModSpells.EMPTY, "million_strike");
    public static final RegistryEntrySupplier<Item> axelDisaster = spell(() -> ModSpells.EMPTY, "axel_disaster");
    public static final RegistryEntrySupplier<Item> stardustUpper = spell(() -> ModSpells.EMPTY, "stardust_upper");
    public static final RegistryEntrySupplier<Item> tornadoSwing = spell(() -> ModSpells.EMPTY, "tornado_swing");
    public static final RegistryEntrySupplier<Item> grandImpact = spell(() -> ModSpells.EMPTY, "grand_impact");
    public static final RegistryEntrySupplier<Item> gigaSwing = spell(() -> ModSpells.EMPTY, "giga_swing");
    public static final RegistryEntrySupplier<Item> upperCut = spell(() -> ModSpells.EMPTY, "upper_cut");
    public static final RegistryEntrySupplier<Item> doubleKick = spell(() -> ModSpells.EMPTY, "double_kick");
    public static final RegistryEntrySupplier<Item> straightPunch = spell(() -> ModSpells.EMPTY, "straight_punch");
    public static final RegistryEntrySupplier<Item> nekoDamashi = spell(() -> ModSpells.EMPTY, "neko_damashi");
    public static final RegistryEntrySupplier<Item> rushPunch = spell(() -> ModSpells.EMPTY, "rush_punch");
    public static final RegistryEntrySupplier<Item> cyclone = spell(() -> ModSpells.EMPTY, "cyclone");
    public static final RegistryEntrySupplier<Item> rapidMove = spell(() -> ModSpells.EMPTY, "rapid_move");
    public static final RegistryEntrySupplier<Item> bonusConcerto = spell(() -> ModSpells.EMPTY, "bonus_concerto");
    public static final RegistryEntrySupplier<Item> strikingMarch = spell(() -> ModSpells.EMPTY, "striking_march");
    public static final RegistryEntrySupplier<Item> ironWaltz = spell(() -> ModSpells.EMPTY, "iron_waltz");
    public static final RegistryEntrySupplier<Item> teleport = spell(() -> ModSpells.TELEPORT, "teleport");

    public static final RegistryEntrySupplier<Item> rockfish = fish("rockfish", Texture.N);
    public static final RegistryEntrySupplier<Item> sandFlounder = fish("sand_flounder", Texture.N);
    public static final RegistryEntrySupplier<Item> pondSmelt = fish("pond_smelt", Texture.N);
    public static final RegistryEntrySupplier<Item> lobster = fish("lobster", Texture.N);
    public static final RegistryEntrySupplier<Item> lampSquid = fish("lamb_squid", Texture.N);
    public static final RegistryEntrySupplier<Item> cherrySalmon = fish("cherry_salmon", Texture.N);
    public static final RegistryEntrySupplier<Item> fallFlounder = fish("fall_flounder", Texture.N);
    public static final RegistryEntrySupplier<Item> girella = fish("girella", Texture.N);
    public static final RegistryEntrySupplier<Item> tuna = fish("tuna", Texture.N);
    public static final RegistryEntrySupplier<Item> crucianCarp = fish("crucian_carp", Texture.N);
    public static final RegistryEntrySupplier<Item> yellowtail = fish("yellowtail", Texture.N);
    public static final RegistryEntrySupplier<Item> blowfish = fish("blowfish", Texture.N);
    public static final RegistryEntrySupplier<Item> flounder = fish("flounder", Texture.N);
    public static final RegistryEntrySupplier<Item> rainbowTrout = fish("rainbow_trout", Texture.N);
    public static final RegistryEntrySupplier<Item> loverSnapper = fish("lover_snapper", Texture.N);
    public static final RegistryEntrySupplier<Item> snapper = fish("snapper", Texture.N);
    public static final RegistryEntrySupplier<Item> shrimp = fish("shrimp", Texture.N);
    public static final RegistryEntrySupplier<Item> sunsquid = fish("sunsquid", Texture.N);
    public static final RegistryEntrySupplier<Item> pike = fish("pike", Texture.N);
    public static final RegistryEntrySupplier<Item> needlefish = fish("needle_fish", Texture.N);
    public static final RegistryEntrySupplier<Item> mackerel = fish("mackerel", Texture.N);
    public static final RegistryEntrySupplier<Item> salmon = fish("salmon", Texture.N);
    public static final RegistryEntrySupplier<Item> gibelio = fish("gibelio", Texture.N);
    public static final RegistryEntrySupplier<Item> turbot = fish("turbot", Texture.N);
    public static final RegistryEntrySupplier<Item> skipjack = fish("skipjack", Texture.N);
    public static final RegistryEntrySupplier<Item> glitterSnapper = fish("glitter_snapper", Texture.N);
    public static final RegistryEntrySupplier<Item> chub = fish("chub", Texture.N);
    public static final RegistryEntrySupplier<Item> charFish = fish("char", Texture.N);
    public static final RegistryEntrySupplier<Item> sardine = fish("sardine", Texture.N);
    public static final RegistryEntrySupplier<Item> taimen = fish("taimen", Texture.N);
    public static final RegistryEntrySupplier<Item> squid = fish("squid", Texture.N);
    public static final RegistryEntrySupplier<Item> masuTrout = fish("masu_trout", Texture.N);

    public static final RegistryEntrySupplier<Item> icon0 = ITEMS.register("icon_0", () -> new Item(new Item.Properties()));
    public static final RegistryEntrySupplier<Item> debug = ITEMS.register("debug_item", () -> new ItemDebug(new Item.Properties()));
    public static final RegistryEntrySupplier<Item> level = ITEMS.register("level_item", () -> new ItemLevelUp(new Item.Properties()));
    public static final RegistryEntrySupplier<Item> skill = ITEMS.register("skill_item", () -> new ItemSkillUp(new Item.Properties()));
    public static final RegistryEntrySupplier<Item> tame = ITEMS.register("insta_tame", () -> new ItemDebug(new Item.Properties()));
    public static final RegistryEntrySupplier<Item> entityLevel = ITEMS.register("entity_level_item", () -> new ItemDebug(new Item.Properties()));
    public static final RegistryEntrySupplier<Item> unknown = ITEMS.register("unknown", () -> new Item(new Item.Properties()));

    //Crop items
    public static final RegistryEntrySupplier<Item> turnipSeeds = seed("turnip", () -> ModBlocks.turnip);
    public static final RegistryEntrySupplier<Item> turnipPinkSeeds = seed("turnip_pink", () -> ModBlocks.turnipPink);
    public static final RegistryEntrySupplier<Item> cabbageSeeds = seed("cabbage", () -> ModBlocks.cabbage);
    public static final RegistryEntrySupplier<Item> pinkMelonSeeds = seed("pink_melon", () -> ModBlocks.pinkMelon);
    public static final RegistryEntrySupplier<Item> hotHotSeeds = seed("hot_hot_fruit", () -> ModBlocks.hotHotFruit);
    public static final RegistryEntrySupplier<Item> goldTurnipSeeds = seed("golden_turnip", () -> ModBlocks.goldenTurnip);
    public static final RegistryEntrySupplier<Item> goldPotatoSeeds = seed("golden_potato", () -> ModBlocks.goldenPotato);
    public static final RegistryEntrySupplier<Item> goldPumpkinSeeds = seed("golden_pumpkin", () -> ModBlocks.goldenPumpkin);
    public static final RegistryEntrySupplier<Item> goldCabbageSeeds = seed("golden_cabbage", () -> ModBlocks.goldenCabbage);
    public static final RegistryEntrySupplier<Item> bokChoySeeds = seed("bok_choy", () -> ModBlocks.bokChoy);
    public static final RegistryEntrySupplier<Item> leekSeeds = seed("leek", () -> ModBlocks.leek);
    public static final RegistryEntrySupplier<Item> radishSeeds = seed("radish", () -> ModBlocks.radish);
    public static final RegistryEntrySupplier<Item> greenPepperSeeds = seed("green_pepper", () -> ModBlocks.greenPepper);
    public static final RegistryEntrySupplier<Item> spinachSeeds = seed("spinach", () -> ModBlocks.spinach);
    public static final RegistryEntrySupplier<Item> yamSeeds = seed("yam", () -> ModBlocks.yam);
    public static final RegistryEntrySupplier<Item> eggplantSeeds = seed("eggplant", () -> ModBlocks.eggplant);
    public static final RegistryEntrySupplier<Item> pineappleSeeds = seed("pineapple", () -> ModBlocks.pineapple);
    public static final RegistryEntrySupplier<Item> pumpkinSeeds = seed("pumpkin", () -> ModBlocks.pumpkin);
    public static final RegistryEntrySupplier<Item> onionSeeds = seed("onion", () -> ModBlocks.onion);
    public static final RegistryEntrySupplier<Item> cornSeeds = seed("corn", () -> ModBlocks.corn);
    public static final RegistryEntrySupplier<Item> tomatoSeeds = seed("tomato", () -> ModBlocks.tomato);
    public static final RegistryEntrySupplier<Item> strawberrySeeds = seed("strawberry", () -> ModBlocks.strawberry);
    public static final RegistryEntrySupplier<Item> cucumberSeeds = seed("cucumber", () -> ModBlocks.cucumber);
    public static final RegistryEntrySupplier<Item> fodderSeeds = seed("fodder", () -> ModBlocks.fodder);

    public static final RegistryEntrySupplier<Item> fodder = mat("fodder", Texture.N);

    public static final RegistryEntrySupplier<Item> turnip = crop("turnip", false, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> turnipGiant = crop("turnip", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> turnipPink = crop("turnip_pink", false, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> turnipPinkGiant = crop("turnip_pink", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> cabbage = crop("cabbage", false, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> cabbageGiant = crop("cabbage", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> pinkMelon = crop("pink_melon", false, Texture.Y, 1);
    public static final RegistryEntrySupplier<Item> pinkMelonGiant = crop("pink_melon", true, Texture.N, 1);
    public static final RegistryEntrySupplier<Item> pineapple = crop("pineapple", false, Texture.N, 1);
    public static final RegistryEntrySupplier<Item> pineappleGiant = crop("pineapple", true, Texture.N, 1);
    public static final RegistryEntrySupplier<Item> strawberry = crop("strawberry", false, Texture.N, 1);
    public static final RegistryEntrySupplier<Item> strawberryGiant = crop("strawberry", true, Texture.N, 1);
    public static final RegistryEntrySupplier<Item> goldenTurnip = crop("golden_turnip", false, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> goldenTurnipGiant = crop("golden_turnip", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> goldenPotato = crop("golden_potato", false, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> goldenPotatoGiant = crop("golden_potato", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> goldenPumpkin = crop("golden_pumpkin", false, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> goldenPumpkinGiant = crop("golden_pumpkin", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> goldenCabbage = crop("golden_cabbage", false, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> goldenCabbageGiant = crop("golden_cabbage", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> hotHotFruit = crop("hot_hot_fruit", false, Texture.N, 1);
    public static final RegistryEntrySupplier<Item> hotHotFruitGiant = crop("hot_hot_fruit", true, Texture.N, 1);
    public static final RegistryEntrySupplier<Item> bokChoy = crop("bok_choy", false, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> bokChoyGiant = crop("bok_choy", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> leek = crop("leek", false, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> leekGiant = crop("leek", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> radish = crop("radish", false, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> radishGiant = crop("radish", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> spinach = crop("spinach", false, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> spinachGiant = crop("spinach", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> greenPepper = crop("green_pepper", false, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> greenPepperGiant = crop("green_pepper", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> yam = crop("yam", false, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> yamGiant = crop("yam", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> eggplant = crop("eggplant", false, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> eggplantGiant = crop("eggplant", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> tomato = crop("tomato", false, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> tomatoGiant = crop("tomato", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> corn = crop("corn", false, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> cornGiant = crop("corn", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> cucumber = crop("cucumber", false, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> cucumberGiant = crop("cucumber", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> pumpkin = crop("pumpkin", false, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> pumpkinGiant = crop("pumpkin", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> onion = crop("onion", false, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> onionGiant = crop("onion", true, Texture.N, 0);

    //Vanilla
    public static final RegistryEntrySupplier<Item> potatoGiant = crop("potato", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> carrotGiant = crop("carrot", true, Texture.N, 0);

    //Flowers
    public static final RegistryEntrySupplier<Item> toyherbSeeds = seed("toyherb", () -> ModBlocks.toyherb);
    public static final RegistryEntrySupplier<Item> moondropSeeds = seed("moondrop_flower", () -> ModBlocks.moondropFlower);
    public static final RegistryEntrySupplier<Item> pinkCatSeeds = seed("pink_cat", () -> ModBlocks.pinkCat);
    public static final RegistryEntrySupplier<Item> charmBlueSeeds = seed("charm_blue", () -> ModBlocks.charmBlue);
    public static final RegistryEntrySupplier<Item> lampGrassSeeds = seed("lamp_grass", () -> ModBlocks.lampGrass);
    public static final RegistryEntrySupplier<Item> cherryGrassSeeds = seed("cherry_grass", () -> ModBlocks.cherryGrass);
    public static final RegistryEntrySupplier<Item> pomPomGrassSeeds = seed("pom_pom_grass", () -> ModBlocks.pomPomGrass);
    public static final RegistryEntrySupplier<Item> autumnGrassSeeds = seed("autumn_grass", () -> ModBlocks.autumnGrass);
    public static final RegistryEntrySupplier<Item> noelGrassSeeds = seed("noel_grass", () -> ModBlocks.noelGrass);
    public static final RegistryEntrySupplier<Item> fireflowerSeeds = seed("fireflower", () -> ModBlocks.fireflower);
    public static final RegistryEntrySupplier<Item> fourLeafCloverSeeds = seed("four_leaf_clover", () -> ModBlocks.fourLeafClover);
    public static final RegistryEntrySupplier<Item> ironleafSeeds = seed("ironleaf", () -> ModBlocks.ironleaf);
    public static final RegistryEntrySupplier<Item> whiteCrystalSeeds = seed("white_crystal", () -> ModBlocks.whiteCrystal);
    public static final RegistryEntrySupplier<Item> redCrystalSeeds = seed("red_crystal", () -> ModBlocks.redCrystal);
    public static final RegistryEntrySupplier<Item> greenCrystalSeeds = seed("green_crystal", () -> ModBlocks.greenCrystal);
    public static final RegistryEntrySupplier<Item> blueCrystalSeeds = seed("blue_crystal", () -> ModBlocks.blueCrystal);
    public static final RegistryEntrySupplier<Item> emeryFlowerSeeds = seed("emery_flower", () -> ModBlocks.emeryFlower);

    public static final RegistryEntrySupplier<Item> toyherb = crop("toyherb", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> toyherbGiant = crop("toyherb", true, Texture.N, 2);
    public static final RegistryEntrySupplier<Item> moondropFlower = crop("moondrop_flower", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> moondropFlowerGiant = crop("moondrop_flower", true, Texture.N, 2);
    public static final RegistryEntrySupplier<Item> pinkCat = crop("pink_cat", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> pinkCatGiant = crop("pink_cat", true, Texture.N, 2);
    public static final RegistryEntrySupplier<Item> charmBlue = crop("charm_blue", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> charmBlueGiant = crop("charm_blue", true, Texture.N, 2);
    public static final RegistryEntrySupplier<Item> lampGrass = crop("lamp_grass", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> lampGrassGiant = crop("lamp_grass", true, Texture.N, 2);
    public static final RegistryEntrySupplier<Item> cherryGrass = crop("cherry_grass", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> cherryGrassGiant = crop("cherry_grass", true, Texture.N, 2);
    public static final RegistryEntrySupplier<Item> pomPomGrass = crop("pom_pom_grass", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> pomPomGrassGiant = crop("pom_pom_grass", true, Texture.N, 2);
    public static final RegistryEntrySupplier<Item> autumnGrass = crop("autumn_grass", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> autumnGrassGiant = crop("autumn_grass", true, Texture.N, 2);
    public static final RegistryEntrySupplier<Item> noelGrass = crop("noel_grass", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> noelGrassGiant = crop("noel_grass", true, Texture.N, 2);
    public static final RegistryEntrySupplier<Item> fireflower = crop("fireflower", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> fireflowerGiant = crop("fireflower", true, Texture.N, 2);
    public static final RegistryEntrySupplier<Item> fourLeafClover = crop("four_leaf_clover", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> fourLeafCloverGiant = crop("four_leaf_clover", true, Texture.N, 2);
    public static final RegistryEntrySupplier<Item> ironleaf = crop("ironleaf", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> ironleafGiant = crop("ironleaf", true, Texture.N, 2);
    public static final RegistryEntrySupplier<Item> whiteCrystal = crop("white_crystal", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> whiteCrystalGiant = crop("white_crystal", true, Texture.N, 2);
    public static final RegistryEntrySupplier<Item> redCrystal = crop("red_crystal", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> redCrystalGiant = crop("red_crystal", true, Texture.N, 2);
    public static final RegistryEntrySupplier<Item> greenCrystal = crop("green_crystal", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> greenCrystalGiant = crop("green_crystal", true, Texture.N, 2);
    public static final RegistryEntrySupplier<Item> blueCrystal = crop("blue_crystal", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> blueCrystalGiant = crop("blue_crystal", true, Texture.N, 2);
    public static final RegistryEntrySupplier<Item> emeryFlower = crop("emery_flower", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> emeryFlowerGiant = crop("emery_flower", true, Texture.N, 2);

    //Special seeds
    public static final RegistryEntrySupplier<Item> shieldSeeds = seed("shield", () -> ModBlocks.shieldCrop);
    public static final RegistryEntrySupplier<Item> swordSeeds = seed("sword", () -> ModBlocks.swordCrop);
    public static final RegistryEntrySupplier<Item> dungeonSeeds = seed("dungeon", () -> ModBlocks.dungeon);

    //Recovery and stuff
    public static final RegistryEntrySupplier<Item> roundoff = medicine("roundoff", false);
    public static final RegistryEntrySupplier<Item> paraGone = medicine("para_gone", false);
    public static final RegistryEntrySupplier<Item> coldMed = medicine("cold_medicine", false);
    public static final RegistryEntrySupplier<Item> antidote = medicine("antidote_potion", false);
    public static final RegistryEntrySupplier<Item> recoveryPotion = medicine("recovery_potion", true);
    public static final RegistryEntrySupplier<Item> healingPotion = medicine("healing_potion", true);
    public static final RegistryEntrySupplier<Item> mysteryPotion = medicine("mystery_potion", true);
    public static final RegistryEntrySupplier<Item> magicalPotion = medicine("magical_potion", true);
    public static final RegistryEntrySupplier<Item> invinciroid = drinkable("invinciroid");
    public static final RegistryEntrySupplier<Item> lovePotion = drinkable("love_potion");
    public static final RegistryEntrySupplier<Item> formuade = drinkable("formuade");
    public static final RegistryEntrySupplier<Item> objectX = ITEMS.register("object_x", () -> new ItemObjectX(new Item.Properties().food(foodProp).tab(RFCreativeTabs.medicine)));

    //Herbs
    public static final RegistryEntrySupplier<Item> elliLeaves = herb("elli_leaves", () -> ModBlocks.elliLeaves);
    public static final RegistryEntrySupplier<Item> witheredGrass = herb("withered_grass", () -> ModBlocks.witheredGrass);
    public static final RegistryEntrySupplier<Item> weeds = herb("weeds", () -> ModBlocks.weeds);
    public static final RegistryEntrySupplier<Item> whiteGrass = herb("white_grass", () -> ModBlocks.whiteGrass);
    public static final RegistryEntrySupplier<Item> indigoGrass = herb("indigo_grass", () -> ModBlocks.indigoGrass);
    public static final RegistryEntrySupplier<Item> purpleGrass = herb("purple_grass", () -> ModBlocks.purpleGrass);
    public static final RegistryEntrySupplier<Item> greenGrass = herb("green_grass", () -> ModBlocks.greenGrass);
    public static final RegistryEntrySupplier<Item> blueGrass = herb("blue_grass", () -> ModBlocks.blueGrass);
    public static final RegistryEntrySupplier<Item> yellowGrass = herb("yellow_grass", () -> ModBlocks.yellowGrass);
    public static final RegistryEntrySupplier<Item> redGrass = herb("red_grass", () -> ModBlocks.redGrass);
    public static final RegistryEntrySupplier<Item> orangeGrass = herb("orange_grass", () -> ModBlocks.orangeGrass);
    public static final RegistryEntrySupplier<Item> blackGrass = herb("black_grass", () -> ModBlocks.blackGrass);
    public static final RegistryEntrySupplier<Item> antidoteGrass = herb("antidote_grass", () -> ModBlocks.antidoteGrass);
    public static final RegistryEntrySupplier<Item> medicinalHerb = herb("medicinal_herb", () -> ModBlocks.medicinalHerb);
    public static final RegistryEntrySupplier<Item> bambooSprout = herb("bamboo_sprout", () -> ModBlocks.bambooSprout);
    public static final RegistryEntrySupplier<Item> mushroom = ITEMS.register("mushroom", () -> new ItemMushroom(new Item.Properties().food(lowFoodProp).tab(RFCreativeTabs.food)));
    public static final RegistryEntrySupplier<Item> monarchMushroom = ITEMS.register("monarch_mushroom", () -> new ItemMushroom(new Item.Properties().food(lowFoodProp).tab(RFCreativeTabs.food)));

    //Food
    public static final RegistryEntrySupplier<Item> riceFlour = food("rice_flour", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> curryPowder = food("curry_powder", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> oil = drinkable("oil", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> flour = food("flour", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> honey = food("honey", Texture.N, foodProp);
    public static final RegistryEntrySupplier<Item> yogurt = food("yogurt", Texture.N);
    public static final RegistryEntrySupplier<Item> cheese = food("cheese", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> mayonnaise = food("mayonnaise", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> eggL = food("egg_l", Texture.Y, lowFoodProp);
    public static final RegistryEntrySupplier<Item> eggM = food("egg_m", Texture.Y, lowFoodProp);
    public static final RegistryEntrySupplier<Item> eggS = food("egg_s", Texture.Y, lowFoodProp);
    public static final RegistryEntrySupplier<Item> milkL = drinkable("milk_l", Texture.Y, lowFoodProp);
    public static final RegistryEntrySupplier<Item> milkM = drinkable("milk_m", Texture.Y, lowFoodProp);
    public static final RegistryEntrySupplier<Item> milkS = drinkable("milk_s", Texture.Y, lowFoodProp);
    public static final RegistryEntrySupplier<Item> wine = drinkable("wine", Texture.N, foodProp);
    public static final RegistryEntrySupplier<Item> chocolate = food("chocolate", Texture.N);
    public static final RegistryEntrySupplier<Item> rice = food("rice", Texture.N, foodProp);
    public static final RegistryEntrySupplier<Item> turnipHeaven = food("turnip_heaven", Texture.N);
    public static final RegistryEntrySupplier<Item> pickleMix = food("pickle_mix", Texture.N);
    public static final RegistryEntrySupplier<Item> salmonOnigiri = food("salmon_onigiri", Texture.N);
    public static final RegistryEntrySupplier<Item> bread = food("bread", Texture.N);
    public static final RegistryEntrySupplier<Item> onigiri = food("onigiri", Texture.Y);
    public static final RegistryEntrySupplier<Item> relaxTeaLeaves = food("relax_tea_leaves", Texture.N);
    public static final RegistryEntrySupplier<Item> iceCream = food("ice_cream", Texture.N);
    public static final RegistryEntrySupplier<Item> raisinBread = food("raisin_bread", Texture.N);
    public static final RegistryEntrySupplier<Item> bambooRice = food("bamboo_rice", Texture.N);
    public static final RegistryEntrySupplier<Item> pickles = food("pickles", Texture.N);
    public static final RegistryEntrySupplier<Item> pickledTurnip = food("pickled_turnip", Texture.Y);
    public static final RegistryEntrySupplier<Item> fruitSandwich = food("fruit_sandwich", Texture.N);
    public static final RegistryEntrySupplier<Item> sandwich = food("sandwich", Texture.N);
    public static final RegistryEntrySupplier<Item> salad = food("salad", Texture.N);
    public static final RegistryEntrySupplier<Item> dumplings = food("dumplings", Texture.N);
    public static final RegistryEntrySupplier<Item> pumpkinFlan = food("pumpkin_flan", Texture.N);
    public static final RegistryEntrySupplier<Item> flan = food("flan", Texture.Y);
    public static final RegistryEntrySupplier<Item> chocolateSponge = food("chocolate_sponge", Texture.N);
    public static final RegistryEntrySupplier<Item> poundCake = food("pound_cake", Texture.N);
    public static final RegistryEntrySupplier<Item> steamedGyoza = food("steamed_gyoza", Texture.N);
    public static final RegistryEntrySupplier<Item> curryManju = food("curry_manju", Texture.N);
    public static final RegistryEntrySupplier<Item> chineseManju = food("chinese_manju", Texture.N);
    public static final RegistryEntrySupplier<Item> meatDumpling = food("meat_dumpling", Texture.N);
    public static final RegistryEntrySupplier<Item> cheeseBread = food("cheese_bread", Texture.N);
    public static final RegistryEntrySupplier<Item> steamedBread = food("steamed_bread", Texture.N);
    public static final RegistryEntrySupplier<Item> hotJuice = drinkable("hot_juice", Texture.N, highFoodProp);
    public static final RegistryEntrySupplier<Item> preludetoLove = drinkable("prelude_to_love", Texture.N, highFoodProp);
    public static final RegistryEntrySupplier<Item> goldJuice = drinkable("gold_juice", Texture.N, highFoodProp);
    public static final RegistryEntrySupplier<Item> butter = food("butter", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> ketchup = drinkable("ketchup", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> mixedSmoothie = drinkable("mixed_smoothie", Texture.N, highFoodProp);
    public static final RegistryEntrySupplier<Item> mixedJuice = drinkable("mixed_juice", Texture.N, highFoodProp);
    public static final RegistryEntrySupplier<Item> veggieSmoothie = drinkable("veggie_smoothie", Texture.N, highFoodProp);
    public static final RegistryEntrySupplier<Item> vegetableJuice = drinkable("vegetable_juice", Texture.N, highFoodProp);
    public static final RegistryEntrySupplier<Item> fruitSmoothie = drinkable("fruit_smoothie", Texture.N, highFoodProp);
    public static final RegistryEntrySupplier<Item> fruitJuice = drinkable("fruit_juice", Texture.N, highFoodProp);
    public static final RegistryEntrySupplier<Item> strawberryMilk = drinkable("strawberry_milk", Texture.N, highFoodProp);
    public static final RegistryEntrySupplier<Item> appleJuice = drinkable("apple_juice", Texture.N, highFoodProp);
    public static final RegistryEntrySupplier<Item> orangeJuice = drinkable("orange_juice", Texture.N, highFoodProp);
    public static final RegistryEntrySupplier<Item> grapeJuice = drinkable("grape_juice", Texture.N, highFoodProp);
    public static final RegistryEntrySupplier<Item> tomatoJuice = drinkable("tomato_juice", Texture.N, highFoodProp);
    public static final RegistryEntrySupplier<Item> pineappleJuice = drinkable("pineapple_juice", Texture.N, highFoodProp);
    public static final RegistryEntrySupplier<Item> applePie = food("apple_pie", Texture.N);
    public static final RegistryEntrySupplier<Item> cheesecake = food("cheesecake", Texture.N);
    public static final RegistryEntrySupplier<Item> chocolateCake = food("chocolate_cake", Texture.N);
    public static final RegistryEntrySupplier<Item> cake = food("cake", Texture.N);
    public static final RegistryEntrySupplier<Item> chocoCookie = food("choco_cookie", Texture.N);
    public static final RegistryEntrySupplier<Item> cookie = food("cookie", Texture.N);
    public static final RegistryEntrySupplier<Item> yamoftheAges = food("yam_of_the_ages", Texture.N);
    public static final RegistryEntrySupplier<Item> seafoodGratin = food("seafood_gratin", Texture.N);
    public static final RegistryEntrySupplier<Item> gratin = food("gratin", Texture.N);
    public static final RegistryEntrySupplier<Item> seafoodDoria = food("seafood_doria", Texture.N);
    public static final RegistryEntrySupplier<Item> doria = food("doria", Texture.N);
    public static final RegistryEntrySupplier<Item> seafoodPizza = food("seafood_pizza", Texture.N);
    public static final RegistryEntrySupplier<Item> pizza = food("pizza", Texture.N);
    public static final RegistryEntrySupplier<Item> butterRoll = food("butter_roll", Texture.N);
    public static final RegistryEntrySupplier<Item> jamRoll = food("jam_roll", Texture.N);
    public static final RegistryEntrySupplier<Item> toast = food("toast", Texture.N);
    public static final RegistryEntrySupplier<Item> sweetPotato = food("sweet_potato", Texture.N);
    public static final RegistryEntrySupplier<Item> bakedOnigiri = food("baked_onigiri", Texture.N);
    public static final RegistryEntrySupplier<Item> cornontheCob = food("corn_on_the_cob", Texture.N);
    public static final RegistryEntrySupplier<Item> rockfishStew = food("rockfish_stew", Texture.N);
    public static final RegistryEntrySupplier<Item> unionStew = food("union_stew", Texture.N);
    public static final RegistryEntrySupplier<Item> grilledMiso = food("grilled_miso", Texture.N);
    public static final RegistryEntrySupplier<Item> relaxTea = food("relax_tea", Texture.N);
    public static final RegistryEntrySupplier<Item> royalCurry = food("royal_curry", Texture.N);
    public static final RegistryEntrySupplier<Item> ultimateCurry = food("ultimate_curry", Texture.N);
    public static final RegistryEntrySupplier<Item> curryRice = food("curry_rice", Texture.N);
    public static final RegistryEntrySupplier<Item> eggBowl = food("egg_bowl", Texture.N);
    public static final RegistryEntrySupplier<Item> tempuraBowl = food("tempura_bowl", Texture.N);
    public static final RegistryEntrySupplier<Item> milkPorridge = food("milk_porridge", Texture.N);
    public static final RegistryEntrySupplier<Item> ricePorridge = food("rice_porridge", Texture.N);
    public static final RegistryEntrySupplier<Item> tempuraUdon = food("tempura_udon", Texture.N);
    public static final RegistryEntrySupplier<Item> curryUdon = food("curry_udon", Texture.N);
    public static final RegistryEntrySupplier<Item> udon = food("udon", Texture.N);
    public static final RegistryEntrySupplier<Item> cheeseFondue = food("cheese_fondue", Texture.N);
    public static final RegistryEntrySupplier<Item> marmalade = food("marmalade", Texture.N);
    public static final RegistryEntrySupplier<Item> grapeJam = food("grape_jam", Texture.N);
    public static final RegistryEntrySupplier<Item> appleJam = food("apple_jam", Texture.N);
    public static final RegistryEntrySupplier<Item> strawberryJam = food("strawberry_jam", Texture.N);
    public static final RegistryEntrySupplier<Item> boiledGyoza = food("boiled_gyoza", Texture.N);
    public static final RegistryEntrySupplier<Item> glazedYam = food("glazed_yam", Texture.N);
    public static final RegistryEntrySupplier<Item> boiledEgg = food("boiled_egg", Texture.N);
    public static final RegistryEntrySupplier<Item> boiledSpinach = food("boiled_spinach", Texture.N);
    public static final RegistryEntrySupplier<Item> boiledPumpkin = food("boiled_pumpkin", Texture.N);
    public static final RegistryEntrySupplier<Item> grapeLiqueur = drinkable("grape_liqueur", Texture.N, highFoodProp);
    public static final RegistryEntrySupplier<Item> hotChocolate = drinkable("hot_chocolate", Texture.Y, highFoodProp);
    public static final RegistryEntrySupplier<Item> hotMilk = drinkable("hot_milk", Texture.Y, highFoodProp);
    public static final RegistryEntrySupplier<Item> grilledSandFlounder = food("grilled_sand_flounder", Texture.N);
    public static final RegistryEntrySupplier<Item> grilledShrimp = food("grilled_shrimp", Texture.N);
    public static final RegistryEntrySupplier<Item> grilledLobster = food("grilled_lobster", Texture.N);
    public static final RegistryEntrySupplier<Item> grilledBlowfish = food("grilled_blowfish", Texture.N);
    public static final RegistryEntrySupplier<Item> grilledLampSquid = food("grilled_lamp_squid", Texture.N);
    public static final RegistryEntrySupplier<Item> grilledSunsquid = food("grilled_sunsquid", Texture.N);
    public static final RegistryEntrySupplier<Item> grilledSquid = food("grilled_squid", Texture.N);
    public static final RegistryEntrySupplier<Item> grilledFallFlounder = food("grilled_fall_flounder", Texture.N);
    public static final RegistryEntrySupplier<Item> grilledTurbot = food("grilled_turbot", Texture.N);
    public static final RegistryEntrySupplier<Item> grilledFlounder = food("grilled_flounder", Texture.N);
    public static final RegistryEntrySupplier<Item> saltedPike = food("salted_pike", Texture.N);
    public static final RegistryEntrySupplier<Item> grilledNeedlefish = food("grilled_needlefish", Texture.N);
    public static final RegistryEntrySupplier<Item> driedSardines = food("dried_sardines", Texture.N);
    public static final RegistryEntrySupplier<Item> tunaTeriyaki = food("tuna_teriyaki", Texture.N);
    public static final RegistryEntrySupplier<Item> saltedPondSmelt = food("salted_pond_smelt", Texture.N);
    public static final RegistryEntrySupplier<Item> grilledYellowtail = food("grilled_yellowtail", Texture.N);
    public static final RegistryEntrySupplier<Item> grilledMackerel = food("grilled_mackerel", Texture.N);
    public static final RegistryEntrySupplier<Item> grilledSkipjack = food("grilled_skipjack", Texture.N);
    public static final RegistryEntrySupplier<Item> grilledLoverSnapper = food("grilled_lover_snapper", Texture.N);
    public static final RegistryEntrySupplier<Item> grilledGlitterSnapper = food("grilled_glitter_snapper", Texture.N);
    public static final RegistryEntrySupplier<Item> grilledGirella = food("grilled_girella", Texture.N);
    public static final RegistryEntrySupplier<Item> grilledSnapper = food("grilled_snapper", Texture.N);
    public static final RegistryEntrySupplier<Item> grilledGibelio = food("grilled_gibelio", Texture.N);
    public static final RegistryEntrySupplier<Item> grilledCrucianCarp = food("grilled_crucian_carp", Texture.N);
    public static final RegistryEntrySupplier<Item> saltedTaimen = food("salted_taimen", Texture.N);
    public static final RegistryEntrySupplier<Item> saltedSalmon = food("salted_salmon", Texture.N);
    public static final RegistryEntrySupplier<Item> saltedChub = food("salted_chub", Texture.N);
    public static final RegistryEntrySupplier<Item> saltedCherrySalmon = food("salted_cherry_salmon", Texture.N);
    public static final RegistryEntrySupplier<Item> saltedRainbowTrout = food("salted_rainbow_trout", Texture.N);
    public static final RegistryEntrySupplier<Item> saltedChar = food("salted_char", Texture.N);
    public static final RegistryEntrySupplier<Item> saltedMasuTrout = food("salted_masu_trout", Texture.N);
    public static final RegistryEntrySupplier<Item> dryCurry = food("dry_curry", Texture.N);
    public static final RegistryEntrySupplier<Item> risotto = food("risotto", Texture.N);
    public static final RegistryEntrySupplier<Item> gyoza = food("gyoza", Texture.N);
    public static final RegistryEntrySupplier<Item> pancakes = food("pancakes", Texture.N);
    public static final RegistryEntrySupplier<Item> tempura = food("tempura", Texture.N);
    public static final RegistryEntrySupplier<Item> friedUdon = food("fried_udon", Texture.N);
    public static final RegistryEntrySupplier<Item> donut = food("donut", Texture.N);
    public static final RegistryEntrySupplier<Item> frenchToast = food("french_toast", Texture.N);
    public static final RegistryEntrySupplier<Item> curryBread = food("curry_bread", Texture.N);
    public static final RegistryEntrySupplier<Item> bakedApple = food("baked_apple", Texture.Y);
    public static final RegistryEntrySupplier<Item> omeletRice = food("omelet_rice", Texture.N);
    public static final RegistryEntrySupplier<Item> omelet = food("omelet", Texture.N);
    public static final RegistryEntrySupplier<Item> friedEggs = food("fried_eggs", Texture.N);
    public static final RegistryEntrySupplier<Item> misoEggplant = food("miso_eggplant", Texture.N);
    public static final RegistryEntrySupplier<Item> cornCereal = food("corn_cereal", Texture.N);
    public static final RegistryEntrySupplier<Item> popcorn = food("popcorn", Texture.N);
    public static final RegistryEntrySupplier<Item> croquettes = food("croquettes", Texture.N);
    public static final RegistryEntrySupplier<Item> frenchFries = food("french_fries", Texture.N);
    public static final RegistryEntrySupplier<Item> cabbageCakes = food("cabbage_cakes", Texture.N);
    public static final RegistryEntrySupplier<Item> friedRice = food("fried_rice", Texture.N);
    public static final RegistryEntrySupplier<Item> friedVeggies = food("fried_veggies", Texture.Y);
    public static final RegistryEntrySupplier<Item> shrimpSashimi = food("shrimp_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> lobsterSashimi = food("lobster_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> blowfishSashimi = food("blowfish_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> lampSquidSashimi = food("lamp_squid_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> sunsquidSashimi = food("sunsquid_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> squidSashimi = food("squid_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> fallSashimi = food("fall_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> turbotSashimi = food("turbot_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> flounderSashimi = food("flounder_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> pikeSashimi = food("pike_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> needlefishSashimi = food("needlefish_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> sardineSashimi = food("sardine_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> tunaSashimi = food("tuna_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> yellowtailSashimi = food("yellowtail_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> skipjackSashimi = food("skipjack_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> girellaSashimi = food("girella_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> loverSashimi = food("lover_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> glitterSashimi = food("glitter_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> snapperSashimi = food("snapper_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> taimenSashimi = food("taimen_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> cherrySashimi = food("cherry_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> salmonSashimi = food("salmon_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> rainbowSashimi = food("rainbow_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> charSashimi = food("char_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> troutSashimi = food("trout_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> disastrousDish = food("disastrous_dish", Texture.Y, lowFoodProp);
    public static final RegistryEntrySupplier<Item> failedDish = food("failed_dish", Texture.Y, lowFoodProp);
    public static final RegistryEntrySupplier<Item> mixedHerbs = food("mixed_herbs", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> sourDrop = food("sour_drop", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> sweetPowder = food("sweet_powder", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> heavySpice = food("heavy_spice", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> orange = food("orange", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> grapes = food("grapes", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> mealyApple = food("mealy_apple", Texture.N, lowFoodProp);

    public static final RegistryEntrySupplier<Item> forgingBread = ITEMS.register("forging_bread", () -> new ItemRecipeBread(EnumCrafting.FORGE, new Item.Properties().tab(RFCreativeTabs.food).stacksTo(16)));
    public static final RegistryEntrySupplier<Item> armorBread = ITEMS.register("armory_bread", () -> new ItemRecipeBread(EnumCrafting.ARMOR, new Item.Properties().tab(RFCreativeTabs.food).stacksTo(16)));
    public static final RegistryEntrySupplier<Item> chemistryBread = ITEMS.register("chemistry_bread", () -> new ItemRecipeBread(EnumCrafting.CHEM, new Item.Properties().tab(RFCreativeTabs.food).stacksTo(16)));
    public static final RegistryEntrySupplier<Item> cookingBread = ITEMS.register("cooking_bread", () -> new ItemRecipeBread(EnumCrafting.COOKING, new Item.Properties().tab(RFCreativeTabs.food).stacksTo(16)));

    public static final RegistryEntrySupplier<Item> shippingBin = blockItem("shipping_bin", () -> ModBlocks.shipping);
    public static final RegistryEntrySupplier<Item> requestBoard = noTexblockItem("quest_box", () -> ModBlocks.board);
    public static final RegistryEntrySupplier<Item> spawner = blockItem("boss_spawner", () -> ModBlocks.bossSpawner, RFCreativeTabs.monsters);
    public static final RegistryEntrySupplier<Item> farmland = blockItem("farmland", () -> ModBlocks.farmland, null);
    public static final RegistryEntrySupplier<Item> cashRegister = blockItem("cash_register", () -> ModBlocks.cashRegister);
    public static final RegistryEntrySupplier<Item> monsterBarn = blockItem("monster_barn", () -> ModBlocks.monsterBarn);
    public static final RegistryEntrySupplier<Item> questBoard = blockItem("quest_board", () -> ModBlocks.questBoard);

    public static RegistryEntrySupplier<Item> hoe(EnumToolTier tier) {
        RegistryEntrySupplier<Item> sup = ITEMS.register("hoe_" + tier.getName(), () -> new ItemToolHoe(tier, new Item.Properties().tab(RFCreativeTabs.weaponToolTab)));
        DATAGENTAGS.computeIfAbsent(ModTags.HOES, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> wateringCan(EnumToolTier tier) {
        RegistryEntrySupplier<Item> sup = ITEMS.register("watering_can_" + tier.getName(), () -> new ItemToolWateringCan(tier, new Item.Properties().tab(RFCreativeTabs.weaponToolTab)));
        DATAGENTAGS.computeIfAbsent(ModTags.WATERINGCANS, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> sickle(EnumToolTier tier) {
        RegistryEntrySupplier<Item> sup = ITEMS.register("sickle_" + tier.getName(), () -> new ItemToolSickle(tier, new Item.Properties().tab(RFCreativeTabs.weaponToolTab)));
        DATAGENTAGS.computeIfAbsent(ModTags.SICKLES, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> hammerTool(EnumToolTier tier) {
        RegistryEntrySupplier<Item> sup = ITEMS.register("hammer_" + tier.getName(), () -> new ItemToolHammer(tier, new Item.Properties().tab(RFCreativeTabs.weaponToolTab)));
        DATAGENTAGS.computeIfAbsent(ModTags.HAMMER_TOOLS, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> axeTool(EnumToolTier tier) {
        RegistryEntrySupplier<Item> sup = ITEMS.register("axe_" + tier.getName(), () -> new ItemToolAxe(tier, new Item.Properties().tab(RFCreativeTabs.weaponToolTab)));
        DATAGENTAGS.computeIfAbsent(ModTags.AXE_TOOLS, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> fishingRod(EnumToolTier tier) {
        RegistryEntrySupplier<Item> sup = ITEMS.register("fishing_rod_" + tier.getName(), () -> new ItemToolFishingRod(tier, new Item.Properties().stacksTo(1).tab(RFCreativeTabs.weaponToolTab)));
        DATAGENTAGS.computeIfAbsent(ModTags.FISHING_RODS, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> shortSword(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemShortSwordBase(new Item.Properties()));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemShortSwordBase(new Item.Properties().tab(RFCreativeTabs.weaponToolTab)));
        DATAGENTAGS.computeIfAbsent(ModTags.SHORTSWORDS, t -> new ArrayList<>()).add(sup);
        DATAGENTAGS.computeIfAbsent(ModTags.CHEST_T3, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> longSword(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemLongSwordBase(new Item.Properties()));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemLongSwordBase(new Item.Properties().tab(RFCreativeTabs.weaponToolTab)));
        DATAGENTAGS.computeIfAbsent(ModTags.LONGSWORDS, t -> new ArrayList<>()).add(sup);
        DATAGENTAGS.computeIfAbsent(ModTags.CHEST_T3, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> spear(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemSpearBase(new Item.Properties()));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemSpearBase(new Item.Properties().tab(RFCreativeTabs.weaponToolTab)));
        DATAGENTAGS.computeIfAbsent(ModTags.SPEARS, t -> new ArrayList<>()).add(sup);
        DATAGENTAGS.computeIfAbsent(ModTags.CHEST_T3, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> axe(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemAxeBase(new Item.Properties()));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemAxeBase(new Item.Properties().tab(RFCreativeTabs.weaponToolTab)));
        DATAGENTAGS.computeIfAbsent(ModTags.AXES, t -> new ArrayList<>()).add(sup);
        DATAGENTAGS.computeIfAbsent(ModTags.CHEST_T3, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> hammer(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemHammerBase(new Item.Properties()));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemHammerBase(new Item.Properties().tab(RFCreativeTabs.weaponToolTab)));
        DATAGENTAGS.computeIfAbsent(ModTags.HAMMERS, t -> new ArrayList<>()).add(sup);
        DATAGENTAGS.computeIfAbsent(ModTags.CHEST_T3, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> dualBlade(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemDualBladeBase(new Item.Properties()));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemDualBladeBase(new Item.Properties().tab(RFCreativeTabs.weaponToolTab)));
        DATAGENTAGS.computeIfAbsent(ModTags.DUALBLADES, t -> new ArrayList<>()).add(sup);
        DATAGENTAGS.computeIfAbsent(ModTags.CHEST_T3, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> gloves(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemGloveBase(new Item.Properties()));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemGloveBase(new Item.Properties().tab(RFCreativeTabs.weaponToolTab)));
        DATAGENTAGS.computeIfAbsent(ModTags.FISTS, t -> new ArrayList<>()).add(sup);
        DATAGENTAGS.computeIfAbsent(ModTags.CHEST_T3, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> staff(String name, EnumElement starterElement, int amount, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> Platform.INSTANCE.staff(starterElement, amount, new Item.Properties().stacksTo(1)));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> Platform.INSTANCE.staff(starterElement, amount, new Item.Properties().stacksTo(1).tab(RFCreativeTabs.weaponToolTab)));
        DATAGENTAGS.computeIfAbsent(ModTags.STAFFS, t -> new ArrayList<>()).add(sup);
        DATAGENTAGS.computeIfAbsent(ModTags.CHEST_T3, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> equipment(EquipmentSlot slot, String name, Texture texture) {
        return equipment(slot, name, texture, false);
    }

    public static RegistryEntrySupplier<Item> equipment(EquipmentSlot slot, String name, Texture texture, boolean useItemTexture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> Platform.INSTANCE.armor(slot, new Item.Properties(), new ResourceLocation(RuneCraftory.MODID, name), useItemTexture));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> Platform.INSTANCE.armor(slot, new Item.Properties().tab(RFCreativeTabs.equipment), new ResourceLocation(RuneCraftory.MODID, name), useItemTexture));
        DATAGENTAGS.computeIfAbsent(ModTags.CHEST_T3, t -> new ArrayList<>()).add(sup);
        switch (slot) {
            case FEET -> DATAGENTAGS.computeIfAbsent(ModTags.BOOTS, t -> new ArrayList<>()).add(sup);
            case LEGS -> DATAGENTAGS.computeIfAbsent(ModTags.ACCESSORIES, t -> new ArrayList<>()).add(sup);
            case CHEST -> DATAGENTAGS.computeIfAbsent(ModTags.CHESTPLATE, t -> new ArrayList<>()).add(sup);
            case HEAD -> DATAGENTAGS.computeIfAbsent(ModTags.HELMET, t -> new ArrayList<>()).add(sup);
        }
        return sup;
    }

    public static RegistryEntrySupplier<Item> shield(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemStatShield(new Item.Properties().stacksTo(1)));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemStatShield(new Item.Properties().stacksTo(1).tab(RFCreativeTabs.equipment)));
        DATAGENTAGS.computeIfAbsent(ModTags.CHEST_T3, t -> new ArrayList<>()).add(sup);
        DATAGENTAGS.computeIfAbsent(ModTags.SHIELDS, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> blockItem(String name, Supplier<Supplier<Block>> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get().get(), new Item.Properties().tab(RFCreativeTabs.blocks)));
    }

    public static RegistryEntrySupplier<Item> noTexblockItem(String name, Supplier<Supplier<Block>> block) {
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new BlockItem(block.get().get(), new Item.Properties()));
        NOTEX.add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> blockItem(String name, Supplier<Supplier<Block>> block, CreativeModeTab group) {
        return ITEMS.register(name, () -> new BlockItem(block.get().get(), new Item.Properties().tab(group)));
    }

    public static RegistryEntrySupplier<Item> mineral(EnumMineralTier tier) {
        Supplier<Block> block = () -> ModBlocks.mineralMap.get(tier).get();
        return ITEMS.register("ore_" + tier.getSerializedName(), () -> new BlockItem(block.get(), new Item.Properties().tab(RFCreativeTabs.blocks)));
    }

    public static RegistryEntrySupplier<Item> brokenMineral(EnumMineralTier tier) {
        Supplier<Block> block = () -> ModBlocks.brokenMineralMap.get(tier).get();
        return ITEMS.register("ore_broken_" + tier.getSerializedName(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static RegistryEntrySupplier<Item> mat(String name, Texture texture) {
        return mat(name, Rarity.COMMON, texture);
    }

    public static RegistryEntrySupplier<Item> mat(String name, Rarity rarity, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new Item(new Item.Properties().rarity(rarity)));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new Item(new Item.Properties().rarity(rarity).tab(RFCreativeTabs.upgradeItems)));
        if (rarity == Rarity.COMMON)
            DATAGENTAGS.computeIfAbsent(ModTags.CHEST_T1, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> medicine(String name, boolean affectStats) {
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemMedicine(affectStats, new Item.Properties().food(foodProp).stacksTo(16).tab(RFCreativeTabs.medicine)));
        DATAGENTAGS.computeIfAbsent(ModTags.CHEST_T2, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> drinkable(String name) {
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new Item(new Item.Properties().food(foodProp).stacksTo(16).tab(RFCreativeTabs.medicine)) {
            @Override
            public UseAnim getUseAnimation(ItemStack stack) {
                return UseAnim.DRINK;
            }
        });
        DATAGENTAGS.computeIfAbsent(ModTags.CHEST_T2, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> spell(Supplier<Supplier<Spell>> sup, String name) {
        RegistryEntrySupplier<Item> ret = ITEMS.register(name, () -> new ItemSpell(sup.get(), new Item.Properties().stacksTo(1).tab(RFCreativeTabs.cast)));
        DATAGENTAGS.computeIfAbsent(ModTags.CHEST_T2, t -> new ArrayList<>()).add(ret);
        DATAGENTAGS.computeIfAbsent(ModTags.SPELLS, t -> new ArrayList<>()).add(ret);
        return ret;
    }

    public static RegistryEntrySupplier<Item> fish(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new Item(new Item.Properties()));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new Item(new Item.Properties().tab(RFCreativeTabs.food)));
        DATAGENTAGS.computeIfAbsent(ModTags.CHEST_T1, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> seed(String name, Supplier<Supplier<Block>> block) {
        RegistryEntrySupplier<Item> sup = ITEMS.register("seed_" + name, () -> new ItemNameBlockItem(block.get().get(), new Item.Properties().tab(RFCreativeTabs.crops)));
        SEEDS.add(sup);
        return sup;
    }

    /**
     * @param type 0 for veggetables, 1 for fruits, 2 for flowers
     */
    public static RegistryEntrySupplier<Item> crop(String name, boolean giant, Texture texture, int type) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup;
            if (giant)
                sup = ITEMS.register("crop_" + name + "_giant", () -> new ItemGiantCrops(new Item.Properties().food(foodProp)));
            else
                sup = ITEMS.register("crop_" + name, () -> new Item(new Item.Properties().food(foodProp)));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup;
        if (giant)
            sup = ITEMS.register("crop_" + name + "_giant", () -> new ItemGiantCrops(new Item.Properties().food(foodProp).tab(RFCreativeTabs.crops)));
        else
            sup = ITEMS.register("crop_" + name, () -> new Item(new Item.Properties().food(foodProp).tab(RFCreativeTabs.crops)));
        DATAGENTAGS.computeIfAbsent(ModTags.CHEST_T1, t -> new ArrayList<>()).add(sup);
        switch (type) {
            case 0 -> VEGGIES.add(sup);
            case 1 -> FRUITS.add(sup);
            case 2 -> FLOWERS.add(sup);
        }
        return sup;
    }

    public static RegistryEntrySupplier<Item> herb(String name, Supplier<Supplier<Block>> block) {
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new BlockItem(block.get().get(), new Item.Properties().food(lowFoodProp).tab(RFCreativeTabs.medicine)));
        DATAGENTAGS.computeIfAbsent(ModTags.CHEST_T1, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> food(String name, Texture texture) {
        return food(name, texture, highFoodProp);
    }

    public static RegistryEntrySupplier<Item> food(String name, Texture texture, FoodProperties foodProp) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new Item(new Item.Properties().food(foodProp)));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new Item(new Item.Properties().food(foodProp).tab(RFCreativeTabs.food)));
        DATAGENTAGS.computeIfAbsent(ModTags.CHEST_T2, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> drinkable(String name, Texture texture, FoodProperties foodProp) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new Item(new Item.Properties().food(foodProp)) {
                @Override
                public UseAnim getUseAnimation(ItemStack stack) {
                    return UseAnim.DRINK;
                }
            });
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new Item(new Item.Properties().food(foodProp).tab(RFCreativeTabs.food)) {
            @Override
            public UseAnim getUseAnimation(ItemStack stack) {
                return UseAnim.DRINK;
            }
        });
        DATAGENTAGS.computeIfAbsent(ModTags.CHEST_T2, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static List<RegistryEntrySupplier<Item>> ribbons() {
        return List.of(ModItems.blueRibbon, ModItems.greenRibbon, ModItems.purpleRibbon, ModItems.blackRibbon,
                ModItems.yellowRibbon, ModItems.redRibbon, ModItems.orangeRibbon, ModItems.whiteRibbon,
                ModItems.indigoRibbon);
    }

    //Here till all items have a texture
    enum Texture {
        Y,
        N
    }
}
