package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.items.ArmorEffect;
import io.github.flemmli97.runecraftory.common.armoreffects.PiyoSandalsEffect;
import io.github.flemmli97.runecraftory.platform.LazyGetter;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import io.github.flemmli97.tenshilib.platform.registry.SimpleRegistryWrapper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class ModArmorEffects {

    public static final ResourceKey<? extends Registry<ArmorEffect>> ARMOR_EFFECT_KEY = ResourceKey.createRegistryKey(new ResourceLocation(RuneCraftory.MODID, "armor_effects"));
    public static final Supplier<SimpleRegistryWrapper<ArmorEffect>> ARMOR_EFFECT_REGISTRY = new LazyGetter<>(() -> PlatformUtils.INSTANCE.registry(ARMOR_EFFECT_KEY));

    public static final PlatformRegistry<ArmorEffect> ARMOR_EFFECTS = PlatformUtils.INSTANCE.customRegistry(ArmorEffect.class, ARMOR_EFFECT_KEY, new ResourceLocation(RuneCraftory.MODID, "none"), true, true);
    public static final RegistryEntrySupplier<ArmorEffect> EMPTY = ARMOR_EFFECTS.register("none", ArmorEffect::new);

    public static final RegistryEntrySupplier<ArmorEffect> PIYO_SANDALS = ARMOR_EFFECTS.register("piyo_sandals", PiyoSandalsEffect::new);

}
