package io.github.flemmli97.runecraftory.api.enums;

public enum EnumWeaponType {

    FARM(1),
    SHORTSWORD(1),
    LONGSWORD(0.5f),
    SPEAR(0.5f),
    HAXE(0.5f),
    DUAL(0),
    GLOVE(0),
    STAFF(0.5f);

    public final float shieldEfficiency;

    EnumWeaponType(float shieldEfficiency) {
        this.shieldEfficiency = shieldEfficiency;
    }

}
