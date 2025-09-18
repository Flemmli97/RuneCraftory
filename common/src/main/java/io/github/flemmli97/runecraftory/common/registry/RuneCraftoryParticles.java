package io.github.flemmli97.runecraftory.common.registry;

import com.mojang.serialization.MapCodec;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.particles.BlockStateParticleData;
import io.github.flemmli97.runecraftory.common.particles.ParticleTypeContainer;
import io.github.flemmli97.runecraftory.common.particles.SimpleParticleTypeAcc;
import io.github.flemmli97.runecraftory.common.particles.SkelefangParticleData;
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

    public static final RegistryEntrySupplier<ParticleType<?>, SimpleParticleTypeAcc> LIGHT = registerSimple("light");
    public static final RegistryEntrySupplier<ParticleType<?>, SimpleParticleTypeAcc> CROSS = registerSimple("cross");
    public static final RegistryEntrySupplier<ParticleType<?>, SimpleParticleTypeAcc> BLINK = registerSimple("blink");
    public static final RegistryEntrySupplier<ParticleType<?>, SimpleParticleTypeAcc> SMOKE = registerSimple("smoke");
    public static final RegistryEntrySupplier<ParticleType<?>, SimpleParticleType> LIGHTNING = register("lightning");
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleTypeContainer<BlockStateParticleData>> BLOCK = register("block", BlockStateParticleData::codec, BlockStateParticleData::streamCodec);
    public static final RegistryEntrySupplier<ParticleType<?>, SimpleParticleType> RUNEY = register("runey");
    public static final RegistryEntrySupplier<ParticleType<?>, ParticleTypeContainer<SkelefangParticleData>> SKELEFANG_BONES = register("skelefang_bones", SkelefangParticleData.CODEC, SkelefangParticleData.STREAM_CODEC);
    public static final RegistryEntrySupplier<ParticleType<?>, SimpleParticleType> SLEEP = register("sleep");
    public static final RegistryEntrySupplier<ParticleType<?>, SimpleParticleType> POISON = register("poison");
    public static final RegistryEntrySupplier<ParticleType<?>, SimpleParticleType> PARALYSIS = register("paralysis");

    private static RegistryEntrySupplier<ParticleType<?>, SimpleParticleTypeAcc> registerSimple(String name) {
        return PARTICLES.register(name, () -> new SimpleParticleTypeAcc(false));
    }

    private static RegistryEntrySupplier<ParticleType<?>, SimpleParticleType> register(String name) {
        return PARTICLES.register(name, () -> new SimpleParticleTypeAcc(false));
    }

    private static <T extends ParticleOptions> RegistryEntrySupplier<ParticleType<?>, ParticleTypeContainer<T>> register(String name, MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        return PARTICLES.register(name, () -> new ParticleTypeContainer<>(false, codec, streamCodec));
    }

    private static <T extends ParticleOptions> RegistryEntrySupplier<ParticleType<?>, ParticleTypeContainer<T>> register(String name, Function<ParticleType<T>, MapCodec<T>> codecFactory,
                                                                                                                         Function<ParticleType<T>, StreamCodec<RegistryFriendlyByteBuf, T>> streamCodecFactory) {
        return PARTICLES.register(name, () -> new ParticleTypeContainer<>(false, codecFactory, streamCodecFactory));
    }
}
