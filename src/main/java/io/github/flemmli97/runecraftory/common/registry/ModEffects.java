package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.effects.ColdEffect;
import io.github.flemmli97.runecraftory.common.effects.ParalysisEffect;
import io.github.flemmli97.runecraftory.common.effects.PermanentEffect;
import io.github.flemmli97.runecraftory.common.effects.PoisonEffect;
import io.github.flemmli97.runecraftory.common.effects.SleepEffect;
import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEffects {

    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, RuneCraftory.MODID);

    public static final RegistryObject<Effect> sleep = EFFECTS.register("sleep", SleepEffect::new);
    public static final RegistryObject<Effect> poison = EFFECTS.register("poison", PoisonEffect::new);
    public static final RegistryObject<Effect> paralysis = EFFECTS.register("paralysis", ParalysisEffect::new);
    public static final RegistryObject<Effect> seal = EFFECTS.register("seal", () -> new PermanentEffect(EffectType.HARMFUL, 0, S2CEntityDataSync.Type.SEAL));
    public static final RegistryObject<Effect> fatigue = EFFECTS.register("fatigue", () -> new PermanentEffect(EffectType.HARMFUL, 0, S2CEntityDataSync.Type.FATIGUE));
    public static final RegistryObject<Effect> cold = EFFECTS.register("cold", ColdEffect::new);

}
