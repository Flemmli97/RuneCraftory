package io.github.flemmli97.runecraftory.common.entities.ai;

import io.github.flemmli97.runecraftory.common.utils.TeleportUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import java.util.EnumSet;

public abstract class FollowEntityGoal<T extends Mob> extends Goal {

    public static final double TP_DISTANCE_SQRT = 225;

    protected final T mob;
    private final double speedModifier;
    protected float stopDistance;
    protected float startDistance;
    protected final float ignoreTargetDist;
    private LivingEntity owner;
    private int timeToRecalcPath;
    private float oldWaterCost;

    protected double tpDistanceSqrt = TP_DISTANCE_SQRT;

    public FollowEntityGoal(T mob, double speed, float startDist, float stopDist, float ignoreTargetDist) {
        this.mob = mob;
        this.speedModifier = speed;
        this.startDistance = startDist;
        this.stopDistance = stopDist;
        this.ignoreTargetDist = ignoreTargetDist;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public abstract LivingEntity getEntityToFollow();

    public abstract boolean canFollow();

    @Override
    public boolean canUse() {
        LivingEntity livingEntity = this.getEntityToFollow();
        if (livingEntity == null) {
            return false;
        }
        if (livingEntity.isSpectator()) {
            return false;
        }
        if (!this.canFollow()) {
            return false;
        }
        if (this.mob.getTarget() != null) {
            if (this.mob.distanceToSqr(livingEntity) < (double) (this.ignoreTargetDist * this.ignoreTargetDist)) {
                return false;
            }
            this.owner = livingEntity;
            return true;
        }
        if (this.mob.distanceToSqr(livingEntity) < (double) (this.startDistance * this.startDistance)) {
            return false;
        }
        this.owner = livingEntity;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob.getNavigation().isDone()) {
            return false;
        }
        if (!this.canFollow()) {
            return false;
        }
        return !(this.mob.distanceToSqr(this.owner) <= (double) (this.stopDistance * this.stopDistance));
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.mob.getPathfindingMalus(BlockPathTypes.WATER);
        this.mob.setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.mob.getNavigation().stop();
        this.mob.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        this.mob.getLookControl().setLookAt(this.owner, 10.0f, this.mob.getMaxHeadXRot());
        if (--this.timeToRecalcPath > 0) {
            return;
        }
        this.timeToRecalcPath = this.adjustedTickDelay(10);
        if (this.mob.isLeashed() || this.mob.isPassenger()) {
            return;
        }
        if (this.mob.distanceToSqr(this.owner) < this.tpDistanceSqrt || !this.teleportToOwner()) {
            this.mob.getNavigation().moveTo(this.owner, this.speedModifier);
        }
    }

    private boolean teleportToOwner() {
        BlockPos blockPos = this.owner.blockPosition();
        for (int i = 0; i < 10; ++i) {
            int j = this.randomIntInclusive(-3, 3);
            int k = this.randomIntInclusive(-1, 1);
            int l = this.randomIntInclusive(-3, 3);
            if (this.maybeTeleportTo(blockPos.getX() + j, blockPos.getY() + k, blockPos.getZ() + l))
                return true;
        }
        return false;
    }

    private boolean maybeTeleportTo(int x, int y, int z) {
        if (Math.abs(x - this.owner.getX()) < 2.0 && Math.abs(z - this.owner.getZ()) < 2.0) {
            return false;
        }
        if (!TeleportUtils.validTeleportPlace(this.mob, new BlockPos(x, y, z), this::canTeleportOn)) {
            return false;
        }
        this.mob.moveTo((double) x + 0.5, y, (double) z + 0.5, this.mob.getYRot(), this.mob.getXRot());
        this.mob.getNavigation().stop();
        return true;
    }

    protected abstract boolean canTeleportOn(BlockState state);

    private int randomIntInclusive(int min, int max) {
        return this.mob.getRandom().nextInt(max - min + 1) + min;
    }
}

