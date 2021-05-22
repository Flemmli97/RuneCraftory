package com.flemmli97.runecraftory.api.datapack;

import com.flemmli97.runecraftory.common.items.consumables.ItemMedicine;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import com.flemmli97.tenshilib.common.utils.ArrayUtils;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FoodProperties {

    private int hpRegen, rpRegen, hpRegenPercent, rpRegenPercent, rpIncrease, rpPercentIncrease, duration;
    private final Map<Attribute, Double> effects = new TreeMap<>(ModAttributes.sorted);
    private final Map<Attribute, Double> effectsPercentage = new TreeMap<>(ModAttributes.sorted);
    private SimpleEffect[] potionApply = new SimpleEffect[0];
    private Effect[] potionRemove = new Effect[0];

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

    public int getRpIncrease() {
        return this.rpIncrease;
    }

    public int getRpPercentIncrease() {
        return this.rpPercentIncrease;
    }

    public int duration() {
        return this.duration;
    }

    public Map<Attribute, Double> effects() {
        return new LinkedHashMap<>(this.effects);
    }

    public Map<Attribute, Double> effectsMultiplier() {
        return new LinkedHashMap<>(this.effectsPercentage);
    }

    public Effect[] potionHeals() {
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
            buffer.writeDouble(val);
        });
        buffer.writeInt(this.effectsPercentage.size());
        this.effectsPercentage.forEach((att, val) -> {
            buffer.writeRegistryId(att);
            buffer.writeDouble(val);
        });
        buffer.writeInt(this.potionRemove.length);
        for (Effect eff : this.potionRemove) {
            buffer.writeRegistryId(eff);
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
            prop.effects.put(buffer.readRegistryIdSafe(Attribute.class), buffer.readDouble());
        size = buffer.readInt();
        for (int i = 0; i < size; i++)
            prop.effectsPercentage.put(buffer.readRegistryIdSafe(Attribute.class), buffer.readDouble());
        size = buffer.readInt();
        prop.potionRemove = new Effect[size];
        for (int i = 0; i < size; i++)
            prop.potionRemove[i] = buffer.readRegistryIdSafe(Effect.class);
        size = buffer.readInt();
        prop.potionApply = new SimpleEffect[size];
        for (int i = 0; i < size; i++)
            prop.potionApply[i] = new SimpleEffect(buffer.readRegistryIdSafe(Effect.class), buffer.readInt(), buffer.readInt());
        return prop;
    }

    public List<ITextComponent> texts() {
        if (this.translationTexts == null) {
            this.translationTexts = new ArrayList<>();
            this.translationTexts.add(new TranslationTextComponent("tooltip.item.eaten"));
            StringTextComponent hprp = new StringTextComponent("");
            if (this.getHPGain() != 0)
                hprp.append(" ").append(new TranslationTextComponent("tooltip.food.hp", this.format(this.getHPGain())));
            if (this.getHpPercentGain() != 0)
                hprp.append(" ").append(new TranslationTextComponent("tooltip.food.hp.percent", this.format(this.getHpPercentGain())));
            if (this.getRPRegen() != 0)
                hprp.append(" ").append(new TranslationTextComponent("tooltip.food.rp", this.format(this.getRPRegen())));
            if (this.getRpPercentRegen() != 0)
                hprp.append(" ").append(new TranslationTextComponent("tooltip.food.rp.percent", this.format(this.getRpPercentRegen())));
            if (!hprp.getSiblings().isEmpty())
                this.translationTexts.add(hprp);
            StringTextComponent rpIncrease = new StringTextComponent("");
            if (this.getRpIncrease() != 0)
                rpIncrease.append(" ").append(new TranslationTextComponent("tooltip.food.rpmax", this.format(this.getRpIncrease())));
            if (this.getRpPercentIncrease() != 0)
                rpIncrease.append(" ").append(new TranslationTextComponent("tooltip.food.rpmax.percent", this.format(this.getRpPercentIncrease())));
            if (!rpIncrease.getSiblings().isEmpty())
                this.translationTexts.add(rpIncrease);
            for (Map.Entry<Attribute, Double> entry : this.effects().entrySet()) {
                IFormattableTextComponent comp = new StringTextComponent(" ").append(new TranslationTextComponent(entry.getKey().getTranslationKey())).append(new StringTextComponent(": " + this.format(entry.getValue())));
                this.translationTexts.add(comp);
            }
            for (Map.Entry<Attribute, Double> entry : this.effectsMultiplier().entrySet()) {
                IFormattableTextComponent comp = new StringTextComponent(" ").append(new TranslationTextComponent(entry.getKey().getTranslationKey())).append(new StringTextComponent(": " + this.format(entry.getValue()) + "%"));
                this.translationTexts.add(comp);
            }
        }
        return this.translationTexts;
    }

    public List<ITextComponent> medicineText(ItemMedicine medicine, ItemStack stack) {
        List<ITextComponent> translationTexts = this.texts();
        StringTextComponent hprp = new StringTextComponent("");
        int hpRegen = medicine.healthRegen(stack, this);
        if (hpRegen != 0)
            hprp.append(" ").append(new TranslationTextComponent("tooltip.food.hp", this.format(hpRegen)));
        int hpPercent = medicine.healthRegenPercent(stack, this);
        if (hpPercent != 0)
            hprp.append(" ").append(new TranslationTextComponent("tooltip.food.hp.percent", this.format(hpPercent)));
        if (this.getRPRegen() != 0)
            hprp.append(" ").append(new TranslationTextComponent("tooltip.food.rp", this.format(this.getRPRegen())));
        if (this.getRpPercentRegen() != 0)
            hprp.append(" ").append(new TranslationTextComponent("tooltip.food.rp.percent", this.format(this.getRpPercentRegen())));
        if (!hprp.getSiblings().isEmpty())
            translationTexts.set(1, hprp);
        return translationTexts;
    }

    private String format(double n) {
        return n >= 0 ? "+" + (int) n : "" + (int) n;
    }

    @Override
    public String toString() {
        return "[HP:" + this.hpRegen + ",RP:" + this.rpRegen + ",HP%:" + this.hpRegenPercent + ",RP%:" + this.rpRegenPercent + ",Duration:" + this.duration + "]" + "{effects:[" + this.effects + "], potions:[" + ArrayUtils.arrayToString(this.potionRemove, null) + "]";
    }

    /**
     * Used in serialization
     */
    public static class MutableFoodProps {

        private int hpRegen, rpRegen, hpRegenPercent, rpRegenPercent, rpIncrease, rpPercentIncrease, duration;
        private final Map<Attribute, Double> effects = new TreeMap<>(ModAttributes.sorted);
        private final Map<Attribute, Double> effectsPercentage = new TreeMap<>(ModAttributes.sorted);
        private final List<SimpleEffect> potionApply = new ArrayList<>();
        private final List<Effect> potionRemove = new ArrayList<>();

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

        public MutableFoodProps setRPIncrease(int increase, int percentIncrease) {
            this.rpIncrease = increase;
            this.rpPercentIncrease = percentIncrease;
            return this;
        }

        public MutableFoodProps addEffect(Attribute att, double value) {
            this.effects.put(att, value);
            return this;
        }

        public MutableFoodProps addEffectPercentage(Attribute att, double value) {
            this.effectsPercentage.put(att, value);
            return this;
        }

        public MutableFoodProps addPotion(Effect effect, int duration, int amplifier) {
            this.potionApply.add(new SimpleEffect(effect, duration, amplifier));
            return this;
        }

        public MutableFoodProps curePotion(Effect effect) {
            this.potionRemove.add(effect);
            return this;
        }
    }
}
