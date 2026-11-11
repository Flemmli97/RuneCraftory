package io.github.flemmli97.runecraftory.common.utils;

import java.util.Map;

public class Utils {

    public static <K, V> void mergeMapAndThrow(Map<K, V> from, Map<K, V> to) {
        from.forEach((k, v) -> {
            if (v.equals(to.getOrDefault(k, v))) {
                to.put(k, v);
            } else {
                throw new IllegalStateException("Map contains element already " + k);
            }
        });
    }
}
