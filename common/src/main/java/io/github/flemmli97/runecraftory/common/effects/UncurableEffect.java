package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import net.minecraft.world.effect.MobEffectCategory;

public class UncurableEffect extends SyncedMobEffect {

    public UncurableEffect(MobEffectCategory type, int color, S2CEntityDataSync.DataType packetType) {
        super(type, color, packetType);
    }
}
