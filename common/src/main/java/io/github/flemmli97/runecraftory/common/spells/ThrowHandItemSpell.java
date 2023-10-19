package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityThrownItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public class ThrowHandItemSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityThrownItem item = new EntityThrownItem(level, entity);
        item.setDamageMultiplier(0.9f + lvl * 0.05f);
        item.setItem(entity.getMainHandItem());
        item.setRotating(true);
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            item.shootAtEntity(mob.getTarget(), 1.2f, 7 - level.getDifficulty().getId() * 2, 0.2f);
        } else {
            item.shootFromRotation(entity, entity.getXRot() + 5, entity.getYRot(), 0.0F, 1.2f, 1.0F);
        }
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.FISHING_BOBBER_THROW, entity.getSoundSource(), 1.0F, 1.0F / (entity.getRandom().nextFloat() * 0.4F + 0.8F));
        level.addFreshEntity(item);
        return true;
    }
}
