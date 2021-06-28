package io.github.flemmli97.runecraftory.common.registry;

import com.flemmli97.tenshilib.common.particle.ColoredParticleData;
import com.flemmli97.tenshilib.common.particle.ColoredParticleType;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.particles.ColoredParticle4fType;
import io.github.flemmli97.runecraftory.common.particles.ColoredParticleData4f;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, RuneCraftory.MODID);

    public static final RegistryObject<ParticleType<ColoredParticleData>> sinkingDust = registerColoredParticleType("sinking_dust", false);
    public static final RegistryObject<ParticleType<ColoredParticleData>> light = registerColoredParticleType("light", false);
    public static final RegistryObject<ParticleType<ColoredParticleData>> cross = registerColoredParticleType("cross", false);
    public static final RegistryObject<ParticleType<ColoredParticleData>> smoke = registerColoredParticleType("smoke", false);
    public static final RegistryObject<ParticleType<ColoredParticleData>> blink = registerColoredParticleType("blink", false);
    public static final RegistryObject<ParticleType<ColoredParticleData>> staticLight = registerColoredParticleType("static_light", false);
    public static final RegistryObject<ParticleType<ColoredParticleData4f>> circlingLight = PARTICLES.register("circling_light", () -> new ColoredParticle4fType(false));
    public static final RegistryObject<ParticleType<ColoredParticleData4f>> vortex = PARTICLES.register("vortex", () -> new ColoredParticle4fType(false));
    public static final RegistryObject<ParticleType<ColoredParticleData>> wind = registerColoredParticleType("static_effect", false);
    public static final RegistryObject<BasicParticleType> sleep = PARTICLES.register("sleep", () -> new BasicParticleType(false));
    public static final RegistryObject<BasicParticleType> poison = PARTICLES.register("poison", () -> new BasicParticleType(false));

    public static final RegistryObject<ParticleType<ColoredParticleData>> registerColoredParticleType(String name, boolean alwaysShow) {
        return PARTICLES.register(name, () -> new ColoredParticleType(alwaysShow));
    }
}
