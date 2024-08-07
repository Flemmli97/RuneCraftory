package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.util.ExtraCodecs;

import java.util.List;
import java.util.Random;

public interface IndexRange {

    Codec<IndexRange> CODEC = CodecUtils.stringEnumCodec(Type.class, Type.FIRST_N)
            .dispatch("type", IndexRange::getType, type -> switch (type) {
                case FIRST_N -> FirstNIndices.CODEC;
                case RANGE -> RangeIndices.CODEC;
                case SELECTED -> SelectedIndices.CODEC;
            });

    Type getType();

    int getRandom(Random random);

    enum Type {
        FIRST_N,
        RANGE,
        SELECTED
    }

    record FirstNIndices(int n) implements IndexRange {

        public static final Codec<FirstNIndices> CODEC = ExtraCodecs.POSITIVE_INT.fieldOf("amount").xmap(FirstNIndices::new, FirstNIndices::n).codec();

        @Override
        public Type getType() {
            return Type.FIRST_N;
        }

        @Override
        public int getRandom(Random random) {
            if (this.n <= 0)
                return 0;
            return random.nextInt(this.n);
        }
    }

    record RangeIndices(int min, int max) implements IndexRange {

        public static final Codec<RangeIndices> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        Codec.INT.fieldOf("min").forGetter(d -> d.min),
                        Codec.INT.fieldOf("max").forGetter(d -> d.max)
                ).apply(inst, RangeIndices::new));

        @Override
        public Type getType() {
            return Type.FIRST_N;
        }

        @Override
        public int getRandom(Random random) {
            return random.nextInt(this.max - this.min) + this.min;
        }
    }

    record SelectedIndices(List<Integer> indices) implements IndexRange {

        public static final Codec<SelectedIndices> CODEC = Codec.INT.listOf().fieldOf("indices").xmap(SelectedIndices::new, SelectedIndices::indices).codec();

        @Override
        public Type getType() {
            return Type.FIRST_N;
        }

        @Override
        public int getRandom(Random random) {
            return this.indices.isEmpty() ? 0 : this.indices.get(random.nextInt(this.indices.size()));
        }
    }
}
