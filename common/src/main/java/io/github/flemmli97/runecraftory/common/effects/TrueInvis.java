package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import net.minecraft.world.effect.MobEffectCategory;

public class TrueInvis extends UncurableEffect {

    public TrueInvis() {
        super(MobEffectCategory.NEUTRAL, 0, S2CEntityDataSync.DataType.INVIS);
    }
}
