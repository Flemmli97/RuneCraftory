package com.flemmli97.runecraftory.common.entity.monster;

import java.util.HashMap;
import java.util.Map;

import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.CalculationConstants;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class EntityWooly extends EntityMobBase{

	private Map<ItemStack, Float>	drops = new HashMap<ItemStack, Float>();
	public EntityWooly(World world) {
		this(world, CalculationConstants.baseLevel);
	}
	
	public EntityWooly(World world, int level)
	{
		super(world, level, true, 3, 1, false);
		this.setSize(0.6F, 1.3F);
		this.drops.put(new ItemStack(ModItems.furs, 1,1), 0.5F);
		this.drops.put(new ItemStack(Items.SHEARS), 0.15F);
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(18*CalculationConstants.DAMAGESCALE);;
        this.getAttributeMap().getAttributeInstance(ItemStats.RFATTACK).setBaseValue(6.5);
        this.getAttributeMap().getAttributeInstance(ItemStats.RFDEFENCE).setBaseValue(6.0);
        this.getAttributeMap().getAttributeInstance(ItemStats.RFMAGICATT).setBaseValue(5.0);
        this.getAttributeMap().getAttributeInstance(ItemStats.RFMAGICDEF).setBaseValue(4.0);
	}

	@Override
	public ItemStack[] tamingItem() {
		return new ItemStack[] {new ItemStack(ModItems.furs, 1,0),new ItemStack(ModItems.furs, 1,0),
				new ItemStack(ModItems.furs, 1,2),new ItemStack(ModItems.furs, 1,5), new ItemStack(Blocks.WOOL, 1,OreDictionary.WILDCARD_VALUE)};
	}

	@Override
	public float tamingChance() {
		return 0.9F;
	}

	@Override
	public Map<ItemStack, Float> getDrops() {
		return this.drops;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
		return SoundEvents.ENTITY_SHEEP_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SHEEP_DEATH;
	}

	@Override
	protected float getSoundVolume() {
		return 0.65F;
	}

	@Override
	protected float getSoundPitch() {
		return 1.7F;
	}

	@Override
	public EnumElement entityElement() {
		return EnumElement.NONE;
	}

	@Override
	public float attackChance() {
		return 0;
	}	
}
