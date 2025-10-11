package io.github.flemmli97.runecraftory.api.datapack.provider;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.common.datapack.manager.CropManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class CropProvider implements DataProvider {

    private final Map<ResourceLocation, CropProperties.Builder> data = new HashMap<>();

    private final PackOutput packOutput;
    private final String modid;
    protected final CompletableFuture<HolderLookup.Provider> provider;

    public CropProvider(PackOutput packOutput, String modid, CompletableFuture<HolderLookup.Provider> provider) {
        this.packOutput = packOutput;
        this.modid = modid;
        this.provider = provider;
    }

    protected abstract void add(HolderLookup.Provider provider);

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return this.provider.thenApply(provider -> {
            this.add(provider);
            return provider;
        }).thenCompose(provider -> {
            DynamicOps<JsonElement> ops = provider.createSerializationContext(JsonOps.INSTANCE);
            ImmutableList.Builder<CompletableFuture<?>> futures = new ImmutableList.Builder<>();
            this.data.forEach((res, builder) -> {
                Path path = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(res.getNamespace() + "/" + CropManager.DIRECTORY + "/" + res.getPath() + ".json");
                JsonElement obj = CropProperties.CODEC.encodeStart(ops, builder.build()).getOrThrow();
                futures.add(DataProvider.saveStable(cache, obj, path));
            });
            return CompletableFuture.allOf(futures.build().toArray(CompletableFuture[]::new));
        });
    }

    @Override
    public String getName() {
        return "CropProps for " + this.modid;
    }

    public void addStat(CropProperties.Builder builder) {
        ResourceLocation res = ResourceLocation.fromNamespaceAndPath(this.modid, builder.tryGetId().getPath());
        this.addStat(res, builder);
    }

    public void addStat(ResourceLocation id, CropProperties.Builder builder) {
        if (this.data.put(id, builder) != null) {
            throw new IllegalStateException("Duplicate id " + id);
        }
    }
}
