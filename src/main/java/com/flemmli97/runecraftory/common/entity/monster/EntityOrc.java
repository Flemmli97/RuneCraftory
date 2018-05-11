package com.flemmli97.runecraftory.common.entity.monster;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIGenericMelee;

import net.minecraft.world.World;

public class EntityOrc extends EntityMobBase{

	public EntityAIGenericMelee attack = new EntityAIGenericMelee(this, 1, true, 1);

	public EntityOrc(World world)
	{
		super(world);
		this.tasks.addTask(2, attack);
	}

	/*@Override
	public EnumElement entityElement() {
		return EnumElement.NONE;
	}*/

	@Override
	public float attackChance() {
		return 0.8F;
	}
	@Override
	public int getAttackTimeFromPattern(byte pattern) {
		return 20;
	}

	@Override
	public int attackFromPattern() {
		return 20;
	}

	@Override
	public int maxAttackPatterns() {
		return 1;
	}
}
