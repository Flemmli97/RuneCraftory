package io.github.flemmli97.runecraftory.common.world.features.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.world.features.TagPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record ChancedBlockClusterConfig(BlockStateProvider stateProvider,
                                        TagPredicate<Biome> biomes,
                                        IntProvider amount, int radius, int tries) implements FeatureConfiguration {

    public static final Codec<ChancedBlockClusterConfig> CODEC = RecordCodecBuilder.create(codec -> codec.group(
                    BlockStateProvider.CODEC.fieldOf("state_provider").forGetter(ChancedBlockClusterConfig::stateProvider),
                    TagPredicate.codec(Registries.BIOME).fieldOf("biomes").forGetter(ChancedBlockClusterConfig::biomes),
                    IntProvider.CODEC.fieldOf("amount").forGetter(ChancedBlockClusterConfig::amount),
                    Codec.INT.fieldOf("radius").orElse(3).forGetter(ChancedBlockClusterConfig::radius),
                    Codec.INT.fieldOf("tries").orElse(3).forGetter(ChancedBlockClusterConfig::tries))
            .apply(codec, ChancedBlockClusterConfig::new));

    public ChancedBlockClusterConfig(Block block, TagKey<Biome> whiteList, TagKey<Biome> blackList, IntProvider amount, int radius, int tries) {
        this(BlockStateProvider.simple(block), new TagPredicate<>(whiteList, blackList),
                amount, radius, tries);
    }
}
