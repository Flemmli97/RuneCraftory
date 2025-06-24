package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;

import java.util.List;

public record IndexedColorSetting(List<Integer> indices, ColorSetting color) {

    public static final Codec<IndexedColorSetting> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.INT.listOf().fieldOf("indices").forGetter(d -> d.indices),
                    ColorSetting.CODEC.fieldOf("colors").forGetter(d -> d.color)
            ).apply(inst, IndexedColorSetting::new));

    public ResolvedIndexColor resolve(RandomSource random) {
        int index = this.indices.isEmpty() ? 0 : this.indices.get(random.nextInt(this.indices.size()));
        return new ResolvedIndexColor(index, this.color.getRandom(random));
    }

    public record ResolvedIndexColor(int index, int color) {

        public static final Codec<ResolvedIndexColor> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        Codec.INT.fieldOf("index").forGetter(ResolvedIndexColor::index),
                        Codec.INT.fieldOf("color").forGetter(ResolvedIndexColor::color)
                ).apply(inst, ResolvedIndexColor::new));
        public static final StreamCodec<ByteBuf, ResolvedIndexColor> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, ResolvedIndexColor::index, ByteBufCodecs.INT, ResolvedIndexColor::color, ResolvedIndexColor::new
        );
    }
}
