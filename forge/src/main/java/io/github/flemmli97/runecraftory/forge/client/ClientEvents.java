package io.github.flemmli97.runecraftory.forge.client;

import com.mojang.datafixers.util.Either;
import io.github.flemmli97.runecraftory.client.ArmorModels;
import io.github.flemmli97.runecraftory.client.BossBarTracker;
import io.github.flemmli97.runecraftory.client.ClientCalls;
import io.github.flemmli97.runecraftory.client.ClientFarmlandHandler;
import io.github.flemmli97.runecraftory.client.ClientRegister;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ClientEvents {

    public static void register() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.register(ForgeClientRegister.class);
        MinecraftForge.EVENT_BUS.register(ClientEvents.class);
        OverlayRegistry.registerOverlayAbove(ForgeIngameGui.EXPERIENCE_BAR_ELEMENT, "rf_overlay_bar", (gui, stack, partialTicks, guiX, guiY) -> ClientCalls.renderScreenOverlays(stack, partialTicks));
        ClientRegister.registerTooltipComponentFactories(MinecraftForgeClient::registerTooltipComponentFactory);
        BossBarTracker.register();
    }

    public static void initClientItemProps(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            @NotNull
            @Override
            public Model getBaseArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
                ArmorModels.ArmorModelGetter getter = ArmorModels.fromItemStack(itemStack);
                if (getter == null)
                    return _default;
                Model model = getter.getModel(entityLiving, itemStack, armorSlot, _default);
                if (model != null) {
                    return model;
                }
                return _default;
            }
        });
    }

    @SubscribeEvent(receiveCanceled = true)
    public static void keyEvent(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            ClientCalls.clientTick();
    }

    @SubscribeEvent
    public static void disableHandle(MovementInputUpdateEvent event) {
        ClientCalls.handleInputUpdate(event.getPlayer(), event.getInput());
    }

    @SubscribeEvent
    public static void renderRunePoints(RenderGameOverlayEvent.PreLayer event) {
        if (ClientConfig.renderHealthRpBar == ClientConfig.HealthRPRenderType.BOTH && event.getOverlay() == ForgeIngameGui.PLAYER_HEALTH_ELEMENT)
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
    public static void tooltipComp(RenderTooltipEvent.GatherComponents event) {
        if (event.getItemStack().isEmpty())
            return;
        List<Either<FormattedText, TooltipComponent>> elements = new ArrayList<>();
        ClientCalls.tooltipComponentEvent(event.getItemStack(), c -> elements.add(Either.right(c)), event.getScreenWidth(), event.getScreenHeight());
        event.getTooltipElements().addAll(1, elements);
    }

    @SubscribeEvent
    public static void worldRender(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES)
            ClientCalls.worldRender(event.getPoseStack());
    }

    @SubscribeEvent
    public static void shaking(EntityViewRenderEvent.CameraSetup event) {
        ClientCalls.renderShaking(event.getCamera(), event.getYaw(), event.getPitch(), event.getRoll(), (float) event.getPartialTicks(), event::setYaw, event::setPitch, event::setRoll);
    }

    @SubscribeEvent
    public static void shaking(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        ClientFarmlandHandler.INSTANCE.onDisconnect();
    }

    @SubscribeEvent
    public static void bossbar(RenderGameOverlayEvent.BossInfo event) {
        int i = BossBarTracker.tryRenderCustomBossbar(event.getMatrixStack(), event.getX(), event.getY(), event.getBossEvent(), true);
        if (i != 0) {
            event.setIncrement(i);
            event.setCanceled(true);
        }
    }
}
