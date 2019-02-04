package com.flemmli97.runecraftory.common.core.handler.capabilities;

import java.util.Map;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.client.render.ArmPosePlus;
import com.flemmli97.runecraftory.common.core.handler.quests.QuestMission;
import com.flemmli97.runecraftory.common.inventory.InventoryShippingBin;
import com.flemmli97.runecraftory.common.inventory.InventorySpells;
import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;

public interface IPlayer{
		
	public float getHealth(EntityPlayer player);
	
	public void setHealth(EntityPlayer player, float amount);
	
	public void regenHealth(EntityPlayer player, float amount);
		
	public float getMaxHealth(EntityPlayer player);
	
	public void setMaxHealth(EntityPlayer player, float amount);
	
	public int getRunePoints();
	
	public int getMaxRunePoints();
	
	public boolean decreaseRunePoints(EntityPlayer player, int amount);
	
	public void refreshRunePoints(EntityPlayer player, int amount);

	public void setRunePoints(EntityPlayer player, int amount);
	
	public void setMaxRunePoints(EntityPlayer player, int amount);
			
	public int getMoney();
	
	public boolean useMoney(EntityPlayer player, int amount);

	public void setMoney(EntityPlayer player, int amount);

	/**
	 * int[0] is the player level, int[1] is the experience gained for the current level
	 */
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
	
	public void applyFoodEffect(EntityPlayer player, Map<IAttribute, Integer> gain, Map<IAttribute, Float> gainMulti, int duration);
	
	public void removeFoodEffect(EntityPlayer player);

	public Map<IAttribute, Integer> foodEffects();
	
	public void updateEquipmentStats(EntityPlayer player, EntityEquipmentSlot slot);
	
	public int getAttributeValue(IAttribute att);

	//=====Skills
	
	public int[] getSkillLevel(EnumSkills skill);
	
	public void setSkillLevel(EnumSkills skill, EntityPlayer player, int level, int xp);
	
	public void increaseSkill(EnumSkills skill, EntityPlayer player, int xp);
	
	//=====NBT 
	
	/**
	 * @param player Used during death
	 */
	public void readFromNBT(NBTTagCompound nbt, EntityPlayer player);
	
	/**
	 * @param player Used during death
	 */
	public NBTTagCompound writeToNBT(NBTTagCompound nbt, EntityPlayer player);
	
	//=====Inventory
	
	public InventorySpells getInv();
	
	public InventoryShippingBin getShippingInv();
	
	//Quest
	@Nullable
	public QuestMission currentMission();
	
	public boolean acceptMission(QuestMission quest);
	
	public boolean finishMission(EntityPlayer player);
	
	//Weapon and ticker
	
	public int animationTick();
	
	public void startAnimation(int tick);
	
	public void update(EntityPlayer player);
	
	public boolean canUseSpear();
	
	public void startSpear();
	
	public int getSpearTick();
	
	public void disableOffHand();
	
	public boolean canUseOffHand();
		
	public EnumHand getPrevSwung();
	
	public void setPrevSwung(EnumHand hand);
	
	public void startWeaponSwing(WeaponSwing swing, int delay);
	
	public boolean isAtUltimate();
	
	public ArmPosePlus currentArmPose();
	
	public void setArmPose(ArmPosePlus armPose);

	public boolean startGlove(EntityPlayer player);

	public enum WeaponSwing
	{
		SHORT(5),
		LONG(5),
		SPEAR(5),
		HAXE(5),
		DUAL(5),
		GLOVE(5);
		
		private int swingAmount;
		WeaponSwing(int swingAmount)
		{
			this.swingAmount=swingAmount;
		}
		
		public int getMaxSwing()
		{
			return this.swingAmount;
		}
	}
}
