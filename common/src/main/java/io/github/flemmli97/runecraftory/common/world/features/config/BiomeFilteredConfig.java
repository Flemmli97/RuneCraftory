package io.github.flemmli97.runecraftory.common.world.features.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.world.features.TagPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public record BiomeFilteredConfig(List<BiomeFilteredEntry> features) implements FeatureConfiguration {

    public static final Codec<BiomeFilteredConfig> CODEC = BiomeFilteredEntry.CODEC.listOf().fieldOf("features")
            .xmap(BiomeFilteredConfig::new, BiomeFilteredConfig::features).codec();

    public record BiomeFilteredEntry(Holder<PlacedFeature> feature, TagPredicate<Biome> biomes,
                                     Weight weight) implements WeightedEntry {

        public static final Codec<BiomeFilteredEntry> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(PlacedFeature.CODEC.fieldOf("feature").forGetter(BiomeFilteredEntry::feature),
                        TagPredicate.codec(Registries.BIOME).fieldOf("biomes").forGetter(BiomeFilteredEntry::biomes),
                        ExtraCodecs.POSITIVE_INT.fieldOf("weight").forGetter(d -> d.weight().asInt())
                ).apply(instance, BiomeFilteredEntry::new));

        public BiomeFilteredEntry(Holder<PlacedFeature> feature, TagPredicate<Biome> biomes, int weight) {
            this(feature, biomes, Weight.of(weight));
        }

        public BiomeFilteredEntry(Holder<PlacedFeature> feature, TagKey<Biome> whiteList, TagKey<Biome> blackList, int weight) {
            this(feature, new TagPredicate<>(whiteList, blackList), Weight.of(weight));
        }

        @Override
        public Weight getWeight() {
            return this.weight();
        }
    }
}
