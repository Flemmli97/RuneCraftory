package com.flemmli97.runecraftory.common.registry;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.effects.ColdEffect;
import com.flemmli97.runecraftory.common.effects.ParalysisEffect;
import com.flemmli97.runecraftory.common.effects.PermanentEffect;
import com.flemmli97.runecraftory.common.effects.PoisonEffect;
import com.flemmli97.runecraftory.common.effects.SleepEffect;
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
    public static final RegistryObject<Effect> seal = EFFECTS.register("seal", () -> new PermanentEffect(EffectType.HARMFUL, 0));
    public static final RegistryObject<Effect> fatigue = EFFECTS.register("fatigue", () -> new PermanentEffect(EffectType.HARMFUL, 0));
    public static final RegistryObject<Effect> cold = EFFECTS.register("cold", ColdEffect::new);

}
