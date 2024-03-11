package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class PoisonEffect extends PermanentEffect {

    public PoisonEffect() {
        super(MobEffectCategory.HARMFUL, 0, S2CEntityDataSync.Type.POISON);
        this.setTickDelay(60);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        float amount = living.getMaxHealth() * 0.05f;
        amount = ((living.getHealth() - amount < 1) ? (living.getHealth() - 1) : amount);
        if (living.getType().is(RunecraftoryTags.BOSSES))
            amount *= 0.25;
        if (amount > 0)
            living.hurt(CustomDamage.POISON, amount);
        super.applyEffectTick(living, amplifier);
    }
}
