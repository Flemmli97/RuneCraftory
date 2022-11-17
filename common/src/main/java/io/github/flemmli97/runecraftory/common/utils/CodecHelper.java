package io.github.flemmli97.runecraftory.common.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.github.flemmli97.tenshilib.platform.registry.SimpleRegistryWrapper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.function.Supplier;

public class CodecHelper {

    public static <T extends Enum<T>> Codec<T> enumCodec(Class<T> clss, T fallback) {
        return Codec.STRING.flatXmap(s -> {
            try {
                return DataResult.success(Enum.valueOf(clss, s));
            } catch (IllegalArgumentException e) {
                if (fallback != null)
                    return DataResult.success(fallback);
                return DataResult.error("No such enum constant " + s + " for class " + clss);
            }
        }, e -> DataResult.success(e.name()));
    }

    public static <T> Codec<T> ofCustomRegistry(Supplier<SimpleRegistryWrapper<T>> registry, ResourceKey<? extends Registry<T>> key) {
        return ResourceLocation.CODEC.flatXmap(res -> {
            T entry = registry.get().getFromId(res);
            if (!registry.get().getIDFrom(entry).equals(res))
                return DataResult.error("Unknown registry key in " + key + ": " + res);
            return DataResult.success(entry);
        }, val -> Optional.ofNullable(registry.get().getIDFrom(val))
                .map(DataResult::success).orElseGet(() -> DataResult.error("Unknown registry element in " + key + ":" + val)));
    }
}
