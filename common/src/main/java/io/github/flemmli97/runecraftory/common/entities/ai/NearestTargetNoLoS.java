package io.github.flemmli97.runecraftory.common.entities.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class NearestTargetNoLoS<T extends LivingEntity> extends NearestTargetHorizontal<T> {

    public NearestTargetNoLoS(Mob mob, Class<T> clss, int i, boolean mustReach, @Nullable Predicate<LivingEntity> predicate) {
        super(mob, clss, i, false, mustReach, predicate);
        this.targetConditions.ignoreLineOfSight();
    }
}
