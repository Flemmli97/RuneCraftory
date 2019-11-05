package com.flemmli97.runecraftory.common.entity.monster;

import com.flemmli97.runecraftory.common.entity.EntityChargeable;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIGenericCharge;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.world.World;

public class EntityBeetle extends EntityChargeable
{
    private EntityAIGenericCharge ai = new EntityAIGenericCharge(this, 1.0, true, 1.0f);;
    
    public EntityBeetle(World world) {
        super(world);
        this.tasks.addTask(2, this.ai);
    }
    
    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityChargeable.isCharging, false);
    }
    
    @Override
    public float attackChance() {
        return 0.9f;
    }
    
	@Override
	public AnimatedAction[] getAnimations() {
		return AnimatedAction.vanillaAttackOnly;
	}
	
	@Override
	public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
		return true;
	}
    
    @Override
    public boolean isCharging() {
        return this.dataManager.get(EntityChargeable.isCharging);
    }
    
    @Override
    public void setCharging(boolean charge) {
        this.dataManager.set(EntityChargeable.isCharging, charge);
    }

}
