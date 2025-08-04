package io.github.flemmli97.runecraftory.common.registry;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.blocks.util.MineralBlockTier;
import io.github.flemmli97.runecraftory.common.components.AttackActionData;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.items.BabySpawnEgg;
import io.github.flemmli97.runecraftory.common.items.CraftingBlockItem;
import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.items.QuestBoardItem;
import io.github.flemmli97.runecraftory.common.items.ToolItemTier;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemMedicine;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemMushroom;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemObjectX;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemRecipeBread;
import io.github.flemmli97.runecraftory.common.items.creative.ItemDebug;
import io.github.flemmli97.runecraftory.common.items.creative.ItemLevelUp;
import io.github.flemmli97.runecraftory.common.items.creative.ItemProp;
import io.github.flemmli97.runecraftory.common.items.creative.ItemSkillUp;
import io.github.flemmli97.runecraftory.common.items.equipment.ItemArmorBase;
import io.github.flemmli97.runecraftory.common.items.tools.ItemCommandStaff;
import io.github.flemmli97.runecraftory.common.items.tools.ItemFertilizer;
import io.github.flemmli97.runecraftory.common.items.tools.ItemStatIncrease;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolAxe;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolFishingRod;
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
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.recipes.CraftingType;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.TenshiLibCrossPlat;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class RuneCraftoryItems {

    public static final LoaderRegister<Item> ITEMS = LoaderRegistryAccess.INSTANCE.of(Registries.ITEM, RuneCraftory.MODID);

    //Here till all items have textures
    public static final List<RegistryEntrySupplier<Item, ?>> NOTEX = new ArrayList<>();
    //Those collections are for datagen
    public static final Map<TagKey<Item>, List<RegistryEntrySupplier<Item, ?>>> DATAGENTAGS = new HashMap<>();
    public static final List<RegistryEntrySupplier<Item, ?>> SEEDS = new ArrayList<>();
    public static final List<Pair<String, RegistryEntrySupplier<Item, ?>>> VEGGIES = new ArrayList<>();
    public static final List<Pair<String, RegistryEntrySupplier<Item, ?>>> FRUITS = new ArrayList<>();
    public static final List<Pair<String, RegistryEntrySupplier<Item, ?>>> FLOWERS = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Item, ?>> GIANT_CROPS = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Item, ?>> FOOD = new ArrayList<>();

    public static final List<RegistryEntrySupplier<Item, ?>> TIER_1_CHEST = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Item, ?>> TIER_2_CHEST = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Item, ?>> TIER_3_CHEST = new ArrayList<>();
    public static final List<RegistryEntrySupplier<Item, ?>> TIER_4_CHEST = new ArrayList<>();

    private static final FoodProperties LOW_FOOD_PROP = new FoodProperties.Builder().nutrition(1).saturationModifier(0.5f).alwaysEdible().build();
    private static final FoodProperties FOOD_PROP = new FoodProperties.Builder().nutrition(2).saturationModifier(0.5f).alwaysEdible().build();
    private static final FoodProperties HIGH_FOOD_PROP = new FoodProperties.Builder().nutrition(6).saturationModifier(0.75f).alwaysEdible().build();

    private static final FoodProperties GIANT_CROP_FOOD_PROP = new FoodProperties(2, 0.5f, true, 2.4f, Optional.empty(), List.of());

    public static final RegistryEntrySupplier<Item, ItemToolHoe> HOE_SCRAP = hoe(ToolItemTier.SCRAP);
    public static final RegistryEntrySupplier<Item, ItemToolHoe> HOE_IRON = hoe(ToolItemTier.IRON);
    public static final RegistryEntrySupplier<Item, ItemToolHoe> HOE_SILVER = hoe(ToolItemTier.SILVER);
    public static final RegistryEntrySupplier<Item, ItemToolHoe> HOE_GOLD = hoe(ToolItemTier.GOLD);
    public static final RegistryEntrySupplier<Item, ItemToolHoe> HOE_PLATINUM = hoe(ToolItemTier.PLATINUM);
    public static final RegistryEntrySupplier<Item, ItemToolWateringCan> WATERING_CAN_SCRAP = wateringCan(ToolItemTier.SCRAP);
    public static final RegistryEntrySupplier<Item, ItemToolWateringCan> WATERING_CAN_IRON = wateringCan(ToolItemTier.IRON);
    public static final RegistryEntrySupplier<Item, ItemToolWateringCan> WATERING_CAN_SILVER = wateringCan(ToolItemTier.SILVER);
    public static final RegistryEntrySupplier<Item, ItemToolWateringCan> WATERING_CAN_GOLD = wateringCan(ToolItemTier.GOLD);
    public static final RegistryEntrySupplier<Item, ItemToolWateringCan> WATERING_CAN_PLATINUM = wateringCan(ToolItemTier.PLATINUM);
    public static final RegistryEntrySupplier<Item, ItemToolSickle> SICKLE_SCRAP = sickle(ToolItemTier.SCRAP);
    public static final RegistryEntrySupplier<Item, ItemToolSickle> SICKLE_IRON = sickle(ToolItemTier.IRON);
    public static final RegistryEntrySupplier<Item, ItemToolSickle> SICKLE_SILVER = sickle(ToolItemTier.SILVER);
    public static final RegistryEntrySupplier<Item, ItemToolSickle> SICKLE_GOLD = sickle(ToolItemTier.GOLD);
    public static final RegistryEntrySupplier<Item, ItemToolSickle> SICKLE_PLATINUM = sickle(ToolItemTier.PLATINUM);
    public static final RegistryEntrySupplier<Item, ItemToolHammer> HAMMER_SCRAP = hammerTool(ToolItemTier.SCRAP);
    public static final RegistryEntrySupplier<Item, ItemToolHammer> HAMMER_IRON = hammerTool(ToolItemTier.IRON);
    public static final RegistryEntrySupplier<Item, ItemToolHammer> HAMMER_SILVER = hammerTool(ToolItemTier.SILVER);
    public static final RegistryEntrySupplier<Item, ItemToolHammer> HAMMER_GOLD = hammerTool(ToolItemTier.GOLD);
    public static final RegistryEntrySupplier<Item, ItemToolHammer> HAMMER_PLATINUM = hammerTool(ToolItemTier.PLATINUM);
    public static final RegistryEntrySupplier<Item, ItemToolAxe> AXE_SCRAP = axeTool(ToolItemTier.SCRAP);
    public static final RegistryEntrySupplier<Item, ItemToolAxe> AXE_IRON = axeTool(ToolItemTier.IRON);
    public static final RegistryEntrySupplier<Item, ItemToolAxe> AXE_SILVER = axeTool(ToolItemTier.SILVER);
    public static final RegistryEntrySupplier<Item, ItemToolAxe> AXE_GOLD = axeTool(ToolItemTier.GOLD);
    public static final RegistryEntrySupplier<Item, ItemToolAxe> AXE_PLATINUM = axeTool(ToolItemTier.PLATINUM);
    public static final RegistryEntrySupplier<Item, ItemToolFishingRod> FISHING_ROD_SCRAP = fishingRod(ToolItemTier.SCRAP);
    public static final RegistryEntrySupplier<Item, ItemToolFishingRod> FISHING_ROD_IRON = fishingRod(ToolItemTier.IRON);
    public static final RegistryEntrySupplier<Item, ItemToolFishingRod> FISHING_ROD_SILVER = fishingRod(ToolItemTier.SILVER);
    public static final RegistryEntrySupplier<Item, ItemToolFishingRod> FISHING_ROD_GOLD = fishingRod(ToolItemTier.GOLD);
    public static final RegistryEntrySupplier<Item, ItemToolFishingRod> FISHING_ROD_PLATINUM = fishingRod(ToolItemTier.PLATINUM);
    public static final RegistryEntrySupplier<Item, ItemCommandStaff> MOB_STAFF = register("monster_command_staff", () -> new ItemCommandStaff(new Item.Properties().stacksTo(1)), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
    public static final RegistryEntrySupplier<Item, Item> BRUSH = register("brush", () -> new Item(new Item.Properties().stacksTo(1)), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
    public static final RegistryEntrySupplier<Item, Item> GLASS = register("magnifying_glass", () -> new Item(new Item.Properties().stacksTo(1)
            .component(RuneCraftoryDataComponentTypes.MAGNIFYING_GLASS.get(), Unit.INSTANCE)), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);

    public static final RegistryEntrySupplier<Item, ItemStatIncrease> LEVELISER = register("leveliser", () -> new ItemStatIncrease(ItemStatIncrease.Stat.LEVEL, new Item.Properties()), RuneCraftoryCreativeTabs.MEDICINE);
    public static final RegistryEntrySupplier<Item, ItemStatIncrease> HEART_DRINK = register("heart_drink", () -> new ItemStatIncrease(ItemStatIncrease.Stat.HP, new Item.Properties()), RuneCraftoryCreativeTabs.MEDICINE);
    public static final RegistryEntrySupplier<Item, ItemStatIncrease> VITAL_GUMMI = register("vital_gummi", () -> new ItemStatIncrease(ItemStatIncrease.Stat.VIT, new Item.Properties()), RuneCraftoryCreativeTabs.MEDICINE);
    public static final RegistryEntrySupplier<Item, ItemStatIncrease> INTELLIGENCER = register("intelligencer", () -> new ItemStatIncrease(ItemStatIncrease.Stat.INT, new Item.Properties()), RuneCraftoryCreativeTabs.MEDICINE);
    public static final RegistryEntrySupplier<Item, ItemStatIncrease> PROTEIN = register("protein", () -> new ItemStatIncrease(ItemStatIncrease.Stat.STR, new Item.Properties()), RuneCraftoryCreativeTabs.MEDICINE);
    public static final RegistryEntrySupplier<Item, ItemFertilizer> FORMULAR_A = register("formular_a", () -> new ItemFertilizer(ItemFertilizer.FORMULAR_A, new Item.Properties()), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
    public static final RegistryEntrySupplier<Item, ItemFertilizer> FORMULAR_B = register("formular_b", () -> new ItemFertilizer(ItemFertilizer.FORMULAR_B, new Item.Properties()), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
    public static final RegistryEntrySupplier<Item, ItemFertilizer> FORMULAR_C = register("formular_c", () -> new ItemFertilizer(ItemFertilizer.FORMULAR_C, new Item.Properties()), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
    public static final RegistryEntrySupplier<Item, ItemFertilizer> MINIMIZER = register("minimizer", () -> new ItemFertilizer(ItemFertilizer.MINIMIZER, new Item.Properties()), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
    public static final RegistryEntrySupplier<Item, ItemFertilizer> GIANTIZER = register("giantizer", () -> new ItemFertilizer(ItemFertilizer.GIANTIZER, new Item.Properties()), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
    public static final RegistryEntrySupplier<Item, ItemFertilizer> GREENIFIER = register("greenifier", () -> new ItemFertilizer(ItemFertilizer.GREENIFIER, new Item.Properties()), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
    public static final RegistryEntrySupplier<Item, ItemFertilizer> GREENIFIER_PLUS = register("greenifier_plus", () -> new ItemFertilizer(ItemFertilizer.GREENIFIER_PLUS, new Item.Properties()), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
    public static final RegistryEntrySupplier<Item, ItemFertilizer> WETTABLE_POWDER = register("wettable_powder", () -> new ItemFertilizer(ItemFertilizer.WETTABLE, new Item.Properties()), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);

    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> BROAD_SWORD = shortSword("broad_sword", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> STEEL_SWORD = shortSword("steel_sword", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> STEEL_SWORD_PLUS = shortSword("steel_sword_plus", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> CUTLASS = shortSword("cutlass", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> AQUA_SWORD = shortSword("aqua_sword", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> INVISI_BLADE = shortSword("invisiblade", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> DEFENDER = shortSword("defender", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> BURNING_SWORD = shortSword("burning_sword", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> GORGEOUS_SWORD = shortSword("gorgeous_sword", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> GAIA_SWORD = shortSword("gaia_sword", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> SNAKE_SWORD = shortSword("snake_sword", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> LUCK_BLADE = shortSword("luck_blade", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> PLATINUM_SWORD = shortSword("platinum_sword", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> WIND_SWORD = shortSword("wind_sword", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> CHAOS_BLADE = shortSword("chaos_blade", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> SAKURA = shortSword("sakura", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> SUNSPOT = shortSword("sunspot", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> DURENDAL = shortSword("durendal", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> AERIAL_BLADE = shortSword("aerial_blade", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> GRANTALE = shortSword("grantale", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> SMASH_BLADE = shortSword("smash_blade", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> ICIFIER = shortSword("icifier", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> SOUL_EATER = shortSword("soul_eater", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> RAVENTINE = shortSword("raventine", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> STAR_SABER = shortSword("star_saber", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> PLATINUM_SWORD_PLUS = shortSword("platinum_sword_plus", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> DRAGON_SLAYER = shortSword("dragon_slayer", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> RUNE_BLADE = shortSword("rune_blade", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> GLADIUS = shortSword("gladius", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> RUNE_LEGEND = shortSword("rune_legend", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> BACK_SCRATCHER = shortSword("back_scratcher", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> SPOON = shortSword("spoon", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> VEGGIE_BLADE = shortSword("veggie_blade", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemShortSwordBase> PLANT_SWORD = register("plant_sword", () -> new ItemShortSwordBase(new Item.Properties()), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);

    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> CLAYMORE = longSword("claymore", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> ZWEIHAENDER = longSword("zweihaender", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> ZWEIHAENDER_PLUS = longSword("zweihaender_plus", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> GREAT_SWORD = longSword("great_sword", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> SEA_CUTTER = longSword("sea_cutter", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> CYCLONE_BLADE = longSword("cyclone_blade", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> POISON_BLADE = longSword("poison_blade", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> KATZBALGER = longSword("katzbalger", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> EARTH_SHADE = longSword("earth_shade", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> BIG_KNIFE = longSword("big_knife", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> KATANA = longSword("katana", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> FLAME_SABER = longSword("flame_saber", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> BIO_SMASHER = longSword("bio_smasher", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> SNOW_CROWN = longSword("snow_crown", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> DANCING_DICER = longSword("dancing_dicer", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> FLAMBERGE = longSword("flamberge", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> FLAMBERGE_PLUS = longSword("flamberge_plus", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> VOLCANON = longSword("volcanon", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> PSYCHO = longSword("psycho", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> SHINE_BLADE = longSword("shine_blade", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> GRAND_SMASHER = longSword("grand_smasher", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> BELZEBUTH = longSword("belzebuth", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> OROCHI = longSword("orochi", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> PUNISHER = longSword("punisher", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> STEEL_SLICER = longSword("steel_slicer", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> MOON_SHADOW = longSword("moon_shadow", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> BLUE_EYED_BLADE = longSword("blue_eyed_blade", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> BALMUNG = longSword("balmung", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> BRAVEHEART = longSword("braveheart", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> FORCE_ELEMENT = longSword("force_element", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> HEAVENS_ASUNDER = longSword("heavens_asunder", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> CALIBURN = longSword("caliburn", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> DEKASH = longSword("dekash", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemLongSwordBase> DAICONE = longSword("daicone", Texture.N);

    public static final RegistryEntrySupplier<Item, ItemSpearBase> SPEAR = spear("spear", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> WOOD_STAFF = spear("wood_staff", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> LANCE = spear("lance", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> LANCE_PLUS = spear("lance_plus", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> NEEDLE_SPEAR = spear("needle_spear", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> TRIDENT = spear("trident", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> WATER_SPEAR = spear("water_spear", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> HALBERD = spear("halberd", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> CORSESCA = spear("corsesca", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> CORSESCA_PLUS = spear("corsesca_plus", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> POISON_SPEAR = spear("poison_spear", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> FIVE_STAFF = spear("five_staff", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> HEAVY_LANCE = spear("heavy_lance", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> FEATHER_LANCE = spear("feather_lance", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> ICEBERG = spear("iceberg", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> BLOOD_LANCE = spear("blood_lance", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> MAGICAL_LANCE = spear("magical_lance", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> FLARE_LANCE = spear("flare_lance", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> BRIONAC = spear("brionac", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> POISON_QUEEN = spear("poison_queen", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> MONK_STAFF = spear("monk_staff", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> METUS = spear("metus", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> SILENT_GRAVE = spear("silent_grave", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> OVERBREAK = spear("overbreak", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> BJOR = spear("bjor", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> BELVAROSE = spear("belvarose", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> GAE_BOLG = spear("gae_bolg", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> DRAGONS_FANG = spear("dragons_fang", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> GUNGNIR = spear("gungnir", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> LEGION = spear("legion", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> PITCHFORK = spear("pitchfork", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> SAFETY_LANCE = spear("safety_lance", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemSpearBase> PINE_CLUB = spear("pine_club", Texture.N);

    public static final RegistryEntrySupplier<Item, ItemAxeBase> BATTLE_AXE = axe("battle_axe", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemAxeBase> BATTLE_SCYTHE = axe("battle_scythe", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemAxeBase> POLE_AXE = axe("pole_axe", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemAxeBase> POLE_AXE_PLUS = axe("pole_axe_plus", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemAxeBase> GREAT_AXE = axe("great_axe", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemAxeBase> TOMAHAWK = axe("tomahawk", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemAxeBase> BASILISK_FANG = axe("basilisk_fang", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemAxeBase> ROCK_AXE = axe("rock_axe", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemAxeBase> DEMON_AXE = axe("demon_axe", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemAxeBase> FROST_AXE = axe("frost_axe", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemAxeBase> CRESCENT_AXE = axe("crescent_axe", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemAxeBase> CRESCENT_AXE_PLUS = axe("crescent_axe_plus", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemAxeBase> HEAT_AXE = axe("heat_axe", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemAxeBase> DOUBLE_EDGE = axe("double_edge", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemAxeBase> ALLDALE = axe("alldale", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemAxeBase> DEVIL_FINGER = axe("devil_finger", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemAxeBase> EXECUTIONER = axe("executioner", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemAxeBase> SAINT_AXE = axe("saint_axe", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemAxeBase> AXE = axe("axe", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemAxeBase> LOLLIPOP = axe("lollipop", Texture.N);

    public static final RegistryEntrySupplier<Item, ItemHammerBase> BATTLE_HAMMER = hammer("battle_hammer", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemHammerBase> BAT = hammer("bat", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemHammerBase> WAR_HAMMER = hammer("war_hammer", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemHammerBase> WAR_HAMMER_PLUS = hammer("war_hammer_plus", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemHammerBase> IRON_BAT = hammer("iron_bat", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemHammerBase> GREAT_HAMMER = hammer("great_hammer", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemHammerBase> ICE_HAMMER = hammer("ice_hammer", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemHammerBase> BONE_HAMMER = hammer("bone_hammer", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemHammerBase> STRONG_STONE = hammer("strong_stone", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemHammerBase> FLAME_HAMMER = hammer("flame_hammer", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemHammerBase> GIGANT_HAMMER = hammer("gigant_hammer", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemHammerBase> SKY_HAMMER = hammer("sky_hammer", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemHammerBase> GRAVITON_HAMMER = hammer("graviton_hammer", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemHammerBase> SPIKED_HAMMER = hammer("spiked_hammer", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemHammerBase> CRYSTAL_HAMMER = hammer("crystal_hammer", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemHammerBase> SCHNABEL = hammer("schnabel", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemHammerBase> GIGANT_HAMMER_PLUS = hammer("gigant_hammer_plus", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemHammerBase> KONGO = hammer("kongo", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemHammerBase> MJOLNIR = hammer("mjolnir", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemHammerBase> FATAL_CRUSH = hammer("fatal_crush", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemHammerBase> SPLASH_STAR = hammer("splash_star", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemHammerBase> HAMMER = hammer("hammer", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemHammerBase> TOY_HAMMER = hammer("toy_hammer", Texture.N);

    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> SHORT_DAGGER = dualBlade("short_dagger", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> STEEL_EDGE = dualBlade("steel_edge", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> FROST_EDGE = dualBlade("frost_edge", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> IRON_EDGE = dualBlade("iron_edge", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> THIEF_KNIFE = dualBlade("thief_knife", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> WIND_EDGE = dualBlade("wind_edge", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> GORGEOUS_LX = dualBlade("gorgeous_lx", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> STEEL_KATANA = dualBlade("steel_katana", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> TWIN_BLADE = dualBlade("twin_blade", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> RAMPAGE = dualBlade("rampage", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> SALAMANDER = dualBlade("salamander", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> PLATINUM_EDGE = dualBlade("platinum_edge", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> SONIC_DAGGER = dualBlade("sonic_dagger", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> CHAOS_EDGE = dualBlade("chaos_edge", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> DESERT_WIND = dualBlade("desert_wind", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> BROKEN_WALL = dualBlade("broken_wall", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> FORCE_DIVIDE = dualBlade("force_divide", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> HEART_FIRE = dualBlade("heart_fire", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> ORCUS_SWORD = dualBlade("orcus_sword", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> DEEP_BLIZZARD = dualBlade("deep_blizzard", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> DARK_INVITATION = dualBlade("dark_invitation", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> PRIEST_SABER = dualBlade("priest_saber", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> EFREET = dualBlade("efreet", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> DRAGOON_CLAW = dualBlade("dragoon_claw", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> EMERALD_EDGE = dualBlade("emerald_edge", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> RUNE_EDGE = dualBlade("rune_edge", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> EARNEST_EDGE = dualBlade("earnest_edge", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> TWIN_JUSTICE = dualBlade("twin_justice", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> DOUBLE_SCRATCH = dualBlade("double_scratch", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> ACUTORIMASS = dualBlade("acutorimass", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemDualBladeBase> TWIN_LEEKS = dualBlade("twin_leeks", Texture.N);

    public static final RegistryEntrySupplier<Item, ItemGloveBase> LEATHER_GLOVE = gloves("leather_glove", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemGloveBase> BRASS_KNUCKLES = gloves("brass_knuckles", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemGloveBase> KOTE = gloves("kote", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemGloveBase> GLOVES = gloves("gloves", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemGloveBase> BEAR_CLAWS = gloves("bear_claws", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemGloveBase> FIST_EARTH = gloves("fist_of_earth", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemGloveBase> FIST_FIRE = gloves("fist_of_fire", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemGloveBase> FIST_WATER = gloves("fist_of_water", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemGloveBase> DRAGON_CLAWS = gloves("dragon_claws", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemGloveBase> FIST_DARK = gloves("fist_of_dark", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemGloveBase> FIST_WIND = gloves("fist_of_wind", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemGloveBase> FIST_LIGHT = gloves("fist_of_light", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemGloveBase> CAT_PUNCH = gloves("cat_punch", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemGloveBase> ANIMAL_PUPPETS = gloves("animal_puppets", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemGloveBase> IRONLEAF_FISTS = gloves("ironleaf_fists", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemGloveBase> CAESTUS = gloves("caestus", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemGloveBase> GOLEM_PUNCH = gloves("golem_punch", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemGloveBase> GOD_HAND = gloves("hand_of_god", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemGloveBase> BAZAL_KATAR = gloves("bazal_katar", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemGloveBase> FENRIR = gloves("fenrir", Texture.N);

    public static final RegistryEntrySupplier<Item, ItemStaffBase> ROD = staff("rod", ItemElement.FIRE, 1, Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> AMETHYST_ROD = staff("amethyst_rod", ItemElement.EARTH, 1, Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> AQUAMARINE_ROD = staff("aquamarine_rod", ItemElement.WATER, 1, Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> FRIENDLY_ROD = staff("friendly_rod", ItemElement.LOVE, 1, Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> LOVE_LOVE_ROD = staff("love_love_rod", ItemElement.LOVE, 1, Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> STAFF = staff("staff", ItemElement.EARTH, 1, Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> EMERALD_ROD = staff("emerald_rod", ItemElement.WIND, 1, Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> SILVER_STAFF = staff("silver_staff", ItemElement.DARK, 2, Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> FLARE_STAFF = staff("flare_staff", ItemElement.FIRE, 2, Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> RUBY_ROD = staff("ruby_rod", ItemElement.FIRE, 2, Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> SAPPHIRE_ROD = staff("sapphire_rod", ItemElement.LIGHT, 2, Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> EARTH_STAFF = staff("earth_staff", ItemElement.EARTH, 2, Texture.N);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> LIGHTNING_WAND = staff("lightning_wand", ItemElement.WIND, 2, Texture.N);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> ICE_STAFF = staff("ice_staff", ItemElement.WATER, 2, Texture.N);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> DIAMOND_ROD = staff("diamond_rod", ItemElement.DARK, 2, Texture.N);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> WIZARDS_STAFF = staff("wizards_staff", ItemElement.LIGHT, 2, Texture.N);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> MAGES_STAFF = staff("mages_staff", ItemElement.EARTH, 2, Texture.N);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> SHOOTING_STAR_STAFF = staff("shooting_star_staff", ItemElement.LIGHT, 2, Texture.N);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> HELL_BRANCH = staff("hell_branch", ItemElement.DARK, 2, Texture.N);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> CRIMSON_STAFF = staff("crimson_staff", ItemElement.FIRE, 2, Texture.N);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> BUBBLE_STAFF = staff("bubble_staff", ItemElement.WATER, 2, Texture.N);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> GAIA_ROD = staff("gaia_rod", ItemElement.EARTH, 2, Texture.N);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> CYCLONE_ROD = staff("cyclone_rod", ItemElement.WIND, 3, Texture.N);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> STORM_WAND = staff("storm_wand", ItemElement.WIND, 3, Texture.N);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> RUNE_STAFF = staff("rune_staff", ItemElement.LIGHT, 3, Texture.N);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> MAGES_STAFF_PLUS = staff("mages_staff_plus", ItemElement.LOVE, 3, Texture.N);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> MAGIC_BROOM = staff("magic_broom", ItemElement.WIND, 3, Texture.N);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> MAGIC_SHOT = staff("magic_shot", ItemElement.LOVE, 3, Texture.N);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> HELL_CURSE = staff("hell_curse", ItemElement.DARK, 3, Texture.N);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> ALGERNON = staff("algernon", ItemElement.EARTH, 3, Texture.N);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> SORCERES_WAND = staff("sorceres_wand", ItemElement.LIGHT, 3, Texture.N);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> BASKET = staff("basket", ItemElement.LOVE, 1, Texture.N);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> GOLDEN_TURNIP_STAFF = staff("golden_turnip_staff", ItemElement.LOVE, 2, Texture.N);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> SWEET_POTATO_STAFF = staff("sweet_potato_staff", ItemElement.LOVE, 1, Texture.N);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> ELVISH_HARP = staff("elvish_harp", ItemElement.LOVE, 3, Texture.N);
    public static final RegistryEntrySupplier<Item, ItemStaffBase> SYRINGE = staff("syringe", ItemElement.WATER, 2, Texture.N);

    public static final RegistryEntrySupplier<Item, Item> LOVE_LETTER = register("love_letter", () -> new Item(new Item.Properties().stacksTo(1)), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
    public static final RegistryEntrySupplier<Item, Item> DIVORCE_PAPER = register("divorce_paper", () -> new Item(new Item.Properties().stacksTo(1)), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> ENGAGEMENT_RING = register("engagement_ring", () -> new ItemArmorBase(ArmorItem.Type.LEGGINGS, new Item.Properties(), RuneCraftory.modRes("engagement_ring"), false), RuneCraftoryCreativeTabs.EQUIPMENT);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> CHEAP_BRACELET = equipment(ArmorItem.Type.LEGGINGS, "cheap_bracelet", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> BRONZE_BRACELET = equipment(ArmorItem.Type.LEGGINGS, "bronze_bracelet", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> SILVER_BRACELET = equipment(ArmorItem.Type.LEGGINGS, "silver_bracelet", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> GOLD_BRACELET = equipment(ArmorItem.Type.LEGGINGS, "gold_bracelet", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> PLATINUM_BRACELET = equipment(ArmorItem.Type.LEGGINGS, "platinum_bracelet", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> SILVER_RING = equipment(ArmorItem.Type.LEGGINGS, "silver_ring", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> GOLD_RING = equipment(ArmorItem.Type.LEGGINGS, "gold_ring", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> PLATINUM_RING = equipment(ArmorItem.Type.LEGGINGS, "platinum_ring", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> SHIELD_RING = equipment(ArmorItem.Type.LEGGINGS, "shield_ring", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> CRITICAL_RING = equipment(ArmorItem.Type.LEGGINGS, "critical_ring", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> SILENT_RING = equipment(ArmorItem.Type.LEGGINGS, "silent_ring", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> PARALYSIS_RING = equipment(ArmorItem.Type.LEGGINGS, "paralysis_ring", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> POISON_RING = equipment(ArmorItem.Type.LEGGINGS, "poison_ring", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> MAGIC_RING = equipment(ArmorItem.Type.LEGGINGS, "magic_ring", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> THROWING_RING = equipment(ArmorItem.Type.LEGGINGS, "throwing_ring", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> STAY_UP_RING = equipment(ArmorItem.Type.LEGGINGS, "stay_up_ring", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> AQUAMARINE_RING = equipment(ArmorItem.Type.LEGGINGS, "aquamarine_ring", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> AMETHYST_RING = equipment(ArmorItem.Type.LEGGINGS, "amethyst_ring", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> EMERALD_RING = equipment(ArmorItem.Type.LEGGINGS, "emerald_ring", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> SAPPHIRE_RING = equipment(ArmorItem.Type.LEGGINGS, "sapphire_ring", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> RUBY_RING = equipment(ArmorItem.Type.LEGGINGS, "ruby_ring", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> CURSED_RING = equipment(ArmorItem.Type.LEGGINGS, "cursed_ring", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> DIAMOND_RING = equipment(ArmorItem.Type.LEGGINGS, "diamond_ring", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> AQUAMARINE_BROOCH = equipment(ArmorItem.Type.LEGGINGS, "aquamarine_brooch", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> AMETHYST_BROOCH = equipment(ArmorItem.Type.LEGGINGS, "amethyst_brooch", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> EMERALD_BROOCH = equipment(ArmorItem.Type.LEGGINGS, "emerald_brooch", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> SAPPHIRE_BROOCH = equipment(ArmorItem.Type.LEGGINGS, "sapphire_brooch", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> RUBY_BROOCH = equipment(ArmorItem.Type.LEGGINGS, "ruby_brooch", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> DIAMOND_BROOCH = equipment(ArmorItem.Type.LEGGINGS, "diamond_brooch", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> DOLPHIN_BROOCH = equipment(ArmorItem.Type.LEGGINGS, "dolphin_brooch", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> FIRE_RING = equipment(ArmorItem.Type.LEGGINGS, "fire_ring", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> WIND_RING = equipment(ArmorItem.Type.LEGGINGS, "wind_ring", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> WATER_RING = equipment(ArmorItem.Type.LEGGINGS, "water_ring", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> EARTH_RING = equipment(ArmorItem.Type.LEGGINGS, "earth_ring", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> HAPPY_RING = equipment(ArmorItem.Type.LEGGINGS, "happy_ring", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> SILVER_PENDANT = equipment(ArmorItem.Type.LEGGINGS, "silver_pendant", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> STAR_PENDANT = equipment(ArmorItem.Type.LEGGINGS, "star_pendant", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> SUN_PENDANT = equipment(ArmorItem.Type.LEGGINGS, "sun_pendant", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> FIELD_PENDANT = equipment(ArmorItem.Type.LEGGINGS, "field_pendant", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> DEW_PENDANT = equipment(ArmorItem.Type.LEGGINGS, "dew_pendant", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> EARTH_PENDANT = equipment(ArmorItem.Type.LEGGINGS, "earth_pendant", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> HEART_PENDANT = equipment(ArmorItem.Type.LEGGINGS, "heart_pendant", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> STRANGE_PENDANT = equipment(ArmorItem.Type.LEGGINGS, "strange_pendant", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> ANETTES_NECKLACE = equipment(ArmorItem.Type.LEGGINGS, "anettes_necklace", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> WORK_GLOVES = equipment(ArmorItem.Type.LEGGINGS, "work_gloves", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> GLOVES_ACCESS = equipment(ArmorItem.Type.LEGGINGS, "gloves_accessory", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> POWER_GLOVES = equipment(ArmorItem.Type.LEGGINGS, "power_gloves", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> EARRINGS = equipment(ArmorItem.Type.LEGGINGS, "earrings", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> WITCH_EARRINGS = equipment(ArmorItem.Type.LEGGINGS, "witch_earrings", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> MAGIC_EARRINGS = equipment(ArmorItem.Type.LEGGINGS, "magic_earrings", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> CHARM = equipment(ArmorItem.Type.LEGGINGS, "charm", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> HOLY_AMULET = equipment(ArmorItem.Type.LEGGINGS, "holy_amulet", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> ROSARY = equipment(ArmorItem.Type.LEGGINGS, "rosary", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> TALISMAN = equipment(ArmorItem.Type.LEGGINGS, "talisman", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> MAGIC_CHARM = equipment(ArmorItem.Type.LEGGINGS, "magic_charm", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> LEATHER_BELT = equipment(ArmorItem.Type.LEGGINGS, "leather_belt", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> LUCKY_STRIKE = equipment(ArmorItem.Type.LEGGINGS, "lucky_strike", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> CHAMP_BELT = equipment(ArmorItem.Type.LEGGINGS, "champ_belt", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> HAND_KNIT_SCARF = equipment(ArmorItem.Type.LEGGINGS, "hand_knit_scarf", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> FLUFFY_SCARF = equipment(ArmorItem.Type.LEGGINGS, "fluffy_scarf", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> HEROS_PROOF = equipment(ArmorItem.Type.LEGGINGS, "heros_proof", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> PROOF_OF_WISDOM = equipment(ArmorItem.Type.LEGGINGS, "proof_of_wisdom", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> ART_OF_ATTACK = equipment(ArmorItem.Type.LEGGINGS, "art_of_attack", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> ART_OF_DEFENSE = equipment(ArmorItem.Type.LEGGINGS, "art_of_defense", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> ART_OF_MAGIC = equipment(ArmorItem.Type.LEGGINGS, "art_of_magic", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> BADGE = equipment(ArmorItem.Type.LEGGINGS, "badge", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> COURAGE_BADGE = equipment(ArmorItem.Type.LEGGINGS, "courage_badge", Texture.N);

    public static final RegistryEntrySupplier<Item, ItemArmorBase> SHIRT = equipment(ArmorItem.Type.CHESTPLATE, "shirt", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> VEST = equipment(ArmorItem.Type.CHESTPLATE, "vest", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> COTTON_CLOTH = equipment(ArmorItem.Type.CHESTPLATE, "cotton_cloth", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> MAIL = equipment(ArmorItem.Type.CHESTPLATE, "mail", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> CHAIN_MAIL = equipment(ArmorItem.Type.CHESTPLATE, "chain_mail", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> SCALE_VEST = equipment(ArmorItem.Type.CHESTPLATE, "scale_vest", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> SPARKLING_SHIRT = equipment(ArmorItem.Type.CHESTPLATE, "sparkling_shirt", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> WIND_CLOAK = equipment(ArmorItem.Type.CHESTPLATE, "wind_cloak", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> PROTECTOR = equipment(ArmorItem.Type.CHESTPLATE, "protector", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> PLATINUM_MAIL = equipment(ArmorItem.Type.CHESTPLATE, "platinum_mail", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> LEMELLAR_VEST = equipment(ArmorItem.Type.CHESTPLATE, "lemellar_vest", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> MERCENARYS_CLOAK = equipment(ArmorItem.Type.CHESTPLATE, "mercenarys_cloak", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> WOOLY_SHIRT = equipment(ArmorItem.Type.CHESTPLATE, "wooly_shirt", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> ELVISH_CLOAK = equipment(ArmorItem.Type.CHESTPLATE, "elvish_cloak", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> DRAGON_CLOAK = equipment(ArmorItem.Type.CHESTPLATE, "dragon_cloak", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> POWER_PROTECTOR = equipment(ArmorItem.Type.CHESTPLATE, "power_protector", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> RUNE_VEST = equipment(ArmorItem.Type.CHESTPLATE, "rune_vest", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> ROYAL_GARTER = equipment(ArmorItem.Type.CHESTPLATE, "royal_garter", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> FOUR_DRAGONS_VEST = equipment(ArmorItem.Type.CHESTPLATE, "four_dragons_vest", Texture.N);

    public static final RegistryEntrySupplier<Item, ItemArmorBase> HEADBAND = equipment(ArmorItem.Type.HELMET, "headband", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> BLUE_RIBBON = equipment(ArmorItem.Type.HELMET, "blue_ribbon", Texture.Y, true);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> GREEN_RIBBON = equipment(ArmorItem.Type.HELMET, "green_ribbon", Texture.Y, true);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> PURPLE_RIBBON = equipment(ArmorItem.Type.HELMET, "purple_ribbon", Texture.Y, true);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> SPECTACLES = equipment(ArmorItem.Type.HELMET, "spectacles", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> STRAW_HAT = equipment(ArmorItem.Type.HELMET, "straw_hat", Texture.Y, true);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> FANCY_HAT = equipment(ArmorItem.Type.HELMET, "fancy_hat", Texture.Y, true);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> BRAND_GLASSES = equipment(ArmorItem.Type.HELMET, "brand_glasses", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> CUTE_KNITTING = equipment(ArmorItem.Type.HELMET, "cute_knitting", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> INTELLIGENT_GLASSES = equipment(ArmorItem.Type.HELMET, "intelligent_glasses", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> FIREPROOF_HOOD = equipment(ArmorItem.Type.HELMET, "fireproof_hood", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> SILK_HAT = equipment(ArmorItem.Type.HELMET, "silk_hat", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> BLACK_RIBBON = equipment(ArmorItem.Type.HELMET, "black_ribbon", Texture.Y, true);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> LOLITA_HEADDRESS = equipment(ArmorItem.Type.HELMET, "lolita_headdress", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> HEADDRESS = equipment(ArmorItem.Type.HELMET, "headdress", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> YELLOW_RIBBON = equipment(ArmorItem.Type.HELMET, "yellow_ribbon", Texture.Y, true);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> CAT_EARS = equipment(ArmorItem.Type.HELMET, "cat_ears", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> SILVER_HAIRPIN = equipment(ArmorItem.Type.HELMET, "silver_hairpin", Texture.N, true);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> RED_RIBBON = equipment(ArmorItem.Type.HELMET, "red_ribbon", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> ORANGE_RIBBON = equipment(ArmorItem.Type.HELMET, "orange_ribbon", Texture.Y, true);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> WHITE_RIBBON = equipment(ArmorItem.Type.HELMET, "white_ribbon", Texture.Y, true);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> FOUR_SEASONS = equipment(ArmorItem.Type.HELMET, "four_seasons", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> FEATHERS_HAT = equipment(ArmorItem.Type.HELMET, "feathers_hat", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> GOLD_HAIRPIN = equipment(ArmorItem.Type.HELMET, "gold_hairpin", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> INDIGO_RIBBON = equipment(ArmorItem.Type.HELMET, "indigo_ribbon", Texture.Y, true);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> CROWN = equipment(ArmorItem.Type.HELMET, "crown", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> TURNIP_HEADGEAR = equipment(ArmorItem.Type.HELMET, "turnip_headgear", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> PUMPKIN_HEADGEAR = equipment(ArmorItem.Type.HELMET, "pumpkin_headgear", Texture.N);

    public static final RegistryEntrySupplier<Item, ItemArmorBase> LEATHER_BOOTS = equipment(ArmorItem.Type.BOOTS, "leather_boots", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> FREE_FARMING_SHOES = equipment(ArmorItem.Type.BOOTS, "free_farming_shoes", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> PIYO_SANDALS = equipment(ArmorItem.Type.BOOTS, "piyo_sandals", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> SECRET_SHOES = equipment(ArmorItem.Type.BOOTS, "secret_shoes", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> SILVER_BOOTS = equipment(ArmorItem.Type.BOOTS, "silver_boots", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> HEAVY_BOOTS = equipment(ArmorItem.Type.BOOTS, "heavy_boots", Texture.Y);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> SNEAKING_BOOTS = equipment(ArmorItem.Type.BOOTS, "sneaking_boots", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> FAST_STEP_BOOTS = equipment(ArmorItem.Type.BOOTS, "fast_step_boots", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> GOLD_BOOTS = equipment(ArmorItem.Type.BOOTS, "gold_boots", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> BONE_BOOTS = equipment(ArmorItem.Type.BOOTS, "bone_boots", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> SNOW_BOOTS = equipment(ArmorItem.Type.BOOTS, "snow_boots", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> STRIDER_BOOTS = equipment(ArmorItem.Type.BOOTS, "strider_boots", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> STEP_IN_BOOTS = equipment(ArmorItem.Type.BOOTS, "step_in_boots", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> FEATHER_BOOTS = equipment(ArmorItem.Type.BOOTS, "feather_boots", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> GHOST_BOOTS = equipment(ArmorItem.Type.BOOTS, "ghost_boots", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> IRON_GETA = equipment(ArmorItem.Type.BOOTS, "iron_geta", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> KNIGHT_BOOTS = equipment(ArmorItem.Type.BOOTS, "knight_boots", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> FAIRY_BOOTS = equipment(ArmorItem.Type.BOOTS, "fairy_boots", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> WET_BOOTS = equipment(ArmorItem.Type.BOOTS, "wet_boots", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> WATER_SHOES = equipment(ArmorItem.Type.BOOTS, "water_shoes", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> ICE_SKATES = equipment(ArmorItem.Type.BOOTS, "ice_skates", Texture.N);
    public static final RegistryEntrySupplier<Item, ItemArmorBase> ROCKET_WING = equipment(ArmorItem.Type.BOOTS, "rocket_wing", Texture.N);

    public static final RegistryEntrySupplier<Item, ShieldItem> SMALL_SHIELD = shield("small_shield", Texture.Y);
    public static final RegistryEntrySupplier<Item, ShieldItem> UMBRELLA = shield("umbrella", Texture.Y);
    public static final RegistryEntrySupplier<Item, ShieldItem> IRON_SHIELD = shield("iron_shield", Texture.Y);
    public static final RegistryEntrySupplier<Item, ShieldItem> MONKEY_PLUSH = shield("monkey_plush", Texture.Y);
    public static final RegistryEntrySupplier<Item, ShieldItem> ROUND_SHIELD = shield("round_shield", Texture.Y);
    public static final RegistryEntrySupplier<Item, ShieldItem> TURTLE_SHIELD = shield("turtle_shield", Texture.Y);
    public static final RegistryEntrySupplier<Item, ShieldItem> CHAOS_SHIELD = shield("chaos_shield", Texture.N);
    public static final RegistryEntrySupplier<Item, ShieldItem> BONE_SHIELD = shield("bone_shield", Texture.N);
    public static final RegistryEntrySupplier<Item, ShieldItem> MAGIC_SHIELD = shield("magic_shield", Texture.N);
    public static final RegistryEntrySupplier<Item, ShieldItem> HEAVY_SHIELD = shield("heavy_shield", Texture.N);
    public static final RegistryEntrySupplier<Item, ShieldItem> PLATINUM_SHIELD = shield("platinum_shield", Texture.N);
    public static final RegistryEntrySupplier<Item, ShieldItem> KITE_SHIELD = shield("kite_shield", Texture.N);
    public static final RegistryEntrySupplier<Item, ShieldItem> KNIGHT_SHIELD = shield("knight_shield", Texture.N);
    public static final RegistryEntrySupplier<Item, ShieldItem> ELEMENT_SHIELD = shield("element_shield", Texture.N);
    public static final RegistryEntrySupplier<Item, ShieldItem> MAGICAL_SHIELD = shield("magical_shield", Texture.N);
    public static final RegistryEntrySupplier<Item, ShieldItem> PRISM_SHIELD = shield("prism_shield", Texture.N);
    public static final RegistryEntrySupplier<Item, ShieldItem> RUNE_SHIELD = shield("rune_shield", Texture.N);
    public static final RegistryEntrySupplier<Item, ShieldItem> PLANT_SHIELD = register("plant_shield", () -> new ShieldItem(new Item.Properties().stacksTo(1)), RuneCraftoryCreativeTabs.EQUIPMENT);

    public static final RegistryEntrySupplier<Item, CraftingBlockItem> FORGE = register("forge", () -> new CraftingBlockItem(RuneCraftoryBlocks.FORGE.get(), new Item.Properties()), RuneCraftoryCreativeTabs.BLOCKS);
    public static final RegistryEntrySupplier<Item, CraftingBlockItem> ACCESSORY_WORKBENCH = register("accessory_workbench", () -> new CraftingBlockItem(RuneCraftoryBlocks.ACCESSORY_WORKBENCH.get(), new Item.Properties()), RuneCraftoryCreativeTabs.BLOCKS);
    public static final RegistryEntrySupplier<Item, CraftingBlockItem> CHEMISTRY_SET = register("chemistry_set", () -> new CraftingBlockItem(RuneCraftoryBlocks.CHEMISTRY_SET.get(), new Item.Properties()), RuneCraftoryCreativeTabs.BLOCKS);
    public static final RegistryEntrySupplier<Item, CraftingBlockItem> COOKING_TABLE = register("cooking_table", () -> new CraftingBlockItem(RuneCraftoryBlocks.COOKING_TABLE.get(), new Item.Properties()), RuneCraftoryCreativeTabs.BLOCKS);

    public static final RegistryEntrySupplier<Item, BlockItem> MINERAL_IRON = mineral(MineralBlockTier.IRON);
    public static final RegistryEntrySupplier<Item, BlockItem> MINERAL_TIN = mineral(MineralBlockTier.TIN);
    public static final RegistryEntrySupplier<Item, BlockItem> MINERAL_SILVER = mineral(MineralBlockTier.SILVER);
    public static final RegistryEntrySupplier<Item, BlockItem> MINERAL_GOLD = mineral(MineralBlockTier.GOLD);
    public static final RegistryEntrySupplier<Item, BlockItem> MINERAL_PLATINUM = mineral(MineralBlockTier.PLATINUM);
    public static final RegistryEntrySupplier<Item, BlockItem> MINERAL_ORICHALCUM = mineral(MineralBlockTier.ORICHALCUM);
    public static final RegistryEntrySupplier<Item, BlockItem> MINERAL_DIAMOND = mineral(MineralBlockTier.DIAMOND);
    public static final RegistryEntrySupplier<Item, BlockItem> MINERAL_DRAGONIC = mineral(MineralBlockTier.DRAGONIC);
    public static final RegistryEntrySupplier<Item, BlockItem> MINERAL_AQUAMARINE = mineral(MineralBlockTier.AQUAMARINE);
    public static final RegistryEntrySupplier<Item, BlockItem> MINERAL_AMETHYST = mineral(MineralBlockTier.AMETHYST);
    public static final RegistryEntrySupplier<Item, BlockItem> MINERAL_RUBY = mineral(MineralBlockTier.RUBY);
    public static final RegistryEntrySupplier<Item, BlockItem> MINERAL_EMERALD = mineral(MineralBlockTier.EMERALD);
    public static final RegistryEntrySupplier<Item, BlockItem> MINERAL_SAPPHIRE = mineral(MineralBlockTier.SAPPHIRE);
    public static final RegistryEntrySupplier<Item, BlockItem> BROKEN_MINERAL_IRON = brokenMineral(MineralBlockTier.IRON);
    public static final RegistryEntrySupplier<Item, BlockItem> BROKEN_MINERAL_TIN = brokenMineral(MineralBlockTier.TIN);
    public static final RegistryEntrySupplier<Item, BlockItem> BROKEN_MINERAL_SILVER = brokenMineral(MineralBlockTier.SILVER);
    public static final RegistryEntrySupplier<Item, BlockItem> BROKEN_MINERAL_GOLD = brokenMineral(MineralBlockTier.GOLD);
    public static final RegistryEntrySupplier<Item, BlockItem> BROKEN_MINERAL_PLATINUM = brokenMineral(MineralBlockTier.PLATINUM);
    public static final RegistryEntrySupplier<Item, BlockItem> BROKEN_MINERAL_ORICHALCUM = brokenMineral(MineralBlockTier.ORICHALCUM);
    public static final RegistryEntrySupplier<Item, BlockItem> BROKEN_MINERAL_DIAMOND = brokenMineral(MineralBlockTier.DIAMOND);
    public static final RegistryEntrySupplier<Item, BlockItem> BROKEN_MINERAL_DRAGONIC = brokenMineral(MineralBlockTier.DRAGONIC);
    public static final RegistryEntrySupplier<Item, BlockItem> BROKEN_MINERAL_AQUAMARINE = brokenMineral(MineralBlockTier.AQUAMARINE);
    public static final RegistryEntrySupplier<Item, BlockItem> BROKEN_MINERAL_AMETHYST = brokenMineral(MineralBlockTier.AMETHYST);
    public static final RegistryEntrySupplier<Item, BlockItem> BROKEN_MINERAL_RUBY = brokenMineral(MineralBlockTier.RUBY);
    public static final RegistryEntrySupplier<Item, BlockItem> BROKEN_MINERAL_EMERALD = brokenMineral(MineralBlockTier.EMERALD);
    public static final RegistryEntrySupplier<Item, BlockItem> BROKEN_MINERAL_SAPPHIRE = brokenMineral(MineralBlockTier.SAPPHIRE);

    public static final RegistryEntrySupplier<Item, Item> RAW_TIN = register("raw_tin", () -> new Item(new Item.Properties()), RuneCraftoryCreativeTabs.MATERIALS);
    public static final RegistryEntrySupplier<Item, Item> TIN_INGOT = mat("tin_ingot", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> BRONZE_DUST = register("bronze_dust", () -> new Item(new Item.Properties()), RuneCraftoryCreativeTabs.MATERIALS);
    public static final RegistryEntrySupplier<Item, Item> BRONZE_INGOT = mat("bronze_ingot", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> RAW_SILVER = register("raw_silver", () -> new Item(new Item.Properties()), RuneCraftoryCreativeTabs.MATERIALS);
    public static final RegistryEntrySupplier<Item, Item> SILVER_INGOT = mat("silver_ingot", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> RAW_PLATINUM = register("raw_platinum", () -> new Item(new Item.Properties()), RuneCraftoryCreativeTabs.MATERIALS);
    public static final RegistryEntrySupplier<Item, Item> PLATINUM_INGOT = mat("platinum_ingot", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> ORICHALCUM = mat("orichalcum", Rarity.UNCOMMON, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> DRAGONIC = mat("dragonic_stone", Rarity.UNCOMMON, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> SCRAP = mat("scrap", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> SCRAP_PLUS = mat("scrap_plus", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> AMETHYST = mat("amethyst", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> AQUAMARINE = mat("aquamarine", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> RUBY = mat("ruby", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> SAPPHIRE = mat("sapphire", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CORE_RED = mat("red_core", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CORE_BLUE = mat("blue_core", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CORE_YELLOW = mat("yellow_core", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CORE_GREEN = mat("green_core", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CRYSTAL_SKULL = mat("crystal_skull", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CRYSTAL_WATER = mat("water_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CRYSTAL_EARTH = mat("earth_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CRYSTAL_FIRE = mat("fire_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CRYSTAL_WIND = mat("wind_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CRYSTAL_LIGHT = mat("light_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CRYSTAL_DARK = mat("dark_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CRYSTAL_LOVE = mat("love_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CRYSTAL_SMALL = mat("small_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CRYSTAL_BIG = mat("big_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CRYSTAL_MAGIC = mat("magic_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CRYSTAL_RUNE = mat("rune_crystal", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CRYSTAL_ELECTRO = mat("electro_crystal", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> STICK_THICK = mat("thick_stick", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> HORN_INSECT = mat("insect_horn", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> HORN_RIGID = mat("rigid_horn", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> PLANT_STEM = mat("plant_stem", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> HORN_BULL = mat("bulls_horn", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> HORN_DEVIL = mat("devil_horn", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> MOVING_BRANCH = mat("moving_branch", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> GLUE = mat("glue", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> DEVIL_BLOOD = mat("devil_blood", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> PARA_POISON = mat("paralysis_poison", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> POISON_KING = mat("poison_king", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> FEATHER_BLACK = mat("black_feather", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> FEATHER_THUNDER = mat("thunder_feather", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> FEATHER_YELLOW = mat("yellow_feather", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> DRAGON_FIN = mat("dragon_fin", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> TURTLE_SHELL = mat("turtle_shell", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> FISH_FOSSIL = mat("fish_fossil", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> SKULL = mat("skull", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> DRAGON_BONES = mat("dragon_bones", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> TORTOISE_SHELL = mat("black_tortoise_shell", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> AMMONITE = mat("ammonite", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> ROCK = mat("rock", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> STONE_ROUND = mat("round_stone", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> STONE_TINY = mat("tiny_golem_stone", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> STONE_GOLEM = mat("golem_stone", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> TABLET_GOLEM = mat("golem_tablet", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> STONE_SPIRIT = mat("golem_spirit_stone", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> TABLET_TRUTH = mat("tablet_of_truth", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> YARN = mat("yarn", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> OLD_BANDAGE = mat("old_bandage", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> AMBROSIAS_THORNS = mat("ambrosias_thorns", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> THREAD_SPIDER = mat("spider_thread", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> PUPPETRY_STRINGS = mat("puppetry_strings", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> VINE = mat("vine", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> TAIL_SCORPION = mat("scorpion_tail", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> STRONG_VINE = mat("strong_vine", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> THREAD_PRETTY = mat("pretty_thread", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> TAIL_CHIMERA = mat("chimera_tail", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> ARROW_HEAD = mat("arrowhead", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> BLADE_SHARD = mat("blade_shard", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> BROKEN_HILT = mat("broken_hilt", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> BROKEN_BOX = mat("broken_box", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> BLADE_GLISTENING = mat("glistening_blade", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> GREAT_HAMMER_SHARD = mat("great_hammer_shard", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> HAMMER_PIECE = mat("hammer_piece", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> SHOULDER_PIECE = mat("shoulder_piece", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> PIRATES_ARMOR = mat("pirates_armor", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> SCREW_RUSTY = mat("rusty_screw", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> SCREW_SHINY = mat("shiny_screw", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> ROCK_SHARD_LEFT = mat("left_rock_shard", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> ROCK_SHARD_RIGHT = mat("right_rock_shard", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> MTGU_PLATE = mat("mtgu_plate", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item, Item> BROKEN_ICE_WALL = mat("broken_ice_wall", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item, Item> FUR_SMALL = mat("fur_s", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> FUR_MEDIUM = mat("fur_m", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> FUR_LARGE = mat("fur_l", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> FUR = mat("fur", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> FURBALL = mat("wooly_furball", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> DOWN_YELLOW = mat("yellow_down", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> FUR_QUALITY = mat("quality_puffy_fur", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> DOWN_PENGUIN = mat("penguin_down", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> LIGHTNING_MANE = mat("lightning_mane", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> FUR_RED_LION = mat("red_lion_fur", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> FUR_BLUE_LION = mat("blue_lion_fur", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CHEST_HAIR = mat("chest_hair", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> SPORE = mat("spore", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> POWDER_POISON = mat("poison_powder", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> SPORE_HOLY = mat("holy_spore", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> FAIRY_DUST = mat("fairy_dust", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> FAIRY_ELIXIR = mat("fairy_elixir", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> ROOT = mat("root", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> POWDER_MAGIC = mat("magic_powder", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> POWDER_MYSTERIOUS = mat("mysterious_powder", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> MAGIC = mat("magic", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> ASH_EARTH = mat("earth_dragon_ash", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item, Item> ASH_FIRE = mat("fire_dragon_ash", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item, Item> ASH_WATER = mat("water_dragon_ash", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item, Item> TURNIPS_MIRACLE = mat("turnips_miracle", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> MELODY_BOTTLE = mat("melody_bottle", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item, Item> CLOTH_CHEAP = mat("cheap_cloth", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CLOTH_QUALITY = mat("quality_cloth", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CLOTH_QUALITY_WORN = mat("quality_worn_cloth", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CLOTH_SILK = mat("silk_cloth", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> GHOST_HOOD = mat("ghost_hood", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> GLOVE_GIANT = mat("giants_glove", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> GLOVE_BLUE_GIANT = mat("blue_giants_glove", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CARAPACE_INSECT = mat("insect_carapace", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CARAPACE_PRETTY = mat("pretty_carapace", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CLOTH_ANCIENT_ORC = mat("ancient_orc_cloth", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> JAW_INSECT = mat("insect_jaw", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CLAW_PANTHER = mat("panther_claw", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CLAW_MAGIC = mat("magic_claw", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> FANG_WOLF = mat("wolf_fang", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> FANG_GOLD_WOLF = mat("gold_wolf_fang", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CLAW_PALM = mat("palm_claw", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CLAW_MALM = mat("malm_claw", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> GIANTS_NAIL = mat("giants_nail", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CLAW_CHIMERA = mat("chimeras_claw", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> TUSK_IVORY = mat("ivory_tusk", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> TUSK_UNBROKEN_IVORY = mat("unbroken_tusk", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> SCORPION_PINCER = mat("scorpion_pincer", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> DANGEROUS_SCISSORS = mat("dangerous_scissors", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> PROPELLOR_CHEAP = mat("cheap_propeller", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> PROPELLOR_QUALITY = mat("quality_propeller", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> FANG_DRAGON = mat("dragon_fang", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> JAW_QUEEN = mat("queens_jaw", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> WIND_DRAGON_TOOTH = mat("wind_dragon_tooth", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item, Item> GIANTS_NAIL_BIG = mat("big_giants_nail", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> SCALE_WET = mat("wet_scale", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> SCALE_GRIMOIRE = mat("grimoire_scale", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> SCALE_DRAGON = mat("dragon_scale", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> SCALE_CRIMSON = mat("crimson_scale", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> SCALE_BLUE = mat("blue_scale", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> SCALE_GLITTER = mat("glitter_scale", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> SCALE_LOVE = mat("love_scale", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> SCALE_BLACK = mat("black_scale", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> SCALE_FIRE = mat("fire_wyrm_scale", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> SCALE_EARTH = mat("earth_wyrm_scale", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> SCALE_LEGEND = mat("legendary_scale", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> STEEL_DOUBLE = mat("double_steel", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> STEEL_TEN = mat("ten_fold_steel", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> GLITTA_AUGITE = mat("glitta_augite", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> INVIS_STONE = mat("invisible_stone", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> LIGHT_ORE = mat("light_ore", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> RUNE_SPHERE_SHARD = mat("rune_sphere_shard", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item, Item> SHADE_STONE = mat("shade_stone", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> RACCOON_LEAF = mat("raccoon_leaf", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> ICY_NOSE = mat("icy_nose", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item, Item> BIG_BIRDS_COMB = mat("big_birds_comb", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> RAFFLESIA_PETAL = mat("rafflesia_petal", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> CURSED_DOLL = mat("cursed_doll", Rarity.RARE, Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> WARRIORS_PROOF = mat("warriors_proof", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> PROOF_OF_RANK = mat("proof_of_rank", Texture.Y);
    public static final RegistryEntrySupplier<Item, Item> THRONE_OF_EMPIRE = mat("throne_of_emire", Rarity.RARE, Texture.N);
    public static final RegistryEntrySupplier<Item, Item> WHITE_STONE = mat("white_stone", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> RARE_CAN = mat("rare_can", Rarity.UNCOMMON, Texture.N);
    public static final RegistryEntrySupplier<Item, Item> CAN = mat("can", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> BOOTS = mat("boots", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> LAWN = mat("ayngondaia_lawn", Rarity.UNCOMMON, Texture.N);

    public static final RegistryEntrySupplier<Item, ItemSpell> FIRE_BALL_SMALL = spell(() -> RuneCraftorySpells.FIREBALL, "fireball", 2);
    public static final RegistryEntrySupplier<Item, ItemSpell> FIRE_BALL_BIG = spell(() -> RuneCraftorySpells.BIG_FIREBALL, "fireball_big", 2);
    public static final RegistryEntrySupplier<Item, ItemSpell> EXPLOSION = spell(() -> RuneCraftorySpells.EXPLOSION, "explosion", 2);
    public static final RegistryEntrySupplier<Item, ItemSpell> WATER_LASER = spell(() -> RuneCraftorySpells.WATER_LASER, "water_laser", true, 2);
    public static final RegistryEntrySupplier<Item, ItemSpell> PARALLEL_LASER = spell(() -> RuneCraftorySpells.PARALLEL_LASER, "parallel_laser", true, 2);
    public static final RegistryEntrySupplier<Item, ItemSpell> DELTA_LASER = spell(() -> RuneCraftorySpells.DELTA_LASER, "delta_laser", true, 2);
    public static final RegistryEntrySupplier<Item, ItemSpell> SCREW_ROCK = spell(() -> RuneCraftorySpells.SCREW_ROCK, "screw_rock", 2);
    public static final RegistryEntrySupplier<Item, ItemSpell> EARTH_SPIKE = spell(() -> RuneCraftorySpells.EARTH_SPIKE, "earth_spike", 2);
    public static final RegistryEntrySupplier<Item, ItemSpell> AVENGER_ROCK = spell(() -> RuneCraftorySpells.AVENGER_ROCK, "avenger_rock", 2);
    public static final RegistryEntrySupplier<Item, ItemSpell> SONIC_WIND = spell(() -> RuneCraftorySpells.SONIC, "sonic_wind", 2);
    public static final RegistryEntrySupplier<Item, ItemSpell> DOUBLE_SONIC = spell(() -> RuneCraftorySpells.DOUBLE_SONIC, "double_sonic", 2);
    public static final RegistryEntrySupplier<Item, ItemSpell> PENETRATE_SONIC = spell(() -> RuneCraftorySpells.PENETRATE_SONIC, "penetrate_sonic", 2);
    public static final RegistryEntrySupplier<Item, ItemSpell> LIGHT_BARRIER = spell(() -> RuneCraftorySpells.LIGHT_BARRIER, "light_barrier", 2);
    public static final RegistryEntrySupplier<Item, ItemSpell> SHINE = spell(() -> RuneCraftorySpells.SHINE, "shine", 2);
    public static final RegistryEntrySupplier<Item, ItemSpell> PRISM = spell(() -> RuneCraftorySpells.PRISM, "prism", 2);
    public static final RegistryEntrySupplier<Item, ItemSpell> DARK_SNAKE = spell(() -> RuneCraftorySpells.DARK_SNAKE, "dark_snake", 2);
    public static final RegistryEntrySupplier<Item, ItemSpell> DARK_BALL = spell(() -> RuneCraftorySpells.DARK_BALL, "dark_ball", 2);
    public static final RegistryEntrySupplier<Item, ItemSpell> DARKNESS = spell(() -> RuneCraftorySpells.DARKNESS, "darkness", 2);
    public static final RegistryEntrySupplier<Item, ItemSpell> CURE = spell(() -> RuneCraftorySpells.CURE, "cure", 2);
    public static final RegistryEntrySupplier<Item, ItemSpell> CURE_ALL = spell(() -> RuneCraftorySpells.CURE_ALL, "cure_all", 2);
    public static final RegistryEntrySupplier<Item, ItemSpell> CURE_MASTER = spell(() -> RuneCraftorySpells.MASTER_CURE, "cure_master", 2);
    public static final RegistryEntrySupplier<Item, ItemSpell> MEDI_POISON = spell(() -> RuneCraftorySpells.MEDI_POISON, "medi_poison", 2);
    public static final RegistryEntrySupplier<Item, ItemSpell> MEDI_PARA = spell(() -> RuneCraftorySpells.MEDI_PARA, "medi_paralysis", 2);
    public static final RegistryEntrySupplier<Item, ItemSpell> MEDI_SEAL = spell(() -> RuneCraftorySpells.MEDI_SEAL, "medi_seal", 2);
    public static final RegistryEntrySupplier<Item, ItemSpell> GREETING = spell(() -> RuneCraftorySpells.EMPTY, "greeting", 0);
    public static final RegistryEntrySupplier<Item, ItemSpell> POWER_WAVE = spell(() -> RuneCraftorySpells.POWER_WAVE, "power_wave", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> DASH_SLASH = spell(() -> RuneCraftorySpells.DASH_SLASH, "dash_slash", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> RUSH_ATTACK = spell(() -> RuneCraftorySpells.RUSH_ATTACK, "rush_attack", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> ROUND_BREAK = spell(() -> RuneCraftorySpells.ROUND_BREAK, "round_break", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> MIND_THRUST = spell(() -> RuneCraftorySpells.MIND_THRUST, "mind_thrust", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> GUST = spell(() -> RuneCraftorySpells.GUST, "gust", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> STORM = spell(() -> RuneCraftorySpells.STORM, "storm", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> BLITZ = spell(() -> RuneCraftorySpells.BLITZ, "blitz", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> TWIN_ATTACK = spell(() -> RuneCraftorySpells.TWIN_ATTACK, "twin_attack", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> RAIL_STRIKE = spell(() -> RuneCraftorySpells.RAIL_STRIKE, "rail_strike", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> WIND_SLASH = spell(() -> RuneCraftorySpells.WIND_SLASH, "wind_slash", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> FLASH_STRIKE = spell(() -> RuneCraftorySpells.FLASH_STRIKE, "flash_strike", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> NAIVE_BLADE = spell(() -> RuneCraftorySpells.NAIVE_BLADE, "naive_blade", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> STEEL_HEART = spell(() -> RuneCraftorySpells.STEEL_HEART, "steel_heart", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> DELTA_STRIKE = spell(() -> RuneCraftorySpells.DELTA_STRIKE, "delta_strike", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> HURRICANE = spell(() -> RuneCraftorySpells.HURRICANE, "hurricane", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> REAPER_SLASH = spell(() -> RuneCraftorySpells.REAPER_SLASH, "reaper_slash", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> MILLION_STRIKE = spell(() -> RuneCraftorySpells.MILLION_STRIKE, "million_strike", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> AXEL_DISASTER = spell(() -> RuneCraftorySpells.AXEL_DISASTER, "axel_disaster", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> STARDUST_UPPER = spell(() -> RuneCraftorySpells.STARDUST_UPPER, "stardust_upper", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> TORNADO_SWING = spell(() -> RuneCraftorySpells.TORNADO_SWING, "tornado_swing", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> GRAND_IMPACT = spell(() -> RuneCraftorySpells.GRAND_IMPACT, "grand_impact", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> GIGA_SWING = spell(() -> RuneCraftorySpells.GIGA_SWING, "giga_swing", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> UPPER_CUT = spell(() -> RuneCraftorySpells.UPPER_CUT, "upper_cut", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> DOUBLE_KICK = spell(() -> RuneCraftorySpells.DOUBLE_KICK, "double_kick", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> STRAIGHT_PUNCH = spell(() -> RuneCraftorySpells.STRAIGHT_PUNCH, "straight_punch", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> NEKO_DAMASHI = spell(() -> RuneCraftorySpells.NEKO_DAMASHI, "neko_damashi", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> RUSH_PUNCH = spell(() -> RuneCraftorySpells.RUSH_PUNCH, "rush_punch", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> CYCLONE = spell(() -> RuneCraftorySpells.CYCLONE, "cyclone", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> RAPID_MOVE = spell(() -> RuneCraftorySpells.RAPID_MOVE, "rapid_move", 1);
    public static final RegistryEntrySupplier<Item, ItemSpell> BONUS_CONCERTO = spell(() -> RuneCraftorySpells.EMPTY, "bonus_concerto", 0);
    public static final RegistryEntrySupplier<Item, ItemSpell> STRIKING_MARCH = spell(() -> RuneCraftorySpells.EMPTY, "striking_march", 0);
    public static final RegistryEntrySupplier<Item, ItemSpell> IRON_WALTZ = spell(() -> RuneCraftorySpells.EMPTY, "iron_waltz", 0);
    public static final RegistryEntrySupplier<Item, ItemSpell> TELEPORT = spell(() -> RuneCraftorySpells.TELEPORT, "teleport", 0);

    public static final RegistryEntrySupplier<Item, Item> ROCKFISH = fish("rockfish", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> SAND_FLOUNDER = fish("sand_flounder", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> POND_SMELT = fish("pond_smelt", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> LOBSTER = fish("lobster", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> LAMP_SQUID = fish("lamb_squid", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> CHERRY_SALMON = fish("cherry_salmon", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> FALL_FLOUNDER = fish("fall_flounder", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> GIRELLA = fish("girella", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> TUNA = fish("tuna", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> CRUCIAN_CARP = fish("crucian_carp", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> YELLOWTAIL = fish("yellowtail", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> BLOWFISH = fish("blowfish", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> FLOUNDER = fish("flounder", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> RAINBOW_TROUT = fish("rainbow_trout", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> LOVER_SNAPPER = fish("lover_snapper", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> SNAPPER = fish("snapper", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> SHRIMP = fish("shrimp", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> SUNSQUID = fish("sunsquid", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> PIKE = fish("pike", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> NEEDLEFISH = fish("needle_fish", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> MACKEREL = fish("mackerel", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> SALMON = fish("salmon", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> GIBELIO = fish("gibelio", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> TURBOT = fish("turbot", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> SKIPJACK = fish("skipjack", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> GLITTER_SNAPPER = fish("glitter_snapper", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> CHUB = fish("chub", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> CHAR_FISH = fish("char", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> SARDINE = fish("sardine", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> TAIMEN = fish("taimen", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> SQUID = fish("squid", Texture.N);
    public static final RegistryEntrySupplier<Item, Item> MASU_TROUT = fish("masu_trout", Texture.N);

    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> TURNIP_SEEDS = seed("turnip", () -> RuneCraftoryBlocks.TURNIP);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> TURNIP_PINK_SEEDS = seed("turnip_pink", () -> RuneCraftoryBlocks.TURNIP_PINK);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> CABBAGE_SEEDS = seed("cabbage", () -> RuneCraftoryBlocks.CABBAGE);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> PINK_MELON_SEEDS = seed("pink_melon", () -> RuneCraftoryBlocks.PINK_MELON);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> HOT_HOT_SEEDS = seed("hot_hot_fruit", () -> RuneCraftoryBlocks.HOT_HOT_FRUIT);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> GOLD_TURNIP_SEEDS = seed("golden_turnip", () -> RuneCraftoryBlocks.GOLDEN_TURNIP);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> GOLD_POTATO_SEEDS = seed("golden_potato", () -> RuneCraftoryBlocks.GOLDEN_POTATO);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> GOLD_PUMPKIN_SEEDS = seed("golden_pumpkin", () -> RuneCraftoryBlocks.GOLDEN_PUMPKIN);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> GOLD_CABBAGE_SEEDS = seed("golden_cabbage", () -> RuneCraftoryBlocks.GOLDEN_CABBAGE);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> BOK_CHOY_SEEDS = seed("bok_choy", () -> RuneCraftoryBlocks.BOK_CHOY);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> LEEK_SEEDS = seed("leek", () -> RuneCraftoryBlocks.LEEK);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> RADISH_SEEDS = seed("radish", () -> RuneCraftoryBlocks.RADISH);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> GREEN_PEPPER_SEEDS = seed("green_pepper", () -> RuneCraftoryBlocks.GREEN_PEPPER);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> SPINACH_SEEDS = seed("spinach", () -> RuneCraftoryBlocks.SPINACH);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> YAM_SEEDS = seed("yam", () -> RuneCraftoryBlocks.YAM);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> EGGPLANT_SEEDS = seed("eggplant", () -> RuneCraftoryBlocks.EGGPLANT);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> PINEAPPLE_SEEDS = seed("pineapple", () -> RuneCraftoryBlocks.PINEAPPLE);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> PUMPKIN_SEEDS = seed("pumpkin", () -> RuneCraftoryBlocks.PUMPKIN);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> ONION_SEEDS = seed("onion", () -> RuneCraftoryBlocks.ONION);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> CORN_SEEDS = seed("corn", () -> RuneCraftoryBlocks.CORN);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> TOMATO_SEEDS = seed("tomato", () -> RuneCraftoryBlocks.TOMATO);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> STRAWBERRY_SEEDS = seed("strawberry", () -> RuneCraftoryBlocks.STRAWBERRY);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> CUCUMBER_SEEDS = seed("cucumber", () -> RuneCraftoryBlocks.CUCUMBER);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> FODDER_SEEDS = seed("fodder", () -> RuneCraftoryBlocks.FODDER);

    public static final RegistryEntrySupplier<Item, Item> FODDER = mat("fodder", Texture.N);

    public static final RegistryEntrySupplier<Item, Item> TURNIP = crop("turnip", null, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item, Item> TURNIP_GIANT = crop("tyrant_turnip", TURNIP, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item, Item> TURNIP_PINK = crop("turnip_pink", null, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item, Item> TURNIP_PINK_GIANT = crop("colossal_pink", TURNIP_PINK, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item, Item> CABBAGE = crop("cabbage", null, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item, Item> CABBAGE_GIANT = crop("king_cabbage", CABBAGE, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item, Item> PINK_MELON = crop("pink_melon", null, Texture.Y, 1);
    public static final RegistryEntrySupplier<Item, Item> PINK_MELON_GIANT = crop("conqueror_melon", PINK_MELON, Texture.Y, 1);
    public static final RegistryEntrySupplier<Item, Item> PINEAPPLE = crop("pineapple", null, Texture.Y, 1);
    public static final RegistryEntrySupplier<Item, Item> PINEAPPLE_GIANT = crop("king_pineapple", PINEAPPLE, Texture.Y, 1);
    public static final RegistryEntrySupplier<Item, Item> STRAWBERRY = crop("strawberry", null, Texture.N, 1);
    public static final RegistryEntrySupplier<Item, Item> STRAWBERRY_GIANT = crop("sultan_strawberry", STRAWBERRY, Texture.N, 1);
    public static final RegistryEntrySupplier<Item, Item> GOLDEN_TURNIP = crop("golden_turnip", null, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item, Item> GOLDEN_TURNIP_GIANT = crop("golden_tyrant_turnip", GOLDEN_TURNIP, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item, Item> GOLDEN_POTATO = crop("golden_potato", null, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item, Item> GOLDEN_POTATO_GIANT = crop("golden_prince_potato", GOLDEN_POTATO, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item, Item> GOLDEN_PUMPKIN = crop("golden_pumpkin", null, Texture.N, 0);
    public static final RegistryEntrySupplier<Item, Item> GOLDEN_PUMPKIN_GIANT = crop("golden_doom_pumpkin", GOLDEN_PUMPKIN, Texture.N, 0);
    public static final RegistryEntrySupplier<Item, Item> GOLDEN_CABBAGE = crop("golden_cabbage", null, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item, Item> GOLDEN_CABBAGE_GIANT = crop("golden_king_cabbage", GOLDEN_CABBAGE, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item, Item> HOT_HOT_FRUIT = crop("hot_hot_fruit", null, Texture.N, 1);
    public static final RegistryEntrySupplier<Item, Item> HOT_HOT_FRUIT_GIANT = crop("giant_hot_hot_fruit", HOT_HOT_FRUIT, Texture.N, 1);
    public static final RegistryEntrySupplier<Item, Item> BOK_CHOY = crop("bok_choy", null, Texture.N, 0);
    public static final RegistryEntrySupplier<Item, Item> BOK_CHOY_GIANT = crop("boss_bok_choy", BOK_CHOY, Texture.N, 0);
    public static final RegistryEntrySupplier<Item, Item> LEEK = crop("leek", null, Texture.N, 0);
    public static final RegistryEntrySupplier<Item, Item> LEEK_GIANT = crop("legendary_leek", LEEK, Texture.N, 0);
    public static final RegistryEntrySupplier<Item, Item> RADISH = crop("radish", null, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item, Item> RADISH_GIANT = crop("noble_radish", RADISH, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item, Item> SPINACH = crop("spinach", null, Texture.N, 0);
    public static final RegistryEntrySupplier<Item, Item> SPINACH_GIANT = crop("sovereign_spinach", SPINACH, Texture.N, 0);
    public static final RegistryEntrySupplier<Item, Item> GREEN_PEPPER = crop("green_pepper", null, Texture.N, 0);
    public static final RegistryEntrySupplier<Item, Item> GREEN_PEPPER_GIANT = crop("green_pepper_rex", GREEN_PEPPER, Texture.N, 0);
    public static final RegistryEntrySupplier<Item, Item> YAM = crop("yam", null, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item, Item> YAM_GIANT = crop("lordly_yam", YAM, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item, Item> EGGPLANT = crop("eggplant", null, Texture.N, 0);
    public static final RegistryEntrySupplier<Item, Item> EGGPLANT_GIANT = crop("emperor_eggplant", EGGPLANT, Texture.N, 0);
    public static final RegistryEntrySupplier<Item, Item> TOMATO = crop("tomato", null, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item, Item> TOMATO_GIANT = crop("titan_tomato", TOMATO, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item, Item> CORN = crop("corn", null, Texture.N, 0);
    public static final RegistryEntrySupplier<Item, Item> CORN_GIANT = crop("gigant_corn", CORN, Texture.N, 0);
    public static final RegistryEntrySupplier<Item, Item> CUCUMBER = crop("cucumber", null, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item, Item> CUCUMBER_GIANT = crop("kaiser_cucumber", CUCUMBER, Texture.Y, 0);
    public static final RegistryEntrySupplier<Item, Item> PUMPKIN = crop("pumpkin", null, Texture.N, 0);
    public static final RegistryEntrySupplier<Item, Item> PUMPKIN_GIANT = crop("doom_pumpkin", PUMPKIN, Texture.N, 0);
    public static final RegistryEntrySupplier<Item, Item> ONION = crop("onion", null, Texture.N, 0);
    public static final RegistryEntrySupplier<Item, Item> ONION_GIANT = crop("ultra_onion", ONION, Texture.N, 0);

    public static final RegistryEntrySupplier<Item, Item> POTATO_GIANT = cropWith("princely_potato", ResourceLocation.withDefaultNamespace("potato"), Texture.Y, 0);
    public static final RegistryEntrySupplier<Item, Item> CARROT_GIANT = cropWith("royal_carrot", ResourceLocation.withDefaultNamespace("carrot"), Texture.Y, 0);

    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> TOYHERB_SEEDS = seed("toyherb", () -> RuneCraftoryBlocks.TOYHERB);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> MOONDROP_SEEDS = seed("moondrop_flower", () -> RuneCraftoryBlocks.MOONDROP_FLOWER);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> PINK_CAT_SEEDS = seed("pink_cat", () -> RuneCraftoryBlocks.PINK_CAT);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> CHARM_BLUE_SEEDS = seed("charm_blue", () -> RuneCraftoryBlocks.CHARM_BLUE);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> LAMP_GRASS_SEEDS = seed("lamp_grass", () -> RuneCraftoryBlocks.LAMP_GRASS);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> CHERRY_GRASS_SEEDS = seed("cherry_grass", () -> RuneCraftoryBlocks.CHERRY_GRASS);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> POM_POM_GRASS_SEEDS = seed("pom_pom_grass", () -> RuneCraftoryBlocks.POM_POM_GRASS);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> AUTUMN_GRASS_SEEDS = seed("autumn_grass", () -> RuneCraftoryBlocks.AUTUMN_GRASS);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> NOEL_GRASS_SEEDS = seed("noel_grass", () -> RuneCraftoryBlocks.NOEL_GRASS);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> FIREFLOWER_SEEDS = seed("fireflower", () -> RuneCraftoryBlocks.FIREFLOWER);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> FOUR_LEAF_CLOVER_SEEDS = seed("four_leaf_clover", () -> RuneCraftoryBlocks.FOUR_LEAF_CLOVER);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> IRONLEAF_SEEDS = seed("ironleaf", () -> RuneCraftoryBlocks.IRONLEAF);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> WHITE_CRYSTAL_SEEDS = seed("white_crystal", () -> RuneCraftoryBlocks.WHITE_CRYSTAL);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> RED_CRYSTAL_SEEDS = seed("red_crystal", () -> RuneCraftoryBlocks.RED_CRYSTAL);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> GREEN_CRYSTAL_SEEDS = seed("green_crystal", () -> RuneCraftoryBlocks.GREEN_CRYSTAL);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> BLUE_CRYSTAL_SEEDS = seed("blue_crystal", () -> RuneCraftoryBlocks.BLUE_CRYSTAL);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> EMERY_FLOWER_SEEDS = seed("emery_flower", () -> RuneCraftoryBlocks.EMERY_FLOWER);

    public static final RegistryEntrySupplier<Item, Item> TOYHERB = crop("toyherb", null, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> TOYHERB_GIANT = crop("ultra_toyherb", TOYHERB, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> MOONDROP_FLOWER = crop("moondrop_flower", null, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> MOONDROP_FLOWER_GIANT = crop("ultra_moondrop_flower", MOONDROP_FLOWER, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> PINK_CAT = crop("pink_cat", null, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> PINK_CAT_GIANT = crop("king_pink_cat", PINK_CAT, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> CHARM_BLUE = crop("charm_blue", null, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> CHARM_BLUE_GIANT = crop("great_charm_blue", CHARM_BLUE, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> LAMP_GRASS = crop("lamp_grass", null, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> LAMP_GRASS_GIANT = crop("kaiser_lamp_grass", LAMP_GRASS, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> CHERRY_GRASS = crop("cherry_grass", null, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> CHERRY_GRASS_GIANT = crop("king_cherry_grass", CHERRY_GRASS, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> POM_POM_GRASS = crop("pom_pom_grass", null, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> POM_POM_GRASS_GIANT = crop("king_pom_pom_grass", POM_POM_GRASS, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> AUTUMN_GRASS = crop("autumn_grass", null, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> AUTUMN_GRASS_GIANT = crop("big_autumn_grass", AUTUMN_GRASS, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> NOEL_GRASS = crop("noel_grass", null, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> NOEL_GRASS_GIANT = crop("large_noel_grass", NOEL_GRASS, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> FIREFLOWER = crop("fireflower", null, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> FIREFLOWER_GIANT = crop("big_fireflower", FIREFLOWER, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> FOUR_LEAF_CLOVER = crop("four_leaf_clover", null, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> FOUR_LEAF_CLOVER_GIANT = crop("great_four_leaf_clover", FOUR_LEAF_CLOVER, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> IRONLEAF = crop("ironleaf", null, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> IRONLEAF_GIANT = crop("super_ironleaf", IRONLEAF, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> WHITE_CRYSTAL = crop("white_crystal", null, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> WHITE_CRYSTAL_GIANT = crop("big_white_crystal", WHITE_CRYSTAL, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> RED_CRYSTAL = crop("red_crystal", null, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> RED_CRYSTAL_GIANT = crop("big_red_crystal", RED_CRYSTAL, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> GREEN_CRYSTAL = crop("green_crystal", null, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> GREEN_CRYSTAL_GIANT = crop("big_green_crystal", GREEN_CRYSTAL, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> BLUE_CRYSTAL = crop("blue_crystal", null, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> BLUE_CRYSTAL_GIANT = crop("big_blue_crystal", BLUE_CRYSTAL, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> EMERY_FLOWER = crop("emery_flower", null, Texture.Y, 2);
    public static final RegistryEntrySupplier<Item, Item> EMERY_FLOWER_GIANT = crop("great_emery_flower", EMERY_FLOWER, Texture.Y, 2);

    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> SHIELD_SEEDS = seed("shield", () -> RuneCraftoryBlocks.SHIELD_CROP);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> SWORD_SEEDS = seed("sword", () -> RuneCraftoryBlocks.SWORD_CROP);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> DUNGEON_SEEDS = seed("dungeon", () -> RuneCraftoryBlocks.DUNGEON);

    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> APPLE_SAPLING = register("apple_sapling", () -> new ItemNameBlockItem(RuneCraftoryBlocks.APPLE_SAPLING.get(), new Item.Properties()), RuneCraftoryCreativeTabs.FARMING);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> ORANGE_SAPLING = register("orange_sapling", () -> new ItemNameBlockItem(RuneCraftoryBlocks.ORANGE_SAPLING.get(), new Item.Properties()), RuneCraftoryCreativeTabs.FARMING);
    public static final RegistryEntrySupplier<Item, ItemNameBlockItem> GRAPE_SAPLING = register("grape_sapling", () -> new ItemNameBlockItem(RuneCraftoryBlocks.GRAPE_SAPLING.get(), new Item.Properties()), RuneCraftoryCreativeTabs.FARMING);

    public static final RegistryEntrySupplier<Item, ItemMedicine> ROUNDOFF = medicine("roundoff", false);
    public static final RegistryEntrySupplier<Item, ItemMedicine> PARA_GONE = medicine("para_gone", false);
    public static final RegistryEntrySupplier<Item, ItemMedicine> COLD_MED = medicine("cold_medicine", false);
    public static final RegistryEntrySupplier<Item, ItemMedicine> ANTIDOTE = medicine("antidote_potion", false);
    public static final RegistryEntrySupplier<Item, ItemMedicine> RECOVERY_POTION = medicine("recovery_potion", true);
    public static final RegistryEntrySupplier<Item, ItemMedicine> HEALING_POTION = medicine("healing_potion", true);
    public static final RegistryEntrySupplier<Item, ItemMedicine> MYSTERY_POTION = medicine("mystery_potion", true);
    public static final RegistryEntrySupplier<Item, ItemMedicine> MAGICAL_POTION = medicine("magical_potion", true);
    public static final RegistryEntrySupplier<Item, Item> INVINCIROID = drinkable("invinciroid");
    public static final RegistryEntrySupplier<Item, Item> LOVE_POTION = drinkable("love_potion");
    public static final RegistryEntrySupplier<Item, Item> FORMUADE = drinkable("formuade");
    public static final RegistryEntrySupplier<Item, ItemObjectX> OBJECT_X = register("object_x", () -> new ItemObjectX(new Item.Properties().food(FOOD_PROP)), RuneCraftoryCreativeTabs.MEDICINE);

    public static final RegistryEntrySupplier<Item, BlockItem> ELLI_LEAVES = herb("elli_leaves", () -> RuneCraftoryBlocks.ELLI_LEAVES);
    public static final RegistryEntrySupplier<Item, BlockItem> WITHERED_GRASS = herb("withered_grass", () -> RuneCraftoryBlocks.WITHERED_GRASS);
    public static final RegistryEntrySupplier<Item, BlockItem> WEEDS = herb("weeds", () -> RuneCraftoryBlocks.WEEDS);
    public static final RegistryEntrySupplier<Item, BlockItem> WHITE_GRASS = herb("white_grass", () -> RuneCraftoryBlocks.WHITE_GRASS);
    public static final RegistryEntrySupplier<Item, BlockItem> INDIGO_GRASS = herb("indigo_grass", () -> RuneCraftoryBlocks.INDIGO_GRASS);
    public static final RegistryEntrySupplier<Item, BlockItem> PURPLE_GRASS = herb("purple_grass", () -> RuneCraftoryBlocks.PURPLE_GRASS);
    public static final RegistryEntrySupplier<Item, BlockItem> GREEN_GRASS = herb("green_grass", () -> RuneCraftoryBlocks.GREEN_GRASS);
    public static final RegistryEntrySupplier<Item, BlockItem> BLUE_GRASS = herb("blue_grass", () -> RuneCraftoryBlocks.BLUE_GRASS);
    public static final RegistryEntrySupplier<Item, BlockItem> YELLOW_GRASS = herb("yellow_grass", () -> RuneCraftoryBlocks.YELLOW_GRASS);
    public static final RegistryEntrySupplier<Item, BlockItem> RED_GRASS = herb("red_grass", () -> RuneCraftoryBlocks.RED_GRASS);
    public static final RegistryEntrySupplier<Item, BlockItem> ORANGE_GRASS = herb("orange_grass", () -> RuneCraftoryBlocks.ORANGE_GRASS);
    public static final RegistryEntrySupplier<Item, BlockItem> BLACK_GRASS = herb("black_grass", () -> RuneCraftoryBlocks.BLACK_GRASS);
    public static final RegistryEntrySupplier<Item, BlockItem> ANTIDOTE_GRASS = herb("antidote_grass", () -> RuneCraftoryBlocks.ANTIDOTE_GRASS);
    public static final RegistryEntrySupplier<Item, BlockItem> MEDICINAL_HERB = herb("medicinal_herb", () -> RuneCraftoryBlocks.MEDICINAL_HERB);
    public static final RegistryEntrySupplier<Item, BlockItem> BAMBOO_SPROUT = herb("bamboo_sprout", () -> RuneCraftoryBlocks.BAMBOO_SPROUT);
    public static final RegistryEntrySupplier<Item, ItemMushroom> MUSHROOM = register("mushroom", () -> new ItemMushroom(new Item.Properties().food(LOW_FOOD_PROP)), RuneCraftoryCreativeTabs.FOOD);
    public static final RegistryEntrySupplier<Item, ItemMushroom> MONARCH_MUSHROOM = register("monarch_mushroom", () -> new ItemMushroom(new Item.Properties().food(LOW_FOOD_PROP)), RuneCraftoryCreativeTabs.FOOD);

    public static final RegistryEntrySupplier<Item, Item> RICE = food("rice", Texture.Y, FOOD_PROP, RunecraftoryTags.Items.RICE);
    public static final RegistryEntrySupplier<Item, Item> RICE_FLOUR = food("rice_flour", Texture.Y, LOW_FOOD_PROP);
    public static final RegistryEntrySupplier<Item, Item> FLOUR = food("flour", Texture.Y, LOW_FOOD_PROP, RunecraftoryTags.Items.FLOUR);
    public static final RegistryEntrySupplier<Item, Item> OIL = drinkable("oil", Texture.Y, LOW_FOOD_PROP, RunecraftoryTags.Items.OIL);
    public static final RegistryEntrySupplier<Item, Item> CURRY_POWDER = food("curry_powder", Texture.Y, LOW_FOOD_PROP);
    public static final RegistryEntrySupplier<Item, Item> WINE = drinkable("wine", Texture.Y, FOOD_PROP);
    public static final RegistryEntrySupplier<Item, Item> CHOCOLATE = food("chocolate", Texture.Y, RunecraftoryTags.Items.CHOCOLATE);
    public static final RegistryEntrySupplier<Item, Item> EGG_S = food("egg_s", Texture.Y, LOW_FOOD_PROP, RunecraftoryTags.Items.FOOD_EGG);
    public static final RegistryEntrySupplier<Item, Item> EGG_M = food("egg_m", Texture.Y, LOW_FOOD_PROP, RunecraftoryTags.Items.FOOD_EGG);
    public static final RegistryEntrySupplier<Item, Item> EGG_L = food("egg_l", Texture.Y, LOW_FOOD_PROP, RunecraftoryTags.Items.FOOD_EGG);
    public static final RegistryEntrySupplier<Item, Item> MILK_S = drinkable("milk_s", Texture.Y, LOW_FOOD_PROP, RunecraftoryTags.Items.FOOD_MILK);
    public static final RegistryEntrySupplier<Item, Item> MILK_M = drinkable("milk_m", Texture.Y, LOW_FOOD_PROP, RunecraftoryTags.Items.FOOD_MILK);
    public static final RegistryEntrySupplier<Item, Item> MILK_L = drinkable("milk_l", Texture.Y, LOW_FOOD_PROP, RunecraftoryTags.Items.FOOD_MILK);

    public static final RegistryEntrySupplier<Item, Item> ONIGIRI = food("onigiri", Texture.Y, RunecraftoryTags.Items.ONIGIRI, RunecraftoryTags.Items.FOOD_SIMPLE);
    public static final RegistryEntrySupplier<Item, Item> CHEESE = food("cheese", Texture.Y, LOW_FOOD_PROP, RunecraftoryTags.Items.CHEESE, RunecraftoryTags.Items.FOOD_SIMPLE);
    public static final RegistryEntrySupplier<Item, Item> PICKLED_TURNIP = food("pickled_turnip", Texture.Y, RunecraftoryTags.Items.FOOD_SIMPLE);
    public static final RegistryEntrySupplier<Item, Item> PICKLES = food("pickles", Texture.N, RunecraftoryTags.Items.FOOD_SIMPLE);
    public static final RegistryEntrySupplier<Item, Item> BAMBOO_RICE = food("bamboo_rice", Texture.N, RunecraftoryTags.Items.FOOD_SIMPLE);
    public static final RegistryEntrySupplier<Item, Item> SALMON_ONIGIRI = food("salmon_onigiri", Texture.Y, RunecraftoryTags.Items.ONIGIRI, RunecraftoryTags.Items.FOOD_SIMPLE);
    public static final RegistryEntrySupplier<Item, Item> PICKLE_MIX = food("pickle_mix", Texture.N, RunecraftoryTags.Items.FOOD_SIMPLE);
    public static final RegistryEntrySupplier<Item, Item> SANDWICH = food("sandwich", Texture.N, RunecraftoryTags.Items.FOOD_SIMPLE);
    public static final RegistryEntrySupplier<Item, Item> FRUIT_SANDWICH = food("fruit_sandwich", Texture.N, RunecraftoryTags.Items.FOOD_SIMPLE);
    public static final RegistryEntrySupplier<Item, Item> SALAD = food("salad", Texture.N, RunecraftoryTags.Items.FOOD_SIMPLE);
    public static final RegistryEntrySupplier<Item, Item> RELAX_TEA_LEAVES = food("relax_tea_leaves", Texture.N, RunecraftoryTags.Items.FOOD_SIMPLE);
    public static final RegistryEntrySupplier<Item, Item> TURNIP_HEAVEN = food("turnip_heaven", Texture.N, RunecraftoryTags.Items.FOOD_SIMPLE);

    public static final RegistryEntrySupplier<Item, Item> DUMPLINGS = food("dumplings", Texture.N, RunecraftoryTags.Items.STEAMED);
    public static final RegistryEntrySupplier<Item, Item> FLAN = food("flan", Texture.Y, RunecraftoryTags.Items.STEAMED);
    public static final RegistryEntrySupplier<Item, Item> PUMPKIN_FLAN = food("pumpkin_flan", Texture.N, RunecraftoryTags.Items.STEAMED);
    public static final RegistryEntrySupplier<Item, Item> STEAMED_BREAD = food("steamed_bread", Texture.N, RunecraftoryTags.Items.STEAMED);
    public static final RegistryEntrySupplier<Item, Item> CHEESE_BREAD = food("cheese_bread", Texture.N, RunecraftoryTags.Items.STEAMED);
    public static final RegistryEntrySupplier<Item, Item> POUND_CAKE = food("pound_cake", Texture.N, RunecraftoryTags.Items.STEAMED);
    public static final RegistryEntrySupplier<Item, Item> CHOCOLATE_SPONGE = food("chocolate_sponge", Texture.N, RunecraftoryTags.Items.STEAMED);
    public static final RegistryEntrySupplier<Item, Item> CURRY_MANJU = food("curry_manju", Texture.N, RunecraftoryTags.Items.STEAMED);
    public static final RegistryEntrySupplier<Item, Item> CHINESE_MANJU = food("chinese_manju", Texture.N, RunecraftoryTags.Items.STEAMED);
    public static final RegistryEntrySupplier<Item, Item> MEAT_DUMPLING = food("meat_dumpling", Texture.N, RunecraftoryTags.Items.STEAMED);
    public static final RegistryEntrySupplier<Item, Item> STEAMED_GYOZA = food("steamed_gyoza", Texture.N, RunecraftoryTags.Items.STEAMED);

    public static final RegistryEntrySupplier<Item, Item> MAYONNAISE = food("mayonnaise", Texture.Y, LOW_FOOD_PROP, RunecraftoryTags.Items.MAYO, RunecraftoryTags.Items.MIXED);
    public static final RegistryEntrySupplier<Item, Item> BUTTER = food("butter", Texture.Y, LOW_FOOD_PROP, RunecraftoryTags.Items.BUTTER, RunecraftoryTags.Items.MIXED);
    public static final RegistryEntrySupplier<Item, Item> KETCHUP = drinkable("ketchup", Texture.Y, LOW_FOOD_PROP, RunecraftoryTags.Items.KETCHUP, RunecraftoryTags.Items.MIXED);
    public static final RegistryEntrySupplier<Item, Item> ICE_CREAM = food("ice_cream", Texture.N, RunecraftoryTags.Items.MIXED);
    public static final RegistryEntrySupplier<Item, Item> APPLE_JUICE = drinkable("apple_juice", Texture.Y, HIGH_FOOD_PROP, RunecraftoryTags.Items.JUICE, RunecraftoryTags.Items.MIXED);
    public static final RegistryEntrySupplier<Item, Item> ORANGE_JUICE = drinkable("orange_juice", Texture.Y, HIGH_FOOD_PROP, RunecraftoryTags.Items.JUICE, RunecraftoryTags.Items.MIXED);
    public static final RegistryEntrySupplier<Item, Item> GRAPE_JUICE = drinkable("grape_juice", Texture.Y, HIGH_FOOD_PROP, RunecraftoryTags.Items.JUICE, RunecraftoryTags.Items.MIXED);
    public static final RegistryEntrySupplier<Item, Item> STRAWBERRY_MILK = drinkable("strawberry_milk", Texture.N, HIGH_FOOD_PROP, RunecraftoryTags.Items.MIXED);
    public static final RegistryEntrySupplier<Item, Item> TOMATO_JUICE = drinkable("tomato_juice", Texture.Y, HIGH_FOOD_PROP, RunecraftoryTags.Items.JUICE, RunecraftoryTags.Items.MIXED);
    public static final RegistryEntrySupplier<Item, Item> PINEAPPLE_JUICE = drinkable("pineapple_juice", Texture.Y, HIGH_FOOD_PROP, RunecraftoryTags.Items.JUICE, RunecraftoryTags.Items.MIXED);
    public static final RegistryEntrySupplier<Item, Item> FRUIT_JUICE = drinkable("fruit_juice", Texture.Y, HIGH_FOOD_PROP, RunecraftoryTags.Items.JUICE, RunecraftoryTags.Items.MIXED);
    public static final RegistryEntrySupplier<Item, Item> FRUIT_SMOOTHIE = drinkable("fruit_smoothie", Texture.Y, HIGH_FOOD_PROP, RunecraftoryTags.Items.MIXED);
    public static final RegistryEntrySupplier<Item, Item> VEGETABLE_JUICE = drinkable("vegetable_juice", Texture.Y, HIGH_FOOD_PROP, RunecraftoryTags.Items.JUICE, RunecraftoryTags.Items.MIXED);
    public static final RegistryEntrySupplier<Item, Item> VEGGIE_SMOOTHIE = drinkable("veggie_smoothie", Texture.Y, HIGH_FOOD_PROP, RunecraftoryTags.Items.JUICE, RunecraftoryTags.Items.MIXED);
    public static final RegistryEntrySupplier<Item, Item> MIXED_JUICE = drinkable("mixed_juice", Texture.Y, HIGH_FOOD_PROP, RunecraftoryTags.Items.JUICE, RunecraftoryTags.Items.MIXED);
    public static final RegistryEntrySupplier<Item, Item> MIXED_SMOOTHIE = drinkable("mixed_smoothie", Texture.Y, HIGH_FOOD_PROP, RunecraftoryTags.Items.MIXED);
    public static final RegistryEntrySupplier<Item, Item> HOT_JUICE = drinkable("hot_juice", Texture.N, HIGH_FOOD_PROP, RunecraftoryTags.Items.MIXED);
    public static final RegistryEntrySupplier<Item, Item> PRELUDE_TO_LOVE = drinkable("prelude_to_love", Texture.N, HIGH_FOOD_PROP, RunecraftoryTags.Items.MIXED);
    public static final RegistryEntrySupplier<Item, Item> GOLD_JUICE = drinkable("gold_juice", Texture.N, HIGH_FOOD_PROP, RunecraftoryTags.Items.MIXED);

    public static final RegistryEntrySupplier<Item, Item> BAKED_ONIGIRI = food("baked_onigiri", Texture.Y, RunecraftoryTags.Items.ONIGIRI, RunecraftoryTags.Items.OVEN);
    public static final RegistryEntrySupplier<Item, Item> SWEET_POTATO = food("sweet_potato", Texture.N, RunecraftoryTags.Items.OVEN);
    public static final RegistryEntrySupplier<Item, Item> CORN_ON_THE_COB = food("corn_on_the_cob", Texture.N, RunecraftoryTags.Items.OVEN);
    public static final RegistryEntrySupplier<Item, Item> BREAD = food("bread", Texture.N, RunecraftoryTags.Items.BREAD, RunecraftoryTags.Items.OVEN);
    public static final RegistryEntrySupplier<Item, Item> TOAST = food("toast", Texture.Y, RunecraftoryTags.Items.TOAST, RunecraftoryTags.Items.OVEN);
    public static final RegistryEntrySupplier<Item, Item> RAISIN_BREAD = food("raisin_bread", Texture.N, RunecraftoryTags.Items.OVEN);
    public static final RegistryEntrySupplier<Item, Item> YAM_OF_THE_AGES = food("yam_of_the_ages", Texture.N, RunecraftoryTags.Items.OVEN);
    public static final RegistryEntrySupplier<Item, Item> BUTTER_ROLL = food("butter_roll", Texture.N, RunecraftoryTags.Items.OVEN);
    public static final RegistryEntrySupplier<Item, Item> JAM_ROLL = food("jam_roll", Texture.N, RunecraftoryTags.Items.OVEN);
    public static final RegistryEntrySupplier<Item, Item> APPLE_PIE = food("apple_pie", Texture.Y, RunecraftoryTags.Items.PIE, RunecraftoryTags.Items.OVEN);
    public static final RegistryEntrySupplier<Item, Item> CAKE = food("cake", Texture.N, RunecraftoryTags.Items.PIE, RunecraftoryTags.Items.OVEN);
    public static final RegistryEntrySupplier<Item, Item> CHEESECAKE = food("cheesecake", Texture.Y, RunecraftoryTags.Items.PIE, RunecraftoryTags.Items.OVEN);
    public static final RegistryEntrySupplier<Item, Item> CHOCOLATE_CAKE = food("chocolate_cake", Texture.Y, RunecraftoryTags.Items.PIE, RunecraftoryTags.Items.OVEN);
    public static final RegistryEntrySupplier<Item, Item> COOKIE = food("cookie", Texture.Y, RunecraftoryTags.Items.OVEN);
    public static final RegistryEntrySupplier<Item, Item> CHOCO_COOKIE = food("choco_cookie", Texture.Y, RunecraftoryTags.Items.OVEN);
    public static final RegistryEntrySupplier<Item, Item> DORIA = food("doria", Texture.N, RunecraftoryTags.Items.OVEN);
    public static final RegistryEntrySupplier<Item, Item> SEAFOOD_DORIA = food("seafood_doria", Texture.N, RunecraftoryTags.Items.OVEN);
    public static final RegistryEntrySupplier<Item, Item> PIZZA = food("pizza", Texture.N, RunecraftoryTags.Items.OVEN);
    public static final RegistryEntrySupplier<Item, Item> SEAFOOD_PIZZA = food("seafood_pizza", Texture.N, RunecraftoryTags.Items.OVEN);
    public static final RegistryEntrySupplier<Item, Item> GRATIN = food("gratin", Texture.N, RunecraftoryTags.Items.OVEN);
    public static final RegistryEntrySupplier<Item, Item> SEAFOOD_GRATIN = food("seafood_gratin", Texture.N, RunecraftoryTags.Items.OVEN);

    public static final RegistryEntrySupplier<Item, Item> YOGURT = food("yogurt", Texture.Y, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> RICE_PORRIDGE = food("rice_porridge", Texture.N, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> MILK_PORRIDGE = food("milk_porridge", Texture.N, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> MARMALADE = food("marmalade", Texture.Y, RunecraftoryTags.Items.JAM, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> APPLE_JAM = food("apple_jam", Texture.Y, RunecraftoryTags.Items.JAM, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> GRAPE_JAM = food("grape_jam", Texture.Y, RunecraftoryTags.Items.JAM, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> STRAWBERRY_JAM = food("strawberry_jam", Texture.Y, RunecraftoryTags.Items.JAM, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> HOT_MILK = drinkable("hot_milk", Texture.Y, HIGH_FOOD_PROP, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> HOT_CHOCOLATE = drinkable("hot_chocolate", Texture.Y, HIGH_FOOD_PROP, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> BOILED_EGG = food("boiled_egg", Texture.N, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> BOILED_SPINACH = food("boiled_spinach", Texture.N, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> BOILED_PUMPKIN = food("boiled_pumpkin", Texture.N, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> CHEESE_FONDUE = food("cheese_fondue", Texture.N, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> GRAPE_LIQUEUR = drinkable("grape_liqueur", Texture.N, HIGH_FOOD_PROP, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> GLAZED_YAM = food("glazed_yam", Texture.N, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> GRILLED_MISO = food("grilled_miso", Texture.N, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> STEW = food("stew", Texture.N, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> ROCKFISH_STEW = food("rockfish_stew", Texture.N, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> UNION_STEW = food("union_stew", Texture.N, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> EGG_BOWL = food("egg_bowl", Texture.N, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> TEMPURA_BOWL = food("tempura_bowl", Texture.N, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> CURRY_RICE = food("curry_rice", Texture.N, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> UDON = food("udon", Texture.Y, RunecraftoryTags.Items.UDON, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> TEMPURA_UDON = food("tempura_udon", Texture.Y, RunecraftoryTags.Items.UDON, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> CURRY_UDON = food("curry_udon", Texture.Y, RunecraftoryTags.Items.UDON, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> BOILED_GYOZA = food("boiled_gyoza", Texture.N, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> RELAX_TEA = food("relax_tea", Texture.N, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> ULTIMATE_CURRY = food("ultimate_curry", Texture.N, RunecraftoryTags.Items.POT);
    public static final RegistryEntrySupplier<Item, Item> ROYAL_CURRY = food("royal_curry", Texture.N, RunecraftoryTags.Items.POT);

    public static final RegistryEntrySupplier<Item, Item> BAKED_APPLE = food("baked_apple", Texture.Y, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> FRIED_EGGS = food("fried_eggs", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> POPCORN = food("popcorn", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> FRENCH_FRIES = food("french_fries", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> CORN_CEREAL = food("corn_cereal", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> OMELET = food("omelet", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> OMELET_RICE = food("omelet_rice", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> FRIED_RICE = food("fried_rice", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> FRIED_VEGGIES = food("fried_veggies", Texture.Y, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> FRENCH_TOAST = food("french_toast", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> CROQUETTES = food("croquettes", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> PANCAKES = food("pancakes", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> DONUT = food("donut", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> RISOTTO = food("risotto", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> MISO_EGGPLANT = food("miso_eggplant", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> GYOZA = food("gyoza", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> TEMPURA = food("tempura", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> CURRY_BREAD = food("curry_bread", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> CABBAGE_CAKES = food("cabbage_cakes", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> DRY_CURRY = food("dry_curry", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> FRIED_UDON = food("fried_udon", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);

    public static final RegistryEntrySupplier<Item, Item> SALTED_CHAR = food("salted_char", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> SALTED_MASU_TROUT = food("salted_masu_trout", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> SALTED_CHERRY_SALMON = food("salted_cherry_salmon", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> SALTED_RAINBOW_TROUT = food("salted_rainbow_trout", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> SALTED_SALMON = food("salted_salmon", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> SALTED_TAIMEN = food("salted_taimen", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> SALTED_CHUB = food("salted_chub", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> GRILLED_SQUID = food("grilled_squid", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> GRILLED_SUNSQUID = food("grilled_sunsquid", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> GRILLED_LAMP_SQUID = food("grilled_lamp_squid", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> GRILLED_SAND_FLOUNDER = food("grilled_sand_flounder", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> GRILLED_SHRIMP = food("grilled_shrimp", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> GRILLED_LOBSTER = food("grilled_lobster", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> GRILLED_BLOWFISH = food("grilled_blowfish", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> GRILLED_FALL_FLOUNDER = food("grilled_fall_flounder", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> GRILLED_TURBOT = food("grilled_turbot", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> GRILLED_FLOUNDER = food("grilled_flounder", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> SALTED_PIKE = food("salted_pike", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> GRILLED_NEEDLEFISH = food("grilled_needlefish", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> DRIED_SARDINES = food("dried_sardines", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> TUNA_TERIYAKI = food("tuna_teriyaki", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> SALTED_POND_SMELT = food("salted_pond_smelt", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> GRILLED_YELLOWTAIL = food("grilled_yellowtail", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> GRILLED_MACKEREL = food("grilled_mackerel", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> GRILLED_SKIPJACK = food("grilled_skipjack", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> GRILLED_LOVER_SNAPPER = food("grilled_lover_snapper", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> GRILLED_GLITTER_SNAPPER = food("grilled_glitter_snapper", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> GRILLED_GIRELLA = food("grilled_girella", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> GRILLED_SNAPPER = food("grilled_snapper", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> GRILLED_GIBELIO = food("grilled_gibelio", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);
    public static final RegistryEntrySupplier<Item, Item> GRILLED_CRUCIAN_CARP = food("grilled_crucian_carp", Texture.N, RunecraftoryTags.Items.FOOD_FRIED);

    public static final RegistryEntrySupplier<Item, Item> CHAR_SASHIMI = food("char_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);
    public static final RegistryEntrySupplier<Item, Item> TROUT_SASHIMI = food("trout_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);
    public static final RegistryEntrySupplier<Item, Item> CHERRY_SASHIMI = food("cherry_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);
    public static final RegistryEntrySupplier<Item, Item> RAINBOW_SASHIMI = food("rainbow_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);
    public static final RegistryEntrySupplier<Item, Item> SALMON_SASHIMI = food("salmon_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);
    public static final RegistryEntrySupplier<Item, Item> TAIMEN_SASHIMI = food("taimen_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);
    public static final RegistryEntrySupplier<Item, Item> SQUID_SASHIMI = food("squid_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);
    public static final RegistryEntrySupplier<Item, Item> SUNSQUID_SASHIMI = food("sunsquid_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);
    public static final RegistryEntrySupplier<Item, Item> LAMP_SQUID_SASHIMI = food("lamp_squid_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);
    public static final RegistryEntrySupplier<Item, Item> SHRIMP_SASHIMI = food("shrimp_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);
    public static final RegistryEntrySupplier<Item, Item> LOBSTER_SASHIMI = food("lobster_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);
    public static final RegistryEntrySupplier<Item, Item> BLOWFISH_SASHIMI = food("blowfish_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);
    public static final RegistryEntrySupplier<Item, Item> FALL_SASHIMI = food("fall_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);
    public static final RegistryEntrySupplier<Item, Item> TURBOT_SASHIMI = food("turbot_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);
    public static final RegistryEntrySupplier<Item, Item> FLOUNDER_SASHIMI = food("flounder_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);
    public static final RegistryEntrySupplier<Item, Item> PIKE_SASHIMI = food("pike_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);
    public static final RegistryEntrySupplier<Item, Item> NEEDLEFISH_SASHIMI = food("needlefish_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);
    public static final RegistryEntrySupplier<Item, Item> SARDINE_SASHIMI = food("sardine_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);
    public static final RegistryEntrySupplier<Item, Item> TUNA_SASHIMI = food("tuna_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);
    public static final RegistryEntrySupplier<Item, Item> YELLOWTAIL_SASHIMI = food("yellowtail_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);
    public static final RegistryEntrySupplier<Item, Item> SKIPJACK_SASHIMI = food("skipjack_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);
    public static final RegistryEntrySupplier<Item, Item> GIRELLA_SASHIMI = food("girella_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);
    public static final RegistryEntrySupplier<Item, Item> LOVER_SASHIMI = food("lover_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);
    public static final RegistryEntrySupplier<Item, Item> GLITTER_SASHIMI = food("glitter_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);
    public static final RegistryEntrySupplier<Item, Item> SNAPPER_SASHIMI = food("snapper_sashimi", Texture.N, RunecraftoryTags.Items.KNIFE);

    public static final RegistryEntrySupplier<Item, Item> DISASTROUS_DISH = food("disastrous_dish", Texture.Y, LOW_FOOD_PROP);
    public static final RegistryEntrySupplier<Item, Item> FAILED_DISH = food("failed_dish", Texture.Y, LOW_FOOD_PROP);
    public static final RegistryEntrySupplier<Item, Item> MIXED_HERBS = food("mixed_herbs", Texture.Y, LOW_FOOD_PROP);
    public static final RegistryEntrySupplier<Item, Item> SOUR_DROP = food("sour_drop", Texture.Y, LOW_FOOD_PROP);
    public static final RegistryEntrySupplier<Item, Item> SWEET_POWDER = food("sweet_powder", Texture.Y, LOW_FOOD_PROP, RunecraftoryTags.Items.SUGAR);
    public static final RegistryEntrySupplier<Item, Item> HEAVY_SPICE = food("heavy_spice", Texture.Y, LOW_FOOD_PROP);
    public static final RegistryEntrySupplier<Item, Item> ORANGE = food("orange", Texture.Y, LOW_FOOD_PROP);
    public static final RegistryEntrySupplier<Item, Item> GRAPES = food("grapes", Texture.Y, LOW_FOOD_PROP);
    public static final RegistryEntrySupplier<Item, Item> MEALY_APPLE = food("mealy_apple", Texture.Y, LOW_FOOD_PROP);

    public static final RegistryEntrySupplier<Item, ItemRecipeBread> FORGING_BREAD = register("forging_bread", () -> new ItemRecipeBread(CraftingType.FORGE, new Item.Properties().stacksTo(16)), RuneCraftoryCreativeTabs.FOOD);
    public static final RegistryEntrySupplier<Item, ItemRecipeBread> ACCESSORY_BREAD = register("accessory_bread", () -> new ItemRecipeBread(CraftingType.ACCESSORY_WORKBENCH, new Item.Properties().stacksTo(16)), RuneCraftoryCreativeTabs.FOOD);
    public static final RegistryEntrySupplier<Item, ItemRecipeBread> MEDICINE_BREAD = register("medicine_bread", () -> new ItemRecipeBread(CraftingType.CHEMISTRY_SET, new Item.Properties().stacksTo(16)), RuneCraftoryCreativeTabs.FOOD);
    public static final RegistryEntrySupplier<Item, ItemRecipeBread> COOKING_BREAD = register("cooking_bread", () -> new ItemRecipeBread(CraftingType.COOKING_TABLE, new Item.Properties().stacksTo(16)), RuneCraftoryCreativeTabs.FOOD);

    public static final RegistryEntrySupplier<Item, BlockItem> SHIPPING_BIN = blockItem("shipping_bin", () -> RuneCraftoryBlocks.SHIPPING);
    public static final RegistryEntrySupplier<Item, BlockItem> SPAWNER = blockItem("boss_spawner", () -> RuneCraftoryBlocks.BOSS_SPAWNER, RuneCraftoryCreativeTabs.MONSTERS);
    public static final RegistryEntrySupplier<Item, BlockItem> CASH_REGISTER = blockItem("cash_register", () -> RuneCraftoryBlocks.CASH_REGISTER);
    public static final RegistryEntrySupplier<Item, BlockItem> MONSTER_BARN = blockItem("monster_barn", () -> RuneCraftoryBlocks.MONSTER_BARN);
    public static final RegistryEntrySupplier<Item, QuestBoardItem> QUEST_BOARD = register("quest_board", () -> new QuestBoardItem(RuneCraftoryBlocks.QUEST_BOARD.get(), new Item.Properties()), RuneCraftoryCreativeTabs.BLOCKS);
    public static final RegistryEntrySupplier<Item, BucketItem> HOT_SPRING_BUCKET = register("hot_spring_bucket", () -> new BucketItem(RuneCraftoryFluids.HOT_SPRING_WATER.get(), new Item.Properties().stacksTo(1)), RuneCraftoryCreativeTabs.BLOCKS);

    public static final RegistryEntrySupplier<Item, Item> ICON_0 = register("icon_0", () -> new Item(new Item.Properties()));
    public static final RegistryEntrySupplier<Item, ItemDebug> DEBUG = register("debug_item", () -> new ItemDebug(new Item.Properties()));
    public static final RegistryEntrySupplier<Item, ItemLevelUp> LEVEL = register("level_item", () -> new ItemLevelUp(new Item.Properties()));
    public static final RegistryEntrySupplier<Item, ItemSkillUp> SKILL = register("skill_item", () -> new ItemSkillUp(new Item.Properties()));
    public static final RegistryEntrySupplier<Item, Item> TAME = register("insta_tame", () -> new Item(new Item.Properties()) {
        @Override
        public boolean isFoil(ItemStack stack) {
            return true;
        }
    });
    public static final RegistryEntrySupplier<Item, Item> UNKNOWN = register("unknown", () -> new Item(new Item.Properties()));
    public static final RegistryEntrySupplier<Item, Item> ORC_MAZE = register("orc_maze", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryEntrySupplier<Item, ItemProp> STEEL_SWORD_PROP = register("steel_sword_prop", () -> new ItemProp(new Item.Properties().stacksTo(1), () -> new ItemStack(RuneCraftoryItems.STEEL_SWORD.get())));
    public static final RegistryEntrySupplier<Item, ItemProp> CUTLASS_PROP = register("cutlass_prop", () -> new ItemProp(new Item.Properties().stacksTo(1), () -> new ItemStack(RuneCraftoryItems.CUTLASS.get())));
    public static final RegistryEntrySupplier<Item, ItemProp> THIEF_KNIFE_PROP = register("thief_knife_prop", () -> new ItemProp(new Item.Properties().stacksTo(1), () -> new ItemStack(RuneCraftoryItems.THIEF_KNIFE.get())));

    public static final RegistryEntrySupplier<Item, BabySpawnEgg> NPC_BABY = register("baby", () -> new BabySpawnEgg(new Item.Properties().stacksTo(1)));

    private static RegistryEntrySupplier<Item, ItemToolHoe> hoe(ToolItemTier tier) {
        RegistryEntrySupplier<Item, ItemToolHoe> sup = register("hoe_" + tier.getName(), () -> new ItemToolHoe(new Item.Properties()
                .component(DataComponents.UNBREAKABLE, new Unbreakable(false))
                .component(DataComponents.RARITY, tier == ToolItemTier.PLATINUM ? Rarity.EPIC : Rarity.COMMON)
                .component(RuneCraftoryDataComponentTypes.TOOL_TIER.get(), tier)), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen())
            DATAGENTAGS.computeIfAbsent(RunecraftoryTags.Items.HOES, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    private static RegistryEntrySupplier<Item, ItemToolWateringCan> wateringCan(ToolItemTier tier) {
        RegistryEntrySupplier<Item, ItemToolWateringCan> sup = register("watering_can_" + tier.getName(), () -> new ItemToolWateringCan(new Item.Properties()
                .component(DataComponents.RARITY, tier == ToolItemTier.PLATINUM ? Rarity.EPIC : Rarity.COMMON)
                .component(RuneCraftoryDataComponentTypes.TOOL_TIER.get(), tier)
                .component(RuneCraftoryDataComponentTypes.MAX_WATER.get(), GeneralConfig.getWaterFrom(tier))), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen())
            DATAGENTAGS.computeIfAbsent(RunecraftoryTags.Items.WATERINGCANS, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    private static RegistryEntrySupplier<Item, ItemToolSickle> sickle(ToolItemTier tier) {
        RegistryEntrySupplier<Item, ItemToolSickle> sup = register("sickle_" + tier.getName(), () -> new ItemToolSickle(new Item.Properties()
                .component(DataComponents.UNBREAKABLE, new Unbreakable(false))
                .component(DataComponents.RARITY, tier == ToolItemTier.PLATINUM ? Rarity.EPIC : Rarity.COMMON)
                .component(RuneCraftoryDataComponentTypes.TOOL_TIER.get(), tier)), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen())
            DATAGENTAGS.computeIfAbsent(RunecraftoryTags.Items.SICKLES, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    private static RegistryEntrySupplier<Item, ItemToolHammer> hammerTool(ToolItemTier tier) {
        RegistryEntrySupplier<Item, ItemToolHammer> sup = register("hammer_" + tier.getName(), () -> new ItemToolHammer(new Item.Properties()
                .component(DataComponents.UNBREAKABLE, new Unbreakable(false))
                .component(DataComponents.RARITY, tier == ToolItemTier.PLATINUM ? Rarity.EPIC : Rarity.COMMON)
                .component(RuneCraftoryDataComponentTypes.TOOL_TIER.get(), tier)), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen())
            DATAGENTAGS.computeIfAbsent(RunecraftoryTags.Items.HAMMER_TOOLS, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    private static RegistryEntrySupplier<Item, ItemToolAxe> axeTool(ToolItemTier tier) {
        RegistryEntrySupplier<Item, ItemToolAxe> sup = register("axe_" + tier.getName(), () -> new ItemToolAxe(new Item.Properties()
                .component(DataComponents.UNBREAKABLE, new Unbreakable(false))
                .component(DataComponents.RARITY, tier == ToolItemTier.PLATINUM ? Rarity.EPIC : Rarity.COMMON)
                .component(RuneCraftoryDataComponentTypes.TOOL_TIER.get(), tier)), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen())
            DATAGENTAGS.computeIfAbsent(RunecraftoryTags.Items.AXE_TOOLS, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    private static RegistryEntrySupplier<Item, ItemToolFishingRod> fishingRod(ToolItemTier tier) {
        RegistryEntrySupplier<Item, ItemToolFishingRod> sup = register("fishing_rod_" + tier.getName(), () -> new ItemToolFishingRod(new Item.Properties()
                .component(DataComponents.RARITY, tier == ToolItemTier.PLATINUM ? Rarity.EPIC : Rarity.COMMON)
                .component(RuneCraftoryDataComponentTypes.TOOL_TIER.get(), tier).stacksTo(1)), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen())
            DATAGENTAGS.computeIfAbsent(RunecraftoryTags.Items.FISHING_RODS, t -> new ArrayList<>()).add(sup);
        return sup;
    }

    private static RegistryEntrySupplier<Item, ItemShortSwordBase> shortSword(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item, ItemShortSwordBase> sup = register(name, () -> new ItemShortSwordBase(new Item.Properties()
                    .component(DataComponents.UNBREAKABLE, new Unbreakable(false))
                    .component(RuneCraftoryDataComponentTypes.ATTACK_ACTION.get(), AttackActionData.of(RuneCraftoryAttackActions.SHORT_SWORD))));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item, ItemShortSwordBase> sup = register(name, () -> new ItemShortSwordBase(new Item.Properties()
                .component(DataComponents.UNBREAKABLE, new Unbreakable(false))
                .component(RuneCraftoryDataComponentTypes.ATTACK_ACTION.get(), AttackActionData.of(RuneCraftoryAttackActions.SHORT_SWORD))), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            DATAGENTAGS.computeIfAbsent(RunecraftoryTags.Items.SHORTSWORDS, t -> new ArrayList<>()).add(sup);
            TIER_3_CHEST.add(sup);
        }
        return sup;
    }

    private static RegistryEntrySupplier<Item, ItemLongSwordBase> longSword(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item, ItemLongSwordBase> sup = register(name, () -> new ItemLongSwordBase(new Item.Properties()
                    .component(DataComponents.UNBREAKABLE, new Unbreakable(false))
                    .component(RuneCraftoryDataComponentTypes.SHIELD_EFFICIENCY.get(), 0.5f)
                    .component(RuneCraftoryDataComponentTypes.ATTACK_ACTION.get(), AttackActionData.of(RuneCraftoryAttackActions.LONG_SWORD))));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item, ItemLongSwordBase> sup = register(name, () -> new ItemLongSwordBase(new Item.Properties()
                .component(DataComponents.UNBREAKABLE, new Unbreakable(false))
                .component(RuneCraftoryDataComponentTypes.SHIELD_EFFICIENCY.get(), 0.5f)
                .component(RuneCraftoryDataComponentTypes.ATTACK_ACTION.get(), AttackActionData.of(RuneCraftoryAttackActions.LONG_SWORD))), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            DATAGENTAGS.computeIfAbsent(RunecraftoryTags.Items.LONGSWORDS, t -> new ArrayList<>()).add(sup);
            TIER_3_CHEST.add(sup);
        }
        return sup;
    }

    private static RegistryEntrySupplier<Item, ItemSpearBase> spear(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item, ItemSpearBase> sup = register(name, () -> new ItemSpearBase(new Item.Properties()
                    .stacksTo(1)
                    .component(RuneCraftoryDataComponentTypes.SHIELD_EFFICIENCY.get(), 0.5f)
                    .component(RuneCraftoryDataComponentTypes.ATTACK_ACTION.get(), AttackActionData.of(RuneCraftoryAttackActions.SPEAR))));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item, ItemSpearBase> sup = register(name, () -> new ItemSpearBase(new Item.Properties()
                .stacksTo(1)
                .component(RuneCraftoryDataComponentTypes.SHIELD_EFFICIENCY.get(), 0.5f)
                .component(RuneCraftoryDataComponentTypes.ATTACK_ACTION.get(), AttackActionData.of(RuneCraftoryAttackActions.SPEAR))), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            DATAGENTAGS.computeIfAbsent(RunecraftoryTags.Items.SPEARS, t -> new ArrayList<>()).add(sup);
            TIER_3_CHEST.add(sup);
        }
        return sup;
    }

    private static RegistryEntrySupplier<Item, ItemAxeBase> axe(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item, ItemAxeBase> sup = register(name, () -> new ItemAxeBase(new Item.Properties()
                    .component(DataComponents.UNBREAKABLE, new Unbreakable(false))
                    .component(RuneCraftoryDataComponentTypes.SHIELD_EFFICIENCY.get(), 0.5f)
                    .component(RuneCraftoryDataComponentTypes.ATTACK_ACTION.get(), AttackActionData.of(RuneCraftoryAttackActions.HAMMER_AXE))));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item, ItemAxeBase> sup = register(name, () -> new ItemAxeBase(new Item.Properties()
                .component(DataComponents.UNBREAKABLE, new Unbreakable(false))
                .component(RuneCraftoryDataComponentTypes.SHIELD_EFFICIENCY.get(), 0.5f)
                .component(RuneCraftoryDataComponentTypes.ATTACK_ACTION.get(), AttackActionData.of(RuneCraftoryAttackActions.HAMMER_AXE))), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            DATAGENTAGS.computeIfAbsent(RunecraftoryTags.Items.AXES, t -> new ArrayList<>()).add(sup);
            TIER_3_CHEST.add(sup);
        }
        return sup;
    }

    private static RegistryEntrySupplier<Item, ItemHammerBase> hammer(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item, ItemHammerBase> sup = register(name, () -> new ItemHammerBase(new Item.Properties()
                    .component(DataComponents.UNBREAKABLE, new Unbreakable(false))
                    .component(RuneCraftoryDataComponentTypes.SHIELD_EFFICIENCY.get(), 0.5f)
                    .component(RuneCraftoryDataComponentTypes.ATTACK_ACTION.get(), AttackActionData.of(RuneCraftoryAttackActions.HAMMER_AXE))));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item, ItemHammerBase> sup = register(name, () -> new ItemHammerBase(new Item.Properties()
                .component(DataComponents.UNBREAKABLE, new Unbreakable(false))
                .component(RuneCraftoryDataComponentTypes.SHIELD_EFFICIENCY.get(), 0.5f)
                .component(RuneCraftoryDataComponentTypes.ATTACK_ACTION.get(), AttackActionData.of(RuneCraftoryAttackActions.HAMMER_AXE))), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            DATAGENTAGS.computeIfAbsent(RunecraftoryTags.Items.HAMMERS, t -> new ArrayList<>()).add(sup);
            TIER_3_CHEST.add(sup);
        }
        return sup;
    }

    private static RegistryEntrySupplier<Item, ItemDualBladeBase> dualBlade(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item, ItemDualBladeBase> sup = register(name, () -> new ItemDualBladeBase(new Item.Properties()
                    .component(DataComponents.UNBREAKABLE, new Unbreakable(false))
                    .component(RuneCraftoryDataComponentTypes.SHIELD_EFFICIENCY.get(), 0f)
                    .component(RuneCraftoryDataComponentTypes.ATTACK_ACTION.get(), AttackActionData.of(RuneCraftoryAttackActions.DUAL_BLADES))));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item, ItemDualBladeBase> sup = register(name, () -> new ItemDualBladeBase(new Item.Properties()
                .component(DataComponents.UNBREAKABLE, new Unbreakable(false))
                .component(RuneCraftoryDataComponentTypes.SHIELD_EFFICIENCY.get(), 0f)
                .component(RuneCraftoryDataComponentTypes.ATTACK_ACTION.get(), AttackActionData.of(RuneCraftoryAttackActions.DUAL_BLADES))), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            DATAGENTAGS.computeIfAbsent(RunecraftoryTags.Items.DUALBLADES, t -> new ArrayList<>()).add(sup);
            TIER_3_CHEST.add(sup);
        }
        return sup;
    }

    private static RegistryEntrySupplier<Item, ItemGloveBase> gloves(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item, ItemGloveBase> sup = register(name, () -> new ItemGloveBase(new Item.Properties()
                    .stacksTo(1)
                    .component(RuneCraftoryDataComponentTypes.SHIELD_EFFICIENCY.get(), 0f)
                    .component(RuneCraftoryDataComponentTypes.ATTACK_ACTION.get(), AttackActionData.of(RuneCraftoryAttackActions.GLOVES))));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item, ItemGloveBase> sup = register(name, () -> new ItemGloveBase(new Item.Properties()
                .stacksTo(1)
                .component(RuneCraftoryDataComponentTypes.SHIELD_EFFICIENCY.get(), 0f)
                .component(RuneCraftoryDataComponentTypes.ATTACK_ACTION.get(), AttackActionData.of(RuneCraftoryAttackActions.GLOVES))), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            DATAGENTAGS.computeIfAbsent(RunecraftoryTags.Items.FISTS, t -> new ArrayList<>()).add(sup);
            TIER_3_CHEST.add(sup);
        }
        return sup;
    }

    private static RegistryEntrySupplier<Item, ItemStaffBase> staff(String name, ItemElement starterElement, int amount, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item, ItemStaffBase> sup = register(name, () -> new ItemStaffBase(starterElement, amount, new Item.Properties()
                    .component(RuneCraftoryDataComponentTypes.SHIELD_EFFICIENCY.get(), 0.5f)
                    .component(RuneCraftoryDataComponentTypes.ATTACK_ACTION.get(), AttackActionData.of(RuneCraftoryAttackActions.STAFF)).stacksTo(1)));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item, ItemStaffBase> sup = register(name, () -> new ItemStaffBase(starterElement, amount, new Item.Properties()
                .component(RuneCraftoryDataComponentTypes.SHIELD_EFFICIENCY.get(), 0.5f)
                .component(RuneCraftoryDataComponentTypes.ATTACK_ACTION.get(), AttackActionData.of(RuneCraftoryAttackActions.STAFF)).stacksTo(1)), RuneCraftoryCreativeTabs.WEAPON_TOOL_TAB);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            DATAGENTAGS.computeIfAbsent(RunecraftoryTags.Items.STAFFS, t -> new ArrayList<>()).add(sup);
            TIER_3_CHEST.add(sup);
        }
        return sup;
    }

    private static RegistryEntrySupplier<Item, ItemArmorBase> equipment(ArmorItem.Type slot, String name, Texture texture) {
        return equipment(slot, name, texture, false);
    }

    private static RegistryEntrySupplier<Item, ItemArmorBase> equipment(ArmorItem.Type slot, String name, Texture texture, boolean useItemTexture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item, ItemArmorBase> sup = register(name, () -> new ItemArmorBase(slot, new Item.Properties().stacksTo(1), RuneCraftory.modRes(name), useItemTexture));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item, ItemArmorBase> sup = register(name, () -> new ItemArmorBase(slot, new Item.Properties().stacksTo(1), RuneCraftory.modRes(name), useItemTexture), RuneCraftoryCreativeTabs.EQUIPMENT);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            TIER_3_CHEST.add(sup);
            switch (slot) {
                case BOOTS ->
                        DATAGENTAGS.computeIfAbsent(RunecraftoryTags.Items.BOOTS, t -> new ArrayList<>()).add(sup);
                case LEGGINGS ->
                        DATAGENTAGS.computeIfAbsent(RunecraftoryTags.Items.ACCESSORIES, t -> new ArrayList<>()).add(sup);
                case CHESTPLATE ->
                        DATAGENTAGS.computeIfAbsent(RunecraftoryTags.Items.CHESTPLATE, t -> new ArrayList<>()).add(sup);
                case HELMET ->
                        DATAGENTAGS.computeIfAbsent(RunecraftoryTags.Items.HELMET, t -> new ArrayList<>()).add(sup);
            }
        }
        return sup;
    }

    private static RegistryEntrySupplier<Item, ShieldItem> shield(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item, ShieldItem> sup = register(name, () -> new ShieldItem(new Item.Properties().stacksTo(1)));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item, ShieldItem> sup = register(name, () -> new ShieldItem(new Item.Properties().stacksTo(1)), RuneCraftoryCreativeTabs.EQUIPMENT);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            TIER_3_CHEST.add(sup);
            DATAGENTAGS.computeIfAbsent(RunecraftoryTags.Items.SHIELDS, t -> new ArrayList<>()).add(sup);
        }
        return sup;
    }

    private static RegistryEntrySupplier<Item, BlockItem> blockItem(String name, Supplier<Supplier<? extends Block>> block) {
        return register(name, () -> new BlockItem(block.get().get(), new Item.Properties()), RuneCraftoryCreativeTabs.BLOCKS);
    }

    private static RegistryEntrySupplier<Item, BlockItem> blockItem(String name, Supplier<Supplier<? extends Block>> block, ResourceLocation group) {
        return register(name, () -> new BlockItem(block.get().get(), new Item.Properties()), group);
    }

    private static RegistryEntrySupplier<Item, BlockItem> mineral(MineralBlockTier tier) {
        Supplier<Block> block = () -> RuneCraftoryBlocks.MINERAL_MAP.get(tier).get();
        return register("ore_" + tier.getSerializedName(), () -> new BlockItem(block.get(), new Item.Properties()), RuneCraftoryCreativeTabs.BLOCKS);
    }

    private static RegistryEntrySupplier<Item, BlockItem> brokenMineral(MineralBlockTier tier) {
        Supplier<Block> block = () -> RuneCraftoryBlocks.BROKEN_MINERAL_MAP.get(tier).get();
        return register("ore_broken_" + tier.getSerializedName(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static RegistryEntrySupplier<Item, Item> mat(String name, Texture texture) {
        return mat(name, Rarity.COMMON, texture);
    }

    private static RegistryEntrySupplier<Item, Item> mat(String name, Rarity rarity, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item, Item> sup = register(name, () -> new Item(new Item.Properties().rarity(rarity)));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item, Item> sup = register(name, () -> new Item(new Item.Properties().rarity(rarity)), RuneCraftoryCreativeTabs.MATERIALS);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            if (rarity == Rarity.COMMON)
                TIER_1_CHEST.add(sup);
        }
        return sup;
    }

    private static RegistryEntrySupplier<Item, ItemMedicine> medicine(String name, boolean affectStats) {
        RegistryEntrySupplier<Item, ItemMedicine> sup = register(name, () -> new ItemMedicine(affectStats, new Item.Properties().food(FOOD_PROP).stacksTo(16)), RuneCraftoryCreativeTabs.MEDICINE);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen())
            TIER_2_CHEST.add(sup);
        return sup;
    }

    private static RegistryEntrySupplier<Item, Item> drinkable(String name) {
        RegistryEntrySupplier<Item, Item> sup = register(name, () -> new Item(new Item.Properties().food(FOOD_PROP).stacksTo(16)) {
            @Override
            public UseAnim getUseAnimation(ItemStack stack) {
                return UseAnim.DRINK;
            }
        }, RuneCraftoryCreativeTabs.MEDICINE);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen())
            TIER_2_CHEST.add(sup);
        return sup;
    }

    private static RegistryEntrySupplier<Item, ItemSpell> spell(Supplier<Supplier<? extends Spell>> sup, String name, int type) {
        return spell(sup, name, false, type);
    }

    private static RegistryEntrySupplier<Item, ItemSpell> spell(Supplier<Supplier<? extends Spell>> sup, String name, boolean canHold, int type) {
        RegistryEntrySupplier<Item, ItemSpell> ret = register(name, () -> canHold ?
                new ItemHoldSpell(sup.get(), new Item.Properties().stacksTo(1)) :
                new ItemSpell(sup.get(), new Item.Properties().stacksTo(1)), RuneCraftoryCreativeTabs.SPELLS);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            TIER_2_CHEST.add(ret);
            DATAGENTAGS.computeIfAbsent(RunecraftoryTags.Items.SPELLS, t -> new ArrayList<>()).add(ret);
            if (type == 2)
                DATAGENTAGS.computeIfAbsent(RunecraftoryTags.Items.MAGIC_SPELLS, t -> new ArrayList<>()).add(ret);
            else if (type == 1)
                DATAGENTAGS.computeIfAbsent(RunecraftoryTags.Items.RUNE_ABILITIES, t -> new ArrayList<>()).add(ret);
        }
        return ret;
    }

    private static RegistryEntrySupplier<Item, Item> fish(String name, Texture texture) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item, Item> sup = register(name, () -> new Item(new Item.Properties()));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item, Item> sup = register(name, () -> new Item(new Item.Properties()), RuneCraftoryCreativeTabs.FOOD);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen())
            TIER_1_CHEST.add(sup);
        return sup;
    }

    private static RegistryEntrySupplier<Item, ItemNameBlockItem> seed(String name, Supplier<Supplier<? extends Block>> block) {
        RegistryEntrySupplier<Item, ItemNameBlockItem> sup = register(name + "_seeds", () -> new ItemNameBlockItem(block.get().get(), new Item.Properties()), RuneCraftoryCreativeTabs.FARMING);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen())
            SEEDS.add(sup);
        return sup;
    }

    private static RegistryEntrySupplier<Item, Item> crop(String name, RegistryEntrySupplier<Item, ?> small, Texture texture, int type) {
        return cropWith(name, small != null ? small.getID() : null, texture, type);
    }

    /**
     * @param type 0 for veggetables, 1 for fruits, 2 for flowers
     */
    private static RegistryEntrySupplier<Item, Item> cropWith(String name, ResourceLocation small, Texture texture, int type) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item, Item> sup;
            if (small != null)
                sup = register(name, () -> new Item(new Item.Properties().food(GIANT_CROP_FOOD_PROP)));
            else
                sup = register(name, () -> new Item(new Item.Properties().food(FOOD_PROP)));
            NOTEX.add(sup);
            if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
                if (small != null)
                    GIANT_CROPS.add(sup);
            }
            return sup;
        }
        RegistryEntrySupplier<Item, Item> sup;
        if (small != null)
            sup = register(name, () -> new Item(new Item.Properties().food(GIANT_CROP_FOOD_PROP)), RuneCraftoryCreativeTabs.FARMING);
        else
            sup = register(name, () -> new Item(new Item.Properties().food(FOOD_PROP)), RuneCraftoryCreativeTabs.FARMING);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            TIER_1_CHEST.add(sup);
            if (small != null)
                GIANT_CROPS.add(sup);
            String tagName = small != null ? small.getPath() : name;
            switch (type) {
                case 0 -> VEGGIES.add(Pair.of(tagName, sup));
                case 1 -> FRUITS.add(Pair.of(tagName, sup));
                case 2 -> FLOWERS.add(Pair.of(tagName, sup));
            }
        }
        return sup;
    }

    private static RegistryEntrySupplier<Item, BlockItem> herb(String name, Supplier<Supplier<? extends Block>> block) {
        RegistryEntrySupplier<Item, BlockItem> sup = register(name, () -> new BlockItem(block.get().get(), new Item.Properties().food(LOW_FOOD_PROP)), RuneCraftoryCreativeTabs.MEDICINE);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen())
            TIER_1_CHEST.add(sup);
        return sup;
    }

    @SafeVarargs
    private static RegistryEntrySupplier<Item, Item> food(String name, Texture texture, TagKey<Item>... tags) {
        return food(name, texture, HIGH_FOOD_PROP, tags);
    }

    @SafeVarargs
    private static RegistryEntrySupplier<Item, Item> food(String name, Texture texture, FoodProperties foodProp, TagKey<Item>... tags) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item, Item> sup = register(name, () -> new Item(new Item.Properties().food(foodProp)));
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item, Item> sup = register(name, () -> new Item(new Item.Properties().food(foodProp)), RuneCraftoryCreativeTabs.FOOD);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            TIER_2_CHEST.add(sup);
            FOOD.add(sup);
            for (TagKey<Item> tag : tags) {
                DATAGENTAGS.computeIfAbsent(tag, t -> new ArrayList<>()).add(sup);
            }
        }
        return sup;
    }

    @SafeVarargs
    private static RegistryEntrySupplier<Item, Item> drinkable(String name, Texture texture, FoodProperties foodProp, TagKey<Item>... tags) {
        if (texture == Texture.N) {
            RegistryEntrySupplier<Item, Item> sup = register(name, () -> new Item(new Item.Properties().food(foodProp)) {
                @Override
                public UseAnim getUseAnimation(ItemStack stack) {
                    return UseAnim.DRINK;
                }
            });
            NOTEX.add(sup);
            return sup;
        }
        RegistryEntrySupplier<Item, Item> sup = register(name, () -> new Item(new Item.Properties().food(foodProp)) {
            @Override
            public UseAnim getUseAnimation(ItemStack stack) {
                return UseAnim.DRINK;
            }
        }, RuneCraftoryCreativeTabs.FOOD);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            TIER_2_CHEST.add(sup);
            FOOD.add(sup);
            for (TagKey<Item> tag : tags) {
                DATAGENTAGS.computeIfAbsent(tag, t -> new ArrayList<>()).add(sup);
            }
        }
        return sup;
    }

    private static <T extends Item> RegistryEntrySupplier<Item, T> register(String name, Supplier<T> item) {
        return register(name, item, null);
    }

    public static <T extends Item> RegistryEntrySupplier<Item, T> register(String name, Supplier<T> item, ResourceLocation tab) {
        RegistryEntrySupplier<Item, T> res = ITEMS.register(name, item);
        if (tab != null) {
            RuneCraftoryCreativeTabs.appendTo(tab, res);
        }
        return res;
    }

    public static List<RegistryEntrySupplier<Item, ?>> ribbons() {
        return List.of(RuneCraftoryItems.BLUE_RIBBON, RuneCraftoryItems.GREEN_RIBBON, RuneCraftoryItems.PURPLE_RIBBON, RuneCraftoryItems.BLACK_RIBBON,
                RuneCraftoryItems.YELLOW_RIBBON, RuneCraftoryItems.RED_RIBBON, RuneCraftoryItems.ORANGE_RIBBON, RuneCraftoryItems.WHITE_RIBBON,
                RuneCraftoryItems.INDIGO_RIBBON);
    }

    public static List<RegistryEntrySupplier<Item, ?>> hatItems() {
        return List.of(RuneCraftoryItems.STRAW_HAT, RuneCraftoryItems.FANCY_HAT);
    }

    //Here till all items have a texture
    public enum Texture {
        Y,
        N
    }
}
