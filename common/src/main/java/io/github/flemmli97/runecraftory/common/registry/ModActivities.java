package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.schedule.Activity;

public class ModActivities {

    public static final LoaderRegister<Activity> ACTIVITIES = LoaderRegistryAccess.INSTANCE.of(Registries.ACTIVITY, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<Activity, Activity> DISABLED = ACTIVITIES.register("disabled", () -> new Activity("disabled"));
    public static final RegistryEntrySupplier<Activity, Activity> EARLYIDLE = ACTIVITIES.register("early_idle", () -> new Activity("early_idle"));
    public static final RegistryEntrySupplier<Activity, Activity> FOLLOW = ACTIVITIES.register("early_idle", () -> new Activity("early_idle"));
    public static final RegistryEntrySupplier<Activity, Activity> FOLLOW_DISTANCE = ACTIVITIES.register("early_idle", () -> new Activity("early_idle"));

}
