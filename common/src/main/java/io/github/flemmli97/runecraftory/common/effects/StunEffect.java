package io.github.flemmli97.runecraftory.common.effects;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class StunEffect extends SyncedMobEffect {

    public StunEffect() {
        super(MobEffectCategory.HARMFUL, 0x997209, eff -> ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0));
    }

    @Override
    public boolean applyEffectTick(LivingEntity living, int amplifier) {
        if (!(living instanceof Player player) || !player.getAbilities().invulnerable) {
            living.setDeltaMovement(Vec3.ZERO);
            living.setOnGround(true);
        }
        return super.applyEffectTick(living, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onEffectAdded(LivingEntity entity, MobEffectInstance instance) {
        entity.setDeltaMovement(Vec3.ZERO);
    }
}
