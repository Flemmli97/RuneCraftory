package com.flemmli97.runecraftory.data;

import com.flemmli97.runecraftory.common.registry.ModParticles;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ParticleGen implements IDataProvider {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    private final DataGenerator generator;

    protected final Map<ResourceLocation, List<ResourceLocation>> particleTextures = Maps.newLinkedHashMap();

    public ParticleGen(DataGenerator generator) {
        this.generator = generator;
    }

    public void add() {
        this.addTo(ModParticles.puffParticle.get());
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        this.particleTextures.clear();
        this.add();
        this.particleTextures.forEach((particle, list)->{
            Path path = this.getPath(particle);
            JsonObject obj = new JsonObject();
            JsonArray textures = new JsonArray();
            list.forEach(res->textures.add(res.toString()));
            obj.add("textures", textures);
            try {
                IDataProvider.save(GSON, cache, obj, path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void addTo(ParticleType<?> type, ResourceLocation... textures) {
        this.particleTextures.put(type.getRegistryName(), Arrays.asList(textures));
    }

    public void addTo(ParticleType<?> type) {
        this.particleTextures.put(type.getRegistryName(), Lists.newArrayList(type.getRegistryName()));
    }

    @Override
    public String getName() {
        return "Particle Jsons";
    }

    private Path getPath(ResourceLocation particle) {
        return generator.getOutputFolder().resolve("assets/" + particle.getNamespace() + "/particles/" + particle.getPath() + ".json");
    }
}
