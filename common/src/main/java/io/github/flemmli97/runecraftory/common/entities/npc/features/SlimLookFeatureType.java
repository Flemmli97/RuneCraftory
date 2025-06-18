package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.serialization.MapCodec;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureHolder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModNPCLooks;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class SlimLookFeatureType implements NPCFeatureHolder<SlimLookFeatureType.SlimLookFeature> {

    public static final SlimLookFeatureType TYPE_INSTANCE = new SlimLookFeatureType();
    public static final SlimLookFeature FEATURE = new SlimLookFeature();
    public static final MapCodec<SlimLookFeatureType> TYPE_CODEC = MapCodec.unit(TYPE_INSTANCE);
    public static final MapCodec<SlimLookFeature> CODEC = MapCodec.unit(FEATURE);
    public static final StreamCodec<ByteBuf, SlimLookFeature> STREAM_CODEC = StreamCodec.unit(FEATURE);

    private SlimLookFeatureType() {
    }

    @Override
    public SlimLookFeature create(EntityNPCBase npc) {
        return FEATURE;
    }

    @Override
    public NPCFeatureType<SlimLookFeature> getType() {
        return ModNPCLooks.SLIM.get();
    }

    public static class SlimLookFeature implements NPCFeature {

        private SlimLookFeature() {
        }

        @Override
        public NPCFeatureType<SlimLookFeature> type() {
            return ModNPCLooks.SLIM.get();
        }
    }
}
