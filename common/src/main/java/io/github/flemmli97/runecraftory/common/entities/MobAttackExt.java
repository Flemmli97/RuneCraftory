package io.github.flemmli97.runecraftory.common.entities;

import net.minecraft.world.phys.Vec3;

public interface MobAttackExt {

    Vec3 targetPosition(Vec3 from);

    default boolean reversed() {
        return false;
    }
}
