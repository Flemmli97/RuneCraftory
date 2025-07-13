package io.github.flemmli97.runecraftory.api.datapack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.util.ExtraCodecs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SpellProperties {

    public static final Codec<SpellProperties> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.FLOAT.fieldOf("percentage").forGetter(d -> d.percentage),
                    CodecUtils.stringEnumCodec(Skills.class, null).listOf().optionalFieldOf("skills").forGetter(d -> d.skills.isEmpty() ? Optional.empty() : Optional.of(List.copyOf(d.skills))),

                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("cooldown").forGetter(d -> d.cooldown),
                    Codec.INT.fieldOf("rp_cost").forGetter(d -> d.rpCost),
                    Codec.unboundedMap(CodecUtils.stringEnumCodec(Skills.class, null), Codec.FLOAT).fieldOf("skill_xp").forGetter(d -> d.skillXP)
            ).apply(instance, (percentage, skills, cooldown, rpCost, skillXp) -> new SpellProperties(skillXp, cooldown, rpCost, percentage, skills.orElse(List.of()))));
    public static final SpellProperties DEFAULT_PROP = new SpellProperties(new EnumMap<>(Skills.class), 20, 0, 0, List.of());
    public final Map<Skills, Float> skillXP;
    public final int cooldown, rpCost;
    public final float percentage;
    public final Set<Skills> skills;

    public SpellProperties(Map<Skills, Float> skillXP, int cooldown, int rpCost, float percentage, List<Skills> skills) {
        this.percentage = percentage;
        EnumSet<Skills> reducingSkills = skills.isEmpty() ? EnumSet.noneOf(Skills.class) : EnumSet.copyOf(skills);
        this.skills = Collections.unmodifiableSet(reducingSkills);
        EnumMap<Skills, Float> xp = new EnumMap<>(Skills.class);
        xp.putAll(skillXP);
        this.skillXP = Collections.unmodifiableMap(xp);
        this.cooldown = cooldown;
        this.rpCost = rpCost;
    }

    /**
     * Used in serialization
     */
    public static class Builder {

        private final Map<Skills, Float> xp = new EnumMap<>(Skills.class);

        private final int cooldown, rpCost;
        private final List<Skills> skills = new ArrayList<>();
        private float percentage;

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

        public SpellProperties build() {
            return new SpellProperties(this.xp, this.cooldown, this.rpCost, this.percentage, this.skills);
        }
    }
}