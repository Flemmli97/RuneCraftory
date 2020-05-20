package com.flemmli97.runecraftory.proxy;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.client.gui.GuiHandler;
import com.flemmli97.runecraftory.common.core.handler.capabilities.CapabilityNetwork;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCap;
import com.flemmli97.runecraftory.common.core.handler.config.ConfigHandler;
import com.flemmli97.runecraftory.common.core.handler.event.EventHandlerClient;
import com.flemmli97.runecraftory.common.core.handler.event.EventHandlerCore;
import com.flemmli97.runecraftory.common.core.handler.event.EventHandlerSounds;
import com.flemmli97.runecraftory.common.init.ModEntities;
import com.flemmli97.runecraftory.common.init.OreDictInit;
import com.flemmli97.runecraftory.common.init.WorldGenRegistry;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.network.PacketHandler;
import com.flemmli97.runecraftory.compat.dynamicTrees.DCInit;
import com.flemmli97.tenshilib.common.config.ConfigUtils.LoadState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent e) {
        LibReference.modDir = e.getModConfigurationDirectory();
        ConfigHandler.load(LoadState.PREINIT);
        if(ConfigHandler.MainConfig.dynamicTrees && Loader.isModLoaded("dynamictrees"))
        	DCInit.initItemAndBlocks();
        PacketHandler.registerPackets();
        ModEntities.init();
    }
    
    public void init(FMLInitializationEvent e) {
        CapabilityManager.INSTANCE.register(IPlayer.class, new CapabilityNetwork.PlayerCapNetwork(), PlayerCap::new);
        MinecraftForge.EVENT_BUS.register(new EventHandlerCore());
        MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
        MinecraftForge.EVENT_BUS.register(new EventHandlerSounds());
        NetworkRegistry.INSTANCE.registerGuiHandler(RuneCraftory.instance, new GuiHandler());
        OreDictInit.init();
        WorldGenRegistry.init();
        if (Loader.isModLoaded("waila") && ConfigHandler.MainConfig.waila) {
        	LibReference.logger.debug("Registering waila compat");
            FMLInterModComms.sendMessage("waila", "register", "com.flemmli97.runecraftory.compat.waila.WailaRegister.init");
        }
    }
    
    public void postInit(FMLPostInitializationEvent e) {
        ConfigHandler.load(LoadState.POSTINIT);
        if(ConfigHandler.MainConfig.noVanillaMobs)
            ForgeRegistries.BIOMES.forEach(biome-> {
                for (EnumCreatureType type : EnumCreatureType.values())
                    biome.getSpawnableList(type).clear();
            });
        if (ConfigHandler.MainConfig.mobs) {
            ModEntities.registerMobSpawn();
        }
    }
    
    public IThreadListener getListener(MessageContext ctx) {
        return (WorldServer) ctx.getServerHandler().player.world;
    }
    
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return ctx.getServerHandler().player;
    }
    
    
    public Object currentGui(MessageContext ctx) {
        return null;
    }
    
    public void guiTextBubble(String text, Object guiId) {
    }
}
