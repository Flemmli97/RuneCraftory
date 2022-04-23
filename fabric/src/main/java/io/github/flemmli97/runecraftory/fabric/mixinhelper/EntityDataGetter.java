package io.github.flemmli97.runecraftory.fabric.mixinhelper;

import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import net.minecraft.world.effect.MobEffectInstance;

public interface EntityDataGetter {

    EntityData getEntityData();

    void onCureEffect(MobEffectInstance effect);
}
