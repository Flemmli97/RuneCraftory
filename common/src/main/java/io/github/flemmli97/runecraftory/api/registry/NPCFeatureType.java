package io.github.flemmli97.runecraftory.api.registry;

import com.mojang.serialization.MapCodec;
import io.github.flemmli97.runecraftory.common.registry.ModNPCLooks;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record NPCFeatureType<F extends NPCFeature>(MapCodec<? extends NPCFeature.NPCFeatureHolder<F>> holderCodec,
                                                   MapCodec<F> codec,
                                                   StreamCodec<? extends ByteBuf, F> streamCodec) {

    @Override
    public String toString() {
        return ModNPCLooks.NPC_FEATURES.registry().getKey(this).toString();
    }
}
