package io.github.flemmli97.runecraftory.fabric.config.values;

import io.github.flemmli97.runecraftory.common.config.values.SkillProperties;
import io.github.flemmli97.tenshilib.common.config.CommentedJsonConfig;

public class SkillPropertySpecs {

    public final CommentedJsonConfig.DoubleVal hp;
    public final CommentedJsonConfig.DoubleVal rp;
    public final CommentedJsonConfig.DoubleVal str;
    public final CommentedJsonConfig.DoubleVal vit;
    public final CommentedJsonConfig.DoubleVal intel;
    public final CommentedJsonConfig.IntVal baseXP;

    public SkillPropertySpecs(CommentedJsonConfig.Builder builder, SkillProperties def) {
        this.hp = builder.comment("HP gain per level").defineInRange("HP", def.healthIncrease(), 0, Double.MAX_VALUE);
        this.rp = builder.comment("RP gain per level").defineInRange("RP", def.rpIncrease(), 0, Double.MAX_VALUE);
        this.str = builder.comment("Strength gain per level").defineInRange("Str", def.strIncrease(), 0, Double.MAX_VALUE);
        this.vit = builder.comment("Vitality gain per level").defineInRange("Vit", def.vitIncrease(), 0, Double.MAX_VALUE);
        this.intel = builder.comment("Intelligence gain per level").defineInRange("Intel", def.intelIncrease(), 0, Double.MAX_VALUE);
        this.baseXP = builder.comment("Base xp gained per action").defineInRange("BaseXP", def.baseXP(), 0, Integer.MAX_VALUE);
    }

    public static SkillProperties ofSpec(SkillPropertySpecs specs) {
        return new SkillProperties(specs.hp.get().floatValue(), specs.rp.get().floatValue(), specs.str.get().floatValue(),
                specs.vit.get().floatValue(), specs.intel.get().floatValue(), specs.baseXP.get());
    }
}
