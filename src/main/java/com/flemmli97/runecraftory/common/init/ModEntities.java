package com.flemmli97.runecraftory.common.init;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.client.models.ModelNPCBase;
import com.flemmli97.runecraftory.client.render.RenderGate;
import com.flemmli97.runecraftory.client.render.RenderNPCBase;
import com.flemmli97.runecraftory.client.render.monsters.RenderAmbrosia;
import com.flemmli97.runecraftory.client.render.monsters.RenderAnt;
import com.flemmli97.runecraftory.client.render.monsters.RenderBeetle;
import com.flemmli97.runecraftory.client.render.monsters.RenderOrc;
import com.flemmli97.runecraftory.client.render.monsters.RenderWooly;
import com.flemmli97.runecraftory.client.render.projectile.RenderAmbrosiaWave;
import com.flemmli97.runecraftory.client.render.projectile.RenderButterfly;
import com.flemmli97.runecraftory.client.render.projectile.RenderFireball;
import com.flemmli97.runecraftory.client.render.projectile.RenderSleepBall;
import com.flemmli97.runecraftory.client.render.projectile.RenderWaterLaser;
import com.flemmli97.runecraftory.common.entity.EntityGate;
import com.flemmli97.runecraftory.common.entity.magic.EntityFireBall;
import com.flemmli97.runecraftory.common.entity.magic.EntityWaterLaser;
import com.flemmli97.runecraftory.common.entity.monster.EntityAnt;
import com.flemmli97.runecraftory.common.entity.monster.EntityBeetle;
import com.flemmli97.runecraftory.common.entity.monster.EntityOrc;
import com.flemmli97.runecraftory.common.entity.monster.EntityWooly;
import com.flemmli97.runecraftory.common.entity.monster.boss.EntityAmbrosia;
import com.flemmli97.runecraftory.common.entity.monster.boss.EntityThunderbolt;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityAmbrosiaSleep;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityAmbrosiaWave;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityButterfly;
import com.flemmli97.runecraftory.common.entity.npc.EntityNPCShopOwner;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModEntities
{
    public static final void init() {
        int id = 0;
        LibReference.logger.info("Registering entities");
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "gate"), EntityGate.class, "gate", id++, RuneCraftory.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "wooly"), EntityWooly.class, "wooly", id++, RuneCraftory.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "orc"), EntityOrc.class, "orc", id++, RuneCraftory.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "ant"), EntityAnt.class, "ant", id++, RuneCraftory.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "beetle"), EntityBeetle.class, "beetle", id++, RuneCraftory.instance, 64, 3, true);
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
    
    @SideOnly(Side.CLIENT)
    public static final void registerRenders() {
        RenderingRegistry.registerEntityRenderingHandler(EntityGate.class, RenderGate::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityWooly.class, RenderWooly::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityOrc.class, RenderOrc::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityAnt.class, RenderAnt::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBeetle.class, RenderBeetle::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityAmbrosia.class, RenderAmbrosia::new);
        
        RenderingRegistry.registerEntityRenderingHandler(EntityButterfly.class, RenderButterfly::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityAmbrosiaSleep.class, RenderSleepBall::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityAmbrosiaWave.class, RenderAmbrosiaWave::new);

        RenderingRegistry.registerEntityRenderingHandler(EntityFireBall.class, RenderFireball::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityWaterLaser.class, RenderWaterLaser::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityNPCShopOwner.class, new IRenderFactory<EntityNPCShopOwner>() 
        {
            @Override
            public RenderNPCBase<? super EntityNPCShopOwner> createRenderFor(RenderManager manager) {
                return new RenderNPCBase<EntityNPCShopOwner>(manager, new ModelNPCBase());
            }
        });
    }
}
