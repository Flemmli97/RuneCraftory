package io.github.flemmli97.runecraftory.common.lib;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public class RunecraftoryTags {

    // Items

    public static final TagKey<Item> SEEDS = tagCommon("seeds");

    public static final TagKey<Item> WOOD_ROD = tagCommon("wooden_rods");
    public static final TagKey<Item> SLIME = tagCommon("slime_balls");

    public static final TagKey<Item> IRON = tagCommon("iron_ingots");
    public static final TagKey<Item> GOLD = tagCommon("gold_ingots");
    public static final TagKey<Item> TIN = tagCommon("tin_ingots");
    public static final TagKey<Item> COPPER = tagCommon("copper_ingots");

    public static final TagKey<Item> EMERALDS = tagCommon("emeralds");

    public static final TagKey<Item> SHEARS = tagCommon("shears");

    public static final TagKey<Item> COBBLESTONE = tagCommon("cobblestone");
    public static final TagKey<Item> CHEST = tagCommon("chests");

    public static final TagKey<Item> BRONZE = tagCommon("bronze_ingots");
    public static final TagKey<Item> SILVER = tagCommon("silver_ingots");
    public static final TagKey<Item> PLATINUM = tagCommon("platinum_ingots");
    public static final TagKey<Item> ORICHALCUM = tagCommon("orichalcum");
    public static final TagKey<Item> DRAGONIC = tagCommon("dragonic");

    public static final TagKey<Item> AMETHYSTS = tagCommon("amethysts");
    public static final TagKey<Item> AQUAMARINES = tagCommon("aquamarines");
    public static final TagKey<Item> RUBIES = tagCommon("rubies");
    public static final TagKey<Item> SAPPHIRES = tagCommon("sapphires");

    public static final TagKey<Item> EGGS = tagCommon("eggs");
    public static final TagKey<Item> MILKS = tagCommon("milks");

    public static final TagKey<Item> GRAPES = tagCommon("fruits/grapes");
    public static final TagKey<Item> ORANGE = tagCommon("fruits/orange");

    public static final TagKey<Item> VEGGIES = tagCommon("vegetables");
    public static final TagKey<Item> FRUITS = tagCommon("fruits");
    public static final TagKey<Item> FLOWERS = tagCommon("flowers");
    public static final TagKey<Item> CROPS = tagCommon("crops");

    public static final TagKey<Item> FOODS = tagCommon("foods");
    public static final TagKey<Item> TURNIP = tagCommon("crops/turnip");

    // Gifts
    public static final TagKey<Item> GENERIC_TRASH = tag("generic_trash");

    // Runefactory categories
    public static final TagKey<Item> MINERALS = tag("mineral");
    public static final TagKey<Item> JEWELS = tag("jewels");
    public static final TagKey<Item> CRYSTALS = tag("crystals");
    public static final TagKey<Item> STICKS = tag("sticks");
    public static final TagKey<Item> LIQUIDS = tag("liquids");
    public static final TagKey<Item> FEATHERS = tagCommon("feathers");
    public static final TagKey<Item> SHELLS_BONES = tag("shells_bones");
    public static final TagKey<Item> STONES = tag("stones");
    public static final TagKey<Item> STRINGS = tagCommon("string");
    public static final TagKey<Item> SHARDS = tag("shards");
    public static final TagKey<Item> FURS = tag("furs");
    public static final TagKey<Item> POWDERS = tag("powders");
    public static final TagKey<Item> CLOTHS = tag("cloths");
    public static final TagKey<Item> CLAWS_FANGS = tag("claws_fangs");
    public static final TagKey<Item> SCALES = tag("scales");

    // Other
    public static final TagKey<Item> SPELLS = tag("spells");
    public static final TagKey<Item> MAGIC_SPELLS = tag("magic_spells");
    public static final TagKey<Item> RUNE_ABILITIES = tag("rune_abilities");
    public static final TagKey<Item> HIGH_TIER_TOOLS = tag("high_tier_tools");

    public static final TagKey<Item> UPGRADABLE_HELD = tag("upgradable_held");

    public static final TagKey<Item> TOOLS = tag("tools");
    public static final TagKey<Item> HOES = tag("tools/hoes");
    public static final TagKey<Item> WATERINGCANS = tag("tools/wateringcans");
    public static final TagKey<Item> SICKLES = tag("tools/sickles");
    public static final TagKey<Item> HAMMER_TOOLS = tag("tools/hammers");
    public static final TagKey<Item> AXE_TOOLS = tag("tools/axes");
    public static final TagKey<Item> FISHING_RODS = tag("tools/fishing_rods");

    public static final TagKey<Item> WEAPONS = tag("weapon");
    public static final TagKey<Item> SHORTSWORDS = tag("weapon/short_swords");
    public static final TagKey<Item> LONGSWORDS = tag("weapon/long_swords");
    public static final TagKey<Item> SPEARS = tag("weapon/spears");
    public static final TagKey<Item> AXES = tag("weapon/axes");
    public static final TagKey<Item> HAMMERS = tag("weapon/hammers");
    public static final TagKey<Item> HAMMER_AXES = tag("weapon/hammers_and_axes");
    public static final TagKey<Item> DUALBLADES = tag("weapon/dual_blades");
    public static final TagKey<Item> FISTS = tag("weapon/fists");
    public static final TagKey<Item> STAFFS = tag("weapon/staffs");

    public static final TagKey<Item> EQUIPMENT = tag("equipment");
    public static final TagKey<Item> HELMET = tag("equipment/helmet");
    public static final TagKey<Item> CHESTPLATE = tag("equipment/chestplate");
    public static final TagKey<Item> ACCESSORIES = tag("equipment/accessories");
    public static final TagKey<Item> BOOTS = tag("equipment/boots");
    public static final TagKey<Item> SHIELDS = tag("equipment/shields");

    public static final TagKey<Item> QUICKHARVEST_BYPASS = tag("quick_harvest_bypass");

    // FOOD
    public static final TagKey<Item> SIMPLE = tag("food/simple");
    public static final TagKey<Item> FRIED = tag("food/fried");
    public static final TagKey<Item> POT = tag("food/pot");
    public static final TagKey<Item> STEAMED = tag("food/steamed");
    public static final TagKey<Item> KNIFE = tag("food/knife");
    public static final TagKey<Item> MIXED = tag("food/mixed");
    public static final TagKey<Item> OVEN = tag("food/oven");

    public static final TagKey<Item> OIL = tagCommon("foods/oil");
    public static final TagKey<Item> FLOUR = tagCommon("foods/flour");
    public static final TagKey<Item> CHEESE = tagCommon("foods/cheese");
    public static final TagKey<Item> MAYO = tagCommon("foods/mayo");
    public static final TagKey<Item> FOOD_EGG = tagCommon("foods/egg");
    public static final TagKey<Item> FOOD_MILK = tagCommon("foods/milk");
    public static final TagKey<Item> CHOCOLATE = tagCommon("foods/chocolate");
    public static final TagKey<Item> RICE = tagCommon("foods/rice");
    public static final TagKey<Item> BUTTER = tagCommon("foods/butter");
    public static final TagKey<Item> KETCHUP = tagCommon("foods/ketchup");
    public static final TagKey<Item> SUGAR = tagCommon("foods/sugar");
    public static final TagKey<Item> BREAD = tagCommon("foods/bread");

    public static final TagKey<Item> ONIGIRI = tagCommon("foods/onigiri");
    public static final TagKey<Item> PIE = tagCommon("foods/pie");
    public static final TagKey<Item> JUICE = tagCommon("foods/juice");
    public static final TagKey<Item> TOAST = tagCommon("foods/toast");
    public static final TagKey<Item> UDON = tagCommon("foods/udon");
    public static final TagKey<Item> JAM = tagCommon("foods/jam");

    private static final Map<EntityType<?>, TagKey<Item>> ENTITY_TAMING_TAGS = new HashMap<>();

    // Blocks
    public static final TagKey<Block> ORES = block("ores");

    public static final TagKey<Block> FARMLAND = blockCommon("farmland");
    public static final TagKey<Block> SICKLE_DESTROYABLE = block("sickle_destroyable");
    public static final TagKey<Block> HAMMER_FLATTENABLE = block("hammer_flattenable");
    public static final TagKey<Block> HAMMER_BREAKABLE = block("hammer_breakable");
    public static final TagKey<Block> HERBS = block("herbs");
    public static final TagKey<Block> SICKLE_EFFECTIVE = blockCommon("mineable/sickle");

    public static final TagKey<Block> CROP_BLOCKS = block("crops");
    public static final TagKey<Block> FLOWER_BLOCKS = block("flowers");
    public static final TagKey<Block> GIANT_CROP_BLOCKS = block("giant_crop");

    public static final TagKey<Block> ENDSTONES = blockCommon("end_stones");

    public static final TagKey<Block> MONSTER_CLEARABLE = block("monster_clearable");

    public static final TagKey<Block> BARN_GROUND = block("barn_ground");
    public static final TagKey<Block> ONSEN_PROVIDER = block("onsen_provider");

    public static final TagKey<Block> STONE = blockCommon("stone");

    public static final TagKey<Block> MINERAL_GEN_PLACE = block("mineral_gen_place");

    // Entities

    public static final TagKey<EntityType<?>> MONSTERS = entity("monsters");
    public static final TagKey<EntityType<?>> BOSS_MONSTERS = entity("boss_monsters");

    public static final TagKey<EntityType<?>> BOSSES = entityCommon("bosses");

    public static final TagKey<EntityType<?>> RAFFLESIA_SUMMONS = entity("rafflesia_summons");

    public static final TagKey<EntityType<?>> HELD_WEAPON_EXEMPT = entity("held_weapon_exempt");

    /**
     * Tag for entities that normally target hostile mobs but shouldn't target monster if they are tamed
     * E.g. iron golems and snow golems
     */
    public static final TagKey<EntityType<?>> TAMED_MONSTER_IGNORE = entity("ignore_tamed_monsters");

    // Biomes
    public static final TagKey<Biome> IS_HOT = biomeCommon("is_hot");

    public static final TagKey<Biome> IS_SPARSE = biomeCommon("is_sparse");
    public static final TagKey<Biome> IS_DENSE_OVERWORLD = biomeCommon("is_dense/overworld");

    public static final TagKey<Biome> IS_WET = biomeCommon("is_wet");
    public static final TagKey<Biome> IS_DRY_OVERWORLD = biomeCommon("is_dry/overworld");

    public static final TagKey<Biome> IS_SAVANNA = biomeCommon("is_savanna");

    public static final TagKey<Biome> IS_SPOOKY = biomeCommon("is_spooky");
    public static final TagKey<Biome> IS_DEAD = biomeCommon("is_dead");
    public static final TagKey<Biome> IS_LUSH = biomeCommon("is_lush");
    public static final TagKey<Biome> IS_MUSHROOM = biomeCommon("is_mushroom");
    public static final TagKey<Biome> IS_MAGICAL = biomeCommon("is_magical");

    public static final TagKey<Biome> IS_WATER = biomeCommon("is_water");

    public static final TagKey<Biome> IS_PLAINS = biomeCommon("is_plains");
    public static final TagKey<Biome> IS_SWAMP = biomeCommon("is_swamp");
    public static final TagKey<Biome> IS_SANDY = biomeCommon("is_sandy");
    public static final TagKey<Biome> IS_SNOWY = biomeCommon("is_snowy");
    public static final TagKey<Biome> IS_WASTELAND = biomeCommon("is_wasteland");
    public static final TagKey<Biome> IS_BEACH = biomeCommon("is_beach");

    public static final TagKey<Biome> IS_PEAK = biomeCommon("is_peak");
    public static final TagKey<Biome> IS_SLOPE = biomeCommon("is_slope");

    public static final TagKey<Biome> IS_END = biomeCommon("is_end");

    public static final TagKey<Biome> NETHER_END = biome("nether_end");

    public static final TagKey<Biome> AQUAMARINE_GEN = biome("aquamarine_gen");
    public static final TagKey<Biome> AMETHYST_GEN = biome("amethyst_gen");
    public static final TagKey<Biome> RUBY_GEN = biome("ruby_gen");
    public static final TagKey<Biome> EMERALD_GEN = biome("emerald_gen");
    public static final TagKey<Biome> SAPPHIRE_GEN = biome("sapphire_gen");

    public static final TagKey<Biome> WATER_NETHER_END = biome("water_nether_end");
    public static final TagKey<Biome> MUSHROOM_GEN = biome("mushroom_gen");
    public static final TagKey<Biome> INDIGO_GEN = biome("indigo_gen");
    public static final TagKey<Biome> PURPLE_GEN = biome("purple_gen");
    public static final TagKey<Biome> BLUE_GEN = biome("blue_gen");
    public static final TagKey<Biome> WATER_END = biome("water_end");
    public static final TagKey<Biome> YELLOW_GEN = biome("yellow_gen");
    public static final TagKey<Biome> ORANGE_GEN = biome("orange_gen");

    public static final TagKey<Biome> BAMBOO_GEN = biome("bamboo_gen");
    public static final TagKey<Biome> GENERAL_HERBS = biome("herb_tree_biomes");

    // Ref Neoforge
    public static final TagKey<DamageType> IS_MAGIC = damageCommon("is_magic");
    public static final TagKey<DamageType> BYPASS_MAGIC = damageCommon("bypass_magic");

    private static TagKey<Item> tag(String name) {
        return TagKey.create(Registries.ITEM, RuneCraftory.modRes(name));
    }

    public static TagKey<Item> tagCommon(String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", name));
    }

    private static TagKey<Block> block(String name) {
        return TagKey.create(Registries.BLOCK, RuneCraftory.modRes(name));
    }

    private static TagKey<Block> blockCommon(String name) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", name));
    }

    private static TagKey<Biome> biome(String name) {
        return TagKey.create(Registries.BIOME, RuneCraftory.modRes(name));
    }

    private static TagKey<Biome> biomeCommon(String name) {
        return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("c", name));
    }

    private static TagKey<EntityType<?>> entity(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, RuneCraftory.modRes(name));
    }

    private static TagKey<EntityType<?>> entityCommon(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("c", name));
    }

    private static TagKey<DamageType> damageCommon(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("c", name));
    }

    public static TagKey<Item> tamingTag(EntityType<?> type) {
        return ENTITY_TAMING_TAGS.computeIfAbsent(type, r -> tag("taming/" + BuiltInRegistries.ENTITY_TYPE.getKey(type).getPath()));
    }
}
