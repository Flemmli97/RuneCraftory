package com.flemmli97.runecraftory.common.init;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.init.defaultval.EntityDefaultSpawns;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class GateSpawning {
	
	private static Multimap<Biome, String> spawningMappingBiome = ArrayListMultimap.create();
	
	public static final void initGateSpawnings()
	{
		for(Class<?extends EntityMobBase> clss : EntityDefaultSpawns.classTypeMap.keys())
			addToBiomeType(clss, EntityDefaultSpawns.classTypeMap.get(clss));
		for(Class<?extends EntityMobBase> clss : EntityDefaultSpawns.classBiomeMap.keys())
			addToBiome(clss, EntityDefaultSpawns.classBiomeMap.get(clss));
	}

	public static void addToBiome(Class<? extends EntityMobBase> clss, Collection<String> biomeNames) {
		for(String biomeName:biomeNames)
		{
			Iterator<Biome> it = Biome.REGISTRY.iterator();
			while(it.hasNext())
			{
				Biome biome = it.next();
				if(biomeName.equals(biome.getBiomeName().trim()))
				{
					spawningMappingBiome.put(biome, nameFromClass(clss));
					break;
				}
			}
		}
	}

	public static void addToBiomeType(Class<? extends EntityMobBase> clss, Collection<String> biomeTypes) {
		for(String typeName:biomeTypes)
		{
			for(Biome biome : BiomeDictionary.getBiomes(BiomeDictionary.Type.getType(typeName)))
			{
				spawningMappingBiome.put(biome, nameFromClass(clss));
			}
		}
	}

	public static final void addToBiomeType(Class<?extends EntityMobBase> clss, BiomeDictionary.Type... biomeType)
	{
		for(int i = 0; i <biomeType.length; i++)
		{
			for(Biome biome : BiomeDictionary.getBiomes(biomeType[i]))
			{					
				spawningMappingBiome.put(biome, nameFromClass(clss));
			}
		}
	}

	public static final void addToBiome(Class<?extends EntityMobBase> clss, Biome... biome)
	{
		for(int i = 0; i <biome.length; i++)
		{
			spawningMappingBiome.put(biome[i], nameFromClass(clss));
		}
	}

	public static final List<String> getSpawningListFromBiome(Biome biome)
	{
		List<String> list = new ArrayList<String>(spawningMappingBiome.get(biome));
		return list;
	}
	
	public static final String nameFromClass(Class<?extends EntityMobBase> living)
	{
		ResourceLocation loc = EntityList.getKey(living);
		return loc.toString();
	}
	
	public static final EntityMobBase entityFromString(World world, String entity)
	{
    		try 
        {
    			Entity e = EntityList.createEntityByIDFromName(new ResourceLocation(entity), world);
    			if(e instanceof EntityMobBase)
    				return (EntityMobBase) e;
    			return null;
    		}  
        catch (Exception exc)
        {
        		return null;
        }	
	}
}
