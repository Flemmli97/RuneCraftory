package io.github.flemmli97.runecraftory.api.datapack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.registry.ArmorEffect;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModArmorEffects;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.ItemUtils;
import io.github.flemmli97.runecraftory.common.utils.StreamCodecUtils;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import io.github.flemmli97.tenshilib.common.utils.MapUtils;
import it.unimi.dsi.fastutil.objects.Object2DoubleAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleSortedMaps;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;

public class ItemStat {

    public static boolean SHOW_STATS_CUSTOM = true;

    public static final Codec<ItemStat> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    ModSpells.SPELLS.registry().holderByNameCodec().optionalFieldOf("tier_3_Spell").forGetter(ItemStat::getTier3Spell),
                    ModArmorEffects.ARMOR_EFFECTS.registry().holderByNameCodec().optionalFieldOf("armor_effect").forGetter(ItemStat::getArmorEffect),

                    CodecUtils.stringEnumCodec(EnumElement.class, EnumElement.NONE).orElse(EnumElement.NONE).fieldOf("element").forGetter(ItemStat::element),
                    ModSpells.SPELLS.registry().holderByNameCodec().optionalFieldOf("tier_1_Spell").forGetter(ItemStat::getTier1Spell),
                    ModSpells.SPELLS.registry().holderByNameCodec().optionalFieldOf("tier_2_Spell").forGetter(ItemStat::getTier2Spell),

