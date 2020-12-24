package com.flemmli97.runecraftory.client;

import com.flemmli97.runecraftory.api.datapack.CropProperties;
import com.flemmli97.runecraftory.api.datapack.FoodProperties;
import com.flemmli97.runecraftory.api.datapack.ItemStat;
import com.flemmli97.runecraftory.common.datapack.DataPackHandler;
import com.flemmli97.runecraftory.common.entities.BaseMonster;
import com.flemmli97.runecraftory.network.C2SRideJump;
import com.flemmli97.runecraftory.network.PacketHandler;
import com.google.common.collect.Lists;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class ClientEvents {

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
    }

    @SubscribeEvent
    public void jump(LivingEvent.LivingUpdateEvent e) {
        if (e.getEntityLiving() instanceof ClientPlayerEntity && e.getEntityLiving().getRidingEntity() instanceof BaseMonster && ((ClientPlayerEntity) e.getEntityLiving()).movementInput.jump)
            PacketHandler.sendToServer(new C2SRideJump());
    }

    @SubscribeEvent
    public void renderRunePoints(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD || event.getType() == RenderGameOverlayEvent.ElementType.HEALTH)
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void renderRunePoints(RenderGameOverlayEvent.Post event) {
        if (event.isCancelable() || event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE)
            return;
        if (ClientHandlers.overlay != null)
            ClientHandlers.overlay.renderBar(event.getMatrixStack());
        if (ClientHandlers.spell != null)
            ClientHandlers.spell.render(event.getMatrixStack(), event.getPartialTicks());
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
        List<ITextComponent> tooltip = Lists.newArrayList();
        boolean shift = Screen.hasShiftDown();
        ItemStat stat = DataPackHandler.getStats(stack.getItem());
        if (stat != null) {
            tooltip.addAll(stat.texts(stack, shift));
        }
        CropProperties props = DataPackHandler.getCropStat(stack.getItem());
        if (props != null) {
            tooltip.addAll(props.texts());
        }
        if (shift) {
            FoodProperties food = DataPackHandler.getFoodStat(stack.getItem());
            if (food != null) {
                tooltip.addAll(food.texts());
            }
        }
        return tooltip;
    }

    @SubscribeEvent
    public void worldRender(RenderWorldLastEvent event) {
        /*if(WeatherData.get(Minecraft.getMinecraft().world).currentWeather()==EnumWeather.RUNEY)
            this.renderRuneyWeather(Minecraft.getMinecraft(), event.getPartialTicks());
        *///if(ConfigHandler.MainConfig.debugAttack)
        //AttackAABBRender.INST.render(event.getMatrixStack(), event.getContext().getEntityFramebuffer());
    }
}
