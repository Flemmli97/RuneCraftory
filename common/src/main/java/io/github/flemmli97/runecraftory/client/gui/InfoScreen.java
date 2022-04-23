package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.gui.widgets.PageButton;
import io.github.flemmli97.runecraftory.common.attachment.PlayerData;
import io.github.flemmli97.runecraftory.common.network.C2SOpenInfo;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class InfoScreen extends EffectRenderingInventoryScreen<AbstractContainerMenu> {

    protected static final ResourceLocation texturepath = new ResourceLocation(RuneCraftory.MODID, "textures/gui/skills_1.png");
    protected static final ResourceLocation bars = new ResourceLocation(RuneCraftory.MODID, "textures/gui/bars.png");

    protected final PlayerData data;
    private final int textureX = 223;
    private final int textureY = 198;
    private final Component levelTxt = new TranslatableComponent("level");

    public InfoScreen(AbstractContainerMenu container, Inventory inv, Component name) {
        super(container, inv, name);
        this.data = Platform.INSTANCE.getPlayerData(inv.player).orElseThrow(EntityUtils::playerDataException);
    }

    @Override
    protected void init() {
        this.imageWidth = this.textureX;
        this.imageHeight = this.textureY;
        super.init();
        this.buttons();
    }

    protected void buttons() {
        this.addRenderableWidget(new PageButton(this.leftPos + 206, this.topPos + 5, new TextComponent(">"), b -> Platform.INSTANCE.sendToServer(new C2SOpenInfo(C2SOpenInfo.Type.SUB))));
        this.addRenderableWidget(new PageButton(this.leftPos + 193, this.topPos + 5, new TextComponent("<"), b -> {
            InventoryScreen inventory = new InventoryScreen(this.minecraft.player);
            ItemStack stack = this.minecraft.player.containerMenu.getCarried();
            this.minecraft.player.containerMenu.setCarried(ItemStack.EMPTY);
            this.minecraft.setScreen(inventory);
            this.minecraft.player.containerMenu.setCarried(stack);
            Platform.INSTANCE.sendToServer(new C2SOpenInfo(C2SOpenInfo.Type.INV));
        }));
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, this.texture());
        this.blit(stack, this.leftPos, this.topPos, 0, 0, this.textureX, this.textureY);
        this.blit(stack, this.leftPos + 122, this.topPos + 58, 226, 2, 8, 8);
        this.blit(stack, this.leftPos + 122, this.topPos + 71, 226, 13, 8, 8);
        this.blit(stack, this.leftPos + 122, this.topPos + 84, 226, 24, 8, 8);
        this.blit(stack, this.leftPos + 122, this.topPos + 97, 226, 35, 8, 8);

        int healthWidth = Math.min(100, (int) ((this.data.getHealth(this.minecraft.player)) / (this.data.getMaxHealth(this.minecraft.player)) * 100.0));
        int runeWidth = Math.min(100, (int) (this.data.getRunePoints() / (float) this.data.getMaxRunePoints() * 100.0f));
        int exp = (int) (this.data.getPlayerLevel()[1] / (float) LevelCalc.xpAmountForLevelUp(this.data.getPlayerLevel()[0]) * 100.0f);
        RenderSystem.setShaderTexture(0, bars);
        this.blit(stack, this.leftPos + 117, this.topPos + 20, 2, 51, healthWidth, 6);
        this.blit(stack, this.leftPos + 117, this.topPos + 30, 2, 58, runeWidth, 6);
        this.blit(stack, this.leftPos + 117, this.topPos + 41, 2, 66, exp, 9);
        this.drawCenteredScaledString(stack, (int) this.data.getHealth(this.minecraft.player) + "/" + (int) this.data.getMaxHealth(this.minecraft.player), this.leftPos + 173, this.topPos + 21f, 0.7f, 0xffffff);
        this.drawCenteredScaledString(stack, this.data.getRunePoints() + "/" + this.data.getMaxRunePoints(), this.leftPos + 173, this.topPos + 30.5f, 0.7f, 0xffffff);
        this.minecraft.font.draw(stack, this.levelTxt, this.leftPos + 120, this.topPos + 42, 0);
        this.drawRightAlignedScaledString(stack, "" + this.data.getMoney(), this.leftPos + 195, this.topPos + 9.25f, 0.6f, 0);
        this.drawRightAlignedScaledString(stack, "" + this.data.getPlayerLevel()[0], this.leftPos + 216, this.topPos + 42, 1.0f, 0);
        this.drawRightAlignedScaledString(stack, "" + CombatUtils.getAttributeValue(this.minecraft.player, Attributes.ATTACK_DAMAGE, null), this.leftPos + 215, this.topPos + 59, 1.0f, 0);
        this.drawRightAlignedScaledString(stack, "" + CombatUtils.getAttributeValue(this.minecraft.player, ModAttributes.RF_DEFENCE.get(), null), this.leftPos + 215, this.topPos + 72, 1.0f, 0);
        this.drawRightAlignedScaledString(stack, "" + CombatUtils.getAttributeValue(this.minecraft.player, ModAttributes.RF_MAGIC.get(), null), this.leftPos + 215, this.topPos + 85, 1.0f, 0);
        this.drawRightAlignedScaledString(stack, "" + CombatUtils.getAttributeValue(this.minecraft.player, ModAttributes.RF_MAGIC_DEFENCE.get(), null), this.leftPos + 215, this.topPos + 98, 1.0f, 0);
        InventoryScreen.renderEntityInInventory(this.leftPos + 58, this.topPos + 76, 29, this.leftPos + 58 - mouseX, this.topPos + 26 - mouseY, this.minecraft.player);
    }

    protected ResourceLocation texture() {
        return texturepath;
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

    public void drawRightAlignedScaledString(PoseStack stack, String string, float x, float y, float scale, int color) {
        stack.pushPose();
        stack.scale(scale, scale, scale);
        float xCenter = x - this.minecraft.font.width(string);
        int xScaled = (int) (xCenter / scale);
        int yScaled = (int) (y / scale);
        this.minecraft.font.draw(stack, string, xScaled, yScaled, color);
        stack.popPose();
    }

    @Override
    protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {

    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
    }
}