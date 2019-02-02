package com.flemmli97.runecraftory.api.entities;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface IEntityAdvanced extends IEntityBase
{
    public NonNullList<ItemStack> tamingItem();
    
    public  Map<ItemStack, Integer> dailyDrops();
    
    public float tamingChance();
    
    public boolean isTamed();
    
    public boolean ridable();
    
    public EntityPlayer getOwner();
    
    public void setOwner(EntityPlayer player);
    
    public boolean isFlyingEntity();
    
    public float attackChance();
}
