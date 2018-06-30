package com.flemmli97.runecraftory.common.lib.enums;

import java.util.*;

import com.google.common.collect.Maps;

public enum EnumSkills
{
    SHORTSWORD("Short Sword", 1.5f, 1, 0.5f, 0.5f, 0.0f), 
    LONGSWORD("Long Sword", 1.5f, 1, 1.0f, 0.0f, 0.0f), 
    SPEAR("Spear", 1.5f, 1, 1.0f, 0.0f, 0.0f), 
    HAMMERAXE("Hammer/Axe", 1.5f, 1, 1.0f, 0.0f, 0.0f), 
    DUAL("Dual Sword", 1.5f, 1, 1.0f, 0.5f, 0.0f), 
    FIST("Fist", 1.0f, 2, 1.0f, 0.5f, 0.0f), 
    FIRE("Fire", 1.0f, 2, 0.0f, 0.0f, 1.0f), 
    WATER("Water", 1.0f, 2, 0.0f, 0.0f, 1.0f), 
    EARTH("Earth", 1.0f, 2, 0.0f, 0.0f, 1.0f), 
    WIND("Wind", 1.0f, 2, 0.0f, 0.0f, 1.0f), 
    DARK("Dark", 1.0f, 2, 0.0f, 0.0f, 1.0f), 
    LIGHT("Light", 1.0f, 2, 0.0f, 0.0f, 1.0f), 
    LOVE("Love", 1.0f, 2, 0.0f, 0.0f, 1.0f), 
    FARMING("Farming", 0.0f, 3, 0.5f, 0.5f, 0.0f), 
    LOGGING("Logging", 0.0f, 4, 1.5f, 0.5f, 0.0f), 
    MINING("Mining", 0.0f, 2, 1.5f, 0.5f, 0.0f), 
    FISHING("Fishing", 0.0f, 5, 0.0f, 0.5f, 1.0f), 
    COOKING("Cooking", 1.0f, 2, 0.0f, 0.5f, 1.0f), 
    FORGING("Forging", 1.0f, 2, 1.0f, 0.5f, 1.0f), 
    CHEMISTRY("Chemistry", 1.0f, 2, 0.0f, 0.5f, 1.5f), 
    CRAFTING("Crafting", 1.0f, 2, 0.5f, 0.5f, 1.0f), 
    SLEEPING("Sleeping", 7.0f, 5, 0.0f, 2.5f, 0.0f), 
    EATING("Eating", 2.0f, 3, 0.0f, 0.5f, 0.0f), 
    DEFENCE("Defence", 5.0f, 3, 0.0f, 1.0f, 0.0f), 
    RESPOISON("Poison-Res", 10.0f, 2, 0.0f, 0.5f, 0.5f), 
    RESSEAL("Seal-Res", 5.0f, 6, 0.0f, 0.5f, 0.5f), 
    RESPARA("Para-Res", 3.0f, 3, 0.0f, 0.5f, 0.5f), 
    RESSLEEP("Sleep-Res", 5.0f, 2, 0.0f, 0.5f, 0.5f), 
    RESFAT("Fatigue-Res", 10.0f, 6, 0.0f, 0.5f, 0.5f), 
    RESCOLD("Cold-Res", 12.0f, 3, 0.0f, 0.5f, 0.5f), 
    BATH("Bathing", 3.0f, 4, 0.0f, 1.0f, 0.0f), 
    TAMING("Taming", 3.0f, 4, 1.0f, 1.0f, 1.0f), 
    LEADER("Leading", 7.0f, 5, 1.0f, 1.0f, 1.0f);
    
    private static final Map<String, EnumSkills> skillMap = Maps.newHashMap();
    private String identifier;
    private float hp;
    private float str;
    private float vit;
    private float intel;
    private int rp;
    
    private EnumSkills(String identifier, float healthIncrease, int rpIncrease, float strIncrease, float vitIncrease, float intelIncrease) {
        this.identifier = identifier;
        this.hp = healthIncrease;
        this.rp = rpIncrease;
        this.str = strIncrease;
        this.vit = vitIncrease;
        this.intel = intelIncrease;
    }
    
    public String getIdentifier() {
        return this.identifier;
    }
    
    public static EnumSkills getFromString(String name) {
        return EnumSkills.skillMap.get(name);
    }
    
    public float getHealthIncrease() {
        return this.hp;
    }
    
    public int getRPIncrease() {
        return this.rp;
    }
    
    public float getStrIncrease() {
        return this.str;
    }
    
    public float getVitIncrease() {
        return this.vit;
    }
    
    public float getIntelIncrease() {
        return this.intel;
    }
    
    static {
        for (EnumSkills skill : values()) {
            EnumSkills.skillMap.put(skill.identifier, skill);
        }
    }
}
