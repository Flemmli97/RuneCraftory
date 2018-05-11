package com.flemmli97.runecraftory.common.init.defaultval;

import java.util.HashMap;
import java.util.Map;

import com.flemmli97.runecraftory.api.entities.EntityProperties;
import com.flemmli97.runecraftory.api.entities.IEntityBase;
import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.api.items.ItemProperties;
import com.flemmli97.runecraftory.common.entity.monster.EntityAnt;
import com.flemmli97.runecraftory.common.entity.monster.EntityBeetle;
import com.flemmli97.runecraftory.common.entity.monster.EntityOrc;
import com.flemmli97.runecraftory.common.entity.monster.EntityWooly;
import com.flemmli97.runecraftory.common.entity.monster.boss.EntityAmbrosia;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.google.common.collect.Maps;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.init.Blocks;

public class EntityDefaultStats {
	
	private static Map<Class<? extends IEntityBase>,EntityProperties> entityStatsMap = Maps.newHashMap();
	
	public static void setDefaultStats(Class<? extends IEntityBase> clss, EntityProperties props)
	{
		entityStatsMap.put(clss, props);
	}
	
	public static EntityProperties getDefaultStats(Class<? extends IEntityBase> clss)
	{
		return entityStatsMap.get(clss);
	}
	
	static
	{
		entityStatsMap.put(EntityWooly.class,
				new EntityProperties(
						new MapWrapper<IAttribute, Double>()
							.mapWrapperAdd(SharedMonsterAttributes.MAX_HEALTH, 166D)
							.mapWrapperAdd(ItemStats.RFATTACK, 14D)
							.mapWrapperAdd(ItemStats.RFDEFENCE, 9D)
							.mapWrapperAdd(ItemStats.RFMAGICATT, 12D)
							.mapWrapperAdd(ItemStats.RFMAGICDEF, 9D),
						new MapWrapper<ItemProperties, Float>()
							.mapWrapperAdd(new ItemProperties(ModItems.furs.getRegistryName().toString(), 1,0), 0.5F), 3, 1, 0.9F, 
						new ItemProperties[] {
							new ItemProperties(ModItems.furs.getRegistryName().toString(), 1,0), new ItemProperties(ModItems.furs.getRegistryName().toString(), 1,1),
							new ItemProperties(ModItems.furs.getRegistryName().toString(), 1,2), new ItemProperties(ModItems.furs.getRegistryName().toString(), 1,4),
							new ItemProperties(Blocks.WOOL.getRegistryName().toString(), 1,-1)},
						new MapWrapper<ItemProperties, Integer>()
							.mapWrapperAdd(new ItemProperties(ModItems.furs.getRegistryName().toString(), 1,0), 0)
							.mapWrapperAdd(new ItemProperties(ModItems.furs.getRegistryName().toString(), 1,1), 5)
							.mapWrapperAdd(new ItemProperties(ModItems.furs.getRegistryName().toString(), 1,2), 10)
						, true, false));
		
		entityStatsMap.put(EntityAnt.class,
				new EntityProperties(
						new MapWrapper<IAttribute, Double>()
							.mapWrapperAdd(SharedMonsterAttributes.MAX_HEALTH, 112D)
							.mapWrapperAdd(ItemStats.RFATTACK, 13.5D)
							.mapWrapperAdd(ItemStats.RFDEFENCE, 10.5D)
							.mapWrapperAdd(ItemStats.RFMAGICATT, 9.0D)
							.mapWrapperAdd(ItemStats.RFMAGICDEF, 9.2D),
						new MapWrapper<ItemProperties, Float>()
							.mapWrapperAdd(new ItemProperties(ModItems.cloth.getRegistryName().toString(), 1,7), 0.4F)
							.mapWrapperAdd(new ItemProperties(ModItems.cloth.getRegistryName().toString(), 1,8), 0.1F), 2, 1, 0.75F, 
						new ItemProperties[] {
							new ItemProperties(ModItems.cloth.getRegistryName().toString(), 1,7)},
						new MapWrapper<ItemProperties, Integer>()
							.mapWrapperAdd(new ItemProperties(ModItems.cloth.getRegistryName().toString(), 1,7), 0)
						, false, false));
		
		entityStatsMap.put(EntityBeetle.class,
				new EntityProperties(
						new MapWrapper<IAttribute, Double>()
							.mapWrapperAdd(SharedMonsterAttributes.MAX_HEALTH, 125D)
							.mapWrapperAdd(ItemStats.RFATTACK, 18.5D)
							.mapWrapperAdd(ItemStats.RFDEFENCE, 7D)
							.mapWrapperAdd(ItemStats.RFMAGICATT, 10D)
							.mapWrapperAdd(ItemStats.RFMAGICDEF, 6D),
						new MapWrapper<ItemProperties, Float>()
						.mapWrapperAdd(new ItemProperties(ModItems.sticks.getRegistryName().toString(), 1,2), 0.6F)
						.mapWrapperAdd(new ItemProperties(ModItems.cloth.getRegistryName().toString(), 1,7), 0.4F)
						.mapWrapperAdd(new ItemProperties(ModItems.cloth.getRegistryName().toString(), 1,8), 0.1F), 5, 1, 0.65F, 
						new ItemProperties[] {
							new ItemProperties(ModItems.sticks.getRegistryName().toString(), 1,0)},
						new MapWrapper<ItemProperties, Integer>()
							.mapWrapperAdd(new ItemProperties(ModItems.sticks.getRegistryName().toString(), 1,0), 0)
						, true, false));
		
		entityStatsMap.put(EntityOrc.class,
				new EntityProperties(
						new MapWrapper<IAttribute, Double>()
							.mapWrapperAdd(SharedMonsterAttributes.MAX_HEALTH, 145D)
							.mapWrapperAdd(ItemStats.RFATTACK, 15.5D)
							.mapWrapperAdd(ItemStats.RFDEFENCE, 10.2D)
							.mapWrapperAdd(ItemStats.RFMAGICATT, 11D)
							.mapWrapperAdd(ItemStats.RFMAGICDEF, 10.8D),
						new MapWrapper<ItemProperties, Float>()
							.mapWrapperAdd(new ItemProperties(ModItems.cloth.getRegistryName().toString(), 1,0), 0.5F)
							.mapWrapperAdd(new ItemProperties(ModItems.sticks.getRegistryName().toString(), 1,0), 0.5F)
							.mapWrapperAdd(new ItemProperties(ModItems.cheapBracelet.getRegistryName().toString(), 1,0), 0.3F), 5, 1, 0.8F, 
						new ItemProperties[] {
							new ItemProperties(ModItems.cloth.getRegistryName().toString(), 1,0)},
						new MapWrapper<ItemProperties, Integer>()
						, true, false));
		
		entityStatsMap.put(EntityAmbrosia.class,
				new EntityProperties(
						new MapWrapper<IAttribute, Double>()
							.mapWrapperAdd(SharedMonsterAttributes.MAX_HEALTH, 186D)
							.mapWrapperAdd(ItemStats.RFATTACK, 21.5D)
							.mapWrapperAdd(ItemStats.RFDEFENCE, 13.5D)
							.mapWrapperAdd(ItemStats.RFMAGICATT, 18.3D)
							.mapWrapperAdd(ItemStats.RFMAGICDEF, 14.2D),
						new MapWrapper<ItemProperties, Float>()
							.mapWrapperAdd(new ItemProperties(ModItems.strings.getRegistryName().toString(), 1,2), 0.5F), 10, 12, 0.35F, 
						new ItemProperties[] {},//big toyherb
						new MapWrapper<ItemProperties, Integer>()
						, true, true));
	}
	
	static class MapWrapper<T,V> extends HashMap<T, V>
	{
		private static final long serialVersionUID = 1773676727445299651L;

		MapWrapper<T,V> mapWrapperAdd(T t,V v)
		{
			this.put(t, v);
			return this;
		}
	}

}
