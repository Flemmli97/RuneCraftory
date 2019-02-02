package com.flemmli97.runecraftory.common.init;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class GateSpawning {
	
	private static Multimap<Biome, ResourceLocation> spawningMappingBiome = ArrayListMultimap.create();

	public static void addToBiome(ResourceLocation res, Collection<String> biomeNames) {
		for(String biomeName:biomeNames)
		{
			Biome biome = Biome.REGISTRY.getObject(new ResourceLocation(biomeName));
			if(biome!=null)
				spawningMappingBiome.put(biome, res);
		}
	}

	public static void addToBiomeType(ResourceLocation res, Collection<String> biomeTypes) {
		for(String typeName:biomeTypes)
		{
			for(Biome biome : BiomeDictionary.getBiomes(BiomeDictionary.Type.getType(typeName)))
			{
				spawningMappingBiome.put(biome, res);
			}
		}
	}

	public static final void addToBiomeType(ResourceLocation res, BiomeDictionary.Type... biomeType)
	{
		for(int i = 0; i <biomeType.length; i++)
		{
			for(Biome biome : BiomeDictionary.getBiomes(biomeType[i]))
			{					
				spawningMappingBiome.put(biome, res);
			}
		}
	}

	public static final void addToBiome(ResourceLocation res, Biome... biome)
	{
		for(int i = 0; i <biome.length; i++)
		{
			spawningMappingBiome.put(biome[i], res);
		}
	}

	public static final List<ResourceLocation> getSpawningListFromBiome(Biome biome)
	{
		return new ArrayList<ResourceLocation>(spawningMappingBiome.get(biome));
	}
	
	public static final ResourceLocation nameFromClass(Class<?extends EntityMobBase> living)
	{
		return EntityList.getKey(living);
	}
	
	public static final EntityMobBase entityFromString(World world, ResourceLocation entity)
	{
		try 
        {
			Entity e = EntityList.createEntityByIDFromName(entity, world);
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
