package io.github.flemmli97.runecraftory.common.entities.ai.behaviour;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.GlobalPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class SetWalkTargetFromMemory<E extends PathfinderMob, T> extends ExtendedBehaviour<E> {

    private static final MemoryTest MEMORIES = MemoryTest.builder(2)
            .noMemory(MemoryModuleType.WALK_TARGET)
            .usesMemories(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);

    protected final MemoryModuleType<T> poiMemory;
    protected final Function<T, GlobalPos> func;
    protected final Consumer<E> onInvalidate;
    protected Function<E, Float> speedMod = entity -> 1f;
    protected ToIntFunction<E> closeEnough = entity -> 0;

    public SetWalkTargetFromMemory(MemoryModuleType<T> poiMemory, Function<T, GlobalPos> func, Consumer<E> onInvalidate) {
        this.poiMemory = poiMemory;
        this.func = func;
        this.onInvalidate = onInvalidate;
        this.entryCondition.put(poiMemory, MemoryStatus.VALUE_PRESENT);
    }

    public static <E extends PathfinderMob> SetWalkTargetFromMemory<E, GlobalPos> create(MemoryModuleType<GlobalPos> poiMemory, Consumer<E> onInvalidate) {
        return new SetWalkTargetFromMemory<>(poiMemory, Function.identity(), onInvalidate);
    }

    public SetWalkTargetFromMemory<E, T> speed(float speedMod) {
        return this.speed(entity -> speedMod);
    }

    public SetWalkTargetFromMemory<E, T> speed(Function<E, Float> speedMod) {
        this.speedMod = speedMod;
        return this;
    }

    public SetWalkTargetFromMemory<E, T> closeEnough(int closeEnough) {
        return this.closeEnough(entity -> closeEnough);
    }

    public SetWalkTargetFromMemory<E, T> closeEnough(ToIntFunction<E> closeEnough) {
        this.closeEnough = closeEnough;
        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected void start(E entity) {
        T memory = BrainUtils.getMemory(entity, this.poiMemory);
        if (memory == null)
            return;
        GlobalPos pos = this.func.apply(memory);
        int range = this.closeEnough.applyAsInt(entity);
        if (this.tiredOfTryingToFindTarget(entity)) {
            this.onInvalidate.accept(entity);
        } else if (pos.pos().distManhattan(entity.blockPosition()) > range * 1.5) {
            Vec3 vec3 = DefaultRandomPos.getPosTowards(entity, 15, 7, Vec3.atCenterOf(pos.pos()), 90 * Mth.DEG_TO_RAD);
            if (vec3 == null) {
                BrainUtils.setMemory(entity, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, entity.level().getGameTime());
                return;
            }
            BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(pos.pos(), this.speedMod.apply(entity), range));
        }
    }

    private boolean tiredOfTryingToFindTarget(E entity) {
        return entity.getBrain().getMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE)
                .map(l -> entity.level().getGameTime() - l > 1000).orElse(false);
    }
}