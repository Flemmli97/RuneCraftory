package com.flemmli97.runecraftory.common.entity.monster;

import java.util.HashMap;
import java.util.Map;

import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.CalculationConstants;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityBeetle extends EntityMobBase{

	private Map<ItemStack, Float>	drops = new HashMap<ItemStack, Float>();
	public EntityBeetle(World world) {
		this(world, CalculationConstants.baseLevel);
	}
	
	public EntityBeetle(World world, int level)
	{
		super(world, level, true, 5, 1, false);
		this.drops.put(new ItemStack(ModItems.sticks, 1,2), 0.6F);
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15*CalculationConstants.DAMAGESCALE);;
        this.getAttributeMap().getAttributeInstance(ItemStats.RFATTACK).setBaseValue(9.5);
        this.getAttributeMap().getAttributeInstance(ItemStats.RFDEFENCE).setBaseValue(7.0);
        this.getAttributeMap().getAttributeInstance(ItemStats.RFMAGICATT).setBaseValue(3.0);
        this.getAttributeMap().getAttributeInstance(ItemStats.RFMAGICDEF).setBaseValue(6.0);
	}

	@Override
	public ItemStack[] tamingItem() {
		return new ItemStack[] {new ItemStack(ModItems.cloth, 1,0)};
	}

	@Override
	public float tamingChance() {
		return 0.65F;
	}

	@Override
	public Map<ItemStack, Float> getDrops() {
		return this.drops;
	}

	@Override
	public EnumElement entityElement() {
		return EnumElement.NONE;
	}

	@Override
	public float attackChance() {
		return 90;
	}
}
