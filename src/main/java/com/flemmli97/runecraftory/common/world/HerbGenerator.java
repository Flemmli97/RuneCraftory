package com.flemmli97.runecraftory.common.world;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.common.init.ModBlocks;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.init.Biomes;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class HerbGenerator implements IWorldGenerator{

	private static final Map<Biome,List<HerbEntry>> map = Maps.newHashMap();
	
	public static void add(Biome biome, HerbEntry entry)
	{
		map.merge(biome, Lists.newArrayList(entry), (old,newVal)->{old.addAll(newVal);return old;});
	}
	
	public HerbGenerator()
	{
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {	
		int i = chunkX * 16;
        int j = chunkZ * 16;
        BlockPos blockpos = new BlockPos(i, 0, j);
        Biome biome = world.getBiome(blockpos.add(16, 0, 16));
		HerbEntry e = this.getEntry(biome, random);
		if(e==null || e.state==null)
			return;
		for(int attempt = 0; attempt < e.tries; attempt++)
		{
			int i7 = random.nextInt(16) + 8;
	        int l10 = random.nextInt(16) + 8;
	        int j14 = world.getHeight(new BlockPos(i+i7, 0, j+l10)).getY() + 32;
	
	        if (j14 > 0)
	        {
	            int k17 = random.nextInt(j14);
	            BlockPos blockpos1 = new BlockPos(i+i7, k17, j+l10);
	            int amount = 0;
	            int maxAmount = random.nextInt(e.maxAmount)+1;
	            for (int n = 0; n < 64; ++n)
	            {
	                BlockPos pos = blockpos1.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));
	                boolean canStay = !(e.state.getBlock() instanceof BlockBush) || ((BlockBush)e.state.getBlock()).canBlockStay(world, pos, e.state);	              
	                if (world.isAirBlock(pos) && (!world.provider.isNether() || pos.getY() < 128) && canStay && amount<maxAmount)
	                {
	                	amount++;
	                    world.setBlockState(pos, e.state, 2);
	                }
	            }
	        }
		}
	}
	
	/*
	 * public static final Block mushroom = new BlockPlants("mushroom", LibOreDictionary.MUSHROOM);
	public static final Block elliLeaves = new BlockPlants("elli_leaves", LibOreDictionary.ELLILEAVES);
	public static final Block witheredGrass = new BlockPlants("withered_grass", LibOreDictionary.WITHEREDGRASS);
	public static final Block weeds = new BlockPlants("weeds", LibOreDictionary.WEEDS);
	public static final Block whiteGrass = new BlockPlants("white_grass", LibOreDictionary.WHITEGRASS);
	public static final Block indigoGrass = new BlockPlants("indigo_grass", LibOreDictionary.INDIGOGRASS);
	public static final Block purpleGrass = new BlockPlants("purple_grass", LibOreDictionary.PURPLEGRASS);
	public static final Block greenGrass = new BlockPlants("green_grass", LibOreDictionary.GREENGRASS);
	public static final Block blueGrass = new BlockPlants("blue_grass", LibOreDictionary.BLUEGRASS);
	public static final Block yellowGrass = new BlockPlants("yellow_grass", LibOreDictionary.YELLOWGRASS);
	public static final Block redGrass = new BlockPlants("red_grass", LibOreDictionary.REDGRASS);
	public static final Block orangeGrass = new BlockPlants("orange_grass", LibOreDictionary.ORANGEGRASS);
	public static final Block blackGrass = new BlockPlants("black_grass", LibOreDictionary.BLACKGRASS);
	public static final Block antidoteGrass = new BlockPlants("antidote_grass", LibOreDictionary.ANTIDOTEGRASS);
	public static final Block medicinalHerb = new BlockPlants("medicinal_herb", LibOreDictionary.MEDICINALHERB);
	public static final Block bambooSprout = new BlockPlants("bamboo_sprout", LibOreDictionary.BAMBOOSPROUT);
	 */
	
	@Nullable
	public HerbEntry getEntry(Biome biome, Random rand)
	{
		if(map.get(biome)!=null && map.get(biome).size()>0)
			return WeightedRandom.getRandomItem(rand, map.get(biome));
		return null;
	}
	
	public static class HerbEntry extends Biome.FlowerEntry
	{
		private Block block;
		private int tries;
		private int maxAmount;
		
		public HerbEntry(Block state, int weight, int tries, int maxAmount) {
			super(state.getDefaultState(), weight);
			this.block=state;
			this.tries = tries;
			this.maxAmount=maxAmount;
		}
		public HerbEntry(String s)
		{
			super(null, 1);
			this.fromString(s);
		}
		
		public String writeToString()
		{
			return block.getRegistryName()+","+this.itemWeight+","+this.tries+","+this.maxAmount;
		}
		
		public void fromString(String s)
		{
			
		}
		@Override
		public String toString()
		{
			return this.writeToString();
		}
	}
	
	static
	{
		add(Biomes.PLAINS, new HerbEntry(ModBlocks.weeds, 30, 4, 5));
		add(Biomes.PLAINS, new HerbEntry(ModBlocks.antidoteGrass, 10, 2, 5));
		add(Biomes.PLAINS, new HerbEntry(ModBlocks.weeds, 30, 4, 5));
		add(Biomes.PLAINS, new HerbEntry(ModBlocks.weeds, 30, 4, 5));

	}
}
