package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.particles.ColoredParticle4fType;
import io.github.flemmli97.runecraftory.common.particles.ColoredParticleData4f;
import io.github.flemmli97.runecraftory.common.particles.MovingGoalParticleType;
import io.github.flemmli97.runecraftory.common.particles.SkelefangParticleData;
import io.github.flemmli97.runecraftory.common.particles.SkelefangParticleType;
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

    public static final RegistryEntrySupplier<ParticleType<ColoredParticleData>> SINKING_DUST = registerColoredParticleType("sinking_dust", false);
    public static final RegistryEntrySupplier<ParticleType<ColoredParticleData>> LIGHT = registerColoredParticleType("light", false);
    public static final RegistryEntrySupplier<ParticleType<ColoredParticleData>> SHORT_LIGHT = registerColoredParticleType("short_light", false);
    public static final RegistryEntrySupplier<ParticleType<ColoredParticleData>> CROSS = registerColoredParticleType("cross", false);
    public static final RegistryEntrySupplier<ParticleType<ColoredParticleData>> SMOKE = registerColoredParticleType("smoke", false);
    public static final RegistryEntrySupplier<ParticleType<ColoredParticleData>> BLINK = registerColoredParticleType("blink", false);
    public static final RegistryEntrySupplier<ParticleType<ColoredParticleData>> STATIC_LIGHT = registerColoredParticleType("static_light", false);
    public static final RegistryEntrySupplier<ParticleType<ColoredParticleData4f>> CIRCLING_LIGHT = PARTICLES.register("circling_light", () -> new ColoredParticle4fType(false));
    public static final RegistryEntrySupplier<ParticleType<ColoredParticleData4f>> VORTEX = PARTICLES.register("vortex", () -> new ColoredParticle4fType(false));
    public static final RegistryEntrySupplier<ParticleType<ColoredParticleData>> WIND = registerColoredParticleType("wind", false);
    public static final RegistryEntrySupplier<SimpleParticleType> LIGHTNING = PARTICLES.register("lightning", () -> Platform.INSTANCE.simple(false));
    public static final RegistryEntrySupplier<ParticleType<ColoredParticleData4f>> TORNADO = PARTICLES.register("tornado", () -> new ColoredParticle4fType(false));

    public static final RegistryEntrySupplier<SimpleParticleType> RUNEY = PARTICLES.register("runey", () -> Platform.INSTANCE.simple(false));

    public static final RegistryEntrySupplier<SimpleParticleType> SLEEP = PARTICLES.register("sleep", () -> Platform.INSTANCE.simple(false));
    public static final RegistryEntrySupplier<SimpleParticleType> POISON = PARTICLES.register("poison", () -> Platform.INSTANCE.simple(false));
    public static final RegistryEntrySupplier<SimpleParticleType> PARALYSIS = PARTICLES.register("paralysis", () -> Platform.INSTANCE.simple(false));

    public static final RegistryEntrySupplier<ParticleType<SkelefangParticleData>> SKELEFANG_BONES = PARTICLES.register("skelefang_bones", () -> new SkelefangParticleType(false));
    public static final RegistryEntrySupplier<MovingGoalParticleType> DURATIONAL_PARTICLE = PARTICLES.register("particle_with_duration", () -> new MovingGoalParticleType(false));

    public static RegistryEntrySupplier<ParticleType<ColoredParticleData>> registerColoredParticleType(String name, boolean alwaysShow) {
        return PARTICLES.register(name, () -> new ColoredParticleType(alwaysShow));
    }
}
