package com.flemmli97.runecraftory.common.entity.monster.boss;

import java.util.HashMap;
import java.util.Map;

import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.common.entity.EntityBossBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIAmbrosia;
import com.flemmli97.runecraftory.common.entity.magic.EntityButterfly;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.CalculationConstants;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityAmbrosia extends EntityBossBase{

	private Map<ItemStack, Float>drops = new HashMap<ItemStack, Float>();

	public EntityAmbrosia(World world) {
		this(world, CalculationConstants.baseLevel);
		//this.tasks.removeTask(this.attackAI);
	}
	
	public EntityAmbrosia(World world, int level) {
		super(world, level, true, 10, 12, true);
		//this.tasks.removeTask(this.attackAI);
		this.tasks.addTask(1, new EntityAIAmbrosia(this, true, 1, 10, 5, 16));
		this.drops.put(new ItemStack(ModItems.strings, 1,2),0.5F);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(16*CalculationConstants.DAMAGESCALE);;
        this.getAttributeMap().getAttributeInstance(ItemStats.RFATTACK).setBaseValue(13.5);
        this.getAttributeMap().getAttributeInstance(ItemStats.RFDEFENCE).setBaseValue(9.5);
        this.getAttributeMap().getAttributeInstance(ItemStats.RFMAGICATT).setBaseValue(12.0);
        this.getAttributeMap().getAttributeInstance(ItemStats.RFMAGICDEF).setBaseValue(8.0);
	}
	
	@Override
	public ItemStack[] tamingItem() {
		// big toyherb
		return null;
	}

	@Override
	public float tamingChance() {
		return 0.45F;
	}

	@Override
	public float attackChance() {
		return 100;
	}

	@Override
	public Map<ItemStack, Float> getDrops() {
		return drops;
	}

	@Override
	public EnumElement entityElement() {
		return EnumElement.EARTH;
	}

	@Override
	public void doRangedAttack(EntityLivingBase target) {
		for(int i = 0; i < 10; i++)
		{
			if(!this.world.isRemote)
			{
				EntityButterfly fly = new EntityButterfly(this.world, this);
				fly.shoot(this, this.rotationPitch, this.rotationYaw, 0, 1, 3);
				this.world.spawnEntity(fly);
			}
		}
	}

}
