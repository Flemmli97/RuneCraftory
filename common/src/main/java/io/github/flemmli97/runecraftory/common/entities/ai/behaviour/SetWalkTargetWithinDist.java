package io.github.flemmli97.runecraftory.common.entities.ai.behaviour;

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.BiFunction;

public class SetWalkTargetWithinDist<E extends PathfinderMob> extends ExtendedBehaviour<E> {

    private static final MemoryTest MEMORIES = MemoryTest.builder(3).hasMemory(MemoryModuleType.ATTACK_TARGET)
            .usesMemories(MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET);

    protected double min = 1, max = 10;
    protected BiFunction<E, LivingEntity, Float> speedMod = (owner, target) -> 1f;

    public SetWalkTargetWithinDist<E> speedMod(float speedModifier) {
        return this.speedMod((owner, target) -> speedModifier);
    }

    public SetWalkTargetWithinDist<E> speedMod(BiFunction<E, LivingEntity, Float> speedModifier) {
        this.speedMod = speedModifier;
        return this;
    }

    public SetWalkTargetWithinDist<E> min(double min) {
        this.min = min;
        return this;
    }

    public SetWalkTargetWithinDist<E> max(double max) {
        this.max = max;
        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected void start(E entity) {
        Brain<?> brain = entity.getBrain();
        LivingEntity target = BrainUtils.getTargetOfEntity(entity);

        double distSqr = entity.distanceToSqr(target);
        if (!entity.getSensing().hasLineOfSight(target) || distSqr >= this.max * this.max) {
            int close = Mth.floor(Math.max(this.min, this.max - 2));
            BrainUtils.setMemory(brain, MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
            BrainUtils.setMemory(brain, MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(target, false), this.speedMod.apply(entity, target), close));
        } else if (distSqr <= this.min * this.min) {
            int range = Mth.ceil(this.max - Math.sqrt(distSqr));
            Vec3 posAway = DefaultRandomPos.getPosAway(entity, range, range, target.position());
            if (posAway != null) {
                BrainUtils.setMemory(brain, MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
                BrainUtils.setMemory(brain, MemoryModuleType.WALK_TARGET, new WalkTarget(posAway, this.speedMod.apply(entity, target), 0));
            }
        } else {
            BrainUtils.clearMemory(brain, MemoryModuleType.WALK_TARGET);
        }
    }
}
