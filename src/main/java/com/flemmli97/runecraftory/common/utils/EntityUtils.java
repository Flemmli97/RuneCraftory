package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.common.registry.ModEffects;
import com.flemmli97.tenshilib.api.entity.IOwnable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityUtils {

    public static boolean isExhaust(LivingEntity entity) {
        return entity.getActivePotionEffect(ModEffects.fatigue.get()) != null;
    }

    public static boolean paralysed(LivingEntity entity) {
        return entity.getActivePotionEffect(ModEffects.paralysis.get()) != null;
    }

    public static float playerLuck(PlayerEntity player) {
        float luckAtt = player.getLuck();

        return luckAtt;
    }

    public static boolean isDisabled(LivingEntity entity) {
        return entity.isPotionActive(ModEffects.sleep.get());
    }

    @Nullable
    public static UUID tryGetOwner(LivingEntity entity) {
        if (entity instanceof TameableEntity)
            return ((TameableEntity) entity).getOwnerId();
        if (entity instanceof IOwnable<?>)
            return ((IOwnable<?>) entity).getOwnerUUID();
        return null;
    }
}
