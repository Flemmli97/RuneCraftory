package com.flemmli97.runecraftory.mobs;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.mobs.config.MobConfigSpec;
import com.flemmli97.runecraftory.mobs.config.SpawnConfig;
import com.flemmli97.runecraftory.mobs.events.MobClientEvents;
import com.flemmli97.runecraftory.mobs.events.MobEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

public class MobModule {

    public static SpawnConfig spawnConfig;

    public MobModule() {
        MinecraftForge.EVENT_BUS.register(new MobEvents());
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        spawnConfig = new SpawnConfig(FMLPaths.CONFIGDIR.get().resolve(RuneCraftory.MODID));
    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event){
        MobConfigSpec.config.loadConfig();
    }
    @SubscribeEvent
    public void clientStuff(FMLClientSetupEvent event){
        MinecraftForge.EVENT_BUS.register(new MobClientEvents());
    }
}
