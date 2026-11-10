package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCLooks;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record HairFeatureType(WeightedIndexedTexture hairs,
                              ColorSetting color) implements NPCFeature.NPCFeatureHolder<HairFeatureType.HairFeature> {

    public static final MapCodec<HairFeatureType> TYPE_CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(WeightedIndexedTexture.CODEC.fieldOf("hairs").forGetter(d -> d.hairs),
                    ColorSetting.CODEC.fieldOf("colors").forGetter(d -> d.color)
            ).apply(inst, HairFeatureType::new));
    public static MapCodec<HairFeature> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(IndexedTexture.CODEC.fieldOf("hair").forGetter(HairFeature::hair),
                    Codec.INT.fieldOf("color").forGetter(HairFeature::color)
            ).apply(inst, HairFeature::new));
    public static final StreamCodec<ByteBuf, HairFeature> STREAM_CODEC = StreamCodec.composite(
            IndexedTexture.STREAM_CODEC, HairFeature::hair, ByteBufCodecs.INT, HairFeature::color, HairFeature::new);

    @Override
    public HairFeature create(NPCEntity npc) {
        IndexedTexture hair = this.hairs.getRandom(npc.getRandom());
        return new HairFeature(hair, this.color.getRandom(npc.getRandom()));
    }

    @Override
    public NPCFeatureType<HairFeature> getType() {
        return RuneCraftoryNPCLooks.HAIR.get();
    }

    public record HairFeature(IndexedTexture hair, int color) implements NPCFeature {

        @Override
        public NPCFeatureType<HairFeature> type() {
            return RuneCraftoryNPCLooks.HAIR.get();
        }
    }
}