package com.flemmli97.runecraftory.common.entity.starter;

import java.util.HashMap;
import java.util.Map;

import com.flemmli97.runecraftory.api.enums.EnumElement;
import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.lib.RFNumbers;

import net.minecraft.item.Item;
import net.minecraft.world.World;

public class EntityAnt extends EntityMobBase{

	private Map<Item, Float>	drops = new HashMap<Item, Float>();

	public EntityAnt(World world) {
		this(world, RFNumbers.baseLevel);
	}
	
	public EntityAnt(World world, int level) {
		super(world, level, false, 4, 2, false);
	}

	@Override
	public Item[] tamingItem() {
		return null;
	}

	@Override
	public float tamingChance() {
		return 0.85F;
	}

	@Override
	public Map<Item, Float> getDrops() {
		return this.drops;
	}

	@Override
	public EnumElement entityElement() {
		return EnumElement.NONE;
	}

}
