package com.flemmli97.runecraftory.client.gui;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.client.gui.widgets.PageButton;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.capability.IPlayerCap;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import com.flemmli97.runecraftory.common.utils.CombatUtils;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import com.flemmli97.runecraftory.network.C2SOpenInfo;
import com.flemmli97.runecraftory.network.PacketHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class InfoScreen extends DisplayEffectsScreen<Container> {

    protected static final ResourceLocation texturepath = new ResourceLocation(RuneCraftory.MODID, "textures/gui/skills_1.png");
    protected static final ResourceLocation bars = new ResourceLocation(RuneCraftory.MODID, "textures/gui/bars.png");

    protected final IPlayerCap cap;
    private final int textureX = 223;
    private final int textureY = 198;
    private final ITextComponent levelTxt = new TranslationTextComponent("level");

    public InfoScreen(Container container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.cap = inv.player.getCapability(CapabilityInsts.PlayerCap).orElseThrow(() -> new NullPointerException("Error getting capability"));
    }

    @Override
    protected void init() {
        this.xSize = textureX;
        this.ySize = textureY;
        super.init();
        this.buttons();
    }

    protected void buttons() {
        this.addButton(new PageButton(this.guiLeft + 206, this.guiTop + 5, new StringTextComponent(">"), b -> PacketHandler.sendToServer(new C2SOpenInfo(C2SOpenInfo.Type.SUB))));
    }

    @Override
    protected void drawBackground(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        this.client.getTextureManager().bindTexture(this.texture());
        this.drawTexture(stack, this.guiLeft, this.guiTop, 0, 0, this.textureX, this.textureY);
        this.drawTexture(stack, this.guiLeft + 122, this.guiTop + 58, 226, 2, 8, 8);
        this.drawTexture(stack, this.guiLeft + 122, this.guiTop + 71, 226, 13, 8, 8);
        this.drawTexture(stack, this.guiLeft + 122, this.guiTop + 84, 226, 24, 8, 8);
        this.drawTexture(stack, this.guiLeft + 122, this.guiTop + 97, 226, 35, 8, 8);

        int healthWidth = Math.min(100, (int) ((this.cap.getHealth(this.client.player)) / (this.cap.getMaxHealth(this.client.player)) * 100.0));
        int runeWidth = Math.min(100, (int) (this.cap.getRunePoints() / (float) this.cap.getMaxRunePoints() * 100.0f));
        int exp = (int) (this.cap.getPlayerLevel()[1] / (float) LevelCalc.xpAmountForLevelUp(this.cap.getPlayerLevel()[0]) * 100.0f);
        this.client.getTextureManager().bindTexture(bars);
        this.drawTexture(stack, this.guiLeft + 117, this.guiTop + 20, 2, 51, healthWidth, 6);
        this.drawTexture(stack, this.guiLeft + 117, this.guiTop + 30, 2, 58, runeWidth, 6);
        this.drawTexture(stack, this.guiLeft + 117, this.guiTop + 41, 2, 66, exp, 9);
        this.drawCenteredScaledString(stack, (int) this.cap.getHealth(this.client.player) + "/" + (int) this.cap.getMaxHealth(this.client.player), this.guiLeft + 173, this.guiTop + 21f, 0.7f, 0xffffff);
        this.drawCenteredScaledString(stack, this.cap.getRunePoints() + "/" + this.cap.getMaxRunePoints(), this.guiLeft + 173, this.guiTop + 30.5f, 0.7f, 0xffffff);
        this.client.fontRenderer.draw(stack, levelTxt, this.guiLeft + 120, this.guiTop + 42, 0);
        this.drawRightAlignedScaledString(stack, "" + this.cap.getMoney(), this.guiLeft + 195, this.guiTop + 9.25f, 0.6f, 0);
        this.drawRightAlignedScaledString(stack, "" + this.cap.getPlayerLevel()[0], this.guiLeft + 216, this.guiTop + 42, 1.0f, 0);
        this.drawRightAlignedScaledString(stack, "" + (int) CombatUtils.getAttributeValue(this.client.player, Attributes.GENERIC_ATTACK_DAMAGE, null), this.guiLeft + 215, this.guiTop + 59, 1.0f, 0);
        this.drawRightAlignedScaledString(stack, "" + (int) CombatUtils.getAttributeValue(this.client.player, ModAttributes.RF_DEFENCE.get(), null), this.guiLeft + 215, this.guiTop + 72, 1.0f, 0);
        this.drawRightAlignedScaledString(stack, "" + (int) CombatUtils.getAttributeValue(this.client.player, ModAttributes.RF_MAGIC.get(), null), this.guiLeft + 215, this.guiTop + 85, 1.0f, 0);
        this.drawRightAlignedScaledString(stack, "" + (int) CombatUtils.getAttributeValue(this.client.player, ModAttributes.RF_MAGIC_DEFENCE.get(), null), this.guiLeft + 215, this.guiTop + 98, 1.0f, 0);
        InventoryScreen.drawEntity(this.guiLeft + 58, this.guiTop + 76, 29, this.guiLeft + 58 - mouseX, this.guiTop + 26 - mouseY, this.client.player);
    }

    protected ResourceLocation texture() {
        return texturepath;
    }

    private void drawCenteredScaledString(MatrixStack stack, String string, float x, float y, float scale, int color) {
        stack.push();
        stack.scale(scale, scale, scale);
        float xCenter = x - this.client.fontRenderer.getStringWidth(string) / 2;
        int xScaled = (int) (xCenter / scale);
        int yScaled = (int) (y / scale);
        this.client.fontRenderer.draw(stack, string, xScaled, yScaled, color);
        stack.pop();
    }

    public void drawRightAlignedScaledString(MatrixStack stack, String string, float x, float y, float scale, int color) {
        stack.push();
        stack.scale(scale, scale, scale);
        float xCenter = x - this.client.fontRenderer.getStringWidth(string);
        int xScaled = (int) (xCenter / scale);
        int yScaled = (int) (y / scale);
        this.client.fontRenderer.draw(stack, string, xScaled, yScaled, color);
        stack.pop();
    }

    @Override
    protected void drawForeground(MatrixStack stack, int mouseX, int mouseY) {

    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);
        this.drawMouseoverTooltip(stack, mouseX, mouseY);
    }
}