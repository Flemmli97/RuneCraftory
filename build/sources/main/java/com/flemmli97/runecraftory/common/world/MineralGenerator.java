package com.flemmli97.runecraftory.common.world;

import java.util.Random;
import java.util.Set;

import com.flemmli97.runecraftory.common.blocks.BlockMineral;
import com.flemmli97.runecraftory.common.init.ModBlocks;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
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

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {	
		if(world.provider.getDimension()==0)
		{
			if(random.nextInt(this.spawnRate)==0)
			{
				int cluster = random.nextInt(3)+1;
				int x = chunkX * 16 + random.nextInt(16);
        			int z = chunkZ * 16 + random.nextInt(16);
				for(int i = 0; i < cluster; i++)
				{
					x+=random.nextInt(2)-1;
					z+=random.nextInt(2)-1;
        				BlockPos pos = new BlockPos(x,0,z);	
					this.generateRandomMineral(random, world, pos);
				}
			}

		}
		else if(world.provider.getDimension()==1)
		{
			if(random.nextInt(Math.floorDiv(this.spawnRate, 6))==0)
			{
				int cluster = random.nextInt(3)+1;
				int x = chunkX * 16 + random.nextInt(16);
        			int z = chunkZ * 16 + random.nextInt(16);
				for(int i = 0; i < cluster; i++)
				{
					x+=random.nextInt(2)-1;
					z+=random.nextInt(2)-1;
					BlockPos pos = new BlockPos(x,0,z);
					this.generateMineral(world, pos, BlockMineral.EnumTier.DRAGONIC);
				}
			}
		}
		else if(world.provider.getDimension()==-1)
		{
			if(random.nextInt(Math.floorDiv(this.spawnRate, 8))==0)
			{
				int x = chunkX * 16 + random.nextInt(16);
				int y = random.nextInt(50)+35;
		        int z = chunkZ * 16 + random.nextInt(16);
				BlockPos pos = new BlockPos(x,y,z);
				if(random.nextInt(3)==0)
					this.generateNether(world, pos, BlockMineral.EnumTier.FIRE);
				else
					this.generateNether(world, pos, BlockMineral.EnumTier.RARE);
			}
		}
		else if(world.provider.getDimension()==LibReference.dimID)
		{
			if(random.nextInt(this.spawnRate)==0)
			{
				int x = chunkX * 16 + random.nextInt(16);
		        int z = chunkZ * 16 + random.nextInt(16);
				BlockPos pos = new BlockPos(x,0,z);
				BlockMineral.EnumTier[] tiers = BlockMineral.EnumTier.values();
				this.generateMineral(world, pos, tiers[random.nextInt(tiers.length)]);
			}
		}
	}
	
	private void generateRandomMineral(Random rand, World world, BlockPos pos)
	{
		Biome biome = world.getBiome(pos);
		Set<Type> types = BiomeDictionary.getTypes(biome);
	}
	
	private boolean generateMineral(World world, BlockPos pos, BlockMineral.EnumTier tier)
	{
		BlockPos pos1 = world.getHeight(pos);
		if((world.getBlockState(pos1).getMaterial()==Material.AIR || world.getBlockState(pos1).getBlock().isReplaceable(world, pos1)) && world.getBlockState(pos1.down()).isSideSolid(world, pos1.down(), EnumFacing.UP))
		{
			IBlockState state = ModBlocks.mineral.getDefaultState().withProperty(BlockMineral.TIER, tier);
			world.setBlockState(pos1, state, 2);
			return true;
		}
		return false;
	}
	
	private boolean generateNether(World world, BlockPos pos, BlockMineral.EnumTier tier)
	{
		if(pos.getY()<120)
		{
			boolean flag = false;
			if((world.getBlockState(pos).getMaterial()==Material.AIR || world.getBlockState(pos).getBlock().isReplaceable(world, pos)) && world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.UP))
			{
				IBlockState state = ModBlocks.mineral.getDefaultState().withProperty(BlockMineral.TIER, tier);
				world.setBlockState(pos, state, 2);
				flag = true;
			}
			if(!flag)
				return this.generateNether(world, pos.up(), tier);
			return true;
		}
		return false;
	}
}
