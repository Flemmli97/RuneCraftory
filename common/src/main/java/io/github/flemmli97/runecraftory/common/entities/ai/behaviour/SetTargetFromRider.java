package io.github.flemmli97.runecraftory.common.entities.ai.behaviour;

import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class SetTargetFromRider<E extends LivingEntity> extends ExtendedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORIES = MemoryTest.builder(1)
            .usesMemories(MemoryModuleType.ATTACK_TARGET);

    private LivingEntity target;

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E owner) {
        LivingEntity currentTarget = BrainUtils.getTargetOfEntity(owner);
        LivingEntity controller = owner.getControllingPassenger();
        if (controller == null)
            return false;
        this.target = BrainUtils.hasMemory(controller, MemoryModuleType.ATTACK_TARGET) ? BrainUtils.getTargetOfEntity(controller) : null;
        if (this.target == null && controller instanceof Mob mob) {
            this.target = mob.getTarget();
        }
        return currentTarget != this.target;
    }

    @Override
    protected void start(E entity) {
        if (this.target == null) {
            BrainUtils.clearMemory(entity, MemoryModuleType.ATTACK_TARGET);
        } else {
            BrainUtils.setTargetOfEntity(entity, this.target);
            BrainUtils.clearMemory(entity, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        }
    }
}
