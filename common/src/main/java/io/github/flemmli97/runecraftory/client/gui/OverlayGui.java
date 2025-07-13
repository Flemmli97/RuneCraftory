package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.calendar.Season;
import io.github.flemmli97.runecraftory.client.ClientCalendarHolder;
import io.github.flemmli97.runecraftory.client.gui.widgets.SpriteResources;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.common.world.data.Calendar;
import io.github.flemmli97.runecraftory.mixinhelper.GuiGraphicsExtension;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class OverlayGui {

    private static final ResourceLocation HEALTH_BAR_BACKGROUND = RuneCraftory.modRes("hud/overlay/health_bar_background");
    private static final ResourceLocation HEALTH_BAR = RuneCraftory.modRes("hud/overlay/health_bar");
    private static final ResourceLocation RUNEPOINTS_BAR_BACKGROUND = RuneCraftory.modRes("hud/overlay/runepoints_bar_background");
    private static final ResourceLocation RUNEPOINTS_BAR = RuneCraftory.modRes("hud/overlay/runepoints_bar");
    private static final Map<Season, ResourceLocation> DATE = Arrays.stream(Season.values())
            .collect(Collectors.toUnmodifiableMap(
                    e -> e,
                    e -> RuneCraftory.modRes("hud/overlay/date_" + e.name().toLowerCase())
            ));

    private final Minecraft mc;

    public OverlayGui(Minecraft mc) {
        this.mc = mc;
    }

    public void renderBar(GuiGraphics graphics) {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        int guiWidth = this.mc.getWindow().getGuiScaledWidth();
        int guiHeight = this.mc.getWindow().getGuiScaledHeight();
        PlayerData data = Platform.INSTANCE.getPlayerData(this.mc.player);
        if (ClientConfig.renderHealthRpBar != ClientConfig.HealthRPRenderType.NONE) {
            int barWidth = 76;
            int yHeight = ClientConfig.renderHealthRpBar == ClientConfig.HealthRPRenderType.BOTH ? 2 * 9 + 12 : 9;
            int xPos = ClientConfig.healthBarWidgetPosition.positionX(guiWidth, barWidth, ClientConfig.healthBarWidgetX) + 1;
            int yPos = ClientConfig.healthBarWidgetPosition.positionY(guiHeight, yHeight, ClientConfig.healthBarWidgetY) + 1;
            if (data != null && !this.mc.player.isCreative()) {
                if (ClientConfig.renderHealthRpBar == ClientConfig.HealthRPRenderType.BOTH) {
                    graphics.blitSprite(HEALTH_BAR_BACKGROUND, xPos, yPos, barWidth, 9);
                    int healthWidth = Math.min(barWidth, (int) (this.mc.player.getHealth() / this.mc.player.getMaxHealth() * barWidth));
                    GuiUtils.drawBorderedBar(graphics, HEALTH_BAR, xPos, yPos, barWidth, 9, healthWidth, 1, 1);
                    yPos += 12;
                }
                graphics.blitSprite(RUNEPOINTS_BAR_BACKGROUND, xPos, yPos, barWidth, 9);
                int runePointsWidth = Math.min(barWidth, (int) (data.getRunePoints() / (float) data.getMaxRunePoints() * barWidth));
                GuiUtils.drawBorderedBar(graphics, RUNEPOINTS_BAR, xPos, yPos, barWidth, 9, runePointsWidth, 1, 1);
            }
        }
        if (ClientConfig.renderCalendar) {
            Calendar calendar = ClientCalendarHolder.CLIENT_CALENDAR;
            Season season = calendar.currentSeason();
            int xPos = ClientConfig.seasonDisplayPosition.positionX(guiWidth, 64, ClientConfig.seasonDisplayX);
            int yPos = ClientConfig.seasonDisplayPosition.positionY(guiHeight, 32 + 15 + 4, ClientConfig.seasonDisplayY);
            graphics.blitSprite(DATE.get(season), xPos, yPos, 64, 32);
            GuiGraphicsExtension.drawCenteredString(graphics, this.mc.font,
                    Component.translatable("runecraftory.gui.date.format", Component.translatable(calendar.date().day().translation()), calendar.date().date())
                            .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.BOLD),
                    xPos + 32, yPos + 15, 0, false);
            Component money = Component.literal(data.getMoney() + "")
                    .withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD);
            yPos += 32 + 4;
            xPos = ClientConfig.seasonDisplayPosition.positionX(guiWidth, this.mc.font.width(money), ClientConfig.seasonDisplayX + 4);
            graphics.blitSprite(SpriteResources.MONEY_ICON, xPos, yPos, 15, 15);
            graphics.drawString(this.mc.font, money, xPos + 20, yPos + 4, 0);
        }
    }
}