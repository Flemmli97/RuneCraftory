package io.github.flemmli97.runecraftory.common.world.features;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.Optional;
import java.util.function.Predicate;

public record TagPredicate<T>(Optional<TagKey<T>> whiteList,
                              Optional<TagKey<T>> blackList) implements Predicate<Holder<T>> {

    public static <T> MapCodec<TagPredicate<T>> codec(ResourceKey<? extends Registry<T>> registry) {
        return RecordCodecBuilder.mapCodec(
                builder -> builder
                        .group(TagKey.codec(registry).optionalFieldOf("white_list").forGetter(TagPredicate::whiteList),
                                TagKey.codec(registry).optionalFieldOf("black_list").forGetter(TagPredicate::blackList))
                        .apply(builder, TagPredicate::new));
    }

    public TagPredicate(TagKey<T> whiteList, TagKey<T> blackList) {
        this(Optional.ofNullable(whiteList), Optional.ofNullable(blackList));
    }

    @Override
    public boolean test(Holder<T> t) {
        return this.whiteList.map(t::is).orElse(true)
                && this.blackList.map(tag -> !t.is(tag)).orElse(true);
    }
}
