package io.github.flemmli97.runecraftory.common.entities.ai.control;

import io.github.flemmli97.runecraftory.common.utils.MathsHelper;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.phys.Vec3;

import java.util.function.BooleanSupplier;

public class FreeMoveControl extends MoveControl {

    public static final BooleanSupplier TRUE = () -> true;

    private final BooleanSupplier flying;
    private final float maxYRot, maxXRot;

    public FreeMoveControl(Mob mob) {
        this(mob, 90, 30, TRUE);
    }

    public FreeMoveControl(Mob mob, BooleanSupplier flying) {
        this(mob, 90, 30, flying);
    }

    public FreeMoveControl(Mob mob, float maxYRot, float maxXRot, BooleanSupplier flying) {
        super(mob);
        this.maxYRot = maxYRot;
        this.maxXRot = maxXRot;
        this.flying = flying;
    }

    @Override
    public void tick() {
        if (this.operation == Operation.STRAFE) {
            float speed = (float) (this.speedModifier * this.getSpeedAttribute());
            float forward = this.strafeForwards;
            float right = this.strafeRight;
            float len = Mth.sqrt(forward * forward + right * right);
            if (len < 0.0001) {
                return;
            }
            len = speed / len;
            forward *= len;
            right *= len;

            Vec3 target = MathUtils.rotate(new Vec3(0, 1, 0), new Vec3(right, 0, forward).normalize().scale(this.mob.getBbWidth() + 0.3),
                    -this.mob.getYRot() * Mth.DEG_TO_RAD);
            PathNavigation pathnavigate = this.mob.getNavigation();
            NodeEvaluator nodeprocessor = pathnavigate.getNodeEvaluator();
            int x = Mth.floor(this.mob.getX() + target.x());
            int y = Mth.floor(this.mob.getY());
            int z = Mth.floor(this.mob.getZ() + target.z());
            BlockPathTypes node = nodeprocessor.getBlockPathType(this.mob.level, x, y, z);
            if (node != BlockPathTypes.WALKABLE) {
                this.strafeForwards *= -1.0F;
                this.strafeRight *= -1.0F;
            }

            this.mob.setSpeed(speed);
            this.mob.setZza(this.strafeForwards);
            this.mob.setXxa(this.strafeRight);
            this.operation = MoveControl.Operation.WAIT;
        } else if (this.operation == Operation.MOVE_TO) {
            this.operation = Operation.WAIT;
            this.mob.setXxa(0);
            Vec3 dir = new Vec3(this.wantedX - this.mob.getX(), this.wantedY - this.mob.getY(), this.wantedZ - this.mob.getZ());
            if (dir.lengthSqr() < 0.0001) {
                this.mob.setYya(0);
                this.mob.setZza(0);
                return;
            }
            float[] yXRot = MathsHelper.YXRotFrom(dir);
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), yXRot[0], this.maxYRot));
            this.mob.setXRot(this.rotlerp(this.mob.getXRot(), yXRot[1], this.maxXRot));

            float speed = (float) (this.speedModifier * (this.getSpeedAttribute()));
            dir = dir.normalize().scale(speed);
            this.mob.setSpeed(speed);
            this.mob.setYya((float) dir.y());
        } else {
            this.mob.setYya(0);
            this.mob.setZza(0);
        }
    }

    protected double getSpeedAttribute() {
        if (this.flying.getAsBoolean()) {
            return this.mob.getAttributeValue(Attributes.FLYING_SPEED);
        }
        return this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }
}
