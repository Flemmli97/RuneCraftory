package com.flemmli97.runecraftory.common.world;

import java.util.HashMap;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class DungeonGenerator implements IWorldGenerator{

	private static HashMap<String, Structure> gens = new HashMap<String, Structure>();
	
	public static void addStructureGen(Structure structure)
	{
		if(!gens.containsKey(structure.structureName()))
			gens.put(structure.structureName(), structure);
	}

	public static Structure fromID(String name)
	{
		return gens.get(name);
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		for(String s : gens.keySet())
		{
			gens.get(s).generate(world, chunkX, chunkZ, random);
		}
	}
	
}
