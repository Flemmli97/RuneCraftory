package com.flemmli97.runecraftory.common.entity;

import com.flemmli97.runecraftory.common.network.PacketAttackDebug;
import com.flemmli97.runecraftory.common.network.PacketHandler;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.google.common.collect.Lists;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public abstract class EntityChargingMobBase extends EntityMobBase{

    private List<EntityLivingBase> hitEntity;
    private double[] chargeMotion;

    public EntityChargingMobBase(World world) {
        super(world);
    }

    public void setChargeMotion(double[] chargeMotion){
        this.chargeMotion = chargeMotion;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        AnimatedAction anim = this.getAnimation();
        if(anim!=null && this.isAnimOfType(anim, AnimationType.CHARGE)){
            this.rotationPitch = 0;
            this.rotationYaw = (float) (MathHelper.atan2(this.motionX, -this.motionZ) * (180D / Math.PI)) + 180;
        }
    }

    @Override
    public void handleAttack(AnimatedAction anim){
        if (this.isAnimOfType(anim, AnimationType.CHARGE)) {
            if(this.chargeMotion ==null)
                return;
            this.getNavigator().clearPath();
            if (anim.getTick() > anim.getAttackTime()) {
                this.motionX = this.chargeMotion[0];
                this.motionZ = this.chargeMotion[2];
                if(this.hitEntity==null)
                    this.hitEntity = Lists.newArrayList();
                AxisAlignedBB aabb = this.calculateAttackAABB(anim, null);
                this.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb, this.attackPred).forEach(e->{
                    if(!this.hitEntity.contains(e)) {
                        this.attackEntityAsMob(e);
                        this.hitEntity.add(e);
                    }
                });
                PacketHandler.sendToAll(new PacketAttackDebug(aabb, 100));
            }
        }
        else
            super.handleAttack(anim);
    }

    @Override
    public void setAnimation(AnimatedAction anim) {
        if(!this.world.isRemote){
            if(this.getAnimation()!=null && this.isAnimOfType(this.getAnimation(), AnimationType.CHARGE))
                this.hitEntity = null;
        }
        super.setAnimation(anim);
    }

    public float chargingLength(){
        return 6;
    }
}
