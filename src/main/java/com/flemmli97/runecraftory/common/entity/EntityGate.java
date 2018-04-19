package com.flemmli97.runecraftory.common.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.api.entities.IEntityBase;
import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.init.GateSpawning;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.LibConstants;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.utils.ItemUtils;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import com.flemmli97.runecraftory.common.utils.RFCalculations;
import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class EntityGate extends EntityLiving  implements IEntityBase{

	private int baseXP, baseMoney;
	private List<String> spawnList = new ArrayList<String>();
	private EnumElement type = EnumElement.NONE;
	public int rotate;
    private static final DataParameter<String> elementType = EntityDataManager.<String>createKey(EntityMobBase.class, DataSerializers.STRING);
    private static final DataParameter<Integer> level = EntityDataManager.<Integer>createKey(EntityMobBase.class, DataSerializers.VARINT);

	public EntityGate(World world) {
		super(world);
		this.setSize(1, 1);
		this.baseXP=10;
		this.baseMoney=4;
		if(world.isRemote)
			rotate=world.rand.nextInt(2)==0?1:-1;
	}
	
	//=====IEntityBase
	
	@Override
	public int level() {
		return this.dataManager.get(level);
	}
	
	public EnumElement getElement()
	{
		return this.type;
	}
	
	public String elementName()
	{
		return this.dataManager.get(elementType);
	}

	@Override
	public int baseXP() {
		return this.baseXP;
	}

	@Override
	public int baseMoney() {
		return this.baseMoney;
	}

	//Unused for this
	@Override
	public Map<ItemStack, Float> getDrops() {
		return null;
	}
	//=====Init

	@Override
	protected void applyEntityAttributes() {
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100*LibConstants.DAMAGESCALE);;
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);;
        this.getAttributeMap().registerAttribute(ItemStats.RFDEFENCE).setBaseValue(5);;
        this.getAttributeMap().registerAttribute(ItemStats.RFMAGICDEF).setBaseValue(5);;
	}
	@Override
	protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(elementType, "none");
        this.dataManager.register(level, LibConstants.baseLevel);
    }
	//=====NBT
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("levelMob", this.dataManager.get(level));
		for(int i = 0; i < this.spawnList.size();i++)
		{
			compound.setString("spawnList["+i+"]", spawnList.get(i));
		}
		compound.setInteger("size", spawnList.size());
		compound.setString("element", this.type.getName());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		if(compound.hasKey("levelMob"))
			this.dataManager.set(level, compound.getInteger("levelMob"));
		for(int i = 0; i < compound.getInteger("size");i++)
			this.spawnList.add(compound.getString("spawnList[" + i + "]"));
		for(EnumElement element : EnumElement.values())
		{
			if(element.getName().equals(compound.getString("element")))
			{
				this.type=element;
				this.dataManager.set(elementType, element.getName());
			}
		}
	}

	//=====Disable vanilla
	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {}
	@Override
	public void heal(float healAmount){}
	@Override
	public Iterable<ItemStack> getArmorInventoryList() {return NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY)	;}
	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {return ItemStack.EMPTY;}
	@Override
	public EnumHandSide getPrimaryHand() {return EnumHandSide.RIGHT;}

	//=====Spawning Logic
	@Override
	public void onLivingUpdate() {
		if(this.rand.nextInt(100)<1)
		{
			this.spawnMobs();
		}
	}
	
	private void spawnMobs()
	{
		if(this.spawnList!=null && !this.spawnList.isEmpty())
		{
			int randAmount = this.rand.nextInt(4)+1;			
			List<Entity> nearby = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().grow(12.0), new Predicate<Entity>() {
				@Override
				public boolean apply(Entity entity) {
					return entity instanceof IEntityBase && !(entity instanceof EntityGate);}});
			if(nearby.size()<=6)
			for(int amount = 0; amount < randAmount; amount++)
			{
				double x = this.posX+this.rand.nextInt(9)-4;
				double y = this.posY+this.rand.nextInt(2)-1;
				double z = this.posZ+this.rand.nextInt(9)-4;
				int entityLevel = this.dataManager.get(level);
				int levelPerc = Math.round(entityLevel*0.1F);
				int levelRand = Math.round(entityLevel + (this.rand.nextFloat()-0.5F) * levelPerc);
				EntityMobBase mob = (EntityMobBase) GateSpawning.entityFromString(world, this.spawnList.get(this.rand.nextInt(spawnList.size())));
				this.doSpawnEntity(mob, x, y, z, levelRand);
			}
		}
	}
	
	private void doSpawnEntity(EntityMobBase mob, double x, double y, double z, int level)
	{
		if(mob!=null)
		{
			BlockPos pos = new BlockPos(x,y,z);
			if(this.world.getBlockState(pos.down()).isSideSolid(this.world, pos, EnumFacing.UP) && this.world.isAirBlock(pos.up()) && this.world.isAirBlock(pos.up(2)))
			{
				mob.setLevel(level);
				mob.setPositionAndRotation(x, y, z, world.rand.nextFloat() * 360.0F, 0);
				if (!net.minecraftforge.event.ForgeEventFactory.doSpecialSpawn(mob, this.world, (float)mob.posX, (float)mob.posY, (float)mob.posZ))
				{
	                ((EntityMobBase)mob).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(mob)), (IEntityLivingData)null);
					mob.setHomePosAndDistance(this.getPosition(), 16);
					if(!this.world.isRemote)
						this.world.spawnEntity(mob);
		            if(this.world.isRemote)
		            		mob.spawnExplosionParticle();
				}
			}
		}
	}
	
	@Override
	public boolean getCanSpawnHere()
    {
        return this.world.getDifficulty() != EnumDifficulty.PEACEFUL && super.getCanSpawnHere() 
    			&& this.world.getEntitiesWithinAABB(EntityGate.class, this.getEntityBoundingBox().grow(48.0)).size()<2;
    }
	//=====Combat stuff
	
	@Override
	protected void onDeathUpdate() {
		this.deathTime++;
		if(deathTime==5)
			if( this.attackingPlayer!=null)
            {
    				IPlayer cap = this.attackingPlayer.getCapability(PlayerCapProvider.PlayerCap, null);
    				cap.addXp(attackingPlayer, LevelCalc.xpFromLevel(this.baseXP(), this.level()));
    				cap.setMoney(attackingPlayer, cap.getMoney()+ LevelCalc.xpFromLevel(this.baseMoney(), this.level()));
            }
        super.onDeathUpdate();
	}

	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount) 
	{
		if (!this.isEntityInvulnerable(damageSrc))
        {
            damageAmount = net.minecraftforge.common.ForgeHooks.onLivingHurt(this, damageSrc, damageAmount);
            if (damageAmount <= 0) 
            		return;
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
	
	protected float reduceDamage(DamageSource damageSrc, float damageAmount)
	{
		float reduce = 0;
		if(!damageSrc.isDamageAbsolute() && !damageSrc.isUnblockable())
			if(damageSrc.isMagicDamage())
			{
				reduce = (float)this.getEntityAttribute(ItemStats.RFMAGICDEF).getAttributeValue();
			}
			else
			{
				reduce = (float)this.getEntityAttribute(ItemStats.RFDEFENCE).getAttributeValue();
			}
		return Math.max(0, damageAmount-reduce);
	}
	
	@Override
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
		int max = 0;
		float luck =0;
		if(source.getTrueSource() instanceof EntityPlayer)
			luck = RFCalculations.getAttributeValue((EntityLivingBase) source.getTrueSource(), ItemStats.RFRLUCK, null, null);
		while(luck>=1.0F)
		{
			luck-=0.8;
			max-=1;
		}
		switch(this.type)
		{
			case DARK:
				while(max<2)
				{
					if(this.rand.nextFloat() < 0.1+luck)
						ItemUtils.spawnItemAtEntity(this, new ItemStack(ModItems.crystal, 1, 5));
					max++;
				}
				break;
			case EARTH:
				while(max<2)
				{
					if(this.rand.nextFloat() < 0.25+luck)
						ItemUtils.spawnItemAtEntity(this, new ItemStack(ModItems.crystal, 1, 1));
					max++;
				}
				break;
			case FIRE:
				while(max<2)
				{
					if(this.rand.nextFloat() < 0.25+luck)
						ItemUtils.spawnItemAtEntity(this, new ItemStack(ModItems.crystal, 1, 2));
					max++;
				}
				break;
			case LIGHT:
				while(max<2)
				{
					if(this.rand.nextFloat() < 0.1+luck)
						ItemUtils.spawnItemAtEntity(this, new ItemStack(ModItems.crystal, 1, 4));
					max++;
				}
				break;
			case LOVE:
				while(max<1)
				{
					if(this.rand.nextFloat() < 0.05+luck)
						ItemUtils.spawnItemAtEntity(this, new ItemStack(ModItems.crystal, 1, 6));
					max++;
				}
				break;
			case NONE:
				break;
			case WATER:
				while(max<2)
				{
					if(this.rand.nextFloat() < 0.3+luck)
						ItemUtils.spawnItemAtEntity(this, new ItemStack(ModItems.crystal, 1, 0));
					max++;
				}
				break;
			case WIND:
				while(max<2)
				{
					if(this.rand.nextFloat() < 0.3+luck)
						ItemUtils.spawnItemAtEntity(this, new ItemStack(ModItems.crystal, 1, 3));
					max++;
				}
				break;
		}
	}
	
	@Override
	@Nullable
    public AxisAlignedBB getCollisionBox(Entity entityIn)
    {
        return entityIn.getEntityBoundingBox();
    }
	
	@Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox()
    {
        return this.getEntityBoundingBox();
    }

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		Biome biome = this.world.getBiome(getPosition());
		List<String> list = GateSpawning.getSpawningListFromBiome(biome);
		int counter = list.size()<3?list.size():3;
		while(counter>0)
		{
			String randEnt = GateSpawning.getSpawningListFromBiome(biome).get(this.rand.nextInt(GateSpawning.getSpawningListFromBiome(biome).size()));
			if(!spawnList.contains(randEnt))
			{
				this.spawnList.add(randEnt);
				counter--;
			}
		}
		this.dataManager.set(level,LevelCalc.levelFromDistSpawn(this.world, this.getPosition()));
		this.type = this.getType(biome);
		this.dataManager.set(elementType, this.type.getName());
		this.posY+=1;
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(
				LevelCalc.initStatIncreaseLevel((float) this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).getBaseValue(), this.dataManager.get(level), false, true));
		this.getAttributeMap().getAttributeInstance(ItemStats.RFDEFENCE).setBaseValue(
				LevelCalc.initStatIncreaseLevel((float) this.getAttributeMap().getAttributeInstance(ItemStats.RFDEFENCE).getBaseValue(), this.dataManager.get(level), false, false));
		this.getAttributeMap().getAttributeInstance(ItemStats.RFMAGICDEF).setBaseValue(
				LevelCalc.initStatIncreaseLevel((float) this.getAttributeMap().getAttributeInstance(ItemStats.RFMAGICDEF).getBaseValue(), this.dataManager.get(level), false, false));
		this.setHealth(this.getMaxHealth());
		return livingdata;
	}
	
	
	private EnumElement getType(Biome biome)
	{
		EnumElement element = EnumElement.values()[this.world.rand.nextInt(EnumElement.values().length)];
		if(BiomeDictionary.getTypes(biome).contains(Type.PLAINS)&&this.world.rand.nextFloat()<0.5)
		{
			element=EnumElement.NONE;
		}
		else if(BiomeDictionary.getTypes(biome).contains(Type.FOREST)&&this.world.rand.nextFloat()<0.5)
		{
			element=EnumElement.WIND;
		}
		else if(BiomeDictionary.getTypes(biome).contains(Type.HOT)&&this.world.rand.nextFloat()<0.5)
		{
			element=EnumElement.FIRE;
		}
		else if(BiomeDictionary.getTypes(biome).contains(Type.MOUNTAIN)&&this.world.rand.nextFloat()<0.5)
		{
			element=EnumElement.WIND;
		}
		else if(BiomeDictionary.getTypes(biome).contains(Type.OCEAN)&&this.world.rand.nextFloat()<0.5)
		{
			element=EnumElement.WATER;
		}
		else if(BiomeDictionary.getTypes(biome).contains(Type.SANDY)&&this.world.rand.nextFloat()<0.5)
		{
			element=EnumElement.EARTH;
		}
		else if(BiomeDictionary.getTypes(biome).contains(Type.MAGICAL))
		{
			if(this.world.rand.nextFloat()<0.4)
				element=EnumElement.LIGHT;
			else if(this.world.rand.nextFloat()<0.2)
				element=EnumElement.LOVE;
		}
		else if(BiomeDictionary.getTypes(biome).contains(Type.SPOOKY)&&this.world.rand.nextFloat()<0.4)
		{
			element=EnumElement.DARK;
		}
		if(biome == Biomes.SKY)
		{
			if(this.world.rand.nextFloat()<0.3)
				element=EnumElement.DARK;
			else if(this.world.rand.nextFloat()<0.3)
				element=EnumElement.LIGHT;
		}
		return element;
	}	
	
	//@Override
	public EnumElement entityElement() {
		return this.type;
	}
}
