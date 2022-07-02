package io.github.flemmli97.runecraftory.common.entities.monster.ai;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityGhost;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.phys.AABB;

public class GhostAttackGoal<T extends EntityGhost> extends AnimatedMeleeGoal<T> {

    public GhostAttackGoal(T entity) {
        super(entity);
    }

    @Override
    public AnimatedAction randomAttack() {
        if (this.attacker.shouldVanishNext(this.prevAnim) || this.distanceToTargetSq > 100)
            return EntityGhost.vanish;
        if (this.distanceToTargetSq >= 25)
            return EntityGhost.darkBall;
        if (this.attacker.getRandom().nextFloat() < 0.7f && this.distanceToTargetSq <= (this.attacker.chargingLength() * this.attacker.chargingLength() + 1) && this.attacker.getY() >= this.target.getY())
            return this.attacker.getRandomAnimation(AnimationType.CHARGE);
        AnimatedAction anim = this.attacker.getRandomAnimation(AnimationType.MELEE);
        AABB aabb = this.attacker.calculateAttackAABB(anim, this.target, 1);
        if (aabb.intersects(this.target.getBoundingBox()))
            return anim;
        return this.attacker.getRandomAnimation(AnimationType.IDLE);
    }

    @Override
    public void handlePreAttack() {
        if (this.attacker.isAnimOfType(this.next, AnimationType.MELEE))
            super.handlePreAttack();
        else if (this.attacker.isAnimOfType(this.next, AnimationType.RANGED)) {
            this.attacker.getLookControl().setLookAt(this.target, 360, 90);
            this.movementDone = true;
        } else {
            this.attacker.setChargeMotion(this.attacker.getChargeTo(this.next, this.target.position()));
            this.attacker.lookAt(this.target, 360, 10);
            this.attacker.lockYaw(this.attacker.getYRot());
            this.movementDone = true;
        }
    }

    @Override
    public void handleIddle() {
        if (this.attacker.getY() < this.target.getY())
            this.attacker.setDeltaMovement(this.attacker.getDeltaMovement().add(0, 0.03, 0));
        super.handleIddle();
    }
}

