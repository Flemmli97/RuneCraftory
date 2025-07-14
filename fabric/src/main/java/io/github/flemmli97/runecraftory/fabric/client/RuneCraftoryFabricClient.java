package io.github.flemmli97.runecraftory.fabric.client;

import io.github.flemmli97.runecraftory.client.BossBarTracker;
import io.github.flemmli97.runecraftory.client.ClientCalls;
import io.github.flemmli97.runecraftory.client.ClientFarmlandHandler;
import io.github.flemmli97.runecraftory.client.ClientRegister;
import io.github.flemmli97.runecraftory.client.render.RunecraftoryShaders;
import io.github.flemmli97.runecraftory.common.items.equipment.ItemArmorBase;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryFluids;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.tenshilib.fabric.client.ClientSetupModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorResolverRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class RuneCraftoryFabricClient implements ClientSetupModInitializer {

    @Override
    public void clientSetup() {
        ClientRegister.init();

        ClientRegister.registerTooltipComponentFactories(TooltipRegistry::registerFactory);

        ClientRegister.registerKeyBinding(KeyBindingHelper::registerKeyBinding);
        ClientRegister.setupBlockRenderLayers(BlockRenderLayerMap.INSTANCE::putBlock);
        ClientRegister.setupFluidRenderLayers(BlockRenderLayerMap.INSTANCE::putFluid);
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
        ClientTickEvents.START_CLIENT_TICK.register(client -> ClientCalls.clientTick());
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> ClientCalls.initSkillTab(screen, Screens.getButtons(screen)::add));
        ItemTooltipCallback.EVENT.register((stack, context, flag, lines) -> ClientCalls.tooltipEvent(stack, lines, flag));
        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(ctx -> ClientCalls.worldRender(ctx.matrixStack()));
        RuneCraftoryItems.ITEMS.getEntries().forEach(e -> {
            if (e.get() instanceof ItemArmorBase) {
                ArmorRenderer.register(new ArmorRendererImpl(), e.get());
            }
        });
        ClientChunkEvents.CHUNK_UNLOAD.register(((world, chunk) -> ClientFarmlandHandler.INSTANCE.onChunkUnLoad(chunk.getPos())));
        CoreShaderRegistrationCallback.EVENT.register(reg -> RunecraftoryShaders.registerShader(reg::register));
        BossBarTracker.register();

        FluidRenderHandlerRegistry.INSTANCE.register(RuneCraftoryFluids.HOT_SPRING_WATER.get(), RuneCraftoryFluids.FLOWING_HOT_SPRING_WATER.get(), new SimpleFluidRenderHandler(SimpleFluidRenderHandler.WATER_STILL, SimpleFluidRenderHandler.WATER_FLOWING, SimpleFluidRenderHandler.WATER_OVERLAY, ClientRegister.HOT_SPRING_BASE) {
            @Override
            public int getFluidColor(@Nullable BlockAndTintGetter getter, @Nullable BlockPos pos, FluidState state) {
                return getter != null && pos != null ? getter.getBlockTint(pos, ClientRegister.HOT_SPRING_COLOR) | 0xff000000 : ClientRegister.HOT_SPRING_BASE;
            }
        });
        ColorResolverRegistry.register(ClientRegister.HOT_SPRING_COLOR);
    }
}
