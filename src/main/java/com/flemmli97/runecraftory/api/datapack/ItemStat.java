package com.flemmli97.runecraftory.api.datapack;

import com.flemmli97.runecraftory.api.enums.EnumElement;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.entity.ai.attributes.Attribute;

import java.util.Map;
import java.util.TreeMap;

public class ItemStat {
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

    @Override
    public String toString() {
        return "[Buy:" + this.buyPrice + ";Sell:" + this.sellPrice + ";UpgradeDifficulty:" + this.upgradeDifficulty + ";DefaultElement:" + this.element + "];{stats:[" + this.itemStats + "]}";
    }

    public static class MutableItemStat {

        public int buyPrice;
        public int sellPrice;
        public int upgradeDifficulty;
        public EnumElement element = EnumElement.NONE;
        public Map<Attribute, Integer> itemStats = Maps.newHashMap();

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