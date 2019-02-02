package com.flemmli97.runecraftory.api.mappings;

import java.util.Map;

import com.flemmli97.runecraftory.api.items.ItemStat;
import com.flemmli97.tenshilib.api.config.SimpleItemStackWrapper;
import com.google.common.collect.Maps;

import net.minecraft.item.ItemStack;

public class ItemStatMap
{
    private static final Map<SimpleItemStackWrapper, ItemStat> stats = Maps.newHashMap();;
    private static int count = 0;
    
    public static void add(SimpleItemStackWrapper item, ItemStat stat) {
        if(stats.put(item, stat)==null)
        	count+=1;
    }
    
    public static ItemStat get(ItemStack stack) {
        ItemStat statNoMeta = stats.get(new SimpleItemStackWrapper(stack).setIgnoreAmount().setIgnoreMeta());
        if (statNoMeta != null) {
            return statNoMeta;
        }
        return stats.get(new SimpleItemStackWrapper(stack).setIgnoreAmount());
    }
    
    public static int configuredItems()
    {
    	return count;
    }
}
