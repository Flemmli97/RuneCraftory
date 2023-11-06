package io.github.flemmli97.runecraftory.common.world.features.trees;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record FruitTreeSproutConfiguration(
        BlockStateProvider trunkProvider,
        BlockStateProvider foliageProvider) implements FeatureConfiguration {

    public static final Codec<FruitTreeSproutConfiguration> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter(d -> d.trunkProvider),
                    BlockStateProvider.CODEC.fieldOf("foliage_provider").forGetter(d -> d.foliageProvider)
            ).apply(instance, FruitTreeSproutConfiguration::new));

}
