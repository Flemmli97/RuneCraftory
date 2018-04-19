package com.flemmli97.runecraftory.common.world;

import java.util.Random;
import java.util.Set;

import com.flemmli97.runecraftory.common.blocks.BlockMineral;
import com.flemmli97.runecraftory.common.blocks.BlockMineral.EnumTier;
import com.flemmli97.runecraftory.common.init.ModBlocks;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockGravel;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.IWorldGenerator;

public class MineralGenerator implements IWorldGenerator{

	private int spawnRate = 200;
	public MineralGenerator(int spawnRate)
	{
		this.spawnRate=spawnRate;
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {	
		int id = world.provider.getDimension();
		if(id==0 || id==1)
		{
			if(random.nextInt(this.spawnRate)==0)
			{
				int x = chunkX * 16 + random.nextInt(16)+8;
    			int z = chunkZ * 16 + random.nextInt(16)+8;
				BlockPos pos = new BlockPos(x,0,z);	
				int amount =random.nextInt(5);
				EnumTier tier = this.getRandomTier(world, random, pos);
				if(amount>=3 && EnumTier.isElemental(tier))
					amount=3;
				for(int i = 0; i < amount; i++)
				{
					this.generateMineral(world, pos, tier);
					pos = pos.add(random.nextInt(5)-2, 0, random.nextInt(5)-2);
				}
			}
		}
		else if(id==-1)
		{
			if(random.nextInt(Math.floorDiv(this.spawnRate, 8))==0)
			{
				int x = chunkX * 16 + random.nextInt(16)+8;
				int y = random.nextInt(50)+35;
		        int z = chunkZ * 16 + random.nextInt(16)+8;
				BlockPos pos = new BlockPos(x,y,z);
				int amount =random.nextInt(5) ;
				if(random.nextInt(3)==0)
				{
					for(int i = 0; i < amount; i++)
					{
						this.generateNether(world, pos, BlockMineral.EnumTier.FIRE);
						pos = pos.add(random.nextInt(5)-2, 0, random.nextInt(5)-2);
					}
				}
				else
				{
					for(int i = 0; i < amount; i++)
					{
						this.generateNether(world, pos, BlockMineral.EnumTier.RARE);
						pos = pos.add(random.nextInt(5)-2, 0, random.nextInt(5)-2);
					}
				}
			}
		}
		else if(id==LibReference.dimID)
		{
			if(random.nextInt(this.spawnRate)==0)
			{
				int x = chunkX * 16 + random.nextInt(16)+8;
		        int z = chunkZ * 16 + random.nextInt(16)+8;
				BlockPos pos = new BlockPos(x,0,z);
				int amount =random.nextInt(5);
				EnumTier tier = this.getRandomTier(world, random, pos);
				if(amount>=3 && EnumTier.isElemental(tier))
					amount=3;
				for(int i = 0; i < amount; i++)
				{
					this.generateMineral(world, pos, tier);
					pos = pos.add(random.nextInt(5)-2, 0, random.nextInt(5)-2);
				}
			}
		}
	}
	
	private void generateMineral(World world, BlockPos pos, BlockMineral.EnumTier tier)
	{
		pos = world.getTopSolidOrLiquidBlock(pos);
		Block block = world.getBlockState(pos.down()).getBlock();
		if(block instanceof BlockGrass || block instanceof BlockStone || block instanceof BlockGravel || block instanceof BlockSand)
		{
			IBlockState state = ModBlocks.mineral.getDefaultState().withProperty(BlockMineral.TIER, tier);
			world.setBlockState(pos, state, 2);
		}
	}
	
	private EnumTier getRandomTier(World world, Random rand, BlockPos pos)
	{
		Biome biome = world.getBiome(pos);
		Set<Type> types = BiomeDictionary.getTypes(biome);
		EnumTier tier = EnumTier.values()[rand.nextInt(5)];
 		if(pos.distanceSq(world.getSpawnPoint())>1500*1500)
			tier = EnumTier.values()[rand.nextInt(4)];
 		else if(pos.distanceSq(world.getSpawnPoint())>1000*1000)
			tier = EnumTier.values()[rand.nextInt(3)];
 		else if(pos.distanceSq(world.getSpawnPoint())>500*500)
			tier = EnumTier.values()[rand.nextInt(2)];
 		else
			tier = EnumTier.NONE;
		if(rand.nextInt(3)==0)
		{
			if(types.contains(Type.FOREST) || types.contains(Type.MOUNTAIN) || types.contains(Type.DEAD))
			{
				tier = EnumTier.EARTH;
			}
			else if(types.contains(Type.PLAINS)|| types.contains(Type.HILLS)||types.contains(Type.WASTELAND)||types.contains(Type.SPARSE))
			{
				tier = EnumTier.WIND;
			}
			else if(types.contains(Type.BEACH) || types.contains(Type.OCEAN)||types.contains(Type.RIVER)||types.contains(Type.WET))
			{
				tier = EnumTier.WATER;
			}
			else if(types.contains(Type.HOT))
			{
				tier = EnumTier.FIRE;
			}
		}
		return tier;
	}
	
	private void generateNether(World world, BlockPos pos, BlockMineral.EnumTier tier)
	{
		pos = world.getTopSolidOrLiquidBlock(pos);
		if(pos.getY()<120)
		{
			IBlockState state = ModBlocks.mineral.getDefaultState().withProperty(BlockMineral.TIER, tier);
			world.setBlockState(pos, state, 2);
		}
	}
}
