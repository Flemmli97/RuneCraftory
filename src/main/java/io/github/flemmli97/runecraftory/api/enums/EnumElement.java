package io.github.flemmli97.runecraftory.api.enums;

import net.minecraft.util.text.TextFormatting;

public enum EnumElement {

    NONE(TextFormatting.GRAY, "element_none", 0xffffff),
    WATER(TextFormatting.DARK_BLUE, "element_water", 0x0505aa),
    EARTH(TextFormatting.YELLOW, "element_earth", 0xe6e605),
    WIND(TextFormatting.GREEN, "element_wind", 0x55ff55),
    FIRE(TextFormatting.DARK_RED, "element_fire", 0xaa0505),
    LIGHT(TextFormatting.WHITE, "element_light", 0xffff69),
    DARK(TextFormatting.DARK_PURPLE, "element_dark", 0x820582),
    LOVE(TextFormatting.RED, "element_love", 0xfcc0fc);

    private final String translation;
    private final TextFormatting color;
    private final int particleColor;

    EnumElement(TextFormatting color, String name, int particleColor) {
        this.color = color;
        this.translation = name;
        this.particleColor = particleColor;
    }

    public String getTranslation() {
        return this.translation;
    }

    public TextFormatting getColor() {
        return this.color;
    }

    public int getParticleColor() {
        return this.particleColor;
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
