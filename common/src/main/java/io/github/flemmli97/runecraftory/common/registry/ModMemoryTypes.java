package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.Optional;

public class ModMemoryTypes {

    public static final LoaderRegister<MemoryModuleType<?>> MEMORYIES = LoaderRegistryAccess.INSTANCE.of(Registries.MEMORY_MODULE_TYPE, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<MemoryModuleType<?>, MemoryModuleType<String>> LAST_ANIMATION = MEMORYIES.register("last_animation", () -> new MemoryModuleType<>(Optional.empty()));
}
