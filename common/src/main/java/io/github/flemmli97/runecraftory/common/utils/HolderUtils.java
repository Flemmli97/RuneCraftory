package io.github.flemmli97.runecraftory.common.utils;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class HolderUtils {

    public static <T> List<T> expandTag(HolderLookup.Provider provider, ResourceKey<? extends Registry<? extends T>> registry, TagKey<T> tag) {
        List<T> elements = new ArrayList<>();
        provider.lookup(registry).flatMap(r -> r.get(tag))
                .ifPresent(n -> n.forEach(h -> elements.add(h.value())));
        return elements;
    }

    public static <T, R> List<R> expandTag(HolderLookup.Provider provider, ResourceKey<? extends Registry<? extends T>> registry, TagKey<T> tag, Function<T, R> map) {
        List<R> elements = new ArrayList<>();
        provider.lookup(registry).flatMap(r -> r.get(tag))
                .ifPresent(n -> n.forEach(h -> elements.add(map.apply(h.value()))));
        return elements;
    }

    public static <T> Optional<Holder<T>> getHolder(HolderLookup.Provider provider, ResourceKey<? extends Registry<T>> registry, ResourceLocation key) {
        return provider.lookupOrThrow(registry)
                .get(ResourceKey.create(registry, key))
                .map(h -> h);
    }

    public static <T> Optional<T> get(HolderLookup.Provider provider, ResourceKey<? extends Registry<T>> registry, ResourceLocation key) {
        return getHolder(provider, registry, key).map(Holder::value);
    }
}
