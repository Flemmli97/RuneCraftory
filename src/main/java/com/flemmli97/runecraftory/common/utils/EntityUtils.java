package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.common.init.PotionRegistry;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityUtils
{
    public static boolean isSealed(EntityLivingBase entity) {
        return entity.isPotionActive(PotionRegistry.seal);
    }
    
    public static boolean isExhaut(EntityLivingBase entity) {
        return entity.isPotionActive(PotionRegistry.fatigue);
    }
    
    public static boolean isEntityDisabled(EntityLivingBase entity) {
        boolean playerAndCreative = false;
        if (entity instanceof EntityPlayer) {
            playerAndCreative = ((EntityPlayer)entity).capabilities.disableDamage;
        }
        return !playerAndCreative && entity.isPotionActive(PotionRegistry.sleep);
    }
}
