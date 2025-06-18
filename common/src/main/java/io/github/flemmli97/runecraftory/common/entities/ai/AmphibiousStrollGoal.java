package io.github.flemmli97.runecraftory.common.entities.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;

public class AmphibiousStrollGoal extends RandomStrollGoal {

    public AmphibiousStrollGoal(PathfinderMob mob, double speedModifier, int interval) {
        super(mob, speedModifier, interval);
    }

    @Override
    protected Vec3 getPosition() {
        Vec3 target = null;
        Vec3 land = null;
        for (int i = 0; i < 10; i++) {
            Vec3 pos = DefaultRandomPos.getPos(this.mob, 10, 7);
            if (pos != null) {
                if (this.mob.level().getBlockState(BlockPos.containing(pos)).isPathfindable(PathComputationType.WATER)) {
                    target = pos;
                    break;
                } else
                    land = pos;
            }
        }
        if (target == null) {
            return land;
        }
        return target;
    }

}
