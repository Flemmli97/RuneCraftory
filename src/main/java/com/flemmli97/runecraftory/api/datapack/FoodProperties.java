package com.flemmli97.runecraftory.api.datapack;

import com.flemmli97.runecraftory.common.registry.ModAttributes;
import com.flemmli97.tenshilib.common.utils.ArrayUtils;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.potion.Effect;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FoodProperties {

    private int hpRegen, rpRegen, hpRegenPercent, rpRegenPercent, duration;
    private Map<Attribute, Integer> effects = new TreeMap<>(ModAttributes.sorted);
    private Map<Attribute, Float> effectsPercentage = new TreeMap<>(ModAttributes.sorted);
    private SimpleEffect[] potionApply;
    private SimpleEffect[] potionRemove;

    public int getHPGain() {
        return this.hpRegen;
    }

    public int getRPRegen() {
        return this.rpRegen;
    }

    public int getHpPercentGain() {
        return this.hpRegenPercent;
    }

    public int getRpPercentRegen() {
        return this.rpRegenPercent;
    }

    public int duration() {
        return this.duration;
    }

    public Map<Attribute, Integer> effects() {
        return ImmutableMap.copyOf(this.effects);
    }

    public Map<Attribute, Float> effectsMultiplier() {
        return ImmutableMap.copyOf(this.effectsPercentage);
    }

    public SimpleEffect[] potionHeals() {
        return this.potionRemove;
    }

    public SimpleEffect[] potionApply() {
        return this.potionApply;
    }

    @Override
    public String toString() {
        return "[HP:" + this.hpRegen + ",RP:" + this.rpRegen + ",HP%:" + this.hpRegenPercent + ",RP%:" + this.rpRegenPercent + ",Duration:" + this.duration + "]" + "{effects:[" + this.effects + "], potions:[" + ArrayUtils.arrayToString(this.potionRemove, null) + "]";
    }

    public static class MutableFoodProps {

        private int hpRegen, rpRegen, hpRegenPercent, rpRegenPercent, duration;
        private Map<Attribute, Integer> effects = new TreeMap<>(ModAttributes.sorted);
        private Map<Attribute, Float> effectsPercentage = new TreeMap<>(ModAttributes.sorted);
        private List<SimpleEffect> potionApply;
        private List<SimpleEffect> potionRemove;

        public MutableFoodProps(int duration) {
            this.duration = duration;
        }

        public MutableFoodProps setHPRegen(int hpRegen, int hpRegenPercent) {
            this.hpRegen = hpRegen;
            this.hpRegenPercent = hpRegenPercent;
            return this;
        }

        public MutableFoodProps setRPRegen(int rpRegen, int rpRegenPercent) {
            this.rpRegen = rpRegen;
            this.rpRegenPercent = rpRegenPercent;
            return this;
        }

        public MutableFoodProps addEffect(Attribute att, int value) {
            this.effects.put(att, value);
            return this;
        }

        public MutableFoodProps addEffectPercentage(Attribute att, float value) {
            this.effectsPercentage.put(att, value);
            return this;
        }

        public MutableFoodProps addPotion(Effect effect, int duration, int amplifier) {
            this.potionApply.add(new SimpleEffect(effect, duration, amplifier));
            return this;
        }

        public MutableFoodProps curePotion(Effect effect, int duration, int amplifier) {
            this.potionRemove.add(new SimpleEffect(effect, duration, amplifier));
            return this;
        }
    }
}
