package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityStatusBall;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public class PoisonBallSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityStatusBall ball = new EntityStatusBall(level, entity);
        ball.setType(EntityStatusBall.Type.MUSHROOM_POISON);
        ball.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.8f));
        ball.setPos(entity.getX(), entity.getY() + 0.4, entity.getZ());
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            ball.shootAtEntity(mob.getTarget(), 0.1f, 7 - level.getDifficulty().getId() * 2, 0.1f);
        } else {
            ball.shootFromRotation(entity, entity.getXRot() + 5, entity.getYRot(), 0.0F, 0.1f, 1.0F);
        }
        level.addFreshEntity(ball);
        return true;
    }
}
