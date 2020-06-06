package com.flemmli97.runecraftory.common.entity.ai;

import com.flemmli97.runecraftory.common.entity.EntityChargingMobBase;
import com.flemmli97.runecraftory.common.entity.EntityMobBase.AnimationType;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class EntityAIChargeAttackBase<T extends EntityChargingMobBase> extends EntityAIMeleeBase<T> {

    private Predicate<EntityLivingBase> pred;

    public EntityAIChargeAttackBase(T entity) {
        super(entity);
    }

    @Override
    public AnimatedAction randomAttack() {
        if (this.attacker.getRNG().nextFloat() < this.attacker.attackChance()) {
            AnimatedAction anim = this.attacker.getRandomAnimation(AnimationType.MELEE);
            if (this.distanceToTargetSq <= this.attacker.maxAttackRange(anim)*3)
                return anim;
            else if(this.distanceToTargetSq <= (this.attacker.chargingLength()*this.attacker.chargingLength()+1))
                return this.attacker.getRandomAnimation(AnimationType.CHARGE);
        }
        return this.attacker.getRandomAnimation(AnimationType.IDLE);
    }

    @Override
    public void handlePreAttack() {
        if(this.attacker.isAnimOfType(this.next, AnimationType.MELEE))
            super.handlePreAttack();
        else {
            int length = this.next.getLength();
            Vec3d vec = this.target.getPositionVector().subtract(this.attacker.getPositionVector()).normalize().scale(this.attacker.chargingLength());
            this.attacker.setChargeMotion(new double[] {vec.x/length, this.attacker.posY, vec.z/length});
            this.movementDone=true;
        }
    }
}
