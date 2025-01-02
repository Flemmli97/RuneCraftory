package io.github.flemmli97.runecraftory.common.utils;

import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MiscUtils {

    public static <T> List<T> expandTag(Registry<T> registry, TagKey<T> tag) {
        List<T> elements = new ArrayList<>();
        registry.getTag(tag)
                .ifPresent(n -> n.forEach(h -> elements.add(h.value())));
        return elements;
    }

    public static <T, R> List<R> expandTag(Registry<T> registry, TagKey<T> tag, Function<T, R> map) {
        List<R> elements = new ArrayList<>();
        registry.getTag(tag)
                .ifPresent(n -> n.forEach(h -> elements.add(map.apply(h.value()))));
        return elements;
    }
}
