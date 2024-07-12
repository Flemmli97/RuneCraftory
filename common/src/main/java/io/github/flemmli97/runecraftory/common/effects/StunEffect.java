package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class StunEffect extends MobEffect {

    public StunEffect() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    private static void apply(LivingEntity entity, boolean flag) {
        Platform.INSTANCE.getEntityData(entity).ifPresent(data -> data.setStunned(entity, flag));
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        if (!(living instanceof Player player) || !player.getAbilities().invulnerable) {
            living.setDeltaMovement(Vec3.ZERO);
            living.setOnGround(true);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        apply(entity, false);
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        entity.setDeltaMovement(Vec3.ZERO);
        apply(entity, true);
        super.addAttributeModifiers(entity, attributeMap, amplifier);
    }
}
