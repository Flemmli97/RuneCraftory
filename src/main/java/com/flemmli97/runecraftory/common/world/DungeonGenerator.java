package com.flemmli97.runecraftory.common.world;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.IWorldGenerator;

public class DungeonGenerator implements IWorldGenerator{

	private static Map<String, Structure> gens = Maps.newHashMap();

	public static void addStructureGen(Structure structure)
	{
		gens.put(structure.structureName(), structure);
	}
	
	public static Collection<Structure> structures()
	{
		return gens.values();
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
	
	static
	{
        DungeonGenerator.addStructureGen(new Structure("AmbrosiaForest", 50, false, 0, -4, Type.FOREST));
	}
	
}
