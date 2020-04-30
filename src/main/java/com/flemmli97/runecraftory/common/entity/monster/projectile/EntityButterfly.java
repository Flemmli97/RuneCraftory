package com.flemmli97.runecraftory.common.entity.monster.projectile;

import com.flemmli97.runecraftory.api.items.ItemStatAttributes;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.utils.RFCalculations;
import com.flemmli97.tenshilib.common.entity.EntityProjectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityButterfly extends EntityProjectile
{
    private Vec3d targetPos;
    private double length;
    private boolean turn;
    public EntityButterfly(World worldIn) {
        super(worldIn);
        this.setSize(0.2f, 0.2f);
    }
    
    public EntityButterfly(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }
    
    public EntityButterfly(World worldIn, EntityLivingBase thrower) {
        super(worldIn, thrower);
    }
    
    @Override
    public int livingTickMax()
    {
        return 100;
    }
    
    @Override
    public void shootAtPosition(double x, double y, double z, float velocity, float inaccuracy)
    {
        Vec3d dir = new Vec3d (x-this.posX, y - this.posY, z-this.posZ);
        this.length=dir.lengthVector();
        dir=dir.scale(1/velocity);
        this.shoot(dir.x, dir.y, dir.z, velocity, inaccuracy);
    }
    
    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy)
    {
        super.shoot(x, y, z, velocity, inaccuracy);
        this.targetPos = new Vec3d(this.motionX, this.motionY, this.motionZ).scale(this.length).addVector(this.posX, this.posY, this.posZ);//.addVector(0, -2, 0);
    }
    
    @Override
	public void onUpdate() {
        if(this.targetPos!=null && this.getDistance(this.targetPos.x, this.targetPos.y, this.targetPos.z)<3)
            this.turn=true;
        if(this.turn && this.motionY<0.5) {
            if(this.motionY<-2.5)
                this.motionY=-2.5;
            this.motionY+=0.005;
        }
        super.onUpdate();
    }
    
    @Override
	protected void onImpact(final RayTraceResult result) {
        if (result.entityHit != null && !this.world.isRemote && result.entityHit != this.getShooter() && result.entityHit instanceof EntityLivingBase) {
            if (RFCalculations.attackEntity(result.entityHit, CustomDamage.attack(this.getShooter(), EnumElement.NONE, CustomDamage.DamageType.NORMAL, CustomDamage.KnockBackType.BACK, 0.0f, 10), RFCalculations.getAttributeValue(this.getShooter(), ItemStatAttributes.RFMAGICATT, null, null) / 6.0f)) {
                ((EntityLivingBase)result.entityHit).addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:slowness"), 60, 3));
                this.setDead();
            }
        }
    }
    
    @Override
	protected float getGravityVelocity() {
        return 0.0f;
    }
}
