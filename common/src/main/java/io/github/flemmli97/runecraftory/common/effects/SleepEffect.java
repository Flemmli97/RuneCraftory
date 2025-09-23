package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.common.entities.utils.SleepingEntity;
import io.github.flemmli97.runecraftory.mixinhelper.MobToggleHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SleepEffect extends SyncedMobEffect {

    public SleepEffect() {
        super(MobEffectCategory.HARMFUL, 0x910b2e, eff -> ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0));
    }

    @Override
    public boolean applyEffectTick(LivingEntity living, int amplifier) {
        if (!(living instanceof Player player) || !player.getAbilities().invulnerable) {
            if (!living.noPhysics)
                living.setDeltaMovement(new Vec3(living.getDeltaMovement().x, -0.08, living.getDeltaMovement().z));
            // Setting no ai prevents mobs moving/getting moved. So we do it here
            if (living instanceof MobToggleHandler mob) {
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
        Platform.INSTANCE.getEntityData(entity).setSleeping(true);
    }

    @Override
    public void onEffectRemoved(LivingEntity entity, MobEffectInstance instance) {
        if (entity instanceof SleepingEntity sleeping)
            sleeping.setSleeping(false);
        Platform.INSTANCE.getEntityData(entity).setSleeping(false);
    }
}
