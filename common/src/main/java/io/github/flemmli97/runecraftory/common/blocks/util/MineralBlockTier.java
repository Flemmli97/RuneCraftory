package io.github.flemmli97.runecraftory.common.blocks.util;

import net.minecraft.util.StringRepresentable;

import java.util.Random;

public enum MineralBlockTier implements StringRepresentable {

    IRON("iron"),
    TIN("tin"),
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

    MineralBlockTier(String name) {
        this.name = name;
    }

    public static MineralBlockTier randomNonElemental(Random rand) {
        return values()[rand.nextInt(8)];
    }

    public static boolean isElemental(MineralBlockTier tier) {
        return tier.ordinal() > 7;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}