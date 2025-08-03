package io.github.flemmli97.runecraftory.common.entities.ai.behaviour;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;

public class MoveToWalkTillClose<E extends PathfinderMob> extends MoveToWalkTarget<E> {

    @Override
    protected boolean hasReachedTarget(E entity, WalkTarget target) {
        if (target.getTarget() instanceof EntityTracker tracker) {
            if (entity.getBoundingBox().inflate(0.5).intersects(tracker.getEntity().getBoundingBox()))
                return true;
        }
        return super.hasReachedTarget(entity, target);
    }
}
