package io.github.flemmli97.runecraftory.fabric.mixinhelper;

import io.github.flemmli97.runecraftory.common.attachment.EntityData;

public interface EntityDataGetter {

    EntityData runecraftory$getEntityData();

    void runecraftory$effectCuringProcess(boolean inProgress);
}
