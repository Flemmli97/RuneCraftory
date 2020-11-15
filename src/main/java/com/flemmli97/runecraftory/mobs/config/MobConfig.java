package com.flemmli97.runecraftory.mobs.config;

import com.flemmli97.runecraftory.mobs.entity.BaseMonster;
import com.google.common.collect.Maps;
import net.minecraft.entity.EntityType;

import java.util.Map;

public class MobConfig {

    public static boolean disableNaturalSpawn;

    public static float gateDef = 5f;
    public static float gateMDef = 5f;
    public static float gateHealth = 100;
    public static int gateXP = 10;
    public static int gateMoney = 4;
    public static int spawnChance = 150;

    public static Map<EntityType<? extends BaseMonster>, EntityProperties> propertiesMap = Maps.newHashMap();

    public static void load(MobConfigSpec spec){
        disableNaturalSpawn = spec.disableNaturalSpawn.get();
        gateHealth = spec.gateHealth.get();
        gateDef = spec.gateDef.get();
        gateMDef = spec.gateMDef.get();
        spawnChance = spec.spawnChance.get();
        gateXP = spec.gateXP.get();
        gateMoney = spec.gateMoney.get();

        for(Map.Entry<EntityType<? extends BaseMonster>, EntityPropertySpecs> e : spec.mobSpecs.entrySet()){
            propertiesMap.merge(e.getKey(), new EntityProperties.Builder().build().read(e.getValue()), (old,v)->old.read(e.getValue()));
        }
    }
}
