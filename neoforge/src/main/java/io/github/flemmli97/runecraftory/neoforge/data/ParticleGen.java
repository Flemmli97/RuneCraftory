package io.github.flemmli97.runecraftory.neoforge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryParticles;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;

import java.util.List;

public class ParticleGen extends ParticleDescriptionProvider {

    protected ParticleGen(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, fileHelper);
    }

    @Override
    public void addDescriptions() {
        this.spriteSet(RuneCraftoryParticles.LIGHT.get());
        this.spriteSet(RuneCraftoryParticles.CROSS.get());
        this.spriteSet(RuneCraftoryParticles.BLINK.get());
        this.spriteSet(RuneCraftoryParticles.SMOKE.get(), 4);
        this.spriteSet(RuneCraftoryParticles.SLEEP.get());
        this.spriteSet(RuneCraftoryParticles.POISON.get());
        this.spriteSet(RuneCraftoryParticles.PARALYSIS.get(), RuneCraftory.modRes("paralysis_0"),
                RuneCraftory.modRes("paralysis_1"),
                RuneCraftory.modRes("paralysis_2"),
                RuneCraftory.modRes("paralysis_3"));
        this.spriteSet(RuneCraftoryParticles.LIGHTNING.get(), RuneCraftory.modRes("lightning_0"),
                RuneCraftory.modRes("lightning_1"),
                RuneCraftory.modRes("lightning_2"),
                RuneCraftory.modRes("lightning_3"));

        this.spriteSet(RuneCraftoryParticles.RUNEY.get(), RuneCraftory.modRes("runey_0"),
                RuneCraftory.modRes("runey_1"),
                RuneCraftory.modRes("runey_2"),
                RuneCraftory.modRes("runey_3"));

        this.empty(RuneCraftoryParticles.SKELEFANG_BONES.get());
        this.empty(RuneCraftoryParticles.BLOCK.get());
    }

    public void empty(ParticleType<?> type) {
        this.descriptions.put(BuiltInRegistries.PARTICLE_TYPE.getKey(type), List.of());
    }

    public void spriteSet(ParticleType<?> type) {
        this.spriteSet(type, BuiltInRegistries.PARTICLE_TYPE.getKey(type));
    }

    public void spriteSet(ParticleType<?> type, int num) {
        this.spriteSet(type, BuiltInRegistries.PARTICLE_TYPE.getKey(type), num, false);
    }
}
