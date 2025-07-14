package io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AcquirePOITask<E extends Mob> extends ExtendedBehaviour<E> {

    protected final MemoryModuleType<GlobalPos> poiMemory;

    protected final Predicate<Holder<PoiType>> predicate;
    protected BiPredicate<E, BlockPos> canAquire = (e, pos) -> true;
    protected Consumer<E> onAqcuire = e -> {
    };

    public AcquirePOITask(MemoryModuleType<GlobalPos> poiMemory, ResourceKey<PoiType> poi) {
        this(poiMemory, h -> h.is(poi));
    }

    public AcquirePOITask(MemoryModuleType<GlobalPos> poiMemory, Predicate<Holder<PoiType>> predicate) {
        this.poiMemory = poiMemory;
        this.predicate = predicate;
        this.cooldownFor(e -> 20);
        this.entryCondition.put(poiMemory, MemoryStatus.VALUE_ABSENT);
    }

    public static BiPredicate<NPCEntity, BlockPos> bedPredicate() {
        return (entity, pos) -> {
            BlockState blockState = entity.level().getBlockState(pos);
            return blockState.is(BlockTags.BEDS) && (!blockState.getValue(BedBlock.OCCUPIED) || entity.isSleeping());
        };
    }

    public static BiPredicate<NPCEntity, BlockPos> withinRangeOf(Function<NPCEntity, GlobalPos> func) {
        return (entity, pos) -> {
            GlobalPos gPos = func.apply(entity);
            if (gPos == null)
                return true;
            if (entity.level().dimension() != gPos.dimension())
                return false;
            return gPos.pos().closerThan(pos, 16);
        };
    }

    public AcquirePOITask<E> canAquire(BiPredicate<E, BlockPos> canAquire) {
        this.canAquire = canAquire;
        return this;
    }

    public AcquirePOITask<E> onAqcuire(Consumer<E> onAqcuire) {
        this.onAqcuire = onAqcuire;
        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of();
    }

    @Override
    protected void start(E entity) {
        this.findAndTakePOI(entity, this.predicate);
    }

    private void findAndTakePOI(E entity, Predicate<Holder<PoiType>> predicate) {
        ServerLevel serverLevel = (ServerLevel) entity.level();
        PoiManager poiManager = serverLevel.getPoiManager();
        Set<Pair<Holder<PoiType>, BlockPos>> set = poiManager.findAllClosestFirstWithType(predicate, p -> true, entity.blockPosition(), 48, PoiManager.Occupancy.HAS_SPACE).limit(5L)
                .filter(p -> this.canAquire.test(entity, p.getSecond()))
                .collect(Collectors.toSet());
        if (set.isEmpty())
            return;
        Path path = this.tryNavigateTo(entity, set);
        if (path != null && path.canReach()) {
            BlockPos target = path.getTarget();
            poiManager.getType(target).ifPresent(pos -> {
                poiManager.take(predicate, (h, pos2) -> pos2.equals(target), target, 1);
                DebugPackets.sendPoiTicketCountPacket(serverLevel, target);
                GlobalPos poiPos = GlobalPos.of(serverLevel.dimension(), target);
                BrainUtils.setMemory(entity, this.poiMemory, poiPos);
                this.onAqcuire.accept(entity);
            });
        }
    }

    public Path tryNavigateTo(E mob, Set<Pair<Holder<PoiType>, BlockPos>> poiPositions) {
        if (poiPositions.isEmpty()) {
            return null;
        }
        HashSet<BlockPos> set = new HashSet<>();
        int i = 1;
        for (Pair<Holder<PoiType>, BlockPos> pair : poiPositions) {
            i = Math.max(i, pair.getFirst().value().validRange());
            set.add(pair.getSecond());
        }
        return mob.getNavigation().createPath(set, i);
    }
}
