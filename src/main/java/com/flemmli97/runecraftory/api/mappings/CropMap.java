package com.flemmli97.runecraftory.api.mappings;

import java.util.Map;
import java.util.Set;

import com.flemmli97.runecraftory.api.items.CropProperties;
import com.flemmli97.runecraftory.common.blocks.crops.BlockCropBase;
import com.flemmli97.runecraftory.common.core.handler.time.CalendarHandler;
import com.flemmli97.runecraftory.common.items.food.ItemCrops;
import com.flemmli97.runecraftory.common.items.itemblocks.ItemCropSeed;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.google.common.collect.Maps;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

public class CropMap
{
    private static final Map<String, ItemCropSeed> seedToID = Maps.newHashMap();
    private static final Map<String, ItemCrops> cropToID = Maps.newHashMap();
    private static final Map<String, BlockCropBase> plantToID = Maps.newHashMap();
    
    private static final Map<String, CropProperties> cropPropMapPre  = Maps.newHashMap();
    
    private static final Map<ResourceLocation, CropProperties> cropProperties  = Maps.newHashMap();
    
    public static void addSeed(String id, ItemCropSeed seed) {
        seedToID.put(id, seed);
    }
    
    public static void addCrop(String id, ItemCrops seed) {
        cropToID.put(id, seed);
    }
    
    public static void addPlant(String id, BlockCropBase seed) {
        plantToID.put(id, seed);
    }
    
    public static void putProps(String id, CropProperties prop) {
        cropPropMapPre.put(id, prop);
    }
    
    public static ItemCropSeed seedFromString(String id) {
        return seedToID.get(id);
    }
    
    public static ItemCrops cropFromString(String id) {
        return cropToID.get(id);
    }
    
    public static BlockCropBase plantFromString(String id) {
        return plantToID.get(id);
    }
    
    public static CropProperties getPropertiesFromID(String ore) {
        return cropPropMapPre.get(ore);
    }
    
    public static void addProperties(ResourceLocation registryName, CropProperties prop) {
        LibReference.logger.info("Registering " + registryName + " with properties " + prop);
        cropProperties.put(registryName, prop);
    }
    
    public static Set<String> allOreDictCrops() {
        return cropPropMapPre.keySet();
    }
    
    public static CropProperties getProperties(ResourceLocation registryName) {
        return cropProperties.get(registryName);
    }
    
    public static void postProcess() {
        for (String s : cropPropMapPre.keySet()) {
            boolean isRegName = s.contains(":");
            if (isRegName) {
                addProperties(new ResourceLocation(s), cropPropMapPre.get(s));
            }
            else {
                for (String ore : OreDictionary.getOreNames()) {
                    if (ore.equals(s)) {
                        for (ItemStack stack : OreDictionary.getOres(ore)) {
                            addProperties(stack.getItem().getRegistryName(), cropPropMapPre.get(s));
                        }
                    }
                }
            }
        }
        cropPropMapPre.clear();
    }
    
    static {
        cropPropMapPre.put("seedTurnip", new CropProperties(CalendarHandler.EnumSeason.SPRING, 4, 2, false));
        cropPropMapPre.put("seedCabbage", new CropProperties(CalendarHandler.EnumSeason.SPRING, 7, 3, false));
        cropPropMapPre.put("seedPinkMelon", new CropProperties(CalendarHandler.EnumSeason.SPRING, 10, 1, false));
        cropPropMapPre.put("seedPinkTurnip", new CropProperties(CalendarHandler.EnumSeason.SPRING, 10, 2, false));
    }
}
