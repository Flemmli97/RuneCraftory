package com.flemmli97.runecraftory.common.entity.ai;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.EntityMobBase.AnimationType;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.google.common.base.Predicate;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class EntityAIChargeAttackBase<T extends EntityMobBase> extends EntityAIMeleeBase<T> {

    private int attackCooldown;
    private Predicate<EntityLivingBase> pred;
    
    public EntityAIChargeAttackBase(T entity, float rangeModifier) {
        super(entity, rangeModifier);
        this.pred = (e) -> {
            boolean flag = false;
            if(e !=entity)
                if (entity.isTamed()) {
                    flag = IMob.VISIBLE_MOB_SELECTOR.apply(e);
                } else if (e instanceof EntityVillager || e instanceof EntityPlayer)
                    flag = true;
            return flag && entity.isWithinHomeDistanceFromPosition(new BlockPos(e)) && EntityAITarget.isSuitableTarget(entity, e, false, false);
        };
    }

    @Override
    public AnimatedAction randomAttack() {
        if (this.attacker.getRNG().nextFloat() < this.attacker.attackChance())
            if(this.distanceToTargetSq <= this.getAttackReachSqr(this.target, this.rangeModifier))
                return this.attacker.getRandomAnimation(AnimationType.MELEE);
            else
                return this.attacker.getRandomAnimation(AnimationType.CHARGE);
        return this.attacker.getRandomAnimation(AnimationType.IDLE);
    }  
    
    @Override
    public void handlePreAttack() {
        if(this.attacker.isAnimOfType(this.next, AnimationType.MELEE))
            super.handlePreAttack();
        else {
            this.attacker.getNavigator().tryMoveToXYZ(this.target.posX, this.target.posY, this.target.posZ, 1.5);
            this.movementDone=true;
        }
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (this.attacker.isAnimOfType(anim, AnimationType.CHARGE)) {
            if(--this.attackCooldown<=0) {
                this.attacker.world.getEntitiesWithinAABB(EntityLivingBase.class, this.attacker.getEntityBoundingBox().grow(this.rangeModifier).offset(this.attacker.getLook(1).scale(this.rangeModifier*0.5)), this.pred).forEach(e->this.attacker.attackEntityAsMob(e));
                this.attackCooldown=10;
            }
            if (this.attacker.getNavigator().noPath()) {
                this.attacker.setAnimation(null);
                this.attackCooldown=0;
            }
        }
        else
            super.handleAttack(anim);
    }
}
