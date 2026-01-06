package io.github.flemmli97.runecraftory.common.entities.utils;

public interface MoveStateHolder {

    float interpolatedMoveTick(float partialTick);

    float interpolatedMoveTickOf(MoveType moveType, float partialTick);

}
