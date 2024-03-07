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
    public static int minSpawnDelay = 200;
    public static int maxSpawnDelay = 600;
    public static double minDist = 32;
    public static int maxGroup = 1;
    public static int minNearby = 2;
    public static int maxNearby = 5;
    public static int baseGateLevel = 1;
    public static GateLevelType gateLevelType = GateLevelType.DISTANCESPAWNPLAYER;
    public static PlayerLevelType playerLevelType = PlayerLevelType.MEAN;
    public static float treasureChance = 0.003f;
    public static float mimicChance = 0.4f;
    public static float mimicStrongChance = 0.3f;

    // Just here for record. Change it via datapack
    public static final double npcHealth = 20;
    public static final double npcAttack = 1;
    public static final double npcDefence = 0;
    public static final double npcMagicAttack = 5;
    public static final double npcMagicDefence = 0;
    public static final double npcHealthGain = 5;
    public static final double npcAttackGain = 2;
    public static final double npcDefenceGain = 2;
    public static final double npcMagicAttackGain = 2;
    public static final double npcMagicDefenceGain = 2;

    public static int npcSpawnRateMin = 3600;
    public static int npcSpawnRateMax = 7200;


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
