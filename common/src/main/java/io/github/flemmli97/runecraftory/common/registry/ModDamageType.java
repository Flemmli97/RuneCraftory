package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageType {

    public static final ResourceKey<DamageType> DYNAMIC_DAMAGE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, RuneCraftory.modRes("dynamic_damage_type"));
    public static final ResourceKey<DamageType> EXHAUST = ResourceKey.create(Registries.DAMAGE_TYPE, RuneCraftory.modRes("exhaust"));
    public static final ResourceKey<DamageType> STRONG_POISON = ResourceKey.create(Registries.DAMAGE_TYPE, RuneCraftory.modRes("strong_poison"));
}
