package io.github.flemmli97.runecraftory.api.datapack;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.util.ExtraCodecs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public record SpellProperties(int cooldown, int rpCost, float percentageCost, float baseDamageMultiplier,
                              Map<Skills, Float> skillXP, Set<Skills> skills) {

    public static final Codec<SpellProperties> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(ExtraCodecs.NON_NEGATIVE_INT.fieldOf("cooldown").forGetter(SpellProperties::cooldown),
                    Codec.INT.fieldOf("runepoints_cost").forGetter(SpellProperties::rpCost),
                    Codec.FLOAT.fieldOf("percentage_cost").forGetter(SpellProperties::percentageCost),
                    Codec.FLOAT.fieldOf("base_damage_multiplier").forGetter(SpellProperties::baseDamageMultiplier),
                    Codec.unboundedMap(CodecUtils.stringEnumCodec(Skills.class, null), Codec.FLOAT).fieldOf("skill_xp").forGetter(SpellProperties::skillXP),
                    CodecUtils.stringEnumCodec(Skills.class, null).listOf().optionalFieldOf("skills").forGetter(d -> d.skills().isEmpty() ? Optional.empty() : Optional.of(List.copyOf(d.skills())))
            ).apply(instance, (cooldown, rpCost, percentage, baseDamageMultiplier, skillXp, skills) ->
                    new SpellProperties(cooldown, rpCost, percentage, baseDamageMultiplier, skillXp, skills.orElse(List.of()))));

    public static final SpellProperties DEFAULT_PROP = new SpellProperties(20, 0, 0, 1, new EnumMap<>(Skills.class), Set.of());

    public SpellProperties(int cooldown, int rpCost, float percentageCost, float baseDamageMultiplier, Map<Skills, Float> skillXP, Collection<Skills> skills) {
        this(cooldown, rpCost, percentageCost, baseDamageMultiplier, skillXP, Set.copyOf(skills));
    }

    public SpellProperties(int cooldown, int rpCost, float percentageCost, float baseDamageMultiplier, Map<Skills, Float> skillXP, Set<Skills> skills) {
        this.cooldown = cooldown;
        this.rpCost = rpCost;
        this.percentageCost = percentageCost;
        this.baseDamageMultiplier = baseDamageMultiplier;
        this.skillXP = skillXP.isEmpty() ? ImmutableMap.of() : ImmutableMap.copyOf(new EnumMap<>(skillXP));
        this.skills = ImmutableSet.copyOf(skills.isEmpty() ? EnumSet.noneOf(Skills.class) : EnumSet.copyOf(skills));
    }

    /**
     * Used in serialization
     */
    public static class Builder {

        private final Map<Skills, Float> xp = new EnumMap<>(Skills.class);

        private final int cooldown, rpCost;
        private final List<Skills> skills = new ArrayList<>();
        private float percentage, damageMultiplier = 1;

        public Builder(int cooldown, int rpCost) {
            this.cooldown = cooldown;
            this.rpCost = rpCost;
        }

        public SpellProperties.Builder withXPGain(Skills skill, float gain) {
            this.xp.put(skill, gain);
            return this;
        }

        public SpellProperties.Builder percentageCost(float cost) {
            this.percentage = cost;
            return this;
        }

        public SpellProperties.Builder affectedSkill(Skills skill) {
            this.skills.add(skill);
            return this;
        }

        public SpellProperties.Builder damageMultiplier(float multiplier) {
            this.damageMultiplier = multiplier;
            return this;
        }

        public SpellProperties build() {
            return new SpellProperties(this.cooldown, this.rpCost, this.percentage, this.damageMultiplier, this.xp, this.skills);
        }
    }
}