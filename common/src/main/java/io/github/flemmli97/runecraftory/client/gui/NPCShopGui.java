package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.gui.widgets.PageButton;
import io.github.flemmli97.runecraftory.client.gui.widgets.SpeechBubble;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerShop;
import io.github.flemmli97.runecraftory.common.network.C2SNPCInteraction;
import io.github.flemmli97.runecraftory.common.network.C2SShopButton;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

public class NPCShopGui extends AbstractContainerScreen<ContainerShop> {

    protected static final ResourceLocation texturepath = new ResourceLocation(RuneCraftory.MODID, "textures/gui/shop.png");

    private final Inventory inventory;

    private Slot lastClickSlotShop;
    private boolean isLeftClickDown, isRightClickDown;
    private int clickDelay, rightDelay = 50, leftDelay = 50;

    private PageButton next, prev;
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

        this.addRenderableWidget(this.next = new PageButton(this.leftPos + 123, this.topPos + 8, new TextComponent(">"), b -> Platform.INSTANCE.sendToServer(new C2SShopButton(true))));
        this.addRenderableWidget(this.prev = new PageButton(this.leftPos + 12, this.topPos + 8, new TextComponent("<"), b -> Platform.INSTANCE.sendToServer(new C2SShopButton(false))));
        this.updateButtons();
        this.addRenderableOnly(this.speech = new SpeechBubble(this.minecraft, this.leftPos + 148, this.topPos + 10, 98, 30));
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
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
    protected void renderBg(PoseStack stack, float partialTick, int mouseX, int mouseY) {
        InventoryScreen.renderEntityInInventory(this.leftPos + 200, this.topPos + 150, 50, this.leftPos + 200 - mouseX, this.topPos + 65 - mouseY, this.menu.getShopOwner());
        RenderSystem.setShaderTexture(0, texturepath);
        this.blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        Platform.INSTANCE.getPlayerData(this.minecraft.player).ifPresent(data -> ClientHandlers.drawRightAlignedScaledString(stack, this.font, new TextComponent("" + data.getMoney()), this.leftPos + 237, this.topPos + 197, 1, 0));
        if (this.menu.getCurrentCost() > 0) {
            ClientHandlers.drawRightAlignedScaledString(stack, this.font, new TextComponent("" + this.menu.getCurrentCost()), this.leftPos + 237, this.topPos + 175, 1, 0);
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
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        ClientHandlers.drawCenteredScaledString(poseStack, this.font, this.title, 74, 10, 1, 0);
    }

    @Override
    public void removed() {
        super.removed();
        Platform.INSTANCE.sendToServer(new C2SNPCInteraction(this.menu.getShopOwner().getId(), C2SNPCInteraction.Type.CLOSE));
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
