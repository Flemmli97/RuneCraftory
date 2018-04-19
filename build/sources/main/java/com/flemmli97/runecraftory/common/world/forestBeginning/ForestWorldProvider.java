package com.flemmli97.runecraftory.common.world.forestBeginning;

import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;

public class ForestWorldProvider extends WorldProvider
{ 
	 public void registerWorldChunkManager()
	 {
		 this.biomeProvider = new BiomeProviderSingle(Biomes.VOID);
		 this.setDimension(LibReference.dimID);
		 this.setAllowedSpawnTypes(false, false);
		 this.hasSkyLight = false;
	 }
	
	 @Override
	 public IChunkGenerator createChunkGenerator()
	 {
		 return new ForestChunkGenerator(this.world);
	 }
	
	 public Biome getBiomeGenForCoords(BlockPos pos)
	 {
		 return Biomes.FOREST;
	 }
	
	 @Override
	 public boolean canRespawnHere()
	 {
		 return true;
	 }
	
	 @Override
	 public int getRespawnDimension(EntityPlayerMP player)
	 {
		 return DimensionType.OVERWORLD.getId();
	 }
	
	 @Override
	 public boolean isSurfaceWorld()
	 {
		 return true;
	 }
	
	 @Override
	 public DimensionType getDimensionType() 
	 {
		 return DIMRegistry.FORESTOFBEGINNING;
	 }
}