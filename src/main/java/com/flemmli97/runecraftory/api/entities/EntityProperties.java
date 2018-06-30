package com.flemmli97.runecraftory.api.entities;

import java.util.Map;

import com.flemmli97.runecraftory.api.items.ItemProperties;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;

import net.minecraft.entity.ai.attributes.IAttribute;

public class EntityProperties
{
    private Map<IAttribute, Double> baseValues;
    private Map<ItemProperties, Float> drops;
    private EnumElement element;
    private int xp;
    private int money;
    private float taming;
    private ItemProperties[] tamingItem;
    private Map<ItemProperties, Integer> daily;
    private boolean ridable;
    private boolean flying;
    
    public EntityProperties(Map<IAttribute, Double> baseValues, Map<ItemProperties, Float> drops, int xp, int money, float tamingChance, ItemProperties[] tamingItem, Map<ItemProperties, Integer> dailyDrops, boolean ridable, boolean flying) {
        this.element = EnumElement.NONE;
        this.baseValues = baseValues;
        this.drops = drops;
        this.xp = xp;
        this.money = money;
        this.taming = tamingChance;
        this.tamingItem = tamingItem;
        this.daily = dailyDrops;
        this.ridable = ridable;
        this.flying = flying;
    }
    
    public Map<IAttribute, Double> getBaseValues() {
        return this.baseValues;
    }
    
    public Map<ItemProperties, Integer> dailyDrops() {
        return this.daily;
    }
    
    public boolean ridable() {
        return this.ridable;
    }
    
    public boolean flying() {
        return this.flying;
    }
    
    public Map<ItemProperties, Float> drops() {
        return this.drops;
    }
    
    public int getXp() {
        return this.xp;
    }
    
    public EnumElement getElement() {
        return this.element;
    }
    
    public int getMoney() {
        return this.money;
    }
    
    public float tamingChance() {
        return this.taming;
    }
    
    public ItemProperties[] getTamingItem() {
        return this.tamingItem;
    }
}
