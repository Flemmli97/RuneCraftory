package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.common.capability.IPlayerCap;
import com.flemmli97.runecraftory.common.config.GeneralConfig;
import com.flemmli97.runecraftory.common.entities.BaseMonster;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LevelCalc {

    private static int[] levelXP;
    private static int[] skillXP;

    /**
     * Experimental calculations
     */
    public static int xpAmountForLevelUp(int level) {
        if (levelXP == null) {
            levelXP = new int[100];
            int[] xpToNext = new int[100];
            for (int x = 0; x < 100; x++) {
                xpToNext[x] = (int) ((x + x * x + 17) * 2.5); //(int) ((x + x*9 + 21) * 2.5);
            }
            for (int x = 0; x < 100; x++) {
                int total = 0;
                for (int t = 0; t <= x; t++)
                    total += xpToNext[t];
                levelXP[x] = total;
            }
        }
        if (level >= 0 && level + 1 < levelXP.length)
            return levelXP[level + 1] - levelXP[level];
        return 0;
    }

    public static int xpAmountForSkills(int level) {
        if (skillXP == null) {
            skillXP = new int[100];
            int[] xpToNext = new int[100];
            xpToNext[0] = 23;
            for (int x = 1; x < 100; x++) {
                xpToNext[x] = xpToNext[x - 1] + x * 5 + (int) Math.sqrt(x * 10) + 15;
            }
            for (int x = 0; x < 100; x++) {
                int total = 0;
                for (int t = 0; t <= x; t++)
                    total += xpToNext[t];
                skillXP[x] = total;
            }
        }
        if (level >= 0 && level + 1 < skillXP.length)
            return skillXP[level + 1] - skillXP[level];
        return 0;
    }

    public static int getMobXP(IPlayerCap cap, BaseMonster monster) {
        return (int) (Math.min(monster.level() / (float) cap.getPlayerLevel()[0], 1) * monster.baseXP());
    }

    public static int getMoney(int base, int level) {
        return base * level;
    }

    public static void addXP(ServerPlayerEntity player, IPlayerCap cap, int amount) {
        cap.addXp(player, (int) (amount * GeneralConfig.xpMultiplier));
    }

    public static int getBaseXP(EnumSkills skill) {
        return GeneralConfig.skillProps.get(skill).getBaseXPGain();
    }

    public static void levelSkill(ServerPlayerEntity player, IPlayerCap cap, EnumSkills skill, float multiplier) {
        cap.increaseSkill(skill, player, (int) (getBaseXP(skill) * multiplier * GeneralConfig.skillXpMultiplier));
    }

    public static int levelFromPos(World world, BlockPos pos) {
        return 1;
    }
}
