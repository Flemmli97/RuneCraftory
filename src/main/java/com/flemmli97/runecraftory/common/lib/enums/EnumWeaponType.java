package com.flemmli97.runecraftory.common.lib.enums;

public enum EnumWeaponType
{
    FARM("farm", 3.0f, 0.0f), 
    SHORTSWORD("short", 2.5f, 1.0f), 
    LONGSWORD("long", 4.0f, 2.5f), 
    SPEAR("spear", 5.0f, 0.5f), 
    HAXE("haxe", 4.5f, 4.0f), 
    DUAL("dual", 3.0f, 1.5f), 
    GLOVE("glove", 3.0f, 0.5f), 
    STAFF("staff", 3.5f, 0.0f);
    
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
