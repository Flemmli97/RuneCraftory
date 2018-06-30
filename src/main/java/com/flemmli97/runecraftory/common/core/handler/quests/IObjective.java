package com.flemmli97.runecraftory.common.core.handler.quests;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

//GIVE,HARVEST,SHIP,MONSTER;
/**
 * Class implementing this needs to have an empty constructor
 */
public interface IObjective {	

	public void updateProgress(EntityPlayer player);
	
	/**
	 * String for comparing if a trigger matches this goal. E.g. for items: their registryname + meta or something
	 */
	public String objGoalID();
	
	public int currentProgress();
	
	public List<ItemStack> rewards();
	
	public int moneyReward();
	
	public void readFromNBT(NBTTagCompound compound);
	
	public NBTTagCompound writeToNBT(NBTTagCompound compound);
	
	public boolean isFinished();
	
	public String objDesc();
}
