package com.flemmli97.runecraftory.common.entities.monster.ai;

import com.flemmli97.runecraftory.common.entities.AnimationType;
import com.flemmli97.runecraftory.common.entities.ChargingMonster;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

public class ChargeAttackGoal<T extends ChargingMonster> extends AnimatedMeleeGoal<T> {

    public ChargeAttackGoal(T entity) {
        super(entity);
    }

    @Override
    public AnimatedAction randomAttack() {
        if (this.attacker.getRNG().nextFloat() < this.attacker.attackChance(AnimationType.GENERICATTACK)) {
            AnimatedAction anim = this.attacker.getRandomAnimation(AnimationType.MELEE);
            if (this.distanceToTargetSq <= this.attacker.maxAttackRange(anim) * 3)
                return anim;
            else if (this.distanceToTargetSq <= (this.attacker.chargingLength() * this.attacker.chargingLength() + 1) && this.attacker.getPosY() >= this.target.getPosY())
                return this.attacker.getRandomAnimation(AnimationType.CHARGE);
        }
        return this.attacker.getRandomAnimation(AnimationType.IDLE);
    }

    @Override
    public void handlePreAttack() {
        if (this.attacker.isAnimOfType(this.next, AnimationType.MELEE))
            super.handlePreAttack();
        else {
            this.attacker.setChargeMotion(this.attacker.getChargeTo(this.next, this.target.getPositionVec()));
            this.attacker.faceEntity(this.target, 360, 10);
            this.attacker.lockYaw(this.attacker.rotationYaw);
            this.movementDone = true;
        }
    }
}
