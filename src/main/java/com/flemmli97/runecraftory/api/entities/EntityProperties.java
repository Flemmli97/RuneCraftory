package com.flemmli97.runecraftory.api.entities;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.flemmli97.runecraftory.api.items.ItemStatAttributes;
import com.flemmli97.runecraftory.common.utils.ItemUtils;
import com.flemmli97.runecraftory.common.utils.MapWrapper;
import com.flemmli97.tenshilib.api.config.IConfigSerializable;
import com.flemmli97.tenshilib.api.config.SimpleItemStackWrapper;
import com.flemmli97.tenshilib.common.config.ConfigUtils;
import com.flemmli97.tenshilib.common.javahelper.ArrayUtils;
import com.flemmli97.tenshilib.common.javahelper.ObjectConverter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraftforge.common.config.Configuration;

public class EntityProperties implements IConfigSerializable<EntityProperties>
{
    private Map<IAttribute, Double> baseValues;
    private Map<SimpleItemStackWrapper, Float> drops;
    private int xp;
    private int money;
    private float taming;
    private SimpleItemStackWrapper[] tamingItem;
    private Map<SimpleItemStackWrapper, Integer> daily;
    private boolean ridable;
    private boolean flying;
    
    public EntityProperties(Map<IAttribute, Double> baseValues, Map<SimpleItemStackWrapper, Float> drops, int xp, int money, float tamingChance, SimpleItemStackWrapper[] tamingItem, Map<SimpleItemStackWrapper, Integer> dailyDrops, boolean ridable, boolean flying) {
        this.baseValues = MapWrapper.sort(baseValues, new ItemStatAttributes.Sort());
        this.drops = drops;
        this.xp = xp;
        this.money = money;
        this.taming = tamingChance;
        this.tamingItem = tamingItem;
        this.daily = dailyDrops;
        this.ridable = ridable;
        this.flying = flying;
    }
    
    public Map<IAttribute, Double> getBaseValues() {
        return ImmutableMap.copyOf(this.baseValues);
    }
    
    public Map<SimpleItemStackWrapper, Integer> dailyDrops() {
        return this.daily;
    }
    
    public boolean ridable() {
        return this.ridable;
    }
    
    public boolean flying() {
        return this.flying;
    }
    
    public Map<SimpleItemStackWrapper, Float> drops() {
        return this.drops;
    }
    
    public int getXp() {
        return this.xp;
    }
    
    public int getMoney() {
        return this.money;
    }
    
    public float tamingChance() {
        return this.taming;
    }
    
    public SimpleItemStackWrapper[] getTamingItem() {
        return this.tamingItem;
    }

	@Override
	public EntityProperties config(Configuration config, String configCategory) {
		List<String> stats = Lists.newArrayList();
        this.baseValues.entrySet().forEach(entry->{
            stats.add(entry.getKey().getName() + ";" + entry.getValue());
        });
        List<String> drops = Lists.newArrayList();
        for (Entry<SimpleItemStackWrapper, Float> mapEntry : this.drops.entrySet()) {
            drops.add(mapEntry.getKey().toString() + ";" + mapEntry.getValue());
        }
        List<String> daily = Lists.newArrayList();
        for (Entry<SimpleItemStackWrapper, Integer> mapEntry : this.dailyDrops().entrySet()) {
            daily.add(mapEntry.getKey().toString() + ";" + mapEntry.getValue());
        }
        
        //Read from config
        this.baseValues.clear();
        for(String s : config.getStringList("Entity Stats", configCategory, stats.toArray(new String[0]), "Entity stats at level 5. Syntax is: \"attribute;value\""))
    	{
        	String[] sub = s.split(";");
            this.baseValues.put(ItemUtils.getAttFromName(sub[0]), Double.parseDouble(sub[1]));
    	}
        this.drops.clear();
        for(String s : config.getStringList("Drops", configCategory, drops.toArray(new String[0]), "Syntax is \"item(,meta)(,amount);dropChance\""))
        {
        	String[] sub = s.split(";");
        	this.drops.put(new SimpleItemStackWrapper(sub[0]), Float.parseFloat(sub[1]));
        }
        this.xp = ConfigUtils.getIntConfig(config, "Base xp", configCategory, this.xp, 0, "Base xp when defeating this entity");
        this.money = ConfigUtils.getIntConfig(config, "Base money", configCategory, this.money, 0, "Base money when defeating this entity");
        this.taming = config.getFloat("Taming chance", configCategory, this.taming, 0.0f, 1.0f, "Base taming chance.");
        this.tamingItem = ArrayUtils.arrayConverter(config.getStringList("Taming Items", configCategory, ArrayUtils.arrayToStringArr(this.tamingItem), "Syntax is \"item(,meta)\""), new ObjectConverter<String,SimpleItemStackWrapper>(){
			@Override
			public SimpleItemStackWrapper convertFrom(String s) {return new SimpleItemStackWrapper(s);}},SimpleItemStackWrapper.class, false);
        this.daily.clear();
        for(String s : config.getStringList("Daily Products", configCategory, daily.toArray(new String[0]), "Syntax is \"item(,meta);hearts. Where hearts is the friendship minimum value for that drop\""))
    	{
        	String[] sub = s.split(";");
        	this.daily.put(new SimpleItemStackWrapper(sub[0]), Integer.parseInt(sub[1]));
    	}
        this.ridable = config.getBoolean("Ridable", configCategory, this.ridable, "If this entity is ridable");        
        this.flying = config.getBoolean("Flying", configCategory, this.flying, "If this entity can fly");
        
		return this;
		//EntityStatMap.setDefaultStats((Class<? extends IEntityBase>) entry.getEntityClass(), 
		//new EntityProperties(statMap, dropMap, baseXP, baseMoney, tamingChance, 
		//		fromString.toArray(new ItemProperties[0]), dailyMap, ridable, canFly));
	}
}
