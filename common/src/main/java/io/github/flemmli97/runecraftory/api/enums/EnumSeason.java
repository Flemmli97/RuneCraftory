package io.github.flemmli97.runecraftory.api.enums;

import net.minecraft.ChatFormatting;

public enum EnumSeason {

    SPRING(ChatFormatting.RED, "runecraftory.season.spring"),
    SUMMER(ChatFormatting.DARK_GREEN, "runecraftory.season.summer"),
    FALL(ChatFormatting.GOLD, "runecraftory.season.fall"),
    WINTER(ChatFormatting.BLUE, "runecraftory.season.winter");

    private final ChatFormatting color;
    private final String translationKey;

    EnumSeason(ChatFormatting color, String id) {
        this.color = color;
        this.translationKey = id;
    }

    public static EnumSeason nextSeason(EnumSeason season) {
        return switch (season) {
            case FALL -> WINTER;
            case SPRING -> SUMMER;
            case SUMMER -> FALL;
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
