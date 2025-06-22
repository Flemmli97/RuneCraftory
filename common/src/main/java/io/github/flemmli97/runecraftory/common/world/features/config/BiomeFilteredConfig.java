package io.github.flemmli97.runecraftory.common.world.features.config;

import io.github.flemmli97.runecraftory.common.world.features.HolderSetPair;
import net.minecraft.core.Holder;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public record BiomeFilteredConfig(List<BiomeFilteredEntry> features,
                                  Holder<PlacedFeature> fallback) implements FeatureConfiguration {

//    public static final Codec<BiomeFilteredConfig> CODEC = RecordCodecBuilder.create((instance) ->
//            instance.group(WeightedPlacedFeature.CODEC.listOf().fieldOf("features").forGetter(BiomeFilteredConfig::features),
//                    PlacedFeature.CODEC.fieldOf("fallback").forGetter(BiomeFilteredConfig::fallback)
//            ).apply(instance, BiomeFilteredConfig::new));

    public record BiomeFilteredEntry(Holder<PlacedFeature> feature, HolderSetPair<Biome> biomes,
                                     Weight weight) implements WeightedEntry {

        public BiomeFilteredEntry(Holder<PlacedFeature> feature, HolderSetPair<Biome> biomes, int weight) {
            this(feature, biomes, Weight.of(weight));
        }

        @Override
        public Weight getWeight() {
            return this.weight();
        }
    }
}
