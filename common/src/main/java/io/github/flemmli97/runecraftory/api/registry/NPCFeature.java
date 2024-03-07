package io.github.flemmli97.runecraftory.api.registry;

import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.common.registry.ModNPCLooks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public abstract class NPCFeature {

    public static final Codec<NPCFeature> CODEC = ResourceLocation.CODEC.dispatch(e -> ModNPCLooks.NPC_FEATURE_REGISTRY.get().getIDFrom(e.getType()), id -> ModNPCLooks.NPC_FEATURE_REGISTRY.get().getFromId(id).getCodec());

    public abstract void writeToBuffer(FriendlyByteBuf buf);

    public abstract NPCFeatureType<?> getType();
}
