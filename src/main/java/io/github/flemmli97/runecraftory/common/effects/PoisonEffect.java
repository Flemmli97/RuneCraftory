package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.common.capability.CapabilityInsts;
import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectType;

public class PoisonEffect extends PermanentEffect {

    public PoisonEffect() {
        super(EffectType.HARMFUL, 0, S2CEntityDataSync.Type.POISON);
        this.setTickDelay(60);
    }

    @Override
    public void performEffect(LivingEntity living, int amplifier) {
        if (living instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) living;
            living.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> {
                float amount = cap.getMaxHealth(player) * 0.05f;
                amount = ((cap.getHealth(player) - amount <= 0.0f) ? (cap.getHealth(player) - 1.0f) : amount);
                player.attackEntityFrom(CustomDamage.EXHAUST, amount);
            });
        }
        super.performEffect(living, amplifier);
    }
}
