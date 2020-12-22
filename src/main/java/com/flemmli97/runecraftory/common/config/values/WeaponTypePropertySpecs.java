package com.flemmli97.runecraftory.common.config.values;

import net.minecraftforge.common.ForgeConfigSpec;

public class WeaponTypePropertySpecs {

    public final ForgeConfigSpec.ConfigValue<Double> range;
    public final ForgeConfigSpec.ConfigValue<Double> aoe;
    public final ForgeConfigSpec.ConfigValue<Integer> chargeTime;
    public final ForgeConfigSpec.ConfigValue<Integer> cooldown;

    public WeaponTypePropertySpecs(ForgeConfigSpec.Builder builder, WeaponTypeProperties def) {
        this.range = builder.comment("Range of the weapon").define("Range", (double) def.range());
        this.aoe = builder.comment("AoE of the weapon").define("AoE", (double) def.aoe());
        this.chargeTime = builder.comment("Time for the charge attack to charge up").define("Charge Time", def.chargeTime());
        this.cooldown = builder.comment("Cooldown time on entity hit").define("Cooldown", def.cooldown());
    }
}
