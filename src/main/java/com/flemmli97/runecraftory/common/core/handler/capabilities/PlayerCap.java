package com.flemmli97.runecraftory.common.core.handler.capabilities;

import java.util.HashMap;
import java.util.Map;

import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.core.handler.quests.QuestMission;
import com.flemmli97.runecraftory.common.inventory.InventoryShippingBin;
import com.flemmli97.runecraftory.common.inventory.InventorySpells;
import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;
import com.flemmli97.runecraftory.common.network.PacketHandler;
import com.flemmli97.runecraftory.common.network.PacketHealth;
import com.flemmli97.runecraftory.common.network.PacketMaxHealth;
import com.flemmli97.runecraftory.common.network.PacketMaxRunePoints;
import com.flemmli97.runecraftory.common.network.PacketMoney;
import com.flemmli97.runecraftory.common.network.PacketPlayerLevel;
import com.flemmli97.runecraftory.common.network.PacketPlayerStats;
import com.flemmli97.runecraftory.common.network.PacketRunePoints;
import com.flemmli97.runecraftory.common.network.PacketSkills;
import com.flemmli97.runecraftory.common.network.PacketUpdateClient;
import com.flemmli97.runecraftory.common.utils.EntityUtils;
import com.flemmli97.runecraftory.common.utils.ItemUtils;
import com.flemmli97.runecraftory.common.utils.LevelCalc;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;

public class PlayerCap implements IPlayer
{
	//max runepoints possible: 2883
	private int money=0;
	private int runePointsMax=56;
	private float healthMax=25;
	private float health=healthMax;
	private int runePoints = runePointsMax;
	private float str = 5;
	private float vit = 4;
	private float intel = 5;
	/** first number is level, second is the xp a.k.a. percent to next level*/
	private int[] level = new int[] {1,0};
	private Map<EnumSkills, int[]> skillMap = new HashMap<EnumSkills, int[]>();
	private InventorySpells spells=new InventorySpells();
	private InventoryShippingBin shipping = new InventoryShippingBin();

	private QuestMission quest;
	
	public PlayerCap()
	{
		for(EnumSkills skill: EnumSkills.values())
		{
			this.skillMap.put(skill, new int[] {1,0});
		}
	}
    
    @Override
    public float getHealth() {
        return this.health;
    }
    
    @Override
    public void setHealth(EntityPlayer player, float amount) {
        if (amount > this.healthMax) {
            this.health = this.healthMax;
        }
        else {
            this.health = amount;
        }
        if (!player.world.isRemote && player instanceof EntityPlayerMP) {
            PacketHandler.sendTo(new PacketHealth(this), (EntityPlayerMP)player);
        }
    }
    
    @Override
    public void regenHealth(EntityPlayer player, float amount) {
        this.health = Math.min(this.getMaxHealth(), this.health + amount);
        if (!player.world.isRemote && player instanceof EntityPlayerMP) {
            PacketHandler.sendTo(new PacketHealth(this), (EntityPlayerMP)player);
        }
    }
    
    @Override
    public void damage(EntityPlayer player, DamageSource source, float amount) {
        player.getCombatTracker().trackDamage(source, this.health, amount);
        this.health -= amount;
        if (this.health <= 0.0f) {
            player.setHealth(0.0f);
        }
        if (!player.world.isRemote && player instanceof EntityPlayerMP) {
            PacketHandler.sendTo(new PacketHealth(this), (EntityPlayerMP)player);
        }
    }
    
    @Override
    public float getMaxHealth() {
        return this.healthMax;
    }
    
    @Override
    public void setMaxHealth(EntityPlayer player, float amount) {
        this.healthMax = amount;
        if (!player.world.isRemote && player instanceof EntityPlayerMP) {
            PacketHandler.sendTo(new PacketMaxHealth(this), (EntityPlayerMP)player);
        }
    }
    
    @Override
    public int getRunePoints() {
        return this.runePoints;
    }
    
    @Override
    public int getMaxRunePoints() {
        return this.runePointsMax;
    }
    
    @Override
    public boolean decreaseRunePoints(EntityPlayer player, int amount) {
        if (EntityUtils.isExhaut((EntityLivingBase)player)) {
            amount *= 2;
        }
        if(!player.capabilities.isCreativeMode)
        {
        	//For when i remember why i did this
	        //if (this.runePointsMax >= amount) 
	        {
	            if (this.runePoints >= amount) 
	                this.runePoints -= amount;
	            else 
	            {
	                int diff = amount - this.runePoints;
	                this.runePoints = 0;
	                if (!player.world.isRemote && player instanceof EntityPlayerMP) 
	                    player.attackEntityFrom(CustomDamage.EXHAUST, (float)(diff * 2));
	            }
	            if (!player.world.isRemote && player instanceof EntityPlayerMP) 
	                PacketHandler.sendTo(new PacketRunePoints(this), (EntityPlayerMP)player);
	            return true;
	        }
	        //return false;
        }
        return true;
    }
    
