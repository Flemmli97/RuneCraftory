package com.flemmli97.runecraftory.common.lib.enums;

public enum EnumWeaponType
{
    FARM("farm", 3.0f, 0), 
    SHORTSWORD("short", 2.5f, 10), 
    LONGSWORD("long", 4.0f, 35), 
    SPEAR("spear", 5.0f, 5), 
    HAXE("haxe", 4.5f, 40), 
    DUAL("dual", 3.0f, 15), 
    GLOVE("glove", 3.0f, 5), 
    STAFF("staff", 3.5f, 0);
    
    private String id;
    private float range;
    private float aoe;
    
    private EnumWeaponType(String identifier, float range, float aoe) {
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
