package io.github.flemmli97.runecraftory.api.enums;

import net.minecraft.ChatFormatting;

public enum EnumElement {

    NONE(ChatFormatting.GRAY, "element_none", 0xffffff),
    WATER(ChatFormatting.DARK_BLUE, "element_water", 0x101099),
    EARTH(ChatFormatting.YELLOW, "element_earth", 0xe6e610),
    WIND(ChatFormatting.GREEN, "element_wind", 0x55ff55),
    FIRE(ChatFormatting.DARK_RED, "element_fire", 0x991010),
    LIGHT(ChatFormatting.WHITE, "element_light", 0xffff60),
    DARK(ChatFormatting.DARK_PURPLE, "element_dark", 0x821082),
    LOVE(ChatFormatting.RED, "element_love", 0xfc60fc);

    private final String translation;
    private final ChatFormatting color;
    private final int particleColor;

    EnumElement(ChatFormatting color, String name, int particleColor) {
        this.color = color;
        this.translation = name;
        this.particleColor = particleColor;
    }

    public static EnumElement opposing(EnumElement element) {
        switch (element) {
            case DARK -> {
                return EnumElement.LIGHT;
            }
            case EARTH -> {
                return EnumElement.WIND;
            }
            case FIRE -> {
                return EnumElement.WATER;
            }
            case LIGHT -> {
                return EnumElement.DARK;
            }
            case NONE -> {
                return EnumElement.LOVE;
            }
            case WATER -> {
                return EnumElement.FIRE;
            }
            case WIND -> {
                return EnumElement.EARTH;
            }
            default -> {
                return EnumElement.NONE;
            }
        }
    }

    public String getTranslation() {
        return this.translation;
    }

    public ChatFormatting getColor() {
        return this.color;
    }

    public int getParticleColor() {
        return this.particleColor;
    }
}
