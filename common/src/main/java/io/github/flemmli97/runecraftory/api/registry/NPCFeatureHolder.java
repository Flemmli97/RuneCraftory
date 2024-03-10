package io.github.flemmli97.runecraftory.api.registry;

import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;

public interface NPCFeatureHolder<F extends NPCFeature> {

    F create(EntityNPCBase npc);

    NPCFeatureType<F> getType();
}
