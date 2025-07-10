package io.github.flemmli97.runecraftory.api.datapack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record SkillProperties(int maxLevel, float healthIncrease, float rpIncrease, float strIncrease,
                              float vitIncrease,
                              float intelIncrease, float xpMultiplier) {

    public static final SkillProperties DEFAULT = new SkillProperties(1, 0, 0, 0, 0, 0, 0);

    public static final Codec<SkillProperties> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.INT.fieldOf("max_level").forGetter(d -> d.maxLevel),
                    Codec.FLOAT.fieldOf("health_increase").forGetter(d -> d.healthIncrease),
                    Codec.FLOAT.fieldOf("rp_increase").forGetter(d -> d.rpIncrease),
                    Codec.FLOAT.fieldOf("str_increase").forGetter(d -> d.strIncrease),
                    Codec.FLOAT.fieldOf("vit_increase").forGetter(d -> d.vitIncrease),
                    Codec.FLOAT.fieldOf("intel_increase").forGetter(d -> d.intelIncrease),
                    Codec.FLOAT.fieldOf("xp_multiplier").forGetter(d -> d.xpMultiplier)
            ).apply(instance, SkillProperties::new));
}
