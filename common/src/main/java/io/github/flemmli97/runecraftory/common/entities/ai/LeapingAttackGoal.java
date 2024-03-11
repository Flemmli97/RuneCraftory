package io.github.flemmli97.runecraftory.common.entities.ai;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.LeapingMonster;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class LeapingAttackGoal<T extends LeapingMonster> extends AnimatedMonsterAttackGoal<T> {

    private final double meleeDistSq;
    private final boolean keepDistance, stayIfClose;
    private int preDelay = -1;

    public LeapingAttackGoal(T entity) {
        this(entity, false, 2);
    }

    public LeapingAttackGoal(T entity, boolean keepDistance, double meleeDist) {
        this(entity, keepDistance, meleeDist, false);
    }

    public LeapingAttackGoal(T entity, boolean keepDistance, double meleeDist, boolean stayIfClose) {
        super(entity);
        this.keepDistance = keepDistance;
        this.meleeDistSq = meleeDist * meleeDist;
        this.stayIfClose = stayIfClose;
    }

    @Override
    public AnimatedAction randomAttack() {
        if (this.attacker.getRandom().nextFloat() < this.attacker.attackChance(AnimationType.GENERICATTACK)) {
            if (this.attacker.getRandom().nextFloat() < this.attacker.attackChance(AnimationType.MELEE)) {
                AnimatedAction anim = this.attacker.getRandomAnimation(AnimationType.MELEE);
                AABB aabb = this.attacker.attackCheckAABB(anim, this.target, -0.3);
                if (aabb.intersects(this.target.getBoundingBox()))
                    return anim;
            }
            double heightDiff = this.target.getY() - this.attacker.getY();
            if (this.distanceToTargetSq <= (this.attacker.maxLeapDistance() * this.attacker.maxLeapDistance() + 1) && heightDiff <= 1 && heightDiff >= 0)
                return this.attacker.getRandomAnimation(AnimationType.LEAP);
        }
        return this.attacker.getRandomAnimation(AnimationType.IDLE);
    }

    @Override
    public void handlePreAttack() {
        if (this.attacker.isAnimOfType(this.next, AnimationType.MELEE))
            super.handlePreAttack();
        else {
            if (this.preDelay == -1 && this.keepDistance && this.distanceToTargetSq <= this.meleeDistSq) {
                Vec3 away = DefaultRandomPos.getPosAway(this.attacker, 5, 4, this.target.position());
                if (away != null) {
                    this.moveToWithDelay(away.x, away.y, away.z, 1.2);
                    this.preDelay = this.attacker.getRandom().nextInt(10) + 10;
                }
            }
            if (--this.preDelay <= 0) {
                this.attacker.lookAt(this.target, 360, 10);
                this.movementDone = true;
                this.preDelay = -1;
            }
        }
    }

    @Override
    public void handleIdle() {
        if (this.keepDistance && this.distanceToTargetSq <= this.meleeDistSq) {
            if (this.stayIfClose) {
                this.attacker.getLookControl().setLookAt(this.target, 30, 30);
                this.attacker.getNavigation().stop();
                return;
            }
            if (this.attacker.getNavigation().isDone()) {
                Vec3 away = DefaultRandomPos.getPosAway(this.attacker, 5, 4, this.target.position());
                if (away != null) {
                    this.moveToWithDelay(away.x, away.y, away.z, 1.2);
                    return;
                }
            }
        }
        super.handleIdle();
    }
}