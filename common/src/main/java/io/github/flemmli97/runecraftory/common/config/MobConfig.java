package io.github.flemmli97.runecraftory.common.config;

import io.github.flemmli97.runecraftory.common.config.values.EntityProperties;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class MobConfig {

    public static boolean disableNaturalSpawn = false;
    public static int farmRadius = 3;
    public static boolean mobAttackNPC;

    public static int bellRadius = 48;
    public static double gateHealth = 100;
    public static double gateDef = 0;
    public static double gateMDef = 0;
    public static double gateHealthGain = 30;
    public static double gateDefGain = 2.5;
    public static double gateMDefGain = 2.5;
    public static int gateXP = 25;
    public static int gateMoney = 5;
    public static int spawnChance = 150;
    public static double minDist = 48;
    public static int maxGroup = 2;
    public static int maxNearby = 4;
    public static int baseGateLevel = 1;
    public static GateLevelType gateLevelType = GateLevelType.CONSTANT;
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

    public static Map<ResourceLocation, EntityProperties> propertiesMap = new HashMap<>();

    public enum GateLevelType {
        CONSTANT,
        DISTANCESPAWN,
        PLAYERLEVELMAX,
        PLAYERLEVELMEAN
    }
}
