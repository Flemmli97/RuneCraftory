package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.common.entities.SleepingEntity;
import io.github.flemmli97.runecraftory.mixinhelper.MobNoAIHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SleepEffect extends MobEffect {

    public SleepEffect() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    private static void applySleep(LivingEntity entity, boolean flag) {
        Platform.INSTANCE.getEntityData(entity).ifPresent(data -> data.setSleeping(entity, flag));
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        if (!(living instanceof Player player) || !player.getAbilities().invulnerable) {
            if (!living.noPhysics)
                living.setDeltaMovement(new Vec3(living.getDeltaMovement().x, -0.08, living.getDeltaMovement().z));
            // Setting no ai prevents mobs moving/getting moved. So we do it here
            if (living instanceof MobNoAIHandler mob) {
                mob.setIgnoreNoAI();
                living.travel(new Vec3(living.xxa, living.yya, living.zza));
            }
        }
        MobEffectInstance eff = living.getEffect(this);
        if (eff.getDuration() > 200) {
            living.removeEffect(this);
            living.addEffect(new MobEffectInstance(this, 200, 0, true, false));
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        if (entity instanceof SleepingEntity sleeping)
            sleeping.setSleeping(false);
        applySleep(entity, false);
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        if (entity instanceof SleepingEntity sleeping)
            sleeping.setSleeping(true);
        entity.setDeltaMovement(Vec3.ZERO);
        applySleep(entity, true);
        super.addAttributeModifiers(entity, attributeMap, amplifier);
    }
}
