package io.github.flemmli97.runecraftory.common.entities;

import com.mojang.math.Vector3f;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public interface IRideOffset {

    Vec3 getOffset(Entity e);

    Vector3f cameraTransform(float pitch, float yaw, float roll);
}
