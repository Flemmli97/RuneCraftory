package com.flemmli97.runecraftory.common.entity.monster;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIGenericMelee;

import net.minecraft.world.World;

public class EntityAnt extends EntityMobBase{

	public EntityAIGenericMelee attack = new EntityAIGenericMelee(this, 1, true, 1);

	public EntityAnt(World world) {
		super(world);
		this.setSize(0.6F, 0.45F);
		this.tasks.addTask(2, attack);
	}

	/*@Override
	public EnumElement entityElement() {
		return EnumElement.NONE;
	}*/

	@Override
	public float attackChance() {
		return 0.6F;
	}

	@Override
	public int getAttackTimeFromPattern(byte pattern) {
		return 10;
	}

	@Override
	public int attackFromPattern() {
		return 7;
	}

	@Override
	public int maxAttackPatterns() {
		return 1;
	}
}
