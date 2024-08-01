package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;

import java.util.List;
import java.util.Random;

public class TypedIndexRange {

    private static final Codec<WeightedEntry.Wrapper<Pair<String, IndexRange>>> VAL = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.INT.fieldOf("weight").forGetter(d -> d.getWeight().asInt()),
                    Codec.STRING.fieldOf("type").forGetter(d -> d.getData().getFirst()),
                    IndexRange.CODEC.fieldOf("range").forGetter(d -> d.getData().getSecond())
            ).apply(inst, (weight, type, range) -> WeightedEntry.wrap(Pair.of(type, range), weight)));

    public static final Codec<TypedIndexRange> CODEC = VAL.listOf().xmap(TypedIndexRange::new, t -> t.types);

    public static final Pair<String, Integer> NONE = Pair.of("", 0);

    private final List<WeightedEntry.Wrapper<Pair<String, IndexRange>>> types;

    public TypedIndexRange(List<WeightedEntry.Wrapper<Pair<String, IndexRange>>> types) {
        this.types = types;
    }

    public Pair<String, Integer> getRandom(Random random) {
        if (this.types.isEmpty())
            return NONE;
        return WeightedRandom.getRandomItem(random, this.types)
                .map(p -> p.getData().mapSecond(i -> i.getRandom(random))).orElse(NONE);
    }
}
