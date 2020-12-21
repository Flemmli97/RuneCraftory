package com.flemmli97.runecraftory.api.enums;

import net.minecraft.util.text.TextFormatting;

public enum EnumSeason {

    SPRING(TextFormatting.RED, "season.spring"),
    SUMMER(TextFormatting.DARK_GREEN, "season.summer"),
    FALL(TextFormatting.GOLD, "season.fall"),
    WINTER(TextFormatting.BLUE, "season.winter");

    private final TextFormatting color;
    private final String translationKey;

    EnumSeason(TextFormatting color, String id) {
        this.color = color;
        this.translationKey = id;
    }

    public static EnumSeason nextSeason(EnumSeason season) {
        switch (season) {
            case FALL:
                return WINTER;
            case SPRING:
                return SUMMER;
            case SUMMER:
                return FALL;
            default:
                return SPRING;
        }
    }

    public TextFormatting getColor() {
        return this.color;
    }

    public String translationKey() {
        return this.translationKey;
    }
}
