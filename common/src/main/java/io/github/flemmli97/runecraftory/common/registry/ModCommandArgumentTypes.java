package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.registries.Registries;

public class ModCommandArgumentTypes {

    public static final LoaderRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES = LoaderRegistryAccess.INSTANCE.of(Registries.COMMAND_ARGUMENT_TYPE, RuneCraftory.MODID);
}
