package com.flemmli97.runecraftory.api.entities;

import java.util.Map;

import com.flemmli97.runecraftory.common.lib.enums.EnumElement;

import net.minecraft.item.ItemStack;

public interface IEntityBase {
	
	public int level();
				
	public Map<ItemStack, Float> getDrops();
		
	public int baseXP();
	
	public int baseMoney();
	
	public EnumElement entityElement();
}
