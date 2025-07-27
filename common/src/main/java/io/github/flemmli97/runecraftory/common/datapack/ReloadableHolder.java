package io.github.flemmli97.runecraftory.common.datapack;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record ReloadableHolder<T>(ResourceLocation id, T value) {

    public static <T> StreamCodec<RegistryFriendlyByteBuf, ReloadableHolder<T>> streamCodec(StreamCodec<RegistryFriendlyByteBuf, T> inner) {
        return StreamCodec.composite(ResourceLocation.STREAM_CODEC, ReloadableHolder::id,
                inner, ReloadableHolder::value, ReloadableHolder::new);
    }
}
