package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.common.registry.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

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
}
