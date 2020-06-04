package com.flemmli97.runecraftory.common.entity.monster.projectile;

import com.flemmli97.runecraftory.api.items.ItemStatAttributes;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.entity.monster.boss.EntityAmbrosia;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.utils.RFCalculations;
import com.flemmli97.tenshilib.common.entity.EntityUtil;
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

import java.util.List;
import java.util.UUID;

public class EntityAmbrosiaWave extends Entity
{
    private static final DataParameter<Float> radius = EntityDataManager.createKey(EntityAmbrosiaWave.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> maxTick = EntityDataManager.createKey(EntityAmbrosiaWave.class, DataSerializers.VARINT);

    private UUID ownerUUID;
    private EntityAmbrosia owner;
    private int livingTick;
    private Predicate<EntityLivingBase> pred;
    
    public float[] clientWaveSizes = new float[] {0,-1,-2,-3,-4};
    public float clientAlphaMult=1;
    public static final float circleInc = 0.2f;
    public static final int timeTillFull = 20;
    
    public EntityAmbrosiaWave(World world) {
        super(world);
        this.setSize(0.1f, 0.1f);
    }
    
    public EntityAmbrosiaWave(World world, EntityAmbrosia caster, int maxLivingTick) {
        this(world);
        this.owner = caster;
        this.ownerUUID = caster.getUniqueID();
        this.setPosition(caster.posX, caster.posY, caster.posZ);
        this.dataManager.set(maxTick, maxLivingTick);
        this.pred = caster.attackPred;
    }

    @Override
    protected void entityInit() {
        this.dataManager.register(radius, 0f);
        this.dataManager.register(maxTick, 200);
    }
    
    public float getRadius() {
        return (float)this.dataManager.get(radius);
    }
    
    public void increaseRadius() {
        this.dataManager.set(radius, (this.getRadius() + circleInc));
    }

    protected EntityAmbrosia getOwner() {
        if (this.owner == null) {
            this.owner = EntityUtil.findFromUUID(EntityAmbrosia.class, this.world, this.ownerUUID);
            if (this.owner != null)
                this.pred = this.owner.attackPred;
        }
        return this.owner;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.livingTick = compound.getInteger("livingTick");
        this.dataManager.set(maxTick, compound.getInteger("maxTick"));
        this.dataManager.set(radius, compound.getFloat("radius"));
        this.ownerUUID = UUID.fromString(compound.getString("owner"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("livingTick", this.livingTick);
        compound.setInteger("maxTick", this.dataManager.get(maxTick));
        compound.setFloat("radius", this.getRadius());
        if (this.owner != null) {
            compound.setString("owner", this.owner.getUniqueID().toString());
        }
    }

    @Override
    public void onUpdate() {
        ++this.livingTick;
        if(this.world.isRemote) {
            for(int i = 0; i < this.clientWaveSizes.length; i++) {
                if(this.clientWaveSizes[i]>-100) {
                    this.clientWaveSizes[i]+=circleInc;
                    if(this.clientWaveSizes[i]>5)
                        if(this.dataManager.get(maxTick)>this.livingTick)
                            this.clientWaveSizes[i]=0;
                        else
                            this.clientWaveSizes[i]=-100;
                }
            }
            if(this.dataManager.get(maxTick)<this.livingTick)
                this.clientAlphaMult=Math.max(0.2f, this.clientAlphaMult-0.1f);
        }
        if (!this.world.isRemote) {
            if(this.livingTick > this.dataManager.get(maxTick)+timeTillFull || (this.owner != null && !this.owner.isEntityAlive()))
                this.setDead();
            if (this.getRadius() <= 5.0f) {
                this.increaseRadius();
            }
            if(this.livingTick < this.dataManager.get(maxTick)) {
                List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(this.getRadius(), 1.0, this.getRadius()), (e)->e.getDistanceSq(this)<=this.getRadius()*this.getRadius() && (this.pred==null|| this.pred.apply(e)));
                for (EntityLivingBase e : list) {
                    if (!e.equals(this.getOwner()) && (this.pred==null || this.pred.apply(e)) && RFCalculations.attackEntity(e, CustomDamage.attack(this.owner, EnumElement.NONE, CustomDamage.DamageType.NORMAL, CustomDamage.KnockBackType.BACK, 0.0f, 15), RFCalculations.getAttributeValue(this.owner, ItemStatAttributes.RFMAGICATT, null, null) / 2.5f)) {
                        e.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:slowness"), 10, 6, true, false));
                        e.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:jump_boost"), 10, 128, true, false));
                    }
                }
            }
        }
    }
}
