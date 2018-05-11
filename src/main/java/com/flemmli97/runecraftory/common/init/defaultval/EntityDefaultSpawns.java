package com.flemmli97.runecraftory.common.init.defaultval;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.monster.EntityAnt;
import com.flemmli97.runecraftory.common.entity.monster.EntityBeetle;
import com.flemmli97.runecraftory.common.entity.monster.EntityOrc;
import com.flemmli97.runecraftory.common.entity.monster.EntityWooly;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import net.minecraftforge.common.BiomeDictionary.Type;

public class EntityDefaultSpawns {
	
	public static final Multimap<Class<?extends EntityMobBase>, String> classBiomeMap = ArrayListMultimap.create();
	public static final Multimap<Class<?extends EntityMobBase>, String> classTypeMap = ArrayListMultimap.create();

	static
	{
		classTypeMap.putAll(EntityWooly.class, Lists.newArrayList(Type.PLAINS.getName(), Type.BEACH.getName(), Type.CONIFEROUS.getName(), Type.FOREST.getName(), Type.HILLS.getName(), Type.MAGICAL.getName(), Type.MOUNTAIN.getName(), Type.SAVANNA.getName()));
		classTypeMap.putAll(EntityOrc.class, Lists.newArrayList(Type.PLAINS.getName(), Type.BEACH.getName(), Type.CONIFEROUS.getName(), Type.FOREST.getName(), Type.HILLS.getName(), Type.MAGICAL.getName(), Type.MOUNTAIN.getName(), Type.SAVANNA.getName()));
		classTypeMap.putAll(EntityAnt.class, Lists.newArrayList(Type.PLAINS.getName(), Type.CONIFEROUS.getName(), Type.FOREST.getName(), Type.HILLS.getName(), Type.SAVANNA.getName(), Type.LUSH.getName()));
		classTypeMap.putAll(EntityBeetle.class, Lists.newArrayList(Type.PLAINS.getName(), Type.FOREST.getName(), Type.HILLS.getName(), Type.LUSH.getName()));
	}
}
