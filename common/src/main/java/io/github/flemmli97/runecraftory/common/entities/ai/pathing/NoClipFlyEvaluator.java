package io.github.flemmli97.runecraftory.common.entities.ai.pathing;

import io.github.flemmli97.runecraftory.mixin.MobAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.pathfinder.FlyNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;

public class NoClipFlyEvaluator extends FlyNodeEvaluator {

    @Override
    public PathType getPathType(PathfindingContext context, int x, int y, int z) {
        if (this.mob.getFirstPassenger() instanceof LivingEntity passenger) {
            if (passenger instanceof MobAccessor other) {
                return other.getTrueNavigator().getNodeEvaluator().getPathType(context, x, y, z);
            }
            return super.getPathType(context, x, y, z);
        }
        return PathType.OPEN;
    }
}
