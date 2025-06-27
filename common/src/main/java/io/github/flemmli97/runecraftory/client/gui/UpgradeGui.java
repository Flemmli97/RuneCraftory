package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerUpgrade;
import io.github.flemmli97.runecraftory.mixinhelper.GuiGraphicsExtension;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class UpgradeGui extends AbstractContainerScreen<ContainerUpgrade> {

    private static final ResourceLocation FORGING = RuneCraftory.modRes("textures/gui/forging_upgrade.png");
    private static final ResourceLocation CRAFTING = RuneCraftory.modRes("textures/gui/crafting_upgrade.png");
    private static final ResourceLocation BARS = RuneCraftory.modRes("textures/gui/bars.png");

    private final EnumSkills skill;

    public UpgradeGui(ContainerUpgrade container, Inventory inv, Component name) {
        super(container, inv, name);
        this.skill = switch (this.menu.craftingType()) {
            case FORGE -> EnumSkills.FORGING;
            case ARMOR -> EnumSkills.CRAFTING;
            case CHEM -> EnumSkills.CHEMISTRY;
            case COOKING -> EnumSkills.COOKING;
        };
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        ResourceLocation texture = FORGING;
        if (this.menu.craftingType() == EnumCrafting.ARMOR)
            texture = CRAFTING;
        graphics.blit(texture, this.leftPos, this.topPos, 0, 0, 176, 166);
        PlayerData data = Platform.INSTANCE.getPlayerData(this.minecraft.player);
        if (this.menu.rpCost() >= 0) {
            int rpMax = data != null ? data.getMaxRunePoints() : 0;
            MutableComponent cost = Component.literal("" + this.menu.rpCost());
            int yOffset = 0;
            if (rpMax < this.menu.rpCost() && !this.minecraft.player.isCreative()) {
                cost = Component.translatable("runecraftory.gui.crafting.rpMax.missing").withStyle(ChatFormatting.DARK_RED);
                yOffset = -25;
            }
            GuiGraphicsExtension.drawCenteredString(graphics, this.font, cost, this.leftPos + 91, this.topPos + 42 + yOffset, 0, false);
        }
        if (data != null) {
            PoseStack stack = graphics.pose();
            stack.pushPose();
            float scale = 0.8f;
            int xPos = this.leftPos;
            int yPos = this.topPos - 12;
            stack.translate(xPos, yPos, 0);
            stack.scale(scale, scale, scale);
            graphics.blit(BARS, 0, 0, 131, 74, 96, 29);
            int runePointsWidth = Math.min(76, (int) (data.getRunePoints() / (float) data.getMaxRunePoints() * 76.0f));
            graphics.blit(BARS, 17, 3, 18, 40, runePointsWidth, 9);
            GuiGraphicsExtension.drawCenteredString(graphics, this.font, data.getRunePoints() + "/" + data.getMaxRunePoints(), 18 + 75 * 0.5f, 5, 0xffffff, false);
            stack.popPose();
            graphics.drawString(this.font, Component.translatable("runecraftory.gui.display.level", data.getSkillLevel(this.skill).getLevel()),
                    this.leftPos + this.titleLabelX + this.font.width(this.title) + 6, this.topPos + this.titleLabelY, 0x404040);
        }
    }
}
