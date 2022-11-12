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
    public static final TagKey<Item> bronzeF = forge("ingots/bronze");
    public static final TagKey<Item> silverF = forge("ingots/silver");
    public static final TagKey<Item> platinumF = forge("ingots/platinum");

    public static final TagKey<Item> amethystF = forge("gems/amethyst");
    public static final TagKey<Item> aquamarineF = forge("gems/aquamarine");
    public static final TagKey<Item> rubyF = forge("gems/ruby");
    public static final TagKey<Item> sapphireF = forge("gems/sapphire");

    //Items

    public static final TagKey<Item> seeds = tagCommon("seeds");

    public static final TagKey<Item> wood_rod = tagCommon("wooden_rods");
    public static final TagKey<Item> slime = tagCommon("slime_balls");

    public static final TagKey<Item> iron = tagCommon("iron_ingots");
    public static final TagKey<Item> gold = tagCommon("gold_ingots");
    public static final TagKey<Item> copper = tagCommon("copper_ingots");
    public static final TagKey<Item> copperBlock = tagCommon("copper_blocks");

    public static final TagKey<Item> emerald = tagCommon("emeralds");

    public static final TagKey<Item> shears = tagCommon("shears");

    public static final TagKey<Item> cobblestone = tagCommon("cobblestone");
    public static final TagKey<Item> chest = tagCommon("chest");

    public static final TagKey<Item> bronze = tagCommon("bronze_ingots");
    public static final TagKey<Item> silver = tagCommon("silver_ingots");
    public static final TagKey<Item> platinum = tagCommon("platinum_ingots");
    public static final TagKey<Item> orichalcum = tagCommon("orichalcum");
    public static final TagKey<Item> dragonic = tagCommon("dragonic");

    public static final TagKey<Item> amethyst = tagCommon("amethysts");
    public static final TagKey<Item> aquamarine = tagCommon("aquamarines");
    public static final TagKey<Item> ruby = tagCommon("rubies");
    public static final TagKey<Item> sapphire = tagCommon("sapphires");

    public static final TagKey<Item> eggs = tagCommon("eggs");
    public static final TagKey<Item> milk = tagCommon("milks");

    public static final TagKey<Item> minerals = tag("mineral");
    public static final TagKey<Item> jewels = tag("jewels");
    public static final TagKey<Item> crystals = tag("crystals");
    public static final TagKey<Item> sticks = tag("sticks");
    public static final TagKey<Item> liquids = tag("liquids");
    public static final TagKey<Item> feathers = tagCommon("feathers");
    public static final TagKey<Item> shellBone = tag("shells_bones");
    public static final TagKey<Item> stones = tag("stones");
    public static final TagKey<Item> strings = tagCommon("string");
    public static final TagKey<Item> shards = tag("shards");
    public static final TagKey<Item> furs = tag("furs");
    public static final TagKey<Item> powders = tag("powders");
    public static final TagKey<Item> cloths = tag("cloths");
    public static final TagKey<Item> clawFangs = tag("claws_fangs");
    public static final TagKey<Item> scales = tag("scales");

    public static final TagKey<Item> spells = tag("spells");
    public static final TagKey<Item> high_tier_tools = tag("high_tier_tools");

    public static final TagKey<Item> woolyTamer = tag("wooly_tame");
    public static final TagKey<Item> marionettaTamer = tag("marionetta_tame");

    public static final TagKey<Item> chest_t1 = tag("chest_t1");
    public static final TagKey<Item> chest_t2 = tag("chest_t2");
    public static final TagKey<Item> chest_t3 = tag("chest_t3");
    public static final TagKey<Item> chest_t4 = tag("chest_t4");

    private static final Map<EntityType<?>, TagKey<Item>> entityTamingTags = new HashMap<>();

    //Blocks
    public static final TagKey<Block> farmland = blockCommon("farmland");
    public static final TagKey<Block> sickleDestroyable = blockMod("sickle_destroyable");
    public static final TagKey<Block> hammerFlattenable = blockMod("hammer_flattenable");
    public static final TagKey<Block> hammerBreakable = blockMod("hammer_breakable");
    public static final TagKey<Block> herbs = blockMod("herbs");
    public static final TagKey<Block> sickleEffective = blockCommon("mineable/sickle");

    public static final TagKey<Block> ENDSTONES = blockCommon("end_stones");

    public static final TagKey<Block> SNOWLAYER = blockCommon("snow_layer");

    //Entities
    public static final TagKey<EntityType<?>> monsters = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(RuneCraftory.MODID, "monsters"));
    public static final TagKey<EntityType<?>> bossMonsters = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(RuneCraftory.MODID, "boss_monsters"));


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

    public static final TagKey<Biome> nether_end = biomeMod("nether_end");

    public static final TagKey<Biome> aquamarine_gen = biomeMod("aquamarine_gen");
    public static final TagKey<Biome> amethyst_gen = biomeMod("amethyst_gen");
    public static final TagKey<Biome> ruby_gen = biomeMod("ruby_gen");
    public static final TagKey<Biome> emerald_gen = biomeMod("emerald_gen");
    public static final TagKey<Biome> sapphire_gen = biomeMod("sapphire_gen");

    public static final TagKey<Biome> water_nether_end = biomeMod("water_nether_end");
    public static final TagKey<Biome> mushroom_gen = biomeMod("mushroom_gen");
    public static final TagKey<Biome> indigo_gen = biomeMod("indigo_gen");
    public static final TagKey<Biome> purple_gen = biomeMod("purple_gen");
    public static final TagKey<Biome> blue_gen = biomeMod("blue_gen");
    public static final TagKey<Biome> water_end = biomeMod("water_end");
    public static final TagKey<Biome> yellow_gen = biomeMod("yellow_gen");
    public static final TagKey<Biome> orange_gen = biomeMod("orange_gen");

    public static final TagKey<Biome> bamboo_gen = biomeMod("bamboo_gen");
    public static final TagKey<Biome> general_herb = biomeMod("herb_tree_biomes");

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
        return entityTamingTags.computeIfAbsent(type, r -> tag("taming/" + PlatformUtils.INSTANCE.entities().getIDFrom(type).getPath()));
    }
}
