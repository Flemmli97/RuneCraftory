package io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc.social;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.common.entities.ai.memory.MeetingPoint;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryMemoryTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.level.pathfinder.Path;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class HandleMeetPoiTask<E extends NPCEntity> extends ExtendedBehaviour<E> {

    private static final MemoryTest MEMORIES = MemoryTest.builder(1)
            .noMemory(RuneCraftoryMemoryTypes.MEETING_LOCATION.get());

    private final Predicate<PoiRecord> valid = r -> r.getPoiType().value().maxTickets() > 1 ? r.hasSpace() : r.isOccupied();
    private final Predicate<PoiRecord> shouldAquire = r -> r.getPoiType().value().maxTickets() > 1 && r.hasSpace();

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected void start(E entity) {
        this.findAndTakePOI(entity);
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        MeetingPoint memory = BrainUtils.getMemory(entity, RuneCraftoryMemoryTypes.MEETING_LOCATION.get());
        if (memory == null)
            return false;
        GlobalPos pos = memory.pos();
        if (entity.level().dimension() != pos.dimension())
            return false;
        ServerLevel poiLevel = (ServerLevel) entity.level();
        return poiLevel.getPoiManager().exists(pos.pos(), entity.getNPCSchedule()::poiMatchesActivity);
    }

    @Override
    protected void stop(E entity) {
        BrainUtils.clearMemory(entity, RuneCraftoryMemoryTypes.MEETING_LOCATION.get());
    }

    private void findAndTakePOI(E entity) {
        ServerLevel serverLevel = (ServerLevel) entity.level();
        PoiManager poiManager = serverLevel.getPoiManager();
        BlockPos pos = entity.blockPosition();
        Set<PoiRecord> set = poiManager
                .getInRange(entity.getNPCSchedule()::poiMatchesActivity, pos, 48, PoiManager.Occupancy.ANY)
                .filter(this.valid)
                .sorted(Comparator.comparingDouble(r -> r.getPos().distSqr(pos)))
                .limit(5L)
                .collect(Collectors.toSet());
        if (set.isEmpty())
            return;
        Path path = this.tryNavigateTo(entity, set);
        if (path != null && path.canReach()) {
            BlockPos target = path.getTarget();
            PoiRecord record = set.stream().filter(r -> r.getPos().equals(target)).findFirst().orElse(null);
            if (record == null)
                return;
            boolean take = this.shouldAquire.test(record);
            if (take) {
                poiManager.take(e -> true, (h, pos2) -> pos2.equals(target), target, 1);
                DebugPackets.sendPoiTicketCountPacket(serverLevel, target);
            }
            GlobalPos poiPos = GlobalPos.of(serverLevel.dimension(), target);
            BrainUtils.setMemory(entity, RuneCraftoryMemoryTypes.MEETING_LOCATION.get(), new MeetingPoint(poiPos, record.getPoiType().unwrapKey().get(), take));
        }
    }

    private Path tryNavigateTo(E mob, Set<PoiRecord> poiPositions) {
        if (poiPositions.isEmpty()) {
            return null;
        }
        HashSet<BlockPos> set = new HashSet<>();
        int i = 1;
        for (PoiRecord record : poiPositions) {
            i = Math.max(i, record.getPoiType().value().validRange());
            set.add(record.getPos());
        }
        return mob.getNavigation().createPath(set, i);
    }
}
