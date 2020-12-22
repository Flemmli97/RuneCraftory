package com.flemmli97.runecraftory.common.config.values;

public class SkillProperties {

    private float hp;
    private int rp;
    private float str;
    private float vit;
    private float intel;
    private int baseXP;

    public SkillProperties(float healthIncrease, int rpIncrease, float strIncrease, float vitIncrease, float intelIncrease, int baseXP) {
        this.hp = healthIncrease;
        this.rp = rpIncrease;
        this.str = strIncrease;
        this.vit = vitIncrease;
        this.intel = intelIncrease;
        this.baseXP = baseXP;
    }

    public SkillProperties(SkillPropertySpecs specs){
        this.read(specs);
    }

    public float getHealthIncrease() {
        return this.hp;
    }

    public int getRPIncrease() {
        return this.rp;
    }

    public float getStrIncrease() {
        return this.str;
    }

    public float getVitIncrease() {
        return this.vit;
    }

    public float getIntelIncrease() {
        return this.intel;
    }

    public int getBaseXPGain() {
        return this.baseXP;
    }

    public SkillProperties read(SkillPropertySpecs specs) {
        this.hp = specs.hp.get();
        this.rp = specs.rp.get();
        this.str = specs.str.get();
        this.vit = specs.vit.get();
        this.intel = specs.intel.get();
        this.baseXP = specs.baseXP.get();
        return this;
    }
}
