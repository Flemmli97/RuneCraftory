package com.flemmli97.runecraftory.api.items;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.entity.ai.attributes.RangedAttribute;

public class ItemStatAttributes extends RangedAttribute
{
	public static final Map<String, ItemStatAttributes> ATTRIBUTESTRINGMAP = new LinkedHashMap<String, ItemStatAttributes>();
	private static int id = 0;
	public static final ItemStatAttributes RFATTACK = new ItemStatAttributes("rf.attack", id++, 1, -99999, 99999);
	public static final ItemStatAttributes RFDEFENCE = new ItemStatAttributes("rf.defence", id++, 1, -99999, 99999);
	public static final ItemStatAttributes RFMAGICATT = new ItemStatAttributes("rf.magicAtt", id++, 1, -99999, 99999);
	public static final ItemStatAttributes RFMAGICDEF = new ItemStatAttributes("rf.magicDef", id++, 1, -99999, 99999);
	
	public static final ItemStatAttributes RFPARA = new ItemStatAttributes("rf.paralysis", id++, 0, 0, 100);
	public static final ItemStatAttributes RFPOISON = new ItemStatAttributes("rf.poison", id++, 0, 0, 100);
	public static final ItemStatAttributes RFSEAL = new ItemStatAttributes("rf.seal", id++, 0, 0, 100);
	public static final ItemStatAttributes RFSLEEP = new ItemStatAttributes("rf.sleep", id++, 0, 0, 100);
	public static final ItemStatAttributes RFFAT = new ItemStatAttributes("rf.fatigue", id++, 0, 0, 100);
	public static final ItemStatAttributes RFCOLD = new ItemStatAttributes("rf.cold", id++, 0, 0, 100);
	public static final ItemStatAttributes RFDIZ = new ItemStatAttributes("rf.diz", id++, 0, -1000, 1000);
	public static final ItemStatAttributes RFCRIT = new ItemStatAttributes("rf.crit", id++, 0, 0, 100);
	public static final ItemStatAttributes RFSTUN = new ItemStatAttributes("rf.stun", id++, 0, 0, 100);
	public static final ItemStatAttributes RFFAINT = new ItemStatAttributes("rf.faint", id++, 0, 0, 100);
	public static final ItemStatAttributes RFDRAIN = new ItemStatAttributes("rf.drain", id++, 0, 0, 100);
	public static final ItemStatAttributes RFKNOCK = new ItemStatAttributes("rf.knock", id++, 0, 0, 100);
	public static final ItemStatAttributes RFRESWATER = new ItemStatAttributes("rf.resWater", id++, 0, -100, 200);
	public static final ItemStatAttributes RFRESEARTH = new ItemStatAttributes("rf.resEarth", id++, 0, -100, 200);
	public static final ItemStatAttributes RFRESWIND = new ItemStatAttributes("rf.resWind", id++, 0, -100, 200);
	public static final ItemStatAttributes RFRESFIRE = new ItemStatAttributes("rf.resFire", id++, 0, -100, 200);
	public static final ItemStatAttributes RFRESDARK = new ItemStatAttributes("rf.resDark", id++, 0, -100, 200);
	public static final ItemStatAttributes RFRESLIGHT = new ItemStatAttributes("rf.resLight", id++, 0, -100, 200);
	public static final ItemStatAttributes RFRESLOVE = new ItemStatAttributes("rf.resLove", id++, 0, -100, 200);
	public static final ItemStatAttributes RFRESPOISON = new ItemStatAttributes("rf.resPoison", id++, 0, -100, 100);
	public static final ItemStatAttributes RFRESSLEEP = new ItemStatAttributes("rf.resSleep", id++, 0, -100, 100);
	public static final ItemStatAttributes RFRESFAT = new ItemStatAttributes("rf.resFat", id++, 0, -100, 100);
	public static final ItemStatAttributes RFRESCOLD = new ItemStatAttributes("rf.resCold", id++, 0, -100, 100);
	public static final ItemStatAttributes RFRESPARA = new ItemStatAttributes("rf.resPara", id++, 0, -100, 100);
	public static final ItemStatAttributes RFRESSEAL = new ItemStatAttributes("rf.resSeal", id++, 0, -100, 100);
	public static final ItemStatAttributes RFRESCRIT = new ItemStatAttributes("rf.resCrit", id++, 0, -100, 100);
	public static final ItemStatAttributes RFRESDIZ = new ItemStatAttributes("rf.resDiz", id++, 0, -100, 100);
	public static final ItemStatAttributes RFRESSTUN = new ItemStatAttributes("rf.resStun", id++, 0, -100, 100);
	public static final ItemStatAttributes RFRESFAINT = new ItemStatAttributes("rf.resFaint", id++, 0, -100, 100);
	public static final ItemStatAttributes RFRESDRAIN = new ItemStatAttributes("rf.resDrain", id++, 0, -100, 100);
	public static final ItemStatAttributes RFRLUCK = new ItemStatAttributes("rf.luck", id++, 0, 0, 200);
	
    private int order;
    
    public ItemStatAttributes(String name, int order, int baseValue, int minValue, int maxValue) {
        super(null, name, baseValue, minValue, maxValue);
        this.order = order;
    }
    
    public int orderNumber() 
    {
        return this.order;
    }
    
    static {
        for (final Field field : ItemStatAttributes.class.getFields()) {
            if (field.getType().isAssignableFrom(ItemStatAttributes.class)) {
                try {
                    final ItemStatAttributes att = (ItemStatAttributes)field.get(null);
                    ItemStatAttributes.ATTRIBUTESTRINGMAP.put(att.getName(), att);
                }
                catch (Exception e) {
                    LibReference.logger.error("Error initializing Attribute: " + field);
                }
            }
        }
    }
    
    public static class Sort implements Comparator<ItemStatAttributes>
    {
        @Override
        public int compare(final ItemStatAttributes o1, final ItemStatAttributes o2) {
            return Integer.compare(o1.order, o2.order);
        }
    }
}
