package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.attachment.PlayerData;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class LevelCalc {

    private static int[] levelXPTotal;
    private static int[] skillXP;

    /**
     * Experimental calculations
     */
    public static int xpAmountForLevelUp(int level) {
        if (level >= GeneralConfig.maxLevel)
            return 0;
        return totalXpForLevel(level + 1) - totalXpForLevel(level);
    }

    public static int totalXpForLevel(int level) {
        if (level <= 0)
            return 0;
        if (levelXPTotal == null || levelXPTotal.length < level) {
            int len = 100;
            if (levelXPTotal != null) {
                len = levelXPTotal.length + 50;
            }
            levelXPTotal = new int[len];
            levelXPTotal[0] = 100;
            int prev = 0;
            for (int l = 0; l < len; l++) {
                levelXPTotal[l] = prev + 90 + 55 * (int) (Math.pow(1.1, l * 0.8));
                prev = levelXPTotal[l];
            }
        }
        return levelXPTotal[level - 1];
    }

    public static int xpAmountForSkills(int level) {
        if (level >= GeneralConfig.maxSkillLevel)
            return 0;
        return totalSkillXpForLevel(level + 1) - totalSkillXpForLevel(level);
    }

    public static int totalSkillXpForLevel(int level) {
        if (level <= 0)
            return 0;
        if (levelXPTotal == null || levelXPTotal.length < level) {
            int len = 100;
            if (levelXPTotal != null) {
                len = levelXPTotal.length + 50;
            }
            levelXPTotal = new int[len];
            levelXPTotal[0] = 100;
            int prev = 0;
            for (int l = 0; l < len; l++) {
                levelXPTotal[l] = prev + 100 + 50 * (int) (Math.pow(1.075, l * 0.7));
                prev = levelXPTotal[l];
            }
        }
        return levelXPTotal[level - 1];
    }

    public static int getMobXP(PlayerData data, BaseMonster monster) {
        return (int) (Math.min(monster.level() / (float) data.getPlayerLevel()[0], 1) * monster.baseXP());
    }

    public static int gateXP(PlayerData data, GateEntity gate) {
        return (int) (Math.min(gate.level() / (float) data.getPlayerLevel()[0], 1) * MobConfig.gateXP);
    }

    public static int getMoney(int base, int level) {
        return (int) (base * Math.min(1, level * 0.5));
    }

    public static void addXP(ServerPlayer player, PlayerData data, int amount) {
        data.addXp(player, (int) (amount * GeneralConfig.xpMultiplier));
    }

    public static int getBaseXP(EnumSkills skill) {
        return GeneralConfig.skillProps.get(skill).baseXP();
    }

    public static void levelSkill(ServerPlayer player, PlayerData data, EnumSkills skill, float multiplier) {
        if (GeneralConfig.skillXpMultiplier == 0)
            return;
        data.increaseSkill(skill, player, (int) (getBaseXP(skill) * multiplier * GeneralConfig.skillXpMultiplier));
    }

    public static int levelFromPos(Level world, BlockPos pos) {
        return 1;
    }

    public static boolean useRP(Player player, PlayerData data, int amount, boolean hurt, boolean percentReduction, boolean mean, float modifier, EnumSkills... skills) {
        int skillVal = 0;
        if (mean) {
            float l = skills.length;
            int sLvl = 0;
            for (EnumSkills skill : skills)
                sLvl += data.getSkillLevel(skill)[0];
            skillVal = (int) (sLvl / l);
        } else {
            for (EnumSkills skill : skills) {
                int lvl = data.getSkillLevel(skill)[0];
                if (lvl > skillVal)
                    skillVal = lvl;
            }
        }
        int val = amount;
        if (percentReduction)
            val *= 1 - Math.min(0.9, Math.log(skillVal) * 0.2);
        else
            val -= (int) (Math.log(skillVal) * modifier);
        return data.decreaseRunePoints(player, Math.max(1, val), hurt);
    }
}
