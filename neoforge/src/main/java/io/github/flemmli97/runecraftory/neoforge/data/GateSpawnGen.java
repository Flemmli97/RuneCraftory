package io.github.flemmli97.runecraftory.neoforge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.provider.GateSpawnProvider;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class GateSpawnGen extends GateSpawnProvider {

    public GateSpawnGen(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
        super(packOutput, RuneCraftory.MODID, provider);
    }

    @Override
    protected void add(HolderLookup.Provider provider) {
        ModEntities.getDefaultGateSpawns().forEach(this::addGateSpawn);
    }
}
