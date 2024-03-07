package io.github.flemmli97.runecraftory.api.registry;

import com.mojang.serialization.Codec;
import io.github.flemmli97.tenshilib.platform.registry.CustomRegistryEntry;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Function;

public class NPCFeatureType<T extends NPCFeature> extends CustomRegistryEntry<NPCFeatureType<T>> {

    private final Function<FriendlyByteBuf, T> factory;
    private final Codec<T> codec;

    public NPCFeatureType(Function<FriendlyByteBuf, T> factory, Codec<T> codec) {
        this.factory = factory;
        this.codec = codec;
    }

    public T create(FriendlyByteBuf buf) {
        return this.factory.apply(buf);
    }

    public Codec<T> getCodec() {
        return this.codec;
    }

    @Override
    public String toString() {
        return "NPC Feature type: " + this.getRegistryName();
    }
}
