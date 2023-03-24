package io.github.flemmli97.runecraftory.common.entities.ai;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class EvadingRangedAttackGoal<T extends BaseMonster> extends AnimatedMeleeGoal<T> {

    private final float reach, reachSq, meleeDistSq;
    private final Predicate<T> canRanged;
    private boolean moveTo;
    private boolean canSee;
    private Vec3 posAway;

    public EvadingRangedAttackGoal(T entity, float meleeDist, float rangedReach, Predicate<T> canRangedAttack) {
        super(entity);
        this.meleeDistSq = meleeDist * meleeDist;
        this.reach = rangedReach;
        this.reachSq = rangedReach * rangedReach;
        this.canRanged = canRangedAttack;
    }

    @Override
    public AnimatedAction randomAttack() {
        if (this.attacker.getRandom().nextFloat() < this.attacker.attackChance(AnimationType.GENERICATTACK)) {
            if (this.attacker.getRandom().nextFloat() < this.attacker.attackChance(AnimationType.MELEE)) {
                AnimatedAction anim = this.attacker.getRandomAnimation(AnimationType.MELEE);
                AABB aabb = this.attacker.calculateAttackAABB(anim, this.target, 1);
                if (aabb.intersects(this.target.getBoundingBox()))
                    return anim;
            }
            if (this.distanceToTargetSq <= this.reachSq && this.canRanged.test(this.attacker))
                return this.attacker.getRandomAnimation(AnimationType.RANGED);
        }
        return this.attacker.getRandomAnimation(AnimationType.IDLE);
    }

    @Override
    public void handlePreAttack() {
        if (this.attacker.isAnimOfType(this.next, AnimationType.MELEE))
            super.handlePreAttack();
        else {
            if (this.distanceToTargetSq > this.reachSq || !this.canSee) {
                this.moveTo = true;
                this.attacker.getNavigation().moveTo(this.target, 1);
            } else if (this.moveTo) {
                this.attacker.getNavigation().stop();
                this.moveTo = false;
            } else {
                this.movementDone = true;
            }
        }
    }

    @Override
    public void handleIddle() {
        if (this.iddleMoveDelay <= 0) {
            if (this.distanceToTargetSq <= this.meleeDistSq) {
                this.iddleMoveFlag = 2;
                this.iddleMoveDelay = this.attacker.getRandom().nextInt(35) + 55 - this.iddleMoveFlag * 10;
                this.posAway = DefaultRandomPos.getPosAway(this.attacker, 7, 4, this.attacker.position());
            } else {
                this.iddleMoveFlag = this.attacker.getRandom().nextInt(3) == 0 ? 1 : 0;
                this.iddleMoveDelay = this.attacker.getRandom().nextInt(20) + 20;
            }
        }
        switch (this.iddleMoveFlag) {
            case 1 -> this.moveRandomlyAround(36);
            case 2 -> {
                if (this.posAway != null)
                    this.moveToWithDelay(this.posAway.x, this.posAway.y, this.posAway.z, 1.1);
            }
        }
    }

    @Override
    public void setupValues() {
        super.setupValues();
        this.canSee = this.attacker.getSensing().hasLineOfSight(this.target);
    }
}
