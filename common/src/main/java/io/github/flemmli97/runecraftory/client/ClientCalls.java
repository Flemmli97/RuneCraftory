package io.github.flemmli97.runecraftory.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.client.gui.widgets.SkillButton;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemMedicine;
import io.github.flemmli97.runecraftory.common.network.C2SOpenInfo;
import io.github.flemmli97.runecraftory.common.network.C2SRideJump;
import io.github.flemmli97.runecraftory.common.network.C2SSpellKey;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.mixin.ContainerScreenAccessor;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ClientCalls {

    public static void keyEvent() {
        if (ClientHandlers.spell1.onPress()) {
            Platform.INSTANCE.sendToServer(new C2SSpellKey(0));
        }
        if (ClientHandlers.spell2.onPress()) {
            Platform.INSTANCE.sendToServer(new C2SSpellKey(1));
        }
        if (ClientHandlers.spell3.onPress()) {
            Platform.INSTANCE.sendToServer(new C2SSpellKey(2));
        }
        if (ClientHandlers.spell4.onPress()) {
            Platform.INSTANCE.sendToServer(new C2SSpellKey(3));
        }
    }

    public static void initSkillTab(Screen screen, Consumer<AbstractWidget> cons) {
        if (ClientConfig.inventoryButton) {
            if (screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen) {
                int x = ((ContainerScreenAccessor) screen).getLeft();
                int y = ((ContainerScreenAccessor) screen).getTop();
                if (screen instanceof InventoryScreen) {
                    x += ClientConfig.inventoryOffsetX;
                    y += ClientConfig.inventoryOffsetY;
                } else {
                    x += ClientConfig.creativeInventoryOffsetX;
                    y += ClientConfig.creativeInventoryOffsetY;
                }
                cons.accept(new SkillButton(x, y, screen, b -> Platform.INSTANCE.sendToServer(new C2SOpenInfo(C2SOpenInfo.Type.MAIN))));
            }
        }
    }

    public static void renderRunePoints(PoseStack stack, float partialTicks) {
        if (ClientHandlers.overlay != null && ClientConfig.renderOverlay)
            ClientHandlers.overlay.renderBar(stack);
        if (ClientHandlers.spellDisplay != null && ClientConfig.inventoryButton)
            ClientHandlers.spellDisplay.render(stack, partialTicks);
    }

    public static void tooltipEvent(ItemStack stack, List<Component> tooltip) {
        if (!stack.isEmpty()) {
            boolean showTooltip = true;
            if (stack.hasTag() && stack.getTag().contains("HideFlags", 99)) {
                showTooltip = (stack.getTag().getInt("HideFlags") & 0x20) == 0x0;
            }
            if (showTooltip) {
                tooltip.addAll(1, injectAdditionalTooltip(stack));
            }
        }
    }

    private static List<Component> injectAdditionalTooltip(ItemStack stack) {
        List<Component> tooltip = new ArrayList<>();
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

    public static void worldRender(PoseStack stack) {
        //if(WeatherData.get(Minecraft.getMinecraft().world).currentWeather()== EnumWeather.RUNEY)
        //    this.renderRuneyWeather(Minecraft.getMinecraft(), event.getPartialTicks());
        if (GeneralConfig.debugAttack) {
            AttackAABBRender.INST.render(stack, Minecraft.getInstance().renderBuffers().crumblingBufferSource());
        }
    }

    public static void tick(LivingEntity entity) {
        Platform.INSTANCE.getEntityData(entity).ifPresent(cap -> {
            if (entity.tickCount % 20 == 0) {
                if (cap.isSleeping()) {
                    entity.level.addParticle(ModParticles.sleep.get(), entity.getX(), entity.getY() + entity.getBbHeight() + 0.1, entity.getZ(), 0, 0, 0);
                }
                if (cap.isPoisoned()) {
                    entity.level.addParticle(ModParticles.poison.get(), entity.getX(), entity.getY() + entity.getBbHeight() + 0.1, entity.getZ(), 0, 0, 0);
                }
            }
        });
        if (entity instanceof LocalPlayer player && entity.getVehicle() instanceof BaseMonster && player.input.jumping)
            Platform.INSTANCE.sendToServer(new C2SRideJump());
    }
}