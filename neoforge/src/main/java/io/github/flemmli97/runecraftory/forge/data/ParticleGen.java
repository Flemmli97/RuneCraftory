package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;

public class ParticleGen extends ParticleDescriptionProvider {

    protected ParticleGen(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, fileHelper);
    }

    @Override
    public void addDescriptions() {
        this.spriteSet(ModParticles.SINKING_DUST.get(), ResourceLocation.withDefaultNamespace("generic_5"), ResourceLocation.withDefaultNamespace("generic_6"),
                ResourceLocation.withDefaultNamespace("generic_7"), ResourceLocation.withDefaultNamespace("generic_6"), ResourceLocation.withDefaultNamespace("generic_5"),
                ResourceLocation.withDefaultNamespace("generic_4"), ResourceLocation.withDefaultNamespace("generic_3"), ResourceLocation.withDefaultNamespace("generic_2"),
                ResourceLocation.withDefaultNamespace("generic_1"), ResourceLocation.withDefaultNamespace("generic_0"));
        this.spriteSet(ModParticles.LIGHT.get());
        this.spriteSet(ModParticles.SHORT_LIGHT.get(), ModParticles.LIGHT.getID());
        this.spriteSet(ModParticles.STATIC_LIGHT.get(), ModParticles.LIGHT.getID());
        this.spriteSet(ModParticles.CIRCLING_LIGHT.get(), ModParticles.LIGHT.getID());
        this.spriteSet(ModParticles.VORTEX.get(), ModParticles.LIGHT.getID());
        this.spriteSet(ModParticles.CROSS.get());
        this.spriteSet(ModParticles.BLINK.get());
        this.spriteSet(ModParticles.SMOKE.get(), 4);
        this.spriteSet(ModParticles.WIND.get(), ResourceLocation.withDefaultNamespace("effect_7"), ResourceLocation.withDefaultNamespace("effect_6"),
                ResourceLocation.withDefaultNamespace("effect_5"), ResourceLocation.withDefaultNamespace("effect_4"), ResourceLocation.withDefaultNamespace("effect_3"),
                ResourceLocation.withDefaultNamespace("effect_2"), ResourceLocation.withDefaultNamespace("effect_1"), ResourceLocation.withDefaultNamespace("effect_0"));
        this.spriteSet(ModParticles.SLEEP.get());
        this.spriteSet(ModParticles.POISON.get());
        this.spriteSet(ModParticles.PARALYSIS.get(), RuneCraftory.modRes("paralysis_0"),
                RuneCraftory.modRes("paralysis_1"),
                RuneCraftory.modRes("paralysis_2"),
                RuneCraftory.modRes("paralysis_3"));
        this.spriteSet(ModParticles.LIGHTNING.get(), RuneCraftory.modRes("lightning_0"),
                RuneCraftory.modRes("lightning_1"),
                RuneCraftory.modRes("lightning_2"),
                RuneCraftory.modRes("lightning_3"));
        this.spriteSet(ModParticles.TORNADO.get(), ResourceLocation.withDefaultNamespace("effect_7"), ResourceLocation.withDefaultNamespace("effect_6"),
                ResourceLocation.withDefaultNamespace("effect_5"), ResourceLocation.withDefaultNamespace("effect_4"), ResourceLocation.withDefaultNamespace("effect_3"),
                ResourceLocation.withDefaultNamespace("effect_2"), ResourceLocation.withDefaultNamespace("effect_1"), ResourceLocation.withDefaultNamespace("effect_0"));

        this.spriteSet(ModParticles.RUNEY.get(), RuneCraftory.modRes("runey_0"),
                RuneCraftory.modRes("runey_1"),
                RuneCraftory.modRes("runey_2"),
                RuneCraftory.modRes("runey_3"));

        this.spriteSet(ModParticles.SKELEFANG_BONES.get());
        this.spriteSet(ModParticles.BLOCK.get());
        this.spriteSet(ModParticles.DURATIONAL_PARTICLE.get(), ModParticles.LIGHT.getID());
    }

    public void spriteSet(ParticleType<?> type) {
        this.spriteSet(type, BuiltInRegistries.PARTICLE_TYPE.getKey(type));
    }

    public void spriteSet(ParticleType<?> type, int num) {
        this.spriteSet(type, BuiltInRegistries.PARTICLE_TYPE.getKey(type), num, false);
    }
}
