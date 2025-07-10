package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureHolder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModNPCLooks;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record SimpleHatFeatureType(
        List<String> hats) implements NPCFeatureHolder<SimpleHatFeatureType.SimpleHatFeature> {

    public static final MapCodec<SimpleHatFeatureType> TYPE_CODEC = Codec.STRING.listOf().fieldOf("hats")
            .xmap(SimpleHatFeatureType::new, SimpleHatFeatureType::hats);
    public static MapCodec<SimpleHatFeature> CODEC = Codec.STRING.fieldOf("hat").xmap(SimpleHatFeature::new, SimpleHatFeature::hat);
    public static final StreamCodec<ByteBuf, SimpleHatFeature> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(SimpleHatFeature::new, SimpleHatFeature::hat);

    @Override
    public SimpleHatFeature create(EntityNPCBase npc) {
        return new SimpleHatFeature(this.hats.isEmpty() ? "" : this.hats.get(npc.getRandom().nextInt(this.hats.size())));
    }

    @Override
    public NPCFeatureType<SimpleHatFeature> getType() {
        return ModNPCLooks.HAT.get();
    }

    public record SimpleHatFeature(String hat) implements NPCFeature {

        @Override
        public NPCFeatureType<SimpleHatFeature> type() {
            return ModNPCLooks.HAT.get();
        }
    }
}