package com.flemmli97.runecraftory.api.enums;

import com.google.common.collect.Maps;
import net.minecraft.util.text.TextFormatting;

import java.util.Map;

public enum EnumElement {
    NONE(TextFormatting.GRAY, "none"),
    WATER(TextFormatting.DARK_BLUE, "water"),
    EARTH(TextFormatting.YELLOW, "earth"),
    WIND(TextFormatting.GREEN, "wind"),
    FIRE(TextFormatting.DARK_RED, "fire"),
    LIGHT(TextFormatting.WHITE, "light"),
    DARK(TextFormatting.DARK_PURPLE, "dark"),
    LOVE(TextFormatting.RED, "love");

    private final String name;
    private final TextFormatting color;
    private static final Map<String, EnumElement> map = Maps.newHashMap();

    EnumElement(TextFormatting color, String name) {
        this.color = color;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public TextFormatting getColor() {
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
