package com.flemmli97.runecraftory.common.entity.monster.projectile;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.monster.boss.EntityAmbrosia;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.utils.RFCalculations;
import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityAmbrosiaWave extends Entity{

    private static final DataParameter<Float> radius = EntityDataManager.<Float>createKey(EntityMobBase.class, DataSerializers.FLOAT);
    private EntityAmbrosia owner;
    private int livingTick;
    private int maxTick=100;
    private Predicate<EntityLivingBase> pred;
	public EntityAmbrosiaWave(World world) {
		super(world);
		this.setSize(0.1F, 0.1F);
	}
	
	public EntityAmbrosiaWave(World world, EntityAmbrosia caster, int maxTick) {
		this(world);
		this.owner=caster;
		this.setPosition(caster.posX, caster.posY, caster.posZ);
		this.maxTick=maxTick;
	}
	
	public EntityAmbrosiaWave(World world, EntityAmbrosia caster, int maxTick, @Nullable Predicate<EntityLivingBase> exclude) {
		this(world, caster, maxTick);
		this.pred = exclude;
	}

	@Override
	protected void entityInit() {
		this.dataManager.register(radius, 0.1F);
	}
	
	public float getRadius()
	{
		return this.dataManager.get(radius);
	}
	
	public void increaseRadius()
	{
		this.dataManager.set(radius, this.getRadius()+0.13F);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		this.livingTick=compound.getInteger("livingTick");
		this.maxTick=compound.getInteger("maxTick");
		this.dataManager.set(radius, compound.getFloat("radius"));
		if(this.world instanceof WorldServer)
			try
		    {
		        Entity entity = ((WorldServer)this.world).getEntityFromUuid(UUID.fromString(compound.getString("owner")));
		
		        if (entity instanceof EntityAmbrosia)
		        {
		            this.owner = (EntityAmbrosia)entity;
		        }
		    }
		    catch (Throwable var2)
		    {
		        this.owner = null;
		        RuneCraftory.logger.error("Error loading EntityAmbrosiaWave owner uuid: " + compound.getString("owner"));
		    }
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setInteger("livingTick", this.livingTick);
		compound.setInteger("maxTick", this.maxTick);
		compound.setFloat("radius", this.getRadius());
		if(this.owner!=null)
			compound.setString("owner", this.owner.getCachedUniqueIdString());
	}
	
	@Override
	public void onUpdate()
    {
		this.livingTick++;
		if(!this.world.isRemote && (this.livingTick>this.maxTick|| (this.owner!=null && !this.owner.isEntityAlive())))
			this.setDead();
		if(this.getRadius()<=5)
			this.increaseRadius();
        List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(this.getRadius(), 1, this.getRadius()), this.pred);
        for(EntityLivingBase e : list)
        {
        		if(!e.equals(this.owner))
        		{
        			if(this.owner!=null)
        			{
        				if(RFCalculations.attackEntity(e, CustomDamage.attack(this.owner, EnumElement.NONE, CustomDamage.DamageType.NORMAL, CustomDamage.KnockBackType.BACK, 0, 15), RFCalculations.getAttributeValue(this.owner, ItemStats.RFMAGICATT, null, null)/2.5F))
    					{
        					e.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:slowness"), 10, 6, true, false));
        					e.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:jump_boost"), 10, 128, true, false));
    					}
        			}
        		}
        }
    }
	
	public EntityAmbrosia getOwner()
	{
		return this.owner;
	}

}
