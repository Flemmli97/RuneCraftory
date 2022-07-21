package io.github.flemmli97.runecraftory.api.enums;

import net.minecraft.util.StringRepresentable;

import java.util.Random;

public enum EnumMineralTier implements StringRepresentable {

    IRON("iron"),
    BRONZE("bronze"),
    SILVER("silver"),
    GOLD("gold"),
    DIAMOND("diamond"),
    PLATINUM("platinum"),
    ORICHALCUM("orichalcum"),
    DRAGONIC("dragonic"),
    AMETHYST("amethyst"),
    AQUAMARINE("aquamarine"),
    RUBY("ruby"),
    EMERALD("emerald"),
    SAPPHIRE("sapphire");

    private final String name;

    EnumMineralTier(String name) {
        this.name = name;
    }

    public static EnumMineralTier randomNonElemental(Random rand) {
        return values()[rand.nextInt(8)];
    }

    public static boolean isElemental(EnumMineralTier tier) {
        return tier.ordinal() > 7;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}