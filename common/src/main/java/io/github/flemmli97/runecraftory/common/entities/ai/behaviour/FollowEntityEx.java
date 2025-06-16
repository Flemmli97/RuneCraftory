package io.github.flemmli97.runecraftory.common.entities.ai.behaviour;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowEntity;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.function.BiFunction;

public class FollowEntityEx<E extends PathfinderMob, T extends Entity> extends FollowEntity<E, T> {

    protected BiFunction<E, T, Double> followStartDist = (entity, target) -> 4d;
    protected BiFunction<E, T, Double> ignoreTargetDist = (entity) -> 20d;

    public FollowEntityEx<E, T> startFollowingWhen(double followStartDist) {
        return this.startFollowingWhen((entity, target) -> followStartDist);
    }

    public FollowEntityEx<E, T> startFollowingWhen(BiFunction<E, T, Double> followStartDist) {
        this.followStartDist = followStartDist;
        return this;
    }

    public FollowEntityEx<E, T> ignoreIfTargetingTill(double ignoreTargetDist) {
        return this.ignoreIfTargetingTill((entity, target) -> ignoreTargetDist);
    }

    public FollowEntityEx<E, T> ignoreIfTargetingTill(BiFunction<E, T, Double> ignoreTargetDist) {
        this.ignoreTargetDist = ignoreTargetDist;
        return this;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        T toFollow = this.followingEntityProvider.apply(entity);
        double startDist = this.followStartDist.apply(entity, toFollow);
        double distSqr = entity.distanceToSqr(toFollow);
        if (toFollow == null || distSqr < startDist * startDist)
            return false;
        LivingEntity target = BrainUtils.getTargetOfEntity(entity);
        double ignoredDist = this.ignoreTargetDist.apply(entity, toFollow);
        if (target != null && distSqr < ignoredDist * ignoredDist)
            return false;
        return super.checkExtraStartConditions(level, entity);
    }
}
