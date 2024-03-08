package io.github.flemmli97.runecraftory.api.datapack;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.utils.CodecHelper;
import io.github.flemmli97.tenshilib.common.utils.SearchUtils;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
                    CodecHelper.ENTITY_PREDICATE_CODEC.optionalFieldOf("spawnerPredicate").forGetter(d -> Optional.ofNullable(d.spawnerPredicate == EntityPredicate.ANY ? null : d.spawnerPredicate)),

                    Codec.BOOL.fieldOf("needsRoof").forGetter(d -> d.needsRoof),
                    OnKilledIncrease.CODEC.listOf().optionalFieldOf("levelIncreaseOnKill").forGetter(d -> d.levelIncreaseOnKill.isEmpty() ? Optional.empty() : Optional.of(d.levelIncreaseOnKill)),
                    EntityRideActionCosts.CODEC.fieldOf("rideActionCosts").forGetter(d -> d.rideActionCosts),

                    Codec.BOOL.fieldOf("rideable").forGetter(d -> d.rideable),
                    Codec.BOOL.fieldOf("flying").forGetter(d -> d.flying),
                    ExtraCodecs.POSITIVE_INT.fieldOf("size").forGetter(d -> d.size),

                    ExtraCodecs.POSITIVE_INT.fieldOf("minLevel").forGetter(d -> d.minLevel),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("xp").forGetter(d -> d.xp),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("money").forGetter(d -> d.money),
                    Codec.FLOAT.fieldOf("tamingChance").forGetter(d -> d.tamingChance)
            ).apply(instance, (baseValues, levelGains, spawnerPredicate, needsRoof, levelIncreaseOnKill, rideActionCosts, rideable, flying, size, minLevel, xp, money, tamingChance) ->
                    new EntityProperties(minLevel, xp, money, tamingChance, rideable, flying, size, needsRoof, rideActionCosts, baseValues, levelGains, levelIncreaseOnKill.orElse(List.of()), spawnerPredicate.orElse(EntityPredicate.ANY))));

    public final int minLevel;
    public final int xp;
    public final int money;
    public final float tamingChance;
    public final boolean rideable;
    public final boolean flying;
    public final int size;
    public final boolean needsRoof;
    public final EntityRideActionCosts rideActionCosts;
    private final Map<Attribute, Double> baseValues;
    private final Map<Attribute, Double> levelGains;

    private final List<OnKilledIncrease> levelIncreaseOnKill;

    public final EntityPredicate spawnerPredicate;

    private EntityProperties(int minLevel, int xp, int money, float tamingChance, boolean rideable, boolean flying, int size, boolean needsRoof, EntityRideActionCosts rideActionCosts, Map<Attribute, Double> baseValues, Map<Attribute, Double> levelGains, List<OnKilledIncrease> levelIncreaseOnKill, EntityPredicate spawnerPredicate) {
        this.minLevel = Math.max(1, minLevel);
        this.xp = xp;
        this.money = money;
        this.tamingChance = tamingChance;
        this.rideable = rideable;
        this.flying = flying;
        this.size = size;
        this.needsRoof = needsRoof;
        this.rideActionCosts = rideActionCosts;
        this.baseValues = baseValues;
        this.levelGains = levelGains;
        this.levelIncreaseOnKill = levelIncreaseOnKill.stream().sorted().toList();
        this.spawnerPredicate = spawnerPredicate;
    }

    public Map<Attribute, Double> getBaseValues() {
        return ImmutableMap.copyOf(this.baseValues);
    }

    public Map<Attribute, Double> getAttributeGains() {
        return ImmutableMap.copyOf(this.levelGains);
    }

    public int levelIncreaseFromKill(int killed, ServerPlayer player) {
        return SearchUtils.searchInfFunc(this.levelIncreaseOnKill.stream().filter(c -> c.condition.matches(player, player)).toList(),
                p -> Integer.compare(p.minKilled(), killed), OnKilledIncrease.DEFAULT).increase();
    }

    private record OnKilledIncrease(int minKilled, int increase,
                                    EntityPredicate condition) implements Comparable<OnKilledIncrease> {

        private static final OnKilledIncrease DEFAULT = new OnKilledIncrease(0, 0, null);

        public static final Codec<OnKilledIncrease> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        Codec.INT.fieldOf("minKilled").forGetter(d -> d.minKilled),
                        Codec.INT.fieldOf("increase").forGetter(d -> d.increase),
                        CodecHelper.ENTITY_PREDICATE_CODEC.optionalFieldOf("predicate").forGetter(d -> Optional.ofNullable(d.condition == EntityPredicate.ANY ? null : d.condition))
                ).apply(inst, (minKilled, increase, predicate) -> new OnKilledIncrease(minKilled, increase, predicate.orElse(EntityPredicate.ANY))));

        @Override
        public int compareTo(@NotNull EntityProperties.OnKilledIncrease o) {
            return Integer.compare(this.minKilled, o.minKilled);
        }
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
        private EntityRideActionCosts rideActionCosts = EntityRideActionCosts.DEFAULT;
        private final List<OnKilledIncrease> levelIncreaseOnKill = new ArrayList<>();
        private EntityPredicate spawnerPredicate = EntityPredicate.ANY;

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

        public Builder withLevelIncrease(int minKilled, int increase) {
            this.levelIncreaseOnKill.add(new OnKilledIncrease(minKilled, increase, EntityPredicate.ANY));
            return this;
        }

        public Builder withLevelIncrease(int minKilled, int increase, EntityPredicate pred) {
            this.levelIncreaseOnKill.add(new OnKilledIncrease(minKilled, increase, pred));
            return this;
        }

        public Builder withRideActionCosts(EntityRideActionCosts.Builder costs) {
            this.rideActionCosts = costs.build();
            return this;
        }

        public Builder withSpawnerPredicate(EntityPredicate.Builder builder) {
            this.spawnerPredicate = builder.build();
            return this;
        }

        public EntityProperties build() {
            return new EntityProperties(this.minLevel, this.xp, this.money, this.taming, this.rideable, this.flying, this.size, this.needsRoof,
                    this.rideActionCosts, this.baseValues.entrySet().stream().collect(Collectors.toMap(
                    e -> e.getKey().get(),
                    Map.Entry::getValue,
                    (e1, e2) -> e1,
                    LinkedHashMap::new
            )),
                    this.gains.entrySet().stream().collect(Collectors.toMap(
                            e -> e.getKey().get(),
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    )), this.levelIncreaseOnKill, this.spawnerPredicate);
        }
    }
}