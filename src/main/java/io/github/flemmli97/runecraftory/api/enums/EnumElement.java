package io.github.flemmli97.runecraftory.api.enums;

import net.minecraft.util.text.TextFormatting;

public enum EnumElement {

    NONE(TextFormatting.GRAY, "element_none"),
    WATER(TextFormatting.DARK_BLUE, "element_water"),
    EARTH(TextFormatting.YELLOW, "element_earth"),
    WIND(TextFormatting.GREEN, "element_wind"),
    FIRE(TextFormatting.DARK_RED, "element_fire"),
    LIGHT(TextFormatting.WHITE, "element_light"),
    DARK(TextFormatting.DARK_PURPLE, "element_dark"),
    LOVE(TextFormatting.RED, "element_love");

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
