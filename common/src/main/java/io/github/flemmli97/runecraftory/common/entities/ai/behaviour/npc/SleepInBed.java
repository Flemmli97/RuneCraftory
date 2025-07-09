package io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class SleepInBed<E extends LivingEntity> extends ExtendedBehaviour<E> {

    private static final MemoryTest MEMORIES = MemoryTest.builder(2)
            .hasMemories(MemoryModuleType.HOME)
            .usesMemories(MemoryModuleType.LAST_WOKEN);

    protected int retryCooldown = 100;

    public SleepInBed() {
        this.noTimeout();
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        if (entity.isPassenger()) {
            return false;
        } else {
            GlobalPos pos = BrainUtils.getMemory(entity, MemoryModuleType.HOME);
            if (level.dimension() != pos.dimension()) {
                return false;
            }
            Long last = BrainUtils.getMemory(entity, MemoryModuleType.LAST_WOKEN);
            if (last != null && Math.abs(level.getGameTime() - last) < this.retryCooldown) {
                return false;
            }
            return pos.pos().closerToCenterThan(entity.position(), 2.0F) &&
                    this.canSleepInBed(entity, pos.pos());
        }
    }

    protected boolean canSleepInBed(E entity, BlockPos pos) {
        BlockState blockState = entity.level().getBlockState(pos);
        return blockState.is(BlockTags.BEDS) && !blockState.getValue(BedBlock.OCCUPIED);
    }

    @Override
    protected void start(E entity) {
        entity.startSleeping(BrainUtils.getMemory(entity, MemoryModuleType.HOME).pos());
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        GlobalPos pos = BrainUtils.getMemory(entity, MemoryModuleType.HOME);
        if (pos == null) {
            return false;
        }
        BlockPos blockPos = pos.pos();
        return entity.getBrain().isActive(Activity.REST) && entity.getY() > blockPos.getY() + 0.4 && blockPos.closerToCenterThan(entity.position(), 1.14);
    }

    @Override
    protected void stop(E entity) {
        if (entity.isSleeping()) {
            entity.stopSleeping();
        }
    }
}
