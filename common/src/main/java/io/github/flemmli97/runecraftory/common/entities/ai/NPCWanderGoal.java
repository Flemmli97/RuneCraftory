package io.github.flemmli97.runecraftory.common.entities.ai;

import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class NPCWanderGoal extends Goal {

    protected final EntityNPCBase npc;

    private int walkCooldown;
    private Activity prevActivity;

    public NPCWanderGoal(EntityNPCBase npc) {
        this.npc = npc;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return true;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        Activity activity = this.npc.getActivity();
        boolean changed = this.prevActivity != activity;
        this.prevActivity = activity;
        if (activity == Activity.REST) {
            GlobalPos pos = this.npc.getBedPos();
            if (pos != null && this.npc.level.dimension() == pos.dimension()) {
                if (changed || (this.npc.getNavigation().isDone() && !this.npc.isSleeping())) {
                    Path path = this.npc.getNavigation().createPath(pos.pos(), 0);
                    this.npc.getNavigation().moveTo(path, 1);
                }
                BlockState blockState;
                if (pos.pos().closerToCenterThan(this.npc.position(), 2.0) && (blockState = this.npc.level.getBlockState(pos.pos())).is(BlockTags.BEDS) && !blockState.getValue(BedBlock.OCCUPIED)) {
                    this.npc.startSleeping(pos.pos());
                }
                return;
            }
        } else if (activity == Activity.WORK) {
            GlobalPos pos = this.npc.getWorkPlace();
            if (pos != null && this.npc.level.dimension() == pos.dimension()) {
                if (pos.pos().closerToCenterThan(this.npc.position(), 2.0)) {
                    this.npc.getNavigation().stop();
                    return;
                }
                if (changed || this.npc.getNavigation().isDone()) {
                    Path path = this.npc.getNavigation().createPath(pos.pos(), 0);
                    this.npc.getNavigation().moveTo(path, 1);
                }
                return;
            }
        } else if (activity == Activity.MEET) {
            GlobalPos pos = this.npc.getMeetingPos();
            if (pos != null && this.npc.level.dimension() == pos.dimension()) {
                --this.walkCooldown;
                if (pos.pos().closerToCenterThan(this.npc.position(), 6.0) && this.walkCooldown <= 0) {
                    Vec3 to = this.getRandomPosition(5);
                    if (to != null) {
                        Path path = this.npc.getNavigation().createPath(to.x(), to.y(), to.z(), 0);
                        this.npc.getNavigation().moveTo(path, 1);
                        this.walkCooldown = this.npc.getRandom().nextInt(40) + 90;
                    } else
                        this.walkCooldown = this.npc.getRandom().nextInt(20) + 30;
                    return;
                }
                if (changed || this.npc.getNavigation().isDone()) {
                    Path path = this.npc.getNavigation().createPath(pos.pos(), 0);
                    this.npc.getNavigation().moveTo(path, 1);
                }
                return;
            }
        }
        if (this.npc.getNavigation().isDone()) {
            if (--this.walkCooldown <= 0) {
                Vec3 pos;
                BlockPos blockPos = this.npc.blockPosition();
                ServerLevel serverLevel = (ServerLevel) this.npc.level;
                if (serverLevel.isVillage(blockPos)) {
                    pos = this.getRandomPosition(10);
                } else {
                    SectionPos sectionPos = SectionPos.of(blockPos);
                    SectionPos sectionPos2 = BehaviorUtils.findSectionClosestToVillage(serverLevel, sectionPos, 2);
                    if (sectionPos2 != sectionPos) {
                        pos = this.setTargetedPosTowards(sectionPos2);
                    } else {
                        pos = this.getRandomPosition(10);
                    }
                }
                if (pos != null) {
                    Path path = this.npc.getNavigation().createPath(pos.x(), pos.y(), pos.z(), 0);
                    this.npc.getNavigation().moveTo(path, 1);
                    this.walkCooldown = this.npc.getRandom().nextInt(40) + 90;
                } else
                    this.walkCooldown = this.npc.getRandom().nextInt(20) + 30;
            }
        }
    }

    @Nullable
    protected Vec3 getRandomPosition(int radius) {
        if (this.npc.isInWaterOrBubble()) {
            Vec3 vec3 = LandRandomPos.getPos(this.npc, 15, 7);
            return vec3 == null ? DefaultRandomPos.getPos(this.npc, 10, 7) : vec3;
        }
        return DefaultRandomPos.getPos(this.npc, radius, 7);
    }

    private Vec3 setTargetedPosTowards(SectionPos sectionPos) {
        return DefaultRandomPos.getPosTowards(this.npc, 10, 7, Vec3.atBottomCenterOf(sectionPos.center()), 1.5707963705062866);
    }
}
