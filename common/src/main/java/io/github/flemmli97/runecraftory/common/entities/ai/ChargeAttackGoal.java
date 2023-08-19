package io.github.flemmli97.runecraftory.common.entities.ai;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.phys.AABB;

public class ChargeAttackGoal<T extends ChargingMonster> extends AnimatedMeleeGoal<T> {

    public ChargeAttackGoal(T entity) {
        super(entity);
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
            if (this.distanceToTargetSq <= (this.attacker.chargingLength() * this.attacker.chargingLength() + 1) && heightDiff <= 1 && heightDiff >= -this.attacker.getMaxFallDistance())
                return this.attacker.getRandomAnimation(AnimationType.CHARGE);
        }
        return this.attacker.getRandomAnimation(AnimationType.IDLE);
    }

    @Override
    public void handlePreAttack() {
        if (this.attacker.isAnimOfType(this.next, AnimationType.MELEE))
            super.handlePreAttack();
        else {
            this.attacker.setChargeMotion(this.attacker.getChargeTo(this.next, this.target.position()));
            this.attacker.lookAt(this.target, 360, 10);
            this.attacker.lockYaw(this.attacker.getYRot());
            this.movementDone = true;
        }
    }
}
