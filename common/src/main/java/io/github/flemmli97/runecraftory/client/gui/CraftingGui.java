package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import io.github.flemmli97.runecraftory.common.network.C2SSelectRecipeCrafting;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CraftingGui extends AbstractContainerScreen<ContainerCrafting> {

    private static final ResourceLocation bars = new ResourceLocation(RuneCraftory.MODID, "textures/gui/bars.png");
    private static final ResourceLocation forging = new ResourceLocation(RuneCraftory.MODID, "textures/gui/forging.png");
    private static final ResourceLocation crafting = new ResourceLocation(RuneCraftory.MODID, "textures/gui/crafting.png");
    private static final ResourceLocation cooking = new ResourceLocation(RuneCraftory.MODID, "textures/gui/cooking.png");
    private static final ResourceLocation chemistry = new ResourceLocation(RuneCraftory.MODID, "textures/gui/chemistry.png");

    private Rect scrollBar = new Rect(195, 12, 8, 142);
    private Rect scrollArea = new Rect(172, 12, 31, 142);
    private final CraftingGui.RecipeSelectButton[] selectButtons = new CraftingGui.RecipeSelectButton[7];
    private int scrollValue;
    private boolean isDragging;

    private final EnumSkills skill;

    public CraftingGui(ContainerCrafting container, Inventory inv, Component name) {
        super(container, inv, name);
        this.imageWidth = 209;
        this.imageHeight = 166;
        this.skill = switch (this.menu.craftingType()) {
            case FORGE -> EnumSkills.FORGING;
            case ARMOR -> EnumSkills.CRAFTING;
            case CHEM -> EnumSkills.CHEMISTRY;
            case COOKING -> EnumSkills.COOKING;
        };
    }

    @Override
    protected void init() {
        super.init();
        for (int i = 0; i < 7; i++) {
            this.addRenderableWidget(this.selectButtons[i] = new RecipeSelectButton(this.leftPos + 173, this.topPos + 13 + i * 20, i, b -> {
                if (b instanceof RecipeSelectButton but)
                    Platform.INSTANCE.sendToServer(new C2SSelectRecipeCrafting(but.getActualIndex()));
            }));
        }
        this.scrollBar = new Rect(this.leftPos + this.scrollBar.x, this.topPos + this.scrollBar.y, this.scrollBar.width, this.scrollBar.height);
        this.scrollArea = new Rect(this.leftPos + this.scrollArea.x, this.topPos + this.scrollArea.y, this.scrollArea.width, this.scrollArea.height);
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        ResourceLocation texture = forging;
        texture = switch (this.menu.craftingType()) {
            case ARMOR -> crafting;
            case COOKING -> cooking;
            case FORGE -> forging;
            case CHEM -> chemistry;
        };
        RenderSystem.setShaderTexture(0, texture);
        this.blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
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
            RenderSystem.setShaderTexture(0, bars);
            this.blit(stack, 0, 0, 131, 74, 96, 29);
            int runePointsWidth = Math.min(76, (int) (data.getRunePoints() / (float) data.getMaxRunePoints() * 76.0f));
            this.blit(stack, 17, 3, 18, 40, runePointsWidth, 9);
            ClientHandlers.drawCenteredScaledString(stack, this.font, data.getRunePoints() + "/" + data.getMaxRunePoints(), 18 + 75 * 0.5f, 5, 0.7f, 0xffffff);
            stack.popPose();
            this.font.draw(stack, new TranslatableComponent("runecraftory.display.level", data.getSkillLevel(this.skill).getLevel()),
                    this.leftPos + this.titleLabelX + this.font.width(this.title) + 6, this.topPos + this.titleLabelY, 0x404040);
        }
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
        List<Pair<Integer, ItemStack>> recipes = this.menu.getMatchingRecipesClient();
        for (int i = this.scrollValue; i < this.scrollValue + 7; i++) {
            if (i < recipes.size()) {
                this.itemRenderer.blitOffset = 100.0f;
                this.itemRenderer.renderAndDecorateFakeItem(recipes.get(i).getSecond(), this.leftPos + 176, this.topPos + 15 + 20 * (i - this.scrollValue));
            }
        }
        for (CraftingGui.RecipeSelectButton button : this.selectButtons) {
            button.visible = button.index < this.menu.getMatchingRecipesClient().size();
        }
        RenderSystem.setShaderTexture(0, bars);
        this.renderScroller(stack, this.scrollBar.x + 1, this.scrollBar.y + 1);
    }

    private void renderScroller(PoseStack poseStack, int posX, int posY) {
        int steps = this.menu.getMatchingRecipesClient().size() - 7;
        int widgetHeight = 27;
        if (steps > 0) {
            int barHeight = this.scrollBar.height - 2;
            int moveableHeight = barHeight - widgetHeight;
            int k = moveableHeight / steps;
            int yOffset = Mth.clamp(this.scrollValue * k, 0, moveableHeight);
            this.blit(poseStack, posX, posY + yOffset, 185, 0, 6, widgetHeight);
        } else {
            this.blit(poseStack, posX, posY, 191, 0, 6, widgetHeight);
        }
    }

    private boolean canScroll() {
        return this.menu.getMatchingRecipesClient().size() > 7;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.canScroll() && this.scrollArea.isMouseOver(mouseX, mouseY)) {
            this.setScrollValue((int) (this.scrollValue - delta));
        }
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.isDragging) {
            int maxY = this.scrollBar.y + this.scrollBar.height - 3;
            int max = this.menu.getMatchingRecipesClient().size() - 7;
            float amount = ((float) mouseY - (float) this.scrollBar.y - 13.5f) / ((float) (maxY - this.scrollBar.y) - 27.0f);
            amount = amount * (float) max + 0.5f;
            this.scrollValue = Mth.clamp((int) amount, 0, max);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.isDragging = this.canScroll() && this.scrollBar.isMouseOver(mouseX, mouseY);
        return super.mouseClicked(mouseX, mouseY, button);
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

    public void setScrollValue(int val) {
        this.scrollValue = Mth.clamp(val, 0, Math.max(0, this.menu.getMatchingRecipesClient().size() - 7));
    }

    record Rect(int x, int y, int width, int height) {
        public boolean isMouseOver(double mouseX, double mouseY) {
            return mouseX >= this.x && mouseY >= this.y && mouseX < (this.x + this.width) && mouseY < (this.y + this.height);
        }
    }

    private class RecipeSelectButton extends Button {

        private final int index;

        public RecipeSelectButton(int i, int j, int k, OnPress onPress) {
            super(i, j, 22, 20, TextComponent.EMPTY, onPress);
            this.index = k;
            this.visible = false;
        }

        public int getActualIndex() {
            return this.index + CraftingGui.this.scrollValue;
        }

        @Override
        public void renderToolTip(PoseStack poseStack, int relativeMouseX, int relativeMouseY) {
            if (this.isHovered && this.getActualIndex() < CraftingGui.this.menu.getMatchingRecipesClient().size()) {
                ItemStack itemStack = CraftingGui.this.menu.getMatchingRecipesClient().get(this.getActualIndex()).getSecond();
                CraftingGui.this.renderTooltip(poseStack, itemStack, relativeMouseX, relativeMouseY);
            }
        }
    }
}
