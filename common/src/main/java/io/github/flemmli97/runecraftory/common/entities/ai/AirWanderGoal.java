package io.github.flemmli97.runecraftory.common.entities.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.phys.Vec3;

public class AirWanderGoal extends RandomStrollGoal {

    private float prevPrio;

    public AirWanderGoal(PathfinderMob entity) {
        super(entity, 1);
    }

    @Override
    protected Vec3 getPosition() {
        //this.prevPrio = this.creature.getPathPriority(PathNodeType.OPEN);
        //this.creature.setPathPriority(PathNodeType.OPEN, 0);
        Vec3 vec = super.getPosition();
        //this.creature.setPathPriority(PathNodeType.OPEN, this.prevPrio);
        BlockPos pos;
        if (vec != null && this.mob.level.getBlockState(pos = new BlockPos(vec).below()).entityCanStandOn(this.mob.level, pos, this.mob)) {
            return vec.add(0, 1, 0);
        }
        return vec;
    }
}