package io.github.flemmli97.runecraftory.common.entities.ai.behaviour;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.data.AnimationPlayHolder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.memory.MoreMemoryModules;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class SetChargeTarget<E extends ChargingMonster> extends ExtendedBehaviour<E> {

    private static final MemoryTest MEMORIES = MemoryTest.builder(1)
            .hasMemories(MemoryModuleType.ATTACK_TARGET)
            .hasMemories(MoreMemoryModules.ANIMATION_TO_PLAY.get());

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected void start(E entity) {
        AnimationPlayHolder<?> anim = BrainUtils.getMemory(entity, MoreMemoryModules.ANIMATION_TO_PLAY.get());
        entity.setChargeMotion(entity.getChargeTo(anim.animation()));
        entity.lookAt(BrainUtils.getTargetOfEntity(entity), 360, 10);
    }
}
