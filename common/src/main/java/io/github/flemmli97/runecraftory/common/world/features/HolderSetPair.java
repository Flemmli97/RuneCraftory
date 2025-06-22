package io.github.flemmli97.runecraftory.common.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;
import java.util.function.Predicate;

public record HolderSetPair<T>(Optional<HolderSet<T>> whiteList,
                               Optional<HolderSet<T>> blackList) implements Predicate<Holder<T>> {

    public static <T> MapCodec<HolderSetPair<T>> codec(ResourceKey<? extends Registry<T>> registryKey, Codec<Holder<T>> holderCodec, boolean forceList) {
        return RecordCodecBuilder.mapCodec(
                builder -> builder
                        .group(HolderSetCodec.create(registryKey, holderCodec, forceList).optionalFieldOf("white_list").forGetter(HolderSetPair::whiteList),
                                HolderSetCodec.create(registryKey, holderCodec, forceList).optionalFieldOf("black_list").forGetter(HolderSetPair::blackList))
                        .apply(builder, HolderSetPair::new));
    }

    @Override
    public boolean test(Holder<T> t) {
        return this.whiteList.map(s -> s.contains(t)).orElse(true)
                && this.blackList.map(s -> !s.contains(t)).orElse(true);
    }
}
