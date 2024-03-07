package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityStatusBall;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SleepBallSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        for (int i = 0; i < 4; ++i) {
            double angle = i / 4.0 * Math.PI * 2.0 + Math.toRadians(entity.getYRot());
            double x = Math.cos(angle) * 1.3;
            double z = Math.sin(angle) * 1.3;
            EntityStatusBall pollen = new EntityStatusBall(level, entity);
            pollen.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.65f));
            pollen.setPos(entity.getX() + x, entity.getY() + 0.4, entity.getZ() + z);
            level.addFreshEntity(pollen);
        }
        playSound(entity, ModSounds.SPELL_GENERIC_POOF.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }
}
