package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.effects.BathEffect;
import io.github.flemmli97.runecraftory.common.effects.BlitzEffect;
import io.github.flemmli97.runecraftory.common.effects.ColdEffect;
import io.github.flemmli97.runecraftory.common.effects.ParalysisEffect;
import io.github.flemmli97.runecraftory.common.effects.PermanentEffect;
import io.github.flemmli97.runecraftory.common.effects.PoisonEffect;
import io.github.flemmli97.runecraftory.common.effects.SleepEffect;
import io.github.flemmli97.runecraftory.common.effects.StunEffect;
import io.github.flemmli97.runecraftory.common.effects.TrueInvis;
import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ModEffects {

    public static final LoaderRegister<MobEffect> EFFECTS = LoaderRegistryAccess.INSTANCE.of(Registries.MOB_EFFECT, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<MobEffect, BathEffect> BATH = EFFECTS.register("bath", BathEffect::new);
    public static final RegistryEntrySupplier<MobEffect, BlitzEffect> BLITZ = EFFECTS.register("blitz", BlitzEffect::new);
    public static final RegistryEntrySupplier<MobEffect, ColdEffect> COLD = EFFECTS.register("cold", ColdEffect::new);
    public static final RegistryEntrySupplier<MobEffect, PermanentEffect> FATIGUE = EFFECTS.register("fatigue", () -> new PermanentEffect(MobEffectCategory.HARMFUL, 0, S2CEntityDataSync.DataType.FATIGUE));
    public static final RegistryEntrySupplier<MobEffect, ParalysisEffect> PARALYSIS = EFFECTS.register("paralysis", ParalysisEffect::new);
    public static final RegistryEntrySupplier<MobEffect, PoisonEffect> POISON = EFFECTS.register("poison", PoisonEffect::new);
    public static final RegistryEntrySupplier<MobEffect, PermanentEffect> SEAL = EFFECTS.register("sealed", () -> new PermanentEffect(MobEffectCategory.HARMFUL, 0, S2CEntityDataSync.DataType.SEAL));
    public static final RegistryEntrySupplier<MobEffect, SleepEffect> SLEEP = EFFECTS.register("sleeping", SleepEffect::new);
    public static final RegistryEntrySupplier<MobEffect, BathEffect> STEEL_HEART = EFFECTS.register("steel_heart", BathEffect::new);
    public static final RegistryEntrySupplier<MobEffect, StunEffect> STUNNED = EFFECTS.register("stunned", StunEffect::new);

    public static final RegistryEntrySupplier<MobEffect, TrueInvis> TRUE_INVIS = EFFECTS.register("true_invisibility", TrueInvis::new);

}
