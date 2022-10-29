package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.schedule.Activity;

public class ModActivities {

    public static PlatformRegistry<Activity> ACTIVITIES = PlatformUtils.INSTANCE.of(Registry.ACTIVITY_REGISTRY, RuneCraftory.MODID);

    public static RegistryEntrySupplier<Activity> DISABLED = ACTIVITIES.register("disabled", () -> Platform.INSTANCE.activity("disabled"));
    public static RegistryEntrySupplier<Activity> EARLYIDLE = ACTIVITIES.register("early_idle", () -> Platform.INSTANCE.activity("early_idle"));

}
