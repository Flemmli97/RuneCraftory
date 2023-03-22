package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.provider.GateSpawnProvider;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import net.minecraft.data.DataGenerator;

public class GateSpawnGen extends GateSpawnProvider {

    public GateSpawnGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID);
    }

    @Override
    protected void add() {
        ModEntities.getDefaultGateSpawns().forEach(this::addGateSpawn);
    }
}
