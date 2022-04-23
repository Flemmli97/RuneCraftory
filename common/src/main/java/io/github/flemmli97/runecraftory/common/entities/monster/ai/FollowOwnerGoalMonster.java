package io.github.flemmli97.runecraftory.common.entities.monster.ai;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

import java.util.EnumSet;

public class FollowOwnerGoalMonster extends Goal {
    private final BaseMonster monster;
    private Entity owner;
    private final double speedModifier;
    private int timeToRecalcPath;
    private final float stopDistance;
    private final float startDistance;
    private float oldWaterCost;

    public FollowOwnerGoalMonster(BaseMonster monster, double speed, float startDist, float stopDist) {
        this.monster = monster;
        this.speedModifier = speed;
        this.startDistance = startDist;
        this.stopDistance = stopDist;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        Player livingEntity = this.monster.getOwner();
        if (livingEntity == null) {
            return false;
        }
        if (livingEntity.isSpectator()) {
            return false;
        }
        if (this.monster.isStaying()) {
            return false;
        }
        if (this.monster.distanceToSqr(livingEntity) < (double) (this.startDistance * this.startDistance)) {
            return false;
        }
        this.owner = livingEntity;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.monster.getNavigation().isDone()) {
            return false;
        }
        if (this.monster.isStaying()) {
            return false;
        }
        return !(this.monster.distanceToSqr(this.owner) <= (double) (this.stopDistance * this.stopDistance));
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.monster.getPathfindingMalus(BlockPathTypes.WATER);
        this.monster.setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.monster.getNavigation().stop();
        this.monster.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        this.monster.getLookControl().setLookAt(this.owner, 10.0f, this.monster.getMaxHeadXRot());
        if (--this.timeToRecalcPath > 0) {
            return;
        }
        this.timeToRecalcPath = this.adjustedTickDelay(10);
        if (this.monster.isLeashed() || this.monster.isPassenger()) {
            return;
        }
        if (this.monster.distanceToSqr(this.owner) >= 144.0) {
            this.teleportToOwner();
        } else {
            this.monster.getNavigation().moveTo(this.owner, this.speedModifier);
        }
    }

    private void teleportToOwner() {
        BlockPos blockPos = this.owner.blockPosition();
        for (int i = 0; i < 10; ++i) {
            int j = this.randomIntInclusive(-3, 3);
            int k = this.randomIntInclusive(-1, 1);
            int l = this.randomIntInclusive(-3, 3);
            boolean bl = this.maybeTeleportTo(blockPos.getX() + j, blockPos.getY() + k, blockPos.getZ() + l);
            if (!bl) continue;
            return;
        }
    }

    private boolean maybeTeleportTo(int x, int y, int z) {
        if (Math.abs((double) x - this.owner.getX()) < 2.0 && Math.abs((double) z - this.owner.getZ()) < 2.0) {
            return false;
        }
        if (!this.canTeleportTo(new BlockPos(x, y, z))) {
            return false;
        }
        this.monster.moveTo((double) x + 0.5, y, (double) z + 0.5, this.monster.getYRot(), this.monster.getXRot());
        this.monster.getNavigation().stop();
        return true;
    }

    private boolean canTeleportTo(BlockPos pos) {
        BlockPathTypes blockPathTypes = WalkNodeEvaluator.getBlockPathTypeStatic(this.monster.level, pos.mutable());
        if (blockPathTypes != BlockPathTypes.WALKABLE) {
            return false;
        }
        BlockState blockState = this.monster.level.getBlockState(pos.below());
        if (!this.monster.isFlyingEntity() && blockState.getBlock() instanceof LeavesBlock) {
            return false;
        }
        BlockPos blockPos = pos.subtract(this.monster.blockPosition());
        return this.monster.level.noCollision(this.monster, this.monster.getBoundingBox().move(blockPos));
    }

    private int randomIntInclusive(int min, int max) {
        return this.monster.getRandom().nextInt(max - min + 1) + min;
    }
}

