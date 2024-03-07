package io.github.flemmli97.runecraftory.common.entities.npc.features;

import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.registry.ModNPCLooks;
import net.minecraft.network.FriendlyByteBuf;

public class SlimLookFeature extends NPCFeature {
    @Override
    public void writeToBuffer(FriendlyByteBuf buf) {
    }

    @Override
    public NPCFeatureType<?> getType() {
        return ModNPCLooks.SLIM.get();
    }
}
