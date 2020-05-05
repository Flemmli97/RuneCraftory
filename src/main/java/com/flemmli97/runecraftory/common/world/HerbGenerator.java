package com.flemmli97.runecraftory.common.world;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
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
	
	public static void clear()
	{
		map.clear();
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

		@Override
		public String toString()
		{
			return this.block.getRegistryName()+","+this.itemWeight+","+this.tries+","+this.maxAmount;
		}
	}
}
