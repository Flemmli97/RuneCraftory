package io.github.flemmli97.runecraftory.common.entities.ai;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.LeapingMonster;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class LeapingAttackGoal<T extends LeapingMonster> extends AnimatedMeleeGoal<T> {

    private final double meleeDistSq;
    private final boolean keepDistance;
    private int preDelay = -1;

    public LeapingAttackGoal(T entity) {
        this(entity, false, 2);
    }

    public LeapingAttackGoal(T entity, boolean keepDistance, double meleeDist) {
        super(entity);
        this.keepDistance = keepDistance;
        this.meleeDistSq = meleeDist * meleeDist;
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
            if (this.distanceToTargetSq <= (this.attacker.maxLeapDistance() * this.attacker.maxLeapDistance() + 1) && this.attacker.getY() >= this.target.getY())
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
    public void handleIddle() {
        if (this.keepDistance && this.attacker.getNavigation().isDone() && this.distanceToTargetSq <= this.meleeDistSq) {
            Vec3 away = DefaultRandomPos.getPosAway(this.attacker, 5, 4, this.target.position());
            if (away != null) {
                this.moveToWithDelay(away.x, away.y, away.z, 1.2);
                return;
            }
        }
        super.handleIddle();
    }
}