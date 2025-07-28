package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.common.entities.misc.PowerWaveEntity;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttackActions;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class PowerWaveSpell extends WeaponSpell {

    public PowerWaveSpell() {
        super(RuneCraftoryAttackActions.POWER_WAVE, RunecraftoryTags.Items.SHORTSWORDS);
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        boolean result = super.use(level, entity, stack, rpUseMultiplier, amount, lvl);
        if (result) {
            PowerWaveEntity wave = new PowerWaveEntity(level, entity);
            wave.setPos(wave.getX(), entity.getY() + entity.getBbHeight() * 0.1, wave.getZ());
            wave.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, this));
            ProjectileUtils.shoot(entity, wave, 0.9f, 0);
            level.addFreshEntity(wave);
            entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), RuneCraftorySounds.PLAYER_ATTACK_SWOOSH.get(), entity.getSoundSource(), 1.4f, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.5f);
        }
        return result;
    }
}
