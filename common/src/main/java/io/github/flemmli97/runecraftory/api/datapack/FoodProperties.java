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
            instance.group(Codec.INT.fieldOf("hpRegen").forGetter(d -> d.hpRegen),
                    Codec.INT.fieldOf("rpRegen").forGetter(d -> d.rpRegen),
                    Codec.INT.fieldOf("hpRegenPercent").forGetter(d -> d.hpRegenPercent),

                    Codec.INT.fieldOf("rpRegenPercent").forGetter(d -> d.rpRegenPercent),
                    Codec.INT.fieldOf("rpIncrease").forGetter(d -> d.rpIncrease),
                    Codec.INT.fieldOf("rpPercentIncrease").forGetter(d -> d.rpPercentIncrease),

                    Codec.INT.fieldOf("duration").forGetter(d -> d.duration),
                    Codec.unboundedMap(Registry.ATTRIBUTE.byNameCodec(), Codec.DOUBLE).fieldOf("effects").forGetter(d -> d.effects),
                    Codec.unboundedMap(Registry.ATTRIBUTE.byNameCodec(), Codec.DOUBLE).fieldOf("effectsPercentage").forGetter(d -> d.effectsPercentage),

                    Codec.unboundedMap(Registry.ATTRIBUTE.byNameCodec(), Codec.DOUBLE).fieldOf("cookingBonus").forGetter(d -> d.cookingBonus),
                    SimpleEffect.CODEC.listOf().fieldOf("potionApply").forGetter(d -> Arrays.asList(d.potionApply)),
                    Registry.MOB_EFFECT.byNameCodec().listOf().fieldOf("potionRemove").forGetter(d -> Arrays.asList(d.potionRemove))
            ).apply(instance, FoodProperties::new));

    private final Map<Attribute, Double> effects = new TreeMap<>(ModAttributes.sorted);
    private final Map<Attribute, Double> effectsPercentage = new TreeMap<>(ModAttributes.sorted);
    private final Map<Attribute, Double> cookingBonus = new TreeMap<>(ModAttributes.sorted);
    private int hpRegen, rpRegen, hpRegenPercent, rpRegenPercent, rpIncrease, rpPercentIncrease, duration;
    private SimpleEffect[] potionApply = new SimpleEffect[0];
    private MobEffect[] potionRemove = new MobEffect[0];

    private transient List<Component> translationTexts;
    private transient ResourceLocation id;

    private FoodProperties() {
    }

    public FoodProperties(int hpRegen, int rpRegen, int hpRegenPercent, int rpRegenPercent, int rpIncrease, int rpPercentIncrease, int duration,
                          Map<Attribute, Double> effects, Map<Attribute, Double> effectsPercentage, Map<Attribute, Double> cookingBonus, List<SimpleEffect> potionApply, List<MobEffect> potionRemove) {
        this.hpRegen = hpRegen;
        this.rpRegen = rpRegen;
        this.hpRegenPercent = hpRegenPercent;
        this.rpRegenPercent = rpRegenPercent;
        this.rpIncrease = rpIncrease;
        this.rpPercentIncrease = rpPercentIncrease;
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
        prop.hpRegen = buffer.readInt();
        prop.rpRegen = buffer.readInt();
        prop.hpRegenPercent = buffer.readInt();
        prop.rpRegenPercent = buffer.readInt();
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
        buffer.writeInt(this.hpRegen);
        buffer.writeInt(this.rpRegen);
        buffer.writeInt(this.hpRegenPercent);
        buffer.writeInt(this.rpRegenPercent);
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
            if (this.getHPGain() != 0)
                hprp.append(" ").append(new TranslatableComponent("tooltip.food.hp", this.format(this.getHPGain())));
            if (this.getHpPercentGain() != 0)
                hprp.append(" ").append(new TranslatableComponent("tooltip.food.hp.percent", this.format(this.getHpPercentGain())));
            if (this.getRPRegen() != 0)
                hprp.append(" ").append(new TranslatableComponent("tooltip.food.rp", this.format(this.getRPRegen())));
            if (this.getRpPercentRegen() != 0)
                hprp.append(" ").append(new TranslatableComponent("tooltip.food.rp.percent", this.format(this.getRpPercentRegen())));
            if (!hprp.getSiblings().isEmpty())
                this.translationTexts.add(hprp.withStyle(ChatFormatting.AQUA));
            TextComponent rpIncrease = new TextComponent("");
            if (this.getRpIncrease() != 0)
                rpIncrease.append(" ").append(new TranslatableComponent("tooltip.food.rpmax", this.format(this.getRpIncrease())));
            if (this.getRpPercentIncrease() != 0)
                rpIncrease.append(" ").append(new TranslatableComponent("tooltip.food.rpmax.percent", this.format(this.getRpPercentIncrease())));
            if (!rpIncrease.getSiblings().isEmpty())
                this.translationTexts.add(rpIncrease.withStyle(ChatFormatting.AQUA));
            for (Map.Entry<Attribute, Double> entry : this.effects().entrySet()) {
                MutableComponent comp = new TextComponent(" ").append(new TranslatableComponent(entry.getKey().getDescriptionId())).append(new TextComponent(": " + this.format(entry.getValue())));
                this.translationTexts.add(comp.withStyle(ChatFormatting.AQUA));
            }
            for (Map.Entry<Attribute, Double> entry : this.effectsMultiplier().entrySet()) {
                MutableComponent comp = new TextComponent(" ").append(new TranslatableComponent(entry.getKey().getDescriptionId())).append(new TextComponent(": " + this.format(entry.getValue()) + "%"));
                this.translationTexts.add(comp.withStyle(ChatFormatting.AQUA));
            }
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
        String s = "[HP:" + this.hpRegen + ",RP:" + this.rpRegen + ",HP%:" + this.hpRegenPercent + ",RP%:" + this.rpRegenPercent + ",Duration:" + this.duration + "]" + "{effects:[" + this.effects + "], potions:[" + ArrayUtils.arrayToString(this.potionRemove, null) + "]";
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
        private int hpRegen, rpRegen, hpRegenPercent, rpRegenPercent, rpIncrease, rpPercentIncrease;

        public Builder(int duration) {
            this.duration = duration;
        }

        public Builder setHPRegen(int hpRegen, int hpRegenPercent) {
            this.hpRegen = hpRegen;
            this.hpRegenPercent = hpRegenPercent;
            return this;
        }

        public Builder setRPRegen(int rpRegen, int rpRegenPercent) {
            this.rpRegen = rpRegen;
            this.rpRegenPercent = rpRegenPercent;
            return this;
        }

        public Builder setRPIncrease(int increase, int percentIncrease) {
            this.rpIncrease = increase;
            this.rpPercentIncrease = percentIncrease;
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
            return new FoodProperties(this.hpRegen, this.rpRegen, this.hpRegenPercent, this.rpRegenPercent, this.rpIncrease, this.rpPercentIncrease, this.duration, this.effects, this.effectsPercentage, this.cookingBonus, this.potionApply, this.potionRemove);
        }
    }
}
