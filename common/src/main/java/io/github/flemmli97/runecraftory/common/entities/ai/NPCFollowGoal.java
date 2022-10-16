package io.github.flemmli97.runecraftory.common.entities.ai;

import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

import java.util.EnumSet;

public class NPCFollowGoal extends Goal {

    private final EntityNPCBase npc;
    private final double speedModifier;
    private final float stopDistance;
    private final float startDistance;
    private LivingEntity owner;
    private int timeToRecalcPath;
    private float oldWaterCost;

    public NPCFollowGoal(EntityNPCBase monster, double speed, float startDist, float stopDist) {
        this.npc = monster;
        this.speedModifier = speed;
        this.startDistance = startDist;
        this.stopDistance = stopDist;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity livingEntity = this.npc.followEntity();
        if (livingEntity == null) {
            return false;
        }
        if (livingEntity.isSpectator()) {
            return false;
        }
        if (this.npc.distanceToSqr(livingEntity) < (double) (this.startDistance * this.startDistance)) {
            return false;
        }
        this.owner = livingEntity;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.npc.getNavigation().isDone()) {
            return false;
        }
        if (this.npc.isStaying()) {
            return false;
        }
        return !(this.npc.distanceToSqr(this.owner) <= (double) (this.stopDistance * this.stopDistance));
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.npc.getPathfindingMalus(BlockPathTypes.WATER);
        this.npc.setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.npc.getNavigation().stop();
        this.npc.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        this.npc.getLookControl().setLookAt(this.owner, 10.0f, this.npc.getMaxHeadXRot());
        if (--this.timeToRecalcPath > 0) {
            return;
        }
        this.timeToRecalcPath = this.adjustedTickDelay(10);
        if (this.npc.isLeashed() || this.npc.isPassenger()) {
            return;
        }
        if (this.npc.distanceToSqr(this.owner) >= 144.0) {
            this.teleportToOwner();
        } else {
            this.npc.getNavigation().moveTo(this.owner, this.speedModifier);
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
        this.npc.moveTo((double) x + 0.5, y, (double) z + 0.5, this.npc.getYRot(), this.npc.getXRot());
        this.npc.getNavigation().stop();
        return true;
    }

    private boolean canTeleportTo(BlockPos pos) {
        BlockPathTypes blockPathTypes = WalkNodeEvaluator.getBlockPathTypeStatic(this.npc.level, pos.mutable());
        if (blockPathTypes != BlockPathTypes.WALKABLE) {
            return false;
        }
        BlockPos blockPos = pos.subtract(this.npc.blockPosition());
        return this.npc.level.noCollision(this.npc, this.npc.getBoundingBox().move(blockPos));
    }

    private int randomIntInclusive(int min, int max) {
        return this.npc.getRandom().nextInt(max - min + 1) + min;
    }
}

