package com.flemmli97.runecraftory.data;

import com.flemmli97.runecraftory.common.registry.ModParticles;
import com.google.common.collect.Lists;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ParticleGen implements IDataProvider {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    private final DataGenerator generator;

    protected final Map<ResourceLocation, List<ResourceLocation>> particleTextures = new LinkedHashMap<>();

    public ParticleGen(DataGenerator generator) {
        this.generator = generator;
    }

    public void add() {
        this.addTo(ModParticles.sinkingDust.get(), new ResourceLocation("generic_5"), new ResourceLocation("generic_6"),
                new ResourceLocation("generic_7"), new ResourceLocation("generic_6"), new ResourceLocation("generic_5"),
                new ResourceLocation("generic_4"), new ResourceLocation("generic_3"), new ResourceLocation("generic_2"),
                new ResourceLocation("generic_1"), new ResourceLocation("generic_0"));
        this.addTo(ModParticles.light.get());
        this.addTo(ModParticles.staticLight.get(), ModParticles.light.getId());
        this.addTo(ModParticles.cross.get());
        this.addTo(ModParticles.blink.get());
        this.addTo(ModParticles.smoke.get(), 4);
        this.addTo(ModParticles.wind.get(), new ResourceLocation("effect_7"), new ResourceLocation("effect_6"),
                new ResourceLocation("effect_5"), new ResourceLocation("effect_4"), new ResourceLocation("effect_3"),
                new ResourceLocation("effect_2"), new ResourceLocation("effect_1"), new ResourceLocation("effect_0"));
        this.addTo(ModParticles.sleep.get());
        this.addTo(ModParticles.poison.get());

    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        this.particleTextures.clear();
        this.add();
        this.particleTextures.forEach((particle, list) -> {
            Path path = this.getPath(particle);
            JsonObject obj = new JsonObject();
            JsonArray textures = new JsonArray();
            list.forEach(res -> textures.add(res.toString()));
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

    public void addTo(ParticleType<?> type, int nums) {
        List<ResourceLocation> list = new ArrayList<>();
        ResourceLocation typeRes = type.getRegistryName();
        for (int i = 0; i < nums; i++) {
            list.add(new ResourceLocation(typeRes.getNamespace(), typeRes.getPath() + "_" + i));
        }
        this.particleTextures.put(typeRes, list);
    }

    @Override
    public String getName() {
        return "Particle Jsons";
    }

    private Path getPath(ResourceLocation particle) {
        return generator.getOutputFolder().resolve("assets/" + particle.getNamespace() + "/particles/" + particle.getPath() + ".json");
    }
}
