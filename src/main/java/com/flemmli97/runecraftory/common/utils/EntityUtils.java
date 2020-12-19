package com.flemmli97.runecraftory.common.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class EntityUtils {

    public static boolean isExhaust(LivingEntity entity) {
        return false;
    }

    public static boolean paralysed(LivingEntity entity) {
        return false;
    }

    public static float playerLuck(PlayerEntity player) {
        float luckAtt = player.getLuck();

        return luckAtt;
    }
}
