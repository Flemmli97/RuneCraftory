package io.github.flemmli97.runecraftory.api.registry;

import com.mojang.serialization.MapCodec;
import io.github.flemmli97.runecraftory.common.registry.ModNPCLooks;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Function;

public record NPCFeatureType<F extends NPCFeature>(MapCodec<? extends NPCFeatureHolder<F>> codec,
                                                   Function<FriendlyByteBuf, F> pkt, Function<Tag, F> load) {

    @Override
    public String toString() {
        return ModNPCLooks.NPC_FEATURES.registry().getKey(this).toString();
    }
}
