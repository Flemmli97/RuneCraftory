package com.flemmli97.runecraftory.common.entity.starter;

import java.util.HashMap;
import java.util.Map;

import com.flemmli97.runecraftory.api.entities.IRFAttributes;
import com.flemmli97.runecraftory.api.enums.EnumElement;
import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.lib.RFNumbers;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityWooly extends EntityMobBase{

	private Map<Item, Float>	drops = new HashMap<Item, Float>();
	public EntityWooly(World world) {
		this(world, RFNumbers.baseLevel);
	}
	
	public EntityWooly(World world, int level)
	{
		super(world, level, true, 3, 1, false);
		this.setSize(0.6F, 1.3F);
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15*RFNumbers.DAMAGESCALE);;
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.1);
        this.getAttributeMap().getAttributeInstance(IRFAttributes.RFATTACK).setBaseValue(8.0);
        this.getAttributeMap().getAttributeInstance(IRFAttributes.RFDEFENCE).setBaseValue(6.0);
        this.getAttributeMap().getAttributeInstance(IRFAttributes.RFMAGICATT).setBaseValue(5.0);
        this.getAttributeMap().getAttributeInstance(IRFAttributes.RFMAGICDEF).setBaseValue(4.0);
	}

	@Override
	public Item[] tamingItem() {
		return null;//small, normal, woolblock
	}

	@Override
	public float tamingChance() {
		return 0.9F;
	}

	@Override
	public Map<Item, Float> getDrops() {
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
}
