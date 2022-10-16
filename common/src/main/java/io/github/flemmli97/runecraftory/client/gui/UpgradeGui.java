package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerUpgrade;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class UpgradeGui extends AbstractContainerScreen<ContainerUpgrade> {

    private static final ResourceLocation forging = new ResourceLocation(RuneCraftory.MODID, "textures/gui/forgef.png");
    private static final ResourceLocation crafting = new ResourceLocation(RuneCraftory.MODID, "textures/gui/craftingf.png");

    public UpgradeGui(ContainerUpgrade container, Inventory inv, Component name) {
        super(container, inv, name);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        ResourceLocation texture = forging;
        //if(this.menu.craftingType() == EnumCrafting.ARMOR)
        //    texture = crafting;
        RenderSystem.setShaderTexture(0, texture);
        this.blit(stack, this.leftPos, this.topPos, 0, 0, 176, 166);
        PlayerData data = Platform.INSTANCE.getPlayerData(this.minecraft.player).orElse(null);
        if (this.menu.rpCost() >= 0) {
            int rpMax = data != null ? data.getMaxRunePoints() : 0;
            MutableComponent cost = new TextComponent("" + this.menu.rpCost());
            int yOffset = 0;
            if (rpMax < this.menu.rpCost() && !this.minecraft.player.isCreative()) {
                cost = new TranslatableComponent("crafting.rpMax.missing").withStyle(ChatFormatting.DARK_RED);
                yOffset = -25;
            }
            OverlayGui.drawStringCenter(stack, this.font, cost, this.leftPos + 91, this.topPos + 42 + yOffset, 0);
        }
        if (data != null) {
            stack.pushPose();
            float scale = 0.8f;
            int xPos = this.leftPos;
            int yPos = this.topPos - 12;
            stack.translate(xPos, yPos, 0);
            stack.scale(scale, scale, scale);
            RenderSystem.setShaderTexture(0, new ResourceLocation(RuneCraftory.MODID, "textures/gui/bars.png"));
            this.blit(stack, 0, 0, 131, 74, 96, 29);
            int runePointsWidth = Math.min(75, (int) (data.getRunePoints() / (float) data.getMaxRunePoints() * 75.0f));
            this.blit(stack, 18, 3, 18, 40, runePointsWidth, 9);
            ClientHandlers.drawCenteredScaledString(stack, this.font, data.getRunePoints() + "/" + data.getMaxRunePoints(), 18 + 75 * 0.5f, 5, 0.7f, 0xffffff);
            stack.popPose();
        }
    }
}
