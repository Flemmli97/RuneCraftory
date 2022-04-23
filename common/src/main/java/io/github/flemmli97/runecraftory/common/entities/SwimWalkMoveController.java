package io.github.flemmli97.runecraftory.common.entities;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class SwimWalkMoveController extends NewMoveController {

    public SwimWalkMoveController(Mob entity) {
        super(entity);
    }

    protected void moveSpeed() {
        if (this.mob.isOnGround()) {
            this.mob.setSpeed(Math.max(this.mob.getSpeed() * 0.5F, 0.06F));
        }
    }

    protected void handleWaterMovement() {
        if (this.operation == MoveControl.Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
            double dX = this.wantedX - this.mob.getX();
            double dY = this.wantedY - this.mob.getY();
            double dZ = this.wantedZ - this.mob.getZ();
            double dist = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
            dY = dY / dist;
            float f = (float) (Mth.atan2(dZ, dX) * (double) (180F / (float) Math.PI)) - 90.0F;
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f, 90.0F));
            this.mob.yBodyRot = this.mob.getYRot();
            float speed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
            this.mob.setSpeed(Mth.lerp(0.125F, this.mob.getSpeed(), speed));
            this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0D, (double) this.mob.getSpeed() * dY * 0.1D, 0.0D));
        } else {
            this.mob.setSpeed(0.0F);
            this.operation = MoveControl.Operation.WAIT;
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
