package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
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
        this.tag(Biomes.PLAINS, RunecraftoryTags.IS_PLAINS);
        this.tag(Biomes.DESERT, RunecraftoryTags.IS_HOT, RunecraftoryTags.IS_DRY_OVERWORLD, RunecraftoryTags.IS_SANDY);
        this.tag(Biomes.SWAMP, RunecraftoryTags.IS_WET, RunecraftoryTags.IS_SWAMP);
        this.tag(Biomes.NETHER_WASTES, RunecraftoryTags.IS_HOT);
        this.tag(Biomes.THE_END, RunecraftoryTags.IS_END);
        this.tag(Biomes.FROZEN_OCEAN, RunecraftoryTags.IS_SNOWY);
        this.tag(Biomes.FROZEN_RIVER, RunecraftoryTags.IS_SNOWY);
        this.tag(Biomes.SNOWY_PLAINS, RunecraftoryTags.IS_SNOWY, RunecraftoryTags.IS_WASTELAND, RunecraftoryTags.IS_PLAINS);
        this.tag(Biomes.MUSHROOM_FIELDS, RunecraftoryTags.IS_MUSHROOM);
        this.tag(Biomes.BEACH, RunecraftoryTags.IS_BEACH);
        this.tag(Biomes.JUNGLE, RunecraftoryTags.IS_HOT, RunecraftoryTags.IS_WET, RunecraftoryTags.IS_DENSE_OVERWORLD);
        this.tag(Biomes.SPARSE_JUNGLE, RunecraftoryTags.IS_HOT, RunecraftoryTags.IS_WET, RunecraftoryTags.IS_WET);
        this.tag(Biomes.STONY_SHORE, RunecraftoryTags.IS_BEACH);
        this.tag(Biomes.SNOWY_BEACH, RunecraftoryTags.IS_BEACH, RunecraftoryTags.IS_SNOWY);
        this.tag(Biomes.DARK_FOREST, RunecraftoryTags.IS_SPOOKY);
        this.tag(Biomes.SNOWY_TAIGA, RunecraftoryTags.IS_SNOWY);
        this.tag(Biomes.WINDSWEPT_FOREST, RunecraftoryTags.IS_SPARSE);
        this.tag(Biomes.SAVANNA, RunecraftoryTags.IS_HOT, RunecraftoryTags.IS_SAVANNA, RunecraftoryTags.IS_SPARSE);
        this.tag(Biomes.SAVANNA_PLATEAU, RunecraftoryTags.IS_HOT, RunecraftoryTags.IS_SAVANNA, RunecraftoryTags.IS_SPARSE, RunecraftoryTags.IS_SLOPE);
        this.tag(Biomes.BADLANDS, RunecraftoryTags.IS_SANDY, RunecraftoryTags.IS_DRY_OVERWORLD);
        this.tag(Biomes.WOODED_BADLANDS, RunecraftoryTags.IS_SANDY, RunecraftoryTags.IS_DRY_OVERWORLD, RunecraftoryTags.IS_SPARSE, RunecraftoryTags.IS_SLOPE);
        this.tag(Biomes.MEADOW, RunecraftoryTags.IS_PLAINS, RunecraftoryTags.IS_SLOPE);
        this.tag(Biomes.GROVE, RunecraftoryTags.IS_SNOWY, RunecraftoryTags.IS_SLOPE);
        this.tag(Biomes.SNOWY_SLOPES, RunecraftoryTags.IS_SPARSE, RunecraftoryTags.IS_SNOWY, RunecraftoryTags.IS_SLOPE);
        this.tag(Biomes.JAGGED_PEAKS, RunecraftoryTags.IS_SPARSE, RunecraftoryTags.IS_SNOWY, RunecraftoryTags.IS_PEAK);
        this.tag(Biomes.FROZEN_PEAKS, RunecraftoryTags.IS_SPARSE, RunecraftoryTags.IS_SNOWY, RunecraftoryTags.IS_PEAK);
        this.tag(Biomes.STONY_PEAKS, RunecraftoryTags.IS_HOT, RunecraftoryTags.IS_PEAK);
        this.tag(Biomes.SMALL_END_ISLANDS, RunecraftoryTags.IS_END);
        this.tag(Biomes.END_MIDLANDS, RunecraftoryTags.IS_END);
        this.tag(Biomes.END_HIGHLANDS, RunecraftoryTags.IS_END);
        this.tag(Biomes.END_BARRENS, RunecraftoryTags.IS_END);
        this.tag(Biomes.WARM_OCEAN, RunecraftoryTags.IS_HOT);
        this.tag(Biomes.SUNFLOWER_PLAINS, RunecraftoryTags.IS_PLAINS);
        this.tag(Biomes.WINDSWEPT_GRAVELLY_HILLS, RunecraftoryTags.IS_SPARSE);
        this.tag(Biomes.ICE_SPIKES, RunecraftoryTags.IS_SNOWY);
        this.tag(Biomes.OLD_GROWTH_BIRCH_FOREST, RunecraftoryTags.IS_DENSE_OVERWORLD);
        this.tag(Biomes.OLD_GROWTH_SPRUCE_TAIGA, RunecraftoryTags.IS_DENSE_OVERWORLD);
        this.tag(Biomes.WINDSWEPT_SAVANNA, RunecraftoryTags.IS_HOT, RunecraftoryTags.IS_DRY_OVERWORLD, RunecraftoryTags.IS_SPARSE, RunecraftoryTags.IS_SAVANNA);
        this.tag(Biomes.ERODED_BADLANDS, RunecraftoryTags.IS_HOT, RunecraftoryTags.IS_DRY_OVERWORLD, RunecraftoryTags.IS_SPARSE);
        this.tag(Biomes.BAMBOO_JUNGLE, RunecraftoryTags.IS_HOT, RunecraftoryTags.IS_WET);
        this.tag(Biomes.LUSH_CAVES, RunecraftoryTags.IS_LUSH, RunecraftoryTags.IS_WET);
        this.tag(Biomes.DRIPSTONE_CAVES, RunecraftoryTags.IS_SPARSE);
        this.tag(Biomes.SOUL_SAND_VALLEY, RunecraftoryTags.IS_HOT);
        this.tag(Biomes.CRIMSON_FOREST, RunecraftoryTags.IS_HOT);
        this.tag(Biomes.WARPED_FOREST, RunecraftoryTags.IS_HOT);
        this.tag(Biomes.BASALT_DELTAS, RunecraftoryTags.IS_HOT);

        this.tag(RunecraftoryTags.IS_WATER).addTag(BiomeTags.IS_OCEAN).addTag(BiomeTags.IS_RIVER);
        this.tag(BiomeTags.IS_MOUNTAIN).addTag(RunecraftoryTags.IS_PEAK).addTag(RunecraftoryTags.IS_SLOPE);

        this.tag(RunecraftoryTags.NETHER_END).addTag(BiomeTags.IS_NETHER).addTag(RunecraftoryTags.IS_END);

        this.tag(RunecraftoryTags.AQUAMARINE_GEN).addTag(RunecraftoryTags.IS_WATER).addTag(RunecraftoryTags.IS_BEACH).addTag(RunecraftoryTags.IS_WET);
        this.tag(RunecraftoryTags.AMETHYST_GEN).addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_MOUNTAIN).addOptionalTag(RunecraftoryTags.IS_DEAD.location());
        this.tag(RunecraftoryTags.RUBY_GEN).addTag(RunecraftoryTags.IS_HOT).addTag(BiomeTags.IS_NETHER);
        this.tag(RunecraftoryTags.EMERALD_GEN).addTag(RunecraftoryTags.IS_PLAINS).addTag(RunecraftoryTags.IS_WASTELAND).addTag(RunecraftoryTags.IS_SPARSE).addTag(BiomeTags.IS_HILL);
        this.tag(RunecraftoryTags.SAPPHIRE_GEN).addOptionalTag(RunecraftoryTags.IS_MAGICAL.location()).addTag(RunecraftoryTags.IS_SNOWY);

        this.tag(RunecraftoryTags.WATER_NETHER_END).addTag(RunecraftoryTags.NETHER_END).addTag(RunecraftoryTags.IS_WATER);
        this.tag(RunecraftoryTags.MUSHROOM_GEN).addTag(BiomeTags.IS_FOREST).addTag(RunecraftoryTags.IS_MUSHROOM).addOptionalTag(RunecraftoryTags.IS_MAGICAL.location());
        this.tag(RunecraftoryTags.INDIGO_GEN).addTag(RunecraftoryTags.IS_WET).addOptionalTag(RunecraftoryTags.IS_MAGICAL.location()).addTag(RunecraftoryTags.IS_LUSH);
        this.tag(RunecraftoryTags.PURPLE_GEN).addTag(RunecraftoryTags.IS_WET).addOptionalTag(RunecraftoryTags.IS_MAGICAL.location());
        this.tag(RunecraftoryTags.BLUE_GEN).addTag(RunecraftoryTags.IS_BEACH).addOptionalTag(RunecraftoryTags.IS_MAGICAL.location()).addTag(BiomeTags.IS_RIVER).addTag(RunecraftoryTags.IS_SWAMP);
        this.tag(RunecraftoryTags.WATER_END).addTag(RunecraftoryTags.IS_WATER).addTag(RunecraftoryTags.IS_END);
        this.tag(RunecraftoryTags.YELLOW_GEN).addTag(RunecraftoryTags.IS_DRY_OVERWORLD).addTag(RunecraftoryTags.IS_SANDY).addTag(BiomeTags.IS_NETHER);
        this.tag(RunecraftoryTags.ORANGE_GEN).addTag(BiomeTags.IS_NETHER).addTag(RunecraftoryTags.IS_HOT).addTag(RunecraftoryTags.IS_SAVANNA);

        this.tag(RunecraftoryTags.BAMBOO_GEN).addTag(BiomeTags.IS_JUNGLE).addTag(RunecraftoryTags.IS_LUSH).addTag(RunecraftoryTags.IS_DENSE_OVERWORLD);
        this.tag(RunecraftoryTags.GENERAL_HERBS).addOptionalTag(RunecraftoryTags.IS_MAGICAL.location()).addTag(RunecraftoryTags.IS_LUSH).addTag(RunecraftoryTags.IS_DENSE_OVERWORLD).addTag(RunecraftoryTags.IS_PLAINS)
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
