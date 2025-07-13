package io.github.flemmli97.runecraftory.neoforge.client;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ArmorModels;
import io.github.flemmli97.runecraftory.client.ClientCalls;
import io.github.flemmli97.runecraftory.client.ClientRegister;
import io.github.flemmli97.runecraftory.client.render.RunecraftoryShaders;
import io.github.flemmli97.runecraftory.common.items.equipment.ItemArmorBase;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import java.io.IOException;
import java.util.function.Function;

public class ForgeClientRegister {

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ClientRegister.init();
        event.enqueueWork(() -> {
            // Cause multi loader
            ClientRegister.setupRenderLayers(ItemBlockRenderTypes::setRenderLayer);
            ClientRegister.registerItemProps(ItemProperties::register);
        });
    }

    @SubscribeEvent
    public static void initClientItemProps(RegisterClientExtensionsEvent event) {
        ModItems.ITEMS.getEntries().forEach(e -> {
            if (e.get() instanceof ItemArmorBase) {
                event.registerItem(new IClientItemExtensions() {
                    @Override
                    public Model getGenericArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
                        ArmorModels.ArmorModelGetter getter = ArmorModels.fromItemStack(stack);
                        if (getter == null)
                            return original;
                        Model model = getter.getModel(living, stack, slot, original);
                        return model != null ? model : original;
                    }
                }, e.get());
            }
        });
    }

    @SubscribeEvent
    public static void menuScreens(RegisterMenuScreensEvent event) {
        ClientRegister.registerScreen(new ClientRegister.MenuScreenRegister() {
            @Override
            public <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void register(MenuType<? extends M> type, ClientRegister.ScreenConstructor<M, U> provider) {
                event.register(type, provider::create);
            }
        });
    }

    @SubscribeEvent
    public static void tooltip(RegisterClientTooltipComponentFactoriesEvent event) {
        ClientRegister.registerTooltipComponentFactories(event::register);
    }

    @SubscribeEvent
    public static void overlay(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.EXPERIENCE_BAR, RuneCraftory.modRes("overlay_bar"),
                ClientCalls::renderScreenOverlays);
    }

    @SubscribeEvent
    public static void keyBindings(RegisterKeyMappingsEvent event) {
        ClientRegister.registerKeyBinding(event::register);
    }

    @SubscribeEvent
    public static void blockColors(RegisterColorHandlersEvent.Block event) {
        ClientRegister.registerBlockColors(event::register);
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
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        ClientRegister.registerParticles(new ClientRegister.PartileRegister() {
            @Override
            public <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> provider) {
                event.registerSpriteSet(type, provider::apply);
            }
        });
    }

    @SubscribeEvent
    public static void registerShader(RegisterShadersEvent event) throws IOException {
        RunecraftoryShaders.registerShader(((id, vertexFormat, onLoad) ->
                event.registerShader(new ShaderInstance(event.getResourceProvider(), id, vertexFormat), onLoad)));
    }
}
