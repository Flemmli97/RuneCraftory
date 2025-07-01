package io.github.flemmli97.runecraftory.common.entities.ai.behaviour;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.data.CircleData;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.memory.MoreMemoryModules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class CircleAround<E extends Mob> extends ExtendedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORIES = MemoryTest.builder(1).hasMemory(MoreMemoryModules.CIRCLE_DATA.get());

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return this.verifyTracker(entity);
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        return this.verifyTracker(entity);
    }

    @Override
    protected void tick(E entity) {
        BrainUtils.withMemory(entity, MoreMemoryModules.CIRCLE_DATA.get(), data -> this.circleAround(entity, data));
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    public void circleAround(E entity, CircleData data) {
        Vec3 pos = data.position().currentPosition();
        Vec3 dir = entity.position().subtract(pos.x(), entity.position().y(), pos.z())
                .normalize().scale(data.radius());
        dir = dir.yRot((data.clockWise() ? 20 : -20) * Mth.DEG_TO_RAD);
        entity.getNavigation().moveTo(pos.x() + dir.x(), entity.getY(), pos.z() + dir.z(), 1);
    }

    protected boolean verifyTracker(E entity) {
        CircleData center = BrainUtils.getMemory(entity, MoreMemoryModules.CIRCLE_DATA.get());
        if (center == null || (center.position() instanceof EntityTracker entityTracker && !entityTracker.getEntity().isAlive())) {
            if (center == null)
                BrainUtils.clearMemory(entity, MoreMemoryModules.CIRCLE_DATA.get());
            return false;
        }
        return true;
    }
}
