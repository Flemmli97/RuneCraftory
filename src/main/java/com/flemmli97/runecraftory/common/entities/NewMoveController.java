package com.flemmli97.runecraftory.common.entities;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.MathHelper;

public class NewMoveController extends MovementController {

    public NewMoveController(MobEntity entity) {
        super(entity);
    }

    /**
     * Made it so entities jump up blocks during strafing
     */
    @Override
    public void tick() {
        if (this.action == MovementController.Action.STRAFE) {
            float f = (float) this.mob.getAttributeValue(Attributes.GENERIC_MOVEMENT_SPEED);
            float f1 = (float) this.speed * f;
            float f2 = this.moveForward;
            float f3 = this.moveStrafe;
            float f4 = MathHelper.sqrt(f2 * f2 + f3 * f3);
            if (f4 < 1.0F) {
                f4 = 1.0F;
            }

            f4 = f1 / f4;
            f2 = f2 * f4;
            f3 = f3 * f4;
            float f5 = MathHelper.sin(this.mob.rotationYaw * ((float) Math.PI / 180F));
            float f6 = MathHelper.cos(this.mob.rotationYaw * ((float) Math.PI / 180F));
            float f7 = f2 * f6 - f3 * f5;
            float f8 = f3 * f6 + f2 * f5;
            PathNavigator pathnavigate = this.mob.getNavigator();

            if (pathnavigate != null) {
                double len = Math.sqrt(f7 * f7 + f8 * f8);
                NodeProcessor nodeprocessor = pathnavigate.getNodeProcessor();
                int x = MathHelper.floor(this.mob.getX() + (double) f7 / len);
                int y = MathHelper.floor(this.mob.getY());
                int z = MathHelper.floor(this.mob.getZ() + (double) f8 / len);
                PathNodeType node = nodeprocessor != null ? nodeprocessor.getPathNodeType(this.mob.world, x, y, z) : PathNodeType.OPEN;
                //System.out.printf("%s %d %d %d \n", node, x, y, z);
                if (node == PathNodeType.BLOCKED) {
                    int yAdd = 0;
                    while (yAdd < this.mob.stepHeight) {
                        yAdd++;
                        node = nodeprocessor.getPathNodeType(this.mob.world, x, y + yAdd, z);
                        if (node == PathNodeType.WALKABLE) {
                            this.mob.getJumpController().setJumping();
                            break;
                        }
                    }
                } else if (node != PathNodeType.WALKABLE) {
                    this.moveForward = 1.0F;
                    this.moveStrafe = 0.0F;
                    f1 = f;
                }
            }
            this.mob.setAIMoveSpeed(f1);
            this.mob.setMoveForward(this.moveForward);
            this.mob.setMoveStrafing(this.moveStrafe);
            this.action = MovementController.Action.WAIT;
        } else {
            super.tick();
            this.mob.setMoveStrafing(0);
        }
    }

    public MovementController.Action currentAction() {
        return this.action;
    }
}