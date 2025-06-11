package io.github.flemmli97.runecraftory.api.registry;

import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.common.registry.ModNPCLooks;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public interface NPCFeature {

    Codec<NPCFeatureHolder<?>> CODEC = ModNPCLooks.NPC_FEATURES.registry().byNameCodec()
            .dispatch(NPCFeatureHolder::getType, NPCFeatureType::codec);

    void writeToBuffer(FriendlyByteBuf buf);

    Tag save();

    NPCFeatureType<?> getType();
}
