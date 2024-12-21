package io.github.flemmli97.runecraftory.common.entities.ai.control;

import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.phys.Vec3;

public class MoveControlerEx extends MoveControl {

    public MoveControlerEx(Mob entity) {
        super(entity);
    }

    /**
     * Make it so entities jump up blocks during strafing
     */
    @Override
    public void tick() {
        if (this.operation == MoveControl.Operation.STRAFE) {
            float speed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
            float forward = this.strafeForwards;
            float right = this.strafeRight;
            float len = Mth.sqrt(forward * forward + right * right);
            if (len < 0.0001) {
                return;
            }

            // Check if its strafing against a wall
            Vec3 target = MathUtils.rotate(new Vec3(0, 1, 0), new Vec3(right, 0, forward).normalize().scale(this.mob.getBbWidth() + 0.3),
                    -this.mob.getYRot() * Mth.DEG_TO_RAD);
            PathNavigation pathnavigate = this.mob.getNavigation();
            NodeEvaluator nodeprocessor = pathnavigate.getNodeEvaluator();
            int x = Mth.floor(this.mob.getX() + target.x());
            int y = Mth.floor(this.mob.getY());
            int z = Mth.floor(this.mob.getZ() + target.z());
            BlockPathTypes node = nodeprocessor.getBlockPathType(this.mob.level, x, y, z);
            // Try jumping
            if (node == BlockPathTypes.BLOCKED) {
                int yAdd = 0;
                while (yAdd < this.mob.maxUpStep) {
                    yAdd++;
                    node = nodeprocessor.getBlockPathType(this.mob.level, x, y + yAdd, z);
                    if (node == BlockPathTypes.WALKABLE) {
                        this.mob.getJumpControl().jump();
                        break;
                    }
                }
            } else if (node == BlockPathTypes.OPEN) {
                // Check if droppable
                int yAdd = 0;
                while (yAdd < this.mob.maxUpStep) {
                    yAdd++;
                    node = nodeprocessor.getBlockPathType(this.mob.level, x, y - yAdd, z);
                    if (node == BlockPathTypes.WALKABLE) {
                        break;
                    }
                }
            }
            if (node != BlockPathTypes.WALKABLE) {
                this.strafeForwards *= -1.0F;
                this.strafeRight *= -1.0F;
            }

            this.mob.setSpeed(speed);
            this.mob.setZza(this.strafeForwards);
            this.mob.setXxa(this.strafeRight);
            this.operation = MoveControl.Operation.WAIT;
        } else {
            super.tick();
            this.mob.setXxa(0);
        }
    }
}