package com.flemmli97.runecraftory.common.core.handler.config;

import java.util.ArrayList;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.init.defaultval.EntityDefaultSpawns;
import com.google.common.collect.Lists;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SpawnConfig {


	protected static void init(Configuration config) {
		config.load();
		spawnData(config);
		config.save();
	}

	@SuppressWarnings("unchecked")
	private static void spawnData(Configuration config) {
		for(EntityEntry entry : ForgeRegistries.ENTITIES.getValues())
		{
			if(EntityMobBase.class.isAssignableFrom(entry.getEntityClass()))
			{
				Class<? extends EntityMobBase> entity = (Class<? extends EntityMobBase>) entry.getEntityClass();
				ArrayList<String> biomeTypeList = Lists.newArrayList(config.getStringList("BiomeTypes", entry.getRegistryName().toString(),
						EntityDefaultSpawns.classTypeMap.get(entity).toArray(new String[0]), "Specify which biome type this entity spawns in"));
				ArrayList<String> biomeList = Lists.newArrayList(config.getStringList("Biomes", entry.getRegistryName().toString(),
						EntityDefaultSpawns.classBiomeMap.get(entity).toArray(new String[0]), "Specify which biome this entity spawns in. Uses the biome name without whitespaces e.g. ColdTaigaM"));
				EntityDefaultSpawns.classTypeMap.removeAll(entity);
				EntityDefaultSpawns.classBiomeMap.removeAll(entity);
				EntityDefaultSpawns.classTypeMap.putAll(entity, biomeTypeList);
				EntityDefaultSpawns.classBiomeMap.putAll(entity, biomeList);
			}
		}
		
	}
}
