package com.flemmli97.runecraftory.common.core.handler.config;

import net.minecraftforge.common.config.*;
import net.minecraftforge.fml.common.registry.*;
import com.flemmli97.runecraftory.common.entity.*;
import com.flemmli97.runecraftory.api.mappings.*;
import com.google.common.collect.*;
import java.util.*;

public class SpawnConfig
{
    protected static void init(Configuration config) 
    {
        config.load();
        spawnData(config);
        config.save();
    }
    
    @SuppressWarnings("unchecked")
	private static void spawnData(Configuration config) 
    {
        for (EntityEntry entry : ForgeRegistries.ENTITIES.getValues()) 
        {
            if (EntityMobBase.class.isAssignableFrom(entry.getEntityClass())) 
            {
                Class<? extends EntityMobBase> entity = (Class<? extends EntityMobBase>)entry.getEntityClass();
                ArrayList<String> biomeTypeList = Lists.newArrayList(config.getStringList("BiomeTypes", entry.getRegistryName().toString(), 
                		EntitySpawnMap.classTypeMap.get(entity).toArray(new String[0]), "Specify which biome type this entity spawns in"));
                ArrayList<String> biomeList = Lists.newArrayList(config.getStringList("Biomes", entry.getRegistryName().toString(),
                		EntitySpawnMap.classBiomeMap.get(entity).toArray(new String[0]), "Specify which biome this entity spawns in. Uses the biome name without whitespaces e.g. ColdTaigaM"));
                EntitySpawnMap.classTypeMap.removeAll(entity);
                EntitySpawnMap.classBiomeMap.removeAll(entity);
                EntitySpawnMap.classTypeMap.putAll(entity, biomeTypeList);
                EntitySpawnMap.classBiomeMap.putAll(entity, biomeList);
            }
        }
    }
}
