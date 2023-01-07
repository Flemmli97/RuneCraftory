package io.github.flemmli97.runecraftory.api.datapack;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemMedicine;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.tenshilib.common.utils.ArrayUtils;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FoodProperties {

    public static final Codec<FoodProperties> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.unboundedMap(Registry.ATTRIBUTE.byNameCodec(), Codec.DOUBLE).fieldOf("cookingBonus").forGetter(d -> d.cookingBonus),
                    SimpleEffect.CODEC.listOf().fieldOf("potionApply").forGetter(d -> Arrays.asList(d.potionApply)),
                    Registry.MOB_EFFECT.byNameCodec().listOf().fieldOf("potionRemove").forGetter(d -> Arrays.asList(d.potionRemove)),
                    Codec.INT.fieldOf("duration").forGetter(d -> d.duration),
                    Codec.unboundedMap(Registry.ATTRIBUTE.byNameCodec(), Codec.DOUBLE).fieldOf("effects").forGetter(d -> d.effects),
                    Codec.unboundedMap(Registry.ATTRIBUTE.byNameCodec(), Codec.DOUBLE).fieldOf("effectsPercentage").forGetter(d -> d.effectsPercentage)
            ).apply(instance, (cooking, potion, remove, duration, effects, effPercent) -> new FoodProperties(duration, effects, effPercent, cooking, potion, remove)));

    private final Map<Attribute, Double> effects = new TreeMap<>(ModAttributes.SORTED);
    private final Map<Attribute, Double> effectsPercentage = new TreeMap<>(ModAttributes.SORTED);
    private final Map<Attribute, Double> cookingBonus = new TreeMap<>(ModAttributes.SORTED);
    private int duration;
    private SimpleEffect[] potionApply = new SimpleEffect[0];
    private MobEffect[] potionRemove = new MobEffect[0];

    private transient List<Component> translationTexts;
    private transient ResourceLocation id;

    private FoodProperties() {
    }

    public FoodProperties(int duration, Map<Attribute, Double> effects, Map<Attribute, Double> effectsPercentage, Map<Attribute, Double> cookingBonus, List<SimpleEffect> potionApply, List<MobEffect> potionRemove) {
        this.duration = duration;
        this.effects.putAll(effects);
        this.effectsPercentage.putAll(effectsPercentage);
        this.cookingBonus.putAll(cookingBonus);
        this.potionApply = potionApply.toArray(new SimpleEffect[0]);
        this.potionRemove = potionRemove.toArray(new MobEffect[0]);
    }

    public static FoodProperties fromPacket(FriendlyByteBuf buffer) {
        FoodProperties prop = new FoodProperties();
        prop.id = buffer.readResourceLocation();
        prop.duration = buffer.readInt();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++)
            prop.effects.put(PlatformUtils.INSTANCE.attributes().getFromId(buffer.readResourceLocation()), buffer.readDouble());
        size = buffer.readInt();
        for (int i = 0; i < size; i++)
            prop.effectsPercentage.put(PlatformUtils.INSTANCE.attributes().getFromId(buffer.readResourceLocation()), buffer.readDouble());
        size = buffer.readInt();
        for (int i = 0; i < size; i++)
            prop.cookingBonus.put(PlatformUtils.INSTANCE.attributes().getFromId(buffer.readResourceLocation()), buffer.readDouble());
        size = buffer.readInt();
        prop.potionRemove = new MobEffect[size];
        for (int i = 0; i < size; i++)
            prop.potionRemove[i] = PlatformUtils.INSTANCE.effects().getFromId(buffer.readResourceLocation());
        size = buffer.readInt();
        prop.potionApply = new SimpleEffect[size];
        for (int i = 0; i < size; i++)
            prop.potionApply[i] = new SimpleEffect(PlatformUtils.INSTANCE.effects().getFromId(buffer.readResourceLocation()), buffer.readInt(), buffer.readInt());
        return prop;
    }

    public void setID(ResourceLocation id) {
        if (this.id == null)
            this.id = id;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public int getHPGain() {
        return this.effects.getOrDefault(ModAttributes.HEALTHGAIN.get(), 0d).intValue();
    }

    public int getRPRegen() {
        return this.effects.getOrDefault(ModAttributes.RPGAIN.get(), 0d).intValue();
    }

    public int getHpPercentGain() {
        return this.effects.getOrDefault(ModAttributes.HEALTHGAIN.get(), 0d).intValue();
    }

    public int getRpPercentRegen() {
        return this.effectsPercentage.getOrDefault(ModAttributes.RPGAIN.get(), 0d).intValue();
    }

    public int getRpIncrease() {
        return this.effects.getOrDefault(ModAttributes.RPINCREASE.get(), 0d).intValue();
    }

    public int getRpPercentIncrease() {
        return this.effectsPercentage.getOrDefault(ModAttributes.RPINCREASE.get(), 0d).intValue();
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

    public Map<Attribute, Double> cookingBonus() {
        return new LinkedHashMap<>(this.cookingBonus);
    }

    public List<MobEffect> potionHeals() {
        return ImmutableList.copyOf(this.potionRemove);
    }

    public List<SimpleEffect> potionApply() {
        return ImmutableList.copyOf(this.potionApply);
    }

    public void toPacket(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(this.id);
        buffer.writeInt(this.duration);
        buffer.writeInt(this.effects.size());
        this.effects.forEach((att, val) -> {
            buffer.writeResourceLocation(PlatformUtils.INSTANCE.attributes().getIDFrom(att));
            buffer.writeDouble(val);
        });
        buffer.writeInt(this.effectsPercentage.size());
        this.effectsPercentage.forEach((att, val) -> {
            buffer.writeResourceLocation(PlatformUtils.INSTANCE.attributes().getIDFrom(att));
            buffer.writeDouble(val);
        });
        buffer.writeInt(this.cookingBonus.size());
        this.cookingBonus.forEach((att, val) -> {
            buffer.writeResourceLocation(PlatformUtils.INSTANCE.attributes().getIDFrom(att));
            buffer.writeDouble(val);
        });
        buffer.writeInt(this.potionRemove.length);
        for (MobEffect eff : this.potionRemove) {
            buffer.writeResourceLocation(PlatformUtils.INSTANCE.effects().getIDFrom(eff));
        }
        buffer.writeInt(this.potionApply.length);
        for (SimpleEffect eff : this.potionApply) {
            buffer.writeResourceLocation(PlatformUtils.INSTANCE.effects().getIDFrom(eff.getPotion()));
            buffer.writeInt(eff.getDuration());
            buffer.writeInt(eff.getAmplifier());
        }
    }

    public List<Component> texts() {
        if (this.translationTexts == null) {
            this.translationTexts = new ArrayList<>();
            this.translationTexts.add(new TranslatableComponent("tooltip.item.eaten").withStyle(ChatFormatting.GRAY));
            TextComponent hprp = new TextComponent("");
            Map<Attribute, Double> effects = this.effects();
            Map<Attribute, Double> effectsPercent = this.effectsMultiplier();
            TextComponent hpIncrease = new TextComponent("");
            TextComponent rpIncrease = new TextComponent("");
            List<Component> attributes = new ArrayList<>();
            for (Map.Entry<Attribute, Double> entry : effects.entrySet()) {
                if(entry.getValue() == 0)
                    continue;
                MutableComponent comp = new TextComponent(" ").append(new TranslatableComponent(entry.getKey().getDescriptionId())).append(new TextComponent(": " + this.format(entry.getValue())));
                if(entry.getKey() == ModAttributes.HEALTHGAIN.get() || entry.getKey() == ModAttributes.RPGAIN.get())
                    hprp.append(comp);
                else if(entry.getKey() == ModAttributes.RPINCREASE.get())
                    rpIncrease.append(comp);
                else if(entry.getKey() == Attributes.MAX_HEALTH)
                    hpIncrease.append(comp);
                else
                    attributes.add(comp.withStyle(ChatFormatting.AQUA));
            }
            for (Map.Entry<Attribute, Double> entry : effectsPercent.entrySet()) {
                if(entry.getValue() == 0)
                    continue;
                MutableComponent comp = new TextComponent(" ").append(new TranslatableComponent(entry.getKey().getDescriptionId())).append(new TextComponent(": " + this.format(entry.getValue()) + "%"));
                if(entry.getKey() == ModAttributes.HEALTHGAIN.get() || entry.getKey() == ModAttributes.RPGAIN.get())
                    hprp.append(comp);
                else if(entry.getKey() == ModAttributes.RPINCREASE.get())
                    rpIncrease.append(comp);
                else if(entry.getKey() == Attributes.MAX_HEALTH)
                    hpIncrease.append(comp);
                else
                    attributes.add(comp.withStyle(ChatFormatting.AQUA));
            }
            if(!hprp.getSiblings().isEmpty())
                this.translationTexts.add(hprp.withStyle(ChatFormatting.AQUA));
            if(!hpIncrease.getSiblings().isEmpty())
                this.translationTexts.add(hpIncrease.withStyle(ChatFormatting.AQUA));
            if(!rpIncrease.getSiblings().isEmpty())
                this.translationTexts.add(rpIncrease.withStyle(ChatFormatting.AQUA));
            this.translationTexts.addAll(attributes);
        }
        return this.translationTexts;
    }

    public List<Component> medicineText(ItemMedicine medicine, ItemStack stack) {
        List<Component> translationTexts = this.texts();
        TextComponent hprp = new TextComponent("");
        int hpRegen = medicine.healthRegen(stack, this);
        if (hpRegen != 0)
            hprp.append(" ").append(new TranslatableComponent("tooltip.food.hp", this.format(hpRegen)));
        int hpPercent = medicine.healthRegenPercent(stack, this);
        if (hpPercent != 0)
            hprp.append(" ").append(new TranslatableComponent("tooltip.food.hp.percent", this.format(hpPercent)));
        if (this.getRPRegen() != 0)
            hprp.append(" ").append(new TranslatableComponent("tooltip.food.rp", this.format(this.getRPRegen())));
        if (this.getRpPercentRegen() != 0)
            hprp.append(" ").append(new TranslatableComponent("tooltip.food.rp.percent", this.format(this.getRpPercentRegen())));
        if (!hprp.getSiblings().isEmpty())
            translationTexts.set(1, hprp.withStyle(ChatFormatting.AQUA));
        return translationTexts;
    }

    private String format(double n) {
        return n >= 0 ? "+" + (int) n : "" + (int) n;
    }

    @Override
    public String toString() {
        String s = "[Duration:" + this.duration + "]" + "{effects:[" + this.effects + "], potions:[" + ArrayUtils.arrayToString(this.potionRemove, null) + "]";
        if (this.id != null)
            s = this.id + ":" + s;
        return s;
    }

    /**
     * Used in serialization
     */
    public static class Builder {

        private final Map<Attribute, Double> effects = new HashMap<>();
        private final Map<Attribute, Double> effectsPercentage = new HashMap<>();
        private final Map<Attribute, Double> cookingBonus = new HashMap<>();
        private final List<SimpleEffect> potionApply = new ArrayList<>();
        private final List<MobEffect> potionRemove = new ArrayList<>();
        private final int duration;
        //private int hpRegen, rpRegen, hpRegenPercent, rpRegenPercent, rpIncrease, rpPercentIncrease;

        public Builder(int duration) {
            this.duration = duration;
        }

        public Builder setHPRegen(int hpRegen, int hpRegenPercent) {
            if(hpRegen != 0)
                this.effects.put(ModAttributes.HEALTHGAIN.get(), (double) hpRegen);
            if(hpRegenPercent != 0)
                this.effectsPercentage.put(ModAttributes.HEALTHGAIN.get(), (double) hpRegenPercent);
            return this;
        }

        public Builder setRPRegen(int rpRegen, int rpRegenPercent) {
            if(rpRegen != 0)
                this.effects.put(ModAttributes.RPGAIN.get(), (double) rpRegen);
            if(rpRegenPercent != 0)
                this.effectsPercentage.put(ModAttributes.RPGAIN.get(), (double) rpRegenPercent);
            return this;
        }

        public Builder setRPIncrease(int increase, int percentIncrease) {
            if(increase != 0)
                this.effects.put(ModAttributes.RPINCREASE.get(), (double) increase);
            if(percentIncrease != 0)
                this.effectsPercentage.put(ModAttributes.RPINCREASE.get(), (double) percentIncrease);
            return this;
        }

        public Builder addEffect(Attribute att, double value) {
            this.effects.put(att, value);
            return this;
        }

        public Builder addEffectPercentage(Attribute att, double value) {
            this.effectsPercentage.put(att, value);
            return this;
        }

        public Builder addCookingBonus(Attribute att, double value) {
            this.cookingBonus.put(att, value);
            return this;
        }

        public Builder addPotion(MobEffect effect, int duration, int amplifier) {
            this.potionApply.add(new SimpleEffect(effect, duration, amplifier));
            return this;
        }

        public Builder curePotion(MobEffect effect) {
            this.potionRemove.add(effect);
            return this;
        }

        public FoodProperties build() {
            return new FoodProperties(this.duration, this.effects, this.effectsPercentage, this.cookingBonus, this.potionApply, this.potionRemove);
        }
    }
}
