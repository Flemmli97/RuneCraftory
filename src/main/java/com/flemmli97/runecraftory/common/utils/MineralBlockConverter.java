package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.common.init.ModBlocks;
import com.flemmli97.runecraftory.common.lib.enums.EnumMineralTier;

import net.minecraft.block.state.IBlockState;

public class MineralBlockConverter {

	public static IBlockState getBrokenState(EnumMineralTier tier)
	{
		switch(tier)
		{
			case BRONZE: return ModBlocks.brokenMineralBronze.getDefaultState();
			case DIAMOND: return ModBlocks.brokenMineralDiamond.getDefaultState();
			case DRAGONIC: return ModBlocks.brokenMineralDragonic.getDefaultState();
			case EARTH: return ModBlocks.brokenMineralEarth.getDefaultState();
			case FIRE: return ModBlocks.brokenMineralFire.getDefaultState();
			case GOLD: return ModBlocks.brokenMineralGold.getDefaultState();
			case IRON: return ModBlocks.brokenMineralIron.getDefaultState();
			case ORICHALCUM: return ModBlocks.brokenMineralOrichalcum.getDefaultState();
			case PLATINUM: return ModBlocks.brokenMineralPlatinum.getDefaultState();
			case SILVER: return ModBlocks.brokenMineralSilver.getDefaultState();
			case WATER: return ModBlocks.brokenMineralWater.getDefaultState();
			case WIND: return ModBlocks.brokenMineralWind.getDefaultState();
		}
		return null;
	}
	
	public static IBlockState getRestoredState(EnumMineralTier tier)
	{
		switch(tier)
		{
			case BRONZE: return ModBlocks.mineralBronze.getDefaultState();
			case DIAMOND: return ModBlocks.mineralDiamond.getDefaultState();
			case DRAGONIC: return ModBlocks.mineralDragonic.getDefaultState();
			case EARTH: return ModBlocks.mineralEarth.getDefaultState();
			case FIRE: return ModBlocks.mineralFire.getDefaultState();
			case GOLD: return ModBlocks.mineralGold.getDefaultState();
			case IRON: return ModBlocks.mineralIron.getDefaultState();
			case ORICHALCUM: return ModBlocks.mineralOrichalcum.getDefaultState();
			case PLATINUM: return ModBlocks.mineralPlatinum.getDefaultState();
			case SILVER: return ModBlocks.mineralSilver.getDefaultState();
			case WATER: return ModBlocks.mineralWater.getDefaultState();
			case WIND: return ModBlocks.mineralWind.getDefaultState();
		}
		return null;
	}
}
