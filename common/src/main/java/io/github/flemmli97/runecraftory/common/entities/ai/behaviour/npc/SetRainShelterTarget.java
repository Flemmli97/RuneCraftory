package io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCProfessions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class SetRainShelterTarget<E extends PathfinderMob> extends ExtendedBehaviour<E> {

    private static final Predicate<Holder<PoiType>> HIDING_POIS = poi ->
            RuneCraftoryNPCProfessions.PROFESSIONS.registry().stream().anyMatch(j -> j.matches(poi));

    private static final MemoryTest MEMORIES = MemoryTest.builder(1)
            .noMemory(MemoryModuleType.WALK_TARGET)
            .usesMemories(MemoryModuleType.HIDING_PLACE)
            .usesMemories(MemoryModuleType.HOME)
            .usesMemories(MemoryModuleType.JOB_SITE);

    protected ToDoubleFunction<E> range = e -> 10;
    protected ToIntFunction<E> wanderChance = e -> e.getRandom().nextInt(80);

    private WalkTarget target;

    public SetRainShelterTarget() {
        this.cooldownFor(e -> 20);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        if (!level.isRaining())
            return false;
        this.target = this.findWalkTarget(entity);
        return this.target != null;
    }

    @Override
    protected void start(E entity) {
        if (this.target != null) {
            BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, this.target);
        }
    }

    protected WalkTarget findWalkTarget(E entity) {
        GlobalPos hide = BrainUtils.getMemory(entity, MemoryModuleType.HIDING_PLACE);
        if (hide == null || hide.dimension() != entity.level().dimension()) {
            hide = this.findHidingPlace(entity);
        }
        if (hide != null) {
            double range = this.range.applyAsDouble(entity);
            Vec3 target = null;
            float spd = 1;
            int acc = 0;
            if (entity.distanceToSqr(hide.pos().getX() + 0.5, hide.pos().getY(), hide.pos().getZ() + 0.5) > range * range
                    || this.isInRain(entity)) {
                target = Vec3.atBottomCenterOf(hide.pos());
                spd = 1.3f;
                acc = 3;
            } else if (this.wanderChance.applyAsInt(entity) == 0) {
                target = this.getWanderPos(entity, hide.pos(), (int) range);
            }
            if (target != null)
                return new WalkTarget(target, spd, acc);
        }
        return null;
    }

    protected GlobalPos findHidingPlace(E entity) {
        PoiManager poiManager = ((ServerLevel) entity.level()).getPoiManager();
        GlobalPos home = BrainUtils.getMemory(entity, MemoryModuleType.HOME);
        GlobalPos work = BrainUtils.getMemory(entity, MemoryModuleType.JOB_SITE);
        Set<Pair<Holder<PoiType>, BlockPos>> set = poiManager.findAllClosestFirstWithType(HIDING_POIS, p -> {
            if (home != null && home.dimension() == entity.level().dimension())
                return entity.blockPosition().distSqr(home.pos()) > entity.blockPosition().distSqr(p);
            return true;
        }, entity.blockPosition(), 64, PoiManager.Occupancy.ANY).limit(5L).collect(Collectors.toSet());
        GlobalPos found = null;
        for (Pair<Holder<PoiType>, BlockPos> pos : set) {
            AABB atPos = new AABB(pos.getSecond()).inflate(10);
            if (entity.level().getEntities(EntityTypeTest.forClass(NPCEntity.class), atPos, e -> true).size() < 5 && this.isUnderRoof(entity, pos.getSecond())) {
                BrainUtils.setMemory(entity, MemoryModuleType.HIDING_PLACE, found = GlobalPos.of(entity.level().dimension(), pos.getSecond()));
                break;
            }
        }
        if (found == null) {
            if (home != null && home.dimension() == entity.level().dimension() && this.isUnderRoof(entity, home.pos()))
                BrainUtils.setMemory(entity, MemoryModuleType.HIDING_PLACE, found = home);
            else if (work != null && work.dimension() == entity.level().dimension() && this.isUnderRoof(entity, work.pos()))
                BrainUtils.setMemory(entity, MemoryModuleType.HIDING_PLACE, found = work);
        }
        return found;
    }

    protected boolean isUnderRoof(E entity, BlockPos pos) {
        return BlockPos.betweenClosedStream(pos.offset(-2, 1, -2), pos.offset(2, 1, 2))
                .noneMatch(p -> entity.level().canSeeSky(p) && entity.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, p).getY() <= p.getY());
    }

    protected boolean isInRain(E entity) {
        return this.isInRain(entity.level(), BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ()));
    }

    protected boolean isInRain(Level level, BlockPos pos) {
        return level.isRainingAt(pos);
    }

    protected Vec3 getWanderPos(E entity, BlockPos around, int radius) {
        for (int i = 0; i < 5; i++) {
            Vec3 target = LandRandomPos.getPos(entity, radius, radius);
            if (target != null && target.distanceToSqr(around.getX() + 0.5, around.getY(), around.getZ() + 0.5) < radius
                    && !this.isInRain(entity.level(), BlockPos.containing(target)))
                return target;
        }
        return null;
    }
}
