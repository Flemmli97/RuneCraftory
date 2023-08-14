package io.github.flemmli97.runecraftory.common.config;

public class MobConfig {

    public static boolean disableNaturalSpawn = false;
    public static int farmRadius = 3;
    public static boolean mobAttackNPC;
    public static boolean vanillaGiveXp;
    public static boolean monsterNeedBarn = true;

    public static int bellRadius = 48;
    public static double gateHealth = 75;
    public static double gateDef = 0;
    public static double gateMDef = 0;
    public static double gateHealthGain = 25;
    public static double gateDefGain = 1.5;
    public static double gateMDefGain = 1.5;
    public static int gateXP = 25;
    public static int gateMoney = 5;
    public static int spawnChance = 100;
    public static double minDist = 32;
    public static int maxGroup = 1;
    public static int minNearby = 2;
    public static int maxNearby = 5;
    public static int baseGateLevel = 1;
    public static GateLevelType gateLevelType = GateLevelType.DISTANCESPAWN;
    public static float treasureChance = 0.001f;
    public static float mimicChance = 0.4f;
    public static float mimicStrongChance = 0.3f;

    public static double npcHealth = 20;
    public static double npcAttack = 1;
    public static double npcDefence = 0;
    public static double npcMagicAttack = 5;
    public static double npcMagicDefence = 0;
    public static double npcHealthGain = 5;
    public static double npcAttackGain = 2;
    public static double npcDefenceGain = 2;
    public static double npcMagicAttackGain = 2;
    public static double npcMagicDefenceGain = 2;

    public enum GateLevelType {
        CONSTANT,
        DISTANCESPAWN,
        DISTANCESPAWNPLAYER,
        PLAYERLEVELMAX,
        PLAYERLEVELMEAN
    }
}
