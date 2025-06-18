package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.registry.ArmorEffect;
import io.github.flemmli97.runecraftory.common.armoreffects.PiyoSandalsEffect;
import io.github.flemmli97.runecraftory.common.armoreffects.SimpleAccessoryEffect;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ModArmorEffects {

    public static final ResourceKey<? extends Registry<ArmorEffect>> ARMOR_EFFECT_KEY = ResourceKey.createRegistryKey(RuneCraftory.modRes("armor_effects"));
    public static final LoaderRegistryAccess.CustomLoaderRegistry<ArmorEffect> ARMOR_EFFECTS = LoaderRegistryAccess.INSTANCE.newRegistry(ARMOR_EFFECT_KEY,
            RuneCraftory.modRes("none"), true, true);

    public static final RegistryEntrySupplier<ArmorEffect, ArmorEffect> EMPTY = ARMOR_EFFECTS.register().register("none", ArmorEffect::new);

    public static final RegistryEntrySupplier<ArmorEffect, PiyoSandalsEffect> PIYO_SANDALS = ARMOR_EFFECTS.register().register("piyo_sandals", PiyoSandalsEffect::new);
    public static final RegistryEntrySupplier<ArmorEffect, SimpleAccessoryEffect> SHIELD_RING = ARMOR_EFFECTS.register().register("shield_ring", SimpleAccessoryEffect::new);
    public static final RegistryEntrySupplier<ArmorEffect, SimpleAccessoryEffect> MAGIC_RING = ARMOR_EFFECTS.register().register("magic_ring", SimpleAccessoryEffect::new);
    public static final RegistryEntrySupplier<ArmorEffect, SimpleAccessoryEffect> THROWING_RING = ARMOR_EFFECTS.register().register("throwing_ring", SimpleAccessoryEffect::new);
}
