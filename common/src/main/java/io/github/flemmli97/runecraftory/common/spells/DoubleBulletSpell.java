package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityBullet;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public class DoubleBulletSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityBullet bullet = new EntityBullet(level, entity);
        bullet.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.8f));
        EntityBullet bullet2 = new EntityBullet(level, entity);
        bullet2.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.8f));
        bullet2.reverseMovement();
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            bullet.shootAtEntity(mob.getTarget(), 0.3f, 7 - level.getDifficulty().getId() * 2, 0);
            bullet2.shootAtEntity(mob.getTarget(), 0.3f, 7 - level.getDifficulty().getId() * 2, 0);
        } else {
            bullet.shootFromRotation(entity, entity.getXRot() + 5, entity.getYRot(), 0.0F, 0.3f, 1.0F);
            bullet2.shootFromRotation(entity, entity.getXRot() + 5, entity.getYRot(), 0.0F, 0.3f, 1.0F);
        }
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.FISHING_BOBBER_THROW, entity.getSoundSource(), 1.0F, 1.0F / (entity.getRandom().nextFloat() * 0.4F + 0.8F));
        level.addFreshEntity(bullet);
        level.addFreshEntity(bullet2);
        return true;
    }
}
