package com.flemmli97.runecraftory.proxy;


import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayerAnim;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerAnim;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerAnimNetwork;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCap;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapNetwork;
import com.flemmli97.runecraftory.common.core.handler.event.EventHandlerClient;
import com.flemmli97.runecraftory.common.core.handler.event.EventHandlerCore;
import com.flemmli97.runecraftory.common.core.handler.event.EventHandlerSounds;
import com.flemmli97.runecraftory.common.core.network.PacketHandler;
import com.flemmli97.runecraftory.common.init.CraftingRegistry;
import com.flemmli97.runecraftory.common.init.ModEntities;
import com.flemmli97.runecraftory.common.init.OreDictInit;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
    		PacketHandler.registerPackets();
    		ModEntities.init();
    }
    
    public void init(FMLInitializationEvent e) {
    		CapabilityManager.INSTANCE.register(IPlayer.class, new PlayerCapNetwork(), PlayerCap::new);
    		CapabilityManager.INSTANCE.register(IPlayerAnim.class, new PlayerAnimNetwork(), PlayerAnim::new);

        MinecraftForge.EVENT_BUS.register(new EventHandlerCore());
        MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
        MinecraftForge.EVENT_BUS.register(new EventHandlerSounds());
    }

    public void postInit(FMLPostInitializationEvent e) {
    		ModEntities.registerMobSpawn();
    		CraftingRegistry.init();
    		OreDictInit.init();
    }
    
    public IThreadListener getListener(MessageContext ctx) {
    	return (WorldServer) ctx.getServerHandler().player.world;
    }
    
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
   	 return ctx.getServerHandler().player;
   	}
}