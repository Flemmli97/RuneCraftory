package io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class LookAtInteractingPlayer extends ExtendedBehaviour<EntityNPCBase> {

    private static final MemoryTest MEMORIES = MemoryTest.builder(1)
            .usesMemories(MemoryModuleType.LOOK_TARGET);

    public LookAtInteractingPlayer() {
        this.noTimeout();
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, EntityNPCBase entity) {
        Player player = entity.getLastInteractedPlayer();
        return entity.isAlive() && player != null;
    }

    @Override
    protected boolean shouldKeepRunning(EntityNPCBase entity) {
        Player player = entity.getLastInteractedPlayer();
        return entity.isAlive() && player != null;
    }

    @Override
    protected void start(EntityNPCBase entity) {
        BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, new EntityTracker(entity.getLastInteractedPlayer(), true));
    }

    @Override
    protected void stop(EntityNPCBase entity) {
        super.stop(entity);
        BrainUtils.clearMemory(entity, MemoryModuleType.LOOK_TARGET);
    }

    @Override
    protected void tick(EntityNPCBase entity) {
        super.tick(entity);
        BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, new EntityTracker(entity.getLastInteractedPlayer(), true));
        BrainUtils.clearMemory(entity, MemoryModuleType.PATH);
        BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
    }
}