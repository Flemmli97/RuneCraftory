package io.github.flemmli97.runecraftory.api.datapack.provider;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.common.datapack.manager.StructureBossManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class StructureBossProvider implements DataProvider {

    private final Map<ResourceLocation, StructureBossManager.BossSpawnList> data = new HashMap<>();

    private final PackOutput packOutput;
    private final String modid;
    private final FileVerifier verifier;
    protected final CompletableFuture<HolderLookup.Provider> provider;

    public StructureBossProvider(PackOutput packOutput, String modid, FileVerifier verifier, CompletableFuture<HolderLookup.Provider> provider) {
        this.packOutput = packOutput;
        this.modid = modid;
        this.verifier = verifier;
        this.provider = provider;
    }

    protected abstract void add(HolderLookup.Provider provider);

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return this.provider.thenApply(provider -> {
            this.add(provider);
            return provider;
        }).thenCompose(provider -> {
            DynamicOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, provider);
            ImmutableList.Builder<CompletableFuture<?>> futures = new ImmutableList.Builder<>();
            this.data.forEach((res, spawnData) -> {
                Path path = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(res.getNamespace() + "/" + StructureBossManager.DIRECTORY + "/" + res.getPath() + ".json");
                JsonElement obj = StructureBossManager.BossSpawnList.CODEC.encodeStart(JsonOps.INSTANCE, spawnData).getOrThrow();
                futures.add(DataProvider.saveStable(cache, obj, path));
            });
            return CompletableFuture.allOf(futures.build().toArray(CompletableFuture[]::new));
        });
    }

    @Override
    public String getName() {
        return "StructureBosses for " + this.modid;
    }

    public void addGateSpawn(ResourceLocation key, StructureBossManager.BossSpawnList spawnData) {
        this.data.put(key, spawnData);
        this.verifier.track(key, PackType.SERVER_DATA, StructureBossManager.DIRECTORY);
    }
}