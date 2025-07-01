package io.github.flemmli97.runecraftory.common.entities.ai.pathing;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.FlyNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.tslat.smartbrainlib.api.core.navigation.SmoothFlyingPathNavigation;

public class FloatingFlyNavigator extends SmoothFlyingPathNavigation {

    public FloatingFlyNavigator(Mob entity, Level world) {
        super(entity, world);
    }

    @Override
    protected PathFinder createPathFinder(int maxDist) {
        this.nodeEvaluator = new FlyNodeEvaluator();
        return new PathFinder(this.nodeEvaluator, maxDist);
    }

    @Override
    protected boolean canUpdatePath() {
        return true;
    }

    @Override
    public boolean isStableDestination(BlockPos pos) {
        return true;
    }
}