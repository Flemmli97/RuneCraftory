package io.github.flemmli97.runecraftory.api.datapack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record WeaponTypeProperties(float aoe, int chargeTime) {

    public static WeaponTypeProperties DEFAULT = new WeaponTypeProperties(0, 20);

    public static final Codec<WeaponTypeProperties> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.FLOAT.fieldOf("aoe").forGetter(d -> d.aoe),
                    Codec.INT.fieldOf("chargeTime").forGetter(d -> d.chargeTime)
            ).apply(instance, WeaponTypeProperties::new));

}
