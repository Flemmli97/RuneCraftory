package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.integration.simplequest.ClientSideQuestDisplay;
import io.github.flemmli97.runecraftory.common.network.C2SQuestSelect;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.List;
import java.util.Optional;

public class QuestGui extends Screen {

    protected static final ResourceLocation TEXTUREPATH = new ResourceLocation(RuneCraftory.MODID, "textures/gui/quest_gui.png");
    protected static final ResourceLocation TEXTUREPATH_WIDGETS = new ResourceLocation(RuneCraftory.MODID, "textures/gui/quest_gui_widgets.png");

    private final int textureX = 238;
    private final int textureY = 175;

    protected int leftPos;
    protected int topPos;

    protected final List<ClientSideQuestDisplay> quests;

    private Rect scrollBar = new Rect(212, 14, 12, 147);
    private Rect scrollArea = new Rect(14, 14, 210, 147);
    private final QuestButton[] questButtons = new QuestButton[7];
    private QuestSelectButton yesButton;
    private QuestSelectButton noButton;
    private int scrollValue;
    private boolean isDragging;

    private ClientSideQuestDisplay selectedQuest;

    public QuestGui(List<ClientSideQuestDisplay> quests) {
        super(new TextComponent(""));
        this.quests = quests;
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - this.textureX) / 2;
        this.topPos = (this.height - this.textureY) / 2;
        this.addRenderableWidget(this.yesButton = new QuestSelectButton(this.leftPos + 57, this.topPos + 92, false, b -> {
            Platform.INSTANCE.sendToServer(new C2SQuestSelect(this.selectedQuest.id(), this.selectedQuest.active()));
            Minecraft.getInstance().setScreen(null);
        }));
        this.addRenderableWidget(this.noButton = new QuestSelectButton(this.leftPos + 137, this.topPos + 92, true, b -> {
            this.selectedQuest = null;
            this.yesButton.visible = false;
            this.noButton.visible = false;
        }));
        for (int i = 0; i < 7; i++) {
            this.addRenderableWidget(this.questButtons[i] = new QuestButton(this.leftPos + 14, this.topPos + 14 + i * 21, i, b -> {
                if (b instanceof QuestButton but) {
                    this.selectedQuest = this.quests.get(but.getActualIndex());
                    this.yesButton.visible = true;
                    this.noButton.visible = true;
                }
            }));
        }
        this.scrollBar = new Rect(this.leftPos + this.scrollBar.x, this.topPos + this.scrollBar.y, this.scrollBar.width, this.scrollBar.height);
        this.scrollArea = new Rect(this.leftPos + this.scrollArea.x, this.topPos + this.scrollArea.y, this.scrollArea.width, this.scrollArea.height);
    }

    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTUREPATH);
        this.blit(stack, this.leftPos, this.topPos, 0, 0, this.textureX, this.textureY);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        this.renderBg(stack, partialTicks, mouseX, mouseY);
        for (QuestButton button : this.questButtons) {
            button.visible = button.index < this.quests.size();
            button.active = this.selectedQuest == null;
        }
        super.render(stack, mouseX, mouseY, partialTicks);
        if (this.selectedQuest != null) {
            RenderSystem.setShaderTexture(0, TEXTUREPATH_WIDGETS);
            this.blit(stack, this.leftPos + 52, this.topPos + 56, 13, 25, 134, 63);
            ClientHandlers.drawCenteredScaledString(stack, this.font, this.selectedQuest.active() ? new TranslatableComponent("runecraftory.gui.quests.reset").withStyle(ChatFormatting.RED) : new TranslatableComponent("runecraftory.gui.quests.accept"), this.leftPos + 52 + 67, this.topPos + 61, 1, 0);
        }
        this.yesButton.renderButtonSelect(stack);
        this.noButton.renderButtonSelect(stack);
        this.renderScroller(stack, this.scrollBar.x, this.scrollBar.y);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void renderScroller(PoseStack poseStack, int posX, int posY) {
        int steps = this.quests.size() - 7;
        int widgetHeight = 21;
        if (steps > 0) {
            int barHeight = this.scrollBar.height - 2;
            int moveableHeight = barHeight - widgetHeight;
            int k = moveableHeight / steps;
            int yOffset = Mth.clamp(this.scrollValue * k, 0, moveableHeight);
            RenderSystem.setShaderTexture(0, TEXTUREPATH_WIDGETS);
            this.blit(poseStack, posX, posY + yOffset, 0, 51, 12, widgetHeight);
        }
    }

    private boolean canScroll() {
        return this.quests.size() > 7;
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
            int max = this.quests.size() - 7;
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

    @Override
    public void removed() {
        super.removed();
        Platform.INSTANCE.sendToServer(new C2SQuestSelect());
    }

    public void setScrollValue(int val) {
        this.scrollValue = Mth.clamp(val, 0, Math.max(0, this.quests.size() - 7));
    }

    record Rect(int x, int y, int width, int height) {
        public boolean isMouseOver(double mouseX, double mouseY) {
            return mouseX >= this.x && mouseY >= this.y && mouseX < (this.x + this.width) && mouseY < (this.y + this.height);
        }
    }

    private class QuestButton extends Button {

        private final int index;

        public QuestButton(int i, int j, int k, OnPress onPress) {
            super(i, j, 198, 21, TextComponent.EMPTY, onPress);
            this.index = k;
            this.visible = false;
        }

        public int getActualIndex() {
            return this.index + QuestGui.this.scrollValue;
        }

        @Override
        public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
            if (this.getActualIndex() >= QuestGui.this.quests.size())
                return;
            if (this.isHoveredOrFocused() && QuestGui.this.selectedQuest == null) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, TEXTUREPATH_WIDGETS);
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.enableDepthTest();
                this.blit(poseStack, this.x, this.y, 0, 0, this.width, this.height);
            }
            ClientSideQuestDisplay display = QuestGui.this.quests.get(this.getActualIndex());
            Minecraft.getInstance().font.draw(poseStack, display.task(), this.x + 2, this.y + 6, display.active() ? 0x518203 : 0);
            this.renderToolTip(poseStack, mouseX, mouseY);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void renderToolTip(PoseStack poseStack, int relativeMouseX, int relativeMouseY) {
            if (this.isHovered && this.getActualIndex() < QuestGui.this.quests.size() && QuestGui.this.selectedQuest == null) {
                List<? extends Component> description = QuestGui.this.quests.get(this.getActualIndex()).description();
                if (!description.isEmpty())
                    QuestGui.this.renderTooltip(poseStack, (List<Component>) description, Optional.empty(), relativeMouseX, relativeMouseY + 24);
            }
        }
    }

    private static class QuestSelectButton extends Button {

        private final boolean red;

        public QuestSelectButton(int i, int j, boolean red, OnPress onPress) {
            super(i, j, 44, 22, red ? new TranslatableComponent("runecraftory.gui.quests.accept.no") : new TranslatableComponent("runecraftory.gui.quests.accept.yes"), onPress);
            this.visible = false;
            this.red = red;
        }

        @Override
        public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        }

        /**
         * Needs to render on top of selection background
         */
        public void renderButtonSelect(PoseStack poseStack) {
            if (!this.visible)
                return;
            Minecraft minecraft = Minecraft.getInstance();
            Font font = minecraft.font;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTUREPATH_WIDGETS);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
            int i = this.isHoveredOrFocused() ? 1 : 0;
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            this.blit(poseStack, this.x, this.y, 150 + i * 47, 26 + (this.red ? 24 : 0), this.width, this.height);
            ClientHandlers.drawCenteredScaledString(poseStack, font, this.getMessage(), this.x + this.width / 2f, this.y + (this.height - 8) / 2f, 1, 0);
        }
    }
}