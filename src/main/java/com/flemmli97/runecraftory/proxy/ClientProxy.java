package com.flemmli97.runecraftory.proxy;

import com.flemmli97.runecraftory.client.gui.GuiShop;
import com.flemmli97.runecraftory.client.models.ModelNPCBase;
import com.flemmli97.runecraftory.client.render.RenderGate;
import com.flemmli97.runecraftory.client.render.RenderNPCBase;
import com.flemmli97.runecraftory.client.render.item.MultiItemColor;
import com.flemmli97.runecraftory.client.render.monsters.*;
import com.flemmli97.runecraftory.client.render.particles.ParticleAmbrosiaSleep;
import com.flemmli97.runecraftory.client.render.projectile.*;
import com.flemmli97.runecraftory.common.entity.EntityGate;
import com.flemmli97.runecraftory.common.entity.magic.EntityFireBall;
import com.flemmli97.runecraftory.common.entity.magic.EntityWaterLaser;
import com.flemmli97.runecraftory.common.entity.monster.*;
import com.flemmli97.runecraftory.common.entity.monster.boss.EntityAmbrosia;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityAmbrosiaSleep;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityAmbrosiaWave;
import com.flemmli97.runecraftory.common.entity.monster.projectile.EntityButterfly;
import com.flemmli97.runecraftory.common.entity.npc.EntityNPCShopOwner;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.LibParticles;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.tenshilib.client.particles.ParticleRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.lwjgl.input.Keyboard;

public class ClientProxy extends CommonProxy
{
	public static KeyBinding skill1 = new KeyBinding("runecraftory.spell1", Keyboard.KEY_H, LibReference.MODID);
	public static KeyBinding skill2 = new KeyBinding("runecraftory.spell2", Keyboard.KEY_L, LibReference.MODID);
	public static KeyBinding skill3 = new KeyBinding("runecraftory.spell3", Keyboard.KEY_L, LibReference.MODID);
	public static KeyBinding skill4 = new KeyBinding("runecraftory.spell4", Keyboard.KEY_L, LibReference.MODID);
    
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        this.registerRenders();
        registerParticles();
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
    
    private static final void registerParticles()
    {
        ParticleRegistries.registerParticle(LibParticles.particleFlame, new ParticleAmbrosiaSleep.Factory());
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

    private void registerRenders(){
        RenderingRegistry.registerEntityRenderingHandler(EntityGate.class, RenderGate::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityWooly.class, RenderWooly::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityOrc.class, RenderOrc::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityAnt.class, RenderAnt::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBeetle.class, RenderBeetle::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBigMuck.class, RenderBigMuck::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBuffamoo.class, RenderBuffamoo::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityCluckadoodle.class, RenderCluckadoodle::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityChipsqueek.class, RenderChipsqueek::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPommePomme.class, RenderPommePomme::new);

        RenderingRegistry.registerEntityRenderingHandler(EntityAmbrosia.class, RenderAmbrosia::new);

        RenderingRegistry.registerEntityRenderingHandler(EntityButterfly.class, RenderButterfly::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityAmbrosiaSleep.class, RenderSleepBall::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityAmbrosiaWave.class, RenderAmbrosiaWave::new);

        RenderingRegistry.registerEntityRenderingHandler(EntityFireBall.class, RenderFireball::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityWaterLaser.class, RenderWaterLaser::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityNPCShopOwner.class, manager -> new RenderNPCBase<EntityNPCShopOwner>(manager, new ModelNPCBase()));
    }
}