                    Codec.unboundedMap(BuiltInRegistries.ATTRIBUTE.holderByNameCodec(), Codec.DOUBLE).fieldOf("item_stats").forGetter(ItemStat::itemStats),
                    Codec.unboundedMap(BuiltInRegistries.ATTRIBUTE.holderByNameCodec(), Codec.DOUBLE).fieldOf("monster_bonus").forGetter(ItemStat::getMonsterGiftIncrease),

                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("buy_price").forGetter(ItemStat::getBuy),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("sell_price").forGetter(ItemStat::getSell),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("upgrade_difficulty").forGetter(ItemStat::getDiff)
            ).apply(instance, ((spell3, armorEffect, element, spell, spell2, atts, monster, buy, sell, upgrade) ->
                    new ItemStat(buy, sell, upgrade, element, spell, spell2, spell3, armorEffect, atts, monster))));
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemStat> STREAM_CODEC = new StreamCodec<>() {

        private static final StreamCodec<RegistryFriendlyByteBuf, Optional<Holder<Spell>>> SPELL_CODEC = ByteBufCodecs.optional(
                ByteBufCodecs.holderRegistry(ModSpells.SPELL_REGISTRY_KEY));

        @Override
        public ItemStat decode(RegistryFriendlyByteBuf buf) {
            return new ItemStat(buf.readInt(), buf.readInt(), buf.readInt(), buf.readEnum(EnumElement.class),
                    SPELL_CODEC.decode(buf), SPELL_CODEC.decode(buf), SPELL_CODEC.decode(buf),
                    ByteBufCodecs.optional(ByteBufCodecs.holderRegistry(ModArmorEffects.ARMOR_EFFECT_KEY)).decode(buf),
                    StreamCodecUtils.ATTRIBUTE_CODEC.decode(buf), StreamCodecUtils.ATTRIBUTE_CODEC.decode(buf))
                    .setID(buf.readResourceLocation());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ItemStat prop) {
            buf.writeInt(prop.buyPrice);
            buf.writeInt(prop.sellPrice);
            buf.writeInt(prop.upgradeDifficulty);
            buf.writeEnum(prop.element);

            SPELL_CODEC.encode(buf, prop.getTier2Spell());
            SPELL_CODEC.encode(buf, prop.getTier2Spell());
            SPELL_CODEC.encode(buf, prop.getTier2Spell());
            ByteBufCodecs.optional(ByteBufCodecs.holderRegistry(ModArmorEffects.ARMOR_EFFECT_KEY)).encode(buf, prop.getArmorEffect());

            StreamCodecUtils.ATTRIBUTE_CODEC.encode(buf, prop.itemStats);
            StreamCodecUtils.ATTRIBUTE_CODEC.encode(buf, prop.monsterGiftIncrease);
            buf.writeResourceLocation(prop.id);
        }
    };

    private final int buyPrice;
    private final int sellPrice;
    private final int upgradeDifficulty;
    private final EnumElement element;
    private final Optional<Holder<Spell>> tier1Spell;
    private final Optional<Holder<Spell>> tier2Spell;
    private final Optional<Holder<Spell>> tier3Spell;
    private final Optional<Holder<ArmorEffect>> armorEffect;
    private final Map<Holder<Attribute>, Double> itemStats;
    private final Map<Holder<Attribute>, Double> monsterGiftIncrease;

    private transient ResourceLocation id;

    private ItemStat(int buyPrice, int sellPrice, int upgradeDifficulty, EnumElement element,
                     Optional<Holder<Spell>> tier1Spell, Optional<Holder<Spell>> tier2Spell, Optional<Holder<Spell>> tier3Spell,
                     Optional<Holder<ArmorEffect>> effect,
                     Map<Holder<Attribute>, Double> itemStats, Map<Holder<Attribute>, Double> monsterGiftIncrease) {
        this.itemStats = createFor(itemStats);
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.upgradeDifficulty = upgradeDifficulty;
        this.element = element;
        this.tier1Spell = tier1Spell;
        this.tier2Spell = tier2Spell;
        this.tier3Spell = tier3Spell;
        this.armorEffect = effect;
        this.monsterGiftIncrease = createFor(monsterGiftIncrease);
    }

    private static Map<Holder<Attribute>, Double> createFor(Map<Holder<Attribute>, Double> map) {
        Object2DoubleAVLTreeMap<Holder<Attribute>> sorted = new Object2DoubleAVLTreeMap<>(ModAttributes.SORTED);
        sorted.putAll(map);
        return Object2DoubleSortedMaps.unmodifiable(sorted);
    }

    public ItemStat setID(ResourceLocation id) {
        if (this.id == null)
            this.id = id;
        return this;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public int getBuy() {
        return this.buyPrice;
    }

    public int getSell() {
        return this.sellPrice;
    }

    public int getDiff() {
        return this.upgradeDifficulty;
    }

    public EnumElement element() {
        return this.element;
    }

    public Map<Holder<Attribute>, Double> itemStats() {
        TreeMap<Holder<Attribute>, Double> map = new TreeMap<>(ModAttributes.SORTED);
        map.putAll(this.itemStats);
        return map;
    }

    public Map<Holder<Attribute>, Double> getMonsterGiftIncrease() {
        return this.monsterGiftIncrease;
    }

    public Optional<Holder<Spell>> getTier1Spell() {
        return this.tier1Spell;
    }

    public Optional<Holder<Spell>> getTier2Spell() {
        return this.tier2Spell;
    }

    public Optional<Holder<Spell>> getTier3Spell() {
        return this.tier3Spell;
    }

    public Optional<Holder<ArmorEffect>> getArmorEffect() {
        return this.armorEffect;
    }

    public List<Component> texts(ItemStack stack, boolean showStat) {
        List<Component> list = new ArrayList<>();
        List<Component> header = new ArrayList<>();
        if (ItemNBT.shouldHaveLevel(stack))
            header.add(Component.translatable("runecraftory.tooltip.item.level", ItemNBT.itemLevel(stack)));
        int buyPrice = ItemUtils.getBuyPrice(stack, this);
        if (buyPrice > 0) {
            header.add(Component.translatable("runecraftory.tooltip.item.buy", buyPrice));
        }
        int sellPrice = ItemUtils.getSellPrice(stack, this);
        if (sellPrice > 0) {
            header.add(Component.translatable("runecraftory.tooltip.item.sell", sellPrice));
        }
        if (!header.isEmpty())
            list.add(ComponentUtils.formatList(header, CommonComponents.space(), Function.identity()).withStyle(ChatFormatting.YELLOW));
        boolean shouldHaveStats = ItemNBT.shouldHaveStats(stack);
        if (!shouldHaveStats && this.getDiff() > 0)
            list.add(Component.translatable("runecraftory.tooltip.item.difficulty", this.getDiff()).withStyle(ChatFormatting.YELLOW));
        if (showStat && !shouldHaveStats) {
            Map<Holder<Attribute>, Double> stats = DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem())
                    .map(ItemStat::itemStats).orElse(null);
            addUpgradeStatsText(stats, list);
        }
        return list;
    }

    private static void addUpgradeStatsText(Map<Holder<Attribute>, Double> stats, List<Component> tooltip) {
        if (stats == null || stats.isEmpty())
            return;
        tooltip.add(Component.translatable("runecraftory.tooltip.item.upgrade").withStyle(ChatFormatting.GRAY));
        for (Map.Entry<Holder<Attribute>, Double> entry : stats.entrySet()) {
            if (entry.getKey().is(RunecraftoryTags.Attributes.DISPLAY_IGNORED))
                continue;
            double d = Attributes.KNOCKBACK_RESISTANCE.is(entry.getKey()) ? entry.getValue() * 10 : entry.getValue();
            String num = format(entry.getKey(), d);
            if (num == null)
                continue;
            MutableComponent comp = CommonComponents.space()
                    .append(Component.translatable("runecraftory.tooltip.item.attribute",
                            Component.translatable(entry.getKey().value().getDescriptionId()), num));
            tooltip.add(comp.withStyle(ChatFormatting.BLUE));
        }
    }

    private static String format(Holder<Attribute> att, double n) {
        String sign = n > 0 ? "+" : "";
        if (att.value() == Attributes.MOVEMENT_SPEED) {
            double val = ((int) (n * 100)) / 100d;
            if (val == 0)
                return null;
            return (sign + ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(val));
        }
        boolean percSign = att.is(RunecraftoryTags.Attributes.PERCENTAGE_DISPLAY);
        double val = (int) (n * 2) * 0.5f;
        if (val == 0)
            return null;
        return (sign + ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(val)) + (percSign ? "%" : "");
    }

    public static AttributeModifier adjustModifier(Holder<Attribute> attribute, AttributeModifier original) {
        if (attribute.is(RunecraftoryTags.Attributes.PERCENTAGE_DISPLAY) && original.operation() == AttributeModifier.Operation.ADD_VALUE)
            return new AttributeModifier(original.id(), original.amount() * 0.01, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        return original;
    }

    @Override
    public String toString() {
        String s = "[Buy:" + this.buyPrice + ";Sell:" + this.sellPrice + ";UpgradeDifficulty:" + this.upgradeDifficulty + ";DefaultElement:" + this.element + "];{stats:[" + MapUtils.toString(this.itemStats, Holder::getRegisteredName, Object::toString) + "]}";
        if (this.id != null)
            s = this.id + ":" + s;
        return s;
    }

    public static class Builder {

        private final Map<Holder<Attribute>, Double> itemStats = new HashMap<>();
        private final Map<Holder<Attribute>, Double> monsterGiftIncrease = new HashMap<>();
        public final int buyPrice;
        public final int sellPrice;
        public final int upgradeDifficulty;
        private EnumElement element = EnumElement.NONE;
        private Holder<Spell> tier1Spell;
        private Holder<Spell> tier2Spell;
        private Holder<Spell> tier3Spell;
        private Holder<ArmorEffect> armorEffect;

        public Builder(int buy, int sell, int upgrade) {
            this.buyPrice = buy;
            this.sellPrice = sell;
            this.upgradeDifficulty = upgrade;
        }

        public Builder setElement(EnumElement element) {
            this.element = element;
            return this;
        }

        public Builder addAttribute(Holder<Attribute> att, double value) {
            this.itemStats.put(att, value);
            return this;
        }

        public Builder addMonsterStat(Holder<Attribute> att, double value) {
            this.monsterGiftIncrease.put(att, value);
            return this;
        }

        public Builder setSpell(Holder<Spell> tier1, Holder<Spell> tier2, Holder<Spell> tier3) {
            this.tier1Spell = tier1;
            this.tier2Spell = tier2;
            this.tier3Spell = tier3;
            return this;
        }

        public Builder withArmorEffect(Holder<ArmorEffect> effect) {
            this.armorEffect = effect;
            return this;
        }

        public ItemStat build() {
            return new ItemStat(this.buyPrice, this.sellPrice, this.upgradeDifficulty, this.element,
                    Optional.ofNullable(this.tier1Spell), Optional.ofNullable(this.tier2Spell), Optional.ofNullable(this.tier3Spell),
                    Optional.ofNullable(this.armorEffect), this.itemStats, this.monsterGiftIncrease);
        }
    }
}
