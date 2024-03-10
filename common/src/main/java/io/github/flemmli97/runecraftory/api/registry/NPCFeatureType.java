package io.github.flemmli97.runecraftory.api.registry;

import com.mojang.serialization.Codec;
import io.github.flemmli97.tenshilib.platform.registry.CustomRegistryEntry;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Function;

public class NPCFeatureType<F extends NPCFeature> extends CustomRegistryEntry<NPCFeatureType<F>> {

    public final Codec<? extends NPCFeatureHolder<F>> codec;
    public final Function<FriendlyByteBuf, F> pkt;
    public final Function<Tag, F> load;

    public NPCFeatureType(Codec<? extends NPCFeatureHolder<F>> codec, Function<FriendlyByteBuf, F> pkt, Function<Tag, F> load) {
        this.codec = codec;
        this.pkt = pkt;
        this.load = load;
    }

    @Override
    public String toString() {
        return "NPC Feature type: " + this.getRegistryName();
    }
}
