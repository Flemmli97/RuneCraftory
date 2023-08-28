package io.github.flemmli97.runecraftory.mixinhelper;

import com.mojang.math.Vector3f;

public interface RotationFromMatrix {

    /**
     * Gets the matrix rotation assuming they are done in ZYX order
     */
    Vector3f getMatrixRotationZYX();
}
