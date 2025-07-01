package io.github.flemmli97.runecraftory.common.entities.ai.behaviour;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.object.SquareRadius;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class SetWaterPrioritizingWalkTarget<E extends PathfinderMob> extends ExtendedBehaviour<E> {

    private static final MemoryTest MEMORIES = MemoryTest.builder(1)
            .noMemory(MemoryModuleType.WALK_TARGET);

    protected BiFunction<E, Vec3, Float> speedModifier = (entity, targetPos) -> 1f;
    protected SquareRadius radius = new SquareRadius(10, 7);
    protected BiPredicate<E, Vec3> positionPredicate = (entity, pos) -> true;

    public SetWaterPrioritizingWalkTarget<E> setRadius(double radius) {
        return this.setRadius(radius, radius);
    }

    public SetWaterPrioritizingWalkTarget<E> setRadius(double xz, double y) {
        this.radius = new SquareRadius(xz, y);
        return this;
    }

    public SetWaterPrioritizingWalkTarget<E> speedModifier(float modifier) {
        return this.speedModifier((entity, targetPos) -> modifier);
    }

    public SetWaterPrioritizingWalkTarget<E> speedModifier(BiFunction<E, Vec3, Float> function) {
        this.speedModifier = function;
        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected void start(E entity) {
        Vec3 targetPos = this.getTargetPos(entity);
        if (!this.positionPredicate.test(entity, targetPos))
            targetPos = null;
        if (targetPos == null) {
            BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
        } else {
            BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(targetPos, this.speedModifier.apply(entity, targetPos), 0));
        }
    }

    @Nullable
    protected Vec3 getTargetPos(E entity) {
        Vec3 target = null;
        Vec3 land = null;
        for (int i = 0; i < 10; i++) {
            Vec3 pos = DefaultRandomPos.getPos(entity, (int) this.radius.xzRadius(), (int) this.radius.yRadius());
            if (pos != null) {
                if (entity.level().getBlockState(BlockPos.containing(pos)).isPathfindable(PathComputationType.WATER)) {
                    target = pos;
                    break;
                } else
                    land = pos;
            }
        }
        if (target == null) {
            return land;
        }
        return target;
    }
}