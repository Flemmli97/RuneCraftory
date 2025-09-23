package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.effects.BathEffect;
import io.github.flemmli97.runecraftory.common.effects.ColdEffect;
import io.github.flemmli97.runecraftory.common.effects.EffectAccess;
import io.github.flemmli97.runecraftory.common.effects.ParalysisEffect;
import io.github.flemmli97.runecraftory.common.effects.PoisonEffect;
import io.github.flemmli97.runecraftory.common.effects.SleepEffect;
import io.github.flemmli97.runecraftory.common.effects.StunEffect;
import io.github.flemmli97.runecraftory.common.effects.SyncedMobEffect;
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class RuneCraftoryEffects {

    public static final LoaderRegister<MobEffect> EFFECTS = LoaderRegistryAccess.INSTANCE.of(Registries.MOB_EFFECT, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<MobEffect, BathEffect> BATH = EFFECTS.register("bath", BathEffect::new);
    public static final RegistryEntrySupplier<MobEffect, MobEffect> BLITZ = EFFECTS.register("blitz", () -> EffectAccess.of(MobEffectCategory.BENEFICIAL, 0x1348ba)
            .addAttributeModifier(RuneCraftoryAttributes.ATTACK_SPEED.asHolder(), LibConstants.BLITZ_EFFECT_MODIFIER, 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final RegistryEntrySupplier<MobEffect, ColdEffect> COLD = EFFECTS.register("cold", ColdEffect::new);
    public static final RegistryEntrySupplier<MobEffect, SyncedMobEffect> FATIGUE = EFFECTS.register("fatigue", () -> new SyncedMobEffect(MobEffectCategory.HARMFUL, 0x1241b2));
    public static final RegistryEntrySupplier<MobEffect, ParalysisEffect> PARALYSIS = EFFECTS.register("paralysis", ParalysisEffect::new);
    public static final RegistryEntrySupplier<MobEffect, PoisonEffect> POISON = EFFECTS.register("poison", PoisonEffect::new);
    public static final RegistryEntrySupplier<MobEffect, SyncedMobEffect> SEAL = EFFECTS.register("sealed", () -> new SyncedMobEffect(MobEffectCategory.HARMFUL, 0x0c0999));
    public static final RegistryEntrySupplier<MobEffect, SleepEffect> SLEEP = EFFECTS.register("sleeping", SleepEffect::new);
    public static final RegistryEntrySupplier<MobEffect, MobEffect> STEEL_HEART = EFFECTS.register("steel_heart", () -> EffectAccess.of(MobEffectCategory.BENEFICIAL, 0x727a87)
            .addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, LibConstants.STEEL_HEART_MODIFIER, 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final RegistryEntrySupplier<MobEffect, StunEffect> STUNNED = EFFECTS.register("stunned", StunEffect::new);
    public static final RegistryEntrySupplier<MobEffect, MobEffect> EARTH_ELEMENT_DEBUFF = EFFECTS.register("earth_element_debuff", () -> EffectAccess.of(MobEffectCategory.HARMFUL, 0x1348ba)
            .addAttributeModifier(Attributes.ARMOR, LibConstants.EARTH_DEBUFF_MODIFIER, -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
}
