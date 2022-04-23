package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.particles.ColoredParticle4fType;
import io.github.flemmli97.runecraftory.common.particles.ColoredParticleData4f;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleType;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

public class ModParticles {

    public static final PlatformRegistry<ParticleType<?>> PARTICLES = PlatformUtils.INSTANCE.of(Registry.PARTICLE_TYPE_REGISTRY, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<ParticleType<ColoredParticleData>> sinkingDust = registerColoredParticleType("sinking_dust", false);
    public static final RegistryEntrySupplier<ParticleType<ColoredParticleData>> light = registerColoredParticleType("light", false);
    public static final RegistryEntrySupplier<ParticleType<ColoredParticleData>> cross = registerColoredParticleType("cross", false);
    public static final RegistryEntrySupplier<ParticleType<ColoredParticleData>> smoke = registerColoredParticleType("smoke", false);
    public static final RegistryEntrySupplier<ParticleType<ColoredParticleData>> blink = registerColoredParticleType("blink", false);
    public static final RegistryEntrySupplier<ParticleType<ColoredParticleData>> staticLight = registerColoredParticleType("static_light", false);
    public static final RegistryEntrySupplier<ParticleType<ColoredParticleData4f>> circlingLight = PARTICLES.register("circling_light", () -> new ColoredParticle4fType(false));
    public static final RegistryEntrySupplier<ParticleType<ColoredParticleData4f>> vortex = PARTICLES.register("vortex", () -> new ColoredParticle4fType(false));
    public static final RegistryEntrySupplier<ParticleType<ColoredParticleData>> wind = registerColoredParticleType("wind", false);
    public static final RegistryEntrySupplier<SimpleParticleType> sleep = PARTICLES.register("sleep", () -> Platform.INSTANCE.simple(false));
    public static final RegistryEntrySupplier<SimpleParticleType> poison = PARTICLES.register("poison", () -> Platform.INSTANCE.simple(false));

    public static RegistryEntrySupplier<ParticleType<ColoredParticleData>> registerColoredParticleType(String name, boolean alwaysShow) {
        return PARTICLES.register(name, () -> new ColoredParticleType(alwaysShow));
    }
}
