package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class PoisonEffect extends PermanentEffect {

    public PoisonEffect() {
        super(MobEffectCategory.HARMFUL, 0, S2CEntityDataSync.Type.POISON);
        this.setTickDelay(60);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        if (living instanceof Player player) {
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                float amount = data.getMaxHealth(player) * 0.05f;
                amount = ((data.getHealth(player) - amount <= 0.0f) ? (data.getHealth(player) - 1.0f) : amount);
                player.hurt(CustomDamage.EXHAUST, amount);
            });
        }
        super.applyEffectTick(living, amplifier);
    }
}
