package com.flemmli97.runecraftory.client;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.client.gui.CraftingGui;
import com.flemmli97.runecraftory.client.gui.InfoScreen;
import com.flemmli97.runecraftory.client.gui.InfoSubScreen;
import com.flemmli97.runecraftory.client.gui.OverlayGui;
import com.flemmli97.runecraftory.client.gui.SpellInvOverlayGui;
import com.flemmli97.runecraftory.client.gui.UpgradeGui;
import com.flemmli97.runecraftory.client.model.monster.ModelBeetle;
import com.flemmli97.runecraftory.client.model.monster.ModelBigMuck;
import com.flemmli97.runecraftory.client.model.monster.ModelBuffamoo;
import com.flemmli97.runecraftory.client.model.monster.ModelChipsqueek;
import com.flemmli97.runecraftory.client.model.monster.ModelCluckadoodle;
import com.flemmli97.runecraftory.client.model.monster.ModelOrc;
import com.flemmli97.runecraftory.client.model.monster.ModelPommePomme;
import com.flemmli97.runecraftory.client.model.monster.ModelSkyFish;
import com.flemmli97.runecraftory.client.model.monster.ModelTortas;
import com.flemmli97.runecraftory.client.model.monster.ModelWeagle;
import com.flemmli97.runecraftory.client.particles.ColoredParticle;
import com.flemmli97.runecraftory.client.particles.SinkingParticle;
import com.flemmli97.runecraftory.client.render.RenderGate;
import com.flemmli97.runecraftory.client.render.RenderMonster;
import com.flemmli97.runecraftory.client.render.monster.RenderAmbrosia;
import com.flemmli97.runecraftory.client.render.monster.RenderAnt;
import com.flemmli97.runecraftory.client.render.monster.RenderGoblin;
import com.flemmli97.runecraftory.client.render.monster.RenderOrcArcher;
import com.flemmli97.runecraftory.client.render.monster.RenderThunderbolt;
import com.flemmli97.runecraftory.client.render.monster.RenderWooly;
import com.flemmli97.runecraftory.client.render.projectiles.EmptyRender;
import com.flemmli97.runecraftory.client.render.projectiles.RenderButterfly;
import com.flemmli97.runecraftory.client.render.projectiles.RenderFireball;
import com.flemmli97.runecraftory.client.render.projectiles.RenderMobArrow;
import com.flemmli97.runecraftory.common.blocks.BlockBrokenMineral;
import com.flemmli97.runecraftory.common.blocks.BlockCrop;
import com.flemmli97.runecraftory.common.blocks.BlockHerb;
import com.flemmli97.runecraftory.common.blocks.BlockMineral;
import com.flemmli97.runecraftory.common.entities.BaseMonster;
import com.flemmli97.runecraftory.common.items.weapons.ItemDualBladeBase;
import com.flemmli97.runecraftory.common.items.weapons.ItemGloveBase;
import com.flemmli97.runecraftory.common.registry.ModBlocks;
import com.flemmli97.runecraftory.common.registry.ModContainer;
import com.flemmli97.runecraftory.common.registry.ModEntities;
import com.flemmli97.runecraftory.common.registry.ModItems;
import com.flemmli97.runecraftory.common.registry.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.particle.HeartParticle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

public class ClientRegister {

