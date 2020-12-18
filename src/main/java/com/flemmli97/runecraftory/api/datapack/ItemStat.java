package com.flemmli97.runecraftory.api.datapack;

import com.flemmli97.runecraftory.api.enums.EnumElement;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import com.flemmli97.runecraftory.common.utils.ItemUtils;
import com.flemmli97.runecraftory.lib.LibAttributes;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ItemStat {

    private static Set<ResourceLocation> flatAttributes = Sets.newHashSet(
            LibAttributes.GENERIC_ATTACK_DAMAGE,
            LibAttributes.rf_defence,
            LibAttributes.rf_magic,
            LibAttributes.rf_magic_defence);

    private int buyPrice;
    private int sellPrice;
    private int upgradeDifficulty;
    private EnumElement element;
    private Map<Attribute, Integer> itemStats = new TreeMap<>(ModAttributes.sorted);

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

    public Map<Attribute, Integer> itemStats() {
        return ImmutableMap.copyOf(this.itemStats);
    }

    public void toPacket(PacketBuffer buffer) {
        buffer.writeInt(this.buyPrice);
        buffer.writeInt(this.sellPrice);
        buffer.writeInt(this.upgradeDifficulty);
        buffer.writeEnumValue(this.element);
        buffer.writeInt(this.itemStats.size());
        this.itemStats.forEach((att, val) -> {
            buffer.writeRegistryId(att);
            buffer.writeInt(val);
        });
    }

    public static ItemStat fromPacket(PacketBuffer buffer) {
        ItemStat stat = new ItemStat();
        stat.buyPrice = buffer.readInt();
        stat.sellPrice = buffer.readInt();
        stat.upgradeDifficulty = buffer.readInt();
        stat.element = buffer.readEnumValue(EnumElement.class);
        int size = buffer.readInt();
        for (int i = 0; i < size; i++)
            stat.itemStats.put(buffer.readRegistryIdSafe(Attribute.class), buffer.readInt());
        return stat;
    }

    public List<ITextComponent> texts(ItemStack stack, boolean showStat) {
        List<ITextComponent> list = Lists.newArrayList();
        CompoundNBT tag = ItemNBT.getItemNBT(stack);
        if (tag != null) {
            if (stack.getItem() instanceof IItemUsable) {
                try {
                    EnumElement element = EnumElement.valueOf(tag.getString("Element"));
                    if (element != EnumElement.NONE) {
                        list.add(new TranslationTextComponent(element.getTranslation()).formatted(element.getColor()));
                    }
                } catch (IllegalArgumentException e) {
                }
            }
            IFormattableTextComponent price = new TranslationTextComponent("tooltip.item.level", tag.getInt("ItemLevel"));
            if (ItemUtils.getBuyPrice(stack, this) > 0)
                price.append(" ").append(new TranslationTextComponent("tooltip.item.buy", ItemUtils.getBuyPrice(stack)));
            price.append(" ").append(new TranslationTextComponent("tooltip.item.sell", ItemUtils.getSellPrice(stack)));
            list.add(price);
            if (showStat) {
                Map<Attribute, Integer> stats = ItemNBT.statIncrease(stack);
                if (!stats.isEmpty()) {
                    String prefix = ItemNBT.shouldHaveStats(stack) ? "tooltip.item.equipped" : "tooltip.item.upgrade";
                    list.add(new TranslationTextComponent(prefix));
                }
                for (Map.Entry<Attribute, Integer> entry : stats.entrySet()) {
                    IFormattableTextComponent comp = new StringTextComponent(" ").append(new TranslationTextComponent(entry.getKey().getTranslationKey())).append(new StringTextComponent(": " + format(entry.getKey(), entry.getValue())));
                    list.add(comp);
                }
            }
        }
        return list;
    }

    private String format(Attribute att, int n) {
        boolean flat = flatAttributes.contains(att.getRegistryName());
        int val = flat ? n : (n > 100 ? n-100 : 100-n);
        return val >= (flat?0:100) ? "+" + val : "-" + val;
    }

    @Override
    public String toString() {
        return "[Buy:" + this.buyPrice + ";Sell:" + this.sellPrice + ";UpgradeDifficulty:" + this.upgradeDifficulty + ";DefaultElement:" + this.element + "];{stats:[" + this.itemStats + "]}";
    }

    public static class MutableItemStat {

        private int buyPrice;
        private int sellPrice;
        private int upgradeDifficulty;
        private EnumElement element = EnumElement.NONE;
        private Map<Attribute, Integer> itemStats = Maps.newHashMap();

        public MutableItemStat(int buy, int sell, int upgrade) {
            this.buyPrice = buy;
            this.sellPrice = sell;
            this.upgradeDifficulty = upgrade;
        }

        public MutableItemStat setElement(EnumElement element) {
            this.element = element;
            return this;
        }

        public MutableItemStat addAttribute(Attribute att, int value) {
            this.itemStats.put(att, value);
            return this;
        }
    }
}