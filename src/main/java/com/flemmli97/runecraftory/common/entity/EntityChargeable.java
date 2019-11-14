package com.flemmli97.runecraftory.common.entity;

import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public abstract class EntityChargeable extends EntityMobBase{

    public static final DataParameter<Boolean> isCharging = EntityDataManager.createKey(EntityChargeable.class, DataSerializers.BOOLEAN);

	public EntityChargeable(World world) {
		super(world);
	}
	
	@Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityChargeable.isCharging, false);
    }
    
    public boolean isCharging()
    {
    	return this.dataManager.get(isCharging);
    }
    
    public void setCharging(boolean charge)
    {
    	this.dataManager.set(isCharging, charge);
    }
        
}
