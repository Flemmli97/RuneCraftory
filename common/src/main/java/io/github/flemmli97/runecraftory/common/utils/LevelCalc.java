package io.github.flemmli97.runecraftory.common.utils;

import com.google.common.collect.Lists;
import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.config.DistanceZoningConfig;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.entities.utils.IBaseMob;
import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.ToIntBiFunction;

public class LevelCalc {

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
            else if (attacker instanceof NPCEntity npc && npc.followEntity() instanceof ServerPlayer sP)
                player = sP;
        }
        if (player != null) {
            PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
            data.addXp(adjustOnLevel ? levelXpWith(base, data.getPlayerLevel().getLevel(), level) : base);
            data.setMoney(data.getMoney() + LevelCalc.getMoney(money, level));
            if (!(attacker instanceof Player))
                tryAddXPTo(attacker, player, base, level, adjustOnLevel);
            for (Mob e : player.level().getEntities(EntityTypeTest.forClass(Mob.class), player.getBoundingBox().inflate(32, 32, 32), e -> true)) {
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
            if (entity instanceof NPCEntity npc && player.getUUID().equals(npc.getEntityToFollowUUID()))
                cons = npc::addXp;
            if (cons == null)
                return;
            cons.accept(adjustOnLevel ? levelXpWith(base, mob.xpLevel().getLevel(), level) : base);
        }
    }

    private static float levelXpWith(int base, int level, int targetLevel) {
        float xp = (base + base * (level - 1) * 0.5f) * GeneralConfig.xpMultiplier;
        if (level <= targetLevel)
            return xp;
        int diff = level - targetLevel;
        return xp * Math.max(0.01f, 1 - diff * 0.075f) * GeneralConfig.xpMultiplier;
    }

    public static void levelSkill(PlayerData data, Skills skill, float amount) {
        if (GeneralConfig.skillXpMultiplier == 0)
            return;
        data.increaseSkill(skill, skill.getProperties().xpMultiplier() * amount * GeneralConfig.skillXpMultiplier);
    }

    public static GateLevelResult levelFromPos(ServerLevel level, Vec3 pos) {
        List<ServerPlayer> nearby = playersAround(level, pos, 256);
        return new GateLevelResult(levelFromPos(level, pos, nearby), nearby);
    }

    public static int levelFromPos(ServerLevel level, Vec3 pos, List<ServerPlayer> list) {
        return Math.max(1, switch (MobConfig.gateLevelType) {
            case CONSTANT -> randomizedLevel(level.random, getLevelFor(MobConfig.baseGateLevel, list, null));
            case DISTANCESPAWN ->
                    randomizedLevel(level.random, getLevelFor(MobConfig.baseGateLevel + distanceLevelFrom(level, pos, level.getSharedSpawnPos()), list, null));
            case DISTANCESPAWNPLAYER ->
                    randomizedLevel(level.random, getLevelFor(MobConfig.baseGateLevel, list, (player, d) -> {
                        ServerPlayer serverPlayer = (ServerPlayer) player;
                        BlockPos center;
                        if (serverPlayer.getRespawnDimension() != level.dimension() || serverPlayer.getRespawnPosition() == null)
                            center = level.getSharedSpawnPos();
                        else
                            center = serverPlayer.getRespawnPosition();
                        return distanceLevelFrom(level, pos, center);
                    }));
            case PLAYERLEVEL ->
                    randomizedLevel(level.random, getLevelFor(MobConfig.baseGateLevel, list, (p, d) -> d.getPlayerLevel().getLevel()));
        });
    }

    private static int getLevelFor(int base, List<ServerPlayer> list, ToIntBiFunction<Player, PlayerData> levelFunc) {
        if (levelFunc == null && !MobConfig.playerLevelType.increased)
            return base;
        if (list.isEmpty())
            return base;
        int lvl = 0;
        boolean mean = MobConfig.playerLevelType.mean;
        for (Player player : list) {
            PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
            int pL = levelFunc != null ? levelFunc.applyAsInt(player, data) : 0;
            if (MobConfig.playerLevelType.increased)
                pL += data.getMobLevelIncrease();
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
        Pair<Float, DistanceZoningConfig.Zone> zone = MobConfig.levelZones.get((float) dist);
        return randomizedLevel(level.random, (int) (zone.getRight().start() + (dist - zone.getLeft()) * zone.getRight().increasePerBlock()));
    }

    public static List<ServerPlayer> playersAround(EntityGetter getter, Vec3 pos, double radius) {
        ArrayList<ServerPlayer> list = Lists.newArrayList();
        for (Player player : getter.players()) {
            if (!EntitySelector.NO_SPECTATORS.test(player) || !EntitySelector.LIVING_ENTITY_STILL_ALIVE.test(player))
                continue;
            if (player instanceof ServerPlayer serverPlayer && player.position().closerThan(pos, radius))
                list.add(serverPlayer);
        }
        return list;
    }

    public static int randomizedLevel(RandomSource random, int level) {
        return level + Math.round((float) ((random.nextDouble() * 2 - 1) * Math.ceil(level * 0.15)));
    }

    public static boolean useRP(PlayerData data, float amount, boolean hurt, float percent, boolean mean, Skills... skills) {
        int skillVal = 0;
        if (skills.length == 0)
            skillVal = 1;
        else if (skills.length == 1)
            skillVal = data.getSkillLevel(skills[0]).getLevel();
        else {
            if (mean) {
                float l = skills.length;
                float sLvl = 0;
                for (Skills skill : skills)
                    sLvl += data.getSkillLevel(skill).getLevel();
                skillVal = (int) (sLvl / l);
            } else {
                for (Skills skill : skills) {
                    int lvl = data.getSkillLevel(skill).getLevel();
                    if (lvl > skillVal)
                        skillVal = lvl;
                }
            }
        }
        float skillReduction = Math.max(1 - (skillVal - 1) * 0.0065f, 0.3f);
        float val = amount * skillReduction;
        float percentAmount = percent > 0 ? data.getMaxRunePoints() * percent * skillReduction : 0;
        val = Math.max(percentAmount, val);
        int usage = Mth.ceil(val);
        return data.useRunePoints(usage, hurt);
    }

    public static float getIntervalledMultiplier(int level, int interval, float max, float increase) {
        level -= interval;
        if (level <= 0 || increase == 0)
            return 0;
        int full = (level / interval);
        int rest = level % interval;
        float multiplier = 0;
        for (int i = 1; i <= full; i++) {
            multiplier += interval * Math.min(max, i * increase);
        }
        multiplier += rest * Math.min(max, (1 + full) * increase);
        return multiplier;
    }

    @Nullable
    public static Skills getSkillFromElement(ItemElement element) {
        return switch (element) {
            case WATER -> Skills.WATER;
            case EARTH -> Skills.EARTH;
            case WIND -> Skills.WIND;
            case FIRE -> Skills.FIRE;
            case LIGHT -> Skills.LIGHT;
            case DARK -> Skills.DARK;
            case LOVE -> Skills.LOVE;
            default -> null;
        };
    }

    public record GateLevelResult(int level, List<ServerPlayer> nearby) {

    }
}
