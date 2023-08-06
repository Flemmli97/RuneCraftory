package io.github.flemmli97.runecraftory.api.datapack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.utils.CodecHelper;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class SpellProperties {

    public final Map<EnumSkills, Float> skillXP;
    public final int cooldown, rpCost;

    public static final Codec<SpellProperties> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.INT.fieldOf("cooldown").forGetter(d -> d.cooldown),
                    Codec.INT.fieldOf("rpCost").forGetter(d -> d.rpCost),
                    Codec.unboundedMap(CodecHelper.enumCodec(EnumSkills.class, null), Codec.FLOAT).fieldOf("skillXP").forGetter(d -> d.skillXP)
            ).apply(instance, (cooldown, rpCost, skillXp) -> new SpellProperties(skillXp, cooldown, rpCost)));

    public static final SpellProperties DEFAULT_PROP = new SpellProperties(new EnumMap<>(EnumSkills.class), 20, 0);

    public SpellProperties(Map<EnumSkills, Float> skillXP, int cooldown, int rpCost) {
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

        public Builder(int cooldown, int rpCost) {
            this.cooldown = cooldown;
            this.rpCost = rpCost;
        }

        public SpellProperties.Builder withXPGain(EnumSkills skill, float gain) {
            this.xp.put(skill, gain);
            return this;
        }

        public SpellProperties build() {
            return new SpellProperties(this.xp, this.cooldown, this.rpCost);
        }
    }
}