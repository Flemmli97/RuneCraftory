package io.github.flemmli97.runecraftory.client.gui;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.common.utils.CalendarImpl;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.SkullBlockEntity;

import java.util.function.Supplier;

public class OverlayGui extends GuiComponent {

    private static final ResourceLocation TEXTURE_PATH = new ResourceLocation(RuneCraftory.MODID, "textures/gui/bars.png");
    private final Minecraft mc;
    private final Supplier<ItemStack> playerHead = Suppliers.memoize(() -> {
        ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
        SkullBlockEntity.updateGameprofile(Minecraft.getInstance().player.getGameProfile(), prof -> stack.getOrCreateTag().put("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), prof)));
        return stack;
    });

    public OverlayGui(Minecraft mc) {
        this.mc = mc;
    }

    public static void drawStringCenter(PoseStack stack, Font fontRenderer, Component component, float x, float y, int color) {
        fontRenderer.draw(stack, component, x - fontRenderer.width(component) / 2f, y, color);
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
            this.renderPlayerHeadIcon(this.mc.getItemRenderer(), this.playerHead.get(), xPos, yPos);
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

            drawStringCenter(stack, this.mc.font, new TranslatableComponent(calendar.currentDay().translation()).append(new TranslatableComponent(" " + calendar.date())), ClientConfig.seasonDisplayX + 26, ClientConfig.seasonDisplayY + 39 + 5, 0xbd1600);
        }
    }

    private void renderPlayerHeadIcon(ItemRenderer renderer, ItemStack stack, int x, int y) {
        BakedModel bakedModel = renderer.getModel(stack, null, null, 0);
        try {
            boolean bl = !bakedModel.usesBlockLight();
            this.mc.getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            PoseStack poseStack = RenderSystem.getModelViewStack();
            poseStack.pushPose();
            poseStack.translate(x, y, -100.0f + renderer.blitOffset + 50);
            poseStack.translate(8.0, 0, 0.0);
            poseStack.scale(1.0f, -1.0f, 1.0f);
            poseStack.scale(32, 32, 32);
            RenderSystem.applyModelViewMatrix();
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            if (bl) {
                Lighting.setupForFlatItems();
            }
            renderer.render(stack, ItemTransforms.TransformType.NONE, false, new PoseStack(), bufferSource, 0xF000F0, OverlayTexture.NO_OVERLAY, bakedModel);
            bufferSource.endBatch();
            RenderSystem.enableDepthTest();
            if (bl) {
                Lighting.setupFor3DItems();
            }
            poseStack.popPose();
            RenderSystem.applyModelViewMatrix();
        } catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.forThrowable(throwable, "Rendering item");
            CrashReportCategory crashReportCategory = crashReport.addCategory("Item being rendered");
            crashReportCategory.setDetail("Item Type", () -> String.valueOf(stack.getItem()));
            crashReportCategory.setDetail("Item Damage", () -> String.valueOf(stack.getDamageValue()));
            crashReportCategory.setDetail("Item NBT", () -> String.valueOf(stack.getTag()));
            crashReportCategory.setDetail("Item Foil", () -> String.valueOf(stack.hasFoil()));
            throw new ReportedException(crashReport);
        }
    }
}