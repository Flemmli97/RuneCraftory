package com.flemmli97.runecraftory.common.entity.monster.projectile;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.api.items.ItemStatAttributes;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.entity.monster.boss.EntityAmbrosia;
import com.flemmli97.runecraftory.common.lib.LibParticles;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.utils.RFCalculations;
import com.flemmli97.tenshilib.common.world.Particles;
import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityAmbrosiaSleep extends Entity
{
    private EntityAmbrosia owner;
    private int livingTick;
    private Predicate<EntityLivingBase> pred;
    
    public EntityAmbrosiaSleep(World world) {
        super(world);
        this.setSize(0.6f, 1.2f);
    }
    
    public EntityAmbrosiaSleep(World world, EntityAmbrosia caster) {
        this(world);
        this.owner = caster;
        this.setPosition(caster.posX, caster.posY, caster.posZ);
    }
    
    public EntityAmbrosiaSleep(World world, EntityAmbrosia caster, @Nullable Predicate<EntityLivingBase> exclude) {
        this(world, caster);
        this.pred = exclude;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.livingTick = compound.getInteger("livingTick");
        if (this.world instanceof WorldServer) {
            try {
                Entity entity = ((WorldServer)this.world).getEntityFromUuid(UUID.fromString(compound.getString("owner")));
                if (entity instanceof EntityAmbrosia) {
                    this.owner = (EntityAmbrosia)entity;
                }
            }
            catch (Throwable var2) {
                this.owner = null;
                LibReference.logger.error("Error loading EntityAmbrosiaWave owner uuid: {}", compound.getString("owner"));
            }
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("livingTick", this.livingTick);
        if (this.owner != null) {
            compound.setString("owner", this.owner.getCachedUniqueIdString());
        }
    }

    @Override
    public void onUpdate() {
        ++this.livingTick;
        if(this.world.isRemote) {
            for(int i = 0; i < 4; i++) {
                Particles.spawnParticle(LibParticles.particleFlame, this.world, this.posX, this.posY+0.35, this.posZ, rand.nextGaussian() * 0.008D, Math.abs(rand.nextGaussian() * 0.025), rand.nextGaussian() * 0.009D);
            }
        }
        if (this.livingTick > 90) {
            this.setDead();
        }
        if (!this.world.isRemote) {
            List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(0.3), this.pred);
            for (EntityLivingBase e : list) {
                if (!e.equals(this.owner)) {
                    if (RFCalculations.attackEntity(e, CustomDamage.attack(this.owner, EnumElement.EARTH, CustomDamage.DamageType.NORMAL, CustomDamage.KnockBackType.BACK, 0.0f, 20), RFCalculations.getAttributeValue(this.owner, ItemStatAttributes.RFMAGICATT, null, null) / 3.0f)){                  
                        e.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:blindness"), 80, 1, true, false));
                        e.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("runecraftory:sleep"), 80, 1, true, false));
                        this.setDead();
                    }
                }
            }
        }
    }
    
    public EntityAmbrosia getOwner() {
        return this.owner;
    }
}
