package io.github.flemmli97.runecraftory.neoforge.data.tags;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class BiomeTagGen extends TagsProvider<Biome> {

    public BiomeTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, ExistingFileHelper existingFileHelper) {
        super(output, Registries.BIOME, completableFuture, RuneCraftory.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(RunecraftoryTags.Biomes.VANILLA_DIMENSIONS)
                .addTag(BiomeTags.IS_OVERWORLD).addTag(BiomeTags.IS_NETHER).addTag(BiomeTags.IS_END);
        this.tag(RunecraftoryTags.Biomes.COMMON_GROUND_BLACKLIST)
                .addTag(BiomeTags.IS_NETHER).addTag(BiomeTags.IS_END).addTag(RunecraftoryTags.Biomes.IS_AQUATIC);
        this.tag(RunecraftoryTags.Biomes.GENERAL_HERBS).addOptionalTag(RunecraftoryTags.Biomes.IS_MAGICAL.location()).addTag(RunecraftoryTags.Biomes.IS_LUSH).addTag(RunecraftoryTags.Biomes.IS_DENSE_VEGETATION_OVERWORLD).addTag(RunecraftoryTags.Biomes.IS_PLAINS)
                .addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_HILL);

        ModBlocks.GENERATION_TAGS.forEach((reg, tags) -> {
            TagAppender<Biome> whitelist = this.tag(RunecraftoryTags.Biomes.getBlockBasedGenerationTag(reg, true));
            tags.whitelist().forEach(tag -> {
                if (provider.lookupOrThrow(this.registryKey)
                        .get(tag).isPresent()) {
                    whitelist.addTag(tag);
                } else {
                    whitelist.addOptionalTag(tag);
                }
            });
            TagAppender<Biome> blacklist = this.tag(RunecraftoryTags.Biomes.getBlockBasedGenerationTag(reg, false));
            tags.blacklist().forEach(tag -> {
                if (provider.lookupOrThrow(this.registryKey)
                        .get(tag).isPresent()) {
                    blacklist.addTag(tag);
                } else {
                    blacklist.addOptionalTag(tag);
                }
            });
        });

        this.tag(RunecraftoryTags.Biomes.FOREST_GROVE).addTag(BiomeTags.IS_FOREST);
        this.tag(RunecraftoryTags.Biomes.WATER_RUINS).addTag(BiomeTags.IS_OCEAN);
        this.tag(RunecraftoryTags.Biomes.THEATER_RUINS).addTag(RunecraftoryTags.Biomes.IS_SPOOKY);
        this.tag(RunecraftoryTags.Biomes.PLAINS_ARENA).addTag(RunecraftoryTags.Biomes.IS_PLAINS);
        this.tag(RunecraftoryTags.Biomes.DESERT_ARENA).addTag(RunecraftoryTags.Biomes.IS_SANDY);
        this.tag(RunecraftoryTags.Biomes.NETHER_ARENA).addTag(BiomeTags.IS_NETHER);
        this.tag(RunecraftoryTags.Biomes.WIND_SHRINE).addTag(RunecraftoryTags.Biomes.IS_MOUNTAIN_PEAK);
        this.tag(RunecraftoryTags.Biomes.LEON_KARNAK).addTag(RunecraftoryTags.Biomes.IS_MOUNTAIN_PEAK);
    }

    @SafeVarargs
    protected final void tag(ResourceKey<Biome> key, TagKey<Biome>... tags) {
        for (TagKey<Biome> tag : tags) {
            this.tag(tag).add(key);
        }
    }
}
