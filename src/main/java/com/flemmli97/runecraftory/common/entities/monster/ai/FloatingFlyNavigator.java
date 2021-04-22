package com.flemmli97.runecraftory.common.entities.monster.ai;

import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.FlyingNodeProcessor;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FloatingFlyNavigator extends SwimmerPathNavigator {

    public FloatingFlyNavigator(MobEntity entity, World world) {
        super(entity, world);
    }

    @Override
    protected PathFinder getPathFinder(int maxDist) {
        this.nodeProcessor = new FlyingNodeProcessor();
        return new PathFinder(this.nodeProcessor, maxDist);
    }

    @Override
    protected boolean canNavigate() {
        return true;
    }

    @Override
    public boolean canEntityStandOnPos(BlockPos pos) {
        return true;
    }

}