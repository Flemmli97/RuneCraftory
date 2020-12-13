package com.flemmli97.runecraftory.common;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.config.MobConfig;
import com.flemmli97.runecraftory.common.config.SpawnConfig;
import com.flemmli97.runecraftory.common.entities.GateEntity;
import com.flemmli97.runecraftory.common.events.MobEvents;
import com.flemmli97.runecraftory.common.registry.ModEntities;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

public class MobModule {

    public static SpawnConfig spawnConfig;

    public MobModule() {
        MinecraftForge.EVENT_BUS.register(new MobEvents());
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        spawnConfig = new SpawnConfig(FMLPaths.CONFIGDIR.get().resolve(RuneCraftory.MODID));
        MobConfig.MobConfigSpec.config.loadConfig();

    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> EntitySpawnPlacementRegistry.register(ModEntities.gate.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GateEntity::canSpawnAt));
    }

}
