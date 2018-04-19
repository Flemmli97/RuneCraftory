package com.flemmli97.runecraftory.common.entity.monster;

import java.util.HashMap;
import java.util.Map;

import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIGenericMelee;
import com.flemmli97.runecraftory.common.init.ModItems;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityAnt extends EntityMobBase{

	private Map<ItemStack, Float>drops = new HashMap<ItemStack, Float>();
	public EntityAIGenericMelee attack = new EntityAIGenericMelee(this, 1, true, 1);

	public EntityAnt(World world) {
		super(world, false, 4, 2, false);
		this.setSize(0.6F, 0.45F);
		this.drops.put(new ItemStack(ModItems.cloth, 1,7), 0.4F);
		this.tasks.addTask(0, attack);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.initiateBaseAttributes(SharedMonsterAttributes.MAX_HEALTH,16);;
		this.initiateBaseAttributes(ItemStats.RFATTACK,7.5);
		this.initiateBaseAttributes(ItemStats.RFDEFENCE,5.5);
		this.initiateBaseAttributes(ItemStats.RFMAGICATT,5.0);
		this.initiateBaseAttributes(ItemStats.RFMAGICDEF,4.0);
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
