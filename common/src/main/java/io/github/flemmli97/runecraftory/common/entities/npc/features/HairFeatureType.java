package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.datafixers.util.Pair;
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

public record HairFeatureType(TypedIndexRange types,
                              ColorSetting color) implements NPCFeature.NPCFeatureHolder<HairFeatureType.HairFeature> {

    public static final MapCodec<HairFeatureType> TYPE_CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(TypedIndexRange.CODEC.fieldOf("styles").forGetter(d -> d.types),
                    ColorSetting.CODEC.fieldOf("colors").forGetter(d -> d.color)
            ).apply(inst, HairFeatureType::new));
    public static MapCodec<HairFeature> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(Codec.STRING.fieldOf("style").forGetter(HairFeature::hair),
                    Codec.INT.fieldOf("index").forGetter(HairFeature::index),
                    Codec.INT.fieldOf("color").forGetter(HairFeature::color)
            ).apply(inst, HairFeature::new));
    public static final StreamCodec<ByteBuf, HairFeature> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, HairFeature::hair, ByteBufCodecs.INT, HairFeature::index,
            ByteBufCodecs.INT, HairFeature::color, HairFeature::new);

    @Override
    public HairFeature create(NPCEntity npc) {
        Pair<String, Integer> style = this.types.getRandom(npc.getRandom());
        return new HairFeature(style.getFirst(), style.getSecond(), this.color.getRandom(npc.getRandom()));
    }

    @Override
    public NPCFeatureType<HairFeature> getType() {
        return RuneCraftoryNPCLooks.HAIR.get();
    }

    public record HairFeature(String hair, int index, int color) implements NPCFeature {

        @Override
        public NPCFeatureType<HairFeature> type() {
            return RuneCraftoryNPCLooks.HAIR.get();
        }
    }
}