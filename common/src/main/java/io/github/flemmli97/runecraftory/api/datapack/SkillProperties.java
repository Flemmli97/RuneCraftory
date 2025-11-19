package io.github.flemmli97.runecraftory.api.datapack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class SkillProperties {

    public static final SkillProperties DEFAULT = new SkillProperties(1, 0, 0, 0, 0, 0, 0,
            new ExpressionHolder("0"));

    public static final Codec<SkillProperties> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(Codec.INT.fieldOf("max_level").forGetter(d -> d.maxLevel),
                    Codec.FLOAT.fieldOf("health_increase").forGetter(d -> d.healthIncrease),
                    Codec.FLOAT.fieldOf("rp_increase").forGetter(d -> d.rpIncrease),
                    Codec.FLOAT.fieldOf("str_increase").forGetter(d -> d.strIncrease),
                    Codec.FLOAT.fieldOf("vit_increase").forGetter(d -> d.vitIncrease),
                    Codec.FLOAT.fieldOf("intel_increase").forGetter(d -> d.intelIncrease),
                    Codec.FLOAT.fieldOf("xp_multiplier").forGetter(d -> d.xpMultiplier),
                    ExpressionHolder.CODEC.fieldOf("experience_per_level").forGetter(d -> d.expressionHolder)
            ).apply(instance, SkillProperties::new));

    private final int maxLevel;
    private final float healthIncrease, rpIncrease, strIncrease, vitIncrease, intelIncrease, xpMultiplier;
    private final ExpressionHolder expressionHolder;
    private final ExperienceCache cache;

    public SkillProperties(int maxLevel, float healthIncrease, float rpIncrease, float strIncrease, float vitIncrease, float intelIncrease, float xpMultiplier, String expression) {
        this(maxLevel, healthIncrease, rpIncrease, strIncrease, vitIncrease, intelIncrease, xpMultiplier, new ExpressionHolder(expression));
    }

    public SkillProperties(int maxLevel, float healthIncrease, float rpIncrease, float strIncrease, float vitIncrease, float intelIncrease, float xpMultiplier, ExpressionHolder expressionHolder) {
        this.maxLevel = maxLevel;
        this.healthIncrease = healthIncrease;
        this.rpIncrease = rpIncrease;
        this.strIncrease = strIncrease;
        this.vitIncrease = vitIncrease;
        this.intelIncrease = intelIncrease;
        this.xpMultiplier = xpMultiplier;
        this.expressionHolder = expressionHolder;
        this.cache = new ExperienceCache(this::maxLevel, this.expressionHolder);
    }

    public int maxLevel() {
        return this.maxLevel;
    }

    public float healthIncrease() {
        return this.healthIncrease;
    }

    public float rpIncrease() {
        return this.rpIncrease;
    }

    public float strIncrease() {
        return this.strIncrease;
    }

    public float vitIncrease() {
        return this.vitIncrease;
    }

    public float intelIncrease() {
        return this.intelIncrease;
    }

    public float xpMultiplier() {
        return this.xpMultiplier;
    }

    public int xpAmountForNext(int level) {
        return this.cache.xpAmountForNext(level);
    }

    public long totalXpAmount(int level) {
        return this.cache.totalXpAmount(level);
    }

}
