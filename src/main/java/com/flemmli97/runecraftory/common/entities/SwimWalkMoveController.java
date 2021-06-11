package com.flemmli97.runecraftory.common.entities;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.math.MathHelper;

public class SwimWalkMoveController extends NewMoveController {

    public SwimWalkMoveController(MobEntity entity) {
        super(entity);
    }

    protected void moveSpeed() {
        if (this.mob.isOnGround()) {
            this.mob.setAIMoveSpeed(Math.max(this.mob.getAIMoveSpeed() * 0.5F, 0.06F));
        }
    }

    protected void handleWaterMovement() {
        if (this.action == Action.MOVE_TO && !this.mob.getNavigator().noPath()) {
            double dX = this.posX - this.mob.getPosX();
            double dY = this.posY - this.mob.getPosY();
            double dZ = this.posZ - this.mob.getPosZ();
            double dist = MathHelper.sqrt(dX * dX + dY * dY + dZ * dZ);
            dY = dY / dist;
            float f = (float) (MathHelper.atan2(dZ, dX) * (double) (180F / (float) Math.PI)) - 90.0F;
            this.mob.rotationYaw = this.limitAngle(this.mob.rotationYaw, f, 90.0F);
            this.mob.renderYawOffset = this.mob.rotationYaw;
            float speed = (float) (this.speed * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
            this.mob.setAIMoveSpeed(MathHelper.lerp(0.125F, this.mob.getAIMoveSpeed(), speed));
            this.mob.setMotion(this.mob.getMotion().add(0.0D, (double) this.mob.getAIMoveSpeed() * dY * 0.1D, 0.0D));
        } else {
            this.mob.setAIMoveSpeed(0.0F);
            this.action = Action.WAIT;
        }
    }

    @Override
    public void tick() {
        this.moveSpeed();
        if (this.mob.isInWater()) {
            this.handleWaterMovement();
        } else
            super.tick();
    }
}
