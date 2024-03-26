package io.github.flemmli97.runecraftory.common.entities.ai.boss;

import io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia.EntityRafflesiaPart;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class RafflesiaPartAttackGoal extends Goal {

    protected final EntityRafflesiaPart attacker;
    protected LivingEntity target;
    protected int idleTime;

    public RafflesiaPartAttackGoal(EntityRafflesiaPart entity) {
        this.attacker = entity;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.idleTime = this.attacker.getRandom().nextInt(120) + 20;
    }

    @Override
    public boolean canUse() {
        LivingEntity living = this.attacker.getTarget();
        return living != null && living.isAlive() && this.attacker.isWithinRestriction(living.blockPosition());
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void stop() {
        this.target = null;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.attacker.getTarget() == null)
            return;
        AnimatedAction anim = this.attacker.getAnimationHandler().getAnimation();
        if (anim != null) {
            return;
        }
        if (this.idleTime <= 0) {
            this.idleTime = this.attacker.cooldown();
            this.attacker.getAnimationHandler().setAnimation(this.attacker.attackAnim());
        } else {
            this.idleTime--;
        }
    }
}