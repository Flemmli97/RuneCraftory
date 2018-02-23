package com.flemmli97.runecraftory.proxy;

import org.lwjgl.input.Keyboard;

import com.flemmli97.runecraftory.client.render.MultiItemColor;
import com.flemmli97.runecraftory.common.init.ModEntities;
import com.flemmli97.runecraftory.common.init.ModItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy extends CommonProxy {
	
	public static KeyBinding command = new KeyBinding("fate.command", Keyboard.KEY_H, "key.categories.misc");
	public static KeyBinding special = new KeyBinding("fate.special", Keyboard.KEY_L, "key.categories.misc");

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        ModEntities.registerRenders();
    }

    @Override
    public void init(FMLInitializationEvent e) {
		FMLClientHandler.instance().getClient().getItemColors().registerItemColorHandler(new MultiItemColor(), ModItems.spawnEgg);
        super.init(e);
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
        
    }
    
    @Override
	public IThreadListener getListener(MessageContext ctx) {
    	return ctx.side.isClient() ? Minecraft.getMinecraft() : super.getListener(ctx);
	}

	@Override
    public EntityPlayer getPlayerEntity(MessageContext ctx) {

     return (ctx.side.isClient() ? Minecraft.getMinecraft().player : super.getPlayerEntity(ctx));
    }

}