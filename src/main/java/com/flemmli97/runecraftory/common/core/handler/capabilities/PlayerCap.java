package com.flemmli97.runecraftory.common.core.handler.capabilities;

import java.util.Map;
import java.util.Map.Entry;

import com.flemmli97.runecraftory.api.entities.IEntityBase;
import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.api.items.ItemStatAttributes;
import com.flemmli97.runecraftory.client.render.ArmPosePlus;
import com.flemmli97.runecraftory.client.render.EnumToolCharge;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.core.handler.quests.QuestMission;
import com.flemmli97.runecraftory.common.inventory.InventoryShippingBin;
import com.flemmli97.runecraftory.common.inventory.InventorySpells;
import com.flemmli97.runecraftory.common.lib.LibConstants;
import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;
import com.flemmli97.runecraftory.common.network.PacketFoodUpdate;
import com.flemmli97.runecraftory.common.network.PacketHandler;
import com.flemmli97.runecraftory.common.network.PacketMaxRunePoints;
import com.flemmli97.runecraftory.common.network.PacketMoney;
import com.flemmli97.runecraftory.common.network.PacketPlayerLevel;
import com.flemmli97.runecraftory.common.network.PacketPlayerStats;
import com.flemmli97.runecraftory.common.network.PacketRunePoints;
import com.flemmli97.runecraftory.common.network.PacketSkills;
import com.flemmli97.runecraftory.common.network.PacketUpdateClient;
import com.flemmli97.runecraftory.common.network.PacketUpdateEquipmentStat;
import com.flemmli97.runecraftory.common.utils.EntityUtils;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import com.flemmli97.runecraftory.common.utils.ItemUtils;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import com.flemmli97.runecraftory.common.utils.RFCalculations;
import com.google.common.collect.Maps;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.Constants;

public class PlayerCap implements IPlayer
{
	//max runepoints possible: 2883
	private int money=0;
	private int runePointsMax=56;
	private int runePoints = runePointsMax;
	private float str = 5;
	private float vit = 4;
	private float intel = 5;
	private Map<IAttribute, Integer> headBonus = Maps.newHashMap();
	private Map<IAttribute, Integer> bodyBonus = Maps.newHashMap();
	private Map<IAttribute, Integer> legsBonus = Maps.newHashMap();
	private Map<IAttribute, Integer> feetBonus = Maps.newHashMap();
	private Map<IAttribute, Integer> mainHandBonus = Maps.newHashMap();
	private Map<IAttribute, Integer> offHandBonus = Maps.newHashMap();

	/** first number is level, second is the xp a.k.a. percent to next level*/
	private int[] level = new int[] {1,0};
	
	private Map<EnumSkills, int[]> skillMap = Maps.newHashMap();
	private InventorySpells spells=new InventorySpells();
	private InventoryShippingBin shipping = new InventoryShippingBin();

	private QuestMission quest;
	
	//Food buff
	private Map<IAttribute, Integer> foodBuffs = Maps.newHashMap();
	private int foodDuration;
	
	//Weapon and ticker
	private int ticker = 0;
	private int offHandTick;
	private EnumHand prevHand = EnumHand.MAIN_HAND;
	private WeaponSwing weapon;
	private int swings, timeSinceLastSwing;
	private ArmPosePlus armPose = ArmPosePlus.DEFAULT;
	//Gloves charge
    private boolean usingGloves;
    private int gloveTick;
    
    //Spear charge
	private int spearUse = 0;
	private int spearTicker = 0;
	
	public PlayerCap()
	{
		for(EnumSkills skill: EnumSkills.values())
		{
			this.skillMap.put(skill, new int[] {1,0});
		}
	}
    
    @Override
    public float getHealth(EntityPlayer player) {
        return player.getHealth();
    }
    
    @Override
    public void setHealth(EntityPlayer player, float amount) {
        if (amount > this.getMaxHealth(player)) {
            amount = this.getMaxHealth(player);
        }
        player.setHealth(amount);
    }
    
    @Override
    public void regenHealth(EntityPlayer player, float amount) {
        this.setHealth(player, amount+this.getHealth(player));
    }
    
