package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.NPCData;
import io.github.flemmli97.runecraftory.client.gui.widgets.DialogueOptionButton;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.network.C2SDialogueAction;
import io.github.flemmli97.runecraftory.common.network.C2SNPCInteraction;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

public class NPCDialogueGui<T extends EntityNPCBase> extends Screen {

    private static final ResourceLocation TEXTURE = new ResourceLocation(RuneCraftory.MODID, "textures/gui/npc_dialogue.png");

    public static final int MAX_WIDTH = 200;
    public static final int MAX_HEIGHT = 73;
    public static final int BORDER_SIZE = 7;

    public static final int LINE_WIDTH = MAX_WIDTH - (2 * BORDER_SIZE);
    public static final int ACTION_WIDTH = 90;

    public static final int LINES_PER_PAGE = 5;

    private final int offSetX = (int) (MAX_WIDTH * 0.5);
    private final int offSetY = 30;
    protected int leftPos;
    protected int topPos;

    private int pageIndex, lineProgress, textProgress;

    protected final T entity;

    private NPCData.ConversationType type;
    private String conversationID;
    private List<ConversationLine> conversation;
    private List<Component> actions;

    private final List<DialogueOptionButton> buttons = new ArrayList<>();
    private boolean reset, calculateButtonVisiblity, removed;

    public NPCDialogueGui(T entity) {
        super(entity.getDisplayName());
        this.entity = entity;
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = this.width / 2 - this.offSetX;
        this.topPos = this.height / 2 + this.offSetY;
        this.buttons();
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTick) {
        if (this.removed) {
            this.onClose();
            return;
        }
        if (this.reset) {
            this.buttons();
            this.reset = false;
        }
        if (this.calculateButtonVisiblity) {
            if (this.buttonsVisible())
                this.showAllButtons();
            this.calculateButtonVisiblity = false;
        }
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1, 1, 1, 0.85f);
        this.blit(stack, this.leftPos, this.topPos, 5, 5, MAX_WIDTH, MAX_HEIGHT);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        int lines = Math.min(this.conversation.size(), (this.pageIndex + 1) * LINES_PER_PAGE);
        for (int i = this.pageIndex * LINES_PER_PAGE; i < lines; i++) {
            if (i > this.lineProgress)
                continue;
            ConversationLine txt = this.conversation.get(i);
            FormattedCharSequence txtRender = this.lineProgress == i ? Language.getInstance().getVisualOrder(this.font.substrByWidth(txt.raw(), this.textProgress))
                    : txt.txt();
            this.font.draw(stack, txtRender, this.leftPos + BORDER_SIZE, this.topPos + BORDER_SIZE + 13 * (i % LINES_PER_PAGE), 0xffffff);
        }
        super.render(stack, mouseX, mouseY, partialTick);
    }

    protected void buttons() {
        this.buttons.forEach(this::removeWidget);
        this.buttons.clear();
        boolean visible = this.buttonsVisible();
        int y = 0;
        for (int i = this.actions.size() - 1; i >= 0; i--) {
            int actionIdx = i;
            DialogueOptionButton btn = new DialogueOptionButton(this.width / 2, this.topPos - 20 + y, this.font, this.actions.get(i), b -> {
                if (this.type != null && this.conversationID != null)
                    Platform.INSTANCE.sendToServer(new C2SDialogueAction(this.entity.getId(), this.type, this.conversationID, actionIdx));
            });
            btn.y -= btn.getHeight();
            btn.visible = visible;
            this.buttons.add(btn);
            this.addRenderableWidget(btn);
            y -= btn.getHeight() + 8;
        }
    }

    @Override
    public void tick() {
        super.tick();
        int max = this.maxCurrentLineIndex();
        if (this.lineProgress <= max) {
            this.textProgress += 4;
            ConversationLine txt = this.conversation.get(this.lineProgress);
            if (this.textProgress > txt.width()) {
                this.lineProgress++;
                if (this.lineProgress >= this.conversation.size() - 1)
                    this.showAllButtons();
                else
                    this.textProgress = 0;
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int id = this.maxCurrentLineIndex();
        if (this.lineProgress < id || (this.lineProgress / LINES_PER_PAGE == this.pageIndex && this.lineProgress < this.conversation.size() && this.textProgress < this.conversation.get(this.lineProgress).width())) {
            this.lineProgress = id + 1;
            this.calculateButtonVisiblity = true;
        } else if (this.pageIndex + 1 <= (this.conversation.size() - 1) / LINES_PER_PAGE) {
            this.pageIndex += 1;
            this.textProgress = 0;
            this.calculateButtonVisiblity = true;
        } else if (this.buttons.isEmpty()) {
            this.removed = true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void showAllButtons() {
        this.buttons.forEach(b -> b.visible = true);
    }

    private int maxCurrentLineIndex() {
        return Math.min(6 + (this.pageIndex * LINES_PER_PAGE), this.conversation.size() - 1);
    }

    private boolean buttonsVisible() {
        return this.lineProgress >= this.conversation.size() - 1;
    }

    @Override
    public void removed() {
        super.removed();
        Minecraft.getInstance().options.keyUse.consumeClick();
        Minecraft.getInstance().options.keyUse.setDown(false);
        Platform.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), this.type == null ? C2SNPCInteraction.Type.CLOSE_QUEST : C2SNPCInteraction.Type.CLOSE, this.conversationID));
    }

    public void updateConversation(Minecraft mc, NPCData.ConversationType type, String conversationID, Component conversation, List<Component> actions) {
        this.conversation = mc.font.getSplitter().splitLines(conversation, LINE_WIDTH, conversation.getStyle())
                .stream().map(txt -> new ConversationLine(mc.font.width(txt), txt, Language.getInstance().getVisualOrder(txt))).toList();
        this.actions = actions;
        this.type = type;
        this.conversationID = conversationID;
        this.lineProgress = 0;
        this.textProgress = 0;
        this.pageIndex = 0;
        this.reset = true;
    }

    record ConversationLine(int width, FormattedText raw, FormattedCharSequence txt) {
    }
}