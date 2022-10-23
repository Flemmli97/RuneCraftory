package io.github.flemmli97.runecraftory.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.client.gui.widgets.SkillButton;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemMedicine;
import io.github.flemmli97.runecraftory.common.network.C2SOpenInfo;
import io.github.flemmli97.runecraftory.common.network.C2SRideJump;
import io.github.flemmli97.runecraftory.common.network.C2SSpellKey;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.mixin.ContainerScreenAccessor;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

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

    public static void handleInputUpdate(Player player, Input input) {
        if (EntityUtils.isDisabled(player)) {
            input.leftImpulse = 0;
            input.forwardImpulse = 0;
            input.up = false;
            input.down = false;
            input.left = false;
            input.right = false;
            input.jumping = false;
            input.shiftKeyDown = false;
        }
    }

    public static void renderRunePoints(PoseStack stack, float partialTicks) {
        if (ClientHandlers.overlay != null)
            ClientHandlers.overlay.renderBar(stack);
        if (ClientHandlers.spellDisplay != null && ClientConfig.inventoryButton)
            ClientHandlers.spellDisplay.render(stack, partialTicks);
        if (ClientHandlers.farmDisplay != null)
            ClientHandlers.farmDisplay.render(stack);
    }

    public static void tooltipEvent(ItemStack stack, List<Component> tooltip, TooltipFlag flag) {
        if (!stack.isEmpty()) {
            boolean showTooltip = true;
            if (stack.hasTag() && stack.getTag().contains("HideFlags", 99)) {
                showTooltip = (stack.getTag().getInt("HideFlags") & 0x20) == 0x0;
            }
            if (showTooltip) {
                Pair<List<Component>, List<Component>> p = injectAdditionalTooltip(stack, flag);
                tooltip.addAll(1, p.getFirst());
                tooltip.addAll(p.getSecond());
            }
        }
    }

    private static Pair<List<Component>, List<Component>> injectAdditionalTooltip(ItemStack stack, TooltipFlag flag) {
        List<Component> tooltip = new ArrayList<>();
        boolean shift = Screen.hasShiftDown();
        List<Component> debug = new ArrayList<>();
        DataPackHandler.getStats(stack.getItem()).ifPresent(stat -> {
            tooltip.addAll(stat.texts(stack, shift));
            if (flag.isAdvanced())
                debug.add(new TranslatableComponent("tooltip.debug.stat", stat.getId().toString()).withStyle(ChatFormatting.GRAY));
        });
        CropProperties props = DataPackHandler.getCropStat(stack.getItem());
        if (props != null) {
            tooltip.addAll(props.texts());
            if (flag.isAdvanced())
                debug.add(new TranslatableComponent("tooltip.debug.crop", props.getId().toString()).withStyle(ChatFormatting.GRAY));
        }
        if (shift) {
            FoodProperties food = DataPackHandler.getFoodStat(stack.getItem());
            if (food != null) {
                if (stack.getItem() instanceof ItemMedicine)
                    tooltip.addAll(food.medicineText((ItemMedicine) stack.getItem(), stack));
                else
                    tooltip.addAll(food.texts());
                if (flag.isAdvanced())
                    debug.add(new TranslatableComponent("tooltip.debug.food", food.getId().toString()).withStyle(ChatFormatting.GRAY));
            }
        }
        return Pair.of(tooltip, debug);
    }

    public static void worldRender(PoseStack stack) {
        if (GeneralConfig.debugAttack) {
            AttackAABBRender.INST.render(stack, Minecraft.getInstance().renderBuffers().crumblingBufferSource());
        }
    }

    public static void tick(LivingEntity entity) {
        Platform.INSTANCE.getEntityData(entity).ifPresent(data -> {
            int mod = entity.tickCount % 20;
            if (mod == 0 && data.isSleeping()) {
                entity.level.addParticle(ModParticles.sleep.get(), entity.getX(), entity.getY() + entity.getBbHeight() + 0.1, entity.getZ(), 0, 0, 0);

            }
            if (mod == 3 && data.isPoisoned()) {
                entity.level.addParticle(ModParticles.poison.get(), entity.getX(), entity.getY() + entity.getBbHeight() + 0.1, entity.getZ(), 0, 0, 0);
            }
            if (data.isParalysed()) {
                boolean bl2 = entity.isInvisible() ? entity.getRandom().nextInt(25) == 0 : entity.getRandom().nextInt(5) == 0;
                if (bl2) {
                    entity.level.addParticle(ModParticles.paralysis.get(), entity.getRandomX(0.5), entity.getRandomY(), entity.getRandomZ(0.5), 0.05, 0.05, 0.05);
                }
            }
        });
        if (entity instanceof LocalPlayer player && entity.getVehicle() instanceof BaseMonster && player.input.jumping)
            Platform.INSTANCE.sendToServer(new C2SRideJump());
        if (entity == Minecraft.getInstance().cameraEntity) {
            ShakeHandler.shakeTick--;
            if (ClientHandlers.isRuneyWeather) {
                int tries = Minecraft.getInstance().options.particles != ParticleStatus.ALL ? 1 : 2;
                for (int i = 0; i < tries; i++)
                    entity.level.addParticle(ModParticles.runey.get(),
                            entity.getX() + (entity.getRandom().nextDouble() - 0.5) * 24,
                            entity.getY() + (entity.getRandom().nextDouble() - 0.5) * 12,
                            entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * 24, 0, 0, 0);
            }
        }
    }

    public static boolean invis(LivingEntity entity) {
        return Platform.INSTANCE.getEntityData(entity).map(EntityData::isInvis).orElse(false);
    }

    public static void renderShaking(Camera camera, float yaw, float pitch, float roll, float partialTicks,
                                     Consumer<Float> setYaw, Consumer<Float> setPitch, Consumer<Float> setRoll) {
        /*if(ClientHandlers.orthorgraphicCam()) {
            Entity entity = Minecraft.getInstance().getCameraEntity();
            if(entity != null) {
                camera.setRotation(entity.getViewYRot(partialTicks), entity.getViewXRot(partialTicks));
                camera.setPosition(Mth.lerp(partialTicks, entity.xo, entity.getX()),
                        Mth.lerp(partialTicks, entity.yo, entity.getY()) + Mth.lerp(partialTicks, camera.eyeHeightOld, camera.eyeHeight),
                        Mth.lerp(partialTicks, entity.zo, entity.getZ()));
                camera.move(-camera.getMaxZoom(4.0), 3.0, 0.0);
                camera.setRotation(camera.getYRot(), camera.getXRot());
            }
        }*/
        int t = ShakeHandler.shakeTick;
        if (t <= 0)
            return;
        float pT = t * 2 - partialTicks;
        setPitch.accept(pitch + Mth.sin(pT * pT) * 2);
        setRoll.accept(roll + Mth.sin(pT * 2) * 2);
    }
}
