package io.github.flemmli97.runecraftory.common.entities;

import net.minecraft.world.entity.LivingEntity;

public interface ExtendedEntity {

    boolean canBeAttackedBy(LivingEntity entity);
}
