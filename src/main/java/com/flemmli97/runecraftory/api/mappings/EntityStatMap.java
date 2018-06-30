package com.flemmli97.runecraftory.api.mappings;

import java.util.Map;

import com.flemmli97.runecraftory.api.entities.EntityProperties;
import com.flemmli97.runecraftory.api.entities.IEntityBase;
import com.flemmli97.runecraftory.api.items.ItemProperties;
import com.flemmli97.runecraftory.api.items.ItemStatAttributes;
import com.flemmli97.runecraftory.common.entity.monster.EntityAnt;
import com.flemmli97.runecraftory.common.entity.monster.EntityBeetle;
import com.flemmli97.runecraftory.common.entity.monster.EntityOrc;
import com.flemmli97.runecraftory.common.entity.monster.EntityWooly;
import com.flemmli97.runecraftory.common.entity.monster.boss.EntityAmbrosia;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.utils.MapWrapper;
import com.google.common.collect.Maps;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.init.Blocks;

public class EntityStatMap
{
    private static final Map<Class<? extends IEntityBase>, EntityProperties> entityStatsMap = Maps.newHashMap();
    
    public static void setDefaultStats(Class<? extends IEntityBase> clss, EntityProperties props) {
        entityStatsMap.put(clss, props);
    }
    
    public static EntityProperties getDefaultStats(Class<? extends IEntityBase> clss) {
        return entityStatsMap.get(clss);
    }
    
