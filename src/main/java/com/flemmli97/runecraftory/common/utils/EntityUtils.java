package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.common.registry.ModPotions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class EntityUtils {

    public static boolean isExhaust(LivingEntity entity) {
        return entity.getActivePotionEffect(ModPotions.fatigue.get()) != null;
    }

    public static boolean paralysed(LivingEntity entity) {
        return entity.getActivePotionEffect(ModPotions.paralysis.get()) != null;
    }

    public static float playerLuck(PlayerEntity player) {
        float luckAtt = player.getLuck();

        return luckAtt;
    }
}
