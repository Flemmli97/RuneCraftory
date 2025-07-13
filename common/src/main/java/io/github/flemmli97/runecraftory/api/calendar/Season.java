package io.github.flemmli97.runecraftory.api.calendar;

import net.minecraft.ChatFormatting;

public enum Season {

    SPRING(ChatFormatting.RED, "spring"),
    SUMMER(ChatFormatting.DARK_GREEN, "summer"),
    AUTUMN(ChatFormatting.GOLD, "autumn"),
    WINTER(ChatFormatting.BLUE, "winter");

    public static final String PREFIX = "runecraftory.season.";

    private final ChatFormatting color;
    private final String translationKey;

    Season(ChatFormatting color, String key) {
        this.color = color;
        this.translationKey = PREFIX + key;
    }

    public static Season nextSeason(Season season) {
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
