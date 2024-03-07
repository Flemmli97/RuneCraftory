package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityAmbrosiaWave;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class WaveSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityAmbrosiaWave wave = new EntityAmbrosiaWave(level, entity, 40);
        wave.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.33f));
        wave.setPos(wave.getX(), wave.getY() + 0.2, wave.getZ());
        level.addFreshEntity(wave);
        return true;
    }
}
