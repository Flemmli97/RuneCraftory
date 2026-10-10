package io.github.flemmli97.runecraftory.client.gui;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.npc.ConversationSet;
import io.github.flemmli97.runecraftory.client.gui.widgets.DialogueOptionButton;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.entities.npc.PlaceHolderComponent;
import io.github.flemmli97.runecraftory.common.network.C2SDialogueAction;
import io.github.flemmli97.runecraftory.common.network.C2SNPCInteraction;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NPCDialogueGui<T extends NPCEntity> extends Screen {

    private static final ResourceLocation TEXTURE = RuneCraftory.modRes("hud/npc_dialogue");
    private static final ResourceLocation NAME_PLATE = RuneCraftory.modRes("hud/npc_dialogue_name_plate");

    /**
     * Dummy data
     */
    private static final List<ConversationSet.NetworkConversation> TEST_CONVERSATION = List.of(
            new ConversationSet.NetworkConversation(Component.literal("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.")),
            new ConversationSet.NetworkConversation(Component.literal("Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.")),
            new ConversationSet.NetworkConversation(Component.literal("Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat."))
    );

    public static final int MAX_WIDTH = 200;
    public static final int MAX_HEIGHT = 73;
    public static final int BORDER_SIZE = 7;

    public static final int LINE_WIDTH = MAX_WIDTH - (2 * BORDER_SIZE);

    public static final int LINES_PER_PAGE = 5;

    protected int leftPos;
    protected int topPos;

    private int conversationIndex, pageIndex, lineProgress, textProgress;

    protected final T entity;

    private List<ConversationContainer> conversation;
    private Map<String, Component> replacements = Map.of();
    private Int2ObjectMap<ConversationSet.NetworkActions> actions;

    private final List<DialogueOptionButton> buttons = new ArrayList<>();
    private boolean reset, calculateButtonVisibility, removed;

    public NPCDialogueGui(T entity) {
        super(entity.getDisplayName());
        this.entity = entity;
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = this.width / 2 - (int) (MAX_WIDTH * 0.5);
        this.topPos = this.height / 2 + 30;
        this.buttons();
    }

    @Override
    protected void renderBlurredBackground(float partialTick) {
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        if (this.removed) {
            this.onClose();
            return;
        }
        if (this.reset) {
            this.buttons();
            this.reset = false;
        }
        if (this.calculateButtonVisibility) {
            if (this.buttonsVisible())
                this.showAllButtons();
            this.calculateButtonVisibility = false;
        }
        graphics.setColor(1, 1, 1, 0.85f);
        Component speaker = this.getCurrentContainer().speaker();
        int plateX = this.leftPos;
        int border = 5;
        int plateY = this.topPos - this.font.lineHeight - border * 2;
        graphics.blitSprite(NAME_PLATE, plateX, plateY, this.font.width(speaker) + border * 2, this.font.lineHeight + 5 * 2);
        graphics.blitSprite(TEXTURE, this.leftPos, this.topPos, MAX_WIDTH, MAX_HEIGHT);
        graphics.setColor(1, 1, 1, 1);

        graphics.drawString(this.font, speaker, plateX + border, plateY + border, 0xffffff, false);
        int lines = Math.min(this.getCurrentLines().size(), (this.pageIndex + 1) * LINES_PER_PAGE);
        for (int i = this.pageIndex * LINES_PER_PAGE; i < lines; i++) {
            if (i > this.lineProgress)
                continue;
            ConversationLine txt = this.getCurrentLines().get(i);
            FormattedCharSequence txtRender = this.lineProgress == i ? Language.getInstance().getVisualOrder(this.font.substrByWidth(txt.raw(), this.textProgress))
                    : txt.txt();
            graphics.drawString(this.font, txtRender, this.leftPos + BORDER_SIZE, this.topPos + BORDER_SIZE + 13 * (i % LINES_PER_PAGE), 0xffffff, false);
        }
    }

    protected void buttons() {
        this.buttons.forEach(this::removeWidget);
        this.buttons.clear();
        boolean visible = this.buttonsVisible();
        int y = 0;
        ConversationSet.NetworkActions actionsHolder = this.actions.get(this.conversationIndex);
        if (actionsHolder != null) {
            List<Component> actions = actionsHolder.actions();
            for (int i = actions.size() - 1; i >= 0; i--) {
                int actionIdx = i;
                DialogueOptionButton btn = new DialogueOptionButton(this.width / 2, this.topPos - 20 + y, this.font, PlaceHolderComponent.parseDialogueComponent(actions.get(i), this.replacements, this.getCurrentContainer().participants()),
                        b -> LoaderNetwork.INSTANCE.sendToServer(new C2SDialogueAction(this.entity.getId(), actionsHolder.setID(), actionsHolder.conversationID(), actionIdx)));
                btn.visible = visible;
                this.buttons.add(btn);
                this.addRenderableWidget(btn);
                y -= btn.getHeight() + 8;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        int max = this.maxCurrentLineIndex();
        if (this.lineProgress <= max) {
            this.textProgress += 4;
            List<NPCDialogueGui.ConversationLine> currentLines = this.getCurrentLines();
            ConversationLine txt = currentLines.get(this.lineProgress);
            if (this.textProgress > txt.width()) {
                this.lineProgress++;
                if (this.lineProgress > currentLines.size() - 1)
                    this.showAllButtons();
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
        if (this.isWriting()) {
            this.lineProgress = this.maxCurrentLineIndex() + 1;
            this.calculateButtonVisibility = true;
        } else if (this.pageIndex + 1 <= (this.getCurrentLines().size() - 1) / LINES_PER_PAGE) {
            this.advancePage();
        } else if (this.conversationIndex < this.conversation.size() - 1) {
            this.advanceConversation();
        } else if (this.buttons.isEmpty()) {
            this.removed = true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void showAllButtons() {
        this.buttons.forEach(b -> b.visible = true);
    }

    private int maxCurrentLineIndex() {
        return Math.min((LINES_PER_PAGE - 1) + (this.pageIndex * LINES_PER_PAGE), this.getCurrentLines().size() - 1);
    }

    private boolean buttonsVisible() {
        return this.lineProgress >= this.getCurrentLines().size() - 1;
    }

    private ConversationContainer getCurrentContainer() {
        return this.conversation.get(Mth.clamp(this.conversationIndex, 0, this.conversation.size() - 1));
    }

    private List<NPCDialogueGui.ConversationLine> getCurrentLines() {
        return this.getCurrentContainer().lines();
    }


    private boolean isWriting() {
        int max = this.maxCurrentLineIndex();
        List<ConversationLine> currentLines = this.getCurrentLines();
        return this.lineProgress < max || (this.lineProgress <= max && this.textProgress < currentLines.get(this.lineProgress).width());
    }

    private void advancePage() {
        this.pageIndex += 1;
        this.textProgress = 0;
        this.calculateButtonVisibility = true;
    }

    private void advanceConversation() {
        this.conversationIndex += 1;
        this.lineProgress = 0;
        this.textProgress = 0;
        this.pageIndex = 0;
        this.calculateButtonVisibility = true;
    }

    private void resetData() {
        this.conversationIndex = 0;
        this.lineProgress = 0;
        this.textProgress = 0;
        this.pageIndex = 0;
        this.reset = true;
        this.calculateButtonVisibility = true;
    }

    @Override
    public void removed() {
        super.removed();
        Minecraft.getInstance().options.keyUse.consumeClick();
        Minecraft.getInstance().options.keyUse.setDown(false);
        if (this.conversationIndex < this.conversation.size() - 1) {
            this.advanceConversation();
            Minecraft.getInstance().tell(() -> Minecraft.getInstance().setScreen(this));
        } else {
            LoaderNetwork.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), C2SNPCInteraction.Action.CLOSE_DIALOGUE));
        }
    }

    public void updateConversation(Minecraft mc, List<ConversationSet.NetworkConversation> conversations, Map<String, Component> data) {
        List<ConversationContainer> lines = new ArrayList<>();
        Int2ObjectMap<ConversationSet.NetworkActions> actions = new Int2ObjectOpenHashMap<>();
        for (ConversationSet.NetworkConversation conversation : conversations) {
            for (ConversationSet.NetworkDialog dialogue : conversation.dialogues()) {
                List<ConversationLine> pageLines = new ArrayList<>();
                Component text = PlaceHolderComponent.parseDialogueComponent(dialogue.text(), data, conversation.participants());
                for (FormattedText txt : mc.font.getSplitter().splitLines(text, LINE_WIDTH, text.getStyle())) {
                    // Ignore empty lines at page start
                    if (pageLines.size() % LINES_PER_PAGE == 0 && txt.getString().trim().isEmpty()) {
                        continue;
                    }
                    pageLines.add(new ConversationLine(mc.font.width(txt), txt, Language.getInstance().getVisualOrder(txt)));
                }
                Component speaker = dialogue.speaker().map(conversation.participants()::get).orElse(data.get(PlaceHolderComponent.NPC));
                if (speaker == null) {
                    speaker = Component.translatable("runecraftory.npc.conversation.participant.missing");
                } else {
                    speaker = speaker.copy().withStyle(ChatFormatting.WHITE);
                }
                lines.add(new ConversationContainer(speaker, conversation.participants(), pageLines));
            }
            int idx = lines.size() - 1;
            conversation.action().ifPresent(act -> actions.put(idx, act));
        }
        this.conversation = lines;
        this.actions = actions;
        this.replacements = data;
        this.resetData();
    }

    record ConversationLine(int width, FormattedText raw, FormattedCharSequence txt) {
    }

    record ConversationContainer(Component speaker, Map<String, Component> participants, List<ConversationLine> lines) {
    }
}