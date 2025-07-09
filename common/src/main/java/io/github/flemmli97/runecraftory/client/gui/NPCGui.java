package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.gui.widgets.SpriteResources;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.entities.npc.job.NPCJob;
import io.github.flemmli97.runecraftory.common.entities.npc.job.ShopState;
import io.github.flemmli97.runecraftory.common.network.C2SNPCInteraction;
import io.github.flemmli97.runecraftory.common.network.C2SProcreationRequest;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import io.github.flemmli97.runecraftory.common.world.family.SyncedFamilyData;
import io.github.flemmli97.runecraftory.mixinhelper.GuiGraphicsExtension;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class NPCGui<T extends EntityNPCBase> extends Screen {

    private static final ResourceLocation TEXTURE = RuneCraftory.modRes("hud/generic_view");

    private final int offSetX = 140;
    private final int offSetY = 50;
    protected int leftPos;
    protected int topPos;
    private int lines;

    protected final T entity;
    private final ShopState isShopOpen;
    private final boolean canFollow;

    private List<FormattedCharSequence> components;

    private final Map<String, List<Component>> actions;

    private final ResourceLocation quest;
    private final SyncedFamilyData family;

    private final List<ToolTipRenderer> tooltipComponents = new ArrayList<>();

    public NPCGui(T entity, ShopState isShopOpen, boolean canFollow, SyncedFamilyData family, Map<String, List<Component>> actions, ResourceLocation quest) {
        super(entity.getDisplayName());
        this.entity = entity;
        this.isShopOpen = isShopOpen;
        this.canFollow = canFollow;
        this.actions = actions;
        this.quest = quest;
        this.family = family;
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = this.width - this.offSetX;
        this.topPos = this.offSetY;
        this.buttons();
    }

    @Override
    protected void renderBlurredBackground(float partialTick) {
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        int posX = 25;
        int posY = 25;
        int texY = this.lines * 13 + 10;
        boolean renderParents = this.family.father().isPresent() || this.family.mother().isPresent();
        graphics.blitSprite(TEXTURE, posX, posY, 150, texY);
        int txtOffX = posX + 5;
        int txtOffY = posY + 5;

        GuiGraphicsExtension.drawCenteredString(graphics, this.font, this.entity.getName(), posX + 75, txtOffY, 0, false);
        int y = 1;
        graphics.blitSprite(SpriteResources.HEART_ICON, posX + 65, txtOffY + 13 * y, 8, 8);
        graphics.drawString(this.font, "" + this.entity.friendPoints(this.minecraft.player), posX + 65 + 10, txtOffY + 13 * y, 0, false);
        y += 1;
        if (renderParents) {
            if (this.family.father().isEmpty()) {
                graphics.drawString(this.font, Component.translatable("runecraftory.gui.npc.parent"), txtOffX, txtOffY + 13 * y, 0, false);
                y += 1;
                for (FormattedCharSequence ch : this.font.split(this.family.mother().get(), 150 - 10)) {
                    graphics.drawString(this.font, ch, txtOffX, txtOffY + 13 * y, 0, false);
                    y += 1;
                }
            } else if (this.family.mother().isEmpty()) {
                graphics.drawString(this.font, Component.translatable("runecraftory.gui.npc.parent"), txtOffX, txtOffY + 13 * y, 0);
                y += 1;
                for (FormattedCharSequence ch : this.font.split(this.family.father().get(), 150 - 10)) {
                    graphics.drawString(this.font, ch, txtOffX, txtOffY + 13 * y, 0, false);
                    y += 1;
                }
            } else {
                graphics.drawString(this.font, Component.translatable("runecraftory.gui.npc.parents"), txtOffX, txtOffY + 13 * y, 0);
                y += 1;
                for (FormattedCharSequence ch : this.font.split(this.family.father().get(), 150 - 10)) {
                    graphics.drawString(this.font, ch, txtOffX, txtOffY + 13 * y, 0, false);
                    y += 1;
                }
                for (FormattedCharSequence ch : this.font.split(this.family.mother().get(), 150 - 10)) {
                    graphics.drawString(this.font, ch, txtOffX, txtOffY + 13 * y, 0, false);
                    y += 1;
                }
            }
        }
        if (!this.entity.isBaby()) {
            switch (this.family.relationship()) {
                case NONE -> {
                }
                case DATING -> {
                    RenderSystem.setShaderTexture(0, TEXTURE);
                    graphics.blitSprite(SpriteResources.HEARTH_LETTER_ICON, txtOffX, txtOffY + 13 * y, 10, 8);
                    graphics.drawString(this.font, this.family.partner().get(), txtOffX + 12, txtOffY + 13 * y, 0, false);
                    y += 1;
                }
                case MARRIED -> {
                    RenderSystem.setShaderTexture(0, TEXTURE);
                    graphics.blitSprite(SpriteResources.ENGAGEMENT_RING_ICON, txtOffX, txtOffY + 13 * y, 10, 8);
                    graphics.drawString(this.font, this.family.partner().get(), txtOffX + 12, txtOffY + 13 * y, 0, false);
                    y += 1;
                }
            }
            y += 1;
        }
        int shopY = txtOffY + 13 * y;
        int shopSizeY = -5;
        if (!this.entity.isBaby()) {
            MutableComponent shopComp = null;
            if (this.entity.getShop() == ModNPCJobs.GENERAL.get())
                shopComp = Component.translatable("runecraftory.gui.npc.shop.owner", Component.translatable(this.entity.getShop().getTranslationKey()));
            else if (this.entity.getShop().hasWorkSchedule)
                shopComp = Component.translatable(this.entity.getShop().getTranslationKey());
            if (shopComp != null) {
                if (this.isShopOpen == ShopState.NOBED || this.isShopOpen == ShopState.NOWORKPLACE)
                    shopComp.withStyle(ChatFormatting.DARK_RED);
                for (FormattedCharSequence comp : this.font.split(shopComp, 140)) {
                    float xCenter = posX + 75 - this.minecraft.font.width(comp) * 0.5f;
                    graphics.drawString(this.font, comp, (int) xCenter, txtOffY + 13 * y, 0, false);
                    y++;
                    shopSizeY += 13;
                }
            }

            if (this.entity.getShop().hasWorkSchedule) {
                for (Component comp : this.entity.getNPCSchedule().viewSchedule()) {
                    for (FormattedCharSequence formatted : this.font.split(comp, 140)) {
                        graphics.drawString(this.font, formatted, txtOffX, txtOffY + 13 * y, 0, false);
                        y++;
                    }
                }
            }
        }
        if (this.components != null && this.isHovering(txtOffX, shopY, 145, shopSizeY, mouseX, mouseY)) {
            graphics.renderTooltip(this.font, this.components, mouseX, mouseY);
        }
        this.tooltipComponents.forEach(r -> r.render(graphics, mouseX, mouseY));
        this.lines = y;
    }

    protected void buttons() {
        int x = -52;
        int xSize = 150;
        int y = 0;
        this.addRenderableWidget(Button.builder(Component.translatable(C2SNPCInteraction.Action.TALK.translation), b -> {
            LoaderNetwork.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), C2SNPCInteraction.Action.TALK));
            this.minecraft.setScreen(null);
        }).bounds(this.leftPos + x, this.topPos + y, xSize, 20).build());
        if (this.canFollow) {
            y += 30;
            this.addRenderableWidget(Button.builder(Component.translatable(C2SNPCInteraction.Action.FOLLOW.translation), b -> {
                LoaderNetwork.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), C2SNPCInteraction.Action.FOLLOW));
                this.minecraft.setScreen(null);
            }).bounds(this.leftPos + x, this.topPos + y, xSize, 20).build());
        }
        if (!this.entity.isBaby() && this.isShopOpen == ShopState.OPEN) {
            if (this.entity.getShop().hasShop) {
                y += 30;
                this.addRenderableWidget(Button.builder(Component.translatable(C2SNPCInteraction.Action.SHOP.translation), b -> {
                    LoaderNetwork.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), C2SNPCInteraction.Action.SHOP));
                    this.minecraft.setScreen(null);
                }).bounds(this.leftPos + x, this.topPos + y, xSize, 20).build());
            }
            for (Map.Entry<String, List<Component>> action : this.actions.entrySet()) {
                y += 30;
                this.addRenderableWidget(Button.builder(Component.translatable(action.getKey()), b -> {
                    LoaderNetwork.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), action.getKey()));
                    this.minecraft.setScreen(null);
                }).bounds(this.leftPos + x, this.topPos + y, xSize, 20).build());
                int tooltipX = this.leftPos + x;
                int tooltipY = this.topPos + y;
                this.tooltipComponents.add((graphics, mouseX, mouseY) -> {
                    if (this.isHovering(tooltipX, tooltipY, xSize, 20, mouseX, mouseY)) {
                        graphics.renderTooltip(this.font, action.getValue(), Optional.empty(), mouseX, mouseY);
                    }
                });
            }
        }
        if (this.quest != null) {
            y += 30;
            this.addRenderableWidget(Button.builder(Component.translatable(C2SNPCInteraction.Action.QUEST.translation), b -> {
                LoaderNetwork.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), C2SNPCInteraction.Action.QUEST, this.quest.toString()));
                this.minecraft.setScreen(null);
            }).bounds(this.leftPos + x, this.topPos + y, xSize, 20).build());
        }
        if (!this.entity.isBaby() && this.family.canProcreate()) {
            y += 30;
            this.addRenderableWidget(Button.builder(Component.translatable("runecraftory.gui.npc.procreate"), b -> {
                LoaderNetwork.INSTANCE.sendToServer(new C2SProcreationRequest(this.entity.getId()));
                this.minecraft.setScreen(null);
            }).bounds(this.leftPos + x, this.topPos + y, xSize, 20).build());
        }
        if (this.isShopOpen == ShopState.NOBED) {
            this.components = new ArrayList<>();
            this.components.addAll(this.font.split(Component.translatable("runecraftory.gui.npc.bed.no"), 150));
        }
        if (!this.entity.isBaby() && this.isShopOpen == ShopState.NOWORKPLACE && this.entity.getShop().hasPoi()) {
            this.components = new ArrayList<>();
            this.components.addAll(this.font.split(Component.translatable("runecraftory.gui.npc.workplace.no", this.formatShopPoi(this.entity.getShop())), 150));
        }
    }

    private Component formatShopPoi(NPCJob job) {
        Set<BlockState> set = job.matchingStates(this.minecraft.level.registryAccess());
        MutableComponent comp = Component.literal("");
        set.stream().map(BlockBehaviour.BlockStateBase::getBlock)
                .distinct()
                .map(Block::getName)
                .forEach(c -> {
                    if (comp.getSiblings().isEmpty())
                        comp.append(c);
                    else
                        comp.append(Component.literal(", ").append(c));
                });
        return comp.withStyle(ChatFormatting.AQUA);
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

    protected boolean isHovering(int x, int y, int width, int height, double mouseX, double mouseY) {
        return mouseX >= x - 1
                && mouseX < (x + width + 1)
                && mouseY >= y - 1
                && mouseY < y + height + 1;
    }

    @Override
    public void removed() {
        super.removed();
        LoaderNetwork.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), C2SNPCInteraction.Action.CLOSE));
    }

    interface ToolTipRenderer {

        void render(GuiGraphics graphics, int mouseX, int mouseY);
    }
}