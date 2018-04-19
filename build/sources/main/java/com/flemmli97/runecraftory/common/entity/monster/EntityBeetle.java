package com.flemmli97.runecraftory.common.entity.monster;

import java.util.HashMap;
import java.util.Map;

import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.IChargeAttack;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIGenericCharge;
import com.flemmli97.runecraftory.common.init.ModItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityBeetle extends EntityMobBase implements IChargeAttack{

	private Map<ItemStack, Float>	drops = new HashMap<ItemStack, Float>();
	private EntityAIGenericCharge<EntityBeetle> ai = new EntityAIGenericCharge<EntityBeetle>(this, 1, true, 1);
	public EntityBeetle(World world)
	{
		super(world, true, 5, 1, false);
		this.tasks.addTask(0, ai);
		this.drops.put(new ItemStack(ModItems.sticks, 1,2), 0.6F);
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.initiateBaseAttributes(SharedMonsterAttributes.MAX_HEALTH,15);;
		this.initiateBaseAttributes(ItemStats.RFATTACK,9.5);
		this.initiateBaseAttributes(ItemStats.RFDEFENCE,7.0);
		this.initiateBaseAttributes(ItemStats.RFMAGICATT,3.0);
		this.initiateBaseAttributes(ItemStats.RFMAGICDEF,6.0);
	}

	@Override
	public ItemStack[] tamingItem() {
		return new ItemStack[] {new ItemStack(ModItems.sticks, 1,0)};
	}

	@Override
	public float tamingChance() {
		return 0.65F;
	}

	@Override
	public Map<ItemStack, Float> getDrops() {
		return this.drops;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCharging(boolean charge) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void chargeAttack(EntityLivingBase target) {
		// TODO Auto-generated method stub
		
	}
}
