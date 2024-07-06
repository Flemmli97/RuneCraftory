package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.common.utils.CalendarImpl;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class OverlayGui extends GuiComponent {

    private static final ResourceLocation TEXTURE_PATH = new ResourceLocation(RuneCraftory.MODID, "textures/gui/bars.png");
    private final Minecraft mc;

    public OverlayGui(Minecraft mc) {
        this.mc = mc;
    }

    public void renderBar(PoseStack stack) {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE_PATH);
        int guiWidth = this.mc.getWindow().getGuiScaledWidth();
        int guiHeight = this.mc.getWindow().getGuiScaledHeight();
        if (ClientConfig.renderHealthRpBar != ClientConfig.HealthRPRenderType.NONE) {
            PlayerData data = Platform.INSTANCE.getPlayerData(this.mc.player).orElse(null);
            int barWidth = 76;
            int yHeight = ClientConfig.renderHealthRpBar == ClientConfig.HealthRPRenderType.BOTH ? 2 + 9 + 9 + 12 : 11;
            int xPos = ClientConfig.healthBarWidgetPosition.positionX(guiWidth, barWidth + 20 + 2, ClientConfig.healthBarWidgetX) + 1;
            int yPos = ClientConfig.healthBarWidgetPosition.positionY(guiHeight, yHeight, ClientConfig.healthBarWidgetY) + 1;
            this.renderHead(stack, xPos, yPos);
            RenderSystem.setShaderTexture(0, TEXTURE_PATH);
            xPos += 20;
            if (data != null && !this.mc.player.isCreative()) {
                if (ClientConfig.renderHealthRpBar == ClientConfig.HealthRPRenderType.BOTH) {
                    this.blit(stack, xPos, yPos, 19, 3, barWidth, 9);
                    int healthWidth = Math.min(barWidth, (int) (this.mc.player.getHealth() / this.mc.player.getMaxHealth() * barWidth));
                    this.blit(stack, xPos, yPos, 19, 28, healthWidth, 9);
                    yPos += 12;
                }
                this.blit(stack, xPos, yPos, 19, 15, barWidth, 9);
                int runePointsWidth = Math.min(barWidth, (int) (data.getRunePoints() / (float) data.getMaxRunePoints() * barWidth));
                this.blit(stack, xPos, yPos, 19, 40, runePointsWidth, 9);
            }
        }
        if (ClientConfig.renderCalendar) {
            CalendarImpl calendar = ClientHandlers.CLIENT_CALENDAR;
            EnumSeason season = calendar.currentSeason();
            int xPos = ClientConfig.seasonDisplayPosition.positionX(guiWidth, 37, ClientConfig.seasonDisplayX);
            int yPos = ClientConfig.seasonDisplayPosition.positionY(guiHeight, 36, ClientConfig.seasonDisplayY);
            this.blit(stack, xPos, yPos, 50, 176, 37, 36);
            this.blit(stack, xPos + 3, yPos + 3, season.ordinal() * 32, 226, 32, 30);
            this.blit(stack, xPos, yPos + 39, 0, 176, 48, 17);

            ClientHandlers.drawCenteredScaledString(stack, this.mc.font, new TranslatableComponent(calendar.currentDay().translation()).append(new TranslatableComponent(" " + calendar.date())),
                    ClientConfig.seasonDisplayX + 26, ClientConfig.seasonDisplayY + 39 + 5, 1, 0xbd1600);
        }
    }

    private void renderHead(PoseStack stack, int x, int y) {
        RenderSystem.setShaderTexture(0, this.mc.player.getSkinTextureLocation());
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        int sizeX = 16;
        int sizeY = 16;
        blit(stack, x, y, sizeX, sizeY, 8.0f, 8, 8, 8, 64, 64);
        RenderSystem.enableBlend();
        blit(stack, x, y, sizeX, sizeY, 40.0F, 8, 8, 8, 64, 64);
        RenderSystem.disableBlend();
    }
}