    static {
        entityStatsMap.put(EntityWooly.class, new EntityProperties(
        		new MapWrapper<IAttribute, Double>()
        			.mapWrapperAdd(SharedMonsterAttributes.MAX_HEALTH, 166.0)
        			.mapWrapperAdd((IAttribute)ItemStatAttributes.RFATTACK, 14.0)
        			.mapWrapperAdd((IAttribute)ItemStatAttributes.RFDEFENCE, 9.0)
        			.mapWrapperAdd((IAttribute)ItemStatAttributes.RFMAGICATT, 12.0)
        			.mapWrapperAdd((IAttribute)ItemStatAttributes.RFMAGICDEF, 9.0), 
        		new MapWrapper<ItemProperties, Float>()
        			.mapWrapperAdd(new ItemProperties(ModItems.furs.getRegistryName().toString(), 0), 0.5f),
        		3, 1, 0.9f,
        		new ItemProperties[] { new ItemProperties(ModItems.furs.getRegistryName().toString(), 0),
        				new ItemProperties(ModItems.furs.getRegistryName().toString(), 1), 
        				new ItemProperties(ModItems.furs.getRegistryName().toString(), 2), 
        				new ItemProperties(ModItems.furs.getRegistryName().toString(), 4), 
        				new ItemProperties(Blocks.WOOL.getRegistryName().toString(), -1) }, 
        		new MapWrapper<ItemProperties, Integer>()
        			.mapWrapperAdd(new ItemProperties(ModItems.furs.getRegistryName().toString(), 0), 0)
        			.mapWrapperAdd(new ItemProperties(ModItems.furs.getRegistryName().toString(), 1), 5)
        			.mapWrapperAdd(new ItemProperties(ModItems.furs.getRegistryName().toString(), 2), 10), 
        		true, false));
        
        entityStatsMap.put(EntityAnt.class, new EntityProperties(
        		new MapWrapper<IAttribute, Double>()
        			.mapWrapperAdd(SharedMonsterAttributes.MAX_HEALTH, 112.0)
        			.mapWrapperAdd((IAttribute)ItemStatAttributes.RFATTACK, 13.5)
        			.mapWrapperAdd((IAttribute)ItemStatAttributes.RFDEFENCE, 10.5)
        			.mapWrapperAdd((IAttribute)ItemStatAttributes.RFMAGICATT, 9.0)
        			.mapWrapperAdd((IAttribute)ItemStatAttributes.RFMAGICDEF, 9.2),
        		new MapWrapper<ItemProperties, Float>()
        			.mapWrapperAdd(new ItemProperties(ModItems.cloth.getRegistryName().toString(), 7), 0.4f)
        			.mapWrapperAdd(new ItemProperties(ModItems.cloth.getRegistryName().toString(), 8), 0.1f),
        		2, 1, 0.75f, 
        		new ItemProperties[] { new ItemProperties(ModItems.cloth.getRegistryName().toString(), 7) }, 
        		new MapWrapper<ItemProperties, Integer>()
        			.mapWrapperAdd(new ItemProperties(ModItems.cloth.getRegistryName().toString(), 7), 0),
        		false, false));
        
        entityStatsMap.put(EntityBeetle.class, new EntityProperties(
        		new MapWrapper<IAttribute, Double>()
        			.mapWrapperAdd(SharedMonsterAttributes.MAX_HEALTH, 125.0)
        			.mapWrapperAdd((IAttribute)ItemStatAttributes.RFATTACK, 18.5)
        			.mapWrapperAdd((IAttribute)ItemStatAttributes.RFDEFENCE, 7.0)
        			.mapWrapperAdd((IAttribute)ItemStatAttributes.RFMAGICATT, 10.0)
        			.mapWrapperAdd((IAttribute)ItemStatAttributes.RFMAGICDEF, 6.0),
        		new MapWrapper<ItemProperties, Float>()
        			.mapWrapperAdd(new ItemProperties(ModItems.sticks.getRegistryName().toString(), 2), 0.6f)
        			.mapWrapperAdd(new ItemProperties(ModItems.cloth.getRegistryName().toString(), 7), 0.4f)
        			.mapWrapperAdd(new ItemProperties(ModItems.cloth.getRegistryName().toString(), 8), 0.1f),
        		5, 1, 0.65f, 
        		new ItemProperties[] { new ItemProperties(ModItems.sticks.getRegistryName().toString(), 0) }, 
        		new MapWrapper<ItemProperties, Integer>()
        			.mapWrapperAdd(new ItemProperties(ModItems.sticks.getRegistryName().toString(), 0), 0),
        		true, false));
        
        entityStatsMap.put(EntityOrc.class, new EntityProperties(
        		new MapWrapper<IAttribute, Double>()
        			.mapWrapperAdd(SharedMonsterAttributes.MAX_HEALTH, 145.0)
        			.mapWrapperAdd((IAttribute)ItemStatAttributes.RFATTACK, 15.5)
        			.mapWrapperAdd((IAttribute)ItemStatAttributes.RFDEFENCE, 10.2)
        			.mapWrapperAdd((IAttribute)ItemStatAttributes.RFMAGICATT, 11.0)
        			.mapWrapperAdd((IAttribute)ItemStatAttributes.RFMAGICDEF, 10.8),
        		new MapWrapper<ItemProperties, Float>()
        			.mapWrapperAdd(new ItemProperties(ModItems.cloth.getRegistryName().toString(), 0), 0.5f)
        			.mapWrapperAdd(new ItemProperties(ModItems.sticks.getRegistryName().toString(), 0), 0.5f)
        			.mapWrapperAdd(new ItemProperties(ModItems.cheapBracelet.getRegistryName().toString(), 0), 0.3f),
        		5, 1, 0.8f, 
        		new ItemProperties[] { new ItemProperties(ModItems.cloth.getRegistryName().toString(), 0) }, 
        		new MapWrapper<ItemProperties, Integer>(),
        		true, false));
        
        entityStatsMap.put(EntityAmbrosia.class, new EntityProperties(
        		new MapWrapper<IAttribute, Double>()
        			.mapWrapperAdd(SharedMonsterAttributes.MAX_HEALTH, 186.0)
        			.mapWrapperAdd((IAttribute)ItemStatAttributes.RFATTACK, 21.5)
        			.mapWrapperAdd((IAttribute)ItemStatAttributes.RFDEFENCE, 13.5)
        			.mapWrapperAdd((IAttribute)ItemStatAttributes.RFMAGICATT, 18.3)
        			.mapWrapperAdd((IAttribute)ItemStatAttributes.RFMAGICDEF, 14.2),
        		new MapWrapper<ItemProperties, Float>()
        			.mapWrapperAdd(new ItemProperties(ModItems.strings.getRegistryName().toString(), 2), 0.5f),
        		10, 12, 0.35f, 
        		new ItemProperties[0], 
        		new MapWrapper<ItemProperties, Integer>(),
        		true, true));
    }
}
