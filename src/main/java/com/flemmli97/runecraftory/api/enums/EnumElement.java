package com.flemmli97.runecraftory.api.enums;

import net.minecraft.util.text.TextFormatting;

public enum EnumElement {
    NONE(TextFormatting.GRAY, "element.none"),
    WATER(TextFormatting.DARK_BLUE, "element.water"),
    EARTH(TextFormatting.YELLOW, "element.earth"),
    WIND(TextFormatting.GREEN, "element.wind"),
    FIRE(TextFormatting.DARK_RED, "element.fire"),
    LIGHT(TextFormatting.WHITE, "element.light"),
    DARK(TextFormatting.DARK_PURPLE, "element.dark"),
    LOVE(TextFormatting.RED, "element.love");

    private final String translation;
    private final TextFormatting color;

    EnumElement(TextFormatting color, String name) {
        this.color = color;
        this.translation = name;
    }

    public String getTranslation() {
        return this.translation;
    }

    public TextFormatting getColor() {
        return this.color;
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
}
