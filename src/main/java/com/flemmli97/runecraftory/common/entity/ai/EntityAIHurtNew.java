package com.flemmli97.runecraftory.common.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;

public class EntityAIHurtNew extends EntityAITarget {

    public EntityAIHurtNew(EntityCreature creature)
    {
        super(creature, false);
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if(this.taskOwner.getAttackTarget()==null && this.taskOwner.getRevengeTarget()!=null)
            return this.checkTarget(this.taskOwner.getRevengeTarget());
        return false;
    }

    private boolean isNotSameTarget()
    {
        if(this.taskOwner.getAttackTarget()==null)
            return true;
        return !this.taskOwner.getRevengeTarget().equals(this.taskOwner.getAttackTarget());
    }
    @Override
    public void startExecuting()
    {
        if(this.isNotSameTarget())
            this.taskOwner.setAttackTarget(this.taskOwner.getRevengeTarget());
        super.startExecuting();
    }

    protected boolean checkTarget(EntityLivingBase livingBase)
    {

        return super.isSuitableTarget(livingBase, false);
    }

}
