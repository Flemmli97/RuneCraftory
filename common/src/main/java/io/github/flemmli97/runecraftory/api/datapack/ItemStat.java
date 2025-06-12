package io.github.flemmli97.runecraftory.api.datapack;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.registry.ArmorEffect;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.lib.LibAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModArmorEffects;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.ItemUtils;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import io.github.flemmli97.tenshilib.common.utils.MapUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

public class ItemStat {

    public static boolean SHOW_STATS_CUSTOM = true;

    public static final Codec<ItemStat> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    ModSpells.SPELLS.registry().byNameCodec().optionalFieldOf("tier_3_Spell").forGetter(s -> Optional.ofNullable(s.getTier3Spell())),
                    ModArmorEffects.ARMOR_EFFECTS.registry().byNameCodec().optionalFieldOf("armor_effect").forGetter(s -> Optional.ofNullable(s.getArmorEffect())),

                    CodecUtils.stringEnumCodec(EnumElement.class, EnumElement.NONE).orElse(EnumElement.NONE).fieldOf("element").forGetter(ItemStat::element),
                    ModSpells.SPELLS.registry().byNameCodec().optionalFieldOf("tier_1_Spell").forGetter(s -> Optional.ofNullable(s.getTier1Spell())),
                    ModSpells.SPELLS.registry().byNameCodec().optionalFieldOf("tier_2_Spell").forGetter(s -> Optional.ofNullable(s.getTier2Spell())),

