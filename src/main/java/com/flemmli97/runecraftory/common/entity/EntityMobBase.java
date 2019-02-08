package com.flemmli97.runecraftory.common.entity;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.api.entities.EntityProperties;
import com.flemmli97.runecraftory.api.entities.IEntityAdvanced;
import com.flemmli97.runecraftory.api.entities.IEntityBase;
import com.flemmli97.runecraftory.api.items.FoodProperties;
import com.flemmli97.runecraftory.api.items.ItemStatAttributes;
import com.flemmli97.runecraftory.api.mappings.EntityStatMap;
import com.flemmli97.runecraftory.api.mappings.ItemFoodMap;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.init.EntitySpawnEggList;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.items.creative.ItemSpawnEgg;
import com.flemmli97.runecraftory.common.lib.LibConstants;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.network.PacketEntityLevelUp;
import com.flemmli97.runecraftory.common.network.PacketFoodUpdateEntity;
import com.flemmli97.runecraftory.common.network.PacketHandler;
import com.flemmli97.runecraftory.common.utils.ItemUtils;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import com.flemmli97.runecraftory.common.utils.RFCalculations;
import com.flemmli97.tenshilib.api.config.SimpleItemStackWrapper;
import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public abstract class EntityMobBase extends EntityCreature implements IEntityAdvanced, IMob, IEntityAdditionalSpawnData
{
    private Map<IAttribute, Float> genes =  Maps.newHashMap();
    protected static final DataParameter<Boolean> isTamed = EntityDataManager.createKey(EntityMobBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<String> owner = EntityDataManager.createKey(EntityMobBase.class, DataSerializers.STRING);
    private static final DataParameter<Integer> attackTimer = EntityDataManager.createKey(EntityMobBase.class, DataSerializers.VARINT);
    private static final DataParameter<Byte> attackPattern = EntityDataManager.createKey(EntityMobBase.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> mobLevel = EntityDataManager.createKey(EntityMobBase.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> levelXP = EntityDataManager.createKey(EntityMobBase.class, DataSerializers.VARINT);
    private static final UUID foodUUID = UUID.fromString("87A55C28-8C8C-4BFF-AF5F-9972A38CCD9D");
    private static final UUID foodUUIDMulti = UUID.fromString("A05442AC-381B-49DF-B0FA-0136B454157B");
    
    protected int tamingTick = -1;
    
    protected int feedTimeOut;
    private boolean doJumping = false;
    private EntityProperties prop;
    private int foodBuffTick;
    public EntityAINearestAttackableTarget<EntityPlayer> targetPlayer = new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 5, true, true, new Predicate<EntityPlayer>() {
        @Override
		public boolean apply(EntityPlayer player) {
            return !EntityMobBase.this.isTamed() || player != EntityMobBase.this.getOwner();
        }
    });
    public EntityAINearestAttackableTarget<EntityVillager> targetNPC = new EntityAINearestAttackableTarget<EntityVillager>(this, EntityVillager.class, 5, true, true, new Predicate<EntityVillager>() {
        @Override
		public boolean apply(EntityVillager mob) {
            return !EntityMobBase.this.isTamed();
        }
    });;
    public EntityAINearestAttackableTarget<EntityCreature> targetMobs = new EntityAINearestAttackableTarget<EntityCreature>(this, EntityCreature.class, 5, true, true, new Predicate<EntityCreature>() {
        @Override
		public boolean apply(EntityCreature mob) {
            if (EntityMobBase.this.isTamed()) {
                return !(mob instanceof EntityMobBase) || !((EntityMobBase)mob).isTamed();
            }
            return mob instanceof EntityMobBase && ((EntityMobBase)mob).isTamed() && IMob.VISIBLE_MOB_SELECTOR.apply(mob);
        }
    });;
    public EntityAIHurtByTarget hurt = new EntityAIHurtByTarget(this, false, new Class[0]);
    
    private Map<ItemStack, Float> drops = Maps.newHashMap();
	private Map<ItemStack, Integer> dailyDrops = Maps.newHashMap();
	private NonNullList<ItemStack> tamingItem = NonNullList.create();
	
    public EntityMobBase(World world) {
        super(world);
        this.hurt = new EntityAIHurtByTarget(this, false, new Class[0]);

        this.prop = EntityStatMap.getDefaultStats(this.getClass());
        if (this.prop == null) {
            throw new NullPointerException("Default stats of " + this.getClass() + " not registered");
        }
        
        for(Entry<SimpleItemStackWrapper, Float> entry : this.prop.drops().entrySet())
			this.drops.put(entry.getKey().getStack(), entry.getValue());
		for(Entry<SimpleItemStackWrapper, Integer> entry : this.prop.dailyDrops().entrySet())
			this.dailyDrops.put(entry.getKey().getStack(), entry.getValue());
		for(SimpleItemStackWrapper itemProp : this.prop.getTamingItem())
			this.tamingItem.add(itemProp.getStack());
		
        this.genes.put(SharedMonsterAttributes.MAX_HEALTH, 1.1f - world.rand.nextFloat() * 0.2f);
        this.genes.put(ItemStatAttributes.RFATTACK, 1.1f - world.rand.nextFloat() * 0.2f);
        this.genes.put(ItemStatAttributes.RFDEFENCE, 1.1f - world.rand.nextFloat() * 0.2f);
        this.genes.put(ItemStatAttributes.RFMAGICATT, 1.1f - world.rand.nextFloat() * 0.2f);
        this.genes.put(ItemStatAttributes.RFMAGICDEF, 1.1f - world.rand.nextFloat() * 0.2f);
        
        this.targetTasks.addTask(1, this.targetPlayer);
        this.targetTasks.addTask(2, this.targetNPC);
        this.targetTasks.addTask(3, this.targetMobs);
    }
    
    //=====Init
    
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAIMoveTowardsRestriction(this, 1.0));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0));
        this.tasks.addTask(3, new EntityAISwimming(this));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
    }

    @Override
    protected void applyEntityAttributes() {
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.2);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.24);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24.0);
        this.getAttributeMap().registerAttribute(SWIM_SPEED);
        this.getAttributeMap().registerAttribute(ItemStatAttributes.RFATTACK);
        this.getAttributeMap().registerAttribute(ItemStatAttributes.RFDEFENCE);
        this.getAttributeMap().registerAttribute(ItemStatAttributes.RFMAGICATT);
        this.getAttributeMap().registerAttribute(ItemStatAttributes.RFMAGICDEF);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(isTamed, false);
        this.dataManager.register(owner, "");
        this.dataManager.register(attackTimer, 0);
        this.dataManager.register(attackPattern, (byte)0);
        this.dataManager.register(mobLevel, LibConstants.baseLevel);
        this.dataManager.register(levelXP, 0);
    }

    //=====IRFEntity
    
    @Override
    public int level() {
        return this.dataManager.get(mobLevel);
    }
    
    public void setLevel(int level) {
        this.dataManager.set(mobLevel, Math.min(10000, level));
        this.updateStatsToLevel();
    }
    
    public void increaseLevel() {
        this.dataManager.set(mobLevel, Math.min(10000, this.level() + 1));
        if (!this.world.isRemote) {
            this.entityLevelUp();
            PacketHandler.sendToAll(new PacketEntityLevelUp(this.getEntityId()));
        }
    }
    
    public void entityLevelUp() {
        for (IAttributeInstance att : this.getAttributeMap().getAllAttributes()) 
        {
            if (LevelCalc.shouldStatIncreaseWithLevel(att.getAttribute())) 
            {
                double oldValue = this.getAttributeMap().getAttributeInstance(att.getAttribute()).getBaseValue();
                this.getAttributeMap().getAttributeInstance(att.getAttribute()).setBaseValue(LevelCalc.onEntityLevelUp(this.prop.getBaseValues().get(att.getAttribute()), oldValue, this instanceof EntityBossBase, this.genes.containsKey(att.getAttribute()) ? ((float)this.genes.get(att.getAttribute())) : 1.0f));
                if (att.getAttribute() == SharedMonsterAttributes.MAX_HEALTH) {
                    this.setHealth(this.getHealth() + (float)(this.getMaxHealth() - oldValue));
                }
            }
        }
    }
    
    public int getLevelXp() {
        return (int)this.dataManager.get(levelXP);
    }
    
    public void addXp(int amount) {
        int neededXP = LevelCalc.xpAmountForLevelUp(this.level());
        int xpToNextLevel = neededXP - this.getLevelXp();
        if (amount >= xpToNextLevel) {
            int diff = amount - xpToNextLevel;
            this.increaseLevel();
            this.addXp(diff);
        }
        else {
            this.dataManager.set(levelXP, amount);
        }
    }

    @Override
    public boolean isTamed() {
        return this.dataManager.get(isTamed);
    }
    
    public void setTamed(boolean tamed) {
        this.dataManager.set(isTamed, tamed);
    }

    @Override
    public boolean ridable() {
        return this.prop.ridable();
    }

    @Override
    public int baseXP() {
        return this.prop.getXp();
    }

    @Override
    public Map<ItemStack, Float> getDrops() {
        return this.drops;
    }

    @Override
    public Map<ItemStack, Integer> dailyDrops() {
        return this.dailyDrops;
    }

    @Override
    public float tamingChance() {
        return this.prop.tamingChance();
    }

    @Override
    public NonNullList<ItemStack> tamingItem() {
        return this.tamingItem;
    }

    @Override
    public int baseMoney() {
        return this.prop.getMoney();
    }

    @Override
    public EntityPlayer getOwner() {
        if (owner != null) {
            try {
                UUID var1 = UUID.fromString(this.dataManager.get(owner));
                return (var1 == null) ? null : this.world.getPlayerEntityByUUID(var1);
            }
            catch (IllegalArgumentException var2) {
                return null;
            }
        }
        return null;
    }

    @Override
    public void setOwner(EntityPlayer player) {
        if (player != null) {
            this.dataManager.set(owner, player.getUniqueID().toString());
        }
    }
    
    public Map<IAttribute, Float> mobGene() {
        return this.genes;
    }

    //=====Move helper
    
    @Override
    public boolean isFlyingEntity() {
        return this.prop.flying();
    }
    
    public void setDoJumping() {
        this.doJumping = true;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        if (!this.isFlyingEntity()) {
            super.fall(distance, damageMultiplier);
        }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 10) {
            this.playTameEffect(true);
        }
        else if (id == 11) {
            this.playTameEffect(false);
        }
        else if (id == 34) {
            this.tamingTick = 120;
        }
        super.handleStatusUpdate(id);
    }
    
    private void playTameEffect(boolean play) {
        EnumParticleTypes enumparticletypes = EnumParticleTypes.HEART;
        if (!play) {
            enumparticletypes = EnumParticleTypes.SMOKE_NORMAL;
        }
        for (int i = 0; i < 7; ++i) {
            double d0 = this.rand.nextGaussian() * 0.02;
            double d2 = this.rand.nextGaussian() * 0.02;
            double d3 = this.rand.nextGaussian() * 0.02;
            this.world.spawnParticle(enumparticletypes, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width, this.posY + 0.5 + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width, d0, d2, d3, new int[0]);
        }
    }
    
    public void setAttackTimeAndPattern(byte pattern) {
        this.dataManager.set(attackTimer, this.getAttackTimeFromPattern(pattern));
        this.dataManager.set(attackPattern, pattern);
    }
    
    public byte getAttackPattern() {
        return this.dataManager.get(attackPattern);
    }
    
    public int getAttackTime() {
        return this.dataManager.get(attackTimer);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("MobLevel", this.level());
        compound.setBoolean("Tamed", this.isTamed());
        NBTTagCompound nbtGene = new NBTTagCompound();
        for (Entry<IAttribute, Float> entry : this.genes.entrySet()) {
            nbtGene.setFloat(entry.getKey().getName(), entry.getValue());
        }
        compound.setTag("Genes", nbtGene);
        compound.setBoolean("Out", this.dead);
        compound.setInteger("FeedTime", this.feedTimeOut);
        compound.setIntArray("Home", new int[] { this.getHomePosition().getX(), this.getHomePosition().getY(), this.getHomePosition().getZ(), (int)this.getMaximumHomeDistance() });
        compound.setInteger("FoodBuffTick", this.foodBuffTick);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("MobLevel")) {
            this.dataManager.set(mobLevel, compound.getInteger("MobLevel"));
        }
        if (compound.hasKey("Tamed")) {
            this.setTamed(compound.getBoolean("Tamed"));
        }
        NBTTagCompound nbtGenes = compound.getCompoundTag("Genes");
        for (String s : nbtGenes.getKeySet()) {
            this.genes.put(ItemUtils.getAttFromName(s), nbtGenes.getFloat(s));
        }
        this.feedTimeOut = compound.getInteger("FeedTime");
        if (compound.hasKey("Home")) {
            int[] home = compound.getIntArray("Home");
            this.setHomePosAndDistance(new BlockPos(home[0], home[1], home[2]), home[3]);
        }
        this.dead = compound.getBoolean("Out");
        this.foodBuffTick=compound.getInteger("FoodBuffTick");
    }

    //=====Disable/Tweak vanilla
    
    @Override
    protected boolean canEquipItem(ItemStack stack) {
        return false;
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
    }
    
    //=====Interact
    
    @Override
	public void onLivingUpdate() {
        if (!this.dead) {
            if (this.tamingTick > 0) {
                --this.tamingTick;
            }
            if (this.tamingTick == 0) {
                if (this.isTamed()) {
                    this.world.setEntityState((Entity)this, (byte)10);
                }
                else {
                    this.world.setEntityState((Entity)this, (byte)11);
                }
                this.tamingTick = -1;
            }
            if (this.feedTimeOut > 0) {
                --this.feedTimeOut;
            }
            super.onLivingUpdate();
            if (this.getAttackTime() > 0) {
                this.dataManager.set(attackTimer, (this.getAttackTime() - 1));
            }
            this.foodBuffTick=Math.max(-1, --this.foodBuffTick);
            if(this.foodBuffTick==0)
            {
            	this.removeFoodEffect();
            }
        }
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        ResourceLocation name = EntityList.getKey((Entity)this);
        if (name != null && EntitySpawnEggList.entityEggs.containsKey(name)) {
            ItemStack stack = new ItemStack(ModItems.spawnEgg);
            ItemSpawnEgg.applyEntityIdToItemStack(stack, name);
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItemMainhand();
		if(!stack.isEmpty() && player.isSneaking())
		{
			if(this.world.isRemote)
				return true;
			if(stack.getItem()==ModItems.tame)
			{
				this.tameEntity(player);
			}
			else if(!this.isTamed() && this.tamingTick==-1 && this.deathTime==0)
			{
				boolean flag=false;
				if(this.tamingItem()!=null)
					for(ItemStack item : this.tamingItem())
						if(OreDictionary.itemMatches(item, stack, false))
							flag=true;
				float rightItemMultiplier = flag ? 2 : 1F;
				if(!player.capabilities.isCreativeMode)
					stack.shrink(1);
				if(this.rand.nextFloat()<=this.tamingChance()*rightItemMultiplier*LevelCalc.tamingMultiplerOnLevel(this.level()))
				{
					this.tameEntity(player);
				}
				if(stack.getItem() instanceof ItemFood)
				{
					this.applyFoodEffect(stack);
				}
				this.tamingTick=100;
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
				else if(feedTimeOut<=0 && stack.getItem() instanceof ItemFood)
				{
					this.applyFoodEffect(stack);
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
    
    protected void tameEntity(EntityPlayer owner) {
        this.setTamed(true);
        this.setOwner(owner);
        this.navigator.clearPath();
        this.setAttackTarget((EntityLivingBase)null);
        this.world.setEntityState((Entity)this, (byte)10);
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
    protected void addPassenger(Entity passenger) {
        this.targetTasks.removeTask(this.targetPlayer);
        this.targetTasks.removeTask(this.targetNPC);
        this.targetTasks.removeTask(this.targetMobs);
        super.addPassenger(passenger);
    }

    @Override
    protected void removePassenger(Entity passenger) {
        this.targetTasks.addTask(1, this.targetPlayer);
        this.targetTasks.addTask(2, this.targetNPC);
        this.targetTasks.addTask(3, this.targetMobs);
        super.removePassenger(passenger);
    }

    @Override
    protected boolean isMovementBlocked() {
        return super.isMovementBlocked() && this.isBeingRidden();
    }
    
    @Nullable
    @Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }
    
    @Override
    public void travel(float strafing, float upward, float forward) 
    {
        if (this.isBeingRidden() && this.canBeSteered() && this.getControllingPassenger() instanceof EntityLivingBase) 
        {
            EntityLivingBase entitylivingbase = (EntityLivingBase)this.getControllingPassenger();
            this.rotationYaw = entitylivingbase.rotationYaw;
            this.prevRotationYaw = this.rotationYaw;
            this.rotationPitch = entitylivingbase.rotationPitch * 0.5f;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.renderYawOffset;
            strafing = entitylivingbase.moveStrafing * 0.5f;
            forward = entitylivingbase.moveForward;
            if (forward <= 0.0f) {
                forward *= 0.25f;
            }
            if (this.doJumping && this.onGround && !this.isFlyingEntity()) 
            {
                this.isAirBorne = true;
                this.jump();
                if (forward > 0.0f) {
                    float f = MathHelper.sin(this.rotationYaw * 0.017453292f);
                    float f2 = MathHelper.cos(this.rotationYaw * 0.017453292f);
                    this.motionX += -0.4f * f;
                    this.motionZ += 0.4f * f2;
                }
            }
            else if (this.doJumping && this.isFlyingEntity()) 
            {
                this.motionY += 0.15000000596046448;
                this.motionY = Math.min(this.motionY, 1.5);
            }
            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1f;
            if (this.canPassengerSteer()) {
                this.setAIMoveSpeed((float)this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
                super.travel(strafing, upward, forward);
            }
            else if (entitylivingbase instanceof EntityPlayer) {
                this.motionX = 0.0;
                this.motionY = 0.0;
                this.motionZ = 0.0;
            }
            if (this.onGround || this.isFlyingEntity()) {
                this.doJumping = false;
            }
            this.jumpMovementFactor = 0.5f;
            this.prevLimbSwingAmount = this.limbSwingAmount;
            double d1 = this.posX - this.prevPosX;
            double d2 = this.posZ - this.prevPosZ;
            float f3 = MathHelper.sqrt(d1 * d1 + d2 * d2) * 4.0f;
            if (f3 > 1.0f) {
                f3 = 1.0f;
            }
            this.limbSwingAmount += (f3 - this.limbSwingAmount) * 0.4f;
            this.limbSwing += this.limbSwingAmount;
        }
        else {
            this.jumpMovementFactor = 0.02f;
            super.travel(strafing, forward, forward);
        }
    }
    
    //=====Combat stuff
    
    @Override
    protected void onDeathUpdate() {
        if (!this.isTamed()) 
        {
            super.onDeathUpdate();
            if (this.deathTime == 5 && this.attackingPlayer != null) 
            {
                IPlayer cap = this.attackingPlayer.getCapability(PlayerCapProvider.PlayerCap, null);
                cap.addXp(this.attackingPlayer, LevelCalc.xpFromLevel(this.baseXP(), this.level()));
                cap.setMoney(this.attackingPlayer, cap.getMoney() + LevelCalc.moneyFromLevel(this.baseMoney(), this.level()));
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return (source.getTrueSource() == null|| this.canAttackFrom(source.getTrueSource().getPosition())) && super.attackEntityFrom(source, amount);
    }
    
    private boolean canAttackFrom(BlockPos pos) {
        return this.getMaximumHomeDistance() == -1.0f || this.getHomePosition().distanceSq((Vec3i)pos) < (this.getMaximumHomeDistance() + 5.0f) * (this.getMaximumHomeDistance() + 5.0f);
    }

    //Damage reduction
    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (damageSrc.getTrueSource() != null && !this.isWithinHomeDistanceFromPosition(damageSrc.getTrueSource().getPosition())) 
        {
            return;
        }
        if (!this.isEntityInvulnerable(damageSrc)) 
        {
            damageAmount = ForgeHooks.onLivingHurt((EntityLivingBase)this, damageSrc, damageAmount);
            if (damageAmount <= 0.0f) 
            {
                return;
            }
            damageAmount = this.reduceDamage(damageSrc, damageAmount);
            if (damageAmount != 0.0f) {
                float f1 = this.getHealth();
                this.setHealth(f1 - damageAmount);
                this.getCombatTracker().trackDamage(damageSrc, f1, damageAmount);
            }
        }
    }
    
    protected float reduceDamage(DamageSource damageSrc, float damageAmount) {
        float reduce = 0.0f;
        RFCalculations.elementalReduction(this, damageSrc, damageAmount);
        if (!damageSrc.isDamageAbsolute()) 
        {
        	if(!damageSrc.isUnblockable())
        	{
	            if (damageSrc.isMagicDamage()) 
	                reduce = (float)this.getEntityAttribute(ItemStatAttributes.RFMAGICDEF).getAttributeValue();
	            else
	                reduce = (float)this.getEntityAttribute(ItemStatAttributes.RFDEFENCE).getAttributeValue();
        	}
            if (this.isPotionActive(MobEffects.RESISTANCE) && damageSrc != DamageSource.OUT_OF_WORLD)
            {
                int i = (this.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
                int j = 25 - i;
                float f = damageAmount * (float)j;
                damageAmount = f / 25.0F;
            }
        }
        return Math.max(0.0f, damageAmount - reduce);
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) 
    {
        return this.attackEntityAsMobWithElement(entity, EnumElement.NONE);
    }
    
    public boolean attackEntityAsMobWithElement(Entity entity, EnumElement element) 
    {
        if (entity instanceof EntityLivingBase) 
        {
            float damage = (float)this.getEntityAttribute(ItemStatAttributes.RFATTACK).getAttributeValue();
            boolean faintChance = this.world.rand.nextInt(100) < RFCalculations.getAttributeValue((EntityLivingBase)this, ItemStatAttributes.RFFAINT, (EntityLivingBase)entity, ItemStatAttributes.RFRESFAINT);
            boolean critChance = this.world.rand.nextInt(100) < RFCalculations.getAttributeValue((EntityLivingBase)this, ItemStatAttributes.RFCRIT, (EntityLivingBase)entity, ItemStatAttributes.RFRESCRIT);
            boolean knockBackChance = this.world.rand.nextInt(100) < RFCalculations.getAttributeValue((EntityLivingBase)this, ItemStatAttributes.RFKNOCK, (EntityLivingBase)entity, SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
            int i = knockBackChance ? 3 : 1;
            if (!(entity instanceof IEntityBase) && !(entity instanceof EntityPlayer)) 
            {
                damage = LevelCalc.scaleForVanilla(damage);
            }
            else 
            {          	
                damage = (faintChance ? Float.MAX_VALUE : damage);
            }
            boolean ignoreArmor = faintChance || critChance;
            CustomDamage source = CustomDamage.attack((EntityLivingBase)this, element, ignoreArmor ? CustomDamage.DamageType.IGNOREDEF : CustomDamage.DamageType.NORMAL, CustomDamage.KnockBackType.VANILLA, i * 0.5f - 0.2f, 5);
            if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL) 
            {
                damage = 0.0f;
            }
            boolean flag = entity.attackEntityFrom(source, damage);
            if (flag) 
            {
                if (entity instanceof EntityLivingBase) 
                {
                    RFCalculations.knockBack((EntityLivingBase) entity, source);
                    this.motionX *= 0.6;
                    this.motionZ *= 0.6;
                }
                float drainPercent = RFCalculations.getAttributeValue((EntityLivingBase)this, ItemStatAttributes.RFDRAIN, (EntityLivingBase)entity, ItemStatAttributes.RFRESDRAIN) / 100.0f;
                if (drainPercent > 0.0f) 
                {
                    this.setHealth(this.getHealth() + drainPercent * damage);
                }
                RFCalculations.spawnElementalParticle(entity, element);
                RFCalculations.applyStatusEffects((EntityLivingBase)this, (EntityLivingBase)entity);
                this.applyEnchantments((EntityLivingBase)this, entity);
            }
            return flag;
        }
        return false;
    }

    @Override
    public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio) 
    {
        strength *= 0.85f;
        super.knockBack(entityIn, strength, xRatio, zRatio);
    }

    @Override
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) 
    {
        if (!this.isTamed() && this.getDrops() != null) 
        {
            for (Entry<ItemStack, Float> entry : this.getDrops().entrySet()) 
            {
                int max = 0;
                float luck = 0.0f;
                if (source.getTrueSource() instanceof EntityPlayer) 
                {
                    luck = RFCalculations.getAttributeValue((EntityLivingBase)source.getTrueSource(), ItemStatAttributes.RFRLUCK, null, null);
                }
                while (luck >= 1.0f) 
                {
                    luck -= 0.8;
                    --max;
                }
                while (this.rand.nextFloat() < entry.getValue() + Math.max(luck - 0.25f, -0.2f) && max < 2) 
                {
                    ItemUtils.spawnLeveledItem((EntityLivingBase)this, entry.getKey().copy(), 1 + this.rand.nextInt(10));
                    ++max;
                }
            }
        }
    }
    
    private void updateStatsToLevel() 
    {
        for (IAttribute att : this.prop.getBaseValues().keySet()) 
        {
            double leveledValue = this.prop.getBaseValues().get(att);
            if (LevelCalc.shouldStatIncreaseWithLevel(att)) 
            {
                leveledValue = LevelCalc.initStatIncreaseLevel(this.prop.getBaseValues().get(att), this.level(), this instanceof EntityBossBase, this.genes.containsKey(att) ? ((float)this.genes.get(att)) : 1.0f);
            }
            if (this.getAttributeMap().getAttributeInstance(att) == null) 
            {
                this.getAttributeMap().registerAttribute(att).setBaseValue(leveledValue);
            }
            else 
            {
                this.getAttributeMap().getAttributeInstance(att).setBaseValue(leveledValue);
            }
        }
        this.setHealth(this.getMaxHealth());
    }
    
    public abstract int getAttackTimeFromPattern(byte pattern);
    
    public abstract int attackFromPattern();
    
    public abstract int maxAttackPatterns();

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        NBTTagCompound compound = new NBTTagCompound();
        NBTTagCompound nbtGene = new NBTTagCompound();
        for (Entry<IAttribute, Float> entry : this.genes.entrySet()) {
            nbtGene.setFloat(entry.getKey().getName(), entry.getValue());
        }
        compound.setTag("Genes", nbtGene);
        compound.setInteger("Level", this.level());
        ByteBufUtils.writeTag(buffer, compound);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        NBTTagCompound compound = ByteBufUtils.readTag(additionalData);
        NBTTagCompound nbtGenes = compound.getCompoundTag("Genes");
        for (String s : nbtGenes.getKeySet()) {
            this.genes.put(ItemUtils.getAttFromName(s), nbtGenes.getFloat(s));
        }
        this.setLevel(compound.getInteger("Level"));
    }

    @Override
	public void applyFoodEffect(ItemStack stack)
	{
    	this.removeFoodEffect();
    	FoodProperties food = ItemFoodMap.get(stack);
    	if(food==null)
    		return;
		for(Entry<IAttribute, Float> entry : food.effectsMultiplier().entrySet())
		{
			if(this.getAttributeMap().getAttributeInstance(entry.getKey())==null)
				this.getAttributeMap().registerAttribute(entry.getKey());
			double base = this.getAttributeMap().getAttributeInstance(entry.getKey()).getBaseValue();
			this.getAttributeMap().getAttributeInstance(entry.getKey()).applyModifier(new AttributeModifier(foodUUIDMulti, "foodBuffMulti "+entry.getKey().getName(), base*entry.getValue(), 0));
		}
		for(Entry<IAttribute, Integer> entry : food.effects().entrySet())
		{
			if(this.getAttributeMap().getAttributeInstance(entry.getKey())==null)
				this.getAttributeMap().registerAttribute(entry.getKey());
			this.getAttributeMap().getAttributeInstance(entry.getKey()).applyModifier(new AttributeModifier(foodUUID, "foodBuff "+entry.getKey().getName(), entry.getValue(), 0));
		}
		this.foodBuffTick=food.duration();
		if(!this.world.isRemote)
		{
			this.heal(food.getHPGain());
			this.heal(this.getMaxHealth()*food.getHpPercentGain()*0.01F);
			PacketHandler.sendToAll(new PacketFoodUpdateEntity(this, food.effects(), food.effectsMultiplier(), food.duration()));
		}
	}
    
    @SideOnly(Side.CLIENT)
    @Override
	public void applyFoodEffect(Map<IAttribute, Integer> stats, Map<IAttribute, Float> multi, int duration)
	{
		this.removeFoodEffect();
		for(Entry<IAttribute, Float> entry : multi.entrySet())
		{
			if(this.getAttributeMap().getAttributeInstance(entry.getKey())==null)
				this.getAttributeMap().registerAttribute(entry.getKey());
			double base = this.getAttributeMap().getAttributeInstance(entry.getKey()).getBaseValue();
			this.getAttributeMap().getAttributeInstance(entry.getKey()).applyModifier(new AttributeModifier(foodUUIDMulti, "foodBuffMulti "+entry.getKey().getName(), base*entry.getValue(), 0));
		}
		for(Entry<IAttribute, Integer> entry : stats.entrySet())
		{
			if(this.getAttributeMap().getAttributeInstance(entry.getKey())==null)
				this.getAttributeMap().registerAttribute(entry.getKey());
			this.getAttributeMap().getAttributeInstance(entry.getKey()).applyModifier(new AttributeModifier(foodUUID, "foodBuff "+entry.getKey().getName(), entry.getValue(), 0));
		}
		this.foodBuffTick=duration;
	}
    
    @Override
   	public void removeFoodEffect()
   	{
    	for(IAttributeInstance ins : this.getAttributeMap().getAllAttributes())
    	{
    		ins.removeModifier(foodUUID);
    		ins.removeModifier(foodUUIDMulti);
    	}
   	}
}
