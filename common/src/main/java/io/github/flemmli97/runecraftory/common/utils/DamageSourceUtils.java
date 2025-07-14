package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryDamageType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;

public class DamageSourceUtils {

    public static DamageSource exhaust(Level level) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(RuneCraftoryDamageType.EXHAUST), null, null);
    }

    public static DamageSource poison(Level level) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(RuneCraftoryDamageType.STRONG_POISON), null, null);
    }
}
