package io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.Function;

public class SetWalkAroundPoiTarget<E extends PathfinderMob, T> extends SetRandomWalkTarget<E> {

    private final MemoryTest memoryTest;

    public SetWalkAroundPoiTarget(MemoryModuleType<T> memory, Function<T, GlobalPos> func, double maxDistance) {
        this.walkTargetPredicate((entity, target) -> {
            if (target == null)
                return false;
            T mem = BrainUtils.getMemory(entity, memory);
            if (mem == null)
                return false;
            GlobalPos pos = func.apply(mem);
            return entity.level().dimension() == pos.dimension() && pos.pos().closerToCenterThan(target, maxDistance);
        });
        this.entryCondition.put(memory, MemoryStatus.VALUE_PRESENT);
        this.memoryTest = MemoryTest.builder(2).noMemory(MemoryModuleType.WALK_TARGET)
                .hasMemory(memory);
    }

    public static <E extends PathfinderMob> SetWalkAroundPoiTarget<E, GlobalPos> create(MemoryModuleType<GlobalPos> poiMemory, double maxDistance) {
        return new SetWalkAroundPoiTarget<>(poiMemory, Function.identity(), maxDistance);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        if (this.memoryTest == null)
            return super.getMemoryRequirements();
        return this.memoryTest;
    }
}
