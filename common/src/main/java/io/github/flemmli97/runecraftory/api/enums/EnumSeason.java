package io.github.flemmli97.runecraftory.api.enums;

import net.minecraft.ChatFormatting;

public enum EnumSeason {

    SPRING(ChatFormatting.RED, "spring"),
    SUMMER(ChatFormatting.DARK_GREEN, "summer"),
    AUTUMN(ChatFormatting.GOLD, "autumn"),
    WINTER(ChatFormatting.BLUE, "winter");

    public static final String PREFIX = "runecraftory.season.";

    private final ChatFormatting color;
    private final String translationKey;

    EnumSeason(ChatFormatting color, String key) {
        this.color = color;
        this.translationKey = PREFIX + key;
    }

    public static EnumSeason nextSeason(EnumSeason season) {
        return switch (season) {
            case AUTUMN -> WINTER;
            case SPRING -> SUMMER;
            case SUMMER -> AUTUMN;
            default -> SPRING;
        };
    }

    public ChatFormatting getColor() {
        return this.color;
    }

    public String translationKey() {
        return this.translationKey;
    }
}
