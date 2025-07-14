package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.serialization.MapCodec;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCLooks;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class SlimLookFeatureType implements NPCFeature.NPCFeatureHolder<SlimLookFeatureType.SlimLookFeature> {

    public static final SlimLookFeatureType TYPE_INSTANCE = new SlimLookFeatureType();
    public static final SlimLookFeature FEATURE = new SlimLookFeature();
    public static final MapCodec<SlimLookFeatureType> TYPE_CODEC = MapCodec.unit(TYPE_INSTANCE);
    public static final MapCodec<SlimLookFeature> CODEC = MapCodec.unit(FEATURE);
    public static final StreamCodec<ByteBuf, SlimLookFeature> STREAM_CODEC = StreamCodec.unit(FEATURE);

    private SlimLookFeatureType() {
    }

    @Override
    public SlimLookFeature create(NPCEntity npc) {
        return FEATURE;
    }

    @Override
    public NPCFeatureType<SlimLookFeature> getType() {
        return RuneCraftoryNPCLooks.SLIM.get();
    }

    public static class SlimLookFeature implements NPCFeature {

        private SlimLookFeature() {
        }

        @Override
        public NPCFeatureType<SlimLookFeature> type() {
            return RuneCraftoryNPCLooks.SLIM.get();
        }
    }
}
