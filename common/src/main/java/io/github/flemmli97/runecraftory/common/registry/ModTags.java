package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public class ModTags {

    //Forge Tags
    public static final TagKey<Item> BRONZE_F = forge("ingots/bronze");
    public static final TagKey<Item> SILVER_F = forge("ingots/silver");
    public static final TagKey<Item> PLATINUM_F = forge("ingots/platinum");

    public static final TagKey<Item> AMETHYST_F = forge("gems/amethyst");
    public static final TagKey<Item> AQUAMARINE_F = forge("gems/aquamarine");
    public static final TagKey<Item> RUBY_F = forge("gems/ruby");
    public static final TagKey<Item> SAPPHIRE_F = forge("gems/sapphire");

    //General items vanilla/modded compat
    public static final TagKey<Item> SEEDS = tagCommon("seeds");

    public static final TagKey<Item> WOOD_ROD = tagCommon("wooden_rods");
    public static final TagKey<Item> SLIME = tagCommon("slime_balls");

    public static final TagKey<Item> IRON = tagCommon("iron_ingots");
    public static final TagKey<Item> GOLD = tagCommon("gold_ingots");
    public static final TagKey<Item> COPPER = tagCommon("copper_ingots");
    public static final TagKey<Item> COPPER_BLOCK = tagCommon("copper_blocks");

    public static final TagKey<Item> EMERALDS = tagCommon("emeralds");

    public static final TagKey<Item> SHEARS = tagCommon("shears");

    public static final TagKey<Item> COBBLESTONE = tagCommon("cobblestone");
    public static final TagKey<Item> CHEST = tagCommon("chest");

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

    //Runefactory categories
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

    //Other
    public static final TagKey<Item> SPELLS = tag("spells");
    public static final TagKey<Item> HIGH_TIER_TOOLS = tag("high_tier_tools");

    public static final TagKey<Item> WEAPONS = tag("weapon");
    public static final TagKey<Item> SHORTSWORDS = tag("weapon/short_swords");
    public static final TagKey<Item> LONGSWORDS = tag("weapon/long_swords");
    public static final TagKey<Item> SPEARS = tag("weapon/spears");
    public static final TagKey<Item> AXES = tag("weapon/axes");
    public static final TagKey<Item> HAMMERS = tag("weapon/hammers");
    public static final TagKey<Item> DUALBLADES = tag("weapon/dual_blades");
    public static final TagKey<Item> FISTS = tag("weapon/fists");
    public static final TagKey<Item> STAFFS = tag("weapon/staffs");

    public static final TagKey<Item> VEGGIES = tagCommon("vegetables");
    public static final TagKey<Item> FRUITS = tagCommon("fruits");
    public static final TagKey<Item> FLOWERS = tagCommon("flowers");
    public static final TagKey<Item> CROPS = tagCommon("crops");

    //Loot
    public static final TagKey<Item> CHEST_T1 = tag("chest_t1");
    public static final TagKey<Item> CHEST_T2 = tag("chest_t2");
    public static final TagKey<Item> CHEST_T3 = tag("chest_t3");
    public static final TagKey<Item> CHEST_T4 = tag("chest_t4");

    private static final Map<EntityType<?>, TagKey<Item>> ENTITY_TAMING_TAGS = new HashMap<>();

    //Blocks
    public static final TagKey<Block> FARMLAND = blockCommon("farmland");
    public static final TagKey<Block> SICKLE_DESTROYABLE = blockMod("sickle_destroyable");
    public static final TagKey<Block> HAMMER_FLATTENABLE = blockMod("hammer_flattenable");
    public static final TagKey<Block> HAMMER_BREAKABLE = blockMod("hammer_breakable");
    public static final TagKey<Block> HERBS = blockMod("herbs");
    public static final TagKey<Block> SICKLE_EFFECTIVE = blockCommon("mineable/sickle");

    public static final TagKey<Block> ENDSTONES = blockCommon("end_stones");

    //Entities
    public static final TagKey<EntityType<?>> MONSTERS = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(RuneCraftory.MODID, "monsters"));
    public static final TagKey<EntityType<?>> BOSS_MONSTERS = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(RuneCraftory.MODID, "boss_monsters"));

    public static final TagKey<EntityType<?>> BOSSES = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("c", "bosses"));


    //BIOME Tags via forges BiomeDictionary
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

    public static final TagKey<Biome> NETHER_END = biomeMod("nether_end");

    public static final TagKey<Biome> AQUAMARINE_GEN = biomeMod("aquamarine_gen");
    public static final TagKey<Biome> AMETHYST_GEN = biomeMod("amethyst_gen");
    public static final TagKey<Biome> RUBY_GEN = biomeMod("ruby_gen");
    public static final TagKey<Biome> EMERALD_GEN = biomeMod("emerald_gen");
    public static final TagKey<Biome> SAPPHIRE_GEN = biomeMod("sapphire_gen");

    public static final TagKey<Biome> WATER_NETHER_END = biomeMod("water_nether_end");
    public static final TagKey<Biome> MUSHROOM_GEN = biomeMod("mushroom_gen");
    public static final TagKey<Biome> INDIGO_GEN = biomeMod("indigo_gen");
    public static final TagKey<Biome> PURPLE_GEN = biomeMod("purple_gen");
    public static final TagKey<Biome> BLUE_GEN = biomeMod("blue_gen");
    public static final TagKey<Biome> WATER_END = biomeMod("water_end");
    public static final TagKey<Biome> YELLOW_GEN = biomeMod("yellow_gen");
    public static final TagKey<Biome> ORANGE_GEN = biomeMod("orange_gen");

    public static final TagKey<Biome> BAMBOO_GEN = biomeMod("bamboo_gen");
    public static final TagKey<Biome> GENERAL_HERBS = biomeMod("herb_tree_biomes");

    private static TagKey<Item> tag(String name) {
        return PlatformUtils.INSTANCE.itemTag(new ResourceLocation(RuneCraftory.MODID, name));
    }

    private static TagKey<Item> tagCommon(String name) {
        return PlatformUtils.INSTANCE.itemTag(new ResourceLocation("c", name));
    }

    private static TagKey<Item> forge(String name) {
        return PlatformUtils.INSTANCE.itemTag(new ResourceLocation("forge", name));
    }

    private static TagKey<Block> blockForge(String name) {
        return PlatformUtils.INSTANCE.blockTag(new ResourceLocation("forge", name));
    }

    private static TagKey<Block> blockCommon(String name) {
        return PlatformUtils.INSTANCE.blockTag(new ResourceLocation("c", name));
    }

    private static TagKey<Block> blockMod(String name) {
        return PlatformUtils.INSTANCE.blockTag(new ResourceLocation(RuneCraftory.MODID, name));
    }

    private static TagKey<Biome> biomeMod(String name) {
        return PlatformUtils.INSTANCE.tag(Registry.BIOME_REGISTRY, new ResourceLocation(RuneCraftory.MODID, name));
    }

    private static TagKey<Biome> biomeCommon(String name) {
        return PlatformUtils.INSTANCE.tag(Registry.BIOME_REGISTRY, new ResourceLocation("c", name));
    }

    public static TagKey<Item> tamingTag(EntityType<?> type) {
        return ENTITY_TAMING_TAGS.computeIfAbsent(type, r -> tag("taming/" + PlatformUtils.INSTANCE.entities().getIDFrom(type).getPath()));
    }
}
