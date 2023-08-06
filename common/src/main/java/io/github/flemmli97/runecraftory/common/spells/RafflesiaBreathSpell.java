package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.entities.DelayedAttacker;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityStatusBall;
import io.github.flemmli97.runecraftory.common.entities.misc.RafflesiaBreathSummoner;
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
        if (!Spell.tryUseWithCost(entity, stack, this.rpCost()))
            return false;
        RafflesiaBreathSummoner summoner = new RafflesiaBreathSummoner(level, entity, this.type);
        Vec3 position = entity.position().add(0, entity.getBbHeight() * 0.5, 0);
        float dirScale = 5;
        Vec3 dir = entity.getLookAngle().scale(dirScale);
        if (entity instanceof Mob mob) {
            Vec3 delayedPos;
            if (mob instanceof DelayedAttacker delayed && (delayedPos = delayed.targetPosition(summoner.position())) != null) {
                dir = delayedPos.subtract(position.x, delayedPos.y, position.z).normalize().scale(dirScale);
            } else if (mob.getTarget() != null) {
                dir = EntityUtil.getStraightProjectileTarget(position, mob.getTarget()).subtract(position).normalize().scale(dirScale);
            }
        }
        summoner.setPos(position.x, position.y, position.z);
        summoner.setTarget(position.x + dir.x, position.y + dir.y, position.z + dir.z);
        level.addFreshEntity(summoner);
        return true;
    }
}