package com.flemmli97.runecraftory.common.core.handler.capabilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.api.enums.EnumStatusEffect;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.core.network.PacketHandler;
import com.flemmli97.runecraftory.common.core.network.PacketHealth;
import com.flemmli97.runecraftory.common.core.network.PacketMaxHealth;
import com.flemmli97.runecraftory.common.core.network.PacketMaxRunePoints;
import com.flemmli97.runecraftory.common.core.network.PacketMoney;
import com.flemmli97.runecraftory.common.core.network.PacketPlayerLevel;
import com.flemmli97.runecraftory.common.core.network.PacketPlayerStats;
import com.flemmli97.runecraftory.common.core.network.PacketRunePoints;
import com.flemmli97.runecraftory.common.core.network.PacketSkills;
import com.flemmli97.runecraftory.common.core.network.PacketStatus;
import com.flemmli97.runecraftory.common.lib.RFCalculations;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class PlayerCap implements IPlayer  {

	//max runpoints possible: 2883
	private int money=0;
	private int runePointsMax=30;
	private float healthMax=25;
	private float health=healthMax;
	private int runePoints = 30;
	private int str = 5;
	private int vit = 5;
	private int intel = 5;
	/** first number is level, second is the xp a.k.a. percent to next level*/
	private int[] level = new int[] {1,0};
	private Map<EnumSkills, int[]> skillMap = new HashMap<EnumSkills, int[]>();
	private InventorySpells spells;
	private List<EnumStatusEffect> activeEffects = new ArrayList<EnumStatusEffect>();

	public PlayerCap()
	{
		for(EnumSkills skill: EnumSkills.values())
		{
			this.skillMap.put(skill, new int[] {0,0});
			spells = new InventorySpells();
		}
	}
	@Override
	public float getHealth()
	{
		return this.health;
	}
	@Override
	public void setHealth(EntityPlayer player, float amount) 
	{
		if(amount>this.healthMax)
			this.health=this.healthMax;
		else
			this.health=amount;
		if(!player.world.isRemote && player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new PacketHealth(this), (EntityPlayerMP) player);
		}
	}
	@Override
	public void regenHealth(EntityPlayer player, float amount){
		if(this.health+amount>this.healthMax)
			this.health=this.healthMax;
		else
			this.health+=amount;
		if(!player.world.isRemote && player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new PacketHealth(this), (EntityPlayerMP) player);
		}
	}
	
	@Override
	public void damage(EntityPlayer player,DamageSource source, float amount)
	{
		this.health-=amount;
		player.getCombatTracker().trackDamage(source, this.health, amount);
		if(this.health<0 && !player.world.isRemote)
		{
            player.setHealth(0);
		}
		if(!player.world.isRemote && player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new PacketHealth(this), (EntityPlayerMP) player);
		}
	}
	
	@Override
	public float getMaxHealth()
	{
		return this.healthMax;
	}
	
	@Override
	public void setMaxHealth(EntityPlayer player, float amount)
	{
		this.healthMax=amount;
		if(!player.world.isRemote && player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new PacketMaxHealth(this), (EntityPlayerMP) player);
		}
	}
	
	@Override
	public int getRunePoints() {
		return this.runePoints;
	}
	
	@Override
	public int getMaxRunePoints()
	{
		return this.runePointsMax;
	}

	@Override
	public void decreaseRunePoints(EntityPlayer player, int amount) {
		if(!player.capabilities.isCreativeMode)
		{
			if(this.runePoints>=amount)
				this.runePoints-=amount;
			else
			{
				int diff =  amount - this.runePoints;
				this.runePoints=0;
				if(!player.world.isRemote && player instanceof EntityPlayerMP)
					player.attackEntityFrom(CustomDamage.EXHAUST, diff*2);
			}
			if(!player.world.isRemote && player instanceof EntityPlayerMP)
			{
				PacketHandler.sendTo(new PacketRunePoints(this), (EntityPlayerMP) player);
			}
		}
	}
	
	@Override
	public void refreshRunePoints(EntityPlayer player)
	{
		this.runePoints=this.runePointsMax;
		if(!player.world.isRemote && player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new PacketRunePoints(this), (EntityPlayerMP) player);
		}
	}

	@Override
	public void setRunePoints(EntityPlayer player, int amount) {
		this.runePoints = amount;
	}
	
	@Override
	public void setMaxRunePoints(EntityPlayer player, int amount)
	{
		this.runePointsMax = amount;
		if(!player.world.isRemote && player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new PacketMaxRunePoints(this), (EntityPlayerMP) player);
		}
	}

	@Override
	public int getMoney() {
		return this.money;
	}

	@Override
	public boolean useMoney(EntityPlayer player, int amount) {
		if(this.money>=amount)
		{
			this.money-=amount;
			if(!player.world.isRemote && player instanceof EntityPlayerMP)
			{
				PacketHandler.sendTo(new PacketMoney(this), (EntityPlayerMP) player);
			}
			return true;
		}
		return false;
	}

	@Override
	public void setMoney(EntityPlayer player, int amount) {
		this.money = amount;
		if(!player.world.isRemote && player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new PacketMoney(this), (EntityPlayerMP) player);
		}
	}

	@Override
	public int[] getPlayerLevel() {
		return this.level;
	}
	
	@Override
	public void addXp(EntityPlayer player, int amount)
	{
		if(!player.capabilities.isCreativeMode)
		{
			int neededXP = RFCalculations.xpAmountForLevelUp(this.level[0]);
			if(amount >= neededXP)
			{
				int diff = neededXP-amount;
				this.level[0]+=1;
				this.addXp(player, diff);
			}
			else
			{
				this.level[1]+=amount;
			}
			if(!player.world.isRemote && player instanceof EntityPlayerMP)
			{
				PacketHandler.sendTo(new PacketPlayerLevel(this), (EntityPlayerMP) player);
			}
		}
	}

	@Override
	public void setPlayerLevel(EntityPlayer player, int level, int xpAmount) {
		this.level[0] = level;
		this.level[1] = xpAmount;
		if(!player.world.isRemote && player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new PacketPlayerLevel(this), (EntityPlayerMP) player);
		}
	}

	@Override
	public int getStr() {
		return this.str;
	}

	@Override
	public void setStr(EntityPlayer player, int amount) {
		this.str+=amount;
		if(!player.world.isRemote && player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new PacketPlayerStats(this), (EntityPlayerMP) player);
		}
	}

	@Override
	public int getVit() {
		return this.vit;
	}

	@Override
	public void setVit(EntityPlayer player, int amount) {
		this.vit+=amount;
		if(!player.world.isRemote && player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new PacketPlayerStats(this), (EntityPlayerMP) player);
		}
	}

	@Override
	public int getIntel() {
		return this.intel;
	}

	@Override
	public void setIntel(EntityPlayer player, int amount) {
		this.intel+=amount;
		if(!player.world.isRemote && player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new PacketPlayerStats(this), (EntityPlayerMP) player);
		}
	}

	@Override
	public int[] getSkillLevel(EnumSkills skill) {
		return this.skillMap.get(skill);
	}

	@Override
	public void setSkillLevel(EnumSkills skill, EntityPlayer player, int level, int percent) {
		this.skillMap.get(skill)[0] = level;
		this.skillMap.get(skill)[1] = percent;
		if(!player.world.isRemote && player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new PacketSkills(this, skill), (EntityPlayerMP) player);
		}
	}
	
	@Override
	public void increaseSkill(EnumSkills skill, EntityPlayer player, int percent)
	{
		if(player.capabilities.isCreativeMode)
		{
			int neededXP = RFCalculations.xpAmountForSkills(this.skillMap.get(skill)[0]);
			if(percent >= neededXP)
			{
				int diff = neededXP-percent;
				this.skillMap.get(skill)[0]+=1;
				this.addXp(player, diff);
			}
			else
			{
				this.skillMap.get(skill)[1]+=percent;
			}
			if(!player.world.isRemote && player instanceof EntityPlayerMP)
			{
				PacketHandler.sendTo(new PacketSkills(this, skill), (EntityPlayerMP) player);
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.runePointsMax = nbt.getInteger("maxRunePoints");
		if(nbt.hasKey("runePoints"))
			this.runePoints = nbt.getInteger("runePoints");
		else
			this.runePoints=this.runePointsMax;		
		this.health=nbt.getFloat("health");
		this.healthMax=nbt.getFloat("healthMax");
		this.money = nbt.getInteger("money");
		this.str = nbt.getInteger("strength");
		this.vit = nbt.getInteger("vitality");
		this.intel = nbt.getInteger("intelligence");
		this.level = nbt.getIntArray("level");
		NBTTagCompound compound = (NBTTagCompound) nbt.getTag("skills");
		for(EnumSkills skill: EnumSkills.values())
		{
			this.skillMap.put(skill, compound.getIntArray(skill.getIdentifier()));
		}
		NBTTagCompound compound2 = (NBTTagCompound) nbt.getTag("effects");
		//if(compound2 !=null && compound2.getKeySet()!=null)
		for(String effectNames : compound2.getKeySet())
		{
			this.activeEffects.add(EnumStatusEffect.fromName(compound.getString(effectNames)));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt, boolean forDeath) {
		if(!forDeath)
		{
			nbt.setFloat("health", health);
			nbt.setInteger("runePoints", this.runePoints);
		}
		else
			nbt.setFloat("health", healthMax/2.0F);
		nbt.setInteger("maxRunePoints", runePointsMax);
		nbt.setFloat("healthMax", healthMax);
		nbt.setInteger("money", this.money);
		nbt.setInteger("strength", this.str);
		nbt.setInteger("vitality", this.vit);
		nbt.setInteger("intelligence", this.intel);
		nbt.setIntArray("level", this.level);
		NBTTagCompound compound = new NBTTagCompound(); 
		for(EnumSkills skill: EnumSkills.values())
		{
			compound.setIntArray(skill.getIdentifier(), this.skillMap.get(skill));
		}
		nbt.setTag("skills", compound);
		NBTTagCompound compound2 = new NBTTagCompound(); 
		for(EnumStatusEffect effect : this.activeEffects)
		{
			compound2.setString(effect.getName(), effect.getName());
		}
		nbt.setTag("effects", compound2);
		return nbt;
	}
	
	@Override
	public InventorySpells getInv()
	{
		return this.spells;
	}
	@Override
	public List<EnumStatusEffect> getActiveStatus()
	{
		return this.activeEffects;
	}
	@Override
	public void addStatus(EntityPlayer player, EnumStatusEffect status)
	{
		if(!this.activeEffects.contains(status))
		{
			this.activeEffects.add(status);
			status.addAttributeModifier(player);
		}
		if(!player.world.isRemote && player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new PacketStatus(this), (EntityPlayerMP) player);
		}
	}
	@Override
	public void clearEffect(EntityPlayer player)
	{
		for(EnumStatusEffect effect : this.activeEffects)
		{
			effect.removeAttributeModifier(player);
		}
		this.activeEffects.clear();
		if(!player.world.isRemote && player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new PacketStatus(this), (EntityPlayerMP) player);
		}
	}
	@Override
	public void cureEffect(EntityPlayer player, EnumStatusEffect status) {
		this.activeEffects.remove(status);
		status.removeAttributeModifier(player);
		if(!player.world.isRemote && player instanceof EntityPlayerMP)
		{
			PacketHandler.sendTo(new PacketStatus(this), (EntityPlayerMP) player);
		}
	}
}
