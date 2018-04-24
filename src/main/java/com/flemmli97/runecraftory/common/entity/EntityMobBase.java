package com.flemmli97.runecraftory.common.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.api.entities.IEntityAdvanced;
import com.flemmli97.runecraftory.api.entities.IEntityBase;
import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.api.items.IItemBase;
import com.flemmli97.runecraftory.api.items.IRFFood;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage.KnockBackType;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.LibConstants;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.lib.enums.EnumStatusEffect;
import com.flemmli97.runecraftory.common.utils.ItemUtils;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import com.flemmli97.runecraftory.common.utils.RFCalculations;
import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class EntityMobBase extends EntityCreature  implements IEntityAdvanced, IMob{

	private Map<IAttribute, Double> baseValues;
    protected static final DataParameter<Boolean> isTamed = EntityDataManager.<Boolean>createKey(EntityMobBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<String> owner = EntityDataManager.<String>createKey(EntityMobBase.class, DataSerializers.STRING);
    private static final DataParameter<Integer> attackTimer = EntityDataManager.<Integer>createKey(EntityMobBase.class, DataSerializers.VARINT);
    private static final DataParameter<Byte> attackPattern = EntityDataManager.<Byte>createKey(EntityMobBase.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> level = EntityDataManager.<Integer>createKey(EntityMobBase.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> levelXP = EntityDataManager.<Integer>createKey(EntityMobBase.class, DataSerializers.VARINT);

	private boolean ridable, flyingEntity;
	protected int tamingTick=-1;
	private int baseXP, baseMoney;
	protected int feedTimeOut;
	private boolean doJumping = false;

	private List<EnumStatusEffect> activeEffects = new ArrayList<EnumStatusEffect>();
	
	public EntityAINearestAttackableTarget<EntityPlayer> targetPlayer = new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class,5,true, true, new Predicate<EntityPlayer>() {
		@Override
		public boolean apply(EntityPlayer player) {
			return !EntityMobBase.this.isTamed() ||player!=EntityMobBase.this.getOwner();
		}} ) ;
	public EntityAINearestAttackableTarget<EntityVillager> targetNPC = new EntityAINearestAttackableTarget<EntityVillager>(this, EntityVillager.class,5, true, true, new Predicate<EntityVillager>() {
		@Override
		public boolean apply(EntityVillager mob) {
			return !EntityMobBase.this.isTamed();
		}});
	
	public EntityAINearestAttackableTarget<EntityCreature> targetMobs = new EntityAINearestAttackableTarget<EntityCreature>(this, EntityCreature.class,5,true, true, new Predicate<EntityCreature>() {
		@Override
		public boolean apply(EntityCreature mob) {
			if(EntityMobBase.this.isTamed())
			{
				if(mob instanceof EntityMobBase && ((EntityMobBase) mob).isTamed())
				{
					return false;
				}
				return true;
			}
			else if(!(mob instanceof EntityMobBase && ((EntityMobBase) mob).isTamed()))
					return false;
			return IMob.VISIBLE_MOB_SELECTOR.apply(mob);
		}} ) ;

	private EntityMobBase(World world) {
		super(world);
		this.targetTasks.addTask(1, targetPlayer);
		this.targetTasks.addTask(2, targetNPC);
		this.targetTasks.addTask(3, targetMobs);
	}
	
	@Override
	protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAIMoveTowardsRestriction(this, 1.0D));
	    this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
	    this.tasks.addTask(3, new EntityAISwimming(this));
	    this.tasks.addTask(4, new EntityAILookIdle(this));
	    this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
	}

	public EntityMobBase(World world, boolean ridable, int baseXP, int baseMoney, boolean isFlyingEntity)
	{
		this(world);
		this.ridable=ridable;
		this.baseXP=baseXP;
		this.baseMoney=baseMoney;
		this.flyingEntity = isFlyingEntity;
	}
	
	//=====Init

	@Override
	protected void applyEntityAttributes() {
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20*LibConstants.DAMAGESCALE);;
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.2);;
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.24);;
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24.0D);
        this.getAttributeMap().registerAttribute(ItemStats.RFATTACK);
        this.getAttributeMap().registerAttribute(ItemStats.RFDEFENCE);
        this.getAttributeMap().registerAttribute(ItemStats.RFMAGICATT);
        this.getAttributeMap().registerAttribute(ItemStats.RFMAGICDEF);
	}
	
	@Override
	protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(isTamed, false);
        this.dataManager.register(owner, "");
        this.dataManager.register(attackTimer, 0);
        this.dataManager.register(attackPattern, (byte)0);
        this.dataManager.register(level, LibConstants.baseLevel);
    }
	
	//=====IRFEntity
	
	@Override
	public int level() {
		return this.dataManager.get(level);
	}
	
	public void setLevel(int level)
	{
		if(level>10000)
			level=10000;
		this.dataManager.set(EntityMobBase.level, level);
	}
	
	public int getLevelXp()
	{
		return this.dataManager.get(levelXP);
	}
	
	public void addXp(int amount)
	{
		int neededXP = LevelCalc.xpAmountForLevelUp(this.level());
		int xpToNextLevel = neededXP-this.getLevelXp();
		if(amount >= xpToNextLevel)
		{				
			int diff = amount-xpToNextLevel;
			this.setLevel(this.level()+1);
			this.onLevelUp();
			this.addXp(diff);
		}
		else
		{
			this.dataManager.set(levelXP, amount);
		}
	}
	
	private void onLevelUp()
	{
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(
				LevelCalc.onEntityLevelUp(baseValues.get(SharedMonsterAttributes.MAX_HEALTH), this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue(), false, true));
		this.getAttributeMap().getAttributeInstance(ItemStats.RFATTACK).setBaseValue(
				LevelCalc.onEntityLevelUp(baseValues.get(ItemStats.RFATTACK), this.getAttributeMap().getAttributeInstance(ItemStats.RFATTACK).getAttributeValue(), false, false));
		this.getAttributeMap().getAttributeInstance(ItemStats.RFDEFENCE).setBaseValue(
				LevelCalc.onEntityLevelUp(baseValues.get(ItemStats.RFDEFENCE), this.getAttributeMap().getAttributeInstance(ItemStats.RFDEFENCE).getAttributeValue(), false, false));
		this.getAttributeMap().getAttributeInstance(ItemStats.RFMAGICATT).setBaseValue(
				LevelCalc.onEntityLevelUp(baseValues.get(ItemStats.RFMAGICATT), this.getAttributeMap().getAttributeInstance(ItemStats.RFMAGICATT).getAttributeValue(), false, false));
		this.getAttributeMap().getAttributeInstance(ItemStats.RFMAGICDEF).setBaseValue(
				LevelCalc.onEntityLevelUp(baseValues.get(ItemStats.RFMAGICDEF), this.getAttributeMap().getAttributeInstance(ItemStats.RFMAGICDEF).getAttributeValue(), false, false));
		this.setHealth(this.getMaxHealth());
	}
	
	@Override
    public void notifyDataManagerChange(DataParameter<?> key)
	{
		if(key.equals(level))
		{
			if(baseValues!=null)
				this.updateStatsToLevel();
		}
		super.notifyDataManagerChange(key);
	}

	@Override
	public boolean isTamed() {
		return this.dataManager.get(isTamed);
	}

	@Override
	public boolean ridable() {
		return this.ridable;
	}

	@Override
	public int baseXP() {
		return this.baseXP;
	}

	@Override
	public int baseMoney() {
		return this.baseMoney;
	}

	@Override
	public List<EnumStatusEffect> getActiveStatus() {return this.activeEffects;}

	@Override
	public void addStatus(EnumStatusEffect status) {
		this.activeEffects.add(status);
	}

	@Override
	public void clearEffect() {
		this.activeEffects.clear();
	}

	@Override
	public void cureEffect(EnumStatusEffect status) {
		this.activeEffects.remove(status);
	}
	
	@Override
	public EntityPlayer getOwner()
    {
	    	if(owner != null)
	    	{
	        try
	        {
	            UUID var1 = UUID.fromString(this.dataManager.get(owner));
	            return var1 == null ? null : this.world.getPlayerEntityByUUID(var1);
	        }
	        catch (IllegalArgumentException var2)
	        {
	            return null;
	        }
	    	}
		return null;
    }
	@Override
    public void setOwner(EntityPlayer player)
    {
    		if(player!=null)
    			this.dataManager.set(owner,player.getUniqueID().toString()); 		
    }

	//=====Move Helper
	@Override
	public boolean isFlyingEntity()
	{
		return this.flyingEntity;
	}
	
	public void setDoJumping()
	{
		this.doJumping=true;
	}

	@Override
	public void fall(float distance, float damageMultiplier) {
		if(!this.isFlyingEntity())
			super.fall(distance, damageMultiplier);
	}

	//=====Client
	@Override
	public void handleStatusUpdate(byte id) {
		if(id==10)
		{
			this.playTameEffect(true);
		}
		else if(id==11)
		{
			this.playTameEffect(false);
		}
		else if(id==34)
		{
			this.tamingTick=60;
		}
		super.handleStatusUpdate(id);
	}
	
	private void playTameEffect(boolean play)
    {
        EnumParticleTypes enumparticletypes = EnumParticleTypes.HEART;
        if (!play)
        {
            enumparticletypes = EnumParticleTypes.SMOKE_NORMAL;
        }

        for (int i = 0; i < 7; ++i)
        {
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            this.world.spawnParticle(enumparticletypes, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d0, d1, d2);
        }
    }
	
	//to improve
	public void setAttackTimeAndPattern(byte pattern)
	{
		this.dataManager.set(attackTimer, this.getAttackTimeFromPattern(pattern));
		this.dataManager.set(attackPattern, pattern);
	}
	
	public byte getAttackPattern()
	{
		return this.dataManager.get(attackPattern);
	}
	
	public int getAttackTime()
	{
		return this.dataManager.get(attackTimer);
	}

	//=====NBT
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("levelMob", this.level());
		compound.setBoolean("tamed", this.dataManager.get(isTamed));
		NBTTagCompound compound2 = new NBTTagCompound(); 
		for(EnumStatusEffect effect : this.activeEffects)
		{
			compound2.setString(effect.getName(), effect.getName());
		}
		compound.setTag("effects", compound2);
		compound.setBoolean("out", this.dead);
		compound.setInteger("feedTime", this.feedTimeOut);
		compound.setIntArray("home", new int[] {this.getHomePosition().getX(),this.getHomePosition().getY(),this.getHomePosition().getZ(), (int) this.getMaximumHomeDistance()});
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		if(compound.hasKey("levelMob"))
			this.dataManager.set(level, compound.getInteger("levelMob"));
		if(compound.hasKey("tamed"))
			this.dataManager.set(isTamed, compound.getBoolean("tamed")); 
		NBTTagCompound compound2 = (NBTTagCompound) compound.getTag("effects");
		if(compound2!=null)
		for(String effectNames : compound2.getKeySet())
		{
			this.activeEffects.add(EnumStatusEffect.fromName(compound.getString(effectNames)));
		}
		this.feedTimeOut=compound.getInteger("feedTime");
		if(compound.hasKey("home"))
		{
			int[] home = compound.getIntArray("home");
			this.setHomePosAndDistance(new BlockPos(home[0],home[1],home[2]), home[3]);
		}
		this.dead=compound.getBoolean("out");
	}

	//=====Disable/Tweak vanilla
	@Override
	protected boolean canEquipItem(ItemStack stack) {return false;}
	/**mobs here cant equip items*/
	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {}
	@Override
	public void heal(float healAmount){
		healAmount*=LibConstants.DAMAGESCALE;
		super.heal(healAmount);
    }
	//Interact

	@Override
	public void onLivingUpdate() {
		if(!this.dead)
		{
			if(this.tamingTick>0)
				this.tamingTick--;
			if(this.tamingTick==0)
			{
				if(this.isTamed())
	                this.world.setEntityState(this, (byte)10);
				else
	                this.world.setEntityState(this, (byte)11);
				this.tamingTick=-1;
			}
			if(this.feedTimeOut>0)
				feedTimeOut--;
			super.onLivingUpdate();
			if(this.getAttackTime()>0)
				this.dataManager.set(attackTimer, this.getAttackTime()-1);
		}
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItemMainhand();
		if(!stack.isEmpty() && player.isSneaking() && !this.world.isRemote)
		{
			if(stack.getItem()==Items.GOLDEN_APPLE)
			{
				this.getAttributeMap().getAttributeInstance(ItemStats.RFATTACK).setBaseValue(999999);
				return true;
			}
			if(stack.getItem()==ModItems.tame)
			{
				this.tameEntity(player);
			}
			else if(!this.isTamed() && this.tamingTick==-1 && this.deathTime==0)
			{
				boolean flag=false;
				if(this.tamingItem()!=null)
					for(ItemStack item : this.tamingItem())
						if(item.getItem()== stack.getItem() && item.getMetadata()==stack.getMetadata())
							flag=true;
				float rightItemMultiplier = flag ? 2 : 1F;
				if(!player.capabilities.isCreativeMode)
					stack.shrink(1);
				if(this.rand.nextFloat()<=this.tamingChance()*rightItemMultiplier*LevelCalc.tamingMultiplerOnLevel(this.level()))
				{
					this.tameEntity(player);
				}
				if(stack.getItem() instanceof IRFFood)
				{
					//heal
				}
				this.tamingTick=60;
                this.world.setEntityState(this, (byte)34);
			}
			else
			{
				//heal
				if(stack.getItem()==Items.STICK)
				{
					System.out.println("untame");
					this.dataManager.set(isTamed, false); //for debugging

				}
				else if(stack.getItem()==ModItems.inspector)
				{
					//open tamed gui
				}
				else if(feedTimeOut<=0 && stack.getItem() instanceof IRFFood || stack.getItem() instanceof IItemBase)
				{
					//food effect
					
					this.feedTimeOut=24000;
				}
				
			}
			return true;
		}
		else if(stack.isEmpty() && !this.world.isRemote && this.isTamed() && this.ridable())
		{
			player.startRiding(this);
			return true;
		}
		return false;
	}
	
	protected void tameEntity(EntityPlayer owner)
	{
		this.dataManager.set(isTamed, true);
		this.setOwner(owner);
		this.navigator.clearPath();
		this.setAttackTarget((EntityLivingBase)null);
        this.world.setEntityState(this, (byte)10);

	}

	@Override
	public boolean canBeSteered() {
		return this.isTamed() && this.ridable();
	}
	
	@Override
	public boolean canPassengerSteer() {
            return this.canBeSteered() && !this.world.isRemote;
	}
	@Override
	protected void addPassenger(Entity passenger)
    {
		this.targetTasks.removeTask(targetPlayer);
		this.targetTasks.removeTask(targetNPC);
		this.targetTasks.removeTask(targetMobs);
		super.addPassenger(passenger);
    }
	@Override
	protected void removePassenger(Entity passenger)
    {
		this.targetTasks.addTask(1, targetPlayer);
		this.targetTasks.addTask(2, targetNPC);
		this.targetTasks.addTask(3, targetMobs);
		super.removePassenger(passenger);
    }
	
	@Override
    protected boolean isMovementBlocked()
    {
        return super.isMovementBlocked() && this.isBeingRidden();
    }
	
	@Override
	@Nullable
    public Entity getControllingPassenger()
    {
        return this.getPassengers().isEmpty() ? null : (Entity)this.getPassengers().get(0);
    }
	
	@Override
    public void travel(float strafing, float upward, float forward)
    {
        if (this.isBeingRidden() && this.canBeSteered())
        {
            EntityLivingBase entitylivingbase = (EntityLivingBase)this.getControllingPassenger();
            this.rotationYaw = entitylivingbase.rotationYaw;
            this.prevRotationYaw = this.rotationYaw;
            this.rotationPitch = entitylivingbase.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.renderYawOffset;
            strafing = entitylivingbase.moveStrafing * 0.5F;
            forward = entitylivingbase.moveForward;
            if (forward <= 0.0F)
            {
                forward *= 0.25F;
            }
            if (this.doJumping && this.onGround && !this.isFlyingEntity())
            {
                this.isAirBorne = true;
                this.jump();
                if (forward > 0.0F)
                {
                    float f = MathHelper.sin(this.rotationYaw * 0.017453292F);
                    float f1 = MathHelper.cos(this.rotationYaw * 0.017453292F);
                    this.motionX += (double)(-0.4F * f );
                    this.motionZ += (double)(0.4F * f1 );
                }
            }
            else if(this.doJumping)
            {
            	this.motionY=Math.min(this.motionY+=0.2F, 2);
            }
            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;
            if (this.canPassengerSteer())
            {
                this.setAIMoveSpeed((float)this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
                super.travel(strafing, upward, forward);
            }
            else if (entitylivingbase instanceof EntityPlayer)
            {
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
            }
            if (this.onGround || this.isFlyingEntity())
            {
                this.doJumping=false;
            }
            this.jumpMovementFactor = 0.5F;
            this.prevLimbSwingAmount = this.limbSwingAmount;
            double d1 = this.posX - this.prevPosX;
            double d0 = this.posZ - this.prevPosZ;
            float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;
            if (f2 > 1.0F)
            {
                f2 = 1.0F;
            }
            this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
            this.limbSwing += this.limbSwingAmount;
        }
        else
        {
            this.jumpMovementFactor = 0.02F;
            super.travel(strafing, forward, forward);
        }
    }
	//=====Combat stuff

	//skill add etc.
	@Override
	protected void onDeathUpdate() {
		if(!this.isTamed())
		{
	        super.onDeathUpdate();

			if(this.deathTime==5)
			{
				if( this.attackingPlayer!=null)
	            {
	    				IPlayer cap = this.attackingPlayer.getCapability(PlayerCapProvider.PlayerCap, null);
	    				cap.addXp(attackingPlayer, LevelCalc.xpFromLevel(this.baseXP(), this.level()));
	    				cap.setMoney(attackingPlayer, cap.getMoney()+ LevelCalc.moneyFromLevel(this.baseMoney(), this.level()));
	            }
			}
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
    {
		if(source.getTrueSource()!=null && !this.canAttackFrom(source.getTrueSource().getPosition()))
			return false;
		return super.attackEntityFrom(source, amount);
	}
	
	private boolean canAttackFrom(BlockPos pos)
    {
        if (this.getMaximumHomeDistance() == -1.0F)
        {
            return true;
        }
        else
        {
            return this.getHomePosition().distanceSq(pos) < (double)((this.getMaximumHomeDistance()+2) * (this.getMaximumHomeDistance()+2));
        }
    }
	//damage reduction from armor
	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount) 
	{
		if(damageSrc.getTrueSource()!=null && !this.isWithinHomeDistanceFromPosition(damageSrc.getTrueSource().getPosition()))
			return;
		if (!this.isEntityInvulnerable(damageSrc))
        {
            damageAmount = net.minecraftforge.common.ForgeHooks.onLivingHurt(this, damageSrc, damageAmount);
            if (damageAmount <= 0) return;
            	damageAmount = this.reduceDamage(damageSrc, damageAmount);
            	if(damageSrc != DamageSource.OUT_OF_WORLD)
	    			damageAmount*=LibConstants.DAMAGESCALE;
            if (damageAmount != 0.0F)
            {
                float f1 = this.getHealth();
                this.setHealth(f1 - damageAmount);
                this.getCombatTracker().trackDamage(damageSrc, f1, damageAmount);
            }
        }
	}
	
	//TODO: elemental reduction
	protected float reduceDamage(DamageSource damageSrc, float damageAmount)
	{
		float reduce = 0;
		if(!damageSrc.isDamageAbsolute() && !damageSrc.isUnblockable())
			if(damageSrc.isMagicDamage())
				reduce = (float)this.getEntityAttribute(ItemStats.RFMAGICDEF).getAttributeValue();
			else
				reduce = (float)this.getEntityAttribute(ItemStats.RFDEFENCE).getAttributeValue();
		return Math.max(0, damageAmount-reduce);
	}

	//status ailment etc.
	@Override
	public boolean attackEntityAsMob(Entity entity) {
		return this.attackEntityAsMobWithElement(entity, EnumElement.NONE);
	}
	
	public boolean attackEntityAsMobWithElement(Entity entity, EnumElement element)
	{
		if(entity instanceof EntityLivingBase)
		{
			float damage = (float)this.getEntityAttribute(ItemStats.RFATTACK).getAttributeValue();
			boolean faintChance = this.world.rand.nextInt(100)<RFCalculations.getAttributeValue(this, ItemStats.RFFAINT, (EntityLivingBase) entity, ItemStats.RFRESFAINT);
			boolean critChance = this.world.rand.nextInt(100)<RFCalculations.getAttributeValue(this, ItemStats.RFCRIT, (EntityLivingBase) entity, ItemStats.RFRESCRIT);
			boolean knockBackChance = this.world.rand.nextInt(100)<RFCalculations.getAttributeValue(this, ItemStats.RFKNOCK, (EntityLivingBase) entity, SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
			int i = knockBackChance?3:1;

	        if(!(entity instanceof IEntityBase) && !(entity instanceof EntityPlayer))
	        		damage = LevelCalc.scaleForVanilla(damage);
	        else
	        		damage = faintChance?Float.MAX_VALUE:damage;

	        boolean ignoreArmor = faintChance||critChance;
	        CustomDamage source = CustomDamage.attack(this, element, ignoreArmor?CustomDamage.DamageType.IGNOREDEF:CustomDamage.DamageType.NORMAL, KnockBackType.BACK, i*0.5F-0.2F, 5);
	        boolean flag = entity.attackEntityFrom(source, damage);
	        if (flag)
	        {
	            if (entity instanceof EntityLivingBase)
	            {
                	RFCalculations.knockBack((EntityLivingBase) entity, source);
	                this.motionX *= 0.6D;
	                this.motionZ *= 0.6D;
	            }
	            float drainPercent= RFCalculations.getAttributeValue(this, ItemStats.RFDRAIN, (EntityLivingBase) entity,ItemStats.RFRESDRAIN)/100F;
                if(drainPercent>0)
                		this.setHealth(this.getHealth() + drainPercent*damage);
                                        
                RFCalculations.applyStatusEffects(this, (EntityLivingBase) entity);
	            this.applyEnchantments(this, entity);
	        }
	        return flag;
		}
		return false;
	}
	
	@Override
	public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio) {
		strength*=0.85F;
		super.knockBack(entityIn, strength, xRatio, zRatio);
	}

	@Override
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
		if(!this.isTamed())
			if(this.getDrops()!=null)
				for(ItemStack item : this.getDrops().keySet())
				{
					int max = 0;
					float luck =0;
					if(source.getTrueSource() instanceof EntityPlayer)
						luck = RFCalculations.getAttributeValue((EntityLivingBase) source.getTrueSource(), ItemStats.RFRLUCK, null, null);
					while(luck>=1.0F)
					{
						luck-=0.8;
						max-=1;
					}
					while(this.rand.nextFloat() < this.getDrops().get(item)+Math.max(luck-0.25F, -0.2F) && max<2)
					{
						ItemUtils.spawnLeveledItem(this, item.copy(), 1+this.rand.nextInt(10));
						max++;
					}
				}
	}
	
	private void updateStatsToLevel()
	{
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(
				LevelCalc.initStatIncreaseLevel(baseValues.get(SharedMonsterAttributes.MAX_HEALTH), this.level(), false, true));
		this.getAttributeMap().getAttributeInstance(ItemStats.RFATTACK).setBaseValue(
				LevelCalc.initStatIncreaseLevel(baseValues.get(ItemStats.RFATTACK), this.level(), false, false));
		this.getAttributeMap().getAttributeInstance(ItemStats.RFDEFENCE).setBaseValue(
				LevelCalc.initStatIncreaseLevel(baseValues.get(ItemStats.RFDEFENCE), this.level(), false, false));
		this.getAttributeMap().getAttributeInstance(ItemStats.RFMAGICATT).setBaseValue(
				LevelCalc.initStatIncreaseLevel(baseValues.get(ItemStats.RFMAGICATT), this.level(), false, false));
		this.getAttributeMap().getAttributeInstance(ItemStats.RFMAGICDEF).setBaseValue(
				LevelCalc.initStatIncreaseLevel(baseValues.get(ItemStats.RFMAGICDEF), this.level(), false, false));
		this.setHealth(this.getMaxHealth());
	}
	
	protected final void initiateBaseAttributes(IAttribute att, double value)
	{
		if(att==SharedMonsterAttributes.MAX_HEALTH)
			value*=LibConstants.DAMAGESCALE;
		this.getAttributeMap().getAttributeInstance(att).setBaseValue(value);
		if(baseValues==null)
			baseValues = Maps.newHashMap();
		this.baseValues.put(att, value);
	}

	public abstract int getAttackTimeFromPattern(byte pattern);
	public abstract int attackFromPattern();
	public abstract int maxAttackPatterns();
}
