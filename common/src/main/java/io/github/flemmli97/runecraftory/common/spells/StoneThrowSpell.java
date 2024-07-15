package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityStone;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public class StoneThrowSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityStone stone = new EntityStone(level, entity);
        stone.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.95f));
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            stone.shootAtEntity(mob.getTarget(), 1.3f, 7 - level.getDifficulty().getId() * 2);
        } else {
            stone.shootFromRotation(entity, entity.getXRot() + 5, entity.getYRot(), 0.0F, 1.5F, 1.0F);
        }
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.FISHING_BOBBER_THROW, entity.getSoundSource(), 1.0F, 1.0F / (entity.getRandom().nextFloat() * 0.4F + 0.8F));
        level.addFreshEntity(stone);
        return true;
    }
}