                    Codec.unboundedMap(BuiltInRegistries.ATTRIBUTE.holderByNameCodec(), Codec.DOUBLE).fieldOf("item_stats").forGetter(ItemStat::itemStats),
                    Codec.unboundedMap(BuiltInRegistries.ATTRIBUTE.holderByNameCodec(), Codec.DOUBLE).fieldOf("monster_bonus").forGetter(ItemStat::getMonsterGiftIncrease),

                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("buy_price").forGetter(ItemStat::getBuy),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("sell_price").forGetter(ItemStat::getSell),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("upgrade_difficulty").forGetter(ItemStat::getDiff)
            ).apply(instance, ((spell3, armorEffect, element, spell, spell2, atts, monster, buy, sell, upgrade) ->
                    new ItemStat(buy, sell, upgrade, element, spell.orElse(null), spell2.orElse(null), spell3.orElse(null), armorEffect.orElse(null), atts, monster))));

    private static final Set<ResourceLocation> PERCENT_ATTRIBUTES = Sets.newHashSet(
            LibAttributes.PARA,
            LibAttributes.POISON,
            LibAttributes.SEAL,
            LibAttributes.SLEEP,
            LibAttributes.FATIGUE,
            LibAttributes.COLD,
            LibAttributes.CRIT,
            LibAttributes.STUN,
            LibAttributes.FAINT,
            LibAttributes.DRAIN,
            LibAttributes.KNOCK,

            LibAttributes.RES_WATER,
            LibAttributes.RES_EARTH,
            LibAttributes.RES_WIND,
            LibAttributes.RES_FIRE,
            LibAttributes.RES_DARK,
            LibAttributes.RES_LIGHT,
            LibAttributes.RES_LOVE,

            LibAttributes.RES_PARA,
            LibAttributes.RES_POISON,
            LibAttributes.RES_SEAL,
            LibAttributes.RES_SLEEP,
            LibAttributes.RES_FATIGUE,
            LibAttributes.RES_COLD,
            LibAttributes.RES_CRIT,
            LibAttributes.RES_STUN,
            LibAttributes.RES_FAINT,
            LibAttributes.RES_DRAIN,
            LibAttributes.RES_KNOCK);

    private static final Set<ResourceLocation> IGNORED = Sets.newHashSet(
            LibAttributes.ATTACK_SPEED,
            LibAttributes.ATTACK_RANGE,
            LibAttributes.HEALTH_GAIN,
            LibAttributes.RP_GAIN
    );

    private final Map<Holder<Attribute>, Double> itemStats;
    private Map<Holder<Attribute>, Double> monsterGiftIncrease = Map.of();

    private int buyPrice;
    private int sellPrice;
    private int upgradeDifficulty;
    private EnumElement element = EnumElement.NONE;
    private Spell tier1Spell;
    private Spell tier2Spell;
    private Spell tier3Spell;
    private ArmorEffect armorEffect;

    private transient ResourceLocation id;

    private ItemStat() {
        this.itemStats = new HashMap<>();
    }

    private ItemStat(int buyPrice, int sellPrice, int upgradeDifficulty, EnumElement element, Spell tier1Spell, Spell tier2Spell, Spell tier3Spell, ArmorEffect effect, Map<Holder<Attribute>, Double> itemStats, Map<Holder<Attribute>, Double> monsterGiftIncrease) {
        this.itemStats = itemStats;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.upgradeDifficulty = upgradeDifficulty;
        this.element = element;
        this.tier1Spell = tier1Spell;
        this.tier2Spell = tier2Spell;
        this.tier3Spell = tier3Spell;
        this.armorEffect = effect;
        this.monsterGiftIncrease = ImmutableSortedMap.copyOf(monsterGiftIncrease, ModAttributes.SORTED);
    }

    public static ItemStat fromPacket(FriendlyByteBuf buffer) {
        ItemStat stat = new ItemStat();
        stat.id = buffer.readResourceLocation();
        stat.buyPrice = buffer.readInt();
        stat.sellPrice = buffer.readInt();
        stat.upgradeDifficulty = buffer.readInt();
        stat.element = buffer.readEnum(EnumElement.class);
        int size = buffer.readInt();
        for (int i = 0; i < size; i++)
            stat.itemStats.put(BuiltInRegistries.ATTRIBUTE.get(buffer.readResourceLocation()), buffer.readDouble());
        size = buffer.readInt();
        ImmutableSortedMap.Builder<Holder<Attribute>, Double> builder = new ImmutableSortedMap.Builder<>(ModAttributes.SORTED);
        for (int i = 0; i < size; i++)
            builder.put(BuiltInRegistries.ATTRIBUTE.get(buffer.readResourceLocation()), buffer.readDouble());
        stat.monsterGiftIncrease = builder.build();
        if (buffer.readBoolean())
            stat.tier1Spell = ModSpells.SPELL_REGISTRY.get().getFromId(buffer.readResourceLocation());
        if (buffer.readBoolean())
            stat.tier2Spell = ModSpells.SPELL_REGISTRY.get().getFromId(buffer.readResourceLocation());
        if (buffer.readBoolean())
            stat.tier3Spell = ModSpells.SPELL_REGISTRY.get().getFromId(buffer.readResourceLocation());
        if (buffer.readBoolean())
            stat.armorEffect = ModArmorEffects.ARMOR_EFFECT_REGISTRY.get().getFromId(buffer.readResourceLocation());
        return stat;
    }

    public void setID(ResourceLocation id) {
        if (this.id == null)
            this.id = id;
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

    @Nullable
    public Spell getTier1Spell() {
        return this.tier1Spell;
    }

    @Nullable
    public Spell getTier2Spell() {
        return this.tier2Spell;
    }

    @Nullable
    public Spell getTier3Spell() {
        return this.tier3Spell;
    }

    @Nullable
    public ArmorEffect getArmorEffect() {
        return this.armorEffect;
    }

    /**
     * Writes this ItemStat to a buffer for synchronization. Spell upgrades are not synched
     */
    public void toPacket(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(this.id);
        buffer.writeInt(this.buyPrice);
        buffer.writeInt(this.sellPrice);
        buffer.writeInt(this.upgradeDifficulty);
        buffer.writeEnum(this.element);
        buffer.writeInt(this.itemStats.size());
        this.itemStats.forEach((att, val) -> {
            buffer.writeResourceLocation(BuiltInRegistries.ATTRIBUTE.getKey(att));
            buffer.writeDouble(val);
        });
        buffer.writeInt(this.monsterGiftIncrease.size());
        this.monsterGiftIncrease.forEach((att, val) -> {
            buffer.writeResourceLocation(BuiltInRegistries.ATTRIBUTE.getKey(att));
            buffer.writeDouble(val);
        });
        buffer.writeBoolean(this.tier1Spell != null);
        if (this.tier1Spell != null)
            buffer.writeResourceLocation(this.tier1Spell.getRegistryName());
        buffer.writeBoolean(this.tier2Spell != null);
        if (this.tier2Spell != null)
            buffer.writeResourceLocation(this.tier2Spell.getRegistryName());
        buffer.writeBoolean(this.tier3Spell != null);
        if (this.tier3Spell != null)
            buffer.writeResourceLocation(this.tier3Spell.getRegistryName());
        buffer.writeBoolean(this.armorEffect != null);
        if (this.armorEffect != null)
            buffer.writeResourceLocation(this.armorEffect.getRegistryName());
    }

    public List<Component> texts(ItemStack stack, boolean showStat) {
        List<Component> list = new ArrayList<>();
        MutableComponent price = ItemNBT.shouldHaveLevel(stack) ? Component.translatable("runecraftory.tooltip.item.level", ItemNBT.itemLevel(stack)) : null;
        if (ItemUtils.getBuyPrice(stack, this) > 0) {
            if (price == null)
                price = Component.translatable("runecraftory.tooltip.item.buy", ItemUtils.getBuyPrice(stack, this));
            else
                price.append(" ").append(Component.translatable("runecraftory.tooltip.item.buy", ItemUtils.getBuyPrice(stack, this))).append(" ");
        }
        if (ItemUtils.getSellPrice(stack, this) > 0) {
            if (price == null)
                price = Component.translatable("runecraftory.tooltip.item.sell", ItemUtils.getSellPrice(stack, this));
            else
                price.append(" ").append(Component.translatable("runecraftory.tooltip.item.sell", ItemUtils.getSellPrice(stack, this)));
        }
        if (price != null)
            list.add(price.withStyle(ChatFormatting.YELLOW));
        boolean shouldHaveStats = ItemNBT.shouldHaveStats(stack);
        if (!shouldHaveStats && this.getDiff() > 0)
            list.add(Component.translatable("runecraftory.tooltip.item.difficulty", this.getDiff()).withStyle(ChatFormatting.YELLOW));
        if (showStat) {
            AttributeMapDisplay stats = getStatsAttributeMap(stack);
            List<Component> statsTooltip = stats.components();
            if (!statsTooltip.isEmpty()) {
                String prefix = shouldHaveStats ? "runecraftory.tooltip.item.equipped" : "runecraftory.tooltip.item.upgrade";
                list.add(Component.translatable(prefix).withStyle(ChatFormatting.GRAY));
                list.addAll(statsTooltip);
            }
        }
        return list;
    }

    /**
     * Attributes and values to display
     */
    private static AttributeMapDisplay getStatsAttributeMap(ItemStack stack) {
        if (!ItemNBT.shouldHaveStats(stack))
            return DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem())
                    .map(s -> new AttributeMapDisplay(s.itemStats, null)).orElse(new AttributeMapDisplay(null, null));
        if (!ItemStat.SHOW_STATS_CUSTOM)
            return new AttributeMapDisplay(null, null);
        Map<Holder<Attribute>, AttributeValues> map = new TreeMap<>(ModAttributes.SORTED);
        Multimap<Holder<Attribute>, AttributeModifier> multimap = stack.getAttributeModifiers(ItemUtils.slotOf(stack));
        multimap.forEach((att, mod) -> map.compute(att, (key, old) -> old == null ? AttributeValues.of(mod) : old.add(mod)));
        return new AttributeMapDisplay(null, map);
    }

    record AttributeMapDisplay(Map<Holder<Attribute>, Double> flat, Map<Holder<Attribute>, AttributeValues> ext) {

        private List<Component> components() {
            List<Component> list = new ArrayList<>();
            if (this.flat != null) {
                for (Map.Entry<Holder<Attribute>, Double> entry : this.flat.entrySet()) {
                    ResourceLocation key = BuiltInRegistries.ATTRIBUTE.getKey(entry.getKey());
                    if (IGNORED.contains(key))
                        continue;
                    double d = entry.getKey().equals(Attributes.KNOCKBACK_RESISTANCE) ? entry.getValue() * 10 : entry.getValue();
                    String num = format(key, d, false);
                    if (num == null)
                        continue;
                    MutableComponent comp = Component.literal(" ").append(Component.translatable(entry.getKey().getDescriptionId())).append(Component.literal(": " + num));
                    list.add(comp.withStyle(ChatFormatting.BLUE));
                }
            } else if (this.ext != null) {
                for (Map.Entry<Holder<Attribute>, AttributeValues> entry : this.ext.entrySet()) {
                    ResourceLocation key = BuiltInRegistries.ATTRIBUTE.getKey(entry.getKey());
                    if (IGNORED.contains(key))
                        continue;
                    if (entry.getValue().flat != 0) {
                        double d = entry.getKey().equals(Attributes.KNOCKBACK_RESISTANCE) ? entry.getValue().flat * 10 : entry.getValue().flat;
                        String num = format(key, d, false);
                        if (num == null)
                            continue;
                        MutableComponent comp = Component.literal(" ").append(Component.translatable(entry.getKey().value().getDescriptionId())).append(Component.literal(": " + num));
                        list.add(comp.withStyle(ChatFormatting.BLUE));
                    }
                    if (entry.getValue().multBase != 0) {
                        String num = format(key, entry.getValue().multBase, true);
                        if (num == null)
                            continue;
                        MutableComponent comp = Component.literal(" ").append(Component.translatable(entry.getKey().value().getDescriptionId())).append(Component.literal(": " + num));
                        list.add(comp.withStyle(ChatFormatting.BLUE));
                    }
                    if (entry.getValue().multTotal != 0) {
                        String num = format(key, entry.getValue().multTotal, true);
                        if (num == null)
                            continue;
                        MutableComponent comp = Component.literal(" ").append(Component.translatable(entry.getKey().value().getDescriptionId())).append(Component.literal(": " + num));
                        list.add(comp.withStyle(ChatFormatting.BLUE));
                    }
                }
            }
            return list;
        }

        private static String format(ResourceLocation att, double n, boolean percentage) {
            String sign = n > 0 ? (percentage ? "x" : "+") : "";
            if (att.equals(LibAttributes.MOVEMENT_SPEED)) {
                double val = percentage ? n : ((int) (n * 100)) / 100d;
                if (val == 0)
                    return null;
                return (sign + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(val));
            }
            boolean percSign = PERCENT_ATTRIBUTES.contains(att);
            double val = percentage ? n : (int) (n * 2) * 0.5f;
            if (val == 0)
                return null;
            return (sign + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(val)) + (percSign ? "%" : "");
        }
    }

    private static class AttributeValues {

        private double flat, multBase, multTotal;

        private AttributeValues(double flat, double multBase, double multTotal) {
            this.flat = flat;
            this.multBase = multBase;
            this.multTotal = multTotal;
        }

        private static AttributeValues of(AttributeModifier mod) {
            return switch (mod.operation()) {
                case ADDITION -> new AttributeValues(mod.getAmount(), 0, 0);
                case MULTIPLY_BASE -> new AttributeValues(0, mod.getAmount(), 0);
                case MULTIPLY_TOTAL -> new AttributeValues(0, 0, mod.getAmount());
            };
        }

        private AttributeValues add(AttributeModifier mod) {
            switch (mod.operation()) {
                case ADDITION -> this.flat += mod.getAmount();
                case MULTIPLY_BASE -> this.multBase += mod.getAmount();
                case MULTIPLY_TOTAL -> this.multTotal += mod.getAmount();
            }
            return this;
        }
    }

    @Override
    public String toString() {
        String s = "[Buy:" + this.buyPrice + ";Sell:" + this.sellPrice + ";UpgradeDifficulty:" + this.upgradeDifficulty + ";DefaultElement:" + this.element + "];{stats:[" + MapUtils.toString(this.itemStats, reg -> BuiltInRegistries.ATTRIBUTE.getKey(reg).toString(), Object::toString) + "]}";
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
        private Spell tier1Spell;
        private Spell tier2Spell;
        private Spell tier3Spell;
        private ArmorEffect armorEffect;

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

        public Builder setSpell(Spell tier1, Spell tier2, Spell tier3) {
            this.tier1Spell = tier1;
            this.tier2Spell = tier2;
            this.tier3Spell = tier3;
            return this;
        }

        public Builder withArmorEffect(ArmorEffect effect) {
            this.armorEffect = effect;
            return this;
        }

        public ItemStat build() {
            return new ItemStat(this.buyPrice, this.sellPrice, this.upgradeDifficulty, this.element, this.tier1Spell, this.tier2Spell, this.tier3Spell, this.armorEffect, this.itemStats, this.monsterGiftIncrease);
        }
    }
}
