package io.github.flemmli97.runecraftory.client.gui;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerUpgrade;
import io.github.flemmli97.runecraftory.common.recipes.CraftingType;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.runecraftory.mixinhelper.GuiGraphicsExtension;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class UpgradeGui extends AbstractContainerScreen<ContainerUpgrade> {

    private static final ResourceLocation FORGING = RuneCraftory.modRes("textures/gui/container/forging_upgrade.png");
    private static final ResourceLocation CRAFTING = RuneCraftory.modRes("textures/gui/container/crafting_upgrade.png");

    public static final ResourceLocation RUNEPOINTS = RuneCraftory.modRes("widget/runepoints_bar");
    public static final ResourceLocation RUNEPOINTS_BORDER = RuneCraftory.modRes("widget/runepoints_bar_border");

    protected final PlayerData data;
    private final Skills skill;

    public UpgradeGui(ContainerUpgrade container, Inventory inv, Component name) {
        super(container, inv, name);
        this.data = RunecraftoryAttachments.PLAYER_DATA.get().get(inv.player);
        this.skill = this.menu.craftingType().skill;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        ResourceLocation texture = FORGING;
        if (this.menu.craftingType() == CraftingType.ACCESSORY_WORKBENCH)
            texture = CRAFTING;
        graphics.blit(texture, this.leftPos, this.topPos, 0, 0, 176, 166);
        if (this.menu.rpCost() >= 0) {
            int rpMax = this.data.getMaxRunePoints();
            MutableComponent cost = Component.literal("" + this.menu.rpCost());
            int yOffset = 0;
            if (rpMax < this.menu.rpCost() && !this.minecraft.player.isCreative()) {
                cost = Component.translatable("runecraftory.gui.crafting.rpMax.missing").withStyle(ChatFormatting.DARK_RED);
                yOffset = -25;
            }
            GuiGraphicsExtension.drawCenteredString(graphics, this.font, cost, this.leftPos + 91, this.topPos + 42 + yOffset, 0, false);
        }
        int xPos = this.leftPos;
        int yPos = this.topPos - 13;
        int barWidth = 102;
        int runeWidth = Math.min(100, (int) (this.data.getRunePoints() / (float) this.data.getMaxRunePoints() * 100.0f));
        graphics.blitSprite(RUNEPOINTS_BORDER, xPos, yPos, barWidth, 11);
        GuiUtils.drawBorderedBar(graphics, RUNEPOINTS,
                xPos, yPos, barWidth, 11, runeWidth, 1, 1);
        GuiGraphicsExtension.drawCenteredString(graphics, this.font, this.data.getRunePoints() + "/" + this.data.getMaxRunePoints(),
                xPos + (barWidth + 1) * 0.5f, yPos + 2, 0xffffff, false);

        graphics.drawString(this.font, Component.translatable("runecraftory.gui.display.level", this.data.getSkillLevel(this.skill).getLevel()),
                this.leftPos + this.titleLabelX + this.font.width(this.title) + 6, this.topPos + this.titleLabelY, 0x404040, false);
    }
}
