package com.flemmli97.runecraftory.common.items;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.flemmli97.runecraftory.common.entity.starter.EntityAnt;
import com.flemmli97.runecraftory.common.entity.starter.EntityOrc;
import com.flemmli97.runecraftory.common.entity.starter.EntityWooly;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;

public class EntitySpawnEggList
{
	public static HashMap<ResourceLocation, EntityEggInfo> entityEggs = new LinkedHashMap<ResourceLocation, EntityEggInfo>();
	
    private static void addMapping(Class<? extends Entity> entity, int eggfirstcolour, int eggsecondcolour)
    {
        entityEggs.put(EntityList.getKey(entity), new EntitySpawnEggList.EntityEggInfo(EntityList.getKey(entity), eggfirstcolour, eggsecondcolour));
    }

    static
    {
        addMapping(EntityWooly.class, 0xffffcc, 0xffffff);
        addMapping(EntityOrc.class, 0x663300, 0xffbf80);
        addMapping(EntityAnt.class, 0x800000, 0x1a0000);
    }

    public static class EntityEggInfo
    {
        public final ResourceLocation spawnedID;
        public final int primaryColor;
        public final int secondaryColor;

        public EntityEggInfo(ResourceLocation resourceLocation, int eggfirstcolour, int eggsecondcolour)
        {
            this.spawnedID = resourceLocation;
            this.primaryColor = eggfirstcolour;
            this.secondaryColor = eggsecondcolour;
        }
    }
}