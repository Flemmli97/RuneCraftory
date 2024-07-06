package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.MobAttackExt;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityButterflySummoner;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ButterflySpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityButterflySummoner summoner = new EntityButterflySummoner(level, entity);
        summoner.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.25f));
        Vec3 delayedPos;
        if (entity instanceof MobAttackExt attacker && (delayedPos = attacker.targetPosition(summoner.position())) != null) {
            summoner.setTarget(delayedPos.x(), delayedPos.y(), delayedPos.z());
        } else if (entity instanceof Mob mob && mob.getTarget() != null) {
            Vec3 target = EntityUtil.getStraightProjectileTarget(summoner.position(), mob.getTarget());
            summoner.setTarget(target.x(), target.y(), target.z());
        } else {
            Vec3 look = Vec3.directionFromRotation(Mth.clamp(entity.getXRot(), -10, 10), entity.getYRot()).scale(5);
            summoner.setTarget(entity.getX() + look.x(), entity.getEyeY() + look.y(), entity.getZ() + look.z());
        }
        level.addFreshEntity(summoner);
        return true;
    }
}
