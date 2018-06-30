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
    
    public boolean shouldExecute() {
        return EntityUtils.isEntityDisabled((EntityLivingBase)this.entity);
    }
    
    public boolean shouldContinueExecuting() {
        return EntityUtils.isEntityDisabled((EntityLivingBase)this.entity);
    }
    
    public void resetTask() {
    }
    
    public boolean isInterruptible() {
        return false;
    }
}
