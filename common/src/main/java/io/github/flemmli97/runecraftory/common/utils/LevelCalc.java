package io.github.flemmli97.runecraftory.common.utils;

import com.google.common.collect.Lists;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.IBaseMob;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class LevelCalc {

    private static long[] levelXPTotal;

    private static long[] commonSkillXP;
    private static long[] fastSkillXP;
    private static long[] mediumSkillXP;
    private static long[] slowSkillXP;

    private static long[] friendXPTotal;

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
                len = level + 50;
            }
            levelXPTotal = new long[len];
            levelXPTotal[0] = 50;
            long prev = levelXPTotal[0];
            for (int l = 1; l < len; l++) {
                levelXPTotal[l] = (long) (prev + 170 + 9 * Math.pow(l, 2.455) - 12 * Math.pow(l, 1.549) + (l - 1) * 125L);
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
                    commonSkillXP = calcSkillXPs(commonSkillXP, level, (l, prev) -> (long) (prev + 35 + 9 * Math.pow(l, 2.555) - 12 * Math.pow(l, 2.249) + l * 21))
                    : commonSkillXP;
            case SLOW -> (slowSkillXP == null || slowSkillXP.length < level) ?
                    slowSkillXP = calcSkillXPs(slowSkillXP, level, (l, prev) -> prev + 25 + (l - 1L) * 35 + (int) (Math.pow(l, 2.3) * 0.1) * 10)
                    : slowSkillXP;
            case VERYFAST -> (mediumSkillXP == null || mediumSkillXP.length < level) ?
                    mediumSkillXP = calcSkillXPs(mediumSkillXP, level, (l, prev) -> prev + 50 + (l - 1L) * 30)
                    : mediumSkillXP;
            case FAST -> (fastSkillXP == null || fastSkillXP.length < level) ?
                    fastSkillXP = calcSkillXPs(fastSkillXP, level, (l, prev) -> prev + 40 + l * 30 + (int) (Math.pow(l, 1.75) * 0.125) * 10)
                    : fastSkillXP;
        };
        return xps[level - 1];
    }

    private static long[] calcSkillXPs(long[] current, int level, BiFunction<Integer, Long, Long> levelXP) {
        int len = 100;
        if (current != null) {
            len = level + 50;
        }
        long[] xps = new long[len];
        xps[0] = 0;
        long prev = xps[0];
        for (int l = 1; l < len; l++) {
            xps[l] = levelXP.apply(l, prev);
            prev = xps[l];
        }
        return xps;
    }

    public static int friendPointsForNext(int level) {
        if (level <= 0)
            return 1;
        if (level >= 10)
            return 0;
        return (int) (totalFriendPointsForLevel(level) - totalFriendPointsForLevel(level - 1));
    }

    public static long totalFriendPointsForLevel(int level) {
        if (level <= 0 || level >= 10)
            return 0;
        if (friendXPTotal == null) {
            friendXPTotal = new long[10];
            friendXPTotal[0] = 30;
            long prev = friendXPTotal[0];
            for (int l = 1; l < 10; l++) {
                friendXPTotal[l] = prev + 45 + l * 5 + l * l * 10;
                prev = friendXPTotal[l];
            }
        }
        return friendXPTotal[level - 1];
    }

    public static int getMoney(int base, int level) {
        return (int) (base * Math.min(1, 1 + level * 0.1));
    }

    public static void addXP(LivingEntity attacker, int base, int money, int level) {
        addXP(attacker, base, money, level, true);
    }

    public static void addXP(LivingEntity attacker, int base, int money, int level, boolean adjustOnLevel) {
        if (GeneralConfig.xpMultiplier == 0)
            return;
        ServerPlayer player = null;
        if (attacker instanceof ServerPlayer sP)
            player = sP;
        else {
            if (attacker instanceof OwnableEntity ownable && ownable.getOwner() instanceof ServerPlayer sP)
                player = sP;
            else if (attacker instanceof EntityNPCBase npc && npc.followEntity() instanceof ServerPlayer sP)
                player = sP;
        }
        if (player != null) {
            ServerPlayer finalPlayer = player;
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                data.addXp(finalPlayer, adjustOnLevel ? levelXpWith(base, data.getPlayerLevel().getLevel(), level) : base);
                data.setMoney(finalPlayer, data.getMoney() + LevelCalc.getMoney(money, level));
            });
            if (!(attacker instanceof Player))
                tryAddXPTo(attacker, player, base, level, adjustOnLevel);
            for (Mob e : player.level.getEntities(EntityTypeTest.forClass(Mob.class), player.getBoundingBox().inflate(32, 32, 32), e -> true)) {
                if (e == attacker)
                    continue;
                tryAddXPTo(e, player, base, level, adjustOnLevel);
            }
        }
    }

    private static void tryAddXPTo(LivingEntity entity, ServerPlayer player, int base, int level, boolean adjustOnLevel) {
        if (entity instanceof IBaseMob mob) {
            Consumer<Float> cons = null;
            if (entity instanceof BaseMonster monster && player.getUUID().equals(monster.getOwnerUUID()) && monster.behaviourState() == BaseMonster.Behaviour.FOLLOW)
                cons = monster::addXp;
            if (entity instanceof EntityNPCBase npc && player.getUUID().equals(npc.getEntityToFollowUUID()))
                cons = npc::addXp;
            if (cons == null)
                return;
            cons.accept(adjustOnLevel ? levelXpWith(base, mob.level().getLevel(), level) : base);
        }
    }

    private static float levelXpWith(int base, int level, int targetLevel) {
        float xp = (base + base * (level - 1) * 0.5f) * GeneralConfig.xpMultiplier;
        if (level <= targetLevel)
            return xp;
        int diff = level - targetLevel;
        return xp * Math.max(0.01f, 1 - diff * 0.075f) * GeneralConfig.xpMultiplier;
    }

    public static float getSkillXpMultiplier(EnumSkills skill) {
        return GeneralConfig.skillProps.get(skill).xpMultiplier();
    }

    public static void levelSkill(ServerPlayer player, PlayerData data, EnumSkills skill, float amount) {
        if (GeneralConfig.skillXpMultiplier == 0)
            return;
        data.increaseSkill(skill, player, getSkillXpMultiplier(skill) * amount * GeneralConfig.skillXpMultiplier);
    }

    public static int levelFromPos(ServerLevel level, Vec3 pos) {
        return switch (MobConfig.gateLevelType) {
            case CONSTANT -> MobConfig.baseGateLevel;
            case DISTANCESPAWN -> {
                Vec3 spawn = Vec3.atCenterOf(level.getSharedSpawnPos());
                double dX = spawn.x - pos.x;
                double dZ = spawn.z - pos.z;
                double dist = Math.sqrt(dX * dX + dZ * dZ);
                if (dist < 400)
                    yield MobConfig.baseGateLevel;
                if (dist < 1000)
                    yield randomizedLevel(level.random, MobConfig.baseGateLevel + uniformInterpolation(0, 20, 1000 - 400, dist));
                if (dist < 5000)
                    yield randomizedLevel(level.random, MobConfig.baseGateLevel + uniformInterpolation(20, 80, 5000 - 1000, dist));
                yield randomizedLevel(level.random, MobConfig.baseGateLevel + (int) (100 + (dist - 5000) * 0.07));
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

    private static int uniformInterpolation(int start, int increase, int len, double x) {
        return (int) (start + increase / (float) len * x);
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