    @Override
    public void refreshRunePoints(EntityPlayer player, int amount) {
        this.runePoints = Math.min(this.getMaxRunePoints(), this.runePoints + amount);
        if (!player.world.isRemote && player instanceof EntityPlayerMP) {
            PacketHandler.sendTo(new PacketRunePoints(this), (EntityPlayerMP)player);
        }
    }
    
    @Override
    public void setRunePoints(EntityPlayer player, int amount) {
        this.runePoints = amount;
    }
    
    @Override
    public void setMaxRunePoints(EntityPlayer player, int amount) {
        this.runePointsMax = amount;
        if (!player.world.isRemote && player instanceof EntityPlayerMP) {
            PacketHandler.sendTo(new PacketMaxRunePoints(this), (EntityPlayerMP)player);
        }
    }
    
    @Override
    public int getMoney() {
        return this.money;
    }
    
    @Override
    public boolean useMoney(EntityPlayer player, int amount) {
        if (this.money >= amount) {
            this.money -= amount;
            if (!player.world.isRemote && player instanceof EntityPlayerMP) {
                PacketHandler.sendTo(new PacketMoney(this), (EntityPlayerMP)player);
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void setMoney(EntityPlayer player, int amount) {
        this.money = amount;
        if (!player.world.isRemote && player instanceof EntityPlayerMP) {
            PacketHandler.sendTo(new PacketMoney(this), (EntityPlayerMP)player);
        }
    }
    
    @Override
    public int[] getPlayerLevel() {
        return this.level;
    }
    
    @Override
    public void addXp(EntityPlayer player, int amount) {
        if (!player.capabilities.isCreativeMode) 
        {
            int neededXP = LevelCalc.xpAmountForLevelUp(this.level[0]);
            int xpToNextLevel = neededXP - this.level[1];
            if (amount >= xpToNextLevel) 
            {
                int diff = amount - xpToNextLevel;
                this.level[0]+=1;
                this.level[1] = 0;
                this.onLevelUp();
                player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.2f, 1.0f);
                this.addXp(player, diff);
            }
            else 
            {
                this.level[1] += amount;
            }
            if (!player.world.isRemote && player instanceof EntityPlayerMP) {
                PacketHandler.sendTo(new PacketUpdateClient(this), (EntityPlayerMP)player);
            }
        }
    }
    
    private void onLevelUp() {
        this.healthMax += 10.0f;
        this.runePointsMax += 5;
        this.health = Math.min(this.health + 10.0f, this.healthMax);
        this.runePoints = Math.min(this.runePoints + 5, this.runePoints);
        this.str += 2.0f;
        this.vit += 2.0f;
        this.intel += 2.0f;
    }
    
    @Override
    public void setPlayerLevel(EntityPlayer player, int level, int xpAmount) {
        this.level[0] = level;
        this.level[1] = xpAmount;
        if (!player.world.isRemote && player instanceof EntityPlayerMP) {
            PacketHandler.sendTo(new PacketPlayerLevel(this), (EntityPlayerMP)player);
        }
    }
    
    @Override
    public float getStr() {
        return this.str;
    }
    
    @Override
    public void setStr(EntityPlayer player, float amount) {
        this.str += amount;
        if (!player.world.isRemote && player instanceof EntityPlayerMP) {
            PacketHandler.sendTo(new PacketPlayerStats(this), (EntityPlayerMP)player);
        }
    }
    
    @Override
    public float getVit() {
        return this.vit;
    }
    
    @Override
    public void setVit(EntityPlayer player, float amount) {
        this.vit += amount;
        if (!player.world.isRemote && player instanceof EntityPlayerMP) {
            PacketHandler.sendTo(new PacketPlayerStats(this), (EntityPlayerMP)player);
        }
    }
    
    @Override
    public float getIntel() {
        return this.intel;
    }
    
    @Override
    public void setIntel(EntityPlayer player, float amount) {
        this.intel += amount;
        if (!player.world.isRemote && player instanceof EntityPlayerMP) {
            PacketHandler.sendTo(new PacketPlayerStats(this), (EntityPlayerMP)player);
        }
    }
    
    @Override
    public int[] getSkillLevel(EnumSkills skill) {
        return this.skillMap.get(skill);
    }
    
    @Override
    public void setSkillLevel(EnumSkills skill, EntityPlayer player, int level, int xp) {
        this.skillMap.get(skill)[0] = level;
        this.skillMap.get(skill)[1] = xp;
        if (!player.world.isRemote && player instanceof EntityPlayerMP) {
            PacketHandler.sendTo(new PacketSkills(this, skill), (EntityPlayerMP)player);
        }
    }
    
    @Override
    public void increaseSkill(EnumSkills skill, EntityPlayer player, int xp) {
        if (!player.capabilities.isCreativeMode) 
        {
            int neededXP = LevelCalc.xpAmountForSkills(this.skillMap.get(skill)[0]);
            int xpToNextLevel = neededXP - this.skillMap.get(skill)[1];
            if (xp >= xpToNextLevel) 
            {
                int diff = xp - xpToNextLevel;
                this.skillMap.get(skill)[0]+=1;
                this.skillMap.get(skill)[1] = 0;
                this.onSkillLevelUp(skill);
                player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.2f, 1.0f);
                this.increaseSkill(skill, player, diff);
            }
            else 
            {
                this.skillMap.get(skill)[1] += xp;
            }
            if (!player.world.isRemote && player instanceof EntityPlayerMP) {
                PacketHandler.sendTo(new PacketUpdateClient(this), (EntityPlayerMP)player);
            }
        }
    }
    
    private void onSkillLevelUp(EnumSkills skill) {
        this.healthMax += skill.getHealthIncrease();
        this.health += skill.getHealthIncrease();
        this.runePointsMax += skill.getRPIncrease();
        this.runePoints += skill.getRPIncrease();
        this.str += skill.getStrIncrease();
        this.vit += skill.getVitIncrease();
        this.intel += skill.getIntelIncrease();
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.runePointsMax = nbt.getInteger("MaxRunePoints");
        if (nbt.hasKey("RunePoints")) {
            this.runePoints = nbt.getInteger("RunePoints");
        }
        else {
            this.runePoints = this.runePointsMax;
        }
        this.health = nbt.getFloat("Health");
        this.healthMax = nbt.getFloat("HealthMax");
        this.money = nbt.getInteger("Money");
        this.str = nbt.getFloat("Strength");
        this.vit = nbt.getFloat("Vitality");
        this.intel = nbt.getFloat("Intelligence");
        this.level = nbt.getIntArray("Level");
        NBTTagCompound compound = (NBTTagCompound)nbt.getTag("Skills");
        for (EnumSkills skill : EnumSkills.values()) {
            this.skillMap.put(skill, compound.getIntArray(skill.getIdentifier()));
        }
        if (nbt.hasKey("Inventory")) {
            NBTTagCompound compound2 = (NBTTagCompound)nbt.getTag("Inventory");
            this.spells.readFromNBT(compound2);
        }
        this.shipping.loadInventoryFromNBT(nbt.getTagList("Shipping", 10));
        if (nbt.hasKey("Quest")) {
            this.quest = new QuestMission(nbt.getCompoundTag("Quest"));
        }
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt, boolean forDeath) {
        if (!forDeath) {
            nbt.setFloat("Health", this.health);
            nbt.setInteger("RunePoints", this.runePoints);
        }
        else {
            nbt.setFloat("Health", this.healthMax / 2.0f);
        }
        nbt.setInteger("MaxRunePoints", this.runePointsMax);
        nbt.setFloat("HealthMax", this.healthMax);
        nbt.setInteger("Money", this.money);
        nbt.setFloat("Strength", this.str);
        nbt.setFloat("Vitality", this.vit);
        nbt.setFloat("Intelligence", this.intel);
        nbt.setIntArray("Level", this.level);
        NBTTagCompound compound = new NBTTagCompound();
        for (EnumSkills skill : EnumSkills.values()) {
            compound.setIntArray(skill.getIdentifier(), (int[])this.skillMap.get(skill));
        }
        nbt.setTag("Skills", (NBTBase)compound);
        NBTTagCompound compound2 = new NBTTagCompound();
        this.spells.writeToNBT(compound2);
        nbt.setTag("Inventory", (NBTBase)compound2);
        nbt.setTag("Shipping", (NBTBase)this.shipping.saveInventoryToNBT());
        if (this.quest != null) {
            nbt.setTag("Quest", (NBTBase)this.quest.writeToNBT(new NBTTagCompound()));
        }
        return nbt;
    }
    
    @Override
    public InventorySpells getInv() {
        return this.spells;
    }
    
    @Override
    public InventoryShippingBin getShippingInv() {
        return this.shipping;
    }
    
    @Override
    public QuestMission currentMission() {
        return this.quest;
    }
    
    @Override
    public boolean acceptMission(QuestMission quest) {
        if (this.quest == null) {
            this.quest = quest;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean finishMission(EntityPlayer player) {
        if (this.quest != null && this.quest.questObjective().isFinished()) {
            for (ItemStack stack : this.quest.questObjective().rewards()) {
                ItemUtils.spawnItemAtEntity(player, stack);
            }
            this.setMoney(player, this.getMoney() + this.quest.questObjective().moneyReward());
            this.quest = null;
            return true;
        }
        return false;
    }
}
