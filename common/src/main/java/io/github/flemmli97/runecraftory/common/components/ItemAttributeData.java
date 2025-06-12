package io.github.flemmli97.runecraftory.common.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import it.unimi.dsi.fastutil.objects.Object2DoubleAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleSortedMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleSortedMaps;
import net.minecraft.core.Holder;
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
            instance.group(Codec.unboundedMap(BuiltInRegistries.ATTRIBUTE.holderByNameCodec(), Codec.DOUBLE).fieldOf("base_stats").forGetter(d -> d.baseStats),
                    Codec.unboundedMap(BuiltInRegistries.ATTRIBUTE.holderByNameCodec(), Codec.DOUBLE).fieldOf("stats").forGetter(d -> d.stats)
            ).apply(instance, ItemAttributeData::new));
    private static final StreamCodec<RegistryFriendlyByteBuf, Map<Holder<Attribute>, Double>> ATTRIBUTE_CODEC = ByteBufCodecs.
            map(HashMap::new, ByteBufCodecs.holderRegistry(Registries.ATTRIBUTE), ByteBufCodecs.DOUBLE);
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

    private final Map<Holder<Attribute>, Double> baseStats;
    private final Map<Holder<Attribute>, Double> stats;

    private final Object2DoubleSortedMap<Holder<Attribute>> totalStats;

    private ItemAttributeData(Map<Holder<Attribute>, Double> baseStats, Map<Holder<Attribute>, Double> stats) {
        this.baseStats = baseStats;
        this.stats = stats;
        Object2DoubleAVLTreeMap<Holder<Attribute>> map = new Object2DoubleAVLTreeMap<>(ModAttributes.SORTED);
        map.putAll(this.baseStats);
        map.putAll(this.stats);
        this.totalStats = Object2DoubleSortedMaps.unmodifiable(map);
    }

    public ItemAttributeData base(Map<Holder<Attribute>, Double> baseStats) {
        return new ItemAttributeData(baseStats, this.getStats());
    }

    public ItemAttributeData of(Map<Holder<Attribute>, Double> stats) {
        return new ItemAttributeData(this.getBaseStats(), stats);
    }

    public ItemAttributeData add(Holder<Attribute> attribute, double value) {
        Map<Holder<Attribute>, Double> map = new HashMap<>(this.stats);
        map.put(attribute, map.getOrDefault(attribute, 0d) + value);
        return new ItemAttributeData(this.getBaseStats(), map);
    }

    public Map<Holder<Attribute>, Double> getBaseStats() {
        return Map.copyOf(this.baseStats);
    }

    public Map<Holder<Attribute>, Double> getStats() {
        return Map.copyOf(this.stats);
    }

    public Map<Holder<Attribute>, Double> getTotalStats() {
        return this.totalStats;
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
