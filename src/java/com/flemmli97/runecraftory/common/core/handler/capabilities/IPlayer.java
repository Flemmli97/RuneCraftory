package com.flemmli97.runecraftory.common.core.handler.capabilities;

import java.util.List;

import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.api.enums.EnumStatusEffect;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public interface IPlayer{
		
	public float getHealth();
	
	public void setHealth(EntityPlayer player, float amount);
	
	public void regenHealth(EntityPlayer player, float amount);
	
	public void damage(EntityPlayer player,DamageSource source, float amount);
	
	public float getMaxHealth();
	
	public void setMaxHealth(EntityPlayer player, float amount);
	
	public int getRunePoints();
	
	public int getMaxRunePoints();
	
	public void decreaseRunePoints(EntityPlayer player, int amount);
	
	public void refreshRunePoints(EntityPlayer player);

	public void setRunePoints(EntityPlayer player, int amount);
	
	public void setMaxRunePoints(EntityPlayer player, int amount);
			
	public int getMoney();
	
	public boolean useMoney(EntityPlayer player, int amount);

	public void setMoney(EntityPlayer player, int amount);

	public int[] getPlayerLevel();
	
	public void addXp(EntityPlayer player, int amount);
	
	public void setPlayerLevel(EntityPlayer player, int level, int xpAmount);
	
	//=====Player stats
	
	public int getStr();
	
	public void setStr(EntityPlayer player, int amount);
	
	public int getVit();
	
	public void setVit(EntityPlayer player, int amount);
	
	public int getIntel();
	
	public void setIntel(EntityPlayer player, int amount);
	
	//=====Skills
	
	public int[] getSkillLevel(EnumSkills skill);
	
	public void setSkillLevel(EnumSkills skill, EntityPlayer player, int level, int percent);
	
	public void increaseSkill(EnumSkills skill, EntityPlayer player, int percent);
	
	//=====NBT 
	
	public void readFromNBT(NBTTagCompound nbt);
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt, boolean forDeath);
	
	//=====Inventory
	
	public InventorySpells getInv();
	
	//=====Status
	
	public List<EnumStatusEffect> getActiveStatus();
	
	public void addStatus(EntityPlayer player, EnumStatusEffect status);
	
	public void clearEffect(EntityPlayer player);
	
	public void cureEffect(EntityPlayer player, EnumStatusEffect status);
	
}