    @Override
    public float getMaxHealth(EntityPlayer player) {
        return player.getMaxHealth()+(this.foodBuffs.containsKey(SharedMonsterAttributes.MAX_HEALTH)?this.foodBuffs.get(SharedMonsterAttributes.MAX_HEALTH):0);
    }
    
    @Override
    public void setMaxHealth(EntityPlayer player, float amount) {
    	IAttributeInstance health = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
    	health.removeModifier(LibConstants.maxHealthModifier);
    	health.applyModifier(new AttributeModifier(LibConstants.maxHealthModifier, "rf.hpModifier", amount-health.getBaseValue(), 0));
    }
    
    @Override
    public int getRunePoints() {
        return this.runePoints;
    }
    
    @Override
    public int getMaxRunePoints() {
        return this.runePointsMax+(this.foodBuffs.containsKey(ItemStatAttributes.RPMAX)?this.foodBuffs.get(ItemStatAttributes.RPMAX):0);
    }
    
    //TODO: boolean forced. so healing spells wont damage player when not enough rp
    @Override
    public boolean decreaseRunePoints(EntityPlayer player, int amount) {
        if (EntityUtils.isExhaut((EntityLivingBase)player)) {
            amount *= 2;
        }
        if(!player.capabilities.isCreativeMode)
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
            PacketHandler.sendTo(new PacketMaxRunePoints(this.runePointsMax), (EntityPlayerMP)player);
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
        //if (!player.capabilities.isCreativeMode) 
        {
            int neededXP = LevelCalc.xpAmountForLevelUp(this.level[0]);
            int xpToNextLevel = neededXP - this.level[1];
            if (amount >= xpToNextLevel) 
            {
                int diff = amount - xpToNextLevel;
                this.level[0]+=1;
                this.level[1] = 0;
                this.onLevelUp(player);
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
    
    private void onLevelUp(EntityPlayer player) {
    	this.setMaxHealth(player, this.getMaxHealth(player)+10);
        this.regenHealth(player, 10);
        this.runePointsMax += 5;
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
        this.str = amount;
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
        this.vit = amount;
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
        this.intel = amount;
        if (!player.world.isRemote && player instanceof EntityPlayerMP) {
            PacketHandler.sendTo(new PacketPlayerStats(this), (EntityPlayerMP)player);
        }
    }
    
    @Override
	public void updateEquipmentStats(EntityPlayer player, EntityEquipmentSlot slot)
	{
		switch(slot)
		{
			case CHEST: this.bodyBonus = ItemNBT.statIncrease(player.getItemStackFromSlot(slot));
				break;
			case FEET: this.feetBonus = ItemNBT.statIncrease(player.getItemStackFromSlot(slot));
				break;
			case HEAD: this.headBonus = ItemNBT.statIncrease(player.getItemStackFromSlot(slot));
				break;
			case LEGS: this.legsBonus = ItemNBT.statIncrease(player.getItemStackFromSlot(slot));
				break;
			case MAINHAND: this.mainHandBonus = ItemNBT.statIncrease(player.getItemStackFromSlot(slot));
				break;
			case OFFHAND: this.offHandBonus = ItemNBT.statIncrease(player.getItemStackFromSlot(slot));
				break;
		}
        if (!player.world.isRemote && player instanceof EntityPlayerMP) {
            PacketHandler.sendTo(new PacketUpdateEquipmentStat(slot), (EntityPlayerMP)player);
        }
	}
    
    @Override
	public int getAttributeValue(IAttribute att)
	{
    	int i = (int) Math.floor(this.helper(this.bodyBonus, att)+this.helper(this.feetBonus, att)+this.helper(this.headBonus, att)+this.helper(this.legsBonus, att)
		+this.helper(this.mainHandBonus, att)+this.helper(this.offHandBonus, att));
    	if(att==ItemStatAttributes.RFATTACK)
    		i+=this.getStr();
    	if(att==ItemStatAttributes.RFMAGICATT)
    		i+=this.getIntel();
    	if(att==ItemStatAttributes.RFMAGICDEF)
    		i+=this.getVit()*0.5;
    	if(att==ItemStatAttributes.RFDEFENCE)
    		i+=this.getVit()*0.5;
    	i+=this.foodBuffs.containsKey(att)?this.foodBuffs.get(att):0;
		return i;
	}
    
    private int helper(Map<IAttribute, Integer> bodyBonus2, IAttribute v)
    {
    	if(bodyBonus2.containsKey(v))
    		return bodyBonus2.get(v);
    	return 0;
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
        //if (!player.capabilities.isCreativeMode) 
        {
            int neededXP = LevelCalc.xpAmountForSkills(this.skillMap.get(skill)[0]);
            int xpToNextLevel = neededXP - this.skillMap.get(skill)[1];
            if (xp >= xpToNextLevel) 
            {
                int diff = xp - xpToNextLevel;
                this.skillMap.get(skill)[0]+=1;
                this.skillMap.get(skill)[1] = 0;
                this.onSkillLevelUp(skill, player);
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
    
    private void onSkillLevelUp(EnumSkills skill, EntityPlayer player) {
        this.setMaxHealth(player, this.getHealth(player)+skill.getHealthIncrease());
        this.regenHealth(player, skill.getHealthIncrease());
        this.runePointsMax += skill.getRPIncrease();
        this.runePoints += skill.getRPIncrease();
        this.str += skill.getStrIncrease();
        this.vit += skill.getVitIncrease();
        this.intel += skill.getIntelIncrease();
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
        if (this.quest == null && quest.questObjective()!=null) {
            this.quest = quest;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean finishMission(EntityPlayer player) {
        if (this.quest != null && this.quest.questObjective().isFinished()) 
        {
            for (ItemStack stack : this.quest.questObjective().rewards()) {
            	System.out.println(stack);
                ItemUtils.spawnItemAtEntity(player, stack);
            }
            this.setMoney(player, this.getMoney() + this.quest.questObjective().moneyReward());
            this.quest = null;
            return true;
        }
        return false;
    }
    
    
    @Override
    public void readFromNBT(NBTTagCompound nbt, EntityPlayer player) {
        this.runePointsMax = nbt.getInteger("MaxRunePoints");
        if (nbt.hasKey("RunePoints")) {
            this.runePoints = nbt.getInteger("RunePoints");
        }
        else {
            this.runePoints = this.runePointsMax;
        }
        if(nbt.hasKey("DeathHP") && player!=null)
        {
        	float f = nbt.getFloat("DeathHP");
        	if(f>0)
        		this.setHealth(player, f);
        }
        this.money = nbt.getInteger("Money");
        this.str = nbt.getFloat("Strength");
        this.vit = nbt.getFloat("Vitality");
        this.intel = nbt.getFloat("Intelligence");
        this.level = nbt.getIntArray("Level");
        NBTTagCompound compound = nbt.getCompoundTag("Skills");
        for (EnumSkills skill : EnumSkills.values()) {
            this.skillMap.put(skill, compound.getIntArray(skill.getIdentifier()));
        }
        if (nbt.hasKey("Inventory")) {
            NBTTagCompound compound2 = (NBTTagCompound)nbt.getTag("Inventory");
            this.spells.readFromNBT(compound2);
        }
        this.shipping.loadInventoryFromNBT(nbt.getTagList("Shipping", Constants.NBT.TAG_COMPOUND));
        if (nbt.hasKey("Quest")) {
            this.quest = new QuestMission(nbt.getCompoundTag("Quest"));
        }
        if(nbt.hasKey("FoodBuffs"))
        {
        	NBTTagCompound tag = nbt.getCompoundTag("FoodBuffs");
        	for(String s : tag.getKeySet())
        	{	
    			this.foodBuffs.put(ItemUtils.getAttFromName(s), tag.getInteger(s));
        	}
        }
        this.foodDuration=nbt.getInteger("FoodBuffDuration");
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt, EntityPlayer player) {
        if (player==null) {
            nbt.setInteger("RunePoints", this.runePoints);
        }
        else {
            nbt.setFloat("DeathHP", player.getMaxHealth() / 2.0f);
            nbt.setInteger("RunePoints", (int) (this.runePointsMax*0.3));
        }
        nbt.setInteger("MaxRunePoints", this.runePointsMax);
        nbt.setInteger("Money", this.money);
        nbt.setFloat("Strength", this.str);
        nbt.setFloat("Vitality", this.vit);
        nbt.setFloat("Intelligence", this.intel);
        nbt.setIntArray("Level", this.level);
        NBTTagCompound compound = new NBTTagCompound();
        for (EnumSkills skill : EnumSkills.values()) {
            compound.setIntArray(skill.getIdentifier(), this.skillMap.get(skill));
        }
        nbt.setTag("Skills", compound);
        NBTTagCompound compound2 = new NBTTagCompound();
        this.spells.writeToNBT(compound2);
        nbt.setTag("Inventory", compound2);
        nbt.setTag("Shipping", this.shipping.saveInventoryToNBT());
        if (this.quest != null) {
            nbt.setTag("Quest", this.quest.writeToNBT(new NBTTagCompound()));
        }
        NBTTagCompound compound3 = new NBTTagCompound();
        for(Entry<IAttribute, Integer> entry : this.foodBuffs.entrySet())
        {
        	compound3.setInteger(entry.getKey().getName(), entry.getValue());
        }
        nbt.setTag("FoodBuffs", compound3);
        nbt.setInteger("FoodBuffDuration", this.foodDuration);
        return nbt;
    }

	@Override
	public void applyFoodEffect(EntityPlayer player, Map<IAttribute, Integer> gain, Map<IAttribute, Float> gainMulti, int duration) {
		this.removeFoodEffect(player);
		for(IAttribute att : gainMulti.keySet())
		{
			int i = 0;
			if(att==SharedMonsterAttributes.MAX_HEALTH)
				i+=this.getMaxHealth(player)*gainMulti.get(att);
			else if(att==ItemStatAttributes.RPMAX)
				i+=this.runePointsMax*gainMulti.get(att);
			else if(att==ItemStatAttributes.RFATTACK)
				i+=this.str*gainMulti.get(att);
			else if(att==ItemStatAttributes.RFDEFENCE)
				i+=this.vit*0.5*gainMulti.get(att);
			else if(att==ItemStatAttributes.RFMAGICATT)
				i+=this.intel*gainMulti.get(att);
			else if(att==ItemStatAttributes.RFMAGICDEF)
				i+=this.vit*0.5*gainMulti.get(att);
			i+=gain.containsKey(att)?gain.get(att):0;
			gain.put(att, i);
		}
		this.foodBuffs=gain;
		this.foodDuration=duration;
        if (!player.world.isRemote && player instanceof EntityPlayerMP) {
            PacketHandler.sendTo(new PacketFoodUpdate(this.foodBuffs, this.foodDuration), (EntityPlayerMP)player);
        }
	}
	
	@Override
	public void removeFoodEffect(EntityPlayer player)
	{
		this.foodBuffs.clear();
		this.foodDuration=-1;
        if (!player.world.isRemote && player instanceof EntityPlayerMP) {
            PacketHandler.sendTo(new PacketFoodUpdate(this.foodBuffs, this.foodDuration), (EntityPlayerMP)player);
        }
	}
	
	@Override
	public Map<IAttribute, Integer> foodEffects()
	{
		return this.foodBuffs;
	}
	
	@Override
    public int animationTick() {
        return this.ticker;
    }
    
    @Override
    public void startAnimation(int tick) {
        this.ticker = tick;
    }
    
    @Override
    public boolean startGlove(EntityPlayer player) {
        if (this.usingGloves) {
            return false;
        }
        this.usingGloves = true;
        this.gloveTick = 60;
        return true;
    }
    
    private void updateGlove(EntityPlayer player) {
        --this.gloveTick;
        Vec3d look = player.getLookVec();
        Vec3d move = new Vec3d(look.x, 0.0, look.z).normalize().scale(0.4);
        player.motionX = move.x;
        player.motionZ = move.z;
        for (EntityLivingBase e : player.world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(1.0))) 
        {
            if (e != player) 
            {
                float damagePhys = this.getAttributeValue(ItemStatAttributes.RFATTACK);
                this.decreaseRunePoints(player, 2);
                this.increaseSkill(EnumSkills.FIST, player, 5);
                if (!(e instanceof IEntityBase)) 
                {
                    damagePhys = LevelCalc.scaleForVanilla(damagePhys);
                }
                RFCalculations.playerDamage(player, e, CustomDamage.attack((EntityLivingBase)player, ItemNBT.getElement(player.getHeldItemMainhand()), CustomDamage.DamageType.NORMAL, CustomDamage.KnockBackType.VANILLA, 1.0f, 20), damagePhys, this, player.getHeldItemMainhand());
            }
        }
        if (this.gloveTick == 0) 
        {
            this.usingGloves = false;
        }
    }
    
    @Override
    public void update(EntityPlayer player) {
        this.ticker = Math.max(--this.ticker, 0);
        this.foodDuration = Math.max(--this.foodDuration, -1);
        if(this.foodDuration==0)
        {
        	this.removeFoodEffect(player);
        }
        this.timeSinceLastSwing = Math.max(--this.timeSinceLastSwing, 0);
        if (this.timeSinceLastSwing == 0) {
            this.swings = 0;
        }
        if (this.usingGloves) 
        {
            this.updateGlove(player);
        }
        this.spearTicker = Math.max(--this.spearTicker, 0);
        this.offHandTick = Math.max(--this.offHandTick, 0);
        if (player.world.isRemote) {
            ItemStack heldMain = player.getHeldItemMainhand();
            if (heldMain.getItem() instanceof IChargeable) 
            {
                if (player.getItemInUseCount() > 0) 
                {
                    EnumToolCharge action = ((IChargeable)heldMain.getItem()).chargeType(heldMain);
                    switch (action) 
                    {
                        case CHARGECAN: this.armPose = ArmPosePlus.CHARGECAN;
                            break;
                        case CHARGEFISHING: this.armPose = ArmPosePlus.CHARGEFISHING;
                            break;
                        case CHARGEFIST: this.armPose = ArmPosePlus.CHARGEFIST;
                            break;
                        case CHARGELONG: this.armPose = ArmPosePlus.CHARGELONG;
                            break;
                        case CHARGESICKLE: this.armPose = ArmPosePlus.CHARGESICKLE;
                            break;
                        case CHARGESPEAR: this.armPose = ArmPosePlus.CHARGESPEAR;
                            break;
                        case CHARGESWORD: this.armPose = ArmPosePlus.CHARGESWORD;
                            break;
                        case CHARGEUPTOOL: this.armPose = ArmPosePlus.CHARGEUPTOOL;
                            break;
                        case CHARGEUPWEAPON: this.armPose = ArmPosePlus.CHARGEUPWEAPON;
                            break;
						case CHARGESEEDS:
							break;
                    }
                }
                else 
                {
                    this.armPose = ArmPosePlus.DEFAULT;
                }
            }
            else 
            {
                this.armPose = ArmPosePlus.DEFAULT;
            }
        }
    }
    
    @Override
    public boolean canUseSpear() {
        if (this.spearTicker > 0 && this.spearUse++ < 20) {
            return true;
        }
        this.spearUse = 0;
        this.spearTicker = 0;
        return false;
    }
    
    @Override
    public void startSpear() {
        this.spearTicker = 60;
    }
    
    @Override
    public int getSpearTick() {
        return this.spearTicker;
    }
    
    @Override
    public void disableOffHand() {
        this.offHandTick = 100;
    }
    
    @Override
    public boolean canUseOffHand() {
        return this.offHandTick == 0;
    }
    
    @Override
    public EnumHand getPrevSwung() {
        return this.prevHand;
    }
    
    @Override
    public void setPrevSwung(EnumHand hand) {
        this.prevHand = hand;
    }
    
    @Override
    public void startWeaponSwing(WeaponSwing swing, int delay) {
        if (this.weapon != swing) {
            this.swings = 0;
        }
        ++this.swings;
        this.timeSinceLastSwing = delay;
        this.weapon = swing;
    }
    
    @Override
    public boolean isAtUltimate() {
        return this.weapon.getMaxSwing() == this.swings;
    }
    
    @Override
    public ArmPosePlus currentArmPose() {
        return this.armPose;
    }
    
    @Override
    public void setArmPose(ArmPosePlus armPose) {
        this.armPose = armPose;
    }
}
