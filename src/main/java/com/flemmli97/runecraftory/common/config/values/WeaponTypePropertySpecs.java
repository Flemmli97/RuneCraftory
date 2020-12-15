package com.flemmli97.runecraftory.common.config.values;

import net.minecraftforge.common.ForgeConfigSpec;

public class WeaponTypePropertySpecs {

    public final ForgeConfigSpec.ConfigValue<Float> range;
    public final ForgeConfigSpec.ConfigValue<Float> aoe;

    public WeaponTypePropertySpecs(ForgeConfigSpec.Builder builder, WeaponTypeProperties def){
        this.range = builder.comment("Range of the weapon").define("Range", def.range());
        this.aoe = builder.comment("AoE of the weapon").define("AoE", def.aoe());
    }
}
