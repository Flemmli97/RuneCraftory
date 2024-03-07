package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.enums.EnumMineralTier;
import io.github.flemmli97.runecraftory.api.enums.EnumToolTier;
import io.github.flemmli97.runecraftory.common.RFCreativeTabs;
import io.github.flemmli97.runecraftory.common.items.CraftingBlockItem;
import io.github.flemmli97.runecraftory.common.items.QuestBoardItem;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemGiantCrops;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemMedicine;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemMushroom;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemObjectX;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemRecipeBread;
import io.github.flemmli97.runecraftory.common.items.creative.ItemDebug;
import io.github.flemmli97.runecraftory.common.items.creative.ItemLevelUp;
import io.github.flemmli97.runecraftory.common.items.creative.ItemProp;
import io.github.flemmli97.runecraftory.common.items.creative.ItemSkillUp;
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
import io.github.flemmli97.runecraftory.common.items.weapons.ItemHoldSpell;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemLongSwordBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemShortSwordBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpearBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpell;
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

    //Here till all items have textures
    public static final List<RegistryEntrySupplier<Item>> NOTEX = new ArrayList<>();
    //Those collections are for datagen
    public static final Map<TagKey<Item>, List<RegistryEntrySupplier<Item>>> DATAGENTAGS = new HashMap<>();
    public static final List<RegistryEntrySupplier<Item>> SEEDS = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Item>> VEGGIES = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Item>> FRUITS = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Item>> FLOWERS = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Item>> CROPS = new ArrayList<>();

    public static final List<RegistryEntrySupplier<Item>> TIER_1_CHEST = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Item>> TIER_2_CHEST = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Item>> TIER_3_CHEST = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Item>> TIER_4_CHEST = new ArrayList<>();

    private static final FoodProperties lowFoodProp = new FoodProperties.Builder().nutrition(1).saturationMod(0.5f).alwaysEat().build();
    private static final FoodProperties FOOD_PROP = new FoodProperties.Builder().nutrition(2).saturationMod(0.5f).alwaysEat().build();
    private static final FoodProperties HIGH_FOOD_PROP = new FoodProperties.Builder().nutrition(6).saturationMod(0.75f).alwaysEat().build();

    public static final RegistryEntrySupplier<Item> HOE_SCRAP = hoe(EnumToolTier.SCRAP);
    public static final RegistryEntrySupplier<Item> HOE_IRON = hoe(EnumToolTier.IRON);
    public static final RegistryEntrySupplier<Item> HOE_SILVER = hoe(EnumToolTier.SILVER);
    public static final RegistryEntrySupplier<Item> HOE_GOLD = hoe(EnumToolTier.GOLD);
    public static final RegistryEntrySupplier<Item> HOE_PLATINUM = hoe(EnumToolTier.PLATINUM);
    public static final RegistryEntrySupplier<Item> WATERING_CAN_SCRAP = wateringCan(EnumToolTier.SCRAP);
    public static final RegistryEntrySupplier<Item> WATERING_CAN_IRON = wateringCan(EnumToolTier.IRON);
    public static final RegistryEntrySupplier<Item> WATERING_CAN_SILVER = wateringCan(EnumToolTier.SILVER);
    public static final RegistryEntrySupplier<Item> WATERING_CAN_GOLD = wateringCan(EnumToolTier.GOLD);
    public static final RegistryEntrySupplier<Item> WATERING_CAN_PLATINUM = wateringCan(EnumToolTier.PLATINUM);
    public static final RegistryEntrySupplier<Item> SICKLE_SCRAP = sickle(EnumToolTier.SCRAP);
    public static final RegistryEntrySupplier<Item> SICKLE_IRON = sickle(EnumToolTier.IRON);
    public static final RegistryEntrySupplier<Item> SICKLE_SILVER = sickle(EnumToolTier.SILVER);
    public static final RegistryEntrySupplier<Item> SICKLE_GOLD = sickle(EnumToolTier.GOLD);
    public static final RegistryEntrySupplier<Item> SICKLE_PLATINUM = sickle(EnumToolTier.PLATINUM);
    public static final RegistryEntrySupplier<Item> HAMMER_SCRAP = hammerTool(EnumToolTier.SCRAP);
    public static final RegistryEntrySupplier<Item> HAMMER_IRON = hammerTool(EnumToolTier.IRON);
    public static final RegistryEntrySupplier<Item> HAMMER_SILVER = hammerTool(EnumToolTier.SILVER);
    public static final RegistryEntrySupplier<Item> HAMMER_GOLD = hammerTool(EnumToolTier.GOLD);
    public static final RegistryEntrySupplier<Item> HAMMER_PLATINUM = hammerTool(EnumToolTier.PLATINUM);
    public static final RegistryEntrySupplier<Item> AXE_SCRAP = axeTool(EnumToolTier.SCRAP);
    public static final RegistryEntrySupplier<Item> AXE_IRON = axeTool(EnumToolTier.IRON);
    public static final RegistryEntrySupplier<Item> AXE_SILVER = axeTool(EnumToolTier.SILVER);
    public static final RegistryEntrySupplier<Item> AXE_GOLD = axeTool(EnumToolTier.GOLD);
    public static final RegistryEntrySupplier<Item> AXE_PLATINUM = axeTool(EnumToolTier.PLATINUM);
    public static final RegistryEntrySupplier<Item> FISHING_ROD_SCRAP = fishingRod(EnumToolTier.SCRAP);
    public static final RegistryEntrySupplier<Item> FISHING_ROD_IRON = fishingRod(EnumToolTier.IRON);
    public static final RegistryEntrySupplier<Item> FISHING_ROD_SILVER = fishingRod(EnumToolTier.SILVER);
    public static final RegistryEntrySupplier<Item> FISHING_ROD_GOLD = fishingRod(EnumToolTier.GOLD);
    public static final RegistryEntrySupplier<Item> FISHING_ROD_PLATINUM = fishingRod(EnumToolTier.PLATINUM);
    public static final RegistryEntrySupplier<Item> MOB_STAFF = ITEMS.register("monster_command_staff", () -> new ItemCommandStaff(new Item.Properties().stacksTo(1).tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
    public static final RegistryEntrySupplier<Item> BRUSH = ITEMS.register("brush", () -> new ItemBrush(new Item.Properties().stacksTo(1).tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
    public static final RegistryEntrySupplier<Item> GLASS = ITEMS.register("magnifying_glass", () -> new ItemToolGlass(new Item.Properties().stacksTo(1).tab(RFCreativeTabs.WEAPON_TOOL_TAB)));

    public static final RegistryEntrySupplier<Item> LEVELISER = ITEMS.register("leveliser", () -> new ItemStatIncrease(ItemStatIncrease.Stat.LEVEL, new Item.Properties().tab(RFCreativeTabs.MEDICINE)));
    public static final RegistryEntrySupplier<Item> HEART_DRINK = ITEMS.register("heart_drink", () -> new ItemStatIncrease(ItemStatIncrease.Stat.HP, new Item.Properties().tab(RFCreativeTabs.MEDICINE)));
    public static final RegistryEntrySupplier<Item> VITAL_GUMMI = ITEMS.register("vital_gummi", () -> new ItemStatIncrease(ItemStatIncrease.Stat.VIT, new Item.Properties().tab(RFCreativeTabs.MEDICINE)));
    public static final RegistryEntrySupplier<Item> INTELLIGENCER = ITEMS.register("intelligencer", () -> new ItemStatIncrease(ItemStatIncrease.Stat.INT, new Item.Properties().tab(RFCreativeTabs.MEDICINE)));
    public static final RegistryEntrySupplier<Item> PROTEIN = ITEMS.register("protein", () -> new ItemStatIncrease(ItemStatIncrease.Stat.STR, new Item.Properties().tab(RFCreativeTabs.MEDICINE)));
    public static final RegistryEntrySupplier<Item> formularA = ITEMS.register("formular_a", () -> new ItemFertilizer(ItemFertilizer.FORMULAR_A, new Item.Properties().tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
    public static final RegistryEntrySupplier<Item> formularB = ITEMS.register("formular_b", () -> new ItemFertilizer(ItemFertilizer.FORMULAR_B, new Item.Properties().tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
    public static final RegistryEntrySupplier<Item> formularC = ITEMS.register("formular_c", () -> new ItemFertilizer(ItemFertilizer.FORMULAR_C, new Item.Properties().tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
    public static final RegistryEntrySupplier<Item> MINIMIZER = ITEMS.register("minimizer", () -> new ItemFertilizer(ItemFertilizer.MINIMIZER, new Item.Properties().tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
    public static final RegistryEntrySupplier<Item> GIANTIZER = ITEMS.register("giantizer", () -> new ItemFertilizer(ItemFertilizer.GIANTIZER, new Item.Properties().tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
    public static final RegistryEntrySupplier<Item> GREENIFIER = ITEMS.register("greenifier", () -> new ItemFertilizer(ItemFertilizer.GREENIFIER, new Item.Properties().tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
    public static final RegistryEntrySupplier<Item> GREENIFIER_PLUS = ITEMS.register("greenifier_plus", () -> new ItemFertilizer(ItemFertilizer.GREENIFIER_PLUS, new Item.Properties().tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
    public static final RegistryEntrySupplier<Item> WETTABLE_POWDER = ITEMS.register("wettable_powder", () -> new ItemFertilizer(ItemFertilizer.WETTABLE, new Item.Properties().tab(RFCreativeTabs.WEAPON_TOOL_TAB)));

    public static final RegistryEntrySupplier<Item> BROAD_SWORD = shortSword("broad_sword", Texture.Y);
    public static final RegistryEntrySupplier<Item> STEEL_SWORD = shortSword("steel_sword", Texture.Y);
    public static final RegistryEntrySupplier<Item> STEEL_SWORD_PLUS = shortSword("steel_sword_plus", Texture.Y);
    public static final RegistryEntrySupplier<Item> CUTLASS = shortSword("cutlass", Texture.Y);
    public static final RegistryEntrySupplier<Item> AQUA_SWORD = shortSword("aqua_sword", Texture.Y);
    public static final RegistryEntrySupplier<Item> INVISI_BLADE = shortSword("invisiblade", Texture.Y);
    public static final RegistryEntrySupplier<Item> DEFENDER = shortSword("defender", Texture.N);
    public static final RegistryEntrySupplier<Item> BURNING_SWORD = shortSword("burning_sword", Texture.N);
    public static final RegistryEntrySupplier<Item> GORGEOUS_SWORD = shortSword("gorgeous_sword", Texture.N);
    public static final RegistryEntrySupplier<Item> GAIA_SWORD = shortSword("gaia_sword", Texture.N);
    public static final RegistryEntrySupplier<Item> SNAKE_SWORD = shortSword("snake_sword", Texture.N);
    public static final RegistryEntrySupplier<Item> LUCK_BLADE = shortSword("luck_blade", Texture.N);
    public static final RegistryEntrySupplier<Item> PLATINUM_SWORD = shortSword("platinum_sword", Texture.N);
    public static final RegistryEntrySupplier<Item> WIND_SWORD = shortSword("wind_sword", Texture.N);
    public static final RegistryEntrySupplier<Item> CHAOS_BLADE = shortSword("chaos_blade", Texture.N);
    public static final RegistryEntrySupplier<Item> SAKURA = shortSword("sakura", Texture.N);
    public static final RegistryEntrySupplier<Item> SUNSPOT = shortSword("sunspot", Texture.N);
    public static final RegistryEntrySupplier<Item> DURENDAL = shortSword("durendal", Texture.N);
    public static final RegistryEntrySupplier<Item> AERIAL_BLADE = shortSword("aerial_blade", Texture.N);
    public static final RegistryEntrySupplier<Item> GRANTALE = shortSword("grantale", Texture.N);
    public static final RegistryEntrySupplier<Item> SMASH_BLADE = shortSword("smash_blade", Texture.N);
    public static final RegistryEntrySupplier<Item> ICIFIER = shortSword("icifier", Texture.N);
    public static final RegistryEntrySupplier<Item> SOUL_EATER = shortSword("soul_eater", Texture.N);
    public static final RegistryEntrySupplier<Item> RAVENTINE = shortSword("raventine", Texture.N);
    public static final RegistryEntrySupplier<Item> STAR_SABER = shortSword("star_saber", Texture.N);
    public static final RegistryEntrySupplier<Item> PLATINUM_SWORD_PLUS = shortSword("platinum_sword_plus", Texture.N);
    public static final RegistryEntrySupplier<Item> DRAGON_SLAYER = shortSword("dragon_slayer", Texture.N);
    public static final RegistryEntrySupplier<Item> RUNE_BLADE = shortSword("rune_blade", Texture.N);
    public static final RegistryEntrySupplier<Item> GLADIUS = shortSword("gladius", Texture.N);
    public static final RegistryEntrySupplier<Item> RUNE_LEGEND = shortSword("rune_legend", Texture.N);
    public static final RegistryEntrySupplier<Item> BACK_SCRATCHER = shortSword("back_scratcher", Texture.N);
    public static final RegistryEntrySupplier<Item> SPOON = shortSword("spoon", Texture.N);
    public static final RegistryEntrySupplier<Item> VEGGIE_BLADE = shortSword("veggie_blade", Texture.N);
    public static final RegistryEntrySupplier<Item> PLANT_SWORD = ITEMS.register("plant_sword", () -> new ItemShortSwordBase(new Item.Properties().tab(RFCreativeTabs.WEAPON_TOOL_TAB)));

    public static final RegistryEntrySupplier<Item> CLAYMORE = longSword("claymore", Texture.Y);
    public static final RegistryEntrySupplier<Item> ZWEIHAENDER = longSword("zweihaender", Texture.Y);
    public static final RegistryEntrySupplier<Item> ZWEIHAENDER_PLUS = longSword("zweihaender_plus", Texture.Y);
    public static final RegistryEntrySupplier<Item> GREAT_SWORD = longSword("great_sword", Texture.Y);
    public static final RegistryEntrySupplier<Item> SEA_CUTTER = longSword("sea_cutter", Texture.Y);
    public static final RegistryEntrySupplier<Item> CYCLONE_BLADE = longSword("cyclone_blade", Texture.Y);
    public static final RegistryEntrySupplier<Item> POISON_BLADE = longSword("poison_blade", Texture.N);
    public static final RegistryEntrySupplier<Item> KATZBALGER = longSword("katzbalger", Texture.N);
    public static final RegistryEntrySupplier<Item> EARTH_SHADE = longSword("earth_shade", Texture.N);
    public static final RegistryEntrySupplier<Item> BIG_KNIFE = longSword("big_knife", Texture.N);
    public static final RegistryEntrySupplier<Item> KATANA = longSword("katana", Texture.N);
    public static final RegistryEntrySupplier<Item> FLAME_SABER = longSword("flame_saber", Texture.N);
    public static final RegistryEntrySupplier<Item> BIO_SMASHER = longSword("bio_smasher", Texture.N);
    public static final RegistryEntrySupplier<Item> SNOW_CROWN = longSword("snow_crown", Texture.N);
    public static final RegistryEntrySupplier<Item> DANCING_DICER = longSword("dancing_dicer", Texture.N);
    public static final RegistryEntrySupplier<Item> FLAMBERGE = longSword("flamberge", Texture.N);
    public static final RegistryEntrySupplier<Item> FLAMBERGE_PLUS = longSword("flamberge_plus", Texture.N);
    public static final RegistryEntrySupplier<Item> VOLCANON = longSword("volcanon", Texture.N);
    public static final RegistryEntrySupplier<Item> PSYCHO = longSword("psycho", Texture.N);
    public static final RegistryEntrySupplier<Item> SHINE_BLADE = longSword("shine_blade", Texture.N);
    public static final RegistryEntrySupplier<Item> GRAND_SMASHER = longSword("grand_smasher", Texture.N);
    public static final RegistryEntrySupplier<Item> BELZEBUTH = longSword("belzebuth", Texture.N);
    public static final RegistryEntrySupplier<Item> OROCHI = longSword("orochi", Texture.N);
    public static final RegistryEntrySupplier<Item> PUNISHER = longSword("punisher", Texture.N);
    public static final RegistryEntrySupplier<Item> STEEL_SLICER = longSword("steel_slicer", Texture.N);
    public static final RegistryEntrySupplier<Item> MOON_SHADOW = longSword("moon_shadow", Texture.N);
    public static final RegistryEntrySupplier<Item> BLUE_EYED_BLADE = longSword("blue_eyed_blade", Texture.N);
    public static final RegistryEntrySupplier<Item> BALMUNG = longSword("balmung", Texture.N);
    public static final RegistryEntrySupplier<Item> BRAVEHEART = longSword("braveheart", Texture.N);
    public static final RegistryEntrySupplier<Item> FORCE_ELEMENT = longSword("force_element", Texture.N);
    public static final RegistryEntrySupplier<Item> HEAVENS_ASUNDER = longSword("heavens_asunder", Texture.N);
    public static final RegistryEntrySupplier<Item> CALIBURN = longSword("caliburn", Texture.N);
    public static final RegistryEntrySupplier<Item> DEKASH = longSword("dekash", Texture.N);
    public static final RegistryEntrySupplier<Item> DAICONE = longSword("daicone", Texture.N);

    public static final RegistryEntrySupplier<Item> SPEAR = spear("spear", Texture.Y);
    public static final RegistryEntrySupplier<Item> WOOD_STAFF = spear("wood_staff", Texture.Y);
    public static final RegistryEntrySupplier<Item> LANCE = spear("lance", Texture.Y);
    public static final RegistryEntrySupplier<Item> LANCE_PLUS = spear("lance_plus", Texture.Y);
    public static final RegistryEntrySupplier<Item> NEEDLE_SPEAR = spear("needle_spear", Texture.Y);
    public static final RegistryEntrySupplier<Item> TRIDENT = spear("trident", Texture.Y);
    public static final RegistryEntrySupplier<Item> WATER_SPEAR = spear("water_spear", Texture.N);
    public static final RegistryEntrySupplier<Item> HALBERD = spear("halberd", Texture.N);
    public static final RegistryEntrySupplier<Item> CORSESCA = spear("corsesca", Texture.N);
    public static final RegistryEntrySupplier<Item> CORSESCA_PLUS = spear("corsesca_plus", Texture.N);
    public static final RegistryEntrySupplier<Item> POISON_SPEAR = spear("poison_spear", Texture.N);
    public static final RegistryEntrySupplier<Item> FIVE_STAFF = spear("five_staff", Texture.N);
    public static final RegistryEntrySupplier<Item> HEAVY_LANCE = spear("heavy_lance", Texture.N);
    public static final RegistryEntrySupplier<Item> FEATHER_LANCE = spear("feather_lance", Texture.N);
    public static final RegistryEntrySupplier<Item> ICEBERG = spear("iceberg", Texture.N);
    public static final RegistryEntrySupplier<Item> BLOOD_LANCE = spear("blood_lance", Texture.N);
    public static final RegistryEntrySupplier<Item> MAGICAL_LANCE = spear("magical_lance", Texture.N);
    public static final RegistryEntrySupplier<Item> FLARE_LANCE = spear("flare_lance", Texture.N);
    public static final RegistryEntrySupplier<Item> BRIONAC = spear("brionac", Texture.N);
    public static final RegistryEntrySupplier<Item> POISON_QUEEN = spear("poison_queen", Texture.N);
    public static final RegistryEntrySupplier<Item> MONK_STAFF = spear("monk_staff", Texture.N);
    public static final RegistryEntrySupplier<Item> METUS = spear("metus", Texture.N);
    public static final RegistryEntrySupplier<Item> SILENT_GRAVE = spear("silent_grave", Texture.N);
    public static final RegistryEntrySupplier<Item> OVERBREAK = spear("overbreak", Texture.N);
    public static final RegistryEntrySupplier<Item> BJOR = spear("bjor", Texture.N);
    public static final RegistryEntrySupplier<Item> BELVAROSE = spear("belvarose", Texture.N);
    public static final RegistryEntrySupplier<Item> GAE_BOLG = spear("gae_bolg", Texture.N);
    public static final RegistryEntrySupplier<Item> DRAGONS_FANG = spear("dragons_fang", Texture.N);
    public static final RegistryEntrySupplier<Item> GUNGNIR = spear("gungnir", Texture.N);
    public static final RegistryEntrySupplier<Item> LEGION = spear("legion", Texture.N);
    public static final RegistryEntrySupplier<Item> PITCHFORK = spear("pitchfork", Texture.N);
    public static final RegistryEntrySupplier<Item> SAFETY_LANCE = spear("safety_lance", Texture.N);
    public static final RegistryEntrySupplier<Item> PINE_CLUB = spear("pine_club", Texture.N);

    public static final RegistryEntrySupplier<Item> BATTLE_AXE = axe("battle_axe", Texture.Y);
    public static final RegistryEntrySupplier<Item> BATTLE_SCYTHE = axe("battle_scythe", Texture.Y);
    public static final RegistryEntrySupplier<Item> POLE_AXE = axe("pole_axe", Texture.Y);
    public static final RegistryEntrySupplier<Item> POLE_AXE_PLUS = axe("pole_axe_plus", Texture.Y);
    public static final RegistryEntrySupplier<Item> GREAT_AXE = axe("great_axe", Texture.Y);
    public static final RegistryEntrySupplier<Item> TOMAHAWK = axe("tomahawk", Texture.Y);
    public static final RegistryEntrySupplier<Item> BASILISK_FANG = axe("basilisk_fang", Texture.N);
    public static final RegistryEntrySupplier<Item> ROCK_AXE = axe("rock_axe", Texture.N);
    public static final RegistryEntrySupplier<Item> DEMON_AXE = axe("demon_axe", Texture.N);
    public static final RegistryEntrySupplier<Item> FROST_AXE = axe("frost_axe", Texture.N);
    public static final RegistryEntrySupplier<Item> CRESCENT_AXE = axe("crescent_axe", Texture.N);
    public static final RegistryEntrySupplier<Item> CRESCENT_AXE_PLUS = axe("crescent_axe_plus", Texture.N);
    public static final RegistryEntrySupplier<Item> HEAT_AXE = axe("heat_axe", Texture.N);
    public static final RegistryEntrySupplier<Item> DOUBLE_EDGE = axe("double_edge", Texture.N);
    public static final RegistryEntrySupplier<Item> ALLDALE = axe("alldale", Texture.N);
    public static final RegistryEntrySupplier<Item> DEVIL_FINGER = axe("devil_finger", Texture.N);
    public static final RegistryEntrySupplier<Item> EXECUTIONER = axe("executioner", Texture.N);
    public static final RegistryEntrySupplier<Item> SAINT_AXE = axe("saint_axe", Texture.N);
    public static final RegistryEntrySupplier<Item> AXE = axe("axe", Texture.N);
    public static final RegistryEntrySupplier<Item> LOLLIPOP = axe("lollipop", Texture.N);

    public static final RegistryEntrySupplier<Item> BATTLE_HAMMER = hammer("battle_hammer", Texture.Y);
    public static final RegistryEntrySupplier<Item> BAT = hammer("bat", Texture.Y);
    public static final RegistryEntrySupplier<Item> WAR_HAMMER = hammer("war_hammer", Texture.Y);
    public static final RegistryEntrySupplier<Item> WAR_HAMMER_PLUS = hammer("war_hammer_plus", Texture.Y);
    public static final RegistryEntrySupplier<Item> IRON_BAT = hammer("iron_bat", Texture.Y);
    public static final RegistryEntrySupplier<Item> GREAT_HAMMER = hammer("great_hammer", Texture.Y);
    public static final RegistryEntrySupplier<Item> ICE_HAMMER = hammer("ice_hammer", Texture.N);
    public static final RegistryEntrySupplier<Item> BONE_HAMMER = hammer("bone_hammer", Texture.N);
    public static final RegistryEntrySupplier<Item> STRONG_STONE = hammer("strong_stone", Texture.N);
    public static final RegistryEntrySupplier<Item> FLAME_HAMMER = hammer("flame_hammer", Texture.N);
    public static final RegistryEntrySupplier<Item> GIGANT_HAMMER = hammer("gigant_hammer", Texture.N);
    public static final RegistryEntrySupplier<Item> SKY_HAMMER = hammer("sky_hammer", Texture.N);
    public static final RegistryEntrySupplier<Item> GRAVITON_HAMMER = hammer("graviton_hammer", Texture.N);
    public static final RegistryEntrySupplier<Item> SPIKED_HAMMER = hammer("spiked_hammer", Texture.N);
    public static final RegistryEntrySupplier<Item> CRYSTAL_HAMMER = hammer("crystal_hammer", Texture.N);
    public static final RegistryEntrySupplier<Item> SCHNABEL = hammer("schnabel", Texture.N);
    public static final RegistryEntrySupplier<Item> GIGANT_HAMMER_PLUS = hammer("gigant_hammer_plus", Texture.N);
    public static final RegistryEntrySupplier<Item> KONGO = hammer("kongo", Texture.N);
    public static final RegistryEntrySupplier<Item> MJOLNIR = hammer("mjolnir", Texture.N);
    public static final RegistryEntrySupplier<Item> FATAL_CRUSH = hammer("fatal_crush", Texture.N);
    public static final RegistryEntrySupplier<Item> SPLASH_STAR = hammer("splash_star", Texture.N);
    public static final RegistryEntrySupplier<Item> HAMMER = hammer("hammer", Texture.N);
    public static final RegistryEntrySupplier<Item> TOY_HAMMER = hammer("toy_hammer", Texture.N);

    public static final RegistryEntrySupplier<Item> SHORT_DAGGER = dualBlade("short_dagger", Texture.Y);
    public static final RegistryEntrySupplier<Item> STEEL_EDGE = dualBlade("steel_edge", Texture.Y);
    public static final RegistryEntrySupplier<Item> FROST_EDGE = dualBlade("frost_edge", Texture.Y);
    public static final RegistryEntrySupplier<Item> IRON_EDGE = dualBlade("iron_edge", Texture.Y);
    public static final RegistryEntrySupplier<Item> THIEF_KNIFE = dualBlade("thief_knife", Texture.Y);
    public static final RegistryEntrySupplier<Item> WIND_EDGE = dualBlade("wind_edge", Texture.Y);
    public static final RegistryEntrySupplier<Item> GORGEOUS_LX = dualBlade("gourgeous_lx", Texture.N);
    public static final RegistryEntrySupplier<Item> STEEL_KATANA = dualBlade("steel_katana", Texture.N);
    public static final RegistryEntrySupplier<Item> TWIN_BLADE = dualBlade("twin_blade", Texture.N);
    public static final RegistryEntrySupplier<Item> RAMPAGE = dualBlade("rampage", Texture.N);
    public static final RegistryEntrySupplier<Item> SALAMANDER = dualBlade("salamander", Texture.N);
    public static final RegistryEntrySupplier<Item> PLATINUM_EDGE = dualBlade("platinum_edge", Texture.N);
    public static final RegistryEntrySupplier<Item> SONIC_DAGGER = dualBlade("sonic_dagger", Texture.N);
    public static final RegistryEntrySupplier<Item> CHAOS_EDGE = dualBlade("chaos_edge", Texture.N);
    public static final RegistryEntrySupplier<Item> DESERT_WIND = dualBlade("desert_wind", Texture.N);
    public static final RegistryEntrySupplier<Item> BROKEN_WALL = dualBlade("broken_wall", Texture.N);
    public static final RegistryEntrySupplier<Item> FORCE_DIVIDE = dualBlade("force_divide", Texture.N);
    public static final RegistryEntrySupplier<Item> HEART_FIRE = dualBlade("heart_fire", Texture.N);
    public static final RegistryEntrySupplier<Item> ORCUS_SWORD = dualBlade("orcus_sword", Texture.N);
    public static final RegistryEntrySupplier<Item> DEEP_BLIZZARD = dualBlade("deep_blizzard", Texture.N);
    public static final RegistryEntrySupplier<Item> DARK_INVITATION = dualBlade("dark_invitation", Texture.N);
    public static final RegistryEntrySupplier<Item> PRIEST_SABER = dualBlade("priest_saber", Texture.N);
    public static final RegistryEntrySupplier<Item> EFREET = dualBlade("efreet", Texture.N);
    public static final RegistryEntrySupplier<Item> DRAGOON_CLAW = dualBlade("dragoon_claw", Texture.N);
    public static final RegistryEntrySupplier<Item> EMERALD_EDGE = dualBlade("emerald_edge", Texture.N);
    public static final RegistryEntrySupplier<Item> RUNE_EDGE = dualBlade("rune_edge", Texture.N);
    public static final RegistryEntrySupplier<Item> EARNEST_EDGE = dualBlade("earnest_edge", Texture.N);
    public static final RegistryEntrySupplier<Item> TWIN_JUSTICE = dualBlade("twin_justice", Texture.N);
    public static final RegistryEntrySupplier<Item> DOUBLE_SCRATCH = dualBlade("double_scratch", Texture.N);
    public static final RegistryEntrySupplier<Item> ACUTORIMASS = dualBlade("acutorimass", Texture.N);
    public static final RegistryEntrySupplier<Item> TWIN_LEEKS = dualBlade("twin_leeks", Texture.N);

    public static final RegistryEntrySupplier<Item> LEATHER_GLOVE = gloves("leather_glove", Texture.Y);
    public static final RegistryEntrySupplier<Item> BRASS_KNUCKLES = gloves("brass_knuckles", Texture.Y);
    public static final RegistryEntrySupplier<Item> KOTE = gloves("kote", Texture.Y);
    public static final RegistryEntrySupplier<Item> GLOVES = gloves("gloves", Texture.Y);
    public static final RegistryEntrySupplier<Item> BEAR_CLAWS = gloves("bear_claws", Texture.Y);
    public static final RegistryEntrySupplier<Item> FIST_EARTH = gloves("fist_of_earth", Texture.Y);
    public static final RegistryEntrySupplier<Item> FIST_FIRE = gloves("fist_of_fire", Texture.N);
    public static final RegistryEntrySupplier<Item> FIST_WATER = gloves("fist_of_water", Texture.N);
    public static final RegistryEntrySupplier<Item> DRAGON_CLAWS = gloves("dragon_claws", Texture.N);
    public static final RegistryEntrySupplier<Item> FIST_DARK = gloves("fist_of_dark", Texture.N);
    public static final RegistryEntrySupplier<Item> FIST_WIND = gloves("fist_of_wind", Texture.N);
    public static final RegistryEntrySupplier<Item> FIST_LIGHT = gloves("fist_of_light", Texture.N);
    public static final RegistryEntrySupplier<Item> CAT_PUNCH = gloves("cat_punch", Texture.N);
    public static final RegistryEntrySupplier<Item> ANIMAL_PUPPETS = gloves("animal_puppets", Texture.N);
    public static final RegistryEntrySupplier<Item> IRONLEAF_FISTS = gloves("ironleaf_fists", Texture.N);
    public static final RegistryEntrySupplier<Item> CAESTUS = gloves("caestus", Texture.N);
    public static final RegistryEntrySupplier<Item> GOLEM_PUNCH = gloves("golem_punch", Texture.N);
    public static final RegistryEntrySupplier<Item> GOD_HAND = gloves("hand_of_god", Texture.N);
    public static final RegistryEntrySupplier<Item> BAZAL_KATAR = gloves("bazal_katar", Texture.N);
    public static final RegistryEntrySupplier<Item> FENRIR = gloves("fenrir", Texture.N);

    public static final RegistryEntrySupplier<Item> ROD = staff("rod", EnumElement.FIRE, 1, Texture.Y);
    public static final RegistryEntrySupplier<Item> AMETHYST_ROD = staff("amethyst_rod", EnumElement.EARTH, 1, Texture.Y);
    public static final RegistryEntrySupplier<Item> AQUAMARINE_ROD = staff("aquamarine_rod", EnumElement.WATER, 1, Texture.Y);
    public static final RegistryEntrySupplier<Item> FRIENDLY_ROD = staff("friendly_rod", EnumElement.LOVE, 1, Texture.Y);
    public static final RegistryEntrySupplier<Item> LOVE_LOVE_ROD = staff("love_love_rod", EnumElement.LOVE, 1, Texture.Y);
    public static final RegistryEntrySupplier<Item> STAFF = staff("staff", EnumElement.EARTH, 1, Texture.Y);
    public static final RegistryEntrySupplier<Item> EMERALD_ROD = staff("emerald_rod", EnumElement.WIND, 1, Texture.N);
    public static final RegistryEntrySupplier<Item> SILVER_STAFF = staff("silver_staff", EnumElement.DARK, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> FLARE_STAFF = staff("flare_staff", EnumElement.FIRE, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> RUBY_ROD = staff("ruby_rod", EnumElement.FIRE, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> SAPPHIRE_ROD = staff("sapphire_rod", EnumElement.LIGHT, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> EARTH_STAFF = staff("earth_staff", EnumElement.EARTH, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> LIGHTNING_WAND = staff("lightning_wand", EnumElement.WIND, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> ICE_STAFF = staff("ice_staff", EnumElement.WATER, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> DIAMOND_ROD = staff("diamond_rod", EnumElement.DARK, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> WIZARDS_STAFF = staff("wizards_staff", EnumElement.LIGHT, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> MAGES_STAFF = staff("mages_staff", EnumElement.EARTH, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> SHOOTING_STAR_STAFF = staff("shooting_star_staff", EnumElement.LIGHT, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> HELL_BRANCH = staff("hell_branch", EnumElement.DARK, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> CRIMSON_STAFF = staff("crimson_staff", EnumElement.FIRE, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> BUBBLE_STAFF = staff("bubble_staff", EnumElement.WATER, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> GAIA_ROD = staff("gaia_rod", EnumElement.EARTH, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> CYCLONE_ROD = staff("cyclone_rod", EnumElement.WIND, 3, Texture.N);
    public static final RegistryEntrySupplier<Item> STORM_WAND = staff("storm_wand", EnumElement.WIND, 3, Texture.N);
    public static final RegistryEntrySupplier<Item> RUNE_STAFF = staff("rune_staff", EnumElement.LIGHT, 3, Texture.N);
    public static final RegistryEntrySupplier<Item> MAGES_STAFF_PLUS = staff("mages_staff_plus", EnumElement.LOVE, 3, Texture.N);
    public static final RegistryEntrySupplier<Item> MAGIC_BROOM = staff("magic_broom", EnumElement.WIND, 3, Texture.N);
    public static final RegistryEntrySupplier<Item> MAGIC_SHOT = staff("magic_shot", EnumElement.LOVE, 3, Texture.N);
    public static final RegistryEntrySupplier<Item> HELL_CURSE = staff("hell_curse", EnumElement.DARK, 3, Texture.N);
    public static final RegistryEntrySupplier<Item> ALGERNON = staff("algernon", EnumElement.EARTH, 3, Texture.N);
    public static final RegistryEntrySupplier<Item> SORCERES_WAND = staff("sorceres_wand", EnumElement.LIGHT, 3, Texture.N);
    public static final RegistryEntrySupplier<Item> BASKET = staff("basket", EnumElement.LOVE, 1, Texture.N);
    public static final RegistryEntrySupplier<Item> GOLDEN_TURNIP_STAFF = staff("golden_turnip_staff", EnumElement.LOVE, 2, Texture.N);
    public static final RegistryEntrySupplier<Item> SWEET_POTATO_STAFF = staff("sweet_potato_staff", EnumElement.LOVE, 1, Texture.N);
    public static final RegistryEntrySupplier<Item> ELVISH_HARP = staff("elvish_harp", EnumElement.LOVE, 3, Texture.N);
    public static final RegistryEntrySupplier<Item> SYRINGE = staff("syringe", EnumElement.WATER, 2, Texture.N);

    public static final RegistryEntrySupplier<Item> LOVE_LETTER = ITEMS.register("love_letter", () -> new Item(new Item.Properties().stacksTo(1).tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
    public static final RegistryEntrySupplier<Item> DIVORCE_PAPER = ITEMS.register("divorce_paper", () -> new Item(new Item.Properties().stacksTo(1).tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
    public static final RegistryEntrySupplier<Item> ENGAGEMENT_RING = ITEMS.register("engagement_ring", () -> Platform.INSTANCE.armor(EquipmentSlot.LEGS, new Item.Properties().tab(RFCreativeTabs.EQUIPMENT), new ResourceLocation(RuneCraftory.MODID, "engagement_ring"), false));
    public static final RegistryEntrySupplier<Item> CHEAP_BRACELET = equipment(EquipmentSlot.LEGS, "cheap_bracelet", Texture.Y);
    public static final RegistryEntrySupplier<Item> BRONZE_BRACELET = equipment(EquipmentSlot.LEGS, "bronze_bracelet", Texture.Y);
    public static final RegistryEntrySupplier<Item> SILVER_BRACELET = equipment(EquipmentSlot.LEGS, "silver_bracelet", Texture.Y);
    public static final RegistryEntrySupplier<Item> GOLD_BRACELET = equipment(EquipmentSlot.LEGS, "gold_bracelet", Texture.Y);
    public static final RegistryEntrySupplier<Item> PLATINUM_BRACELET = equipment(EquipmentSlot.LEGS, "platinum_bracelet", Texture.Y);
    public static final RegistryEntrySupplier<Item> SILVER_RING = equipment(EquipmentSlot.LEGS, "silver_ring", Texture.Y);
    public static final RegistryEntrySupplier<Item> GOLD_RING = equipment(EquipmentSlot.LEGS, "gold_ring", Texture.Y);
    public static final RegistryEntrySupplier<Item> PLATINUM_RING = equipment(EquipmentSlot.LEGS, "platinum_ring", Texture.Y);
    public static final RegistryEntrySupplier<Item> SHIELD_RING = equipment(EquipmentSlot.LEGS, "shield_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> CRITICAL_RING = equipment(EquipmentSlot.LEGS, "critical_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> SILENT_RING = equipment(EquipmentSlot.LEGS, "silent_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> PARALYSIS_RING = equipment(EquipmentSlot.LEGS, "paralysis_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> POISON_RING = equipment(EquipmentSlot.LEGS, "poison_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> MAGIC_RING = equipment(EquipmentSlot.LEGS, "magic_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> THROWING_RING = equipment(EquipmentSlot.LEGS, "throwing_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> STAY_UP_RING = equipment(EquipmentSlot.LEGS, "stay_up_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> AQUAMARINE_RING = equipment(EquipmentSlot.LEGS, "aquamarine_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> AMETHYST_RING = equipment(EquipmentSlot.LEGS, "amethyst_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> EMERALD_RING = equipment(EquipmentSlot.LEGS, "emerald_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> SAPPHIRE_RING = equipment(EquipmentSlot.LEGS, "sapphire_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> RUBY_RING = equipment(EquipmentSlot.LEGS, "ruby_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> CURSED_RING = equipment(EquipmentSlot.LEGS, "cursed_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> DIAMOND_RING = equipment(EquipmentSlot.LEGS, "diamond_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> AQUAMARINE_BROOCH = equipment(EquipmentSlot.LEGS, "aquamarine_brooch", Texture.N);
    public static final RegistryEntrySupplier<Item> AMETHYST_BROOCH = equipment(EquipmentSlot.LEGS, "amethyst_brooch", Texture.N);
    public static final RegistryEntrySupplier<Item> EMERALD_BROOCH = equipment(EquipmentSlot.LEGS, "emerald_brooch", Texture.N);
    public static final RegistryEntrySupplier<Item> SAPPHIRE_BROOCH = equipment(EquipmentSlot.LEGS, "sapphire_brooch", Texture.N);
    public static final RegistryEntrySupplier<Item> RUBY_BROOCH = equipment(EquipmentSlot.LEGS, "ruby_brooch", Texture.N);
    public static final RegistryEntrySupplier<Item> DIAMOND_BROOCH = equipment(EquipmentSlot.LEGS, "diamond_brooch", Texture.N);
    public static final RegistryEntrySupplier<Item> DOLPHIN_BROOCH = equipment(EquipmentSlot.LEGS, "dolphin_brooch", Texture.N);
    public static final RegistryEntrySupplier<Item> FIRE_RING = equipment(EquipmentSlot.LEGS, "fire_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> WIND_RING = equipment(EquipmentSlot.LEGS, "wind_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> WATER_RING = equipment(EquipmentSlot.LEGS, "water_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> EARTH_RING = equipment(EquipmentSlot.LEGS, "earth_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> HAPPY_RING = equipment(EquipmentSlot.LEGS, "happy_ring", Texture.N);
    public static final RegistryEntrySupplier<Item> SILVER_PENDANT = equipment(EquipmentSlot.LEGS, "silver_pendant", Texture.N);
    public static final RegistryEntrySupplier<Item> STAR_PENDANT = equipment(EquipmentSlot.LEGS, "star_pendant", Texture.N);
    public static final RegistryEntrySupplier<Item> SUN_PENDANT = equipment(EquipmentSlot.LEGS, "sun_pendant", Texture.N);
    public static final RegistryEntrySupplier<Item> FIELD_PENDANT = equipment(EquipmentSlot.LEGS, "field_pendant", Texture.N);
    public static final RegistryEntrySupplier<Item> DEW_PENDANT = equipment(EquipmentSlot.LEGS, "dew_pendant", Texture.N);
    public static final RegistryEntrySupplier<Item> EARTH_PENDANT = equipment(EquipmentSlot.LEGS, "earth_pendant", Texture.N);
    public static final RegistryEntrySupplier<Item> HEART_PENDANT = equipment(EquipmentSlot.LEGS, "heart_pendant", Texture.N);
    public static final RegistryEntrySupplier<Item> STRANGE_PENDANT = equipment(EquipmentSlot.LEGS, "strange_pendant", Texture.N);
    public static final RegistryEntrySupplier<Item> ANETTES_NECKLACE = equipment(EquipmentSlot.LEGS, "anettes_necklace", Texture.N);
    public static final RegistryEntrySupplier<Item> WORK_GLOVES = equipment(EquipmentSlot.LEGS, "work_gloves", Texture.N);
    public static final RegistryEntrySupplier<Item> GLOVES_ACCESS = equipment(EquipmentSlot.LEGS, "gloves_accessory", Texture.N);
    public static final RegistryEntrySupplier<Item> POWER_GLOVES = equipment(EquipmentSlot.LEGS, "power_gloves", Texture.N);
    public static final RegistryEntrySupplier<Item> EARRINGS = equipment(EquipmentSlot.LEGS, "earrings", Texture.N);
    public static final RegistryEntrySupplier<Item> WITCH_EARRINGS = equipment(EquipmentSlot.LEGS, "witch_earrings", Texture.N);
    public static final RegistryEntrySupplier<Item> MAGIC_EARRINGS = equipment(EquipmentSlot.LEGS, "magic_earrings", Texture.Y);
    public static final RegistryEntrySupplier<Item> CHARM = equipment(EquipmentSlot.LEGS, "charm", Texture.N);
    public static final RegistryEntrySupplier<Item> HOLY_AMULET = equipment(EquipmentSlot.LEGS, "holy_amulet", Texture.N);
    public static final RegistryEntrySupplier<Item> ROSARY = equipment(EquipmentSlot.LEGS, "rosary", Texture.N);
    public static final RegistryEntrySupplier<Item> TALISMAN = equipment(EquipmentSlot.LEGS, "talisman", Texture.N);
    public static final RegistryEntrySupplier<Item> MAGIC_CHARM = equipment(EquipmentSlot.LEGS, "magic_charm", Texture.N);
    public static final RegistryEntrySupplier<Item> LEATHER_BELT = equipment(EquipmentSlot.LEGS, "leather_belt", Texture.N);
    public static final RegistryEntrySupplier<Item> LUCKY_STRIKE = equipment(EquipmentSlot.LEGS, "lucky_strike", Texture.N);
    public static final RegistryEntrySupplier<Item> CHAMP_BELT = equipment(EquipmentSlot.LEGS, "champ_belt", Texture.N);
    public static final RegistryEntrySupplier<Item> HAND_KNIT_SCARF = equipment(EquipmentSlot.LEGS, "hand_knit_scarf", Texture.N);
    public static final RegistryEntrySupplier<Item> FLUFFY_SCARF = equipment(EquipmentSlot.LEGS, "fluffy_scarf", Texture.N);
    public static final RegistryEntrySupplier<Item> HEROS_PROOF = equipment(EquipmentSlot.LEGS, "heros_proof", Texture.N);
    public static final RegistryEntrySupplier<Item> PROOF_OF_WISDOM = equipment(EquipmentSlot.LEGS, "proof_of_wisdom", Texture.N);
    public static final RegistryEntrySupplier<Item> ART_OF_ATTACK = equipment(EquipmentSlot.LEGS, "art_of_attack", Texture.N);
    public static final RegistryEntrySupplier<Item> ART_OF_DEFENSE = equipment(EquipmentSlot.LEGS, "art_of_defense", Texture.N);
    public static final RegistryEntrySupplier<Item> ART_OF_MAGIC = equipment(EquipmentSlot.LEGS, "art_of_magic", Texture.N);
    public static final RegistryEntrySupplier<Item> BADGE = equipment(EquipmentSlot.LEGS, "badge", Texture.N);
    public static final RegistryEntrySupplier<Item> COURAGE_BADGE = equipment(EquipmentSlot.LEGS, "courage_badge", Texture.N);

    public static final RegistryEntrySupplier<Item> SHIRT = equipment(EquipmentSlot.CHEST, "shirt", Texture.Y);
    public static final RegistryEntrySupplier<Item> VEST = equipment(EquipmentSlot.CHEST, "vest", Texture.Y);
    public static final RegistryEntrySupplier<Item> COTTON_CLOTH = equipment(EquipmentSlot.CHEST, "cotton_cloth", Texture.Y);
    public static final RegistryEntrySupplier<Item> MAIL = equipment(EquipmentSlot.CHEST, "mail", Texture.N);
    public static final RegistryEntrySupplier<Item> CHAIN_MAIL = equipment(EquipmentSlot.CHEST, "chain_mail", Texture.N);
    public static final RegistryEntrySupplier<Item> SCALE_VEST = equipment(EquipmentSlot.CHEST, "scale_vest", Texture.N);
    public static final RegistryEntrySupplier<Item> SPARKLING_SHIRT = equipment(EquipmentSlot.CHEST, "sparkling_shirt", Texture.N);
    public static final RegistryEntrySupplier<Item> WIND_CLOAK = equipment(EquipmentSlot.CHEST, "wind_cloak", Texture.N);
    public static final RegistryEntrySupplier<Item> PROTECTOR = equipment(EquipmentSlot.CHEST, "protector", Texture.N);
    public static final RegistryEntrySupplier<Item> PLATINUM_MAIL = equipment(EquipmentSlot.CHEST, "platinum_mail", Texture.N);
    public static final RegistryEntrySupplier<Item> LEMELLAR_VEST = equipment(EquipmentSlot.CHEST, "lemellar_vest", Texture.N);
    public static final RegistryEntrySupplier<Item> MERCENARYS_CLOAK = equipment(EquipmentSlot.CHEST, "mercenarys_cloak", Texture.N);
    public static final RegistryEntrySupplier<Item> WOOLY_SHIRT = equipment(EquipmentSlot.CHEST, "wooly_shirt", Texture.N);
    public static final RegistryEntrySupplier<Item> ELVISH_CLOAK = equipment(EquipmentSlot.CHEST, "elvish_cloak", Texture.N);
    public static final RegistryEntrySupplier<Item> DRAGON_CLOAK = equipment(EquipmentSlot.CHEST, "dragon_cloak", Texture.N);
    public static final RegistryEntrySupplier<Item> POWER_PROTECTOR = equipment(EquipmentSlot.CHEST, "power_protector", Texture.N);
    public static final RegistryEntrySupplier<Item> RUNE_VEST = equipment(EquipmentSlot.CHEST, "rune_vest", Texture.N);
    public static final RegistryEntrySupplier<Item> ROYAL_GARTER = equipment(EquipmentSlot.CHEST, "royal_garter", Texture.N);
    public static final RegistryEntrySupplier<Item> FOUR_DRAGONS_VEST = equipment(EquipmentSlot.CHEST, "four_dragons_vest", Texture.N);

    public static final RegistryEntrySupplier<Item> HEADBAND = equipment(EquipmentSlot.HEAD, "headband", Texture.Y);
    public static final RegistryEntrySupplier<Item> BLUE_RIBBON = equipment(EquipmentSlot.HEAD, "blue_ribbon", Texture.Y, true);
    public static final RegistryEntrySupplier<Item> GREEN_RIBBON = equipment(EquipmentSlot.HEAD, "green_ribbon", Texture.Y, true);
    public static final RegistryEntrySupplier<Item> PURPLE_RIBBON = equipment(EquipmentSlot.HEAD, "purple_ribbon", Texture.Y, true);
    public static final RegistryEntrySupplier<Item> SPECTACLES = equipment(EquipmentSlot.HEAD, "spectacles", Texture.N);
    public static final RegistryEntrySupplier<Item> STRAW_HAT = equipment(EquipmentSlot.HEAD, "straw_hat", Texture.N);
    public static final RegistryEntrySupplier<Item> FANCY_HAT = equipment(EquipmentSlot.HEAD, "fancy_hat", Texture.N);
    public static final RegistryEntrySupplier<Item> BRAND_GLASSES = equipment(EquipmentSlot.HEAD, "brand_glasses", Texture.N);
    public static final RegistryEntrySupplier<Item> CUTE_KNITTING = equipment(EquipmentSlot.HEAD, "cute_knitting", Texture.N);
    public static final RegistryEntrySupplier<Item> INTELLIGENT_GLASSES = equipment(EquipmentSlot.HEAD, "intelligent_glasses", Texture.N);
    public static final RegistryEntrySupplier<Item> FIREPROOF_HOOD = equipment(EquipmentSlot.HEAD, "fireproof_hood", Texture.N);
    public static final RegistryEntrySupplier<Item> SILK_HAT = equipment(EquipmentSlot.HEAD, "silk_hat", Texture.N);
    public static final RegistryEntrySupplier<Item> BLACK_RIBBON = equipment(EquipmentSlot.HEAD, "black_ribbon", Texture.Y, true);
    public static final RegistryEntrySupplier<Item> LOLITA_HEADDRESS = equipment(EquipmentSlot.HEAD, "lolita_headdress", Texture.N);
    public static final RegistryEntrySupplier<Item> HEADDRESS = equipment(EquipmentSlot.HEAD, "headdress", Texture.N);
    public static final RegistryEntrySupplier<Item> YELLOW_RIBBON = equipment(EquipmentSlot.HEAD, "yellow_ribbon", Texture.Y, true);
    public static final RegistryEntrySupplier<Item> CAT_EARS = equipment(EquipmentSlot.HEAD, "cat_ears", Texture.N);
    public static final RegistryEntrySupplier<Item> SILVER_HAIRPIN = equipment(EquipmentSlot.HEAD, "silver_hairpin", Texture.N, true);
    public static final RegistryEntrySupplier<Item> RED_RIBBON = equipment(EquipmentSlot.HEAD, "red_ribbon", Texture.Y);
    public static final RegistryEntrySupplier<Item> ORANGE_RIBBON = equipment(EquipmentSlot.HEAD, "orange_ribbon", Texture.Y, true);
    public static final RegistryEntrySupplier<Item> WHITE_RIBBON = equipment(EquipmentSlot.HEAD, "white_ribbon", Texture.Y, true);
    public static final RegistryEntrySupplier<Item> FOUR_SEASONS = equipment(EquipmentSlot.HEAD, "four_seasons", Texture.N);
    public static final RegistryEntrySupplier<Item> FEATHERS_HAT = equipment(EquipmentSlot.HEAD, "feathers_hat", Texture.N);
    public static final RegistryEntrySupplier<Item> GOLD_HAIRPIN = equipment(EquipmentSlot.HEAD, "gold_hairpin", Texture.N);
    public static final RegistryEntrySupplier<Item> INDIGO_RIBBON = equipment(EquipmentSlot.HEAD, "indigo_ribbon", Texture.Y, true);
    public static final RegistryEntrySupplier<Item> CROWN = equipment(EquipmentSlot.HEAD, "crown", Texture.N);
    public static final RegistryEntrySupplier<Item> TURNIP_HEADGEAR = equipment(EquipmentSlot.HEAD, "turnip_headgear", Texture.N);
    public static final RegistryEntrySupplier<Item> PUMPKIN_HEADGEAR = equipment(EquipmentSlot.HEAD, "pumpkin_headgear", Texture.N);

    public static final RegistryEntrySupplier<Item> LEATHER_BOOTS = equipment(EquipmentSlot.FEET, "leather_boots", Texture.Y);
    public static final RegistryEntrySupplier<Item> FREE_FARMING_SHOES = equipment(EquipmentSlot.FEET, "free_farming_shoes", Texture.Y);
    public static final RegistryEntrySupplier<Item> PIYO_SANDALS = equipment(EquipmentSlot.FEET, "piyo_sandals", Texture.Y);
    public static final RegistryEntrySupplier<Item> SECRET_SHOES = equipment(EquipmentSlot.FEET, "secret_shoes", Texture.N);
    public static final RegistryEntrySupplier<Item> SILVER_BOOTS = equipment(EquipmentSlot.FEET, "silver_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> HEAVY_BOOTS = equipment(EquipmentSlot.FEET, "heavy_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> SNEAKING_BOOTS = equipment(EquipmentSlot.FEET, "sneaking_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> FAST_STEP_BOOTS = equipment(EquipmentSlot.FEET, "fast_step_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> GOLD_BOOTS = equipment(EquipmentSlot.FEET, "gold_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> BONE_BOOTS = equipment(EquipmentSlot.FEET, "bone_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> SNOW_BOOTS = equipment(EquipmentSlot.FEET, "snow_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> STRIDER_BOOTS = equipment(EquipmentSlot.FEET, "strider_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> STEP_IN_BOOTS = equipment(EquipmentSlot.FEET, "step_in_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> FEATHER_BOOTS = equipment(EquipmentSlot.FEET, "feather_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> GHOST_BOOTS = equipment(EquipmentSlot.FEET, "ghost_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> IRON_GETA = equipment(EquipmentSlot.FEET, "iron_geta", Texture.N);
    public static final RegistryEntrySupplier<Item> KNIGHT_BOOTS = equipment(EquipmentSlot.FEET, "knight_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> FAIRY_BOOTS = equipment(EquipmentSlot.FEET, "fairy_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> WET_BOOTS = equipment(EquipmentSlot.FEET, "wet_boots", Texture.N);
    public static final RegistryEntrySupplier<Item> WATER_SHOES = equipment(EquipmentSlot.FEET, "water_shoes", Texture.N);
    public static final RegistryEntrySupplier<Item> ICE_SKATES = equipment(EquipmentSlot.FEET, "ice_skates", Texture.N);
    public static final RegistryEntrySupplier<Item> ROCKET_WING = equipment(EquipmentSlot.FEET, "rocket_wing", Texture.N);

    public static final RegistryEntrySupplier<Item> SMALL_SHIELD = shield("small_shield", Texture.Y);
    public static final RegistryEntrySupplier<Item> UMBRELLA = shield("umbrella", Texture.N);
    public static final RegistryEntrySupplier<Item> IRON_SHIELD = shield("iron_shield", Texture.Y);
    public static final RegistryEntrySupplier<Item> MONKEY_PLUSH = shield("monkey_plush", Texture.N);
    public static final RegistryEntrySupplier<Item> ROUND_SHIELD = shield("round_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> TURTLE_SHIELD = shield("turtle_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> CHAOS_SHIELD = shield("chaos_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> BONE_SHIELD = shield("bone_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> MAGIC_SHIELD = shield("magic_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> HEAVY_SHIELD = shield("heavy_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> PLATINUM_SHIELD = shield("platinum_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> KITE_SHIELD = shield("kite_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> KNIGHT_SHIELD = shield("knight_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> ELEMENT_SHIELD = shield("element_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> MAGICAL_SHIELD = shield("magical_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> PRISM_SHIELD = shield("prism_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> RUNE_SHIELD = shield("rune_shield", Texture.N);
    public static final RegistryEntrySupplier<Item> PLANT_SHIELD = ITEMS.register("plant_shield", () -> new ItemStatShield(new Item.Properties().stacksTo(1).tab(RFCreativeTabs.EQUIPMENT)));

    public static final RegistryEntrySupplier<Item> ITEM_BLOCK_FORGE = ITEMS.register("forge", () -> new CraftingBlockItem(ModBlocks.FORGE.get(), new Item.Properties().tab(RFCreativeTabs.BLOCKS)));
    public static final RegistryEntrySupplier<Item> ITEM_BLOCK_ACCESS = ITEMS.register("accessory_workbench", () -> new CraftingBlockItem(ModBlocks.ACCESSORY.get(), new Item.Properties().tab(RFCreativeTabs.BLOCKS)));
    public static final RegistryEntrySupplier<Item> ITEM_BLOCK_COOKING = ITEMS.register("cooking_table", () -> new CraftingBlockItem(ModBlocks.COOKING.get(), new Item.Properties().tab(RFCreativeTabs.BLOCKS)));
    public static final RegistryEntrySupplier<Item> ITEM_BLOCK_CHEM = ITEMS.register("chemistry_set", () -> new CraftingBlockItem(ModBlocks.CHEMISTRY.get(), new Item.Properties().tab(RFCreativeTabs.BLOCKS)));

    public static final RegistryEntrySupplier<Item> MINERAL_IRON = mineral(EnumMineralTier.IRON);
    public static final RegistryEntrySupplier<Item> MINERAL_BRONZE = mineral(EnumMineralTier.BRONZE);
    public static final RegistryEntrySupplier<Item> MINERAL_SILVER = mineral(EnumMineralTier.SILVER);
    public static final RegistryEntrySupplier<Item> MINERAL_GOLD = mineral(EnumMineralTier.GOLD);
    public static final RegistryEntrySupplier<Item> MINERAL_PLATINUM = mineral(EnumMineralTier.PLATINUM);
    public static final RegistryEntrySupplier<Item> MINERAL_ORICHALCUM = mineral(EnumMineralTier.ORICHALCUM);
    public static final RegistryEntrySupplier<Item> MINERAL_DIAMOND = mineral(EnumMineralTier.DIAMOND);
    public static final RegistryEntrySupplier<Item> MINERAL_DRAGONIC = mineral(EnumMineralTier.DRAGONIC);
    public static final RegistryEntrySupplier<Item> MINERAL_AQUAMARINE = mineral(EnumMineralTier.AQUAMARINE);
    public static final RegistryEntrySupplier<Item> MINERAL_AMETHYST = mineral(EnumMineralTier.AMETHYST);
    public static final RegistryEntrySupplier<Item> MINERAL_RUBY = mineral(EnumMineralTier.RUBY);
    public static final RegistryEntrySupplier<Item> MINERAL_EMERALD = mineral(EnumMineralTier.EMERALD);
    public static final RegistryEntrySupplier<Item> MINERAL_SAPPHIRE = mineral(EnumMineralTier.SAPPHIRE);
    public static final RegistryEntrySupplier<Item> BROKEN_MINERAL_IRON = brokenMineral(EnumMineralTier.IRON);
    public static final RegistryEntrySupplier<Item> BROKEN_MINERAL_BRONZE = brokenMineral(EnumMineralTier.BRONZE);
    public static final RegistryEntrySupplier<Item> BROKEN_MINERAL_SILVER = brokenMineral(EnumMineralTier.SILVER);
    public static final RegistryEntrySupplier<Item> BROKEN_MINERAL_GOLD = brokenMineral(EnumMineralTier.GOLD);
    public static final RegistryEntrySupplier<Item> BROKEN_MINERAL_PLATINUM = brokenMineral(EnumMineralTier.PLATINUM);
    public static final RegistryEntrySupplier<Item> BROKEN_MINERAL_ORICHALCUM = brokenMineral(EnumMineralTier.ORICHALCUM);
    public static final RegistryEntrySupplier<Item> BROKEN_MINERAL_DIAMOND = brokenMineral(EnumMineralTier.DIAMOND);
    public static final RegistryEntrySupplier<Item> BROKEN_MINERAL_DRAGONIC = brokenMineral(EnumMineralTier.DRAGONIC);
    public static final RegistryEntrySupplier<Item> BROKEN_MINERAL_AQUAMARINE = brokenMineral(EnumMineralTier.AQUAMARINE);
    public static final RegistryEntrySupplier<Item> BROKEN_MINERAL_AMETHYST = brokenMineral(EnumMineralTier.AMETHYST);
    public static final RegistryEntrySupplier<Item> BROKEN_MINERAL_RUBY = brokenMineral(EnumMineralTier.RUBY);
    public static final RegistryEntrySupplier<Item> BROKEN_MINERAL_EMERALD = brokenMineral(EnumMineralTier.EMERALD);
    public static final RegistryEntrySupplier<Item> BROKEN_MINERAL_SAPPHIRE = brokenMineral(EnumMineralTier.SAPPHIRE);

    public static final RegistryEntrySupplier<Item> BRONZE = mat("bronze", Texture.Y);
    public static final RegistryEntrySupplier<Item> SILVER = mat("silver", Texture.Y);
    public static final RegistryEntrySupplier<Item> PLATINUM = mat("platinum", Texture.Y);
    public static final RegistryEntrySupplier<Item> ORICHALCUM = mat("orichalcum", Rarity.UNCOMMON, Texture.Y);
    public static final RegistryEntrySupplier<Item> DRAGONIC = mat("dragonic_stone", Rarity.UNCOMMON, Texture.Y);
    public static final RegistryEntrySupplier<Item> SCRAP = mat("scrap", Texture.Y);
    public static final RegistryEntrySupplier<Item> SCRAP_PLUS = mat("scrap_plus", Texture.Y);
    public static final RegistryEntrySupplier<Item> AMETHYST = mat("amethyst", Texture.Y);
    public static final RegistryEntrySupplier<Item> AQUAMARINE = mat("aquamarine", Texture.Y);
    public static final RegistryEntrySupplier<Item> RUBY = mat("ruby", Texture.Y);
    public static final RegistryEntrySupplier<Item> SAPPHIRE = mat("sapphire", Texture.Y);
    public static final RegistryEntrySupplier<Item> CORE_RED = mat("red_core", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> CORE_BLUE = mat("blue_core", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> CORE_YELLOW = mat("yellow_core", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> CORE_GREEN = mat("green_core", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> CRYSTAL_SKULL = mat("crystal_skull", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> CRYSTAL_WATER = mat("water_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item> CRYSTAL_EARTH = mat("earth_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item> CRYSTAL_FIRE = mat("fire_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item> CRYSTAL_WIND = mat("wind_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item> CRYSTAL_LIGHT = mat("light_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item> CRYSTAL_DARK = mat("dark_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item> CRYSTAL_LOVE = mat("love_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item> CRYSTAL_SMALL = mat("small_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item> CRYSTAL_BIG = mat("big_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item> CRYSTAL_MAGIC = mat("magic_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item> CRYSTAL_RUNE = mat("rune_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item> CRYSTAL_ELECTRO = mat("electro_crystal", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> STICK_THICK = mat("thick_stick", Texture.Y);
    public static final RegistryEntrySupplier<Item> HORN_INSECT = mat("insect_horn", Texture.Y);
    public static final RegistryEntrySupplier<Item> HORN_RIGID = mat("rigid_horn", Texture.Y);
    public static final RegistryEntrySupplier<Item> PLANT_STEM = mat("plant_stem", Texture.Y);
    public static final RegistryEntrySupplier<Item> HORN_BULL = mat("bulls_horn", Texture.Y);
    public static final RegistryEntrySupplier<Item> HORN_DEVIL = mat("devil_horn", Texture.Y);
    public static final RegistryEntrySupplier<Item> MOVING_BRANCH = mat("moving_branch", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> GLUE = mat("glue", Texture.Y);
    public static final RegistryEntrySupplier<Item> DEVIL_BLOOD = mat("devil_blood", Texture.Y);
    public static final RegistryEntrySupplier<Item> PARA_POISON = mat("paralysis_poison", Texture.Y);
    public static final RegistryEntrySupplier<Item> POISON_KING = mat("poison_king", Texture.Y);
    public static final RegistryEntrySupplier<Item> FEATHER_BLACK = mat("black_feather", Texture.Y);
    public static final RegistryEntrySupplier<Item> FEATHER_THUNDER = mat("thunder_feather", Texture.Y);
    public static final RegistryEntrySupplier<Item> FEATHER_YELLOW = mat("yellow_feather", Texture.Y);
    public static final RegistryEntrySupplier<Item> DRAGON_FIN = mat("dragon_fin", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> TURTLE_SHELL = mat("turtle_shell", Texture.Y);
    public static final RegistryEntrySupplier<Item> FISH_FOSSIL = mat("fish_fossil", Texture.Y);
    public static final RegistryEntrySupplier<Item> SKULL = mat("skull", Texture.Y);
    public static final RegistryEntrySupplier<Item> DRAGON_BONES = mat("dragon_bones", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> TORTOISE_SHELL = mat("black_tortoise_shell", Texture.Y);
    public static final RegistryEntrySupplier<Item> ROCK = mat("rock", Texture.Y);
    public static final RegistryEntrySupplier<Item> STONE_ROUND = mat("round_stone", Texture.Y);
    public static final RegistryEntrySupplier<Item> STONE_TINY = mat("tiny_golem_stone", Texture.Y);
    public static final RegistryEntrySupplier<Item> STONE_GOLEM = mat("golem_stone", Texture.Y);
    public static final RegistryEntrySupplier<Item> TABLET_GOLEM = mat("golem_tablet", Texture.Y);
    public static final RegistryEntrySupplier<Item> STONE_SPIRIT = mat("golem_spirit_stone", Texture.Y);
    public static final RegistryEntrySupplier<Item> TABLET_TRUTH = mat("tablet_of_truth", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> YARN = mat("yarn", Texture.Y);
    public static final RegistryEntrySupplier<Item> OLD_BANDAGE = mat("old_bandage", Texture.Y);
    public static final RegistryEntrySupplier<Item> AMBROSIAS_THORNS = mat("ambrosias_thorns", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> THREAD_SPIDER = mat("spider_thread", Texture.N);
    public static final RegistryEntrySupplier<Item> PUPPETRY_STRINGS = mat("puppetry_strings", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> VINE = mat("vine", Texture.Y);
    public static final RegistryEntrySupplier<Item> TAIL_SCORPION = mat("scorpion_tail", Texture.Y);
    public static final RegistryEntrySupplier<Item> STRONG_VINE = mat("strong_vine", Texture.N);
    public static final RegistryEntrySupplier<Item> THREAD_PRETTY = mat("pretty_thread", Texture.N);
    public static final RegistryEntrySupplier<Item> TAIL_CHIMERA = mat("chimera_tail", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> ARROW_HEAD = mat("arrowhead", Texture.Y);
    public static final RegistryEntrySupplier<Item> BLADE_SHARD = mat("blade_shard", Texture.Y);
    public static final RegistryEntrySupplier<Item> BROKEN_HILT = mat("broken_hilt", Texture.Y);
    public static final RegistryEntrySupplier<Item> BROKEN_BOX = mat("broken_box", Texture.Y);
    public static final RegistryEntrySupplier<Item> BLADE_GLISTENING = mat("glistening_blade", Texture.N);
    public static final RegistryEntrySupplier<Item> GREAT_HAMMER_SHARD = mat("great_hammer_shard", Texture.N);
    public static final RegistryEntrySupplier<Item> HAMMER_PIECE = mat("hammer_piece", Texture.N);
    public static final RegistryEntrySupplier<Item> SHOULDER_PIECE = mat("shoulder_piece", Texture.N);
    public static final RegistryEntrySupplier<Item> PIRATES_ARMOR = mat("pirates_armor", Texture.N);
    public static final RegistryEntrySupplier<Item> SCREW_RUSTY = mat("rusty_screw", Texture.N);
    public static final RegistryEntrySupplier<Item> SCREW_SHINY = mat("shiny_screw", Texture.N);
    public static final RegistryEntrySupplier<Item> ROCK_SHARD_LEFT = mat("left_rock_shard", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> ROCK_SHARD_RIGHT = mat("right_rock_shard", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> MTGUPlate = mat("mtgu_plate", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> BROKEN_ICE_WALL = mat("broken_ice_wall", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> FUR_SMALL = mat("fur_s", Texture.Y);
    public static final RegistryEntrySupplier<Item> FUR_MEDIUM = mat("fur_m", Texture.Y);
    public static final RegistryEntrySupplier<Item> FUR_LARGE = mat("fur_l", Texture.Y);
    public static final RegistryEntrySupplier<Item> FUR = mat("fur", Texture.Y);
    public static final RegistryEntrySupplier<Item> FURBALL = mat("wooly_furball", Texture.N);
    public static final RegistryEntrySupplier<Item> DOWN_YELLOW = mat("yellow_down", Texture.Y);
    public static final RegistryEntrySupplier<Item> FUR_QUALITY = mat("quality_puffy_fur", Texture.Y);
    public static final RegistryEntrySupplier<Item> DOWN_PENGUIN = mat("penguin_down", Texture.Y);
    public static final RegistryEntrySupplier<Item> LIGHTNING_MANE = mat("lightning_mane", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> FUR_RED_LION = mat("red_lion_fur", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> FUR_BLUE_LION = mat("blue_lion_fur", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> CHEST_HAIR = mat("chest_hair", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> SPORE = mat("spore", Texture.Y);
    public static final RegistryEntrySupplier<Item> POWDER_POISON = mat("poison_powder", Texture.Y);
    public static final RegistryEntrySupplier<Item> SPORE_HOLY = mat("holy_spore", Texture.N);
    public static final RegistryEntrySupplier<Item> FAIRY_DUST = mat("fairy_dust", Texture.Y);
    public static final RegistryEntrySupplier<Item> FAIRY_ELIXIR = mat("fairy_elixir", Texture.Y);
    public static final RegistryEntrySupplier<Item> ROOT = mat("root", Texture.Y);
    public static final RegistryEntrySupplier<Item> POWDER_MAGIC = mat("magic_powder", Texture.N);
    public static final RegistryEntrySupplier<Item> POWDER_MYSTERIOUS = mat("mysterious_powder", Texture.N);
    public static final RegistryEntrySupplier<Item> MAGIC = mat("magic", Texture.N);
    public static final RegistryEntrySupplier<Item> ASH_EARTH = mat("earth_dragon_ash", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> ASH_FIRE = mat("fire_dragon_ash", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> ASH_WATER = mat("water_dragon_ash", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> TURNIPS_MIRACLE = mat("turnips_miracle", Texture.N);
    public static final RegistryEntrySupplier<Item> MELODY_BOTTLE = mat("melody_bottle", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> CLOTH_CHEAP = mat("cheap_cloth", Texture.Y);
    public static final RegistryEntrySupplier<Item> CLOTH_QUALITY = mat("quality_cloth", Texture.Y);
    public static final RegistryEntrySupplier<Item> CLOTH_QUALITY_WORN = mat("quality_worn_cloth", Texture.Y);
    public static final RegistryEntrySupplier<Item> CLOTH_SILK = mat("silk_cloth", Texture.N);
    public static final RegistryEntrySupplier<Item> GHOST_HOOD = mat("ghost_hood", Texture.Y);
    public static final RegistryEntrySupplier<Item> GLOVE_GIANT = mat("giants_glove", Texture.Y);
    public static final RegistryEntrySupplier<Item> GLOVE_BLUE_GIANT = mat("blue_giants_glove", Texture.Y);
    public static final RegistryEntrySupplier<Item> CARAPACE_INSECT = mat("insect_carapace", Texture.Y);
    public static final RegistryEntrySupplier<Item> CARAPACE_PRETTY = mat("pretty_carapace", Texture.Y);
    public static final RegistryEntrySupplier<Item> CLOTH_ANCIENT_ORC = mat("ancient_orc_cloth", Texture.N);
    public static final RegistryEntrySupplier<Item> JAW_INSECT = mat("insect_jaw", Texture.Y);
    public static final RegistryEntrySupplier<Item> CLAW_PANTHER = mat("panther_claw", Texture.Y);
    public static final RegistryEntrySupplier<Item> CLAW_MAGIC = mat("magic_claw", Texture.Y);
    public static final RegistryEntrySupplier<Item> FANG_WOLF = mat("wolf_fang", Texture.Y);
    public static final RegistryEntrySupplier<Item> FANG_GOLD_WOLF = mat("gold_wolf_fang", Texture.Y);
    public static final RegistryEntrySupplier<Item> CLAW_PALM = mat("palm_claw", Texture.Y);
    public static final RegistryEntrySupplier<Item> CLAW_MALM = mat("malm_claw", Texture.Y);
    public static final RegistryEntrySupplier<Item> GIANTS_NAIL = mat("giants_nail", Texture.Y);
    public static final RegistryEntrySupplier<Item> CLAW_CHIMERA = mat("chimeras_claw", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> TUSK_IVORY = mat("ivory_tusk", Texture.N);
    public static final RegistryEntrySupplier<Item> TUSK_UNBROKEN_IVORY = mat("unbroken_tusk", Texture.N);
    public static final RegistryEntrySupplier<Item> SCORPION_PINCER = mat("scorpion_pincer", Texture.Y);
    public static final RegistryEntrySupplier<Item> DANGEROUS_SCISSORS = mat("dangerous_scissors", Texture.N);
    public static final RegistryEntrySupplier<Item> PROPELLOR_CHEAP = mat("cheap_propeller", Texture.N);
    public static final RegistryEntrySupplier<Item> PROPELLOR_QUALITY = mat("quality_propeller", Texture.N);
    public static final RegistryEntrySupplier<Item> FANG_DRAGON = mat("dragon_fang", Texture.N);
    public static final RegistryEntrySupplier<Item> JAW_QUEEN = mat("queens_jaw", Texture.Y);
    public static final RegistryEntrySupplier<Item> WIND_DRAGON_TOOTH = mat("wind_dragon_tooth", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> GIANTS_NAIL_BIG = mat("big_giants_nail", Texture.N);
    public static final RegistryEntrySupplier<Item> SCALE_WET = mat("wet_scale", Texture.Y);
    public static final RegistryEntrySupplier<Item> SCALE_GRIMOIRE = mat("grimoire_scale", Texture.Y);
    public static final RegistryEntrySupplier<Item> SCALE_DRAGON = mat("dragon_scale", Texture.Y);
    public static final RegistryEntrySupplier<Item> SCALE_CRIMSON = mat("crimson_scale", Texture.Y);
    public static final RegistryEntrySupplier<Item> SCALE_BLUE = mat("blue_scale", Texture.Y);
    public static final RegistryEntrySupplier<Item> SCALE_GLITTER = mat("glitter_scale", Texture.Y);
    public static final RegistryEntrySupplier<Item> SCALE_LOVE = mat("love_scale", Texture.Y);
    public static final RegistryEntrySupplier<Item> SCALE_BLACK = mat("black_scale", Texture.Y);
    public static final RegistryEntrySupplier<Item> SCALE_FIRE = mat("fire_wyrm_scale", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> SCALE_EARTH = mat("earth_wyrm_scale", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> SCALE_LEGEND = mat("legendary_scale", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> STEEL_DOUBLE = mat("double_steel", Texture.Y);
    public static final RegistryEntrySupplier<Item> STEEL_TEN = mat("ten_fold_steel", Texture.N);
    public static final RegistryEntrySupplier<Item> GLITTA_AUGITE = mat("glitta_augite", Texture.N);
    public static final RegistryEntrySupplier<Item> INVIS_STONE = mat("invisible_stone", Texture.Y);
    public static final RegistryEntrySupplier<Item> LIGHT_ORE = mat("light_ore", Texture.Y);
    public static final RegistryEntrySupplier<Item> RUNE_SPHERE_SHARD = mat("rune_sphere_shard", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> SHADE_STONE = mat("shade_stone", Texture.N);
    public static final RegistryEntrySupplier<Item> RACCOON_LEAF = mat("raccoon_leaf", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> ICY_NOSE = mat("icy_nose", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> BIG_BIRDS_COMB = mat("big_birds_comb", Texture.N);
    public static final RegistryEntrySupplier<Item> RAFFLESIA_PETAL = mat("rafflesia_petal", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> CURSED_DOLL = mat("cursed_doll", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item> WARRIORS_PROOF = mat("warriors_proof", Texture.Y);
    public static final RegistryEntrySupplier<Item> PROOF_OF_RANK = mat("proof_of_rank", Texture.Y);
    public static final RegistryEntrySupplier<Item> THRONE_OF_EMPIRE = mat("throne_of_emire", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item> WHITE_STONE = mat("white_stone", Texture.N);
    public static final RegistryEntrySupplier<Item> RARE_CAN = mat("rare_can", Rarity.UNCOMMON, Texture.N);
    public static final RegistryEntrySupplier<Item> CAN = mat("can", Texture.N);
    public static final RegistryEntrySupplier<Item> BOOTS = mat("boots", Texture.N);
    public static final RegistryEntrySupplier<Item> LAWN = mat("ayngondaia_lawn", Rarity.UNCOMMON, Texture.N);

    public static final RegistryEntrySupplier<Item> FIRE_BALL_SMALL = spell(() -> ModSpells.FIREBALL, "fireball");
    public static final RegistryEntrySupplier<Item> FIRE_BALL_BIG = spell(() -> ModSpells.BIG_FIREBALL, "fireball_big");
    public static final RegistryEntrySupplier<Item> EXPLOSION = spell(() -> ModSpells.EXPLOSION, "explosion");
    public static final RegistryEntrySupplier<Item> WATER_LASER = spell(() -> ModSpells.WATER_LASER, "water_laser", true);
    public static final RegistryEntrySupplier<Item> PARALLEL_LASER = spell(() -> ModSpells.PARALLEL_LASER, "parallel_laser", true);
    public static final RegistryEntrySupplier<Item> DELTA_LASER = spell(() -> ModSpells.DELTA_LASER, "delta_laser", true);
    public static final RegistryEntrySupplier<Item> SCREW_ROCK = spell(() -> ModSpells.SCREW_ROCK, "screw_rock");
    public static final RegistryEntrySupplier<Item> EARTH_SPIKE = spell(() -> ModSpells.EARTH_SPIKE, "earth_spike");
    public static final RegistryEntrySupplier<Item> AVENGER_ROCK = spell(() -> ModSpells.AVENGER_ROCK, "avenger_rock");
    public static final RegistryEntrySupplier<Item> SONIC_WIND = spell(() -> ModSpells.SONIC, "sonic_wind");
    public static final RegistryEntrySupplier<Item> DOUBLE_SONIC = spell(() -> ModSpells.DOUBLE_SONIC, "double_sonic");
    public static final RegistryEntrySupplier<Item> PENETRATE_SONIC = spell(() -> ModSpells.PENETRATE_SONIC, "penetrate_sonic");
    public static final RegistryEntrySupplier<Item> LIGHT_BARRIER = spell(() -> ModSpells.LIGHT_BARRIER, "light_barrier");
    public static final RegistryEntrySupplier<Item> SHINE = spell(() -> ModSpells.SHINE, "shine");
    public static final RegistryEntrySupplier<Item> PRISM = spell(() -> ModSpells.PRISM, "prism");
    public static final RegistryEntrySupplier<Item> DARK_SNAKE = spell(() -> ModSpells.DARK_SNAKE, "dark_snake");
    public static final RegistryEntrySupplier<Item> DARK_BALL = spell(() -> ModSpells.DARK_BALL, "dark_ball");
    public static final RegistryEntrySupplier<Item> DARKNESS = spell(() -> ModSpells.DARKNESS, "darkness");
    public static final RegistryEntrySupplier<Item> CURE = spell(() -> ModSpells.CURE, "cure");
    public static final RegistryEntrySupplier<Item> CURE_ALL = spell(() -> ModSpells.CURE_ALL, "cure_all");
    public static final RegistryEntrySupplier<Item> CURE_MASTER = spell(() -> ModSpells.MASTER_CURE, "cure_master");
    public static final RegistryEntrySupplier<Item> MEDI_POISON = spell(() -> ModSpells.MEDI_POISON, "medi_poison");
    public static final RegistryEntrySupplier<Item> MEDI_PARA = spell(() -> ModSpells.MEDI_PARA, "medi_paralysis");
    public static final RegistryEntrySupplier<Item> MEDI_SEAL = spell(() -> ModSpells.MEDI_SEAL, "medi_seal");
    public static final RegistryEntrySupplier<Item> GREETING = spell(() -> ModSpells.EMPTY, "greeting");
    public static final RegistryEntrySupplier<Item> POWER_WAVE = spell(() -> ModSpells.POWER_WAVE, "power_wave");
    public static final RegistryEntrySupplier<Item> DASH_SLASH = spell(() -> ModSpells.DASH_SLASH, "dash_slash");
    public static final RegistryEntrySupplier<Item> RUSH_ATTACK = spell(() -> ModSpells.RUSH_ATTACK, "rush_attack");
    public static final RegistryEntrySupplier<Item> ROUND_BREAK = spell(() -> ModSpells.ROUND_BREAK, "round_break");
    public static final RegistryEntrySupplier<Item> MIND_THRUST = spell(() -> ModSpells.MIND_THRUST, "mind_thrust");
    public static final RegistryEntrySupplier<Item> GUST = spell(() -> ModSpells.GUST, "gust");
    public static final RegistryEntrySupplier<Item> STORM = spell(() -> ModSpells.STORM, "storm");
    public static final RegistryEntrySupplier<Item> BLITZ = spell(() -> ModSpells.BLITZ, "blitz");
    public static final RegistryEntrySupplier<Item> TWIN_ATTACK = spell(() -> ModSpells.TWIN_ATTACK, "twin_attack");
    public static final RegistryEntrySupplier<Item> RAIL_STRIKE = spell(() -> ModSpells.RAIL_STRIKE, "rail_strike");
    public static final RegistryEntrySupplier<Item> WIND_SLASH = spell(() -> ModSpells.WIND_SLASH, "wind_slash");
    public static final RegistryEntrySupplier<Item> FLASH_STRIKE = spell(() -> ModSpells.FLASH_STRIKE, "flash_strike");
    public static final RegistryEntrySupplier<Item> NAIVE_BLADE = spell(() -> ModSpells.NAIVE_BLADE, "naive_blade");
    public static final RegistryEntrySupplier<Item> STEEL_HEART = spell(() -> ModSpells.STEEL_HEART, "steel_heart");
    public static final RegistryEntrySupplier<Item> DELTA_STRIKE = spell(() -> ModSpells.DELTA_STRIKE, "delta_strike");
    public static final RegistryEntrySupplier<Item> HURRICANE = spell(() -> ModSpells.HURRICANE, "hurricane");
    public static final RegistryEntrySupplier<Item> REAPER_SLASH = spell(() -> ModSpells.REAPER_SLASH, "reaper_slash");
    public static final RegistryEntrySupplier<Item> MILLION_STRIKE = spell(() -> ModSpells.MILLION_STRIKE, "million_strike");
    public static final RegistryEntrySupplier<Item> AXEL_DISASTER = spell(() -> ModSpells.AXEL_DISASTER, "axel_disaster");
    public static final RegistryEntrySupplier<Item> STARDUST_UPPER = spell(() -> ModSpells.STARDUST_UPPER, "stardust_upper");
    public static final RegistryEntrySupplier<Item> TORNADO_SWING = spell(() -> ModSpells.TORNADO_SWING, "tornado_swing");
    public static final RegistryEntrySupplier<Item> GRAND_IMPACT = spell(() -> ModSpells.GRAND_IMPACT, "grand_impact");
    public static final RegistryEntrySupplier<Item> GIGA_SWING = spell(() -> ModSpells.GIGA_SWING, "giga_swing");
    public static final RegistryEntrySupplier<Item> UPPER_CUT = spell(() -> ModSpells.UPPER_CUT, "upper_cut");
    public static final RegistryEntrySupplier<Item> DOUBLE_KICK = spell(() -> ModSpells.DOUBLE_KICK, "double_kick");
    public static final RegistryEntrySupplier<Item> STRAIGHT_PUNCH = spell(() -> ModSpells.STRAIGHT_PUNCH, "straight_punch");
    public static final RegistryEntrySupplier<Item> NEKO_DAMASHI = spell(() -> ModSpells.NEKO_DAMASHI, "neko_damashi");
    public static final RegistryEntrySupplier<Item> RUSH_PUNCH = spell(() -> ModSpells.RUSH_PUNCH, "rush_punch");
    public static final RegistryEntrySupplier<Item> CYCLONE = spell(() -> ModSpells.CYCLONE, "cyclone");
    public static final RegistryEntrySupplier<Item> RAPID_MOVE = spell(() -> ModSpells.RAPID_MOVE, "rapid_move");
    public static final RegistryEntrySupplier<Item> BONUS_CONCERTO = spell(() -> ModSpells.EMPTY, "bonus_concerto");
    public static final RegistryEntrySupplier<Item> STRIKING_MARCH = spell(() -> ModSpells.EMPTY, "striking_march");
    public static final RegistryEntrySupplier<Item> IRON_WALTZ = spell(() -> ModSpells.EMPTY, "iron_waltz");
    public static final RegistryEntrySupplier<Item> TELEPORT = spell(() -> ModSpells.TELEPORT, "teleport");

    public static final RegistryEntrySupplier<Item> ROCKFISH = fish("rockfish", Texture.N);
    public static final RegistryEntrySupplier<Item> SAND_FLOUNDER = fish("sand_flounder", Texture.N);
    public static final RegistryEntrySupplier<Item> POND_SMELT = fish("pond_smelt", Texture.N);
    public static final RegistryEntrySupplier<Item> LOBSTER = fish("lobster", Texture.N);
    public static final RegistryEntrySupplier<Item> LAMP_SQUID = fish("lamb_squid", Texture.N);
    public static final RegistryEntrySupplier<Item> CHERRY_SALMON = fish("cherry_salmon", Texture.N);
    public static final RegistryEntrySupplier<Item> FALL_FLOUNDER = fish("fall_flounder", Texture.N);
    public static final RegistryEntrySupplier<Item> GIRELLA = fish("girella", Texture.N);
    public static final RegistryEntrySupplier<Item> TUNA = fish("tuna", Texture.N);
    public static final RegistryEntrySupplier<Item> CRUCIAN_CARP = fish("crucian_carp", Texture.N);
    public static final RegistryEntrySupplier<Item> YELLOWTAIL = fish("yellowtail", Texture.N);
    public static final RegistryEntrySupplier<Item> BLOWFISH = fish("blowfish", Texture.N);
    public static final RegistryEntrySupplier<Item> FLOUNDER = fish("flounder", Texture.N);
    public static final RegistryEntrySupplier<Item> RAINBOW_TROUT = fish("rainbow_trout", Texture.N);
    public static final RegistryEntrySupplier<Item> LOVER_SNAPPER = fish("lover_snapper", Texture.N);
    public static final RegistryEntrySupplier<Item> SNAPPER = fish("snapper", Texture.N);
    public static final RegistryEntrySupplier<Item> SHRIMP = fish("shrimp", Texture.N);
    public static final RegistryEntrySupplier<Item> SUNSQUID = fish("sunsquid", Texture.N);
    public static final RegistryEntrySupplier<Item> PIKE = fish("pike", Texture.N);
    public static final RegistryEntrySupplier<Item> NEEDLEFISH = fish("needle_fish", Texture.N);
    public static final RegistryEntrySupplier<Item> MACKEREL = fish("mackerel", Texture.N);
    public static final RegistryEntrySupplier<Item> SALMON = fish("salmon", Texture.N);
    public static final RegistryEntrySupplier<Item> GIBELIO = fish("gibelio", Texture.N);
    public static final RegistryEntrySupplier<Item> TURBOT = fish("turbot", Texture.N);
    public static final RegistryEntrySupplier<Item> SKIPJACK = fish("skipjack", Texture.N);
    public static final RegistryEntrySupplier<Item> GLITTER_SNAPPER = fish("glitter_snapper", Texture.N);
    public static final RegistryEntrySupplier<Item> CHUB = fish("chub", Texture.N);
    public static final RegistryEntrySupplier<Item> CHAR_FISH = fish("char", Texture.N);
    public static final RegistryEntrySupplier<Item> SARDINE = fish("sardine", Texture.N);
    public static final RegistryEntrySupplier<Item> TAIMEN = fish("taimen", Texture.N);
    public static final RegistryEntrySupplier<Item> SQUID = fish("squid", Texture.N);
    public static final RegistryEntrySupplier<Item> MASU_TROUT = fish("masu_trout", Texture.N);

    public static final RegistryEntrySupplier<Item> TURNIP_SEEDS = seed("turnip", () -> ModBlocks.TURNIP);
    public static final RegistryEntrySupplier<Item> TURNIP_PINK_SEEDS = seed("turnip_pink", () -> ModBlocks.TURNIP_PINK);
    public static final RegistryEntrySupplier<Item> CABBAGE_SEEDS = seed("cabbage", () -> ModBlocks.CABBAGE);
    public static final RegistryEntrySupplier<Item> PINK_MELON_SEEDS = seed("pink_melon", () -> ModBlocks.PINK_MELON);
    public static final RegistryEntrySupplier<Item> HOT_HOT_SEEDS = seed("hot_hot_fruit", () -> ModBlocks.HOT_HOT_FRUIT);
    public static final RegistryEntrySupplier<Item> GOLD_TURNIP_SEEDS = seed("golden_turnip", () -> ModBlocks.GOLDEN_TURNIP);
    public static final RegistryEntrySupplier<Item> GOLD_POTATO_SEEDS = seed("golden_potato", () -> ModBlocks.GOLDEN_POTATO);
    public static final RegistryEntrySupplier<Item> GOLD_PUMPKIN_SEEDS = seed("golden_pumpkin", () -> ModBlocks.GOLDEN_PUMPKIN);
    public static final RegistryEntrySupplier<Item> GOLD_CABBAGE_SEEDS = seed("golden_cabbage", () -> ModBlocks.GOLDEN_CABBAGE);
    public static final RegistryEntrySupplier<Item> BOK_CHOY_SEEDS = seed("bok_choy", () -> ModBlocks.BOK_CHOY);
    public static final RegistryEntrySupplier<Item> LEEK_SEEDS = seed("leek", () -> ModBlocks.LEEK);
    public static final RegistryEntrySupplier<Item> RADISH_SEEDS = seed("radish", () -> ModBlocks.RADISH);
    public static final RegistryEntrySupplier<Item> GREEN_PEPPER_SEEDS = seed("green_pepper", () -> ModBlocks.GREEN_PEPPER);
    public static final RegistryEntrySupplier<Item> SPINACH_SEEDS = seed("spinach", () -> ModBlocks.SPINACH);
    public static final RegistryEntrySupplier<Item> YAM_SEEDS = seed("yam", () -> ModBlocks.YAM);
    public static final RegistryEntrySupplier<Item> EGGPLANT_SEEDS = seed("eggplant", () -> ModBlocks.EGGPLANT);
    public static final RegistryEntrySupplier<Item> PINEAPPLE_SEEDS = seed("pineapple", () -> ModBlocks.PINEAPPLE);
    public static final RegistryEntrySupplier<Item> PUMPKIN_SEEDS = seed("pumpkin", () -> ModBlocks.PUMPKIN);
    public static final RegistryEntrySupplier<Item> ONION_SEEDS = seed("onion", () -> ModBlocks.ONION);
    public static final RegistryEntrySupplier<Item> CORN_SEEDS = seed("corn", () -> ModBlocks.CORN);
    public static final RegistryEntrySupplier<Item> TOMATO_SEEDS = seed("tomato", () -> ModBlocks.TOMATO);
    public static final RegistryEntrySupplier<Item> STRAWBERRY_SEEDS = seed("strawberry", () -> ModBlocks.STRAWBERRY);
    public static final RegistryEntrySupplier<Item> CUCUMBER_SEEDS = seed("cucumber", () -> ModBlocks.CUCUMBER);
    public static final RegistryEntrySupplier<Item> FODDER_SEEDS = seed("fodder", () -> ModBlocks.FODDER);

    public static final RegistryEntrySupplier<Item> FODDER = mat("fodder", Texture.N);

    public static final RegistryEntrySupplier<Item> TURNIP = crop("turnip", false, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> TURNIP_GIANT = crop("tyrant_turnip", true, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> TURNIP_PINK = crop("turnip_pink", false, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> TURNIP_PINK_GIANT = crop("colossal_pink", true, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> CABBAGE = crop("cabbage", false, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> CABBAGE_GIANT = crop("king_cabbage", true, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> PINK_MELON = crop("pink_melon", false, Texture.Y, 1);
    public static final RegistryEntrySupplier<Item> PINK_MELON_GIANT = crop("conqueror_melon", true, Texture.Y, 1);
    public static final RegistryEntrySupplier<Item> PINEAPPLE = crop("pineapple", false, Texture.N, 1);
    public static final RegistryEntrySupplier<Item> PINEAPPLE_GIANT = crop("king_pineapple", true, Texture.N, 1);
    public static final RegistryEntrySupplier<Item> STRAWBERRY = crop("strawberry", false, Texture.N, 1);
    public static final RegistryEntrySupplier<Item> STRAWBERRY_GIANT = crop("sultan_strawberry", true, Texture.N, 1);
    public static final RegistryEntrySupplier<Item> GOLDEN_TURNIP = crop("golden_turnip", false, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> GOLDEN_TURNIP_GIANT = crop("golden_tyrant_turnip", true, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> GOLDEN_POTATO = crop("golden_potato", false, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> GOLDEN_POTATO_GIANT = crop("golden_prince_potato", true, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> GOLDEN_PUMPKIN = crop("golden_pumpkin", false, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> GOLDEN_PUMPKIN_GIANT = crop("golden_doom_pumpkin", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> GOLDEN_CABBAGE = crop("golden_cabbage", false, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> GOLDEN_CABBAGE_GIANT = crop("golden_king_cabbage", true, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> HOT_HOT_FRUIT = crop("hot_hot_fruit", false, Texture.N, 1);
    public static final RegistryEntrySupplier<Item> HOT_HOT_FRUIT_GIANT = crop("giant_hot_hot_fruit", true, Texture.N, 1);
    public static final RegistryEntrySupplier<Item> BOK_CHOY = crop("bok_choy", false, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> BOK_CHOY_GIANT = crop("boss_bok_choy", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> LEEK = crop("leek", false, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> LEEK_GIANT = crop("legendary_leek", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> RADISH = crop("radish", false, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> RADISH_GIANT = crop("noble_radish", true, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> SPINACH = crop("spinach", false, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> SPINACH_GIANT = crop("sovereign_spinach", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> GREEN_PEPPER = crop("green_pepper", false, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> GREEN_PEPPER_GIANT = crop("green_pepper_rex", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> YAM = crop("yam", false, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> YAM_GIANT = crop("lordly_yam", true, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> EGGPLANT = crop("eggplant", false, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> EGGPLANT_GIANT = crop("emperor_eggplant", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> TOMATO = crop("tomato", false, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> TOMATO_GIANT = crop("titan_tomato", true, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> CORN = crop("corn", false, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> CORN_GIANT = crop("gigant_corn", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> CUCUMBER = crop("cucumber", false, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> CUCUMBER_GIANT = crop("kaiser_cucumber", true, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> PUMPKIN = crop("pumpkin", false, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> PUMPKIN_GIANT = crop("doom_pumpkin", true, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> ONION = crop("onion", false, Texture.N, 0);
    public static final RegistryEntrySupplier<Item> ONION_GIANT = crop("ultra_onion", true, Texture.N, 0);

    public static final RegistryEntrySupplier<Item> POTATO_GIANT = crop("princely_potato", true, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item> CARROT_GIANT = crop("royal_carrot", true, Texture.Y, 0);

    public static final RegistryEntrySupplier<Item> TOYHERB_SEEDS = seed("toyherb", () -> ModBlocks.TOYHERB);
    public static final RegistryEntrySupplier<Item> MOONDROP_SEEDS = seed("moondrop_flower", () -> ModBlocks.MOONDROP_FLOWER);
    public static final RegistryEntrySupplier<Item> PINK_CAT_SEEDS = seed("pink_cat", () -> ModBlocks.PINK_CAT);
    public static final RegistryEntrySupplier<Item> CHARM_BLUE_SEEDS = seed("charm_blue", () -> ModBlocks.CHARM_BLUE);
    public static final RegistryEntrySupplier<Item> LAMP_GRASS_SEEDS = seed("lamp_grass", () -> ModBlocks.LAMP_GRASS);
    public static final RegistryEntrySupplier<Item> CHERRY_GRASS_SEEDS = seed("cherry_grass", () -> ModBlocks.CHERRY_GRASS);
    public static final RegistryEntrySupplier<Item> POM_POM_GRASS_SEEDS = seed("pom_pom_grass", () -> ModBlocks.POM_POM_GRASS);
    public static final RegistryEntrySupplier<Item> AUTUMN_GRASS_SEEDS = seed("autumn_grass", () -> ModBlocks.AUTUMN_GRASS);
    public static final RegistryEntrySupplier<Item> NOEL_GRASS_SEEDS = seed("noel_grass", () -> ModBlocks.NOEL_GRASS);
    public static final RegistryEntrySupplier<Item> FIREFLOWER_SEEDS = seed("fireflower", () -> ModBlocks.FIREFLOWER);
    public static final RegistryEntrySupplier<Item> FOUR_LEAF_CLOVER_SEEDS = seed("four_leaf_clover", () -> ModBlocks.FOUR_LEAF_CLOVER);
    public static final RegistryEntrySupplier<Item> IRONLEAF_SEEDS = seed("ironleaf", () -> ModBlocks.IRONLEAF);
    public static final RegistryEntrySupplier<Item> WHITE_CRYSTAL_SEEDS = seed("white_crystal", () -> ModBlocks.WHITE_CRYSTAL);
    public static final RegistryEntrySupplier<Item> RED_CRYSTAL_SEEDS = seed("red_crystal", () -> ModBlocks.RED_CRYSTAL);
    public static final RegistryEntrySupplier<Item> GREEN_CRYSTAL_SEEDS = seed("green_crystal", () -> ModBlocks.GREEN_CRYSTAL);
    public static final RegistryEntrySupplier<Item> BLUE_CRYSTAL_SEEDS = seed("blue_crystal", () -> ModBlocks.BLUE_CRYSTAL);
    public static final RegistryEntrySupplier<Item> EMERY_FLOWER_SEEDS = seed("emery_flower", () -> ModBlocks.EMERY_FLOWER);

    public static final RegistryEntrySupplier<Item> TOYHERB = crop("toyherb", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> TOYHERB_GIANT = crop("ultra_toyherb", true, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> MOONDROP_FLOWER = crop("moondrop_flower", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> MOONDROP_FLOWER_GIANT = crop("ultra_moondrop_flower", true, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> PINK_CAT = crop("pink_cat", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> PINK_CAT_GIANT = crop("king_pink_cat", true, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> CHARM_BLUE = crop("charm_blue", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> CHARM_BLUE_GIANT = crop("great_charm_blue", true, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> LAMP_GRASS = crop("lamp_grass", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> LAMP_GRASS_GIANT = crop("kaiser_lamp_grass", true, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> CHERRY_GRASS = crop("cherry_grass", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> CHERRY_GRASS_GIANT = crop("king_cherry_grass", true, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> POM_POM_GRASS = crop("pom_pom_grass", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> POM_POM_GRASS_GIANT = crop("king_pom_pom_grass", true, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> AUTUMN_GRASS = crop("autumn_grass", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> AUTUMN_GRASS_GIANT = crop("big_autumn_grass", true, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> NOEL_GRASS = crop("noel_grass", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> NOEL_GRASS_GIANT = crop("large_noel_grass", true, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> FIREFLOWER = crop("fireflower", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> FIREFLOWER_GIANT = crop("big_fireflower", true, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> FOUR_LEAF_CLOVER = crop("four_leaf_clover", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> FOUR_LEAF_CLOVER_GIANT = crop("great_four_leaf_clover", true, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> IRONLEAF = crop("ironleaf", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> IRONLEAF_GIANT = crop("super_ironleaf", true, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> WHITE_CRYSTAL = crop("white_crystal", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> WHITE_CRYSTAL_GIANT = crop("big_white_crystal", true, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> RED_CRYSTAL = crop("red_crystal", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> RED_CRYSTAL_GIANT = crop("big_red_crystal", true, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> GREEN_CRYSTAL = crop("green_crystal", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> GREEN_CRYSTAL_GIANT = crop("big_green_crystal", true, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> BLUE_CRYSTAL = crop("blue_crystal", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> BLUE_CRYSTAL_GIANT = crop("big_blue_crystal", true, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> EMERY_FLOWER = crop("emery_flower", false, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item> EMERY_FLOWER_GIANT = crop("great_emery_flower", true, Texture.Y, 2);

    public static final RegistryEntrySupplier<Item> SHIELD_SEEDS = seed("shield", () -> ModBlocks.SHIELD_CROP);
    public static final RegistryEntrySupplier<Item> SWORD_SEEDS = seed("sword", () -> ModBlocks.SWORD_CROP);
    public static final RegistryEntrySupplier<Item> DUNGEON_SEEDS = seed("dungeon", () -> ModBlocks.DUNGEON);

    public static final RegistryEntrySupplier<Item> APPLE_SAPLING = ITEMS.register("apple_sapling", () -> new ItemNameBlockItem(ModBlocks.APPLE_SAPLING.get(), new Item.Properties().tab(RFCreativeTabs.CROPS)));
    public static final RegistryEntrySupplier<Item> ORANGE_SAPLING = ITEMS.register("orange_sapling", () -> new ItemNameBlockItem(ModBlocks.ORANGE_SAPLING.get(), new Item.Properties().tab(RFCreativeTabs.CROPS)));
    public static final RegistryEntrySupplier<Item> GRAPE_SAPLING = ITEMS.register("grape_sapling", () -> new ItemNameBlockItem(ModBlocks.GRAPE_SAPLING.get(), new Item.Properties().tab(RFCreativeTabs.CROPS)));

    public static final RegistryEntrySupplier<Item> ROUNDOFF = medicine("roundoff", false);
    public static final RegistryEntrySupplier<Item> PARA_GONE = medicine("para_gone", false);
    public static final RegistryEntrySupplier<Item> COLD_MED = medicine("cold_medicine", false);
    public static final RegistryEntrySupplier<Item> ANTIDOTE = medicine("antidote_potion", false);
    public static final RegistryEntrySupplier<Item> RECOVERY_POTION = medicine("recovery_potion", true);
    public static final RegistryEntrySupplier<Item> HEALING_POTION = medicine("healing_potion", true);
    public static final RegistryEntrySupplier<Item> MYSTERY_POTION = medicine("mystery_potion", true);
    public static final RegistryEntrySupplier<Item> MAGICAL_POTION = medicine("magical_potion", true);
    public static final RegistryEntrySupplier<Item> INVINCIROID = drinkable("invinciroid");
    public static final RegistryEntrySupplier<Item> LOVE_POTION = drinkable("love_potion");
    public static final RegistryEntrySupplier<Item> FORMUADE = drinkable("formuade");
    public static final RegistryEntrySupplier<Item> objectX = ITEMS.register("object_x", () -> new ItemObjectX(new Item.Properties().food(FOOD_PROP).tab(RFCreativeTabs.MEDICINE)));

    public static final RegistryEntrySupplier<Item> ELLI_LEAVES = herb("elli_leaves", () -> ModBlocks.ELLI_LEAVES);
    public static final RegistryEntrySupplier<Item> WITHERED_GRASS = herb("withered_grass", () -> ModBlocks.WITHERED_GRASS);
    public static final RegistryEntrySupplier<Item> WEEDS = herb("weeds", () -> ModBlocks.WEEDS);
    public static final RegistryEntrySupplier<Item> WHITE_GRASS = herb("white_grass", () -> ModBlocks.WHITE_GRASS);
    public static final RegistryEntrySupplier<Item> INDIGO_GRASS = herb("indigo_grass", () -> ModBlocks.INDIGO_GRASS);
    public static final RegistryEntrySupplier<Item> PURPLE_GRASS = herb("purple_grass", () -> ModBlocks.PURPLE_GRASS);
    public static final RegistryEntrySupplier<Item> GREEN_GRASS = herb("green_grass", () -> ModBlocks.GREEN_GRASS);
    public static final RegistryEntrySupplier<Item> BLUE_GRASS = herb("blue_grass", () -> ModBlocks.BLUE_GRASS);
    public static final RegistryEntrySupplier<Item> YELLOW_GRASS = herb("yellow_grass", () -> ModBlocks.YELLOW_GRASS);
    public static final RegistryEntrySupplier<Item> RED_GRASS = herb("red_grass", () -> ModBlocks.RED_GRASS);
    public static final RegistryEntrySupplier<Item> ORANGE_GRASS = herb("orange_grass", () -> ModBlocks.ORANGE_GRASS);
    public static final RegistryEntrySupplier<Item> BLACK_GRASS = herb("black_grass", () -> ModBlocks.BLACK_GRASS);
    public static final RegistryEntrySupplier<Item> ANTIDOTE_GRASS = herb("antidote_grass", () -> ModBlocks.ANTIDOTE_GRASS);
    public static final RegistryEntrySupplier<Item> MEDICINAL_HERB = herb("medicinal_herb", () -> ModBlocks.MEDICINAL_HERB);
    public static final RegistryEntrySupplier<Item> BAMBOO_SPROUT = herb("bamboo_sprout", () -> ModBlocks.BAMBOO_SPROUT);
    public static final RegistryEntrySupplier<Item> MUSHROOM = ITEMS.register("mushroom", () -> new ItemMushroom(new Item.Properties().food(lowFoodProp).tab(RFCreativeTabs.FOOD)));
    public static final RegistryEntrySupplier<Item> MONARCH_MUSHROOM = ITEMS.register("monarch_mushroom", () -> new ItemMushroom(new Item.Properties().food(lowFoodProp).tab(RFCreativeTabs.FOOD)));

    public static final RegistryEntrySupplier<Item> RICE_FLOUR = food("rice_flour", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> CURRY_POWDER = food("curry_powder", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> OIL = drinkable("oil", Texture.Y, lowFoodProp);
    public static final RegistryEntrySupplier<Item> FLOUR = food("flour", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> HONEY = food("honey", Texture.N, FOOD_PROP);
    public static final RegistryEntrySupplier<Item> YOGURT = food("yogurt", Texture.N);
    public static final RegistryEntrySupplier<Item> CHEESE = food("cheese", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> MAYONNAISE = food("mayonnaise", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> eggL = food("egg_l", Texture.Y, lowFoodProp);
    public static final RegistryEntrySupplier<Item> eggM = food("egg_m", Texture.Y, lowFoodProp);
    public static final RegistryEntrySupplier<Item> eggS = food("egg_s", Texture.Y, lowFoodProp);
    public static final RegistryEntrySupplier<Item> milkL = drinkable("milk_l", Texture.Y, lowFoodProp);
    public static final RegistryEntrySupplier<Item> milkM = drinkable("milk_m", Texture.Y, lowFoodProp);
    public static final RegistryEntrySupplier<Item> milkS = drinkable("milk_s", Texture.Y, lowFoodProp);
    public static final RegistryEntrySupplier<Item> WINE = drinkable("wine", Texture.N, FOOD_PROP);
    public static final RegistryEntrySupplier<Item> CHOCOLATE = food("chocolate", Texture.N);
    public static final RegistryEntrySupplier<Item> RICE = food("rice", Texture.N, FOOD_PROP);
    public static final RegistryEntrySupplier<Item> TURNIP_HEAVEN = food("turnip_heaven", Texture.N);
    public static final RegistryEntrySupplier<Item> PICKLE_MIX = food("pickle_mix", Texture.N);
    public static final RegistryEntrySupplier<Item> SALMON_ONIGIRI = food("salmon_onigiri", Texture.N);
    public static final RegistryEntrySupplier<Item> BREAD = food("bread", Texture.N);
    public static final RegistryEntrySupplier<Item> ONIGIRI = food("onigiri", Texture.Y);
    public static final RegistryEntrySupplier<Item> RELAX_TEA_LEAVES = food("relax_tea_leaves", Texture.N);
    public static final RegistryEntrySupplier<Item> ICE_CREAM = food("ice_cream", Texture.N);
    public static final RegistryEntrySupplier<Item> RAISIN_BREAD = food("raisin_bread", Texture.N);
    public static final RegistryEntrySupplier<Item> BAMBOO_RICE = food("bamboo_rice", Texture.N);
    public static final RegistryEntrySupplier<Item> PICKLES = food("pickles", Texture.N);
    public static final RegistryEntrySupplier<Item> PICKLED_TURNIP = food("pickled_turnip", Texture.Y);
    public static final RegistryEntrySupplier<Item> FRUIT_SANDWICH = food("fruit_sandwich", Texture.N);
    public static final RegistryEntrySupplier<Item> SANDWICH = food("sandwich", Texture.N);
    public static final RegistryEntrySupplier<Item> SALAD = food("salad", Texture.N);
    public static final RegistryEntrySupplier<Item> DUMPLINGS = food("dumplings", Texture.N);
    public static final RegistryEntrySupplier<Item> PUMPKIN_FLAN = food("pumpkin_flan", Texture.N);
    public static final RegistryEntrySupplier<Item> FLAN = food("flan", Texture.Y);
    public static final RegistryEntrySupplier<Item> CHOCOLATE_SPONGE = food("chocolate_sponge", Texture.N);
    public static final RegistryEntrySupplier<Item> POUND_CAKE = food("pound_cake", Texture.N);
    public static final RegistryEntrySupplier<Item> STEAMED_GYOZA = food("steamed_gyoza", Texture.N);
    public static final RegistryEntrySupplier<Item> CURRY_MANJU = food("curry_manju", Texture.N);
    public static final RegistryEntrySupplier<Item> CHINESE_MANJU = food("chinese_manju", Texture.N);
    public static final RegistryEntrySupplier<Item> MEAT_DUMPLING = food("meat_dumpling", Texture.N);
    public static final RegistryEntrySupplier<Item> CHEESE_BREAD = food("cheese_bread", Texture.N);
    public static final RegistryEntrySupplier<Item> STEAMED_BREAD = food("steamed_bread", Texture.N);
    public static final RegistryEntrySupplier<Item> HOT_JUICE = drinkable("hot_juice", Texture.N, HIGH_FOOD_PROP);
    public static final RegistryEntrySupplier<Item> PRELUDETO_LOVE = drinkable("prelude_to_love", Texture.N, HIGH_FOOD_PROP);
    public static final RegistryEntrySupplier<Item> GOLD_JUICE = drinkable("gold_juice", Texture.N, HIGH_FOOD_PROP);
    public static final RegistryEntrySupplier<Item> BUTTER = food("butter", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> KETCHUP = drinkable("ketchup", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> MIXED_SMOOTHIE = drinkable("mixed_smoothie", Texture.N, HIGH_FOOD_PROP);
    public static final RegistryEntrySupplier<Item> MIXED_JUICE = drinkable("mixed_juice", Texture.N, HIGH_FOOD_PROP);
    public static final RegistryEntrySupplier<Item> VEGGIE_SMOOTHIE = drinkable("veggie_smoothie", Texture.N, HIGH_FOOD_PROP);
    public static final RegistryEntrySupplier<Item> VEGETABLE_JUICE = drinkable("vegetable_juice", Texture.N, HIGH_FOOD_PROP);
    public static final RegistryEntrySupplier<Item> FRUIT_SMOOTHIE = drinkable("fruit_smoothie", Texture.N, HIGH_FOOD_PROP);
    public static final RegistryEntrySupplier<Item> FRUIT_JUICE = drinkable("fruit_juice", Texture.N, HIGH_FOOD_PROP);
    public static final RegistryEntrySupplier<Item> STRAWBERRY_MILK = drinkable("strawberry_milk", Texture.N, HIGH_FOOD_PROP);
    public static final RegistryEntrySupplier<Item> APPLE_JUICE = drinkable("apple_juice", Texture.N, HIGH_FOOD_PROP);
    public static final RegistryEntrySupplier<Item> ORANGE_JUICE = drinkable("orange_juice", Texture.N, HIGH_FOOD_PROP);
    public static final RegistryEntrySupplier<Item> GRAPE_JUICE = drinkable("grape_juice", Texture.Y, HIGH_FOOD_PROP);
    public static final RegistryEntrySupplier<Item> TOMATO_JUICE = drinkable("tomato_juice", Texture.N, HIGH_FOOD_PROP);
    public static final RegistryEntrySupplier<Item> PINEAPPLE_JUICE = drinkable("pineapple_juice", Texture.N, HIGH_FOOD_PROP);
    public static final RegistryEntrySupplier<Item> APPLE_PIE = food("apple_pie", Texture.N);
    public static final RegistryEntrySupplier<Item> CHEESECAKE = food("cheesecake", Texture.N);
    public static final RegistryEntrySupplier<Item> CHOCOLATE_CAKE = food("chocolate_cake", Texture.N);
    public static final RegistryEntrySupplier<Item> CAKE = food("cake", Texture.N);
    public static final RegistryEntrySupplier<Item> CHOCO_COOKIE = food("choco_cookie", Texture.N);
    public static final RegistryEntrySupplier<Item> COOKIE = food("cookie", Texture.N);
    public static final RegistryEntrySupplier<Item> YAMOFTHE_AGES = food("yam_of_the_ages", Texture.N);
    public static final RegistryEntrySupplier<Item> SEAFOOD_GRATIN = food("seafood_gratin", Texture.N);
    public static final RegistryEntrySupplier<Item> GRATIN = food("gratin", Texture.N);
    public static final RegistryEntrySupplier<Item> SEAFOOD_DORIA = food("seafood_doria", Texture.N);
    public static final RegistryEntrySupplier<Item> DORIA = food("doria", Texture.N);
    public static final RegistryEntrySupplier<Item> SEAFOOD_PIZZA = food("seafood_pizza", Texture.N);
    public static final RegistryEntrySupplier<Item> PIZZA = food("pizza", Texture.N);
    public static final RegistryEntrySupplier<Item> BUTTER_ROLL = food("butter_roll", Texture.N);
    public static final RegistryEntrySupplier<Item> JAM_ROLL = food("jam_roll", Texture.N);
    public static final RegistryEntrySupplier<Item> TOAST = food("toast", Texture.N);
    public static final RegistryEntrySupplier<Item> SWEET_POTATO = food("sweet_potato", Texture.N);
    public static final RegistryEntrySupplier<Item> BAKED_ONIGIRI = food("baked_onigiri", Texture.N);
    public static final RegistryEntrySupplier<Item> CORN_ON_THE_COB = food("corn_on_the_cob", Texture.N);
    public static final RegistryEntrySupplier<Item> ROCKFISH_STEW = food("rockfish_stew", Texture.N);
    public static final RegistryEntrySupplier<Item> UNION_STEW = food("union_stew", Texture.N);
    public static final RegistryEntrySupplier<Item> GRILLED_MISO = food("grilled_miso", Texture.N);
    public static final RegistryEntrySupplier<Item> RELAX_TEA = food("relax_tea", Texture.N);
    public static final RegistryEntrySupplier<Item> ROYAL_CURRY = food("royal_curry", Texture.N);
    public static final RegistryEntrySupplier<Item> ULTIMATE_CURRY = food("ultimate_curry", Texture.N);
    public static final RegistryEntrySupplier<Item> CURRY_RICE = food("curry_rice", Texture.N);
    public static final RegistryEntrySupplier<Item> EGG_BOWL = food("egg_bowl", Texture.N);
    public static final RegistryEntrySupplier<Item> TEMPURA_BOWL = food("tempura_bowl", Texture.N);
    public static final RegistryEntrySupplier<Item> MILK_PORRIDGE = food("milk_porridge", Texture.N);
    public static final RegistryEntrySupplier<Item> RICE_PORRIDGE = food("rice_porridge", Texture.N);
    public static final RegistryEntrySupplier<Item> TEMPURA_UDON = food("tempura_udon", Texture.Y);
    public static final RegistryEntrySupplier<Item> CURRY_UDON = food("curry_udon", Texture.Y);
    public static final RegistryEntrySupplier<Item> UDON = food("udon", Texture.Y);
    public static final RegistryEntrySupplier<Item> CHEESE_FONDUE = food("cheese_fondue", Texture.N);
    public static final RegistryEntrySupplier<Item> MARMALADE = food("marmalade", Texture.N);
    public static final RegistryEntrySupplier<Item> GRAPE_JAM = food("grape_jam", Texture.N);
    public static final RegistryEntrySupplier<Item> APPLE_JAM = food("apple_jam", Texture.N);
    public static final RegistryEntrySupplier<Item> STRAWBERRY_JAM = food("strawberry_jam", Texture.N);
    public static final RegistryEntrySupplier<Item> BOILED_GYOZA = food("boiled_gyoza", Texture.N);
    public static final RegistryEntrySupplier<Item> GLAZED_YAM = food("glazed_yam", Texture.N);
    public static final RegistryEntrySupplier<Item> BOILED_EGG = food("boiled_egg", Texture.N);
    public static final RegistryEntrySupplier<Item> BOILED_SPINACH = food("boiled_spinach", Texture.N);
    public static final RegistryEntrySupplier<Item> BOILED_PUMPKIN = food("boiled_pumpkin", Texture.N);
    public static final RegistryEntrySupplier<Item> GRAPE_LIQUEUR = drinkable("grape_liqueur", Texture.N, HIGH_FOOD_PROP);
    public static final RegistryEntrySupplier<Item> HOT_CHOCOLATE = drinkable("hot_chocolate", Texture.Y, HIGH_FOOD_PROP);
    public static final RegistryEntrySupplier<Item> HOT_MILK = drinkable("hot_milk", Texture.Y, HIGH_FOOD_PROP);
    public static final RegistryEntrySupplier<Item> GRILLED_SAND_FLOUNDER = food("grilled_sand_flounder", Texture.N);
    public static final RegistryEntrySupplier<Item> GRILLED_SHRIMP = food("grilled_shrimp", Texture.N);
    public static final RegistryEntrySupplier<Item> GRILLED_LOBSTER = food("grilled_lobster", Texture.N);
    public static final RegistryEntrySupplier<Item> GRILLED_BLOWFISH = food("grilled_blowfish", Texture.N);
    public static final RegistryEntrySupplier<Item> GRILLED_LAMP_SQUID = food("grilled_lamp_squid", Texture.N);
    public static final RegistryEntrySupplier<Item> GRILLED_SUNSQUID = food("grilled_sunsquid", Texture.N);
    public static final RegistryEntrySupplier<Item> GRILLED_SQUID = food("grilled_squid", Texture.N);
    public static final RegistryEntrySupplier<Item> GRILLED_FALL_FLOUNDER = food("grilled_fall_flounder", Texture.N);
    public static final RegistryEntrySupplier<Item> GRILLED_TURBOT = food("grilled_turbot", Texture.N);
    public static final RegistryEntrySupplier<Item> GRILLED_FLOUNDER = food("grilled_flounder", Texture.N);
    public static final RegistryEntrySupplier<Item> SALTED_PIKE = food("salted_pike", Texture.N);
    public static final RegistryEntrySupplier<Item> GRILLED_NEEDLEFISH = food("grilled_needlefish", Texture.N);
    public static final RegistryEntrySupplier<Item> DRIED_SARDINES = food("dried_sardines", Texture.N);
    public static final RegistryEntrySupplier<Item> TUNA_TERIYAKI = food("tuna_teriyaki", Texture.N);
    public static final RegistryEntrySupplier<Item> SALTED_POND_SMELT = food("salted_pond_smelt", Texture.N);
    public static final RegistryEntrySupplier<Item> GRILLED_YELLOWTAIL = food("grilled_yellowtail", Texture.N);
    public static final RegistryEntrySupplier<Item> GRILLED_MACKEREL = food("grilled_mackerel", Texture.N);
    public static final RegistryEntrySupplier<Item> GRILLED_SKIPJACK = food("grilled_skipjack", Texture.N);
    public static final RegistryEntrySupplier<Item> GRILLED_LOVER_SNAPPER = food("grilled_lover_snapper", Texture.N);
    public static final RegistryEntrySupplier<Item> GRILLED_GLITTER_SNAPPER = food("grilled_glitter_snapper", Texture.N);
    public static final RegistryEntrySupplier<Item> GRILLED_GIRELLA = food("grilled_girella", Texture.N);
    public static final RegistryEntrySupplier<Item> GRILLED_SNAPPER = food("grilled_snapper", Texture.N);
    public static final RegistryEntrySupplier<Item> GRILLED_GIBELIO = food("grilled_gibelio", Texture.N);
    public static final RegistryEntrySupplier<Item> GRILLED_CRUCIAN_CARP = food("grilled_crucian_carp", Texture.N);
    public static final RegistryEntrySupplier<Item> SALTED_TAIMEN = food("salted_taimen", Texture.N);
    public static final RegistryEntrySupplier<Item> SALTED_SALMON = food("salted_salmon", Texture.N);
    public static final RegistryEntrySupplier<Item> SALTED_CHUB = food("salted_chub", Texture.N);
    public static final RegistryEntrySupplier<Item> SALTED_CHERRY_SALMON = food("salted_cherry_salmon", Texture.N);
    public static final RegistryEntrySupplier<Item> SALTED_RAINBOW_TROUT = food("salted_rainbow_trout", Texture.N);
    public static final RegistryEntrySupplier<Item> SALTED_CHAR = food("salted_char", Texture.N);
    public static final RegistryEntrySupplier<Item> SALTED_MASU_TROUT = food("salted_masu_trout", Texture.N);
    public static final RegistryEntrySupplier<Item> DRY_CURRY = food("dry_curry", Texture.N);
    public static final RegistryEntrySupplier<Item> RISOTTO = food("risotto", Texture.N);
    public static final RegistryEntrySupplier<Item> GYOZA = food("gyoza", Texture.N);
    public static final RegistryEntrySupplier<Item> PANCAKES = food("pancakes", Texture.N);
    public static final RegistryEntrySupplier<Item> TEMPURA = food("tempura", Texture.N);
    public static final RegistryEntrySupplier<Item> FRIED_UDON = food("fried_udon", Texture.N);
    public static final RegistryEntrySupplier<Item> DONUT = food("donut", Texture.N);
    public static final RegistryEntrySupplier<Item> FRENCH_TOAST = food("french_toast", Texture.N);
    public static final RegistryEntrySupplier<Item> CURRY_BREAD = food("curry_bread", Texture.N);
    public static final RegistryEntrySupplier<Item> BAKED_APPLE = food("baked_apple", Texture.Y);
    public static final RegistryEntrySupplier<Item> OMELET_RICE = food("omelet_rice", Texture.N);
    public static final RegistryEntrySupplier<Item> OMELET = food("omelet", Texture.N);
    public static final RegistryEntrySupplier<Item> FRIED_EGGS = food("fried_eggs", Texture.N);
    public static final RegistryEntrySupplier<Item> MISO_EGGPLANT = food("miso_eggplant", Texture.N);
    public static final RegistryEntrySupplier<Item> CORN_CEREAL = food("corn_cereal", Texture.N);
    public static final RegistryEntrySupplier<Item> POPCORN = food("popcorn", Texture.N);
    public static final RegistryEntrySupplier<Item> CROQUETTES = food("croquettes", Texture.N);
    public static final RegistryEntrySupplier<Item> FRENCH_FRIES = food("french_fries", Texture.N);
    public static final RegistryEntrySupplier<Item> CABBAGE_CAKES = food("cabbage_cakes", Texture.N);
    public static final RegistryEntrySupplier<Item> FRIED_RICE = food("fried_rice", Texture.N);
    public static final RegistryEntrySupplier<Item> FRIED_VEGGIES = food("fried_veggies", Texture.Y);
    public static final RegistryEntrySupplier<Item> SHRIMP_SASHIMI = food("shrimp_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> LOBSTER_SASHIMI = food("lobster_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> BLOWFISH_SASHIMI = food("blowfish_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> LAMP_SQUID_SASHIMI = food("lamp_squid_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> SUNSQUID_SASHIMI = food("sunsquid_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> SQUID_SASHIMI = food("squid_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> FALL_SASHIMI = food("fall_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> TURBOT_SASHIMI = food("turbot_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> FLOUNDER_SASHIMI = food("flounder_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> PIKE_SASHIMI = food("pike_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> NEEDLEFISH_SASHIMI = food("needlefish_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> SARDINE_SASHIMI = food("sardine_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> TUNA_SASHIMI = food("tuna_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> YELLOWTAIL_SASHIMI = food("yellowtail_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> SKIPJACK_SASHIMI = food("skipjack_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> GIRELLA_SASHIMI = food("girella_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> LOVER_SASHIMI = food("lover_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> GLITTER_SASHIMI = food("glitter_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> SNAPPER_SASHIMI = food("snapper_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> TAIMEN_SASHIMI = food("taimen_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> CHERRY_SASHIMI = food("cherry_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> SALMON_SASHIMI = food("salmon_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> RAINBOW_SASHIMI = food("rainbow_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> CHAR_SASHIMI = food("char_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> TROUT_SASHIMI = food("trout_sashimi", Texture.N);
    public static final RegistryEntrySupplier<Item> DISASTROUS_DISH = food("disastrous_dish", Texture.Y, lowFoodProp);
    public static final RegistryEntrySupplier<Item> FAILED_DISH = food("failed_dish", Texture.Y, lowFoodProp);
    public static final RegistryEntrySupplier<Item> MIXED_HERBS = food("mixed_herbs", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> SOUR_DROP = food("sour_drop", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> SWEET_POWDER = food("sweet_powder", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> HEAVY_SPICE = food("heavy_spice", Texture.N, lowFoodProp);
    public static final RegistryEntrySupplier<Item> ORANGE = food("orange", Texture.Y, lowFoodProp);
    public static final RegistryEntrySupplier<Item> GRAPES = food("grapes", Texture.Y, lowFoodProp);
    public static final RegistryEntrySupplier<Item> MEALY_APPLE = food("mealy_apple", Texture.N, lowFoodProp);

    public static final RegistryEntrySupplier<Item> FORGING_BREAD = ITEMS.register("forging_bread", () -> new ItemRecipeBread(EnumCrafting.FORGE, new Item.Properties().tab(RFCreativeTabs.FOOD).stacksTo(16)));
    public static final RegistryEntrySupplier<Item> ARMOR_BREAD = ITEMS.register("armory_bread", () -> new ItemRecipeBread(EnumCrafting.ARMOR, new Item.Properties().tab(RFCreativeTabs.FOOD).stacksTo(16)));
    public static final RegistryEntrySupplier<Item> CHEMISTRY_BREAD = ITEMS.register("chemistry_bread", () -> new ItemRecipeBread(EnumCrafting.CHEM, new Item.Properties().tab(RFCreativeTabs.FOOD).stacksTo(16)));
    public static final RegistryEntrySupplier<Item> COOKING_BREAD = ITEMS.register("cooking_bread", () -> new ItemRecipeBread(EnumCrafting.COOKING, new Item.Properties().tab(RFCreativeTabs.FOOD).stacksTo(16)));

    public static final RegistryEntrySupplier<Item> SHIPPING_BIN = blockItem("shipping_bin", () -> ModBlocks.SHIPPING);
    public static final RegistryEntrySupplier<Item> SPAWNER = blockItem("boss_spawner", () -> ModBlocks.BOSS_SPAWNER, RFCreativeTabs.MONSTERS);
    public static final RegistryEntrySupplier<Item> CASH_REGISTER = blockItem("cash_register", () -> ModBlocks.CASH_REGISTER);
    public static final RegistryEntrySupplier<Item> MONSTER_BARN = blockItem("monster_barn", () -> ModBlocks.MONSTER_BARN);
    public static final RegistryEntrySupplier<Item> QUEST_BOARD = ITEMS.register("quest_board", () -> new QuestBoardItem(ModBlocks.QUEST_BOARD.get(), new Item.Properties().tab(RFCreativeTabs.BLOCKS)));


    public static final RegistryEntrySupplier<Item> icon0 = ITEMS.register("icon_0", () -> new Item(new Item.Properties()));
    public static final RegistryEntrySupplier<Item> DEBUG = ITEMS.register("debug_item", () -> new ItemDebug(new Item.Properties()));
    public static final RegistryEntrySupplier<Item> LEVEL = ITEMS.register("level_item", () -> new ItemLevelUp(new Item.Properties()));
    public static final RegistryEntrySupplier<Item> SKILL = ITEMS.register("skill_item", () -> new ItemSkillUp(new Item.Properties()));
    public static final RegistryEntrySupplier<Item> TAME = ITEMS.register("insta_tame", () -> new Item(new Item.Properties()) {
        @Override
        public boolean isFoil(ItemStack stack) {
            return true;
        }
    });
    public static final RegistryEntrySupplier<Item> ENTITY_LEVEL = ITEMS.register("entity_level_item", () -> new ItemDebug(new Item.Properties()));
    public static final RegistryEntrySupplier<Item> UNKNOWN = ITEMS.register("unknown", () -> new Item(new Item.Properties()));
    public static final RegistryEntrySupplier<Item> ORC_MAZE = ITEMS.register("orc_maze", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryEntrySupplier<Item> STEEL_SWORD_PROP = ITEMS.register("steel_sword_prop", () -> new ItemProp(new Item.Properties().stacksTo(1), real -> new ItemStack(ModItems.STEEL_SWORD.get())));
    public static final RegistryEntrySupplier<Item> CUTLASS_PROP = ITEMS.register("cutlass_prop", () -> new ItemProp(new Item.Properties().stacksTo(1), real -> new ItemStack(ModItems.CUTLASS.get())));
    public static final RegistryEntrySupplier<Item> THIEF_KNIFE_PROP = ITEMS.register("thief_knife_prop", () -> new ItemProp(new Item.Properties().stacksTo(1), real -> new ItemStack(ModItems.THIEF_KNIFE.get())));

    public static RegistryEntrySupplier<Item> hoe(EnumToolTier tier) {
        RegistryEntrySupplier<Item> sup = ITEMS.register("hoe_" + tier.getName(), () -> new ItemToolHoe(tier, new Item.Properties().tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
        if (Platform.INSTANCE.isDatagen())
            DATAGENTAGS.computeIfAbsent(ModTags.HOES, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> wateringCan(EnumToolTier tier) {
        RegistryEntrySupplier<Item> sup = ITEMS.register("watering_can_" + tier.getName(), () -> new ItemToolWateringCan(tier, new Item.Properties().tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
        if (Platform.INSTANCE.isDatagen())
            DATAGENTAGS.computeIfAbsent(ModTags.WATERINGCANS, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> sickle(EnumToolTier tier) {
        RegistryEntrySupplier<Item> sup = ITEMS.register("sickle_" + tier.getName(), () -> new ItemToolSickle(tier, new Item.Properties().tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
        if (Platform.INSTANCE.isDatagen())
            DATAGENTAGS.computeIfAbsent(ModTags.SICKLES, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> hammerTool(EnumToolTier tier) {
        RegistryEntrySupplier<Item> sup = ITEMS.register("hammer_" + tier.getName(), () -> new ItemToolHammer(tier, new Item.Properties().tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
        if (Platform.INSTANCE.isDatagen())
            DATAGENTAGS.computeIfAbsent(ModTags.HAMMER_TOOLS, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> axeTool(EnumToolTier tier) {
        RegistryEntrySupplier<Item> sup = ITEMS.register("axe_" + tier.getName(), () -> new ItemToolAxe(tier, new Item.Properties().tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
        if (Platform.INSTANCE.isDatagen())
            DATAGENTAGS.computeIfAbsent(ModTags.AXE_TOOLS, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> fishingRod(EnumToolTier tier) {
        RegistryEntrySupplier<Item> sup = ITEMS.register("fishing_rod_" + tier.getName(), () -> new ItemToolFishingRod(tier, new Item.Properties().stacksTo(1).tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
        if (Platform.INSTANCE.isDatagen())
            DATAGENTAGS.computeIfAbsent(ModTags.FISHING_RODS, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> shortSword(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemShortSwordBase(new Item.Properties()));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemShortSwordBase(new Item.Properties().tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
        if (Platform.INSTANCE.isDatagen())
            DATAGENTAGS.computeIfAbsent(ModTags.SHORTSWORDS, t -> new ArrayList<>()).add(sup);
        if (Platform.INSTANCE.isDatagen())
            TIER_3_CHEST.add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> longSword(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemLongSwordBase(new Item.Properties()));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemLongSwordBase(new Item.Properties().tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
        if (Platform.INSTANCE.isDatagen())
            DATAGENTAGS.computeIfAbsent(ModTags.LONGSWORDS, t -> new ArrayList<>()).add(sup);
        if (Platform.INSTANCE.isDatagen())
            TIER_3_CHEST.add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> spear(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemSpearBase(new Item.Properties()));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemSpearBase(new Item.Properties().tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
        if (Platform.INSTANCE.isDatagen())
            DATAGENTAGS.computeIfAbsent(ModTags.SPEARS, t -> new ArrayList<>()).add(sup);
        if (Platform.INSTANCE.isDatagen())
            TIER_3_CHEST.add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> axe(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemAxeBase(new Item.Properties()));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemAxeBase(new Item.Properties().tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
        if (Platform.INSTANCE.isDatagen())
            DATAGENTAGS.computeIfAbsent(ModTags.AXES, t -> new ArrayList<>()).add(sup);
        if (Platform.INSTANCE.isDatagen())
            TIER_3_CHEST.add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> hammer(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemHammerBase(new Item.Properties()));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemHammerBase(new Item.Properties().tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
        if (Platform.INSTANCE.isDatagen())
            DATAGENTAGS.computeIfAbsent(ModTags.HAMMERS, t -> new ArrayList<>()).add(sup);
        if (Platform.INSTANCE.isDatagen())
            TIER_3_CHEST.add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> dualBlade(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemDualBladeBase(new Item.Properties()));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemDualBladeBase(new Item.Properties().tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
        if (Platform.INSTANCE.isDatagen())
            DATAGENTAGS.computeIfAbsent(ModTags.DUALBLADES, t -> new ArrayList<>()).add(sup);
        if (Platform.INSTANCE.isDatagen())
            TIER_3_CHEST.add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> gloves(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemGloveBase(new Item.Properties()));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemGloveBase(new Item.Properties().tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
        if (Platform.INSTANCE.isDatagen())
            DATAGENTAGS.computeIfAbsent(ModTags.FISTS, t -> new ArrayList<>()).add(sup);
        if (Platform.INSTANCE.isDatagen())
            TIER_3_CHEST.add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> staff(String name, EnumElement starterElement, int amount, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> Platform.INSTANCE.staff(starterElement, amount, new Item.Properties().stacksTo(1)));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> Platform.INSTANCE.staff(starterElement, amount, new Item.Properties().stacksTo(1).tab(RFCreativeTabs.WEAPON_TOOL_TAB)));
        if (Platform.INSTANCE.isDatagen())
            DATAGENTAGS.computeIfAbsent(ModTags.STAFFS, t -> new ArrayList<>()).add(sup);
        if (Platform.INSTANCE.isDatagen())
            TIER_3_CHEST.add(sup);
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
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> Platform.INSTANCE.armor(slot, new Item.Properties().tab(RFCreativeTabs.EQUIPMENT), new ResourceLocation(RuneCraftory.MODID, name), useItemTexture));
        if (Platform.INSTANCE.isDatagen()) {
            TIER_3_CHEST.add(sup);
            switch (slot) {
                case FEET -> DATAGENTAGS.computeIfAbsent(ModTags.BOOTS, t -> new ArrayList<>()).add(sup);
                case LEGS -> DATAGENTAGS.computeIfAbsent(ModTags.ACCESSORIES, t -> new ArrayList<>()).add(sup);
                case CHEST -> DATAGENTAGS.computeIfAbsent(ModTags.CHESTPLATE, t -> new ArrayList<>()).add(sup);
                case HEAD -> DATAGENTAGS.computeIfAbsent(ModTags.HELMET, t -> new ArrayList<>()).add(sup);
            }
        }
        return sup;
    }

    public static RegistryEntrySupplier<Item> shield(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemStatShield(new Item.Properties().stacksTo(1)));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemStatShield(new Item.Properties().stacksTo(1).tab(RFCreativeTabs.EQUIPMENT)));
        if (Platform.INSTANCE.isDatagen())
            TIER_3_CHEST.add(sup);
        if (Platform.INSTANCE.isDatagen())
            DATAGENTAGS.computeIfAbsent(ModTags.SHIELDS, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> blockItem(String name, Supplier<Supplier<Block>> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get().get(), new Item.Properties().tab(RFCreativeTabs.BLOCKS)));
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
        Supplier<Block> block = () -> ModBlocks.MINERAL_MAP.get(tier).get();
        return ITEMS.register("ore_" + tier.getSerializedName(), () -> new BlockItem(block.get(), new Item.Properties().tab(RFCreativeTabs.BLOCKS)));
    }

    public static RegistryEntrySupplier<Item> brokenMineral(EnumMineralTier tier) {
        Supplier<Block> block = () -> ModBlocks.BROKEN_MINERAL_MAP.get(tier).get();
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
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new Item(new Item.Properties().rarity(rarity).tab(RFCreativeTabs.UPGRADE_ITEMS)));
        if (rarity == Rarity.COMMON)
            if (Platform.INSTANCE.isDatagen())
                TIER_1_CHEST.add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> medicine(String name, boolean affectStats) {
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new ItemMedicine(affectStats, new Item.Properties().food(FOOD_PROP).stacksTo(16).tab(RFCreativeTabs.MEDICINE)));
        if (Platform.INSTANCE.isDatagen())
            TIER_2_CHEST.add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> drinkable(String name) {
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new Item(new Item.Properties().food(FOOD_PROP).stacksTo(16).tab(RFCreativeTabs.MEDICINE)) {
            @Override
            public UseAnim getUseAnimation(ItemStack stack) {
                return UseAnim.DRINK;
            }
        });
        if (Platform.INSTANCE.isDatagen())
            TIER_2_CHEST.add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> spell(Supplier<Supplier<Spell>> sup, String name) {
        return spell(sup, name, false);
    }

    public static RegistryEntrySupplier<Item> spell(Supplier<Supplier<Spell>> sup, String name, boolean canHold) {
        RegistryEntrySupplier<Item> ret = ITEMS.register(name, () -> canHold ?
                new ItemHoldSpell(sup.get(), new Item.Properties().stacksTo(1).tab(RFCreativeTabs.CAST)) :
                new ItemSpell(sup.get(), new Item.Properties().stacksTo(1).tab(RFCreativeTabs.CAST)));
        if (Platform.INSTANCE.isDatagen())
            TIER_2_CHEST.add(ret);
        if (Platform.INSTANCE.isDatagen())
            DATAGENTAGS.computeIfAbsent(ModTags.SPELLS, t -> new ArrayList<>()).add(ret);
        return ret;
    }

    public static RegistryEntrySupplier<Item> fish(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new Item(new Item.Properties()));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new Item(new Item.Properties().tab(RFCreativeTabs.FOOD)));
        if (Platform.INSTANCE.isDatagen())
            TIER_1_CHEST.add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> seed(String name, Supplier<Supplier<Block>> block) {
        RegistryEntrySupplier<Item> sup = ITEMS.register(name + "_seeds", () -> new ItemNameBlockItem(block.get().get(), new Item.Properties().tab(RFCreativeTabs.CROPS)));
        if (Platform.INSTANCE.isDatagen())
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
                sup = ITEMS.register(name, () -> new ItemGiantCrops(new Item.Properties().food(FOOD_PROP)));
            else
                sup = ITEMS.register(name, () -> new Item(new Item.Properties().food(FOOD_PROP)));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup;
        if (giant)
            sup = ITEMS.register(name, () -> new ItemGiantCrops(new Item.Properties().food(FOOD_PROP).tab(RFCreativeTabs.CROPS)));
        else
            sup = ITEMS.register(name, () -> new Item(new Item.Properties().food(FOOD_PROP).tab(RFCreativeTabs.CROPS)));
        if (Platform.INSTANCE.isDatagen())
            TIER_1_CHEST.add(sup);
        if (Platform.INSTANCE.isDatagen()) {
            switch (type) {
                case 0 -> VEGGIES.add(sup);
                case 1 -> FRUITS.add(sup);
                case 2 -> FLOWERS.add(sup);
            }
        }
        return sup;
    }

    public static RegistryEntrySupplier<Item> herb(String name, Supplier<Supplier<Block>> block) {
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new BlockItem(block.get().get(), new Item.Properties().food(lowFoodProp).tab(RFCreativeTabs.MEDICINE)));
        if (Platform.INSTANCE.isDatagen())
            TIER_1_CHEST.add(sup);
        return sup;
    }

    public static RegistryEntrySupplier<Item> food(String name, Texture texture) {
        return food(name, texture, HIGH_FOOD_PROP);
    }

    public static RegistryEntrySupplier<Item> food(String name, Texture texture, FoodProperties foodProp) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new Item(new Item.Properties().food(foodProp)));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new Item(new Item.Properties().food(foodProp).tab(RFCreativeTabs.FOOD)));
        if (Platform.INSTANCE.isDatagen())
            TIER_2_CHEST.add(sup);
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
        RegistryEntrySupplier<Item> sup = ITEMS.register(name, () -> new Item(new Item.Properties().food(foodProp).tab(RFCreativeTabs.FOOD)) {
            @Override
            public UseAnim getUseAnimation(ItemStack stack) {
                return UseAnim.DRINK;
            }
        });
        if (Platform.INSTANCE.isDatagen())
            TIER_2_CHEST.add(sup);
        return sup;
    }

    public static List<RegistryEntrySupplier<Item>> ribbons() {
        return List.of(ModItems.BLUE_RIBBON, ModItems.GREEN_RIBBON, ModItems.PURPLE_RIBBON, ModItems.BLACK_RIBBON,
                ModItems.YELLOW_RIBBON, ModItems.RED_RIBBON, ModItems.ORANGE_RIBBON, ModItems.WHITE_RIBBON,
                ModItems.INDIGO_RIBBON);
    }

    //Here till all items have a texture
    public enum Texture {
        Y,
        N
    }
}
