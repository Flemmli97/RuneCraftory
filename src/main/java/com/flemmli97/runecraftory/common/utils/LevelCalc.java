package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.api.items.ItemStatAttributes;
import com.flemmli97.runecraftory.common.lib.LibConstants;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LevelCalc
{
    public static int xpAmountForLevelUp(int level) {
        return level ^ 2 + level + 50;
    }
    
    public static int xpAmountForSkills(int level) {
        return level * 10 + 15;
    }
    
    public static float tamingMultiplerOnLevel(int levelDiff) {
        if (levelDiff <= 0) {
            levelDiff = 1;
        }
        float f = (float)(1.5 / (Math.sqrt(levelDiff) * levelDiff));
        return Math.min(f, 1.0f);
    }
    
    public static int xpAmountForLevelUpMobs(int level) {
        return level ^ 2 + level * 2 + 50;
    }
    
    public static float scaleForVanilla(float damage) {
        if (damage <= 100.0f) {
            damage = damage * 0.03f + 2.0f;
        }
        else if (damage <= 1000.0f) {
            damage = damage * 0.012f + 5.0f;
        }
        else if (damage <= 10000.0f) {
            damage = (float)(Math.sqrt((damage - 1000.0f) / 10.0f) + 17.0);
        }
        else {
            damage = (float)(Math.sqrt((damage - 10000.0f) / 29.751736f) + 47.0);
        }
        return damage;
    }
    
    public static int xpFromLevel(int base, int level) {
        return (int)(base + base / 10.0f * level);
    }
    
    public static int moneyFromLevel(int base, int level) {
        return Math.round(base + base * level / 100.0f);
    }
    
    public static int levelFromDistSpawn(World world, BlockPos currentPos) {
        int level = LibConstants.baseLevel;
        BlockPos spawn = world.getSpawnPoint();
        double dis = 1.0;
        if (world.provider.getDimension() == 0) {
            dis = Math.sqrt(spawn.distanceSq((double)currentPos.getX(), (double)spawn.getY(), (double)currentPos.getZ()));
        }
        else {
            dis = Math.sqrt(spawn.distanceSq((double)currentPos.getX(), (double)spawn.getY(), (double)currentPos.getZ())) + 200.0;
            dis *= 1.2;
        }
        return Math.round((float)(level + Math.max(0.0, (dis - 250.0) * 0.10000000149011612)));
    }
    
    public static double initStatIncreaseLevel(double baseValue, int level, boolean isBoss, boolean isHealth, float modifier) {
        float scale = isBoss ? 1.2f : 1.0f;
        double newValue = baseValue + baseValue / 10.0 * Math.max(0, level - LibConstants.baseLevel) * scale * modifier;
        if (isHealth) {
            newValue *= LibConstants.DAMAGESCALE;
        }
        return newValue;
    }
    
    public static double onEntityLevelUp(double baseValue, double currentValue, boolean isBoss, boolean isHealth, float modifier) {
        float scale = isBoss ? 1.2f : 1.0f;
        if (isHealth) {
            baseValue *= LibConstants.DAMAGESCALE;
        }
        return currentValue + baseValue / 10.0 * scale * modifier;
    }
    
    public static boolean shouldStatIncreaseWithLevel(IAttribute att) {
        return att == SharedMonsterAttributes.MAX_HEALTH || att == ItemStatAttributes.RFATTACK || att == ItemStatAttributes.RFDEFENCE 
        		|| att == ItemStatAttributes.RFMAGICATT || att == ItemStatAttributes.RFMAGICDEF;
    }
}
