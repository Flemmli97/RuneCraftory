package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.DelayedAttacker;
import io.github.flemmli97.runecraftory.common.entities.misc.WindBladeBarrageSummoner;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class WindBladeBarrageSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        WindBladeBarrageSummoner summoner = new WindBladeBarrageSummoner(level, entity);
        summoner.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.75f));
        Vec3 look = new Vec3(entity.getLookAngle().x, 0, entity.getLookAngle().z).normalize().scale(entity.getBbWidth() * 0.8);
        Vec3 pos = entity.position().add(look.x, entity.getBbHeight() * 0.7, look.z);
        float dirScale = 5;
        Vec3 target = pos.add(entity.getLookAngle().scale(dirScale));
        if (entity instanceof Mob mob) {
            Vec3 delayedPos;
            if (mob instanceof DelayedAttacker delayed && (delayedPos = delayed.targetPosition(summoner.position())) != null) {
                target = delayedPos;
            } else if (mob.getTarget() != null) {
                target = EntityUtil.getStraightProjectileTarget(pos, mob.getTarget());
            }
        }
        summoner.setPos(pos.x, pos.y, pos.z);
        summoner.setTarget(target.x, target.y, target.z);
        level.addFreshEntity(summoner);
        return true;
    }
}