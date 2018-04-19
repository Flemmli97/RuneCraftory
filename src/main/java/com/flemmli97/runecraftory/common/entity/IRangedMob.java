package com.flemmli97.runecraftory.common.entity;

import net.minecraft.entity.EntityLivingBase;

public interface IRangedMob {
	
	public void attackRanged(EntityLivingBase target);
	
	public float strafeChance();

}