    public static void registerRender(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.gate.get(), RenderGate::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.wooly.get(), RenderWooly::new);
        register(ModEntities.orc.get(), new ModelOrc<>());
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.orcArcher.get(), RenderOrcArcher::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ant.get(), RenderAnt::new);
        register(ModEntities.beetle.get(), new ModelBeetle<>());
        register(ModEntities.big_muck.get(), new ModelBigMuck<>());
        register(ModEntities.buffamoo.get(), new ModelBuffamoo<>());
        register(ModEntities.chipsqueek.get(), new ModelChipsqueek<>());
        register(ModEntities.cluckadoodle.get(), new ModelCluckadoodle<>());
        register(ModEntities.pomme_pomme.get(), new ModelPommePomme<>());
        register(ModEntities.tortas.get(), new ModelTortas<>());
        register(ModEntities.sky_fish.get(), new ModelSkyFish<>());
        register(ModEntities.weagle.get(), new ModelWeagle<>());
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.goblin.get(), RenderGoblin::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.goblinArcher.get(), RenderGoblin::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ambrosia.get(), RenderAmbrosia::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.thunderbolt.get(), RenderThunderbolt::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.arrow.get(), RenderMobArrow::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.spore.get(), EmptyRender::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.gust.get(), EmptyRender::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.sleep_ball.get(), EmptyRender::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.pollen.get(), EmptyRender::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ambrosia_wave.get(), EmptyRender::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.butterfly.get(), RenderButterfly::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.lightningOrbBolt.get(), EmptyRender::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.lightningBeam.get(), EmptyRender::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.fireBall.get(), RenderFireball::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.windBlade.get(), EmptyRender::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.waterLaser.get(), EmptyRender::new);

        ClientHandlers.overlay = new OverlayGui(Minecraft.getInstance());
        ClientHandlers.spellDisplay = new SpellInvOverlayGui(Minecraft.getInstance());

        event.enqueueWork(() -> {
            ModBlocks.BLOCKS.getEntries().forEach(reg -> {
                if (reg.get() instanceof BlockHerb || reg.get() instanceof BlockCrop || reg.get() instanceof BlockMineral || reg.get() instanceof BlockBrokenMineral)
                    RenderTypeLookup.setRenderLayer(reg.get(), RenderType.getCutout());
            });

            RenderTypeLookup.setRenderLayer(ModBlocks.bossSpawner.get(), RenderType.getCutout());

            ScreenManager.registerFactory(ModContainer.craftingContainer.get(), CraftingGui::new);
            ScreenManager.registerFactory(ModContainer.upgradeContainer.get(), UpgradeGui::new);
            ScreenManager.registerFactory(ModContainer.infoContainer.get(), InfoScreen::new);
            ScreenManager.registerFactory(ModContainer.infoSubContainer.get(), InfoSubScreen::new);

            ClientRegistry.registerKeyBinding(ClientHandlers.spell1 = new KeyBinding(RuneCraftory.MODID + ".key.spell_1", GLFW.GLFW_KEY_C, RuneCraftory.MODID + ".keycategory"));
            ClientRegistry.registerKeyBinding(ClientHandlers.spell2 = new KeyBinding(RuneCraftory.MODID + ".key.spell_2", GLFW.GLFW_KEY_V, RuneCraftory.MODID + ".keycategory"));
            ClientRegistry.registerKeyBinding(ClientHandlers.spell3 = new KeyBinding(RuneCraftory.MODID + ".key.spell_3", GLFW.GLFW_KEY_G, RuneCraftory.MODID + ".keycategory"));
            ClientRegistry.registerKeyBinding(ClientHandlers.spell4 = new KeyBinding(RuneCraftory.MODID + ".key.spell_4", GLFW.GLFW_KEY_B, RuneCraftory.MODID + ".keycategory"));

            ModItems.ITEMS.getEntries().forEach(reg -> {
                if (reg.get() instanceof ItemDualBladeBase || reg.get() instanceof ItemGloveBase)
                    ItemModelsProperties.register(reg.get(), new ResourceLocation("held"), ItemModelProps.heldMainProp);
            });
        });
    }

    public static void registerParticles(ParticleFactoryRegisterEvent event) {
        ParticleManager manager = Minecraft.getInstance().particles;
        manager.registerFactory(ModParticles.sinkingDust.get(), SinkingParticle.Factory::new);
        manager.registerFactory(ModParticles.light.get(), ColoredParticle.LightParticleFactory::new);
        manager.registerFactory(ModParticles.cross.get(), ColoredParticle.LightParticleFactory::new);
        manager.registerFactory(ModParticles.blink.get(), ColoredParticle.LightParticleFactory::new);
        manager.registerFactory(ModParticles.smoke.get(), ColoredParticle.LightParticleFactory::new);
        manager.registerFactory(ModParticles.staticLight.get(), ColoredParticle.NoGravityParticleFactory::new);
        manager.registerFactory(ModParticles.wind.get(), ColoredParticle.NoGravityParticleFactory::new);
        manager.registerFactory(ModParticles.sleep.get(), HeartParticle.Factory::new);
        manager.registerFactory(ModParticles.poison.get(), HeartParticle.Factory::new);

    }

    private static <T extends BaseMonster, M extends EntityModel<T>> IRenderFactory<? super T> getMonsterRender(M model, ResourceLocation texture) {
        return manager -> new RenderMonster<>(manager, model, texture);
    }

    private static <T extends BaseMonster, M extends EntityModel<T>> void register(EntityType<T> reg, M model) {
        RenderingRegistry.registerEntityRenderingHandler(reg, getMonsterRender(model, mobTexture(reg)));
    }

    public static <T extends BaseMonster> ResourceLocation mobTexture(EntityType<T> reg) {
        return new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/" + reg.getRegistryName().getPath() + ".png");
    }
}
