package io.github.flemmli97.runecraftory.common.config;

public class MobConfig {

    public static boolean DISABLE_NATURAL_SPAWN = false;
    public static int FARM_RADIUS = 3;
    public static boolean MOB_ATTACK_NPC;
    public static boolean VANILLA_GIVE_XP;
    public static boolean MONSTER_NEED_BARN = true;

    public static int BELL_RADIUS = 48;
    public static double GATE_HEALTH = 75;
    public static double GATE_DEF = 0;
    public static double GATE_M_DEF = 0;
    public static double GATE_HEALTH_GAIN = 25;
    public static double GATE_DEF_GAIN = 1.5;
    public static double GATE_M_DEF_GAIN = 1.5;
    public static int GATE_XP = 25;
    public static int GATE_MONEY = 5;
    public static int MIN_SPAWN_DELAY = 200;
    public static int MAX_SPAWN_DELAY = 600;
    public static double MIN_DIST = 32;
    public static int MAX_GROUP = 1;
    public static int MIN_NEARBY = 2;
    public static int MAX_NEARBY = 5;
    public static int BASE_GATE_LEVEL = 1;
    public static GateLevelType GATE_LEVEL_TYPE = GateLevelType.DISTANCESPAWNPLAYER;
    public static PlayerLevelType PLAYER_LEVEL_TYPE = PlayerLevelType.MEAN;
    public static float TREASURE_CHANCE = 0.003f;
    public static float MIMIC_CHANCE = 0.4f;
    public static float MIMIC_STRONG_CHANCE = 0.3f;

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

    public static int NPC_SPAWN_RATE_MIN = 3600;
    public static int NPC_SPAWN_RATE_MAX = 7200;


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
