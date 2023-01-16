package io.github.flemmli97.runecraftory.forge.config.values;

import io.github.flemmli97.runecraftory.common.config.values.WeaponTypeProperties;
import net.minecraftforge.common.ForgeConfigSpec;

public class WeaponTypePropertySpecs {

    public final ForgeConfigSpec.DoubleValue aoe;
    public final ForgeConfigSpec.IntValue chargeTime;

    public WeaponTypePropertySpecs(ForgeConfigSpec.Builder builder, WeaponTypeProperties def) {
        this.aoe = builder.comment("AoE of the weapon").defineInRange("AoE", def.aoe(), 0, Double.MAX_VALUE);
        this.chargeTime = builder.comment("Time for the charge attack to charge up").defineInRange("Charge Time", def.chargeTime(), 0, Integer.MAX_VALUE);
    }

    public static WeaponTypeProperties ofSpec(WeaponTypePropertySpecs specs) {
        return new WeaponTypeProperties(specs.aoe.get().floatValue(), specs.chargeTime.get());
    }
}
