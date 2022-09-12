package io.github.flemmli97.runecraftory.common.config;

import io.github.flemmli97.runecraftory.common.config.values.EntityProperties;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class MobConfig {

    public static boolean disableNaturalSpawn = false;

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

    public static Map<ResourceLocation, EntityProperties> propertiesMap = new HashMap<>();

    public enum GateLevelType {
        CONSTANT,
        DISTANCESPAWN,
        PLAYERLEVELMAX,
        PLAYERLEVELMEAN
    }
}
