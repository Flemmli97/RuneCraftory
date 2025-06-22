package io.github.flemmli97.runecraftory.common.world.features;

import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.common.world.features.config.BiomeFilteredConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public class RandomFeature extends Feature<BiomeFilteredConfig> {

    public RandomFeature(Codec<BiomeFilteredConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BiomeFilteredConfig> ctx) {
        BiomeFilteredConfig config = ctx.config();
        RandomSource random = ctx.random();
        WorldGenLevel level = ctx.level();
        ChunkGenerator generator = ctx.chunkGenerator();
        BlockPos origin = ctx.origin();
        Holder<Biome> biome = ctx.level().getBiome(origin);

        List<BiomeFilteredConfig.BiomeFilteredEntry> features = config.features().stream()
                .filter(p -> p.getWeight().asInt() > 0 && p.biomes().test(biome)).toList();
        Holder<PlacedFeature> feature = WeightedRandom.getRandomItem(random, features).map(BiomeFilteredConfig.BiomeFilteredEntry::feature).orElse(config.fallback());
        return feature.value().place(level, generator, random, origin);
    }
}
