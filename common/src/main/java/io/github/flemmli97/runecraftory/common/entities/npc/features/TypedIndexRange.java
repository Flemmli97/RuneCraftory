package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;

import java.util.List;

public class TypedIndexRange {

    private static final Codec<WeightedEntry.Wrapper<Pair<String, IndexRange>>> VAL = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.INT.fieldOf("weight").forGetter(d -> d.getWeight().asInt()),
                    Codec.STRING.fieldOf("outfit").forGetter(d -> d.data().getFirst()),
                    IndexRange.CODEC.fieldOf("range").forGetter(d -> d.data().getSecond())
            ).apply(inst, (weight, type, range) -> WeightedEntry.wrap(Pair.of(type, range), weight)));

    public static final Codec<TypedIndexRange> CODEC = VAL.listOf().xmap(TypedIndexRange::new, t -> t.outfits);

    public static final Pair<String, Integer> NONE = Pair.of("", 0);

    private final List<WeightedEntry.Wrapper<Pair<String, IndexRange>>> outfits;

    public TypedIndexRange(List<WeightedEntry.Wrapper<Pair<String, IndexRange>>> outfits) {
        this.outfits = outfits;
    }

    public Pair<String, Integer> getRandom(RandomSource random) {
        if (this.outfits.isEmpty())
            return NONE;
        return WeightedRandom.getRandomItem(random, this.outfits)
                .map(p -> p.data().mapSecond(i -> i.getRandom(random))).orElse(NONE);
    }
}
