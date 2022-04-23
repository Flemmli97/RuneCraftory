package io.github.flemmli97.runecraftory.mixinhelper;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;

public interface StructureModification {

    void addSpawn(MobCategory category, StructureSpawnOverride override);
}
