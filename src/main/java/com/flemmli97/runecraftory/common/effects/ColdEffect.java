package com.flemmli97.runecraftory.common.effects;

import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectType;

public class ColdEffect extends PermanentEffect {

    public ColdEffect() {
        super(EffectType.HARMFUL, 0);
        this.setTickDelay(60);
    }

    @Override
    public void performEffect(LivingEntity living, int amplifier) {
        if (living instanceof PlayerEntity)
            living.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> {
                int amount = Math.min(cap.getRunePoints(), (int) (cap.getMaxRunePoints() * 0.05));
                cap.decreaseRunePoints((PlayerEntity) living, amount, false);
            });
        super.performEffect(living, amplifier);
    }
}
