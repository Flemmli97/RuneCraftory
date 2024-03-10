package io.github.flemmli97.runecraftory.api.registry;

import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.common.registry.ModNPCLooks;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface NPCFeature {

    Codec<NPCFeatureHolder<?>> CODEC = ResourceLocation.CODEC.dispatch(e -> ModNPCLooks.NPC_FEATURE_REGISTRY.get().getIDFrom(e.getType()),
            id -> ModNPCLooks.NPC_FEATURE_REGISTRY.get().getFromId(id).codec);

    void writeToBuffer(FriendlyByteBuf buf);

    Tag save();

    NPCFeatureType<?> getType();
}
