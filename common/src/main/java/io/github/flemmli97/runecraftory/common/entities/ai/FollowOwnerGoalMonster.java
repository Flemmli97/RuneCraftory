package io.github.flemmli97.runecraftory.common.entities.ai;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public class FollowOwnerGoalMonster extends FollowEntityGoal<BaseMonster> {

    public FollowOwnerGoalMonster(BaseMonster mob, double speed, float startDist, float stopDist, float ignoreTargetDist) {
        super(mob, speed, startDist, stopDist, ignoreTargetDist);
    }

    @Override
    public LivingEntity getEntityToFollow() {
        return this.mob.getOwner();
    }

    @Override
    public boolean canFollow() {
        return this.mob.behaviourState() == BaseMonster.Behaviour.FOLLOW;
    }

    @Override
    protected boolean canTeleportOn(BlockState state) {
        return this.mob.isFlyingEntity() || !(state.getBlock() instanceof LeavesBlock);
    }
}

