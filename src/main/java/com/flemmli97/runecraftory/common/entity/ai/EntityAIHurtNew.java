package com.flemmli97.runecraftory.common.entity.ai;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;

import javax.annotation.Nullable;

public class EntityAIHurtNew extends EntityAIHurtByTarget {

    private EntityLivingBase target;
    private Predicate<EntityLivingBase> pred;

    public EntityAIHurtNew(EntityMobBase mob, Predicate<EntityLivingBase> pred)
    {
        super(mob, false);
        this.pred=pred;
    }

    @Override
    protected boolean isSuitableTarget(@Nullable EntityLivingBase target, boolean includeInvincibles)
    {
        return (this.taskOwner.getAttackTarget()==null || this.taskOwner.getAttackTarget()!=target) &&
                (this.pred==null || this.pred.apply(target)) && super.isSuitableTarget(target, includeInvincibles);
    }
}
