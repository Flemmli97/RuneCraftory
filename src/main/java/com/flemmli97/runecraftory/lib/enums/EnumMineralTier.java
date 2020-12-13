package com.flemmli97.runecraftory.lib.enums;

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

    private EnumMineralTier(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getString() {
        return this.name;
    }

    public static EnumMineralTier randomNonElemental(Random rand) {
        return values()[rand.nextInt(5)];
    }

    public static boolean isElemental(EnumMineralTier tier) {
        return tier == EnumMineralTier.AMETHYST || tier == EnumMineralTier.AQUAMARINE || tier == EnumMineralTier.EMERALD || tier == EnumMineralTier.RUBY || tier == EnumMineralTier.SAPPHIRE;
    }

}