package io.github.flemmli97.runecraftory.api.datapack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record SkillProperties(int maxLevel, float healthIncrease, float rpIncrease, float strIncrease,
                              float vitIncrease,
                              float intelIncrease, float xpMultiplier) {

    public static final SkillProperties DEFAULT = new SkillProperties(1, 0, 0, 0, 0, 0, 0);

    public static final Codec<SkillProperties> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.FLOAT.fieldOf("vitIncrease").forGetter(d -> d.vitIncrease),
                    Codec.FLOAT.fieldOf("intelIncrease").forGetter(d -> d.intelIncrease),
                    Codec.FLOAT.fieldOf("xpMultiplier").forGetter(d -> d.xpMultiplier),

                    Codec.INT.fieldOf("maxLevel").forGetter(d -> d.maxLevel),
                    Codec.FLOAT.fieldOf("healthIncrease").forGetter(d -> d.healthIncrease),
                    Codec.FLOAT.fieldOf("rpIncrease").forGetter(d -> d.rpIncrease),
                    Codec.FLOAT.fieldOf("strIncrease").forGetter(d -> d.strIncrease)
            ).apply(instance, (vitIncrease, intelIncrease, xpMultiplier, maxLevel, healthIncrease, rpIncrease, strIncrease) ->
                    new SkillProperties(maxLevel, healthIncrease, rpIncrease, strIncrease, vitIncrease, intelIncrease, xpMultiplier)));
}
