package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureHolder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModNPCLooks;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public class SlimLookFeatureType implements NPCFeatureHolder<SlimLookFeatureType.SlimLookFeature> {

    public static final Codec<SlimLookFeatureType> CODEC = Codec.unit(SlimLookFeatureType::new);
    public static final SlimLookFeature FEATURE = new SlimLookFeature();

    @Override
    public SlimLookFeature create(EntityNPCBase npc) {
        return FEATURE;
    }

    @Override
    public NPCFeatureType<SlimLookFeature> getType() {
        return ModNPCLooks.SLIM.get();
    }

    public static class SlimLookFeature implements NPCFeature {

        public SlimLookFeature() {
        }

        @Override
        public void writeToBuffer(FriendlyByteBuf buf) {
        }

        @Override
        public Tag save() {
            return null;
        }

        @Override
        public NPCFeatureType<SlimLookFeature> getType() {
            return ModNPCLooks.SLIM.get();
        }

    }
}
