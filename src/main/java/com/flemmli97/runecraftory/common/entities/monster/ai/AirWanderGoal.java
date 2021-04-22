package com.flemmli97.runecraftory.common.entities.monster.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class AirWanderGoal extends RandomWalkingGoal {

    private float prevPrio;

    public AirWanderGoal(CreatureEntity entity) {
        super(entity, 1);
    }

    @Override
    protected Vector3d getPosition() {
        //this.prevPrio = this.creature.getPathPriority(PathNodeType.OPEN);
        //this.creature.setPathPriority(PathNodeType.OPEN, 0);
        Vector3d vec = super.getPosition();
        //this.creature.setPathPriority(PathNodeType.OPEN, this.prevPrio);
        BlockPos pos;
        if (vec != null && this.creature.world.getBlockState(pos = new BlockPos(vec).down()).hasSolidTopSurface(this.creature.world, pos, this.creature)) {
            return vec.add(0, 1, 0);
        }
        return vec;
    }
}
