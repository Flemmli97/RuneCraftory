package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.api.datapack.provider.GateSpawnProvider;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import net.minecraft.data.PackOutput;

public class GateSpawnGen extends GateSpawnProvider {

    public GateSpawnGen(PackOutput packOutput) {
        super(gen);
    }

    @Override
    protected void add() {
        ModEntities.getDefaultGateSpawns().forEach(this::addGateSpawn);
    }
}
