package com.flemmli97.runecraftory.common.init;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.entity.EntityGate;
import com.flemmli97.runecraftory.common.entity.magic.EntityFireBall;
import com.flemmli97.runecraftory.common.entity.magic.EntityWaterLaser;
import com.flemmli97.runecraftory.common.entity.monster.*;
import com.flemmli97.runecraftory.common.entity.monster.boss.EntityAmbrosia;
import com.flemmli97.runecraftory.common.entity.monster.boss.EntityThunderbolt;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityAmbrosiaSleep;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityAmbrosiaWave;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityButterfly;
import com.flemmli97.runecraftory.common.entity.npc.EntityNPCShopOwner;
import com.flemmli97.runecraftory.common.lib.LibReference;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities
{
    public static final void init() {
        int id = 0;
        LibReference.logger.info("Registering entities");
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "gate"), EntityGate.class, "gate", id++, RuneCraftory.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "wooly"), EntityWooly.class, "wooly", id++, RuneCraftory.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "orc"), EntityOrc.class, "orc", id++, RuneCraftory.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "orc_archer"), EntityOrcArcher.class, "orc_archer", id++, RuneCraftory.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "ant"), EntityAnt.class, "ant", id++, RuneCraftory.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "beetle"), EntityBeetle.class, "beetle", id++, RuneCraftory.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "big_muck"), EntityBigMuck.class, "big_muck", id++, RuneCraftory.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "buffamoo"), EntityBuffamoo.class, "buffamoo", id++, RuneCraftory.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "cluckadoodle"), EntityCluckadoodle.class, "cluckadoodle", id++, RuneCraftory.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "pomme_pomme"), EntityPommePomme.class, "pomme_pomme", id++, RuneCraftory.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "chipsqueek"), EntityChipsqueek.class, "chipsqueek", id++, RuneCraftory.instance, 64, 3, true);

        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "ambrosia"), EntityAmbrosia.class, "ambrosia", id++, RuneCraftory.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "thunderbolt"), EntityThunderbolt.class, "thunderbolt", id++, RuneCraftory.instance, 64, 3, true);

        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "shop_npc"), EntityNPCShopOwner.class, "shop_npc", id++, RuneCraftory.instance, 64, 6, true);
        
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "butterfly"), EntityButterfly.class, "butterfly", id++, RuneCraftory.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "ambrosiaSleep"), EntityAmbrosiaSleep.class, "ambrosiaSleep", id++, RuneCraftory.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "ambrosiaWave"), EntityAmbrosiaWave.class, "ambrosiaWave", id++, RuneCraftory.instance, 64, 3, true);
        
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "fireball_small"), EntityFireBall.class, "fireball_small", id++, RuneCraftory.instance, 64, 6, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "water_laser"), EntityWaterLaser.class, "water_laser", id++, RuneCraftory.instance, 64, 6, true);
    }
    
    public static final void registerMobSpawn() {
        for (final Biome biome : Biome.REGISTRY) {
            EntityRegistry.addSpawn(EntityGate.class, 50, 1, 2, EnumCreatureType.MONSTER, biome);
        }
    }
}
