package com.flemmli97.runecraftory.common.entity;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

public abstract class EntityBossBase extends EntityMobBase
{
    private BossInfoServer bossInfo = new BossInfoServer(this.getDisplayName(), BossInfo.Color.GREEN, BossInfo.Overlay.PROGRESS);
    private static DataParameter<Boolean> enraged = EntityDataManager.createKey(EntityBossBase.class, DataSerializers.BOOLEAN);

    protected EntityBossBase(World world) {
        super(world);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(enraged, false);
    }
    
    public boolean isEnraged() {
        return this.dataManager.get(enraged);
    }
    
    public void setEnraged(boolean flag) {
        this.dataManager.set(enraged, flag);
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!this.world.isRemote && !this.isTamed()) {
            this.updateplayers();
            this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        }
    }
    
    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
    	super.damageEntity(damageSrc, damageAmount);
    	if(this.getHealth()/this.getMaxHealth()<0.5)
    		this.setEnraged(true);
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("Enraged", this.isEnraged());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setEnraged(compound.getBoolean("Enraged"));
    }
    
    @Override
    public void setOwner(EntityPlayer player) {
        super.setOwner(player);
        if (player!=null) 
        {
            for (EntityPlayerMP entityplayermp1 : this.bossInfo.getPlayers()) 
            {
                this.bossInfo.removePlayer(entityplayermp1);
            }
        }
    }
    
    private void updateplayers() {
        Set<EntityPlayerMP> set = Sets.newHashSet();
        for (EntityPlayerMP entityplayermp : this.world.getEntitiesWithinAABB(EntityPlayerMP.class, this.getEntityBoundingBox().grow(10.0))) 
        {
            this.bossInfo.addPlayer(entityplayermp);
            set.add(entityplayermp);
        }
        Set<EntityPlayerMP> set2 = Sets.newHashSet(this.bossInfo.getPlayers());
        set2.removeAll(set);
        for (EntityPlayerMP entityplayermp2 : set2) 
        {     	
            this.bossInfo.removePlayer(entityplayermp2);
        }
    }
    
    @Override
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }
    
    @Override
    public void setDead() {
        this.isDead = true;
        for (EntityPlayerMP entityplayermp1 : this.bossInfo.getPlayers()) 
        {
            this.removeTrackingPlayer(entityplayermp1);
        }
    }
}
