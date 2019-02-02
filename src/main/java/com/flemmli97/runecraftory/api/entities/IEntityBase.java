package com.flemmli97.runecraftory.api.entities;

import java.util.Map;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IEntityBase
{
	public int level();
    
	public Map<ItemStack, Float> getDrops();
    
	public int baseXP();
    
	public int baseMoney();
	
	public void applyFoodEffect(ItemStack stack);
	
	@SideOnly(Side.CLIENT)
	public void applyFoodEffect(Map<IAttribute, Integer> stats, Map<IAttribute, Float> multi, int duration);
	
	public void removeFoodEffect();
}
