package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WeightedIndexedTexture {

    private static final Codec<WeightedEntry.Wrapper<Pair<ResourceLocation, Optional<IndexRange>>>> VALUE_CODEC = RecordCodecBuilder.create(inst ->
            inst.group(ResourceLocation.CODEC.fieldOf("texture").forGetter(d -> d.data().getFirst()),
                    IndexRange.CODEC.optionalFieldOf("range").forGetter(d -> d.data().getSecond()),
                    Codec.INT.fieldOf("weight").forGetter(d -> d.getWeight().asInt())
            ).apply(inst, (texture, range, weight) -> WeightedEntry.wrap(Pair.of(texture, range), weight)));

    public static final Codec<WeightedIndexedTexture> CODEC = VALUE_CODEC.listOf().xmap(WeightedIndexedTexture::new, t -> t.textures);

    public static final IndexedTexture NONE = new IndexedTexture(RuneCraftory.modRes(""), Optional.empty());

    private final List<WeightedEntry.Wrapper<Pair<ResourceLocation, Optional<IndexRange>>>> textures;

    public WeightedIndexedTexture(ResourceLocation texture) {
        this(List.of(WeightedEntry.wrap(Pair.of(texture, Optional.empty()), 1)));
    }

    public WeightedIndexedTexture(ResourceLocation texture, IndexRange range) {
        this(List.of(WeightedEntry.wrap(Pair.of(texture, Optional.of(range)), 1)));
    }

    public WeightedIndexedTexture(List<WeightedEntry.Wrapper<Pair<ResourceLocation, Optional<IndexRange>>>> textures) {
        this.textures = textures;
    }

    public IndexedTexture getRandom(RandomSource random) {
        if (this.textures.isEmpty())
            return NONE;
        return WeightedRandom.getRandomItem(random, this.textures)
                .map(p -> new IndexedTexture(p.data().getFirst(), p.data().getSecond().map(r -> r.getRandom(random)))).orElse(NONE);
    }

    public static class Builder {

        private final List<WeightedEntry.Wrapper<Pair<ResourceLocation, Optional<IndexRange>>>> textures = new ArrayList<>();

        public void add(ResourceLocation texture, int weight) {
            this.textures.add(WeightedEntry.wrap(Pair.of(texture, Optional.empty()), weight));
        }

        public void add(ResourceLocation texture, IndexRange range, int weight) {
            this.textures.add(WeightedEntry.wrap(Pair.of(texture, Optional.of(range)), weight));
        }

        public WeightedIndexedTexture build() {
            return new WeightedIndexedTexture(this.textures);
        }
    }
}
