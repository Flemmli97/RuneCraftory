package com.flemmli97.runecraftory.common.init;

import java.util.ArrayList;
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
import net.minecraftforge.common.BiomeDictionary.Type;

public class GateSpawning {
	
	private static Multimap<Biome, String> spawningMappingBiome = ArrayListMultimap.create();

	public static void addToBiomeType(Class<?extends EntityMobBase> clss, Type... biomeType)
	{
		for(Biome biome : Biome.REGISTRY)
		{
			for(int i = 0; i <biomeType.length; i++)
			{
				if(BiomeDictionary.getTypes(biome).contains(biomeType[i]))
				{
					spawningMappingBiome.put(biome, nameFromClass(clss));
				}
			}
		}
	}

	public static void addToBiome(Class<?extends EntityMobBase> clss, Biome... biome)
	{
		for(int i = 0; i <biome.length; i++)
		{
			spawningMappingBiome.put(biome[i], nameFromClass(clss));
		}
	}

	public static List<String> getSpawningListFromBiome(Biome biome)
	{
		List<String> list = new ArrayList<String>(spawningMappingBiome.get(biome));
		return list;
	}
	
	public static String nameFromClass(Class<?extends EntityMobBase> living)
	{
		ResourceLocation loc = EntityList.getKey(living);
		return loc.toString();
	}
	
	public static EntityMobBase entityFromString(World world, String entity)
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
