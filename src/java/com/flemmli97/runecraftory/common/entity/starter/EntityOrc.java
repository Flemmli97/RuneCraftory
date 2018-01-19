package com.flemmli97.runecraftory.common.entity.starter;

import java.util.HashMap;
import java.util.Map;

import com.flemmli97.runecraftory.api.enums.EnumElement;
import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.lib.RFNumbers;

import net.minecraft.item.Item;
import net.minecraft.world.World;

public class EntityOrc extends EntityMobBase{

	private Map<Item, Float>	drops = new HashMap<Item, Float>();
	public EntityOrc(World world) {
		this(world, RFNumbers.baseLevel);
	}
	
	public EntityOrc(World world, int level)
	{
		super(world, level, true, 5, 1, false);
		//init stats
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
	}

	@Override
	public Item[] tamingItem() {
		return null;//cheap cloth
	}

	@Override
	public float tamingChance() {
		return 0.8F;
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
