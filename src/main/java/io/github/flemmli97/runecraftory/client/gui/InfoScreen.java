package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.gui.widgets.PageButton;
import io.github.flemmli97.runecraftory.common.capability.CapabilityInsts;
import io.github.flemmli97.runecraftory.common.capability.IPlayerCap;
import io.github.flemmli97.runecraftory.common.network.C2SOpenInfo;
import io.github.flemmli97.runecraftory.common.network.PacketHandler;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
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
        this.cap = inv.player.getCapability(CapabilityInsts.PlayerCap).orElseThrow(EntityUtils::capabilityException);
    }

    @Override
    protected void init() {
        this.xSize = this.textureX;
        this.ySize = this.textureY;
        super.init();
        this.buttons();
    }

    protected void buttons() {
        this.addButton(new PageButton(this.guiLeft + 206, this.guiTop + 5, new StringTextComponent(">"), b -> PacketHandler.sendToServer(new C2SOpenInfo(C2SOpenInfo.Type.SUB))));
        this.addButton(new PageButton(this.guiLeft + 193, this.guiTop + 5, new StringTextComponent("<"), b -> {
            InventoryScreen inventory = new InventoryScreen(this.minecraft.player);
            ItemStack stack = this.minecraft.player.inventory.getItemStack();
            this.minecraft.player.inventory.setItemStack(ItemStack.EMPTY);
            this.minecraft.displayGuiScreen(inventory);
            this.minecraft.player.inventory.setItemStack(stack);
            PacketHandler.sendToServer(new C2SOpenInfo(C2SOpenInfo.Type.INV));
        }));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bindTexture(this.texture());
        this.blit(stack, this.guiLeft, this.guiTop, 0, 0, this.textureX, this.textureY);
        this.blit(stack, this.guiLeft + 122, this.guiTop + 58, 226, 2, 8, 8);
        this.blit(stack, this.guiLeft + 122, this.guiTop + 71, 226, 13, 8, 8);
        this.blit(stack, this.guiLeft + 122, this.guiTop + 84, 226, 24, 8, 8);
        this.blit(stack, this.guiLeft + 122, this.guiTop + 97, 226, 35, 8, 8);

        int healthWidth = Math.min(100, (int) ((this.cap.getHealth(this.minecraft.player)) / (this.cap.getMaxHealth(this.minecraft.player)) * 100.0));
        int runeWidth = Math.min(100, (int) (this.cap.getRunePoints() / (float) this.cap.getMaxRunePoints() * 100.0f));
        int exp = (int) (this.cap.getPlayerLevel()[1] / (float) LevelCalc.xpAmountForLevelUp(this.cap.getPlayerLevel()[0]) * 100.0f);
        this.minecraft.getTextureManager().bindTexture(bars);
        this.blit(stack, this.guiLeft + 117, this.guiTop + 20, 2, 51, healthWidth, 6);
        this.blit(stack, this.guiLeft + 117, this.guiTop + 30, 2, 58, runeWidth, 6);
        this.blit(stack, this.guiLeft + 117, this.guiTop + 41, 2, 66, exp, 9);
        this.drawCenteredScaledString(stack, (int) this.cap.getHealth(this.minecraft.player) + "/" + (int) this.cap.getMaxHealth(this.minecraft.player), this.guiLeft + 173, this.guiTop + 21f, 0.7f, 0xffffff);
        this.drawCenteredScaledString(stack, this.cap.getRunePoints() + "/" + this.cap.getMaxRunePoints(), this.guiLeft + 173, this.guiTop + 30.5f, 0.7f, 0xffffff);
        this.minecraft.fontRenderer.drawText(stack, this.levelTxt, this.guiLeft + 120, this.guiTop + 42, 0);
        this.drawRightAlignedScaledString(stack, "" + this.cap.getMoney(), this.guiLeft + 195, this.guiTop + 9.25f, 0.6f, 0);
        this.drawRightAlignedScaledString(stack, "" + this.cap.getPlayerLevel()[0], this.guiLeft + 216, this.guiTop + 42, 1.0f, 0);
        this.drawRightAlignedScaledString(stack, "" + CombatUtils.getAttributeValue(this.minecraft.player, Attributes.ATTACK_DAMAGE, null), this.guiLeft + 215, this.guiTop + 59, 1.0f, 0);
        this.drawRightAlignedScaledString(stack, "" + CombatUtils.getAttributeValue(this.minecraft.player, ModAttributes.RF_DEFENCE.get(), null), this.guiLeft + 215, this.guiTop + 72, 1.0f, 0);
        this.drawRightAlignedScaledString(stack, "" + CombatUtils.getAttributeValue(this.minecraft.player, ModAttributes.RF_MAGIC.get(), null), this.guiLeft + 215, this.guiTop + 85, 1.0f, 0);
        this.drawRightAlignedScaledString(stack, "" + CombatUtils.getAttributeValue(this.minecraft.player, ModAttributes.RF_MAGIC_DEFENCE.get(), null), this.guiLeft + 215, this.guiTop + 98, 1.0f, 0);
        InventoryScreen.drawEntityOnScreen(this.guiLeft + 58, this.guiTop + 76, 29, this.guiLeft + 58 - mouseX, this.guiTop + 26 - mouseY, this.minecraft.player);
    }

    protected ResourceLocation texture() {
        return texturepath;
    }

    private void drawCenteredScaledString(MatrixStack stack, String string, float x, float y, float scale, int color) {
        stack.push();
        stack.scale(scale, scale, scale);
        float xCenter = x - this.minecraft.fontRenderer.getStringWidth(string) / 2;
        int xScaled = (int) (xCenter / scale);
        int yScaled = (int) (y / scale);
        this.minecraft.fontRenderer.drawString(stack, string, xScaled, yScaled, color);
        stack.pop();
    }

    public void drawRightAlignedScaledString(MatrixStack stack, String string, float x, float y, float scale, int color) {
        stack.push();
        stack.scale(scale, scale, scale);
        float xCenter = x - this.minecraft.fontRenderer.getStringWidth(string);
        int xScaled = (int) (xCenter / scale);
        int yScaled = (int) (y / scale);
        this.minecraft.fontRenderer.drawString(stack, string, xScaled, yScaled, color);
        stack.pop();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY) {

    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(stack, mouseX, mouseY);
    }
}