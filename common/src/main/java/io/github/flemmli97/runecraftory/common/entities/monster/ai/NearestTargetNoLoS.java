package io.github.flemmli97.runecraftory.common.entities.monster.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class NearestTargetNoLoS<T extends LivingEntity> extends NearestTargetHorizontal<T> {

    public NearestTargetNoLoS(Mob mob, Class<T> class_, int i, boolean mustReach, @Nullable Predicate<LivingEntity> predicate) {
        super(mob, class_, i, false, mustReach, predicate);
        this.targetConditions.ignoreLineOfSight();
    }
}
