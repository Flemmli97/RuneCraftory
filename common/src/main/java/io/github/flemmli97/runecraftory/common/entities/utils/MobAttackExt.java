package io.github.flemmli97.runecraftory.common.entities.utils;

import io.github.flemmli97.tenshilib.common.entity.ai.TargetPosition;

public interface MobAttackExt {

    TargetPosition getTargetPosition();

    default boolean reversed() {
        return false;
    }
}
