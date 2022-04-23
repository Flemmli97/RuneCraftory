package io.github.flemmli97.runecraftory.forge.data.worldgen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class WorldGenData<T> implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final ResourceKey<? extends Registry<?>> registryKey;
    private final Codec<T> elementCodec;
    private final String modid;
    private final DataGenerator generator;

    protected final Map<ResourceLocation, T> elements = new HashMap<>();

    public WorldGenData(DataGenerator generator, String modid, ResourceKey<? extends Registry<?>> registryKey, Codec<T> elementCodec) {
        this.generator = generator;
        this.registryKey = registryKey;
        this.elementCodec = elementCodec;
        this.modid = modid;
    }

    protected abstract void gen();

    public T addElement(ResourceLocation res, T e) {
        this.elements.put(res, e);
        return e;
    }

    @Override
    public void run(HashCache cache) throws IOException {
        this.elements.clear();
        this.gen();
        this.elements.forEach((res, e) -> {
            try {
                this.save(cache, res, e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public void runExternal(HashCache cache) throws IOException {
        this.elements.forEach((res, e) -> {
            try {
                this.save(cache, res, e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void save(HashCache cache, ResourceLocation res, T e) throws IOException {
        Path path = this.getPath(res);
        JsonElement obj = this.elementCodec.encode(e, JsonOps.INSTANCE, new JsonObject()).result().orElseThrow();
        DataProvider.save(GSON, cache, obj, path);
    }

    protected Path getPath(ResourceLocation id) {
        return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/" + this.registryKey.location().getPath().replace(":", "/") + "/" + id.getPath() + ".json");
    }

    @Override
    public String getName() {
        return this.registryKey + " Data Gen";
    }
}
