package io.github.flemmli97.runecraftory.api.registry;

import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCLooks;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public interface NPCFeature {

    Codec<NPCFeatureHolder<?>> CODEC = RuneCraftoryNPCLooks.NPC_FEATURES.registry().byNameCodec()
            .dispatch(NPCFeatureHolder::getType, NPCFeatureType::holderCodec);
    Codec<NPCFeature> FEATURE_CODEC = RuneCraftoryNPCLooks.NPC_FEATURES.registry().byNameCodec()
            .dispatch(NPCFeature::type, NPCFeatureType::codec);
    @SuppressWarnings("unchecked")
    StreamCodec<RegistryFriendlyByteBuf, NPCFeature> STREAM_CODEC = ByteBufCodecs.registry(RuneCraftoryNPCLooks.NPC_FEATURE_REGISTRY_KEY)
            .dispatch(NPCFeature::type, s -> (StreamCodec<ByteBuf, NPCFeature>) s.streamCodec());

    NPCFeatureType<?> type();

    interface NPCFeatureHolder<F extends NPCFeature> {

        F create(NPCEntity npc);

        NPCFeatureType<F> getType();
    }
}
