package com.flemmli97.runecraftory.api.entities;

import java.util.Map;

import com.flemmli97.runecraftory.api.enums.EnumElement;

import net.minecraft.item.Item;

public interface IEntityBase {
	
	public int level();
				
	public Map<Item, Float> getDrops();
		
	public int baseXP();
	
	public int baseMoney();
	
	public EnumElement entityElement();
}
