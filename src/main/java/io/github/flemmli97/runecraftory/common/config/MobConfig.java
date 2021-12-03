package io.github.flemmli97.runecraftory.common.config;

import io.github.flemmli97.runecraftory.common.config.values.EntityProperties;
import io.github.flemmli97.runecraftory.common.config.values.EntityPropertySpecs;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class MobConfig {

    public static boolean disableNaturalSpawn;

    public static float gateDef;
    public static float gateMDef;
    public static float gateHealth;
    public static float gateDefGain;
    public static float gateMDefGain;
    public static float gateHealthGain;
    public static int gateXP;
    public static int gateMoney;
    public static int spawnChance;

    public static Map<ResourceLocation, EntityProperties> propertiesMap = new HashMap<>();

    public static void load(MobConfigSpec spec) {
        disableNaturalSpawn = spec.disableNaturalSpawn.get();
        gateHealth = spec.gateHealth.get();
        gateDef = spec.gateDef.get();
        gateMDef = spec.gateMDef.get();
        gateHealthGain = spec.gateHealthGain.get();
        gateDefGain = spec.gateDefGain.get();
        gateMDefGain = spec.gateMDefGain.get();
        spawnChance = spec.spawnChance.get();
        gateXP = spec.gateXP.get();
        gateMoney = spec.gateMoney.get();

        for (Map.Entry<ResourceLocation, EntityPropertySpecs> e : spec.mobSpecs.entrySet()) {
            propertiesMap.merge(e.getKey(), new EntityProperties.Builder().build().read(e.getValue()), (old, v) -> old.read(e.getValue()));
        }
    }
}
