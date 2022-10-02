package io.github.flemmli97.runecraftory.common.entities.monster.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class NoClipFlyMoveController extends MoveControl {

    public NoClipFlyMoveController(Mob mob) {
        super(mob);
    }

    @Override
    public void tick() {
        if (this.operation == MoveControl.Operation.MOVE_TO) {
            Vec3 dir = new Vec3(this.wantedX - this.mob.getX(), this.wantedY - this.mob.getY(), this.wantedZ - this.mob.getZ());
            double d = dir.lengthSqr();
            if (d < this.mob.getBoundingBox().getSize() * this.mob.getBoundingBox().getSize()) {
                this.operation = MoveControl.Operation.WAIT;
                this.mob.setDeltaMovement(this.mob.getDeltaMovement().scale(0.5));
                return;
            }
            double i = this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED);
            this.mob.setDeltaMovement(dir.normalize().scale(i));
            Vec3 vec32 = this.mob.getDeltaMovement();
            this.mob.setYRot(-((float) Mth.atan2(vec32.x, vec32.z)) * 57.295776f);
            this.mob.yBodyRot = this.mob.getYRot();
        } else {
            this.mob.setYya(0.0f);
            this.mob.setZza(0.0f);
        }
    }
}
