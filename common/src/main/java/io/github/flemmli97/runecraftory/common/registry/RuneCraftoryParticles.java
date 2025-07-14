package io.github.flemmli97.runecraftory.common.registry;

import com.mojang.serialization.MapCodec;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.particles.BlockStateParticleData;
import io.github.flemmli97.runecraftory.common.particles.ColoredParticleData4f;
import io.github.flemmli97.runecraftory.common.particles.DurationalParticleData;
import io.github.flemmli97.runecraftory.common.particles.ParticleTypeContainer;
import io.github.flemmli97.runecraftory.common.particles.SimpleParticleTypeAcc;
import io.github.flemmli97.runecraftory.common.particles.SkelefangParticleData;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleType;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;

public class RuneCraftoryParticles {

    public static final LoaderRegister<ParticleType<?>> PARTICLES = LoaderRegistryAccess.INSTANCE.of(Registries.PARTICLE_TYPE, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<ColoredParticleData>> SINKING_DUST = registerColoredParticleType("sinking_dust", false);
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<ColoredParticleData>> LIGHT = registerColoredParticleType("light", false);
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<ColoredParticleData>> SHORT_LIGHT = registerColoredParticleType("short_light", false);
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<ColoredParticleData>> CROSS = registerColoredParticleType("cross", false);
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<ColoredParticleData>> SMOKE = registerColoredParticleType("smoke", false);
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<ColoredParticleData>> BLINK = registerColoredParticleType("blink", false);
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<ColoredParticleData>> STATIC_LIGHT = registerColoredParticleType("static_light", false);
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleTypeContainer<ColoredParticleData4f>> CIRCLING_LIGHT = register("circling_light", false, ColoredParticleData4f::codec4f, ColoredParticleData4f::streamCodec4f);
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleType<ColoredParticleData>> WIND = registerColoredParticleType("wind", false);
    public static final RegistryEntrySupplier<ParticleType<?>, SimpleParticleType> LIGHTNING = register("lightning", false);
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleTypeContainer<ColoredParticleData4f>> TORNADO = register("tornado", false, ColoredParticleData4f::codec4f, ColoredParticleData4f::streamCodec4f);
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleTypeContainer<BlockStateParticleData>> BLOCK = register("block", false, BlockStateParticleData::codec, BlockStateParticleData::streamCodec);

    public static final RegistryEntrySupplier<ParticleType<?>, SimpleParticleType> RUNEY = register("runey", false);

    public static final RegistryEntrySupplier<ParticleType<?>, SimpleParticleType> SLEEP = register("sleep", false);
    public static final RegistryEntrySupplier<ParticleType<?>, SimpleParticleType> POISON = register("poison", false);
    public static final RegistryEntrySupplier<ParticleType<?>, SimpleParticleType> PARALYSIS = register("paralysis", false);

    public static final RegistryEntrySupplier<ParticleType<?>, ParticleTypeContainer<SkelefangParticleData>> SKELEFANG_BONES = register("skelefang_bones", false, SkelefangParticleData.CODEC, SkelefangParticleData.STREAM_CODEC);
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleTypeContainer<DurationalParticleData>> DURATIONAL_PARTICLE = register("particle_with_duration", false, DurationalParticleData.CODEC, DurationalParticleData.STREAM_CODEC);

    private static RegistryEntrySupplier<ParticleType<?>, ParticleType<ColoredParticleData>> registerColoredParticleType(String name, boolean alwaysShow) {
        return PARTICLES.register(name, () -> new ColoredParticleType(alwaysShow));
    }

    private static RegistryEntrySupplier<ParticleType<?>, SimpleParticleType> register(String name, boolean alwaysShow) {
        return PARTICLES.register(name, () -> new SimpleParticleTypeAcc(alwaysShow));
    }

    private static <T extends ParticleOptions> RegistryEntrySupplier<ParticleType<?>, ParticleTypeContainer<T>> register(String name, boolean alwaysShow, MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        return PARTICLES.register(name, () -> new ParticleTypeContainer<>(alwaysShow, codec, streamCodec));
    }

    private static <T extends ParticleOptions> RegistryEntrySupplier<ParticleType<?>, ParticleTypeContainer<T>> register(String name, boolean alwaysShow, Function<ParticleType<T>, MapCodec<T>> codecFactory,
                                                                                                                         Function<ParticleType<T>, StreamCodec<RegistryFriendlyByteBuf, T>> streamCodecFactory) {
        return PARTICLES.register(name, () -> new ParticleTypeContainer<>(alwaysShow, codecFactory, streamCodecFactory));
    }
}
