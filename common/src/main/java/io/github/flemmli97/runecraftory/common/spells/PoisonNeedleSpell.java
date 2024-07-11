package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityPoisonNeedle;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public class PoisonNeedleSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityPoisonNeedle projectile = new EntityPoisonNeedle(level, entity);
        projectile.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.8f));
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            projectile.shootAtEntity(mob.getTarget(), 1.2f, 4 - level.getDifficulty().getId(), 0);
        } else {
            projectile.shootFromRotation(entity, entity.getXRot() + 5, entity.getYRot(), 0.0F, 1.2F, 0.4F);
        }
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARROW_SHOOT, entity.getSoundSource(), 1.0F, 1.0F / (entity.getRandom().nextFloat() * 0.4F + 0.8F));
        level.addFreshEntity(projectile);
        return true;
    }
}
