package io.github.flemmli97.runecraftory.common.config.values;

import net.minecraftforge.common.ForgeConfigSpec;

public class SkillPropertySpecs {

    public final ForgeConfigSpec.ConfigValue<Double> hp;
    public final ForgeConfigSpec.ConfigValue<Integer> rp;
    public final ForgeConfigSpec.ConfigValue<Double> str;
    public final ForgeConfigSpec.ConfigValue<Double> vit;
    public final ForgeConfigSpec.ConfigValue<Double> intel;
    public final ForgeConfigSpec.ConfigValue<Integer> baseXP;

    public SkillPropertySpecs(ForgeConfigSpec.Builder builder, SkillProperties def) {
        this.hp = builder.comment("HP gain per level").define("HP", (double) def.getHealthIncrease());
        this.rp = builder.comment("RP gain per level").define("RP", def.getRPIncrease());
        this.str = builder.comment("Strength gain per level").define("Str", (double) def.getStrIncrease());
        this.vit = builder.comment("Vitality gain per level").define("Vit", (double) def.getVitIncrease());
        this.intel = builder.comment("Intelligence gain per level").define("Intel", (double) def.getIntelIncrease());
        this.baseXP = builder.comment("Base xp gained per action").define("BaseXP", def.getBaseXPGain());
    }
}
