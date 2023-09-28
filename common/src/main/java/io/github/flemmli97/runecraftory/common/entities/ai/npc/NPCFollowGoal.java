package io.github.flemmli97.runecraftory.common.entities.ai.npc;

import io.github.flemmli97.runecraftory.common.entities.ai.FollowEntityGoal;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;

public class NPCFollowGoal extends FollowEntityGoal<EntityNPCBase> {

    public NPCFollowGoal(EntityNPCBase mob, double speed, float startDist, float stopDist, float ignoreTargetDist) {
        super(mob, speed, startDist, stopDist, ignoreTargetDist);
    }

    @Override
    public LivingEntity getEntityToFollow() {
        return this.mob.followEntity();
    }

    @Override
    public boolean canFollow() {
        return !this.mob.isStaying();
    }

    @Override
    protected boolean canTeleportOn(BlockState state) {
        return !(state.getBlock() instanceof LeavesBlock);
    }

    @Override
    protected void moveToFollower(LivingEntity follower, double speed) {
        if (this.mob.behaviourState() == EntityNPCBase.Behaviour.FOLLOW)
            super.moveToFollower(follower, speed);
        else {
            Path path = this.mob.getNavigation().createPath(follower, 3);
            if (path != null)
                this.mob.getNavigation().moveTo(path, speed);
        }
    }
}

