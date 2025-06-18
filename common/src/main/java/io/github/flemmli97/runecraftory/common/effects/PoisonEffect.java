package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import io.github.flemmli97.runecraftory.common.utils.DamageSourceUtils;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class PoisonEffect extends UncurableEffect {

    public PoisonEffect() {
        super(MobEffectCategory.HARMFUL, 0, S2CEntityDataSync.DataType.POISON);
    }

    @Override
    public boolean applyEffectTick(LivingEntity living, int amplifier) {
        float amount = living.getMaxHealth() * 0.05f;
        amount = ((living.getHealth() - amount < 1) ? (living.getHealth() - 1) : amount);
        if (living.getType().is(RunecraftoryTags.EntityTypes.BOSSES))
            amount *= 0.25;
        if (amount > 0)
            living.hurt(DamageSourceUtils.poison(living.level()), amount);
        return super.applyEffectTick(living, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 60 == 0;
    }
}
