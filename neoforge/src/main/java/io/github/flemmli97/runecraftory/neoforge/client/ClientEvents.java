package io.github.flemmli97.runecraftory.neoforge.client;

import com.mojang.datafixers.util.Either;
import io.github.flemmli97.runecraftory.client.BossBarTracker;
import io.github.flemmli97.runecraftory.client.ClientCalls;
import io.github.flemmli97.runecraftory.client.ClientFarmlandHandler;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.ArrayList;
import java.util.List;

public class ClientEvents {

    public static void register(IEventBus modBus) {
        modBus.register(ForgeClientRegister.class);
        NeoForge.EVENT_BUS.register(ClientEvents.class);
        BossBarTracker.register();
    }

    @SubscribeEvent(receiveCanceled = true)
    public static void keyEvent(ClientTickEvent.Pre event) {
        ClientCalls.clientTick();
    }

    @SubscribeEvent
    public static void disableHandle(MovementInputUpdateEvent event) {
        ClientCalls.handleInputUpdate(event.getEntity(), event.getInput());
    }

    @SubscribeEvent
    public static void renderRunePoints(RenderGuiLayerEvent.Pre event) {
        if (ClientConfig.renderHealthRpBar == ClientConfig.HealthRPRenderType.BOTH && event.getName().equals(VanillaGuiLayers.PLAYER_HEALTH))
            event.setCanceled(true);
        if (GeneralConfig.disableHunger && event.getName().equals(VanillaGuiLayers.FOOD_LEVEL))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void initSkillTab(ScreenEvent.Init.Post event) {
        ClientCalls.initSkillTab(event.getScreen(), event::addListener);
    }

    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent event) {
        ClientCalls.tooltipEvent(event.getItemStack(), event.getToolTip(), event.getFlags());
    }

    @SubscribeEvent
    public static void tooltipComp(RenderTooltipEvent.GatherComponents event) {
        if (event.getItemStack().isEmpty())
            return;
        List<Either<FormattedText, TooltipComponent>> elements = new ArrayList<>();
        ClientCalls.tooltipComponentEvent(event.getItemStack(), c -> elements.add(Either.right(c)));
        event.getTooltipElements().addAll(1, elements);
    }

    @SubscribeEvent
    public static void worldRender(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES)
            ClientCalls.worldRender(event.getPoseStack());
    }

    @SubscribeEvent
    public static void shaking(ViewportEvent.ComputeCameraAngles event) {
        ClientCalls.renderShaking(event.getCamera(), event.getYaw(), event.getPitch(), event.getRoll(), (float) event.getPartialTick(), event::setYaw, event::setPitch, event::setRoll);
    }

    @SubscribeEvent
    public static void livingShaking(RenderLivingEvent.Pre<?, ?> event) {
        ClientCalls.renderEntityShake(event.getEntity(), event.getPoseStack(), event.getPartialTick());
    }

    @SubscribeEvent
    public static void logout(PlayerEvent.PlayerLoggedOutEvent event) {
        ClientFarmlandHandler.INSTANCE.onDisconnect();
    }

    @SubscribeEvent
    public static void bossbar(CustomizeGuiOverlayEvent.BossEventProgress event) {
        int i = BossBarTracker.tryRenderCustomBossbar(event.getGuiGraphics(), event.getX(), event.getY(), event.getBossEvent(), true);
        if (i != 0) {
            event.setIncrement(i);
            event.setCanceled(true);
        }
    }
}
