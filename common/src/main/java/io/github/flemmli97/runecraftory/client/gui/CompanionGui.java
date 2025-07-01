package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.gui.widgets.SpriteResources;
import io.github.flemmli97.runecraftory.common.entities.utils.IBaseMob;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.mixinhelper.GuiGraphicsExtension;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

public abstract class CompanionGui<T extends LivingEntity & IBaseMob> extends Screen {

    protected static final ResourceLocation TEXTURE_PATH = RuneCraftory.modRes("textures/gui/misc/companion_gui.png");

    public static final ResourceLocation HEALTH = RuneCraftory.modRes("widget/health_bar");
    public static final ResourceLocation EXPERIENCE = RuneCraftory.modRes("widget/experience_bar");

    private final int textureX = 190;
    private final int textureY = 117;
    private final int sizeY = this.textureY + 70;
    private final Component levelTxt = Component.translatable("runecraftory.gui.level");
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
        this.topPos = (this.height - this.sizeY) / 2;
        this.buttons();
    }

    @Override
    protected void renderBlurredBackground(float partialTick) {
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        RenderSystem.setShaderTexture(0, this.texture());
        graphics.blit(RuneCraftory.modRes("textures/gui/misc/companion_gui.png"), this.leftPos, this.topPos, 0, 0, this.textureX, this.textureY);
        int iconX = 86;
        int iconY = 46;
        graphics.blitSprite(SpriteResources.ATTACK_ICON, this.leftPos + iconX, this.topPos + iconY, 8, 8);
        graphics.blitSprite(SpriteResources.DEFENCE_ICON, this.leftPos + iconX, this.topPos + iconY + 13, 8, 8);
        graphics.blitSprite(SpriteResources.MAGIC_ATTACK_ICON, this.leftPos + iconX, this.topPos + iconY + 13 * 2, 8, 8);
        graphics.blitSprite(SpriteResources.MAGIC_DEFENCE_ICON, this.leftPos + iconX, this.topPos + iconY + 13 * 3, 8, 8);
        graphics.blitSprite(SpriteResources.FRIENDSHIP_ICON, this.leftPos + iconX, this.topPos + iconY + 13 * 4, 8, 8);

        int healthWidth = Math.min(100, (int) (this.entity.getHealth() / this.entity.getMaxHealth() * 100.0));
        int exp = Math.min(100, (int) (this.entity.xpLevel().getProgress() * 100.0));
        int barX = 80;
        GuiUtils.drawBorderedBar(graphics, HEALTH, this.leftPos + barX, this.topPos + 18, 102, 11, healthWidth, 1, 1);
        GuiGraphicsExtension.drawCenteredString(graphics, this.font, (int) this.entity.getHealth() + "/" + (int) this.entity.getMaxHealth(),
                this.leftPos + barX + (102 + 1) * 0.5f, this.topPos + 20, 0xffffff, false);
        GuiUtils.drawBorderedBar(graphics, EXPERIENCE, this.leftPos + barX, this.topPos + 32, 102, 11, exp, 1, 1);
        graphics.drawString(this.font, this.levelTxt, this.leftPos + 83, this.topPos + 38, 0, false);
        GuiGraphicsExtension.drawRightAlignedString(graphics, this.font, "" + this.entity.xpLevel().getLevel(),
                this.leftPos + barX + 178, this.topPos + 38, 0, false);

        int statX = 177;
        int statY = 46;
        GuiGraphicsExtension.drawRightAlignedString(graphics, this.font, "" + (int) CombatUtils.getAttributeValue(this.entity, Attributes.ATTACK_DAMAGE),
                this.leftPos + statX, this.topPos + statY, 0, false);
        GuiGraphicsExtension.drawRightAlignedString(graphics, this.font, "" + (int) CombatUtils.getAttributeValue(this.entity, ModAttributes.DEFENCE.asHolder()),
                this.leftPos + statX, this.topPos + statY + 13, 0, false);
        GuiGraphicsExtension.drawRightAlignedString(graphics, this.font, "" + (int) CombatUtils.getAttributeValue(this.entity, ModAttributes.MAGIC_ATTACK.asHolder()),
                this.leftPos + statX, this.topPos + statY + 13 * 2, 0, false);
        GuiGraphicsExtension.drawRightAlignedString(graphics, this.font, "" + (int) CombatUtils.getAttributeValue(this.entity, ModAttributes.MAGIC_DEFENCE.asHolder()),
                this.leftPos + statX, this.topPos + statY + 13 * 3, 0, false);
        GuiGraphicsExtension.drawRightAlignedString(graphics, this.font, "" + this.entity.friendPoints(this.minecraft.player),
                this.leftPos + statX, this.topPos + statY + 13 * 4, 0, false);

        RenderUtils.renderScaledEntityGui(graphics, this.leftPos + 13, this.topPos + 18, 52, 72, 27, 0, mouseX, mouseY, this.entity);
    }

    protected abstract void buttons();

    protected ResourceLocation texture() {
        return TEXTURE_PATH;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        GuiGraphicsExtension.drawCenteredString(graphics, this.font, this.title,
                this.leftPos + this.textureX * 0.5f, this.topPos + 7, 0, false);
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