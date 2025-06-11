package io.github.flemmli97.runecraftory.forge.data.worldgen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.GsonInstances;
import net.minecraft.core.Registry;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class WorldGenData<T> implements DataProvider {

    protected final Map<ResourceLocation, T> elements = new HashMap<>();
    private final ResourceKey<? extends Registry<?>> registryKey;
    private final Codec<T> elementCodec;
    private final PackOutput packOutput;

    public WorldGenData(PackOutput packOutput, ResourceKey<? extends Registry<?>> registryKey, Codec<T> elementCodec) {
        this.generator = generator;
        this.registryKey = registryKey;
        this.elementCodec = elementCodec;
    }

    protected abstract void gen();

    public T addElement(ResourceLocation res, T e) {
        this.elements.put(res, e);
        return e;
    }

    @Override
    public void run(HashCache cache) {
        this.elements.clear();
        this.gen();
        this.elements.forEach((res, e) -> {
            try {
                this.save(cache, res, e);
            } catch (IOException ex) {
                RuneCraftory.LOGGER.error(ex);
            }
        });
    }

    @Override
    public String getName() {
        return this.registryKey + " Data Gen";
    }

    public void runExternal(HashCache cache) {
        this.elements.forEach((res, e) -> {
            try {
                this.save(cache, res, e);
            } catch (IOException ex) {
                RuneCraftory.LOGGER.error(ex);
            }
        });
    }

    private void save(HashCache cache, ResourceLocation res, T e) throws IOException {
        Path path = this.getPath(res);
        JsonElement obj = this.elementCodec.encode(e, JsonOps.INSTANCE, new JsonObject()).result().orElseThrow();
        DataProvider.save(GsonInstances.GSON, cache, obj, path);
    }

    protected Path getPath(ResourceLocation id) {
        return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/" + this.registryKey.location().getPath().replace(":", "/") + "/" + id.getPath() + ".json");
    }
}
