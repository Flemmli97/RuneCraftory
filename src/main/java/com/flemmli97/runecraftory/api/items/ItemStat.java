package com.flemmli97.runecraftory.api.items;

import java.util.Map;

import com.flemmli97.runecraftory.common.lib.enums.EnumElement;

public class ItemStat
{
    private Map<ItemStatAttributes, Integer> map;
    private int buy;
    private int sell;
    private int upgradeDifficulty;
    private EnumElement element;
    
    public ItemStat(int buy, int sell, int upgradeDiff, EnumElement element, Map<ItemStatAttributes, Integer> stats) {
        this.buy = buy;
        this.sell = sell;
        this.upgradeDifficulty = upgradeDiff;
        this.element = element;
        this.map = stats;
    }
    
    public int getBuy() {
        return this.buy;
    }
    
    public int getSell() {
        return this.sell;
    }
    
    public int getDiff() {
        return this.upgradeDifficulty;
    }
    
    public EnumElement element() {
        return this.element;
    }
    
    public Map<ItemStatAttributes, Integer> itemStats() {
        return this.map;
    }
    
    @Override
    public String toString() {
        return "[Buy:" + this.buy + ";Sell:" + this.sell + ";UpgradeDifficulty:" + this.upgradeDifficulty + ";DefaultElement:" + this.element + "]";
    }
}
