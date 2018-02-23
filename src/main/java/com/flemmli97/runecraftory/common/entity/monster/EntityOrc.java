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

public class EntityOrc extends EntityMobBase{

	private Map<ItemStack, Float>	drops = new HashMap<ItemStack, Float>();
	public EntityOrc(World world) {
		this(world, CalculationConstants.baseLevel);
	}
	
	public EntityOrc(World world, int level)
	{
		super(world, level, true, 5, 1, false);
		this.drops.put(new ItemStack(ModItems.liquids, 1,0), 0.5F);
		this.drops.put(new ItemStack(ModItems.cloth, 1,0), 0.5F);
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(14*CalculationConstants.DAMAGESCALE);;
        this.getAttributeMap().getAttributeInstance(ItemStats.RFATTACK).setBaseValue(10.5);
        this.getAttributeMap().getAttributeInstance(ItemStats.RFDEFENCE).setBaseValue(6.5);
        this.getAttributeMap().getAttributeInstance(ItemStats.RFMAGICATT).setBaseValue(4.0);
        this.getAttributeMap().getAttributeInstance(ItemStats.RFMAGICDEF).setBaseValue(5.0);
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

	@Override
	public EnumElement entityElement() {
		return EnumElement.NONE;
	}

	@Override
	public float attackChance() {
		return 90;
	}
}
