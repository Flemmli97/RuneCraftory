package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCLooks;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;

import java.util.List;

public record SimpleHatFeatureType(
        List<WeightedEntry.Wrapper<ResourceLocation>> hats) implements NPCFeature.NPCFeatureHolder<SimpleHatFeatureType.SimpleHatFeature> {

    private static final Codec<WeightedEntry.Wrapper<ResourceLocation>> VALUE_CODEC = RecordCodecBuilder.create(inst ->
            inst.group(ResourceLocation.CODEC.fieldOf("texture").forGetter(WeightedEntry.Wrapper::data),
                    Codec.INT.fieldOf("weight").forGetter(d -> d.getWeight().asInt())
            ).apply(inst, WeightedEntry::wrap));

    public static final MapCodec<SimpleHatFeatureType> TYPE_CODEC = VALUE_CODEC.listOf().fieldOf("hats")
            .xmap(SimpleHatFeatureType::new, SimpleHatFeatureType::hats);
    public static MapCodec<SimpleHatFeature> CODEC = ResourceLocation.CODEC.fieldOf("hat").xmap(SimpleHatFeature::new, SimpleHatFeature::hat);
    public static final StreamCodec<ByteBuf, SimpleHatFeature> STREAM_CODEC = ResourceLocation.STREAM_CODEC.map(SimpleHatFeature::new, SimpleHatFeature::hat);

    public SimpleHatFeatureType(ResourceLocation texture) {
        this(List.of(WeightedEntry.wrap(texture, 1)));
    }

    @Override
    public SimpleHatFeature create(NPCEntity npc) {
        return new SimpleHatFeature(this.hats.isEmpty() ? LibConstants.EMPTY : WeightedRandom.getRandomItem(npc.getRandom(), this.hats)
                                                                               .map(WeightedEntry.Wrapper::data).orElse(LibConstants.EMPTY));
    }

    @Override
    public NPCFeatureType<SimpleHatFeature> getType() {
        return RuneCraftoryNPCLooks.HAT.get();
    }

    public record SimpleHatFeature(ResourceLocation hat) implements NPCFeature {

        @Override
        public NPCFeatureType<SimpleHatFeature> type() {
            return RuneCraftoryNPCLooks.HAT.get();
        }
    }
}