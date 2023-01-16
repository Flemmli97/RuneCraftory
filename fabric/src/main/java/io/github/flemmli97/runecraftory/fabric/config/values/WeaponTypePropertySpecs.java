package io.github.flemmli97.runecraftory.fabric.config.values;

import io.github.flemmli97.runecraftory.common.config.values.WeaponTypeProperties;
import io.github.flemmli97.tenshilib.common.config.CommentedJsonConfig;

public class WeaponTypePropertySpecs {

    public final CommentedJsonConfig.DoubleVal aoe;
    public final CommentedJsonConfig.IntVal chargeTime;

    public WeaponTypePropertySpecs(CommentedJsonConfig.Builder builder, WeaponTypeProperties def) {
        this.aoe = builder.comment("AoE of the weapon").defineInRange("AoE", def.aoe(), 0, Double.MAX_VALUE);
        this.chargeTime = builder.comment("Time for the charge attack to charge up").defineInRange("Charge Time", def.chargeTime(), 0, Integer.MAX_VALUE);
    }

    public static WeaponTypeProperties ofSpec(WeaponTypePropertySpecs specs) {
        return new WeaponTypeProperties(specs.aoe.get().floatValue(), specs.chargeTime.get());
    }
}
