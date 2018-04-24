package com.flemmli97.runecraftory.common.world;

import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class DungeonGenerator implements IWorldGenerator{

	private static Map<String, Structure> gens = Maps.newHashMap();

	public static void addStructureGen(Structure structure)
	{
		gens.put(structure.structureName(), structure);
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		for(String s : gens.keySet())
		{
			gens.get(s).start(world, chunkX, chunkZ, random);
			gens.get(s).gen(world);
		}
	}
	
}
