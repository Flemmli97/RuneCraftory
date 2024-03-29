package io.github.flemmli97.runecraftory.client;

import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.client.gui.widgets.SkillButton;
import io.github.flemmli97.runecraftory.common.capability.CapabilityInsts;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemMedicine;
import io.github.flemmli97.runecraftory.common.network.C2SOpenInfo;
import io.github.flemmli97.runecraftory.common.network.C2SRideJump;
import io.github.flemmli97.runecraftory.common.network.C2SSpellKey;
import io.github.flemmli97.runecraftory.common.network.PacketHandler;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.ArrayList;
import java.util.List;

public class ClientEvents {

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(ClientRegister::registerParticles);
        modBus.addListener(ClientRegister::registerRender);
    }

    @SubscribeEvent
    public void jump(LivingEvent.LivingUpdateEvent e) {
        if (e.getEntityLiving() instanceof ClientPlayerEntity && e.getEntityLiving().getRidingEntity() instanceof BaseMonster && ((ClientPlayerEntity) e.getEntityLiving()).movementInput.jump)
            PacketHandler.sendToServer(new C2SRideJump());
    }

    @SubscribeEvent(receiveCanceled = true)
    public void keyEvent(InputEvent.KeyInputEvent event) {
        if (ClientHandlers.spell1.onPress()) {
            PacketHandler.sendToServer(new C2SSpellKey(0));
        }
        if (ClientHandlers.spell2.onPress()) {
            PacketHandler.sendToServer(new C2SSpellKey(1));
        }
        if (ClientHandlers.spell3.onPress()) {
            PacketHandler.sendToServer(new C2SSpellKey(2));
        }
        if (ClientHandlers.spell4.onPress()) {
            PacketHandler.sendToServer(new C2SSpellKey(3));
        }
    }

    @SubscribeEvent
    public void renderRunePoints(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD || event.getType() == RenderGameOverlayEvent.ElementType.HEALTH)
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void initSkillTab(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof InventoryScreen || (event.getGui() instanceof CreativeScreen)) {
            int x = ((ContainerScreen<?>) event.getGui()).getGuiLeft();
            int y = ((ContainerScreen<?>) event.getGui()).getGuiTop();
            if (event.getGui() instanceof InventoryScreen) {
                x += ClientConfig.inventoryOffsetX;
                y += ClientConfig.inventoryOffsetY;
            } else {
                x += ClientConfig.creativeInventoryOffsetX;
                y += ClientConfig.creativeInventoryOffsetY;
            }
            event.addWidget(new SkillButton(x, y, event.getGui(), b -> PacketHandler.sendToServer(new C2SOpenInfo(C2SOpenInfo.Type.MAIN))));
        }
    }

    @SubscribeEvent
    public void renderRunePoints(RenderGameOverlayEvent.Post event) {
        if (event.isCancelable() || event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE)
            return;
        if (ClientHandlers.overlay != null)
            ClientHandlers.overlay.renderBar(event.getMatrixStack());
        if (ClientHandlers.spellDisplay != null)
            ClientHandlers.spellDisplay.render(event.getMatrixStack(), event.getPartialTicks());
    }

    @SubscribeEvent
    public void tooltipEvent(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.isEmpty()) {
            boolean showTooltip = true;
            if (stack.hasTag() && stack.getTag().contains("HideFlags", 99)) {
                showTooltip = (stack.getTag().getInt("HideFlags") & 0x20) == 0x0;
            }
            if (showTooltip) {
                event.getToolTip().addAll(1, this.injectAdditionalTooltip(stack));
            }
        }
    }

    private List<ITextComponent> injectAdditionalTooltip(ItemStack stack) {
        List<ITextComponent> tooltip = new ArrayList<>();
        boolean shift = Screen.hasShiftDown();
        DataPackHandler.getStats(stack.getItem()).ifPresent(stat -> tooltip.addAll(stat.texts(stack, shift)));
        CropProperties props = DataPackHandler.getCropStat(stack.getItem());
        if (props != null) {
            tooltip.addAll(props.texts());
        }
        if (shift) {
            FoodProperties food = DataPackHandler.getFoodStat(stack.getItem());
            if (food != null) {
                if (stack.getItem() instanceof ItemMedicine)
                    tooltip.addAll(food.medicineText((ItemMedicine) stack.getItem(), stack));
                else
                    tooltip.addAll(food.texts());
            }
        }
        return tooltip;
    }

    @SubscribeEvent
    public void worldRender(RenderWorldLastEvent event) {
        //if(WeatherData.get(Minecraft.getMinecraft().world).currentWeather()== EnumWeather.RUNEY)
        //    this.renderRuneyWeather(Minecraft.getMinecraft(), event.getPartialTicks());
        if (GeneralConfig.debugAttack) {
            AttackAABBRender.INST.render(event.getMatrixStack(), Minecraft.getInstance().getRenderTypeBuffers().getCrumblingBufferSource());
        }
    }

    /*@SubscribeEvent
    public void pathDebug(LivingEvent e) {
        if (e.getEntity() instanceof MobEntity && !e.getEntity().world.isRemote) {
            Path path = ((MobEntity) e.getEntity()).getNavigator().getPath();
            if (path != null) {
                for (int i = 0; i < path.getCurrentPathLength(); i++)
                    ((ServerWorld) e.getEntity().world).spawnParticle(ParticleTypes.NOTE, path.getPathPointFromIndex(i).x + 0.5, path.getPathPointFromIndex(i).y + 0.2, path.getPathPointFromIndex(i).z + 0.5, 1, 0, 0, 0, 0);
                ((ServerWorld) e.getEntity().world).spawnParticle(ParticleTypes.HEART, path.getFinalPathPoint().x + 0.5, path.getFinalPathPoint().y + 0.2, path.getFinalPathPoint().z + 0.5, 1, 0, 0, 0, 0);
            }
        }
    }*/

    @SubscribeEvent
    public void tick(LivingEvent.LivingUpdateEvent event) {
        Entity e = event.getEntity();
        if (e.world.isRemote && e instanceof LivingEntity) {
            e.getCapability(CapabilityInsts.EntityCap).ifPresent(cap -> {
                if (e.ticksExisted % 20 == 0) {
                    if (cap.isSleeping()) {
                        e.world.addParticle(ModParticles.sleep.get(), e.getPosX(), e.getPosY() + e.getHeight() + 0.1, e.getPosZ(), 0, 0, 0);
                    }
                    if (cap.isPoisoned()) {
                        e.world.addParticle(ModParticles.poison.get(), e.getPosX(), e.getPosY() + e.getHeight() + 0.1, e.getPosZ(), 0, 0, 0);
                    }
                }
            });
        }
    }
}
