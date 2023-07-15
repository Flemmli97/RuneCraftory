package io.github.flemmli97.runecraftory.common.entities.ai.pathing;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.FlyNodeEvaluator;

public class NoClipFlyEvaluator extends FlyNodeEvaluator {

    @Override
    public BlockPathTypes getBlockPathType(BlockGetter blockaccess, int x, int y, int z, Mob entityliving, int xSize, int ySize, int zSize, boolean canBreakDoors, boolean canEnterDoors) {
        if (entityliving.isVehicle())
            return super.getBlockPathType(blockaccess, x, y, z, entityliving, xSize, ySize, zSize, canBreakDoors, canEnterDoors);
        return BlockPathTypes.OPEN;
    }
}
