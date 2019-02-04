package com.flemmli97.runecraftory.common.core.handler.quests;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

//GIVE,HARVEST,SHIP,MONSTER;
/**
 * Class implementing this needs to have an empty constructor
 */
public interface IObjective<T> {	

	public void updateProgress(EntityPlayer player, T t);
	
	/**
	 * String for comparing if a trigger matches this goal. E.g. for items: their registryname + meta or something
	 */
	public boolean matches(T t);
	
	public String currentProgress();
	
	public List<ItemStack> rewards();
	
	public int moneyReward();
	
	public void readFromNBT(NBTTagCompound compound);
	
	public NBTTagCompound writeToNBT(NBTTagCompound compound);
	
	public boolean isFinished();
	
	public String objDesc();
	
	public String rewardDesc();
	
	public ObjectiveType type();
	
	@SuppressWarnings("unchecked")
	public default IObjective<T> copy()
	{
		IObjective<T> obj;
		try {
			obj = this.getClass().newInstance();
			obj.readFromNBT(this.writeToNBT(new NBTTagCompound()));
			return obj;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static enum ObjectiveType
	{
		KILL,
		BRING,
		SHIP,
		HARVEST;
	}
}
