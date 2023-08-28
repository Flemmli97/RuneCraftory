package io.github.flemmli97.runecraftory.api.enums;

public enum EnumWeaponType {

    FARM(1, 20),
    SHORTSWORD(1, 11),
    LONGSWORD(0.5f, 16),
    SPEAR(0.5f, 12),
    HAXE(0.5f, 18),
    DUAL(0, 7),
    GLOVE(0, 7),
    STAFF(0.5f, 12);

    public final float shieldEfficiency;

    public final int defaultWeaponSpeed;

    EnumWeaponType(float shieldEfficiency, int defaultWeaponSpeed) {
        this.shieldEfficiency = shieldEfficiency;
        this.defaultWeaponSpeed = defaultWeaponSpeed;
    }

}
