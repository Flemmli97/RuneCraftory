package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ColdEffect extends UncurableEffect {

    public ColdEffect() {
        super(MobEffectCategory.HARMFUL, 0x128ab2);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 60 == 0;
    }

    @Override
    public boolean applyEffectTick(LivingEntity living, int amplifier) {
        if (living instanceof Player player) {
            PlayerData data = Platform.INSTANCE.getPlayerData(player);
            int amount = Math.min(data.getRunePoints(), (int) (data.getMaxRunePoints() * 0.05));
            data.useRunePoints(amount, false);
        }
        return super.applyEffectTick(living, amplifier);
    }
}
