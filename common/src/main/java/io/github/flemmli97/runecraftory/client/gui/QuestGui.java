package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.render.npc.NPCRender;
import io.github.flemmli97.runecraftory.client.render.npc.NPCTextureLayer;
import io.github.flemmli97.runecraftory.common.network.C2SQuestSelect;
import io.github.flemmli97.runecraftory.common.network.C2SSubmitQuestBoard;
import io.github.flemmli97.runecraftory.common.quests.ClientSideQuestDisplay;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCLooks;
import io.github.flemmli97.runecraftory.mixinhelper.GuiGraphicsExtension;
import io.github.flemmli97.tenshilib.client.gui.widget.TexturedButton;
import io.github.flemmli97.tenshilib.client.gui.widget.list.SelectableEntry;
import io.github.flemmli97.tenshilib.client.gui.widget.list.SelectableListWidget;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuestGui extends Screen {

    protected static final ResourceLocation BACKGROUND = RuneCraftory.modRes("textures/gui/misc/quest_gui.png");
    protected static final ResourceLocation POPUP_BACKGROUND = RuneCraftory.modRes("widget/quest_confirmation_popup");
    protected static final ResourceLocation SCROLLBAR = RuneCraftory.modRes("widget/quest_scrollbar");

    protected static final WidgetSprites ACCEPT = new WidgetSprites(RuneCraftory.modRes("widget/quest_confirm"), RuneCraftory.modRes("widget/quest_confirm_highlighted"));
    protected static final WidgetSprites DENY = new WidgetSprites(RuneCraftory.modRes("widget/quest_deny"), RuneCraftory.modRes("widget/quest_deny_highlighted"));
    protected static final WidgetSprites SUBMIT = new WidgetSprites(RuneCraftory.modRes("widget/quest_submit"),
            RuneCraftory.modRes("widget/quest_submit_disabled"), RuneCraftory.modRes("widget/quest_submit_highlighted"));

    private final int textureX = 238;
    private final int textureY = 175;

    protected int leftPos;
    protected int topPos;

    protected final List<ClientSideQuestDisplay> quests;
    protected final List<Pair<String, List<Pair<Integer, ResourceLocation>>>> heads;

    private SelectPopup popup;

    private ClientSideQuestDisplay selectedQuest;
    private final boolean hasActive;

    public QuestGui(boolean hasActive, List<ClientSideQuestDisplay> quests) {
        super(Component.literal(""));
        this.hasActive = hasActive;
        this.quests = quests;
        this.heads = this.quests.stream().map(display -> {
            // Resolve the textures of the npcs
            List<Pair<Integer, ResourceLocation>> textures = new ArrayList<>();
            if (display.features() != null) {
                for (NPCTextureLayer.LayerType layerType : NPCTextureLayer.LayerType.values()) {
                    ResourceLocation text = NPCRender.getTextureFromLook(display.features(), display.features().view.containsKey(RuneCraftoryNPCLooks.SLIM.get()), layerType, null);
                    if (!text.equals(NPCRender.EMPTY)) {
                        textures.add(Pair.of(NPCTextureLayer.setColor(display.features(), layerType), text));
                    }
                }
            }
            return Pair.of(display.npcSkin(), textures);
        }).toList();
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - this.textureX) / 2;
        this.topPos = (this.height - this.textureY) / 2;
        this.addRenderableWidget(new SubmitButton(this.leftPos + 238, this.topPos + 153, 20, 22)).active = this.hasActive;

        List<SelectableEntry> entries = new ArrayList<>();
        for (int i = 0; i < this.quests.size(); i++) {
            entries.add(new QuestEntry(i));
        }
        this.addRenderableWidget(new SelectableListWidget(this.leftPos + 14, this.topPos + 14, 210, 147, this.font, entries)
                .withPadding(21)
                .scrollbar(new SelectableListWidget.Scrollbar(SCROLLBAR, SCROLLBAR, 12, 21, 2, 0)));

        this.addRenderableOnly(this.popup = new SelectPopup(this.leftPos, this.topPos, () -> {
            LoaderNetwork.INSTANCE.sendToServer(new C2SQuestSelect(this.selectedQuest.id(), this.selectedQuest.active()));
            Minecraft.getInstance().setScreen(null);
        }, () -> this.selectedQuest = null));
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.textureX, this.textureY);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.popup.active = this.selectedQuest != null;
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.popup.mouseClicked(mouseX, mouseY, button))
            return true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (QuestGui.this.selectedQuest != null)
            return false;
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
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
        LoaderNetwork.INSTANCE.sendToServer(new C2SQuestSelect());
    }

    protected void selectQuest(int index) {
        this.selectedQuest = this.quests.get(index);
    }

    private class QuestEntry implements SelectableEntry {

        private final int index;

        private QuestEntry(int index) {
            this.index = index;
        }

        @Override
        public void updateDimensions(int width, int height) {
        }

        @SuppressWarnings("unchecked")
        @Override
        public void render(SelectableListWidget widget, GuiGraphics graphics, int mouseX, int mouseY, float partialTick, int x, int y, boolean selected, boolean hovered) {
            if (this.index < QuestGui.this.quests.size()) {
                boolean shouldHighlight = hovered && QuestGui.this.selectedQuest == null;
                if (shouldHighlight)
                    graphics.blitSprite(RuneCraftory.modRes("widget/quest_entry_background"), x, y, 198, 21);
                ClientSideQuestDisplay display = QuestGui.this.quests.get(this.index);
                Pair<String, List<Pair<Integer, ResourceLocation>>> head = QuestGui.this.heads.get(this.index);
                int offset = NPCRender.renderForTooltip(graphics, x + 2, y + 2,
                        head.getFirst(), head.getSecond()) ? 18 : 0;
                graphics.drawString(widget.getFont(), display.task(), x + 2 + offset, y + 6, display.active() ? 0x518203 : 0, false);
                if (shouldHighlight && !display.description().isEmpty())
                    graphics.renderTooltip(widget.getFont(), (List<Component>) display.description(), Optional.empty(), mouseX, mouseY);
            }
        }

        @Override
        public boolean onClick(double relativeMouseX, double relativeMouseY, boolean selected) {
            QuestGui.this.selectQuest(this.index);
            return true;
        }
    }

    private class SelectPopup implements Renderable {

        private final int leftPos, topPos;
        private final AbstractWidget yesButton;
        private final AbstractWidget noButton;

        private boolean active;

        public SelectPopup(int leftPos, int topPos, Runnable acceptCallback, Runnable denyCallback) {
            this.leftPos = leftPos;
            this.topPos = topPos;
            this.yesButton = new TexturedButton(this.leftPos + 57, this.topPos + 92, 44, 22,
                    Component.translatable("runecraftory.generic.yes"), b -> {
                acceptCallback.run();
            }).withSprite(ACCEPT);
            this.noButton = new TexturedButton(this.leftPos + 137, this.topPos + 92, 44, 22,
                    Component.translatable("runecraftory.generic.no"), b -> {
                denyCallback.run();
            }).withSprite(DENY);
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            if (!this.active)
                return;
            graphics.blitSprite(RuneCraftory.modRes("widget/quest_confirmation_popup"), this.leftPos + 52, this.topPos + 56,
                    134, 63);
            GuiGraphicsExtension.drawCenteredString(graphics, QuestGui.this.font,
                    QuestGui.this.selectedQuest.active() ? Component.translatable("runecraftory.gui.quests.reset").withStyle(ChatFormatting.RED) : Component.translatable("runecraftory.gui.quests.accept"),
                    this.leftPos + 52 + 134 * 0.5f, this.topPos + 56 + 8, 0, false);
            this.yesButton.render(graphics, mouseX, mouseY, partialTick);
            this.noButton.render(graphics, mouseX, mouseY, partialTick);
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (!this.active)
                return false;
            return this.yesButton.mouseClicked(mouseX, mouseY, button) || this.noButton.mouseClicked(mouseX, mouseY, button);
        }
    }

    private static class SubmitButton extends TexturedButton {

        public SubmitButton(int x, int y, int width, int height) {
            super(x, y, width, height, Component.empty(), b -> {
                LoaderNetwork.INSTANCE.sendToServer(C2SSubmitQuestBoard.INSTANCE);
                Minecraft.getInstance().setScreen(null);
            });
            this.withSprite(SUBMIT);
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            super.renderWidget(graphics, mouseX, mouseY, partialTick);
            if (this.isHovered() && this.isActive()) {
                graphics.renderTooltip(Minecraft.getInstance().font, Component.translatable("runecraftory.gui.quest.submit.button"), mouseX, mouseY);
            }
        }
    }
}