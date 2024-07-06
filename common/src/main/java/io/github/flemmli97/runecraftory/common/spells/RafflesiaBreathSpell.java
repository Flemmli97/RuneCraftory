package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.MobAttackExt;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityStatusBall;
import io.github.flemmli97.runecraftory.common.entities.misc.RafflesiaBreathSummoner;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class RafflesiaBreathSpell extends Spell {

    private final EntityStatusBall.Type type;

    public RafflesiaBreathSpell(EntityStatusBall.Type type) {
        this.type = type;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        RafflesiaBreathSummoner summoner = new RafflesiaBreathSummoner(level, entity, this.type);
        summoner.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.7f));
        Vec3 position = entity.position().add(0, entity.getBbHeight() * 0.5, 0);
        float dirScale = 5;
        Vec3 target = position.add(entity.getLookAngle().scale(dirScale));
        if (entity instanceof Mob mob) {
            Vec3 delayedPos;
            if (mob instanceof MobAttackExt delayed && (delayedPos = delayed.targetPosition(summoner.position())) != null) {
                target = delayedPos;
            } else if (mob.getTarget() != null) {
                target = EntityUtil.getStraightProjectileTarget(position, mob.getTarget());
            }
        }
        summoner.setPos(position.x, position.y, position.z);
        summoner.setTarget(target.x, target.y, target.z);
        level.addFreshEntity(summoner);
        return true;
    }
}