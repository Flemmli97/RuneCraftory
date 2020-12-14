package com.flemmli97.runecraftory.api.enums;

public enum EnumCrafting {
    FORGE(0),
    ARMOR(1),
    PHARMA(2),
    COOKING(3);

    private int id;

    private EnumCrafting(final int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }

    public static EnumCrafting fromID(final int id) {
        if (id >= values().length) {
            return EnumCrafting.FORGE;
        }
        return values()[id];
    }
}
