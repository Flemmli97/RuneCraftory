package com.flemmli97.runecraftory.api.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Random;

public enum EnumMineralTier implements IStringSerializable {

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

    @Override
    public String getString() {
        return this.name;
    }

    public static EnumMineralTier randomNonElemental(Random rand) {
        return values()[rand.nextInt(8)];
    }

    public static boolean isElemental(EnumMineralTier tier) {
        return tier.ordinal() > 7;
    }
}