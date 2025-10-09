package io.github.flemmli97.runecraftory.common.entities.utils;

public interface MoveStateHolder {

    float interpolatedMoveTick(float partialTicks);

    float interpolatedMoveTickOf(MoveType moveType, float partialTicks);

}
