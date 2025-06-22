package io.github.flemmli97.runecraftory.common.world.features.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.world.features.HolderSetPair;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.List;
import java.util.Optional;

public record HerbFeatureConfig(int tries, int radius, int ySpread,
                                List<HerbEntry> entries) implements FeatureConfiguration {

    public static final Codec<HerbFeatureConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(ExtraCodecs.POSITIVE_INT.fieldOf("tries").orElse(128).forGetter(HerbFeatureConfig::tries),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("radius").orElse(7).forGetter(HerbFeatureConfig::radius),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("y_spread").orElse(5).forGetter(HerbFeatureConfig::ySpread),
                    HerbEntry.CODEC.listOf().fieldOf("entries").forGetter(HerbFeatureConfig::entries)
            ).apply(instance, HerbFeatureConfig::new));

    public record HerbEntry(BlockStateProvider stateProvider, HolderSetPair<Biome> biomes,
                            Weight weight) implements WeightedEntry {

        public static final Codec<HerbEntry> CODEC = RecordCodecBuilder.create(codec -> codec.group(
                BlockStateProvider.CODEC.fieldOf("state_provider").forGetter(HerbEntry::stateProvider),
                HolderSetPair.codec(Registries.BIOME, Biome.CODEC, false).fieldOf("biomes").forGetter(HerbEntry::biomes),
                Codec.INT.fieldOf("weight").forGetter((cb) -> cb.weight().asInt())
        ).apply(codec, HerbEntry::new));

        public HerbEntry(BlockStateProvider stateProvider, HolderSetPair<Biome> biomes, int weight) {
            this(stateProvider, biomes, Weight.of(weight));
        }

        @SuppressWarnings("deprecation")
        public HerbEntry(Block block, HolderLookup.Provider provider, TagKey<Biome> whiteList, TagKey<Biome> blackList, int weight) {
            this(BlockStateProvider.simple(block),
                    new HolderSetPair<>(Optional.ofNullable(whiteList == null ? null : HolderSet.emptyNamed(provider.lookupOrThrow(Registries.BIOME), whiteList)),
                            Optional.ofNullable(blackList == null ? null : HolderSet.emptyNamed(provider.lookupOrThrow(Registries.BIOME), blackList))),
                    Weight.of(weight));
        }

        @Override
        public Weight getWeight() {
            return this.weight();
        }
    }
}
