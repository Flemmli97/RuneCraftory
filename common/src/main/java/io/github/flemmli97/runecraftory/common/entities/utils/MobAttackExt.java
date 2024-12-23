package io.github.flemmli97.runecraftory.common.entities.utils;

import net.minecraft.world.phys.Vec3;

public interface MobAttackExt {

    Vec3 getTargetPosition();

    default boolean reversed() {
        return false;
    }
}
