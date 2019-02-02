package com.flemmli97.runecraftory.common.world;

import java.util.Random;
import java.util.Set;

import com.flemmli97.runecraftory.common.core.handler.config.ConfigHandler;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumMineralTier;
import com.flemmli97.runecraftory.common.utils.MineralBlockConverter;

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
	
	private int bronze=250;
	private int silver=500;
	private int gold=1000;
	private int diamond=1250;
	private int platinum=1500;
	private int orichalcum=2000;

	public MineralGenerator()
	{
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {	
		int id = world.provider.getDimension();
		if(id==0 || id==1)
		{
			if(random.nextInt(ConfigHandler.GenerationConfig.mineralRate)==0)
			{
				int x = chunkX * 16 + random.nextInt(16)+8;
    			int z = chunkZ * 16 + random.nextInt(16)+8;
				BlockPos pos = new BlockPos(x,0,z);	
				int amount =random.nextInt(5);
				EnumMineralTier tier = this.getRandomTier(world, random, pos);
				if(amount>=3 && EnumMineralTier.isElemental(tier))
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
			if(random.nextInt(Math.floorDiv(ConfigHandler.GenerationConfig.mineralRate, 8))==0)
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
						this.generateNether(world, pos, EnumMineralTier.FIRE);
						pos = pos.add(random.nextInt(5)-2, 0, random.nextInt(5)-2);
					}
				}
				else
				{
					for(int i = 0; i < amount; i++)
					{
						this.generateNether(world, pos, EnumMineralTier.GOLD);
						pos = pos.add(random.nextInt(5)-2, 0, random.nextInt(5)-2);
					}
				}
			}
		}
		else if(id==LibReference.dimID)
		{
			if(random.nextInt(ConfigHandler.GenerationConfig.mineralRate)==0)
			{
				int x = chunkX * 16 + random.nextInt(16)+8;
		        int z = chunkZ * 16 + random.nextInt(16)+8;
				BlockPos pos = new BlockPos(x,0,z);
				int amount =random.nextInt(5);
				EnumMineralTier tier = this.getRandomTier(world, random, pos);
				if(amount>=3 && EnumMineralTier.isElemental(tier))
					amount=3;
				for(int i = 0; i < amount; i++)
				{
					this.generateMineral(world, pos, tier);
					pos = pos.add(random.nextInt(5)-2, 0, random.nextInt(5)-2);
				}
			}
		}
	}
	
	private void generateMineral(World world, BlockPos pos, EnumMineralTier tier)
	{
		pos = world.getTopSolidOrLiquidBlock(pos);
		Block block = world.getBlockState(pos.down()).getBlock();
		if(block instanceof BlockGrass || block instanceof BlockStone || block instanceof BlockGravel || block instanceof BlockSand)
		{
			IBlockState state = MineralBlockConverter.getRestoredState(tier);
			world.setBlockState(pos, state, 2);
		}
	}
	
	private EnumMineralTier getRandomTier(World world, Random rand, BlockPos pos)
	{
		Biome biome = world.getBiome(pos);
		Set<Type> types = BiomeDictionary.getTypes(biome);
		EnumMineralTier tier = EnumMineralTier.values()[rand.nextInt(5)];
		if(pos.distanceSq(world.getSpawnPoint())>this.orichalcum)
			tier = EnumMineralTier.values()[rand.nextInt(7)];
		else if(pos.distanceSq(world.getSpawnPoint())>this.platinum)
			tier = EnumMineralTier.values()[rand.nextInt(6)];
		else if(pos.distanceSq(world.getSpawnPoint())>this.diamond)
			tier = EnumMineralTier.values()[rand.nextInt(5)];
		else if(pos.distanceSq(world.getSpawnPoint())>this.gold)
			tier = EnumMineralTier.values()[rand.nextInt(4)];
 		else if(pos.distanceSq(world.getSpawnPoint())>this.silver)
			tier = EnumMineralTier.values()[rand.nextInt(3)];
 		else if(pos.distanceSq(world.getSpawnPoint())>this.bronze)
			tier = EnumMineralTier.values()[rand.nextInt(2)];
 		else
			tier = EnumMineralTier.IRON;
		if(rand.nextInt(3)==0)
		{
			if(types.contains(Type.FOREST) || types.contains(Type.MOUNTAIN) || types.contains(Type.DEAD))
			{
				tier = EnumMineralTier.EARTH;
			}
			else if(types.contains(Type.PLAINS)|| types.contains(Type.HILLS)||types.contains(Type.WASTELAND)||types.contains(Type.SPARSE))
			{
				tier = EnumMineralTier.WIND;
			}
			else if(types.contains(Type.BEACH) || types.contains(Type.OCEAN)||types.contains(Type.RIVER)||types.contains(Type.WET))
			{
				tier = EnumMineralTier.WATER;
			}
			else if(types.contains(Type.HOT))
			{
				tier = EnumMineralTier.FIRE;
			}
		}
		return tier;
	}
	
	private void generateNether(World world, BlockPos pos, EnumMineralTier tier)
	{
		pos = world.getTopSolidOrLiquidBlock(pos);
		if(pos.getY()<120)
		{
			IBlockState state = MineralBlockConverter.getRestoredState(tier);
			world.setBlockState(pos, state, 2);
		}
	}
}
