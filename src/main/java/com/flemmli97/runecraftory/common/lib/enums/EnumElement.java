package com.flemmli97.runecraftory.common.lib.enums;

import java.util.*;

import com.google.common.collect.Maps;

public enum EnumElement
{
    NONE("white", "none"), 
    WATER("dark_blue", "water"), 
    EARTH("yellow", "earth"), 
    WIND("green", "wind"), 
    FIRE("dark_red", "fire"), 
    LIGHT("white", "light"), 
    DARK("dark_purple", "dark"), 
    LOVE("red", "love");
    
    private String name;
    private String color;
    private static final Map<String, EnumElement> map = Maps.newHashMap();
    
    private EnumElement(String color, String name) {
        this.color = color;
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getColor() {
        return this.color;
    }
    
    public static EnumElement fromName(String name) {
        return EnumElement.map.get(name);
    }
    
    public static EnumElement opposing(EnumElement element) {
        switch (element) {
            case DARK: {
                return EnumElement.LIGHT;
            }
            case EARTH: {
                return EnumElement.WIND;
            }
            case FIRE: {
                return EnumElement.WATER;
            }
            case LIGHT: {
                return EnumElement.DARK;
            }
            case LOVE: {
                return EnumElement.NONE;
            }
            case NONE: {
                return EnumElement.LOVE;
            }
            case WATER: {
                return EnumElement.FIRE;
            }
            case WIND: {
                return EnumElement.EARTH;
            }
            default: {
                return EnumElement.NONE;
            }
        }
    }
    
    static {
        for (EnumElement skill : values()) {
            EnumElement.map.put(skill.name, skill);
        }
    }
}
