package io.github.flemmli97.runecraftory.common.entities.ai.control;

import io.github.flemmli97.runecraftory.common.utils.MathsHelper;
import io.github.flemmli97.tenshilib.common.entity.ai.MoveControllerPlus;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;

public class SwimWalkMoveController extends MoveControllerPlus {

    protected final double swimSpeedBoost;

    public SwimWalkMoveController(Mob entity, double swimSpeedBoost) {
        super(entity);
        this.swimSpeedBoost = swimSpeedBoost;
    }

    protected void directMovement() {
        if (this.operation == MoveControl.Operation.MOVE_TO) {
            this.operation = Operation.WAIT;
            Vec3 dir = new Vec3(this.wantedX - this.mob.getX(), this.wantedY - this.mob.getY(), this.wantedZ - this.mob.getZ());
            if (dir.lengthSqr() < 0.0001) {
                this.mob.setYya(0.0F);
                this.mob.setZza(0.0F);
                return;
            }
            float[] yXRot = MathsHelper.YXRotFrom(dir);
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), yXRot[0], 90));
            this.mob.setXRot(this.rotlerp(this.mob.getXRot(), yXRot[1], 30));
            Path path = this.mob.getNavigation().getPath();
            Node node = path != null && !path.isDone() ? path.getPreviousNode() : null;
            if (node != null && node.type == PathType.WATER_BORDER
                    && !this.mob.level().getFluidState(node.asBlockPos()).is(FluidTags.WATER)
                    && this.mob.isInWater() && this.mob.getDeltaMovement().y() < 0.2) {
                this.mob.getJumpControl().jump();
            }

            float speed = (float) (this.speedModifier * this.swimSpeedBoost * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
            dir = dir.normalize().scale(speed);
            this.mob.setSpeed((float) dir.horizontalDistance());
            this.mob.setYya((float) dir.y());
        } else {
            this.mob.setSpeed(0.0F);
            this.operation = MoveControl.Operation.WAIT;
        }
    }

    @Override
    public void tick() {
        if (this.mob.isInWater()) {
            this.directMovement();
        } else
            super.tick();
    }
}
