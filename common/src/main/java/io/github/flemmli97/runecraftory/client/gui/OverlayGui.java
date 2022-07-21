package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.attachment.PlayerData;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.common.utils.CalendarImpl;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class OverlayGui extends GuiComponent {

    private static final ResourceLocation texturepath = new ResourceLocation(RuneCraftory.MODID, "textures/gui/bars.png");
    private final Minecraft mc;

    public OverlayGui(Minecraft mc) {
        this.mc = mc;
    }

    public static void drawStringCenter(PoseStack stack, Font fontRenderer, Component component, float x, float y, int color) {
        fontRenderer.draw(stack, component, x - fontRenderer.width(component) / 2f, y, color);
    }

    public void renderBar(PoseStack stack) {
        PlayerData data = Platform.INSTANCE.getPlayerData(this.mc.player).orElse(null);
        CalendarImpl calendar = ClientHandlers.clientCalendar;
        int xPos = ClientConfig.healthBarWidgetX;
        int yPos = ClientConfig.healthBarWidgetY;
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        EnumSeason season = calendar.currentSeason();
        RenderSystem.setShaderTexture(0, texturepath);
        if (data != null && !this.mc.player.getAbilities().invulnerable) {
            this.blit(stack, xPos, yPos, 0, 0, 96, 29);
            int healthWidth = Math.min(75, (int) (data.getHealth(this.mc.player) / data.getMaxHealth(this.mc.player) * 75.0f));
            int runePointsWidth = Math.min(75, (int) (data.getRunePoints() / (float) data.getMaxRunePoints() * 75.0f));
            this.blit(stack, xPos + 18, yPos + 3, 18, 30, healthWidth, 9);
            this.blit(stack, xPos + 18, yPos + 17, 18, 40, runePointsWidth, 9);
        }

        this.blit(stack, ClientConfig.seasonDisplayX, ClientConfig.seasonDisplayY, 50, 176, 37, 36);
        this.blit(stack, ClientConfig.seasonDisplayX + 3, ClientConfig.seasonDisplayY + 3, 0 + season.ordinal() * 32, 226, 32, 30);
        this.blit(stack, ClientConfig.seasonDisplayX, ClientConfig.seasonDisplayY + 39, 0, 176, 48, 17);

        drawStringCenter(stack, this.mc.font, new TranslatableComponent(calendar.currentDay().translation()).append(new TranslatableComponent(" " + calendar.date())), ClientConfig.seasonDisplayX + 26, ClientConfig.seasonDisplayY + 39 + 5, 0xbd1600);
    }
}