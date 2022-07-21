package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.effects.ColdEffect;
import io.github.flemmli97.runecraftory.common.effects.ParalysisEffect;
import io.github.flemmli97.runecraftory.common.effects.PermanentEffect;
import io.github.flemmli97.runecraftory.common.effects.PoisonEffect;
import io.github.flemmli97.runecraftory.common.effects.SleepEffect;
import io.github.flemmli97.runecraftory.common.effects.TrueInvis;
import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ModEffects {

    public static final PlatformRegistry<MobEffect> EFFECTS = PlatformUtils.INSTANCE.of(Registry.MOB_EFFECT_REGISTRY, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<MobEffect> sleep = EFFECTS.register("sleep", SleepEffect::new);
    public static final RegistryEntrySupplier<MobEffect> poison = EFFECTS.register("poison", PoisonEffect::new);
    public static final RegistryEntrySupplier<MobEffect> paralysis = EFFECTS.register("paralysis", ParalysisEffect::new);
    public static final RegistryEntrySupplier<MobEffect> seal = EFFECTS.register("seal", () -> new PermanentEffect(MobEffectCategory.HARMFUL, 0, S2CEntityDataSync.Type.SEAL));
    public static final RegistryEntrySupplier<MobEffect> fatigue = EFFECTS.register("fatigue", () -> new PermanentEffect(MobEffectCategory.HARMFUL, 0, S2CEntityDataSync.Type.FATIGUE));
    public static final RegistryEntrySupplier<MobEffect> cold = EFFECTS.register("cold", ColdEffect::new);

    public static final RegistryEntrySupplier<MobEffect> trueInvis = EFFECTS.register("true_invis", TrueInvis::new);

}
