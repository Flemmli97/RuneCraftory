package com.flemmli97.runecraftory.proxy;

import org.lwjgl.input.Keyboard;

import com.flemmli97.runecraftory.client.gui.GuiShop;
import com.flemmli97.runecraftory.client.render.item.MultiItemColor;
import com.flemmli97.runecraftory.common.init.ModEntities;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy extends CommonProxy
{
	public static KeyBinding skill1 = new KeyBinding("runecraftory.spell1", Keyboard.KEY_H, LibReference.MODID);
	public static KeyBinding skill2 = new KeyBinding("runecraftory.spell2", Keyboard.KEY_L, LibReference.MODID);
	public static KeyBinding skill3 = new KeyBinding("runecraftory.spell3", Keyboard.KEY_L, LibReference.MODID);
	public static KeyBinding skill4 = new KeyBinding("runecraftory.spell4", Keyboard.KEY_L, LibReference.MODID);
    
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        ModEntities.registerRenders();
    }
    
    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        FMLClientHandler.instance().getClient().getItemColors().registerItemColorHandler(new MultiItemColor(), ModItems.spawnEgg);
        ClientRegistry.registerKeyBinding(ClientProxy.skill1);
        ClientRegistry.registerKeyBinding(ClientProxy.skill2);
        ClientRegistry.registerKeyBinding(ClientProxy.skill3);
        ClientRegistry.registerKeyBinding(ClientProxy.skill4);
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
        return ctx.side.isClient() ? Minecraft.getMinecraft().player : super.getPlayerEntity(ctx);
    }
    
    @Override
    public Object currentGui(MessageContext ctx) {
        return ctx.side.isClient() ? Minecraft.getMinecraft().currentScreen : super.currentGui(ctx);
    }
    
    @Override
    public void guiTextBubble(String text, Object gui) {
        if (gui instanceof GuiShop) {
            ((GuiShop)gui).displayMessage(text);
        }
    }
}
