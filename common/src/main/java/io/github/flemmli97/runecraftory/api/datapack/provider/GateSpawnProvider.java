package io.github.flemmli97.runecraftory.api.datapack.provider;

import io.github.flemmli97.runecraftory.api.datapack.GateSpawnData;
import io.github.flemmli97.runecraftory.common.datapack.manager.GateSpawnsManager;
import io.github.flemmli97.tenshilib.common.data.provider.CodecBasedProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public abstract class GateSpawnProvider extends CodecBasedProvider<GateSpawnData> {

    public GateSpawnProvider(PackOutput output, String modid, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, PackOutput.Target.DATA_PACK, "Gate Spawns", modid, GateSpawnsManager.DIRECTORY, GateSpawnData.CODEC, provider);
    }

    public void addGateSpawn(GateSpawnData spawnData) {
        this.addGateSpawn(spawnData.entity(), spawnData);
    }

    public void addGateSpawn(ResourceLocation key, GateSpawnData spawnData) {
        this.contents.put(key, spawnData);
    }
}