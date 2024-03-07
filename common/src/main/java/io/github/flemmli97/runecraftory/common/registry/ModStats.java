package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.stats.StatType;
import net.minecraft.world.entity.EntityType;

public class ModStats {

    public static final PlatformRegistry<StatType<?>> STATS = PlatformUtils.INSTANCE.of(Registry.STAT_TYPE_REGISTRY, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<StatType<EntityType<?>>> COMBINED_KILLS = STATS.register("combined_killed", () -> new StatType<>(Registry.ENTITY_TYPE));
}
