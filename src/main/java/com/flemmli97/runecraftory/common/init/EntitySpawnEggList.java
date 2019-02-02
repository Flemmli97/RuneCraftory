package com.flemmli97.runecraftory.common.init;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.flemmli97.runecraftory.common.entity.monster.EntityAnt;
import com.flemmli97.runecraftory.common.entity.monster.EntityBeetle;
import com.flemmli97.runecraftory.common.entity.monster.EntityOrc;
import com.flemmli97.runecraftory.common.entity.monster.EntityWooly;
import com.flemmli97.runecraftory.common.entity.monster.boss.EntityAmbrosia;
import com.flemmli97.runecraftory.common.entity.monster.boss.EntityThunderbolt;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.util.ResourceLocation;

public class EntitySpawnEggList
{
	public static HashMap<ResourceLocation, EntityEggInfo> entityEggs = new LinkedHashMap<ResourceLocation, EntityEggInfo>();
	
    private static void addMapping(Class<? extends Entity> entity, int eggfirstcolour, int eggsecondcolour)
    {
        entityEggs.put(EntityList.getKey(entity), new EntityList.EntityEggInfo(EntityList.getKey(entity), eggfirstcolour, eggsecondcolour));
    }
    
    static
    {
        addMapping(EntityWooly.class, 0xffffcc, 0xffffff);
        addMapping(EntityOrc.class, 0x663300, 0xffbf80);
        addMapping(EntityAnt.class, 0x800000, 0x1a0000);
        addMapping(EntityBeetle.class, 0x000000, 0x800000);
        addMapping(EntityAmbrosia.class, 0x00ff00, 0xe600e6);
        addMapping(EntityThunderbolt.class, 0x010e23, 0x3c3693);
    }
}