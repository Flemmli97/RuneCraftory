package io.github.flemmli97.runecraftory.forge.config.values;

import io.github.flemmli97.runecraftory.common.config.values.WeaponTypeProperties;
import net.minecraftforge.common.ForgeConfigSpec;

public class WeaponTypePropertySpecs {

    public final ForgeConfigSpec.DoubleValue range;
    public final ForgeConfigSpec.DoubleValue aoe;
    public final ForgeConfigSpec.IntValue chargeTime;
    public final ForgeConfigSpec.IntValue cooldown;

    public WeaponTypePropertySpecs(ForgeConfigSpec.Builder builder, WeaponTypeProperties def) {
        this.range = builder.comment("Range of the weapon").defineInRange("Range", def.range(), 0, Double.MAX_VALUE);
        this.aoe = builder.comment("AoE of the weapon").defineInRange("AoE", def.aoe(), 0, Double.MAX_VALUE);
        this.chargeTime = builder.comment("Time for the charge attack to charge up").defineInRange("Charge Time", def.chargeTime(), 0, Integer.MAX_VALUE);
        this.cooldown = builder.comment("Cooldown time on entity hit").defineInRange("Cooldown", def.cooldown(), 0, Integer.MAX_VALUE);
    }

    public static WeaponTypeProperties ofSpec(WeaponTypePropertySpecs specs) {
        return new WeaponTypeProperties(specs.range.get().floatValue(), specs.aoe.get().floatValue(), specs.chargeTime.get(),
                specs.cooldown.get());
    }
}
