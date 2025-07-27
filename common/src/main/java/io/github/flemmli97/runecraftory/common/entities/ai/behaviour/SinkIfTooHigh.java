package io.github.flemmli97.runecraftory.common.entities.ai.behaviour;

import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class SinkIfTooHigh<E extends Mob> extends ExtendedBehaviour<E> {

    private int heightToSinkTo;

    public SinkIfTooHigh() {
        this.cooldownFor(e -> 20);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of();
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        if (!entity.isNoGravity() || BrainUtils.getTargetOfEntity(entity) != null || entity.getAttribute(Attributes.GRAVITY) == null)
            return false;
        int heightMap = level.getHeight(Heightmap.Types.MOTION_BLOCKING, entity.getBlockX(), entity.getBlockZ());
        if (entity.getY() - heightMap > 40) {
            this.heightToSinkTo = heightMap - 8;
            return true;
        }
        return false;
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        if (!entity.isNoGravity() || BrainUtils.getTargetOfEntity(entity) != null || entity.getAttribute(Attributes.GRAVITY) == null)
            return false;
        return entity.getY() > this.heightToSinkTo;
    }

    @Override
    protected void tick(E entity) {
        Vec3 delta = entity.getDeltaMovement();
        delta = new Vec3(delta.x(), Math.max(-entity.getAttributeValue(Attributes.GRAVITY), delta.y() - entity.getAttributeValue(Attributes.GRAVITY)), delta.z());
        entity.setDeltaMovement(delta);
        BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
        entity.getNavigation().stop();
    }
}
