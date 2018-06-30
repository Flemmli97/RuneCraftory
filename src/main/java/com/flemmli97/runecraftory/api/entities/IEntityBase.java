package com.flemmli97.runecraftory.api.entities;

import java.util.Map;

import net.minecraft.item.ItemStack;

public interface IEntityBase
{
	public int level();
    
	public Map<ItemStack, Float> getDrops();
    
	public int baseXP();
    
	public int baseMoney();
}
