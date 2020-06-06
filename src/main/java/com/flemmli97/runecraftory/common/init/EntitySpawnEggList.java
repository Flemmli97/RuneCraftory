package com.flemmli97.runecraftory.common.init;

import com.flemmli97.runecraftory.common.entity.monster.EntityAnt;
import com.flemmli97.runecraftory.common.entity.monster.EntityBeetle;
import com.flemmli97.runecraftory.common.entity.monster.EntityBigMuck;
import com.flemmli97.runecraftory.common.entity.monster.EntityBuffamoo;
import com.flemmli97.runecraftory.common.entity.monster.EntityChipsqueek;
import com.flemmli97.runecraftory.common.entity.monster.EntityCluckadoodle;
import com.flemmli97.runecraftory.common.entity.monster.EntityOrc;
import com.flemmli97.runecraftory.common.entity.monster.EntityOrcArcher;
import com.flemmli97.runecraftory.common.entity.monster.EntityPommePomme;
import com.flemmli97.runecraftory.common.entity.monster.EntityWooly;
import com.flemmli97.runecraftory.common.entity.monster.boss.EntityAmbrosia;
import com.flemmli97.runecraftory.common.entity.monster.boss.EntityThunderbolt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class EntitySpawnEggList
{
	private static Map<ResourceLocation, EntityEggInfo> entityEggs = new TreeMap<ResourceLocation, EntityEggInfo>();
	
    private static void addMapping(Class<? extends Entity> entity, int eggfirstcolour, int eggsecondcolour) {
        entityEggs.put(EntityList.getKey(entity), new EntityList.EntityEggInfo(EntityList.getKey(entity), eggfirstcolour, eggsecondcolour));
    }

    public static EntityEggInfo get(ResourceLocation res){
        return entityEggs.get(res);
    }

    public static Collection<EntityEggInfo> values() {
        return entityEggs.values();
    }

    static {
        addMapping(EntityWooly.class, 0xffffcc, 0xffffff);
        addMapping(EntityOrc.class, 0x663300, 0xffbf80);
        addMapping(EntityOrcArcher.class, 0x663300, 0xffbf80);
        addMapping(EntityAnt.class, 0x800000, 0x1a0000);
        addMapping(EntityBeetle.class, 0x000000, 0x800000);
        addMapping(EntityBigMuck.class, 0xd7ce4a, 0xad5c25);
        addMapping(EntityAmbrosia.class, 0x00ff00, 0xe600e6);
        addMapping(EntityThunderbolt.class, 0x010e23, 0x3c3693);
        addMapping(EntityBuffamoo.class, 0xd8d8d0, 0x4e4e4c);
        addMapping(EntityCluckadoodle.class, 0xc2c2c2, 0xdc2121);
        addMapping(EntityChipsqueek.class, 0xff3b5b, 0xf9ffbb);
        addMapping(EntityPommePomme.class, 0xff1c2b, 0xf7b4b8);
    }
}