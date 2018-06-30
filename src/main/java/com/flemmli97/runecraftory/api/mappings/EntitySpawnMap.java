package com.flemmli97.runecraftory.api.mappings;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.monster.EntityAnt;
import com.flemmli97.runecraftory.common.entity.monster.EntityBeetle;
import com.flemmli97.runecraftory.common.entity.monster.EntityOrc;
import com.flemmli97.runecraftory.common.entity.monster.EntityWooly;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import net.minecraftforge.common.BiomeDictionary;

public class EntitySpawnMap
{
    public static final Multimap<Class<? extends EntityMobBase>, String> classBiomeMap = ArrayListMultimap.create();
    public static final Multimap<Class<? extends EntityMobBase>, String> classTypeMap = ArrayListMultimap.create();
    
    static {
        classTypeMap.putAll(EntityWooly.class, Lists.newArrayList(new String[] { BiomeDictionary.Type.PLAINS.getName(), BiomeDictionary.Type.BEACH.getName(), BiomeDictionary.Type.CONIFEROUS.getName(), BiomeDictionary.Type.FOREST.getName(), BiomeDictionary.Type.HILLS.getName(), BiomeDictionary.Type.MAGICAL.getName(), BiomeDictionary.Type.MOUNTAIN.getName(), BiomeDictionary.Type.SAVANNA.getName() }));
        classTypeMap.putAll(EntityOrc.class, Lists.newArrayList(new String[] { BiomeDictionary.Type.PLAINS.getName(), BiomeDictionary.Type.BEACH.getName(), BiomeDictionary.Type.CONIFEROUS.getName(), BiomeDictionary.Type.FOREST.getName(), BiomeDictionary.Type.HILLS.getName(), BiomeDictionary.Type.MAGICAL.getName(), BiomeDictionary.Type.MOUNTAIN.getName(), BiomeDictionary.Type.SAVANNA.getName() }));
        classTypeMap.putAll(EntityAnt.class, Lists.newArrayList(new String[] { BiomeDictionary.Type.PLAINS.getName(), BiomeDictionary.Type.CONIFEROUS.getName(), BiomeDictionary.Type.FOREST.getName(), BiomeDictionary.Type.HILLS.getName(), BiomeDictionary.Type.SAVANNA.getName(), BiomeDictionary.Type.LUSH.getName() }));
        classTypeMap.putAll(EntityBeetle.class, Lists.newArrayList(new String[] { BiomeDictionary.Type.PLAINS.getName(), BiomeDictionary.Type.FOREST.getName(), BiomeDictionary.Type.HILLS.getName(), BiomeDictionary.Type.LUSH.getName() }));
    }
}
