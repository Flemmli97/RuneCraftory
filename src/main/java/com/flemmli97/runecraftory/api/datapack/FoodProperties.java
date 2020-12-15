package com.flemmli97.runecraftory.api.datapack;

import com.flemmli97.runecraftory.common.registry.ModAttributes;
import com.flemmli97.tenshilib.common.utils.ArrayUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FoodProperties {

    private int hpRegen, rpRegen, hpRegenPercent, rpRegenPercent, rpIncrease, rpPercentIncrease, duration;
    private Map<Attribute, Integer> effects = new TreeMap<>(ModAttributes.sorted);
    private Map<Attribute, Float> effectsPercentage = new TreeMap<>(ModAttributes.sorted);
    private SimpleEffect[] potionApply = new SimpleEffect[0];
    private SimpleEffect[] potionRemove = new SimpleEffect[0];

    private transient List<ITextComponent> translationTexts;

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

    public int getRpIncrease(){
        return this.rpIncrease;
    }

    public int getRpPercentIncrease(){
        return this.rpPercentIncrease;
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

    public void toPacket(PacketBuffer buffer) {
        buffer.writeInt(this.hpRegen);
        buffer.writeInt(this.rpRegen);
        buffer.writeInt(this.hpRegenPercent);
        buffer.writeInt(this.rpRegenPercent);
        buffer.writeInt(this.duration);
        buffer.writeInt(this.effects.size());
        this.effects.forEach((att, val) -> {
            buffer.writeRegistryId(att);
            buffer.writeInt(val);
        });
        buffer.writeInt(this.effectsPercentage.size());
        this.effectsPercentage.forEach((att, val) -> {
            buffer.writeRegistryId(att);
            buffer.writeFloat(val);
        });
        buffer.writeInt(this.potionRemove.length);
        for (SimpleEffect eff : this.potionRemove) {
            buffer.writeRegistryId(eff.getPotion());
            buffer.writeInt(eff.getDuration());
            buffer.writeInt(eff.getAmplifier());
        }
        buffer.writeInt(this.potionApply.length);
        for (SimpleEffect eff : this.potionApply) {
            buffer.writeRegistryId(eff.getPotion());
            buffer.writeInt(eff.getDuration());
            buffer.writeInt(eff.getAmplifier());
        }
    }

    public static FoodProperties fromPacket(PacketBuffer buffer) {
        FoodProperties prop = new FoodProperties();
        prop.hpRegen = buffer.readInt();
        prop.rpRegen = buffer.readInt();
        prop.hpRegenPercent = buffer.readInt();
        prop.rpRegenPercent = buffer.readInt();
        prop.duration = buffer.readInt();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++)
            prop.effects.put(buffer.readRegistryIdSafe(Attribute.class), buffer.readInt());
        size = buffer.readInt();
        for (int i = 0; i < size; i++)
            prop.effectsPercentage.put(buffer.readRegistryIdSafe(Attribute.class), buffer.readFloat());
        size = buffer.readInt();
        prop.potionRemove = new SimpleEffect[size];
        for (int i = 0; i < size; i++)
            prop.potionRemove[i] = new SimpleEffect(buffer.readRegistryIdSafe(Effect.class), buffer.readInt(), buffer.readInt());
        size = buffer.readInt();
        prop.potionApply = new SimpleEffect[size];
        for (int i = 0; i < size; i++)
            prop.potionApply[i] = new SimpleEffect(buffer.readRegistryIdSafe(Effect.class), buffer.readInt(), buffer.readInt());
        return prop;
    }

    public List<ITextComponent> texts() {
        if(this.translationTexts == null){
            this.translationTexts = Lists.newArrayList();
            this.translationTexts.add(new TranslationTextComponent("item.eaten"));
            StringTextComponent hprp = new StringTextComponent("");
            if(this.getHPGain()!=0)
                hprp.append(" ").append(new TranslationTextComponent("tooltip.food.hp", this.getHPGain()));
            if(this.getHpPercentGain()!=0)
                hprp.append(" ").append(new TranslationTextComponent("tooltip.food.hp.percent", this.getHpPercentGain()));
            if(this.getRPRegen()!=0)
                hprp.append(" ").append(new TranslationTextComponent("tooltip.food.rp", this.getRPRegen()));
            if(this.getRpPercentRegen()!=0)
                hprp.append(" ").append(new TranslationTextComponent("tooltip.food.rp.percent", this.getRpPercentRegen()));
            if(!hprp.getSiblings().isEmpty())
                this.translationTexts.add(hprp);
            StringTextComponent rpIncrease = new StringTextComponent("");
            if(this.getRpIncrease()!=0)
                rpIncrease.append(" ").append(new TranslationTextComponent("tooltip.food.rpmax", this.getRpIncrease()));
            if(this.getRpPercentIncrease()!=0)
                rpIncrease.append(" ").append(new TranslationTextComponent("tooltip.food.rpmax.percent", this.getRpPercentIncrease()));
            if(!rpIncrease.getSiblings().isEmpty())
                this.translationTexts.add(rpIncrease);
            for (Map.Entry<Attribute, Integer> entry : this.effects().entrySet()) {
                IFormattableTextComponent comp = new StringTextComponent(" ").append(new TranslationTextComponent(entry.getKey().getTranslationKey())).append(new StringTextComponent(": " + entry.getValue()));
                this.translationTexts.add(comp);
            }
            for (Map.Entry<Attribute, Float> entry : this.effectsMultiplier().entrySet()) {
                IFormattableTextComponent comp = new StringTextComponent(" ").append(new TranslationTextComponent(entry.getKey().getTranslationKey())).append(new StringTextComponent(": " + (int) (100 * entry.getValue()) + "%"));
                this.translationTexts.add(comp);
            }
        }
        return this.translationTexts;
    }

    @Override
    public String toString() {
        return "[HP:" + this.hpRegen + ",RP:" + this.rpRegen + ",HP%:" + this.hpRegenPercent + ",RP%:" + this.rpRegenPercent + ",Duration:" + this.duration + "]" + "{effects:[" + this.effects + "], potions:[" + ArrayUtils.arrayToString(this.potionRemove, null) + "]";
    }

    public static class MutableFoodProps {

        private int hpRegen, rpRegen, hpRegenPercent, rpRegenPercent, rpIncrease, rpPercentIncrease, duration;
        private Map<Attribute, Integer> effects = new TreeMap<>(ModAttributes.sorted);
        private Map<Attribute, Float> effectsPercentage = new TreeMap<>(ModAttributes.sorted);
        private List<SimpleEffect> potionApply = Lists.newArrayList();
        private List<SimpleEffect> potionRemove = Lists.newArrayList();

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

        public MutableFoodProps setRPIncrease(int increase, int percentIncrease){
            this.rpIncrease = increase;
            this.rpPercentIncrease = percentIncrease;
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
