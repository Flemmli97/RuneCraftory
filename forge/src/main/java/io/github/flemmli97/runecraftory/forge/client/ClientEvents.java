package io.github.flemmli97.runecraftory.forge.client;

import io.github.flemmli97.runecraftory.client.ClientCalls;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientEvents {

    public static void register() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.register(ForgeClientRegister.class);
        MinecraftForge.EVENT_BUS.register(ClientEvents.class);
        OverlayRegistry.registerOverlayTop("rf_overlay_bar", (gui, stack, partialTicks, guiX, guiY) -> ClientCalls.renderRunePoints(stack, partialTicks));
    }

    @SubscribeEvent(receiveCanceled = true)
    public static void keyEvent(TickEvent.ClientTickEvent event) {
        ClientCalls.keyEvent();
    }

    @SubscribeEvent
    public static void disableHandle(MovementInputUpdateEvent event) {
        ClientCalls.handleInputUpdate(event.getPlayer(), event.getInput());
    }

    @SubscribeEvent
    public static void renderRunePoints(RenderGameOverlayEvent.PreLayer event) {
        if (ClientConfig.renderHealthRPBar == ClientConfig.HealthRPRenderType.BOTH && event.getOverlay() == ForgeIngameGui.PLAYER_HEALTH_ELEMENT)
            event.setCanceled(true);
        if (GeneralConfig.disableHunger && event.getOverlay() == ForgeIngameGui.FOOD_LEVEL_ELEMENT)
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void initSkillTab(ScreenEvent.InitScreenEvent.Post event) {
        ClientCalls.initSkillTab(event.getScreen(), event::addListener);
    }

    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent event) {
        ClientCalls.tooltipEvent(event.getItemStack(), event.getToolTip(), event.getFlags());
    }

    @SubscribeEvent
    public static void worldRender(RenderLevelLastEvent event) {
        ClientCalls.worldRender(event.getPoseStack());
    }

    @SubscribeEvent
    public static void render(RenderLivingEvent.Pre<?, ?> event) {
        if (ClientCalls.invis(event.getEntity()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void shaking(EntityViewRenderEvent.CameraSetup event) {
        ClientCalls.renderShaking(event.getCamera(), event.getYaw(), event.getPitch(), event.getRoll(), (float) event.getPartialTicks(), event::setYaw, event::setPitch, event::setRoll);
    }
}
