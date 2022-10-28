package io.github.flemmli97.runecraftory.common.entities.ai;

import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

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
}

