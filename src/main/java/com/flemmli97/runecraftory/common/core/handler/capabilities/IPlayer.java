package com.flemmli97.runecraftory.common.core.handler.capabilities;

import java.util.List;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.common.core.handler.quests.QuestMission;
import com.flemmli97.runecraftory.common.inventory.InventorySpells;
import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;
import com.flemmli97.runecraftory.common.lib.enums.EnumStatusEffect;

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
	
	public boolean decreaseRunePoints(EntityPlayer player, int amount);
	
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
	
	public float getStr();
	
	public void setStr(EntityPlayer player, float amount);
	
	public float getVit();
	
	public void setVit(EntityPlayer player, float amount);
	
	public float getIntel();
	
	public void setIntel(EntityPlayer player, float amount);
	
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
	
	//Quest
	@Nullable
	public QuestMission currentMission();
	
	public boolean acceptMission(QuestMission quest);
	
	public boolean finishMission(EntityPlayer player);
}
