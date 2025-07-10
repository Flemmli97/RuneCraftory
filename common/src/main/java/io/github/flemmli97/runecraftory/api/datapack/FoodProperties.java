package io.github.flemmli97.runecraftory.api.datapack;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.StreamCodecUtils;
import it.unimi.dsi.fastutil.objects.Object2DoubleAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleSortedMaps;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodProperties {

    public static final Codec<FoodProperties> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.unboundedMap(BuiltInRegistries.ATTRIBUTE.holderByNameCodec(), Codec.DOUBLE).fieldOf("cooking_bonus_percent").forGetter(d -> d.cookingBonusPercent),
                    SimpleEffect.CODEC.listOf().fieldOf("potion_apply").forGetter(d -> d.potionApply),
                    BuiltInRegistries.MOB_EFFECT.holderByNameCodec().listOf().fieldOf("potion_remove").forGetter(d -> d.potionRemove),

                    Codec.INT.fieldOf("duration").forGetter(d -> d.duration),
                    Codec.unboundedMap(BuiltInRegistries.ATTRIBUTE.holderByNameCodec(), Codec.DOUBLE).fieldOf("effects").forGetter(d -> d.effects),
                    Codec.unboundedMap(BuiltInRegistries.ATTRIBUTE.holderByNameCodec(), Codec.DOUBLE).fieldOf("effects_percentage").forGetter(d -> d.effectsPercentage),
                    Codec.unboundedMap(BuiltInRegistries.ATTRIBUTE.holderByNameCodec(), Codec.DOUBLE).fieldOf("cooking_bonus").forGetter(d -> d.cookingBonus)
            ).apply(instance, (cookingPercent, potion, remove,
                               duration, effects, effPercent, cooking)
                    -> new FoodProperties(effects, effPercent, cooking, cookingPercent, duration, potion, remove)));
    public static final StreamCodec<RegistryFriendlyByteBuf, FoodProperties> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public FoodProperties decode(RegistryFriendlyByteBuf buf) {
            return new FoodProperties(StreamCodecUtils.ATTRIBUTE_CODEC.decode(buf),
                    StreamCodecUtils.ATTRIBUTE_CODEC.decode(buf),
                    StreamCodecUtils.ATTRIBUTE_CODEC.decode(buf),
                    StreamCodecUtils.ATTRIBUTE_CODEC.decode(buf),
                    buf.readInt(),
                    buf.readList(b -> SimpleEffect.STREAM_CODEC.decode(buf)),
                    buf.readList(b -> ByteBufCodecs.holderRegistry(Registries.MOB_EFFECT).decode(buf)))
                    .setID(buf.readResourceLocation());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, FoodProperties props) {
            StreamCodecUtils.ATTRIBUTE_CODEC.encode(buf, props.effects);
            StreamCodecUtils.ATTRIBUTE_CODEC.encode(buf, props.effectsPercentage);
            StreamCodecUtils.ATTRIBUTE_CODEC.encode(buf, props.cookingBonus);
            StreamCodecUtils.ATTRIBUTE_CODEC.encode(buf, props.cookingBonusPercent);
            buf.writeInt(props.duration);
            buf.writeCollection(props.potionApply, (b, val) -> SimpleEffect.STREAM_CODEC.encode(buf, val));
            buf.writeCollection(props.potionRemove, (b, val) -> ByteBufCodecs.holderRegistry(Registries.MOB_EFFECT).encode(buf, val));
            buf.writeResourceLocation(props.id);
        }
    };

    private final Map<Holder<Attribute>, Double> effects;
    private final Map<Holder<Attribute>, Double> effectsPercentage;
    private final Map<Holder<Attribute>, Double> cookingBonus;
    private final Map<Holder<Attribute>, Double> cookingBonusPercent;
    private final int duration;
    private final List<SimpleEffect> potionApply;
    private final List<Holder<MobEffect>> potionRemove;

    private ResourceLocation id;

    public FoodProperties(Map<Holder<Attribute>, Double> effects, Map<Holder<Attribute>, Double> effectsPercentage,
                          Map<Holder<Attribute>, Double> cookingBonus, Map<Holder<Attribute>, Double> cookingBonusPercent, int duration,
                          List<SimpleEffect> potionApply, List<Holder<MobEffect>> potionRemove) {
        this.effects = createFor(effects);
        this.effectsPercentage = createFor(effectsPercentage);
        this.cookingBonus = createFor(cookingBonus);
        this.cookingBonusPercent = createFor(cookingBonusPercent);
        this.duration = duration;
        this.potionApply = potionApply;
        this.potionRemove = potionRemove;
    }

    private static Map<Holder<Attribute>, Double> createFor(Map<Holder<Attribute>, Double> map) {
        Object2DoubleAVLTreeMap<Holder<Attribute>> sorted = new Object2DoubleAVLTreeMap<>(ModAttributes.SORTED);
        sorted.putAll(map);
        return Object2DoubleSortedMaps.unmodifiable(sorted);
    }

    public FoodProperties setID(ResourceLocation id) {
        if (this.id == null)
            this.id = id;
        return this;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public int getHPGain() {
        return this.effects.getOrDefault(ModAttributes.HEALTH_GAIN.asHolder(), 0d).intValue();
    }

    public int getHpPercentGain() {
        return this.effects.getOrDefault(ModAttributes.HEALTH_GAIN.asHolder(), 0d).intValue();
    }

    public int getRPRegen() {
        return this.effects.getOrDefault(ModAttributes.RUNE_POINTS_GAIN.asHolder(), 0d).intValue();
    }

    public int getRpPercentRegen() {
        return this.effectsPercentage.getOrDefault(ModAttributes.RUNE_POINTS_GAIN.asHolder(), 0d).intValue();
    }

    public int duration() {
        return this.duration;
    }

    public Map<Holder<Attribute>, Double> effects() {
        return this.effects;
    }

    public Map<Holder<Attribute>, Double> effectsMultiplier() {
        return this.effectsPercentage;
    }

    public Map<Holder<Attribute>, Double> cookingBonus() {
        return this.cookingBonus;
    }

    public Map<Holder<Attribute>, Double> cookingBonusPercent() {
        return this.cookingBonusPercent;
    }

    public List<Holder<MobEffect>> potionHeals() {
        return this.potionRemove;
    }

    public List<SimpleEffect> potionApply() {
        return this.potionApply;
    }

    public List<Component> texts(ItemStack stack) {
        List<Component> list = new ArrayList<>();
        list.add(Component.translatable("runecraftory.tooltip.item.eaten").withStyle(ChatFormatting.GRAY));

        Pair<Map<Holder<Attribute>, Double>, Map<Holder<Attribute>, Double>> foodStats = ItemNBT.foodStats(stack);
        Map<Holder<Attribute>, Double> effects = foodStats.getFirst();
        Map<Holder<Attribute>, Double> effectsPercent = foodStats.getSecond();

        List<Component> hpRpGain = new ArrayList<>();
        List<Component> hpRpIncrease = new ArrayList<>();
        List<Component> attributes = new ArrayList<>();
        for (Map.Entry<Holder<Attribute>, Double> entry : effects.entrySet()) {
            if (entry.getValue() == 0)
                continue;
            MutableComponent comp = Component.translatable("runecraftory.tooltip.item.attribute", Component.translatable(entry.getKey().value().getDescriptionId()), this.format(entry.getValue()))
                    .withStyle(ChatFormatting.AQUA);
            if (entry.getKey().is(ModAttributes.HEALTH_GAIN.getKey()) || entry.getKey().is(ModAttributes.RUNE_POINTS_GAIN.getKey()))
                hpRpGain.add(comp);
            else if (entry.getKey().value() == Attributes.MAX_HEALTH.value() || entry.getKey().is(ModAttributes.MAX_RUNEPOINTS.getKey()))
                hpRpIncrease.add(comp);
            else
                attributes.add(CommonComponents.space().append(comp));
        }
        for (Map.Entry<Holder<Attribute>, Double> entry : effectsPercent.entrySet()) {
            if (entry.getValue() == 0)
                continue;
            MutableComponent comp = Component.translatable("runecraftory.tooltip.item.attribute.percentage", Component.translatable(entry.getKey().value().getDescriptionId()), this.format(entry.getValue()))
                    .withStyle(ChatFormatting.AQUA);
            if (entry.getKey().is(ModAttributes.HEALTH_GAIN.getKey()) || entry.getKey().is(ModAttributes.RUNE_POINTS_GAIN.getKey()))
                hpRpGain.add(comp);
            else if (entry.getKey().value() == Attributes.MAX_HEALTH.value() || entry.getKey().is(ModAttributes.MAX_RUNEPOINTS.getKey()))
                hpRpIncrease.add(comp);
            else
                attributes.add(CommonComponents.space().append(comp));
        }
        if (!hpRpGain.isEmpty()) {
            list.add(CommonComponents.space().append(ComponentUtils.formatList(hpRpGain, CommonComponents.space())));
        }
        if (!hpRpIncrease.isEmpty()) {
            list.add(CommonComponents.space().append(ComponentUtils.formatList(hpRpIncrease, CommonComponents.space())));
        }
        list.addAll(attributes);
        return list;
    }

    private String format(double n) {
        return n >= 0 ? "+" + (int) n : "" + (int) n;
    }

    @Override
    public String toString() {
        String s = "[Duration:" + this.duration + "]" + "{effects:[" + this.effects + "], potions:[" + this.potionRemove + "]";
        if (this.id != null)
            s = this.id + ":" + s;
        return s;
    }

    /**
     * Used in serialization
     */
    public static class Builder {

        private final Map<Holder<Attribute>, Double> effects = new HashMap<>();
        private final Map<Holder<Attribute>, Double> effectsPercentage = new HashMap<>();
        private final Map<Holder<Attribute>, Double> cookingBonus = new HashMap<>();
        private final Map<Holder<Attribute>, Double> cookingBonusPercent = new HashMap<>();
        private final List<SimpleEffect> potionApply = new ArrayList<>();
        private final List<Holder<MobEffect>> potionRemove = new ArrayList<>();
        private final int duration;

        public Builder(int duration) {
            this.duration = duration;
        }

        public Builder setHPRegen(int hpRegen, int hpRegenPercent) {
            if (hpRegen != 0)
                this.effects.put(ModAttributes.HEALTH_GAIN.asHolder(), (double) hpRegen);
            if (hpRegenPercent != 0)
                this.effectsPercentage.put(ModAttributes.HEALTH_GAIN.asHolder(), (double) hpRegenPercent);
            return this;
        }

        public Builder setRPRegen(int rpRegen, int rpRegenPercent) {
            if (rpRegen != 0)
                this.effects.put(ModAttributes.RUNE_POINTS_GAIN.asHolder(), (double) rpRegen);
            if (rpRegenPercent != 0)
                this.effectsPercentage.put(ModAttributes.RUNE_POINTS_GAIN.asHolder(), (double) rpRegenPercent);
            return this;
        }

        public Builder setRPIncrease(int increase, int percentIncrease) {
            if (increase != 0)
                this.effects.put(ModAttributes.MAX_RUNEPOINTS.asHolder(), (double) increase);
            if (percentIncrease != 0)
                this.effectsPercentage.put(ModAttributes.MAX_RUNEPOINTS.asHolder(), (double) percentIncrease);
            return this;
        }

        public Builder addEffect(Holder<Attribute> att, double value) {
            this.effects.put(att, value);
            return this;
        }

        public Builder addEffectPercentage(Holder<Attribute> att, double value) {
            this.effectsPercentage.put(att, value);
            return this;
        }

        public Builder addCookingBonus(Holder<Attribute> att, double value) {
            this.cookingBonus.put(att, value);
            return this;
        }

        public Builder addCookingBonusPercent(Holder<Attribute> att, double value) {
            this.cookingBonusPercent.put(att, value);
            return this;
        }

        public Builder addPotion(Holder<MobEffect> effect, int duration, int amplifier) {
            this.potionApply.add(new SimpleEffect(effect, duration, amplifier));
            return this;
        }

        public Builder curePotion(Holder<MobEffect> effect) {
            this.potionRemove.add(effect);
            return this;
        }

        public FoodProperties build() {
            return new FoodProperties(this.effects, this.effectsPercentage, this.cookingBonus, this.cookingBonusPercent, this.duration, this.potionApply, this.potionRemove);
        }
    }
}
