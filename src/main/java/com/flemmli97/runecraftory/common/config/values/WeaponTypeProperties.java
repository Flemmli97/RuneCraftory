package com.flemmli97.runecraftory.common.config.values;

public class WeaponTypeProperties {

    private float range;
    private float aoe;

    public WeaponTypeProperties(float range, float aoe){
        this.range = range;
        this.aoe = aoe;
    }

    public float range(){
        return this.range;
    }

    public float aoe(){
        return aoe;
    }

    public WeaponTypeProperties read(WeaponTypePropertySpecs specs){
        this.range = specs.range.get();
        this.aoe = specs.aoe.get();
        return this;
    }
}
