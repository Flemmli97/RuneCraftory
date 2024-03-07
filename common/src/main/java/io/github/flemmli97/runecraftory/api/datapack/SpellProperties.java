package io.github.flemmli97.runecraftory.api.datapack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.utils.CodecHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SpellProperties {

    public final Map<EnumSkills, Float> skillXP;
    public final int cooldown, rpCost;
    public final boolean percentage;
    public final Set<EnumSkills> skills;

    public static final Codec<SpellProperties> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.BOOL.fieldOf("percentage").forGetter(d -> d.percentage),
                    CodecHelper.enumCodec(EnumSkills.class, null).listOf().optionalFieldOf("skills").forGetter(d -> d.skills.isEmpty() ? Optional.empty() : Optional.of(List.copyOf(d.skills))),

                    Codec.INT.fieldOf("cooldown").forGetter(d -> d.cooldown),
                    Codec.INT.fieldOf("rpCost").forGetter(d -> d.rpCost),
                    Codec.unboundedMap(CodecHelper.enumCodec(EnumSkills.class, null), Codec.FLOAT).fieldOf("skillXP").forGetter(d -> d.skillXP)
            ).apply(instance, (percentage, skills, cooldown, rpCost, skillXp) -> new SpellProperties(skillXp, cooldown, rpCost, percentage, skills.orElse(List.of()))));

    public static final SpellProperties DEFAULT_PROP = new SpellProperties(new EnumMap<>(EnumSkills.class), 20, 0, false, List.of());

    public SpellProperties(Map<EnumSkills, Float> skillXP, int cooldown, int rpCost, boolean percentage, List<EnumSkills> skills) {
        this.percentage = percentage;
        EnumSet<EnumSkills> reducingSkills = skills.isEmpty() ? EnumSet.noneOf(EnumSkills.class) : EnumSet.copyOf(skills);
        this.skills = Collections.unmodifiableSet(reducingSkills);
        EnumMap<EnumSkills, Float> xp = new EnumMap<>(EnumSkills.class);
        xp.putAll(skillXP);
        this.skillXP = Collections.unmodifiableMap(xp);
        this.cooldown = cooldown;
        this.rpCost = rpCost;
    }

    /**
     * Used in serialization
     */
    public static class Builder {

        private final Map<EnumSkills, Float> xp = new EnumMap<>(EnumSkills.class);

        private final int cooldown, rpCost;
        private boolean percentage;
        private final List<EnumSkills> skills = new ArrayList<>();

        public Builder(int cooldown, int rpCost) {
            this.cooldown = cooldown;
            this.rpCost = rpCost;
        }

        public SpellProperties.Builder withXPGain(EnumSkills skill, float gain) {
            this.xp.put(skill, gain);
            return this;
        }

        public SpellProperties.Builder usePercentageCost() {
            this.percentage = true;
            return this;
        }

        public SpellProperties.Builder affectedSkill(EnumSkills skill) {
            this.skills.add(skill);
            return this;
        }

        public SpellProperties build() {
            return new SpellProperties(this.xp, this.cooldown, this.rpCost, this.percentage, this.skills);
        }
    }
}