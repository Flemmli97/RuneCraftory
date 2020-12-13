package com.flemmli97.runecraftory.lib.enums;

public enum EnumWeaponType {
    FARM("farm", 3.0f, 0),
    SHORTSWORD("short", 2.5f, 6),
    LONGSWORD("long", 4.0f, 25),
    SPEAR("spear", 5.0f, 1.5f),
    HAXE("haxe", 4.5f, 40),
    DUAL("dual", 3.0f, 10),
    GLOVE("glove", 3.0f, 3),
    STAFF("staff", 3.5f, 0);

    private String id;
    private float range;
    private float aoe;

    EnumWeaponType(String identifier, float range, float aoe) {
        this.id = identifier;
        this.range = range;
        this.aoe = aoe;
    }

    public String getName() {
        return this.id;
    }

    public float getRange() {
        return this.range;
    }

    public float getAOE() {
        return this.aoe;
    }
}
