package com.flemmli97.runecraftory.api.mappings;

import java.util.Map;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.api.items.CropProperties;
import com.flemmli97.runecraftory.common.blocks.crops.BlockCropBase;
import com.flemmli97.runecraftory.common.core.handler.time.CalendarHandler.EnumSeason;
import com.flemmli97.runecraftory.common.items.consumables.ItemCrops;
import com.flemmli97.runecraftory.common.items.itemblocks.ItemCropSeed;
import com.flemmli97.tenshilib.api.config.SimpleItemStackWrapper;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CropMap
{
	/**
	 * Uses the crops (not the seeds) oredict name
	 */
    private static final Map<String, ItemCropSeed> seedToID = Maps.newHashMap();
    private static final Map<String, Item> cropToID = Maps.newHashMap();
    private static final Map<String, ItemCrops> giantCropToID = Maps.newHashMap();
    private static final Map<String, BlockCropBase> plantToID = Maps.newHashMap();
    
    /**
     * Maps seed items to properties
     */
    private static final Map<SimpleItemStackWrapper, CropProperties> cropProperties  = Maps.newHashMap();
    
    private static final CropProperties def = new CropProperties(Sets.newHashSet(EnumSeason.SPRING), Sets.newHashSet(), 7, 2, false);
    
    private static int count = 0;
        
    public static void addSeed(String id, ItemCropSeed seed) {
        seedToID.put(id, seed);
    }
    
    public static Item addCrop(String id, Item crop) {
        cropToID.put(id, crop);
        return crop;
    }
    
    public static void addGiantCrop(String id, ItemCrops seed) {
    	giantCropToID.put(id, seed);
    }
    
    public static void addPlant(String id, BlockCropBase seed) {
        plantToID.put(id, seed);
    }
    
    public static ItemCropSeed seedFromString(String id) {
        return seedToID.get(id);
    }
    
    public static Item cropFromString(String id) {
        return cropToID.get(id);
    }
    
    @Nullable
    public static ItemCrops giantCropFromString(String id) {
        return giantCropToID.get(id);
    }
    
    public static BlockCropBase plantFromString(String id) {
        return plantToID.get(id);
    }
    
    public static void addProperties(SimpleItemStackWrapper item, CropProperties prop) {
        if(cropProperties.put(item, prop) == null)
        	count+=1;
    }
    
    @Nullable
    public static CropProperties getProperties(ItemStack stack) {
        return getProperties(stack, false);
    }
    
    @Nullable
    public static CropProperties getProperties(ItemStack stack, boolean getDefault) {
        CropProperties statNoMeta = cropProperties.get(new SimpleItemStackWrapper(stack).setIgnoreAmount().setIgnoreMeta());
        if (statNoMeta != null) {
            return statNoMeta;
        }
        if(getDefault)
        	return cropProperties.getOrDefault(new SimpleItemStackWrapper(stack).setIgnoreAmount(), def);
        return cropProperties.get(new SimpleItemStackWrapper(stack).setIgnoreAmount());
    }
    
    public static int propCount()
    {
    	return count;
    }
}
