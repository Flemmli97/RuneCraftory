package io.github.flemmli97.runecraftory.fabric.client;

import io.github.flemmli97.runecraftory.client.ArmorModels;
import io.github.flemmli97.runecraftory.client.ClientCalls;
import io.github.flemmli97.runecraftory.client.ClientRegister;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.fabric.config.ClientConfigSpec;
import io.github.flemmli97.runecraftory.fabric.config.ConfigHolder;
import io.github.flemmli97.runecraftory.fabric.network.ClientPacketHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.fabric.impl.client.rendering.ArmorRendererRegistryImpl;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Function;

public class RuneCraftoryFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPacketHandler.registerClientPackets();
        ConfigHolder.configs.get(ClientConfigSpec.spec.getLeft())
                .reloadConfig();
        //ClientRegister
        ClientRegister.init();

        ClientRegister.registerTooltipComponentFactories(TooltipRegistry::registerFactory);

        ClientRegister.registerKeyBinding(KeyBindingHelper::registerKeyBinding);
        ClientRegister.setupRenderLayers(BlockRenderLayerMap.INSTANCE::putBlock);
        ClientRegister.registerItemProps(ItemProperties::register);
        ClientRegister.registerBlockColors(ColorProviderRegistry.BLOCK::register);
        ClientRegister.registerScreen(new ClientRegister.MenuScreenRegister() {
            @Override
            public <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void register(MenuType<? extends M> type, ClientRegister.ScreenConstructor<M, U> provider) {
                MenuScreens.register(type, provider::create);
            }
        });
        ClientRegister.registerRenderers(EntityRendererRegistry::register);
        ClientRegister.layerRegister((loc, sup) -> EntityModelLayerRegistry.registerModelLayer(loc, sup::get));

        ClientRegister.registerParticles(new ClientRegister.PartileRegister() {
            @Override
            public <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> provider) {
                ParticleFactoryRegistry.getInstance().register(type, provider::apply);
            }
        });

        //ClientCalls
        HudRenderCallback.EVENT.register(ClientCalls::renderRunePoints);
        ClientTickEvents.END_CLIENT_TICK.register(client -> ClientCalls.keyEvent());
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> ClientCalls.initSkillTab(screen, Screens.getButtons(screen)::add));
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> ClientCalls.tooltipEvent(stack, lines, context));
        WorldRenderEvents.END.register(ctx -> ClientCalls.worldRender(ctx.matrixStack()));
        ModItems.ITEMS.getEntries().forEach(e -> {
            ArmorModels.ArmorModelGetter r = ArmorModels.armorGetter.get(e.getID());
            if (r != null)
                ArmorRendererRegistryImpl.register(new ArmorRendererImpl(r, new ResourceLocation(e.getID().getNamespace(), "textures/models/armor/" + e.getID().getPath() + ".png")), e.get());
        });
    }
}
