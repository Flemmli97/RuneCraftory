package com.flemmli97.runecraftory.api.mappings;

import java.util.Map;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.api.items.FoodProperties;
import com.flemmli97.tenshilib.api.config.SimpleItemStackWrapper;
import com.google.common.collect.Maps;

import net.minecraft.item.ItemStack;

public class ItemFoodMap {

	private static final Map<SimpleItemStackWrapper, FoodProperties> stats = Maps.newHashMap();

    private static int count = 0;
        
    public static void add(SimpleItemStackWrapper item, FoodProperties stat) {
        if(stats.put(item, stat)==null)
        	count+=1;
    }
    
    @Nullable
    public static FoodProperties get(ItemStack stack) {
        FoodProperties prop = stats.get(new SimpleItemStackWrapper(stack).setIgnoreAmount().setIgnoreMeta());
        if (prop != null) {
            return prop;
        }
        return stats.get(new SimpleItemStackWrapper(stack).setIgnoreAmount());
    }
    
    public static int configuredFood()
    {
    	return count;
    }
}
