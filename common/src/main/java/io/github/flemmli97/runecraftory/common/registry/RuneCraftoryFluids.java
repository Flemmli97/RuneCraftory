package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.fluid.HotSpringFluid;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;

public class RuneCraftoryFluids {

    public static final LoaderRegister<Fluid> FLUIDS = LoaderRegistryAccess.INSTANCE.of(Registries.FLUID, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<Fluid, HotSpringFluid.Flowing> FLOWING_HOT_SPRING_WATER = FLUIDS.register("flowing_hot_spring_water", HotSpringFluid.Flowing::new);
    public static final RegistryEntrySupplier<Fluid, HotSpringFluid.Source> HOT_SPRING_WATER = FLUIDS.register("hot_spring_water", HotSpringFluid.Source::new);
}
