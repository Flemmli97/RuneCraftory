package com.flemmli97.runecraftory.common.init;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
		for (BiomeDictionary.Type type : biomeType) {
			for (Biome biome : BiomeDictionary.getBiomes(type)) {
				spawningMappingBiome.put(biome, res);
			}
		}
	}

	public static final void addToBiome(ResourceLocation res, Biome... biome)
	{
		for (Biome value : biome) {
			spawningMappingBiome.put(value, res);
		}
	}

	public static final List<ResourceLocation> getSpawningListFromBiome(Biome biome)
	{
		return new ArrayList<ResourceLocation>(spawningMappingBiome.get(biome));
	}

	public static ResourceLocation getWeightedRes(World world, BlockPos pos){
		return null;
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

	private static class WeightedResourceLoc extends WeightedRandom.Item{

		private ResourceLocation loc;
		private int distToSpawnSq;
		public WeightedResourceLoc(ResourceLocation loc, int itemWeightIn, int distToSpawn) {
			super(itemWeightIn);
			this.loc = loc;
			this.distToSpawnSq = distToSpawn*distToSpawn;
		}

		public boolean canSpawn(World world, BlockPos pos){
			return pos.distanceSq(world.getSpawnPoint()) < this.distToSpawnSq;
		}
	}
}
