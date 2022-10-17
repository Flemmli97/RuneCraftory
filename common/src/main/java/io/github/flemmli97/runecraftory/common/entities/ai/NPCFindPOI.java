package io.github.flemmli97.runecraftory.common.entities.ai;

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
            GlobalPos found = this.findAndTakePOI(serverLevel, PoiType.HOME);
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
        this.poiCheck(this.npc.getWorkPlace(), this.npc.getShop().poiType.get(), serverLevel, this.npc::setWorkPlace);
        this.poiCheck(this.npc.getMeetingPos(), PoiType.MEETING, serverLevel, this.npc::setMeetingPos);
        this.cooldown = this.npc.getWorkPlace() != null && this.npc.getBedPos() != null ? 20 : 10;
    }

    private void poiCheck(GlobalPos pos, PoiType poiType, ServerLevel serverLevel, Consumer<GlobalPos> set) {
        if (pos == null) {
            if (poiType != null) {
                GlobalPos found = this.findAndTakePOI(serverLevel, poiType);
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
            } else if (poiLevel == null || !poiLevel.getPoiManager().exists(blockPos, poiType.getPredicate())) {
                set.accept(null);
            }
        }
    }

    private GlobalPos findAndTakePOI(ServerLevel serverLevel, PoiType poiType) {
        PoiManager poiManager = serverLevel.getPoiManager();
        Set<BlockPos> set = poiManager.findAllClosestFirst(poiType.getPredicate(), p -> true, this.npc.blockPosition(), 48, PoiManager.Occupancy.HAS_SPACE).limit(5L).collect(Collectors.toSet());
        Path path = this.npc.getNavigation().createPath(set, poiType.getValidRange());
        if (path != null && path.canReach()) {
            BlockPos blockPos2 = path.getTarget();
            return poiManager.getType(blockPos2).map(type -> {
                poiManager.take(poiType.getPredicate(), blockPos2::equals, blockPos2, 1);
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
}
