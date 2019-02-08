package com.flemmli97.runecraftory.common.entity.ai;

import com.flemmli97.runecraftory.common.utils.EntityUtils;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIDisable extends EntityAIBase
{
    EntityLiving entity;
    
    public EntityAIDisable(EntityLiving entity) {
        this.setMutexBits(7);
        this.entity = entity;
    }
    
    @Override
	public boolean shouldExecute() {
        return EntityUtils.isEntityDisabled((EntityLivingBase)this.entity);
    }
    
    @Override
	public boolean shouldContinueExecuting() {
        return EntityUtils.isEntityDisabled((EntityLivingBase)this.entity);
    }
    
    @Override
	public void resetTask() {
    }
    
    @Override
	public boolean isInterruptible() {
        return false;
    }
}
