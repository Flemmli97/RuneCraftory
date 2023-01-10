package io.github.flemmli97.runecraftory.common.entities.ai;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public class FollowOwnerGoalMonster extends FollowEntityGoal<BaseMonster> {

    private final float originStartDistance, originStopDistance;

    public FollowOwnerGoalMonster(BaseMonster mob, double speed, float startDist, float stopDist, float ignoreTargetDist) {
        super(mob, speed, startDist, stopDist, ignoreTargetDist);
        this.originStartDistance = startDist;
        this.originStopDistance = stopDist;
    }

    @Override
    public LivingEntity getEntityToFollow() {
        if (this.mob.behaviourState() == BaseMonster.Behaviour.FOLLOW_DISTANCE) {
            this.startDistance = this.originStartDistance + 6;
            this.stopDistance = this.originStopDistance + 3;
            this.tpDistanceSqrt = TP_DISTANCE_SQRT * 2;
        } else {
            this.startDistance = this.originStartDistance;
            this.stopDistance = this.originStopDistance;
            this.tpDistanceSqrt = TP_DISTANCE_SQRT;
        }
        return this.mob.getOwner();
    }

    @Override
    public boolean canFollow() {
        return this.mob.behaviourState().following;
    }

    @Override
    protected boolean canTeleportOn(BlockState state) {
        return this.mob.isFlyingEntity() || !(state.getBlock() instanceof LeavesBlock);
    }
}

