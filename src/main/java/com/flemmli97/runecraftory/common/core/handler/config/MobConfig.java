package com.flemmli97.runecraftory.common.core.handler.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.flemmli97.runecraftory.api.entities.EntityProperties;
import com.flemmli97.runecraftory.api.entities.IEntityBase;
import com.flemmli97.runecraftory.api.items.ItemProperties;
import com.flemmli97.runecraftory.api.items.ItemStatAttributes;
import com.flemmli97.runecraftory.api.mappings.EntityStatMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class MobConfig
{
    public static double gateDef;
    public static double gateMDef;
    public static double gateHealth;
    
    protected static void init(Configuration config) 
    {
        config.load();
        //A list of applicable keywords for entity attributes
        ArrayList<String> validStatValues = new ArrayList<String>();
        validStatValues.add(SharedMonsterAttributes.MAX_HEALTH.getName());
        validStatValues.addAll(ItemStatAttributes.ATTRIBUTESTRINGMAP.keySet());
        int i = 0;
        String values = Configuration.NEW_LINE + "<";
        for (String s : validStatValues) 
        {
            if (++i == 3) 
            {
                values = values + s + ">" + Configuration.NEW_LINE + "<";
                i = 0;
            }
            else 
            {
                values = values + s + "> <";
            }
        }
        values = values.substring(0, values.length() - 2);
        config.addCustomCategoryComment("conf", "Valid attribute names are:" + values);
        
        //Main config for mobs
        mobData(config);
        //Gate
        gateDef = ConfigHandler.getFloatConfig(config, "GateDef", "conf.runecraftory:gate", 5.0f, "Base health for gates");
        gateMDef = ConfigHandler.getFloatConfig(config, "GateMDef", "conf.runecraftory:gate", 5.0f, "Base defence for gates");
        gateHealth = ConfigHandler.getFloatConfig(config, "GateHealth", "conf.runecraftory:gate", 100.0f, "Base magic defence for gates");
        config.save();
    }
    
    @SuppressWarnings("unchecked")
	private static void mobData(Configuration config) {
        for (EntityEntry entry : ForgeRegistries.ENTITIES.getValues()) 
        {
            if (IEntityBase.class.isAssignableFrom(entry.getEntityClass())) 
            {
                EntityProperties old = EntityStatMap.getDefaultStats((Class<? extends IEntityBase>) entry.getEntityClass());
                if (old != null) 
                {
	                List<String> stats = Lists.newArrayList();
	                for (IAttribute att : old.getBaseValues().keySet()) {
	                    stats.add(att.getName() + ";" + old.getBaseValues().get(att));
	                }
	                List<String> drops = Lists.newArrayList();
	                for (ItemProperties item : old.drops().keySet()) {
	                    drops.add(item.toString() + "," + old.drops().get(item));
	                }
	                List<String> daily = Lists.newArrayList();
	                for (ItemProperties item : old.dailyDrops().keySet()) {
	                    daily.add(item.toString() + "," + old.dailyDrops().get(item));
	                }
	                List<String> taming = Lists.newArrayList();
	                for (ItemProperties item : old.getTamingItem()) {
	                    taming.add(item.toString());
	                }
	                //Read from config
	                String configCat = "conf." + entry.getRegistryName().toString();
	                String[] entityStats = config.getStringList("Entity Stats", configCat, stats.toArray(new String[0]), "Entity stats at level 5");
	                String[] entityDrops = config.getStringList("Drops", configCat, drops.toArray(new String[0]), "Syntax is \"item,meta,dropChance\"");
	                String[] entityDaily = config.getStringList("Daily Products", configCat, daily.toArray(new String[0]), "Syntax is \"item,meta,hearts. Where hearts is the friendship minimum value for that drop\"");
	                String[] tamingItemsString = config.getStringList("Taming Items", configCat, taming.toArray(new String[0]), "Syntax is \"item,meta\"");
	                float tamingChance = config.getFloat("Taming chance", configCat, old.tamingChance(), 0.0f, 1.0f, "Base taming chance.");
	                int baseXP = ConfigHandler.getIntConfig(config, "Base xp", configCat, old.getXp(), "Base xp when defeating this entity");
	                int baseMoney = ConfigHandler.getIntConfig(config, "Base money", configCat, old.getMoney(), "Base money when defeating this entity");
	                boolean canFly = config.getBoolean("Flying", configCat, old.flying(), "If this entity can fly");
	                boolean ridable = config.getBoolean("Ridable", configCat, old.ridable(), "If this entity is ridable");
	                
	                //Parsing config stuff
	                Map<IAttribute, Double> statMap = Maps.newHashMap();	           
	                for (String s : entityStats) {
	                    int breakPoint = s.indexOf(";");
	                    String attributeName = s.substring(0, breakPoint);
	                    Double value = Double.parseDouble(s.substring(breakPoint + 1));
	                    if (attributeName.equals(SharedMonsterAttributes.MAX_HEALTH.getName())) {
	                        statMap.put(SharedMonsterAttributes.MAX_HEALTH, value);
	                    }
	                    else {
	                        statMap.put((IAttribute)ItemStatAttributes.ATTRIBUTESTRINGMAP.get(attributeName), value);
	                    }
	                }
	                Map<ItemProperties, Float> dropMap = Maps.newHashMap();
	                for (String s : entityDrops) {
	                    String[] item = s.split(",");
	                    String itemName = item[0];
	                    String meta = item[1];
	                    String dropChance = item[2];
	                    dropMap.put(new ItemProperties(itemName, Integer.parseInt(meta)), Float.parseFloat(dropChance));
	                }
	                Map<ItemProperties, Integer> dailyMap = Maps.newHashMap();
	                for (String s : entityDaily) {
	                    String[] item = s.split(",");
	                    String itemName = item[0];
	                    String meta = item[1];
	                    String happiness = item[2];
	                    dailyMap.put(new ItemProperties(itemName, Integer.parseInt(meta)), Integer.parseInt(happiness));
	                }
	                List<ItemProperties> fromString = Lists.newArrayList();
	                for (String s : tamingItemsString) {
	                    String[] item = s.split(",");
	                    String itemName = item[0];
	                    String meta = item[1];
	                    fromString.add(new ItemProperties(itemName, Integer.parseInt(meta)));
	                }
	                EntityStatMap.setDefaultStats((Class<? extends IEntityBase>) entry.getEntityClass(), 
	                		new EntityProperties(statMap, dropMap, baseXP, baseMoney, tamingChance, 
	                				fromString.toArray(new ItemProperties[0]), dailyMap, ridable, canFly));
                }
            }
        }
    }
}
