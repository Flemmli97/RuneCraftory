package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.schedule.Activity;

public class ModActivities {

    public static final LoaderRegister<Activity> ACTIVITIES = LoaderRegistryAccess.INSTANCE.of(Registries.ACTIVITY, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<Activity, Activity> EARLY_IDLE = ACTIVITIES.register("early_idle", () -> new Activity("early_idle"));
    public static final RegistryEntrySupplier<Activity, Activity> STAY = ACTIVITIES.register("stay", () -> new Activity("stay"));
}
