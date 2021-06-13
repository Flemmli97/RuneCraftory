package io.github.flemmli97.runecraftory.common.config.values;

public class WeaponTypeProperties {

    private float range;
    private float aoe;
    private int chargeTime;
    private int cooldown;

    public WeaponTypeProperties(float range, float aoe, int chargeTime, int cooldown) {
        this.range = range;
        this.aoe = aoe;
        this.chargeTime = chargeTime;
        this.cooldown = cooldown;
    }

    public WeaponTypeProperties(WeaponTypePropertySpecs specs) {
        this.read(specs);
    }

    public float range() {
        return this.range;
    }

    public float aoe() {
        return this.aoe;
    }

    public int chargeTime() {
        return this.chargeTime;
    }

    public int cooldown() {
        return this.cooldown;
    }

    public WeaponTypeProperties read(WeaponTypePropertySpecs specs) {
        this.range = specs.range.get().floatValue();
        this.aoe = specs.aoe.get().floatValue();
        this.chargeTime = specs.chargeTime.get();
        this.cooldown = specs.cooldown.get();
        return this;
    }
}
