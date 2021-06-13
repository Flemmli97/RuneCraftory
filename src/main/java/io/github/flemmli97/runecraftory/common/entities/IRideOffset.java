package io.github.flemmli97.runecraftory.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public interface IRideOffset {

    Vector3d getOffset(Entity e);

    Vector3f cameraTransform(float pitch, float yaw, float roll);
}
