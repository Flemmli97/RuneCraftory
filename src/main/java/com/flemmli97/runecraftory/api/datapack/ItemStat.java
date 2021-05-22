package com.flemmli97.runecraftory.api.datapack;

import com.flemmli97.runecraftory.api.Spell;
import com.flemmli97.runecraftory.api.enums.EnumElement;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.common.lib.LibAttributes;
import com.flemmli97.runecraftory.common.lib.LibNBT;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import com.flemmli97.runecraftory.common.utils.ItemUtils;
import com.flemmli97.tenshilib.common.utils.MapUtils;
import com.google.common.collect.Sets;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ItemStat {

    private static final Set<ResourceLocation> flatAttributes = Sets.newHashSet(
            LibAttributes.GENERIC_ATTACK_DAMAGE,
            LibAttributes.rf_defence,
            LibAttributes.rf_magic,
            LibAttributes.rf_magic_defence);

    private int buyPrice;
    private int sellPrice;
    private int upgradeDifficulty;
    private EnumElement element;
    private final Map<Attribute, Double> itemStats = new TreeMap<>(ModAttributes.sorted);
    private Spell tier1Spell;
    private Spell tier2Spell;
    private Spell tier3Spell;

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

    public Map<Attribute, Double> itemStats() {
        return new LinkedHashMap<>(this.itemStats);
    }

    public Spell getTier1Spell() {
        return this.tier1Spell;
    }

    public Spell getTier2Spell() {
        return this.tier2Spell;
    }

    public Spell getTier3Spell() {
        return this.tier3Spell;
    }

    /**
     * Writes this ItemStat to a buffer for synchronization. Spell upgrades are not synched
     */
    public void toPacket(PacketBuffer buffer) {
        buffer.writeInt(this.buyPrice);
        buffer.writeInt(this.sellPrice);
        buffer.writeInt(this.upgradeDifficulty);
        buffer.writeEnumValue(this.element);
        buffer.writeInt(this.itemStats.size());
        this.itemStats.forEach((att, val) -> {
            buffer.writeRegistryId(att);
            buffer.writeDouble(val);
        });
        buffer.writeBoolean(this.tier1Spell != null);
        if (this.tier1Spell != null)
            buffer.writeRegistryId(this.tier1Spell);
        buffer.writeBoolean(this.tier2Spell != null);
        if (this.tier2Spell != null)
            buffer.writeRegistryId(this.tier2Spell);
        buffer.writeBoolean(this.tier3Spell != null);
        if (this.tier3Spell != null)
            buffer.writeRegistryId(this.tier3Spell);
    }

    public static ItemStat fromPacket(PacketBuffer buffer) {
        ItemStat stat = new ItemStat();
        stat.buyPrice = buffer.readInt();
        stat.sellPrice = buffer.readInt();
        stat.upgradeDifficulty = buffer.readInt();
        stat.element = buffer.readEnumValue(EnumElement.class);
        int size = buffer.readInt();
        for (int i = 0; i < size; i++)
            stat.itemStats.put(buffer.readRegistryIdSafe(Attribute.class), buffer.readDouble());
        if (buffer.readBoolean())
            stat.tier1Spell = buffer.readRegistryIdSafe(Spell.class);
        if (buffer.readBoolean())
            stat.tier2Spell = buffer.readRegistryIdSafe(Spell.class);
        if (buffer.readBoolean())
            stat.tier3Spell = buffer.readRegistryIdSafe(Spell.class);
        return stat;
    }

    public List<ITextComponent> texts(ItemStack stack, boolean showStat) {
        List<ITextComponent> list = new ArrayList<>();
        CompoundNBT tag = ItemNBT.getItemNBT(stack);
        if (stack.getItem() instanceof IItemUsable && tag != null) {
            try {
                EnumElement element = EnumElement.valueOf(tag.getString(LibNBT.Element));
                if (element != EnumElement.NONE) {
                    list.add(new TranslationTextComponent(element.getTranslation()).formatted(element.getColor()));
                }
            } catch (IllegalArgumentException ignored) {
            }
        }
        IFormattableTextComponent price = tag != null ? new TranslationTextComponent("tooltip.item.level", tag.getInt(LibNBT.Level)) : null;
        if (ItemUtils.getBuyPrice(stack, this) > 0) {
            if(price == null)
                price = new TranslationTextComponent("tooltip.item.buy", ItemUtils.getBuyPrice(stack));
            else
                price.append(" ").append(new TranslationTextComponent("tooltip.item.buy", ItemUtils.getBuyPrice(stack))).append(" ");
        }
        if(price == null)
            price = new TranslationTextComponent("tooltip.item.sell", ItemUtils.getSellPrice(stack));
        else
            price.append(" ").append(new TranslationTextComponent("tooltip.item.sell", ItemUtils.getSellPrice(stack))).append(" ");
        list.add(price);
        if (showStat) {
            Map<Attribute, Double> stats = ItemNBT.statBonusRaw(stack);
            if (!stats.isEmpty()) {
                String prefix = ItemNBT.shouldHaveStats(stack) ? "tooltip.item.equipped" : "tooltip.item.upgrade";
                list.add(new TranslationTextComponent(prefix));
            }
            for (Map.Entry<Attribute, Double> entry : stats.entrySet()) {
                IFormattableTextComponent comp = new StringTextComponent(" ").append(new TranslationTextComponent(entry.getKey().getTranslationKey())).append(new StringTextComponent(": " + this.format(entry.getKey(), entry.getValue().intValue())));
                list.add(comp);
            }
        }
        return list;
    }

    private String format(Attribute att, int n) {
        boolean flat = flatAttributes.contains(att.getRegistryName());
        int val = flat && n < 0 ? - n : n;
        return (val >= 0 ? "+" + val : "" + val) + (flat ? "" : "%");
    }

    @Override
    public String toString() {
        return "[Buy:" + this.buyPrice + ";Sell:" + this.sellPrice + ";UpgradeDifficulty:" + this.upgradeDifficulty + ";DefaultElement:" + this.element + "];{stats:[" + MapUtils.toString(this.itemStats, reg -> reg.getRegistryName().toString(), Object::toString) + "]}";
    }

    /**
     * Used in serialization
     */
    public static class MutableItemStat {

        private int buyPrice;
        private int sellPrice;
        private int upgradeDifficulty;
        private EnumElement element = EnumElement.NONE;
        private final Map<Attribute, Double> itemStats = new TreeMap<>(ModAttributes.sorted);
        private Spell tier1Spell;
        private Spell tier2Spell;
        private Spell tier3Spell;

        public MutableItemStat(int buy, int sell, int upgrade) {
            this.buyPrice = buy;
            this.sellPrice = sell;
            this.upgradeDifficulty = upgrade;
        }

        public MutableItemStat setElement(EnumElement element) {
            this.element = element;
            return this;
        }

        public MutableItemStat addAttribute(Attribute att, double value) {
            this.itemStats.put(att, value);
            return this;
        }

        public MutableItemStat setSpell(Spell tier1, Spell tier2, Spell tier3) {
            this.tier1Spell = tier1;
            this.tier2Spell = tier2;
            this.tier3Spell = tier3;
            return this;
        }
    }
}