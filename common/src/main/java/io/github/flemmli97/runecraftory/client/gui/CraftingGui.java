package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.client.gui.widgets.PageButton;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import io.github.flemmli97.runecraftory.common.network.C2SUpdateCraftingScreen;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CraftingGui extends AbstractContainerScreen<ContainerCrafting> {

    private static final ResourceLocation forging = new ResourceLocation(RuneCraftory.MODID, "textures/gui/forgec.png");
    private static final ResourceLocation crafting = new ResourceLocation(RuneCraftory.MODID, "textures/gui/craftingc.png");
    private static final ResourceLocation chem = new ResourceLocation(RuneCraftory.MODID, "textures/gui/chemc.png");
    private static final ResourceLocation cooking = new ResourceLocation(RuneCraftory.MODID, "textures/gui/cookingc.png");
    //private static final ResourceLocation bars = new ResourceLocation(RuneCraftory.MODID, "textures/gui/bars.png");

    public CraftingGui(ContainerCrafting container, Inventory inv, Component name) {
        super(container, inv, name);
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(new PageButton(this.leftPos + 108, this.topPos + 60, new TextComponent("<"), b -> Platform.INSTANCE.sendToServer(new C2SUpdateCraftingScreen(false))));
        this.addRenderableWidget(new PageButton(this.leftPos + 128, this.topPos + 60, new TextComponent(">"), b -> Platform.INSTANCE.sendToServer(new C2SUpdateCraftingScreen(true))));
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
        /*texture = switch (this.menu.craftingType()) {
            case ARMOR -> crafting;
            case COOKING -> cooking;
            case FORGE -> forging;
            case CHEM -> chem;
        };*/
        RenderSystem.setShaderTexture(0, texture);
        this.blit(stack, this.leftPos, this.topPos, 0, 0, 176, 166);
        PlayerData data = Platform.INSTANCE.getPlayerData(this.minecraft.player).orElse(null);
        if (this.menu.rpCost() >= 0) {
            int rpMax = data != null ? data.getMaxRunePoints() : 0;
            MutableComponent cost = new TextComponent("" + this.menu.rpCost());
            if (rpMax < this.menu.rpCost() && !this.minecraft.player.isCreative()) {
                cost = new TranslatableComponent("crafting.rpMax.missing").withStyle(ChatFormatting.DARK_RED);
            }
            OverlayGui.drawStringCenter(stack, this.font, cost, this.leftPos + 123, this.topPos + 20, 0);
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
            this.drawCenteredScaledString(stack, data.getRunePoints() + "/" + data.getMaxRunePoints(), 60, 5, 0.7f, 0xffffff);
            stack.popPose();
        }
    }

    private void drawCenteredScaledString(PoseStack stack, String string, float x, float y, float scale, int color) {
        stack.pushPose();
        stack.scale(scale, scale, scale);
        float xCenter = x - this.minecraft.font.width(string) / 2;
        int xScaled = (int) (xCenter / scale);
        int yScaled = (int) (y / scale);
        this.minecraft.font.draw(stack, string, xScaled, yScaled, color);
        stack.popPose();
    }

    public EnumCrafting type() {
        return this.menu.craftingType();
    }

    public int getLeft() {
        return this.leftPos;
    }

    public int getTop() {
        return this.topPos;
    }
}
