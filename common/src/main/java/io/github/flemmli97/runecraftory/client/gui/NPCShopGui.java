package io.github.flemmli97.runecraftory.client.gui;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.gui.widgets.SpeechBubble;
import io.github.flemmli97.runecraftory.client.gui.widgets.SpriteResources;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerShop;
import io.github.flemmli97.runecraftory.common.network.C2SNPCInteraction;
import io.github.flemmli97.runecraftory.common.network.C2SShopButton;
import io.github.flemmli97.runecraftory.mixinhelper.GuiGraphicsExtension;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.client.gui.widget.TexturedButton;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

public class NPCShopGui extends AbstractContainerScreen<ContainerShop> {

    protected static final ResourceLocation BACKGROUND = RuneCraftory.modRes("textures/gui/container/shop.png");

    private final Inventory inventory;

    private Slot lastClickSlotShop;
    private boolean isLeftClickDown, isRightClickDown;
    private int clickDelay, rightDelay = 50, leftDelay = 50;

    private AbstractWidget next, prev;
    private SpeechBubble speech;

    public NPCShopGui(ContainerShop abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.inventory = inventory;
    }

    @Override
    protected void init() {
        this.imageWidth = 245;
        this.imageHeight = 217;
        super.init();

        this.addRenderableWidget(this.prev = new TexturedButton(this.leftPos + 14, this.topPos + 8, 12, 12,
                Component.literal("<"), b -> LoaderNetwork.INSTANCE.sendToServer(new C2SShopButton(false)))
                .withSprite(SpriteResources.PAGE_BUTTON));
        this.addRenderableWidget(this.next = new TexturedButton(this.leftPos + 121, this.topPos + 8, 12, 12,
                Component.literal(">"), b -> LoaderNetwork.INSTANCE.sendToServer(new C2SShopButton(true)))
                .withSprite(SpriteResources.PAGE_BUTTON));
        this.updateButtons();
        this.addRenderableOnly(this.speech = new SpeechBubble(this.minecraft, this.leftPos + 148, this.topPos + 10, 100, 40));
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        GuiGraphicsExtension.drawRightAlignedString(graphics, this.font,
                Component.literal("" + Platform.INSTANCE.getPlayerData(this.minecraft.player).getMoney()),
                this.leftPos + 237, this.topPos + 197, 0, false);
        if (this.menu.getCurrentCost() > 0) {
            GuiGraphicsExtension.drawRightAlignedString(graphics, this.font, Component.literal("" + this.menu.getCurrentCost()),
                    this.leftPos + 237, this.topPos + 175, 0, false);
        }
        graphics.enableScissor(this.leftPos + 148, this.topPos, this.leftPos + this.width - 148, this.topPos + 126);
        RenderUtils.renderScaledEntityGui(graphics, this.leftPos + 156, this.topPos + 55, 80, 100,
                50, 0, mouseX, mouseY, this.menu.getShopOwner());
        graphics.disableScissor();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
        if (this.lastClickSlotShop != this.hoveredSlot) {
            this.lastClickSlotShop = null;
            this.isLeftClickDown = false;
            this.isRightClickDown = false;
        }
        if (this.lastClickSlotShop != null) {
            --this.clickDelay;
            if (this.isLeftClickDown) {
                this.rightDelay = 50;
                if (--this.leftDelay <= 0 && this.clickDelay <= 0) {
                    ClickType click = hasShiftDown() ? ClickType.QUICK_MOVE : ClickType.PICKUP;
                    this.slotClicked(this.lastClickSlotShop, this.lastClickSlotShop.index, 0, click);
                    this.clickDelay = 4;
                }
            } else if (this.isRightClickDown) {
                this.leftDelay = 50;
                if (--this.rightDelay <= 0 && this.clickDelay <= 0) {
                    ClickType click = hasShiftDown() ? ClickType.QUICK_MOVE : ClickType.PICKUP;
                    this.slotClicked(this.lastClickSlotShop, this.lastClickSlotShop.index, 1, click);
                    this.clickDelay = 4;
                }
            } else {
                this.leftDelay = 50;
                this.rightDelay = 50;
            }
        }
    }

    @Override
    protected void slotClicked(Slot slot, int slotId, int mouseButton, ClickType type) {
        super.slotClicked(slot, slotId, mouseButton, type);
        if (slot != null && slot.container != this.inventory) {
            this.lastClickSlotShop = slot;
        } else
            this.lastClickSlotShop = null;
        this.isLeftClickDown = mouseButton == 0;
        this.isRightClickDown = mouseButton == 1;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.isLeftClickDown = false;
        this.isRightClickDown = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        GuiGraphicsExtension.drawCenteredString(graphics, this.font, this.title, 74, 10, 0, false);
    }

    @Override
    public void removed() {
        super.removed();
        LoaderNetwork.INSTANCE.sendToServer(new C2SNPCInteraction(this.menu.getShopOwner().getId(), C2SNPCInteraction.Action.CLOSE));
    }

    public void drawBubble(Component txt) {
        this.speech.showBubble(txt, 200);
    }

    public void updateButtons() {
        if (this.next != null)
            this.next.visible = this.menu.hasNext();
        if (this.prev != null)
            this.prev.visible = this.menu.hasPrev();
    }
}
