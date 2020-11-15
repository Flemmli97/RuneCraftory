package com.flemmli97.runecraftory.mobs.entity.monster.ai;

import com.flemmli97.runecraftory.mobs.entity.AnimationType;
import com.flemmli97.runecraftory.mobs.entity.BaseMonster;
import com.flemmli97.runecraftory.mobs.entity.ChargingMonster;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.util.math.vector.Vector3d;

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
            else if (this.distanceToTargetSq <= (this.attacker.chargingLength() * this.attacker.chargingLength() + 1) && this.attacker.getY() >= this.target.getY())
                return this.attacker.getRandomAnimation(AnimationType.CHARGE);
        }
        return this.attacker.getRandomAnimation(AnimationType.IDLE);
    }

    @Override
    public void handlePreAttack() {
        if (this.attacker.isAnimOfType(this.next, AnimationType.MELEE))
            super.handlePreAttack();
        else {
            int length = this.next.getLength();
            Vector3d vec = this.target.getPositionVec().subtract(this.attacker.getPositionVec()).normalize().scale(this.attacker.chargingLength());
            this.attacker.setChargeMotion(new double[]{vec.x / length, this.attacker.getY(), vec.z / length});
            this.movementDone = true;
        }
    }
}
