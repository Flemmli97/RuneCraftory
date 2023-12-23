package io.github.flemmli97.runecraftory.common.utils;

import com.google.common.collect.Lists;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.IBaseMob;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.ToIntBiFunction;

public class LevelCalc {

    private static final int MAX_HEALTH_MULTIPLIER = 20;
    private static final int HEALTH_MULTIPLIER_INTERVAL = 10;

    private static long[] LEVEL_XP_TOTAL;

    private static long[] COMMON_SKILL_XP;
    private static long[] SLOW_SKILL_XP;
    private static long[] FAST_SKILL_XP;
    private static long[] VERY_FAST_SKILL_XP;
    private static long[] CRAFTING_SKILL_XP;

    private static long[] FRIEND_XP_TOTAL;

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
        if (LEVEL_XP_TOTAL == null || LEVEL_XP_TOTAL.length < level) {
            int len = 100;
            if (LEVEL_XP_TOTAL != null) {
                len = level + 50;
            }
            LEVEL_XP_TOTAL = new long[len];
            LEVEL_XP_TOTAL[0] = 50;
            long prev = LEVEL_XP_TOTAL[0];
            for (int l = 1; l < len; l++) {
                LEVEL_XP_TOTAL[l] = (long) (prev + 20 + l * 30L + 15 * Math.pow(l, 1.25) + (l / 10) * 250L + (l / 20) * (l / 20) * 1000L);
                //Old calc. here for now
                //levelXPTotal[l] = (long) (prev + 170 + 9 * Math.pow(l, 2.455) - 12 * Math.pow(l, 1.549) + (l - 1) * 125L);
                prev = LEVEL_XP_TOTAL[l];
            }
        }
        return LEVEL_XP_TOTAL[level - 1];
    }

    public static int xpAmountForSkillLevelUp(EnumSkills skill, int level) {
        if (level <= 0)
            return 1;
        if (level >= DataPackHandler.INSTANCE.skillPropertiesManager().getPropertiesFor(skill).maxLevel())
            return 0;
        return (int) (totalSkillXpForLevel(skill, level + 1) - totalSkillXpForLevel(skill, level));
    }

    public static long totalSkillXpForLevel(EnumSkills skill, int level) {
        if (level <= 0)
            return 0;
        long[] xps = switch (skill.gainType) {
            case COMMON -> (COMMON_SKILL_XP == null || COMMON_SKILL_XP.length < level) ?
                    COMMON_SKILL_XP = calcSkillXPs(COMMON_SKILL_XP, level, (l, prev) -> (long) (prev + 35 + 9 * Math.pow(l, 2.555) - 12 * Math.pow(l, 2.249) + l * 21L))
                    : COMMON_SKILL_XP;
            case SLOW -> (SLOW_SKILL_XP == null || SLOW_SKILL_XP.length < level) ?
                    SLOW_SKILL_XP = calcSkillXPs(SLOW_SKILL_XP, level, (l, prev) -> prev + 25 + (l - 1L) * 15 + (l / 10) * 100L
                            + (long) (Math.pow(l, 1.2) * 3 + Math.pow(l / 10, 2) * 50 + Math.pow(Math.max(0, l - 50) / 10, 1.235) * 500))
                    : SLOW_SKILL_XP;
            case FAST -> (FAST_SKILL_XP == null || FAST_SKILL_XP.length < level) ?
                    FAST_SKILL_XP = calcSkillXPs(FAST_SKILL_XP, level, (l, prev) -> prev + 40 + l * 30 + (int) (Math.pow(l, 1.75) * 0.125) * 10)
                    : FAST_SKILL_XP;
            case VERY_FAST -> (VERY_FAST_SKILL_XP == null || VERY_FAST_SKILL_XP.length < level) ?
                    VERY_FAST_SKILL_XP = calcSkillXPs(VERY_FAST_SKILL_XP, level, (l, prev) -> prev + 50 + (l - 1L) * 30)
                    : VERY_FAST_SKILL_XP;
            case CRAFTING -> (CRAFTING_SKILL_XP == null || CRAFTING_SKILL_XP.length < level) ?
                    CRAFTING_SKILL_XP = calcSkillXPs(CRAFTING_SKILL_XP, level, (l, prev) -> prev + 50 + (l - 1L) * 15 + (l / 10) * 25L + (l % 10 == 0 ? (l / 10) * 35L : 0))
                    : CRAFTING_SKILL_XP;
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
        if (FRIEND_XP_TOTAL == null) {
            FRIEND_XP_TOTAL = new long[10];
            FRIEND_XP_TOTAL[0] = 30;
            long prev = FRIEND_XP_TOTAL[0];
            for (int l = 1; l < 10; l++) {
                FRIEND_XP_TOTAL[l] = prev + 45 + l * 5 + l * l * 10;
                prev = FRIEND_XP_TOTAL[l];
            }
        }
        return FRIEND_XP_TOTAL[level - 1];
    }

    public static int getMoney(int base, int level) {
        return base;
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
        return DataPackHandler.INSTANCE.skillPropertiesManager().getPropertiesFor(skill).xpMultiplier();
    }

    public static void levelSkill(ServerPlayer player, PlayerData data, EnumSkills skill, float amount) {
        if (GeneralConfig.skillXpMultiplier == 0)
            return;
        data.increaseSkill(skill, player, getSkillXpMultiplier(skill) * amount * GeneralConfig.skillXpMultiplier);
    }

    public static int levelFromPos(ServerLevel level, Vec3 pos) {
        return Math.max(1, switch (MobConfig.gateLevelType) {
            case CONSTANT -> randomizedLevel(level.random, getLevelFor(level, pos, MobConfig.baseGateLevel, null));
            case DISTANCESPAWN -> randomizedLevel(level.random, getLevelFor(level, pos, MobConfig.baseGateLevel + distanceLevelFrom(level, pos, level.getSharedSpawnPos()), null));
            case DISTANCESPAWNPLAYER -> randomizedLevel(level.random, getLevelFor(level, pos, MobConfig.baseGateLevel, (player, d) -> {
                ServerPlayer serverPlayer = (ServerPlayer) player;
                BlockPos center;
                if (serverPlayer.getRespawnDimension() != level.dimension() || serverPlayer.getRespawnPosition() == null)
                    center = level.getSharedSpawnPos();
                else
                    center = serverPlayer.getRespawnPosition();
                return distanceLevelFrom(level, pos, center);
            }));
            case PLAYERLEVEL -> randomizedLevel(level.random, getLevelFor(level, pos, MobConfig.baseGateLevel, (p, d) -> d.map(data -> data.getPlayerLevel().getLevel()).orElse(1)));
        });
    }

    private static int getLevelFor(ServerLevel level, Vec3 pos, int base, ToIntBiFunction<Player, Optional<PlayerData>> levelFunc) {
        if (levelFunc == null && !MobConfig.playerLevelType.increased)
            return base;
        List<Player> list = playersIn(level, pos, 256);
        if (list.isEmpty())
            return base;
        int lvl = 0;
        boolean mean = MobConfig.playerLevelType.mean;
        for (Player player : list) {
            Optional<PlayerData> data = Platform.INSTANCE.getPlayerData(player);
            int pL = levelFunc != null ? levelFunc.applyAsInt(player, data) : 0;
            if (MobConfig.playerLevelType.increased)
                pL += data.map(PlayerData::getMobLevelIncrease).orElse(0);
            if (mean)
                lvl += pL;
            else if (pL > lvl)
                lvl = pL;
        }
        lvl = mean ? (lvl / list.size()) : lvl;
        return base + lvl;
    }

    private static int distanceLevelFrom(Level level, Vec3 pos, BlockPos center) {
        Vec3 spawn = Vec3.atCenterOf(center);
        double dX = spawn.x - pos.x;
        double dZ = spawn.z - pos.z;
        double dist = Math.sqrt(dX * dX + dZ * dZ);
        if (dist < 300)
            return MobConfig.baseGateLevel;
        if (dist < 2000)
            return randomizedLevel(level.random, MobConfig.baseGateLevel + uniformInterpolation(0, 25, 2000 - 300, dist));
        if (dist < 7500)
            return randomizedLevel(level.random, MobConfig.baseGateLevel + uniformInterpolation(25, 80, 7500 - 2000, dist));
        return randomizedLevel(level.random, MobConfig.baseGateLevel + (int) (80 + (dist - 7500) * 0.07));
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

    public static float getHealthIncreaseFor(float base, int level) {
        int healthMultiplierLvl = Math.min(MAX_HEALTH_MULTIPLIER, 1 + (level / HEALTH_MULTIPLIER_INTERVAL));
        return healthMultiplierLvl * base;
    }

    public static float getHealthTotalFor(float base, int level) {
        if (level < HEALTH_MULTIPLIER_INTERVAL)
            return (level - 1) * base;
        int mod = level % HEALTH_MULTIPLIER_INTERVAL;
        int completed = level / HEALTH_MULTIPLIER_INTERVAL;
        int multiplier = HEALTH_MULTIPLIER_INTERVAL - 2;
        for (int i = 1; i < completed; i++) {
            multiplier += HEALTH_MULTIPLIER_INTERVAL * Math.min(MAX_HEALTH_MULTIPLIER, i + 1);
        }
        multiplier += (mod + 1) * Math.min(MAX_HEALTH_MULTIPLIER, (completed + 1));
        return base * multiplier;
    }

    @Nullable
    public static EnumSkills getSkillFromElement(EnumElement element) {
        return switch (element) {
            case WATER -> EnumSkills.WATER;
            case EARTH -> EnumSkills.EARTH;
            case WIND -> EnumSkills.WIND;
            case FIRE -> EnumSkills.FIRE;
            case LIGHT -> EnumSkills.LIGHT;
            case DARK -> EnumSkills.DARK;
            case LOVE -> EnumSkills.LOVE;
            default -> null;
        };
    }
}
