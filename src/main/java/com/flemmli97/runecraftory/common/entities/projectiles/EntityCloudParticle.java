/*package com.flemmli97.runecraftory.common.entity.projectiles;

import com.flemmli97.runecraftory.api.items.ItemStatAttributes;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.utils.RFCalculations;
import com.flemmli97.tenshilib.common.entity.EntityProjectile;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.List;

public class EntityCloudParticle extends EntityProjectile {

    private Predicate<EntityLivingBase> pred;

    private List<EntityLivingBase> hitEntities;

    private float[] color;

    private float damageScale = 1;
    public EntityCloudParticle(World world) {
        super(world);
    }

    public EntityCloudParticle(World world, EntityLivingBase shooter, @Nullable Predicate<EntityLivingBase> pred) {
        super(world, shooter);
        this.pred=pred;
    }

    public EntityCloudParticle setDamageScale(float scale){
        this.damageScale=scale;
        return this;
    }

    public void setColor(float[] color){
        this.color = color;
    }

    public float[] getColor(){
        return this.color;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(!this.world.isRemote){
            ((WorldServer)this.world).spawnParticle(EnumParticleTypes.REDSTONE, this.posX, this.posY, this.posZ, 0,0,0);
        }
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        EntityLivingBase e = null;
        if (result.entityHit instanceof EntityLivingBase && !this.world.isRemote && (e = (EntityLivingBase) result.entityHit) != this.getShooter()
                && (this.pred==null || this.pred.apply((EntityLivingBase) result.entityHit)) && !this.hitEntities.contains(e)) {
            if (RFCalculations.attackEntity(result.entityHit, CustomDamage.attack(this.getShooter(), EnumElement.NONE, CustomDamage.DamageType.NORMAL, CustomDamage.KnockBackType.UP, 0.0f, 10), RFCalculations.getAttributeValue(this.getShooter(), ItemStatAttributes.RFMAGICATT, null, null) * this.damageScale)) {
                this.hitEntities.add(e);
                //this.setDead();
            }
        }
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Nullable
    @Override
    public EntityLivingBase getShooter() {
        EntityLivingBase entityLivingBase = super.getShooter();
        if(entityLivingBase instanceof EntityMobBase)
            this.pred = ((EntityMobBase)entityLivingBase).attackPred;
        return entityLivingBase;
    }


    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
    }
}
*/