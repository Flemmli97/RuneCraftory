package io.github.flemmli97.runecraftory.api.datapack;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class EntityProperties {

    public static final EntityProperties DEFAULT_PROP = new EntityProperties.Builder()
            .putAttributes(() -> Attributes.MAX_HEALTH, 20)
            .putAttributes(() -> Attributes.ATTACK_DAMAGE, 1)
            .xp(5).money(5).tamingChance(0.3f).build();

    public static final Codec<EntityProperties> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.unboundedMap(Registry.ATTRIBUTE.byNameCodec(), Codec.DOUBLE).fieldOf("baseValues").forGetter(d -> d.baseValues),
                    Codec.unboundedMap(Registry.ATTRIBUTE.byNameCodec(), Codec.DOUBLE).fieldOf("levelGains").forGetter(d -> d.levelGains),

                    Codec.BOOL.fieldOf("flying").forGetter(d -> d.flying),
                    ExtraCodecs.POSITIVE_INT.fieldOf("size").forGetter(d -> d.size),
                    Codec.BOOL.fieldOf("needsRoof").forGetter(d -> d.needsRoof),

                    Codec.FLOAT.fieldOf("tamingChance").forGetter(d -> d.tamingChance),
                    Codec.BOOL.fieldOf("rideable").forGetter(d -> d.rideable),

                    ExtraCodecs.POSITIVE_INT.fieldOf("minLevel").forGetter(d -> d.minLevel),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("xp").forGetter(d -> d.xp),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("money").forGetter(d -> d.money)
            ).apply(instance, (baseValues, levelGains, flying, size, needsRoof, tamingChance, rideable, minLevel, xp, money) -> new EntityProperties(minLevel, xp, money, tamingChance, rideable, flying, size, needsRoof, baseValues, levelGains)));

    public final int minLevel;
    public final int xp;
    public final int money;
    public final float tamingChance;
    public final boolean rideable;
    public final boolean flying;
    public final int size;
    public final boolean needsRoof;
    private final Map<Attribute, Double> baseValues;
    private final Map<Attribute, Double> levelGains;

    private EntityProperties(int minLevel, int xp, int money, float tamingChance, boolean rideable, boolean flying, int size, boolean needsRoof, Map<Attribute, Double> baseValues, Map<Attribute, Double> levelGains) {
        this.minLevel = Math.max(1, minLevel);
        this.xp = xp;
        this.money = money;
        this.tamingChance = tamingChance;
        this.rideable = rideable;
        this.flying = flying;
        this.size = size;
        this.needsRoof = needsRoof;
        this.baseValues = baseValues;
        this.levelGains = levelGains;
    }

    public Map<Attribute, Double> getBaseValues() {
        return ImmutableMap.copyOf(this.baseValues);
    }

    public Map<Attribute, Double> getAttributeGains() {
        return ImmutableMap.copyOf(this.levelGains);
    }

    public static class Builder {

        private final Map<Supplier<Attribute>, Double> baseValues = new LinkedHashMap<>();
        private final Map<Supplier<Attribute>, Double> gains = new LinkedHashMap<>();
        private int xp;
        private int money;
        private float taming;
        private boolean rideable;
        private boolean flying;
        private int size = 1;
        private boolean needsRoof = true;
        private int minLevel = 1;

        public Builder putAttributes(Supplier<Attribute> att, double val) {
            this.baseValues.put(att, val);
            return this;
        }

        public Builder putLevelGains(Supplier<Attribute> att, double val) {
            this.gains.put(att, val);
            return this;
        }

        public Builder xp(int xp) {
            this.xp = xp;
            return this;
        }

        public Builder money(int money) {
            this.money = money;
            return this;
        }

        public Builder tamingChance(float chance) {
            this.taming = chance;
            return this;
        }

        public Builder setRideable() {
            this.rideable = true;
            return this;
        }

        public Builder setFlying() {
            this.flying = true;
            return this;
        }

        public Builder setBarnOccupancy(int size) {
            this.size = Math.max(1, size);
            return this;
        }

        public Builder doesntNeedBarnRoof() {
            this.needsRoof = false;
            return this;
        }

        public Builder setMinLevel(int minLevel) {
            this.minLevel = minLevel;
            return this;
        }

        public EntityProperties build() {
            return new EntityProperties(this.minLevel, this.xp, this.money, this.taming, this.rideable, this.flying, this.size, this.needsRoof,
                    this.baseValues.entrySet().stream().collect(Collectors.toMap(
                            e -> e.getKey().get(),
                            Map.Entry::getValue
                    )),
                    this.gains.entrySet().stream().collect(Collectors.toMap(
                            e -> e.getKey().get(),
                            Map.Entry::getValue
                    )));
        }
    }
}