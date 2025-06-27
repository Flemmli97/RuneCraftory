package io.github.flemmli97.runecraftory.mixinhelper;

public interface PlayerExtended {

    /**
     * Vanillas version only returns true outside of a gui
     */
    default boolean runcraftory$hasActualShiftKeyDown() {
        return false;
    }
}
