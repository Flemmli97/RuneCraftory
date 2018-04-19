package com.flemmli97.runecraftory.api.entities;

import java.util.List;

import com.flemmli97.runecraftory.common.lib.enums.EnumStatusEffect;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IEntityAdvanced extends IEntityBase{
	
	public ItemStack[] tamingItem();
	
	public float tamingChance();
	
	public boolean isTamed();
	
	public boolean ridable();

	public List<EnumStatusEffect> getActiveStatus();
	
	public void addStatus(EnumStatusEffect status);
	
	public void clearEffect();
	
	public void cureEffect(EnumStatusEffect status);
	
	public EntityPlayer getOwner();
	
	public void setOwner(EntityPlayer player);
	
	public boolean isFlyingEntity();
	
	public float attackChance();
	
	//friendship value TODO

}
