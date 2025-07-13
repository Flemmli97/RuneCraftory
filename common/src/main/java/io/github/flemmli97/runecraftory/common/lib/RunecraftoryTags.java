package io.github.flemmli97.runecraftory.common.lib;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.blocks.MineralBlockTier;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunecraftoryTags {

    public static class Items {

        public static final TagKey<Item> WOOD_ROD = tagCommon("rods/wooden");
        public static final TagKey<Item> SLIME = tagCommon("slime_balls");
        public static final TagKey<Item> SHEARS = tagCommon("tools/shear");
        public static final TagKey<Item> COBBLESTONE = tagCommon("cobblestones");
        public static final TagKey<Item> CHEST = tagCommon("chests");

        public static final TagKey<Item> IRON = tagCommon("ingots/iron");
        public static final TagKey<Item> GOLD = tagCommon("ingots/gold");
        public static final TagKey<Item> COPPER = tagCommon("ingots/copper");

        public static final TagKey<Item> RAW_MATERIALS_TIN = tagCommon("raw_materials/tin");
        public static final TagKey<Item> INGOTS_TIN = tagCommon("ingots/tin");
        public static final TagKey<Item> DUSTS_BRONZE = tagCommon("dusts/bronze");
        public static final TagKey<Item> INGOTS_BRONZE = tagCommon("ingots/bronze");
        public static final TagKey<Item> RAW_MATERIALS_SILVER = tagCommon("raw_materials/silver");
        public static final TagKey<Item> INGOTS_SILVER = tagCommon("ingots/silver");
        public static final TagKey<Item> RAW_MATERIALS_PLATINUM = tagCommon("raw_materials/platinum");
        public static final TagKey<Item> INGOTS_PLATINUM = tagCommon("ingots/platinum");

        public static final TagKey<Item> GEMS_EMERALD = tagCommon("gems/emerald");
        public static final TagKey<Item> GEMS_AMETHYST = tagCommon("gems/amethyst");
        public static final TagKey<Item> GEMS_AQUAMARINE = tagCommon("gems/aquamarine");
        public static final TagKey<Item> GEMS_RUBY = tagCommon("gems/ruby");
        public static final TagKey<Item> GEMS_SAPPHIRE = tagCommon("gems/sapphire");

        public static final TagKey<Item> EGGS = tagCommon("eggs");
        public static final TagKey<Item> MILKS = tagCommon("milks");

        public static final TagKey<Item> SEEDS = tagCommon("seeds");

        public static final TagKey<Item> FOODS = tagCommon("foods");
        public static final TagKey<Item> FOODS_FRUIT = tagCommon("foods/fruit");
        public static final TagKey<Item> FOODS_VEGGETABLE = tagCommon("foods/vegetable");

        public static final TagKey<Item> GRAPES = tagCommon("foods/fruit/grapes");
        public static final TagKey<Item> ORANGE = tagCommon("foods/fruit/orange");

        public static final TagKey<Item> FLOWERS = tagCommon("flowers");

        public static final TagKey<Item> CROPS = tagCommon("crops");
        public static final TagKey<Item> TURNIP = tagCommon("crops/turnip");

        public static final TagKey<Item> ORICHALCUM = tagCommon("orichalcum");
        public static final TagKey<Item> DRAGONIC = tagCommon("dragonic");

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
        public static final TagKey<Item> STRINGS = tagCommon("strings");
        public static final TagKey<Item> SHARDS = tag("shards");
        public static final TagKey<Item> FURS = tag("furs");
        public static final TagKey<Item> POWDERS = tag("powders");
        public static final TagKey<Item> CLOTHS = tag("cloths");
        public static final TagKey<Item> CLAWS_FANGS = tag("claws_fangs");
        public static final TagKey<Item> SCALES = tag("scales");

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
        public static final List<TagKey<Item>> WEAPONTAGS = List.of(
                SHORTSWORDS, LONGSWORDS, SPEARS, AXES, HAMMERS, DUALBLADES, FISTS, STAFFS
        );

        public static final TagKey<Item> EQUIPMENT = tag("equipment");
        public static final TagKey<Item> HELMET = tag("equipment/helmet");
        public static final TagKey<Item> CHESTPLATE = tag("equipment/chestplate");
        public static final TagKey<Item> ACCESSORIES = tag("equipment/accessories");
        public static final TagKey<Item> BOOTS = tag("equipment/boots");
        public static final TagKey<Item> SHIELDS = tag("equipment/shields");

        public static final TagKey<Item> FORGING_BLACKLIST = tag("forging_attribute_upgrade_blacklist");
        public static final TagKey<Item> ACCESSORY_BLACKLIST = tag("accessory_attribute_upgrade_blacklist");
        public static final TagKey<Item> CHEMISTRY_BLACKLIST = tag("chemistry_attribute_upgrade_blacklist");
        public static final TagKey<Item> COOKING_BLACKLIST = tag("cooking_attribute_upgrade_blacklist");
        public static final TagKey<Item> ONE_TIME_UPGRADE = tag("one_time_upgrade");

        public static final TagKey<Item> QUICKHARVEST_BYPASS = tag("quick_harvest_bypass");

        public static final TagKey<Item> FOOD_SIMPLE = tag("food/simple");
        public static final TagKey<Item> FOOD_FRIED = tag("food/fried");
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

        /**
         * Taming items for monsters
         */
        private static final Map<EntityType<?>, TagKey<Item>> ENTITY_TAMING_TAGS = new HashMap<>();
    }

    public static class Blocks {

        // Blocks
        public static final TagKey<Block> FARMLAND = blockCommon("farmland");
        public static final TagKey<Block> STONE = blockCommon("stones");
        public static final TagKey<Block> ENDSTONES = blockCommon("end_stones");

        public static final TagKey<Block> ORES = block("ores");
        public static final TagKey<Block> SICKLE_DESTROYABLE = block("sickle_destroyable");
        public static final TagKey<Block> SICKLE_EFFECTIVE = blockCommon("mineable/sickle");
        public static final TagKey<Block> HAMMER_FLATTENABLE = block("hammer_flattenable");
        public static final TagKey<Block> HAMMER_BREAKABLE = block("hammer_breakable");

        public static final TagKey<Block> HERBS = block("herbs");
        public static final TagKey<Block> CROP_BLOCKS = block("crops");
        public static final TagKey<Block> FLOWER_BLOCKS = block("flowers");
        public static final TagKey<Block> GIANT_CROP_BLOCKS = block("giant_crop");

        public static final TagKey<Block> MONSTER_CLEARABLE = block("monster_clearable");

        public static final TagKey<Block> BARN_GROUND = block("barn_ground");
        public static final TagKey<Block> MINERAL_GEN_PLACE = block("mineral_gen_place");
    }

    public static class EntityTypes {

        public static final TagKey<EntityType<?>> BOSSES = entityCommon("bosses");

        public static final TagKey<EntityType<?>> MONSTERS = entity("monsters");
        public static final TagKey<EntityType<?>> BOSS_MONSTERS = entity("boss_monsters");
        public static final TagKey<EntityType<?>> RAFFLESIA_SUMMONS = entity("rafflesia_summons");
        public static final TagKey<EntityType<?>> HELD_WEAPON_EXEMPT = entity("held_weapon_exempt");
        /**
         * Tag for entities that normally target hostile mobs but shouldn't target monster if they are tamed
         * E.g. iron golems and snow golems
         */
        public static final TagKey<EntityType<?>> TAMED_MONSTER_IGNORE = entity("ignore_tamed_monsters");
    }

    public static class Biomes {

        /*
         * ====================
         * Copy of all common tags defined in neoforge WITHOUT vanilla mirrors.
         * E.g. IS_END is not here since vanilla has it already and for this mod the difference has no use
         */
        public static final TagKey<Biome> IS_VOID = biomeCommon("is_void");
        public static final TagKey<Biome> IS_HOT = biomeCommon("is_hot");
        public static final TagKey<Biome> IS_HOT_OVERWORLD = biomeCommon("is_hot/overworld");
        public static final TagKey<Biome> IS_HOT_NETHER = biomeCommon("is_hot/nether");
        public static final TagKey<Biome> IS_HOT_END = biomeCommon("is_hot/end");
        public static final TagKey<Biome> IS_COLD = biomeCommon("is_cold");
        public static final TagKey<Biome> IS_COLD_OVERWORLD = biomeCommon("is_cold/overworld");
        public static final TagKey<Biome> IS_COLD_NETHER = biomeCommon("is_cold/nether");
        public static final TagKey<Biome> IS_COLD_END = biomeCommon("is_cold/end");
        public static final TagKey<Biome> IS_SPARSE_VEGETATION = biomeCommon("is_sparse_vegetation");
        public static final TagKey<Biome> IS_SPARSE_VEGETATION_OVERWORLD = biomeCommon("is_sparse_vegetation/overworld");
        public static final TagKey<Biome> IS_SPARSE_VEGETATION_NETHER = biomeCommon("is_sparse_vegetation/nether");
        public static final TagKey<Biome> IS_SPARSE_VEGETATION_END = biomeCommon("is_sparse_vegetation/end");
        public static final TagKey<Biome> IS_DENSE_VEGETATION = biomeCommon("is_dense_vegetation");
        public static final TagKey<Biome> IS_DENSE_VEGETATION_OVERWORLD = biomeCommon("is_dense_vegetation/overworld");
        public static final TagKey<Biome> IS_DENSE_VEGETATION_NETHER = biomeCommon("is_dense_vegetation/nether");
        public static final TagKey<Biome> IS_DENSE_VEGETATION_END = biomeCommon("is_dense_vegetation/end");
        public static final TagKey<Biome> IS_WET = biomeCommon("is_wet");
        public static final TagKey<Biome> IS_WET_OVERWORLD = biomeCommon("is_wet/overworld");
        public static final TagKey<Biome> IS_WET_NETHER = biomeCommon("is_wet/nether");
        public static final TagKey<Biome> IS_WET_END = biomeCommon("is_wet/end");
        public static final TagKey<Biome> IS_DRY = biomeCommon("is_dry");
        public static final TagKey<Biome> IS_DRY_OVERWORLD = biomeCommon("is_dry/overworld");
        public static final TagKey<Biome> IS_DRY_NETHER = biomeCommon("is_dry/nether");
        public static final TagKey<Biome> IS_DRY_END = biomeCommon("is_dry/end");
        public static final TagKey<Biome> IS_CONIFEROUS_TREE = biomeCommon("is_tree/coniferous");
        public static final TagKey<Biome> IS_SAVANNA_TREE = biomeCommon("is_tree/savanna");
        public static final TagKey<Biome> IS_JUNGLE_TREE = biomeCommon("is_tree/jungle");
        public static final TagKey<Biome> IS_DECIDUOUS_TREE = biomeCommon("is_tree/deciduous");
        public static final TagKey<Biome> IS_MOUNTAIN_PEAK = biomeCommon("is_mountain/peak");
        public static final TagKey<Biome> IS_MOUNTAIN_SLOPE = biomeCommon("is_mountain/slope");
        public static final TagKey<Biome> IS_PLAINS = biomeCommon("is_plains");
        public static final TagKey<Biome> IS_SNOWY_PLAINS = biomeCommon("is_snowy_plains");
        public static final TagKey<Biome> IS_BIRCH_FOREST = biomeCommon("is_birch_forest");
        public static final TagKey<Biome> IS_FLOWER_FOREST = biomeCommon("is_flower_forest");
        public static final TagKey<Biome> IS_OLD_GROWTH = biomeCommon("is_old_growth");
        public static final TagKey<Biome> IS_WINDSWEPT = biomeCommon("is_windswept");
        public static final TagKey<Biome> IS_SWAMP = biomeCommon("is_swamp");
        public static final TagKey<Biome> IS_DESERT = biomeCommon("is_desert");
        public static final TagKey<Biome> IS_STONY_SHORES = biomeCommon("is_stony_shores");
        public static final TagKey<Biome> IS_MUSHROOM = biomeCommon("is_mushroom");
        public static final TagKey<Biome> IS_SHALLOW_OCEAN = biomeCommon("is_shallow_ocean");
        public static final TagKey<Biome> IS_UNDERGROUND = biomeCommon("is_underground");
        public static final TagKey<Biome> IS_CAVE = biomeCommon("is_cave");
        public static final TagKey<Biome> IS_LUSH = biomeCommon("is_lush");
        public static final TagKey<Biome> IS_MAGICAL = biomeCommon("is_magical");
        public static final TagKey<Biome> IS_RARE = biomeCommon("is_rare");
        public static final TagKey<Biome> IS_PLATEAU = biomeCommon("is_plateau");
        public static final TagKey<Biome> IS_MODIFIED = biomeCommon("is_modified");
        public static final TagKey<Biome> IS_SPOOKY = biomeCommon("is_spooky");
        public static final TagKey<Biome> IS_WASTELAND = biomeCommon("is_wasteland");
        public static final TagKey<Biome> IS_DEAD = biomeCommon("is_dead");
        public static final TagKey<Biome> IS_FLORAL = biomeCommon("is_floral");
        public static final TagKey<Biome> IS_SANDY = biomeCommon("is_sandy");
        public static final TagKey<Biome> IS_SNOWY = biomeCommon("is_snowy");
        public static final TagKey<Biome> IS_ICY = biomeCommon("is_icy");
        public static final TagKey<Biome> IS_AQUATIC = biomeCommon("is_aquatic");
        public static final TagKey<Biome> IS_AQUATIC_ICY = biomeCommon("is_aquatic_icy");
        public static final TagKey<Biome> IS_NETHER_FOREST = biomeCommon("is_nether_forest");
        public static final TagKey<Biome> IS_OUTER_END_ISLAND = biomeCommon("is_outer_end_island");
        /*
         * ====================
         */
        public static final TagKey<Biome> VANILLA_DIMENSIONS = biome("vanilla_dimensions");

        public static final TagKey<Biome> COMMON_GROUND_BLACKLIST = biome("common_ground_blacklist");
        public static final TagKey<Biome> GENERAL_HERBS = biome("general_herbs");

        // Structure gen tags
        public static final TagKey<Biome> FOREST_GROVE = biome("forest_grove");
        public static final TagKey<Biome> WATER_RUINS = biome("water_ruins");
        public static final TagKey<Biome> THEATER_RUINS = biome("theater_ruins");
        public static final TagKey<Biome> PLAINS_ARENA = biome("plains_arena");
        public static final TagKey<Biome> DESERT_ARENA = biome("desert_arena");
        public static final TagKey<Biome> NETHER_ARENA = biome("nether_arena");
        public static final TagKey<Biome> WIND_SHRINE = biome("wind_shrine");
        public static final TagKey<Biome> LEON_KARNAK = biome("leon_karnak");

        public static TagKey<Biome> getMineralGenTag(MineralBlockTier mineral, boolean whitelist) {
            return biome(mineral.getSerializedName() + (whitelist ? "_whitelist" : "_blacklist"));
        }

        public static TagKey<Biome> getBlockBasedGenerationTag(RegistryEntrySupplier<Block, ?> block, boolean whitelist) {
            String path = block.getID().getPath().replace("ore_", "");
            return biome(path + (whitelist ? "_whitelist" : "_blacklist"));
        }

        public record BiomeGenerationTags(List<TagKey<Biome>> whitelist, List<TagKey<Biome>> blacklist) {

        }
    }

    public static class Fluids {

        public static final TagKey<Fluid> HOT_SPRING_FLUID = TagKey.create(Registries.FLUID, RuneCraftory.modRes("hot_spring_water"));
    }

    public static class Attributes {

        public static final TagKey<Attribute> WEAPON_ONLY = TagKey.create(Registries.ATTRIBUTE, RuneCraftory.modRes("weapon_only"));
        public static final TagKey<Attribute> ARMOR_ONLY = TagKey.create(Registries.ATTRIBUTE, RuneCraftory.modRes("armor_only"));
        public static final TagKey<Attribute> NON_INHERITABLE = TagKey.create(Registries.ATTRIBUTE, RuneCraftory.modRes("non_inheritable"));
        public static final TagKey<Attribute> PERCENTAGE_DISPLAY = TagKey.create(Registries.ATTRIBUTE, RuneCraftory.modRes("percentage_display"));
        public static final TagKey<Attribute> DISPLAY_IGNORED = TagKey.create(Registries.ATTRIBUTE, RuneCraftory.modRes("display_ignored"));
    }

    public static class DamageTypes {

        // Neoforge damage types
        public static final TagKey<DamageType> IS_MAGIC = damageCommon("is_magic");
        public static final TagKey<DamageType> BYPASS_MAGIC = damageCommon("bypass_magic");
    }

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
        return Items.ENTITY_TAMING_TAGS.computeIfAbsent(type, r -> tag("taming/" + BuiltInRegistries.ENTITY_TYPE.getKey(type).getPath()));
    }
}
