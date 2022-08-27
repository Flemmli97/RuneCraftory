package io.github.flemmli97.runecraftory.common.utils;

import com.google.common.collect.Lists;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class LevelCalc {

    private static long[] levelXPTotal;

    private static long[] commonSkillXP;
    private static long[] fastSkillXP;
    private static long[] mediumSkillXP;
    private static long[] slowSkillXP;

    /**
     * Experimental calculations
     */
    public static int xpAmountForLevelUp(int level) {
        if (level <= 0)
            return 1;
        if (level >= GeneralConfig.maxLevel)
            return 0;
        return (int) (totalXpForLevel(level + 1) - totalXpForLevel(level));
    }

    public static long totalXpForLevel(int level) {
        if (level <= 0)
            return 0;
        if (levelXPTotal == null || levelXPTotal.length < level) {
            int len = 100;
            if (levelXPTotal != null) {
                len = levelXPTotal.length + 50;
            }
            levelXPTotal = new long[len];
            levelXPTotal[0] = 50;
            long prev = levelXPTotal[0];
            for (int l = 1; l < len; l++) {
                levelXPTotal[l] = (long) (prev + 100 + 9 * Math.pow(l, 2.655) - 12 * Math.pow(l, 1.549) + l * 29L);
                prev = levelXPTotal[l];
            }
        }
        return levelXPTotal[level - 1];
    }

    public static int xpAmountForSkillLevelUp(EnumSkills skill, int level) {
        if (level <= 0)
            return 1;
        if (level >= GeneralConfig.maxSkillLevel)
            return 0;
        return (int) (totalSkillXpForLevel(skill, level + 1) - totalSkillXpForLevel(skill, level));
    }

    public static long totalSkillXpForLevel(EnumSkills skill, int level) {
        if (level <= 0)
            return 0;
        long[] xps = switch (skill.gainType) {
            case COMMON -> (commonSkillXP == null || commonSkillXP.length < level) ?
                    commonSkillXP = calcSkillXPs(commonSkillXP, 30, (l, prev) -> (long) (prev + 100 + 9 * Math.pow(l, 2.555) - 12 * Math.pow(l, 2.249) + l * 21))
                    : commonSkillXP;
            case SLOW -> (slowSkillXP == null || slowSkillXP.length < level) ?
                    slowSkillXP = calcSkillXPs(slowSkillXP, 10, (l, prev) -> prev + 50 + l * 35 + (int) (Math.pow(l, 2.35) * 0.1) * 10)
                    : slowSkillXP;
            case MEDIUM -> (mediumSkillXP == null || mediumSkillXP.length < level) ?
                    mediumSkillXP = calcSkillXPs(mediumSkillXP, 50, (l, prev) -> prev + 40 + l * 15 + (int) (Math.pow(l, 2) * 0.1) * 10)
                    : mediumSkillXP;
            case FAST -> (fastSkillXP == null || fastSkillXP.length < level) ?
                    fastSkillXP = calcSkillXPs(fastSkillXP, 60, (l, prev) -> prev + 40 + l * 20 + (int) (Math.pow(l, 1.85) * 0.1) * 10)
                    : fastSkillXP;
        };
        return xps[level - 1];
    }

    private static long[] calcSkillXPs(long[] current, int base, BiFunction<Integer, Long, Long> levelXP) {
        int len = 100;
        if (current != null) {
            len = current.length + 50;
        }
        long[] xps = new long[len];
        xps[0] = base;
        long prev = xps[0];
        for (int l = 1; l < len; l++) {
            xps[l] = levelXP.apply(l, prev);
            prev = xps[l];
        }
        return xps;
    }

    public static int getMoney(int base, int level) {
        return (int) (base * Math.min(1, level * 0.1));
    }

    public static void addXP(ServerPlayer player, PlayerData data, int base, int level) {
        if (GeneralConfig.xpMultiplier == 0)
            return;
        if (data.getPlayerLevel().getLevel() <= level)
            data.addXp(player, (base + base * (level - 1) * 0.5f) * GeneralConfig.xpMultiplier);
        else {
            int diff = data.getPlayerLevel().getLevel() - level * 2;
            data.addXp(player, (base + base * Math.max(0, level - diff - 1) * 0.5f) * GeneralConfig.xpMultiplier);
        }
    }

    public static int getBaseXP(EnumSkills skill) {
        return GeneralConfig.skillProps.get(skill).baseXP();
    }

    public static void levelSkill(ServerPlayer player, PlayerData data, EnumSkills skill, float multiplier) {
        if (GeneralConfig.skillXpMultiplier == 0)
            return;
        data.increaseSkill(skill, player, getBaseXP(skill) * multiplier * GeneralConfig.skillXpMultiplier);
    }

    public static int levelFromPos(ServerLevel level, Vec3 pos) {
        return switch (MobConfig.gateLevelType) {
            case CONSTANT -> MobConfig.baseGateLevel;
            case DISTANCESPAWN -> {
                double dist = Math.sqrt(pos.distanceToSqr(Vec3.atCenterOf(level.getSharedSpawnPos())));
                if (dist < 400)
                    yield MobConfig.baseGateLevel;
                if (dist < 1000)
                    yield randomizedLevel(level.random, MobConfig.baseGateLevel + (int) ((dist - 400) * 0.03));
                if (dist < 5000)
                    yield randomizedLevel(level.random, MobConfig.baseGateLevel + (int) (600 * 0.03 + (dist - 1000) * 0.05));
                yield randomizedLevel(level.random, MobConfig.baseGateLevel + (int) (4000 * 0.05 + (dist - 5000) * 0.07));
            }
            case PLAYERLEVELMAX -> {
                int diff = MobConfig.baseGateLevel;
                List<Player> list = playersIn(level, pos, 256);
                if (list.isEmpty())
                    yield MobConfig.baseGateLevel;
                for (Player player : list) {
                    int pD = Platform.INSTANCE.getPlayerData(player).map(data -> data.getPlayerLevel().getLevel()).orElse(1);
                    if (pD > diff)
                        diff = pD;
                }
                yield randomizedLevel(level.random, MobConfig.baseGateLevel + diff);
            }
            case PLAYERLEVELMEAN -> {
                int diff = 0;
                List<Player> list = playersIn(level, pos, 256);
                if (list.isEmpty())
                    yield MobConfig.baseGateLevel;
                for (Player player : list) {
                    diff += Platform.INSTANCE.getPlayerData(player).map(data -> data.getPlayerLevel().getLevel()).orElse(1);
                }
                diff = (diff / list.size());
                yield randomizedLevel(level.random, MobConfig.baseGateLevel + diff);
            }
        };
    }

    private static List<Player> playersIn(EntityGetter getter, Vec3 pos, double radius) {
        ArrayList<Player> list = Lists.newArrayList();
        for (Player player : getter.players()) {
            if (player.position().closerThan(pos, radius))
                list.add(player);
        }
        return list;
    }

    public static int randomizedLevel(Random random, int level) {
        return level + Math.round((float) ((random.nextDouble() * 2 - 1) * Math.ceil(level * 0.1)));
    }

    public static boolean useRP(Player player, PlayerData data, float amount, boolean hurt, boolean percent, boolean mean, EnumSkills... skills) {
        int skillVal = 0;
        if (skills.length == 0)
            skillVal = 1;
        else if (skills.length == 1)
            skillVal = data.getSkillLevel(skills[0]).getLevel();
        else {
            if (mean) {
                float l = skills.length;
                float sLvl = 0;
                for (EnumSkills skill : skills)
                    sLvl += data.getSkillLevel(skill).getLevel();
                skillVal = (int) (sLvl / l);
            } else {
                for (EnumSkills skill : skills) {
                    int lvl = data.getSkillLevel(skill).getLevel();
                    if (lvl > skillVal)
                        skillVal = lvl;
                }
            }
        }
        float val = amount * Math.max(1 - (skillVal - 1) * 0.0065f, 0.3f);
        int usage = Mth.ceil(percent ? data.getMaxRunePoints() * val * 0.01 : val);
        return data.decreaseRunePoints(player, usage, hurt);
    }
}
