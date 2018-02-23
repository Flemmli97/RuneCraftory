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

public class EntityAnt extends EntityMobBase{

	private Map<ItemStack, Float>drops = new HashMap<ItemStack, Float>();

	public EntityAnt(World world) {
		this(world, CalculationConstants.baseLevel);
	}
	
	public EntityAnt(World world, int level) {
		super(world, level, false, 4, 2, false);
		this.setSize(0.6F, 0.45F);
		this.drops.put(new ItemStack(ModItems.cloth, 1,7), 0.4F);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(16*CalculationConstants.DAMAGESCALE);;
        this.getAttributeMap().getAttributeInstance(ItemStats.RFATTACK).setBaseValue(7.5);
        this.getAttributeMap().getAttributeInstance(ItemStats.RFDEFENCE).setBaseValue(5.5);
        this.getAttributeMap().getAttributeInstance(ItemStats.RFMAGICATT).setBaseValue(5.0);
        this.getAttributeMap().getAttributeInstance(ItemStats.RFMAGICDEF).setBaseValue(4.0);
	}
	
	@Override
	public ItemStack[] tamingItem() {
		return new ItemStack[] {new ItemStack(ModItems.cloth, 1,7)};
	}

	@Override
	public float tamingChance() {
		return 0.75F;
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
		return 0.6F;
	}

}
