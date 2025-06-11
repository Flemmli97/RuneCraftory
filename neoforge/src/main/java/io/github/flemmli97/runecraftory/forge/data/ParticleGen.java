package io.github.flemmli97.runecraftory.forge.data;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.GsonInstances;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ParticleGen implements DataProvider {

    protected final Map<ResourceLocation, List<ResourceLocation>> particleTextures = new LinkedHashMap<>();
    private final PackOutput packOutput;

    public ParticleGen(PackOutput packOutput) {
        this.generator = generator;
    }

    public void add() {
        this.addTo(ModParticles.SINKING_DUST.get(), new ResourceLocation("generic_5"), new ResourceLocation("generic_6"),
                new ResourceLocation("generic_7"), new ResourceLocation("generic_6"), new ResourceLocation("generic_5"),
                new ResourceLocation("generic_4"), new ResourceLocation("generic_3"), new ResourceLocation("generic_2"),
                new ResourceLocation("generic_1"), new ResourceLocation("generic_0"));
        this.addTo(ModParticles.LIGHT.get());
        this.addTo(ModParticles.SHORT_LIGHT.get(), ModParticles.LIGHT.getID());
        this.addTo(ModParticles.STATIC_LIGHT.get(), ModParticles.LIGHT.getID());
        this.addTo(ModParticles.CIRCLING_LIGHT.get(), ModParticles.LIGHT.getID());
        this.addTo(ModParticles.VORTEX.get(), ModParticles.LIGHT.getID());
        this.addTo(ModParticles.CROSS.get());
        this.addTo(ModParticles.BLINK.get());
        this.addTo(ModParticles.SMOKE.get(), 4);
        this.addTo(ModParticles.WIND.get(), new ResourceLocation("effect_7"), new ResourceLocation("effect_6"),
                new ResourceLocation("effect_5"), new ResourceLocation("effect_4"), new ResourceLocation("effect_3"),
                new ResourceLocation("effect_2"), new ResourceLocation("effect_1"), new ResourceLocation("effect_0"));
        this.addTo(ModParticles.SLEEP.get());
        this.addTo(ModParticles.POISON.get());
        this.addTo(ModParticles.PARALYSIS.get(), RuneCraftory.modRes("paralysis_0"),
                RuneCraftory.modRes("paralysis_1"),
                RuneCraftory.modRes("paralysis_2"),
                RuneCraftory.modRes("paralysis_3"));
        this.addTo(ModParticles.LIGHTNING.get(), RuneCraftory.modRes("lightning_0"),
                RuneCraftory.modRes("lightning_1"),
                RuneCraftory.modRes("lightning_2"),
                RuneCraftory.modRes("lightning_3"));
        this.addTo(ModParticles.TORNADO.get(), new ResourceLocation("effect_7"), new ResourceLocation("effect_6"),
                new ResourceLocation("effect_5"), new ResourceLocation("effect_4"), new ResourceLocation("effect_3"),
                new ResourceLocation("effect_2"), new ResourceLocation("effect_1"), new ResourceLocation("effect_0"));

        this.addTo(ModParticles.RUNEY.get(), RuneCraftory.modRes("runey_0"),
                RuneCraftory.modRes("runey_1"),
                RuneCraftory.modRes("runey_2"),
                RuneCraftory.modRes("runey_3"));

        this.addTo(ModParticles.SKELEFANG_BONES.get());
        this.addTo(ModParticles.BLOCK.get());
        this.addTo(ModParticles.DURATIONAL_PARTICLE.get(), ModParticles.LIGHT.getID());
    }

    @Override
    public void run(HashCache cache) {
        this.particleTextures.clear();
        this.add();
        this.particleTextures.forEach((particle, list) -> {
            Path path = this.getPath(particle);
            JsonObject obj = new JsonObject();
            JsonArray textures = new JsonArray();
            list.forEach(res -> textures.add(res.toString()));
            obj.add("textures", textures);
            try {
                DataProvider.save(GsonInstances.GSON, cache, obj, path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public String getName() {
        return "Particle Jsons";
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

    private Path getPath(ResourceLocation particle) {
        return this.generator.getOutputFolder().resolve("assets/" + particle.getNamespace() + "/particles/" + particle.getPath() + ".json");
    }
}
