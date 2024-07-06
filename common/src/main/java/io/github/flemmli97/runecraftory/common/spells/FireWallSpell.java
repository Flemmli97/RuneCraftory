package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.MobAttackExt;
import io.github.flemmli97.runecraftory.common.entities.misc.FireWallSummoner;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class FireWallSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        FireWallSummoner wall = new FireWallSummoner(level, entity);
        Vec3 lookDir = new Vec3(entity.getLookAngle().x, 0, entity.getLookAngle().z).normalize().scale(entity.getBbWidth() * 0.8);
        wall.setPos(wall.position().add(lookDir).add(0, -entity.getBbHeight() * 0.2, 0));
        Vec3 delayedPos;
        if (entity instanceof MobAttackExt attacker && (delayedPos = attacker.targetPosition(wall.position())) != null) {
            wall.setTarget(delayedPos.x(), delayedPos.y(), delayedPos.z());
        } else if (entity instanceof Mob mob && mob.getTarget() != null) {
            wall.setTarget(mob.getTarget().position().x, mob.getTarget().position().y, mob.getTarget().position().z);
        } else {
            Vec3 look = entity.getLookAngle().scale(10);
            wall.setTarget(look.x, look.y, look.z);
        }
        wall.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.9f));
        level.addFreshEntity(wall);
        playSound(entity, ModSounds.SPELL_GENERIC_FIRE_BALL.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }
}
