package io.github.flemmli97.runecraftory.common.entities.ai.npc;

import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NPCFindPOI extends Goal {

    protected final EntityNPCBase npc;
    private int cooldown = 2;

    public NPCFindPOI(EntityNPCBase npc) {
        this.npc = npc;
    }

    @Override
    public boolean canUse() {
        return --this.cooldown <= 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void start() {
        GlobalPos pos = this.npc.getBedPos();
        ServerLevel serverLevel = (ServerLevel) this.npc.level;
        if (pos == null) {
            GlobalPos found = this.findAndTakePOI(serverLevel, PoiType.HOME, PoiType.HOME.getPredicate(), p -> this.isNearOf(serverLevel, p, this.npc.getWorkPlace()));
            if (found != null)
                this.npc.setBedPos(found);
        } else {
            BlockPos blockPos = pos.pos();
            ServerLevel poiLevel = serverLevel.dimension() != pos.dimension() ? serverLevel.getServer().getLevel(pos.dimension()) : serverLevel;
            if (poiLevel == null || !poiLevel.getPoiManager().exists(blockPos, PoiType.HOME.getPredicate())) {
                this.npc.setBedPos(null);
            } else if (this.bedIsOccupied(poiLevel, blockPos)) {
                this.npc.setBedPos(null);
                poiLevel.getPoiManager().release(blockPos);
                DebugPackets.sendPoiTicketCountPacket(poiLevel, blockPos);
            }
        }
        this.poiCheck(this.npc.getWorkPlace(), this.npc.getShop().poiType.get(), this.npc.getShop().predicate, serverLevel, p -> this.isNearOf(serverLevel, p, this.npc.getBedPos()), this.npc::setWorkPlace);
        this.poiCheck(this.npc.getMeetingPos(), PoiType.MEETING, PoiType.MEETING.getPredicate(), serverLevel, p -> true, this.npc::setMeetingPos);
        this.cooldown = this.npc.getWorkPlace() != null && this.npc.getBedPos() != null ? 20 : 10;
    }

    private void poiCheck(GlobalPos pos, PoiType poiType, Predicate<PoiType> predicate, ServerLevel serverLevel, Predicate<BlockPos> pred, Consumer<GlobalPos> set) {
        if (pos == null) {
            if (poiType != null) {
                GlobalPos found = this.findAndTakePOI(serverLevel, poiType, predicate, pred);
                if (found != null)
                    set.accept(found);
            }
        } else {
            BlockPos blockPos = pos.pos();
            ServerLevel poiLevel = serverLevel.dimension() != pos.dimension() ? serverLevel.getServer().getLevel(pos.dimension()) : serverLevel;
            if (poiType == null) {
                if (poiLevel != null && poiLevel.getPoiManager().exists(blockPos, t -> true))
                    poiLevel.getPoiManager().release(blockPos);
                set.accept(null);
            } else if (poiLevel == null || !poiLevel.getPoiManager().exists(blockPos, predicate)) {
                set.accept(null);
            }
        }
    }

    private GlobalPos findAndTakePOI(ServerLevel serverLevel, PoiType poiType, Predicate<PoiType> predicate, Predicate<BlockPos> pred) {
        PoiManager poiManager = serverLevel.getPoiManager();
        Set<BlockPos> set = poiManager.findAllClosestFirst(predicate, pred, this.npc.blockPosition(), 48, PoiManager.Occupancy.HAS_SPACE).limit(5L).collect(Collectors.toSet());
        if (set.isEmpty())
            return null;
        Path path = this.npc.getNavigation().createPath(set, poiType.getValidRange());
        if (path != null && path.canReach()) {
            BlockPos blockPos2 = path.getTarget();
            if (this.bedIsOccupied(serverLevel, blockPos2))
                return null;
            return poiManager.getType(blockPos2).map(type -> {
                poiManager.take(predicate, blockPos2::equals, blockPos2, 1);
                DebugPackets.sendPoiTicketCountPacket(serverLevel, blockPos2);
                return GlobalPos.of(serverLevel.dimension(), blockPos2);
            }).orElse(null);
        }
        return null;
    }

    private boolean bedIsOccupied(ServerLevel serverLevel, BlockPos blockPos) {
        BlockState blockState = serverLevel.getBlockState(blockPos);
        return blockState.is(BlockTags.BEDS) && blockState.getValue(BedBlock.OCCUPIED) && !this.npc.isSleeping();
    }

    private boolean isNearOf(ServerLevel level, BlockPos pos, GlobalPos gPos) {
        if (gPos == null)
            return true;
        if (level.dimension() != gPos.dimension())
            return false;
        return gPos.pos().closerThan(pos, 16);
    }
}
