package com.flemmli97.runecraftory.common.entity;

import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;

public interface IChargeAttack {

    public static final DataParameter<Boolean> isCharging = EntityDataManager.<Boolean>createKey(EntityMobBase.class, DataSerializers.BOOLEAN);

	public boolean isCharging();
	
	public void setCharging(boolean charge);
}
