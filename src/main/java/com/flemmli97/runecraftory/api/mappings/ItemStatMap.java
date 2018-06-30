package com.flemmli97.runecraftory.api.mappings;

import java.util.Map;

import com.flemmli97.runecraftory.api.items.ItemProperties;
import com.flemmli97.runecraftory.api.items.ItemStat;
import com.flemmli97.runecraftory.api.items.ItemStatAttributes;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.utils.MapWrapper;
import com.google.common.collect.Maps;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemStatMap
{
    private static final Map<ItemProperties, ItemStat> stats = Maps.newHashMap();;
    private static final Map<String, ItemStat> oreDictPrice = Maps.newHashMap();;
    private static final Map<ItemProperties, ItemStat> statsPre = Maps.newHashMap();;
    private static int count = 0;
    public static void add(ItemProperties item, ItemStat stat) {
        if(ItemStatMap.stats.put(item, stat)==null)
        	count+=1;
    }
    
    public static ItemStat get(ItemStack stack) {
        ItemStat statNoMeta = ItemStatMap.stats.get(new ItemProperties(stack));
        if (statNoMeta != null) {
            return statNoMeta;
        }
        return ItemStatMap.stats.get(new ItemProperties(stack, false));
    }
    
    public static void preAdd(String ore, ItemStat price) {
        ItemStatMap.oreDictPrice.put(ore, price);
    }
    
    public static void preAdd(ItemProperties item, ItemStat stat) {
        ItemStatMap.statsPre.put(item, stat);
    }
    
    public static void postProcess() {
        LibReference.logger.debug("Configuring stats for items");
        for (String s : ItemStatMap.oreDictPrice.keySet()) {
            for (String ore : OreDictionary.getOreNames()) {
                if (ore.equals(s)) {
                    for (ItemStack stack : OreDictionary.getOres(ore)) {
                        add(new ItemProperties(stack, false), ItemStatMap.oreDictPrice.get(s));
                    }
                }
            }
        }
        for (ItemProperties prop : ItemStatMap.statsPre.keySet()) {
            add(prop, ItemStatMap.statsPre.get(prop));
        }
        ItemStatMap.oreDictPrice.clear();
        ItemStatMap.statsPre.clear();
        LibReference.logger.debug("Configured stats for " +  count + " items");
    }
    
    static {
        preAdd(new ItemProperties(new ItemStack(ModItems.cheapBracelet)), new ItemStat(100, 5, -1, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICDEF, 5)));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.sickleScrap)), new ItemStat(300, 17, -1, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 2)));
        preAdd(new ItemProperties(new ItemStack(ModItems.sickleIron)), new ItemStat(3000, 170, -1, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 20)));
        preAdd(new ItemProperties(new ItemStack(ModItems.sickleSilver)), new ItemStat(2500, 150, -1, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 60)));
        preAdd(new ItemProperties(new ItemStack(ModItems.sickleGold)), new ItemStat(0, 600, -1, EnumElement.WIND, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 120)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 20)));
        preAdd(new ItemProperties(new ItemStack(ModItems.sicklePlatinum)), new ItemStat(0, 2500, -1, EnumElement.WIND, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 238)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 80)
        			.mapWrapperAdd(ItemStatAttributes.RFDIZ, 5)
        			.mapWrapperAdd(ItemStatAttributes.RFCRIT, 20)
        			.mapWrapperAdd(ItemStatAttributes.RFSTUN, 30)));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.hammerScrap)), new ItemStat(1500, 17, -1, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 3)
        			.mapWrapperAdd(ItemStatAttributes.RFDIZ, 5)
        			.mapWrapperAdd(ItemStatAttributes.RFCRIT, -5)
        			.mapWrapperAdd(ItemStatAttributes.RFSTUN, 20)));
        preAdd(new ItemProperties(new ItemStack(ModItems.hammerIron)), new ItemStat(3000, 170, -1, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 25)
        			.mapWrapperAdd(ItemStatAttributes.RFDIZ, 5)
        			.mapWrapperAdd(ItemStatAttributes.RFCRIT, -5)
        			.mapWrapperAdd(ItemStatAttributes.RFSTUN, 20)));
        preAdd(new ItemProperties(new ItemStack(ModItems.hammerSilver)), new ItemStat(2500, 150, -1, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 68)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 5)
        			.mapWrapperAdd(ItemStatAttributes.RFDIZ, 5)
        			.mapWrapperAdd(ItemStatAttributes.RFCRIT, -5)
        			.mapWrapperAdd(ItemStatAttributes.RFSTUN, 20)));
        preAdd(new ItemProperties(new ItemStack(ModItems.hammerGold)), new ItemStat(0, 600, -1, EnumElement.NONE,
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 106)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 10)
        			.mapWrapperAdd(ItemStatAttributes.RFDIZ, 5)
        			.mapWrapperAdd(ItemStatAttributes.RFCRIT, -5)
        			.mapWrapperAdd(ItemStatAttributes.RFSTUN, 20)));
        preAdd(new ItemProperties(new ItemStack(ModItems.hammerPlatinum)), new ItemStat(0, 2500, -1, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 207)
        			.mapWrapperAdd(ItemStatAttributes.RFDEFENCE, 50)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 15)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICDEF, 30)
        			.mapWrapperAdd(ItemStatAttributes.RFDIZ, 18)
        			.mapWrapperAdd(ItemStatAttributes.RFCRIT, -5)
        			.mapWrapperAdd(ItemStatAttributes.RFSTUN, 75)));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.hoeScrap)), new ItemStat(150, 17, -1, EnumElement.EARTH, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 1)));
        preAdd(new ItemProperties(new ItemStack(ModItems.hoeIron)), new ItemStat(3000, 170, -1, EnumElement.EARTH, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 18)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 5)));
        preAdd(new ItemProperties(new ItemStack(ModItems.hoeSilver)), new ItemStat(2500, 150, -1, EnumElement.EARTH, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 30)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 10)));
        preAdd(new ItemProperties(new ItemStack(ModItems.hoeGold)), new ItemStat(0, 600, -1, EnumElement.EARTH, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 64)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 50)));
        preAdd(new ItemProperties(new ItemStack(ModItems.hoePlatinum)), new ItemStat(0, 2500, -1, EnumElement.EARTH, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 170)
        			.mapWrapperAdd(ItemStatAttributes.RFDEFENCE, 70)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 60)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICDEF, 30)));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.wateringCanScrap)), new ItemStat(150, 17, -1, EnumElement.WATER, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 1)));
        preAdd(new ItemProperties(new ItemStack(ModItems.wateringCanIron)), new ItemStat(3000, 170, -1, EnumElement.WATER, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 18)));
        preAdd(new ItemProperties(new ItemStack(ModItems.wateringCanSilver)), new ItemStat(2500, 150, -1, EnumElement.WATER, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 35)));
        preAdd(new ItemProperties(new ItemStack(ModItems.wateringCanGold)), new ItemStat(0, 600, -1, EnumElement.WATER, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 60)));
        preAdd(new ItemProperties(new ItemStack(ModItems.wateringCanPlatinum)), new ItemStat(0, 2500, -1, EnumElement.WATER, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFDEFENCE, 40)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICATT, 150)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICDEF, 80)
        			.mapWrapperAdd(ItemStatAttributes.RFDIZ, 5)
        			.mapWrapperAdd(ItemStatAttributes.RFCRIT, 15)
        			.mapWrapperAdd(ItemStatAttributes.RFSTUN, 80)));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.inspector)), new ItemStat(2500, 700, -1, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.broadSword)), new ItemStat(90, 23, -1, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 5)
        			.mapWrapperAdd(ItemStatAttributes.RFDIZ, 6)));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.claymore)), new ItemStat(160, 23, -1, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 12)
        			.mapWrapperAdd(ItemStatAttributes.RFDIZ, 12)));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.spear)), new ItemStat(200, 23, -1, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 14)
        			.mapWrapperAdd(ItemStatAttributes.RFDEFENCE, 1)
        			.mapWrapperAdd(ItemStatAttributes.RFDIZ, 8)));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.battleAxe)), new ItemStat(1380, 42, -1, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 38)
        			.mapWrapperAdd(ItemStatAttributes.RFDIZ, 2)
        			.mapWrapperAdd(ItemStatAttributes.RFCRIT, 5)));
        preAdd(new ItemProperties(new ItemStack(ModItems.battleScythe)), new ItemStat(6590, 140, -1, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 70)
        			.mapWrapperAdd(ItemStatAttributes.RFDIZ, 2)
        			.mapWrapperAdd(ItemStatAttributes.RFCRIT, 15)));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.battleHammer)), new ItemStat(340, 42, -1, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 29)
        			.mapWrapperAdd(ItemStatAttributes.RFDIZ, 35)
        			.mapWrapperAdd(ItemStatAttributes.RFSTUN, 10)));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.leatherGlove)), new ItemStat(290, 13, -1, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 24)
        			.mapWrapperAdd(ItemStatAttributes.RFDEFENCE, 3)
        			.mapWrapperAdd(ItemStatAttributes.RFDIZ, 3)));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.dagger)), new ItemStat(1310, 45, -1, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFATTACK, 28)
        			.mapWrapperAdd(ItemStatAttributes.RFDIZ, 3)));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.bones, 1, 0), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.bones, 1, 1), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.bones, 1, 2), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.bones, 1, 3), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.bones, 1, 4), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.bones, 1, 5), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.cloth, 1, 0), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.cloth, 1, 1), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.cloth, 1, 2), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.cloth, 1, 3), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.cloth, 1, 4), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.cloth, 1, 5), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.cloth, 1, 6), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.cloth, 1, 7), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.cloth, 1, 8), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.cloth, 1, 9), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.crystal, 1, 0), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.crystal, 1, 1), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.crystal, 1, 2), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.crystal, 1, 3), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.crystal, 1, 4), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.crystal, 1, 5), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.crystal, 1, 6), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.crystal, 1, 7), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.crystal, 1, 8), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.crystal, 1, 9), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.crystal, 1, 10), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.crystal, 1, 11), false), new ItemStat(1, 1, 5, EnumElement.NONE,
        		new MapWrapper<ItemStatAttributes, Integer>()));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.feathers, 1, 0), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.feathers, 1, 1), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.feathers, 1, 2), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.feathers, 1, 3), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.feathers, 1, 4), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.furs, 1, 0), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.furs, 1, 1), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.furs, 1, 2), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.furs, 1, 3), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.furs, 1, 4), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.furs, 1, 5), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.furs, 1, 6), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.furs, 1, 7), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.furs, 1, 8), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.furs, 1, 9), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.furs, 1, 10), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.furs, 1, 11), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.furs, 1, 12), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.jewel, 1, 0), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.jewel, 1, 1), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.jewel, 1, 2), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.jewel, 1, 3), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.jewel, 1, 4), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.jewel, 1, 5), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.jewel, 1, 6), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.jewel, 1, 7), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.jewel, 1, 8), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.jewel, 1, 9), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.jewel, 1, 10), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.liquids, 1, 0), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.liquids, 1, 1), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.liquids, 1, 2), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.liquids, 1, 3), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.mineral, 1, 5), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFDEFENCE, 95)));
        preAdd(new ItemProperties(new ItemStack(ModItems.mineral, 1, 6), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFDEFENCE, 160)
        			.mapWrapperAdd(ItemStatAttributes.RFMAGICDEF, 120)));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.scrap, 1, 0), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.scrap, 1, 1), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.powders, 1, 0), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.powders, 1, 1), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.powders, 1, 2), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.powders, 1, 3), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.powders, 1, 4), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.powders, 1, 5), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.powders, 1, 6), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.powders, 1, 7), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.powders, 1, 8), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.powders, 1, 9), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.powders, 1, 10), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.powders, 1, 11), false), new ItemStat(1, 1, 5, EnumElement.NONE,
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.powders, 1, 12), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.powders, 1, 13), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.powders, 1, 14), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.sticks, 1, 0), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.sticks, 1, 1), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.sticks, 1, 2), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.sticks, 1, 3), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.sticks, 1, 4), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.sticks, 1, 5), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.sticks, 1, 6), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.sticks, 1, 7), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.stones, 1, 0), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.stones, 1, 1), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.stones, 1, 2), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.stones, 1, 3), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.stones, 1, 4), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.stones, 1, 5), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.strings, 1, 0), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.strings, 1, 1), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.strings, 1, 2), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.strings, 1, 3), false), new ItemStat(1, 1, 5, EnumElement.NONE,
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.strings, 1, 4), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.strings, 1, 5), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.strings, 1, 6), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.strings, 1, 7), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.strings, 1, 8), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        preAdd(new ItemProperties(new ItemStack(ModItems.strings, 1, 9), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        
        preAdd(new ItemProperties(new ItemStack(ModItems.fireBallSmall, 1, 0), false), new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()));
        
        preAdd("ingotIron", new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFDEFENCE, 1)));
        preAdd("ingotBronze", new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFDEFENCE, 4)));
        preAdd("ingotSilver", new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFDEFENCE, 7)));
        preAdd("ingotGold", new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFDEFENCE, 10)));
        preAdd("ingotPlatinum", new ItemStat(1, 1, 5, EnumElement.NONE, 
        		new MapWrapper<ItemStatAttributes, Integer>()
        			.mapWrapperAdd(ItemStatAttributes.RFDEFENCE, 25)));
    }
}
