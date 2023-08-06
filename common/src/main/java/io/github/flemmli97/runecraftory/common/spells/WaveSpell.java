package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityAmbrosiaWave;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class WaveSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this.rpCost()))
            return false;
        EntityAmbrosiaWave wave = new EntityAmbrosiaWave(level, entity, 40);
        wave.setDamageMultiplier(0.25f + lvl * 0.05f);
        wave.setPos(wave.getX(), wave.getY() + 0.2, wave.getZ());
        level.addFreshEntity(wave);
        return true;
    }
}
