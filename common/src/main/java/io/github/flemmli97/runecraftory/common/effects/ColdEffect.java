package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ColdEffect extends PermanentEffect {

    public ColdEffect() {
        super(MobEffectCategory.HARMFUL, 0, S2CEntityDataSync.Type.COLD);
        this.setTickDelay(60);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        if (living instanceof Player player)
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                int amount = Math.min(data.getRunePoints(), (int) (data.getMaxRunePoints() * 0.05));
                data.decreaseRunePoints((Player) living, amount, false);
            });
        super.applyEffectTick(living, amplifier);
    }
}
