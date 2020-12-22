package com.flemmli97.runecraftory.client;

import com.flemmli97.runecraftory.client.gui.CraftingGui;
import com.flemmli97.runecraftory.client.gui.OverlayGui;
import com.flemmli97.runecraftory.client.render.RenderGate;
import com.flemmli97.runecraftory.client.render.monster.RenderAmbrosia;
import com.flemmli97.runecraftory.client.render.monster.RenderAnt;
import com.flemmli97.runecraftory.client.render.monster.RenderBeetle;
import com.flemmli97.runecraftory.client.render.monster.RenderBigMuck;
import com.flemmli97.runecraftory.client.render.monster.RenderBuffamoo;
import com.flemmli97.runecraftory.client.render.monster.RenderChipsqueek;
import com.flemmli97.runecraftory.client.render.monster.RenderCluckadoodle;
import com.flemmli97.runecraftory.client.render.monster.RenderOrc;
import com.flemmli97.runecraftory.client.render.monster.RenderOrcArcher;
import com.flemmli97.runecraftory.client.render.monster.RenderPommePomme;
import com.flemmli97.runecraftory.client.render.monster.RenderWooly;
import com.flemmli97.runecraftory.client.render.projectiles.RenderAmbrosiaWave;
import com.flemmli97.runecraftory.client.render.projectiles.RenderButterfly;
import com.flemmli97.runecraftory.client.render.projectiles.RenderFireball;
import com.flemmli97.runecraftory.client.render.projectiles.RenderMobArrow;
import com.flemmli97.runecraftory.client.render.projectiles.RenderSleepBall;
import com.flemmli97.runecraftory.common.blocks.BlockBrokenMineral;
import com.flemmli97.runecraftory.common.blocks.BlockCrop;
import com.flemmli97.runecraftory.common.blocks.BlockHerb;
import com.flemmli97.runecraftory.common.blocks.BlockMineral;
import com.flemmli97.runecraftory.common.items.weapons.ItemDualBladeBase;
import com.flemmli97.runecraftory.common.items.weapons.ItemGloveBase;
import com.flemmli97.runecraftory.common.registry.ModBlocks;
import com.flemmli97.runecraftory.common.registry.ModContainer;
import com.flemmli97.runecraftory.common.registry.ModEntities;
import com.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientRegister {

    public static void registerRender(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.gate.get(), RenderGate::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.wooly.get(), RenderWooly::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.orc.get(), RenderOrc::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.orcArcher.get(), RenderOrcArcher::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ant.get(), RenderAnt::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.beetle.get(), RenderBeetle::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.big_muck.get(), RenderBigMuck::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.buffamoo.get(), RenderBuffamoo::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.chipsqueek.get(), RenderChipsqueek::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.cluckadoodle.get(), RenderCluckadoodle::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.pomme_pomme.get(), RenderPommePomme::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ambrosia.get(), RenderAmbrosia::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.arrow.get(), RenderMobArrow::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.sleep_ball.get(), RenderSleepBall::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ambrosia_wave.get(), RenderAmbrosiaWave::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.butterfly.get(), RenderButterfly::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.fireBall.get(), RenderFireball::new);

        ClientHandlers.overlay = new OverlayGui(Minecraft.getInstance());

        event.enqueueWork(() -> {
            ModBlocks.BLOCKS.getEntries().forEach(reg -> {
                if (reg.get() instanceof BlockHerb || reg.get() instanceof BlockCrop || reg.get() instanceof BlockMineral || reg.get() instanceof BlockBrokenMineral)
                    RenderTypeLookup.setRenderLayer(reg.get(), RenderType.getCutout());
            });

            RenderTypeLookup.setRenderLayer(ModBlocks.bossSpawner.get(), RenderType.getCutout());

            ScreenManager.registerFactory(ModContainer.craftingContainer.get(), CraftingGui::new);

            ModItems.ITEMS.getEntries().forEach(reg-> {
                if(reg.get() instanceof ItemDualBladeBase || reg.get() instanceof ItemGloveBase)
                    ItemModelsProperties.register(reg.get(), new ResourceLocation("held"), ItemModelProps.heldMainProp);
            });
        });
    }
}
