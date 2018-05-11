package com.flemmli97.runecraftory.common.entity.monster;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.IChargeAttack;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIGenericCharge;

import net.minecraft.world.World;

public class EntityBeetle extends EntityMobBase implements IChargeAttack{

	private EntityAIGenericCharge<EntityBeetle> ai = new EntityAIGenericCharge<EntityBeetle>(this, 1, true, 1);
	public EntityBeetle(World world)
	{
		super(world);
		this.tasks.addTask(2, ai);
	}
	
	@Override
	public void entityInit()
	{
		super.entityInit();
		this.dataManager.register(IChargeAttack.isCharging, false);
	}

	/*@Override
	public EnumElement entityElement() {
		return EnumElement.NONE;
	}*/

	@Override
	public float attackChance() {
		return 0.9F;
	}
	@Override
	public int getAttackTimeFromPattern(byte pattern) {
		return 20;
	}

	@Override
	public int attackFromPattern() {
		return 18;
	}

	@Override
	public int maxAttackPatterns() {
		return 1;
	}

	@Override
	public boolean isCharging() {
		return this.dataManager.get(IChargeAttack.isCharging);
	}

	@Override
	public void setCharging(boolean charge) {
		this.dataManager.set(IChargeAttack.isCharging, charge);
	}
}
