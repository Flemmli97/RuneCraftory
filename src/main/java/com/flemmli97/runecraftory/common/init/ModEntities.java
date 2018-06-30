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
import com.flemmli97.runecraftory.client.render.projectile.RenderButterfly;
import com.flemmli97.runecraftory.client.render.projectile.RenderFireball;
import com.flemmli97.runecraftory.common.entity.EntityGate;
import com.flemmli97.runecraftory.common.entity.magic.EntityFireBall;
import com.flemmli97.runecraftory.common.entity.monster.EntityAnt;
import com.flemmli97.runecraftory.common.entity.monster.EntityBeetle;
import com.flemmli97.runecraftory.common.entity.monster.EntityOrc;
import com.flemmli97.runecraftory.common.entity.monster.EntityWooly;
import com.flemmli97.runecraftory.common.entity.monster.boss.EntityAmbrosia;
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
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "gate"), EntityGate.class, "gate", id++, (Object)RuneCraftory.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "wooly"), EntityWooly.class, "wooly", id++, (Object)RuneCraftory.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "orc"), EntityOrc.class, "orc", id++, (Object)RuneCraftory.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "ant"), EntityAnt.class, "ant", id++, (Object)RuneCraftory.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "beetle"), EntityBeetle.class, "beetle", id++, (Object)RuneCraftory.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "ambrosia"), EntityAmbrosia.class, "ambrosia", id++, (Object)RuneCraftory.instance, 64, 3, true);
        
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "shop_npc"), EntityNPCShopOwner.class, "shop_npc", id++, (Object)RuneCraftory.instance, 64, 6, true);
        
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "butterfly"), EntityButterfly.class, "butterfly", id++, (Object)RuneCraftory.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "ambrosiaSleep"), EntityAmbrosiaSleep.class, "ambrosiaSleep", id++, (Object)RuneCraftory.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "ambrosiaWave"), EntityAmbrosiaWave.class, "ambrosiaWave", id++, (Object)RuneCraftory.instance, 64, 3, true);
        
        EntityRegistry.registerModEntity(new ResourceLocation(LibReference.MODID, "fireball_small"), EntityFireBall.class, "fireball_small", id++, (Object)RuneCraftory.instance, 64, 6, true);
    }
    
    public static final void registerMobSpawn() {
        for (final Biome biome : Biome.REGISTRY) {
            EntityRegistry.addSpawn(EntityGate.class, 50, 1, 2, EnumCreatureType.MONSTER, new Biome[] { biome });
        }
        LibReference.logger.info("Registering gate spawns");
        GateSpawning.initGateSpawnings();
        LibReference.logger.info("Finished registering gate spawns");
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
        RenderingRegistry.registerEntityRenderingHandler(EntityFireBall.class, RenderFireball::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityNPCShopOwner.class, new IRenderFactory<EntityNPCShopOwner>() 
        {
            public RenderNPCBase<? super EntityNPCShopOwner> createRenderFor(RenderManager manager) {
                return new RenderNPCBase<EntityNPCShopOwner>(manager, new ModelNPCBase());
            }
        });
    }
}
