package com.flemmli97.runecraftory.common.init.defaultval;

import java.util.Map;

import com.flemmli97.runecraftory.api.items.CropProperties;
import com.flemmli97.runecraftory.common.blocks.crops.BlockCropBase;
import com.flemmli97.runecraftory.common.core.handler.time.CalendarHandler.EnumSeason;
import com.flemmli97.runecraftory.common.items.food.ItemCrops;
import com.flemmli97.runecraftory.common.items.itemblocks.ItemCropSeed;
import com.flemmli97.runecraftory.common.lib.LibCropOreDictionary;
import com.google.common.collect.Maps;

/**
 * Maps crops oredictnames with their properties
 */
public class CropMap {

	private static final Map<String, ItemCropSeed> seedToID = Maps.newHashMap();
	private static final Map<String, ItemCrops> cropToID = Maps.newHashMap();
	private static final Map<String, BlockCropBase> plantToID = Maps.newHashMap();
	private static final Map<String, CropProperties> cropPropMap = Maps.newHashMap();
	
	public static void addSeed(String id, ItemCropSeed seed)
	{
		seedToID.put(id, seed);
	}
	
	public static void addCrop(String id, ItemCrops seed)
	{
		cropToID.put(id, seed);
	}
	
	public static void addPlant(String id, BlockCropBase seed)
	{
		plantToID.put(id, seed);
	}
	
	public static void overwriteProps(String id, CropProperties prop)
	{
		cropPropMap.put(id, prop);
	}
	
	public static ItemCropSeed seedFromString(String id)
	{
		return seedToID.get(id);
	}
	
	public static ItemCrops cropFromString(String id)
	{
		return cropToID.get(id);
	}
	
	public static BlockCropBase plantFromString(String id)
	{
		return plantToID.get(id);
	}
	
	public static CropProperties getProperties(String id)
	{
		return cropPropMap.get(id);
	}
	
	static
	{
		cropPropMap.put(LibCropOreDictionary.TURNIP, new CropProperties(EnumSeason.SPRING, 4, 2));
		cropPropMap.put(LibCropOreDictionary.CABBAGE, new CropProperties(EnumSeason.SPRING, 7, 3));
		cropPropMap.put(LibCropOreDictionary.PINKMELON, new CropProperties(EnumSeason.SPRING, 10, 1));
		cropPropMap.put(LibCropOreDictionary.PINKTURNIP, new CropProperties(EnumSeason.SPRING, 10, 2));

	}
}
