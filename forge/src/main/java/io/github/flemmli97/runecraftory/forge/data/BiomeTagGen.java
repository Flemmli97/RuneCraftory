package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.HashSet;
import java.util.Set;

public class BiomeTagGen extends TagsProvider<Biome> {

    private final Set<TagKey<Biome>> addedTags = new HashSet<>();

    @SuppressWarnings("deprecation")
    public BiomeTagGen(DataGenerator arg, ExistingFileHelper fileHelper) {
        super(arg, BuiltinRegistries.BIOME, RuneCraftory.MODID, fileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(Biomes.PLAINS, ModTags.IS_PLAINS);
        this.tag(Biomes.DESERT, ModTags.IS_HOT, ModTags.IS_DRY_OVERWORLD, ModTags.IS_SANDY);
        this.tag(Biomes.SWAMP, ModTags.IS_WET, ModTags.IS_SWAMP);
        this.tag(Biomes.NETHER_WASTES, ModTags.IS_HOT);
        this.tag(Biomes.THE_END, ModTags.IS_END);
        this.tag(Biomes.FROZEN_OCEAN, ModTags.IS_SNOWY);
        this.tag(Biomes.FROZEN_RIVER, ModTags.IS_SNOWY);
        this.tag(Biomes.SNOWY_PLAINS, ModTags.IS_SNOWY, ModTags.IS_WASTELAND, ModTags.IS_PLAINS);
        this.tag(Biomes.MUSHROOM_FIELDS, ModTags.IS_MUSHROOM);
        this.tag(Biomes.BEACH, ModTags.IS_BEACH);
        this.tag(Biomes.JUNGLE, ModTags.IS_HOT, ModTags.IS_WET, ModTags.IS_DENSE_OVERWORLD);
        this.tag(Biomes.SPARSE_JUNGLE, ModTags.IS_HOT, ModTags.IS_WET, ModTags.IS_WET);
        this.tag(Biomes.STONY_SHORE, ModTags.IS_BEACH);
        this.tag(Biomes.SNOWY_BEACH, ModTags.IS_BEACH, ModTags.IS_SNOWY);
        this.tag(Biomes.DARK_FOREST, ModTags.IS_SPOOKY);
        this.tag(Biomes.SNOWY_TAIGA, ModTags.IS_SNOWY);
        this.tag(Biomes.WINDSWEPT_FOREST, ModTags.IS_SPARSE);
        this.tag(Biomes.SAVANNA, ModTags.IS_HOT, ModTags.IS_SAVANNA, ModTags.IS_SPARSE);
        this.tag(Biomes.SAVANNA_PLATEAU, ModTags.IS_HOT, ModTags.IS_SAVANNA, ModTags.IS_SPARSE, ModTags.IS_SLOPE);
        this.tag(Biomes.BADLANDS, ModTags.IS_SANDY, ModTags.IS_DRY_OVERWORLD);
        this.tag(Biomes.WOODED_BADLANDS, ModTags.IS_SANDY, ModTags.IS_DRY_OVERWORLD, ModTags.IS_SPARSE, ModTags.IS_SLOPE);
        this.tag(Biomes.MEADOW, ModTags.IS_PLAINS, ModTags.IS_SLOPE);
        this.tag(Biomes.GROVE, ModTags.IS_SNOWY, ModTags.IS_SLOPE);
        this.tag(Biomes.SNOWY_SLOPES, ModTags.IS_SPARSE, ModTags.IS_SNOWY, ModTags.IS_SLOPE);
        this.tag(Biomes.JAGGED_PEAKS, ModTags.IS_SPARSE, ModTags.IS_SNOWY, ModTags.IS_PEAK);
        this.tag(Biomes.FROZEN_PEAKS, ModTags.IS_SPARSE, ModTags.IS_SNOWY, ModTags.IS_PEAK);
        this.tag(Biomes.STONY_PEAKS, ModTags.IS_HOT, ModTags.IS_PEAK);
        this.tag(Biomes.SMALL_END_ISLANDS, ModTags.IS_END);
        this.tag(Biomes.END_MIDLANDS, ModTags.IS_END);
        this.tag(Biomes.END_HIGHLANDS, ModTags.IS_END);
        this.tag(Biomes.END_BARRENS, ModTags.IS_END);
        this.tag(Biomes.WARM_OCEAN, ModTags.IS_HOT);
        this.tag(Biomes.SUNFLOWER_PLAINS, ModTags.IS_PLAINS);
        this.tag(Biomes.WINDSWEPT_GRAVELLY_HILLS, ModTags.IS_SPARSE);
        this.tag(Biomes.ICE_SPIKES, ModTags.IS_SNOWY);
        this.tag(Biomes.OLD_GROWTH_BIRCH_FOREST, ModTags.IS_DENSE_OVERWORLD);
        this.tag(Biomes.OLD_GROWTH_SPRUCE_TAIGA, ModTags.IS_DENSE_OVERWORLD);
        this.tag(Biomes.WINDSWEPT_SAVANNA, ModTags.IS_HOT, ModTags.IS_DRY_OVERWORLD, ModTags.IS_SPARSE, ModTags.IS_SAVANNA);
        this.tag(Biomes.ERODED_BADLANDS, ModTags.IS_HOT, ModTags.IS_DRY_OVERWORLD, ModTags.IS_SPARSE);
        this.tag(Biomes.BAMBOO_JUNGLE, ModTags.IS_HOT, ModTags.IS_WET);
        this.tag(Biomes.LUSH_CAVES, ModTags.IS_LUSH, ModTags.IS_WET);
        this.tag(Biomes.DRIPSTONE_CAVES, ModTags.IS_SPARSE);
        this.tag(Biomes.SOUL_SAND_VALLEY, ModTags.IS_HOT);
        this.tag(Biomes.CRIMSON_FOREST, ModTags.IS_HOT);
        this.tag(Biomes.WARPED_FOREST, ModTags.IS_HOT);
        this.tag(Biomes.BASALT_DELTAS, ModTags.IS_HOT);

        this.tag(ModTags.IS_WATER).addTag(BiomeTags.IS_OCEAN).addTag(BiomeTags.IS_RIVER);
        this.tag(BiomeTags.IS_MOUNTAIN).addTag(ModTags.IS_PEAK).addTag(ModTags.IS_SLOPE);

        this.tag(ModTags.nether_end).addTag(BiomeTags.IS_NETHER).addTag(ModTags.IS_END);

        this.tag(ModTags.aquamarine_gen).addTag(ModTags.IS_WATER).addTag(ModTags.IS_BEACH).addTag(ModTags.IS_WET);
        this.tag(ModTags.amethyst_gen).addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_MOUNTAIN).addOptionalTag(ModTags.IS_DEAD.location());
        this.tag(ModTags.ruby_gen).addTag(ModTags.IS_HOT).addTag(BiomeTags.IS_NETHER);
        this.tag(ModTags.emerald_gen).addTag(ModTags.IS_PLAINS).addTag(ModTags.IS_WASTELAND).addTag(ModTags.IS_SPARSE).addTag(BiomeTags.IS_HILL);
        this.tag(ModTags.sapphire_gen).addOptionalTag(ModTags.IS_MAGICAL.location()).addTag(ModTags.IS_SNOWY);

        this.tag(ModTags.water_nether_end).addTag(ModTags.nether_end).addTag(ModTags.IS_WATER);
        this.tag(ModTags.mushroom_gen).addTag(BiomeTags.IS_FOREST).addTag(ModTags.IS_MUSHROOM).addOptionalTag(ModTags.IS_MAGICAL.location());
        this.tag(ModTags.indigo_gen).addTag(ModTags.IS_WET).addOptionalTag(ModTags.IS_MAGICAL.location()).addTag(ModTags.IS_LUSH);
        this.tag(ModTags.purple_gen).addTag(ModTags.IS_WET).addOptionalTag(ModTags.IS_MAGICAL.location());
        this.tag(ModTags.blue_gen).addTag(ModTags.IS_BEACH).addOptionalTag(ModTags.IS_MAGICAL.location()).addTag(BiomeTags.IS_RIVER).addTag(ModTags.IS_SWAMP);
        this.tag(ModTags.water_end).addTag(ModTags.IS_WATER).addTag(ModTags.IS_END);
        this.tag(ModTags.yellow_gen).addTag(ModTags.IS_DRY_OVERWORLD).addTag(ModTags.IS_SANDY).addTag(BiomeTags.IS_NETHER);
        this.tag(ModTags.orange_gen).addTag(BiomeTags.IS_NETHER).addTag(ModTags.IS_HOT).addTag(ModTags.IS_SAVANNA);

        this.tag(ModTags.bamboo_gen).addTag(BiomeTags.IS_JUNGLE).addTag(ModTags.IS_LUSH).addTag(ModTags.IS_DENSE_OVERWORLD);
        this.tag(ModTags.general_herb).addOptionalTag(ModTags.IS_MAGICAL.location()).addTag(ModTags.IS_LUSH).addTag(ModTags.IS_DENSE_OVERWORLD).addTag(ModTags.IS_PLAINS)
                .addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_HILL);

        this.addedTags.forEach(tag -> this.tag(tag).addOptionalTag(new ResourceLocation("byg", tag.location().getPath())));
        this.addedTags.forEach(tag -> this.tag(tag).addOptionalTag(new ResourceLocation("forge", tag.location().getPath())));
    }

    @SafeVarargs
    protected final void tag(ResourceKey<Biome> biome, TagKey<Biome>... tags) {
        for (TagKey<Biome> key : tags) {
            this.tag(key).add(biome);
            this.addedTags.add(key);
        }
    }

    @Override
    public String getName() {
        return "Biome Tags";
    }
}
