package io.github.flemmli97.runecraftory.common.entities.ai;

import com.google.common.base.Suppliers;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class NPCWanderGoal extends Goal {

    private static final Supplier<Set<PoiType>> shelterPOIS = Suppliers.memoize(() ->
            ModNPCJobs.allJobs().stream().map(s -> s.poiType.get()).filter(Objects::nonNull).collect(Collectors.toSet()));

    protected final EntityNPCBase npc;

    private int walkCooldown, shelterPOISearchCooldown, pathFindCooldown;
    private Activity prevActivity;

    public NPCWanderGoal(EntityNPCBase npc) {
        this.npc = npc;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.npc.getEntityToFollowUUID() == null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
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
        --this.shelterPOISearchCooldown;
        --this.walkCooldown;
        --this.pathFindCooldown;
        if (changed) {
            this.npc.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
            this.npc.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        }
        this.moveToTarget();
        if (activity == Activity.REST) {
            if (this.rest())
                return;
        } else if (activity == Activity.WORK) {
            if (this.work())
                return;
        } else if (activity == Activity.MEET) {
            if (this.goMeet())
                return;
        }
        this.wanderAround();
    }

    public boolean rest() {
        GlobalPos pos = this.npc.getBedPos();
        if (pos != null && this.npc.level.dimension() == pos.dimension()) {
            if (this.npc.getBrain().getMemory(MemoryModuleType.WALK_TARGET).isEmpty() && !this.npc.isSleeping()) {
                float spd = this.npc.isInWaterOrRain() && this.npc.distanceToSqr(pos.pos().getX() + 0.5, pos.pos().getY(), pos.pos().getZ() + 0.5) > 25 ? 1.3f : 1;
                this.setWalkTargetTo(pos.pos(), spd, 0, () -> this.npc.setBedPos(null));
            }
            BlockState blockState;
            if (pos.pos().closerToCenterThan(this.npc.position(), 2.0) && (blockState = this.npc.level.getBlockState(pos.pos())).is(BlockTags.BEDS) && !blockState.getValue(BedBlock.OCCUPIED)) {
                this.npc.startSleeping(pos.pos());
                this.npc.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
            }
            return true;
        }
        return false;
    }

    public boolean work() {
        GlobalPos pos = this.npc.getWorkPlace();
        if (pos != null && this.npc.level.dimension() == pos.dimension()) {
            if (pos.pos().closerToCenterThan(this.npc.position(), 2.0)) {
                return true;
            }
            if (this.npc.getBrain().getMemory(MemoryModuleType.WALK_TARGET).isEmpty()) {
                this.setWalkTargetTo(pos.pos(), 1, 1, () -> this.npc.setWorkPlace(null));
            }
            return true;
        }
        return false;
    }

    public boolean goMeet() {
        if (this.seekShelter())
            return true;
        GlobalPos pos = this.npc.getMeetingPos();
        if (pos != null && this.npc.level.dimension() == pos.dimension()) {
            if (pos.pos().closerToCenterThan(this.npc.position(), 6.0) && this.walkCooldown <= 0) {
                Vec3 to = this.getRandomPosition(5);
                if (to != null) {
                    this.setWalkTarget(new BlockPos(to), 1, 0);
                    this.walkCooldown = this.npc.getRandom().nextInt(40) + 90;
                } else
                    this.walkCooldown = this.npc.getRandom().nextInt(20) + 30;
                return true;
            }
            if (this.npc.getBrain().getMemory(MemoryModuleType.WALK_TARGET).isEmpty()) {
                this.setWalkTargetTo(pos.pos(), 1, 1, () -> this.npc.setMeetingPos(null));
            }
            return true;
        }
        return false;
    }

    public boolean seekShelter() {
        if (this.npc.level.isRaining()) {
            if (--this.shelterPOISearchCooldown < 0) {
                Optional<GlobalPos> hide = this.npc.getBrain().getMemory(MemoryModuleType.HIDING_PLACE);
                if (hide.isEmpty()) {
                    PoiManager poiManager = ((ServerLevel) this.npc.level).getPoiManager();
                    Set<BlockPos> set = poiManager.findAllClosestFirst(p -> shelterPOIS.get().contains(p), p -> {
                        if (this.npc.getBedPos() != null)
                            return this.npc.blockPosition().distSqr(this.npc.getBedPos().pos()) > p.distSqr(this.npc.blockPosition());
                        return true;
                    }, this.npc.blockPosition(), 48, PoiManager.Occupancy.ANY).limit(5L).collect(Collectors.toSet());
                    boolean found = false;
                    for (BlockPos pos : set) {
                        AABB atPos = new AABB(pos).inflate(10);
                        if (this.npc.level.getEntities(EntityTypeTest.forClass(EntityNPCBase.class), atPos, e -> true).size() < 5 && this.isUnderRoof(pos)) {
                            this.npc.getBrain().setMemory(MemoryModuleType.HIDING_PLACE, GlobalPos.of(this.npc.level.dimension(), pos));
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        if (this.npc.getBedPos() != null && this.npc.getBedPos().dimension() == this.npc.level.dimension() && this.isUnderRoof(this.npc.getBedPos().pos()))
                            this.npc.getBrain().setMemory(MemoryModuleType.HIDING_PLACE, this.npc.getBedPos());
                        else if (this.npc.getWorkPlace() != null && this.npc.getWorkPlace().dimension() == this.npc.level.dimension() && this.isUnderRoof(this.npc.getWorkPlace().pos()))
                            this.npc.getBrain().setMemory(MemoryModuleType.HIDING_PLACE, this.npc.getWorkPlace());
                    }
                } else {
                    if (!this.isUnderRoof(hide.get().pos()))
                        this.npc.getBrain().eraseMemory(MemoryModuleType.HIDING_PLACE);
                }
                this.shelterPOISearchCooldown = 60 + this.npc.getRandom().nextInt(40);
            }
            if (--this.walkCooldown <= 0 && this.npc.getNavigation().isDone()) {
                this.npc.getBrain().getMemory(MemoryModuleType.HIDING_PLACE).ifPresent(p -> {
                    Vec3 target;
                    float spd = 1;
                    int acc = 0;
                    if (this.npc.distanceToSqr(p.pos().getX() + 0.5, p.pos().getY(), p.pos().getZ() + 0.5) < 49) {
                        target = this.getRandomPosition(6);
                        if (target != null && this.npc.level.canSeeSky(new BlockPos(target)))
                            target = null;
                    } else {
                        target = Vec3.atCenterOf(p.pos());
                        spd = 1.3f;
                        acc = 4;
                    }
                    if (target != null) {
                        this.setWalkTargetTo(new BlockPos(target), spd, acc, () -> this.npc.getBrain().eraseMemory(MemoryModuleType.HIDING_PLACE));
                        this.walkCooldown = this.npc.getRandom().nextInt(40) + 90;
                    } else
                        this.walkCooldown = this.npc.getRandom().nextInt(20) + 30;
                });
            }
            return true;
        }
        if (this.npc.getBrain().getMemory(MemoryModuleType.HIDING_PLACE).isPresent())
            this.npc.getBrain().eraseMemory(MemoryModuleType.HIDING_PLACE);
        return false;
    }

    public void wanderAround() {
        if (this.seekShelter())
            return;
        if (this.walkCooldown <= 0 && this.npc.getNavigation().isDone()) {
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
                this.setWalkTarget(new BlockPos(pos), 1, 0);
                this.walkCooldown = this.npc.getRandom().nextInt(40) + 90;
            } else
                this.walkCooldown = this.npc.getRandom().nextInt(20) + 30;
        }
    }

    private void setWalkTargetTo(BlockPos pos, float speed, int accuracy, Runnable onFail) {
        if (this.tiredOfTryingToFindTarget(this.npc.level, this.npc)) {
            onFail.run();
        } else if (pos.distManhattan(this.npc.blockPosition()) > 100) {
            int i;
            Vec3 vec3 = null;
            int attempts = 300;
            for (i = 0; i < attempts && (vec3 == null || this.distManhattan(this.npc.position(), vec3) > 100); ++i) {
                vec3 = DefaultRandomPos.getPosTowards(this.npc, 15, 7, Vec3.atCenterOf(pos), 1.5707963705062866);
            }
            if (i == attempts || vec3 == null) {
                onFail.run();
                return;
            }
            this.setWalkTarget(new BlockPos(vec3), speed, accuracy);
        } else
            this.setWalkTarget(pos, speed, accuracy);
    }

    private boolean tiredOfTryingToFindTarget(Level level, EntityNPCBase npc) {
        return npc.getBrain().getMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE)
                .map(l -> level.getGameTime() - l > 1000).orElse(false);
    }

    private double distManhattan(Vec3 vec3, Vec3 other) {
        double f = Math.abs(vec3.x() - other.x());
        double g = Math.abs(vec3.y() - other.y());
        double h = Math.abs(vec3.z() - other.z());
        return f + g + h;
    }

    private void setWalkTarget(BlockPos pos, float speed, int accuracy) {
        this.npc.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(pos, speed, accuracy));
    }

    private void moveToTarget() {
        if (this.pathFindCooldown <= 0 && this.npc.getNavigation().isDone()) {
            this.npc.getBrain().getMemory(MemoryModuleType.WALK_TARGET).ifPresent(target -> {
                Path path = this.npc.getNavigation().createPath(target.getTarget().currentBlockPosition(), target.getCloseEnoughDist());
                if (path == null || !path.canReach()) {
                    if (!this.npc.getBrain().hasMemoryValue(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE))
                        this.npc.getBrain().setMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, this.npc.level.getGameTime());
                    if (path == null) {
                        this.pathFindCooldown = this.npc.getRandom().nextInt(30) + 15;
                        return;
                    }
                }
                this.npc.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
                this.npc.getNavigation().moveTo(path, target.getSpeedModifier());
                this.pathFindCooldown = this.npc.getRandom().nextInt(15) + 15;
            });
        }
        this.npc.getBrain().getMemory(MemoryModuleType.WALK_TARGET).ifPresent(target -> {
            if (this.npc.blockPosition().distManhattan(target.getTarget().currentBlockPosition()) <= target.getCloseEnoughDist()) {
                this.npc.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
                this.npc.getNavigation().stop();
            }
        });
    }

    private boolean isUnderRoof(BlockPos pos) {
        return BlockPos.betweenClosedStream(pos.offset(-2, 1, -2), pos.offset(2, 1, 2))
                .noneMatch(p -> this.npc.level.canSeeSky(p) && this.npc.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, p).getY() <= p.getY());
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
