package io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.InteractWithDoor;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class OpenDoors<E extends LivingEntity> extends ExtendedBehaviour<E> {

    private static final MemoryTest MEMORIES = MemoryTest.builder(3)
            .usesMemories(MemoryModuleType.PATH)
            .usesMemories(MemoryModuleType.DOORS_TO_CLOSE)
            .usesMemories(MemoryModuleType.NEAREST_LIVING_ENTITIES);

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return super.checkExtraStartConditions(level, entity);
    }

    @Override
    protected void start(E entity) {
        Path path = BrainUtils.getMemory(entity, MemoryModuleType.PATH);
        Set<GlobalPos> toClose = BrainUtils.memoryOrDefault(entity, MemoryModuleType.DOORS_TO_CLOSE, HashSet::new);
        Node previous = null;
        Node next = null;
        if (path != null) {
            previous = path.getPreviousNode();
            if (previous != null) {
                GlobalPos res = this.tryOpenDoor(entity, previous.asBlockPos());
                if (res != null)
                    toClose.add(res);
            }
            if (!path.isDone()) {
                next = path.getNextNode();
                GlobalPos res = this.tryOpenDoor(entity, next.asBlockPos());
                if (res != null)
                    toClose.add(res);
            }
        }
        InteractWithDoor.closeDoorsThatIHaveOpenedOrPassedThrough((ServerLevel) entity.level(), entity,
                previous, next, toClose, Optional.ofNullable(BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_LIVING_ENTITIES)));
    }

    protected GlobalPos tryOpenDoor(E entity, BlockPos pos) {
        BlockState state = entity.level().getBlockState(pos);
        if (state.is(BlockTags.MOB_INTERACTABLE_DOORS, s -> s.getBlock() instanceof DoorBlock)) {
            DoorBlock doorBlock = (DoorBlock) state.getBlock();
            if (!doorBlock.isOpen(state)) {
                doorBlock.setOpen(entity, entity.level(), state, pos, true);
            }
            return GlobalPos.of(entity.level().dimension(), pos);
        }
        return null;
    }
}
