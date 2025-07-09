package io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc;

import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.util.BrainUtils;

public class SetWalkAroundPoiTarget<E extends PathfinderMob> extends SetRandomWalkTarget<E> {

    public SetWalkAroundPoiTarget(MemoryModuleType<GlobalPos> memory, double maxDistance) {
        this.walkTargetPredicate((entity, target) -> {
            GlobalPos pos = BrainUtils.getMemory(entity, memory);
            return pos != null && entity.level().dimension() == pos.dimension() && pos.pos().closerToCenterThan(target, maxDistance);
        });
        this.entryCondition.put(memory, MemoryStatus.VALUE_PRESENT);
    }
}
