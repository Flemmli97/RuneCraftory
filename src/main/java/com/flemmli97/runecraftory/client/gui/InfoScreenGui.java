package com.flemmli97.runecraftory.client.gui;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.capability.IPlayerCap;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import com.flemmli97.runecraftory.common.utils.MobUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class InfoScreenGui extends DisplayEffectsScreen<Container> {

    private static final ResourceLocation texturepath = new ResourceLocation(RuneCraftory.MODID,"textures/gui/skills.png");
    private static final ResourceLocation bars = new ResourceLocation(RuneCraftory.MODID,"textures/gui/bars.png");

    private final IPlayerCap cap;

    public InfoScreenGui(Container container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.cap = inv.player.getCapability(CapabilityInsts.PlayerCap).orElseThrow(()->new NullPointerException("Error getting capability"));
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - 218) / 2;
        this.guiTop = (this.height - 218) / 2;
        //this.buttonList.add(this.pageButton = new ButtonPage(0, this.guiX + 207, this.guiY + 7, ">"));
    }

    @Override
    protected void drawBackground(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        InventoryScreen.drawEntity(this.guiLeft + 58, this.guiTop + 76, 29, (float)(this.guiLeft + 58 - mouseX), (float)(this.guiTop + 76 - 50 - mouseY), this.client.player);
        this.client.getTextureManager().bindTexture(texturepath);
        this.drawTexture(stack, this.guiLeft, this.guiTop, 15, 15, 224, 224);
        int healthWidth = Math.min(100, (int)((this.cap.getHealth(this.client.player)) / (this.cap.getMaxHealth(this.client.player)) * 100.0));
        int runeWidth = Math.min(100, (int)(this.cap.getRunePoints() / (float)this.cap.getMaxRunePoints() * 100.0f));
        int exp = (int)(this.cap.getPlayerLevel()[1] / (float) LevelCalc.xpAmountForLevelUp(this.cap.getPlayerLevel()[0]) * 100.0f);
        this.client.getTextureManager().bindTexture(bars);
        this.drawTexture(stack, this.guiLeft + 118, this.guiTop + 23, 2, 51, healthWidth, 6);
        this.drawTexture(stack, this.guiLeft + 118, this.guiTop + 33, 2, 58, runeWidth, 6);
        this.drawTexture(stack, this.guiLeft + 118, this.guiTop + 44, 2, 66, exp, 12);
        this.drawCenteredScaledString(stack, (int)this.cap.getHealth(this.client.player) + "/" + (int)this.cap.getMaxHealth(this.client.player), this.guiLeft + 173, this.guiTop + 23.5f, 0.7f, 16777215);
        this.drawCenteredScaledString(stack, this.cap.getRunePoints() + "/" + this.cap.getMaxRunePoints(), this.guiLeft + 173, this.guiTop + 34, 0.7f, 16777215);
        this.client.fontRenderer.draw(stack, "Level", this.guiLeft + 120, this.guiTop + 46, 0);
        this.client.fontRenderer.draw(stack, "Att.", this.guiLeft + 120, this.guiTop + 63, 0);
        this.client.fontRenderer.draw(stack, "Def.", this.guiLeft + 120, this.guiTop + 75, 0);
        this.client.fontRenderer.draw(stack, "M.Att.", this.guiLeft + 120, this.guiTop + 87, 0);
        this.client.fontRenderer.draw(stack, "M.Def.", this.guiLeft + 120, this.guiTop + 99, 0);
        this.drawRightAlignedScaledString(stack, "" + this.cap.getMoney(), this.guiLeft + 194 - ((this.client.fontRenderer.getStringWidth("" + this.cap.getMoney()) == 6) ? 3 : 0), this.guiTop + 11, 0.6f, 0);
        this.drawRightAlignedScaledString(stack, "" + this.cap.getPlayerLevel()[0], this.guiLeft + 216, this.guiTop + 46, 1.0f, 0);
        this.drawRightAlignedScaledString(stack, "" + MobUtils.getAttributeValue(this.client.player, Attributes.GENERIC_ATTACK_DAMAGE, null), this.guiLeft + 216, this.guiTop + 64, 1.0f, 0);
        this.drawRightAlignedScaledString(stack, "" + MobUtils.getAttributeValue(this.client.player, ModAttributes.RF_DEFENCE.get(), null), this.guiLeft + 216, this.guiTop + 75, 1.0f, 0);
        this.drawRightAlignedScaledString(stack, "" + MobUtils.getAttributeValue(this.client.player, ModAttributes.RF_MAGIC.get(), null), this.guiLeft + 216, this.guiTop + 87, 1.0f, 0);
        this.drawRightAlignedScaledString(stack, "" + MobUtils.getAttributeValue(this.client.player, ModAttributes.RF_MAGIC_DEFENCE.get(), null), this.guiLeft + 216, this.guiTop + 99, 1.0f, 0);
    }

    private void drawCenteredScaledString(MatrixStack stack, String string, float x, float y, float scale, int color)
    {
        stack.push();
        stack.scale(scale, scale, scale);
        float xCenter = x - this.client.fontRenderer.getStringWidth(string) / 2;
        int xScaled = (int)(xCenter / scale);
        int yScaled = (int)(y / scale);
        this.client.fontRenderer.draw(stack, string, xScaled, yScaled, color);
        stack.pop();
    }

    private void drawRightAlignedScaledString(MatrixStack stack, String string, float x, float y, float scale, int color)
    {
        stack.push();
        stack.scale(scale, scale, scale);
        float xCenter = x - this.client.fontRenderer.getStringWidth(string);
        int xScaled = (int)(xCenter / scale);
        int yScaled = (int)(y / scale);
        this.client.fontRenderer.draw(stack, string, xScaled, yScaled, color);
        stack.pop();
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);
        this.drawMouseoverTooltip(stack, mouseX, mouseY);
    }
}
