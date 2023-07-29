package io.github.flemmli97.runecraftory.common.entities.ai.pathing;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class NoClipFlyMoveController extends MoveControl {

    public NoClipFlyMoveController(Mob mob) {
        super(mob);
    }

    @Override
    public void setWantedPosition(double x, double y, double z, double speed) {
        Path path = this.mob.getNavigation().getPath();
        if (path != null && this.mob.isVehicle() && path.getEndNode() != null) {
            Vec3 end = path.getEntityPosAtNode(this.mob, path.getNodeCount() - 1);
            super.setWantedPosition(end.x, end.y, end.z, speed);
            path.setNextNodeIndex(path.getNodeCount() - 1); //Skip to end of path
        } else
            super.setWantedPosition(x, y, z, speed);
    }

    @Override
    public void tick() {
        if (this.operation == MoveControl.Operation.MOVE_TO) {
            Vec3 dir = new Vec3(this.wantedX - this.mob.getX(), this.wantedY - this.mob.getY(), this.wantedZ - this.mob.getZ());
            double d = dir.lengthSqr();
            if (d < this.mob.getBoundingBox().getSize() * this.mob.getBoundingBox().getSize()) {
                this.operation = MoveControl.Operation.WAIT;
                this.mob.setDeltaMovement(this.mob.getDeltaMovement().scale(0.8));
                return;
            }
            float rotPre = this.mob.getYRot();
            double horLen = Math.sqrt(dir.x() * dir.x() + dir.z() * dir.z());
            this.mob.setXRot(Mth.wrapDegrees((float) (-(Mth.atan2(dir.y(), horLen) * Mth.RAD_TO_DEG))));
            float newRot = Mth.wrapDegrees((float) (Mth.atan2(dir.z(), dir.x()) * Mth.RAD_TO_DEG));
            this.mob.setYRot(Mth.approachDegrees(rotPre + 90, newRot, 20.0f) - 90.0f);
            this.mob.yBodyRot = this.mob.getYRot();
            this.speedModifier = Mth.approach((float) this.speedModifier, 0.23f, 0.025f);
            Vec3 moveDir = Vec3.directionFromRotation(this.mob.getXRot(), this.mob.getYRot());
            double xDir = this.speedModifier * moveDir.x() * 0.02;
            double yDir = this.speedModifier * moveDir.y() * 0.02;
            double zDir = this.speedModifier * moveDir.z() * 0.02;

            Vec3 delta = this.mob.getDeltaMovement();
            this.mob.setDeltaMovement(delta.add(xDir, yDir, zDir));
        }
    }
}
