package io.github.flemmli97.runecraftory.common.config;

import io.github.flemmli97.runecraftory.common.config.values.EntityProperties;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class MobConfig {

    public static boolean disableNaturalSpawn;

    public static double gateDef;
    public static double gateMDef;
    public static double gateHealth;
    public static double gateDefGain;
    public static double gateMDefGain;
    public static double gateHealthGain;
    public static int gateXP;
    public static int gateMoney;
    public static int spawnChance;
    public static double minDist;
    public static int maxGroup;
    public static int maxNearby;

    public static Map<ResourceLocation, EntityProperties> propertiesMap = new HashMap<>();
}
