package io.github.flemmli97.runecraftory.common.config;

public class MobConfig {

    public static boolean disableNaturalSpawn = false;
    public static int farmRadius = 3;
    public static boolean mobAttackNpc;
    public static boolean vanillaGiveXp;
    public static boolean monsterNeedBarn = true;

    public static int bellRadius = 48;
    public static double gateHealth = 75;
    public static double gateDef = 0;
    public static double gateMDef = 0;
    public static double gateHealthGain = 10;
    public static double gateDefGain = 1.5;
    public static double gateMDefGain = 1.5;
    public static int gateXp = 25;
    public static int gateMoney = 5;
    public static int minSpawnDelay = 200;
    public static int maxSpawnDelay = 600;
    public static double minDist = 32;
    public static int maxGroup = 1;
    public static int minNearby = 2;
    public static int maxNearby = 5;
    public static int baseGateLevel = 1;
    public static GateLevelType gateLevelType = GateLevelType.CONSTANT;
    public static DistanceZoningConfig levelZones = new DistanceZoningConfig();
    public static PlayerLevelType playerLevelType = PlayerLevelType.MEANINCREASED;
    public static float treasureChance = 0.003f;
    public static float mimicChance = 0.4f;
    public static float mimicStrongChance = 0.3f;

    // Just here for record. Change it via datapack
    public static final double NPC_HEALTH = 20;
    public static final double NPC_ATTACK = 1;
    public static final double NPC_DEFENCE = 0;
    public static final double NPC_MAGIC_ATTACK = 5;
    public static final double NPC_MAGIC_DEFENCE = 0;
    public static final double NPC_HEALTH_GAIN = 5;
    public static final double NPC_ATTACK_GAIN = 2;
    public static final double NPC_DEFENCE_GAIN = 2;
    public static final double NPC_MAGIC_ATTACK_GAIN = 2;
    public static final double NPC_MAGIC_DEFENCE_GAIN = 2;

    public static int npcSpawnRateMin = 3600;
    public static int npcSpawnRateMax = 7200;
    public static int initialProcreationCooldown = 24000;
    public static int procreationCooldown = 24000;


    public enum GateLevelType {
        CONSTANT,
        DISTANCESPAWN,
        DISTANCESPAWNPLAYER,
        PLAYERLEVEL,

    }

    public enum PlayerLevelType {
        MEAN(true, false),
        MEANINCREASED(true, true),
        MAX(false, false),
        MAXINCREASED(false, true);

        public final boolean mean, increased;

        PlayerLevelType(boolean mean, boolean increased) {
            this.mean = mean;
            this.increased = increased;
        }
    }
}
