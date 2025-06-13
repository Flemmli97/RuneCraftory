package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.common.entities.utils.SleepingEntity;
import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import io.github.flemmli97.runecraftory.mixinhelper.MobNoAIHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SleepEffect extends SyncedMobEffect {

    public SleepEffect() {
        super(MobEffectCategory.HARMFUL, 0, S2CEntityDataSync.DataType.SLEEP);
    }

    private static void applySleep(LivingEntity entity, boolean flag) {
        Platform.INSTANCE.getEntityData(entity).setSleeping(entity, flag);
    }

    @Override
    public boolean applyEffectTick(LivingEntity living, int amplifier) {
        if (!(living instanceof Player player) || !player.getAbilities().invulnerable) {
            if (!living.noPhysics)
                living.setDeltaMovement(new Vec3(living.getDeltaMovement().x, -0.08, living.getDeltaMovement().z));
            // Setting no ai prevents mobs moving/getting moved. So we do it here
            if (living instanceof MobNoAIHandler mob) {
                mob.runecraftory$setIgnoreNoAI();
                living.travel(new Vec3(0, 0, 0));
            }
        }
        return super.applyEffectTick(living, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onEffectAdded(LivingEntity entity, MobEffectInstance instance) {
        if (entity instanceof SleepingEntity sleeping)
            sleeping.setSleeping(true);
        entity.setDeltaMovement(Vec3.ZERO);
        super.onEffectAdded(entity, instance);
    }

    @Override
    public void onEffectRemoved(LivingEntity entity, MobEffectInstance instance) {
        if (entity instanceof SleepingEntity sleeping)
            sleeping.setSleeping(false);
        super.onEffectRemoved(entity, instance);
    }
}
