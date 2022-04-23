package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class ModTags {

    //Forge Tags
    public static final TagKey<Item> bronzeF = forge("ingots/bronze");
    public static final TagKey<Item> silverF = forge("ingots/silver");
    public static final TagKey<Item> platinumF = forge("ingots/platinum");

    //Items
    public static final TagKey<Item> bronze = tag("bronze_ingots");
    public static final TagKey<Item> silver = tag("silver_ingots");
    public static final TagKey<Item> platinum = tag("platinum_ingots");
    public static final TagKey<Item> orichalcum = tag("gems/orichalcum");
    public static final TagKey<Item> dragonic = tag("gems/dragonic");
    public static final TagKey<Item> minerals = tag("mineral");

    public static final TagKey<Item> woolyTamer = tag("wooly_tame");

    //Blocks
    public static final TagKey<Block> farmland = blockCommon("farmland");
    public static final TagKey<Block> farmlandTill = blockCommon("farmland_tillable");
    public static final TagKey<Block> sickleDestroyable = blockCommon("sickle_destroyable");
    public static final TagKey<Block> hammerFlattenable = blockCommon("hammer_flattenable");
    public static final TagKey<Block> hammerBreakable = blockCommon("hammer_breakable");
    public static final TagKey<Block> herbs = blockCommon("herbs");
    public static final TagKey<Block> sickleEffective = blockCommon("mineable/sickle");

    public static final TagKey<Block> ENDSTONES = blockCommon("end_stones");

    //BIOME Tags via forges BiomeDictionary
    public static final TagKey<Biome> IS_HOT = biomeCommon("is_hot");

    public static final TagKey<Biome> IS_SPARSE = biomeCommon("is_sparse");
    public static final TagKey<Biome> IS_DENSE_OVERWORLD = biomeCommon("is_dense/overworld");

    public static final TagKey<Biome> IS_WET = biomeCommon("is_wet");
    public static final TagKey<Biome> IS_DRY_OVERWORLD = biomeCommon("is_dry/overworld");

    public static final TagKey<Biome> IS_SAVANNA = biomeCommon("is_savanna");
    public static final TagKey<Biome> IS_JUNGLE = biomeCommon("is_jungle");

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

    private static TagKey<Item> forge(String name) {
        return PlatformUtils.INSTANCE.itemTag(new ResourceLocation("forge", name));
    }

    private static TagKey<Block> blockForge(String name) {
        return PlatformUtils.INSTANCE.blockTag(new ResourceLocation("forge", name));
    }

    private static TagKey<Block> blockCommon(String name) {
        return PlatformUtils.INSTANCE.blockTag(new ResourceLocation("c", name));
    }

    private static TagKey<Biome> biomeMod(String name) {
        return PlatformUtils.INSTANCE.tag(Registry.BIOME_REGISTRY, new ResourceLocation(RuneCraftory.MODID, name));
    }

    private static TagKey<Biome> biomeCommon(String name) {
        return PlatformUtils.INSTANCE.tag(Registry.BIOME_REGISTRY, new ResourceLocation("c", name));
    }
}
