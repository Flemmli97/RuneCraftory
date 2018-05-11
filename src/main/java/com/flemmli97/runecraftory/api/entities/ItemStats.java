package com.flemmli97.runecraftory.api.entities;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.flemmli97.runecraftory.RuneCraftory;

import net.minecraft.entity.ai.attributes.RangedAttribute;

public class ItemStats extends RangedAttribute{

	public static final Map<String, ItemStats> ATTRIBUTESTRINGMAP = new LinkedHashMap<String, ItemStats>();
	
    public static final ItemStats RFATTACK = new ItemStats("rf.attack", 0, 1, -99999, 99999); 
    public static final ItemStats RFDEFENCE = new ItemStats("rf.defence", 1, 1, -99999, 99999); 
    public static final ItemStats RFMAGICATT = new ItemStats("rf.magicAtt", 2, 1, -99999, 99999); 
    public static final ItemStats RFMAGICDEF = new ItemStats("rf.magicDef", 3, 1, -99999, 99999); 

    //=====Percents
    public static final ItemStats RFPARA = new ItemStats("rf.paralysis", 4, 0, 0, 100); 
    public static final ItemStats RFPOISON = new ItemStats("rf.poison", 5, 0, 0, 100);
    public static final ItemStats RFSEAL = new ItemStats("rf.seal", 6, 0, 0, 100); 
    public static final ItemStats RFSLEEP = new ItemStats("rf.sleep", 7, 0, 0, 100); 
    public static final ItemStats RFFAT = new ItemStats("rf.fatigue", 8, 0, 0, 100); 
    public static final ItemStats RFCOLD = new ItemStats("rf.cold", 9, 0, 0, 100); 
    
    public static final ItemStats RFDIZ = new ItemStats("rf.diz", 10, 0, -1000, 1000); 
    public static final ItemStats RFCRIT = new ItemStats("rf.crit", 11, 0, 0, 100); 
    public static final ItemStats RFSTUN = new ItemStats("rf.stun", 12, 0, 0, 100); 
    public static final ItemStats RFFAINT = new ItemStats("rf.faint", 13, 0, 0, 100); 
    public static final ItemStats RFDRAIN = new ItemStats("rf.drain", 14, 0, 0, 100); 
    public static final ItemStats RFKNOCK = new ItemStats("rf.knock", 15, 0, 0, 100); 

    public static final ItemStats RFRESWATER = new ItemStats("rf.resWater", 16, 0, -100, 200); 
    public static final ItemStats RFRESEARTH = new ItemStats("rf.resEarth", 17, 0, -100, 200); 
    public static final ItemStats RFRESWIND = new ItemStats("rf.resWind", 18, 0, -100, 200); 
    public static final ItemStats RFRESFIRE = new ItemStats("rf.resFire", 19, 0, -100, 200); 
    public static final ItemStats RFRESDARK = new ItemStats("rf.resDark", 20, 0, -100, 200); 
    public static final ItemStats RFRESLIGHT = new ItemStats("rf.resLight", 21, 0, -100, 200); 
    public static final ItemStats RFRESLOVE = new ItemStats("rf.resLove", 22, 0, -100, 200); 
    public static final ItemStats RFRESPOISON = new ItemStats("rf.resPoison", 23, 0, -100, 100); 
    public static final ItemStats RFRESSLEEP = new ItemStats("rf.resSleep", 24, 0, -100, 100); 
    public static final ItemStats RFRESFAT = new ItemStats("rf.resFat", 25, 0, -100, 100); 
    public static final ItemStats RFRESCOLD = new ItemStats("rf.resCold", 26, 0, -100, 100); 
    public static final ItemStats RFRESPARA = new ItemStats("rf.resPara", 27, 0, -100, 100); 
    public static final ItemStats RFRESSEAL = new ItemStats("rf.resSeal", 28, 0, -100, 100); 
    public static final ItemStats RFRESCRIT = new ItemStats("rf.resCrit", 29, 0, -100, 100); 
    public static final ItemStats RFRESDIZ = new ItemStats("rf.resDiz", 30, 0, -100, 100); 
    public static final ItemStats RFRESSTUN = new ItemStats("rf.resStun", 31, 0, -100, 100); 
    public static final ItemStats RFRESFAINT = new ItemStats("rf.resFaint", 32, 0, -100, 100); 
    public static final ItemStats RFRESDRAIN = new ItemStats("rf.resDrain", 33, 0, -100, 100);
    
    public static final ItemStats RFRLUCK = new ItemStats("rf.luck",20, 0, 0, 200);

    private int order;

    public ItemStats(String name, int order, int baseValue, int minValue, int maxValue)
    {
    		super(null, name, baseValue, minValue, maxValue);
    		this.order=order;
    }
    
    public int orderNumber()
    {
    		return this.order;
    }
    
    static
    {
    		for(Field field : ItemStats.class.getFields())
    		{
    			if(field.getType().isAssignableFrom(ItemStats.class))
    			{
    				try {
    						ItemStats att = (ItemStats) field.get(null);
						ATTRIBUTESTRINGMAP.put(att.getName(), att);
					} catch (Exception e) {
						RuneCraftory.logger.error("Error initializing Attribute: " + field);
					}
    			}
    		}
    }
	
	public static class Sort implements Comparator<ItemStats>
	{
		@Override
		public int compare(ItemStats o1, ItemStats o2) {
			return Integer.compare(o1.order, o2.order);
		}	
	}
}
