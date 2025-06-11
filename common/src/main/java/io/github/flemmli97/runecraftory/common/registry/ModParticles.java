package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.particles.BlockParticleType;
import io.github.flemmli97.runecraftory.common.particles.BlockStateParticleData;
import io.github.flemmli97.runecraftory.common.particles.ColoredParticle4fType;
import io.github.flemmli97.runecraftory.common.particles.ColoredParticleData4f;
import io.github.flemmli97.runecraftory.common.particles.DurationalParticleData;
import io.github.flemmli97.runecraftory.common.particles.GenericParticleType;
import io.github.flemmli97.runecraftory.common.particles.SkelefangParticleData;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleType;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;

public class ModParticles {

    public static final LoaderRegister<ParticleType<?>> PARTICLES = LoaderRegistryAccess.INSTANCE.of(Registries.PARTICLE_TYPE, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<ColoredParticleData>> SINKING_DUST = registerColoredParticleType("sinking_dust", false);
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<ColoredParticleData>> LIGHT = registerColoredParticleType("light", false);
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<ColoredParticleData>> SHORT_LIGHT = registerColoredParticleType("short_light", false);
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<ColoredParticleData>> CROSS = registerColoredParticleType("cross", false);
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<ColoredParticleData>> SMOKE = registerColoredParticleType("smoke", false);
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<ColoredParticleData>> BLINK = registerColoredParticleType("blink", false);
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<ColoredParticleData>> STATIC_LIGHT = registerColoredParticleType("static_light", false);
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<ColoredParticleData4f>> CIRCLING_LIGHT = PARTICLES.register("circling_light", () -> new ColoredParticle4fType(false));
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<ColoredParticleData4f>> VORTEX = PARTICLES.register("vortex", () -> new ColoredParticle4fType(false));
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<ColoredParticleData>> WIND = registerColoredParticleType("wind", false);
    public static final RegistryEntrySupplier<ParticleType<?>, SimpleParticleType> LIGHTNING = PARTICLES.register("lightning", () -> Platform.INSTANCE.simple(false));
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<ColoredParticleData4f>> TORNADO = PARTICLES.register("tornado", () -> new ColoredParticle4fType(false));
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<BlockStateParticleData>> BLOCK = PARTICLES.register("block", () -> new BlockParticleType(false));

    public static final RegistryEntrySupplier<ParticleType<?>, SimpleParticleType> RUNEY = PARTICLES.register("runey", () -> Platform.INSTANCE.simple(false));

    public static final RegistryEntrySupplier<ParticleType<?>, SimpleParticleType> SLEEP = PARTICLES.register("sleep", () -> Platform.INSTANCE.simple(false));
    public static final RegistryEntrySupplier<ParticleType<?>, SimpleParticleType> POISON = PARTICLES.register("poison", () -> Platform.INSTANCE.simple(false));
    public static final RegistryEntrySupplier<ParticleType<?>, SimpleParticleType> PARALYSIS = PARTICLES.register("paralysis", () -> Platform.INSTANCE.simple(false));

    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<SkelefangParticleData>> SKELEFANG_BONES = PARTICLES.register("skelefang_bones", () -> new GenericParticleType<>(false, SkelefangParticleData.DESERIALIZER, SkelefangParticleData.codec()));
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<DurationalParticleData>> DURATIONAL_PARTICLE = PARTICLES.register("particle_with_duration", () -> new GenericParticleType<>(false, DurationalParticleData.DESERIALIZER, DurationalParticleData.codec()));

    public static RegistryEntrySupplier<ParticleType<?>, ParticleType<ColoredParticleData>> registerColoredParticleType(String name, boolean alwaysShow) {
        return PARTICLES.register(name, () -> new ColoredParticleType(alwaysShow));
    }
}
