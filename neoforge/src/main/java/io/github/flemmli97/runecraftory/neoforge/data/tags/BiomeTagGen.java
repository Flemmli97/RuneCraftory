package io.github.flemmli97.runecraftory.neoforge.data.tags;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class BiomeTagGen extends TagsProvider<Biome> {

    public BiomeTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, ExistingFileHelper existingFileHelper) {
        super(output, Registries.BIOME, completableFuture, RuneCraftory.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(RunecraftoryTags.Biomes.NETHER_END).addTag(BiomeTags.IS_NETHER).addTag(BiomeTags.IS_END);

        this.tag(RunecraftoryTags.Biomes.AQUAMARINE_GEN).addTag(RunecraftoryTags.Biomes.IS_AQUATIC).addTag(BiomeTags.IS_BEACH).addTag(RunecraftoryTags.Biomes.IS_WET);
        this.tag(RunecraftoryTags.Biomes.AMETHYST_GEN).addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_MOUNTAIN).addOptionalTag(RunecraftoryTags.Biomes.IS_DEAD.location());
        this.tag(RunecraftoryTags.Biomes.RUBY_GEN).addTag(RunecraftoryTags.Biomes.IS_HOT).addTag(BiomeTags.IS_NETHER);
        this.tag(RunecraftoryTags.Biomes.EMERALD_GEN).addTag(RunecraftoryTags.Biomes.IS_PLAINS).addTag(RunecraftoryTags.Biomes.IS_WASTELAND).addTag(RunecraftoryTags.Biomes.IS_SPARSE_VEGETATION_OVERWORLD).addTag(BiomeTags.IS_HILL);
        this.tag(RunecraftoryTags.Biomes.SAPPHIRE_GEN).addOptionalTag(RunecraftoryTags.Biomes.IS_MAGICAL.location()).addTag(RunecraftoryTags.Biomes.IS_SNOWY);

        this.tag(RunecraftoryTags.Biomes.WATER_NETHER_END).addTag(RunecraftoryTags.Biomes.NETHER_END).addTag(RunecraftoryTags.Biomes.IS_AQUATIC);
        this.tag(RunecraftoryTags.Biomes.MUSHROOM_GEN).addTag(BiomeTags.IS_FOREST).addTag(RunecraftoryTags.Biomes.IS_MUSHROOM).addOptionalTag(RunecraftoryTags.Biomes.IS_MAGICAL.location());
        this.tag(RunecraftoryTags.Biomes.INDIGO_GEN).addTag(RunecraftoryTags.Biomes.IS_WET).addOptionalTag(RunecraftoryTags.Biomes.IS_MAGICAL.location()).addTag(RunecraftoryTags.Biomes.IS_LUSH);
        this.tag(RunecraftoryTags.Biomes.PURPLE_GEN).addTag(RunecraftoryTags.Biomes.IS_WET).addOptionalTag(RunecraftoryTags.Biomes.IS_MAGICAL.location());
        this.tag(RunecraftoryTags.Biomes.BLUE_GEN).addTag(BiomeTags.IS_BEACH).addOptionalTag(RunecraftoryTags.Biomes.IS_MAGICAL.location()).addTag(BiomeTags.IS_RIVER).addTag(RunecraftoryTags.Biomes.IS_SWAMP);
        this.tag(RunecraftoryTags.Biomes.WATER_END).addTag(RunecraftoryTags.Biomes.IS_AQUATIC).addTag(BiomeTags.IS_END);
        this.tag(RunecraftoryTags.Biomes.YELLOW_GEN).addTag(RunecraftoryTags.Biomes.IS_DRY_OVERWORLD).addTag(RunecraftoryTags.Biomes.IS_SANDY).addTag(BiomeTags.IS_NETHER);
        this.tag(RunecraftoryTags.Biomes.ORANGE_GEN).addTag(BiomeTags.IS_NETHER).addTag(RunecraftoryTags.Biomes.IS_HOT).addTag(BiomeTags.IS_SAVANNA);

        this.tag(RunecraftoryTags.Biomes.BAMBOO_GEN).addTag(BiomeTags.IS_JUNGLE).addTag(RunecraftoryTags.Biomes.IS_LUSH).addTag(RunecraftoryTags.Biomes.IS_DENSE_VEGETATION_OVERWORLD);
        this.tag(RunecraftoryTags.Biomes.GENERAL_HERBS).addOptionalTag(RunecraftoryTags.Biomes.IS_MAGICAL.location()).addTag(RunecraftoryTags.Biomes.IS_LUSH).addTag(RunecraftoryTags.Biomes.IS_DENSE_VEGETATION_OVERWORLD).addTag(RunecraftoryTags.Biomes.IS_PLAINS)
                .addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_HILL);

        this.tag(RunecraftoryTags.Biomes.VANILLA_DIMENSIONS)
                .addTag(BiomeTags.IS_OVERWORLD).addTag(BiomeTags.IS_NETHER).addTag(BiomeTags.IS_END);

        this.tag(RunecraftoryTags.Biomes.FOREST_GROVE).addTag(BiomeTags.IS_FOREST);
        this.tag(RunecraftoryTags.Biomes.WATER_RUINS).addTag(BiomeTags.IS_OCEAN);
        this.tag(RunecraftoryTags.Biomes.THEATER_RUINS).addTag(RunecraftoryTags.Biomes.IS_SPOOKY);
        this.tag(RunecraftoryTags.Biomes.PLAINS_ARENA).addTag(RunecraftoryTags.Biomes.IS_PLAINS);
        this.tag(RunecraftoryTags.Biomes.DESERT_ARENA).addTag(RunecraftoryTags.Biomes.IS_SANDY);
        this.tag(RunecraftoryTags.Biomes.NETHER_ARENA).addTag(BiomeTags.IS_NETHER);
        this.tag(RunecraftoryTags.Biomes.WIND_SHRINE).addTag(RunecraftoryTags.Biomes.IS_MOUNTAIN_PEAK);
        this.tag(RunecraftoryTags.Biomes.LEON_KARNAK).addTag(RunecraftoryTags.Biomes.IS_MOUNTAIN_PEAK);
    }
}
