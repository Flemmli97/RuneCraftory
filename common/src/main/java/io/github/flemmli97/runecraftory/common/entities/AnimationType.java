package io.github.flemmli97.runecraftory.common.entities;

public enum AnimationType {

    /**
     * Used to decide whether a mob can attack.
     * All other enums are used when deciding if an attack of that type can happen
     */
    GENERICATTACK,
    MELEE,
    LEAP,
    CHARGE,
    RANGED,
    IDLE
}
