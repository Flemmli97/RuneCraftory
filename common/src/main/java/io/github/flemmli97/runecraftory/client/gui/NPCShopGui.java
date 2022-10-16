package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerShop;
import io.github.flemmli97.runecraftory.common.network.C2SNPCInteraction;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

public class NPCShopGui extends AbstractContainerScreen<ContainerShop> {

    protected static final ResourceLocation texturepath = new ResourceLocation(RuneCraftory.MODID, "textures/gui/shop.png");

    private Inventory inventory;

    private Slot lastClickSlotShop;
    private boolean isLeftClickDown, isRightClickDown;
    private int clickDelay, rightDelay = 50, leftDelay = 50;

    public NPCShopGui(ContainerShop abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.inventory = inventory;
    }

    @Override
    protected void init() {
        this.imageWidth = 245;
        this.imageHeight = 217;
        super.init();
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
        RenderSystem.setShaderTexture(0, texturepath);
        this.blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        PlayerData data = Platform.INSTANCE.getPlayerData(this.minecraft.player).orElse(null);
        if (data != null)
            this.drawRightAlignedScaledString(stack, new TextComponent("" + data.getMoney()), this.leftPos + 237, this.topPos + 197, 0);
        if (this.menu.getCurrentCost() > 0) {
            this.drawRightAlignedScaledString(stack, new TextComponent("" + this.menu.getCurrentCost()), this.leftPos + 237, this.topPos + 175, 0);
        }

        renderEntityInInventoryBehind(this.leftPos + 200, this.topPos + 150, 50, this.leftPos + 200 - mouseX, this.topPos + 150 - mouseY, this.menu.getShopOwner());
    }

    protected void drawCenteredScaledString(PoseStack stack, Component string, float x, float y, int color) {
        float xCenter = x - this.minecraft.font.width(string) * 0.5f;
        this.minecraft.font.draw(stack, string, xCenter, y, color);
    }

    protected void drawRightAlignedScaledString(PoseStack stack, Component string, float x, float y, int color) {
        float xCenter = x - this.minecraft.font.width(string);
        this.minecraft.font.draw(stack, string, xCenter, y, color);
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
        this.drawCenteredScaledString(poseStack, this.title, 74, 10, 0x404040);
    }

    public static void renderEntityInInventoryBehind(int posX, int posY, int scale, float mouseX, float mouseY, LivingEntity livingEntity) {
        float f = (float) Math.atan(mouseX / 40.0f);
        float g = (float) Math.atan(mouseY / 40.0f);
        PoseStack poseStack = RenderSystem.getModelViewStack();
        poseStack.pushPose();
        poseStack.translate(posX, posY, -50);
        poseStack.scale(1.0f, 1.0f, -1.0f);
        RenderSystem.applyModelViewMatrix();
        PoseStack poseStack2 = new PoseStack();
        poseStack2.translate(0.0, 0.0, 1000.0);
        poseStack2.scale(scale, scale, scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0f);
        Quaternion quaternion2 = Vector3f.XP.rotationDegrees(g * 20.0f);
        quaternion.mul(quaternion2);
        poseStack2.mulPose(quaternion);
        float h = livingEntity.yBodyRot;
        float i = livingEntity.getYRot();
        float j = livingEntity.getXRot();
        float k = livingEntity.yHeadRotO;
        float l = livingEntity.yHeadRot;
        livingEntity.yBodyRot = 180.0f + f * 20.0f;
        livingEntity.setYRot(180.0f + f * 40.0f);
        livingEntity.setXRot(-g * 20.0f);
        livingEntity.yHeadRot = livingEntity.getYRot();
        livingEntity.yHeadRotO = livingEntity.getYRot();
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion2.conj();
        entityRenderDispatcher.overrideCameraOrientation(quaternion2);
        entityRenderDispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> entityRenderDispatcher.render(livingEntity, 0.0, 0.0, 0.0, 0.0f, 1.0f, poseStack2, bufferSource, 0xF000F0));
        bufferSource.endBatch();
        entityRenderDispatcher.setRenderShadow(true);
        livingEntity.yBodyRot = h;
        livingEntity.setYRot(i);
        livingEntity.setXRot(j);
        livingEntity.yHeadRotO = k;
        livingEntity.yHeadRot = l;
        poseStack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }

    @Override
    public void onClose() {
        super.onClose();
        Platform.INSTANCE.sendToServer(new C2SNPCInteraction(this.menu.getShopOwner().getId(), C2SNPCInteraction.Type.CLOSE));
    }
}
