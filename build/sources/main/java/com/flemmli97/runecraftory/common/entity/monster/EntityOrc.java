package com.flemmli97.runecraftory.common.entity.monster;

import java.util.HashMap;
import java.util.Map;

import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.init.ModItems;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityOrc extends EntityMobBase{

	private Map<ItemStack, Float>	drops = new HashMap<ItemStack, Float>();
	
	public EntityOrc(World world)
	{
		super(world, true, 5, 1, false);
		this.drops.put(new ItemStack(ModItems.liquids, 1,0), 0.5F);
		this.drops.put(new ItemStack(ModItems.cloth, 1,0), 0.5F);
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.initiateBaseAttributes(SharedMonsterAttributes.MAX_HEALTH,14);;
		this.initiateBaseAttributes(ItemStats.RFATTACK,10.5);
		this.initiateBaseAttributes(ItemStats.RFDEFENCE,6.5);
		this.initiateBaseAttributes(ItemStats.RFMAGICATT,4.0);
		this.initiateBaseAttributes(ItemStats.RFMAGICDEF,5.0);
	}

	@Override
	public ItemStack[] tamingItem() {
		return new ItemStack[] {new ItemStack(ModItems.cloth, 1,0)};
	}

	@Override
	public float tamingChance() {
		return 0.8F;
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
		return 0.8F;
	}
	@Override
	public int getAttackTimeFromPattern(byte pattern) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int attackFromPattern() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int maxAttackPatterns() {
		// TODO Auto-generated method stub
		return 0;
	}
}
