package io.github.flemmli97.runecraftory.common.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.StreamCodecUtils;
import it.unimi.dsi.fastutil.objects.Object2DoubleAVLTreeMap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FoodAttributeData {

    public static final FoodAttributeData DEFAULT = new FoodAttributeData(Map.of(), Map.of());
    public static final Codec<FoodAttributeData> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(Codec.unboundedMap(BuiltInRegistries.ATTRIBUTE.holderByNameCodec(), Codec.DOUBLE).fieldOf("flat_stats").forGetter(d -> d.flatStats),
                    Codec.unboundedMap(BuiltInRegistries.ATTRIBUTE.holderByNameCodec(), Codec.DOUBLE).fieldOf("multiplier_stats").forGetter(d -> d.multiplierStats)
            ).apply(instance, FoodAttributeData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, FoodAttributeData> STREAM_CODEC = new StreamCodec<>() {

        @Override
        public FoodAttributeData decode(RegistryFriendlyByteBuf buf) {
            return new FoodAttributeData(StreamCodecUtils.ATTRIBUTE_CODEC.decode(buf),
                    StreamCodecUtils.ATTRIBUTE_CODEC.decode(buf));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, FoodAttributeData component) {
            StreamCodecUtils.ATTRIBUTE_CODEC.encode(buf, component.flatStats);
            StreamCodecUtils.ATTRIBUTE_CODEC.encode(buf, component.multiplierStats);
        }
    };

    private final Map<Holder<Attribute>, Double> flatStats;
    private final Map<Holder<Attribute>, Double> multiplierStats;

    private FoodAttributeData(Map<Holder<Attribute>, Double> flatStats, Map<Holder<Attribute>, Double> multiplierStats) {
        Object2DoubleAVLTreeMap<Holder<Attribute>> stats = new Object2DoubleAVLTreeMap<>(ModAttributes.SORTED);
        stats.putAll(flatStats);
        this.flatStats = stats;
        Object2DoubleAVLTreeMap<Holder<Attribute>> stats2 = new Object2DoubleAVLTreeMap<>(ModAttributes.SORTED);
        stats2.putAll(multiplierStats);
        this.multiplierStats = stats2;
    }

    public FoodAttributeData add(Map<Holder<Attribute>, Double> stats) {
        Map<Holder<Attribute>, Double> map = new HashMap<>(this.flatStats);
        stats.forEach((attribute, value) -> map.put(attribute, map.getOrDefault(attribute, 0d) + value));
        return new FoodAttributeData(map, this.getFlatStats());
    }

    public FoodAttributeData addMultiplier(Map<Holder<Attribute>, Double> stats) {
        Map<Holder<Attribute>, Double> map = new HashMap<>(this.multiplierStats);
        stats.forEach((attribute, value) -> map.put(attribute, map.getOrDefault(attribute, 0d) + value));
        return new FoodAttributeData(this.getFlatStats(), map);
    }

    public Map<Holder<Attribute>, Double> getFlatStats() {
        return Map.copyOf(this.flatStats);
    }

    public Map<Holder<Attribute>, Double> getMultiplierStats() {
        return Map.copyOf(this.multiplierStats);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof FoodAttributeData other) {
            return this.flatStats.equals(other.flatStats) && this.multiplierStats.equals(other.multiplierStats);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.flatStats, this.multiplierStats);
    }
}
