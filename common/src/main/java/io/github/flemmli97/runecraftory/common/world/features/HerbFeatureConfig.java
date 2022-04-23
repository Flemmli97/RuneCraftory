package io.github.flemmli97.runecraftory.common.world.features;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

public record HerbFeatureConfig(int tries, int radius, int ySpread,
                                List<HerbFeature.Entry> entries) implements FeatureConfiguration {
    public static final Codec<HerbFeatureConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(ExtraCodecs.POSITIVE_INT.fieldOf("tries").orElse(128).forGetter(HerbFeatureConfig::tries),
                            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("radius").orElse(7).forGetter(HerbFeatureConfig::radius),
                            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("ySpread").orElse(5).forGetter(HerbFeatureConfig::ySpread),
                            Codec.mapPair(Registry.BLOCK.byNameCodec().fieldOf("block"), ExtraCodecs.POSITIVE_INT.fieldOf("weight")).codec().listOf().fieldOf("entries").forGetter(data -> data.entries
                                    .stream().map(e -> Pair.of(e.block, e.weight.asInt())).toList()))
                    .apply(instance, (tries, radius, amount, list) -> new HerbFeatureConfig(tries, radius, amount, list.stream().map(p -> new HerbFeature.Entry(p.getFirst(), p.getSecond())).toList())));
}
