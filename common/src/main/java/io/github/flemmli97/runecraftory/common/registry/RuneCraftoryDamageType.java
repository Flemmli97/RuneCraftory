package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

import java.util.List;
import java.util.Map;

public class RuneCraftoryDamageType {

    public static final ResourceKey<DamageType> PHYSICAL = ResourceKey.create(Registries.DAMAGE_TYPE, RuneCraftory.modRes("physical"));
    public static final ResourceKey<DamageType> IGNORE_DEFENCE = ResourceKey.create(Registries.DAMAGE_TYPE, RuneCraftory.modRes("ignore_defence"));
    public static final ResourceKey<DamageType> MAGIC = ResourceKey.create(Registries.DAMAGE_TYPE, RuneCraftory.modRes("magic"));
    public static final ResourceKey<DamageType> IGNORE_MAGIC_DEFENCE = ResourceKey.create(Registries.DAMAGE_TYPE, RuneCraftory.modRes("ignore_magic_defence"));

    public static final ResourceKey<DamageType> PHYSICAL_PROJECTILE = ResourceKey.create(Registries.DAMAGE_TYPE, RuneCraftory.modRes("physical_projectile"));
    public static final ResourceKey<DamageType> PROJECTILE_IGNORE_DEFENCE = ResourceKey.create(Registries.DAMAGE_TYPE, RuneCraftory.modRes("projectile_ignore_defence"));
    public static final ResourceKey<DamageType> MAGIC_PROJECTILE = ResourceKey.create(Registries.DAMAGE_TYPE, RuneCraftory.modRes("magic_projectile"));
    public static final ResourceKey<DamageType> PROJECTILE_IGNORE_MAGIC_DEFENCE = ResourceKey.create(Registries.DAMAGE_TYPE, RuneCraftory.modRes("projectile_ignore_magic_defence"));

    public static final ResourceKey<DamageType> TRUE_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, RuneCraftory.modRes("true_damage"));

    public static final List<ResourceKey<DamageType>> ATTACK_TYPES = List.of(PHYSICAL, IGNORE_DEFENCE, MAGIC, IGNORE_MAGIC_DEFENCE,
            PHYSICAL_PROJECTILE, PROJECTILE_IGNORE_DEFENCE, MAGIC_PROJECTILE, PROJECTILE_IGNORE_MAGIC_DEFENCE, TRUE_DAMAGE);
    public static final Map<ResourceKey<DamageType>, ResourceKey<DamageType>> PROJECTILE_EQUIVALENT = Map.of(
            PHYSICAL, PHYSICAL_PROJECTILE,
            IGNORE_DEFENCE, PROJECTILE_IGNORE_DEFENCE,
            MAGIC, MAGIC_PROJECTILE,
            IGNORE_MAGIC_DEFENCE, PROJECTILE_IGNORE_MAGIC_DEFENCE
    );

    public static final ResourceKey<DamageType> EXHAUST = ResourceKey.create(Registries.DAMAGE_TYPE, RuneCraftory.modRes("exhaust"));
    public static final ResourceKey<DamageType> STRONG_POISON = ResourceKey.create(Registries.DAMAGE_TYPE, RuneCraftory.modRes("strong_poison"));
}
