package com.flemmli97.runecraftory.common.entity.ai;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.IChargeAttack;

public class EntityAIGenericCharge<T extends EntityMobBase & IChargeAttack> extends EntityAIGenericMelee{

	public EntityAIGenericCharge(T creature, double speedIn, boolean useLongMemory, float rangeModifier) {
		super(creature, speedIn, useLongMemory, rangeModifier);
	}

}
