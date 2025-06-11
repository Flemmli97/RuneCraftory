package io.github.flemmli97.runecraftory.common.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ItemAttributeData {

    public static final ItemAttributeData DEFAULT = new ItemAttributeData(Map.of(), Map.of());
    public static final Codec<ItemAttributeData> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(Codec.unboundedMap(BuiltInRegistries.ATTRIBUTE.byNameCodec(), Codec.DOUBLE).fieldOf("base_stats").forGetter(d -> d.baseStats),
                    Codec.unboundedMap(BuiltInRegistries.ATTRIBUTE.byNameCodec(), Codec.DOUBLE).fieldOf("stats").forGetter(d -> d.stats)
            ).apply(instance, ItemAttributeData::new));
    private static final StreamCodec<RegistryFriendlyByteBuf, Map<Attribute, Double>> ATTRIBUTE_CODEC = ByteBufCodecs.
            map(HashMap::new, ByteBufCodecs.registry(Registries.ATTRIBUTE), ByteBufCodecs.DOUBLE);
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemAttributeData> STREAM_CODEC = new StreamCodec<>() {

        @Override
        public ItemAttributeData decode(RegistryFriendlyByteBuf buf) {
            return new ItemAttributeData(ATTRIBUTE_CODEC.decode(buf),
                    ATTRIBUTE_CODEC.decode(buf));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ItemAttributeData component) {
            ATTRIBUTE_CODEC.encode(buf, component.baseStats);
            ATTRIBUTE_CODEC.encode(buf, component.baseStats);
        }
    };

    private final Map<Attribute, Double> baseStats;
    private final Map<Attribute, Double> stats;

    private ItemAttributeData(Map<Attribute, Double> baseStats, Map<Attribute, Double> stats) {
        this.baseStats = baseStats;
        this.stats = stats;
    }

    public ItemAttributeData base(Map<Attribute, Double> baseStats) {
        return new ItemAttributeData(baseStats, this.getStats());
    }

    public ItemAttributeData of(Map<Attribute, Double> stats) {
        return new ItemAttributeData(this.getBaseStats(), stats);
    }

    public ItemAttributeData add(Attribute attribute, double value) {
        Map<Attribute, Double> map = new HashMap<>(this.stats);
        map.put(attribute, map.getOrDefault(attribute, 0d) + value);
        return new ItemAttributeData(this.getBaseStats(), map);
    }

    public Map<Attribute, Double> getBaseStats() {
        return Map.copyOf(this.baseStats);
    }

    public Map<Attribute, Double> getStats() {
        return Map.copyOf(this.stats);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof ItemAttributeData other) {
            return this.baseStats.equals(other.baseStats) && this.stats.equals(other.stats);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.baseStats, this.stats);
    }
}
