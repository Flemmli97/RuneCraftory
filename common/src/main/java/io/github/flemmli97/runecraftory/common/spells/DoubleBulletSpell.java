package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.BulletEntity;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class DoubleBulletSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        BulletEntity bullet = new BulletEntity(level, entity);
        bullet.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, this));
        BulletEntity bullet2 = new BulletEntity(level, entity);
        bullet2.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, this));
        bullet2.reverseMovement();
        ProjectileUtils.shoot(entity, bullet, 0.3f, entity instanceof Player ? 1 : 7 - level.getDifficulty().getId() * 2);
        ProjectileUtils.shoot(entity, bullet2, 0.3f, entity instanceof Player ? 1 : 7 - level.getDifficulty().getId() * 2);
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.FISHING_BOBBER_THROW, entity.getSoundSource(), 1.0F, 1.0F / (entity.getRandom().nextFloat() * 0.4F + 0.8F));
        level.addFreshEntity(bullet);
        level.addFreshEntity(bullet2);
        return true;
    }
}
