package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.IBaseMob;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

public abstract class CompanionGui<T extends LivingEntity & IBaseMob> extends Screen {

    protected static final ResourceLocation texturepath = new ResourceLocation(RuneCraftory.MODID, "textures/gui/companion_gui.png");
    protected static final ResourceLocation bars = new ResourceLocation(RuneCraftory.MODID, "textures/gui/bars.png");

    private final int textureX = 190;
    private final int textureY = 113 + 70;
    private final Component levelTxt = new TranslatableComponent("level");
    protected int leftPos;
    protected int topPos;

    protected final T entity;

    public CompanionGui(T entity) {
        super(entity.getDisplayName());
        this.entity = entity;
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - this.textureX) / 2;
        this.topPos = (this.height - this.textureY) / 2;
        this.buttons();
    }

    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, this.texture());
        this.blit(stack, this.leftPos, this.topPos, 0, 0, this.textureX, this.textureY);
        int iconX = 86;
        int iconY = 42;
        this.blit(stack, this.leftPos + iconX, this.topPos + iconY, 226, 2, 8, 8);
        this.blit(stack, this.leftPos + iconX, this.topPos + iconY + 13, 226, 13, 8, 8);
        this.blit(stack, this.leftPos + iconX, this.topPos + iconY + 13 * 2, 226, 24, 8, 8);
        this.blit(stack, this.leftPos + iconX, this.topPos + iconY + 13 * 3, 226, 35, 8, 8);
        this.blit(stack, this.leftPos + iconX, this.topPos + iconY + 13 * 4, 226, 46, 8, 8);

        int healthWidth = Math.min(100, (int) (this.entity.getHealth() / this.entity.getMaxHealth() * 100.0));
        int exp = (int) (this.entity.level().getXp() / (float) LevelCalc.xpAmountForLevelUp(this.entity.level().getLevel()) * 100.0f);
        RenderSystem.setShaderTexture(0, bars);
        int barX = 81;
        this.blit(stack, this.leftPos + barX, this.topPos + 14, 2, 51, healthWidth, 6);
        this.blit(stack, this.leftPos + barX, this.topPos + 25, 2, 66, exp, 9);
        this.drawCenteredScaledString(stack, (int) this.entity.getHealth() + "/" + (int) this.entity.getMaxHealth(), this.leftPos + barX + 56, this.topPos + 15, 0.7f, 0xffffff);
        this.font.draw(stack, this.levelTxt, this.leftPos + barX + 3, this.topPos + 26, 0);
        this.drawRightAlignedScaledString(stack, "" + this.entity.level().getLevel(), this.leftPos + barX + 99, this.topPos + 26, 1.0f, 0);
        int statX = 179;
        int statY = 43;
        this.drawRightAlignedScaledString(stack, "" + CombatUtils.getAttributeValue(this.entity, Attributes.ATTACK_DAMAGE, null), this.leftPos + statX, this.topPos + statY, 1.0f, 0);
        this.drawRightAlignedScaledString(stack, "" + CombatUtils.getAttributeValue(this.entity, ModAttributes.RF_DEFENCE.get(), null), this.leftPos + statX, this.topPos + statY + 13, 1.0f, 0);
        this.drawRightAlignedScaledString(stack, "" + CombatUtils.getAttributeValue(this.entity, ModAttributes.RF_MAGIC.get(), null), this.leftPos + statX, this.topPos + statY + 13 * 2, 1.0f, 0);
        this.drawRightAlignedScaledString(stack, "" + CombatUtils.getAttributeValue(this.entity, ModAttributes.RF_MAGIC_DEFENCE.get(), null), this.leftPos + statX, this.topPos + statY + 13 * 3, 1.0f, 0);
        this.drawRightAlignedScaledString(stack, "" + this.entity.friendPoints(this.minecraft.player), this.leftPos + statX, this.topPos + statY + 13 * 4, 1.0f, 0);

        float scale = 1;
        if (this.entity.getBbWidth() > 1.2) {
            scale = 1.2f / this.entity.getBbWidth();
        }
        if (this.entity.getBbHeight() > 1.6) {
            scale = Math.min(scale, 1.6f / this.entity.getBbHeight());
        }
        InventoryScreen.renderEntityInInventory(this.leftPos + 39, this.topPos + 77, (int) (29 * scale), this.leftPos + 40 - mouseX, this.topPos + 27 - mouseY, this.entity);
    }

    protected abstract void buttons();

    protected ResourceLocation texture() {
        return texturepath;
    }

    protected void drawCenteredScaledString(PoseStack stack, String string, float x, float y, float scale, int color) {
        stack.pushPose();
        stack.scale(scale, scale, scale);
        float xCenter = x - (this.font.width(string) * 0.5f * scale);
        int xScaled = (int) (xCenter / scale);
        int yScaled = (int) (y / scale);
        this.font.draw(stack, string, xScaled, yScaled, color);
        stack.popPose();
    }

    protected void drawCenteredScaledString(PoseStack stack, Component txt, float x, float y, float scale, int color) {
        stack.pushPose();
        stack.scale(scale, scale, scale);
        float xCenter = x - (this.font.width(txt) * 0.5f * scale);
        int xScaled = (int) (xCenter / scale);
        int yScaled = (int) (y / scale);
        int yOffset = 0;
        for (FormattedCharSequence sub : this.font.split(txt, 64)) {
            this.font.draw(stack, sub, xScaled, yScaled + yOffset, color);
            yOffset += 9;
        }
        stack.popPose();
    }

    protected void drawRightAlignedScaledString(PoseStack stack, String string, float x, float y, float scale, int color) {
        stack.pushPose();
        stack.scale(scale, scale, scale);
        float xCenter = x - this.font.width(string) * scale;
        int xScaled = (int) (xCenter / scale);
        int yScaled = (int) (y / scale);
        this.font.draw(stack, string, xScaled, yScaled, color);
        stack.popPose();
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        this.renderBg(stack, partialTicks, mouseX, mouseY);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.drawCenteredScaledString(stack, this.title, this.leftPos + 38, this.topPos + 88, 0.8f, 0x404040);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        if (this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
            this.onClose();
            return true;
        }
        return true;
    }
}