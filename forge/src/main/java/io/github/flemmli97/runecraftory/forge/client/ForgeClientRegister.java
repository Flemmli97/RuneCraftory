package io.github.flemmli97.runecraftory.forge.client;

import io.github.flemmli97.runecraftory.client.ClientRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Function;

public class ForgeClientRegister {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ClientRegister.init();
        ClientRegister.registerKeyBinding(ClientRegistry::registerKeyBinding);
        event.enqueueWork(() -> {
            ClientRegister.setupRenderLayers(ItemBlockRenderTypes::setRenderLayer);
            ClientRegister.registerItemProps(ItemProperties::register);
            ClientRegister.registerScreen(new ClientRegister.MenuScreenRegister() {
                @Override
                public <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void register(MenuType<? extends M> type, ClientRegister.ScreenConstructor<M, U> provider) {
                    MenuScreens.register(type, provider::create);
                }
            });
        });
    }

    @SubscribeEvent
    public static void entityRenders(EntityRenderersEvent.RegisterRenderers event) {
        ClientRegister.registerRenderers(event::registerEntityRenderer);
    }

    @SubscribeEvent
    public static void layerModels(EntityRenderersEvent.RegisterLayerDefinitions event) {
        ClientRegister.layerRegister(event::registerLayerDefinition);
    }

    @SubscribeEvent
    public static void registerParticles(ParticleFactoryRegisterEvent event) {
        ParticleEngine manager = Minecraft.getInstance().particleEngine;
        ClientRegister.registerParticles(new ClientRegister.PartileRegister() {
            @Override
            public <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> provider) {
                manager.register(type, provider::apply);
            }
        });
    }
}
